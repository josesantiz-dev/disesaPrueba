package util;

import java.util.Iterator;

import java.util.Properties;
import java.util.Set;

import java.util.Date;
import javax.mail.*;
import javax.mail.internet.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;


public class Correo {


	//private String from = "pld@aspintegraopciones.com";
    private String cc=null;
    private String bcc=null;
    private static  Logger log =  Logger.getLogger(Correo.class);
    private Session mailSession = null;
    private Transport tr=null;
    private boolean conectado = false;

    public Correo() {}

    public void conexion(InitialContext ctx) throws NamingException, MessagingException  {
    	 if (conectado)
			 	return;
       mailSession = (Session) ctx.lookup( "java:/MailSistacc" );
  	   Properties properties = mailSession.getProperties();
  	   Set<Object> keys = properties.keySet();
  	   for(Iterator<Object> i = keys.iterator(); i.hasNext();) {
	  	   String key = i.next().toString();
	  	   log.info(key + ": " + properties.get(key));
	   }
       tr = mailSession.getTransport("smtp");
       tr.connect();
       this.conectado = true;
    }

    /*
    public void conexion() {
    		 if (conectado)
    			 	return;
             try {
	              Properties props = System.getProperties();
	              props.put("mail.smtp.host", smtphost);
	              props.put("mail.smtp.auth", "false");
	              props.put("mail.smtp.port", "26");
	               // Get a Session object
	              Session mailSession = Session.getDefaultInstance(props, null);
	              //mailSession.setDebug(true);
	              tr = mailSession.getTransport("smtp");
	              tr.connect();
	              this.conectado = true;
             } catch (Exception e) {
	              e.printStackTrace();

	         }
    }
    */


    public void desconexion() {
        try {
        	if (conectado && tr != null)
        		tr.close();
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    public void msgsend(String to,  String subject, String body) {
    		try {
				  // construct the message
				  Message msg = new MimeMessage(mailSession);
				  //msg.setFrom(new InternetAddress(from));

				  msg.setRecipients(Message.RecipientType.TO,
				                    InternetAddress.parse(to, false));
				  if (  cc!=null ) {
				                   msg.setRecipients(Message.RecipientType.CC,
				                       InternetAddress.parse(cc, false));
				  }
				  if ( bcc!=null ) {
				           msg.setRecipients(Message.RecipientType.BCC,
				               InternetAddress.parse(bcc, false));
				  }
				  msg.setSubject(subject);
				  msg.setSentDate(new Date());
				  Multipart mp = new MimeMultipart();

				  MimeBodyPart contpart = new  MimeBodyPart();
				  contpart.setContent(body,"text/html");


				  mp.addBodyPart(contpart);
				  msg.setContent(mp);

				  msg.saveChanges(); // don't forget this
				  tr.sendMessage(msg, msg.getAllRecipients());

				  log.info("\nCorreo: "  + " enviado  a " + to + "  Asunto: "+ subject );
            }  catch (Exception e) {
            	log.error("envio de correo" , e);
            }
    	}


}
