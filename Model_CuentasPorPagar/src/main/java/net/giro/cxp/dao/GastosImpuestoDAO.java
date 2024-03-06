package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.cxp.beans.GastosImpuesto;

@Remote
public interface GastosImpuestoDAO extends DAO<GastosImpuesto> {
	public List<GastosImpuesto> findAll(long idGasto, String orderBy);

	public List<GastosImpuesto> findAllByImpuesto(long idImpuesto, String orderBy);

	public List<GastosImpuesto> findAllById(List<Long> idImpuestos);
	
	public List<GastosImpuesto> findByProperty(String propertyName, Object value);
	
	public List<GastosImpuesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
}
