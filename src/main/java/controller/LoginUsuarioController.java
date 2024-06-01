package controller;

import EJB.UsuarioFacadeLocal;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import modelo.Usuario;

@Named
@ViewScoped
public class LoginUsuarioController implements Serializable {
    
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    private Usuario usuario;
    private String nuevoNombre;
    private String nuevoTipoUsuario;
    private Usuario usuarioSesion;

    @PostConstruct
    public void init() {
        usuario = new Usuario();
    }

    public UsuarioFacadeLocal getUsuarioFacade() {
        return usuarioFacade;
    }

    public void setUsuarioFacade(UsuarioFacadeLocal usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void login() {
        System.out.println("Intentando iniciar sesión para el usuario: " + usuario.getNombre());
        
        if (usuario != null) {
            Usuario usuarioEncontrado = usuarioFacade.findByUsername(usuario);

            if (usuarioEncontrado != null) {
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.getSessionMap().put("nombreUsuario", usuarioEncontrado.getNombre());
                externalContext.getSessionMap().put("id_usuario", usuarioEncontrado.getId());
                externalContext.getSessionMap().put("usuarioSesion", usuarioEncontrado);

                if (usuarioEncontrado.getTipoUsuario().equals("B") || usuarioEncontrado.getTipoUsuario().equals("P")) {
                    System.out.println("Inicio de sesión exitoso. Redirigiendo a la página principal.");
                    FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                            .handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principal.xhtml?faces-redirect=true");
                } else if (usuarioEncontrado.getTipoUsuario().equals("S")) {
                    System.out.println("Inicio de sesión SuperUsuario");
                    FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                            .handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principalSuper.xhtml?faces-redirect=true");
                } else {
                    System.out.println("Permisos insuficientes para el usuario: " + usuarioEncontrado.getNombre());
                    FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                            .handleNavigation(FacesContext.getCurrentInstance(), null, "/public/permisosInsuficientes.xhtml?faces-redirect=true");
                }
            } else {
                System.out.println("Usuario no encontrado. Redirigiendo a la página de permisos insuficientes.");
                FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                        .handleNavigation(FacesContext.getCurrentInstance(), null, "/public/permisosInsuficientes.xhtml?faces-redirect=true");
            }
        } else {
            System.out.println("Datos de usuario no proporcionados. Redirigiendo a la página de permisos insuficientes.");
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                    .handleNavigation(FacesContext.getCurrentInstance(), null, "/public/permisosInsuficientes.xhtml?faces-redirect=true");
        }
    }

    public void eliminarUsuario(Usuario usuarioAEliminar) {
        System.out.println("Inicio de sesión SuperUsuario" + usuarioAEliminar.getNombre());
        
        if (usuarioAEliminar == null) {
            return;
        }

        try {
            Usuario usuario = usuarioFacade.find(usuarioAEliminar.getId());
            if (usuario.getTipoUsuario().equals("S")) {
                return;
            } else {
                if (usuario == null) {
                    return;
                }

                usuarioFacade.remove(usuario);
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        }
    }

    public String getNuevoNombre() {
        return nuevoNombre;
    }

    public void setNuevoNombre(String nuevoNombre) {
        this.nuevoNombre = nuevoNombre;
    }
    
    public String getNuevoTipo() {
        return this.nuevoTipoUsuario;
    }

    public void setNuevoTipo(String nuevoTipo) {
        this.nuevoTipoUsuario = nuevoTipo;
    }

    public void modificarDatosUsuario(int id) {
        usuario = usuarioFacade.find(id);
        
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
                usuarioFacade.edit(usuario);
                 ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    Map<String, Object> sessionMap = externalContext.getSessionMap();
    Usuario usuario = (Usuario) sessionMap.get("usuarioSesion");
    usuario.setNombre(nuevoNombre); // suponiendo que nuevoNombre es el nuevo nombre ingresado
    sessionMap.put("usuarioSesion", usuario);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioSesion", usuario);
                FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                        .handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principal.xhtml?faces-redirect=true");
            }
        }
    }

    public void modificarNombreUsuario() {
        try {
            Usuario usuarioActual = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
            
            if (usuarioActual != null) {
                usuarioActual.setNombre(nuevoNombre);
                usuarioFacade.edit(usuarioActual);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioSesion", usuarioActual);
            }
        } catch (Exception e) {
            System.out.println("Error al modificar el nombre de usuario: " + e.getMessage());
        }
    }

    public void modificarUsuario() {
        try {
            if (usuario != null) {
                usuario.setNombre(nuevoNombre);
                usuario.setTipoUsuario(nuevoTipoUsuario);
                usuarioFacade.edit(usuario);
                FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                        .handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principal.xhtml?faces-redirect=true");
            }
        } catch (Exception e) {
            System.out.println("Error al modificar el tipo de usuario: " + e.getMessage());
        }
    }

    public void cargarUsuario() {
        usuario = usuarioFacade.find(this);
    }

    public void modificarTipoUsuario() {
        try {
            if (usuario != null) {
                usuarioFacade.edit(usuario);
            }
        } catch (Exception e) {
            System.out.println("Error al modificar el tipo de usuario: " + e.getMessage());
        }
    }

    public void redirigir() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Usuario usuarioSesion = (Usuario) externalContext.getSessionMap().get("usuarioSesion");

        if (usuarioSesion == null) {
            return;
        }

        try {
            usuario = usuarioFacade.find(usuarioSesion.getId());

            if (usuario == null) {
                return;
            }

            if ("S".equals(usuario.getTipoUsuario())) {
                System.out.println("Redirigiendo a la página principal de superusuario.");
                FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                        .handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principalSuper.xhtml?faces-redirect=true");
            } else {
                System.out.println("Redirigiendo a la página principal.");
                FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                        .handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principal.xhtml?faces-redirect=true");
            }
        } catch (Exception e) {
            System.out.println("Error al redirigir al usuario: " + e.getMessage());
        }
    }

    public List<Usuario> getUsuariosRegistrados() {
        return usuarioFacade.findAll();
    }

    public Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    public void setUsuarioSesion(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
    }
}
