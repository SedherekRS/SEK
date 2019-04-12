/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import unam.dgtic.model.Usuario;

/**
 *
 * @author edher
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {

    @PersistenceContext(unitName = "primePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    @Override
    public Usuario iniciarSesion(Usuario us){
        Usuario usuario = null;
        String consulta;
        try{
            consulta = "FROM Usuario u WHERE u.correo = ?1 and u.pass = ?2";
            Query query= em.createQuery(consulta);
            query.setParameter(1,us.getCorreo());
            query.setParameter(2, us.getPass());
            
            List<Usuario> lista = query.getResultList();
            if(!lista.isEmpty()){
                usuario = lista.get(0);
            }
            
        }
        catch(Exception e){
            throw e;
        }

        return usuario;
    }

    @Override
    public Usuario passRetrieve(Usuario us) {
        Usuario usuario = null;
        String consulta;
        try{
            consulta = "FROM Usuario u WHERE u.correo = ?1";
            Query query= em.createQuery(consulta);
            query.setParameter(1,us.getCorreo());
           
            List<Usuario> lista = query.getResultList();
            if(!lista.isEmpty()){
                usuario = lista.get(0);
                //Lógica para enviar una contraseña generada aleatoriamente al correo registrado
            }           
        }
        catch(Exception e){
            throw e;
        }
        return usuario;
    }
}
