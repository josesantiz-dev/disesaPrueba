package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosRH;
import net.giro.rh.admon.beans.EmpleadoIncapacidad;
import net.giro.rh.admon.dao.EmpleadoIncapacidadDAO;

@Stateless
public class EmpleadoIncapacidadFac implements EmpleadoIncapacidadRem {
	private static Logger log = Logger.getLogger(EmpleadoIncapacidadFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private EmpleadoIncapacidadDAO ifzEmpIncapacidad;
	
	public EmpleadoIncapacidadFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzEmpIncapacidad = (EmpleadoIncapacidadDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoIncapacidadImp!net.giro.rh.admon.dao.EmpleadoIncapacidadDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.EmpleadoIncapacidadFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { 
		this.infoSesion = infoSesion; 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(EmpleadoIncapacidad entity) throws Exception {
		Long idEntity = null;
		
		try {
			idEntity = this.ifzEmpIncapacidad.save(entity, getCodigoEmpresa());
			if (idEntity != null && idEntity > 0L)
				this.eventoIncapacidad(idEntity);
			return idEntity;
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.save(EmpleadoIncapacidad)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<EmpleadoIncapacidad> saveOrUpdateList(List<EmpleadoIncapacidad> entities) throws Exception {
		try {
			entities = this.ifzEmpIncapacidad.saveOrUpdateList(entities, getCodigoEmpresa());
			this.eventoIncapacidad(entities);
			return entities;
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(EmpleadoIncapacidad entity) throws Exception {
		try {
			this.ifzEmpIncapacidad.update(entity);
			this.eventoIncapacidad(entity.getId());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.update(EmpleadoIncapacidad)", e);
			throw e;
		}
	}

	@Override
	public void cancelar(EmpleadoIncapacidad entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(1l);
			if (this.infoSesion != null)
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			this.ifzEmpIncapacidad.update(entity);
			this.cancelarIncapacidad(entity.getId());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.cancelar(entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(long idEmpleadoIncapacidad) throws Exception {
		try {
			this.ifzEmpIncapacidad.delete(idEmpleadoIncapacidad);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public EmpleadoIncapacidad findById(long idEmpleadoIncapacidad) {
		try {
			return this.ifzEmpIncapacidad.findById(idEmpleadoIncapacidad);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<EmpleadoIncapacidad> findAll(long idEmpleado) throws Exception {
		try {
			return this.ifzEmpIncapacidad.findAll(idEmpleado);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.findAll(idEmpleado)", e);
			throw e;
		}
	}

	@Override
	public List<EmpleadoIncapacidad> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			return this.ifzEmpIncapacidad.findByProperty(propertyName, value, getIdEmpresa(), max);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} 
	}

	@Override
	public List<EmpleadoIncapacidad> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			return this.ifzEmpIncapacidad.findLikeProperty(propertyName, value, getIdEmpresa(), max);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} 
	}

	@Override
	public List<EmpleadoIncapacidad> comprobarPorFecha(long idEmpleado, Date fecha) throws Exception {
		try {
			return this.ifzEmpIncapacidad.comprobarPorFecha(idEmpleado, fecha, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.comprobarDescuentoPorFechas(idEmpleado, fecha)", e);
			throw e;
		} 
	}

	@Override
	public List<EmpleadoIncapacidad> comprobarPorFecha(long idEmpleado, Date fechaDesde, Date fechaHasta) throws Exception {
		try {
			return this.ifzEmpIncapacidad.comprobarPorFecha(idEmpleado, fechaDesde, fechaHasta, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.comprobarDescuentoPorFechas(idEmpleado, fechaDesde, fechaHasta)", e);
			throw e;
		} 
	}

	@Override
	public boolean comprobarRegistro(EmpleadoIncapacidad entity) throws Exception {
		List<EmpleadoIncapacidad> lista = null;
		
		try {
			lista = this.ifzEmpIncapacidad.comprobarPorFecha(entity.getIdEmpleado().getId(), entity.getFechaDesde(), entity.getFechaHasta(), getIdEmpresa());
			return ! (lista == null || lista.isEmpty());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.comprobarRegistro(idEmpleado, bimestre, annio)", e);
			throw e;
		} 
	}

	@Override
	public boolean verificarIncapacidad(long idEmpleado, Date fecha) throws Exception {
		List<EmpleadoIncapacidad> lista = null;
		
		try {
			lista = this.comprobarPorFecha(idEmpleado, fecha);
			return (lista != null && ! lista.isEmpty());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.comprobarRegistro(idEmpleado, bimestre, annio)", e);
			throw e;
		}
	}

	@Override
	public List<EmpleadoIncapacidad> comprobarParaCancelar(Date fecha) throws Exception {
		List<EmpleadoIncapacidad> resultado = new ArrayList<EmpleadoIncapacidad>();
		List<EmpleadoIncapacidad> entities = null;
		
		try {
			entities = this.ifzEmpIncapacidad.findAllByDate(fecha, getIdEmpresa());
			if (entities != null && ! entities.isEmpty()) {
				for (EmpleadoIncapacidad entity : entities) {
					if (entity.getEstatus() != 0)
						continue;
					if (fecha.after(entity.getFechaHasta()))
						resultado.add(entity);
				}
			}
			
			return resultado;
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoIncapacidadFac.comprobarDescuentoPorFechas(idEmpleado, fecha)", e);
			throw e;
		} 
	}

	// --------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------

	private void eventoIncapacidad(List<EmpleadoIncapacidad> entities) {
		if (entities == null || entities.isEmpty())
			return;
		
		for (EmpleadoIncapacidad entity : entities) {
			if (entity.getId() == null || entity.getId() <= 0L)
				continue;
			this.eventoIncapacidad(entity.getId());
		}
	}

	private void eventoIncapacidad(Long idIncapacidad) {
		MensajeTopic msgTopic = null;
		String comando = "";
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (idIncapacidad == null || idIncapacidad <= 0L)
				return;
			target = String.valueOf(idIncapacidad);
			msgTopic = new MensajeTopic(TopicEventosRH.EMPLEADOS_INCAPACIDAD, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al lanzar el evento para la cancelacion de incapacidad\nEvento\n" + comando, e);
		} 
	}

	private void cancelarIncapacidad(Long idIncapacidad) {
		MensajeTopic msgTopic = null;
		String comando = "";
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (idIncapacidad == null || idIncapacidad <= 0L)
				return;
			target = String.valueOf(idIncapacidad);
			msgTopic = new MensajeTopic(TopicEventosRH.EMPLEADOS_CANCELAR_INCAPACIDAD, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al lanzar el evento para la cancelacion de incapacidad\nEvento\n" + comando, e);
		} 
	}
	
	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *   VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  2.2	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadoIncapacidadFac
*/