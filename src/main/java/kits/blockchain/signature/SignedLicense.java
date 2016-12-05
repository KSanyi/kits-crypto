package kits.blockchain.signature;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class SignedLicense {

	public License license;
	
	public byte[] signature;

	public SignedLicense(License license, byte[] signature) {
		this.license = license;
		this.signature = signature;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(license + "\n");
		builder.append(new String(KeyFileManager.toPemFormat(signature, "SIGNATURE")));
		return builder.toString();
	}
	
	public static SignedLicense parse(String signedLicenseString) {
		List<String> lines = Arrays.asList(signedLicenseString.split("\n"));
		try {
			LocalDate expiration = LocalDate.parse(lines.get(0).split("Expiration: ")[1]);
			String hardwareId = lines.get(1).split("HardwareId: ")[1];
			String[] features = lines.get(2).split("Features: ")[1].split(", ");	
			License license = new License(expiration, hardwareId, features);
			
			byte[] signature = Base64.getDecoder().decode(String.join("", lines.subList(4, lines.size()-1)).getBytes());
			return new SignedLicense(license, signature);
		} catch(Exception ex) {
			throw new IllegalArgumentException("Could not parse license", ex);
		}
	}
	
}
