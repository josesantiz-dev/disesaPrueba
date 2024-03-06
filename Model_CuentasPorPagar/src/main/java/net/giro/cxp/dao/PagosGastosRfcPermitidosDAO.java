package net.giro.cxp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxp.beans.PagosGastosRfcPermitidos;

@Remote
public interface PagosGastosRfcPermitidosDAO extends DAO<PagosGastosRfcPermitidos> {
	public long save(PagosGastosRfcPermitidos entity, long codigoEmpresa) throws Exception;
	
	public List<PagosGastosRfcPermitidos> saveOrUpdateList(List<PagosGastosRfcPermitidos> entities, long codigoEmpresa) throws Exception;

	public List<PagosGastosRfcPermitidos> findAll(String rfc, long idEmpresa, String orderBy) throws Exception;

	public List<PagosGastosRfcPermitidos> findByProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<PagosGastosRfcPermitidos> findLikeProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception;
}
