package net.giro.tyg.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.dao.FormasPagosDAO;

@Stateless
public class FormasPagosFac implements FormasPagosRem {
	private static Logger log = Logger.getLogger(FormasPagosFac.class);
	private InitialContext ctx;
	private FormasPagosDAO ifzFormasPagos;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
 	public FormasPagosFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzFormasPagos = (FormasPagosDAO) ctx.lookup("ejb:/Model_TYG//FormasPagosImp!net.giro.tyg.dao.FormasPagosDAO");
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaPagosFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public long save(FormasPagos entity) throws Exception {
		try {
			return this.ifzFormasPagos.save(entity, null);
		} catch (Exception re) {
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public void update(FormasPagos entity) throws Exception {
		try {
			this.ifzFormasPagos.update(entity);
		} catch (Exception re) {
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public void delete(FormasPagos entity) throws Exception {
		try {
			this.ifzFormasPagos.delete(entity.getId());
		} catch (Exception re) {
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public FormasPagos findById(long id) {
		try {
			return this.ifzFormasPagos.findById(id);
		} catch (Exception re) {
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public List<FormasPagos> findByColumnName(String columnName, Object value) {
		try {
			return this.ifzFormasPagos.findByColumnName(columnName, value);
		} catch (Exception re) {
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public List<FormasPagos> findLikeColumnName(String columnName, String value) {
		try {
			return this.ifzFormasPagos.findLikeColumnName(columnName, value);
		} catch (Exception re) {
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public List<FormasPagos> findAll(String orderBy) throws Exception {
		try {
			return this.ifzFormasPagos.findAll(orderBy);
		} catch (Exception re) {
			log.error("Error en el método findAll", re);
			throw re;
		}
	}

	@Override
	public List<FormasPagos> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzFormasPagos.findByProperty(propertyName, value, orderBy, limite);
		} catch (Exception re) {
			log.error("Error en el método findByProperty", re);
			throw re;
		}
	}

	@Override
	public List<FormasPagos> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzFormasPagos.findLikeProperty(propertyName, value, orderBy, limite);
		} catch (Exception re) {
			log.error("Error en el método findLikeProperty", re);
			throw re;
		}
	}
}
