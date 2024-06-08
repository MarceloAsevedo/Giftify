package Giftify.Giftify.Repositories;

import Giftify.Giftify.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByMailAndPassword(String mail, String password);
    Usuario findByMail(String mail);
}