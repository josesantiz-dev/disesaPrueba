package net.giro.cargas.documentos.dao;

import java.util.Date;
import java.util.List;

import net.giro.DAO;
import net.giro.cargas.documentos.beans.ComprobacionFactura;

import javax.ejb.Remote;

@Remote
public interface ComprobacionFacturaDAO extends DAO<ComprobacionFactura> {
	public long save(ComprobacionFactura entity, long codigoEmpresa) throws Exception;
	
	public List<ComprobacionFactura> saveOrUpdateList(List<ComprobacionFactura> entities, long idEmpresa) throws Exception;
	
	public List<ComprobacionFactura> comprobar(String expresionImpresa, String orderBy, long idEmpresa) throws Exception;
	
	public List<ComprobacionFactura> findByDates(Date fechaDesde, Date fechaHasta, String orderBy, long idEmpresa) throws Exception;
	
	public List<ComprobacionFactura> findInProperty(String propertyName, List<Object> values, long idEmpresa) throws Exception;
}
