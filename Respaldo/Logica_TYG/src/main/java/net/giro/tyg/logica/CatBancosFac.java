package net.giro.tyg.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.CatBancos;
import net.giro.tyg.dao.CatBancosDAO;

@Stateless
public class CatBancosFac implements CatBancosRem {
	private static Logger log = Logger.getLogger(CatBancosFac.class);
	
	private InitialContext ctx;
	private CatBancosDAO ifzCatBancos;
	
 	public CatBancosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            
            this.ctx = new InitialContext(p);
            
            this.ifzCatBancos = (CatBancosDAO) ctx.lookup("ejb:/Model_TYG//CatBancosImp!net.giro.tyg.dao.CatBancosDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaPagosFac", e);
			ctx = null;
		}
	}

	@Override
	public long save(CatBancos entity) throws ExcepConstraint {
		try {
			return this.ifzCatBancos.save(entity);
		} catch (Exception re) {
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public void update(CatBancos entity) throws ExcepConstraint {
		try {
			this.ifzCatBancos.update(entity);
		} catch (Exception re) {
			log.error("Error en el método update", re);
			throw re;
		}
	}

	@Override
	public void delete(CatBancos entity) throws ExcepConstraint {
		try {
			this.ifzCatBancos.delete(entity.getId());
		} catch (Exception re) {
			log.error("Error en el método delete", re);
			throw re;
		}
	}

	@Override
	public CatBancos findById(long id) {
		try {
			return this.ifzCatBancos.findById(id);
		} catch (Exception re) {
			log.error("Error en el método findById", re);
			throw re;
		}
	}

	@Override
	public List<CatBancos> findByColumnName(String columnName, Object value) {
		try {
			return this.ifzCatBancos.findByColumnName(columnName, value);
		} catch (Exception re) {
			log.error("Error en el método findByColumnName", re);
			throw re;
		}
	}

	@Override
	public List<CatBancos> findLikeColumnName(String columnName, String value) {
		try {
			return this.ifzCatBancos.findLikeColumnName(columnName, value);
		} catch (Exception re) {
			log.error("Error en el método findLikeColumnName", re);
			throw re;
		}
	}

}
