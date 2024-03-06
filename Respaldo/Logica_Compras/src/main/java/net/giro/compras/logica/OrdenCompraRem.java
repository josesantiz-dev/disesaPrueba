package net.giro.compras.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraExt;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;

@Remote
public interface OrdenCompraRem {
	public void setInfoSecion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public void OrderBy(String orderBy);
	
	public void estatus(Long estatus);

	public Long save(OrdenCompra entity) throws ExcepConstraint;
	
	public Long save(OrdenCompraExt entityExt) throws ExcepConstraint;
	
	public void update(OrdenCompra entity) throws ExcepConstraint;
	
	public void update(OrdenCompraExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public OrdenCompra findById(Long id);
	
	public OrdenCompraExt findExtById(Long id) throws Exception;
	
	public List<OrdenCompra> findByProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<OrdenCompraExt> findExtByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<OrdenCompra> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<OrdenCompraExt> findExtLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<OrdenCompra> findInProperty(String columnName, List<Object> values) throws Exception;	
	
	public List<OrdenCompraExt> findExtInProperty(String columnName, List<Object> values) throws Exception;
	
	public List<OrdenCompra> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public List<OrdenCompraExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<OrdenCompra> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<OrdenCompraExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<Moneda> findMonedas() throws Exception;

	public List<OrdenCompra> findNoCompletas(String propertyName, Object value, int max) throws Exception;
	
	public List<OrdenCompraExt> findNoCompletasExt(String propertyName, Object value, int max) throws Exception;
	
	public int findConsecutivoByProveedor(long idProveedor) throws Exception;
	
	public Respuesta procesarArchivo(byte[] fileSrc, HashMap<String, String> dataMap) throws Exception;
}
