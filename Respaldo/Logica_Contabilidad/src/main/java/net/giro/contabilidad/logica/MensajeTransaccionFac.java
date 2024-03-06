package net.giro.contabilidad.logica;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.beans.MensajeTransaccionExt;
import net.giro.contabilidad.dao.MensajeTransaccionDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class MensajeTransaccionFac implements MensajeTransaccionRem {
	private static Logger log = Logger.getLogger(MensajeTransaccionFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private java.sql.Connection conn;
	private DataSource datasource = null;
	private MensajeTransaccionDAO ifzMensajeTransaccion;
	private ConvertExt convertidor;
	@SuppressWarnings("unused")
	private static Integer limite;
	private static String orderBy;
	//private Long estatus;
	
	public MensajeTransaccionFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try{
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzMensajeTransaccion = (MensajeTransaccionDAO) this.ctx.lookup("ejb:/Model_Contabilidad//MensajeTransaccionImp!net.giro.contabilidad.dao.MensajeTransaccionDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("MensajeTransaccionFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.MensajeTransaccionFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { 
		this.infoSesion = infoSesion; 
	}

	@Override
	public void showSystemOuts(boolean value) {
		/*this.convertidor.setMostrarSystemOut(value);*/ 
	}

	@Override
	public void limite(Integer limite) { 
		MensajeTransaccionFac.limite = limite; 
	}

	@Override
	public void orderBy(String orderBy) { 
		MensajeTransaccionFac.orderBy = orderBy; 
	}
	
	@Override
	public Long save(MensajeTransaccion entity) throws Exception {
		long idMensajeTransaccion = 0L;
		
		try {
			MensajeTransaccion entityAux = this.comprobarMensajeTransaccion(entity.getIdTransaccion(), entity.getIdOperacion());
			if (entityAux == null) { // || (entityAux != null && entityAux.getEstatus() == 1)) {
				ResultSet rs = null;
				String strQuery;
				entityAux = null;
				
				// Generamos numero POLIZA si corresponde
				if (entity.getPoliza() == null || entity.getPoliza() <= 0L) {
					entity.setPoliza(0L);
		        	strQuery = "select nextval('seq_polizas_numero') AS poliza";
		        	
		        	if (this.getConnection()) {
			        	rs = this.conn.createStatement().executeQuery(strQuery);
						if (rs.next()) {
							entity.setPoliza(rs.getLong(1));
						}
		        	}
				}

				// Generamos numero LOTE si corresponde
				if (entity.getLote() == null || entity.getLote() <= 0L) {
					entity.setLote(0L);
		        	strQuery = "select nextval('seq_lote_numero') AS lote";
		        	
		        	if (this.getConnection()) {
			        	rs = this.conn.createStatement().executeQuery(strQuery);
						if (rs.next()) {
							entity.setLote(rs.getLong(1));
						} 
		        	}
				}
				
				// Guardamos en la BD PolizasDetalles
				entity.setId(this.ifzMensajeTransaccion.save(entity));
			} else {
				try {
					// Actualizamos las propiedades del mensaje guardado con el actual y lo actualizo en la BD si corresponde
					if (actualizaPropiedades(entity, entityAux))
						this.update(entityAux);
					BeanUtils.copyProperties(entity, entityAux);
				} catch (Exception e) {
					log.info("No pude copiar el mensaje por BeanUtils, uso asignacion directa :(");
					entity = entityAux;
				}
			}

			// Enviamos mensaje a cola TRANSACCION
			enviarMensajeTransaccion(entity);
			idMensajeTransaccion = entity.getId();
			log.info("Se envio mensaje ID " + idMensajeTransaccion + " a cola de transacciones.");
			
			return idMensajeTransaccion;
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.save(MensajeTransaccion)", e);
			throw e;
		}
	}
	
	@Override
	public Long save(Long idTransaccion, Long idOperacion, Long idMoneda, String descripcionMoneda, BigDecimal importe,
			Long idPersonaReferencia, String nombrePersonaReferencia, String referencia, Long idFormaPago,
			String referenciaFormaPago, long idUsuarioCreacionRegistro, Long idSucursal, Date fechaRegistro, Long idEmpresa,
			long creadoPor, Date fechaCreacion, long anuladoPor, Date fechaAnulacion, int estatus) throws Exception {
		try {
			MensajeTransaccion entity = new MensajeTransaccion();
			
			entity.setIdTransaccion(idTransaccion);
			entity.setIdOperacion(idOperacion);
			entity.setIdMoneda(idMoneda);
			entity.setDescripcionMoneda(descripcionMoneda);
			entity.setImporte(importe);
			entity.setIdPersonaReferencia(idPersonaReferencia);
			entity.setNombrePersonaReferencia(nombrePersonaReferencia);
			entity.setReferencia(referenciaFormaPago);
			entity.setIdFormaPago(idFormaPago);
			entity.setReferenciaFormaPago(referenciaFormaPago);
			entity.setIdUsuarioCreacionRegistro(idUsuarioCreacionRegistro);
			entity.setIdSucursal(idSucursal);
			entity.setCreadoPor(creadoPor);
			entity.setFechaCreacion(fechaCreacion);
			entity.setAnuladoPor(anuladoPor);
			entity.setFechaAnulacion(fechaAnulacion);
			entity.setEstatus(estatus);
			entity.setFechaRegistro(fechaRegistro);
			entity.setIdEmpresa(idEmpresa);

			// Guardamos en la BD
			entity.setId(this.save(entity));
			
			return entity.getId();
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.guardar(Long id, Long idTransaccion, Long idRegistro, Long idMoneda, BigDecimal importe, Long idPersonaReferencia, String nombrePersonaReferencia, String referencia, Long idFormaPago, String referenciaFormaPago, long idUsuarioCreacionRegistro, Long idSucursal, Long poliza, Long lote, long creadoPor, Date fechaCreacion, long anuladoPor, Date fechaAnulacion)", e);
			throw e;
		} finally {
			this.closeConnection();
		}
	}

	@Override
	public Long save(MensajeTransaccionExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.MensajeTransaccionExtToMensajeTransaccion(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.save(MensajeTransaccionExt)", e);
			throw e;
		}
	}

	@Override
	public void update(MensajeTransaccion entity) throws Exception {
		try {
			this.ifzMensajeTransaccion.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.update(MensajeTransaccion)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void update(MensajeTransaccionExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.MensajeTransaccionExtToMensajeTransaccion(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.update(MensajeTransaccionExt)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzMensajeTransaccion.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public MensajeTransaccion findById(Long id) {
		try {
			return this.ifzMensajeTransaccion.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<MensajeTransaccion> findAll() throws Exception {
		try {
			this.ifzMensajeTransaccion.orderBy(orderBy);
			return this.ifzMensajeTransaccion.findAll();
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findAll()", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findByProperty(propertyName, value)", e);
			throw e;
		}
	}
	
	@Override
	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value, int limite) throws Exception {
		try {
			this.ifzMensajeTransaccion.orderBy(orderBy);
			return this.ifzMensajeTransaccion.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			return this.findByProperties(params, 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findByProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzMensajeTransaccion.orderBy(orderBy);
			return this.ifzMensajeTransaccion.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<MensajeTransaccion> findLikeProperty(String propertyName, String value) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findLikeProperty(propertyName, value)", e);
			throw e;
		}
	}
	
	@Override
	public List<MensajeTransaccion> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		try {
			this.ifzMensajeTransaccion.orderBy(orderBy);
			return this.ifzMensajeTransaccion.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params) throws Exception {
		try {
			return this.findLikeProperties(params, 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findLikeProperties(params)", e);
			throw e;
		}
	}
	
	@Override
	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzMensajeTransaccion.orderBy(orderBy);
			return this.ifzMensajeTransaccion.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values) throws Exception {
		try {
			return this.findInProperty(columnName, values, 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findInProperty(columnName, values)", e);
			throw e;
		}
	}
	
	@Override
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzMensajeTransaccion.orderBy(orderBy);
			return this.ifzMensajeTransaccion.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public MensajeTransaccion comprobarMensajeTransaccion(Long idTransaccion, Long idOperacion) throws Exception {
		try {
			return this.ifzMensajeTransaccion.comprobarMensajeTransaccion(idTransaccion, idOperacion);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.comprobarMensajeTransaccion(idTransaccion, idOperacion)", e);
			throw e;
		}
	}
	
	@Override
	public MensajeTransaccion convertir(MensajeTransaccionExt entityExt) throws Exception {
		return this.convertidor.MensajeTransaccionExtToMensajeTransaccion(entityExt);
	}

	@Override
	public MensajeTransaccionExt convertir(MensajeTransaccion entity) throws Exception {
		return this.convertidor.MensajeTransaccionToMensajeTransaccionExt(entity);
	}

	@Override
	public void enviarMensajeTransaccion(MensajeTransaccion entity) throws Exception {
		TopicConnectionFactory tcf = null;
		Topic topicTransacciones = null;
		MessageProducer sendtopic = null;
		TextMessage tm = null;
		Connection jmsConn = null;
		Session session = null;
		String jsonString = "";
		
		try {
			// Generamos mensaje
			jsonString = generaMensaje(entity);
			
			// Intanciamos el Topic TRANSACCIONES
			tcf = (TopicConnectionFactory) this.ctx.lookup("ConnectionFactory");
			jmsConn = tcf.createTopicConnection();
			topicTransacciones = (Topic) this.ctx.lookup("topic/TRANSACCIONES");
			session = jmsConn.createSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			jmsConn.start();
			
			// Generamos el Producer y asignamos el mensaje
			sendtopic = session.createProducer(topicTransacciones);
			tm = session.createTextMessage(jsonString);
			
			// Enviamos mensaje
			sendtopic.send(tm);
		} catch (JMSException e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.enviarTransaccion(String jsonString)", e);
			throw e;
		} catch (NamingException e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.enviarTransaccion(String jsonString)", e);
			throw e;
		} catch (Exception e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.enviarTransaccion(String jsonString)", e);
			throw e;
		} finally {
			if (jmsConn != null) {
				jmsConn.stop();
				if (session != null) 
					session.close();
				jmsConn.close();
			}
		}
	}

	private String generaMensaje(MensajeTransaccion entity) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			JsonObject json = new JsonObject();	

			json.addProperty("id", entity.getId());
			json.addProperty("idTransaccion", entity.getIdTransaccion());
			json.addProperty("idOperacion", entity.getIdOperacion());
			json.addProperty("idMoneda", entity.getIdMoneda());
			json.addProperty("descripcionMoneda", entity.getDescripcionMoneda());
			json.addProperty("importe", entity.getImporte());
			json.addProperty("idPersonaReferencia", entity.getIdPersonaReferencia());
			json.addProperty("nombrePersonaReferencia", entity.getNombrePersonaReferencia());
			json.addProperty("referencia", entity.getReferencia());
			json.addProperty("idFormaPago", entity.getIdFormaPago());
			json.addProperty("referenciaFormaPago", entity.getReferenciaFormaPago());
			json.addProperty("idUsuarioCreacionRegistro", entity.getCreadoPor());
			json.addProperty("idSucursal", entity.getIdSucursal());
			json.addProperty("poliza", entity.getPoliza());
			json.addProperty("lote", entity.getLote());
			json.addProperty("creadoPor", entity.getCreadoPor());
			json.addProperty("anuladoPor", 0);
			json.addProperty("estatus", entity.getEstatus());
			json.addProperty("idEmpresa", entity.getIdEmpresa());
			
			if (entity.getFechaCreacion() != null) 
				json.addProperty("fechaCreacion", formatter.format(entity.getFechaCreacion()));
			else 
				json.addProperty("fechaCreacion", formatter.format(Calendar.getInstance().getTime()));
			
			if (entity.getFechaAnulacion() != null) 
				json.addProperty("fechaAnulacion", formatter.format(entity.getFechaAnulacion()));
			else 
				json.addProperty("fechaAnulacion", "");
			
			if (entity.getFechaRegistro() != null) 
				json.addProperty("fechaRegistro", formatter.format(entity.getFechaRegistro()));
			else 
				json.addProperty("fechaRegistro", "");
			
			log.info("|---> MENSAJE TRANSACCION :: " + json.toString());
			
			return json.toString();
		} catch (Exception e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.generaMensaje(entity)", e);
			throw e;
		}
	}
	
    private boolean getConnection() throws Exception {
		try {
			if (this.conn == null) {
				if (this.ctx == null)
					this.ctx = new InitialContext();
				this.datasource = ((DataSource) this.ctx.lookup("java:/giroDS"));
				this.conn = this.datasource.getConnection();
			}
		} catch (Exception e) {
			log.error("Error al obtener la coneccion a giroDS", e);
			e.printStackTrace();
			throw e;
		}
		
		return true;
	}
    
    private void closeConnection() {
		try {
			if (this.conn == null)
				return;
				
			if (! this.conn.isClosed()) 
				this.conn.close();
				
			this.conn = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    private boolean actualizaPropiedades(MensajeTransaccion msgActual, MensajeTransaccion msgStored) {
    	boolean cambio = false;
    	
    	try {
			if (! msgStored.getIdTransaccion().equals(msgActual.getIdTransaccion()) && msgActual.getIdTransaccion() != null) {
				msgStored.setIdTransaccion(msgActual.getIdTransaccion());
				cambio = true;
			}

			if (! msgStored.getIdOperacion().equals(msgActual.getIdOperacion()) && msgActual.getIdOperacion() != null) {
				msgStored.setIdOperacion(msgActual.getIdOperacion());
				cambio = true;
			}

			if (! msgStored.getIdMoneda().equals(msgActual.getIdMoneda()) && msgActual.getIdMoneda() != null) {
				msgStored.setIdMoneda(msgActual.getIdMoneda());
				cambio = true;
			}

			if (! msgStored.getDescripcionMoneda().equals(msgActual.getDescripcionMoneda()) && msgActual.getDescripcionMoneda() != null) {
				msgStored.setDescripcionMoneda(msgActual.getDescripcionMoneda());
				cambio = true;
			}

			if (! msgStored.getImporte().equals(msgActual.getImporte()) && msgActual.getImporte() != null) {
				msgStored.setImporte(msgActual.getImporte());
				cambio = true;
			}

			if (! msgStored.getIdPersonaReferencia().equals(msgActual.getIdPersonaReferencia()) && msgActual.getIdPersonaReferencia() != null) {
				msgStored.setIdPersonaReferencia(msgActual.getIdPersonaReferencia());
				cambio = true;
			}

			if (! msgStored.getNombrePersonaReferencia().equals(msgActual.getNombrePersonaReferencia()) && msgActual.getNombrePersonaReferencia() != null) {
				msgStored.setNombrePersonaReferencia(msgActual.getNombrePersonaReferencia());
				cambio = true;
			}

			if (! msgStored.getReferenciaFormaPago().equals(msgActual.getReferenciaFormaPago()) && msgActual.getReferenciaFormaPago() != null) {
				msgStored.setReferencia(msgActual.getReferenciaFormaPago());
				cambio = true;
			}

			if (! msgStored.getIdFormaPago().equals(msgActual.getIdFormaPago()) && msgActual.getIdFormaPago() != null) {
				msgStored.setIdFormaPago(msgActual.getIdFormaPago());
				cambio = true;
			}

			if (! msgStored.getReferenciaFormaPago().equals(msgActual.getReferenciaFormaPago()) && msgActual.getReferenciaFormaPago() != null) {
				msgStored.setReferenciaFormaPago(msgActual.getReferenciaFormaPago());
				cambio = true;
			}

			if (msgStored.getIdUsuarioCreacionRegistro() != msgActual.getIdUsuarioCreacionRegistro() && msgActual.getIdUsuarioCreacionRegistro() > 0L) {
				msgStored.setIdUsuarioCreacionRegistro(msgActual.getIdUsuarioCreacionRegistro());
				cambio = true;
			}

			if (! msgStored.getIdSucursal().equals(msgActual.getIdSucursal()) && msgActual.getIdSucursal() != null) {
				msgStored.setIdSucursal(msgActual.getIdSucursal());
				cambio = true;
			}

			if (! msgStored.getPoliza().equals(msgActual.getPoliza()) && msgActual.getPoliza() != null) {
				msgStored.setPoliza(msgActual.getPoliza());
				cambio = true;
			}

			if (! msgStored.getLote().equals(msgActual.getLote()) && msgActual.getLote() != null) {
				msgStored.setLote(msgActual.getLote());
				cambio = true;
			}

			if (msgStored.getCreadoPor() != msgActual.getCreadoPor() && msgActual.getCreadoPor() > 0L) {
				msgStored.setCreadoPor(msgActual.getCreadoPor());
				cambio = true;
			}

			if (msgStored.getAnuladoPor() != msgActual.getAnuladoPor() && msgActual.getAnuladoPor() > 0L) {
				msgStored.setAnuladoPor(msgActual.getAnuladoPor());
				cambio = true;
			}

			if (msgStored.getEstatus() != msgActual.getEstatus() && msgActual.getEstatus() > 0L) {
				msgStored.setEstatus(msgActual.getEstatus());
				cambio = true;
			}

			if (! msgStored.getIdEmpresa().equals(msgActual.getIdEmpresa()) && msgActual.getIdEmpresa() != null) {
				msgStored.setIdEmpresa(msgActual.getIdEmpresa());
				cambio = true;
			}

			if (! msgStored.getEstatusMensaje().equals(msgActual.getEstatusMensaje()) && msgActual.getEstatusMensaje() != null) {
				msgStored.setEstatusMensaje(msgActual.getEstatusMensaje());
				cambio = true;
			}

			if (! msgStored.getFechaCreacion().equals(msgActual.getFechaCreacion()) && msgActual.getFechaCreacion() != null) {
				msgStored.setFechaCreacion(msgActual.getFechaCreacion());
				cambio = true;
			}

			if (! msgStored.getFechaAnulacion().equals(msgActual.getFechaAnulacion()) && msgActual.getFechaAnulacion() != null) {
				msgStored.setFechaAnulacion(msgActual.getFechaAnulacion());
				cambio = true;
			}

			if (! msgStored.getFechaRegistro().equals(msgActual.getFechaRegistro()) && msgActual.getFechaRegistro() != null) {
				msgStored.setFechaRegistro(msgActual.getFechaRegistro());
				cambio = true;
			}
    	} catch (Exception e) {
    		log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.actualizaPropiedades(). No se pudo actualizar las propiedades el mensaje guardado", e);
    		return false;
    	}
    	
    	return cambio;
    }
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2	| 14/07/2016 | Javier Tirado	| Creacion de MensajeTransaccionFac
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */