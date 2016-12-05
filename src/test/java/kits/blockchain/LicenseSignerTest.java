package kits.blockchain;

import java.time.LocalDate;

import org.junit.Test;

import kits.blockchain.signature.KeyManager;
import kits.blockchain.signature.License;
import kits.blockchain.signature.LicenseSigner;
import kits.blockchain.signature.SignedLicense;

public class LicenseSignerTest {

	@Test
	public void test2() {

		KeyManager.generateKeyPair("testdata/keypair");
		KeyManager.generateKeyPair("testdata/keypair2");

		String privateKeyPath = "testdata/keypair/private";
		String publicKeyPath = "testdata/keypair/public";

		String hardwareId = "";
		License license = new License(LocalDate.of(2017, 9, 30), hardwareId, "feature1", "feature2");

		SignedLicense signedLicense = LicenseSigner.signLicense(license, privateKeyPath);

		System.out.println(LicenseSigner.verifySignature(signedLicense, publicKeyPath));

	}

}
