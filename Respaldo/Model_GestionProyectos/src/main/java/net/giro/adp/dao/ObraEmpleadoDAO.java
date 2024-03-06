package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraEmpleado;

@Remote
public interface ObraEmpleadoDAO extends DAO<ObraEmpleado> {
	public List<ObraEmpleado> findByProperty(String propertyName, Object value, int max);

	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, int max);
}
