package controller;

import EJB.RecetaFacadeLocal;
import EJB.UsuarioFacadeLocal;
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
import java.util.List;
import java.util.Map;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
public class publicacionesFavoritas implements Serializable {

    private List<Publicacion> publicacionesFavoritas;
    private Receta recetaSeleccionada;

    @EJB
    private RecetaFacadeLocal recetaEJB;
    
    @EJB
    private UsuarioFacadeLocal usuarioEJB;

    @PostConstruct
    public void init() {
        cargarPublicacionesFavoritas();
    }

    private void cargarPublicacionesFavoritas() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            int idUsuario = (int) sessionMap.get("id_usuario");

            Usuario usuario = obtenerUsuarioActual();

            if (usuario != null) {
                publicacionesFavoritas = new ArrayList<>(usuario.getRecetasFavoritas());
            } else {
                publicacionesFavoritas = new ArrayList<>();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "No se encontraron recetas favoritas.");
                context.addMessage(null, message);
            }
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar las recetas favoritas: " + e.getMessage());
            context.addMessage(null, message);
            e.printStackTrace();
            publicacionesFavoritas = new ArrayList<>();
        }
    }

    public void quitarRecetaFav(Receta receta) {
        try {
            Usuario usuario = obtenerUsuarioActual();
            if (usuario != null) {
                for (Publicacion publicacion : usuario.getRecetasFavoritas()) {
                    if (publicacion.getReceta().equals(receta)) {
                        usuario.getRecetasFavoritas().remove(publicacion);
                        break;
                    }
                }
                recetaEJB.edit(receta);
                cargarPublicacionesFavoritas();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Has quitado la receta de favoritos."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo obtener el usuario actual."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo quitar la receta de favoritos: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void verRecetaCompleta(Receta receta) {
        if (receta != null) {
            this.recetaSeleccionada = receta;
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('dialogoReceta').show();");
        } else {
            this.recetaSeleccionada = null;
        }
    }

    private Usuario obtenerUsuarioActual() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        int idUsuario = (int) sessionMap.get("id_usuario");
        return usuarioEJB.find(idUsuario);
       
    }

  

    // Getters y Setters

    public List<Publicacion> getPublicacionesFavoritas() {
        return publicacionesFavoritas;
    }

    public void setPublicacionesFavoritas(List<Publicacion> publicacionesFavoritas) {
        this.publicacionesFavoritas = publicacionesFavoritas;
    }

    public Receta getRecetaSeleccionada() {
        return recetaSeleccionada;
    }

    public void setRecetaSeleccionada(Receta recetaSeleccionada) {
        this.recetaSeleccionada = recetaSeleccionada;
    }
}
