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
import javax.persistence.TypedQuery;
import modelo.Publicacion;
import modelo.PuntuacionYComentarios;
import modelo.Usuario;

/**
 *
 * @author Miranda
 */
@Stateless
public class PuntuacionYComentariosFacade extends AbstractFacade<PuntuacionYComentarios> implements PuntuacionYComentariosFacadeLocal {

    @PersistenceContext(unitName = "pomfit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PuntuacionYComentariosFacade() {
        super(PuntuacionYComentarios.class);
    }
  public void agregarPuntuacionYComentario(PuntuacionYComentarios puntuacionYComentario) {
        try {
            em.persist(puntuacionYComentario);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al agregar el comentario y puntuación.");
        }
    }

    public List<PuntuacionYComentarios> obtenerPuntuacionesYComentariosPorPublicacion(Publicacion publicacion) {
        try {
            TypedQuery<PuntuacionYComentarios> query = em.createQuery(
                    "SELECT pc FROM PuntuacionYComentarios pc WHERE pc.publicacion = :publicacion", PuntuacionYComentarios.class);
            query.setParameter("publicacion", publicacion);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener los comentarios y puntuaciones de la publicación.");
        }
    }

    public List<PuntuacionYComentarios> obtenerPuntuacionesYComentariosPorUsuario(Usuario usuario) {
        try {
            TypedQuery<PuntuacionYComentarios> query = em.createQuery(
                    "SELECT pc FROM PuntuacionYComentarios pc WHERE pc.usuario = :usuario", PuntuacionYComentarios.class);
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener los comentarios y puntuaciones del usuario.");
        }
    }

    public void eliminarPuntuacionYComentario(PuntuacionYComentarios puntuacionYComentario) {
        try {
            em.remove(em.contains(puntuacionYComentario) ? puntuacionYComentario : em.merge(puntuacionYComentario));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la puntuación y comentario.");
        }
    }
}