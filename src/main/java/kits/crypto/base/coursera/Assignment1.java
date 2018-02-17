package kits.crypto.base.coursera;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kits.crypto.base.FrequencyMap;
import kits.crypto.base.Util;

public class Assignment1 {

    public static void main(String[] args) throws IOException {

        List<String> encriptedMessages = Files.readAllLines(Paths.get("./testdata/coursera/assignment1.txt"));
        
        System.out.println(String.join("\n", encriptedMessages));
        
        List<byte[]> encriptedMessageBytes = encriptedMessages.stream().map(Assignment1::convertToBytes).collect(Collectors.toList());
        
        List<String> candidates = new ArrayList<>();
        
        for(int n=0;n<10;n++) {
            List<String> messagenTries = new ArrayList<>();
            for(int i=0;i<10;i++) {
                if(i != n) {
                    messagenTries.add(new String(Util.xor(encriptedMessageBytes.get(n), encriptedMessageBytes.get(i))));
                }
            }
            
            int minLength = messagenTries.stream().mapToInt(m -> m.length()).min().getAsInt();
            
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<minLength;i++) {
                Character charFound = null;
                for(int j=0;j<9;j++) {
                    char ch = messagenTries.get(j).charAt(i);
                    if(ch >= 'A' && ch <='Z') {
                        charFound = messagenTries.get(j).charAt(i); 
                    }
                }
                if(charFound != null) {
                    sb.append(charFound);
                } else {
                    sb.append("_");
                }
            }
            
            candidates.add(switchCase(sb.toString()));
            
            System.out.println(candidates.get(n));
        }
        
        byte[] keyBytes = findKey(encriptedMessageBytes, candidates);
        
        System.out.println();
        
        //byte[] keyBytes = Util.xor("We can factor the number 15 with quantum computers. We can also factor the number 15 with a Üa".getBytes(), encriptedMessageBytes.get(0));
        
        for(int i=0;i<10;i++) {
            System.out.println(new String(Util.xor(encriptedMessageBytes.get(i), keyBytes)));
        }
        
        System.out.println(new String(Util.xor(encriptedMessageBytes.get(10), keyBytes)));
    }
    
    private static byte[] findKey(List<byte[]> encriptedMessageBytes, List<String> candidates) {
        List<byte[]> keyCandidtes = new ArrayList<>();
        
        for(int i=0;i<10;i++) {
            keyCandidtes.add(Util.xor(encriptedMessageBytes.get(i), candidates.get(i).getBytes()));
        }
        
        int minLength = keyCandidtes.stream().mapToInt(m -> m.length).min().getAsInt();
        
        byte[] keyBytes = new byte[minLength];
        for(int i=0;i<minLength;i++) {
            FrequencyMap<Byte> freqs = new FrequencyMap<>();
            for(int j=0;j<10;j++) {
                freqs.put(keyCandidtes.get(j)[i]);
            }
           
            if(freqs.frequency(freqs.getMostFrequent()) > 4) {
                keyBytes[i] = freqs.getMostFrequent();
            } else {
                keyBytes[i] = 0;
            }
        }
        
        return keyBytes;
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
    
    private static String switchCase(String str) {
        StringBuilder sb = new StringBuilder();
        for(int i =0;i<str.length();i++) {
            Character ch = str.charAt(i);
            if(Character.isLowerCase(ch)) {
                sb.append(Character.toUpperCase(ch));
            } else {
                sb.append(Character.toLowerCase(ch));
            }
        }
        
        return sb.toString();
    }

}
