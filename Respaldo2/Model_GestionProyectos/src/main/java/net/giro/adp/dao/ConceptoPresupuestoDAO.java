package net.giro.adp.dao;

import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;
import net.giro.adp.beans.ConceptoPresupuesto;

@Remote
public interface ConceptoPresupuestoDAO  extends DAO<ConceptoPresupuesto> {
	public void setEmpresa(Long idEmpresa);
		
	public long save(ConceptoPresupuesto entity) throws Exception;
	
	public List<ConceptoPresupuesto> saveOrUpdateList(List<ConceptoPresupuesto> entities) throws Exception;
	
	public List<ConceptoPresupuesto> findAllActivos();
	
	public List<ConceptoPresupuesto> findByProperty(String propertyName, final Object value, int limite);

	public List<ConceptoPresupuesto> findLikeProperty(String propertyName, final String value, int limite);
}
