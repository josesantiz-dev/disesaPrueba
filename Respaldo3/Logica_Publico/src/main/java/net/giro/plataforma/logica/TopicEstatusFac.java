package net.giro.plataforma.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.TopicEstatus;
import net.giro.plataforma.dao.TopicEstatusDAO;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.topics.TiposTopicEstatus;
import net.giro.respuesta.Respuesta;

@Stateless
public class TopicEstatusFac implements TopicEstatusRem {
	private static Logger log = Logger.getLogger(TopicEstatusFac.class);
	private InfoSesion infoSesion;
	private TopicEstatusDAO ifzTopicEstatus;
	private ReportesRem ifzReportes;

	public TopicEstatusFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzTopicEstatus = (TopicEstatusDAO) ctx.lookup("ejb:/Model_Publico//TopicEstatusImp!net.giro.plataforma.dao.TopicEstatusDAO");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Publico.TopicEstatusFac", e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { 
		this.infoSesion = infoSesion; 
	}

	@Override
	public long save(String nombre, String evento, String comando, Long idTopicEstatus) throws Exception {
		TopicEstatus entity = null;
		
		try {
			idTopicEstatus = (idTopicEstatus != null && idTopicEstatus > 0L) ? idTopicEstatus : 0L;
			nombre = (nombre != null && ! "".equals(nombre.trim())) ? nombre.trim() : "DESCONOCIDO";
			evento = (evento != null && ! "".equals(evento.trim())) ? evento.trim() : "";
			comando = (comando != null && ! "".equals(comando.trim())) ? comando.trim() : "";
			if ("".equals(evento.trim()) || "".equals(comando.trim()))
				return 0L;

			if (idTopicEstatus <= 0L) 
				idTopicEstatus = comprobarEvento(comando);
			entity = new TopicEstatus(nombre, evento, comando);
			entity.setIdEmpresa(this.getIdEmpresa());
			entity.setId(idTopicEstatus);
			entity = this.saveOrUpdate(entity);
			return entity.getId();
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.save(entity)", e);
			throw e;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(TopicEstatus entity) throws Exception {
		try {
			if (entity == null)
				return 0L;
			return this.ifzTopicEstatus.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.save(entity)", e);
			throw e;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(TopicEstatus entity) throws Exception {
		try {
			this.ifzTopicEstatus.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public TopicEstatus saveOrUpdate(TopicEstatus entity) throws Exception {
		try {
			if (entity == null)
				return null;

			entity = populateData(entity);
			if (entity.getId() == null || entity.getId() <= 0L) 
				entity.setId(this.save(entity));
			else
				this.update(entity);
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.saveOrUpdate(entity)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<TopicEstatus> saveOrUpdateList(List<TopicEstatus> entities) throws Exception {
		try {
			for (TopicEstatus entity : entities)
				entity = populateData(entity);
			return this.ifzTopicEstatus.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	public TopicEstatus findById(long idTopicEstatus) throws Exception {
		try {
			return this.ifzTopicEstatus.findById(idTopicEstatus);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.findById(idTopicEstatus)", e);
			throw e;
		}
	}

	@Override
	public List<TopicEstatus> findAll(String orderBy, int limite) throws Exception {
		try {
			return this.ifzTopicEstatus.findAll(orderBy, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.findAll(orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<TopicEstatus> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, null, "", limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<TopicEstatus> findLikeProperty(String propertyName, Object value, Date fecha, String orderBy, int limite) throws Exception {
		String[] splitted = null; 
		String valor = "";
		String key = "";
		
		try {
   			if (! "fechaCreado".equals(propertyName)) {
   				if (value instanceof String) {
	   				valor = value.toString().trim().replace(" ", "%");
	   	   			if (propertyName.contains(":")) {
	   	   				splitted = propertyName.split(":");
	   	   				propertyName = splitted[0];
	   	   				key = splitted[1];
	   	   				valor = key + "\":\"%" + valor;
	   	   			}
   				}
   			}
   			
			return this.ifzTopicEstatus.findLikeProperty(propertyName, value, fecha, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.findLikeProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<TopicEstatus> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, null, "", limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<TopicEstatus> findByProperty(String propertyName, Object value, Date fecha, String orderBy, int limite) throws Exception {
		try {
			return this.ifzTopicEstatus.findByProperty(propertyName, value, fecha, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void ejecucion(long idTopicEstatus, TiposTopicEstatus estatus, String mensaje) throws Exception {
		TopicEstatus entity = null;
		
		try {
			if (idTopicEstatus <= 0L)
				return;
			
			// Actualizamos Registro
			entity = this.ifzTopicEstatus.findById(idTopicEstatus);
			if (entity == null)
				return;
			
			entity.setEstatus(estatus.ordinal());
			entity.setDescripcion(mensaje);
			entity.setFechaEjecutado(Calendar.getInstance().getTime());
			this.saveUpdate(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.ejecucion(idTopicEstatus, estatus, mensaje)", e);
			throw e;
		}
	}

	@Override
	public TopicEstatus populateCommand(TopicEstatus topic) throws Exception {
		try {
			topic = populateData(topic);
			this.update(topic);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.populateCommand(entity)", e);
			throw e;
		}
		
		return topic;
	}

	@Override
	public Respuesta enviarCorreo(String receptores, String asunto, String mensaje, HashMap<String, Object> adjuntos) throws Exception {
		try {
			return this.ifzReportes.enviarCorreo(receptores, asunto, mensaje, adjuntos);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	public Respuesta enviarCorreo(String destinatario, String destinatarioCC, String asunto, String mensaje, HashMap<String, Object> adjuntos) throws Exception {
		try {
			return this.enviarCorreo(destinatario, destinatarioCC, "", asunto, mensaje, adjuntos);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	public Respuesta enviarCorreo(String destinatario, String destinatarioCC, String destinatarioCCO, String asunto, String mensaje, HashMap<String, Object> adjuntos) throws Exception {
		HashMap<String, String> correo = new HashMap<String, String>();
		
		try {
			correo.put("to", destinatario);
			correo.put("cc", destinatarioCC);
			correo.put("cco", destinatarioCCO);
			correo.put("subject", asunto);
			correo.put("body", mensaje);
			
			return this.ifzReportes.enviarCorreo(correo, adjuntos);
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void inflateTopicEstatus(Date fechaInicial, Date fechaFinal) throws Exception {
		List<TopicEstatus> itemsFull = null;
		List<TopicEstatus> items = null;
		int idxInicial = 0;
		int idxFinal = 0;
		int steps = 0;
		int step = 0;
		
		try {
			log.info("Retriving data from ...");
			itemsFull = this.ifzTopicEstatus.findByDates(fechaInicial, fechaFinal, getIdEmpresa());
			if (itemsFull == null || itemsFull.isEmpty())
				return;

			log.info("Populating data from " + itemsFull.size() + " items");
			steps = (int) Math.ceil(itemsFull.size() * 0.001);
			for (TopicEstatus item : itemsFull)
				item.populateData();
			
			do {
				idxInicial = 1000 * step++;
				idxFinal = idxInicial + 1000;
				idxFinal = (idxFinal > itemsFull.size() ? itemsFull.size() : idxFinal);
				
				log.info("Step " + step + "/" + steps + " ... recuperando");
				items = new ArrayList<TopicEstatus>();
				for (int index = idxInicial; index < idxFinal; index++)
					items.add(itemsFull.get(index));
				log.info("Step " + step + "/" + steps + " ... guardando");
				this.saveOrUpdateList(items);
				log.info("Step " + step + "/" + steps + " ... terminado");
			} while (step < steps);
		} catch (Exception e) {
			log.error("Ocurrio un problema al inflar TopicEstatus", e);
		}
	}
	
	// ---------------------------------------------------------------------------------------------------------------
	// PRIVADOS 
	// ---------------------------------------------------------------------------------------------------------------

	public long saveUpdate(TopicEstatus entity) throws Exception {
		try {
			if (entity == null)
				return 0L;
			if (entity.getId() == null || entity.getId() <= 0L)
				return this.save(entity);
			this.update(entity);
			return entity.getId();
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.saveUpdate(entity)", e);
			throw e;
		}
	}

	private long comprobarEvento(String comando) {
		JsonParser parser = null;
		JsonObject json = null;
		String target = "";
		String referencia = "";
		String referenciaExtra = "";
		String atributos = "";
		List<TopicEstatus> eventos = null;

		try {
			if (comando == null || "".equals(comando.trim()))
				return 0L;
			parser = new JsonParser();
			json = parser.parse(comando).getAsJsonObject();
			if (json.has("target"))
				target = json.get("target").getAsString();
			if (json.has("referencia"))
				referencia = json.get("referencia").getAsString();
			if (json.has("referenciaExtra"))
				referenciaExtra = json.get("referenciaExtra").getAsString();
			if (json.has("atributos"))
				atributos = json.get("atributos").getAsString();
		} catch (Exception e) {
			log.error("Ocurrio un problema al convertir a JSON del comando: " + comando, e);
			return 0L;
		} finally {
			parser = null;
			json = null;
		}
		
		try {
			target = (target != null && ! "".equals(target.trim()) ? target.trim() : "");
			referencia = (referencia != null && ! "".equals(referencia.trim()) ? referencia.trim() : "");
			referenciaExtra = (referenciaExtra != null && ! "".equals(referenciaExtra.trim()) ? referenciaExtra.trim() : "");
			atributos = (atributos != null && ! "".equals(atributos.trim()) ? atributos.trim() : "");
			eventos = this.ifzTopicEstatus.comprobarComando(target, referencia, referenciaExtra, atributos, null, getIdEmpresa());
			return (eventos != null && ! eventos.isEmpty() ? eventos.get(0).getId() : 0L);
		} catch (Exception e) {
			log.error("Ocurrio un problema al comprobar el comando: " + comando, e);
		}
		
		return 0L;
	}
	
	private TopicEstatus populateData(TopicEstatus topic) {
		JsonParser parser = null;
		JsonObject json = null;

		try {
			if (topic == null || topic.getComando() == null || "".equals(topic.getComando().trim()))
				return topic;
			
			parser = new JsonParser();
			json = parser.parse(topic.getComando()).getAsJsonObject();
			if (json.has("target"))
				topic.setTarget(json.get("target").getAsString());
			if (json.has("referencia"))
				topic.setReferencia(json.get("referencia").getAsString());
			if (json.has("atributos"))
				topic.setAtributos(json.get("atributos").getAsString());
			return topic;
		} catch (Exception e) {
			log.error("Ocurrio un problema al distribuir los valores con JSON del comando: " + topic.getComando(), e);
		} finally {
			parser = null;
			json = null;
		}
		
		try {
			topic.populateData();
		} catch (Exception e) {
			log.error("Ocurrio un problema al distribuir los valores del comando: " + topic.getComando(), e);
		}
		
		return topic;
	}
	
	private Long getIdEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L;
	}

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L;
	}
}
