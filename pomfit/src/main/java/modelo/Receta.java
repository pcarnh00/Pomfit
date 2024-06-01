/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Miranda
 */

@Entity
@Table (name = "receta")
public class Receta implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; 
    
    @Column (name ="nombre")
    private String nombre;
    
    @Column (name = "descripcion")
    private String descripcion; 
    
    @Column (name = "tiempo")
    private int tiempo; 
    
    @Column (name = "dificultad")
    private String dificultad; 
    
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Ingredientes> ingredientes = new ArrayList<>();
    
    @JoinTable(name = "receta_ingrediente",
        joinColumns = @JoinColumn(name = "receta_id"),
        inverseJoinColumns = @JoinColumn(name = "ingrediente_id"))
    
  
    @JoinColumn(name = "id_usuario")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Usuario usuario; 

    @Column(name = "id_usuario")
    private int idUsuario;
    
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

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public List<Ingredientes> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingredientes> ingredientes) {
        this.ingredientes = ingredientes;
    }
    
   public void setIdUsuario(int idUsuario) {
    this.idUsuario = idUsuario;
}

   @Override
public int hashCode() {
    int hash = 7;
    hash = 31 * hash + this.id;
    hash = 31 * hash + Objects.hashCode(this.nombre);
    hash = 31 * hash + Objects.hashCode(this.descripcion);
    hash = 31 * hash + this.tiempo;
    hash = 31 * hash + Objects.hashCode(this.dificultad);
    hash = 31 * hash + this.idUsuario;
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
        final Receta other = (Receta) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.tiempo != other.tiempo) {
            return false;
        }
        if (this.idUsuario != other.idUsuario) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.dificultad, other.dificultad)) {
            return false;
        }
        if (!Objects.equals(this.ingredientes, other.ingredientes)) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        return true;
    }

  
    
  
    
    
    
    
}
