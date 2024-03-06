package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.AlmacenMovimientos;

@Remote
public interface AlmacenMovimientosDAO extends DAO<AlmacenMovimientos> {
	public long save(AlmacenMovimientos entity, long codigoEmpresa) throws Exception;
	
	public List<AlmacenMovimientos> saveOrUpdateList(List<AlmacenMovimientos> entities, long codigoEmpresa) throws Exception;
	
	/**
	 * Consultas de movimientos de almacen
	 * @param value Cadena de busqueda
	 * @param idAlmacen ID almacen donde se realizara la busqueda de movimientos. Default 0 (todos los almacenes)
	 * @param tipoMovimiento Movimiento de busqueda. 0: Entrada, 1:Salida
	 * @param tipoEntrada Tipos de Entradas: OC(Orden de Compra), TR(Traspaso), DE(Devolucion),SO(Obra)
	 * @param idEmpresa
	 * @param orderBy
	 * @param limite
	 * @return
	 * @throws Exception
	 */
	public List<AlmacenMovimientos> findLike(String value, long idAlmacen, int tipoMovimiento, String tipoEntrada, boolean incluyeCancelados, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception;

	/**
	 * 
	 * @param propertyName
	 * @param value Cadena de busqueda
	 * @param idAlmacen ID almacen donde se realizara la busqueda de movimientos. Default 0 (todos los almacenes)
	 * @param tipoMovimiento Movimiento de busqueda. 0: Entrada, 1:Salida
	 * @param tipoEntrada Tipos de Entradas: OC(Orden de Compra), TR(Traspaso), DE(Devolucion),SO(Obra)
	 * @param idEmpresa
	 * @param orderBy
	 * @param limite
	 * @return
	 * @throws Exception
	 */
	public List<AlmacenMovimientos> findLikeProperty(String propertyName, Object value, long idAlmacen, int tipoMovimiento, String tipoEntrada, boolean incluyeCancelados, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value, int tipoMovimiento, String tipoEntrada, long idEmpresa, int limite);

	public List<AlmacenMovimientos> findSalidaByTraspaso(long idTraspaso, int tipoTraspaso, long idEmpresa, int limite);
}
