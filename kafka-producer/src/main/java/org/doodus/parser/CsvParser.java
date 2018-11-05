package org.doodus.parser;

import lombok.extern.slf4j.Slf4j;
import org.doodus.context.IModelContext;
import org.doodus.kafka.KafkaWriter;
import lombok.Builder;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import java.io.InputStreamReader;
import static org.doodus.utils.Utils.getJsonString;

@Builder
@Slf4j
public class CsvParser implements IParse {
    IModelContext modelContext;
    KafkaWriter kafkaWriter;

    @Override
    public void Parse(InputStreamReader csvFile) {
        if (modelContext == null) {
            System.out.println("Model Not Supported");
        } else {
            final CellProcessor[] cellProcessors = modelContext.getProcessors();
            try {
                ICsvBeanReader modelContextBeanReader = new CsvBeanReader(csvFile, CsvPreference.STANDARD_PREFERENCE);
                final String[] header = modelContextBeanReader.getHeader(true);
                while (true) {
                    modelContext = modelContextBeanReader.read(modelContext.getClass(), header, cellProcessors);
                    if (modelContext == null) {
                        log.info("Completed CSV Parsing...");
                        break;
                    }
                    log.info(modelContext.toString());
                    try {
                        kafkaWriter.write(getJsonString(modelContext));
                    } catch (Exception e) {
                        log.error("Error while writing to kafka " + e.getMessage());
                    }


                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void Stop() {
        modelContext = null;
        kafkaWriter.stop();
    }
}
