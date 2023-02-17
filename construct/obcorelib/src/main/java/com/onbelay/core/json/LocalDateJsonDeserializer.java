package com.onbelay.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Registered in the JsonMapperConfig to override json serialization
 * @author canmxf
 *
 */
public class LocalDateJsonDeserializer extends JsonDeserializer<LocalDate> {
    public LocalDateJsonDeserializer() {
    }

    public LocalDate deserialize(JsonParser parser, DeserializationContext deserCtx) throws IOException, JsonProcessingException {
        ObjectCodec oc = parser.getCodec();
        JsonNode node = oc.readTree(parser);
        return convertToDate(node.asText());
    }

    public static LocalDate convertToDate(String inputStr) {
        if(inputStr == null)
            return null;
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(inputStr, DateTimeFormatter.ofPattern("yyyy-MM-ddX"));
        }catch (DateTimeParseException dtpe) {
            localDate = LocalDate.parse(inputStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        return localDate;
    }
}
