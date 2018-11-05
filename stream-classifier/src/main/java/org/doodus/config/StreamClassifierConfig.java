package org.doodus.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
public class StreamClassifierConfig {
    @NonNull
    @Getter
    @Setter
    KafkaStreamConfig kafkaStreamConfig;


    @NonNull
    @Getter
    @Setter
    ModelLoaderConfig modelLoaderConfig;

}
