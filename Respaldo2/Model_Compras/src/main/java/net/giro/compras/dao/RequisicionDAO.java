package net.giro.compras.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.Requisicion;
import net.giro.comun.ExcepConstraint;

@Remote
public interface RequisicionDAO extends DAO<Requisicion> {
	public void OrderBy(String orderBy);
	
	public void estatus(Long estatus);
	
    public long save(Requisicion entity, Long idEmpresa) throws ExcepConstraint;
    
    public List<Requisicion> saveOrUpdateList(List<Requisicion> entities, Long idEmpresa) throws Exception;
	
	public List<Requisicion> findByProperty(String propertyName, final Object value, int limite, Long idEmpresa) throws Exception;

	public List<Requisicion> findLikeProperty(String propertyName, final Object value, int limite, Long idEmpresa) throws Exception;
	
	public List<Requisicion> findLikeProperty(String propertyName, final String value, int tipo, int limite, Long idEmpresa) throws Exception;
	
	public List<Requisicion> findInProperty(String columnName, List<Object> values, Long idEmpresa) throws Exception;
	
	public List<Requisicion> findByProperties(HashMap<String, Object> params, int limite, Long idEmpresa) throws Exception;
	
	public List<Requisicion> findLikeProperties(HashMap<String, String> params, int limite, Long idEmpresa) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-19 | Javier Tirado 	| Añado los metodos estatus, findByProperties y findLikeProperties. 
 */