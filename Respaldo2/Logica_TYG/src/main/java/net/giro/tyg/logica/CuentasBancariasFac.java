package net.giro.tyg.logica;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.dao.CuentasBancariasDAO;

@Stateless
public class CuentasBancariasFac implements CuentasBancariasRem {
	private static Logger log = Logger.getLogger(CuentasBancariasFac.class);
	private InitialContext ctx = null;
	private CuentasBancariasDAO ifzCtasBanco;
	private InfoSesion infoSesion;
    
    public CuentasBancariasFac() throws Exception { 
    	Hashtable<String, Object> p = new Hashtable<String, Object>();
        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");            
        ctx = new InitialContext(p);
		this.ifzCtasBanco = (CuentasBancariasDAO) ctx.lookup("ejb:/Model_TYG//CuentasBancariasImp!net.giro.tyg.dao.CuentasBancariasDAO");
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public long save(CuentaBancaria entity) throws Exception {
		try {
			this.ifzCtasBanco.save(entity, getIdEmpresa());
			return entity.getId();
		} catch (Exception re) {
			log.error("error en Logica_TYG.ifzCtasBanco.save(CtasBanco entity)", re);
			throw re;
		}
	}

	@Override
	public void delete(CuentaBancaria entity) throws Exception {
		try {
			this.ifzCtasBanco.delete(entity);
		} catch (Exception re) {
			log.error("error en Logica_TYG.ifzCtasBanco.save(CtasBanco entity)", re);
			throw re;
		}
	}

	@Override
	public CuentaBancaria update(CuentaBancaria entity) throws Exception {
		try {
			this.ifzCtasBanco.update(entity);
			return entity;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public CuentaBancaria findById(long id) {
		try {
			return this.ifzCtasBanco.findById(id);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public CuentaBancaria findAllById(long id) {
		try {
			return this.ifzCtasBanco.findAllById(id);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<CuentaBancaria> findAllByProperty(String propertyName, String value, int maximo, String sucursales) {
		try {
			return this.ifzCtasBanco.findAllByProperty(propertyName, value, maximo, sucursales);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<CuentaBancaria> findLikeClaveNombreCuenta(String value, int max, String sucursales, Long empresaId) {
		try {
			return this.ifzCtasBanco.findLikeClaveNombreCuenta(value, max, sucursales, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<CuentaBancaria> findByProperty(String propertyName, final Object value, String sucursales) {
		try {
			return this.ifzCtasBanco.findByProperty(propertyName, value, sucursales);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<CuentaBancaria> findLikeColumnName(String tipoBusqueda, String valorBusqueda) {
		try {
			System.out.println("---> LOGICA: findLikeColumnName('" + tipoBusqueda + "', '" + valorBusqueda + "')");
			return this.ifzCtasBanco.findLikeColumnName(tipoBusqueda, valorBusqueda);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
 	public List<CuentaBancaria> findAll(String sucursales) {
		try {
			return this.ifzCtasBanco.findAll(sucursales);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<CuentaBancaria> findTodas() {
		try {
			return this.ifzCtasBanco.findTodas();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public boolean esBancoCierre(final Object value) {
		try {
			return this.ifzCtasBanco.esBancoCierre(value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public String aplicaCargo(Double monto, CuentaBancaria ctaBancaria){
		try{
			ctaBancaria.setSaldo(ctaBancaria.getSaldo().add(BigDecimal.valueOf(monto)));
			return "OK";
		}catch (Exception re) {
			throw re;
		}
	}

	@Override
	public String aplicaCargoAbono(double monto, CuentaBancaria ctaBancaria, String movto) throws Exception{
		try{
			BigDecimal mto = new BigDecimal(monto);
			BigDecimal saldo = ctaBancaria.getSaldo();
			saldo = saldo.setScale(2,BigDecimal.ROUND_HALF_UP);
			mto = mto.setScale(2, BigDecimal.ROUND_HALF_UP);
			if("+".equals(movto))
				saldo = saldo.add(mto);
			else if ("-".equals(movto))
				saldo = saldo.subtract(mto);
			ctaBancaria.setSaldo(saldo);
			ifzCtasBanco.update(ctaBancaria);
			return "OK";
		}catch (Exception re) {
			throw re;
		}
	}

	@Override
	public boolean haySaldoSuficiente(double montoSolicitado, long bancoId){
		boolean res = true;

		try {
			CuentaBancaria banco = this.ifzCtasBanco.findById(bancoId);  //findAllById(bancoId);
			res = banco!=null && banco.getSaldo().doubleValue() >= montoSolicitado;
		} catch (Exception e) {
			throw e;
		}
		return res;
	}

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	@SuppressWarnings("unused")
	private boolean puedeGuardar(Long id) {
		try {
			return this.ifzCtasBanco.findById((long) id) == null;
		} catch (Exception re) {
			throw re;
		}
	}
}
