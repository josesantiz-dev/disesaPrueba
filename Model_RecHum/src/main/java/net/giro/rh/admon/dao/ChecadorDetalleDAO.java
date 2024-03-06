package net.giro.rh.admon.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.ChecadorDetalle;

@Remote
public interface ChecadorDetalleDAO extends DAO<ChecadorDetalle> {
	public long save(ChecadorDetalle entity, long codigoEmpresa) throws Exception;
	
	public List<ChecadorDetalle> saveOrUpdateList(List<ChecadorDetalle> entities, long codigoEmpresa) throws Exception;
	
	public List<ChecadorDetalle> findAll(long idChecador, String orderBy) throws Exception;
	
	public List<ChecadorDetalle> findLike(String value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<ChecadorDetalle> findLikeProperty(String propertyName, final Object value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<ChecadorDetalle> findByProperty(String propertyName, final Object value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<ChecadorDetalle> findByProperties(HashMap<String, Object> params, long idEmpresa, int limite) throws Exception;

	public List<ChecadorDetalle> findByDates(Date fechaDesde, Date fechaHasta, long idEmpresa, String orderBy) throws Exception;
	
	public List<ChecadorDetalle> findAsistenciasPosteriorFecha(long idEmpleado, Date fecha, String orderBy) throws Exception;
}
