package net.giro.compras.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.OrdenCompraDetalleExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface OrdenCompraDetalleRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public void OrderBy(String orderBy);

	public Long save(OrdenCompraDetalle entity) throws Exception;
	
	public void update(OrdenCompraDetalle entity) throws Exception;
	
	public List<OrdenCompraDetalle> saveOrUpdateList(List<OrdenCompraDetalle> entities) throws Exception;
	
	public void delete(Long id) throws Exception;

	public OrdenCompraDetalle findById(Long id);
	
	public List<OrdenCompraDetalle> findAll(Long idOrdenCompra) throws Exception;
	
	public List<OrdenCompraDetalle> findByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<OrdenCompraDetalle> findLikeProperty(String propertyName, final Object value, int max) throws Exception;

	public List<OrdenCompraDetalle> findInProperty(String columnName, List<Object> values) throws Exception;
	
	public OrdenCompraDetalle convertir(OrdenCompraDetalleExt pojoTarget) throws Exception;

	public OrdenCompraDetalleExt convertir(OrdenCompraDetalle pojoTarget) throws Exception;
	
	// ------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// ------------------------------------------------------------------------------------------------------------------
	
	public Long save(OrdenCompraDetalleExt entityExt) throws Exception;
	
	public void update(OrdenCompraDetalleExt entityExt) throws Exception;
	
	public OrdenCompraDetalleExt findExtById(Long id) throws Exception;
	
	public List<OrdenCompraDetalleExt> findExtAll(Long idOrdenCompra) throws Exception;
	
	public List<OrdenCompraDetalleExt> findExtByProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<OrdenCompraDetalleExt> findExtLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<OrdenCompraDetalleExt> findExtInProperty(String columnName, List<Object> values) throws Exception;
}
