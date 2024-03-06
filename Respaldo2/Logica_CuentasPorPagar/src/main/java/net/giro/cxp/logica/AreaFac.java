package net.giro.cxp.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.Area;
import net.giro.rh.admon.dao.AreaDAO;

@Stateless
public class AreaFac implements AreaRem {
	private static Logger log = Logger.getLogger(AreaFac.class);
	private InitialContext ctx;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private AreaDAO ifzAreas;

	public AreaFac(){
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    		this.ifzAreas = (AreaDAO) ctx.lookup("ejb:/Model_RecHum//AreaImp!net.giro.rh.admon.dao.AreaDAO");
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear AreaFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(Area entity) throws Exception {
		try {
			return this.ifzAreas.save(entity, null);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Area> saveOrUpdateList(List<Area> entities) throws Exception {
		try {
			return this.ifzAreas.saveOrUpdateList(entities, null);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public void update(Area entity) throws Exception {
		try {
			this.ifzAreas.update(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void delete(Area entity) throws Exception {
		try {
			this.ifzAreas.delete(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public Area findById(Integer id) {
		try {
			return this.ifzAreas.findById(id);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Area> findAll() {
		try {
			return this.ifzAreas.findAll();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Area> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzAreas.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Area> findLikeClaveNombre(String value) {
		try {
			return this.ifzAreas.findLikeClaveNombre(value);
		} catch (Exception re) {
			throw re;
		}
	}
}
