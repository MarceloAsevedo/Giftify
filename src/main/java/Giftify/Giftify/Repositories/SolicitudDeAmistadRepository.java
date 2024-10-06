package Giftify.Giftify.Repositories;

import Giftify.Giftify.Models.Perfil;
import Giftify.Giftify.Models.SolicitudDeAmistad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudDeAmistadRepository extends JpaRepository<SolicitudDeAmistad, Long> {
    Optional<SolicitudDeAmistad> findByEmisorAndReceptor(Perfil emisor, Perfil receptor);

    List<SolicitudDeAmistad> findByReceptorAndStatus(Perfil receptor, String status);  // Este método obtiene las solicitudes pendientes
}
