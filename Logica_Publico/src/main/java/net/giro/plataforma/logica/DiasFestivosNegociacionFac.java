package net.giro.plataforma.logica;

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

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.DiasFestivosNegociacion;
import net.giro.plataforma.dao.DiasFestivosNegociacionDAO;

import org.apache.log4j.Logger;

@Stateless
public class DiasFestivosNegociacionFac implements DiasFestivosNegociacionRem {
	private static Logger log = Logger.getLogger(DiasFestivosNegociacionFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private DiasFestivosNegociacionDAO ifzNegociacion;

	public DiasFestivosNegociacionFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzNegociacion = (DiasFestivosNegociacionDAO) this.ctx.lookup("ejb:/Model_Publico//DiasFestivosNegociacionImp!net.giro.plataforma.dao.DiasFestivosNegociacionDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Publico.DiasFestivosNegociacionFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion; 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(DiasFestivosNegociacion entity) throws Exception {
		try {
			return this.ifzNegociacion.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<DiasFestivosNegociacion> saveOrUpdateList(List<DiasFestivosNegociacion> entities) throws Exception {
		try {
			return this.ifzNegociacion.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(DiasFestivosNegociacion entity) throws Exception {
		try {
			this.ifzNegociacion.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public DiasFestivosNegociacion cancelar(DiasFestivosNegociacion entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setModificadoPor(1L);
			if (this.infoSesion != null)
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzNegociacion.update(entity);
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public DiasFestivosNegociacion findById(long idEntity) throws Exception {
		try {
			return this.ifzNegociacion.findById(idEntity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.findById(idEntity)", e);
			throw e;
		}
	}

	@Override
	public List<DiasFestivosNegociacion> findAll(int limite) throws Exception {
		try {
			return this.findAll(0L, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.findAll(limite)", e);
			throw e;
		}
	}

	@Override
	public List<DiasFestivosNegociacion> findAll(long idObra, int limite) throws Exception {
		try {
			return this.ifzNegociacion.findAll(idObra, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.findAll(idObra, limite)", e);
			throw e;
		}
	}

	@Override
	public List<DiasFestivosNegociacion> findByProperty(String propertyName, Object value, long idObra, int limite) throws Exception {
		try {
			return this.ifzNegociacion.findByProperty(propertyName, value, idObra, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.findByProperty(propertyName, value, idObra, limite)", e);
			throw e;
		}
	}

	@Override
	public List<DiasFestivosNegociacion> findLikeProperty(String propertyName, Object value, long idObra, int limite) throws Exception {
		try {
			return this.ifzNegociacion.findLikeProperty(propertyName, value, idObra, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.findLikeProperty(propertyName, value, idObra, limite)", e);
			throw e;
		}
	}

	@Override
	public List<DiasFestivosNegociacion> comprobarNegociacion(long idDiaFestivo, long idObra) throws Exception {
		try {
			return this.ifzNegociacion.comprobarNegociacion(idDiaFestivo, idObra, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.comprobarNegociacion(idDiaFestivo, idObra)", e);
			throw e;
		}
	}

	@Override
	public boolean validarNegociacion(long idDiaFestivo, long idObra) throws Exception {
		List<DiasFestivosNegociacion> negociaciones = null;
		
		try {
			negociaciones = this.comprobarNegociacion(idDiaFestivo, idObra);
			return ! (negociaciones == null || negociaciones.isEmpty());
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.validarNegociacion(idDiaFestivo, idObra)", e);
			throw e;
		}
	}

	@Override
	public List<DiasFestivosNegociacion> cancelables(Date fecha, long idObra) throws Exception {
		List<DiasFestivosNegociacion> cancelables = new ArrayList<DiasFestivosNegociacion>();
		List<DiasFestivosNegociacion> negociaciones = null;
		
		try {
			negociaciones = this.findAll(idObra, 0);
			if (negociaciones != null && ! negociaciones.isEmpty()) {
				for (DiasFestivosNegociacion negociacion : negociaciones) {
					if (fecha.after(negociacion.getFecha()))
						cancelables.add(negociacion);
				}
			}
			
			return cancelables;
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosNegociacionFac.cancelables(fecha, idObra)", e);
			throw e;
		}
	}

	// ---------------------------------------------------------------------------------------------------------------
	// PRIVADOS 
	// ---------------------------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		Long idEmpresa = 1L;
		
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getId();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}

	private Long getCodigoEmpresa() {
		Long idEmpresa = 1L;
		
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getCodigo();//.getId();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}
}
