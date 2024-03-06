package net.giro.adp.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ConceptoPresupuesto;
import net.giro.adp.dao.ConceptoPresupuestoDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ConceptoPresupuestoFac implements ConceptoPresupuestoRem {
	private static Logger log = Logger.getLogger(ConceptoPresupuestoFac.class);
	private InitialContext ctx;
	private ConceptoPresupuestoDAO ifzConceptoPresupuesto;
	private InfoSesion infoSesion;
	
	
	public ConceptoPresupuestoFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzConceptoPresupuesto = (ConceptoPresupuestoDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ConceptoPresupuestoImp!net.giro.adp.dao.ConceptoPresupuestoDAO"); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear ConceptoPresupuestoFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(ConceptoPresupuesto entity) throws Exception {
		try {
			return this.ifzConceptoPresupuesto.save(entity, getIdEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<ConceptoPresupuesto> saveOrUpdateList(List<ConceptoPresupuesto> entities) throws Exception {
		try {
			return this.ifzConceptoPresupuesto.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(ConceptoPresupuesto entity) throws Exception {
		try {
			this.ifzConceptoPresupuesto.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(ConceptoPresupuesto entity) throws Exception {
		try {
			this.ifzConceptoPresupuesto.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public ConceptoPresupuesto findById(long id) {
		try {
			return this.ifzConceptoPresupuesto.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<ConceptoPresupuesto> findAllActivos() {
		try {
			return this.ifzConceptoPresupuesto.findAllActivos();
		} catch (Exception re) {	
			log.error("Error en ConceptoPresupuestoFac.findAllActivos()", re);
			throw re;
		}
	}
	
	@Override
	public List<ConceptoPresupuesto> findByProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzConceptoPresupuesto.findByProperty(propertyName, value, max);
		} catch (Exception re) {	
			log.error("Error en ConceptoPresupuestoFac.findByProperty(propertyName, value, max)", re);
			throw re;
		}
	}

	@Override
	public List<ConceptoPresupuesto> findLikeProperty(String propertyName, String value, int max) {
		try {
			return this.ifzConceptoPresupuesto.findLikeProperty(propertyName, value, max);
		} catch (Exception re) {	
			log.error("Error en ConceptoPresupuestoFac.findLikeProperty(propertyName, value, max)", re);
			throw re;
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
}
