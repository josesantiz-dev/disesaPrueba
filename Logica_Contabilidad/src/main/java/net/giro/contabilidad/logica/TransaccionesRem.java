package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.beans.TransaccionesExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface TransaccionesRem {
	public void showSystemOuts(boolean value);
	
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Transacciones entity) throws Exception;

	public List<Transacciones> saveOrUpdateList(List<Transacciones> entities) throws Exception;

	public void update(Transacciones entity) throws Exception;

	public void delete(long idTransaccion) throws Exception;

	public Transacciones findById(long idTransaccion);

	public Transacciones findByCodigo(long codigoTransaccion) throws Exception;

	public Long findIdByCodigo(long codigoTransaccion) throws Exception;
	
	public List<Transacciones> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Transacciones> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Transacciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Transacciones> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Transacciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	/**
	 * Metodo para comprobar si el codigo indicado es valido
	 * @param idTransaccion 
	 * @param codigo
	 * @return True si el codigo es valido, en caso contratio False
	 * @throws Exception
	 */
	public boolean comprobarCodigo(Long idTransaccion, Long codigo) throws Exception;
	
	public long generarCodigo() throws Exception;

	public Transacciones convertir(TransaccionesExt entityExt) throws Exception;
	
	public TransaccionesExt convertir(Transacciones entity) throws Exception;

	// ----------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------------------------------------------------------------

	public Long save(TransaccionesExt entityExt) throws Exception;

	public void update(TransaccionesExt entityExt) throws Exception;
	
	public TransaccionesExt findExtById(long idTransaccion) throws Exception;
	
	public TransaccionesExt findExtByCodigo(long codigoTransaccion) throws Exception;
	
	public List<TransaccionesExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<TransaccionesExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<TransaccionesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<TransaccionesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<TransaccionesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}