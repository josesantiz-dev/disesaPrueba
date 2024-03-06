package net.giro.rh.admon.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.Checador;

@Remote
public interface ChecadorDAO extends DAO<Checador> {
	public long save(Checador entity, long codigoEmpresa) throws Exception;
	
	public List<Checador> saveOrUpdateList(List<Checador> entities, long codigoEmpresa) throws Exception;

	public List<Checador> findAll(long idObra, String orderBy) throws Exception;

	public List<Checador> findLike(String value, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<Checador> findLikeProperty(String propertyName, final Object value, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<Checador> findByProperty(String propertyName, final Object value, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<Checador> findByDate(Date fecha, String obra, long idEmpresa, String orderBy) throws Exception;

	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta, long idEmpresa, String orderBy) throws Exception;

	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta, long idObra, long idEmpresa, String orderBy) throws Exception;

	public List<Checador> asistenciasNominas(Date fechaDesde, Date fechaHasta, long idObra, long idEmpresa, String orderBy) throws Exception;
	
	public List<Checador> findList(List<Long> listValues, String orderBy) throws Exception;
}
