/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author edher
 */
@FacesValidator("passValidator")
public class PassValidator implements Validator {

     private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,15}$");
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (value == null || ((String) value).isEmpty()) {
              throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password:", "Campo Vacio"));
        }
        
        if(!PATTERN.matcher((String) value).matches()) {
            System.out.println(value);
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password:", "tiene que ser de 6 a 15 caracteres"));
        }
    }
    
}
