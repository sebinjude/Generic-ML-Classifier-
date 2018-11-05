package org.doodus.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dmg.pmml.DataType;

import java.io.IOException;

public class Utils {
    public static ObjectMapper mapper  = new ObjectMapper();

    public static JsonNode getJsonNodeFromString(String jsonString) throws IOException {
        return mapper.readTree(jsonString);
    }
    public static String getJsonString(Object modelContext) throws JsonProcessingException {
        return mapper.writeValueAsString(modelContext);
    }
    public static Object dynamicTypeCast(DataType type, Object value) {
        if (type == null || value == null) {
            return value;
        }
        switch (type) {
            case FLOAT:
                return Double.parseDouble(String.valueOf(value));
            case STRING:
                return String.valueOf(value);
            default:
                return value;
        }
    }
}
