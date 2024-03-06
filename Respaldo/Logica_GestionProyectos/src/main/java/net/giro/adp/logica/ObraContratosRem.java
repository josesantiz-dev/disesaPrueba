package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraContratos;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraContratosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(ObraContratos entity) throws ExcepConstraint;
	//public Long save(ObraContratosExt entityExt) throws ExcepConstraint;
	
	public void update(ObraContratos entity) throws ExcepConstraint;
	//public void update(ObraContratosExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public ObraContratos findById(Long id);
	//public ObraContratosExt findExtById(Long id) throws Exception;
	
	public List<ObraContratos> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<ObraContratosExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraContratos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<ObraContratosExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ObraContratos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<ObraContratosExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<ObraContratos> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	//public List<ObraContratosExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<ObraContratos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<ObraContratosExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;

	//public ObraContratos cancelar(ObraContratos entity) throws Exception;
	//public ObraContratos cancelar(ObraContratosExt entityExt) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 15/06/2016 | Javier Tirado	| Creacion de ObraContratosRem