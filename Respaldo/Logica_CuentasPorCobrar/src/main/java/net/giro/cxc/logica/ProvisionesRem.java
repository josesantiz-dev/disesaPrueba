package net.giro.cxc.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.Provisiones;

@Remote
public interface ProvisionesRem {
	public Provisiones saveWithGrupo(Provisiones entity) throws Exception;
	
	public Long save(Provisiones entity) throws Exception;
	
	public Provisiones saveOrUpdate(Provisiones entity) throws Exception;
	
	public List<Provisiones> save(List<Provisiones> listEntities) throws Exception;

	public void update(Provisiones entity) throws Exception;

	public void delete(Long entityId) throws Exception;

	public void delete(Provisiones entity) throws Exception;
	
	public List<Provisiones> findAll() throws Exception;
	
	public List<Provisiones> findAllInactives() throws Exception;

	public List<Provisiones> findByProperty(String propertyName, Object value) throws Exception;

	public List<Provisiones> findByProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<Provisiones> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<Provisiones> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception;
	
	public List<Provisiones> findLikeProperty(String propertyName, String value) throws Exception;
	
	public List<Provisiones> findLikeProperty(String propertyName, String value, int limite) throws Exception;
	
	public List<Provisiones> findLikeProperties(HashMap<String, String> params) throws Exception;
	
	public List<Provisiones> findLikeProperties(HashMap<String, String> params, String orderBy, int limite) throws Exception;

	public Provisiones findProvision(Provisiones entityProvision) throws Exception;
	
	public Provisiones findProvision(Long entityIdFactura) throws Exception;
	
	public Provisiones findProvision(Factura entityFactura) throws Exception;
}

/*
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN
 *  ----------------------------------------------------------------------------------------------------------------
 *    2.1	| 28/09/2017 | Javier Tirado	| Creacion de ProvisionesRem
 */