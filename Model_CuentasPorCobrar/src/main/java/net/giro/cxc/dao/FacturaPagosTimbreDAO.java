package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.FacturaPagosTimbre;

@Remote
public interface FacturaPagosTimbreDAO extends DAO<FacturaPagosTimbre> {
	public long save(FacturaPagosTimbre entity, long codigoEmpresa) throws Exception;
	
	public long saveOrUpdate(FacturaPagosTimbre entity, long codigoEmpresa) throws Exception;
	
	public List<FacturaPagosTimbre> saveOrUpdateList(List<FacturaPagosTimbre> entities, long codigoEmpresa) throws Exception;
	
	public List<FacturaPagosTimbre> findAll(long idEmpresa) throws Exception;
	
	public List<FacturaPagosTimbre> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<FacturaPagosTimbre> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<FacturaPagosTimbre> comprobarTimbre(String serie, String folio, long idEmpresa) throws Exception;
}
