package net.giro.tyg.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.dao.FormasPagosDAO;

@Stateless
public class FormasPagosFac implements FormasPagosRem {
	private static Logger log = Logger.getLogger(FormasPagosFac.class);
	
	private InitialContext ctx;
	private FormasPagosDAO ifzFormasPagos;
	
 	public FormasPagosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            
            this.ctx = new InitialContext(p);
            
            this.ifzFormasPagos = (FormasPagosDAO) ctx.lookup("ejb:/Model_TYG//FormasPagosImp!net.giro.tyg.dao.FormasPagosDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaPagosFac", e);
			ctx = null;
		}
	}

	@Override
	public long save(FormasPagos entity) throws ExcepConstraint {
		try {
			return this.ifzFormasPagos.save(entity);
		} catch (Exception re) {
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public void update(FormasPagos entity) throws ExcepConstraint {
		try {
			this.ifzFormasPagos.update(entity);
		} catch (Exception re) {
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public void delete(FormasPagos entity) throws ExcepConstraint {
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
}
