package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "canal")
public class Canal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToMany(mappedBy = "canal", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Publicacion> publicaciones;
    
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "miembro_canal",
               joinColumns = @JoinColumn(name = "id_canal"),
               inverseJoinColumns = @JoinColumn(name = "id_usuario"))
    private List<Usuario> usuarios = new ArrayList<>();

   
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario; // Relación con el propietario del canal

    // Constructor vacío
    public Canal() {
    }

    // Getters y Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void addUsuario(Usuario usuario) {
        if (this.usuarios == null) {
            this.usuarios = new ArrayList<>();
        }
        this.usuarios.add(usuario);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public void addPublicacion(Publicacion publicacion) {
        if (this.publicaciones == null) {
            this.publicaciones = new ArrayList<>();
        }
        this.publicaciones.add(publicacion);
    }
    
   
    public void removePublicacion(Publicacion publicacion) {
        if (this.publicaciones != null) {
            this.publicaciones.remove(publicacion);
            publicacion.getCanales().remove(this); // Asegurar que la relación bidireccional se mantenga
        }
    }

    
}
