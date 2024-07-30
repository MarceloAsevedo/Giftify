package Giftify.Giftify.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "deseos")
public class Deseo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deseo")
    private Long idDeseo;

    @Column(name = "titulo_deseo", nullable = false)
    private String tituloDeseo;

    @Column(name = "url", nullable = true, length = 255)
    private String url;

    @Column(name = "imagen", nullable = true)
    private String imagen;

    @Column(name = "descripcion", nullable = true)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lista", referencedColumnName = "id_lista")
    @JsonBackReference
    private ListaDeseo listaDeseo;

    // Getters y Setters
    public Long getIdDeseo() {
        return idDeseo;
    }

    public void setIdDeseo(Long idDeseo) {
        this.idDeseo = idDeseo;
    }

    public String getTituloDeseo() {
        return tituloDeseo;
    }

    public void setTituloDeseo(String tituloDeseo) {
        this.tituloDeseo = tituloDeseo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ListaDeseo getListaDeseo() {
        return listaDeseo;
    }

    public void setListaDeseo(ListaDeseo listaDeseo) {
        this.listaDeseo = listaDeseo;
    }
}
