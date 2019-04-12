/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import unam.dgtic.ejb.UsuarioFacadeLocal;
import unam.dgtic.model.Usuario;

/**
 *
 * @author edher
 */
@Named
@ViewScoped
public class IndexController implements Serializable{
    @EJB
    private UsuarioFacadeLocal EJBUsuario;
    
    private Usuario usuario;
    
    @PostConstruct
    public void init(){
        
        usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String iniciarSesion(){
        Usuario us;
        Boolean acceso;
        String redireccion = null;
        try{
            us = EJBUsuario.iniciarSesion(usuario);
            if(us != null){
                if(us.isActivo()){
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", us);               
                    redireccion = "/protegido/principal";  
                } 
                else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Aviso:","Usuario inactivo, contacte al administrador"));
                }               
            }
            else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Aviso:","Credenciales Incorrectas"));
            }
        }
        catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Aviso:","Error"));
        }
        return redireccion;
    }
    
}
