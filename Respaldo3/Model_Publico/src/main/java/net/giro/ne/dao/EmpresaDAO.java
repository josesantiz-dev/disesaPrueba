package net.giro.ne.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAO;
import net.giro.ne.beans.Empresa;

public interface EmpresaDAO extends DAO<Empresa> {
	public void orderBy(String orderBy);

	public List<Empresa> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Empresa> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Empresa> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Empresa> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<Empresa> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	//public List<Empresa> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint;
	
	//public Empresa findByNombre(String nombre) throws Exception;
	
	//public Empresa monedaBase(Long empresaId);
}


//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/07/2016 | Javier Tirado	| Modificacion de EmpresaDAO