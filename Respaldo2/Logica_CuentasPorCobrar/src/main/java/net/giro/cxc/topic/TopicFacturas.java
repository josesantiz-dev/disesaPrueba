package net.giro.cxc.topic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/FACTURAS")}, 
	mappedName = "topic/FACTURAS")*/
public class TopicFacturas implements MessageListener {
	private static Logger log = Logger.getLogger(TopicFacturas.class);
	private InitialContext ctx;
	private DataSource datasource = null;
	private Connection conn;
	private JsonObject json;
	
	
	public TopicFacturas() throws Exception {
    	try {
        	this.ctx = new InitialContext();
        } catch (Exception e) {
        	log.error("Error al crear TransaccionesTopic", e);
        	throw e;
        }
	}
	
	
	@Override
	public void onMessage(Message message) {
		TextMessage mensaje = null;
		JsonParser parser = null;
    	String jsonString = "";
		String evento = "";
		long idTarget = 0L;
		double monto = 0;
    	
    	try {
        	if (message instanceof TextMessage) {
        		mensaje = (TextMessage) message;
        		jsonString = mensaje.getText().trim();
        		
        		// Convertimos el mensaje en objeto JSON.
        		parser = new JsonParser();
    			this.json = parser.parse(jsonString).getAsJsonObject();
            	Thread.sleep(3000);
            	
            	switch (evento) {
        		case "SALDO" : backOffice_saldo(idTarget); break;
        		case "ABONO" : backOffice_abono(idTarget); break;
        		case "CANCELACION" : backOffice_cancelacion(idTarget); break;
        		case "PROVISION" : backOffice_provision(idTarget, monto); break;
        		default: break;
            	}
			}
        } catch (Exception e) {
        	guardarLog(-1L, "Error al recibir Evento TopicFacturas\n" + jsonString, e);
		} finally {
			this.closeConnection();
		}
	}

	// ------------------------------------------------------
	// EVENTOS
	// ------------------------------------------------------
	
	private void backOffice_saldo(long idFacturaPago) {
		
	}

	private void backOffice_abono(long idFacturaPago) {
		
	}

	private void backOffice_cancelacion(long idFactura) {
		
	}

	private void backOffice_provision(long idFactura, double montoProvision) {
		
	}
	
    // ----------------------------------------------------------------------
 	// METODOS PRIVADOS
 	// ----------------------------------------------------------------------

    @SuppressWarnings("unused")
	private void guardarLog(long codigoError, String mensajeError) {
    	guardarLog(codigoError, mensajeError, null);
    }
    
    private void guardarLog(long codigoError, String mensajeError, Throwable t) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	BufferedWriter out = null;
    	FileWriter fwco = null;
    	File file = null;
    	String logFileName = "";
    	String encabezado = "";
    	
    	try {
    		// Guardamos estatus en MensajeTransaccion
    		/*if (this.msgTransaccion != null && this.ifzMsgTransaccion != null) {
	    		this.msgTransaccion.setEstatusMensaje(mensajeError + " :: Etapa 3 del proceso.");
	    		this.ifzMsgTransaccion.update(this.msgTransaccion);
    		}*/
    		
    		// Guardamos estatus en TransaccionesData si corresponde
    		/*if (tdata != null) {// && this.ifzTData != null) {
    			tdata.setCodigoError(codigoError);
    			tdata.setDescripcionError(mensajeError);
    			this.ifzTData.update(tdata);
    		}*/
    		
    		// Guardamos archivo log
    		log.error(mensajeError, t);
    		logFileName = "/tmp/C-" + formatter.format(Calendar.getInstance().getTime()) + ".log";
    		file = new File(logFileName);
    		
    		if (! file.exists()) {
    			formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
    			file.createNewFile();
    			encabezado = this.getClass().getCanonicalName() 
    					+ "        " + formatter.format(Calendar.getInstance().getTime())
    					+ "\n-------------------------------------------------------------------------\n";
    		}
    		
        	fwco = new FileWriter(new File(logFileName), true);
        	out = new BufferedWriter(fwco);
        	if (! "".equals(encabezado))
        		out.write(encabezado);
        	out.write("\nMensaje:\n");
        	out.write(this.json.toString() + "\n");
    		if (t != null) {
    			out.write("\nStackTrace:\n");
    			t.printStackTrace(new PrintWriter(out));
    		} 
    		out.close();
    	} catch (Exception e) {
    		log.error("Error en Logica_Contabilidad.TopicContabilizar.guardarLog()", e);
    	}
    }

    @SuppressWarnings("unused")
    private void getConnection() throws Exception {
		try {
			if (this.conn == null) {
				if (this.ctx == null)
					this.ctx = new InitialContext();
				this.datasource = ((DataSource) this.ctx.lookup("java:/giroDS"));
				this.conn = this.datasource.getConnection();
			}
		} catch (Exception e) {
    		log.error("Error en Logica_Contabilidad.TopicTransacciones.getConnection(). Error al obtener la coneccion a giroDS", e);
		}
	}
    
    private void closeConnection() {
		try {
			if (this.conn == null)
				return;
			if (! this.conn.isClosed()) 
				this.conn.close();
			this.conn = null;
		} catch (SQLException e) {
    		log.error("Error en Logica_Contabilidad.TopicTransacciones.closeConnection()", e);
		}
	} 
}
