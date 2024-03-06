package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.OrdenCompra;

@Remote
public interface OrdenCompraDAO extends DAO<OrdenCompra> {
    public long save(OrdenCompra entity, long codigoEmpresa) throws Exception;
    
    public List<OrdenCompra> saveOrUpdateList(List<OrdenCompra> entities, long codigoEmpresa) throws Exception;

    public List<OrdenCompra> findAll(long idObra, boolean incluyeSistema, boolean incluyeCanceladas, String orderBy) throws Exception;
    
	public List<OrdenCompra> findLike(String value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<OrdenCompra> findLikeProperty(String property, Object value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<OrdenCompra> findByProperty(String property, Object value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<OrdenCompra> findByObra(long idObra, int estatus, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public int findConsecutivoByProveedor(long idProveedor, long idEmpresa) throws Exception;
	
	public List<OrdenCompra> findNoCompletas(String property, Object value, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<OrdenCompra> findNoAutorizadas(String property, Object value, long idObra, boolean incluyeCanceladas, long idEmpresa, String orderBy, int limite) throws Exception;
}
