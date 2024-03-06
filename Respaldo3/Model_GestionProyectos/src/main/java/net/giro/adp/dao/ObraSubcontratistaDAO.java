package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraSubcontratista;

@Remote
public interface ObraSubcontratistaDAO extends DAO<ObraSubcontratista> {
	public long save(ObraSubcontratista entity, long codigoEmpresa) throws Exception;
	
	public List<ObraSubcontratista> saveOrUpdateList(List<ObraSubcontratista> entities, long codigoEmpresa) throws Exception;

	public List<ObraSubcontratista> findAll(long idObra, long idEmpleado, String orderBy) throws Exception;

	public List<ObraSubcontratista> findLikeProperty(String propertyName, final Object value, long idObra, long idEmpleado, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<ObraSubcontratista> findByProperty(String propertyName, final Object value, long idObra, long idEmpleado, long idEmpresa, String orderBy, int limite) throws Exception;
}
