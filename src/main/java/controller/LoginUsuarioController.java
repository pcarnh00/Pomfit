/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import EJB.UsuarioFacadeLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.Usuario;

/**
 *
 * @author pauli
 */
@Named
@ViewScoped
public class LoginUsuarioController implements Serializable {
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    private Usuario usuario;

    public UsuarioFacadeLocal getUsuarioFacade() {
        return usuarioFacade;
    }

    public void setUsuarioFacade(UsuarioFacadeLocal usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }

    @PostConstruct
    public void init(){
        usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

   public void login() {
    System.out.println("Intentando iniciar sesión para el usuario: " + usuario.getNombre());

    usuario = usuarioFacade.findByUsername(usuario);
    Usuario usuarioEncontrado = usuarioFacade.findByUsername(usuario);
    

    if (usuario != null && (usuario.getTipoUsuario().equals("B") || usuario.getTipoUsuario().equals("P"))) {
        // Inicio de sesión exitoso, almacenar nombre de usuario en la sesión
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("nombreUsuario", usuario.getNombre());
        externalContext.getSessionMap().put("id_usuario", usuario.getId());
         externalContext.getSessionMap().put("usuarioSesion", usuarioEncontrado); 
        

        System.out.println("Inicio de sesión exitoso. Redirigiendo a la página principal.");
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principal.xhtml?faces-redirect=true");
    } else if(usuario != null && usuario.getTipoUsuario().equals("S")){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("nombreUsuario", usuario.getNombre());
        externalContext.getSessionMap().put("id_usuario", usuario.getId());
         externalContext.getSessionMap().put("usuarioSesion", usuarioEncontrado); 
        System.out.println("Inicio de sesión SuperUsuario");
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principalSuper.xhtml?faces-redirect=true");
    }else {
        System.out.println("Inicio de sesión fallido. Redirigiendo a la página de permisos insuficientes.");
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/public/permisosInsuficientes.xhtml?faces-redirect=true");
    }
   }
   
   public void eliminarUsuario(Usuario usuarioAEliminar) {
       System.out.println("Inicio de sesión SuperUsuario"+ usuarioAEliminar.getNombre());
       if (usuarioAEliminar == null) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario a eliminar no especificado."));
        return;
    }

    try {
        Usuario usuario = usuarioFacade.find(usuarioAEliminar.getId());
        if(usuario.getTipoUsuario().equals("S")){
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pueden eliminar usuarios de tipo SuperUsuario"));
            return;
        }else{
            if (usuario == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no existe en la base de datos."));
            return;
        }

        usuarioFacade.remove(usuario);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "El usuario se eliminó correctamente."));
        }
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el usuario."));
        System.out.println("Error al eliminar usuario: " + e.getMessage());
    }
   }
  public void redirigir() {
    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    Usuario usuarioSesion = (Usuario) externalContext.getSessionMap().get("usuarioSesion");

    if (usuarioSesion == null) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario de sesión no especificado."));
        return;
    }

    try {
        usuario = usuarioFacade.find(usuarioSesion.getId());

        if (usuario == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no existe en la base de datos."));
            return;
        }

        // Redirigir a la página correspondiente
        if ("S".equals(usuario.getTipoUsuario())) {
            System.out.println("Redirigiendo a la página principal de superusuario.");
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principalSuper.xhtml?faces-redirect=true");
        } else {
            System.out.println("Redirigiendo a la página principal.");
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principal.xhtml?faces-redirect=true");
        }
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al redirigir al usuario: " + e.getMessage()));
        System.out.println("Error al redirigir al usuario: " + e.getMessage());
    }
}

    
    public List<Usuario> getUsuariosRegistrados() {
        return usuarioFacade.findAll();
    }

}
