package net.giro.contabilidad.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.Conceptos;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ConceptosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Conceptos entity) throws Exception;

	public List<Conceptos> saveOrUpdateList(List<Conceptos> entities) throws Exception;

	public void update(Conceptos entity) throws Exception;
	
	public void delete(Long idConcepto) throws Exception;
	
	public Conceptos cancelar(Conceptos entity) throws Exception;

	public Conceptos findById(Long idConcepto);

	public List<Conceptos> findAll() throws Exception;

	public List<Conceptos> findAll(String orderBy) throws Exception;

	public List<Conceptos> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;

	public List<Conceptos> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
	
	public List<Conceptos> findLikeProperty(String propertyName, Object value, Integer limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 30/05/2016 | Javier Tirado	| Creacion de ConceptosRem