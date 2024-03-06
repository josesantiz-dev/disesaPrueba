package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.FacturaPagos;

@Remote
public interface FacturaPagosDAO extends DAO<FacturaPagos> {

	public void setEmpresa(Long idEmpresa);
		
	public long save(FacturaPagos entity) throws Exception;
	
	public List<FacturaPagos> saveOrUpdateList(List<FacturaPagos> entities) throws Exception;
	
	public List<FacturaPagos> findByProperty(String columnName, Object value, int limite);
	
	public List<FacturaPagos> findLikeProperty(String columnName, String value, int limite);
	
	public List<FacturaPagos> findInProperty(String columnName, List<Long> values, int limite);
	
	public List<FacturaPagos> findByFactura(Long idFactura);
}
