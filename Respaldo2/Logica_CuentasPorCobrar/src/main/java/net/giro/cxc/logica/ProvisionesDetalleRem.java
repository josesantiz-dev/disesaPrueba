package net.giro.cxc.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.Provisiones;
import net.giro.cxc.beans.ProvisionesDetalle;
import net.giro.cxc.beans.ProvisionesDetalleExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ProvisionesDetalleRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(ProvisionesDetalle entity) throws Exception;
	
	public List<ProvisionesDetalle> saveOrUpdateList(List<ProvisionesDetalle> entities) throws Exception;

	public void update(ProvisionesDetalle entity) throws Exception;

	public void delete(Long entityId) throws Exception;

	public void delete(ProvisionesDetalle entity) throws Exception;
	
	public ProvisionesDetalle findById(Long entityId) throws Exception;
	
	public List<ProvisionesDetalle> findAll(Long idProvisiones) throws Exception;
	
	public List<ProvisionesDetalle> findAll(Provisiones entity) throws Exception;
	
	public List<ProvisionesDetalle> findByProperty(String propertyName, final Object value) throws Exception;
	
	public List<ProvisionesDetalle> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ProvisionesDetalle> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<ProvisionesDetalle> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception;

	public List<ProvisionesDetalle> findLikeProperty(String propertyName, final String value) throws Exception;

	public List<ProvisionesDetalle> findLikeProperty(String propertyName, final String value, int limite) throws Exception;

	public List<ProvisionesDetalle> findLikeProperties(HashMap<String, String> params) throws Exception;

	public List<ProvisionesDetalle> findLikeProperties(HashMap<String, String> params, String orderBy, int limite) throws Exception;
	
	public List<ProvisionesDetalle> findInProperty(String propertyName, List<Object> values) throws Exception;
	
	public List<ProvisionesDetalle> findInProperty(String propertyName, List<Object> values, int limite) throws Exception;
	
	public ProvisionesDetalle convertir(ProvisionesDetalleExt entityExt) throws Exception;
	
	public ProvisionesDetalleExt convertir(ProvisionesDetalle entity) throws Exception;

	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------
	
	public Long save(ProvisionesDetalleExt entityExt) throws Exception;

	public void update(ProvisionesDetalleExt entityExt) throws Exception;

	public void delete(ProvisionesDetalleExt entityExt) throws Exception;
	
	public ProvisionesDetalleExt findExtById(Long entityId) throws Exception;
	
	public List<ProvisionesDetalleExt> findExtAll(Long idProvisiones) throws Exception;
	
	public List<ProvisionesDetalleExt> findExtAll(Provisiones entity) throws Exception;
	
	public List<ProvisionesDetalleExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ProvisionesDetalleExt> findExtLikeProperty(String propertyName, final String value, int limite) throws Exception;
	
	public List<ProvisionesDetalleExt> extenderLista(List<ProvisionesDetalle> lista) throws Exception;
	
	public List<ProvisionesDetalle> normalizarLista(List<ProvisionesDetalleExt> listaExt) throws Exception;
}

/*
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN
 *  ----------------------------------------------------------------------------------------------------------------
 *    2.1	| 28/09/2017 | Javier Tirado	| Creacion de ProvisionesDetalleRem
 */