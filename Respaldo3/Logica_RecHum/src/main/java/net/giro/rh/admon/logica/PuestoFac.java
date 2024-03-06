package net.giro.rh.admon.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.Puesto;
import net.giro.rh.admon.dao.PuestoDAO;

@Stateless
public class PuestoFac implements PuestoRem {
	private static Logger log = Logger.getLogger(PuestoFac.class);
	private InitialContext ctx;
	private PuestoDAO ifzPuesto;
	private InfoSesion infoSesion;
	
	public PuestoFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzPuesto = (PuestoDAO) this.ctx.lookup("ejb:/Model_RecHum//PuestoImp!net.giro.rh.admon.dao.PuestoDAO");
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear PuestoFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(Puesto entity) throws Exception {
		try {
			return this.ifzPuesto.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Puesto> saveOrUpdateList(List<Puesto> entities) throws Exception {
		try {
			return this.ifzPuesto.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(Puesto entity) throws Exception {
		try {
			this.ifzPuesto.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(long idPuesto) throws Exception {
		try {
			this.ifzPuesto.delete(idPuesto);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public Puesto findById(long idPuesto) throws Exception {
		try {
			return this.ifzPuesto.findById(idPuesto);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Puesto> findAll() throws Exception {
		try {
			return this.findAll(false);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Puesto> findAll(boolean incluyeEliminados) throws Exception {
		try {
			return this.ifzPuesto.findAll(incluyeEliminados, getCodigoEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Puesto> findByProperty(String propertyName, Object value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, false);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Puesto> findByProperty(String propertyName, Object value, boolean incluyeEliminados) throws Exception {
		try {
			return this.ifzPuesto.findByProperty(propertyName, value, incluyeEliminados, getCodigoEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Puesto> findLikeProperty(String propertyName, String value) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, false);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Puesto> findLikeProperty(String propertyName, String value, boolean incluyeEliminados) throws Exception {
		try {
			return this.ifzPuesto.findLikeProperty(propertyName, value, incluyeEliminados, getCodigoEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}
	
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
