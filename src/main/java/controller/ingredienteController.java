/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import EJB.IngredientesFacadeLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.Ingredientes;

/**
 *
 * @author Miranda
 */

@Named
@ViewScoped

public class ingredienteController implements Serializable{
    private Ingredientes ingrediente; 
    
    @EJB
    private IngredientesFacadeLocal ingredienteEJB; 
    
    //Decir que es el primer método que se ejecute postconstruct
    @PostConstruct
    public void init(){
        ingrediente = new Ingredientes(); 
    }
    
    public void insertarIngrediente() {
    try {
        // Verificar si el ingrediente ya existe en la base de datos
        Ingredientes ingredienteExistente = ingredienteEJB.findByName(ingrediente.getNombre());

        if (ingredienteExistente != null) {
            // Ingrediente ya existe, mostrar mensaje de error
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El ingrediente ya existe en la base de datos."));
        } else {
            // Ingrediente no existe, proceder con la inserción
            ingredienteEJB.create(ingrediente);
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/private/CrearReceta.xhtml?faces-redirect=true");
        }
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al insertar el ingrediente en la base de datos: " + e.getMessage()));
        System.out.println("Error al insertar el ingrediente en la base de datos: " + e.getMessage());
    }
}

 
    public Ingredientes getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingredientes ingrediente) {
        this.ingrediente = ingrediente;
    }

    public IngredientesFacadeLocal getCategoriaEJB() {
        return ingredienteEJB;
    }

    public void setIngredienteEJB(IngredientesFacadeLocal ingredienteEJB) {
        this.ingredienteEJB = ingredienteEJB;
    }
    
    
    
}
