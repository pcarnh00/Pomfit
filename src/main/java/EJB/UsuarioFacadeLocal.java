/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.List;
import javax.ejb.Local;
import modelo.Canal;
import modelo.Publicacion;
import modelo.Receta;
import modelo.Usuario;

/**
 *
 * @author Miranda
 */
@Local
public interface UsuarioFacadeLocal {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();
    public Usuario findByUsername(Usuario usuario);
     public List<Canal> findCanalesUsuario(Usuario usuario);
     public List<Receta> findRecetasUsuario(Usuario usuario);
      public Publicacion findPublicacion(int id);
      
}
