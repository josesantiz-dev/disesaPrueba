package net.giro.cargas.documentos.dao;

import java.util.Date;
import java.util.List;

import net.giro.DAO;
import net.giro.cargas.documentos.beans.ComprobacionPago;

import javax.ejb.Remote;

@Remote
public interface ComprobacionPagosDAO extends DAO<ComprobacionPago> {
	public long save(ComprobacionPago entity, long codigoEmpresa) throws Exception;
	
	public List<ComprobacionPago> saveOrUpdateList(List<ComprobacionPago> entities, long idEmpresa) throws Exception;
	
	public List<ComprobacionPago> comprobar(String expresionImpresa, boolean incluyeCancelados, String orderBy, long idEmpresa) throws Exception;
	
	public List<ComprobacionPago> findByDates(Date fechaDesde, Date fechaHasta, boolean incluyeCancelados, String orderBy, long idEmpresa) throws Exception;
	
	public List<ComprobacionPago> findInProperty(String propertyName, List<Object> values, boolean incluyeCancelados, String orderBy, long idEmpresa) throws Exception;
}
