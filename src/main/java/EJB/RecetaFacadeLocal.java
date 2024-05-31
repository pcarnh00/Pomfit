/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.List;
import javax.ejb.Local;
import modelo.Ingredientes;
import modelo.Publicacion;
import modelo.Receta;
import modelo.Usuario;

/**
 *
 * @author Miranda
 */
@Local
public interface RecetaFacadeLocal {

    void create(Receta receta);

    void edit(Receta receta);

    void remove(Receta receta);

    Receta find(Object id);

    List<Receta> findAll();

    List<Receta> findRange(int[] range);

    int count();
    public List<Receta> findByUsuario(Usuario usuario);
    public boolean existeReceta(Receta receta);
    public List<Receta> recetasFavoritas(Usuario usuario) ;
    public void agregarIngrediente(Receta receta, Ingredientes ingrediente);
    public List<Publicacion> publicacionesFavoritas(Usuario usuario);
     public List<Publicacion> recetasFavoritasPorUsuario(int idUsuario);
    
}
