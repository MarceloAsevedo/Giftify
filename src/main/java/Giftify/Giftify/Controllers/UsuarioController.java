package Giftify.Giftify.Controllers;

import Giftify.Giftify.Models.Usuario;
import Giftify.Giftify.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/cambioPassword")
    public ResponseEntity<?> cambioPassword(@RequestBody CambioPasswordRequest cambioPasswordRequest) {
        Usuario usuario = usuarioRepository.findByMail(cambioPasswordRequest.getMail());
        
        if (usuario == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        String contraHashActual = HashCodeSha1.SHA256(cambioPasswordRequest.getPassword());
        
        if (!usuario.getPassword().equals(contraHashActual)) {
            return new ResponseEntity<>("Contraseña actual incorrecta", HttpStatus.UNAUTHORIZED);
        }

        String nuevaContraHash = HashCodeSha1.SHA256(cambioPasswordRequest.getNewPassword());
        usuario.setPassword(nuevaContraHash);
        usuarioRepository.save(usuario);

        return new ResponseEntity<>("Contraseña cambiada correctamente", HttpStatus.OK);
    }

    public static class CambioPasswordRequest {
        private String mail;
        private String password;
        private String newPassword;

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

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    
    }

}