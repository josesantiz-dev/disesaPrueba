package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Checador;
import net.giro.rh.admon.dao.ChecadorDAO;

@Stateless
public class ChecadorFac implements ChecadorRem {
	private static Logger log = Logger.getLogger(ChecadorFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ChecadorDAO ifzChecador;
	private static String orderBy;
	
	
	public ChecadorFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzChecador = (ChecadorDAO) this.ctx.lookup("ejb:/Model_RecHum//ChecadorImp!net.giro.rh.admon.dao.ChecadorDAO");
        } catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.ChecadorFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void orderBy(String orderBy) {
		ChecadorFac.orderBy = orderBy;
	}

	@Override
	public Long save(Checador Checador) throws Exception {
		try {
			return this.ifzChecador.save(Checador, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.save(Checador)", e);
			throw e;
		}
	}

	@Override
	public List<Checador> saveOrUpdateList(List<Checador> entities) throws Exception {
		try {
			return this.ifzChecador.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.save(Checador)", e);
			throw e;
		}
	}

	@Override
	public void update(Checador Checador) throws Exception {
		try {
			this.ifzChecador.update(Checador);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.update(Checador)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long Checador) throws Exception {
		try {
			this.ifzChecador.delete(Checador);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public Checador findById(Long id) {
		try {
			return this.ifzChecador.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<Checador> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzChecador.orderBy(orderBy);
			return this.ifzChecador.findByProperty(propertyName, value, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Checador> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzChecador.orderBy(orderBy);
			return this.ifzChecador.findByProperties(params, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findExtByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Checador> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		try {
			this.ifzChecador.orderBy(orderBy);
			return this.ifzChecador.findLikeProperty(propertyName, value, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Checador> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzChecador.orderBy(orderBy);
			return this.ifzChecador.findByProperties(params, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Checador> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzChecador.orderBy(orderBy);
			return this.ifzChecador.findInProperty(columnName, values, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension) throws Exception {
		return null;
	}

	@Override
	public List<Checador> findByDate(Date fecha, String obra) throws Exception {
		try {
			return this.ifzChecador.findByDate(fecha, obra, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findByDate(Date fecha, String obra)", e);
			throw e;
		}
	}
	
	@Override
	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta) throws Exception {
		try {
			return this.ifzChecador.findByDates(fechaDesde, fechaHasta, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findByDates(fechaDesde, fechaHasta)", e);
			throw e;
		}
	}

	// ------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}
