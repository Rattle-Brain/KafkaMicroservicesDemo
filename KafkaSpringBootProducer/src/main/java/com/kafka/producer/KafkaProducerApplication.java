package com.kafka.producer;

import com.kafka.producer.config.KafkaTopicConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;

@SpringBootApplication
public class KafkaProducerApplication {

	@Autowired
	private KafkaAdmin kafkaAdmin;
	@Autowired
	private KafkaTopicConfig ktc;

	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerApplication.class, args);
	}

	@Bean
	public NewTopic topic1() {
		ktc = new KafkaTopicConfig();
		return ktc.createTopic("this-new-topic", 3, KafkaTopicConfig.DEFAULT_REPLICATION_FACTOR);
	}

	@Bean
	public NewTopic topic2() {
		ktc = new KafkaTopicConfig();
		return ktc.createTopic("kafka-games-topic", 2, KafkaTopicConfig.DEFAULT_REPLICATION_FACTOR);
	}

}
