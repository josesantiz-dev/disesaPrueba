package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraSubcontratistaImpuestos;

@Remote
public interface ObraSubcontratistaImpuestosDAO extends DAO<ObraSubcontratistaImpuestos> {
	public long save(ObraSubcontratistaImpuestos entity, long codigoEmpresa) throws Exception;
	
	public List<ObraSubcontratistaImpuestos> saveOrUpdateList(List<ObraSubcontratistaImpuestos> entities, long codigoEmpresa) throws Exception;

	public List<ObraSubcontratistaImpuestos> findAll(long idObraSubcontratista, String orderBy) throws Exception;

	public List<ObraSubcontratistaImpuestos> findLikeProperty(String propertyName, final Object value, long idObraSubcontratista, String orderBy, int limite) throws Exception;

	public List<ObraSubcontratistaImpuestos> findByProperty(String propertyName, final Object value, long idObraSubcontratista, String orderBy, int limite) throws Exception;
}
