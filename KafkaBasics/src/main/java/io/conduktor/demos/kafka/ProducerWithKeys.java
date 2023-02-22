package io.conduktor.demos.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithKeys {
    private static final Logger log = LoggerFactory.getLogger(Producer.class.getSimpleName());

    public static void main(String[] args) {
        log.info("Producer");

//        Create Producer Properties
        Properties properties = new Properties();

//        Connect to Conduktor Playground
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"6ccDjxKMjKXL9dxK9QA7az\" password=\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiI2Y2NEanhLTWpLWEw5ZHhLOVFBN2F6Iiwib3JnYW5pemF0aW9uSWQiOjcwMjgzLCJ1c2VySWQiOjgxMjkzLCJmb3JFeHBpcmF0aW9uQ2hlY2siOiJjYWI3MTQxMy03MzE1LTQzOGItODI5MS05YzA2OWE4NWFjMjIifX0.gpg_6rBd-QGcm3fmzRz1H29e9D6RsTdEO21kOQ4ojII\";\n");
        properties.setProperty("sasl.mechanism", "PLAIN");
        properties.setProperty("bootstrap.servers", "cluster.playground.cdkt.io:9092");

//        Set Producer Properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());


//        Create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 10; i++) {
                String topic = "demo_topic";
                String key = "key " + i;
                String value = "HelloWorld! " + i;
                //        Create Producer Record to send data
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value );

                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception == null) {
                            log.info("Key: " + key + " | Partition: " + metadata.partition());
                        } else {
                            log.error("Error while producing: ", exception);
                        }
                    }
                });
            }

            try {
                Thread.sleep(2000);
                log.info("Slept for 2 sec");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


//        tell the producer to send all the data and block until done --synchronous
        producer.flush();

//        flush and close the producer
        producer.close();
    }
}
