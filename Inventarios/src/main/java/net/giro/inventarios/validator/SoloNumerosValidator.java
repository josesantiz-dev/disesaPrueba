package net.giro.inventarios.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class SoloNumerosValidator implements Validator {
	public void validate(FacesContext context, UIComponent comp, Object obj) throws ValidatorException {
		String valor = String.valueOf(obj);
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(valor);
		if(!m.find()){
		   FacesMessage message = new FacesMessage();
	       message.setDetail("Debe capturar solo números");
	       message.setSummary("Debe capturar solo números");
	       message.setSeverity(FacesMessage.SEVERITY_ERROR);
	       throw new ValidatorException(message);
		}
		
	}
}
