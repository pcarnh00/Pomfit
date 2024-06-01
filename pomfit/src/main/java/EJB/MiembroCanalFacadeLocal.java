/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.List;
import javax.ejb.Local;
import modelo.MiembroCanal;

/**
 *
 * @author Miranda
 */
@Local
public interface MiembroCanalFacadeLocal {

    void create(MiembroCanal miembroCanal);

    void edit(MiembroCanal miembroCanal);

    void remove(MiembroCanal miembroCanal);

    MiembroCanal find(Object id);

    List<MiembroCanal> findAll();

    List<MiembroCanal> findRange(int[] range);

    int count();
    
}
