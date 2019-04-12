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
import unam.dgtic.ejb.SolicitudUsuarioFacadeLocal;
import unam.dgtic.model.SolicitudUsuario;

/**
 *
 * @author edher
 */
@Named
@ViewScoped
public class SolicitudUsuarioController implements Serializable {
    
    @EJB
    private SolicitudUsuarioFacadeLocal solicitudUsuarioEJB;
    @Inject
    private SolicitudUsuario solicitudUsuario;
    
    private List<SolicitudUsuario> solicitudes;
    
    private SolicitudUsuario selectedSolicitud;
    
    @PostConstruct  
    public void init(){
        solicitudUsuario = new SolicitudUsuario();
        solicitudes = solicitudUsuarioEJB.findAll();
    }
    
    
    
    public SolicitudUsuario getSolicitudUsuario() {
        return solicitudUsuario;
    }

    public void setSolicitudUsuario(SolicitudUsuario solicitudUsuario) {
        this.solicitudUsuario = solicitudUsuario;
    }
    
    public void registrar(){
        try{
            if(solicitudUsuario!=null){
                solicitudUsuarioEJB.create(solicitudUsuario);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","La solictud ha sido enviada"));
                clear();
            }
        }
        catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aviso","Error"));
        }
        finally{
        
        }
    }

    public List<SolicitudUsuario> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(List<SolicitudUsuario> solicitudes) {
        this.solicitudes = solicitudes;
    }

    public SolicitudUsuario getSelectedSolicitud() {
        return selectedSolicitud;
    }

    public void setSelectedSolicitud(SolicitudUsuario selectedSolicitud) {
        this.selectedSolicitud = selectedSolicitud;
    }
    public void deleteSolicitud(){   
        solicitudUsuarioEJB.remove(selectedSolicitud);      
        solicitudes.remove(selectedSolicitud);
        selectedSolicitud=null;
    
    }
    public void clear(){
        //solicitudUsuario=null;
        solicitudUsuario.setId((long)0);
        solicitudUsuario.setNombre("");
        solicitudUsuario.setCorreo("");
        solicitudUsuario.setPuesto("");
        solicitudUsuario.setInfo("");
    }
}
