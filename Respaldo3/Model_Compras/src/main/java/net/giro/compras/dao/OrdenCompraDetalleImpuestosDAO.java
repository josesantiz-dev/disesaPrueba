package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.OrdenCompraDetalleImpuestos;
import net.giro.compras.comun.ExcepConstraint;

@Remote
public interface OrdenCompraDetalleImpuestosDAO extends DAO<OrdenCompraDetalleImpuestos> {
    public long save(OrdenCompraDetalleImpuestos entity, Long idEmpresa) throws ExcepConstraint;
    
    public List<OrdenCompraDetalleImpuestos> saveOrUpdateList(List<OrdenCompraDetalleImpuestos> entities, Long idEmpresa) throws Exception;
    
	public List<OrdenCompraDetalleImpuestos> findByProperty(String propertyName, Object value, int limite) throws Exception;

	public List<OrdenCompraDetalleImpuestos> findLikeProperty(String propertyName, String value, int limite) throws Exception;
}

/*
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN
 *  ----------------------------------------------------------------------------------------------------------------
 *    2.1	| 11/12/2017 | Javier Tirado	| Creacion de OrdenCompraDetalleImpuestosDAO
 */