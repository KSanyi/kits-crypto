package kits.crypto.signature;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.Assert;
import org.junit.Test;

import kits.crypto.signature.KeyFileManager;
import kits.crypto.signature.KeyGenerator;

public class KeyFileManagerTest {

    @Test
    public void testPrivateKeyStorage() throws IOException {
        KeyPair keyPair = KeyGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        String privateKeyPath = File.createTempFile("private", "key").getAbsolutePath();

        KeyFileManager.savePrivateKeyToPemFile(privateKey, privateKeyPath);

        PrivateKey restoredPrivateKey = KeyFileManager.loadPrivateKeyFromPemFile(privateKeyPath);

        Assert.assertEquals(privateKey, restoredPrivateKey);
    }

    @Test
    public void testPublicKeyStorage() throws IOException {
        KeyPair keyPair = KeyGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        String publicKeyPath = File.createTempFile("public", "key").getAbsolutePath();

        KeyFileManager.savePublicKeyToPemFile(publicKey, publicKeyPath);

        PublicKey restoredPublicKey = KeyFileManager.loadPublicKeyFromPemFile(publicKeyPath);

        Assert.assertEquals(publicKey, restoredPublicKey);
    }

}
