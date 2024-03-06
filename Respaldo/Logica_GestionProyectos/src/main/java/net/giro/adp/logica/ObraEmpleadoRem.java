package net.giro.adp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraEmpleado;
import net.giro.adp.beans.ObraEmpleadoExt;
import net.giro.comun.ExcepConstraint;

@Remote
public interface ObraEmpleadoRem {
	public void showSystemOuts(boolean value);
	
	public Long save(ObraEmpleado entity) throws ExcepConstraint;
	public Long save(ObraEmpleadoExt entityExt) throws ExcepConstraint;
	
	public void delete(Long idObraEmpleado) throws ExcepConstraint;

	public void update(ObraEmpleado entity) throws ExcepConstraint;
	public void update(ObraEmpleadoExt entityExt) throws ExcepConstraint;

	public List<ObraEmpleado> findAll();

	public ObraEmpleado findById(Long id);
	public ObraEmpleadoExt findByIdExt(Long id) throws Exception;

	public List<ObraEmpleado> findByProperty(String propertyName, Object value);
	public List<ObraEmpleado> findByProperty(String propertyName, Object value, int max);

	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value);
	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, int max);
	
	public List<ObraEmpleadoExt> findByPropertyExt(String propertyName, Object value) throws Exception;
	public List<ObraEmpleadoExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception;
	
	public List<ObraEmpleadoExt> findLikePropertyExt(String propertyName, Object value) throws Exception;
	public List<ObraEmpleadoExt> findLikePropertyExt(String propertyName, Object value, int max) throws Exception;
}
