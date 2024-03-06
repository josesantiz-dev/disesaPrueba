package net.giro.cxc.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class StrValorValidator implements Validator {
	public void validate(FacesContext context, UIComponent comp, Object obj) throws ValidatorException {
		String valor = (String)obj;
		Pattern p = Pattern.compile("^[\\d_\\-\\w]+\\s{1}-{1}\\s{1}[\\d\\s\\w]+");
		Matcher m = p.matcher(valor);
		
		if (!m.find()) {
		   FacesMessage message = new FacesMessage();
	       message.setDetail("Debe seleccionar una Opcion de la lista");
	       message.setSummary("Debe seleccionar una Opcion de la lista");
	       message.setSeverity(FacesMessage.SEVERITY_ERROR);
	       throw new ValidatorException(message);
		}
	}
}
