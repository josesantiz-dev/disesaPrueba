package net.giro.inventarios.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface AlmacenMovimientosRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(AlmacenMovimientos entity) throws Exception;

	public List<AlmacenMovimientos> saveOrUpdateList(List<AlmacenMovimientos> entities) throws Exception;

	public void update(AlmacenMovimientos entity) throws Exception;
	
	public Respuesta cancelar(AlmacenMovimientos entity) throws Exception;
	
	public void delete(Long idAlmacenMovimientos) throws Exception;

	public AlmacenMovimientos findById(Long idAlmacenMovimientos);

	public List<AlmacenMovimientos> findLike(String value, long idAlmacen, int tipoMovimiento, String tipoEntrada, String orderBy, int limite) throws Exception;
	
	public List<AlmacenMovimientos> findLike(String value, long idAlmacen, int tipoMovimiento, String tipoEntrada, boolean incluyeCancelados, boolean incluyeSistema, String orderBy, int limite) throws Exception;

	public List<AlmacenMovimientos> findLikeProperty(String propertyName, Object propertyValue, long idAlmacen, int tipoMovimiento, String tipoEntrada, boolean incluyeCancelados, boolean incluyeSistema, String orderBy, int limite) throws Exception;

	public List<AlmacenMovimientos> findLikeProperty(String propertyName, Object propertyValue, long idAlmacen, int tipoMovimiento, String tipoEntrada, String orderBy, int limite) throws Exception;

	public List<AlmacenMovimientos> findLikeProperty(String propertyName, String value, int tipoMovimiento, String tipoEntrada, int limite) throws Exception;

	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value);

	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value, int tipoMovimiento, String tipoEntrada, int limite);
	
	public List<AlmacenMovimientos> findSalidaByTraspaso(long idTraspaso, int tipoTraspaso, int limite);
	
	public boolean backOfficeInventario(Long idMovimiento, String referencia) throws Exception;

	// ----------------------------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// ----------------------------------------------------------------------------------------------------------------
	
	public AlmacenMovimientos convertir(AlmacenMovimientosExt target);

	public AlmacenMovimientosExt convertir(AlmacenMovimientos target);

	// ----------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------------------------------------------------------------
	
	public Long save(AlmacenMovimientosExt extendido) throws Exception;

	public void update(AlmacenMovimientosExt extendido) throws Exception;
	
	public AlmacenMovimientosExt findByIdExt(Long idAlmacenMovimientos);
}
