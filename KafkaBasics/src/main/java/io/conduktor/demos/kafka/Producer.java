package io.conduktor.demos.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {
    private static final Logger log = LoggerFactory.getLogger(ProducerWithKeys.class.getSimpleName());

    public static void main(String[] args) {
        log.info("Producer with Keys");

//        Create Producer Properties
        Properties properties = new Properties();

//        Connect to Conduktor Playground
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.mechanism", "PLAIN");
        properties.setProperty("bootstrap.servers", "cluster.playground.cdkt.io:9092");

//        Set Producer Properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

//        Create the Producer
        KafkaProducer <String, String> producer = new KafkaProducer<>(properties);

//        Create Producer Record to send data
        ProducerRecord <String, String> producerRecord = new ProducerRecord<>("demo_topic", "HelloWorld!");

        producer.send(producerRecord);

//        tell the producer to send all the data and block until done --synchronous
        producer.flush();

//        flush and close the producer
        producer.close();
    }
}
