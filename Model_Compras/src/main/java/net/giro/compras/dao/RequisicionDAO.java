package net.giro.compras.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.Requisicion;

@Remote
public interface RequisicionDAO extends DAO<Requisicion> {
    public long save(Requisicion entity, long codigoEmpresa) throws Exception;
    
    public List<Requisicion> saveOrUpdateList(List<Requisicion> entities, long codigoEmpresa) throws Exception;

	public List<Requisicion> findAll(long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idEmpresa, long idPropietario, String orderBy, int limite) throws Exception;
	
	public List<Requisicion> findLike(String value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idEmpresa, long idPropietario, String orderBy, int limite) throws Exception;

	public List<Requisicion> findLikeProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idEmpresa, long idPropietario, String orderBy, int limite) throws Exception;
	
	public List<Requisicion> findByProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idEmpresa, long idPropietario, String orderBy, int limite) throws Exception;
	
	public List<Requisicion> findByProperties(HashMap<String, Object> params, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idEmpresa, long idPropietario, String orderBy, int limite) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-19 | Javier Tirado 	| Añado los metodos estatus, findByProperties y findLikeProperties. 
 */