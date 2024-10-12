package Giftify.Giftify.Repositories;

import Giftify.Giftify.Models.Chat;
import Giftify.Giftify.Models.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {


        // Verifica si ya existe un chat entre dos perfiles (sin importar la dirección)
        @Query("SELECT c FROM Chat c WHERE (c.emisor = :emisor AND c.receptor = :receptor) OR (c.emisor = :receptor AND c.receptor = :emisor)")
        Optional<Chat> findChatBetween(@Param("emisor") Perfil emisor, @Param("receptor") Perfil receptor);


    // Método para buscar un chat entre dos perfiles
    Optional<Chat> findByEmisorAndReceptor(Perfil emisor, Perfil receptor);

    // Método para buscar un chat donde el perfil pueda ser emisor o receptor
    Optional<Chat> findByEmisorAndReceptorOrReceptorAndEmisor(Perfil emisor, Perfil receptor, Perfil receptorInverso, Perfil emisorInverso);

    @Query("SELECT c FROM Chat c WHERE c.emisor = :perfil OR c.receptor = :perfil")
    List<Chat> findChatsByPerfil(@Param("perfil") Perfil perfil);


}
