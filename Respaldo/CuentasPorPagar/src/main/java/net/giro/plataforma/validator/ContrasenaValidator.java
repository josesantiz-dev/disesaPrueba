package net.giro.plataforma.validator;

/**
 * permite validar que la contraseña contenga de 6 a 9 cacteres y que contenga ademas por lo menos 2 letras y 2 numeros
 * @author Omar Magdiel Aguayo Garcia
 * 
 */

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

public class ContrasenaValidator implements Validator {
	Logger log = Logger.getLogger(ContrasenaValidator.class);
	
	public void validate(FacesContext context, UIComponent comp, Object obj) throws ValidatorException {
		FacesMessage message = new FacesMessage();
		FacesContext facesContext =  null;
        Application app = null;
        
		ValueExpression dato = null;
		PropertyResourceBundle propPlataforma = null;
		int numLetras = 0;
		int numNumeros = 0;
		try {
		    message.setSeverity(FacesMessage.SEVERITY_ERROR);
		    
		    facesContext =  FacesContext.getCurrentInstance();
	        app = facesContext.getApplication();
	        
			dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
			propPlataforma = (PropertyResourceBundle)dato.getValue(facesContext.getELContext());
			
		    String valor = (String)obj;
		    Pattern numero = Pattern.compile("[\\d]");
		    Matcher match = null;
		  
		    if(valor == null || valor.length() < 6 || valor.length() > 9){
		    	message.setDetail(propPlataforma.getString("mensaje.validacion.longitudPassword"));
			    message.setSummary(propPlataforma.getString("mensaje.validacion.longitudPassword"));
		    	throw new ValidatorException(message);
		    }
		  
		    for(int i=0 ; i < valor.length() ; i++){
			  match = numero.matcher(String.valueOf(valor.charAt(i)));
			  if(match.find())
				  numNumeros++;
			  else{
				  if(valor.charAt(i) != ' ')
					  numLetras++;
				  else{
					  message.setDetail(propPlataforma.getString("mensaje.validacion.espacioEnContrasena"));
					  message.setSummary(propPlataforma.getString("mensaje.validacion.espacioEnContrasena"));
				      throw new ValidatorException(message);
				  }
			  }
		    }
		  
			if(numNumeros < 2){
				message.setDetail(propPlataforma.getString("mensaje.validacion.sinNumeroPassword"));
				message.setSummary(propPlataforma.getString("mensaje.validacion.sinNumeroPassword"));
				throw new ValidatorException(message);
			}
			if(numLetras < 2){
				message.setDetail(propPlataforma.getString("mensaje.validacion.sinLetrasPassword"));
				message.setSummary(propPlataforma.getString("mensaje.validacion.sinLetrasPassword"));
				throw new ValidatorException(message);
			}
		} catch(ValidatorException e){
			throw e;
		} catch (Exception e) {
			message.setDetail(propPlataforma.getString("mensajes.error.cambiarContrasena"));
			message.setSummary(propPlataforma.getString("mensajes.error.cambiarContrasena"));
			log.error("Error al validar la contraseña ",e);
		    throw new ValidatorException(message);
		}
		
	}

}
