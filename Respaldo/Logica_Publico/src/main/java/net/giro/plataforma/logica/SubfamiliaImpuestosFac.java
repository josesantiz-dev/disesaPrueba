package net.giro.plataforma.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.beans.SubfamiliaImpuestos;
import net.giro.plataforma.dao.SubfamiliaImpuestosDAO;

@Stateless
public class SubfamiliaImpuestosFac implements SubfamiliaImpuestosRem {
	private static Logger log = Logger.getLogger(SubfamiliaImpuestosFac.class);
	private InitialContext ctx;
	private SubfamiliaImpuestosDAO ifzSubfamiliaImpuestos;

	public SubfamiliaImpuestosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzSubfamiliaImpuestos = (SubfamiliaImpuestosDAO) this.ctx.lookup("ejb:/Model_Publico//SubfamiliaImpuestosImp!net.giro.plataforma.dao.SubfamiliaImpuestosDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Publico.SubfamiliaImpuestosFac", e);
			ctx = null;
		}
	}

	@Override
	public Long save(SubfamiliaImpuestos entity) throws ExcepConstraint {
		try {
			return this.ifzSubfamiliaImpuestos.save(entity);
		} catch (Exception e) {
			log.error("error en MODULO.SubfamiliaImpuestosFac.save(SubfamiliaImpuestos)", e);
			throw e;
		}
	}

	@Override
	public void update(SubfamiliaImpuestos entity) throws ExcepConstraint {
		try {
			this.ifzSubfamiliaImpuestos.update(entity);
		} catch (Exception e) {
			log.error("error en MODULO.SubfamiliaImpuestosFac.update(SubfamiliaImpuestos)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzSubfamiliaImpuestos.delete(entityId);
		} catch (Exception e) {
			log.error("error en MODULO.SubfamiliaImpuestosFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public SubfamiliaImpuestos findById(Long id) {
		try {
			return this.ifzSubfamiliaImpuestos.findById(id);
		} catch (Exception e) {
			log.error("error en MODULO.SubfamiliaImpuestosFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findAll() throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findAll();
		} catch (Exception e) {
			log.error("error en MODULO.SubfamiliaImpuestosFac.findAll()", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findByProperty(String propertyName, Object value) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findByProperty(propertyName, value, 0);
		} catch (Exception e) {
			log.error("error en MODULO.SubfamiliaImpuestosFac.findByProperty(propertyName, value)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findByProperties(params, 0);
		} catch (Exception e) {
			log.error("error en MODULO.SubfamiliaImpuestosFac.findByProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findLikeProperty(String propertyName, String value) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findLikeProperty(propertyName, value, 0);
		} catch (Exception e) {
			log.error("error en MODULO.SubfamiliaImpuestosFac.findLikeProperty(propertyName, value)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findLikeProperties(HashMap<String, String> params) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findLikeProperties(params, 0);
		} catch (Exception e) {
			log.error("error en MODULO.SubfamiliaImpuestosFac.findLikeProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findInProperty(String columnName, List<Object> values) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findInProperty(columnName, values, 0);
		} catch (Exception e) {
			log.error("error en MODULO.SubfamiliaImpuestosFac.findInProperty(columnName, values)", e);
			throw e;
		}
	}
}
