package io.appname.employeeservice.util;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class StringToBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        String value = jp.getText().replaceAll("[^0-9.]", "");
        return new BigDecimal(value.isEmpty() ? "0" : value);
    }
}