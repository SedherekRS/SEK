/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import unam.dgtic.model.SolicitudUsuario;

/**
 *
 * @author edher
 */
@Stateless
public class SolicitudUsuarioFacade extends AbstractFacade<SolicitudUsuario> implements SolicitudUsuarioFacadeLocal {

    @PersistenceContext(unitName = "primePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SolicitudUsuarioFacade() {
        super(SolicitudUsuario.class);
    }
    
}
