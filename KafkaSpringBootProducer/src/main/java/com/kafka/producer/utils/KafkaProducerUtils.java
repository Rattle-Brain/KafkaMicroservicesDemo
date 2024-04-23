package com.kafka.producer.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.DestinationTopic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    public static void readSendUserInput(KafkaTemplate<String, String> kt) throws IOException {
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
    }
}
