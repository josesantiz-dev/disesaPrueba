package net.giro.cxp.logica;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import net.giro.cargas.documentos.logica.ComprobacionFacturaRem;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.NegociosRem;
import net.giro.clientes.logica.PersonaRem;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.logica.MensajeTransaccionRem;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.EjercicioHolder;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PagosGastosHolder;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.ProveedorHolder;
import net.giro.cxp.beans.SucursalExt;
import net.giro.cxp.beans.TiposGastoCXP;
import net.giro.cxp.dao.PagosGastosDAO;
import net.giro.cxp.logica.ConvertExt;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.logica.EmpresasRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.Meses;
import net.giro.plataforma.topics.TopicEventosCXP;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.logica.ChequesRem;
import net.giro.tyg.logica.CuentasBancariasRem;

@Stateless
public class PagosGastosFac implements PagosGastosRem {
	private static Logger log = Logger.getLogger(PagosGastosFac.class);
	private InfoSesion infoSesion;
	private PagosGastosDAO ifzPagosGastos;
	private PagosGastosDetRem ifzPagosGastosDet;
	private ComprobacionFacturaRem ifzFacturas;
	private SucursalesRem ifzSucursal;
	private PersonaRem ifzPersona;
	private NegociosRem ifzNegocio;
	private CuentasBancariasRem ifzCtasBanco;
	private EmpresasRem ifzEmpresas;
	private sendMessageRem ifzMessage;
	private ChequesRem ifzCheques;
	private MensajeTransaccionRem ifzMsgTransaccion;
	private ConvertExt convertidor;
	
	public PagosGastosFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzPagosGastos = (PagosGastosDAO) ctx.lookup("ejb:/Model_CuentasPorPagar//PagosGastosImp!net.giro.cxp.dao.PagosGastosDAO");
			this.ifzPagosGastosDet = (PagosGastosDetRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
			this.ifzSucursal = (SucursalesRem) ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzPersona = (PersonaRem) ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzNegocio = (NegociosRem) ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
			this.ifzCtasBanco = (CuentasBancariasRem) ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
			this.ifzEmpresas = (EmpresasRem) ctx.lookup("ejb:/Logica_Publico//EmpresasFac!net.giro.plataforma.logica.EmpresasRem");
			this.ifzMessage = (sendMessageRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//sendMessageFac!net.giro.cxp.logica.sendMessageRem");
			this.ifzCheques = (ChequesRem) ctx.lookup("ejb:/Logica_TYG//ChequesFac!net.giro.tyg.logica.ChequesRem");
			this.ifzMsgTransaccion = (MensajeTransaccionRem) ctx.lookup("ejb:/Logica_Contabilidad//MensajeTransaccionFac!net.giro.contabilidad.logica.MensajeTransaccionRem");
			this.ifzFacturas = (ComprobacionFacturaRem) ctx.lookup("ejb:/Logica_Cargas_Documentos//ComprobacionFacturaFac!net.giro.cargas.documentos.logica.ComprobacionFacturaRem");
    		
			this.ifzPagosGastosDet.setInfoSesion(this.infoSesion);
			this.ifzSucursal.setInfoSesion(this.infoSesion);
			this.ifzPersona.setInfoSesion(this.infoSesion);
			this.ifzNegocio.setInfoSesion(this.infoSesion);
			this.ifzCtasBanco.setInfoSesion(this.infoSesion);
			this.ifzEmpresas.setInfoSesion(this.infoSesion);
			this.ifzMsgTransaccion.setInfoSesion(this.infoSesion);
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			this.ifzCheques.setInfoSesion(this.infoSesion);

			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("PagosGastosFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear Logica_CuentasPorPagar.PagosGastosFac", e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value); 
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(PagosGastos object) throws Exception {
		try {
			return this.ifzPagosGastos.save(object, getCodigoEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<PagosGastos> saveOrUpdateList(List<PagosGastos> entities) throws Exception {
		try {
			return this.ifzPagosGastos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(PagosGastos pojoEntity) throws Exception {
		try {
			this.ifzPagosGastos.update(pojoEntity);
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public Respuesta salvar(PagosGastos entity) throws Exception {
		Respuesta reg = null;
		Long idEntity = 0L;
		String tipos = "";

		try {
			reg = new Respuesta();
			tipos = TiposGastoCXP.CajaChica.toString() + TiposGastoCXP.Gastos.toString();
			if (tipos.contains(entity.getTipo().trim()) && entity.getConsecutivo() <= 0 && entity.getIdBeneficiario() != null && entity.getIdBeneficiario() > 0L) 
				entity.setConsecutivo(this.findConsecutivoByBeneficiario(entity.getIdBeneficiario(), entity.getTipo().trim(), "G"));
			
			if (entity.getId() == null || entity.getId() <= 0L) {
				idEntity = this.save(entity);
				entity.setId(idEntity);
			} else {
				idEntity = entity.getId();
				this.update(entity);
			}

			reg.setResultado(0);
			reg.setObjeto(entity);
			reg.setReferencia(idEntity.toString());
			reg.getBody().addValor("entityId", idEntity);
			reg.getBody().addValor("entity", entity);
		} catch (Exception re) {
			log.error("error en metodo salvar", re);
			reg.setResultado(-1);
			reg.setRespuesta("Error al salvar");
			throw re;
		}

		return reg;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public PagosGastos findById(Long idPagosGastos) {
		try {
			return this.ifzPagosGastos.findById(idPagosGastos);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findById(idPagosGastos)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastos> findAll(String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner) throws Exception {
		try {
			return this.ifzPagosGastos.findAll(tipo, estatus, incluyeCancelados, orderBy, getIdEmpresa(), idOwner);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findAll(tipo, estatus, incluyeCancelados, orderBy)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastos> findLike(String value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception {
		try {
			if (value != null && ! "".equals(value) && value.contains(" "))
				value = value.replace(" ", "%");
			return this.ifzPagosGastos.findLike(value, tipo, estatus, incluyeCancelados, orderBy, getIdEmpresa(), idOwner, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findLike(value, tipo, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastos> findLikeProperty(String propertyName, Object value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.contains("*"))
				return this.findLike(value.toString(), tipo, estatus, incluyeCancelados, orderBy, idOwner, limite);
			if (value != null && value instanceof String && ! "".equals(value.toString().trim()) && value.toString().trim().contains(" "))
				value = value.toString().trim().replace(" ", "%");
			return this.ifzPagosGastos.findLikeProperty(propertyName, value, tipo, estatus, incluyeCancelados, orderBy, getIdEmpresa(), idOwner, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findLikeProperty(propertyName, value, tipo, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastos> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, "", "", false, "", 0L, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<PagosGastos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzPagosGastos.findByProperty(propertyName, value, "", "", "", getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<PagosGastos> findInProperty(String propertyName, List<Object> values, int limite) throws Exception {
		try {
			return this.ifzPagosGastos.findInProperty(propertyName, values, "", "", getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findInProperty(propertyName, values, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<PagosGastos> busquedaDinamica(String value) throws Exception {
		try {
			return this.findLike(value, "", "", false, "", 0L, 0);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.busquedaDinamica(value)", e);
			throw e;
		}
	}

	@Override
	public Respuesta nuevoNegocioProveedor(String uniqueValue, String nombre, String rfc, String tipoPersona) {
		Respuesta respuesta = null;
		// ---------------------------------
		String tipoPersonalidad = "";
		Negocio negocio = null;
		Persona persona = null;
		
		try {
			if (tipoPersona == null || "".equals(tipoPersona.trim()) || ! "NP".contains(tipoPersona.trim()))
				tipoPersona = validaTipoPersona(rfc);
			tipoPersonalidad = ("N".equals(tipoPersona)) ? "M" : "F";
			
			negocio = new Negocio();
			negocio.setId(0L);
			negocio.setRfc(rfc);
			negocio.setNombre(nombre);
			negocio.setTipoPersonalidad(tipoPersonalidad);
			negocio.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			negocio.setFechaCreacion(Calendar.getInstance().getTime());
			negocio.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			negocio.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.ifzNegocio.setInfoSesion(this.infoSesion);
			respuesta = this.ifzNegocio.salvarNegocio(negocio);
			if (respuesta.getErrores().getCodigoError() != 0L && respuesta.getErrores().getCodigoError() != 115L) {
				log.error("Ocurrio un problema al guardar el Negocio indicado: " + rfc);
				mensajeNuevoProveedor(uniqueValue, nombre, rfc, tipoPersonalidad);
				return respuesta;
			}

			respuesta.getErrores().setCodigoError(0L);
			negocio = (Negocio) respuesta.getBody().getValor("pojoNegocio");
			
			persona = new Persona();
			persona.setId(negocio.getId());
			persona.setNombre(negocio.getNombre());
			persona.setRfc(negocio.getRfc());
			persona.setTipoPersona(2L);

			respuesta.getBody().addValor("negocio", negocio);
			respuesta.getBody().addValor("persona", persona);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/CXP:CXP_TRANSACCION\n\n\n\n", e);
			mensajeNuevoProveedor(uniqueValue, nombre, rfc, tipoPersonalidad);
		}
		
		return respuesta;
	}
	
	@Override
	public void contabilizador(Long idPagosGastos) throws Exception {
		MensajeTopic msgTopic = null;
		String comando = "";
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (idPagosGastos == null || idPagosGastos <= 0L)
				return;
			
			target = idPagosGastos.toString();
			msgTopic = new MensajeTopic(TopicEventosCXP.TRANSACCION, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/CXP:CXP_TRANSACCION\n\n" + comando + "\n\n", e);
		}
	}
	
	// --------------------------------------------------------------------------------------------------------------
	
	@Override
	public List<PagosGastos> findCajaChicaLikeProperty(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, TiposGastoCXP.CajaChica.toString(), estatus, incluyeCancelados, orderBy, idOwner, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findCajaChicaLikeProperty(propertyName, value, tipo, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastos> findProvisionLikeProperty(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, TiposGastoCXP.Provision.toString(), estatus, incluyeCancelados, orderBy, 0L, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findProvisionLikeProperty(propertyName, value, tipo, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastos> findRegistroGastosLikeProperty(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, TiposGastoCXP.RegistroGasto.toString(), estatus, incluyeCancelados, orderBy, 0L, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findRegistroGastosLikeProperty(propertyName, value, tipo, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastos> findMovimientoCuentasLikeProperty(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, TiposGastoCXP.MovimientoCuentas.toString(), estatus, incluyeCancelados, orderBy, 0L, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findMovimientoCuentasLikeProperty(propertyName, value, tipo, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastos> findGastoComprobarLikeProperty(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, TiposGastoCXP.Gastos.toString(), estatus, incluyeCancelados, orderBy, 0L, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findGastoComprobarLikeProperty(propertyName, value, tipo, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}
	}
	
	// --------------------------------------------------------------------------------------------------------------
	
	@Override
	public Respuesta mensajeSaldoCtas(PagosGastos pojoEntity) throws Exception {
		Respuesta reg = new Respuesta();
		PagosGastosExt entity = null;

		try {
			entity = this.convertidor.PagosGastosToPagosGastosExt(pojoEntity);

			// envio mensaje para la contabilidad
			if (! "T".equals(entity.getTipo()))
				this.ifzMessage.enviar(pojoEntity, getIdEmpresa());

			reg.setObjeto(entity);
			reg.setResultado(0);
			reg.setReferencia(String.valueOf(entity.getId()));
			return reg;
		} catch (Exception re) {
			log.error("error en metodo mensajeSaldoCtas", re);
			throw re;
		}
	}

	@Override
	public Empresa findEmpresa(PagosGastos movCta) {
		CuentaBancaria cuentaOrigen = null;
		
		try {
			this.ifzCtasBanco.setInfoSesion(this.infoSesion);
			cuentaOrigen = this.ifzCtasBanco.findById(movCta.getIdCuentaOrigen().shortValue());
			return this.ifzEmpresas.findById(cuentaOrigen.getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastos> findTransferencias(String propertyName, Object value, String v_tipo, int max, String sucursales) {
		try {
			return this.ifzPagosGastos.findTransferencias(propertyName, value, v_tipo, getIdEmpresa(), max, sucursales);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastos> findLikeGtosPorComprobarPersona(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max) {
		try {
			return this.ifzPagosGastos.findLikeGtosPorComprobarPersona(value, suc, tipo, estatus, tipoPersona, fecha, traer, getIdEmpresa(), max);
		} catch (Exception re) {
			throw re;
		}
	}

	//@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private int findConsecutivoByBeneficiario(long idBeneficiario, String tipo, String estatus) {
		try {
			return this.ifzPagosGastos.findConsecutivoByBeneficiario(idBeneficiario, tipo, estatus, getIdEmpresa());
		} catch (Exception e) {
			throw e;
		}
	}

	/*@Override
	public int generarNumCajaChicaByBeneficiario(long idBeneficiario, String estatus) {
		try {
			return this.findConsecutivoByBeneficiario(idBeneficiario, TiposGastoCXP.CajaChica.toString(), estatus);
		} catch (Exception e) {
			throw e;
		}
	}*/

	@Override
	public Respuesta enviarTransaccion(PagosGastos entity) throws Exception {
		Respuesta respuesta = new Respuesta();
		LinkedHashMap<Long, Double> listTransacciones = null;
		List<PagosGastosDetExt> listConceptos = null;
		List<MensajeTransaccion> listMensajes = null;
		MensajeTransaccion msg = null;
		double importe = 0;
		String descOperacion = "";
		Long idMensaje = 0L;
		Long idTransaccion = 0L;
		Long idTransaccionInicial = 0L;
		Long idFormaPago = 0L;
		String descFormaPago = "";
		String referencia = "";
		String tracking = "";
		Sucursal sucursal = null;
		String descSucursal = "";
		CuentaBancaria cuenta = null;
		Long idMoneda = 0L;
		String descMoneda = "";
		
		try {
			if (entity == null) {
				log.info("No se disparo ninguna Transaccion, recibi Entity nulo");
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No puedo determinar una Transaccion valida. El registro indicado esta nulo o vacio");
				return respuesta;
			}
			
			// Comprobamos registro completo si corresponde
			if (! "F".equals(entity.getTipo())) {
				if (entity.getOperacion() == null || "".equals(entity.getOperacion().trim())) {
					log.info("NO tiene asignada la Operacion");
					respuesta.getErrores().setCodigoError(1L);
					respuesta.getErrores().setDescError("No puedo determinar una Transaccion valida. El registro indicado esta nulo o vacio");
					return respuesta;
				}
	
				if (entity.getIdCuentaOrigen() == null || entity.getIdCuentaOrigen() <= 0L) {
					log.info("NO tiene asignada la Cuenta Bancaria de Origen");
					respuesta.getErrores().setCodigoError(1L);
					respuesta.getErrores().setDescError("No puedo determinar una Transaccion valida. El registro indicado esta nulo o vacio");
					return respuesta;
				}
				
				// Obtenemos la cuenta asignada
				this.ifzCtasBanco.setInfoSesion(this.infoSesion);
				cuenta = this.ifzCtasBanco.findById(entity.getIdCuentaOrigen());
				if (cuenta != null) {
					// Obtenemos la moneda de la cuenta
					idMoneda = cuenta.getMoneda().getId();
					descMoneda = cuenta.getMoneda().getNombre();
				}
			}
			
			// Obtenemos la sucursal
			sucursal = this.ifzSucursal.findById(entity.getIdSucursal());
			if (sucursal != null && sucursal.getId() > 0L)
				descSucursal = sucursal.getSucursal();
			
			// Validamos moneda
			if (idMoneda == null || idMoneda <= 0L) {
				idMoneda = 10000001L;
				descMoneda = "PESOS";
			}

			// Forma de Pago y Referencia
			referencia = ("F".equals(entity.getTipo()) ? "X" : entity.getOperacion());
			if ("E".equals(referencia)) {
				idFormaPago = 10000031L; // EFECTIVO 
				descFormaPago = "EFECTIVO";
				referencia = "";
			} else if ("C".equals(referencia)) {
				idFormaPago = 10000032L; // CHEQUE 
				descFormaPago = "CHEQUE";
				referencia = (entity.getNoCheque() != null && entity.getNoCheque() > 0) ? entity.getNoCheque().toString() : "";
			} else if ("T".equals(referencia)) {
				idFormaPago = 10000033L; // TRANSFERENCIA 
				descFormaPago = "TRANSFERENCIA";
				referencia = (entity.getFolioAutorizacion() != null && ! "".equals(entity.getFolioAutorizacion())) ? entity.getFolioAutorizacion() : "";
			} else if ("D".equals(referencia)) {
				idFormaPago = 10000035L; // DEPOSITO 
				descFormaPago = "DEPOSITO";
				referencia = (entity.getNoCheque() != null && entity.getNoCheque() > 0) ? entity.getNoCheque().toString() : "";
			} else if ("X".equals(referencia)) {
				idFormaPago = 10000038L; // POR DEFINIR :: Propio para PROVISION
				descFormaPago = "POR DEFINIR";
				referencia = "";
			} else {
				log.info("No se disparo ninguna Transaccion. No se pudo determinar la Forma de Pago. Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus());
				respuesta.getErrores().setCodigoError(5L);
				respuesta.getErrores().setDescError("No se pudo determinar la Forma de Pago.");
				return respuesta;
			}
			
			// Determinamos transacciones
			log.info("Determinando lote de Transacciones. Recuperando conceptos ... ");
			this.ifzPagosGastosDet.setInfoSesion(this.infoSesion);
			listConceptos = this.ifzPagosGastosDet.findExtAll(entity.getId());
			if (listConceptos == null || listConceptos.isEmpty()) {
				log.info("No puedo determinar una Transaccion valida porque el registro indicado no tiene detalles. Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus());
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No puedo determinar una Transaccion. El registro indicado no tiene conceptos");
				return respuesta;
			}
			
			listTransacciones = new LinkedHashMap<Long, Double>();
			for (PagosGastosDetExt comprobado : listConceptos) {
				importe = comprobado.getSubtotal() + (comprobado.getTotalImpuestos() - comprobado.getTotalRetenciones());
				if (idTransaccionInicial <= 0L)
					idTransaccionInicial = idTransaccion;
				
				// REGISTRO DE GASTOS
				if ("P".equals(entity.getTipo()) && "C".equals(entity.getEstatus())) {
					// COMPRA DE MATERIAL - CONTADO --- 10004040L:COMPRAS
					if (10004040L == comprobado.getIdConcepto().getId()) {
						tracking += "\nTransaccion 1010 (Registro de Gastos: COMPRA DE MATERIAL - CONTADO). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1010L))
							listTransacciones.put(1010L, importe);
						else
							listTransacciones.put(1010L, listTransacciones.get(1010L) + importe);
						idTransaccion = 1010L;
						descOperacion = "Registro Egresos: COMPRA DE MATERIAL - CONTADO";
						
					// COMPRA DE MATERIAL - CREDITO --- 10004515:PROVEEDORES, 10004510:ACREEDORES DIVERSOS
					} else if (10004515L == comprobado.getIdConcepto().getId() || 10004510L == comprobado.getIdConcepto().getId()) {
						tracking += "\nTransaccion 1012 (Registro de Gastos: COMPRA DE MATERIAL - CREDITO). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1012L))
							listTransacciones.put(1012L, importe);
						else
							listTransacciones.put(1012L, listTransacciones.get(1012L) + importe);
						idTransaccion = 1012L;
						descOperacion = "Registro Egresos: COMPRA DE MATERIAL - CREDITO";
						
					// PAGO DE NOMINA --- 10003977:IMPUESTO SOBRE NOMINA 2.5%
					/*} else if (10003977L == comprobado.getIdConcepto().getId()) {
						tracking += "\nTransaccion 1018 (Registro de Gastos: PAGO DE NOMINA). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1018L))
							listTransacciones.put(1018L, importe);
						else
							listTransacciones.put(1018L, listTransacciones.get(1018L) + importe);
						idTransaccion = 1018L;
						descOperacion = "Registro Egresos: PAGO DE NOMINA";*/
						
					// CONSUMO DE COMBUSTIBLE
					} else if (10003921L == comprobado.getIdConcepto().getId()) { 
						tracking += "\nTransaccion 1019 (Registro de Gastos: CONSUMO DE COMBUSTIBLE). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1019L))
							listTransacciones.put(1019L, importe);
						else
							listTransacciones.put(1019L, listTransacciones.get(1019L) + importe);
						idTransaccion = 1019L;
						descOperacion = "Registro Egresos: CONSUMO DE COMBUSTIBLE";
						
					// CANCELACION DE CHEQUES --- 
					/*} else if (0L == comprobado.getIdConcepto().getId()) {
						tracking += "\nTransaccion 1020 (Registro de Gastos: CANCELACION DE CHEQUES). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1020L))
							listTransacciones.put(1020L, importe);
						else
							listTransacciones.put(1020L, listTransacciones.get(1020L) + importe);
						idTransaccion = 1020L;
						descOperacion = "Registro Egresos: CANCELACION DE CHEQUES";*/
					
					// COMISIONES DEL BANCO
					} else if (10003924L == comprobado.getIdConcepto().getId()) { 
						tracking += "\nTransaccion 1022 (Registro de Gastos: COMISIONES DEL BANCO). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1022L))
							listTransacciones.put(1022L, importe);
						else
							listTransacciones.put(1022L, listTransacciones.get(1022L) + importe);
						idTransaccion = 1022L;
						descOperacion = "Registro Egresos: COMISIONES DEL BANCO";
						
					// PAGO MENSUALIDAD PRESTAMO PYME
					} else if ("10004469 10003982".contains(String.valueOf(comprobado.getIdConcepto().getId()))) { 
						tracking += "\nTransaccion 1024 (Registro de Gastos: PAGO MENSUALIDAD PRESTAMO PYME). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1024L))
							listTransacciones.put(1024L, importe);
						else
							listTransacciones.put(1024L, listTransacciones.get(1024L) + importe);
						idTransaccion = 1024L;
						descOperacion = "Registro Egresos: PAGO MENSUALIDAD PRESTAMO PYME";
						
					// PAGO DE IMPUESTOS FEDERALES
					} else if ("10003977 10003987 10004005 10004438 10004059 10004440 10004441 10004443".contains(String.valueOf(comprobado.getIdConcepto().getId()))) { 
						tracking += "\nTransaccion 1025 (Registro de Gastos: PAGO DE IMPUESTOS FEDERALES). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1025L))
							listTransacciones.put(1025L, importe);
						else
							listTransacciones.put(1025L, listTransacciones.get(1025L) + importe);
						idTransaccion = 1025L;
						descOperacion = "Registro Egresos: PAGO DE IMPUESTOS FEDERALES";
					
					// HONORARIOS
					} else if (10003973L == comprobado.getIdConcepto().getId()) { 
						tracking += "\nTransaccion 1026 (Registro de Gastos: HONORARIOS). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1026L))
							listTransacciones.put(1026L, importe);
						else
							listTransacciones.put(1026L, listTransacciones.get(1026L) + importe);
						idTransaccion = 1026L;
						descOperacion = "Registro Egresos: HONORARIOS";
						
					// FLETES
					} else if ("10004043 10004044 10004045".contains(String.valueOf(comprobado.getIdConcepto().getId()))) { 
						tracking += "\nTransaccion 1027 (Registro de Gastos: FLETES). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1027L))
							listTransacciones.put(1027L, importe);
						else
							listTransacciones.put(1027L, listTransacciones.get(1027L) + importe);
						idTransaccion = 1027L;
						descOperacion = "Registro Egresos: FLETES";
					
					// ARRENDAMIENTO
					} else if (10003915L == comprobado.getIdConcepto().getId()) { 
						tracking += "\nTransaccion 1028 (Registro de Gastos: ARRENDAMIENTO). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1028L))
							listTransacciones.put(1028L, importe);
						else
							listTransacciones.put(1028L, listTransacciones.get(1028L) + importe);
						idTransaccion = 1028L;
						descOperacion = "Registro Egresos: ARRENDAMIENTO";
					
					// CUOTAS IMSS* E INFONAVIT --- 10003978:IMSS
					} else if ("10003978 10003980".contains(String.valueOf(comprobado.getIdConcepto().getId()))) { 
						tracking += "\nTransaccion 1029 (Registro de Gastos: CUOTAS IMSS E INFONAVIT*). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1029L))
							listTransacciones.put(1029L, importe);
						else
							listTransacciones.put(1029L, listTransacciones.get(1029L) + importe);
						idTransaccion = 1029L;
						descOperacion = "Registro Egresos: CUOTAS IMSS E INFONAVIT*";
					
					// ACTIVO FIJO
					} else if (comprobado.getIdConcepto().getAtributo1() != null && "10000791".equals(comprobado.getIdConcepto().getAtributo1().trim())) { 
						tracking += "\nTransaccion 1030 (Registro de Gastos: ACTIVO FIJO). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
						if (! listTransacciones.containsKey(1030L))
							listTransacciones.put(1030L, importe);
						else
							listTransacciones.put(1030L, listTransacciones.get(1030L) + importe);
						idTransaccion = 1030L;
						descOperacion = "Registro Egresos: ACTIVO FIJO";
					} 
				
				// PROVISION PROVEEDORES
				} else if ("F".equals(entity.getTipo()) && "CG".contains(entity.getEstatus())) {
					tracking += "\nTransaccion 1011 (Provision Proveedores). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
					if (! listTransacciones.containsKey(1011L))
						listTransacciones.put(1011L, importe);
					else
						listTransacciones.put(1011L, listTransacciones.get(1011L) + importe);
					idTransaccion = 1011L;
					descOperacion = "PROVISION PROVEEDORES";
				
				// CAJA CHICA
				} else if ("C".equals(entity.getTipo()) && "G".equals(entity.getEstatus())) {
					tracking += "\nTransaccion 1016 (Caja Chica). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
					if (! listTransacciones.containsKey(1016L))
						listTransacciones.put(1016L, importe);
					else
						listTransacciones.put(1016L, listTransacciones.get(1016L) + importe);
					idTransaccion = 1016L;
					descOperacion = "CAJA CHICA";
					
				// TRASPASO ENTRE CUENTAS BANCARIAS
				} else if ("M".equals(entity.getTipo()) && "G".equals(entity.getEstatus())) {
					tracking += "\nTransaccion 1017 (TRASPASO ENTRE CUENTAS BANCARIAS). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
					if (! listTransacciones.containsKey(1017L))
						listTransacciones.put(1017L, importe);
					else
						listTransacciones.put(1017L, listTransacciones.get(1017L) + importe);
					idTransaccion = 1017L;
					descOperacion = "TRASPASO ENTRE CUENTAS BANCARIAS";
				
				// GASTOS A COMPROBAR: ANTICIPO A PROVEEDORES
				} else if ("G".equals(entity.getTipo()) && "G".equals(entity.getEstatus())) {
					tracking += "\nTransaccion 1034 (Gastos a Comprobar: Anticipo a Proveedores). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus() + ". Concepto: " + comprobado.getId();
					if (! listTransacciones.containsKey(1034L))
						listTransacciones.put(1034L, importe);
					else
						listTransacciones.put(1034L, listTransacciones.get(1034L) + importe);
					idTransaccion = 1034L;
					descOperacion = "GASTOS A COMPROBAR: ANTICIPO A PROVEEDORES";
				
				// DESCONOCIDO
				} else {
					log.info("No se disparo ninguna Transaccion, Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus());
					respuesta.getErrores().setCodigoError(3L);
					respuesta.getErrores().setDescError("No puedo determinar una Transaccion valida. Tipo de registro no identificado");
					return respuesta;
				}
				
				if (idTransaccionInicial > 0L && idTransaccionInicial.longValue() != idTransaccion.longValue()) {
					log.info("No se disparo ninguna Transaccion. Multiples Transacciones posibles para el registro indicado. Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus());
					respuesta.getErrores().setCodigoError(4L);
					respuesta.getErrores().setDescError("Multiples Transacciones posibles para el registro indicado.");
					return respuesta;
				}
			}
			log.info("Transacciones encontradas: " + tracking);
			
			// Generamos el mensajes necesarios
			for (Entry<Long, Double> item : listTransacciones.entrySet()) {
				idTransaccion = item.getKey();
				importe = item.getValue();
				// --------------------------------------------------------
				msg = new MensajeTransaccion();
				msg.setIdTransaccion(idTransaccion);
				msg.setIdOperacion(entity.getId());
				msg.setDescripcionOperacion(descOperacion);
				msg.setIdMoneda(idMoneda);
				msg.setDescripcionMoneda(descMoneda);
				msg.setImporte(new BigDecimal(importe));
				msg.setIdPersonaReferencia(entity.getIdBeneficiario());
				msg.setNombrePersonaReferencia(entity.getBeneficiario());
				msg.setTipoPersona(entity.getTipoBeneficiario());
				msg.setReferencia("");
				msg.setIdFormaPago(idFormaPago);
				msg.setDescripcionFormaPago(descFormaPago);
				msg.setReferenciaFormaPago(referencia);
				msg.setIdSucursal(entity.getIdSucursal());
				msg.setDescripcionSucursal(descSucursal);
				msg.setIdEmpresa(getIdEmpresa());
				msg.setDescripcionEmpresa(getEmpresa());
				msg.setIdUsuarioCreacionRegistro(entity.getCreadoPor());
				msg.setFechaRegistro(entity.getFecha());
				msg.setCreadoPor(entity.getCreadoPor());
				msg.setFechaCreacion(entity.getFechaCreacion());
				msg.setAnuladoPor(0L);
				msg.setFechaAnulacion(null);
				msg.setEstatus(0);
				// --------------------------------------------------------
				if (listTransacciones.size() > 1) {
					if (listMensajes == null)
						listMensajes = new ArrayList<MensajeTransaccion>();
					listMensajes.add(msg);
					continue;
				}
				// --------------------------------------------------------
				// Registramos el mensaje
				this.ifzMsgTransaccion.setInfoSesion(this.infoSesion);
				idMensaje = this.ifzMsgTransaccion.save(msg);
				log.info("Mensaje " + idMensaje + " generado para la Transaccion " + idTransaccion);
			}
			
			if (listMensajes != null && listMensajes.isEmpty()) {
				this.ifzMsgTransaccion.setInfoSesion(this.infoSesion);
				listMensajes = this.ifzMsgTransaccion.saveOrUpdateList(listMensajes);
				idMensaje = listMensajes.get(0).getId();
				log.info("Multiples Mensaje generados (x" + listMensajes.size() + ", ej. " + idMensaje + ") para la Transaccion " + idTransaccion);
			}
		} catch (Exception e) {
			log.error("Error Logica_CuentasPorPagar.PagosGastosFac.enviarTransaccion(PagosGastosEntity, idEmpresa)", e);
			respuesta.getBody().addValor("exception", e);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("No se pudo determinar la Forma de Pago.");
		} 
		
		return respuesta;
	}
	
	@Override
	public PagosGastos cancelar(PagosGastos entity) throws Exception {
		List<PagosGastosDet> detalles = null;
		
		try {
			if (this.infoSesion != null && this.infoSesion.getAcceso() != null && this.infoSesion.getAcceso().getUsuario() != null && this.infoSesion.getAcceso().getUsuario().getId() > 0L)
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			else
				entity.setModificadoPor(entity.getModificadoPor() * -1);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setEstatus("X");
			this.ifzPagosGastos.update(entity);

			this.ifzPagosGastosDet.setInfoSesion(this.infoSesion);
			detalles = this.ifzPagosGastosDet.findAll(entity.getId());
			if (detalles != null && ! detalles.isEmpty()) {
				for (PagosGastosDet det : detalles) {
					if (det.getIdCfdi() != null && det.getIdCfdi() > 0L) {
						this.ifzFacturas.setInfoSesion(this.infoSesion); //this.ifzFacturas.setInfoSesion(convertInfoSesion());
						this.ifzFacturas.cancelar(det.getIdCfdi());
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		
		return entity;
	}

	@Override
	public List<PagosGastosHolder> encapsularPagosGastos(List<PagosGastos> listPagosGastos) throws Exception {
		List<PagosGastosHolder> resultados = new ArrayList<PagosGastosHolder>();
		Calendar cal = Calendar.getInstance();
		//----------------------------------------------------
		HashMap<String, String> mapProveedores = new HashMap<String, String>();
		HashMap<String, Double> auxProvision = new HashMap<String, Double>();
		HashMap<Meses, HashMap<String, Double>> mapProvision = new HashMap<Meses, HashMap<String, Double>>();
		Meses mes = null;
		double montoProvision = 0;
		//----------------------------------------------------
		HashMap<Meses, HashMap<String, List<PagosGastos>>> mapProvProveedor = new HashMap<Meses, HashMap<String, List<PagosGastos>>>();
		HashMap<String, List<PagosGastos>> auxMap = new HashMap<String, List<PagosGastos>>();
		String keyProveedor = "";
		int vuelta = 0;
		
		try {
			for (PagosGastos var : listPagosGastos) {
				vuelta++;
				keyProveedor = var.getIdBeneficiario() + var.getTipoBeneficiario();
				cal.setTime(var.getFecha());
				mes = Meses.fromOrdinal(cal.get(Calendar.MONTH));
				montoProvision = var.getMonto();
				
				auxProvision = new HashMap<String, Double>();
				auxMap = new HashMap<String, List<PagosGastos>>();
				mapProveedores.put(keyProveedor, var.getBeneficiario());
				if (mapProvision.containsKey(mes)) {
					auxProvision = mapProvision.get(mes);
					auxMap = mapProvProveedor.get(mes);
					if (auxProvision.containsKey(var.getIdBeneficiario().toString())) 
						montoProvision += auxProvision.get(var.getIdBeneficiario().toString());
				}
				
				auxProvision.put(keyProveedor, montoProvision);
				mapProvision.put(mes, auxProvision);
				if (! auxMap.containsKey(keyProveedor))
					auxMap.put(keyProveedor, new ArrayList<PagosGastos>());
				auxMap.get(keyProveedor).add(var);
				mapProvProveedor.put(mes, auxMap);
			}
			
			if (mapProvision.isEmpty())
				return resultados;
			
			for (Entry<Meses, HashMap<String, Double>> map : mapProvision.entrySet()) {
				for (Entry<String, Double> item : map.getValue().entrySet()) 
				    resultados.add(new PagosGastosHolder(map.getKey(), mapProveedores.get(item.getKey()), item.getValue(), mapProvProveedor.get(map.getKey()).get(item.getKey())));
			}
			
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.encapsularPagosGastos(listPagosGastos) :: " + vuelta, e);
			throw e;
		}
		
		return resultados;
	}

	@Override
	public List<EjercicioHolder> encapsularProvisiones(List<PagosGastos> provisiones) throws Exception {
		List<EjercicioHolder> resultados = new ArrayList<EjercicioHolder>();
		List<ProveedorHolder> proveedores = null;
		// ----------------------------------------------------------------------------------------------
		SimpleDateFormat formatter = null;
		LinkedHashMap<String, List<ProveedorHolder>> mapHolder = null;
		LinkedHashMap<String, List<PagosGastos>> mapEjercicioPeriodo = null;
		LinkedHashMap<String, List<PagosGastos>> mapProveedor = null;
		List<PagosGastos> listAuxiliar = null;
		String key = "";
		
		try {
			if (provisiones == null || provisiones.isEmpty())
				return resultados;
			
			// Ordenamos por fecha descendente
			Collections.sort(provisiones, new Comparator<PagosGastos>() {
				@Override
				public int compare(PagosGastos o1, PagosGastos o2) {
					return o2.getFecha().compareTo(o1.getFecha());
				}
			});
			
			// Separacion año/mes
			formatter = new SimpleDateFormat("yyyyMM");
			mapEjercicioPeriodo = new LinkedHashMap<String, List<PagosGastos>>();
			for (PagosGastos provision : provisiones) {
				listAuxiliar = new ArrayList<PagosGastos>();
				key = formatter.format(provision.getFecha());
				if (mapEjercicioPeriodo.containsKey(key))
					listAuxiliar = mapEjercicioPeriodo.get(key);
				listAuxiliar.add(provision);
				mapEjercicioPeriodo.put(key, listAuxiliar);
			}
			
			// Agrupacion por proveedor del Ejercicio/Periodo
			mapProveedor = new LinkedHashMap<String, List<PagosGastos>>();
			for (Entry<String, List<PagosGastos>> item : mapEjercicioPeriodo.entrySet()) {
				provisiones = item.getValue();
				for (PagosGastos provision : provisiones) {
					listAuxiliar = new ArrayList<PagosGastos>();
					key = item.getKey() + "|" + provision.getIdBeneficiario() + provision.getTipoBeneficiario();
					if (mapProveedor.containsKey(key))
						listAuxiliar = mapProveedor.get(key);
					listAuxiliar.add(provision);
					mapProveedor.put(key, listAuxiliar);
				}
			}

			mapHolder = new LinkedHashMap<String, List<ProveedorHolder>>();
			for (Entry<String, List<PagosGastos>> item : mapProveedor.entrySet()) {
				key = getKeyEjercicio(item.getKey());
				provisiones = item.getValue();
				proveedores = new ArrayList<ProveedorHolder>();
				if (mapHolder.containsKey(key)) 
					proveedores = mapHolder.get(key);
				proveedores.add(new ProveedorHolder(provisiones.get(0).getIdBeneficiario(), provisiones.get(0).getBeneficiario(), provisiones));
				mapHolder.put(key, proveedores);
			}

			for (Entry<String, List<ProveedorHolder>> item : mapHolder.entrySet()) 
				resultados.add(new EjercicioHolder(item.getValue()));
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.PagosGastosFac.encapsularProvisiones(provisiones)", e);
			throw e;
		} 
		
		return resultados;
	}

	// ------------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// ------------------------------------------------------------------------------------------------

	@Override
	public PagosGastos convertir(PagosGastosExt entity) throws Exception {
		return this.convertidor.PagosGastosExtToPagosGastos(entity);
	}

	@Override
	public PagosGastosExt convertir(PagosGastos entity) throws Exception {
		return this.convertidor.PagosGastosToPagosGastosExt(entity);
	}
	
	// -------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------
	
	@Override
	public PagosGastosExt actualizar(PagosGastosExt extendido, String estatus, Date fech_modificacion) throws Exception {
		try {
			extendido.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			extendido.setFechaModificacion(fech_modificacion);
			extendido.setEstatus(estatus);

			this.update(this.convertidor.PagosGastosExtToPagosGastos(extendido));
			return extendido;
		} catch (Exception re) {
			log.error("error metodo actualizar", re);
			throw re;
		}
	}

	@Override
	public Respuesta salvar(PagosGastosExt extendido) throws Exception {
		Respuesta reg = new Respuesta();
		PagosGastos entity = null;

		try { 
			entity = this.convertidor.PagosGastosExtToPagosGastos(extendido);
			entity.setBeneficiario(extendido.getBeneficiario());

			reg = this.salvar(entity);
			if (reg.getResultado() != 0)
				return reg;
			
			entity = (PagosGastos) reg.getObjeto();
			extendido.setId(entity.getId());
			extendido.setConsecutivo(entity.getConsecutivo());
			
			reg.setResultado(0);
			reg.setObjeto(extendido);
			reg.getBody().addValor("entity", entity);
			reg.getBody().addValor("extendido", extendido);
		} catch (Exception re) {
			log.error("error en metodo salvar", re);
			reg.setResultado(-1);
			reg.setRespuesta("Error al salvar");
			throw re;
		}

		return reg;
	}

	@Override
	public void update(PagosGastosExt extendido) throws Exception {
		try {
			this.update(this.convertidor.PagosGastosExtToPagosGastos(extendido));
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public PagosGastosExt findExtById(Long idPagosGastos) throws Exception {
		try {
			return this.convertidor.PagosGastosToPagosGastosExt(this.findById(idPagosGastos));
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findExtById(idPagosGastos)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastosExt> findAllExt(String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner) throws Exception {
		List<PagosGastosExt> extendidos = new ArrayList<PagosGastosExt>();
		List<PagosGastos> entities = null;

		try {
			entities = this.findAll(tipo, estatus, incluyeCancelados, orderBy, idOwner);
			if (entities != null && ! entities.isEmpty()) {
				for (PagosGastos entity : entities) 
					extendidos.add(this.convertidor.PagosGastosToPagosGastosExt(entity));
			}
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findAllExt(tipo, estatus, incluyeCancelados, orderBy)", e);
			throw e;
		}

		return extendidos;
	}

	@Override
	public List<PagosGastosExt> findLikeExt(String value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception {
		List<PagosGastosExt> extendidos = new ArrayList<PagosGastosExt>();
		List<PagosGastos> entities = null;

		try {
			entities = this.findLike(value, tipo, estatus, incluyeCancelados, orderBy, idOwner, limite);
			if (entities != null && ! entities.isEmpty()) {
				for (PagosGastos entity : entities) 
					extendidos.add(this.convertidor.PagosGastosToPagosGastosExt(entity));
			}
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findLikeExt(value, tipo, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}

		return extendidos;
	}

	@Override
	public List<PagosGastosExt> findLikePropertyExt(String propertyName, Object value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception {
		List<PagosGastosExt> extendidos = new ArrayList<PagosGastosExt>();
		List<PagosGastos> entities = null;

		try {
			entities = this.findLikeProperty(propertyName, value, tipo, estatus, incluyeCancelados, orderBy, idOwner, limite);
			if (entities != null && ! entities.isEmpty()) {
				for (PagosGastos entity : entities) 
					extendidos.add(this.convertidor.PagosGastosToPagosGastosExt(entity));
			}
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findLikePropertyExt(propertyName, value, tipo, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}

		return extendidos;
	}
	
	// --------------------------------------------------------------------------------------------------------------
	
	@Override
	public List<PagosGastosExt> findCajaChicaLikePropertyExt(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, long idOwner, int limite) throws Exception {
		List<PagosGastosExt> extendidos = new ArrayList<PagosGastosExt>();
		List<PagosGastos> entities = null;

		try {
			entities = this.findCajaChicaLikeProperty(propertyName, value, estatus, incluyeCancelados, orderBy, idOwner, limite);
			if (entities != null && ! entities.isEmpty()) {
				for (PagosGastos entity : entities) 
					extendidos.add(this.convertidor.PagosGastosToPagosGastosExt(entity));
			}
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findCajaChicaLikePropertyExt(propertyName, value, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}

		return extendidos;
	}

	@Override
	public List<PagosGastosExt> findProvisionLikePropertyExt(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		List<PagosGastosExt> extendidos = new ArrayList<PagosGastosExt>();
		List<PagosGastos> entities = null;

		try {
			entities = this.findProvisionLikeProperty(propertyName, value, estatus, incluyeCancelados, orderBy, limite);
			if (entities != null && ! entities.isEmpty()) {
				for (PagosGastos entity : entities) 
					extendidos.add(this.convertidor.PagosGastosToPagosGastosExt(entity));
			}
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findProvisionLikePropertyExt(propertyName, value, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}

		return extendidos;
	}

	@Override
	public List<PagosGastosExt> findMovimientoCuentasLikePropertyExt(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		List<PagosGastosExt> extendidos = new ArrayList<PagosGastosExt>();
		List<PagosGastos> entities = null;

		try {
			entities = this.findRegistroGastosLikeProperty(propertyName, value, estatus, incluyeCancelados, orderBy, limite);
			if (entities != null && ! entities.isEmpty()) {
				for (PagosGastos entity : entities) 
					extendidos.add(this.convertidor.PagosGastosToPagosGastosExt(entity));
			}
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findRegistroGastosLikePropertyExt(propertyName, value, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}

		return extendidos;
	}

	@Override
	public List<PagosGastosExt> findRegistroGastosLikePropertyExt(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		List<PagosGastosExt> extendidos = new ArrayList<PagosGastosExt>();
		List<PagosGastos> entities = null;

		try {
			entities = this.findRegistroGastosLikeProperty(propertyName, value, estatus, incluyeCancelados, orderBy, limite);
			if (entities != null && ! entities.isEmpty()) {
				for (PagosGastos entity : entities) 
					extendidos.add(this.convertidor.PagosGastosToPagosGastosExt(entity));
			}
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findRegistroGastosLikePropertyExt(propertyName, value, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}

		return extendidos;
	}

	@Override
	public List<PagosGastosExt> findGastoComprobarLikePropertyExt(String propertyName, Object value, String estatus, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		List<PagosGastosExt> extendidos = new ArrayList<PagosGastosExt>();
		List<PagosGastos> entities = null;

		try {
			entities = this.findRegistroGastosLikeProperty(propertyName, value, estatus, incluyeCancelados, orderBy, limite);
			if (entities != null && ! entities.isEmpty()) {
				for (PagosGastos entity : entities) 
					extendidos.add(this.convertidor.PagosGastosToPagosGastosExt(entity));
			}
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findRegistroGastosLikePropertyExt(propertyName, value, estatus, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}

		return extendidos;
	}
	
	// --------------------------------------------------------------------------------------------------------------
	
	@Override
	public List<PagosGastosExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();
		List<PagosGastos> lista = null;

		try {
			lista = this.findByProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listaExt.size() + " registros");
			}
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}

		return listaExt;
	}

	@Override
	public List<PagosGastosExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();
		List<PagosGastos> lista = null;

		try {
			lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listaExt.size() + " registros");
			}
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}

		return listaExt;
	}

	@Override
	public List<PagosGastosExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();
		List<PagosGastos> lista = null;

		try {
			lista = this.findInProperty(columnName, values, limite);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listaExt.size() + " registros");
			}
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}

		return listaExt;
	}

	@Override
	public Respuesta cancelacion(PagosGastosExt entityExt, Date fech_modificacion) throws Exception {
		Respuesta reg = new Respuesta();
		PagosGastos entity = null;

		try {
			entity = this.convertidor.PagosGastosExtToPagosGastos(entityExt);
			if (entityExt.getIdBeneficiario() != null)
				entity.setBeneficiario(entityExt.getIdBeneficiario().getNombre());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(fech_modificacion);
			entity.setEstatus("X");
			/*
			 * reg = cancelacionPojo(entityExt, fech_modificacion, usuario);
			 * if(reg.getResultado() == 0) reg = mensajeSaldoCtas((PagosGastos)
			 * reg.getObjeto(), empresa);
			 */

			this.ifzPagosGastos.update(entity);
			reg.setObjeto(entityExt);
			reg.setResultado(0);

			return reg;
		} catch (Exception re) {
			log.error("error en metodo cancelacion", re);
			throw re;
		}
	}
	
	@Override
	public Respuesta mensajeSaldoCtas(PagosGastosExt pojoEntityExt) throws Exception {
		Respuesta reg = new Respuesta();

		try {
			return reg;
		} catch (Exception re) {
			log.error("error en metodo mensajeSaldoCtas", re);
			throw re;
		}
	}

	@Override
	public long save(PagosGastosExt object) throws Exception {
		try {
			return this.save(this.convertidor.PagosGastosExtToPagosGastos(object));
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosExt> findLikeGtosComprobar(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales) {
		List<PagosGastosExt> listGtosComprobarExt = new ArrayList<PagosGastosExt>();
		List<PagosGastos> lista = null;

		try {
			lista = this.ifzPagosGastos.findLikeGtosComprobar(propertyName, value, v_tipo, v_estatus1, getIdEmpresa(), max, sucursales);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listGtosComprobarExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listGtosComprobarExt.size() + " registros");
			}
		} catch (Exception re) {
			throw re;
		}

		return listGtosComprobarExt;
	}

	@Override
	public List<PagosGastosExt> findTransferenciasExt(String propertyName, Object value, String v_tipo, int max, String sucursales) {
		List<PagosGastosExt> listTransferenciasExt = new ArrayList<PagosGastosExt>();
		List<PagosGastos> lista = null;

		try {
			lista = this.findTransferencias(propertyName, value, v_tipo, max, sucursales);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listTransferenciasExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listTransferenciasExt.size() + " registros");
			}
		} catch (Exception re) {
			throw re;
		}

		return listTransferenciasExt;
	}

	@Override
	public List<PagosGastosExt> findLikeGtosPorComprobarPersonaExt(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max) {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();
		List<PagosGastos> lista = null;

		try {
			lista = this.findLikeGtosPorComprobarPersona(value, suc, tipo, estatus, tipoPersona, fecha, traer, max);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listaExt.size() + " registros");
			}
		} catch (Exception re) {
			throw re;
		}

		return listaExt;
	}

	@Override
	public List<PersonaExt> findLikePersonasConGastos(Object value, ConGrupoValores valGpo, String tipoPersona, String tipo, String estatus, int max, Date fecha, String sucursales) {
		List<PersonaExt> listPersonasConGastos = new ArrayList<PersonaExt>();
		List<PagosGastos> listPagosGastos = null;

		try {
			listPagosGastos = this.ifzPagosGastos.findLikePersonasConGastos(value.toString(), tipoPersona, tipo, estatus, getIdEmpresa(), max, null, sucursales);
			if (listPagosGastos != null && ! listPagosGastos.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : listPagosGastos)
					listPersonasConGastos.add(this.convertidor.PagosGastosToPagosGastosExt(var).getIdBeneficiario());
				log.info("Se extendieron " + listPersonasConGastos.size() + " registros");
			}
		} catch (Exception re) {
			throw re;
		}

		return listPersonasConGastos;
	}

	@Override
	public SucursalExt findSucursalById(long idSucursal) {
		SucursalExt pojoSucursalExt = new SucursalExt();
		Sucursal pojoSucursal = null;

		try {
			this.ifzSucursal.setInfoSesion(this.infoSesion);
			pojoSucursal = this.ifzSucursal.findById(idSucursal);
			if (pojoSucursal != null) 
				pojoSucursalExt = this.convertidor.SucursalToSucursalExt(pojoSucursal);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pojoSucursalExt;
	}

	@Override
	public PersonaExt findPersonaById(long idPersona) {
		PersonaExt pojoPersonaExt = new PersonaExt();
		Persona pojoPersona = null;

		try {
			this.ifzPersona.setInfoSesion(this.infoSesion);
			pojoPersona = this.ifzPersona.findById(idPersona);
			if (pojoPersona != null) 
				pojoPersonaExt = this.convertidor.PersonaToPersonaExt(pojoPersona);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pojoPersonaExt;
	}

	@Override
	public PersonaExt findPersonaById(long idPersona, String tipoPersona) {
		PersonaExt pojoPersonaExt = new PersonaExt();
		Persona pojoPersona = null;
		Negocio pojoNegocio = null;

		try {
			if ("P".equals(tipoPersona)) {
				this.ifzPersona.setInfoSesion(this.infoSesion);
				pojoPersona = this.ifzPersona.findById(idPersona);
			} else {
				this.ifzNegocio.setInfoSesion(this.infoSesion);
				pojoNegocio = this.ifzNegocio.findById(idPersona);
			}

			if (pojoNegocio != null && pojoNegocio.getId() != null && pojoNegocio.getId() > 0L) {
				pojoPersona = new Persona();
				pojoPersona.setId(pojoNegocio.getId());
				pojoPersona.setNombre(pojoNegocio.getNombre());
				pojoPersona.setRfc(pojoNegocio.getRfc());
				pojoPersona.setTipoPersona(2L);
			}
			
			if (pojoPersona != null) 
				pojoPersonaExt = this.convertidor.PersonaToPersonaExt(pojoPersona);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pojoPersonaExt;
	}

	@Override
	public CtasBancoExt findCuentaBancariaById(long idCuentaBancaria) {
		CtasBancoExt pojoCtasBancoExt = new CtasBancoExt();
		CuentaBancaria pojoCtasBanco = null;

		try {
			this.ifzCtasBanco.setInfoSesion(this.infoSesion);
			pojoCtasBanco = this.ifzCtasBanco.findById(idCuentaBancaria);
			if (pojoCtasBanco != null) 
				pojoCtasBancoExt = this.convertidor.CtasBancoToCtasBancoExt(pojoCtasBanco);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pojoCtasBancoExt;
	}

	@Override
	@Deprecated
	public List<PagosGastosExt> buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda) {
		List<PagosGastosExt> listPagosGastosExt = new ArrayList<PagosGastosExt>();
		List<PagosGastos> listPagosGastos = null;

		try {
			listPagosGastos = this.ifzPagosGastos.buscarMovimientosCuentas(tipoBusqueda, valorBusqueda, getIdEmpresa());
			if (listPagosGastos != null && ! listPagosGastos.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : listPagosGastos)
					listPagosGastosExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listPagosGastosExt.size() + " registros");
			}
		} catch (Exception ex) {
			throw ex;
		}

		return listPagosGastosExt;
	}

	@Override
	public PagosGastosExt cancelar(PagosGastosExt entity) throws Exception {
		try {
			return this.convertidor.PagosGastosToPagosGastosExt(this.cancelar(this.convertidor.PagosGastosExtToPagosGastos(entity)));
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public List<PagosGastosHolder> encapsularPagosGastosExt(List<PagosGastosExt> listPagosGastos) throws Exception {
		List<PagosGastos> entities = new ArrayList<PagosGastos>();

		try {
			for (PagosGastosExt var : listPagosGastos)
				entities.add(this.convertidor.PagosGastosExtToPagosGastos(var));
			return this.encapsularPagosGastos(entities);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosFac.findExtLikeMovimientoCuentas(propertyName, value, estatus, orderBy, limite)", e);
			throw e;
		}
	}

	// -------------------------------------------------------------------------------------------
	// PRIVADOS
	// -------------------------------------------------------------------------------------------

	private void mensajeNuevoProveedor(String uniqueValue, String nombre, String rfc, String tipoPersona) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "";
		String referencia = "";
		String atributos = "";
		String comando = "";
		// -----------------------------------------
		HashMap<String, String> params = new HashMap<String, String>();
		Gson gson = new Gson();
		
		try {
			if ((rfc == null || "".equals(rfc)) || (tipoPersona == null || "".equals(tipoPersona)))
				return;
			
			nombre = nombre == null || "".equals(nombre.trim()) ? "" : nombre;
			params.put("nombre", nombre);
			params.put("rfc", rfc);
			params.put("tipoPersona", tipoPersona);
			
			target = uniqueValue == null || "".equals(uniqueValue.trim()) ? "" : uniqueValue;
			atributos = gson.toJson(params);
			
			msgTopic = new MensajeTopic(TopicEventosCXP.PROVEEDOR, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/CXP:CXP_TRANSACCION\n\n" + comando + "\n\n", e);
		}
	}

	private String validaTipoPersona(String rfc) {
		if (rfc == null || "".equals(rfc.trim()))
			return "P";
		if ("XAXX010101000".equals(rfc.trim()) || "XEXX010101000".equals(rfc.trim()))
			return "N";
		return (rfc.length() == 12 ? "N" : (rfc.length() == 13 ? "P" : "X"));
	}

	private String getKeyEjercicio(String key) {
		String[] splitted = null;
		
		if (key.contains("|"))
			splitted = key.split("\\|");
		
		return splitted != null ? splitted[0] : key;
	}
	
	@SuppressWarnings("unused")
	private Properties getProperties() {
		Properties properties = null;

		try {
			properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("config.properties"));
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar cargar el archivo 'config.properties'.", e);
			properties = null;
		}
		
		return properties;
	}
	
	private Long getIdEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L);
	}

	private Long getCodigoEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}

	private String getEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getEmpresa() : "");
	}
}
