package modelo;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "ingrediente_receta")

public class IngredienteReceta implements Serializable{
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    private Receta receta;
    
    @ManyToOne
    private Ingredientes ingrediente;
    
   
    @Column(name = "id_receta")
    private int idReceta;
    
    
    @Column(name = "id_ingrediente")
    private int idIngrediente;
    
    // Constructor sin parámetros
    public IngredienteReceta() {
        // Constructor vacío requerido por JPA
        
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public Ingredientes getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingredientes ingrediente) {
        this.ingrediente = ingrediente;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.id;
        hash = 61 * hash + Objects.hashCode(this.receta);
        hash = 61 * hash + Objects.hashCode(this.ingrediente);
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
        final IngredienteReceta other = (IngredienteReceta) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.receta, other.receta)) {
            return false;
        }
        if (!Objects.equals(this.ingrediente, other.ingrediente)) {
            return false;
        }
        return true;
    }

  
}
