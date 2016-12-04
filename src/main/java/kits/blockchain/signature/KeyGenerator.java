package kits.blockchain.signature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyGenerator {

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
			keyGen.initialize(1024);
			return keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
			throw new RuntimeException("Could not generate key pair", ex);
		}
	}
	
	public static void generateKeyPair(String keyFolder) {
		KeyPair keyPair = generateKeyPair();
		
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		
		try{
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
			Files.write(Paths.get(keyFolder, "private"), Base64.getEncoder().encode(privateKeySpec.getEncoded()));
			
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
			Files.write(Paths.get(keyFolder, "public"), Base64.getEncoder().encode(publicKeySpec.getEncoded()));	
		} catch(IOException ex) {
			throw new RuntimeException("Could not save key into file", ex);
		}
	}
	
}
