package portmapper.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


class Caller {

    static String call(String address, int port, String args) {

        Socket socket;
        PrintWriter out;
        BufferedReader in;
        String answer;

        try {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            return "Unknown host";
        } catch (IOException e) {
            return "No I/O";
        }

        try {
            out.println(args);

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            answer = sb.toString();
        } catch (IOException e) {
            return "Error during communication";
        }

        try {
            socket.close();
        } catch (IOException e) {
            return "Cannot close the socket";
        }

        return answer;

    }

}