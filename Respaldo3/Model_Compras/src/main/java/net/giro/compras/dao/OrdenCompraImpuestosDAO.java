package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.OrdenCompraImpuestos;

@Remote
public interface OrdenCompraImpuestosDAO extends DAO<OrdenCompraImpuestos> {
    public long save(OrdenCompraImpuestos entity, long codigoEmpresa) throws Exception;
    
    public List<OrdenCompraImpuestos> saveOrUpdateList(List<OrdenCompraImpuestos> entities, long codigoEmpresa) throws Exception;
    
	public List<OrdenCompraImpuestos> findAll(long idOrdenCompra) throws Exception;
}

/*
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN
 *  ----------------------------------------------------------------------------------------------------------------
 *    2.1	| 11/12/2017 | Javier Tirado	| Creacion de OrdenCompraImpuestosDAO
 */