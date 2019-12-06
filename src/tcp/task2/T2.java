package tcp.task2;

import java.io.*;
import java.net.*;


public class T2 {
    private void listenSocket() {
        ServerSocket server = null;
        Socket client = null;
        try {
            server = new ServerSocket(0);
        }
        catch (IOException e) {
            System.out.println("Could not listen");
            System.exit(-1);
        }
        System.out.println("Server listens on port: " + server.getLocalPort());

        while(true) {
            try {
                client = server.accept();
            }
            catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(-1);
            }

            (new T2Thread(client)).start();
        }

    }

    public static void main(String[] args) {
        T2 server = new T2();
        server.listenSocket();
    }
}