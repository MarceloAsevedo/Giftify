package Giftify.Giftify.Repositories;

import Giftify.Giftify.Models.ListaDeseo;
import Giftify.Giftify.Models.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ListaDeseoRepository extends JpaRepository<ListaDeseo, Long> {
    List<ListaDeseo> findByPerfil(Perfil perfil);

    public Optional<ListaDeseo> findById(Long idLista);
}
