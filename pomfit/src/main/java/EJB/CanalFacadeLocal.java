/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.List;
import javax.ejb.Local;
import modelo.Canal;

/**
 *
 * @author Miranda
 */
@Local
public interface CanalFacadeLocal {

    void create(Canal canal);

    void edit(Canal canal);

    void remove(Canal canal);

    Canal find(Object id);

    List<Canal> findAll();

    List<Canal> findRange(int[] range);

    int count();
    
}
