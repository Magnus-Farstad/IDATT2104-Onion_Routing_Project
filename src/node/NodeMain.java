package node;

import cryptography.AESencryption;
import model.Node;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class NodeMain {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        AESencryption aesEncryption = new AESencryption();
        SecretKey aesKey = aesEncryption.generateAESKey();

        Node thisNode = new Node(aesKey, 1250);

    }

}
