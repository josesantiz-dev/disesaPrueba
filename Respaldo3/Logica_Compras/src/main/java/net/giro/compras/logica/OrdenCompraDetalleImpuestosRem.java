package net.giro.compras.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.OrdenCompraDetalleImpuestos;
import net.giro.compras.beans.OrdenCompraDetalleImpuestosExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface OrdenCompraDetalleImpuestosRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(OrdenCompraDetalleImpuestos entity) throws Exception;
	
	public void update(OrdenCompraDetalleImpuestos entity) throws Exception;
    
    public List<OrdenCompraDetalleImpuestos> saveOrUpdateList(List<OrdenCompraDetalleImpuestos> entities) throws Exception;

	public void delete(Long entityId) throws Exception;
	
	public OrdenCompraDetalleImpuestos findById(long entityId) throws Exception;
	
	public List<OrdenCompraDetalleImpuestos> findByProperty(String propertyName, Object value, int limite) throws Exception;

	public List<OrdenCompraDetalleImpuestos> findLikeProperty(String propertyName, String value, int limite) throws Exception;
	
	public OrdenCompraDetalleImpuestos convertir(OrdenCompraDetalleImpuestosExt entityExt) throws Exception;
	
	public OrdenCompraDetalleImpuestosExt convertir(OrdenCompraDetalleImpuestos entity) throws Exception;

	// -----------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// -----------------------------------------------------------------------------------------------------------------------
	
	public Long save(OrdenCompraDetalleImpuestosExt entityExt) throws Exception;
	
	public void update(OrdenCompraDetalleImpuestosExt entityExt) throws Exception;
	
	public OrdenCompraDetalleImpuestosExt findExtById(long entityId) throws Exception;
	
	public List<OrdenCompraDetalleImpuestosExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception;

	public List<OrdenCompraDetalleImpuestosExt> findExtLikeProperty(String propertyName, String value, int limite) throws Exception;
}

/*
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN
 *  ----------------------------------------------------------------------------------------------------------------
 *    2.1	| 11/12/2017 | Javier Tirado	| Creacion de OrdenCompraDetalleImpuestosDAO
 */