package org.doodus.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.doodus.context.IModelContext;
import org.doodus.context.ModelContext1;

public class Utils {
    public static ObjectMapper mapper  = new ObjectMapper();
    public static IModelContext getModelContext(String modelName){
        switch (modelName){
            case "model1":
                return new ModelContext1();
            default:
                return null;
        }

    }
    public static String getJsonString(IModelContext modelContext) throws JsonProcessingException {
        return mapper.writeValueAsString(modelContext);
    }
}
