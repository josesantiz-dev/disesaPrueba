package net.giro.tyg.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.giro.plataforma.InfoSesion;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;

public interface MonedasValoresRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public long save(MonedasValores entity) throws Exception;

	public void delete(MonedasValores entity) throws Exception;

	public MonedasValores update(MonedasValores entity) throws Exception;

	public MonedasValores findById(Long id) throws Exception;

	public List<MonedasValores> findAll() throws Exception;

	public List<MonedasValores> findByProperty(String propertyName, Object value) throws Exception;
	
	public List<MonedasValores> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<MonedasValores> findMonedaAFecha(long monedaId, Date fechaMovimiento) throws Exception;
	
	public boolean rangoFechasValido(long moneda, Date inicio, Date fin, Long idMonedaValor) throws Exception;
	
	public List<MonedasValores> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<MonedasValores> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<MonedasValores> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<MonedasValores> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public MonedasValores findActual() throws Exception;

	public MonedasValores findActual(long monedaBaseId, long monedaDestinoId) throws Exception;

	public MonedasValores findActual(String monedaBaseAbreviatura, String monedaDestinoAbreviatura) throws Exception;

	public MonedasValores findActual(Moneda monedaBase, Moneda monedaDestino) throws Exception;

	public MonedasValores findActualPagos() throws Exception;

	public MonedasValores findActualPagos(long monedaBaseId, long monedaDestinoId) throws Exception;

	public MonedasValores findActualPagos(Moneda monedaBase, Moneda monedaDestino) throws Exception;

	public List<MonedasValores> findByFecha(Date fecha) throws Exception;
	
	public List<MonedasValores> findByFecha(long monedaBaseId, long monedaDestinoId, Date fecha, int limite) throws Exception;
	
	public List<MonedasValores> findByFecha(String monedaBaseAbreviatura, String monedaDestinoAbreviatura, Date fecha, int limite) throws Exception;
	
	public List<MonedasValores> findByFecha(Moneda monedaBase, Moneda monedaDestino, Date fecha, int limite) throws Exception;
	
	public List<MonedasValores> findByFechas(long monedaBaseId, long monedaDestinoId, Date fechaDesde, Date fechaHasta, int limite) throws Exception;
	
	public List<MonedasValores> findByFechas(String monedaBaseAbreviatura, String monedaDestinoAbreviatura, Date fechaDesde, Date fechaHasta, int limite) throws Exception;
	
	public List<MonedasValores> findByFechas(Moneda monedaBase, Moneda monedaDestino, Date fechaDesde, Date fechaHasta, int limite) throws Exception;

	public Double tipoCambio() throws Exception;

	public Double tipoCambio(Date fecha) throws Exception;

	public Double tipoCambioPagos() throws Exception;

	public Double tipoCambioPagos(Date fecha) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-07-11 | Javier Tirado 	| Añado nuevos metodos de busqueda
 */