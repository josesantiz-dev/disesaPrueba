package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.ConceptoFacturacion;

@Remote
public interface ConceptoFacturacionDAO extends DAO<ConceptoFacturacion> {
	
	//public long save(ConceptoFacturacion entity);
	
	//public void delete(ConceptoFacturacion entity);
	
	//public void update(ConceptoFacturacion entity);

	//public ConceptoFacturacion findById(Long id);
	
	public List<ConceptoFacturacion> findAll();

	public List<ConceptoFacturacion> findByProperty(String propertyName, Object value, int limite);
	
	public List<ConceptoFacturacion> findLikeProperty(String propertyName, final String value, int limite);
	
	public List<ConceptoFacturacion> findByPropertyPojoCompleto(String propertyName, String tipo, Object value);
}
