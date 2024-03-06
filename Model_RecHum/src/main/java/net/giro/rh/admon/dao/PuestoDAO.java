package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.Puesto;

@Remote
public interface PuestoDAO extends DAO<Puesto> {
	public long save(Puesto entity, long codigoEmpresa) throws Exception;
	
	public List<Puesto> saveOrUpdateList(List<Puesto> entities, long codigoEmpresa) throws Exception;
	
	public List<Puesto> findAll(boolean incluyeEliminados, long idEmpresa) throws Exception;

	public List<Puesto> findByProperty(String propertyName, Object value, boolean incluyeEliminados, long idEmpresa) throws Exception;

	public List<Puesto> findLikeProperty(String propertyName, String value, boolean incluyeEliminados, long idEmpresa) throws Exception;
}
