/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import modelo.Canal;
import modelo.Publicacion;
import modelo.Receta;
import modelo.Usuario;

/**
 *
 * @author Miranda
 */
@Stateless
public class PublicacionFacade extends AbstractFacade<Publicacion> implements PublicacionFacadeLocal {

    @PersistenceContext(unitName = "pomfit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PublicacionFacade() {
        super(Publicacion.class);
    }
    
  public List<Publicacion> findByCanal(Canal canal) {
    try {
        // Suponiendo que tienes una consulta JPQL para buscar publicaciones por canal
        return em.createQuery("SELECT p FROM Publicacion p JOIN p.canal c WHERE c = :canal", Publicacion.class)
                 .setParameter("canal", canal.getPublicaciones())
                 .getResultList();
    } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
    
}

     
      public List<Publicacion> findByCanalId(int canalId) {
        TypedQuery<Publicacion> query = em.createQuery("SELECT p FROM Publicacion p WHERE p.canal.id = :canalId", Publicacion.class);
        query.setParameter("canalId", canalId);
        return query.getResultList();
    }
      
        public List<Publicacion> findPublicacionesPorCanal(Canal canal) {
        return em.createQuery("SELECT p FROM Publicacion p JOIN p.canal c WHERE c.id = :canalId", Publicacion.class)
                 .setParameter("canalId", canal.getId())
                 .getResultList();
    }
      
 public List<Integer> findPublicacionIdsByCanalId(int canalId) {
    try {
        TypedQuery<Integer> query = em.createQuery(
            "SELECT p.id FROM Publicacion p JOIN p.canal c WHERE c.id = :canalId", Integer.class);
        query.setParameter("canalId", canalId);
        List<Integer> publicacionIds = query.getResultList();
        
        System.out.println("IDs de publicaciones encontrados para el canal con ID " + canalId + ": " + publicacionIds);
        
        return publicacionIds;
    } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}


public List<Publicacion> findPublicacionesByIds(List<Integer> publicacionIds) {
    if (publicacionIds == null || publicacionIds.isEmpty()) {
        return new ArrayList<>();
    }
    try {
        TypedQuery<Publicacion> query = em.createQuery(
            "SELECT p FROM Publicacion p WHERE p.id IN :ids", Publicacion.class);
        query.setParameter("ids", publicacionIds);
        List<Publicacion> publicaciones = query.getResultList();
        
        System.out.println("Publicaciones encontradas para los IDs " + publicacionIds + ": " + publicaciones);
        
        return publicaciones;
    } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}



  public List<Publicacion> findPublicacionesByCanalId(int canalId) {
    List<Integer> publicacionIds = findPublicacionIdsByCanalId(canalId);
    if (publicacionIds.isEmpty()) {
        return new ArrayList<>(); // Retorna lista vacía si no hay IDs de publicaciones
    }
    return findPublicacionesByIds(publicacionIds);
}
  public List<Publicacion> findPublicacionesPorUsuario(Usuario usuario) {
        try {
            String jpql = "SELECT p FROM Publicacion p WHERE p.usuario = :usuario";
            Query query = em.createQuery(jpql);
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
  
    public List<Integer> findPublicacionIdsByUsuario(int idUsuario) {
        List<Integer> idsPublicaciones = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT p.id FROM Publicacion p WHERE p.usuario.id = :idUsuario");
            query.setParameter("idUsuario", idUsuario);
            idsPublicaciones = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idsPublicaciones;
    }
    
     public List<Receta> findRecetasByPublicacionIds(List<Integer> publicacionIds) {
        List<Receta> recetas = new ArrayList<>();
        if (publicacionIds.isEmpty()) {
            return recetas;
        }
        try {
            TypedQuery<Receta> query = em.createQuery(
                    "SELECT p.receta FROM Publicacion p WHERE p.id IN :ids", Receta.class);
            query.setParameter("ids", publicacionIds);
            recetas = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recetas;
    }

     public List<Publicacion> publicacionesFavoritas(Usuario usuario) {
        try {
            Query query = em.createQuery("SELECT p FROM Publicacion p JOIN p.usuariosQueLaFavoritan u WHERE u.id = :usuarioId");
            query.setParameter("usuarioId", usuario.getId());
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las publicaciones favoritas del usuario", e);
        }
    }

}
