package net.giro.contabilidad.logica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import net.giro.contabilidad.beans.MensajeContabilizar;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.beans.MensajeTransaccionExt;
import net.giro.contabilidad.dao.MensajeTransaccionDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class MensajeTransaccionFac implements MensajeTransaccionRem {
	private static Logger log = Logger.getLogger(MensajeTransaccionFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private java.sql.Connection conn;
	private DataSource datasource;
	private MensajeTransaccionDAO ifzMensajeTransaccion;
	private ConvertExt convertidor;
	
	public MensajeTransaccionFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
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
	public Long save(MensajeTransaccion entity) throws Exception {
		MensajeTransaccion stored = null;
		long idMensajeTransaccion = 0L;
		ResultSet rs = null;
		String strQuery;
		
		try {
			stored = this.comprobarMensajeTransaccion(entity.getIdTransaccion(), entity.getIdOperacion());
			if (stored != null) {
				BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
				BeanUtils.copyProperties(entity, stored);
				stored = null;
			}
			
			if (entity.getPoliza() == null || entity.getPoliza() <= 0L) {
				entity.setPoliza(0L);
	        	strQuery = "select nextval('seq_polizas_numero') AS poliza";
	        	if (this.getConnection()) { 
		        	rs = this.conn.createStatement().executeQuery(strQuery);
					if (rs.next()) 
						entity.setPoliza(rs.getLong(1));
	        	}
			}
			
			// Generamos numero LOTE si corresponde
			if (entity.getLote() == null || entity.getLote() <= 0L) {
				entity.setLote(0L);
	        	strQuery = "select nextval('seq_lote_numero') AS lote";
	        	if (this.getConnection()) {
		        	rs = this.conn.createStatement().executeQuery(strQuery);
					if (rs.next()) 
						entity.setLote(rs.getLong(1));
	        	}
			}
			
			// Guardamos/Actualizamos segun corresponda
			entity = this.saveOrUpdate(entity);

			// Enviamos mensaje a cola TRANSACCION
			enviarMensajeTransaccion(entity);
			idMensajeTransaccion = entity.getId();
			log.info("Se envio mensaje ID " + idMensajeTransaccion + " a cola de transacciones.");
			
			return idMensajeTransaccion;
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.save(MensajeTransaccion)", e);
			throw e;
		} finally {
			closeConnection();
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public MensajeTransaccion saveOrUpdate(MensajeTransaccion entity) throws Exception {
		try {
			if (entity.getId() == null || entity.getId() <= 0L)
				entity.setId(this.ifzMensajeTransaccion.save(entity, getCodigoEmpresa()));
			else
				this.ifzMensajeTransaccion.update(entity);
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.saveOrUpdate(MensajeTransaccion)", e);
			throw e;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MensajeTransaccion> saveOrUpdateList(List<MensajeTransaccion> entities) throws Exception {
		List<MensajeTransaccion> nuevas = null;
		List<MensajeTransaccion> actualizables = null;
		MensajeTransaccion stored = null;
		// --------------------------------------------
		ResultSet rs = null;
		String strQuery;
		Long lote = 0L;
		Long poliza = 0L;
		
		try {
			if (entities == null || entities.isEmpty())
				return entities;

			nuevas = new ArrayList<MensajeTransaccion>();
			actualizables = new ArrayList<MensajeTransaccion>();
			// comprobacion de mensajes
			for (MensajeTransaccion entity : entities) {
				if (entity.getLote() != null || entity.getLote() > 0L)
					lote = lote <= 0L ? entity.getLote() : lote;
				if (entity.getPoliza() != null || entity.getPoliza() > 0L)
					poliza = poliza <= 0L ? entity.getPoliza() : poliza;
				
				stored = this.comprobarMensajeTransaccion(entity.getIdTransaccion(), entity.getIdOperacion());
				if (stored != null) {
					try {
						// Actualizamos las propiedades del mensaje guardado con el actual y lo actualizo en la BD si corresponde
						if (actualizaPropiedades(entity, stored))
							this.update(stored);
						BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
						BeanUtils.copyProperties(entity, stored);
					} catch (Exception e) {
						log.info("No pude copiar el mensaje por BeanUtils, uso asignacion directa :(");
						entity.setId(stored.getId());
					}
					
					actualizables.add(entity);
					continue;
				}
				
				// No existe evento
				nuevas.add(entity);
			}
			
			if (lote <= 0L) {
	        	strQuery = "select nextval('seq_lote_numero') AS lote";
	        	if (this.getConnection()) {
		        	rs = this.conn.createStatement().executeQuery(strQuery);
					if (rs.next()) 
						lote = rs.getLong(1);
	        	}
			}
			
			if (poliza <= 0L) {
	        	strQuery = "select nextval('seq_polizas_numero') AS poliza";
	        	if (this.getConnection()) {
		        	rs = this.conn.createStatement().executeQuery(strQuery);
					if (rs.next()) 
						poliza = rs.getLong(1);
	        	}
			}
			
			entities.clear();
			entities.addAll(actualizables);
			entities.addAll(nuevas);
			for (MensajeTransaccion entity : nuevas) {
				if (entity.getPoliza() == null || entity.getPoliza() <= 0L) 
					entity.setPoliza(poliza);
				if (entity.getLote() == null || entity.getLote() <= 0L) 
					entity.setLote(lote);
			}
			
			// Guardamos mensajes (Eventos)
			return this.ifzMensajeTransaccion.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.saveOrUpdateList(List<MensajeTransaccion> entities)", e);
			throw e;
		} finally {
			closeConnection();
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
	public void delete(long idMensajeTransaccion) throws Exception {
		try {
			this.ifzMensajeTransaccion.delete(idMensajeTransaccion);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.delete(idMensajeTransaccion)", e);
			throw e;
		}
	}

	@Override
	public MensajeTransaccion findById(long idMensajeTransaccion) {
		try {
			return this.ifzMensajeTransaccion.findById(idMensajeTransaccion);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findById(idMensajeTransaccion)", e);
			throw e;
		}
	}

	@Override
	public List<MensajeTransaccion> findAll() throws Exception {
		try {
			return this.ifzMensajeTransaccion.findAll();
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findAll()", e);
			throw e;
		} 
	}

	@Override
	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, false, false, "", 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findByProperty(propertyName, value)", e);
			throw e;
		}
	}
	
	@Override
	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value, String orderBy, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, false, false, orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<MensajeTransaccion> findByProperty(String propertyName, Object value, boolean incluyeContabilizados, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		try {
			return this.ifzMensajeTransaccion.findByProperty(propertyName, value, incluyeContabilizados, incluyeCancelados, getIdEmpresa(), orderBy, limite);
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
			return this.findByProperties(params, "", 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findByProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception {
		try {
			return this.ifzMensajeTransaccion.findByProperties(params, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<MensajeTransaccion> findLikeProperty(String propertyName, Object value) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, false, false, "", 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findLikeProperty(propertyName, value)", e);
			throw e;
		}
	}
	
	@Override
	public List<MensajeTransaccion> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, false, false, orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<MensajeTransaccion> findLikeProperty(String propertyName, Object value, boolean incluyeContabilizados, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		try {
			return this.ifzMensajeTransaccion.findLikeProperty(propertyName, value, incluyeContabilizados, incluyeCancelados, getIdEmpresa(), orderBy, limite);
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
			return this.findLikeProperties(params, "", 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findLikeProperties(params)", e);
			throw e;
		}
	}
	
	@Override
	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params, String orderBy, int limite) throws Exception {
		try {
			return this.ifzMensajeTransaccion.findLikeProperties(params, getIdEmpresa(), orderBy, limite);
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
			return this.findInProperty(columnName, values, "", 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findInProperty(columnName, values)", e);
			throw e;
		}
	}
	
	@Override
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values, String orderBy, int limite) throws Exception {
		try {
			return this.ifzMensajeTransaccion.findInProperty(columnName, values, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public MensajeTransaccion comprobarMensajeTransaccion(long idTransaccion, long idOperacion) throws Exception {
		try {
			return this.ifzMensajeTransaccion.comprobarMensajeTransaccion(idTransaccion, idOperacion, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.comprobarMensajeTransaccion(idTransaccion, idOperacion)", e);
			throw e;
		}
	}

	@Override
	public void enviarMensajeTransaccion(MensajeTransaccion mensaje) throws Exception {
		MensajeContabilizar msgContabilizar = null;
		String json = "";
		
		try {
			if (mensaje == null || mensaje.getId() == null || mensaje.getId() <= 0L)
				return;

			if (mensaje.getEstatus() == 1) {
				msgContabilizar = new MensajeContabilizar();
				msgContabilizar.setIdMensajeTransaccion(mensaje.getId());
				msgContabilizar.setIdTransaccion(mensaje.getIdTransaccion());
				msgContabilizar.setPoliza(mensaje.getPoliza());
				msgContabilizar.setLote(mensaje.getLote());
				msgContabilizar.setFecha((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(mensaje.getFechaRegistro()));
				msgContabilizar.setEjercicio(Long.valueOf((new SimpleDateFormat("yyyy")).format(mensaje.getFechaRegistro())));
				msgContabilizar.setPeriodo(getPeriodo(mensaje.getFechaRegistro()));
				msgContabilizar.setIdEmpresa(mensaje.getIdEmpresa());
				json = generaMensaje(msgContabilizar);
				json = (json != null && ! "".equals(json.trim())) ? json : "";
				enviarCONTABILIZADOR(json);
				return;
			}

			json = generaMensaje(mensaje);
			json = (json != null && ! "".equals(json.trim())) ? json : "";
			enviarTRANSACCIONADOR(json);
		} catch (Exception e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.enviarTransaccion(String jsonString)", e);
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

	// -------------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------------------------------------

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
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(MensajeTransaccionExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.MensajeTransaccionExtToMensajeTransaccion(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.MensajeTransaccionFac.update(MensajeTransaccionExt)", e);
			throw e;
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// -------------------------------------------------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L;
	}

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L;
	}

	private String generaMensaje(MensajeTransaccion mensaje) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		JsonObject json = null;	
		Gson gson = null;
		String strJson = "";
		
		try {
			gson = new GsonBuilder().serializeNulls().setDateFormat("MM/dd/yyyy HH:mm:ss").create();
			strJson = gson.toJson(mensaje);
		} catch (Exception e) {
			log.error("Ocurrio un problema al transformar el mensaje (Evento) a Json. Procedo con modo manual ... ", e);
			strJson = "";
		}
		
		if ("".equals(strJson)) {
			try {
				json = new JsonObject();
				json.addProperty("id", mensaje.getId());
				json.addProperty("idTransaccion", mensaje.getIdTransaccion());
				json.addProperty("idOperacion", mensaje.getIdOperacion());
				json.addProperty("descripcionOperacion", mensaje.getDescripcionOperacion());
				json.addProperty("idMoneda", mensaje.getIdMoneda());
				json.addProperty("descripcionMoneda", mensaje.getDescripcionMoneda());
				json.addProperty("importe", mensaje.getImporte());
				json.addProperty("idPersonaReferencia", mensaje.getIdPersonaReferencia());
				json.addProperty("nombrePersonaReferencia", mensaje.getNombrePersonaReferencia());
				json.addProperty("tipoPersona", mensaje.getTipoPersona());
				json.addProperty("referencia", mensaje.getReferencia());
				json.addProperty("idFormaPago", mensaje.getIdFormaPago());
				json.addProperty("descripcionFormaPago", mensaje.getDescripcionFormaPago());
				json.addProperty("referenciaFormaPago", mensaje.getReferenciaFormaPago());
				json.addProperty("idSucursal", mensaje.getIdSucursal());
				json.addProperty("descripcionSucursal", mensaje.getDescripcionSucursal());
				json.addProperty("idEmpresa", mensaje.getIdEmpresa());
				json.addProperty("descripcionEmpresa", mensaje.getDescripcionEmpresa());
				json.addProperty("poliza", mensaje.getPoliza());
				json.addProperty("lote", mensaje.getLote());
				json.addProperty("idUsuarioCreacionRegistro", mensaje.getIdUsuarioCreacionRegistro());
				json.addProperty("creadoPor", mensaje.getCreadoPor());
				json.addProperty("anuladoPor", mensaje.getAnuladoPor());
				json.addProperty("estatus", mensaje.getEstatus());
				json.addProperty("estatusMensaje", mensaje.getEstatusMensaje());
				if (mensaje.getFechaCreacion() != null) 
					json.addProperty("fechaCreacion", formatter.format(mensaje.getFechaCreacion()));
				else 
					json.addProperty("fechaCreacion", formatter.format(Calendar.getInstance().getTime()));
				if (mensaje.getFechaAnulacion() != null) 
					json.addProperty("fechaAnulacion", formatter.format(mensaje.getFechaAnulacion()));
				else 
					json.add("fechaAnulacion", JsonNull.INSTANCE);
				if (mensaje.getFechaRegistro() != null) 
					json.addProperty("fechaRegistro", formatter.format(mensaje.getFechaRegistro()));
				else 
					json.add("fechaRegistro", JsonNull.INSTANCE);
				log.info("|---> MENSAJE TRANSACCION :: " + json.toString());
				strJson = json.toString();
			} catch (Exception e) {
				log.error("Ocurrio un problema al intentar transformar el mensaje (Evento) a Json. :(", e);
				strJson = "";
			}
		}
		
		return strJson;
	}

	private String generaMensaje(MensajeContabilizar mensaje) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		JsonObject json = null;	
		Gson gson = null;
		String strJson = "";
		
		try {
			gson = new GsonBuilder().serializeNulls().setDateFormat("MM/dd/yyyy HH:mm:ss").create();
			strJson = gson.toJson(mensaje);
		} catch (Exception e) {
			log.error("Ocurrio un problema al transformar el mensaje (Evento) a Json. Procedo con modo manual ... ", e);
			strJson = "";
		}
		
		if ("".equals(strJson)) {
			try {
				json = new JsonObject();
				json.addProperty("idTransaccion", mensaje.getIdTransaccion());
				json.addProperty("poliza", mensaje.getPoliza());
				json.addProperty("lote", mensaje.getLote());
				json.addProperty("idEmpresa", mensaje.getIdEmpresa());
				json.addProperty("ejercicio", formatter.format(mensaje.getEjercicio()));
				json.addProperty("periodo", formatter.format(mensaje.getPeriodo()));
				json.addProperty("fecha", formatter.format(mensaje.getFecha()));
				json.addProperty("idMensajeTransaccion", mensaje.getIdMensajeTransaccion());
				log.info("|---> MENSAJE TRANSACCION :: " + json.toString());
				strJson = json.toString();
			} catch (Exception e) {
				log.error("Ocurrio un problema al intentar transformar el mensaje (Evento) a Json. :(", e);
				strJson = "";
			}
		}
		
		return strJson;
	}
	
	private void enviarTRANSACCIONADOR(String jsonString) throws Exception {
		TopicConnectionFactory topicFactory = null;
		Connection jmsConnection = null;
		Session session = null;
		// ----------------------------------------
		Topic topic = null;
		MessageProducer sender = null;
		TextMessage topicMensaje = null;

		try {
			if (jsonString == null || "".equals(jsonString.trim()))
				return;
			
			// Instanciamos el Topic TRANSACCIONES
			topicFactory = (TopicConnectionFactory) this.ctx.lookup("ConnectionFactory");
			jmsConnection = topicFactory.createTopicConnection();
			topic = (Topic) this.ctx.lookup("topic/TRANSACCIONES");
			session = jmsConnection.createSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			jmsConnection.start();
			// Generamos el Producer y asignamos el mensaje
			sender = session.createProducer(topic);
			topicMensaje = session.createTextMessage(jsonString);
			// Enviamos mensaje
			sender.send(topicMensaje);
		} catch (JMSException e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.enviarTRANSACCIONADOR(jsonString)", e);
			throw e;
		} catch (NamingException e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.enviarTRANSACCIONADOR(jsonString)", e);
			throw e;
		} catch (Exception e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.enviarTRANSACCIONADOR(jsonString)", e);
			throw e;
		} finally {
			if (jmsConnection != null) {
				jmsConnection.stop();
				if (session != null) 
					session.close();
				jmsConnection.close();
			}
		}
	}
	
	private void enviarCONTABILIZADOR(String jsonString) throws Exception {
		TopicConnectionFactory tcf = null;
		Topic topic = null;
		MessageProducer sendtopic = null;
		TextMessage tm = null;
		Connection jmsConn = null;
		Session session = null;

		try {
			if (jsonString == null || "".equals(jsonString.trim()))
				return;
			
			// Instanciamos el Topic TRANSACCIONES
			tcf = (TopicConnectionFactory) this.ctx.lookup("ConnectionFactory");
			jmsConn = tcf.createTopicConnection();
			topic = (Topic) this.ctx.lookup("topic/CONTABILIZAR");
			session = jmsConn.createSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			jmsConn.start();
			// Generamos el Producer y asignamos el mensaje
			sendtopic = session.createProducer(topic);
			tm = session.createTextMessage(jsonString);
			// Enviamos mensaje
			sendtopic.send(tm);
		} catch (JMSException e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.enviarCONTABILIZADOR(jsonString)", e);
			throw e;
		} catch (NamingException e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.enviarCONTABILIZADOR(jsonString)", e);
			throw e;
		} catch (Exception e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.enviarCONTABILIZADOR(jsonString)", e);
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
			
			if (! msgStored.getDescripcionOperacion().equals(msgActual.getDescripcionOperacion()) && msgActual.getDescripcionOperacion() != null) {
				msgStored.setDescripcionOperacion(msgActual.getDescripcionOperacion());
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
			
			if (msgStored.getImporte().doubleValue() != msgActual.getImporte().doubleValue() && msgActual.getImporte() != null) {
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
			
			if (! msgStored.getTipoPersona().equals(msgActual.getTipoPersona()) && msgActual.getTipoPersona() != null) {
				msgStored.setTipoPersona(msgActual.getTipoPersona());
				cambio = true;
			}
			
			if (! msgStored.getReferencia().equals(msgActual.getReferencia()) && msgActual.getReferencia() != null) {
				msgStored.setReferencia(msgActual.getReferencia());
				cambio = true;
			}
			
			if (! msgStored.getIdFormaPago().equals(msgActual.getIdFormaPago()) && msgActual.getIdFormaPago() != null) {
				msgStored.setIdFormaPago(msgActual.getIdFormaPago());
				cambio = true;
			}
			
			if (! msgStored.getDescripcionFormaPago().equals(msgActual.getDescripcionFormaPago()) && msgActual.getDescripcionFormaPago() != null) {
				msgStored.setDescripcionFormaPago(msgActual.getDescripcionFormaPago());
				cambio = true;
			}
			
			if (! msgStored.getReferenciaFormaPago().equals(msgActual.getReferenciaFormaPago()) && msgActual.getReferenciaFormaPago() != null) {
				msgStored.setReferenciaFormaPago(msgActual.getReferenciaFormaPago());
				cambio = true;
			}
			
			if (! msgStored.getIdSucursal().equals(msgActual.getIdSucursal()) && msgActual.getIdSucursal() != null) {
				msgStored.setIdSucursal(msgActual.getIdSucursal());
				cambio = true;
			}
			
			if (! msgStored.getDescripcionSucursal().equals(msgActual.getDescripcionSucursal()) && msgActual.getDescripcionSucursal() != null) {
				msgStored.setDescripcionSucursal(msgActual.getDescripcionSucursal());
				cambio = true;
			}
			
			if (! msgStored.getIdEmpresa().equals(msgActual.getIdEmpresa()) && msgActual.getIdEmpresa() != null) {
				msgStored.setIdEmpresa(msgActual.getIdEmpresa());
				cambio = true;
			}
			
			if (! msgStored.getDescripcionEmpresa().equals(msgActual.getDescripcionEmpresa()) && msgActual.getDescripcionEmpresa() != null) {
				msgStored.setDescripcionEmpresa(msgActual.getDescripcionEmpresa());
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
			
			if (msgStored.getIdUsuarioCreacionRegistro() != msgActual.getIdUsuarioCreacionRegistro() && msgActual.getIdUsuarioCreacionRegistro() > 0L) {
				msgStored.setIdUsuarioCreacionRegistro(msgActual.getIdUsuarioCreacionRegistro());
				cambio = true;
			}
			
			if ((msgStored.getFechaRegistro().compareTo(msgActual.getFechaRegistro()) != 0) && msgActual.getFechaRegistro() != null) {
				msgStored.setFechaRegistro(msgActual.getFechaRegistro());
				cambio = true;
			}
			
			if (msgStored.getCreadoPor() != msgActual.getCreadoPor() && msgActual.getCreadoPor()  > 0L) {
				msgStored.setCreadoPor(msgActual.getCreadoPor());
				cambio = true;
			}
			
			if ((msgStored.getFechaCreacion().compareTo(msgActual.getFechaCreacion()) != 0) && msgActual.getFechaCreacion() != null) {
				msgStored.setFechaCreacion(msgActual.getFechaCreacion());
				cambio = true;
			}
			
			if (msgStored.getAnuladoPor() != msgActual.getAnuladoPor() && msgActual.getAnuladoPor() > 0L) {
				msgStored.setAnuladoPor(msgActual.getAnuladoPor());
				cambio = true;
			}
			
			if ((msgStored.getFechaAnulacion().compareTo(msgActual.getFechaAnulacion()) != 0) && msgActual.getFechaAnulacion() != null) {
				msgStored.setFechaAnulacion(msgActual.getFechaAnulacion());
				cambio = true;
			}
			
			if (msgStored.getEstatus() != msgActual.getEstatus() && msgActual.getEstatus() > 0L) {
				msgStored.setEstatus(msgActual.getEstatus());
				cambio = true;
			}
			
			if (! msgStored.getEstatusMensaje().equals(msgActual.getEstatusMensaje()) && msgActual.getEstatusMensaje() != null) {
				msgStored.setEstatusMensaje(msgActual.getEstatusMensaje());
				cambio = true;
			}
    	} catch (Exception e) {
    		log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.actualizaPropiedades(). No se pudo actualizar las propiedades el mensaje guardado", e);
    		return false;
    	}
    	
    	return cambio;
    }

    private String getPeriodo(Date fecha) {
		SimpleDateFormat formatterPeriodo = new SimpleDateFormat("MM");
		String periodo = "";
		
		switch (formatterPeriodo.format(fecha)) {
			case "01": periodo = "ENERO"; break;
			case "02": periodo = "FEBRERO"; break;
			case "03": periodo = "MARZO"; break;
			case "04": periodo = "ABRIL"; break;
			case "05": periodo = "MAYO"; break;
			case "06": periodo = "JUNIO"; break;
			case "07": periodo = "JULIO"; break;
			case "08": periodo = "AGOSTO"; break;
			case "09": periodo = "SEPTIEMBRE"; break;
			case "10": periodo = "OCTUBRE"; break;
			case "11": periodo = "NOVIEMBRE"; break;
			case "12": periodo = "DICIEMBRE"; break;
			default : periodo = ""; break;
		}
		
    	return periodo;
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