package net.giro.adp.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraEmpleado;
import net.giro.adp.beans.ObraEmpleadoExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface ObraEmpleadoRem {
	public void showSystemOuts(boolean value);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(ObraEmpleado entity) throws Exception;

	public List<ObraEmpleado> saveOrUpdateList(List<ObraEmpleado> entities) throws Exception;
	
	public void delete(Long idObraEmpleado) throws Exception;

	public void update(ObraEmpleado entity) throws Exception;

	public ObraEmpleado findById(Long idObraEmpleado);

	public List<ObraEmpleado> findAll(long idObra) throws Exception;

	public List<ObraEmpleado> findAll(long idObra, String orderBy) throws Exception;
	
	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, int limite);

	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, String orderBy, int limite);

	public List<ObraEmpleado> findByProperty(String propertyName, Object value, int limite);

	public List<ObraEmpleado> findByProperty(String propertyName, Object value, String orderBy, int limite);
	
	public Respuesta altaRetroactiva(long idRegistro, Date fechaBase);
	
	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	public Long save(ObraEmpleadoExt extendido) throws Exception;
	
	public void update(ObraEmpleadoExt extendido) throws Exception;
	
	public ObraEmpleadoExt findByIdExt(Long idObraEmpleado) throws Exception;

	public List<ObraEmpleadoExt> findExtAll(long idObra) throws Exception;
}
