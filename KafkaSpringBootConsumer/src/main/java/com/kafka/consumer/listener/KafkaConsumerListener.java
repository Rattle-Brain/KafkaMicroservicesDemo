package com.kafka.consumer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class KafkaConsumerListener {

    private Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerListener.class);

    @KafkaListener(topics = {"default-topic"}, groupId = "new-topic-groupid")
    public void listenerNew(String msg){
        LOGGER.info("Msg from topic \"this-new-topic\": " + msg);
    }
/*
    @KafkaListener(topics = {"kafka-games-topic"}, groupId = "kafka-games-groupid")
    public void listenerGames(String msg){
        LOGGER.info("Msg from topic \"kafka-games-toic\": " + msg);
    }

 */
}
