package kits.blockchain.signature;

public class SignedLicense {

	public License license;
	
	public String signature;

	public SignedLicense(License license, String signature) {
		this.license = license;
		this.signature = signature;
	}
	
}
