
package controller;

import EJB.IngredientesFacadeLocal;
import EJB.RecetaFacadeLocal;
import EJB.UsuarioFacadeLocal;
import java.io.IOException;
import java.io.InputStream;
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
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.util.IOUtils;


@Named
@ViewScoped
public class CrearRecetaController implements Serializable {
    private Receta receta;
    private Usuario usuario;
    private List<Ingredientes> ingredientesSeleccionados;
    private List<Ingredientes> listaIngredientesDisponibles;
    private int idUsuario;
    private int idIngrediente;
    private Ingredientes ingrediente;
     private byte[] imagenReceta;
      private Ingredientes nuevoIngrediente;
     private static final int LIMITE_RECETAS_BASICO = 3;
    private static final int LIMITE_RECETAS_PREMIUM = 10;
    
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
    
    
    ingredientesSeleccionados = new ArrayList<Ingredientes>();
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
    
    cargarReceta();
}




 public void insertarReceta() {
    try {
        if (receta.getNombre() == null || receta.getNombre().isEmpty() ||
            receta.getDescripcion() == null || receta.getDescripcion().isEmpty() ||
            receta.getTiempo() <= 0 || receta.getDificultad() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Por favor, complete todos los campos de la receta."));
            return;
        }
        if (usuario == null) {
            System.out.println("El objeto usuario es null. No se puede obtener su ID.");
            return;
        }

        int numeroRecetas = recetaEJB.findByUsuario(usuario).size();
        String tipoUsuario = usuario.getTipoUsuario();

        if ((tipoUsuario.equals("B") && numeroRecetas >= LIMITE_RECETAS_BASICO) || (tipoUsuario.equals("P") && numeroRecetas >= LIMITE_RECETAS_PREMIUM)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Has alcanzado el límite de recetas permitidas para tu tipo de cuenta."));
            return;
        }

        if (recetaEJB.existeReceta(receta)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya existe una receta con los mismos atributos."));
            return;
        }

        receta.setUsuario(usuario);
        for (Ingredientes ingrediente : ingredientesSeleccionados) {
            Ingredientes ingredienteExistente = ingredientesEJB.find(ingrediente.getId());
            if (ingredienteExistente == null) {
                ingredientesEJB.create(ingrediente);
            } else {
                ingrediente = ingredienteExistente;
            }
            receta.addIngrediente(ingrediente);
        }
        recetaEJB.create(receta);

        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principal.xhtml?faces-redirect=true");
        System.out.println("Receta insertada correctamente.");
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al insertar receta: " + e.getMessage()));
        e.printStackTrace();
    }
}

     public void agregarIngrediente() {
        if (nuevoIngrediente != null && nuevoIngrediente.getNombre() != null && !nuevoIngrediente.getNombre().isEmpty()) {
            ingredientesSeleccionados.add(nuevoIngrediente);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Ingrediente agregado correctamente"));
            nuevoIngrediente = new Ingredientes(); // Limpiar el nuevo ingrediente
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar un nombre para el ingrediente"));
        }
    }
   
    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        try (InputStream input = uploadedFile.getInputStream()) {
            imagenReceta = IOUtils.toByteArray(input);
            FacesMessage message = new FacesMessage("La imagen se ha subido correctamente", uploadedFile.getFileName());
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (IOException e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al subir la imagen", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
   
    public void modificarReceta() {
        try {
            if (receta != null) {
                if (usuario != null) {
                    receta.setUsuario(usuario);
                } else {
                    System.out.println("El objeto usuario es null. No se puede obtener su ID.");
                    return;
                }

               for (Ingredientes ingrediente : ingredientesSeleccionados) {
                // Busca si el ingrediente ya existe en la base de datos
                Ingredientes ingredienteExistente = ingredientesEJB.find(ingrediente.getId());
                if (ingredienteExistente == null) {
                    // Si no existe, crea el ingrediente y luego lo asocia a la receta
                    ingredientesEJB.create(ingrediente);
                } else {
                    // Si existe, actualiza la referencia con el ingrediente existente
                    ingrediente = ingredienteExistente;
                }
                 receta.addIngrediente(ingrediente);
               }

                recetaEJB.edit(receta);

                FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                        .handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principal.xhtml?faces-redirect=true");

                System.out.println("Receta modificada correctamente.");
            } else {
                System.out.println("La receta es null. No se puede modificar.");
            }
        } catch (Exception e) {
            System.out.println("Error al modificar receta: " + e.getMessage());
        }
    }


   public void cargarReceta() {
      try {
        // Obtener el ID de la receta desde los parámetros de la URL
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        int idReceta = Integer.parseInt(params.get("id_receta"));
        
        // Buscar la receta en la base de datos
        Receta recetaModificar = recetaEJB.find(idReceta);
        
        // Verificar si se encontró la receta
        if (recetaModificar != null) {
            // Asignar la receta a la propiedad del controlador para que pueda ser utilizada en la vista
            this.receta = recetaModificar;
        } else {
            // Mostrar un mensaje de error si no se encontró la receta
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La receta no se encontró."));
        }
    } catch (NumberFormatException e) {
        // Manejar la excepción si no se proporciona un ID válido en los parámetros de la URL
       
    } catch (Exception e) {
        // Manejar otras excepciones
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error al cargar la receta."));
    }
}
   
   

   public List<Receta> getRecetasUsuario() {
    try {
        // Obtener las recetas asociadas al usuario actual desde el EJB
        return recetaEJB.findByUsuario(usuario);
    } catch (Exception e) {
        // Manejar cualquier excepción que pueda ocurrir al recuperar las recetas
        System.out.println("Error al obtener las recetas del usuario: " + e.getMessage());
        return new ArrayList<>(); // Devolver una lista vacía en caso de error
    }
}

   public void eliminarReceta(Receta receta) {
        try {
            recetaEJB.remove(receta);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "La receta y sus publicaciones se eliminaron correctamente."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la receta."));
            System.out.println("Error al eliminar receta: " + e.getMessage());
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
    
    public Ingredientes getNuevoIngrediente() {
        return nuevoIngrediente;
    }

    public void setNuevoIngrediente(Ingredientes nuevoIngrediente) {
        this.nuevoIngrediente = nuevoIngrediente;
    }

}