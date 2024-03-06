package net.giro.inventarios.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.comun.TipoTraspaso;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface AlmacenTraspasoRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(AlmacenTraspaso entity) throws Exception;
	
	public List<AlmacenTraspaso> saveOrUpdateList(List<AlmacenTraspaso> entities) throws Exception;

	public void update(AlmacenTraspaso entity) throws Exception;

	public Respuesta cancelar(AlmacenTraspaso entity) throws Exception;
	
	public void delete(long idAlmacenTraspaso) throws Exception;

	public AlmacenTraspaso findById(long idAlmacenTraspaso);

	public List<AlmacenTraspaso> findLike(String value, long idAlmacenOrigen, long idAlmacenDestino, TipoTraspaso tipoTraspaso, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite);

	public List<AlmacenTraspaso> findLikeProperty(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, TipoTraspaso tipoTraspaso, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite);

	public List<AlmacenTraspaso> findLikeProperty(String propertyName, String value);

	public List<AlmacenTraspaso> findByProperty(String propertyName, Object value);

	// -----------------------------------------------------------------------------------------------

	public List<AlmacenTraspaso> findLikePropertyTraspasoDevolucion(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite);

	public List<AlmacenTraspaso> findTraspasoLikeProperty(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite);

	public List<AlmacenTraspaso> findDevolucionLikeProperty(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite);

	public List<AlmacenTraspaso> findSolicitudLikeProperty(String propertyName, Object value, long idAlmacen, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite);

	// -----------------------------------------------------------------------------------------------
	
	/**
	 * Realiza los registros necesarios para soportar los movimientos que implique el traspaso (traspaso o devolucion).
	 * @param idTraspaso
	 * @return
	 * @throws Exception
	 */
	public boolean postTraspaso(Long idTraspaso) throws Exception;

	// -----------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// -----------------------------------------------------------------------------------------------
	
	public AlmacenTraspaso convertir(AlmacenTraspasoExt entity);
	
	public AlmacenTraspasoExt convertir(AlmacenTraspaso entity);

	// -----------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------
	
	public Long save(AlmacenTraspasoExt extendido) throws Exception;
	
	public void update(AlmacenTraspasoExt extendido) throws Exception;
	
	public AlmacenTraspasoExt findByIdExt(long idAlmacenTraspaso);

	public List<AlmacenTraspasoExt> findExtLikeProperty(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, TipoTraspaso tipoTraspaso, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite);
}
