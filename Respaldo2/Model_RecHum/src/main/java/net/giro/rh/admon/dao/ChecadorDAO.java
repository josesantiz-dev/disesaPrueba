package net.giro.rh.admon.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.Checador;

@Remote
public interface ChecadorDAO extends DAO<Checador> {
	public void orderBy(String orderBy);

	public long save(Checador entity, Long idEmpresa) throws Exception;
	
	public List<Checador> saveOrUpdateList(List<Checador> entities, Long idEmpresa) throws Exception;
	
	public List<Checador> findByProperty(String propertyName, final Object value, int limite, Long idEmpresa) throws Exception;

	public List<Checador> findByProperties(HashMap<String, String> params, int limite, Long idEmpresa) throws Exception;

	public List<Checador> findLikeProperty(String propertyName, final String value, int limite, Long idEmpresa) throws Exception;

	public List<Checador> findLikeProperties(HashMap<String, String> params, int limite, Long idEmpresa) throws Exception;
	
	public List<Checador> findInProperty(String columnName, List<Object> values, int limite, Long idEmpresa) throws Exception;

	public List<Checador> findByDate(Date fecha, String obra, Long idEmpresa) throws Exception;

	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta, Long idEmpresa) throws Exception;
}
