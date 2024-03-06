package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraEmpleado;

@Remote
public interface ObraEmpleadoDAO extends DAO<ObraEmpleado> {
	public long save(ObraEmpleado entity, long codigoEmpresa) throws Exception;
	
	public List<ObraEmpleado> saveOrUpdateList(List<ObraEmpleado> entities, long codigoEmpresa) throws Exception;

	public List<ObraEmpleado> findAll(long idObra, String orderBy);

	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite);

	public List<ObraEmpleado> findByProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite);
}
