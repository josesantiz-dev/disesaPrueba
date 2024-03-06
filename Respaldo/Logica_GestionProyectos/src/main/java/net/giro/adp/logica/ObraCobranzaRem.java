package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraCobranza;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraCobranzaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(ObraCobranza entity) throws ExcepConstraint;
	//public Long save(ObraCobranzaExt entityExt) throws ExcepConstraint;
	
	public void update(ObraCobranza entity) throws ExcepConstraint;
	//public void update(ObraCobranzaExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public ObraCobranza findById(Long id);
	//public ObraCobranzaExt findExtById(Long id) throws Exception;
	
	public List<ObraCobranza> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<ObraCobranzaExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraCobranza> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<ObraCobranzaExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ObraCobranza> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<ObraCobranzaExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<ObraCobranza> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<ObraCobranzaExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<ObraCobranza> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<ObraCobranzaExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<ObraCobranza> findCobranza(List<Object> facturasActuales, long idObra, int limite) throws Exception;
	
	public ObraCobranza comprobarConcepto(Long idObra, Long idFactura, Long idConcepto) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 24/05/2016 | Javier Tirado	| Creacion de ObraCobranzaRem