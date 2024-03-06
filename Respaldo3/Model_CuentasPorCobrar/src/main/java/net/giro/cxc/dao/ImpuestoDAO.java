package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.Impuesto;

@Remote
public interface ImpuestoDAO extends DAO<Impuesto>{
	public long save(Impuesto entity) throws Exception;
	
	public List<Impuesto> saveOrUpdateList(List<Impuesto> entities) throws Exception;

	public List<Impuesto> findByProperty(String propertyName, Object value);

	public List<Impuesto> findLikeProperty(String propertyName, String value);
}
