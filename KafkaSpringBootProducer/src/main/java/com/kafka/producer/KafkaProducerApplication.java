package com.kafka.producer;

import com.kafka.producer.config.KafkaTopicConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
	CommandLineRunner init (KafkaTemplate<String, String> kt){
		return args -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter your message (type 'exit' to quit):");
			while (true) {
				String input = reader.readLine();
				if (input.equalsIgnoreCase("exit")) {
					break;
				}
				kt.send("default-topic", input);
				System.out.println("Message sent to Kafka: " + input);
			}
			System.out.println("Exiting Kafka Producer Application...");
		};
	}

	@Bean
	public NewTopic topic1() {
		ktc = new KafkaTopicConfig();
		//return ktc.createTopic("this-new-topic".toCharArray(), (short)3, KafkaTopicConfig.DEFAULT_REPLICATION_FACTOR);
		return ktc.generateDefaultTopic();
	}
/*
	@Bean
	public NewTopic topic2() {
		ktc = new KafkaTopicConfig();
		return ktc.createTopic("kafka-games-topic".toCharArray(), (short)2, KafkaTopicConfig.DEFAULT_REPLICATION_FACTOR);
	}
*/
}
