package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.PlantillaPresupuesto;

@Remote
public interface PlantillaPresupuestoDAO extends DAO<PlantillaPresupuesto> {
	public List<PlantillaPresupuesto> findByProperty(String propertyName, final Object value, int max);

	public List<PlantillaPresupuesto> findLikeProperty(String propertyName, final Object value, int max);
}
