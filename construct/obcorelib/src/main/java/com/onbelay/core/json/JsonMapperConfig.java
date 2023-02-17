package com.onbelay.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

/**
 * Registers serializers, deserializers and a filter to used in the Jackson JSON marshalling by the REST Controllers.
 * This config will override the existing objectMapper bean.
 * @author canmxf
 *
 */
@Configuration
public class JsonMapperConfig {
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper  = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		//mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		//mapper.setDateFormat(new StdDateFormat());
		//mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		//mapper.enableDefaultTyping();
        SimpleModule module = new SimpleModule("dagmodule", new Version(1, 1, 0, "snap", "onbelay", "dagnabit"));


        module.addSerializer(LocalDate.class, new LocalDateJsonSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateJsonDeserializer());


        mapper.registerModule(module);

        return mapper;
	}

}
