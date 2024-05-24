
package Giftify.Giftify.Models;


public class Usuario {

    private int id;

    private String mail;
 
    private String password;
 
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario(int id, String mail, String password, String token) {
        this.id = id;
        this.mail = mail;
        this.password = password;
        this.token = token;
    }
     public Usuario(){}
     public Usuario(String mail, String password, String token){
        this.mail = mail;
        this.password = password;
        this.token = token;
     }
     
}
