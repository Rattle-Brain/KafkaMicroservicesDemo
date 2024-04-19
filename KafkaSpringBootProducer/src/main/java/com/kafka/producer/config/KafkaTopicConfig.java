 package com.kafka.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    public static final short DEFAULT_REPLICATION_FACTOR = 1;

    private static Map<String, String> default_configurations;
/*
    @Bean
    public NewTopic createTopic(char[] topicName, short partitions, Map<String, String> configs){
        return TopicBuilder
                .name(topicName.toString())
                .partitions(partitions)
                .configs(configs)
                .replicas(DEFAULT_REPLICATION_FACTOR)
                .build();
    }

    public NewTopic createTopic(String topicName, short partitions){
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .configs(generateDefaultConfigs(default_configurations))
                .replicas(DEFAULT_REPLICATION_FACTOR)
                .build();
    }

    public NewTopic createTopic(char[] topicName, short partitions, short replicationFactor){
        return TopicBuilder
                .name(topicName.toString())
                .partitions(partitions)
                .configs(generateDefaultConfigs(default_configurations))
                .replicas(replicationFactor)
                .build();
    }
*/
    @Bean
    public NewTopic generateDefaultTopic(){
        return new NewTopic("default-topic", 1, (short)1);
    }

    private Map<String, String> generateDefaultConfigs(Map<String, String> defConfig){
        defConfig = new HashMap<>();
        defConfig.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE); // delete (borra mensaje) - compact (Mantiene el mas actual)
        defConfig.put(TopicConfig.RETENTION_MS_CONFIG, "86400000"); // Tiempo de retencion de mensajes, defecto -1
        defConfig.put(TopicConfig.SEGMENT_BYTES_CONFIG, "1073741824"); // Tamanio maximo del segmento - defecto 1073741824 bytes - 1GB
        defConfig.put(TopicConfig.MAX_MESSAGE_BYTES_CONFIG, "1000012"); // Tamanio maximo de cada mensaje - defecto 1000000 - 1 MB

        return defConfig;
    }
}
