package controller;

import EJB.CanalFacadeLocal;
import EJB.PublicacionFacadeLocal;
import modelo.Canal;
import modelo.Publicacion;
import modelo.Usuario;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CanalController implements Serializable {

    private List<Canal> canales;
    private Canal canalSeleccionado;
    private Publicacion nuevaPublicacion;

    @EJB
    private CanalFacadeLocal canalFacade;

    @EJB
    private PublicacionFacadeLocal publicacionFacade;

    @PostConstruct
    public void init() {
        canales = canalFacade.findAll();
        nuevaPublicacion = new Publicacion();
    }

    public void agregarPublicacion() {
        if (canalSeleccionado != null && nuevaPublicacion.getReceta() != null) {
            nuevaPublicacion.setCanal(canalSeleccionado);
            publicacionFacade.create(nuevaPublicacion);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Publicación de receta agregada correctamente en el canal."));
            nuevaPublicacion = new Publicacion(); // Limpiar el objeto para futuras publicaciones
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Seleccione un canal y una receta antes de publicar."));
        }
    }
    // Getters y Setters

    public List<Canal> getCanales() {
        return canales;
    }

    public void setCanales(List<Canal> canales) {
        this.canales = canales;
    }

    public Canal getCanalSeleccionado() {
        return canalSeleccionado;
    }

    public void setCanalSeleccionado(Canal canalSeleccionado) {
        this.canalSeleccionado = canalSeleccionado;
    }

    public Publicacion getNuevaPublicacion() {
        return nuevaPublicacion;
    }

    public void setNuevaPublicacion(Publicacion nuevaPublicacion) {
        this.nuevaPublicacion = nuevaPublicacion;
    }
}
