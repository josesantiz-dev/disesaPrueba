package net.giro.navegador.comun;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;


public class Util {
	private static ResourceBundle messageBundle = null;
    private static Locale locale = null;
    @SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(Util.class);
	public static String StackTrace(Exception exception ) {
	     StackTraceElement[] stackTrace =  exception.getStackTrace();
	     StringBuilder stackTraceBuilder = new StringBuilder();
	     for( int idx=0;stackTrace.length!=idx; idx++ ) {
	    	 stackTraceBuilder.append( stackTrace[idx].toString() );
	    	 stackTraceBuilder.append("\n");
	     }
	     return stackTraceBuilder.toString();
	}
	
    /**
     * Gets the message bundle used by the webmail applications used for
     * internationalization.
     *
     * @return message bundle for internationalization.
     */
    public static ResourceBundle getMessageBundle() {
        initMessageBundle();
        return messageBundle;
    }

    private static void initMessageBundle() {
        if (messageBundle == null) {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            // assign a default local if the faces context has none, shouldn't happen
            if (locale == null) {
                locale = Locale.ENGLISH;
            }
            System.out.println("Locale "  + locale.getDisplayLanguage());
            messageBundle = ResourceBundle.getBundle(
                    "cde.plataforma.messages",
                    locale);
        }
    }

    /**
    * Utility method for setting the error message for the loging screen.
    *
    * @param errorMessage name of properties from messages.properites resource.
    * @param formName     name of form containing error message to add.
    * @param fieldName id of component that will have message appended.
    * @param miscText text to add that is not in a message bundle, can be null
    */
    public static void addMessage(String formName,
            String errorMessage, Object params[], Severity severity) {
    	addMessage(formName,formName,errorMessage,params,severity);
    }
    
    public static void addMessage(String formName, String fieldName,
            String errorMessage, Object params[], Severity severity) {
	    	    FacesMessage message;
				@SuppressWarnings("unused")
				ResourceBundle messages = getMessageBundle();
				if (errorMessage != null){
						/*try {
							errorMessage = messages.getString(errorMessage);
						}
						// eat any errors, and just use origional message if there is a problem
						catch (MissingResourceException e) {
							if (log.isErrorEnabled())
								log.error("Missing Resource bundle, could not display message");
							}
						catch (NullPointerException e) {
								if (log.isErrorEnabled())
								  log.error("Missing Resource bundle, could not dipslay message");
						}*/
				}
				else {
						errorMessage = "";
				}
				
				if ( params!=null ) {
					MessageFormat mf = new MessageFormat(errorMessage,locale);
					errorMessage = mf.format(params,new StringBuffer(), null).toString();
				}

				FacesContext context = FacesContext.getCurrentInstance();
				message = new FacesMessage();
				message.setSeverity(severity);
				message.setDetail(errorMessage);
				message.setSummary(errorMessage);
				if ( formName.equals(fieldName) )
					context.addMessage("subPagina:pagDinamica:" + formName, message);
				else
					context.addMessage("subPagina:pagDinamica:" + formName + ":" + fieldName, message);	
	}

}
