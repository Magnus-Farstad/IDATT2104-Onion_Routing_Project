package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Payload implements Serializable {

    private String data;
    private String address;
    private int portNumber;
    private Payload lastPayload;
    private byte[] nextPayload;

    public Payload(String data, String address, int portNumber) {
        this.data = data;
        this.address = address;
        this.portNumber = portNumber;
    }

    public Payload(String address, int portNumber) {
        this.address = address;
        this.portNumber = portNumber;
    }

    public Payload(String data) {
        this.data = data;
    }

    public Payload() {
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setNextPayload(byte[] nextPayload) {
        this.nextPayload = nextPayload;
    }
}
