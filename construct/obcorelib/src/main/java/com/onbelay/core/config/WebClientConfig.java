package com.onbelay.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.onbelay.core.exception.OBRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Profile("!test")
@Configuration
public class WebClientConfig {
    private static final Logger logger = LogManager.getLogger();



    public ObjectMapper clientObjectMapper() {
        ObjectMapper mapper  = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        //mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JSR310Module());

        return mapper;
    }

    @Bean
    @Primary
    ReactiveClientRegistrationRepository reactiveClientRegistrationRepository(
            @Value("${spring.security.oauth2.client.provider.okta.token-uri}") String tokenUri,
            @Value("${spring.security.oauth2.client.registration.okta.client-id}") String clientId,
            @Value("${spring.security.oauth2.client.registration.okta.client-secret}") String clientSecret,
            @Value("${core.authorities:uua.none}") String authorities
    ) {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("okta")
                .tokenUri(tokenUri)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope(authorities)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }


    @Bean
    public ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService(
            ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
        InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(reactiveClientRegistrationRepository);
        return clientService;
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository reactiveClientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService) {

        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        reactiveClientRegistrationRepository,
                        reactiveOAuth2AuthorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }


    @Bean
    public WebClient webClient(ClientRegistrationRepository clientRegistrations,
                               OAuth2AuthorizedClientRepository authorizedClients) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                        clientRegistrations,
                        authorizedClients);
        oauth2.setDefaultOAuth2AuthorizedClient(true);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(clientObjectMapper()));
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(clientObjectMapper()));
                    configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024);
                })

                .build();

        return WebClient.builder()
                .apply(oauth2.oauth2Configuration())
                .filter(new ServletBearerExchangeFilterFunction())
                .filter(logResponse())
                .filter(handleError())
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    /*
        The OAuth2AuthorizedClientRepository is responsible for managing the lifecycle of the access token loaded by the UI
        The ServletBearerExchangeFilterFunction will pass the JWT received by the resource server through to the other calls webclient makes
     */
    @Bean(name="serviceWebClient")
    public WebClient serviceWebClient(
            ReactiveOAuth2AuthorizedClientManager authorizedClientManager
            ) {

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                authorizedClientManager);

        oauth2.setDefaultClientRegistrationId("okta");

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(clientObjectMapper()));
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(clientObjectMapper()));
                    configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024);
                })

                .build();

        return WebClient.builder()
                .filter(oauth2)
                .filter(logResponse())
                .filter(handleError())
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            logStatus(response);
            logHeaders(response);

            return logBody(response);
        });
    }

    private ExchangeFilterFunction handleError() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (response.statusCode() != null && (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError())) {
                return response.bodyToMono(String.class)
                        .defaultIfEmpty(getHttpStatus(response.statusCode()).getReasonPhrase())
                        .flatMap(body -> {
                            logger.error("Error is {}", body);
                            return Mono.error(new OBRuntimeException(response.statusCode().toString()));
                        });
            } else {
                return Mono.just(response);
            }
        });
    }

    private static void logStatus(ClientResponse response) {
        HttpStatus status = getHttpStatus(response.statusCode());

        logger.debug("Returned staus code {} ({})", status.value(), status.getReasonPhrase());
    }

    private static HttpStatus getHttpStatus(HttpStatusCode statusCode) {
        HttpStatus status = HttpStatus.valueOf(statusCode.value());
        return status;
    }


    private Mono<ClientResponse> logBody(ClientResponse response) {
        if (response.statusCode() != null && (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError())) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> {
                        logger.debug("Body received is {}", body);
                        return Mono.just(response);
                    });
        } else {
            return Mono.just(response);
        }
    }


    private void logHeaders(ClientResponse response) {
        response.headers().asHttpHeaders().forEach((name, values) -> {
            values.forEach(value -> {
                logger.debug(name + " = " + value);
            });
        });
    }
}
