package modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "publicacion")
public class Publicacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_y_hora")
    private Date fechaYHora;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "id_receta")
    private Receta receta;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "publicaciones_canal",
        joinColumns = @JoinColumn(name = "id_publicacion"),
        inverseJoinColumns = @JoinColumn(name = "id_canal")
    )
     private List<Canal> canal;
    
     /*
     @ManyToMany(mappedBy = "publicacion", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Usuario> usuarios; 
*/

       @ManyToMany(mappedBy = "recetasFavoritas", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Usuario> usuariosRecetasFav;
       
     @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<PuntuacionYComentarios> puntuacionesYComentarios;
    
      

     //private List<Canal> canales;


    // Constructor vacío
    public Publicacion() {
    }

    public List<Usuario> getUsuariosRecetasFav() {
        return usuariosRecetasFav;
    }

    public void setUsuariosRecetasFav(List<Usuario> usuariosRecetasFav) {
        this.usuariosRecetasFav = usuariosRecetasFav;
    }

  
    public List<PuntuacionYComentarios> getPuntuacionesYComentarios() {
        return puntuacionesYComentarios;
    }

    // Getters y Setters
    public void setPuntuacionesYComentarios(List<PuntuacionYComentarios> puntuacionesYComentarios) {
        this.puntuacionesYComentarios = puntuacionesYComentarios;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(Date fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public List<Canal> getCanales() {
        return canal;
    }

    public void setCanales(List<Canal> canales) {
        this.canal = canales;
    }

    public void addCanal(Canal canal) {
        if (this.canal == null) {
            this.canal = new ArrayList<>();
        }
        this.canal.add(canal);
    }

    public List<Canal> getCanal() {
        return canal;
    }

    public void setCanal(List<Canal> canal) {
        this.canal = canal;
    }
    public String toString() {
        return "Publicacion{" +
               "id=" + id +
               ", fechaYHora=" + fechaYHora +
               ", usuario=" + usuario +
               ", receta=" + receta +
               '}';
    }

    // Equals y HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publicacion that = (Publicacion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
