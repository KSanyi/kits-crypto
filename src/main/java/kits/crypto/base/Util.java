package kits.crypto.base;

import java.nio.charset.StandardCharsets;

public class Util {

    public static String xor(String a, String b) {
        
        Pair<String, String> equalLengthStrings = makeEqualLengthStrings(a, b);
        
        String aConverted = equalLengthStrings.first;
        String bConverted = equalLengthStrings.second;
        
        byte[] aBytes = aConverted.getBytes(StandardCharsets.US_ASCII);
        byte[] bBytes = bConverted.getBytes(StandardCharsets.US_ASCII);
        
        return new String(xor(aBytes, bBytes));
    }
    
    private static Pair<String, String> makeEqualLengthStrings(String a, String b) {
        
        int diff = Math.abs(b.length() - a.length());
        
        if(diff == 0) {
            return new Pair<>(a, b);
        } else if(a.length() < b.length()) {
            return new Pair<>(a + createSpacesString(diff) , b);
        } else {
            return new Pair<>(a , b + createSpacesString(diff));
        }
    }
    
    private static String createSpacesString(int n) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<n;i++ ) {
            sb.append(" ");
        }
        return sb.toString();
    }
    
    public static byte[] xor(byte[] a, byte[] b) {
        int minLength = Math.min(a.length, b.length);
        
        byte[] xor = new byte[minLength];
        for(int i=0;i<minLength;i++) {
            xor[i] = xor(a[i], b[i]);
        }
        
        return xor;
    }
    
    public static byte xor(byte a, byte b) {
        int aInt = (int)a;
        int bInt = (int)b;
        int xor = aInt ^ bInt;

        // convert back to byte
        return (byte)(0xff & xor);
    }
    
    
}
