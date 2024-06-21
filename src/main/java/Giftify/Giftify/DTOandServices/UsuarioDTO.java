package Giftify.Giftify.DTOandServices;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UsuarioDTO {

    private String mail;

    private Long id;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String mail, Long id) {
        this.mail = mail;
        this.id = id;
    }

    public UsuarioDTO(String mail) {
        this.mail = mail;
    }

    public UsuarioDTO(Long id) {
        this.id = id;
    }
}
