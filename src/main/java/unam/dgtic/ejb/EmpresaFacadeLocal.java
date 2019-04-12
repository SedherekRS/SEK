/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.ejb;

import java.util.List;
import javax.ejb.Local;
import unam.dgtic.model.Empresa;

/**
 *
 * @author edher
 */
@Local
public interface EmpresaFacadeLocal {

    void create(Empresa empresa);

    void edit(Empresa empresa);

    void remove(Empresa empresa);

    Empresa find(Object id);
    
    Empresa findByRfc(String rfc);

    List<Empresa> findAll();

    List<Empresa> findRange(int[] range);

    int count();
    
}
