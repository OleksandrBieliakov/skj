package tuto4.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Portmapper {

    private Map<String, Service> services = new HashMap<>();

    private void listenSocket() {
        ServerSocket server = null;
        Socket client = null;
        try {
            server = new ServerSocket(8081);
        } catch (IOException e) {
            System.out.println("Could not listen");
            System.exit(-1);
        }
        System.out.println("Server listens on port: " + server.getLocalPort());

        while (true) {
            try {
                client = server.accept();
            } catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(-1);
            }

            (new PortmapperThread(client, services)).start();
        }

    }

    public static void main(String[] args) {
        Portmapper server = new Portmapper();
        server.listenSocket();
    }

}