package org.doodus.modelloader;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseModel {
    @JsonProperty
    private JsonNode rawInput;
    @JsonProperty
    private Object prediction;

}
