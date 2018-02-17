package kits.crypto.base;

public class Main {

    public static void main(String[] args) {
        
        String message = "An old friend of mine recently posted a video blog that I found to be brilliant";
        String secretK = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

        String encrypedMessage = Util.xor(message, secretK);
        
        System.out.println(encrypedMessage);
        
        String decrypedMessage = Util.xor(encrypedMessage, secretK);
        System.out.println(decrypedMessage);

        String message2 = "This is not as good as I expected it to be but I can live with it for a while.";
        String encrypedMessage2 = Util.xor(message2, secretK);
        System.out.println(Util.xor(message, message2));
        System.out.println(Util.xor(encrypedMessage, encrypedMessage2));
    }
    
}
