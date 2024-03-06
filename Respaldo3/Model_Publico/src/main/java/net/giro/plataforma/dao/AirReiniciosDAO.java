package net.giro.plataforma.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.plataforma.beans.AirReinicios;

@Remote
public interface AirReiniciosDAO extends DAO<AirReinicios> {
	public long save(AirReinicios entity, long codigoEmpresa) throws Exception;

	public List<AirReinicios> saveOrUpdateList(List<AirReinicios> entities, long codigoEmpresa) throws Exception;

	public List<AirReinicios> findAll(String orderBy) throws Exception;
	
	public List<AirReinicios> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;

	public List<AirReinicios> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
}
