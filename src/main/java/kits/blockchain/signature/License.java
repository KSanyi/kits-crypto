package kits.blockchain.signature;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class License {

	public LocalDate expiration;
	
	public String hardwareId;
	
	public List<String> features;

	public License(LocalDate expiration, String hardwareId, String ... features) {
		this.expiration = expiration;
		this.hardwareId = hardwareId;
		this.features = Arrays.asList(features);
	}
	
}
