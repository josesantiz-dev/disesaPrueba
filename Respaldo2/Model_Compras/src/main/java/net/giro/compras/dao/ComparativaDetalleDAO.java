package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.ComparativaDetalle;
import net.giro.comun.ExcepConstraint;

@Remote
public interface ComparativaDetalleDAO extends DAO<ComparativaDetalle> {
	public void OrderBy(String orderBy);
	
    public long save(ComparativaDetalle entity, Long idEmpresa) throws ExcepConstraint;
    
    public List<ComparativaDetalle> saveOrUpdateList(List<ComparativaDetalle> entities, Long idEmpresa) throws Exception;
	
	public List<ComparativaDetalle> findByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<ComparativaDetalle> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<ComparativaDetalle> findInProperty(String columnName, List<Object> values) throws Exception;
}
