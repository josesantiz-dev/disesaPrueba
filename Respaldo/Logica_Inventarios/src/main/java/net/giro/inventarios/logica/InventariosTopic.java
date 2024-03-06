package net.giro.inventarios.logica;

import java.util.HashMap;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.google.gson.Gson;


@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
			propertyName = "destinationType", propertyValue = "javax.jms.Topic"), @ActivationConfigProperty(
			propertyName = "destination", propertyValue = "topic/INVENTARIOS")
		}, 
		mappedName = "topic/INVENTARIOS")
public class InventariosTopic implements MessageListener {
	private static Logger log = Logger.getLogger(InventariosTopic.class);
	//private InitialContext ctx;
	
	public InventariosTopic() {}

	@SuppressWarnings({ "unused", "unchecked" })
	public void onMessage(Message message) {
    	try {
	    	if (message instanceof TextMessage) {
				TextMessage msg = (TextMessage) message;
				Gson gson = new Gson();
				HashMap<String, String> mensaje = gson.fromJson(msg.getText(), HashMap.class);

				String evento = mensaje.get("evento").toString().toLowerCase();
				/*
				log.info("****************************** Logica Inventarios: Evento recibido: , "+new Date());
				log.info("InventariosTopic Message is : " + msg.getText());
				*/
			}
    	} catch (Exception e) {
    		log.error("Error en onMessage", e);
			e.printStackTrace();
		}
    }
}
