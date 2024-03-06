package net.giro.plataforma.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.plataforma.beans.DiasFestivos;

@Remote
public interface DiasFestivosDAO extends DAO<DiasFestivos> {
	public long save(DiasFestivos entity, long codigoEmpresa) throws Exception;

	public List<DiasFestivos> saveOrUpdateList(List<DiasFestivos> entities, long codigoEmpresa) throws Exception;

	public List<DiasFestivos> findAll(long idEmpresa, int limite) throws Exception;
	
	public List<DiasFestivos> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception;
	
	public List<DiasFestivos> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception;
	
	public List<DiasFestivos> comprobarDiaFestivo(int mes, int dia, long idEmpresa) throws Exception;
}
