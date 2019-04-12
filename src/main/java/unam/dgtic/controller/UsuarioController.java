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
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import unam.dgtic.ejb.EmpresaFacadeLocal;
import unam.dgtic.ejb.TipoUsuarioFacadeLocal;
import unam.dgtic.ejb.UsuarioFacadeLocal;
import unam.dgtic.model.Empresa;
import unam.dgtic.model.TipoUsuario;
import unam.dgtic.model.Usuario;
import static unam.dgtic.utils.EnviarCorreo.sendMail;
import unam.dgtic.utils.GeneratePass;

/**
 *
 * @author edher
 */
@Named
@ViewScoped
public class UsuarioController implements Serializable {
    
    @EJB
    private UsuarioFacadeLocal usuarioEJB;
    @EJB
    private EmpresaFacadeLocal empresaEJB;
    @EJB
    private TipoUsuarioFacadeLocal tipoUsuarioEJB;
    @Inject
    private Usuario usuario;

    
    private List<Usuario> usuarios;
    

    private Integer selectedEmpresa;
    private Integer selectedTipoUsuario;
    private Usuario selectedUsuario;
    
    @PostConstruct  
    public void init(){
        usuario = new Usuario();
        usuarios = usuarioEJB.findAll();
    }
    
    
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edición Cancelada", ((Usuario) event.getObject()).getNombre());
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

        //Edición de empresas
        Empresa empresaEdit = new Empresa();        
        empresaEdit=empresaEJB.find(selectedUsuario.getEmpresa().getId());
        selectedUsuario.setEmpresa(empresaEdit);
        //Edición de Tipo Usuario
        TipoUsuario tipoUsuarioEdit = new TipoUsuario();
        tipoUsuarioEdit=tipoUsuarioEJB.find(selectedUsuario.getTipoUsuario().getId());
        selectedUsuario.setTipoUsuario(tipoUsuarioEdit);
        //Se ejecuta la Edición del Row
        try{
            usuarioEJB.edit(selectedUsuario);
            FacesMessage msg = new FacesMessage("Editado",((Usuario) event.getObject()).getNombre());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aviso","Error: Intente con otro valor"));
        }
    }
    public void deleteUsuario(){   
        if(selectedUsuario!=null){
            if(!selectedUsuario.isActivo()){
                try{
                    usuarioEJB.remove(selectedUsuario);
                    usuarios.remove(selectedUsuario);  
                    selectedUsuario = null;
                }
                catch(Exception e){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aviso","Error: Tiene usuarios asociados"));
                }  
            }
            else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aviso","Primero debe deshabilitar al usuario"));
            }
        }
        else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aviso","Error: No se ha elegido registro"));
        }
    }
    public void onAddNew() {
        try{
        //Busca y signa empresa
        Empresa empresaEdit = new Empresa();
        empresaEdit=empresaEJB.find(selectedEmpresa);
        //Busca y asigna tipoUsuario
        TipoUsuario tipoUsuarioEdit = new TipoUsuario();
        tipoUsuarioEdit=tipoUsuarioEJB.find(selectedTipoUsuario);
        //Se asigna a un usuario los nuevos 
        usuario.setEmpresa(empresaEdit);
        usuario.setTipoUsuario(tipoUsuarioEdit);
        usuario.setPass(GeneratePass.getPassword(
		GeneratePass.MINUSCULAS+
		GeneratePass.MAYUSCULAS+
		GeneratePass.NUMEROS,10));
        System.out.println(usuario.toString());
        sendMail(usuario.getCorreo(),"Recuperación de Password",usuario.getPass());
        usuarioEJB.create(usuario);
        usuarios.add(usuario);
        FacesMessage msg = new FacesMessage("Registro Nuevo", usuario.getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        catch(Exception e){
        }
        finally{
        }
 
    }

    public void disableUser(){
        if(selectedUsuario!=null){
            if(selectedUsuario.isActivo()){
                selectedUsuario.setActivo(false);
                usuarioEJB.edit(selectedUsuario);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","El usuario se ha deshabilitado"));

            }
            else{
                selectedUsuario.setActivo(true);
                usuarioEJB.edit(selectedUsuario);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","El usuario se ha habilitado"));
            }
        }
        else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aviso","Error: No se ha elegido registro"));
        }
    }

    public Integer getSelectedEmpresa() {
        return selectedEmpresa;
    }

    public void setSelectedEmpresa(Integer selectedEmpresa) {
        this.selectedEmpresa = selectedEmpresa;
    }

    public Integer getSelectedTipoUsuario() {
        return selectedTipoUsuario;
    }

    public void setSelectedTipoUsuario(Integer selectedTipoUsuario) {
        this.selectedTipoUsuario = selectedTipoUsuario;
    }

    public void clear(){
        //solicitudUsuario=null;
        usuario.setId(0);
        usuario.setNombre("");
        usuario.setCorreo("");
        usuario.setApellidoMaterno("");
        usuario.setApellidoPaterno("");
        usuario.setTipoUsuario(null);
        usuario.setEmpresa(null);
    }

    
}