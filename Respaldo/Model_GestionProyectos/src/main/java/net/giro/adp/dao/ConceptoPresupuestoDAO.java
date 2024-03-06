package net.giro.adp.dao;

import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;
import net.giro.adp.beans.ConceptoPresupuesto;

@Remote
public interface ConceptoPresupuestoDAO  extends DAO<ConceptoPresupuesto>{

	public void delete(ConceptoPresupuesto entity);

	public void update(ConceptoPresupuesto entity);

	public ConceptoPresupuesto findById(long id);

	public List<ConceptoPresupuesto> findByProperty(String propertyName, final Object value, int max);

	public List<ConceptoPresupuesto> findLikeProperty(String propertyName, final Object value, int max);
	
	public List<ConceptoPresupuesto> findAllActivos();
	
}
