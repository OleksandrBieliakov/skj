package tuto4;

public class Service {
    private String name;
    private String adress;
    private int port;

    Service(String name, String adress, int port) {
        this.name = name;
        this.adress = adress;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getAdress() {
        return adress;
    }

    public int getPort() {
        return port;
    }

}
