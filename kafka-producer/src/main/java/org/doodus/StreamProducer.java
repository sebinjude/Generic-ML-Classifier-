package org.doodus;


import org.doodus.config.ProducerConfig;
import org.doodus.kafka.KafkaWriter;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.doodus.parser.CsvParser;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.doodus.utils.Utils.getModelContext;

@Slf4j
public class StreamProducer {

    public static void main(String[] args) {
        if (args.length != 2) {
            log.error("Fatal error input files missing");
            log.error("Use the command : java -jar kafka-producer-1.0-SNAPSHOT.jar input_model1.csv dev-config.yml");
        }
        else {

            ProducerConfig producerConfig;
            String inputFileName = args[0];
            String modelName = fetchModelName(inputFileName);
            log.info("Input File Name : " + inputFileName);
            log.info("Infered Model Name : " + modelName);
            try(InputStream configStream = Files.newInputStream(Paths.get(args[1]))){
                Yaml yaml = new Yaml();
                producerConfig = yaml.loadAs(configStream,ProducerConfig.class);
                KafkaWriter kafkaWriter = KafkaWriter.builder()
                        .kafkaProducerConfig(producerConfig.getKafkaProducerConfig())
                        .build();
                kafkaWriter.initializeKafka();
                CsvParser csvParser = CsvParser.builder().modelContext(getModelContext(modelName))
                        .kafkaWriter(kafkaWriter)
                        .build();
                InputStreamReader csvFileReader = new InputStreamReader(new FileInputStream(inputFileName));
                csvParser.Parse(csvFileReader);
            } catch (IOException e) {
                log.error(e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
