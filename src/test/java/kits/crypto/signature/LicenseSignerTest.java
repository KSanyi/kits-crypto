package kits.crypto.signature;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class LicenseSignerTest {

    @Test
    public void testSignature() {
        KeyPair keyPair = KeyGenerator.generateKeyPair();

        License license = new License(LocalDate.of(2017, 9, 30), "hwid012zwq", "feature1");

        String liceneString = LicenseSigner.signLicense(license, keyPair.getPrivate()).toString();

        SignedLicense restoredSignedLicense = SignedLicense.parse(liceneString);

        assertTrue(LicenseSigner.verifySignature(restoredSignedLicense, keyPair.getPublic()));
    }

    @Test
    public void testTamperedLicense() {
        KeyPair keyPair = KeyGenerator.generateKeyPair();

        License license = new License(LocalDate.of(2017, 9, 30), "hwid012zwq", "feature1");

        SignedLicense signedLicense = LicenseSigner.signLicense(license, keyPair.getPrivate());

        License tamperedLicense1 = new License(LocalDate.of(2017, 9, 30), "xxxx", "feature1");
        assertFalse(LicenseSigner.verifySignature(new SignedLicense(tamperedLicense1, signedLicense.signature), keyPair.getPublic()));

        License tamperedLicense2 = new License(LocalDate.of(2017, 9, 30), "hwid012zwq", "feature1", "feature2");
        assertFalse(LicenseSigner.verifySignature(new SignedLicense(tamperedLicense2, signedLicense.signature), keyPair.getPublic()));

        License tamperedLicense3 = new License(LocalDate.of(2018, 9, 30), "hwid012zwq", "feature1");
        assertFalse(LicenseSigner.verifySignature(new SignedLicense(tamperedLicense3, signedLicense.signature), keyPair.getPublic()));

        License goodLicense = new License(LocalDate.of(2017, 9, 30), "hwid012zwq", "feature1");
        assertTrue(LicenseSigner.verifySignature(new SignedLicense(goodLicense, signedLicense.signature), keyPair.getPublic()));
    }

    @Test
    public void testSignatureWithKeyFiles() {
        License license = new License(LocalDate.of(2017, 9, 30), "", "feature1", "feature2");

        SignedLicense signedLicense = LicenseSigner.signLicense(license, "testdata/keypair/private.key");

        assertTrue(LicenseSigner.verifySignature(signedLicense, "testdata/keypair/public.key"));
    }

}
