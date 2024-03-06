package net.giro.plataforma.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.plataforma.beans.ManualesProcesos;

@Remote
public interface ManualesProcesosDAO extends DAO<ManualesProcesos> {
	public long save(ManualesProcesos entity, long codigoEmpresa) throws Exception;
	
	public List<ManualesProcesos> saveOrUpdateList(List<ManualesProcesos> entities, long codigoEmpresa) throws Exception;

	public List<ManualesProcesos> findAll(String orderBy, long idEmpresa, int limite) throws Exception;

	public List<ManualesProcesos> findLike(String value, boolean incluyeCancelados, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<ManualesProcesos> findLikeProperty(String propertyName, Object value, boolean incluyeCancelados, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<ManualesProcesos> findByProperty(String propertyName, Object value, boolean incluyeCancelados, long idEmpresa, String orderBy, int limite) throws Exception;
}
