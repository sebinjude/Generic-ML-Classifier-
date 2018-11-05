package org.doodus.modelloader;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.jpmml.model.VisitorBattery;
import org.xml.sax.SAXException;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.doodus.utils.Utils.dynamicTypeCast;
import static org.doodus.utils.Utils.getJsonNodeFromString;
import static org.doodus.utils.Utils.getJsonString;

@Slf4j
@Data
@Builder
public class ModelLoader {
    private ResponseModel responseModel;
    private String modelPath;
    private PMML pmml;
    private Evaluator evaluator;
    private Map<FieldName,?> result;

    public void loadModel() throws FileNotFoundException, JAXBException, SAXException {
        try{
            InputStream modelStream = new FileInputStream(new File(modelPath));
            pmml = org.jpmml.model.PMMLUtil.unmarshal(modelStream);
            VisitorBattery visitorBattery = new VisitorBattery();
            // Getting rid of SAX Locator information
            visitorBattery.add(org.jpmml.model.visitors.LocatorNullifier.class);
            // Getting rid of duplicate PMML attribute values and PMML elements
            visitorBattery.addAll(new org.jpmml.model.visitors.AttributeInternerBattery());
            visitorBattery.addAll(new org.jpmml.evaluator.visitors.ElementInternerBattery());

            visitorBattery.applyTo(pmml);
        }
        catch (FileNotFoundException e){
            log.error("PMML File not found at " + modelPath + e.getMessage());
            throw e;
        } catch (JAXBException e) {
            log.error("Unmarshaling of model failed");
            e.printStackTrace();
            throw e;
        } catch (SAXException e) {
            log.error("Unmarshaling of model failed");
            e.printStackTrace();
            throw e;
        }
    }
    public void initializeEvaluator(){
        ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();
        ValueFactoryFactory valueFactoryFactory = ReportingValueFactoryFactory.newInstance();
        modelEvaluatorFactory.setValueFactoryFactory(valueFactoryFactory);
        evaluator = (Evaluator)modelEvaluatorFactory.newModelEvaluator(pmml);


    }
    public String evaluateModel(String rawInputData) throws IOException {
        JsonNode streamInput = getJsonNodeFromString(rawInputData);
        List<InputField> modelInputFields = evaluator.getInputFields();
        Map<FieldName,FieldValue> arguments = new LinkedHashMap<>();
        for(InputField inputField: modelInputFields) {
            FieldName inputFieldName = inputField.getName();
            FieldValue value = inputField.prepare(dynamicTypeCast(inputField.getDataType(),streamInput.get(inputFieldName.getValue())));
            arguments.put(inputFieldName, value);
        }
        result = evaluator.evaluate(arguments);
        List<TargetField> targetFields = evaluator.getTargetFields();
        responseModel.setRawInput(streamInput);
        for (TargetField targetField : targetFields)
        {
            FieldName targetFieldName = targetField.getName();
            Object targetFieldValue = result.get(targetFieldName);
            if(targetFieldValue instanceof  Computable){
                Computable computable = (Computable)targetFieldValue;
                Object predictionResult = computable.getResult().toString();
                log.debug("Computed Result : ",predictionResult.toString());
                responseModel.setPrediction(predictionResult);
            }
            else{
                responseModel.setPrediction(targetFieldValue);
            }
        }
        return getJsonString(responseModel);
    }
}
