package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.beans.ChecadorDetalleExt;

@Remote
public interface ChecadorDetalleRem {
	public void setInfoSecion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(ChecadorDetalle ChecadorDetalle) throws ExcepConstraint;
	public Long save(ChecadorDetalleExt ChecadorDetalleExt) throws ExcepConstraint;
	
	public void update(ChecadorDetalle ChecadorDetalle) throws ExcepConstraint;
	public void update(ChecadorDetalleExt ChecadorDetalleExt) throws ExcepConstraint;
	
	public void delete(Long ChecadorDetalle) throws ExcepConstraint;

	public ChecadorDetalle findById(Long id);
	public ChecadorDetalleExt findExtById(Long id) throws Exception;
	
	public List<ChecadorDetalle> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	public List<ChecadorDetalleExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ChecadorDetalle> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	public List<ChecadorDetalleExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ChecadorDetalle> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	public List<ChecadorDetalleExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<ChecadorDetalle> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	public List<ChecadorDetalleExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<ChecadorDetalle> findLikeProperties(HashMap<String, Object> params, int limite) throws Exception;
	public List<ChecadorDetalleExt> findExtLikeProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public Respuesta analizaDetalles(String fileName, String fileExtension, byte[] fileSrc) throws Exception;
	
	public boolean existeAsistencia(Long idChecador, Long idEmpleado, Date fecha) throws Exception;
	public Long existeAsistenciaID(Long idChecador, Long idEmpleado, Date fecha) throws Exception;
	public ChecadorDetalle existeAsistenciaPojo(Long idChecador, Long idEmpleado, Date fecha) throws Exception;
	
	public List<ChecadorDetalle> findByDates(Date fechaDesde, Date fechaHasta) throws Exception;
	
	public ChecadorDetalle convertChecadorDetalleExtToChecadorDetalle(ChecadorDetalleExt entity) throws Exception;
	public ChecadorDetalleExt convertChecadorDetalleToChecadorDetalleExt(ChecadorDetalle entity) throws Exception;
}
