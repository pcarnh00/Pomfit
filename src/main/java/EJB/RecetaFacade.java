/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import modelo.Ingredientes;
import modelo.Publicacion;
import modelo.Receta;
import modelo.Usuario;

/**
 *
 * @author Miranda
 */
@Stateless
public class RecetaFacade extends AbstractFacade<Receta> implements RecetaFacadeLocal {

    @PersistenceContext(unitName = "pomfit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecetaFacade() {
        super(Receta.class);
    }
    
  
    public List<Receta> findByUsuario(Usuario usuario) {
        try {
            Query query = em.createQuery("SELECT r FROM Receta r WHERE r.usuario = :usuario");
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error al obtener recetas por usuario: " + e.getMessage());
            return null;
        }
    }
    
     public boolean existeReceta(Receta receta) {
        String jpql = "SELECT r FROM Receta r WHERE r.nombre = :nombre AND r.descripcion = :descripcion";
        Query query = em.createQuery(jpql);
        query.setParameter("nombre", receta.getNombre());
        query.setParameter("descripcion", receta.getDescripcion());
        List<Receta> resultados = query.getResultList();
        return !resultados.isEmpty();
    }
     
  public List<Receta> findRecetasFavoritasByUsuarioId(int usuarioId) {
    try {
        String jpql = "SELECT r FROM Receta r WHERE r.id IN (SELECT rf.receta.id FROM RecetaFavorita rf WHERE rf.usuario.id = :usuarioId)";
        TypedQuery<Receta> query = em.createQuery(jpql, Receta.class);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    } catch (Exception e) {
        // Manejar la excepción apropiadamente
        e.printStackTrace();
        return Collections.emptyList(); // Devuelve una lista vacía en caso de error
    }
}

    // Método para obtener recetas favoritas del usuario
   public List<Receta> recetasFavoritas(Usuario usuario) {
        try {
            TypedQuery<Receta> query = em.createQuery(
                    "SELECT r FROM Receta r WHERE r.id IN " +
                            "(SELECT rf.receta.id FROM RecetaFavorita rf WHERE rf.usuario.id = :usuarioId)",
                    Receta.class);
            query.setParameter("usuarioId", usuario.getId());
            return query.getResultList();
        } catch (Exception e) {
            // Log the exception or print stack trace for debugging
            e.printStackTrace();
           
        }
        return null;
   }
   
   public void agregarIngrediente(Receta receta, Ingredientes ingrediente) {
        receta.getIngredientes().add(ingrediente); // Asumiendo que Receta tiene una lista de ingredientes
        ingrediente.getRecetas().add(receta); // Asumiendo que Ingrediente tiene una lista de recetas
        em.merge(receta);
        em.merge(ingrediente);
    }
   
 public List<Publicacion> publicacionesFavoritas(Usuario usuario) {
        List<Publicacion> publicacionesFavoritas = new ArrayList<>();

        try {
            Query query = em.createQuery("SELECT p FROM Publicacion p WHERE p.usuario = :usuario");
            query.setParameter("usuario", usuario);
            publicacionesFavoritas = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return publicacionesFavoritas;
    }
 
  public List<Publicacion> recetasFavoritasPorUsuario(int idUsuario) {
        try {
            Usuario usuario = em.find(Usuario.class, idUsuario);
            if (usuario != null) {
                // Consulta para obtener las publicaciones favoritas de un usuario
                Query query = em.createQuery("SELECT p FROM Publicacion p WHERE p.usuario = :usuario");
                query.setParameter("usuario", usuario);
                return query.getResultList();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


