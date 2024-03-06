package net.giro.compras.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.OrdenCompraDetalleExt;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Remote
public interface OrdenCompraDetalleRem {
	public void setInfoSecion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void OrderBy(String orderBy);

	public Long save(OrdenCompraDetalle entity) throws ExcepConstraint;
	public Long save(OrdenCompraDetalleExt entityExt) throws ExcepConstraint;
	
	public void update(OrdenCompraDetalle entity) throws ExcepConstraint;
	public void update(OrdenCompraDetalleExt entityExt) throws ExcepConstraint;
	
	public void delete(Long id) throws ExcepConstraint;

	public OrdenCompraDetalle findById(Long id);
	public OrdenCompraDetalleExt findExtById(Long id) throws Exception;
	
	public List<OrdenCompraDetalle> findByProperty(String propertyName, final Object value, int max) throws Exception;
	public List<OrdenCompraDetalleExt> findExtByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<OrdenCompraDetalle> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	public List<OrdenCompraDetalleExt> findExtLikeProperty(String propertyName, final Object value, int max) throws Exception;

	public List<OrdenCompraDetalle> findInProperty(String columnName, List<Object> values) throws Exception;
	public List<OrdenCompraDetalleExt> findExtInProperty(String columnName, List<Object> values) throws Exception;
}
