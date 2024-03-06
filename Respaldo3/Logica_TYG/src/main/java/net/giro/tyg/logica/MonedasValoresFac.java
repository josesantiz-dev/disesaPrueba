package net.giro.tyg.logica;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.tyg.dao.MonedasValoresDAO;

@Stateless
public class MonedasValoresFac implements MonedasValoresRem {
	private static Logger log = Logger.getLogger(MonedasValoresFac.class);
	private InitialContext ctx = null;
	private MonedasValoresDAO ifzMonedasValores;
	private MonedaDAO ifzMonedas;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
	public MonedasValoresFac() throws Exception { 
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzMonedasValores = (MonedasValoresDAO) this.ctx.lookup("ejb:/Model_TYG//MonedasValoresImp!net.giro.tyg.dao.MonedasValoresDAO");
			this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_TYG.MonedasValoresFac", e);
			this.ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public long save(MonedasValores entity) throws Exception {
		try {
			return this.ifzMonedasValores.save(entity, null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void delete(MonedasValores entity) throws Exception {
		try {
			this.ifzMonedasValores.delete(entity);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MonedasValores update(MonedasValores entity) throws Exception {
		try {
			this.ifzMonedasValores.update(entity);
			return entity;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MonedasValores findById(Long id) throws Exception {
		try {
			return this.ifzMonedasValores.findById(id);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findByProperty(String propertyName, Object value) throws Exception {
		try {
			return this.ifzMonedasValores.findLikeColumnName(propertyName, value.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findAll() throws Exception {
		try {
			return this.ifzMonedasValores.findAll();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findMonedaAFecha(long monedaId, Date fechaMovimiento) throws Exception {
		try {
			return this.ifzMonedasValores.findMonedaAFecha(monedaId, fechaMovimiento);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public boolean rangoFechasValido(long moneda, Date inicio, Date fin, Long idMonedaValor) throws Exception {
		try {
			return this.ifzMonedasValores.rangoFechasValido(moneda, inicio, fin, idMonedaValor);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzMonedasValores.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			return this.ifzMonedasValores.findByProperties(params, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzMonedasValores.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			return this.ifzMonedasValores.findLikeProperties(params, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			return this.ifzMonedasValores.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MonedasValores findActual() throws Exception {
		Moneda monedaBase = null;
		Moneda monedaDestino = null;
		
		try {
			monedaBase = this.getMonedaByAbreviatura("MXN");
			monedaDestino = this.getMonedaByAbreviatura("USD");
			return this.findActual(monedaBase, monedaDestino);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MonedasValores findActual(long monedaBaseId, long monedaDestinoId) throws Exception {
		Moneda monedaBase = null;
		Moneda monedaDestino = null;
		
		try {
			monedaBase = this.ifzMonedas.findById(monedaBaseId);
			monedaDestino = this.ifzMonedas.findById(monedaDestinoId);
			return this.findActual(monedaBase, monedaDestino);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public MonedasValores findActual(String monedaBaseAbreviatura, String monedaDestinoAbreviatura) throws Exception {
		Moneda monedaBase = null;
		Moneda monedaDestino = null;
		
		try {
			monedaBase = this.getMonedaByAbreviatura(monedaBaseAbreviatura);
			monedaDestino = this.getMonedaByAbreviatura(monedaDestinoAbreviatura);
			return this.findActual(monedaBase, monedaDestino);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MonedasValores findActual(Moneda monedaBase, Moneda monedaDestino) throws Exception {
		try {
			return this.ifzMonedasValores.findActual(monedaBase, monedaDestino);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MonedasValores findActualPagos() throws Exception {
		Moneda monedaBase = null;
		Moneda monedaDestino = null;
		
		try {
			monedaBase = this.getMonedaByAbreviatura("MXN");
			monedaDestino = this.getMonedaByAbreviatura("USD");
			return this.findActualPagos(monedaBase, monedaDestino);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MonedasValores findActualPagos(long monedaBaseId, long monedaDestinoId) throws Exception {
		Moneda monedaBase = null;
		Moneda monedaDestino = null;
		
		try {
			monedaBase = this.ifzMonedas.findById(monedaBaseId);
			monedaDestino = this.ifzMonedas.findById(monedaDestinoId);
			return this.findActualPagos(monedaBase, monedaDestino);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MonedasValores findActualPagos(Moneda monedaBase, Moneda monedaDestino) throws Exception {
		List<MonedasValores> valores = null;
		MonedasValores valor = null;
		Calendar cal = null;
		
		try {
			cal = Calendar.getInstance();
			cal.setTime(Calendar.getInstance().getTime());
			do {
				cal.add(Calendar.DAY_OF_MONTH, -1);
			} while (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
			valores = this.findByFecha(monedaBase, monedaDestino, cal.getTime(), 0);
			if (valores != null && ! valores.isEmpty())
				valor = valores.get(0);
			return valor;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findByFecha(Date fecha) throws Exception {
		Moneda monedaBase = null;
		Moneda monedaDestino = null;
		
		try {
			monedaBase = this.getMonedaByAbreviatura("MXN");
			monedaDestino = this.getMonedaByAbreviatura("USD");
			return this.findByFecha(monedaBase, monedaDestino, fecha, 0);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findByFecha(long monedaBaseId, long monedaDestinoId, Date fecha, int limite) throws Exception {
		Moneda monedaBase = null;
		Moneda monedaDestino = null;
		
		try {
			monedaBase = this.ifzMonedas.findById(monedaBaseId);
			monedaDestino = this.ifzMonedas.findById(monedaDestinoId);
			return this.findByFecha(monedaBase, monedaDestino, fecha, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findByFecha(String monedaBaseAbreviatura, String monedaDestinoAbreviatura, Date fecha, int limite) throws Exception {
		Moneda monedaBase = null;
		Moneda monedaDestino = null;
		
		try {
			monedaBase = this.getMonedaByAbreviatura(monedaBaseAbreviatura);
			monedaDestino = this.getMonedaByAbreviatura(monedaDestinoAbreviatura);
			return this.findByFecha(monedaBase, monedaDestino, fecha, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findByFecha(Moneda monedaBase, Moneda monedaDestino, Date fecha, int limite) throws Exception {
		try {
			return this.ifzMonedasValores.findByFecha(monedaBase, monedaDestino, fecha, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findByFechas(long monedaBaseId, long monedaDestinoId, Date fechaDesde, Date fechaHasta, int limite) throws Exception {
		Moneda monedaBase = null;
		Moneda monedaDestino = null;
		
		try {
			monedaBase = this.ifzMonedas.findById(monedaBaseId);
			monedaDestino = this.ifzMonedas.findById(monedaDestinoId);
			return this.findByFechas(monedaBase, monedaDestino, fechaDesde, fechaHasta, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findByFechas(String monedaBaseAbreviatura, String monedaDestinoAbreviatura, Date fechaDesde, Date fechaHasta, int limite) throws Exception {
		Moneda monedaBase = null;
		Moneda monedaDestino = null;
		
		try {
			monedaBase = this.getMonedaByAbreviatura(monedaBaseAbreviatura);
			monedaDestino = this.getMonedaByAbreviatura(monedaDestinoAbreviatura);
			return this.findByFechas(monedaBase, monedaDestino, fechaDesde, fechaHasta, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MonedasValores> findByFechas(Moneda monedaBase, Moneda monedaDestino, Date fechaDesde, Date fechaHasta, int limite) throws Exception {
		try {
			return this.ifzMonedasValores.findByFechas(monedaBase, monedaDestino, fechaDesde, fechaHasta, limite);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private Moneda getMonedaByAbreviatura(String valor) {
		Moneda resultado = null;
		
		try {
			List<Moneda> lista = this.ifzMonedas.findByProperty("abreviacion", valor);
			if (lista != null && ! lista.isEmpty())
				resultado = lista.get(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar la moneda " + valor, e);
			resultado = null;
		}
		
		return resultado;
	}
	
	@Override
	public Double tipoCambio() throws Exception {
		/*MonedasValores valor = null;
		
		try {
			valor = this.findActual();
			if (valor == null || valor.getId() == null || valor.getId() <= 0L)
				return 1.0;
			return valor.getValor().doubleValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar la tipo de cambio actual " + valor, e);
			throw e;
		}*/
		
		return tipoCambio(Calendar.getInstance().getTime());
	}

	@Override
	public Double tipoCambio(Date fecha) throws Exception {
		List<MonedasValores> valores = null;
		MonedasValores valor = null;
		double tipoCambio = 0;
		// ---------------------------------------
		Calendar calendar = null;
		int factor = 0;
		int dia = 0;
		
		try {
    		calendar = Calendar.getInstance();
    		calendar.setTime(fecha);
    		factor = 0;
    		dia = calendar.get(Calendar.DAY_OF_WEEK);
    		if (dia == Calendar.SUNDAY) 
    			factor = 2;
    		if (dia == Calendar.SATURDAY)
    			factor = 1;
    		dia = factor * -1;
			calendar.add(Calendar.DAY_OF_YEAR, dia);
    		
    		// Tipo de Cambio del dia
			fecha = calendar.getTime();
			valores = this.findByFecha(fecha);
			valor = valores.get(0);
			tipoCambio = valor.getValor().doubleValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Tipo de Cambio", e);
			tipoCambio = 0;
		} finally {
			tipoCambio = (tipoCambio > 0 ? tipoCambio : 1);
		}
		
		return tipoCambio;
	}

	@Override
	public Double tipoCambioPagos() throws Exception {
		/*MonedasValores valor = null;
		
		try {
			valor = this.findActualPagos();
			if (valor == null || valor.getId() == null || valor.getId() <= 0L)
				return 1.0;
			return valor.getValor().doubleValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar la tipo de cambio actual para pagos" + valor, e);
			throw e;
		}*/
		
		return tipoCambioPagos(Calendar.getInstance().getTime());
	}

	@Override
	public Double tipoCambioPagos(Date fecha) throws Exception {
		List<MonedasValores> valores = null;
		MonedasValores valor = null;
		double tipoCambio = 0;
		// ---------------------------------------
		Calendar calendar = null;
		int factor = 0;
		int dia = 0;
		
		try {
    		calendar = Calendar.getInstance();
    		calendar.setTime(fecha);
    		factor = 1;
    		dia = calendar.get(Calendar.DAY_OF_WEEK);
    		if (dia == Calendar.MONDAY)
    			factor = 3;
    		if (dia == Calendar.SUNDAY) 
    			factor = 2;
    		if (dia == Calendar.SATURDAY)
    			factor = 1;
    		dia = factor * -1;
			calendar.add(Calendar.DAY_OF_YEAR, dia);
    		
    		// Tipo de Cambio del dia
			fecha = calendar.getTime();
			valores = this.findByFecha(fecha);
			valor = valores.get(0);
			tipoCambio = valor.getValor().doubleValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Tipo de Cambio para Pagos", e);
			tipoCambio = 0;
		} finally {
			if (tipoCambio <= 0)
				tipoCambio = 1;
		}
		
		return tipoCambio;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-07-11 | Javier Tirado 	| Implemento nuevos metodos de busqueda
 */