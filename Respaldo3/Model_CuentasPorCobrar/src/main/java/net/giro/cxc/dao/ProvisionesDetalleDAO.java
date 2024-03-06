package net.giro.cxc.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.ProvisionesDetalle;

@Remote
public interface ProvisionesDetalleDAO extends DAO<ProvisionesDetalle> {
	public long save(ProvisionesDetalle entity, long codigoEmpresa) throws Exception;
	
	public List<ProvisionesDetalle> saveOrUpdateList(List<ProvisionesDetalle> entities, long codigoEmpresa) throws Exception;
	
	public List<ProvisionesDetalle> findAll(Long idProvisiones) throws Exception;
	
	public List<ProvisionesDetalle> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ProvisionesDetalle> findLikeProperty(String propertyName, final String value, int limite) throws Exception;

	public List<ProvisionesDetalle> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception;

	public List<ProvisionesDetalle> findLikeProperties(HashMap<String, String> params, String orderBy, int limite) throws Exception;
	
	public List<ProvisionesDetalle> findInProperty(String propertyName, List<Object> values, int limite) throws Exception;
}

/*
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN
 *  ----------------------------------------------------------------------------------------------------------------
 *    2.1	| 28/09/2017 | Javier Tirado	| Creacion de ProvisionesDetalleDAO
 */