package net.giro.adp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraEmpleado;
import net.giro.adp.beans.ObraEmpleadoExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraEmpleadoRem {
	public void showSystemOuts(boolean value);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(ObraEmpleado entity) throws Exception;

	public List<ObraEmpleado> saveOrUpdateList(List<ObraEmpleado> entities) throws Exception;
	
	public void delete(Long idObraEmpleado) throws Exception;

	public void update(ObraEmpleado entity) throws Exception;

	public List<ObraEmpleado> findAll();

	public ObraEmpleado findById(Long id);

	public List<ObraEmpleado> findByProperty(String propertyName, Object value);

	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value);
	
	public List<ObraEmpleadoExt> findByPropertyExt(String propertyName, Object value) throws Exception;
	
	public List<ObraEmpleadoExt> findLikePropertyExt(String propertyName, Object value) throws Exception;

	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	public Long save(ObraEmpleadoExt entityExt) throws Exception;
	
	public void update(ObraEmpleadoExt entityExt) throws Exception;
	
	public ObraEmpleadoExt findByIdExt(Long id) throws Exception;
	
	public List<ObraEmpleado> findByProperty(String propertyName, Object value, int max);
	
	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, int max);
	
	public List<ObraEmpleadoExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception;
	
	public List<ObraEmpleadoExt> findLikePropertyExt(String propertyName, Object value, int max) throws Exception;

}
