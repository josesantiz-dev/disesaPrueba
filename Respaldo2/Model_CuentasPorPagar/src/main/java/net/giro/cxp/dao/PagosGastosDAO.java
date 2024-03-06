package net.giro.cxp.dao;

import javax.ejb.Remote;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.giro.DAO;
import net.giro.cxp.beans.PagosGastos;

@Remote
public interface PagosGastosDAO extends DAO<PagosGastos> {
	public void orderBy(String orderBy);
	
	public void estatus(Long estatus);

	public void setEmpresa(Long idEmpresa);
		
	public long save(PagosGastos entity) throws Exception;
	
	public List<PagosGastos> saveOrUpdateList(List<PagosGastos> entities) throws Exception;
	
	public List<PagosGastos> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<PagosGastos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<PagosGastos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	
	public List<PagosGastos> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public List<PagosGastos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public PagosGastos findAllById(Long id);

	public List<PagosGastos> findLikeBenefTipoPersona(String beneficiario, String tipoPer, String tipo, String estatus, int max, String sucursales);

	public List<PagosGastos> findLikeGtosComprobar(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales);
	
	public List<PagosGastos> findLikeMovCtas(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales);
	
	public List<PagosGastos> findTransferencias(String propertyName, Object value, String v_tipo, int max, String sucursales);
		
	public List<PagosGastos> findLikeCajaChica(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales);
	
	public List<PagosGastos> findProvisiones(String propertyName, Object value, String estatus, String estatus2, int max, String sucursales);
	
	public List<PagosGastos> findTransPorDia(Date fecha) throws Exception;
	
	public List<PagosGastos> findLikeGtosPorComprobarPersona(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max);

	public List<PagosGastos> findPersonaEnUso(long persona);
	
	public boolean existeTransferencia(long ctaDestino, String folioAutorizacion, Date fecha);
	
	public List<PagosGastos> findCtasBancoById(String value, String v_tipo, String v_estatus1, int max);
	
	public List<PagosGastos> findLikePersonasConGastos(String beneficiario, String tipoPer, String tipo, String estatus, int max, Date fecha, String sucursales);
	
	public List<PagosGastos> buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda);
	
	public int findConsecutivoByBeneficiario(long beneficiario, String tipo, String estatus);
}
