package controller;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseEvent;
import modelo.Usuario;

@Named
@RequestScoped
public class PlantillaController {

    public void verificarYMostrar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // Verificar si existe un usuario en el contexto de la sesión
        Usuario usuario = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");

        if (usuario == null) {
            // Obtener la dirección de contexto relativa
            ExternalContext externalContext = facesContext.getExternalContext();
            String contexto = externalContext.getRequestContextPath();

            // Redireccionar a una página de error de permisos insuficientes
           
        }
    }
}
