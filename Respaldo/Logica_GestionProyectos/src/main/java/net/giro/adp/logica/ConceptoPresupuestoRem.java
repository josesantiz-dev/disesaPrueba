package net.giro.adp.logica;

import java.util.List;
import javax.ejb.Remote;
import net.giro.comun.ExcepConstraint;

import net.giro.adp.beans.ConceptoPresupuesto;
import net.giro.adp.beans.ConceptoPresupuestoExt;

@Remote
public interface ConceptoPresupuestoRem {

	public Long save(ConceptoPresupuesto entity) throws ExcepConstraint;
	public Long save(ConceptoPresupuestoExt entityExt) throws ExcepConstraint;

	public void delete(ConceptoPresupuesto entity) throws ExcepConstraint;
	public void delete(ConceptoPresupuestoExt entity) throws ExcepConstraint;

	public ConceptoPresupuesto update(ConceptoPresupuesto entity) throws ExcepConstraint;
	public ConceptoPresupuesto update(ConceptoPresupuestoExt entity) throws ExcepConstraint;

	public ConceptoPresupuesto findById(long id);
	public ConceptoPresupuestoExt findExtById(long id);

	public List<ConceptoPresupuesto> findByProperty(String propertyName, final Object value, int max);
	public List<ConceptoPresupuestoExt> findExtByProperty(String propertyName, final Object value, int max);

	public List<ConceptoPresupuesto> findLikeProperty(String propertyName, final Object value, int max);
	public List<ConceptoPresupuestoExt> findExtLikeProperty(String propertyName, final Object value, int max);
	
	public List<ConceptoPresupuesto> findAllActivos();
	public List<ConceptoPresupuestoExt> findAllExtActivos();
}
