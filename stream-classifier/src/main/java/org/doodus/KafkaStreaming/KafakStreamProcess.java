package org.doodus.KafkaStreaming;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.doodus.config.KafkaStreamConfig;
import org.doodus.modelloader.ModelLoader;
import java.util.Properties;

@Data
@Builder
@Slf4j
public class KafakStreamProcess {

    @NonNull
    private KafkaStreamConfig kafkaStreamConfig;

    private KafkaStreams stream;

    private ModelLoader modelLoader;

    public void initStream(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaStreamConfig.getApplicationId());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"".join(",",kafkaStreamConfig.getBrokerList()));
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        final StreamsBuilder builder = new StreamsBuilder();
        KStream inStream = builder.stream(kafkaStreamConfig.getReadTopic(), Consumed.with(Serdes.String(), Serdes.String()));
        inStream.mapValues(x -> {
            try {
                log.info(x.toString());
                return modelLoader.evaluateModel(x.toString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).to(kafkaStreamConfig.getWriteTopic());
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }
}
