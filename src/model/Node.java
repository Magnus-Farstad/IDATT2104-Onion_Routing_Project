package model;

import javax.crypto.SecretKey;

public class Node {

    private SecretKey aesKey;
    private int port;
    private String address;

    public Node(SecretKey aesKey, int port, String address){
        this.aesKey = aesKey;
        this.port = port;
        this.address = address;
    }

    public SecretKey getAesKey() {
        return aesKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAesKey(SecretKey aesKey) {
        this.aesKey = aesKey;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}