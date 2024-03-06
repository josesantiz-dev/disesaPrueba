package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.ComparativaDetalle;

@Remote
public interface ComparativaDetalleDAO extends DAO<ComparativaDetalle> {
	public void OrderBy(String orderBy);
	
    public long save(ComparativaDetalle entity, long codigoEmpresa) throws Exception;
    
    public List<ComparativaDetalle> saveOrUpdateList(List<ComparativaDetalle> entities, Long codigoEmpresa) throws Exception;
	
	public List<ComparativaDetalle> findByProperty(String propertyName, Object value, int limite) throws Exception;

	public List<ComparativaDetalle> findLikeProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<ComparativaDetalle> findInProperty(String propertyName, List<Object> values) throws Exception;
}
