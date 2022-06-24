package kits.crypto.base.coursera;

import java.util.Arrays;
import java.util.List;

import kits.crypto.base.Pair;
import kits.crypto.base.Util;

public class ProblemSet2 {

    public static void main(String[] args) {
        
        List<Pair<String, String>> pairs = Arrays.asList(
                new Pair<>("290b6e3a39155d6f", "d6f491c5b645c008"),
                new Pair<>("5f67abaf5210722b", "bbe033c00bc9330e"),
                new Pair<>("9d1a4f78cb28d863", "75e5e3ea773ec3e6"),
                new Pair<>("7b50baab07640c3d", "ac343a22cea46d60"));
        
        for(Pair<String, String> pair : pairs) {
            byte[] bytes1 = convertToBytes(pair.first());
            byte[] bytes2 = convertToBytes(pair.second());
            
            byte[] res = Util.xor(bytes1, bytes2);
            
            System.out.println("");
            for(byte b : res) {
                System.out.print(Integer.toHexString(b) + " ");
            }
        }
        
    }
    
    private static byte[] convertToBytes(String hexString) {
        byte[] bytes = new byte[hexString.length()/2];
        
        for(int i=0;i<hexString.length();i=i+2) {
            String ch1 = Character.toString(hexString.charAt(i));
            String ch2 = Character.toString(hexString.charAt(i+1));
            
            int i1 = Integer.parseInt(ch1, 16);
            int i2 = Integer.parseInt(ch2, 16);
            
            int i3 = i1 * 16 + i2;
            
            bytes[i/2] = (byte)i3;
        }
            
        return bytes;
    }
    
}
