package kits.crypto.signature;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

public class LicenseSigner {

    public static SignedLicense signLicense(License license, String privateKeyFilePath) {
        PrivateKey privateKey = KeyFileManager.loadPrivateKeyFromPemFile(privateKeyFilePath);
        return signLicense(license, privateKey);
    }

    public static SignedLicense signLicense(License license, PrivateKey privateKey) {
        try {
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(privateKey);
            dsa.update(license.getBytes());

            String signature = Base64.getEncoder().encodeToString(dsa.sign());
            return new SignedLicense(license, signature.getBytes());
        } catch (SignatureException | InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean verifySignature(SignedLicense signedLicense, String publicKeyFilePath) {
        PublicKey publicKey = KeyFileManager.loadPublicKeyFromPemFile(publicKeyFilePath);
        return verifySignature(signedLicense, publicKey);
    }

    public static boolean verifySignature(SignedLicense signedLicense, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
            signature.initVerify(publicKey);
            signature.update(signedLicense.license.getBytes());
            return signature.verify(Base64.getDecoder().decode(signedLicense.signature));
        } catch (SignatureException | InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

}
