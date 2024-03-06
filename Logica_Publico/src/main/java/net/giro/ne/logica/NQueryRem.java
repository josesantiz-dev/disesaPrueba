package net.giro.ne.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.ne.beans.NQuery;

@Remote
public interface NQueryRem {
	public NQuery findByProperty( String sql );
	
	/**
	 * 
	 * @param SrtQuery cadena con el query sql, "deben ir ya los parametros concatenados en el query" 
	 * @return lista de n campos solicitados en el query
	 */
	@SuppressWarnings("rawtypes")
	public List findNativeQuery(String SrtQuery);
	
	/**
	 * 
	 * @param SrtQuery cadena con el query en HQL o EJB-QL, los parametros deben ir definidos como :nombre_param dentro del query
	 * @param parametros deben ir string (nombre del parametro a buscar en el query) y Object (valor que se reemplazara en el query) 
	 * @return lista de n campos u objetos solicitados en el query
	 */
	@SuppressWarnings("rawtypes")
	public List findJPAQuery(String SrtQuery, HashMap<String, Object> parametros);
	
	/**
	 * @param strSentencia ejecuta updates y deletes
	 */
	public void ejecutaAccion(String strSentencia);
}
