package org.doodus.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;


@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ModelContext1 implements IModelContext {
    @Getter
    @Setter
    private SupportedModels modelName = SupportedModels.MODEL1.getModelName();
    @JsonProperty("FeaA")
    @Setter
    private double FeaA;

    @JsonProperty("FeaB")
    @Setter
    private double FeaB;

    @JsonProperty("FeaC")
    @Setter
    private double FeaC;

    @JsonProperty("FeaD")
    @Setter
    private double FeaD;

    @JsonProperty("FeaE")
    @Setter
    private double FeaE;

    @JsonProperty("Timestamp")
    @Setter
    private String Timestamp;

    @JsonIgnore
    public CellProcessor[] getProcessors(){
        final CellProcessor[] processors = new CellProcessor[] {

                new Optional(), //Timestamp
                new Optional(new ParseDouble()), //FeaA
                new Optional(new ParseDouble()), //FeaB
                new Optional(new ParseDouble()), //FeaC
                new Optional(new ParseDouble()), //FeaD
                new Optional(new ParseDouble()), //FeaE
        };
        return processors;
    }
}