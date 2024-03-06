package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.MensajeTransaccion;

@Remote
public interface MensajeTransaccionDAO extends DAO<MensajeTransaccion> {
	public long save(MensajeTransaccion entity, long codigoEmpresa) throws Exception;
	
	public List<MensajeTransaccion> saveOrUpdateList(List<MensajeTransaccion> entities, long codigoEmpresa) throws Exception;

	public List<MensajeTransaccion> findByProperty(String propertyName, Object value, boolean incluyeContabilizados, boolean incluyeCancelados, long idEmpresa, String orderBy, int limite);

	public List<MensajeTransaccion> findLikeProperty(String propertyName, Object value, boolean incluyeContabilizados, boolean incluyeCancelados, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public MensajeTransaccion comprobarMensajeTransaccion(Long idTransaccion, Long idOperacion, long idEmpresa) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 14/07/2016 | Javier Tirado	| Creacion de MensajeTransaccionDAO