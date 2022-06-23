package kits.crypto.base;

public class Main {

    public static void main(String[] args) {
        
        String message = "attack at dawn";

        String crypted = "09e1c5f70a65ac519458e7e53f36";
        
        byte[] messageBytes = message.getBytes();
        byte[] cryptedBytes = convertToBytes(crypted);
        
        byte[] keyBytes = Util.xor(messageBytes, cryptedBytes);
        
        String message2 = "attack at dusk";
        byte[] message2Bytes = message2.getBytes();
        byte[] crypted2Bytes = Util.xor(message2Bytes, keyBytes);
        
        for(byte b : crypted2Bytes) {
            System.out.print(Integer.toHexString(b) + " ");
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
