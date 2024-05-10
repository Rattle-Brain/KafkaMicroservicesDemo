package com.kafka.producer;

import com.kafka.producer.config.KafkaTopicConfig;
import com.kafka.producer.utils.KafkaProducerUtils;
import com.kafka.producer.utils.UDPSockUtils;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaProducerApplication {

	public final static String JSON_EVENTS_TOPIC = "json-file-events";

	@Autowired
	private KafkaTopicConfig ktc;

	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerApplication.class, args);
	}

	@Bean
	CommandLineRunner init (KafkaTemplate<String, ProducerRecord> kt){
		return args -> {
			KafkaProducerUtils.sendFileJSON(kt);
			//UDPSockUtils.retrieveUDP(kt, 3040);
		};
	}

	@Bean
	public NewTopic topic1() {
		ktc = new KafkaTopicConfig();
		return ktc.createTopic("bpf-events-topic", 3, KafkaTopicConfig.DEFAULT_REPLICATION_FACTOR);
	}

	@Bean
	public NewTopic topic2() {
		ktc = new KafkaTopicConfig();
		return ktc.createTopic("kafka-games-topic", 2, KafkaTopicConfig.DEFAULT_REPLICATION_FACTOR);
	}

	@Bean
	public NewTopic topic3() {
		ktc = new KafkaTopicConfig();
		return ktc.createTopic("default-topic", 3, KafkaTopicConfig.DEFAULT_REPLICATION_FACTOR);
	}

	@Bean
	public NewTopic topicJSON() {
		ktc = new KafkaTopicConfig();
		return ktc.createTopic(JSON_EVENTS_TOPIC, 1, KafkaTopicConfig.DEFAULT_REPLICATION_FACTOR);
	}
}
