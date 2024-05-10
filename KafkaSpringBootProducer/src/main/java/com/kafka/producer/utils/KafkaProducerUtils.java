package com.kafka.producer.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.producer.KafkaProducerApplication;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

    public static void sendFileJSON(KafkaTemplate<String, ProducerRecord> kt) throws NullPointerException{
        ArrayList<ProducerRecord> records = null;
        while(true) {
            try {
                records = fileEventJSONTracer(Paths.get(System.getProperty("user.home")));
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } catch (InterruptedException inte) {
                System.err.println(inte.getMessage());
            }

            for (ProducerRecord pr : records) {
                kt.send(KafkaProducerApplication.JSON_EVENTS_TOPIC, pr);
                System.out.println("Event Sent");
            }
        }
    }

    public static ArrayList<ProducerRecord> fileEventJSONTracer(Path rootPath)
            throws IOException, InterruptedException {
        int batch = 10; // Prevent infinite loop (an async thread would be better but...)
        WatchService watchService
                = FileSystems.getDefault().newWatchService();

        rootPath.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        ObjectMapper om = new ObjectMapper();

        ArrayList<ProducerRecord> producerRecords = new ArrayList<>();

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                // Get the fields necessary
                Path filename = (Path)event.context();
                Path fullPath = rootPath.resolve(filename);
                String user = System.getProperty("user.name");
                Date date = new Date();

                // Format necessary fields to a string format
                String action = stringifyAction(event.kind());
                String formattedDate = formatDate(date);
                batch--;
                String avroKey = "key1";

                // Get the schema from registry
                Schema schema = SchemaRegistryUtils.retrieveSchemaFromRegistry(SchemaRegistryUtils.SCHEMA_URL);
                GenericRecord avroRecord = new GenericData.Record(schema);

                // Fill the fields of the avro schema with data
                avroRecord.put("fileName", filename);
                avroRecord.put("filePath", fullPath.toString());
                avroRecord.put("user", user);
                avroRecord.put("date", formattedDate);
                avroRecord.put("action", action);

                // Create a record witht the info
                ProducerRecord<Object, Object> record =
                        new ProducerRecord<>(KafkaProducerApplication.JSON_EVENTS_TOPIC, avroKey, avroRecord);

                /*
                FileEventData eventData = new FileEventData(filename.toString(), fullPath.toString(), user, formattedDate, action);
                String jsonEvent = om.writeValueAsString(eventData);
                */

                // Store the records in an arraylist
                producerRecords.add(record);
                if(batch == 0){
                    batch = 10;
                    return producerRecords;
                }
            }
            key.reset();
        }
        return producerRecords;
    }

    private static String stringifyAction(WatchEvent.Kind<?> kind) {
        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
            return "FILE CREATION";
        }else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)){
            return "FILE DELETION";
        }else{
            return "FILE MODIFICATION";
        }
    }

    private static String formatDate(Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(d);
    }
}
