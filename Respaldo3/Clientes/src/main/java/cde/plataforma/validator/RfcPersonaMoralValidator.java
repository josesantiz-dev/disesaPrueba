package cde.plataforma.validator;

import java.util.PropertyResourceBundle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

public class RfcPersonaMoralValidator implements Validator {
	private Logger log = Logger.getLogger(RfcPersonaMoralValidator.class);
	
	@Override
	public void validate(FacesContext context, UIComponent comp, Object obj) throws ValidatorException {
		FacesMessage message = null;
		FacesContext facesContext =  null;
	    Application app = null;
	    ValueExpression dato = null;
		PropertyResourceBundle propPlataforma = null;
		String valor = "";
		// Validacion
		String expresionRegular = "";
		Pattern p = null;
		Matcher m = null;
		boolean valido = false;
		
		try {
			facesContext =  FacesContext.getCurrentInstance();
			app = facesContext.getApplication();
			 
			dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			propPlataforma = (PropertyResourceBundle) dato.getValue(facesContext.getELContext());					 
			expresionRegular = propPlataforma.getString("rfcM");
			    
			dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
			propPlataforma = (PropertyResourceBundle)dato.getValue(facesContext.getELContext());
	
			// Inicializamos
			valor = (String) obj;
			message = new FacesMessage(propPlataforma.getString("mensaje.validacion.rfcM"), propPlataforma.getString("mensaje.validacion.rfcM"));
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			
			// Validamos
			p = Pattern.compile(expresionRegular);
			m = p.matcher(valor);
			valido = m.find();
		} catch (Exception e) {
			log.error("Error al validar el RFC para Persona Moral.", e);
			valido = false;
			message = new FacesMessage("No se pudo validar el RFC");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
		}
		
		if (! valido)
			throw new ValidatorException(message);
	}
}
