package net.giro.tyg.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.dao.BancosDAO;

@Stateless
public class BancosFac implements BancosRem {
	private static Logger log = Logger.getLogger(BancosFac.class);
	private InitialContext ctx;
	private BancosDAO ifzCatBancos;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
 	public BancosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzCatBancos = (BancosDAO) ctx.lookup("ejb:/Model_TYG//BancosImp!net.giro.tyg.dao.BancosDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaPagosFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public long save(Banco entity) throws Exception {
		try {
			return this.ifzCatBancos.save(entity, null);
		} catch (Exception re) {
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public List<Banco> saveOrUpdateList(List<Banco> entities) throws Exception {
		try {
			return this.ifzCatBancos.saveOrUpdateList(entities, null);
		} catch (Exception e) {
			log.error("error en Logica_Publico.FACADE.saveOrUpdateList(List<CatBancos> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(Banco entity) throws Exception {
		try {
			this.ifzCatBancos.update(entity);
		} catch (Exception re) {
			log.error("Error en el método update", re);
			throw re;
		}
	}

	@Override
	public void delete(Banco entity) throws Exception {
		try {
			this.ifzCatBancos.delete(entity.getId());
		} catch (Exception re) {
			log.error("Error en el método delete", re);
			throw re;
		}
	}

	@Override
	public Banco findById(long id) {
		try {
			return this.ifzCatBancos.findById(id);
		} catch (Exception re) {
			log.error("Error en el método findById", re);
			throw re;
		}
	}

	@Override
	public List<Banco> findByColumnName(String columnName, Object value) {
		try {
			return this.ifzCatBancos.findByColumnName(columnName, value);
		} catch (Exception re) {
			log.error("Error en el método findByColumnName", re);
			throw re;
		}
	}

	@Override
	public List<Banco> findLikeColumnName(String columnName, String value) {
		try {
			return this.ifzCatBancos.findLikeColumnName(columnName, value);
		} catch (Exception re) {
			log.error("Error en el método findLikeColumnName", re);
			throw re;
		}
	}

}
