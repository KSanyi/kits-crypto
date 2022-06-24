package kits.crypto.signature;

import java.security.KeyPair;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        KeyPair keyPair = KeyGenerator.generateKeyPair();

        License license = new License(LocalDate.of(2017, 9, 30), "hwid012zwq", "feature1");

        String signedLicenseString = LicenseSigner.signLicense(license, keyPair.getPrivate()).toString();

        System.out.println("Signed license:\n" + signedLicenseString);

        System.out.println();

        String publicKeyString = new String(KeyFileManager.convertToPemFormat(keyPair.getPublic()));
        
        System.out.println("Public key:\n" + publicKeyString);

        System.out.println("Verifying signature");
        boolean isValid = LicenseSigner.verifySignature(SignedLicense.parse(signedLicenseString), KeyFileManager.loadPublicKeyFromPemString(publicKeyString));
        System.out.println("Signaure is valid? " + isValid);
    }

}
