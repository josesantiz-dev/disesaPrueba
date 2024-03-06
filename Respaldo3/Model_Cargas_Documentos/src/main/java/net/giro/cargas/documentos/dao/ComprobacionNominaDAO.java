package net.giro.cargas.documentos.dao;

import java.util.Date;
import java.util.List;

import net.giro.DAO;
import net.giro.cargas.documentos.beans.ComprobacionNomina;

import javax.ejb.Remote;

@Remote
public interface ComprobacionNominaDAO extends DAO<ComprobacionNomina> {
	public long save(ComprobacionNomina entity, long codigoEmpresa) throws Exception;
	
	public List<ComprobacionNomina> saveOrUpdateList(List<ComprobacionNomina> entities, long idEmpresa) throws Exception;
	
	public List<ComprobacionNomina> comprobar(String expresionImpresa, boolean incluyeCancelados, String orderBy, long idEmpresa) throws Exception;
	
	public List<ComprobacionNomina> findByDates(Date fechaDesde, Date fechaHasta, boolean incluyeCancelados, String orderBy, long idEmpresa) throws Exception;
	
	public List<ComprobacionNomina> findInProperty(String propertyName, List<Object> values, boolean incluyeCancelados, String orderBy, long idEmpresa) throws Exception;
}
