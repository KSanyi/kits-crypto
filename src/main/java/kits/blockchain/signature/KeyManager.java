package kits.blockchain.signature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class KeyManager {

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
			Files.write(Paths.get(keyFolder, "private"), toPemFormat(privateKeySpec.getEncoded(), "PRIVATE KEY"));
			
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
			Files.write(Paths.get(keyFolder, "public"), toPemFormat(publicKeySpec.getEncoded(), "PUBLIC KEY"));	
		} catch(IOException ex) {
			throw new RuntimeException("Could not save key into file", ex);
		}
	}
	
	private static byte[] toPemFormat(byte[] keyBytes, String keyType) {
		byte[] encodedBytes = Base64.getEncoder().encode(keyBytes);
		
		List<String> lines = new ArrayList<>();
		lines.add("-----BEGIN " + keyType + "-----");

		for(int i=0;i<encodedBytes.length;i+=64) {
			lines.add(new String(Arrays.copyOfRange(encodedBytes, i, Integer.min(encodedBytes.length, i+64))));
		}
		lines.add("-----END " + keyType + "-----");
		
		return String.join("\n", lines).getBytes();
	}
	
	public static PrivateKey loadPrivateKey(String privateKeyFilePath) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		List<String> lines = Files.readAllLines(Paths.get(privateKeyFilePath));
		
		byte[] encodedKeyBytes = String.join("", lines.subList(1, lines.size()-1)).getBytes();
		byte[] keyBytes = Base64.getDecoder().decode(encodedKeyBytes);
		
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
	    return KeyFactory.getInstance("DSA").generatePrivate(spec);
	}
	
	public static PublicKey loadPublicKey(String publicKeyFilePath) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		List<String> lines = Files.readAllLines(Paths.get(publicKeyFilePath));
		
		byte[] encodedKeyBytes = String.join("", lines.subList(1, lines.size()-1)).getBytes();
		byte[] keyBytes = Base64.getDecoder().decode(encodedKeyBytes);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
	    return KeyFactory.getInstance("DSA").generatePublic(spec);
	}
	
}
