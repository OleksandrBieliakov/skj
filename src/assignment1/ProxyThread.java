package assignment1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;


public class ProxyThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ProxyThread.class);

    private Socket clientSocket;

    ProxyThread(Socket socket) {
        this.clientSocket = socket;
    }

    private void closeClientSocket() throws IOException {
        clientSocket.close();
    }

    public void run() {
        try {
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = clientIn.readLine();
            logger.debug("First line: {}", request);

            String[] request_parts;

            if (request == null) {
                closeClientSocket();
                return;
            }
            request_parts = request.split(" ");
            if (!request_parts[0].equals("CONNECT")) {
                closeClientSocket();
                return;
            }

            String line;
            while ((line = clientIn.readLine()) != null && !line.isEmpty()) {
                logger.debug("Next line: {}", line);
            }
            /*
            for(int i=0;i<5;i++){
                logger.debug("Next line: {}", clientIn.readLine());
            }
             */

            String[] addressAndPort = request_parts[1].split(":");
            logger.debug("Connecting to host: {}\n", Arrays.toString(addressAndPort));

            String address = addressAndPort[0];

            if(!address.startsWith("http")){
                String temp = "http://";
                address = temp + address;
            }

            InetAddress inetAddress = InetAddress.getByName(address);

            Socket serverSocket = new Socket(inetAddress, Integer.parseInt(addressAndPort[1]));

            clientOut.println("HTTP/1.1 200 OK");

            clientIn.close();
            clientOut.close();

            new Thread(() -> {
                while (true) {
                    try {
                        byte[] buffer = new byte[4096];
                        int read;
                        do {
                            read = clientSocket.getInputStream().read(buffer);
                            if (read > 0) {
                                serverSocket.getOutputStream().write(buffer, 0, read);
                                if (clientSocket.getInputStream().available() < 1) {
                                    serverSocket.getOutputStream().flush();
                                }
                            }
                        } while (read >= 0);
                    } catch (IOException e) {
                        logger.error("Error during communication", e);
                    }
                }
            }).start();

            byte[] buffer = new byte[4096];
            int read;
            do {
                read = serverSocket.getInputStream().read(buffer);
                if (read > 0) {
                    clientSocket.getOutputStream().write(buffer, 0, read);
                    if (serverSocket.getInputStream().available() < 1) {
                        clientSocket.getOutputStream().flush();
                    }
                }
            } while (read >= 0);

            closeClientSocket();
            serverSocket.close();

        } catch (UnknownHostException e) {
            logger.error("Unknown host");
        } catch (IOException e) {
            logger.error("Error during communication", e);
        }
    }

}