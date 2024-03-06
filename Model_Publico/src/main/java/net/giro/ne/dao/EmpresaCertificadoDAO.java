package net.giro.ne.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.ne.beans.EmpresaCertificado;

@Remote
public interface EmpresaCertificadoDAO extends DAO<EmpresaCertificado> {
	public void orderBy(String orderBy);

	//public void estatus(Long estatus);
	
	public List<EmpresaCertificado> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpresaCertificado> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<EmpresaCertificado> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<EmpresaCertificado> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<EmpresaCertificado> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public EmpresaCertificado findByEmpresa(Long idEmpresa) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/07/2016 | Javier Tirado	| Creacion de EmpresaCertificadoDAO