package assignment1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;


public class ProxyThread extends Thread {

    private Socket clientSocket;
    private static int nextThreadID = 0;
    private int threadID;

    ProxyThread(Socket socket) {
        this.clientSocket = socket;
    }

    private synchronized void setID() {
        threadID = nextThreadID++;
    }

    private void closeClientSocket() throws IOException {
        clientSocket.close();
        setID();
    }

    public void run() {
        try {
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = clientIn.readLine();
            System.out.println("(" + threadID + ")First line: " + request);

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
                System.out.println("(" + threadID + ")Next line: " + line);
            }
            /*
            for(int i=0;i<5;i++){
                System.out.println("(" + threadID + ")Next line: " + clientIn.readLine());
            }
             */

            String[] addressAndPort = request_parts[1].split(":");
            System.out.println("(" + threadID + ")Connecting to host: " + Arrays.toString(addressAndPort) + "\n");

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
                        System.out.println("(" + threadID + ")Error during communication");
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
            System.out.println("(" + threadID + ")Unknown host");
        } catch (IOException e) {
            System.out.println("(" + threadID + ")Error during communication");
        }
    }

}