package controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import modelo.Usuario;

@Named(value = "modificarPerfilController")
@RequestScoped
public class modificarPerfilController {

    @PersistenceContext
    private EntityManager entityManager;

    private String nuevoNombre;
    private String nuevoTipoUsuario;

    public String getNuevoNombre() {
        return nuevoNombre;
    }

    public void setNuevoNombre(String nuevoNombre) {
        this.nuevoNombre = nuevoNombre;
    }

    public String getNuevoTipoUsuario() {
        return nuevoTipoUsuario;
    }

    public void setNuevoTipoUsuario(String nuevoTipoUsuario) {
        this.nuevoTipoUsuario = nuevoTipoUsuario;
    }

    public Usuario findById(int id) {
        return entityManager.find(Usuario.class, id);
    }

    @Transactional
    public void modificarDatosUsuario(int id) {
        Usuario usuario = findById(id);
        if (usuario != null) {
            boolean updated = false;
            if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
                usuario.setNombre(nuevoNombre);
                updated = true;
            }
            if (nuevoTipoUsuario != null && !nuevoTipoUsuario.isEmpty()) {
                usuario.setTipoUsuario(nuevoTipoUsuario);
                updated = true;
            }
            if (updated) {
                entityManager.merge(usuario);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioSesion", usuario);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos de usuario modificados correctamente."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se han realizado cambios."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario con ID " + id + " no fue encontrado."));
        }
    }
}