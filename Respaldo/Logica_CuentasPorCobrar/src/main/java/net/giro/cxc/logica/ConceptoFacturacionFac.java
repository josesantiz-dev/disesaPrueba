package net.giro.cxc.logica;

import java.util.Hashtable;
import java.util.List;

import net.giro.cxc.beans.ConceptoFacturacion;
import net.giro.cxc.dao.ConceptoFacturacionDAO;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

@Stateless
public class ConceptoFacturacionFac implements ConceptoFacturacionRem {
	private static Logger log = Logger.getLogger(ConceptoFacturacionFac.class);
	private InitialContext ctx;
	private ConceptoFacturacionDAO ifzConceptoFacturacion;
	
	
	public ConceptoFacturacionFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try{
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzConceptoFacturacion = (ConceptoFacturacionDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//ConceptoFacturacionImp!net.giro.cxc.dao.ConceptoFacturacionDAO");
		} catch(Exception e) {
			log.error("Error en el método de contexto");
			this.ctx = null;
		}
	}
	

	@Override
	public Long save(ConceptoFacturacion entity) throws Exception {
		try {
			return this.ifzConceptoFacturacion.save(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void update(ConceptoFacturacion entity) throws Exception {
		try {
			this.ifzConceptoFacturacion.update(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void delete(ConceptoFacturacion entity) throws Exception {
		try {
			this.ifzConceptoFacturacion.delete(entity.getId());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public ConceptoFacturacion findById(Long id) throws Exception {
		try {
			return this.ifzConceptoFacturacion.findById(id);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findAll() throws Exception {
		try {
			return this.ifzConceptoFacturacion.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzConceptoFacturacion.findByProperty(propertyName, value, limite);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		try {
			return this.ifzConceptoFacturacion.findLikeProperty(propertyName, value, limite);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findByPropertyPojoCompleto(String propertyName, String tipo, long value) throws Exception {
		try {
			return this.ifzConceptoFacturacion.findByPropertyPojoCompleto(propertyName, tipo, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
