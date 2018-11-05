package org.doodus;

import org.doodus.KafkaStreaming.KafakStreamProcess;
import org.doodus.config.StreamClassifierConfig;
import lombok.extern.slf4j.Slf4j;
import org.doodus.modelloader.ModelLoader;
import org.doodus.modelloader.ResponseModel;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class StreamClassifier {
    public static void main(String[] args) {
        ModelLoader modelLoader;
        StreamClassifierConfig streamClassifierConfig;
        KafakStreamProcess kafakStreamProcess;
        Yaml yaml = new Yaml();
        try(InputStream configStream = Files.newInputStream(Paths.get(args[0]))) {
            streamClassifierConfig = yaml.loadAs(configStream, StreamClassifierConfig.class);

            modelLoader = ModelLoader.builder()
                    .responseModel(ResponseModel.builder().build())
                    .modelPath(streamClassifierConfig.getModelLoaderConfig().getModelPath())
                    .build();
            modelLoader.loadModel();
            modelLoader.initializeEvaluator();
            kafakStreamProcess = KafakStreamProcess.builder()
                    .kafkaStreamConfig(streamClassifierConfig.getKafkaStreamConfig())
                    .modelLoader(modelLoader)
                    .build();
            kafakStreamProcess.initStream();
        } catch (FileNotFoundException e){
            log.error("Unable to load PMMLModel");
        } catch (IOException e) {
            log.error("Unable to Load org.doodus.config File");
        } catch (JAXBException e) {
            log.error("Exiting");
        } catch (SAXException e) {
            log.error("Exiting");
        }
    }
}
