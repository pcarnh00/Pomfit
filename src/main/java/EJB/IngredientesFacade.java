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
import modelo.Ingredientes;

/**
 *
 * @author Miranda
 */
@Stateless
public class IngredientesFacade extends AbstractFacade<Ingredientes> implements IngredientesFacadeLocal {

    @PersistenceContext(unitName = "pomfit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IngredientesFacade() {
        super(Ingredientes.class);
    }
    
    public Ingredientes findByName(String nombre) {
        TypedQuery<Ingredientes> query = em.createQuery("SELECT i FROM Ingredientes i WHERE i.nombre = :nombre", Ingredientes.class);
        query.setParameter("nombre", nombre);
        List<Ingredientes> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    
}
