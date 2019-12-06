package tcp.task2;

import java.io.*;
import java.net.*;


public class T2Thread extends Thread {
    private Socket socket;

    T2Thread(Socket socket) {
        super();
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String line1 = in.readLine();
            String line2 = in.readLine();
            out.println("Echo: "+ line1 + " " + line2);
            System.out.println("After sending information");
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