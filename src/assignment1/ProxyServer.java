package assignment1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer {

    private void listenSocket() {
        ServerSocket proxyServerSocket = null;
        Socket clientSocket = null;
        try {
            proxyServerSocket = new ServerSocket(50000);
        } catch (IOException e) {
            System.out.println("Could not listen");
            System.exit(-1);
        }
        System.out.println("Server listens on port: " + proxyServerSocket.getLocalPort());

        while (true) {
            try {
                clientSocket = proxyServerSocket.accept();
            } catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(-1);
            }
            new ProxyThread(clientSocket).start();
        }
    }

    public static void main(String[] args) {
        ProxyServer server = new ProxyServer();
        server.listenSocket();
    }

}