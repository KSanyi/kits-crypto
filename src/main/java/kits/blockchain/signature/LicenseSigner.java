package kits.blockchain.signature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class LicenseSigner {

	public static SignedLicense signLicense(License license, String privateKeyFilePath) {
		
		try {
			PrivateKey privateKey = loadPrivateKey(privateKeyFilePath);
			return signLicense(license, privateKey);
		} catch(IOException ex) {
			throw new RuntimeException("Can not load private key file from " + privateKeyFilePath, ex);
		} catch (InvalidKeySpecException | NoSuchProviderException | NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		} 
	}
	
	private static PrivateKey loadPrivateKey(String privateKeyFilePath) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		byte[] encodedKeyBytes = Files.readAllBytes(Paths.get(privateKeyFilePath));
		byte[] keyBytes = Base64.getDecoder().decode(encodedKeyBytes);
		
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
	    return KeyFactory.getInstance("DSA").generatePrivate(spec);
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
			PublicKey publicKey = loadPublicKey(publicKeyFilePath);
			return verifySignature(signedLicense, publicKey);
		} catch(IOException ex) {
			throw new RuntimeException("Can not load public key file from " + publicKeyFilePath, ex);
		} catch (InvalidKeySpecException | NoSuchProviderException | NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static PublicKey loadPublicKey(String publicKeyFilePath) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		byte[] encodedKeyBytes = Files.readAllBytes(Paths.get(publicKeyFilePath));
		byte[] keyBytes = Base64.getDecoder().decode(encodedKeyBytes);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
	    return KeyFactory.getInstance("DSA").generatePublic(spec);
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
