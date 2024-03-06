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

//Valida que la estructura del correo electrónico sea valida  xxxxx@xxxxxx.xxx
public class EmailValidator implements Validator{
	Logger log = Logger.getLogger(EmailValidator.class);

	
	public void validate(FacesContext context, UIComponent comp, Object obj) throws ValidatorException {
		FacesMessage message = new FacesMessage();
		FacesContext facesContext =  null;
        Application app = null;
        
        ValueExpression dato = null;
		PropertyResourceBundle propPlataforma = null;
		
		try {
			    message.setSeverity(FacesMessage.SEVERITY_ERROR);
			 
			    facesContext =  FacesContext.getCurrentInstance();
		        app = facesContext.getApplication();
		        
				dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
				propPlataforma = (PropertyResourceBundle)dato.getValue(facesContext.getELContext());
				
			    String valor = (String)obj;
			    Pattern pat = null;
		        Matcher mat = null;
		        
		        pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
		        mat = pat.matcher(valor);
		        
		        if (! mat.find()) {
		          /*  System.out.println("[" + mat.group() + "]");
		           // si entra aqui el email es correcto.
		        }else{*/
		        	 message.setDetail(propPlataforma.getString("mensaje.validacion.email"));
					 message.setSummary(propPlataforma.getString("mensaje.validacion.email"));
				     throw new ValidatorException(message);
		        }
		} catch(ValidatorException e){
			throw e;	
			
		}catch (Exception e) {			
			log.error("Error al validar el correo electrónico.",e);
		    throw new ValidatorException(message);
	}
	}
}
