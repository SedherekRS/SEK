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
import org.primefaces.PrimeFaces;
import unam.dgtic.ejb.UsuarioFacadeLocal;
import unam.dgtic.model.Usuario;
import unam.dgtic.utils.EnviarCorreo;
import static unam.dgtic.utils.EnviarCorreo.sendMail;
import unam.dgtic.utils.GeneratePass;

/**
 *
 * @author edher
 */
@Named
@ViewScoped
public class PassRetrieveController implements Serializable{
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
    
    public void passRetrieve(){
        Usuario us;
        try{
            us = EJBUsuario.passRetrieve(usuario);
            if(us != null){
                //Almacenar en la sesión de JSF
                String pass=GeneratePass.getPassword(
		GeneratePass.MINUSCULAS+
		GeneratePass.MAYUSCULAS+
		GeneratePass.NUMEROS,10);
                sendMail(us.getCorreo(),"Recuperación de Password",pass);
                us.setPass(pass);
                EJBUsuario.edit(us);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","La nueva contraseña ha sido enviada"));           
                //FacesContext.getCurrentInstance().getExternalContext().redirect(redireccion);
                PrimeFaces.current().resetInputs("form");
            }
            else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Aviso","El usuario no existe"));

                //FacesContext.getCurrentInstance().getExternalContext().redirect(redireccion);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
  
    }

}
