package net.giro.compras.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;

@Remote
public interface OrdenCompraRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public Long save(OrdenCompra entity) throws Exception;
	
	public void update(OrdenCompra entity) throws Exception;
    
    public List<OrdenCompra> saveOrUpdateList(List<OrdenCompra> entities) throws Exception;

	public Respuesta guardar(OrdenCompra entity);
	
	public Respuesta cancelar(long idOrdenCompra, long idUsuario) throws Exception;
	
	public Respuesta cancelar(long idOrdenCompra, long idUsuario, boolean forzarCancelacion) throws Exception;

	public Respuesta autorizar(long idOrdenCompra, boolean forzarCancelacion, boolean incluyeExtendido);
	
	public void delete(long idOrdenCompra) throws Exception;

	public OrdenCompra findById(long idOrdenCompra);

	public OrdenCompra findByCodigo(String codigo) throws Exception;

	public List<OrdenCompra> findAll(long idObra) throws Exception;
	
	public List<OrdenCompra> findAll(long idObra, boolean incluyeSistema, boolean incluyeCanceladas, String orderBy) throws Exception;

	public List<OrdenCompra> findLike(String value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<OrdenCompra> findLike(String value) throws Exception;
	
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, long idObra, int limite) throws Exception;

	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, long idObra, boolean incluyeCanceladas, int limite) throws Exception;

	public List<OrdenCompra> findByProperty(String propertyName, Object value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<OrdenCompra> findByProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<OrdenCompra> findByProperty(String propertyName, Object value, long idObra, int limite) throws Exception;
	
	public List<OrdenCompra> findByProperty(String propertyName, Object value, long idObra, boolean incluyeCanceladas, int limite) throws Exception;

	public List<OrdenCompra> findByObra(long idObra, int estatus, boolean incluyeSistema, String orderBy, int limite) throws Exception;

	public List<OrdenCompra> findByCotizacion(long idCotizacion, boolean incluyeCanceladas, int limite) throws Exception;
	
	// ----------------------------------------------------------------------------------------------------------------------------------------

	public List<Moneda> findMonedas() throws Exception;
	
	public double tipoCambio(long idMonedaOrigen, long idMonedaDestino, Date fecha) throws Exception;

	public List<OrdenCompra> findNoCompletas(String propertyName, Object value, int limite) throws Exception;
	
	public List<OrdenCompra> findNoAutorizadas(String property, Object value, long idObra, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;
	
	public int findConsecutivoByProveedor(long idProveedor) throws Exception;
	
	public Respuesta procesarArchivo(byte[] fileSrc, HashMap<String, String> dataMap) throws Exception;
	
	public Respuesta procesarLibro(byte[] fileSrc, HashMap<String, String> dataMap) throws Exception;
	
	public List<OrdenCompra> busquedaDinamica(String value) throws Exception;

	public OrdenCompra asignarFolio(OrdenCompra entity) throws Exception;

	/**
	 * Ejecuta evento para actualizar el precio del producto en el catalogo
	 * @param idOrdenCompra
	 * @return
	 */
	public boolean backOfficeOrdenCompra(long idOrdenCompra);

	// -----------------------------------------------------------------------------------------------------------------------
	// CONVERTIDOR 
	// -----------------------------------------------------------------------------------------------------------------------
	
	public OrdenCompra convertir(OrdenCompraExt extendido) throws Exception;
	
	public OrdenCompraExt convertir(OrdenCompra entity) throws Exception;

	// -----------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// -----------------------------------------------------------------------------------------------------------------------

	public Long save(OrdenCompraExt entityExt) throws Exception;
	
	public void update(OrdenCompraExt entityExt) throws Exception;

	public Respuesta guardar(OrdenCompraExt entity);
	
	public OrdenCompraExt findExtById(long idOrdenCompra) throws Exception;
}
