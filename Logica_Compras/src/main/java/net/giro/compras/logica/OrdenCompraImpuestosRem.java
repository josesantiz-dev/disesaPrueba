package net.giro.compras.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.OrdenCompraImpuestos;
import net.giro.compras.beans.OrdenCompraImpuestosExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface OrdenCompraImpuestosRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(OrdenCompraImpuestos entity) throws Exception;
	
	public void update(OrdenCompraImpuestos entity) throws Exception;
    
    public List<OrdenCompraImpuestos> saveOrUpdateList(List<OrdenCompraImpuestos> entities) throws Exception;

	public void delete(long idOrdenCompraImpuestos) throws Exception;
	
	public OrdenCompraImpuestos findById(long idOrdenCompraImpuestos) throws Exception;
	
	public List<OrdenCompraImpuestos> findAll(long idOrdenCompra) throws Exception;
	
	//public List<OrdenCompraImpuestos> findByProperty(String propertyName, Object value, int limite) throws Exception;

	//public List<OrdenCompraImpuestos> findLikeProperty(String propertyName, String value, int limite) throws Exception;

	// -----------------------------------------------------------------------------------------------------------------------
	// CONVERTIDOR 
	// -----------------------------------------------------------------------------------------------------------------------
	
	public OrdenCompraImpuestos convertir(OrdenCompraImpuestosExt extendido) throws Exception;
	
	public OrdenCompraImpuestosExt convertir(OrdenCompraImpuestos entity) throws Exception;

	// -----------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// -----------------------------------------------------------------------------------------------------------------------
	
	public Long save(OrdenCompraImpuestosExt extendido) throws Exception;

    public List<OrdenCompraImpuestosExt> saveOrUpdateListExt(List<OrdenCompraImpuestosExt> extendidos) throws Exception;

	public void update(OrdenCompraImpuestosExt extendido) throws Exception;
	
	public OrdenCompraImpuestosExt findExtById(long idOrdenCompraImpuestos) throws Exception;
	
	public List<OrdenCompraImpuestosExt> findExtAll(long idOrdenCompra) throws Exception;
	
	//public List<OrdenCompraImpuestosExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception;

	//public List<OrdenCompraImpuestosExt> findExtLikeProperty(String propertyName, String value, int limite) throws Exception;
}

/*
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN
 *  ----------------------------------------------------------------------------------------------------------------
 *    2.1	| 11/12/2017 | Javier Tirado	| Creacion de OrdenCompraImpuestosDAO
 */