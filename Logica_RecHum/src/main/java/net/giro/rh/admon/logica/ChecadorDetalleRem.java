package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.beans.ChecadorDetalleExt;

@Remote
public interface ChecadorDetalleRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public Long save(ChecadorDetalle ChecadorDetalle) throws Exception;

	public List<ChecadorDetalle> saveOrUpdateList(List<ChecadorDetalle> entities) throws Exception;
	
	public void update(ChecadorDetalle ChecadorDetalle) throws Exception;
	
	public void delete(Long idChecadorDetalle) throws Exception;

	public void delete(List<ChecadorDetalle> entities) throws Exception;

	public ChecadorDetalle findById(Long idChecadorDetalle);

	public List<ChecadorDetalle> findAll(long idChecador, String orderBy) throws Exception;
	
	public List<ChecadorDetalle> findLike(String value, long idObra, String orderBy, int limite) throws Exception;
	
	public List<ChecadorDetalle> findLikeProperty(String propertyName, final Object value, long idObra, String orderBy, int limite) throws Exception;

	public List<ChecadorDetalle> findByProperty(String propertyName, final Object value, long idObra, String orderBy, int limite) throws Exception;

	public List<ChecadorDetalle> findByDates(Date fechaDesde, Date fechaHasta, String orderBy) throws Exception;
	
	public List<ChecadorDetalle> findAsistenciasPosteriorFecha(long idEmpleado, Date fecha, String orderBy) throws Exception;

	/**
	 * Listado de asistencias desde la fecha indicada hasta el dia actual donde esta el trabajador indicado
	 * @param idObra Obra donde debemos revisar las listas de asistencia
	 * @param idEmpleado Empleado de busqueda
	 * @param fechaBase Fecha a partir de la cual se partira la busqueda
	 * @return
	 * @throws Exception
	 */
	//public List<ChecadorDetalle> findByObraEmpleado(long idObra, long idEmpleado, Date fechaBase, String orderBy) throws Exception;

	public Respuesta analizaDetalles(String fileName, String fileExtension, byte[] fileSrc) throws Exception;
	
	public boolean existeAsistencia(Long idChecador, Long idEmpleado, Date fecha) throws Exception;
	
	public Long existeAsistenciaID(Long idChecador, Long idEmpleado, Date fecha) throws Exception;
	
	public ChecadorDetalle existeAsistenciaPojo(Long idChecador, Long idEmpleado, Date fecha) throws Exception;
	
	public Respuesta importarAsistencia(String fileName, byte[] fileSrc, HashMap<String, String> mapLayout) throws Exception;
	
	// -----------------------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// -----------------------------------------------------------------------------------------------------------

	public ChecadorDetalle convertir(ChecadorDetalleExt extendido) throws Exception;
	
	public ChecadorDetalleExt convertir(ChecadorDetalle entity) throws Exception;

	// -----------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------------------

	public Long save(ChecadorDetalleExt extendido) throws Exception;

	public List<ChecadorDetalleExt> saveOrUpdateListExt(List<ChecadorDetalleExt> extendidos) throws Exception;
	
	public void update(ChecadorDetalleExt extendido) throws Exception;
	
	public ChecadorDetalleExt findExtById(Long idChecadorDetalle) throws Exception;
}
