package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.ObraAlmacenesExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraAlmacenesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);

	public Long save(ObraAlmacenes entity) throws Exception;

	public List<ObraAlmacenes> saveOrUpdateList(List<ObraAlmacenes> entities) throws Exception;

	public void update(ObraAlmacenes entity) throws Exception;
	
	public void delete(Long idObraAlmacen) throws Exception;

	public ObraAlmacenes findById(Long idObraAlmacen);
	
	public List<ObraAlmacenes> findAll(long idObra) throws Exception;
	
	public List<ObraAlmacenes> findAll(long idObra, String orderBy) throws Exception;
	
	public List<ObraAlmacenes> findLike(String propertyValue, long idAlmacen, String orderBy, int limite) throws Exception;
	
	public List<ObraAlmacenes> findLikeProperty(String propertyName, Object propertyValue, long idAlmacen, String orderBy, int limite) throws Exception;

	public List<ObraAlmacenes> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraAlmacenes> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraAlmacenes> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public ObraAlmacenes findAlmacenPrincipal(long idBodega);
	
	public ObraAlmacenes findAlmacenPrincipal(long idObra, long idSucursal);

	public ObraAlmacenes findBodega(long idObra);

	// --------------------------------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// --------------------------------------------------------------------------------------------------------------------
	
	public ObraAlmacenes convertir(ObraAlmacenesExt extendido) throws Exception;

	public ObraAlmacenesExt convertir(ObraAlmacenes entity) throws Exception;

	// --------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------------------------
	
	public Long save(ObraAlmacenesExt extendido) throws Exception;
	
	public void update(ObraAlmacenesExt extendido) throws Exception;
	
	public ObraAlmacenesExt findExtById(Long idObraAlmacen) throws Exception;
	
	public List<ObraAlmacenesExt> findExtAll(long idObra, String orderBy) throws Exception;
	
	public ObraAlmacenesExt findExtAlmacenPrincipal(long idObra, long idSucursal) throws Exception;
	
	public ObraAlmacenesExt findExtBodega(long idObra) throws Exception;
	
	public List<ObraAlmacenesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|19/05/2016		|Javier Tirado	|Creando la interface remota ObraAlmacenesRem