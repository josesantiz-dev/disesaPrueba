package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.ObraAlmacenesExt;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraAlmacenesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(ObraAlmacenes entity) throws ExcepConstraint;
	public Long save(ObraAlmacenesExt entityExt) throws ExcepConstraint;
	
	public void update(ObraAlmacenes entity) throws ExcepConstraint;
	public void update(ObraAlmacenesExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public ObraAlmacenes findById(Long id);
	public ObraAlmacenesExt findExtById(Long id) throws Exception;
	
	public List<ObraAlmacenes> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	public List<ObraAlmacenesExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraAlmacenes> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	public List<ObraAlmacenesExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ObraAlmacenes> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	public List<ObraAlmacenesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<ObraAlmacenes> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	public List<ObraAlmacenesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<ObraAlmacenes> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	public List<ObraAlmacenesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|19/05/2016		|Javier Tirado	|Creando la interface remota ObraAlmacenesRem