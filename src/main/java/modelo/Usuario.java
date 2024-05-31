package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "contrasena")
    private String contrasena;
    
    @Column(name = "correo")
    private String correo;
    
    @Column(name = "tipo_usuario")
    private String tipoUsuario;
    
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    
    @Column(name = "fecha_fin")
    private Date fechaFin;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Canal> canalesPropios; // Relación con el propietario del canal
    
    @ManyToMany(mappedBy = "usuarios", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Canal> canales; // Relación con los canales donde el usuario es miembro
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Receta> recetas;
    
      
   
      @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "receta_favorita",
        joinColumns = @JoinColumn(name = "id_usuario"),
        inverseJoinColumns = @JoinColumn(name = "id_publicacion")
    )
    private List<Publicacion> recetasFavoritas;
    
   
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PuntuacionYComentarios> puntuacionesYComentarios;

    // Constructor vacío
    public Usuario() {
    }

   

    public List<PuntuacionYComentarios> getPuntuacionesYComentarios() {
        return puntuacionesYComentarios;
    }

    // Getters y setters
    public void setPuntuacionesYComentarios(List<PuntuacionYComentarios> puntuacionesYComentarios) {
        this.puntuacionesYComentarios = puntuacionesYComentarios;
    }

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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<Canal> getCanalesPropios() {
        return canalesPropios;
    }

    public void setCanalesPropios(List<Canal> canalesPropios) {
        this.canalesPropios = canalesPropios;
    }

    public List<Canal> getCanales() {
        return canales;
    }

    public void setCanales(List<Canal> canales) {
        this.canales = canales;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }

 

    public void removeRecetaFavorita(Publicacion publicacion) {
        if (this.recetasFavoritas != null) {
            this.recetasFavoritas.remove(publicacion);
        }
    }
    
    public List<Publicacion> getRecetasFavoritas() {
        return recetasFavoritas;
    }

    public void setRecetasFavoritas(List<Publicacion> recetasFavoritas) {
        this.recetasFavoritas = recetasFavoritas;
    }

    public void addRecetaFavorita(Publicacion publicacion) {
        if (this.recetasFavoritas == null) {
            this.recetasFavoritas = new ArrayList<Publicacion>();
        }
        this.recetasFavoritas.add(publicacion);
        System.out.println("LISTA DE RECETAS FAVORITAS: "+recetasFavoritas.toString());
    }

   
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        hash = 67 * hash + Objects.hashCode(this.nombre);
        hash = 67 * hash + Objects.hashCode(this.contrasena);
        hash = 67 * hash + Objects.hashCode(this.correo);
        hash = 67 * hash + Objects.hashCode(this.tipoUsuario);
        hash = 67 * hash + Objects.hashCode(this.fechaInicio);
        hash = 67 * hash + Objects.hashCode(this.fechaFin);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.contrasena, other.contrasena)) {
            return false;
        }
        if (!Objects.equals(this.correo, other.correo)) {
            return false;
        }
        if (!Objects.equals(this.tipoUsuario, other.tipoUsuario)) {
            return false;
        }
        if (!Objects.equals(this.fechaInicio, other.fechaInicio)) {
            return false;
        }
        if (!Objects.equals(this.fechaFin, other.fechaFin)) {
            return false;
        }
        return true;
    }

    public void removeRecetaFavorita(Receta receta) {
        recetasFavoritas.remove(receta);
    }
}
