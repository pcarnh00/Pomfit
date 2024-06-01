package controller;

import modelo.Usuario;
import EJB.UsuarioFacadeLocal;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;


/**
 *
 * @author Miranda
 */
@Named
@ViewScoped
public class AltaUsuarioController implements Serializable {
    private Usuario usuario;
    
    

    
 
    
    @EJB
    private UsuarioFacadeLocal usuarioEJB;
    
   
    
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

  

    public UsuarioFacadeLocal getUsuarioEJB() {
        return usuarioEJB;
    }

    public void setUsuarioEJB(UsuarioFacadeLocal usuarioEJB) {
        this.usuarioEJB = usuarioEJB;
    }




    public void altaUsuario(){
        try {
            // Obtener el rol seleccionado
            
            // Establecer los datos del usuario
            usuario.setNombre(usuario.getNombre());
            usuario.setContrasena("123456"); // Contraseña por defecto, cambiar según tu política de seguridad
            usuario.setCorreo(usuario.getCorreo());
            usuario.setTipoUsuario(usuario.getTipoUsuario());
            usuario.setFechaInicio(new Date()); // Establecer la fecha de inicio como la fecha actual
            
            // Establecer la fecha de fin, puedes establecerla según tus necesidades
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, 1); // Añadir un año a la fecha actual
            usuario.setFechaFin(cal.getTime());
            
            // Insertar el usuario en la base de datos
            usuarioEJB.create(usuario);
            System.out.println("Usuario insertado correctamente en la base de datos");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Usuario registrado correctamente"));
        } catch (Exception e) {
            System.out.println("Error al insertar el usuario en la base de datos: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo registrar el usuario"));
        }
    }
    
  
}
