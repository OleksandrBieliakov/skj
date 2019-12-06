package portmapper.service;

import java.io.*;
import java.net.*;


public class ServiceThread extends Thread {
    private Socket socket;

    ServiceThread(Socket socket) {
        super();
        this.socket = socket;
    }

    private static String calculate(String message) {
        String[] parts = message.split(" ");
        double sum = 0.;
        for (String p : parts) {
            try {
                sum += Double.parseDouble(p);
            } catch (NumberFormatException e) {
                return "Incorrect number format.";
            }
        }
        return sum + "";
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(calculate(in.readLine()));
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