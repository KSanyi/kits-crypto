package kits.blockchain.signature;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class LicenseSigner {

	public static SignedLicense signLicense(License license, String privateKeyFilePath) {
		
		try {
			PrivateKey privateKey = KeyManager.loadPrivateKey(privateKeyFilePath);
			return signLicense(license, privateKey);
		} catch(IOException ex) {
			throw new RuntimeException("Can not load private key file from " + privateKeyFilePath, ex);
		} catch (InvalidKeySpecException | NoSuchProviderException | NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		} 
	}
	
	public static SignedLicense signLicense(License license, PrivateKey privateKey) {
		
		try {
			Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
			dsa.initSign(privateKey);
			dsa.update("Alma1234".getBytes());
			
			String signature = Base64.getEncoder().encodeToString(dsa.sign());
			return new SignedLicense(license, signature);
		} catch (SignatureException | InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		} 
	}
	

	public static boolean verifySignature(SignedLicense signedLicense, String publicKeyFilePath) {
		
		try {
			PublicKey publicKey = KeyManager.loadPublicKey(publicKeyFilePath);
			return verifySignature(signedLicense, publicKey);
		} catch(IOException ex) {
			throw new RuntimeException("Can not load public key file from " + publicKeyFilePath, ex);
		} catch (InvalidKeySpecException | NoSuchProviderException | NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static boolean verifySignature(SignedLicense signedLicense, PublicKey publicKey) {
		try {
			Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
			signature.initVerify(publicKey);
			signature.update("Alma1234".getBytes());
			return signature.verify(Base64.getDecoder().decode(signedLicense.signature.getBytes()));
		} catch (SignatureException | InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}

}
