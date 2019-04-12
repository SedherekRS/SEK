/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.controller;


import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import unam.dgtic.ejb.TipoUsuarioFacadeLocal;
import unam.dgtic.model.TipoUsuario;

/**
 *
 * @author edher
 */
@Named
@ViewScoped
public class TipoUsuarioController implements Serializable {
    
    @EJB
    private TipoUsuarioFacadeLocal tipoUsuarioEJB;
    @Inject
    private TipoUsuario tipoUsuario;
    
    private List<TipoUsuario> tipos;
    
    private TipoUsuario selectedTipo;
    
    

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    @PostConstruct
    public void init(){
        //tipoUsuario = new TipoUsuario();
        tipos = tipoUsuarioEJB.findAll();
    }
    
    public void registrar(){
        try{
            tipoUsuarioEJB.create(tipoUsuario);
            }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<TipoUsuario> getTipos() {
        return tipos;
    }

    public void setTipos(List<TipoUsuario> tipos) {
        this.tipos = tipos;
    }

    public TipoUsuario getSelectedTipo() {
        return selectedTipo;
    }

    public void setSelectedTipo(TipoUsuario selectedTipo) {
        this.selectedTipo = selectedTipo;
    }
     public void onRowEdit(RowEditEvent event) {
        tipoUsuarioEJB.edit(selectedTipo);
        FacesMessage msg = new FacesMessage("Editado",((TipoUsuario) event.getObject()).getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edición Cancelada", ((TipoUsuario) event.getObject()).getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
   
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void deleteTipo(){   
        if(selectedTipo!=null){
            try{
                tipoUsuarioEJB.remove(selectedTipo);
                tipos.remove(selectedTipo);  
                selectedTipo = null;
            }
            catch(Exception e){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aviso","Error: Tiene usuarios asociados"));
            }   
        }
        else{
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aviso","Error: No se ha elegido registro"));
        }
    }
    public void onAddNew() {
        TipoUsuario tipoUsuarioA = new TipoUsuario();
        //tipoUsuarioA.setId(10);
        tipoUsuarioA.setNombre("Por Asignar");
        tipos.add(tipoUsuarioA);
        FacesMessage msg = new FacesMessage("Registro Nuevo", tipoUsuarioA.getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
