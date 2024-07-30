package Giftify.Giftify.DTOandServices;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeseoDTO {
    private String tituloDeseo;
    private String url;
    private String imagen;
    private String nombrePerfil;
    private String apellidoPerfil;
    private String descripcion;

    public DeseoDTO() {}

    public DeseoDTO(String tituloDeseo, String url, String imagen, String nombrePerfil, String apellidoPerfil, String descripcion) {
        this.tituloDeseo = tituloDeseo;
        this.url = url;
        this.imagen = imagen;
        this.nombrePerfil = nombrePerfil;
        this.apellidoPerfil = apellidoPerfil;
        this.descripcion = descripcion;
    }
}
