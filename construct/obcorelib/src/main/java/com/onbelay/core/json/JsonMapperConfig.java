package com.onbelay.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
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
		mapper.setDateFormat(new StdDateFormat());
		//mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		mapper.registerModule(new JSR310Module());

        return mapper;
	}

}
