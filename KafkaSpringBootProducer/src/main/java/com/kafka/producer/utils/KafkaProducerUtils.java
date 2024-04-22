package com.kafka.producer.utils;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.kafka.retrytopic.DestinationTopic;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class KafkaProducerUtils {

    public static HashMap<String, String> parseUserInput(String input){
        HashMap<String, String> result = new HashMap<>();

        // Split the input by "/"
        String[] parts = input.split("/", 2);

        // Check if the input has two parts (topic name and message)
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid input format. Expected format: topicname/message-to-send");
        }

        String topic = parts[0].trim(); // Remove leading and trailing spaces
        String msg = parts[1].trim();   // Remove leading and trailing spaces

        // Validate the topic name to ensure it does not contain spaces
        if (topic.contains(" ")) {
            throw new IllegalArgumentException("Topic name cannot contain spaces.");
        }

        result.put("topic", topic);
        result.put("msg", msg);

        return result;
    }

}
