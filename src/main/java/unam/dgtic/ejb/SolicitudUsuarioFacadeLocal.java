/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.ejb;

import java.util.List;
import javax.ejb.Local;
import unam.dgtic.model.SolicitudUsuario;

/**
 *
 * @author edher
 */
@Local
public interface SolicitudUsuarioFacadeLocal {

    void create(SolicitudUsuario solicitudUsuario);

    void edit(SolicitudUsuario solicitudUsuario);

    void remove(SolicitudUsuario solicitudUsuario);

    SolicitudUsuario find(Object id);

    List<SolicitudUsuario> findAll();

    List<SolicitudUsuario> findRange(int[] range);

    int count();
    
}
