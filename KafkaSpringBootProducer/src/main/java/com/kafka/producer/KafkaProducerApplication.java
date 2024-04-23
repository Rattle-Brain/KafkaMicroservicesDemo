package com.kafka.producer;

import ch.qos.logback.core.net.SyslogOutputStream;
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
import java.util.HashMap;

import static com.kafka.producer.utils.KafkaProducerUtils.parseUserInput;

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
					kt.send("default-topic", "Producer died");
					break;
				}

				HashMap <String, String> result;
				try {
					result = parseUserInput(input);
				}catch(IllegalArgumentException iae){
					System.err.println(iae.getMessage());
					continue;
				}
				kt.send(result.get("topic"), result.get("msg"));
				System.out.println("Message sent to Kafka: " + input);
			}
			System.out.println("Exiting Kafka Producer Application...");
			System.exit(0);
		};
	}

	@Bean
	public NewTopic topic1() {
		ktc = new KafkaTopicConfig();
		return ktc.createTopic("this-new-topic", 3, KafkaTopicConfig.DEFAULT_REPLICATION_FACTOR);
		//return ktc.generateDefaultTopic();
	}

	@Bean
	public NewTopic topic2() {
		ktc = new KafkaTopicConfig();
		return ktc.createTopic("kafka-games-topic", 2, KafkaTopicConfig.DEFAULT_REPLICATION_FACTOR);
	}

	@Bean
	public NewTopic topic3() {
		ktc = new KafkaTopicConfig();
		return ktc.createTopic("default-topic", 1, KafkaTopicConfig.DEFAULT_REPLICATION_FACTOR);
	}
}
