package net.giro.contabilidad.validator;

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
		Logger log = Logger.getLogger(RfcPersonaFisicaValidator.class);
		
		public void validate(FacesContext context, UIComponent comp, Object obj) throws ValidatorException {
				FacesMessage message = new FacesMessage();
				FacesContext facesContext =  null;
			    Application app = null;
			    
			    ValueExpression dato = null;
				PropertyResourceBundle propPlataforma = null;
				String expresionRegular;;
				try{
					 message.setSeverity(FacesMessage.SEVERITY_ERROR);
					 
					 facesContext =  FacesContext.getCurrentInstance();
				     app = facesContext.getApplication();
				     
				     dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entornoAcc}", PropertyResourceBundle.class);
					 propPlataforma = (PropertyResourceBundle)dato.getValue(facesContext.getELContext());					 
					 expresionRegular = propPlataforma.getString("rfcF");
				        
					 dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
					 propPlataforma = (PropertyResourceBundle)dato.getValue(facesContext.getELContext());
						
						
					String valor = (String)obj;
					Pattern 	p = Pattern.compile(expresionRegular);
					Matcher m = p.matcher(valor);
					
					if(!m.find()){				
						message.setDetail(propPlataforma.getString("mensaje.validacion.rfcF"));
						message.setSummary(propPlataforma.getString("mensaje.validacion.rfcF"));
				       throw new ValidatorException(message);
					}
				} catch(ValidatorException e){
					throw e;
				} catch(Exception e){
					log.error("Error al validar el RFC de la persona Física.",e);
				    throw new ValidatorException(message);
				}
			
				
				
			}
}
