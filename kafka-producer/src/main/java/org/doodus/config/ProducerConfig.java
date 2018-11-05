package org.doodus.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class ProducerConfig {

    @NonNull
    @Getter
    @Setter
    private KafkaProducerConfig kafkaProducerConfig;
}
