package controller;

import EJB.CanalFacadeLocal;
import EJB.PublicacionFacadeLocal;
import EJB.RecetaFacadeLocal;
import EJB.UsuarioFacadeLocal;
import modelo.Canal;
import modelo.Publicacion;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import modelo.Receta;
import modelo.Usuario;

@Named
@ViewScoped
public class publicacionController implements Serializable {

    private Publicacion publicacion;
    private Usuario usuario;
    private List<Receta> recetasUsuario;
    private List<Canal> canalesDisponibles;
    private Integer recetaSeleccionadaId; // ID de la receta seleccionada
    private List<Integer> selectedCanalesIds; // IDs de los canales seleccionados
    private List<Publicacion> publicacionesCanalSeleccionado;
    private Canal canalSeleccionado;
    private List<Receta> recetasFavoritas;
     private boolean recetaEnFavoritos;
      private List<Publicacion> publicaciones;

    @EJB
    private PublicacionFacadeLocal publicacionEJB;

    @EJB
    private UsuarioFacadeLocal usuarioEJB;

    @EJB
    private CanalFacadeLocal canalEJB;

    @EJB
    private RecetaFacadeLocal recetaEJB;

    @PostConstruct
    public void init() {
        System.out.println("Iniciando PublicacionController...");
        publicacion = new Publicacion();
        usuario = obtenerUsuarioActual();
        recetasUsuario = obtenerRecetasUsuario();
        canalesDisponibles = obtenerCanalesDisponibles();
        selectedCanalesIds = new ArrayList<>();
        publicacionesCanalSeleccionado = new ArrayList<>();
        cargarPublicaciones();
    }

    private Usuario obtenerUsuarioActual() {
    try {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        Object idUsuarioObj = sessionMap.get("id_usuario");
        
        if (idUsuarioObj instanceof Integer) {
            int idUsuario = (Integer) idUsuarioObj;
            return usuarioEJB.find(idUsuario);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "ID del usuario en la sesión no es un entero."));
            return null;
        }
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Error", "No se pudo obtener el usuario actual: " + e.getMessage()));
        e.printStackTrace();
        return null;
    }
}


    private List<Receta> obtenerRecetasUsuario() {
        try {
            return recetaEJB.findByUsuario(usuario);
        } catch (Exception e) {
            System.out.println("Error al obtener las recetas del usuario: " + e.getMessage());
            return new ArrayList<>(); // Devolver una lista vacía en caso de error
        }
    }

    private List<Canal> obtenerCanalesDisponibles() {
        try {
            return canalEJB.findAll();
        } catch (Exception e) {
            System.out.println("Error al obtener los canales disponibles: " + e.getMessage());
            return new ArrayList<>(); // Devolver una lista vacía en caso de error
        }
    }

    public void agregarPublicacion() {
        try {
            if (recetaSeleccionadaId == null || selectedCanalesIds.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "Por favor, seleccione una receta y al menos un canal."));
                return;
            }

            publicacion.setUsuario(usuario);
            Receta recetaSeleccionada = recetaEJB.find(recetaSeleccionadaId);
            publicacion.setReceta(recetaSeleccionada);
            publicacion.setFechaYHora(new Date());

            List<Canal> canalesSeleccionados = new ArrayList<>();
            for (Integer canalId : selectedCanalesIds) {
                Canal canal = canalEJB.find(canalId);
                canalesSeleccionados.add(canal);
            }
            publicacion.setCanales(canalesSeleccionados);

            publicacionEJB.create(publicacion);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Publicación agregada correctamente."));

            // Limpiar el formulario después de la inserción
            publicacion = new Publicacion();
            recetaSeleccionadaId = null;
            selectedCanalesIds.clear();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudo agregar la publicación: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void cargarPublicacionesYRedirigir() {
        
        try {
            if (canalSeleccionado != null) {
                publicacionesCanalSeleccionado = obtenerPublicacionesPorCanal(canalSeleccionado);
                
                System.out.println("Publicaciones del canal " + canalSeleccionado.getNombre() + ": " + publicacionesCanalSeleccionado.size());
            
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El canal seleccionado es nulo."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar las publicaciones del canal: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private List<Publicacion> obtenerPublicacionesPorCanal(Canal canal) {
        try {
            return publicacionEJB.findPublicacionesPorCanal(canal);
        } catch (Exception e) {
            System.out.println("Error al obtener las publicaciones del canal: " + e.getMessage());
            return new ArrayList<>(); // Devolver una lista vacía en caso de error
        }
    }
    public void cargarPublicaciones() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            int idUsuario = (int) sessionMap.get("id_usuario");
            Usuario usuario = usuarioEJB.find(idUsuario);
            publicaciones = publicacionEJB.findPublicacionesPorUsuario(usuario);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar las publicaciones."));
            e.printStackTrace();
            publicaciones = new ArrayList<>();
        }
    }
    
    
  public void agregarRecetaAFavoritos(Publicacion publicacion) {
        try {
            Usuario usuario = obtenerUsuarioActual();
            Publicacion publicacionDesdeBD = usuarioEJB.findPublicacion(publicacion.getId());

            if (publicacionDesdeBD != null) {
                usuario.addRecetaFavorita(publicacionDesdeBD);
                usuarioEJB.edit(usuario);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Receta agregada a favoritos correctamente."));

                // Actualizar la lista de publicaciones
                cargarPublicaciones();

            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontró la receta en la base de datos."));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar la receta en favoritos: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void quitarRecetaFav(Publicacion publicacion) {
        try {
            Usuario usuario = obtenerUsuarioActual();
            usuario.removeRecetaFavorita(publicacion);
            usuarioEJB.edit(usuario);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Has quitado la publicación de favoritos."));

            // Actualizar la lista de publicaciones
            cargarPublicaciones();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo quitar la receta de favoritos."));
            e.printStackTrace();
        }
    }
    
  public boolean esRecetaFav(Publicacion publicacion) {
        try {
            Usuario usuario = obtenerUsuarioActual();
            if (usuario != null) {
                // Cargar la Publicacion desde la base de datos
                Publicacion publicacionDesdeBD = publicacionEJB.find(publicacion.getId());
                if (publicacionDesdeBD != null) {
                    return usuario.getRecetasFavoritas().contains(publicacionDesdeBD);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
   
public List<Receta> getRecetasFavoritas() {
    try {
        // Obtener el usuario actual desde la sesión
        FacesContext context = FacesContext.getCurrentInstance();
        Usuario usuarioActual = (Usuario) context.getExternalContext().getSessionMap().get("usuarioActual");
        
        // Verificar que el usuario actual no sea nulo
        if (usuarioActual == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "No tienes recetas favoritas.");
            context.addMessage(null, message);
            return new ArrayList<>(); // Devolver una lista vacía
        }

        // Obtener las recetas favoritas del usuario actual
        recetasFavoritas = recetaEJB.recetasFavoritas(usuarioActual);
        

        if (recetasFavoritas.isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "No tienes recetas favoritas.");
            context.addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Recetas favoritas cargadas correctamente.");
            context.addMessage(null, message);
        }

    } catch (Exception e) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar las recetas favoritas: " + e.getMessage());
        context.addMessage(null, message);
        e.printStackTrace();
    }
    
    return recetasFavoritas;
}
    /*
    public void eliminarRecetaFavorita(Receta receta) {
        FacesContext context = FacesContext.getCurrentInstance();
        Usuario usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuarioActual");
        recetaEJB.eliminarRecetaFavorita(usuario, receta);
    }
*/
  
public void eliminarPublicacion(Publicacion publicacion) {
    try {
        Usuario usuario = obtenerUsuarioActual();
        Publicacion publicacionDesdeBD = publicacionEJB.find(publicacion.getId());

        if (publicacionDesdeBD != null) {
            // Verificar si el usuario es el dueño de la publicación o es superUsuario
            if (publicacionDesdeBD.getUsuario().equals(usuario) || "S".equals(usuario.getTipoUsuario())) {
                // El usuario logueado es el dueño de la publicación o es superUsuario, puede eliminarla
                publicacionEJB.remove(publicacionDesdeBD);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Publicación eliminada correctamente."));

                // Actualizar la lista de publicaciones
                cargarPublicaciones();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tienes permisos para eliminar esta publicación."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontró la publicación en la base de datos."));
        }
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la publicación: " + e.getMessage()));
        e.printStackTrace();
    }
}

public boolean esPublicacionPropia(Publicacion publicacion) {
    try {
        Usuario usuario = obtenerUsuarioActual();
        if (usuario != null) {
            // Verificar si el usuario es superUsuario
            if ("S".equals(usuario.getTipoUsuario())) {
                return true; // El superusuario puede eliminar cualquier publicación
            }

            // Si no es superUsuario, verificar si la publicación es del usuario
            Publicacion publicacionDesdeBD = publicacionEJB.find(publicacion.getId());
            if (publicacionDesdeBD != null) {
                return publicacionDesdeBD.getUsuario().equals(usuario);
            }
        }
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo verificar la propiedad de la publicación: " + e.getMessage()));
        e.printStackTrace();
    }
    
    return false;
}

    // Getters y Setters

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public List<Receta> getRecetasUsuario() {
        return recetasUsuario;
    }

    public void setRecetasUsuario(List<Receta> recetasUsuario) {
        this.recetasUsuario = recetasUsuario;
    }

    public List<Canal> getCanalesDisponibles() {
        return canalesDisponibles;
    }

    public void setCanalesDisponibles(List<Canal> canalesDisponibles) {
        this.canalesDisponibles = canalesDisponibles;
    }

    public Integer getRecetaSeleccionadaId() {
        return recetaSeleccionadaId;
    }

    public void setRecetaSeleccionadaId(Integer recetaSeleccionadaId) {
        this.recetaSeleccionadaId = recetaSeleccionadaId;
    }

    public List<Integer> getSelectedCanalesIds() {
        return selectedCanalesIds;
    }

    public void setSelectedCanalesIds(List<Integer> selectedCanalesIds) {
        this.selectedCanalesIds = selectedCanalesIds;
    }

    public List<Publicacion> getPublicacionesCanalSeleccionado() {
        return publicacionesCanalSeleccionado;
    }

    public void setPublicacionesCanalSeleccionado(List<Publicacion> publicacionesCanalSeleccionado) {
        this.publicacionesCanalSeleccionado = publicacionesCanalSeleccionado;
    }

    public Canal getCanalSeleccionado() {
        return canalSeleccionado;
    }

    public void setCanalSeleccionado(Canal canalSeleccionado) {
        this.canalSeleccionado = canalSeleccionado;
    }
    
      public boolean isRecetaEnFavoritos() {
        return recetaEnFavoritos;
    }

    public void setRecetaEnFavoritos(boolean recetaEnFavoritos) {
        this.recetaEnFavoritos = recetaEnFavoritos;
    }
    
     
   

}
