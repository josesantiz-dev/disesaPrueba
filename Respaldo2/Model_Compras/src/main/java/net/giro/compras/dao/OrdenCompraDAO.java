package net.giro.compras.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.OrdenCompra;
import net.giro.comun.ExcepConstraint;

@Remote
public interface OrdenCompraDAO extends DAO<OrdenCompra> {
	public void OrderBy(String orderBy);
	
	public void estatus(Long estatus);
	
	public OrdenCompra findByCodigo(String codigo, Long idEmpresa);
	
    public long save(OrdenCompra entity, Long idEmpresa) throws ExcepConstraint;
    
    public List<OrdenCompra> saveOrUpdateList(List<OrdenCompra> entities, Long idEmpresa) throws Exception;
	
	public int findConsecutivoByProveedor(long idProveedor, Long idEmpresa) throws Exception;
	
	public List<OrdenCompra> findBy(Object value, long idObra, boolean incluyeSistema, int limite, Long idEmpresa) throws Exception;
	
	public List<OrdenCompra> findByProperty(String propertyName, final Object value, int limite, Long idEmpresa) throws Exception;

	public List<OrdenCompra> findByProperty(String propertyName, final Object value, long idObra, int limite, Long idEmpresa) throws Exception;

	public List<OrdenCompra> findByProperties(HashMap<String, Object> params, int limite, Long idEmpresa) throws Exception;
	
	public List<OrdenCompra> findLike(String value, long idObra, boolean incluyeSistema, int limite, Long idEmpresa) throws Exception;

	public List<OrdenCompra> findLikeProperty(String propertyName, final Object value, int limite, Long idEmpresa) throws Exception;

	public List<OrdenCompra> findLikeProperty(String propertyName, final Object value, long idObra, int limite, Long idEmpresa) throws Exception;

	public List<OrdenCompra> findLikeProperties(HashMap<String, String> params, int limite, Long idEmpresa) throws Exception;
	
	public List<OrdenCompra> findInProperty(String columnName, List<Object> values, Long idEmpresa) throws Exception;
	
	public List<OrdenCompra> findNoCompletas(String propertyName, Object value, int limite, Long idEmpresa) throws Exception;
}
