package org.doodus.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
public class KafkaStreamConfig {
    @Getter
    @Setter
    @NonNull
    @JsonProperty
    private String applicationId;

    @Getter
    @Setter
    @NonNull
    private String[] brokerList;

    @Getter
    @Setter
    @NonNull
    private String readTopic;

    @Getter
    @Setter
    @NonNull
    private String writeTopic;
}
