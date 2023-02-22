package io.conduktor.demos.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithCallback {
    private static final Logger log = LoggerFactory.getLogger(Producer.class.getSimpleName());

    public static void main(String[] args) {
        log.info("Producer");

//        Create Producer Properties
        Properties properties = new Properties();

//        Connect to Conduktor Playground
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.mechanism", "PLAIN");
        properties.setProperty("bootstrap.servers", "cluster.playground.cdkt.io:9092");

//        Set Producer Properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

//        Not recommended on production, just for demo
        properties.setProperty("batch.size", "400");

//        Create the Producer
        KafkaProducer <String, String> producer = new KafkaProducer<>(properties);

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 50; i++) {
                //        Create Producer Record to send data
                ProducerRecord <String, String> producerRecord = new ProducerRecord<>("demo_topic", "HelloWorld! " + i);

                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if(exception == null) {
                            log.info(
                                    "\nReceived metadata" +
                                            "\nTopic: " + metadata.topic() +
                                            "\nPartition: " + metadata.partition() +
                                            "\nOffset: " + metadata.offset() +
                                            "\nTimestamp: " + metadata.timestamp()
                            );
                        } else {
                            log.error("Error while producing: ", exception);
                        }
                    }
                });
            }

//            sleep between
            try {
                Thread.sleep(500);
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
