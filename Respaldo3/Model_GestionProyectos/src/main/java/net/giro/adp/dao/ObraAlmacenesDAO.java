package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraAlmacenes;

@Remote
public interface ObraAlmacenesDAO extends DAO<ObraAlmacenes> {
	public long save(ObraAlmacenes entity, long codigoEmpresa) throws Exception;
	
	public List<ObraAlmacenes> saveOrUpdateList(List<ObraAlmacenes> entities, long codigoEmpresa) throws Exception;
	
	public List<ObraAlmacenes> findAll(long idObra, String orderBy) throws Exception;

	public List<ObraAlmacenes> findLike(String value, long idAlmacen, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<ObraAlmacenes> findLikeProperty(String propertyName, final Object value, long idAlmacen, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<ObraAlmacenes> findByProperty(String propertyName, final Object value, long idAlmacen, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<ObraAlmacenes> findByProperties(HashMap<String, String> params, long idEmpresa, String orderBy, int limite) throws Exception;

	public ObraAlmacenes findAlmacenPrincipal(long idObra, long idSucursal, long idEmpresa);
	
	public ObraAlmacenes findBodega(long idObra, long idEmpresa);
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|19/05/2016		|Javier Tirado	|Creando la clase ObraAlmacenesDAO