
package Giftify.Giftify.Controllers;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCodeSha1 {
    public static String SHA1 (String pass){
        try {
        MessageDigest sha1=MessageDigest.getInstance("SHA-1");
        byte [] MessageBytes = pass.getBytes();
        byte [] HashBytes = sha1.digest(MessageBytes);
        
        StringBuilder sb = new StringBuilder();
        
        for (byte b : HashBytes){
            sb.append(String.format("%02x",b));       
        }
        String sha1Hash=sb.toString();
        return sha1Hash;
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException (e);
        }
    }

   
}
