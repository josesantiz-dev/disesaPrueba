package net.giro.tyg.dao;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Remote
public interface MonedasValoresDAO extends DAO<MonedasValores> {
	public List<MonedasValores> findByBaseDestino(Long monedaBase, Long monedaDestino);
	
	public List<MonedasValores> findMonedaAFecha(long monedaId, Date fechaMovimiento);
	
	public boolean rangoFechasValido(long moneda, Date inicio, Date fin, Long idMonedaValor);
	
	public List<MonedasValores> findByProperty(String propertyName, final Object value, int limite) throws RuntimeException;

	public List<MonedasValores> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<MonedasValores> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<MonedasValores> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<MonedasValores> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public MonedasValores findActual(Moneda monedaBase, Moneda monedaDestino) throws Exception;
	
	public List<MonedasValores> findByFecha(Moneda monedaBase, Moneda monedaDestino, Date fecha, int limite) throws Exception;
	
	public List<MonedasValores> findByFechas(Moneda monedaBase, Moneda monedaDestino, Date fechaDesde, Date fechaHasta, int limite) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-07-11 | Javier Tirado 	| Añado nuevos metodos de busqueda
 */