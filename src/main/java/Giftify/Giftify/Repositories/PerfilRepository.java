
package Giftify.Giftify.Repositories;

import Giftify.Giftify.Models.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> { //Es una interfaz de jpa
}