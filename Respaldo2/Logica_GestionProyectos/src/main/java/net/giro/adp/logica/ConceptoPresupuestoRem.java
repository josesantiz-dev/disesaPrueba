package net.giro.adp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ConceptoPresupuesto;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ConceptoPresupuestoRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(ConceptoPresupuesto entity) throws Exception;

	public List<ConceptoPresupuesto> saveOrUpdateList(List<ConceptoPresupuesto> entities) throws Exception;

	public void update(ConceptoPresupuesto entity) throws Exception;

	public void delete(ConceptoPresupuesto entity) throws Exception;

	public ConceptoPresupuesto findById(long id);
	
	public List<ConceptoPresupuesto> findAllActivos();

	public List<ConceptoPresupuesto> findByProperty(String propertyName, final Object value, int max);

	public List<ConceptoPresupuesto> findLikeProperty(String propertyName, final String value, int max);
}
