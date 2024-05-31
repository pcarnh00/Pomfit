package controller;

import EJB.CanalFacadeLocal;
import EJB.PublicacionFacadeLocal;
import EJB.RecetaFacadeLocal;
import EJB.UsuarioFacadeLocal;
import modelo.Canal;
import modelo.Publicacion;
import modelo.Receta;
import modelo.Usuario;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class CanalController implements Serializable {

    private Canal canal;
    private List<Publicacion> publicacionesCanal;
    private int idCanal;

    private Integer recetaSeleccionadaId;
    private List<Integer> selectedCanalesIds = new ArrayList<>();
    private Publicacion publicacion = new Publicacion();
    private Usuario usuario;
    private List<Receta> recetasUsuario;
    private Receta recetaSeleccionada;

    @EJB
    private CanalFacadeLocal canalFacade;

    @EJB
    private PublicacionFacadeLocal publicacionEJB;

    @EJB
    private RecetaFacadeLocal recetaEJB;

    @EJB
    private UsuarioFacadeLocal usuarioEJB;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        idCanal = (int) sessionMap.get("id_canal");

        canal = canalFacade.find(idCanal);
        if (canal != null) {
            publicacionesCanal = canal.getPublicaciones();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar el canal."));
        }

        // Load user details
        int idUsuario = (int) sessionMap.get("id_usuario");
        usuario = usuarioEJB.find(idUsuario);
        recetasUsuario = recetaEJB.findByUsuario(usuario);
    }

    public void agregarPublicacion() {
        try {
            if (recetaSeleccionadaId == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "Por favor, seleccione una receta."));
                return;
            }

            publicacion.setUsuario(usuario);
            Receta recetaSeleccionada = recetaEJB.find(recetaSeleccionadaId);
            publicacion.setReceta(recetaSeleccionada);
            publicacion.setFechaYHora(new Date());

            List<Canal> canalesSeleccionados = new ArrayList<>();
            canalesSeleccionados.add(canal); // Asumiendo que siempre es el canal actual
            publicacion.setCanales(canalesSeleccionados);

            publicacionEJB.create(publicacion);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Publicación agregada correctamente."));

            // Actualizar las publicaciones del canal seleccionado
            cargarPublicacionesDelCanal();

            // Limpiar el formulario después de la inserción
            publicacion = new Publicacion();
            recetaSeleccionadaId = null;

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudo agregar la publicación: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void cargarPublicacionesDelCanal() {
        try {
            if (canal != null) {
                publicacionesCanal = publicacionEJB.findPublicacionesPorCanal(canal);
                System.out.println("Publicaciones del canal " + canal.getNombre() + ": " + publicacionesCanal.size());
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El canal seleccionado es nulo."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar las publicaciones del canal: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void verRecetaCompleta(Receta receta) {
        this.recetaSeleccionada = receta;
    }

    // Getters y Setters

    public Canal getCanal() {
        return canal;
    }

    public void setCanal(Canal canal) {
        this.canal = canal;
    }

    public List<Publicacion> getPublicacionesCanal() {
        publicacionesCanal = publicacionEJB.findPublicacionesPorCanal(canal);
        return publicacionesCanal;
    }

    public void setPublicacionesCanal(List<Publicacion> publicacionesCanal) {
        this.publicacionesCanal = publicacionesCanal;
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

    public Receta getRecetaSeleccionada() {
        return recetaSeleccionada;
    }

    public void setRecetaSeleccionada(Receta recetaSeleccionada) {
        this.recetaSeleccionada = recetaSeleccionada;
    }
}
