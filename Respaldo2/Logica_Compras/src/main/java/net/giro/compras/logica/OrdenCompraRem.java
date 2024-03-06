package net.giro.compras.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.OrdenCompraDetalleExt;
import net.giro.compras.beans.OrdenCompraExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;

@Remote
public interface OrdenCompraRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public void OrderBy(String orderBy);
	
	public void estatus(Long estatus);
	
	public Long save(OrdenCompra entity) throws Exception;
	
	public Respuesta save(OrdenCompra entity, List<OrdenCompraDetalle> details) throws Exception;
	
	public void update(OrdenCompra entity) throws Exception;
    
    public List<OrdenCompra> saveOrUpdateList(List<OrdenCompra> entities) throws Exception;
	
	public Respuesta cancelar(long idOrdenCompra, long idUsuario) throws Exception;
	
	public Respuesta cancelar(long idOrdenCompra, long idUsuario, boolean forzarCancelacion) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public OrdenCompra findById(Long id);
	
	public OrdenCompra findByCodigo(String codigo);
	
	public List<OrdenCompra> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OrdenCompra> findByProperty(String propertyName, final Object value, long idObra, int limite) throws Exception;
	
	public List<OrdenCompra> findLike(String value) throws Exception;

	public List<OrdenCompra> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OrdenCompra> findLikeProperty(String propertyName, final Object value, long idObra, int limite) throws Exception;

	public List<OrdenCompra> findInProperty(String columnName, List<Object> values) throws Exception;	
	
	public List<OrdenCompra> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<OrdenCompra> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<Moneda> findMonedas() throws Exception;

	public List<OrdenCompra> findNoCompletas(String propertyName, Object value, int limite) throws Exception;
	
	public int findConsecutivoByProveedor(long idProveedor) throws Exception;
	
	public Respuesta procesarArchivo(byte[] fileSrc, HashMap<String, String> dataMap) throws Exception;
	
	public Respuesta procesarLibro(byte[] fileSrc, HashMap<String, String> dataMap) throws Exception;

	// -----------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// -----------------------------------------------------------------------------------------------------------------------

	
	public Long save(OrdenCompraExt entityExt) throws Exception;
	
	public Respuesta save(OrdenCompraExt entity, List<OrdenCompraDetalleExt> details) throws Exception;
	
	public void update(OrdenCompraExt entityExt) throws Exception;
	
	public OrdenCompraExt findExtById(Long id) throws Exception;
	
	public OrdenCompraExt findExtByCodigo(String codigo) throws Exception;
	
	public List<OrdenCompraExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OrdenCompraExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OrdenCompraExt> findExtInProperty(String columnName, List<Object> values) throws Exception;
	
	public List<OrdenCompraExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public List<OrdenCompraExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<OrdenCompraExt> findNoCompletasExt(String propertyName, Object value, int limite) throws Exception;

	// -----------------------------------------------------------------------------------------------------------------------
	// PRIVADOS 
	// -----------------------------------------------------------------------------------------------------------------------

	/**
	 * Ejecuta evento para actualizar el precio del producto en el catalogo
	 * @param idOrdenCompra
	 * @return
	 */
	public boolean backOfficeOrdenCompra(Long idOrdenCompra);
	
	/**
	 * Ejecuta evento para bloquear o liberar la Cotizacion que origino la Orden de Compra
	 * @param idOrdenCompra
	 * @return
	 */
	public boolean backOfficeCotizacion(Long idOrdenCompra);
	
	/**
	 * Ejecuta evento para bloquear o liberar la Requisicion que origino la Orden de Compra
	 * @param idOrdenCompra
	 * @return
	 */
	public boolean backOfficeRequisicion(Long idOrdenCompra);
}
