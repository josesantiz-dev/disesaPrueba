package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.ConceptoFacturacionImpuestos;

@Remote
public interface ConceptoFacturacionImpuestosDAO extends DAO<ConceptoFacturacionImpuestos> {
	public long save(ConceptoFacturacionImpuestos entity) throws Exception;
	
	public List<ConceptoFacturacionImpuestos> saveOrUpdateList(List<ConceptoFacturacionImpuestos> entities) throws Exception;
	
	public List<ConceptoFacturacionImpuestos> findAll(long idConceptoFacturacion);

	public List<ConceptoFacturacionImpuestos> findByProperty(String propertyName, Object value, int limite);
	
	public List<ConceptoFacturacionImpuestos> findLikeProperty(String propertyName, final String value, int limite);
}
