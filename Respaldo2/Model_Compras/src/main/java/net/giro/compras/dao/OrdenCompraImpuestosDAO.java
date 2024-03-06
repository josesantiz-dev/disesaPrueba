package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.OrdenCompraImpuestos;
import net.giro.comun.ExcepConstraint;

@Remote
public interface OrdenCompraImpuestosDAO extends DAO<OrdenCompraImpuestos> {
    public long save(OrdenCompraImpuestos entity, Long idEmpresa) throws ExcepConstraint;
    
    public List<OrdenCompraImpuestos> saveOrUpdateList(List<OrdenCompraImpuestos> entities, Long idEmpresa) throws Exception;
    
	public List<OrdenCompraImpuestos> findByProperty(String propertyName, Object value, int limite) throws Exception;

	public List<OrdenCompraImpuestos> findLikeProperty(String propertyName, String value, int limite) throws Exception;
}

/*
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN
 *  ----------------------------------------------------------------------------------------------------------------
 *    2.1	| 11/12/2017 | Javier Tirado	| Creacion de OrdenCompraImpuestosDAO
 */