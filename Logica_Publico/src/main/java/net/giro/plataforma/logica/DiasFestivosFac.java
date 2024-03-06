package net.giro.plataforma.logica;

import java.text.SimpleDateFormat;
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
import net.giro.plataforma.beans.DiasFestivos;
import net.giro.plataforma.dao.DiasFestivosDAO;

import org.apache.log4j.Logger;

@Stateless
public class DiasFestivosFac implements DiasFestivosRem {
	private static Logger log = Logger.getLogger(DiasFestivosFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private DiasFestivosDAO ifzDiasFestivos;

	public DiasFestivosFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzDiasFestivos = (DiasFestivosDAO) this.ctx.lookup("ejb:/Model_Publico//DiasFestivosImp!net.giro.plataforma.dao.DiasFestivosDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Publico.DiasFestivosFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion; 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(DiasFestivos entity) throws Exception {
		try {
			return this.ifzDiasFestivos.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<DiasFestivos> saveOrUpdateList(List<DiasFestivos> entities) throws Exception {
		try {
			return this.ifzDiasFestivos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(DiasFestivos entity) throws Exception {
		try {
			this.ifzDiasFestivos.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public DiasFestivos cancelar(DiasFestivos entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setModificadoPor(1L);
			if (this.infoSesion != null)
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzDiasFestivos.update(entity);
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public DiasFestivos findById(long idEntity) throws Exception {
		try {
			return this.ifzDiasFestivos.findById(idEntity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	public DiasFestivos findByDate(Date fecha) throws Exception {
		List<DiasFestivos> diasFestivos = null;
		
		try {
			diasFestivos = this.findAll(0);
			if (diasFestivos == null || diasFestivos.isEmpty())
				return null;

			for (DiasFestivos diaFestivo : diasFestivos) {
				if (diaFestivo.getDiaFestivo().equals(fecha) || diaFestivo.getDiaFeriado().equals(fecha)) 
					return diaFestivo;
			}
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.saveOrUpdateList(entities)", e);
			throw e;
		}

		return null;
	}

	@Override
	public List<DiasFestivos> findAll(int limite) throws Exception {
		try {
			return this.ifzDiasFestivos.findAll(getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.findAll(limite)", e);
			throw e;
		}
	}

	@Override
	public List<DiasFestivos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzDiasFestivos.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<DiasFestivos> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzDiasFestivos.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<DiasFestivos> comprobarDiaFestivo(int mes, int dia) throws Exception {
		try {
			return this.ifzDiasFestivos.comprobarDiaFestivo(mes, dia, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.comprobarDiaFestivo(mes, dia)", e);
			throw e;
		}
	}

	@Override
	public DiasFestivos comprobarDiaFestivo(Date fecha) throws Exception {
		List<DiasFestivos> diasFestivos = null;
		// --------------------------------------
		SimpleDateFormat formatter = null;
		Calendar calendar = null;
		
		try {
			calendar = Calendar.getInstance();
			calendar.setTime(fecha);
			diasFestivos = this.comprobarDiaFestivo(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
			if (diasFestivos == null || diasFestivos.isEmpty())
				return null;
			
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			for (DiasFestivos dia : diasFestivos) {
				if (formatter.format(fecha).equals(formatter.format(dia.getDiaFeriado()))) 
					return dia;
			}
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.comprobarDiaFestivo(fecha)", e);
			throw e;
		}
		
		return null;
	}

	@Override
	public boolean validarDiaFestivo(Date fecha) throws Exception {
		List<DiasFestivos> diasFestivos = null;
		Calendar calendar = null;
		
		try {
			calendar = Calendar.getInstance();
			calendar.setTime(fecha);
			diasFestivos = this.comprobarDiaFestivo(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
			if (diasFestivos == null)
				diasFestivos = new ArrayList<DiasFestivos>();
			return diasFestivos.size() > 0;
		} catch (Exception e) {
			log.error("error en Logica_Publico.DiasFestivosFac.comprobarDiaFestivo(fecha)", e);
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
