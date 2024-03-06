package net.giro.cxp.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.clientes.beans.Persona;
import net.giro.comun.ExcepConstraint;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.SucursalExt;
import net.giro.ne.beans.Empresa;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CtasBanco;

@Remote
public interface PagosGastosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);
	public void estatus(Long estatus);
	
	public long save(PagosGastos entity) throws ExcepConstraint;

	public long save(PagosGastosExt object) throws ExcepConstraint;

	public void update(PagosGastos pojoEntity) throws ExcepConstraint;
	
	public void update(PagosGastosExt pojoEntity) throws ExcepConstraint;

	public PagosGastos findById(Long id);
	
	public PagosGastosExt findExtById(Long id) throws Exception;

	public List<PagosGastos> findAll() throws Exception;
	
	public List<PagosGastosExt> findExtAll() throws Exception;
	
	public List<PagosGastos> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<PagosGastosExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<PagosGastos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<PagosGastosExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<PagosGastos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	
	public List<PagosGastosExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<PagosGastos> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public List<PagosGastosExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<PagosGastos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<PagosGastosExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;

	public PagosGastos actualizar(PagosGastos entity, String estatus,Date fech_modificacion,Short usuario, Long empresa) throws Exception;
	
	public PagosGastosExt actualizar(PagosGastosExt entityExt, String estatus, Date fech_modificacion, Short usuario, Long empresa) throws Exception;
		
	public Respuesta salvar(PagosGastos entity, boolean band, Long empresa) throws Exception;
	
	public Respuesta salvar(PagosGastosExt entity, boolean band, Long empresa) throws Exception;
	
	public Respuesta salvaPojo(PagosGastos entity, boolean band) throws Exception;
	
	public Respuesta cancelacion(PagosGastos entity, Date fech_modificacion,Short usuario, Long empresa) throws Exception;
	
	public Respuesta cancelacion(PagosGastosExt entity, Date fech_modificacion,Short usuario, Long empresa) throws Exception;
	
	public Respuesta cancelacionPojo(PagosGastosExt entity, Date fech_modificacion,Short usuario) throws Exception;
	
	public Respuesta mensajeSaldoCtas(PagosGastos entity, Long empId) throws Exception;
	
	public Respuesta mensajeSaldoCtas(PagosGastosExt pojoEntityExt, Long empId) throws Exception;
	
	public boolean findPersonaEnUso(Persona persona);
	
	public boolean existeTransferencia(CtasBanco ctaDestino, String folioAutorizacion, Date fecha);
		
	public Empresa findEmpresa(PagosGastos movCta);
		
	public PagosGastos findAllById(Long id);

	public List<PagosGastos> findLikeBenefTipoPersona(String beneficiario, String tipoPer, String tipo, String estatus, int max, String sucursales);
	
	public List<PagosGastosExt> findLikeBenefTipoPersonaExt(String beneficiario, String tipoPer, String tipo, String estatus, int max, String sucursales);

	public List<PagosGastosExt> findLikeGtosComprobar(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales);
	
	public List<PagosGastos> findLikeMovCtas(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales);
	
	public List<PagosGastosExt> findExtLikeMovCtas(String propertyName,Object value,String v_tipo,String v_estatus1,int max,String sucursales);
	
	public List<PagosGastos> findTransferencias(String propertyName, Object value, String v_tipo, int max, String sucursales);
	
	public List<PagosGastosExt> findTransferenciasExt(String propertyName, Object value, String v_tipo, int max, String sucursales);
		
	public List<PagosGastos> findLikeCajaChica(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales);
	
	public List<PagosGastosExt> findLikeCajaChicaExt(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales);
	
	public List<PagosGastos> findProvisiones(String propertyName, Object value, String estatus, String estatus2, int max, String sucursales);
	
	public List<PagosGastos> findTransPorDia(Date fecha) throws Exception;
	
	public List<PagosGastos> findLikeGtosPorComprobarPersona(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max);
	
	public List<PagosGastosExt> findLikeGtosPorComprobarPersonaExt(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max);
	
	public List<PersonaExt> findLikePersonasConGastos(Object value, ConGrupoValores valGpo, String tipoPersona, String tipo, String estatus, int max, Date fecha, String sucursales);
	
	public SucursalExt findSucursalById(long id);
	
	public PersonaExt findPersonaById(long id);
	
	public PersonaExt findPersonaById(long id, String tipoPersona);
	
	public CtasBancoExt findCuentaBancariaById(long id);
	
	public List<PagosGastosExt> findLikeCuentaBancaria(String value, String v_tipo, String v_estatus1, int max);
	
	public List<PagosGastosExt> buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda);
	
	public int findConsecutivoByBeneficiario(long idBeneficiario, String tipo, String estatus);
	
	public Respuesta enviarTransaccion(Long entityId, Long idEmpresa) throws Exception;
	
	public Respuesta enviarTransaccion(PagosGastos entity, Long idEmpresa) throws Exception;
	
	public Respuesta enviarTransaccion(PagosGastosExt entityExt, Long idEmpresa) throws Exception;
	
	public PagosGastos convertir(PagosGastosExt entity) throws Exception;
	
	public PagosGastosExt convertir(PagosGastos entity) throws Exception;
}

/* 
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ---------------------------------------------------------------------------------------------------------------- 
 *  VER |   FECHA    |   	AUTOR 		| DESCRIPCIÓN 
 * ---------------------------------------------------------------------------------------------------------------- 
 *  2.2 | 2017-04-24 | Javier Tirado 	| Añado los metods convertir de pojo PagosGastos a extendido y viceversa
 */