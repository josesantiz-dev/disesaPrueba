package net.giro.cxp.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.EjercicioHolder;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PagosGastosHolder;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.SucursalExt;
import net.giro.ne.beans.Empresa;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.respuesta.Respuesta;

@Remote
public interface PagosGastosRem {
	public void showSystemOuts(boolean value);
	
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(PagosGastos entity) throws Exception;
	
	public List<PagosGastos> saveOrUpdateList(List<PagosGastos> entities) throws Exception;

	public void update(PagosGastos pojoEntity) throws Exception;

	public PagosGastos findById(Long idPagosGastos);
	
	public List<PagosGastos> findAll(String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner) throws Exception;

	public List<PagosGastos> findLike(String value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception;
	
	public List<PagosGastos> findLikeProperty(String propertyName, Object value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception;

	public List<PagosGastos> findLikeProperty(String propertyName, Object value, int limite) throws Exception;

	public List<PagosGastos> findByProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<PagosGastos> findInProperty(String propertyName, List<Object> values, int limite) throws Exception;

	public List<PagosGastos> busquedaDinamica(String value) throws Exception;
	
	public Respuesta nuevoNegocioProveedor(String uniqueValue, String nombre, String rfc, String tipoPersona);
	
	public void contabilizador(Long idPagosGastos) throws Exception;
	
	// --------------------------------------------------------------------------------------------------------------
	
	public List<PagosGastos> findCajaChicaLikeProperty(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception;

	public List<PagosGastos> findProvisionLikeProperty(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception;

	public List<PagosGastos> findRegistroGastosLikeProperty(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception;

	public List<PagosGastos> findMovimientoCuentasLikeProperty(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception;

	public List<PagosGastos> findGastoComprobarLikeProperty(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception;
	
	// --------------------------------------------------------------------------------------------------------------
	
	public Respuesta salvar(PagosGastos entity) throws Exception;
	
	/***
	 * Metodo para Cancelar un PagosGastos (CajaChica, RegistroGasto, GastosAComprobar)
	 * @param entity a Cancelar
	 * @throws Exception
	 */
	public PagosGastos cancelar(PagosGastos entity) throws Exception;
	
	public Respuesta mensajeSaldoCtas(PagosGastos entity) throws Exception;
	
	public Respuesta mensajeSaldoCtas(PagosGastosExt pojoEntityExt) throws Exception;
	
	public Empresa findEmpresa(PagosGastos movCta);
	
	public List<PagosGastos> findTransferencias(String propertyName, Object value, String v_tipo, int max, String sucursales);
	
	public List<PagosGastos> findLikeGtosPorComprobarPersona(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max);
	
	public Respuesta enviarTransaccion(PagosGastos entity) throws Exception;

	public List<PagosGastosHolder> encapsularPagosGastos(List<PagosGastos> listPagosGastos) throws Exception;

	public List<EjercicioHolder> encapsularProvisiones(List<PagosGastos> provisiones) throws Exception;

	// ------------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// ------------------------------------------------------------------------------------------------

	public PagosGastos convertir(PagosGastosExt extendido) throws Exception;
	
	public PagosGastosExt convertir(PagosGastos entity) throws Exception;

	// ------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------

	public long save(PagosGastosExt extendido) throws Exception;

	public Respuesta salvar(PagosGastosExt extendido) throws Exception; //, boolean band) throws Exception;
	
	public void update(PagosGastosExt extendido) throws Exception;

	public PagosGastosExt actualizar(PagosGastosExt extendido, String estatus, Date fech_modificacion) throws Exception;

	/***
	 * Metodo para Cancelar un PagosGastos (CajaChica, RegistroGasto, GastosAComprobar)
	 * @param entity a Cancelar
	 * @throws Exception
	 */
	public PagosGastosExt cancelar(PagosGastosExt extendido) throws Exception;

	public Respuesta cancelacion(PagosGastosExt extendido, Date fech_modificacion) throws Exception;
	
	public PagosGastosExt findExtById(Long idPagosGastos) throws Exception;

	// --------------------------------------------------------------------------------------------------------------

	public List<PagosGastosExt> findAllExt(String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner) throws Exception;
	
	public List<PagosGastosExt> findLikeExt(String value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception;
	
	public List<PagosGastosExt> findLikePropertyExt(String propertyName, Object value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception;

	// --------------------------------------------------------------------------------------------------------------
	
	public List<PagosGastosExt> findCajaChicaLikePropertyExt(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception;

	public List<PagosGastosExt> findProvisionLikePropertyExt(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception;

	public List<PagosGastosExt> findRegistroGastosLikePropertyExt(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception;

	public List<PagosGastosExt> findMovimientoCuentasLikePropertyExt(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception;
	
	public List<PagosGastosExt> findGastoComprobarLikePropertyExt(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception;

	// --------------------------------------------------------------------------------------------------------------
	
	public List<PagosGastosExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<PagosGastosExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<PagosGastosExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;
	
	public List<PagosGastosExt> findLikeGtosComprobar(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales);
	
	public List<PagosGastosExt> findTransferenciasExt(String propertyName, Object value, String v_tipo, int max, String sucursales);
	
	public List<PersonaExt> findLikePersonasConGastos(Object value, ConGrupoValores valGpo, String tipoPersona, String tipo, String estatus, int max, Date fecha, String sucursales);
	
	public SucursalExt findSucursalById(long idSucursal);
	
	public PersonaExt findPersonaById(long idPersona);
	
	public PersonaExt findPersonaById(long idPersona, String tipoPersona);
	
	public CtasBancoExt findCuentaBancariaById(long idCuentaBancaria);
	
	public List<PagosGastosExt> buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda);

	public List<PagosGastosExt> findLikeGtosPorComprobarPersonaExt(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max);

	public List<PagosGastosHolder> encapsularPagosGastosExt(List<PagosGastosExt> listPagosGastos) throws Exception;
}

/* 
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ---------------------------------------------------------------------------------------------------------------- 
 *  VER |   FECHA    |   	AUTOR 		| DESCRIPCIÓN 
 * ---------------------------------------------------------------------------------------------------------------- 
 *  2.2 | 2017-04-24 | Javier Tirado 	| Añado los metods convertir de pojo PagosGastos a extendido y viceversa
 */