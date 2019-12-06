package portmapper.service;

import java.io.*;
import java.net.*;

public class ServiceLauncher {

    private void listenSocket() {
        ServerSocket server = null;
        Socket client = null;
        try {
            server = new ServerSocket(8082);
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

            (new ServiceThread(client)).start();
        }

    }

    public static void main(String[] args) {
        ServiceLauncher server = new ServiceLauncher();
        server.listenSocket();
    }

}