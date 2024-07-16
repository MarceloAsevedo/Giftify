
package Giftify.Giftify.DTOandServices;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicacionDTO {
        private String descripcionPublicacion;
        private LocalDateTime fechaHoraPublicado;
        private String fotoPublicacion;
        private String nombrePerfil;
        private String apellidoPerfil;
        private String fotoPerfil;

        public PublicacionDTO(String descripcionPublicacion, LocalDateTime fechaHoraPublicado, String fotoPublicacion, String nombrePerfil, String apellidoPerfil, String fotoPerfil) {
            this.descripcionPublicacion = descripcionPublicacion;
            this.fechaHoraPublicado = fechaHoraPublicado;
            this.fotoPublicacion = fotoPublicacion;
            this.nombrePerfil = nombrePerfil;
            this.apellidoPerfil = apellidoPerfil;
            this.fotoPerfil = fotoPerfil;
        }
}