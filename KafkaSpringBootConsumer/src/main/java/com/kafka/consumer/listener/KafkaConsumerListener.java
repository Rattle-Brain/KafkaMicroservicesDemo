package com.kafka.consumer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
public class KafkaConsumerListener {

    private Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerListener.class);

    @KafkaListener(topics = {"this-new-topic"}, groupId = "new-topic-groupid")
    public void listenerNew(String msg){
        LOGGER.info("Msg from topic \"this-new-topic\": " + msg);
    }
    @KafkaListener(id = "my-consumer", groupId = "new-topic-groupid", topicPattern = ".*")
    public void listenToAllTopics(String message, @Header("kafka_receivedTopic") String topic) {
        System.out.println("Msg from topic \"" + topic + "\": " + message);
    }
/*
    @KafkaListener(topics = {"kafka-games-topic"}, groupId = "kafka-games-groupid")
    public void listenerGames(String msg){
        LOGGER.info("Msg from topic \"kafka-games-toic\": " + msg);
    }

 */
}
