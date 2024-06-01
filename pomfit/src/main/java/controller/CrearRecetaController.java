
package controller;

import EJB.IngredientesFacadeLocal;
import EJB.RecetaFacadeLocal;
import EJB.UsuarioFacadeLocal;
import modelo.Ingredientes;
import modelo.Receta;
import modelo.Usuario;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named
@ViewScoped
public class CrearRecetaController implements Serializable {
    private Receta receta;
    private Usuario usuario;
    private List<Ingredientes> ingredientesSeleccionados;
    private List<Ingredientes> listaIngredientesDisponibles;
    private int idUsuario;
    
    @EJB
    private RecetaFacadeLocal recetaEJB;

    @EJB
    private IngredientesFacadeLocal ingredientesEJB;
    
     @EJB
    private UsuarioFacadeLocal usuarioEJB;



    @PostConstruct
   
public void init() {
    System.out.println("Iniciando CrearRecetaController...");
    receta = new Receta();
    ingredientesSeleccionados = new ArrayList<>();
    listaIngredientesDisponibles = ingredientesEJB.findAll();
    
    // Obtener el ID del usuario desde la sesión
    FacesContext context = FacesContext.getCurrentInstance();
    ExternalContext externalContext = context.getExternalContext();
    Map<String, Object> sessionMap = externalContext.getSessionMap();
    idUsuario = (int) sessionMap.get("id_usuario"); // Asegúrate de que el tipo de dato sea el correcto
    
    // Obtener el objeto Usuario correspondiente al ID
    usuario = usuarioEJB.find(idUsuario);
    
    System.out.println("USUARIO CON ID: " + usuario.getId() + " NOMBRE: " + usuario.getNombre());

    if (usuario != null) {
        System.out.println("Usuario encontrado: " + usuario.getId());
    } else {
        System.out.println("El usuario no ha sido encontrado en la sesión.");
    }
}




   public void insertarReceta() {
    try {
        if (usuario!= null) { // Verifica que el objeto usuario no sea null
            // Asigna manualmente el ID del usuario a la receta
            receta.setIdUsuario(usuario.getId());
     
        } else {
            System.out.println("El objeto usuario es null. No se puede obtener su ID.");
            return; // Sale del método si el objeto usuario es null para evitar NullPointerException
        }

        // Asigna los ingredientes seleccionados a la receta
        receta.setIngredientes(ingredientesSeleccionados);
        // Persiste la receta en la base de datos
        recetaEJB.create(receta);
        System.out.println("Receta insertada correctamente.");
    } catch (Exception e) {
        System.out.println("Error al insertar receta: " + e.getMessage());
    }
}


    // Getters y Setters

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Ingredientes> getIngredientesSeleccionados() {
        return ingredientesSeleccionados;
    }

    public void setIngredientesSeleccionados(List<Ingredientes> ingredientesSeleccionados) {
        this.ingredientesSeleccionados = ingredientesSeleccionados;
    }

    public List<Ingredientes> getListaIngredientesDisponibles() {
        return listaIngredientesDisponibles;
    }

    public void setListaIngredientesDisponibles(List<Ingredientes> listaIngredientesDisponibles) {
        this.listaIngredientesDisponibles = listaIngredientesDisponibles;
    }
}