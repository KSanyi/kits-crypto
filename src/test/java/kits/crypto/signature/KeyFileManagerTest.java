package kits.crypto.signature;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.jupiter.api.Test;

public class KeyFileManagerTest {

    @Test
    public void testPrivateKeyStorage() throws IOException {
        KeyPair keyPair = KeyGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        String privateKeyPath = File.createTempFile("private", "key").getAbsolutePath();

        KeyFileManager.savePrivateKeyToPemFile(privateKey, privateKeyPath);

        PrivateKey restoredPrivateKey = KeyFileManager.loadPrivateKeyFromPemFile(privateKeyPath);

        assertEquals(privateKey, restoredPrivateKey);
    }

    @Test
    public void testPublicKeyStorage() throws IOException {
        KeyPair keyPair = KeyGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        String publicKeyPath = File.createTempFile("public", "key").getAbsolutePath();

        KeyFileManager.savePublicKeyToPemFile(publicKey, publicKeyPath);

        PublicKey restoredPublicKey = KeyFileManager.loadPublicKeyFromPemFile(publicKeyPath);

        assertEquals(publicKey, restoredPublicKey);
    }

}
