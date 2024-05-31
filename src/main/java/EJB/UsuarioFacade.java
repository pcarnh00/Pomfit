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
import modelo.Canal;
import modelo.Publicacion;
import modelo.Receta;
import modelo.Usuario;

/**
 *
 * @author Miranda
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {

    @PersistenceContext(unitName = "pomfit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
     public Usuario findByUsername(Usuario usuario) {
        Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.nombre = :nombre and u.contrasena = :contrasena");
        query.setParameter("nombre", usuario.getNombre());
        query.setParameter("contrasena", usuario.getContrasena());
        List<Usuario> usuarios = query.getResultList();
        if (!usuarios.isEmpty()) {
            return usuarios.get(0);
        }
        return null;
    }
    
     public List<Receta> findRecetasUsuario(Usuario usuario) {
        Query query = em.createQuery("SELECT r FROM Receta r WHERE r.usuario = :usuario");
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }

   
    public List<Canal> findCanalesUsuario(Usuario usuario) {
        Query query = em.createQuery("SELECT c FROM Canal c JOIN c.usuarios u WHERE u.id = :usuarioId");
        query.setParameter("usuarioId", usuario.getId());
        return query.getResultList();
    }
     public Publicacion findPublicacion(int id) {
        return em.find(Publicacion.class, id);
    }
     
     
     
     
}
