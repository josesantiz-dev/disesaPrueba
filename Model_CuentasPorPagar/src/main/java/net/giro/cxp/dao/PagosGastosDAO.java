package net.giro.cxp.dao;

import javax.ejb.Remote;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.giro.DAO;
import net.giro.cxp.beans.PagosGastos;

@Remote
public interface PagosGastosDAO extends DAO<PagosGastos> {
	public long save(PagosGastos entity, long codigoEmpresa) throws Exception;
	
	public List<PagosGastos> saveOrUpdateList(List<PagosGastos> entities, long codigoEmpresa) throws Exception;

	public List<PagosGastos> findAll(String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idEmpresa, long idOwner) throws Exception;
	
	public List<PagosGastos> findLike(String value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idEmpresa, long idOwner, int limite) throws Exception;
	
	public List<PagosGastos> findLikeProperty(String propertyName, Object value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idEmpresa, long idOwner, int limite) throws Exception;

	public List<PagosGastos> findLikeProperty(String propertyName, Object value, String tipo, String estatus, String orderBy, long idEmpresa, int limite) throws Exception;
	
	public List<PagosGastos> findByProperty(String propertyName, Object value, String tipo, String estatus, String orderBy, long idEmpresa, int limite) throws Exception;

	// --------------------------------------------------------------------------------------------------------------
	
	public int findConsecutivoByBeneficiario(long beneficiario, String tipo, String estatus, long idEmpresa);

	// --------------------------------------------------------------------------------------------------------------
	
	public List<PagosGastos> findInProperty(String columnName, List<Object> values, String estatus, String orderBy, long idEmpresa, int limite) throws Exception;
	
	public List<PagosGastos> findByProperties(HashMap<String, Object> params, String estatus, String orderBy, long idEmpresa, int limite) throws Exception;
	
	public List<PagosGastos> findLikeGtosComprobar(String propertyName, Object value, String v_tipo, String v_estatus1, long idEmpresa, int limite, String sucursales);
	
	public List<PagosGastos> findTransferencias(String propertyName, Object value, String v_tipo, long idEmpresa, int limite, String sucursales);
	
	public List<PagosGastos> findLikeGtosPorComprobarPersona(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, long idEmpresa, int limite);
	
	public List<PagosGastos> findLikePersonasConGastos(String beneficiario, String tipoPer, String tipo, String estatus, long idEmpresa, int limite, Date fecha, String sucursales);
	
	public List<PagosGastos> buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda, long idEmpresa);
}
