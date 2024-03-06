package net.giro.contabilidad.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.Conceptos;

@Remote
public interface ConceptosDAO extends DAO<Conceptos> {
	public long save(Conceptos entity, long codigoEmpresa) throws Exception;
	
	public List<Conceptos> saveOrUpdateList(List<Conceptos> entities, long codigoEmpresa) throws Exception;

	public List<Conceptos> findAll(long idEmpresa, String orderBy) throws Exception;

	public List<Conceptos> findByProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<Conceptos> findLikeProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 30/05/2016 | Javier Tirado	| Creacion de ConceptosDAO