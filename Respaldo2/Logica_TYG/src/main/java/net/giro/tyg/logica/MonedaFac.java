package net.giro.tyg.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.plataforma.InfoSesion;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.MonedaDAO;

@Stateless
public class MonedaFac implements MonedaRem {
	private InitialContext ctx = null;
	private MonedaDAO ifzMonedas;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
	public MonedaFac() throws Exception { 
    	Hashtable<String, Object> p = new Hashtable<String, Object>();
        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");            
        ctx = new InitialContext(p);
		this.ifzMonedas = (MonedaDAO) ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public long save(Moneda entity) throws Exception {
		try {
			return (long) this.ifzMonedas.save(entity, null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void delete(Moneda entity) throws Exception {
		try {
			this.ifzMonedas.delete(entity);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Moneda update(Moneda entity) throws Exception {
		try {
			this.ifzMonedas.update(entity);
			return entity;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Moneda findById(long idMoneda) throws Exception {
		try {
			return this.ifzMonedas.findById(idMoneda);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Moneda> findAll() throws Exception {
		try {
			return this.ifzMonedas.findAll();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Moneda> findByProperty(String propertyName, Object value) throws Exception {
		try {
			return this.ifzMonedas.findByProperty(propertyName, value);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Moneda> findByPropertyLike(String propertyName, String value) throws Exception {
		try {
			return this.ifzMonedas.findByPropertyLike(propertyName, value);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Moneda findByAbreviacion(String valor) throws Exception {
		try {
			return this.ifzMonedas.findByAbreviacion(valor);
		} catch (Exception e) {
			throw e;
		}
	}
}
