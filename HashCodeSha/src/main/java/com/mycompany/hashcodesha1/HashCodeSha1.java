
package com.mycompany.hashcodesha1;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
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

    public static void main(String[] args) {
        String user,pass;
        
        Scanner usuario = new Scanner(System.in);
        System.out.println("Ingresar su usuario\n");
        user = usuario.nextLine();
        Scanner passs = new Scanner (System.in);
        System.out.println("Ingresar su contraseña\n");
        pass = passs.nextLine();
        System.out.println("\n El usuario con nombre "+user+ "\nPosee un Hash de \n"+SHA1(pass));
    }
}
