package tuto4;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class PortmapperThread extends Thread {
    private Socket socket;
    private List<Service> services;

    PortmapperThread(Socket socket, List<Service> services) {
        super();
        this.socket = socket;
        this.services = services;
    }

    private String register(String[] message) {
        return "registered";
    }

    private String get(String[] message) {
        return "got";
    }

    private String call(String[] message) {
        return "called";
    }
    private String processMessage(String message) {
        System.out.println(message);

        String[] parts = message.split(" ");
        String command = parts[0];

        if(command.equals("register")) {
            return register(parts);
        } else if(command.equals("get")) {
            return get(parts);
        } else if(command.equals("call")) {
            return call(parts);
        }

        System.out.println("ERROR: Incorrect command.");
        return "Incorrect command. Try again";
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(processMessage(in.readLine()));
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