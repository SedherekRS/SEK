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
import unam.dgtic.model.Empresa;

/**
 *
 * @author edher
 */
@Stateless
public class EmpresaFacade extends AbstractFacade<Empresa> implements EmpresaFacadeLocal {

    @PersistenceContext(unitName = "primePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpresaFacade() {
        super(Empresa.class);
    }

    @Override
    public Empresa findByRfc(String rfc) {
        Empresa empresa = null;
        String consulta;
        try{
            consulta = "FROM Empresa u WHERE u.rfc = ?1";
            Query query= em.createQuery(consulta);
            query.setParameter(1,rfc);
            
            List<Empresa> lista = query.getResultList();
            if(!lista.isEmpty()){
                empresa = lista.get(0);
            }
            
        }
        catch(Exception e){
            throw e;
        }

        return empresa;
    }

    
}
