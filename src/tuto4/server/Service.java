package tuto4.server;

public class Service {

    private String address;
    private int port;

    Service(String adress, int port) {
        this.address = adress;
        this.port = port;
    }

    String getAddress() {
        return address;
    }

    int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return address + " " + port;
    }

}
