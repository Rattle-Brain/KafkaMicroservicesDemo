package com.kafka.producer.utils;

import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPSockUtils {
    public static final int DEF_BUF_SIZE = 1024;

    /**
     * Connects to a socket and receives events from an eBPF application
     * set to send data to port 3040 by default.
     * The eBPF application reacts to kernel events every time execv is
     * called. Then sends de process calling, the called process and the pid.
     *
     * @param kt Kafka template to send msgs to topic
     * @param port port to listen to
     */
    public static void retrieveUDP(KafkaTemplate<String, String> kt,  int port) {
        // Define buffer to retrieve data
        byte[] buffer = new byte[DEF_BUF_SIZE];

        try (DatagramSocket socket = new DatagramSocket(port)) {
            // Create a packet
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Bucle infinito para recibir mensajes continuamente
            while (true) {
                // Recibir un mensaje
                socket.receive(packet);

                // From bytes to string
                String message = new String(packet.getData());

                // Split the msg in parts
                String[] parts = message.split(" ");
                int cpu = Integer.parseInt(parts[0]);
                String comm = parts[1];
                int pid = Integer.parseInt(parts[2]);
                String fName = parts[3];
                System.out.println("Message received and parsed...");

                // We want to observe the events of dockerd, not all of them.
                if(comm.trim().contains("dockerd")){
                    message = String.format("CPU %02d %s %d ran: %s\n", cpu, comm.trim(), pid, fName.trim());


                    kt.send("bpf-events-topic", message);
                    System.out.println("Sent!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
