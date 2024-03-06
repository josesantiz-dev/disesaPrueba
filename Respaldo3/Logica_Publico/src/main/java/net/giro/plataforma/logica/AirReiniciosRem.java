package net.giro.plataforma.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.AirReinicios;

@Remote
public interface AirReiniciosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public long save(AirReinicios entity) throws Exception;
	
	public List<AirReinicios> saveOrUpdateList(List<AirReinicios> entities) throws Exception;

	public void update(AirReinicios entity) throws Exception;

	public void delete(long idEntity) throws Exception;

	public AirReinicios findById(long idEntity) throws Exception;

	public List<AirReinicios> findAll(String orderBy) throws Exception;
	
	public List<AirReinicios> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
	
	public List<AirReinicios> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
	
	public AirReinicios comprobarProgramado(String comando) throws Exception;
}
