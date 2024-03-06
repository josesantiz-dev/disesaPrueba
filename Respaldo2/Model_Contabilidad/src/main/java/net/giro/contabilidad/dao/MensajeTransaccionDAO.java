package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.MensajeTransaccion;

@Remote
public interface MensajeTransaccionDAO extends DAO<MensajeTransaccion> {
	public void orderBy(String orderBy);

	public void setEmpresa(Long idEmpresa);
	
	public long save(MensajeTransaccion entity, Long idEmpresa) throws Exception;
	
	public List<MensajeTransaccion> saveOrUpdateList(List<MensajeTransaccion> entities, Long idEmpresa) throws Exception;

	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value);
	
	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value, int limite);

	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params) throws Exception;

	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<MensajeTransaccion> findLikeProperty(String propertyName, String value) throws Exception;
	
	public List<MensajeTransaccion> findLikeProperty(String propertyName, String value, int limite) throws Exception;

	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params) throws Exception;
	
	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values) throws Exception;
	
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	
	public MensajeTransaccion comprobarMensajeTransaccion(Long idTransaccion, Long idOperacion) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 14/07/2016 | Javier Tirado	| Creacion de MensajeTransaccionDAO