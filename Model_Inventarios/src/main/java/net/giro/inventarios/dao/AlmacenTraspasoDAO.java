package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.AlmacenTraspaso;

@Remote
public interface AlmacenTraspasoDAO extends DAO<AlmacenTraspaso> {
	public long save(AlmacenTraspaso entity, long codigoEmpresa) throws Exception;
	
	public List<AlmacenTraspaso> saveOrUpdateList(List<AlmacenTraspaso> entities, long codigoEmpresa) throws Exception;
	
	/**
	 * 
	 * @param value Valor de busqueda
	 * @param idAlmacenOrigen Opcional
	 * @param idAlmacenDestino Opcional
	 * @param tipo Tipo de Traspaso (1:Traspaso, 2:Devolucion, 3:Solicitud de Bodega)
	 * @param incluyeCompleto
	 * @param incluyeSistema
	 * @param orderBy
	 * @param idEmpresa
	 * @param limite
	 * @return
	 */
	public List<AlmacenTraspaso> findLike(String value, long idAlmacenOrigen, long idAlmacenDestino, int tipo, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, long idEmpresa, int limite);

	/**
	 * 
	 * @param propertyName Campo de busqueda
	 * @param value Valor de busqueda
	 * @param idAlmacenOrigen Opcional
	 * @param idAlmacenDestino Opcional
	 * @param tipo Tipo de Traspaso (1:Traspaso, 2:Devolucion, 3:Solicitud de Bodega)
	 * @param incluyeCompleto 
	 * @param incluyeSistema
	 * @param orderBy
	 * @param idEmpresa
	 * @param limite
	 * @return
	 */
	public List<AlmacenTraspaso> findLikeProperty(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, int tipo, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, long idEmpresa, int limite);

	public List<AlmacenTraspaso> findByProperty(String propertyName, Object value, int tipo, boolean incluyeCompleto, boolean incluyeSistema, long idEmpresa, int limite);
}
