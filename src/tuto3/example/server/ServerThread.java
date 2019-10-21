package tuto3.example.server;

import java.io.*;
import java.net.*;


public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        super();
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while((line = in.readLine()) != null && !line.isEmpty())
            {
                System.out.println(line);

            }

            out.println("HTTP/1.1 200 OK");
            out.println();
            out.println();
        } catch (IOException e1) {
            // do nothing
        }

        try {
            socket.close();
        } catch (IOException e) {
            // do nothing
        }
    }
}