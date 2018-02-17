package kits.crypto.signature;

import java.security.KeyPair;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import kits.crypto.signature.KeyGenerator;
import kits.crypto.signature.License;
import kits.crypto.signature.LicenseSigner;
import kits.crypto.signature.SignedLicense;

public class LicenseSignerTest {

    @Test
    public void testSignature() {
        KeyPair keyPair = KeyGenerator.generateKeyPair();

        License license = new License(LocalDate.of(2017, 9, 30), "hwid012zwq", "feature1");

        String liceneString = LicenseSigner.signLicense(license, keyPair.getPrivate()).toString();

        SignedLicense restoredSignedLicense = SignedLicense.parse(liceneString);

        Assert.assertTrue(LicenseSigner.verifySignature(restoredSignedLicense, keyPair.getPublic()));
    }

    @Test
    public void testTamperedLicense() {
        KeyPair keyPair = KeyGenerator.generateKeyPair();

        License license = new License(LocalDate.of(2017, 9, 30), "hwid012zwq", "feature1");

        SignedLicense signedLicense = LicenseSigner.signLicense(license, keyPair.getPrivate());

        License tamperedLicense1 = new License(LocalDate.of(2017, 9, 30), "xxxx", "feature1");
        Assert.assertFalse(LicenseSigner.verifySignature(new SignedLicense(tamperedLicense1, signedLicense.signature), keyPair.getPublic()));

        License tamperedLicense2 = new License(LocalDate.of(2017, 9, 30), "hwid012zwq", "feature1", "feature2");
        Assert.assertFalse(LicenseSigner.verifySignature(new SignedLicense(tamperedLicense2, signedLicense.signature), keyPair.getPublic()));

        License tamperedLicense3 = new License(LocalDate.of(2018, 9, 30), "hwid012zwq", "feature1");
        Assert.assertFalse(LicenseSigner.verifySignature(new SignedLicense(tamperedLicense3, signedLicense.signature), keyPair.getPublic()));

        License goodLicense = new License(LocalDate.of(2017, 9, 30), "hwid012zwq", "feature1");
        Assert.assertTrue(LicenseSigner.verifySignature(new SignedLicense(goodLicense, signedLicense.signature), keyPair.getPublic()));
    }

    @Test
    public void testSignatureWithKeyFiles() {
        License license = new License(LocalDate.of(2017, 9, 30), "", "feature1", "feature2");

        SignedLicense signedLicense = LicenseSigner.signLicense(license, "testdata/keypair/private.key");

        Assert.assertTrue(LicenseSigner.verifySignature(signedLicense, "testdata/keypair/public.key"));
    }

}
