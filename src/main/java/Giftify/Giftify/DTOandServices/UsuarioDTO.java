
package Giftify.Giftify.DTOandServices;
public class UsuarioDTO {
    private String mail;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}