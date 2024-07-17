
package Giftify.Giftify.Repositories;

import Giftify.Giftify.Models.Perfil;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> { //Es una interfaz de jpa

     @Query("SELECT p FROM Perfil p WHERE (p.nombre LIKE %:query% OR p.apellido LIKE %:query%) AND p.idPerfil <> :perfilId")
    List<Perfil> buscarPerfiles(@Param("query") String query, @Param("perfilId") Long perfilId);

}