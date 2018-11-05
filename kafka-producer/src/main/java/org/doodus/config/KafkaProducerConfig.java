package org.doodus.config;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@ToString
public class KafkaProducerConfig {
    @NonNull
    @Getter
    @Setter
    private String[] kafkaBrokers;

    @NonNull
    @Getter
    @Setter

    private String clientId;

    @NonNull
    @Getter
    @Setter
    private String topic;

    @NonNull
    @Getter
    @Setter
    private int ackConfig;

    @NonNull
    @Getter
    @Setter
    private String retryConfig;

    @NonNull
    @Getter
    @Setter
    private String retryBackoffMsConfig;
}
