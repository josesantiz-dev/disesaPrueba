package net.giro.tyg.logica;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.dao.CtasBancariasDAO;

@Stateless
public class CtasBancoFac implements CtasBancoRem {
	//private static Logger log = Logger.getLogger(CtasBancoFac.class);
	private InitialContext ctx = null;
	private CtasBancariasDAO ifzCtasBanco;
	
    
    public CtasBancoFac() throws Exception { 
    	Hashtable<String, Object> p = new Hashtable<String, Object>();
        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");            
        ctx = new InitialContext(p);
        
		this.ifzCtasBanco = (CtasBancariasDAO) ctx.lookup("ejb:/Model_TYG//CtasBancariasImp!net.giro.tyg.dao.CtasBancariasDAO");
	}
    
	public long save(CtasBanco entity) throws ExcepConstraint, RuntimeException {
		try {
			this.ifzCtasBanco.save(entity);
			return entity.getId();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unused")
	private boolean puedeGuardar(Short id) {
		try {
			return this.ifzCtasBanco.findById((long) id) == null;
		} catch (RuntimeException re) {
			throw re;
		}
		/*
		int num = 0;try {
			String queryString = "select count(model) from CtasBanco model where model.ctaBancoId =:id";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("id", id);
			num = Integer.valueOf(query.getSingleResult().toString());
		} catch (Exception re) {
			num = 1;
		}
		return num == 0;*/
	}
	
	public void delete(CtasBanco entity) {
		try {
			this.ifzCtasBanco.delete(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public CtasBanco update(CtasBanco entity) throws ExcepConstraint {		
		try {
			this.ifzCtasBanco.update(entity);
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public CtasBanco findById(long id) {
		try {
			return this.ifzCtasBanco.findById(id);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public CtasBanco findAllById(long id) {
		try {
			return this.ifzCtasBanco.findAllById(id);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<CtasBanco> findAllByProperty(String propertyName, String value, int maximo, String sucursales) {
		try {
			return this.ifzCtasBanco.findAllByProperty(propertyName, value, maximo, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<CtasBanco> findLikeClaveNombreCuenta(String value, int max, String sucursales, Integer empresaId) {
		try {
			return this.ifzCtasBanco.findLikeClaveNombreCuenta(value, max, sucursales, empresaId);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<CtasBanco> findByProperty(String propertyName, final Object value, String sucursales) {
		try {
			return this.ifzCtasBanco.findByProperty(propertyName, value, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<CtasBanco> findLikeColumnName(String tipoBusqueda, String valorBusqueda) {
		try {
			System.out.println("---> LOGICA: findLikeColumnName('" + tipoBusqueda + "', '" + valorBusqueda + "')");
			return this.ifzCtasBanco.findLikeColumnName(tipoBusqueda, valorBusqueda);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
 	public List<CtasBanco> findAll(String sucursales) {
		try {
			return this.ifzCtasBanco.findAll(sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<CtasBanco> findTodas() {
		try {
			return this.ifzCtasBanco.findTodas();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public boolean esBancoCierre(final Object value) {
		try {
			return this.ifzCtasBanco.esBancoCierre(value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public String aplicaCargo(Double monto, CtasBanco ctaBancaria){
		try{
			ctaBancaria.setSaldo(ctaBancaria.getSaldo().add(BigDecimal.valueOf(monto)));
			return "OK";
		}catch (RuntimeException re) {
			throw re;
		}
	}

	public boolean haySaldoSuficiente(double montoSolicitado, long bancoId){
		boolean res = true;

		try {
			CtasBanco banco = this.ifzCtasBanco.findById(bancoId);  //findAllById(bancoId);
			res = banco!=null && banco.getSaldo().doubleValue() >= montoSolicitado;
		} catch (RuntimeException e) {
			throw e;
		}
		return res;
	}
}
