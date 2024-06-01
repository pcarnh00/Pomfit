/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.List;
import javax.ejb.Local;
import modelo.IngredienteReceta;

/**
 *
 * @author Miranda
 */
@Local
public interface IngredienteRecetaFacadeLocal {

    void create(IngredienteReceta ingredienteReceta);

    void edit(IngredienteReceta ingredienteReceta);

    void remove(IngredienteReceta ingredienteReceta);

    IngredienteReceta find(Object id);

    List<IngredienteReceta> findAll();

    List<IngredienteReceta> findRange(int[] range);

    int count();
    
}
