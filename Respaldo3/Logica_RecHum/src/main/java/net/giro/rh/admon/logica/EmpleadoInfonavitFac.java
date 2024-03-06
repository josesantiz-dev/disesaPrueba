package net.giro.rh.admon.logica;

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

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoInfonavit;
import net.giro.rh.admon.dao.EmpleadoInfonavitDAO;

@Stateless
public class EmpleadoInfonavitFac implements EmpleadoInfonavitRem {
	private static Logger log = Logger.getLogger(EmpleadoInfonavitFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private EmpleadoInfonavitDAO ifzEmpInfonavit;
	private static String orderBy;
	
	public EmpleadoInfonavitFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzEmpInfonavit = (EmpleadoInfonavitDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoInfonavitImp!net.giro.rh.admon.dao.EmpleadoInfonavitDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.EmpleadoInfonavitFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void orderBy(String orderBy) { EmpleadoInfonavitFac.orderBy = orderBy; }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(EmpleadoInfonavit entity) throws ExcepConstraint {
		try {
			return this.ifzEmpInfonavit.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.save(EmpleadoInfonavit)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<EmpleadoInfonavit> saveOrUpdateList(List<EmpleadoInfonavit> entities) throws Exception {
		try {
			return this.ifzEmpInfonavit.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.save(EmpleadoInfonavit)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(EmpleadoInfonavit entity) throws ExcepConstraint {
		try {
			this.ifzEmpInfonavit.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.update(EmpleadoInfonavit)", e);
			throw e;
		}
	}

	@Override
	public void cancelar(EmpleadoInfonavit entity) throws ExcepConstraint {
		try {
			entity.setEstatus(1);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(1l);
			if (this.infoSesion != null)
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			this.ifzEmpInfonavit.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.cancelar(entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(long idEmpleadoInfonavit) throws ExcepConstraint {
		try {
			this.ifzEmpInfonavit.delete(idEmpleadoInfonavit);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public EmpleadoInfonavit findById(long idEmpleadoInfonavit) {
		try {
			return this.ifzEmpInfonavit.findById(idEmpleadoInfonavit);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<EmpleadoInfonavit> findAll(long idEmpleado) throws Exception {
		try {
			return this.ifzEmpInfonavit.findAll(idEmpleado);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findAll(idEmpleado)", e);
			throw e;
		}
	}

	@Override
	public List<EmpleadoInfonavit> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzEmpInfonavit.orderBy(orderBy);
			return this.ifzEmpInfonavit.findByProperty(propertyName, value, getIdEmpresa(), max);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoInfonavit> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzEmpInfonavit.orderBy(orderBy);
			return this.ifzEmpInfonavit.findLikeProperty(propertyName, value, getIdEmpresa(), max);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<EmpleadoInfonavit> comprobarPorFecha(long idEmpleado, Date fecha) throws Exception {
		try {
			return this.ifzEmpInfonavit.comprobarPorFecha(idEmpleado, fecha, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.comprobarDescuentoPorFechas(idEmpleado, fecha)", e);
			throw e;
		} 
	}

	@Override
	public List<EmpleadoInfonavit> comprobarPorFecha(long idEmpleado, Date fechaDesde, Date fechaHasta) throws Exception {
		try {
			return this.ifzEmpInfonavit.comprobarPorFecha(idEmpleado, fechaDesde, fechaHasta, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.comprobarDescuentoPorFechas(idEmpleado, fechaDesde, fechaHasta)", e);
			throw e;
		} 
	}

	@Override
	public boolean comprobarRegistro(EmpleadoInfonavit entity) throws Exception {
		List<EmpleadoInfonavit> lista = null;
		
		try {
			this.ifzEmpInfonavit.orderBy(orderBy);
			lista = this.ifzEmpInfonavit.comprobarPorFecha(entity.getIdEmpleado().getId(), entity.getFechaDesde(), entity.getFechaHasta(), getIdEmpresa());
			return ! (lista == null || lista.isEmpty());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.comprobarRegistro(idEmpleado, bimestre, annio)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<EmpleadoInfonavit> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzEmpInfonavit.orderBy(orderBy);
			return this.ifzEmpInfonavit.findInProperty(columnName, values, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoInfonavit> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzEmpInfonavit.orderBy(orderBy);
			return this.ifzEmpInfonavit.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoInfonavit> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzEmpInfonavit.orderBy(orderBy);
			return this.ifzEmpInfonavit.findLikeProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}*/

	// --------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------

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

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadoInfonavitFac