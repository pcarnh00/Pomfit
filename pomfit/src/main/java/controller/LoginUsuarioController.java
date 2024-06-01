/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import EJB.UsuarioFacadeLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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

    if (usuario != null) {
        // Inicio de sesión exitoso, almacenar nombre de usuario en la sesión
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("nombreUsuario", usuario.getNombre());
        externalContext.getSessionMap().put("id_usuario", usuario.getId());

        System.out.println("Inicio de sesión exitoso. Redirigiendo a la página principal.");
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principal.xhtml?faces-redirect=true");
    } else {
        System.out.println("Inicio de sesión fallido. Redirigiendo a la página de permisos insuficientes.");
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/public/permisosInsuficientes.xhtml?faces-redirect=true");
    }
   }

}
