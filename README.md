# Generic-ML-Classifier-Pipeline
An ML pipeline that converts a given csv file into a Kafka stream. This stream is then consumed by a Generic Kafka-PMML pipeline to produce the result. This result form a new stream that can be further consumed down the line.

## CORE PRINCIPLE
**Any ML pipeline should be as generic as possible to accommodate as many ML Models as possible without code change.**


## Assumptions
* I assume that input (test-input) as csv is just a starting point for this dummy assignment.
* In an actual production environment a streaming classifier would consume from some realtime source. Owing to the afore said assumptions I have not invested much time in making the kafka-producer generic.
* However the Producer is modular and extensible.
* Any null value in the test as well as train csv has been regarded as 0.
* The headers of the input csv should be the same as the feature names used while training the model.
* This ML Pipeline only supports ML models generated as _**PMML**_ _(Predictive Model Markup Language )_ files.

## Approach
The project is split into 2 sub modules, one kafka-producer and stream-classifier.
The kafka-converts a csv (as of now) to make a stream of structured (named) record of features.
The stream-classifier loads a PMML model and classifies the input data in **real-time** and writes it to another Kafka topic.


### kafka-producer
* It is a configurable Kafka producer that accepts input as a csv and config as a yml file from command line.
* The input csv file name should confer to the standard \<some-string\>_\<model-name\>.csv.
* The producer only accepts inputs for the model schemas that are pre-defined.
* It is modular enough to add new input-model schemas and there by extend the types of models it can support.
* Finally the Producer converts each record to a standard json format and publishes it to the configured write topic.

### stream-consumer
* Accepts a configuration yml file as command line argument. This specifies topic to read from and topic to write along with other relevant kafka props.(sample yml given in the project).
* Loads the .pmml file mentioned in the config.
* Unmarshals the pmml file.
* Sets up the pmml evaluator.
* Sets up the kafka-stream to read from specified read topic and write to the write topic.
* Making use of the streaming API of Kafka, each record in the input stream is mapped to the evaluator.
* The output of the evaluator is combined with the input(for reference purposes) as is written to the configured write topic.


## Trade-Offs
**Time Ordering Of Predictions** Though the Pipeline portrayed in this project is easily distributed by proportionally increasing the number of partitions in the Kafka topic and rolling out more instances,
The data written to the output topic is not time-ordered.
### How to ensure time ordering:
1. Run the cluster in a single partition mode (Not at all advisable as it kill the parallelism)
2. Add subsequent sorting layers to the pipeline. However this approach may hamper the real-time nature of the data.
Hence such a decision depends on the exact production requirements.

## Bottle Neck (*Excluding producer side bottle-necks*) 
1. Though not exactly a bottle-neck, the current architecture dedicates one model per topic. Owing to the fact that PMML model loading is pretty fast (on an average), we could easily provide multi-model support.
** This multi-model support can again be made much more performant by bringing in caching mechanism for the models. **


## Future Imporvements In Order
1. Add more TestCases.
2. Support OpenTSDB metrics (did not add due to time restrictions)
3. Extend the model Loading mechanism (Bring in s3 or REST or any other method suitable to the production environment to replace simple file loading mechanism.)   
4. Add more pre-processing layers. *(Again depends on production requirements. Hence at this stage I am not develing into data pre-processing layers as it would simply be over-engineering.)*
