package com.onbelay.core.codes.annotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.onbelay.core.codes.model.CodeManager;
import com.onbelay.core.codes.snapshot.CodeLabel;
import com.onbelay.core.entity.component.ApplicationContextFactory;
import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.exception.OBRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CodeLabelSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private static final Logger logger = LogManager.getLogger();
    private String codeFamily;
    private String propertyName;

    @Override
    public void serialize(
            String value,
            JsonGenerator jsonGenerator,
            SerializerProvider serializers) throws IOException, JsonProcessingException {

        jsonGenerator.writeString(value);
        if (codeFamily == null || codeFamily.isEmpty())
            throw new OBRuntimeException(CoreTransactionErrorCode.SYSTEM_FAILURE.getCode());

        if (propertyName == null || propertyName.isEmpty())
            throw new OBRuntimeException(CoreTransactionErrorCode.SYSTEM_FAILURE.getCode());

        CodeLabel codeLabel = getCodeManager().getCodeLabel(
                codeFamily,
                value);
        if (codeLabel == null) {
            logger.warn("Missing code for :" + value);
            codeLabel = new CodeLabel(value, value);
        }

        jsonGenerator.writeObjectField(propertyName, codeLabel);
    }

    @Override
    public JsonSerializer<?> createContextual(
            SerializerProvider prov,
            BeanProperty property) throws JsonMappingException {

        if (property == null)
            return this;

        InjectCodeLabel annotation = property.getAnnotation(InjectCodeLabel.class);
        if (annotation != null) {
            codeFamily = annotation.codeFamily();
            if (codeFamily != null)
                codeFamily = codeFamily.trim();
            propertyName = annotation.injectedPropertyName();
            if (propertyName != null)
                propertyName = propertyName.trim();
        }

        return this;
    }

    private CodeManager getCodeManager() {
        return (CodeManager) ApplicationContextFactory.getBean("codeManager");
    }
}
