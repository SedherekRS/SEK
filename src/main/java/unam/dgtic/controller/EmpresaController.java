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
import unam.dgtic.ejb.EmpresaFacadeLocal;
import unam.dgtic.model.Empresa;
import unam.dgtic.model.TipoUsuario;

/**
 *
 * @author edher
 */
@Named
@ViewScoped
public class EmpresaController implements Serializable {
    
    @EJB
    private EmpresaFacadeLocal empresaEJB;
    @Inject
    private Empresa empresa;
    
    private List<Empresa> empresas;
    
    private Empresa selectedEmpresa;
    
    @PostConstruct  
    public void init(){
        empresa = new Empresa();
        empresas = empresaEJB.findAll();
    }
    
    
    
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    public Empresa getSelectedEmpresa() {
        return selectedEmpresa;
    }

    public void setSelectedEmpresa(Empresa selectedEmpresa) {
        this.selectedEmpresa = selectedEmpresa;
    }
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edición Cancelada", ((Empresa) event.getObject()).getNombre());
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
     public void onRowEdit(RowEditEvent event) {
        empresaEJB.edit(selectedEmpresa);
        FacesMessage msg = new FacesMessage("Editado",((Empresa) event.getObject()).getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     public void deleteEmpresa(){   
        if(selectedEmpresa!=null){
            try{
                empresaEJB.remove(selectedEmpresa);
                empresas.remove(selectedEmpresa);  
                selectedEmpresa = null;
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
        Empresa empresaA = new Empresa();
        //tipoUsuarioA.setId(10);
        empresaA.setNombre("Por Asignar");
        empresas.add(empresaA);
        FacesMessage msg = new FacesMessage("Registro Nuevo", empresaA.getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
}