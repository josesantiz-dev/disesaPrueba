package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.cxp.beans.PagosGastosDetImpuestos;

@Remote
public interface PagosGastosDetImpuestosDAO extends DAO<PagosGastosDetImpuestos> {
	public PagosGastosDetImpuestos findById(Long id);

	public List<PagosGastosDetImpuestos> findByProperty(String propertyName,Object value);
		
	public List<PagosGastosDetImpuestos> findAll();
	
	public List<PagosGastosDetImpuestos> findLikePojoCompleto(Object value, int max);
	
	public List<PagosGastosDetImpuestos> findImptos2DetGtos (Object value, int max);
}
