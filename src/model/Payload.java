package model;

import java.util.ArrayList;

public class Payload {

    private ArrayList<String> data;
    private ArrayList<String> addresses;
    private ArrayList<String> portNumbers;
    private Payload nextPayload;

    public Payload(ArrayList<String> data, ArrayList<String> addresses, ArrayList<String> portNumbers) {
        this.data = data;
        this.addresses = addresses;
        this.portNumbers = portNumbers;
    }

    public Payload(ArrayList<String> addresses, ArrayList<String> portNumbers) {
        this.addresses = addresses;
        this.portNumbers = portNumbers;
    }

    public Payload(String data) {
        this.data.add(data);
    }

    public ArrayList<String> getData() {
        return data;
    }

    public ArrayList<String> getAddresses() {
        return addresses;
    }

    public ArrayList<String> getPortNumbers() {
        return portNumbers;
    }
}
