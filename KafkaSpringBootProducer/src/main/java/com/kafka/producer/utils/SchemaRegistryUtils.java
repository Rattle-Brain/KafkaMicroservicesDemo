package com.kafka.producer.utils;

import org.apache.avro.Schema;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SchemaRegistryUtils {

    /**
     * Tries to connect to a schema registry by url and retrieve a schema for later use.
     *
     * @param schemaRegistryUrl URL of the schema registry
     * @return the Schema retrieved as an Apache Avro Schema
     * @throws IOException if something went wrong while retrieving the schema
     */
    public static Schema retrieveSchemaFromRegistry(String schemaRegistryUrl) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet(schemaRegistryUrl);
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String schemaJson = EntityUtils.toString(entity);

                // Parse the JSON schema string into an Avro Schema object
                return new Schema.Parser().parse(schemaJson);
            } else {
                throw new IOException("Failed to retrieve schema from Schema Registry."+
                        " Status code: " + response.getStatusLine().getStatusCode());
            }
        } finally {
            httpClient.close();
        }
    }
}
