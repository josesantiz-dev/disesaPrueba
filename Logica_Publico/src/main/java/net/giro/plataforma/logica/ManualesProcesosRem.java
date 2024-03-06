package net.giro.plataforma.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ManualesProcesos;
import net.giro.plataforma.beans.ManualesProcesosAplicaciones;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.respuesta.Respuesta;

@Remote
public interface ManualesProcesosRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public long save(ManualesProcesos entity) throws Exception;

	public ManualesProcesos saveOrUpdate(ManualesProcesos entity);
	
	public List<ManualesProcesos> saveOrUpdateList(List<ManualesProcesos> entities) throws Exception;

	public void update(ManualesProcesos entity) throws Exception;

	public Respuesta cancelar(ManualesProcesos entity);

	public void delete(ManualesProcesos entity) throws Exception;

	public ManualesProcesos findById(long idManualProceso) throws Exception;
	
	public List<ManualesProcesos> findAll(String orderBy, int limite) throws Exception;

	public List<ManualesProcesos> findLike(String value, boolean incluyeCancelados, String orderBy, int limite) throws Exception;

	public List<ManualesProcesos> findLikeProperty(String propertyName, Object value, boolean incluyeCancelados, String orderBy, int limite) throws Exception;

	public List<ManualesProcesos> findByProperty(String propertyName, Object value, boolean incluyeCancelados, String orderBy, int limite) throws Exception;
	
	// -------------------------------------------------------------------------------------------------------------------------------------------------------

	public Respuesta save(ManualesProcesos entity, List<ManualesProcesosAplicaciones> aplicaciones);

	public List<ManualesProcesosAplicaciones> findByManualProceso(long idManualProceso, boolean incluyeCancelados, String orderBy) throws Exception;

	public List<ManualesProcesosAplicaciones> findByAplicacion(long idAplicacion, boolean incluyeCancelados, String orderBy) throws Exception;

	public void deleteAplicaciones(List<ManualesProcesosAplicaciones> aplicaciones) throws Exception;

	// -------------------------------------------------------------------------------------------------------------------------------------------------------

	public List<Aplicacion> findAllAplicaciones(String orderBy) throws Exception;
}
