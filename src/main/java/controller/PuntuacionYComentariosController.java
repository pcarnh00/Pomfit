package controller;
import EJB.PublicacionFacadeLocal;
import EJB.PuntuacionYComentariosFacadeLocal;
import EJB.UsuarioFacadeLocal;
import modelo.PuntuacionYComentarios;
import modelo.Publicacion;
import modelo.Usuario;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

// Import statements...

@Named(value = "puntuacionYComentariosController")
@SessionScoped
public class PuntuacionYComentariosController implements Serializable {

    @Inject
    private PuntuacionYComentariosFacadeLocal puntuacionYComentariosEJB;

    @Inject
    private UsuarioFacadeLocal usuarioEJB;

    @Inject
    private PublicacionFacadeLocal publicacionEJB;

    private int puntuacion;
    private String comentario;
    private Usuario usuarioActual;
    private Publicacion publicacionSeleccionada;
    private List<Publicacion> publicaciones; // Lista de publicaciones disponibles

    private List<PuntuacionYComentarios> comentariosDePublicacion;

    public PuntuacionYComentariosController() {
    }

    @PostConstruct
    public void init() {
        usuarioActual = obtenerUsuarioActual();
        cargarPublicaciones();
        comentariosDePublicacion = new ArrayList<>();
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public Publicacion getPublicacionSeleccionada() {
        return publicacionSeleccionada;
    }

    public void setPublicacionSeleccionada(Publicacion publicacionSeleccionada) {
        this.publicacionSeleccionada = publicacionSeleccionada;
    }

    public List<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public List<PuntuacionYComentarios> getComentariosDePublicacion() {
        return comentariosDePublicacion;
    }

    public void setComentariosDePublicacion(List<PuntuacionYComentarios> comentariosDePublicacion) {
        this.comentariosDePublicacion = comentariosDePublicacion;
    }

    private void cargarPublicaciones() {
        try {
            publicaciones = publicacionEJB.findAll();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudieron cargar las publicaciones: " + e.getMessage()));
            e.printStackTrace();
            publicaciones = new ArrayList<>();
        }
    }

    public void cargarPuntuacionesYComentariosPorPublicacion() {
        try {
            if (publicacionSeleccionada != null) {
                comentariosDePublicacion = puntuacionYComentariosEJB.obtenerPuntuacionesYComentariosPorPublicacion(publicacionSeleccionada);
            } else {
                comentariosDePublicacion = new ArrayList<>();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Advertencia", "No se ha seleccionado una publicación."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudieron cargar los comentarios: " + e.getMessage()));
            e.printStackTrace();
            comentariosDePublicacion = new ArrayList<>();
        }
    }

    private Usuario obtenerUsuarioActual() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            Integer idUsuario = (Integer) sessionMap.get("id_usuario");
            if (idUsuario != null) {
                Usuario usuario = usuarioEJB.find(idUsuario);
                return usuario;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "No se encontró el ID del usuario en la sesión."));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudo obtener el usuario actual: " + e.getMessage()));
            e.printStackTrace();
            return null;
        }
    }

    public void prepararComentarioYPuntuacion(int publicacionId) {
        publicacionSeleccionada = publicacionEJB.find(publicacionId);
        cargarPuntuacionesYComentariosPorPublicacion();
    }

    public void agregarComentarioYPuntuacion() {
        if (usuarioActual == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Usuario no autenticado."));
            return;
        }

        try {
            PuntuacionYComentarios comentarioYPuntuacion = new PuntuacionYComentarios();
            comentarioYPuntuacion.setFechaYHora(new Date());
            comentarioYPuntuacion.setPuntuacion(puntuacion);
            comentarioYPuntuacion.setComentario(comentario);
            comentarioYPuntuacion.setUsuario(usuarioActual);
            comentarioYPuntuacion.setPublicacion(publicacionSeleccionada);

            puntuacionYComentariosEJB.create(comentarioYPuntuacion);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Comentario y puntuación agregados correctamente."));

            cargarPuntuacionesYComentariosPorPublicacion();

            puntuacion = 0;
            comentario = "";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudo agregar el comentario y la puntuación: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void eliminarPuntuacionYComentario(PuntuacionYComentarios puntuacionYComentario) {
        try {
            puntuacionYComentariosEJB.remove(puntuacionYComentario);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Comentario eliminado correctamente."));

            cargarPuntuacionesYComentariosPorPublicacion();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudo eliminar el comentario."));
            e.printStackTrace();
        }
    }
    
    public boolean esSuperUsuario(PuntuacionYComentarios comentario) {
   // usuarioActual = obtenerUsuarioActual(); 
      FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            Integer idUsuario = (Integer) sessionMap.get("id_usuario");
    Usuario usuario = usuarioEJB.find(idUsuario);
                   //usuarioActual.getId() == comentario.getUsuario().getId();                         // Método para obtener el usuario actual
    if (usuario.getTipoUsuario().equals("S")){
        return true;
    }
    return false;
}
}
