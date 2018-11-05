package org.doodus.kafka;

import lombok.extern.slf4j.Slf4j;
import org.doodus.config.KafkaProducerConfig;
import lombok.Builder;
import lombok.Data;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Builder
@Data
@Slf4j
public class KafkaWriter {
    private String topic;
    private KafkaProducer kafkaProducer;
    private KafkaProducerConfig kafkaProducerConfig;

    public void initializeKafka() throws Exception {
        Properties props = new Properties();
        try {
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "".join(",",kafkaProducerConfig.getKafkaBrokers()));
            props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerConfig.getClientId());
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//            props.put(ProducerConfig.ACKS_CONFIG,kafkaProducerConfig.getAckConfig());
            props.put(ProducerConfig.RETRIES_CONFIG,kafkaProducerConfig.getRetryConfig());
            props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,kafkaProducerConfig.getRetryBackoffMsConfig());
            kafkaProducer = new KafkaProducer<>(props);
            topic = kafkaProducerConfig.getTopic();
        }
        catch (Exception e){
            log.error("Unable to Initialize Kafka",e.getMessage());
            log.error("For the config ", kafkaProducerConfig.toString());
            throw new Exception("Unable to Initialize Kafka");
        }

    }
    public void write(String jsonDataString){
        log.info(topic);
        ProducerRecord record = new ProducerRecord<>(topic,jsonDataString);
        kafkaProducer.send(record);

    }
    public void stop(){
        kafkaProducer.flush();
        kafkaProducer.close();
    }
}
