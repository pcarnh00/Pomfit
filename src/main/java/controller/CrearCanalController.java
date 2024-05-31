package controller;

import EJB.CanalFacadeLocal;
import EJB.UsuarioFacadeLocal;
import modelo.Canal;
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
import modelo.Publicacion;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
public class CrearCanalController implements Serializable {

    private Canal canal;
    private Usuario usuario;
    private List<Canal> canalesDisponibles;
    private List<Canal> canalesUsuario;
    private int idUsuario;

    @EJB
    private CanalFacadeLocal canalFacade;

    @EJB
    private UsuarioFacadeLocal usuarioFacade;

    @PostConstruct
   
    public void init() {
    System.out.println("Iniciando CrearCanalController...");
    canal = new Canal();
    canalesDisponibles = new ArrayList<>();
    canalesUsuario = new ArrayList<>();

    FacesContext context = FacesContext.getCurrentInstance();
    ExternalContext externalContext = context.getExternalContext();
    Map<String, Object> sessionMap = externalContext.getSessionMap();
    
    Object idUsuarioObj = sessionMap.get("id_usuario");
    if (idUsuarioObj instanceof String) {
        idUsuario = Integer.parseInt((String) idUsuarioObj);
    } else if (idUsuarioObj instanceof Integer) {
        idUsuario = (Integer) idUsuarioObj;
    } else {
        throw new IllegalArgumentException("Invalid type for id_usuario in session");
    }

    usuario = usuarioFacade.find(idUsuario);
    if (usuario != null) {
        System.out.println("Usuario encontrado: " + usuario.getId());
        cargarCanalesDisponibles();
        cargarCanalesUsuario();
    } else {
        System.out.println("El usuario no ha sido encontrado en la sesión.");
    }
}


   public void crearCanal() {
    try {
            if (usuario.getTipoUsuario().equals("B") ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Los usuarios Básicos no pueden crear canales."));
                return;
            } else if (usuario.getTipoUsuario().equals("P") && usuario.getCanales().size() >= 3) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Los usuarios premium solo pueden crear un máximo de 3 canales."));
                return;
            }

            canal.addUsuario(usuario);
            canal.setUsuario(usuario);
            canalFacade.create(canal);
            canalesUsuario.add(canal);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Éxito", "Canal creado correctamente y te has unido automáticamente."));
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/private/principal.xhtml?faces-redirect=true");
            System.out.println("Canal creado correctamente.");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "No se pudo crear el canal."));
            e.printStackTrace();
        }
}

    
    public void unirseAlCanal(Canal canal) {
        try {
            if (usuario.getTipoUsuario().equals("B") && usuario.getCanales().size()>3) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Los usuarios básicos solo pueden unirse a 3 canales"));
                return;
            } else if (usuario.getTipoUsuario().equals("P") && usuario.getCanales().size() > 10) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Los usuarios premium solo pueden unirse a un máximo de 10 canales."));
                return;
            }

            canal.getUsuarios().add(usuario);
            canalFacade.edit(canal);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Te has unido al canal."));
            cargarCanalesUsuario();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo unir al canal."));
            e.printStackTrace();
        }
    }
    
     public void salirseDelCanal(Canal canal) {
        try {
            canal.getUsuarios().remove(usuario);
            canalFacade.edit(canal);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Te has salido del canal."));
            cargarCanalesUsuario();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo salir del canal."));
            e.printStackTrace();
        }
    }

    public void cargarCanalesDisponibles() {
        try {
            canalesDisponibles = canalFacade.findAll(); // Obtener todos los canales disponibles
        } catch (Exception e) {
            System.out.println("Error al cargar los canales disponibles: " + e.getMessage());
        }
    }

    public void cargarCanalesUsuario() {
        try {
            canalesUsuario = canalFacade.findByUsuario(usuario);
        } catch (Exception e) {
            System.out.println("Error al cargar los canales del usuario: " + e.getMessage());
        }
    }
    
  public void eliminarCanal(Canal canal) {
    try {
        // Desasociar todos los usuarios del canal
        for (Usuario usuario : canal.getUsuarios()) {
            usuario.getCanales().remove(canal);
        }
        canal.getUsuarios().clear();
         this.cargarCanalesUsuario(); // Actualizar la lista de canales del usuario

        // Eliminar todas las publicaciones asociadas al canal
        /*for (Publicacion publicacion : canal.getPublicaciones()) {
            publicacion.setCanal(null);
        }
        canal.getPublicaciones().clear();
*/
        // Eliminar el canal de la base de datos
        
        canalFacade.remove(canal);
         this.cargarCanalesUsuario(); // Actualizar la lista de canales del usuario
        

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Canal eliminado correctamente."));
        
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el canal."));
        e.printStackTrace();
    }
}



      public boolean esMiCanal(Canal canal) {
        return canal.getUsuario().equals(usuario);
    }
      public  boolean esSuperUsuario(Canal canal){
          return "S".equals(usuario.getTipoUsuario());
      }
        public boolean estaUnidoAlCanal(Canal canal) {
        return canal.getUsuarios().contains(usuario);
    }

    private Usuario obtenerUsuarioActual() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        int idUsuario = (int) sessionMap.get("id_usuario");
        return usuarioFacade.find(idUsuario);
    }


    // Getters y Setters

    public Canal getCanal() {
        return canal;
    }

    public void setCanal(Canal canal) {
        this.canal = canal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Canal> getCanalesDisponibles() {
        return canalesDisponibles;
    }

    public void setCanalesDisponibles(List<Canal> canalesDisponibles) {
        this.canalesDisponibles = canalesDisponibles;
    }

    public List<Canal> getCanalesUsuario() {
        return canalesUsuario;
    }

    public void setCanalesUsuario(List<Canal> canalesUsuario) {
        this.canalesUsuario = canalesUsuario;
    }
    
   
     
     public String cargarCanal(Canal canal) {
    try {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("id_canal", canal.getId());
        return "/private/canal.xhtml?faces-redirect=true&id_canal=" + canal.getId();
    } catch (Exception e) {
        e.printStackTrace();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar el canal: " + e.getMessage()));
        return null;
    }
}


    
}
