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

public class RfcPersonaFisicaValidator implements Validator {
	private Logger log = Logger.getLogger(RfcPersonaFisicaValidator.class);
	
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
		
		try {
			facesContext =  FacesContext.getCurrentInstance();
			app = facesContext.getApplication();
			 
			dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			propPlataforma = (PropertyResourceBundle) dato.getValue(facesContext.getELContext());					 
			expresionRegular = propPlataforma.getString("rfcF");
			    
			dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
			propPlataforma = (PropertyResourceBundle)dato.getValue(facesContext.getELContext());
	
			valor = (String) obj;
			
			// Validamos
			p = Pattern.compile(expresionRegular);
			m = p.matcher(valor);
			if (! m.find()) {
				message = new FacesMessage();
				message.setSeverity(FacesMessage.SEVERITY_ERROR);		
				message.setDetail(propPlataforma.getString("mensaje.validacion.rfcF"));
				message.setSummary(propPlataforma.getString("mensaje.validacion.rfcF"));
			   throw new ValidatorException(message);
			}
		} catch (ValidatorException e) {
			log.error("Error al validar el RFC para Persona Física.", e);
		} catch(Exception e){
			log.error("Error al validar el RFC para Persona Física.", e);
		}
	}
}
