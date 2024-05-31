/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import modelo.Canal;
import modelo.Receta;
import modelo.Usuario;

/**
 *
 * @author Miranda
 */
@Stateless
public class CanalFacade extends AbstractFacade<Canal> implements CanalFacadeLocal {

    @PersistenceContext(unitName = "pomfit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CanalFacade() {
        super(Canal.class);
    }
    
    
    public List<Canal> findByUsuario(Usuario usuario) {
        TypedQuery<Canal> query = em.createQuery("SELECT c FROM Canal c JOIN c.usuarios u WHERE u.id = :userId", Canal.class);
        query.setParameter("userId", usuario.getId());
        return query.getResultList();
    }
    
     public List<Canal> findByReceta(Receta receta) {
        TypedQuery<Canal> query = em.createQuery("SELECT c FROM Canal c WHERE :receta MEMBER OF c.recetas", Canal.class);
        query.setParameter("receta", receta);
        return query.getResultList();
    }
     
    public List<Canal> obtenerCanalesDisponiblesParaReceta(Receta receta) {
        try {
            String consulta = "SELECT c FROM Canal c WHERE :receta MEMBER OF c.recetas";
            Query query = em.createQuery(consulta);
            query.setParameter("receta", receta);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
