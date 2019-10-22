package tuto4.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {

    public static void main(String[] args) {

        Socket socket = null;
        PrintWriter out;
        BufferedReader in;
        String address = "127.0.0.1";
        int port = 8081;

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        try {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            switch (command) {
                case "r":
                    out.println("register adder 127.0.0.1 8082");
                    break;
                case "g":
                    out.println("get adder");
                    break;
                case "c":
                    out.println("call adder 1 2 0.6 -0.5");
                    break;
            }
            System.out.println(in.readLine());
        } catch (UnknownHostException e) {
            System.out.println("Unknown host");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Error during communication");
            System.exit(-1);
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Cannot close the socket");
            System.exit(-1);
        }

    }

}
