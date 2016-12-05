package kits.blockchain.signature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class KeyFileManager {

    public static void savePrivateKeyToPemFile(PrivateKey privateKey, String privateKeyFilePath) {
        try {
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
            Files.write(Paths.get(privateKeyFilePath), toPemFormat(privateKeySpec.getEncoded(), "PRIVATE KEY"));
        } catch (IOException ex) {
            throw new RuntimeException("Could not save key into file", ex);
        }
    }

    public static void savePublicKeyToPemFile(PublicKey publicKey, String publicKeyFilePath) {
        try {
            Files.write(Paths.get(publicKeyFilePath), convertToPemFormat(publicKey));
        } catch (IOException ex) {
            throw new RuntimeException("Could not save public key into file", ex);
        }
    }

    public static byte[] convertToPemFormat(PublicKey publicKey) {
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        return toPemFormat(publicKeySpec.getEncoded(), "PUBLIC KEY");
    }

    public static byte[] toPemFormat(byte[] keyBytes, String keyType) {
        byte[] encodedBytes = Base64.getEncoder().encode(keyBytes);

        List<String> lines = new ArrayList<>();
        lines.add("-----BEGIN " + keyType + "-----");

        for (int i = 0; i < encodedBytes.length; i += 64) {
            lines.add(new String(Arrays.copyOfRange(encodedBytes, i, Integer.min(encodedBytes.length, i + 64))));
        }
        lines.add("-----END " + keyType + "-----");

        return String.join("\n", lines).getBytes();
    }

    public static PrivateKey loadPrivateKeyFromPemFile(String privateKeyFilePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(privateKeyFilePath));

            byte[] encodedKeyBytes = String.join("", lines.subList(1, lines.size() - 1)).getBytes();
            byte[] keyBytes = Base64.getDecoder().decode(encodedKeyBytes);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("DSA").generatePrivate(spec);
        } catch (IOException ex) {
            throw new RuntimeException("Can not read private key file from " + privateKeyFilePath, ex);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static PublicKey loadPublicKeyFromPemFile(String publicKeyFilePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(publicKeyFilePath));

            byte[] encodedKeyBytes = String.join("", lines.subList(1, lines.size() - 1)).getBytes();
            byte[] keyBytes = Base64.getDecoder().decode(encodedKeyBytes);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("DSA").generatePublic(spec);
        } catch (IOException ex) {
            throw new RuntimeException("Can not read public key file from " + publicKeyFilePath, ex);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

}
