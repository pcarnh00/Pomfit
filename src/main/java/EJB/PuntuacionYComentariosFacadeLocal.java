/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.List;
import javax.ejb.Local;
import modelo.Publicacion;
import modelo.PuntuacionYComentarios;
import modelo.Usuario;

/**
 *
 * @author Miranda
 */
@Local
public interface PuntuacionYComentariosFacadeLocal {

    void create(PuntuacionYComentarios puntuacionYComentarios);

    void edit(PuntuacionYComentarios puntuacionYComentarios);

    void remove(PuntuacionYComentarios puntuacionYComentarios);

    PuntuacionYComentarios find(Object id);

    List<PuntuacionYComentarios> findAll();

    List<PuntuacionYComentarios> findRange(int[] range);

    int count();
     public void agregarPuntuacionYComentario(PuntuacionYComentarios puntuacionYComentario);
      public List<PuntuacionYComentarios> obtenerPuntuacionesYComentariosPorPublicacion(Publicacion publicacion);
      public List<PuntuacionYComentarios> obtenerPuntuacionesYComentariosPorUsuario(Usuario usuario);
        public void eliminarPuntuacionYComentario(PuntuacionYComentarios puntuacionYComentario);
    
}
