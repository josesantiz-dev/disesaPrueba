package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.Conceptos;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ConceptosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(Conceptos entity) throws ExcepConstraint;
	//public Long save(ConceptosExt entityExt) throws ExcepConstraint;
	
	public void update(Conceptos entity) throws ExcepConstraint;
	//public void update(ConceptosExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public Conceptos findById(Long id);
	//public ConceptosExt findExtById(Long id) throws Exception;
	
	public List<Conceptos> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<ConceptosExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Conceptos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	public List<Conceptos> findLikeProperty(String propertyName, final Object value, Integer limite) throws Exception;
	//public List<ConceptosExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Conceptos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<ConceptosExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Conceptos> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<ConceptosExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Conceptos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<ConceptosExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public Conceptos cancelar(Conceptos entity) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 30/05/2016 | Javier Tirado	| Creacion de ConceptosRem