package net.giro.plataforma.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.plataforma.beans.DiasFestivosNegociacion;

@Remote
public interface DiasFestivosNegociacionDAO extends DAO<DiasFestivosNegociacion> {
	public long save(DiasFestivosNegociacion entity, long codigoEmpresa) throws Exception;

	public List<DiasFestivosNegociacion> saveOrUpdateList(List<DiasFestivosNegociacion> entities, long codigoEmpresa) throws Exception;

	public List<DiasFestivosNegociacion> findAll(long idObra, long idEmpresa, int limite) throws Exception;
	
	public List<DiasFestivosNegociacion> findByProperty(String propertyName, Object value, long idObra, long idEmpresa, int limite) throws Exception;
	
	public List<DiasFestivosNegociacion> findLikeProperty(String propertyName, Object value, long idObra, long idEmpresa, int limite) throws Exception;
	
	public List<DiasFestivosNegociacion> comprobarNegociacion(long idDiaFestivo, long idObra, long idEmpresa) throws Exception;
}
