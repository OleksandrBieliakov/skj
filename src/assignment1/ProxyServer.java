package assignment1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer {

    private static final Logger logger = LoggerFactory.getLogger(ProxyServer.class);

    private void listenSocket() {
        ServerSocket proxyServerSocket = null;
        Socket clientSocket = null;
        try {
            proxyServerSocket = new ServerSocket(50000);
        } catch (IOException e) {
            logger.error("Could not listen", e);
            System.exit(-1);
        }
        logger.info("Server listens on port: {}", proxyServerSocket.getLocalPort());

        while (true) {
            try {
                clientSocket = proxyServerSocket.accept();
                logger.debug("Client Socket: {}", clientSocket);
            } catch (IOException e) {
                logger.error("Accept failed", e);
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