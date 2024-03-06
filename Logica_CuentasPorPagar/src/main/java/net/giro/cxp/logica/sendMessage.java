package net.giro.cxp.logica;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxp.beans.PagosGastos;

public class sendMessage implements sendMessageRem {
	private static Logger log = Logger.getLogger(sendMessage.class);
	private Connection connection;
	private Session session;
	private MessageProducer producer;
	private Context ctx;
	private ConnectionFactory cf;
	private Destination request;
	
	public sendMessage() throws Exception {
	   ctx = new InitialContext();
	   cf = (ConnectionFactory) ctx.lookup("ConnectionFactory") ;
	   request = (Destination) ctx.lookup("queue/testQueue") ;
	}

	public void enviar(PagosGastos mc1, Long emp) {
		ObjectMessage om1;
		
		try {
			connection = cf.createConnection();
			connection.setClientID("contabilidad");
			session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE);
		    producer = session.createProducer(request);
		    connection.start();
			om1 = session.createObjectMessage(mc1);
			om1.setLongProperty("empresa", emp);
			producer.send(om1);
			if (session != null) 
				session.close();
			if (connection != null) 
				connection. close();
		} catch (JMSException e) {
			log.error("error al enviar mensaje para contabilizar", e);
		}
	}
}
