package com.onbelay.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Registered in the JsonMapperConfig to override json serialization
 * @author canmxf
 *
 */
public class LocalDateJsonSerializer extends JsonSerializer<LocalDate> {
    public LocalDateJsonSerializer() {
    }

    public void serialize(LocalDate rawValue, JsonGenerator jsonGen, SerializerProvider serProv) throws IOException, JsonProcessingException {
        String dateStr = rawValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'Z'"));
        jsonGen.writeString(dateStr);
    }
}
