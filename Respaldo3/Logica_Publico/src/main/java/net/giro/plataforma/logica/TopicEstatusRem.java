package net.giro.plataforma.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.TopicEstatus;
import net.giro.plataforma.topics.TiposTopicEstatus;
import net.giro.respuesta.Respuesta;

@Remote
public interface TopicEstatusRem {
	public void setInfoSesion(InfoSesion infoSesion);

	/**
	 * Metodo para registrar evento, previamente hace una validacion para comprobar existencia de comando
	 * @param nombre
	 * @param evento
	 * @param comando
	 * @return
	 * @throws Exception
	 */
	public long save(String nombre, String evento, String comando, Long idTopicEstatus) throws Exception;
	
	/**
	 * Metodo para registrar evento, previamente hace una validacion para comprobar existencia de comando
	 * @param nombre
	 * @param evento
	 * @param comando
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	//public long save(String nombre, String evento, String comando, Date fecha) throws Exception;

	/**
	 * Metodo para guardar/actualizar registro de evento
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public TopicEstatus saveOrUpdate(TopicEstatus entity) throws Exception;
	
	public long save(TopicEstatus entity) throws Exception;
	
	public List<TopicEstatus> saveOrUpdateList(List<TopicEstatus> entities) throws Exception;

	public void update(TopicEstatus entity) throws Exception;

	public TopicEstatus findById(long idTopicEstatus) throws Exception;
	
	public List<TopicEstatus> findAll(String orderBy, int limite) throws Exception;

	public List<TopicEstatus> findLikeProperty(String propertyName, Object value, int limite) throws Exception;

	public List<TopicEstatus> findLikeProperty(String propertyName, Object value, Date fecha, String orderBy, int limite) throws Exception;

	public List<TopicEstatus> findByProperty(String propertyName, Object value, int limite) throws Exception;

	public List<TopicEstatus> findByProperty(String propertyName, Object value, Date fecha, String orderBy, int limite) throws Exception;
	
	public void ejecucion(long idTopicEstatus, TiposTopicEstatus estatus, String mensaje) throws Exception;
	
	public TopicEstatus populateCommand(TopicEstatus topic) throws Exception;
	
	public void inflateTopicEstatus(Date fechaInicial, Date fechaFinal) throws Exception;
	
	public Respuesta enviarCorreo(String destinatario, String asunto, String mensaje, HashMap<String, Object> adjuntos) throws Exception;
	
	public Respuesta enviarCorreo(String destinatario, String destinatarioCC, String asunto, String mensaje, HashMap<String, Object> adjuntos) throws Exception;
	
	public Respuesta enviarCorreo(String destinatario, String destinatarioCC, String destinatarioCCO, String asunto, String mensaje, HashMap<String, Object> adjuntos) throws Exception;
}
