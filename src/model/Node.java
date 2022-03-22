import javax.crypto.SecretKey;

public class Node {

    private SecretKey aesKey;
    private int port;

    public Node( String host, int port){
        this.port = port;
    }
    public Node(SecretKey aesKey, int port){
        this.aesKey = aesKey;
        this.port = port;
    }

    public SecretKey getAesKey() {
        return aesKey;
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
}