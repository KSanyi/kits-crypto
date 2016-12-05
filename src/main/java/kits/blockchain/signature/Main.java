package kits.blockchain.signature;

import java.security.KeyPair;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        KeyPair keyPair = KeyGenerator.generateKeyPair();

        License license = new License(LocalDate.of(2017, 9, 30), "hwid012zwq", "feature1");

        String liceneString = LicenseSigner.signLicense(license, keyPair.getPrivate()).toString();

        System.out.println("License:\n" + liceneString);

        System.out.println();

        System.out.println("Public key:\n" + new String(KeyFileManager.convertToPemFormat(keyPair.getPublic())));

    }

}
