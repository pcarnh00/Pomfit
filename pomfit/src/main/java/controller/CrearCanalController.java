package controller;

import EJB.CanalFacadeLocal;
import modelo.Canal;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@Named
@javax.faces.view.ViewScoped
public class CrearCanalController implements Serializable {

    private String nombreCanal;
    private String descripcionCanal;

    @EJB
    private CanalFacadeLocal canalFacade;

    public String getNombreCanal() {
        return nombreCanal;
    }

    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
    }

    public String getDescripcionCanal() {
        return descripcionCanal;
    }

    public void setDescripcionCanal(String descripcionCanal) {
        this.descripcionCanal = descripcionCanal;
    }

    public void crearCanal() {
        try {
            Canal nuevoCanal = new Canal();
            nuevoCanal.setNombre(nombreCanal);
            nuevoCanal.setDescripcion(descripcionCanal);
            canalFacade.create(nuevoCanal);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Canal creado correctamente."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo crear el canal."));
            e.printStackTrace();
        }
    }
}
