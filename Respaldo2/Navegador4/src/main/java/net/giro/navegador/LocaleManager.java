package net.giro.navegador;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
 
public class LocaleManager implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Locale locale = new Locale("en","US");

	private String formatoFecha; 
    //FacesContext.getCurrentInstance().getViewRoot().getLocale();

	private String formatoMoneda;

    public LocaleManager() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application app = facesContext.getApplication();
		ValueExpression dato = app.getExpressionFactory()
				.createValueExpression(facesContext.getELContext(), "#{msg}",
						PropertyResourceBundle.class);
		PropertyResourceBundle prop = (PropertyResourceBundle) dato
				.getValue(facesContext.getELContext());
		formatoFecha = prop.getString("formatoFecha");
		formatoMoneda = prop.getString("formatoMoneda");
    }
    
    public String getFormatoEntero() { 
    	return "####";
    }
    
    public Locale getLocale() {    	
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }


	public String getFormatoFecha() {
		return formatoFecha;
	}


	public String getFormatoMoneda() {
		return formatoMoneda;
	}

}