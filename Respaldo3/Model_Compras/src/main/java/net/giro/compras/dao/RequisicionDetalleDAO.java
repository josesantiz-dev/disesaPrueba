package net.giro.compras.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.comun.ExcepConstraint;

@Remote
public interface RequisicionDetalleDAO extends DAO<RequisicionDetalle> {
	public void OrderBy(String orderBy);
	
    public long save(RequisicionDetalle entity, Long idEmpresa) throws ExcepConstraint;
	
	public List<RequisicionDetalle> saveOrUpdateList(List<RequisicionDetalle> entities, Long idEmpresa) throws Exception;
	
	public List<RequisicionDetalle> findByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<RequisicionDetalle> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<RequisicionDetalle> findInProperty(String columnName, List<Object> values) throws Exception;

	public List<RequisicionDetalle> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<RequisicionDetalle> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
