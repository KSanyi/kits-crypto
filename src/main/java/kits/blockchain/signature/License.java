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
	
	byte[] getBytes() {
		StringBuilder builder = new StringBuilder();
		builder.append(expiration).append(hardwareId);
		features.stream().forEach(builder::append);
		return builder.toString().getBytes();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Expiration: " + expiration + "\n");
		builder.append("HardwareId: " + hardwareId + "\n");
		builder.append("Features: " + String.join(", ", features));
		return builder.toString();
	}
	
}
