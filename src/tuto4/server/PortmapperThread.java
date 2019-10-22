package tuto4.server;

import java.io.*;
import java.net.*;
import java.util.Map;

public class PortmapperThread extends Thread {

    private Socket socket;
    private Map<String, Service> services;

    PortmapperThread(Socket socket, Map<String, Service> services) {
        super();
        this.socket = socket;
        this.services = services;
    }

    private static boolean checkIPv4(String sample) {
        String[] parts = sample.split("\\.");
        if (parts.length != 4) return false;
        for (int i = 0, part; i < 4; ++i) {
            try {
                part = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                return false;
            }
            if (part < 0 || part > 255) return false;
        }
        return true;
    }

    private static boolean checkPort(String sample) {
        int port;
        try {
            port = Integer.parseInt(sample);
        } catch (NumberFormatException e) {
            return false;
        }
        return port >= 0;
    }

    private String register(String[] parts) {
        if (parts.length != 4) return "REGISTER: incorrect command. Should be: \"register <name> <IPv4> <port>\"";
        String name = parts[1];
        if (services.containsKey(name)) return "REGISTER: entered name is not available";
        if (!checkIPv4(parts[2])) return "REGISTER: incorrect IPv4 format";
        if (!checkPort(parts[3])) return "REGISTER: incorrect port number format";
        Service service = new Service(parts[2], Integer.parseInt(parts[3]));
        services.put(name, service);
        return "Service " + service + " successfully registered as \"" + name + "\"";
    }

    private String get(String[] parts) {
        if (parts.length != 2) return "GET: incorrect command. Should be: \"get <name>\"";
        String name = parts[1];
        if (!services.containsKey(name)) return "There is no service registered as: " + name;
        return services.get(name).toString();
    }

    private String call(String[] parts) {
        int len = parts.length;
        if (len < 2) return "CALL: incorrect command. Should be: \"call <name> args...\"";
        String name = parts[1];
        if (!services.containsKey(name)) return "There is no service registered as: " + name;

        StringBuilder args = new StringBuilder();
        for (int i = 2; i < len; i++) {
            args.append(parts[i]);
            if (i != len - 1) args.append(" ");
        }
        Service service = services.get(name);
        return Caller.call(service.getAddress(), service.getPort(), args.toString());
    }

    private String processMessage(String message) {
        System.out.println("Received message: " + message);
        String[] parts = message.split(" ");

        switch (parts[0]) {
            case "register":
                return register(parts);
            case "get":
                return get(parts);
            case "call":
                return call(parts);
        }
        return "Incorrect command. Try again.";
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String response = processMessage(in.readLine());
            System.out.println("Response: " + response + "\n");
            out.println(response);
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