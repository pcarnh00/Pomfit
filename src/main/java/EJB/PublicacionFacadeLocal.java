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
public interface PublicacionFacadeLocal {

    void create(Publicacion publicacion);

    void edit(Publicacion publicacion);

    void remove(Publicacion publicacion);

    Publicacion find(Object id);

    List<Publicacion> findAll();

    List<Publicacion> findRange(int[] range);

    int count();
    public List<Publicacion> findByCanal(Canal canal);
     List<Publicacion> findByCanalId(int canalId); 
      public List<Publicacion> findPublicacionesPorCanal(Canal canal);
      public List<Integer> findPublicacionIdsByCanalId(int canalId) ;
      public List<Publicacion> findPublicacionesByIds(List<Integer> publicacionIds);
       public List<Publicacion> findPublicacionesByCanalId(int canalId);
        public List<Publicacion> findPublicacionesPorUsuario(Usuario usuario);
         public List<Integer> findPublicacionIdsByUsuario(int idUsuario);
         public List<Receta> findRecetasByPublicacionIds(List<Integer> publicacionIds);
          public List<Publicacion> publicacionesFavoritas(Usuario usuario);
}
