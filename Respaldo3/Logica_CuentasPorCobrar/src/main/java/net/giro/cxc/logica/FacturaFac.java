package net.giro.cxc.logica;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.ssl.Base64;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;

import com.google.gson.Gson;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.logica.MensajeTransaccionRem;
import net.giro.cxc.FEv33.CMetodoPago;
import net.giro.cxc.FEv33.CMoneda;
import net.giro.cxc.FEv33.CTipoDeComprobante;
import net.giro.cxc.FEv33.CTipoFactor;
import net.giro.cxc.FEv33.CUsoCFDI;
import net.giro.cxc.FEv33.Comprobante;
import net.giro.cxc.FEv33.Comprobante.Conceptos;
import net.giro.cxc.FEv33.Comprobante.Conceptos.Concepto;
import net.giro.cxc.FEv33.Comprobante.Emisor;
import net.giro.cxc.FEv33.Comprobante.Impuestos;
import net.giro.cxc.FEv33.Comprobante.Impuestos.Retenciones.Retencion;
import net.giro.cxc.FEv33.Comprobante.Impuestos.Traslados.Traslado;
import net.giro.cxc.FEv33.Comprobante.Receptor;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.beans.FacturaDetalleImpuestoExt;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.FacturaTimbre;
import net.giro.cxc.beans.FacturasRelacionadas;
import net.giro.cxc.beans.Provisiones;
import net.giro.cxc.dao.FacturaDAO;
import net.giro.cxc.realvirtual.beans.FactElectv33;
import net.giro.cxc.util.FACTURA_ESTATUS;
import net.giro.ne.beans.EmpresaCertificado;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.NQueryRem;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.logica.EmpresaCertificadoRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosCXC;
import net.giro.plataforma.topics.TopicEventosGP;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.logica.FormasPagosRem;

@Stateless
public class FacturaFac implements FacturaRem {
	private static Logger log = Logger.getLogger(FacturaFac.class);
	private InfoSesion infoSesion;
	private long idUsuario;
	private long idEmpresa;
	private long codigoEmpresa;
	// -------------------------------------------------------------------
	private FacturaDAO ifzFactura;
	private FacturaDetalleRem ifzFacturaDetalles;
	private FacturaPagosRem ifzPagos;
	private ProvisionesRem ifzProvision;
	private ObraRem ifzObras;
	private MensajeTransaccionRem ifzMsgTransaccion;
	private FormasPagosRem ifzFormasPagos;
	private FacturaTimbreRem ifzTimbres;
	private EmpresaCertificadoRem ifzEmpCertificado;
	private SucursalesRem ifzSucursales;
	private NQueryRem ifzQuery;
	private ConvertExt convertidor;
	
	public FacturaFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environment);
            this.ifzFactura = (FacturaDAO) ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturaImp!net.giro.cxc.dao.FacturaDAO");
            this.ifzFacturaDetalles = (FacturaDetalleRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			this.ifzPagos = (FacturaPagosRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosFac!net.giro.cxc.logica.FacturaPagosRem");
            this.ifzProvision = (ProvisionesRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//ProvisionesFac!net.giro.cxc.logica.ProvisionesRem");
            this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
            this.ifzMsgTransaccion = (MensajeTransaccionRem) ctx.lookup("ejb:/Logica_Contabilidad//MensajeTransaccionFac!net.giro.contabilidad.logica.MensajeTransaccionRem");
    		this.ifzFormasPagos = (FormasPagosRem) ctx.lookup("ejb:/Logica_TYG//FormasPagosFac!net.giro.tyg.logica.FormasPagosRem");
			this.ifzTimbres = (FacturaTimbreRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaTimbreFac!net.giro.cxc.logica.FacturaTimbreRem");
            this.ifzEmpCertificado = (EmpresaCertificadoRem) ctx.lookup("ejb:/Logica_Publico//EmpresaCertificadoFac!net.giro.plataforma.logica.EmpresaCertificadoRem");
			this.ifzSucursales = (SucursalesRem) ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
            this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
            this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaFac", e);
		}
	}
	
	@Override
	public void showSystemOuts(boolean value) { 
		this.convertidor.setMostrarSystemOut(value); 
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
		if (this.infoSesion != null) {
			this.idUsuario = this.infoSesion.getAcceso().getUsuario().getId();
			this.idEmpresa = this.infoSesion.getEmpresa().getId();
			this.codigoEmpresa = this.infoSesion.getEmpresa().getCodigo();
		}
	}

	@Override
	public void setInfoSesion(long idUsuario, long idEmpresa, long codigoEmpresa) {
		this.idUsuario = idUsuario;
		this.idEmpresa = idEmpresa;
		this.codigoEmpresa = codigoEmpresa;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(Factura entity) throws Exception {
		try {
			return this.ifzFactura.save(entity, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Error en Logica_CuentasPorCobrar.FacturaFac.save(entity)", re);
			throw re;
		}
	}
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Factura> save(List<Factura> listEntities) throws Exception {
		try {
			return this.ifzFactura.saveOrUpdateList(listEntities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.FacturaFac.save(listEntities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(Factura entity) throws Exception {
		try {
			this.ifzFactura.update(entity);
		} catch (Exception re) { 
			log.error("Error en Logica_CuentasPorCobrar.FacturaFac.update(entity)", re);
			throw re;
		}
	}

	@Override
	public void delete(long idFactura) throws Exception {
		try {
			this.ifzFactura.delete(idFactura);
		} catch (Exception re) {
			log.error("Error en Logica_CuentasPorCobrar.FacturaFac.delete(idFactura)", re);
			throw re;
		}
	}

	@Override
	public Respuesta cancelarFactura(long idFactura, String motivoCancelacion) throws Exception {
		try {
			return this.cancelarFactura(this.ifzFactura.findById(idFactura), motivoCancelacion, false, false);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.FacturaFac.cancelarFactura(idFactura)", e);
			throw e;
		}
	}

	@Override
	public Respuesta cancelarFactura(Factura entity, String motivoCancelacion) throws Exception {
		try {
			return this.cancelarFactura(entity, motivoCancelacion, false, false);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.FacturaFac.cancelarFactura(entity)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta cancelarFactura(Factura factura, String motivoCancelacion, boolean debugging, boolean testing) throws Exception {
		Respuesta respuesta = new Respuesta();
		// -----------------------------------
		FacturaExt extendida = null;
		FacturaTimbre timbre = null;
		
		try {
			if (factura == null || factura.getId() == null || factura.getId() <= 0L) {
				respuesta.getBody().addValor("factura", factura);
				respuesta.getErrores().setCodigoError(0);
				respuesta.getErrores().setDescError("Pago no registrado");
				return respuesta;
			}
			
			// Evaluamos Factura para cancelacion
			respuesta = this.evaluaCancelacion(factura.getId());
			if (respuesta.getErrores().getCodigoError() != 0L)
				return respuesta;

			factura.setEstatus(FACTURA_ESTATUS.Cancelada);
			if (factura.getTimbrado() == 1 && factura.getIdTimbre() > 0L) {
				factura.setEstatus(FACTURA_ESTATUS.PendienteCancelacion);
				
				timbre = this.ifzTimbres.findById(factura.getIdTimbre());
				if (timbre != null && timbre.getId() != null && timbre.getId() > 0L) {
					if (timbre.getCanceladoPor() <= 0L)
						timbre.setCanceladoPor(getIdUsuario());
					if (timbre.getMotivoCancelacion() == null || "".equals(timbre.getMotivoCancelacion().trim()))
						timbre.setMotivoCancelacion(motivoCancelacion);
					if (timbre.getFechaSolicitudCancelacion() == null)
						timbre.setFechaSolicitudCancelacion(Calendar.getInstance().getTime());
					if (timbre.getIdEmpresa() <= 0L)
						timbre.setIdEmpresa(getIdEmpresa());
					timbre.setModificadoPor(getIdUsuario());
					timbre.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzTimbres.setInfoSesion(this.infoSesion);
					this.ifzTimbres.update(timbre);
					// enviamos peticion de cancelacion
					cancelacionFactura(factura.getId());
				}
			}
			
			if (factura.getCanceladoPor() <= 0L)
				factura.setCanceladoPor(getIdUsuario());
			if (factura.getMensajeCancelacion() == null || "".equals(factura.getMensajeCancelacion().trim()))
				factura.setMensajeCancelacion(motivoCancelacion);
			factura.setFechaCancelacion(Calendar.getInstance().getTime());
			factura.setFechaModificacion(Calendar.getInstance().getTime());
			factura.setModificadoPor(getIdUsuario());
			this.update(factura);
			
			// Extiendo factura
			extendida = this.findExtById(factura.getId());
			
			// Actualizacion de Cobranza en GP
			actualizaCobranza(factura.getId());
			respuesta = transaccionCancelacionFactura(factura, getIdUsuario(), getIdEmpresa());
			
			// Añadimos la factura al resultado
			respuesta.getBody().addValor("factura", factura);
			respuesta.getBody().addValor("extendida", extendida);
			respuesta.getBody().addValor("timbre", timbre);
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.cancelarFactura(entity, debugging, testing)\n", e);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("No se pudo cancelar la Factura indicada");
			throw e;
		}
		
		return respuesta;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta evaluaCancelacion(long idFactura) throws Exception {
		Respuesta respuesta = null;
		// ---------------------------------
		FacturaExt extendida = null;
		Factura factura = null;
		// ---------------------------------
		//Factura relacionada = null;
		int pagosRegistrados = 0;
		
		try {
			respuesta = new Respuesta();
			factura = this.findById(idFactura);
			if (factura == null || factura.getId() == null || factura.getId() <= 0L) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No se pudo recuperar la Factura indicada");
				return respuesta;
			}
			
			// Validamos estatus de Factura
			if (factura.getEstatus() != FACTURA_ESTATUS.Activa.ordinal()) {
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("La Factura indicada ya esta Cancelada o en proceso de Cancelacion");
				return respuesta;
			}
			
			// Validamos pagos multiples
			this.ifzPagos.setInfoSesion(this.infoSesion);
			pagosRegistrados = this.ifzPagos.validarPagosMultiples(idFactura);
			if (pagosRegistrados > 0) {
				respuesta.getErrores().setCodigoError(3L);
				respuesta.getErrores().setDescError("Se detectaron " + pagosRegistrados + " Pagos multiples que involucran a esta Factura");
				return respuesta;
			}
			
			// Validamos factura esta relacionada con algun egreso
			/*if (factura.getIdFacturaRelacionada() > 0L) {
				relacionada = this.findById(factura.getIdFacturaRelacionada());
				if (relacionada.getEstatus() != FACTURA_ESTATUS.Cancelada.ordinal()) {
					respuesta.getErrores().setCodigoError(4L);
					respuesta.getErrores().setDescError("La Factura relacionada debe estar Cancelada");
					return respuesta;
				}
			}*/
			
			// Recuperamos Factura extendida
			extendida = this.convertir(factura);
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.evaluaCancelacion(idFactura)\n", e);
			throw e;
		} finally {
			respuesta.getBody().addValor("factura", factura);
			respuesta.getBody().addValor("extendida", extendida);
			respuesta.getBody().addValor("pagosRegistrados", pagosRegistrados);
		}
		
		return respuesta;
	}

	@Override
	public Respuesta provisionar(long idFactura, double montoProvision, long usuarioId) throws Exception {
		try {
			return this.provisionar(this.ifzFactura.findById(idFactura), montoProvision, usuarioId);
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.provisionar(entityId, montoProvision, usuarioId)\n", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta provisionar(Factura entity, double montoProvision, long usuarioId) throws Exception {
		Respuesta respuesta = new Respuesta();
		Provisiones pojoProvision = null;
		
		try {
			this.ifzProvision.setInfoSesion(this.infoSesion);
			respuesta = transaccionProvision(entity, montoProvision, 0L);
			if (respuesta.getErrores().getCodigoError() == 0L) {
				pojoProvision = this.ifzProvision.findProvision(entity);
				if (pojoProvision == null) {
					pojoProvision = new Provisiones();
					pojoProvision.setIdFactura(entity);
					pojoProvision.setMonto(montoProvision);
					pojoProvision.setMontoOriginal(montoProvision);
					pojoProvision.setObservaciones("Provision sin grupo");
					pojoProvision.setGrupo(0);
					pojoProvision.setEstatus(0);
					pojoProvision.setCreadoPor(usuarioId);
					pojoProvision.setFechaCreacion(Calendar.getInstance().getTime());
				}
				
				pojoProvision.setModificadoPor(usuarioId);
				pojoProvision.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzProvision.saveOrUpdate(pojoProvision);
				this.facturaProvisionada(entity, usuarioId);
			}
			
			// Añadimos la factura al resultado
			respuesta.getBody().addValor("factura", entity);
			
			return respuesta;
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.provisionar(entity, montoProvision, usuarioId)\n", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Factura findById(long idFactura) throws Exception {
		try {
			return this.ifzFactura.findById(idFactura);
		} catch (Exception re) { 
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findById(idFactura)\n", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Factura findByIdTimbre(long idTimbre) throws Exception {
		List<Factura> facturas = null;
		
		try {
			facturas = this.findByProperty("idTimbre", idTimbre, 0L, "", 0, false, true, "id desc", 0);
			if (facturas == null || facturas.isEmpty())
				return null;
			return facturas.get(0);
		} catch (Exception re) { 
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findByIdTimbre(idTimbre)\n", re);
			throw re;
		}
	}

	@Override
	public List<Factura> findLike(String value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			while (value.trim().contains("  "))
				value = value.trim().replace("  ", " ");
			value = (value.trim().contains("+") ? value.trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.trim());
			value = (value.trim().contains("|") ? value.trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.trim());
			value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			return this.ifzFactura.findLike(value, idCliente, tipoComprobante, tipoObra, timbradas, incluyeCanceladas, orderBy, getIdEmpresa(), limite);
		} catch (Exception re) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findLike(value, idCliente, tipoComprobante, tipoObra, incluyeCanceladas, limite)\n", re);
			throw re;
		} 
	}

	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), idCliente, tipoComprobante, tipoObra, timbradas, incluyeCanceladas, orderBy, limite);
			if (value.getClass() == java.lang.String.class) {
				while (value.toString().trim().contains("  "))
					value = value.toString().trim().replace("  ", " ");
				value = (value.toString().trim().contains("+") ? value.toString().trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.toString().trim());
				value = (value.toString().trim().contains("|") ? value.toString().trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.toString().trim());
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			}
			return this.ifzFactura.findLikeProperty(propertyName, value, idCliente, tipoComprobante, tipoObra, timbradas, incluyeCanceladas, orderBy, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("Ocurrio un problema con FacturaFac.findLikeProperty(propertyName, value, idCliente, tipoComprobante, tipoObra, incluyeCanceladas, limite)", e);
			throw e;
		} 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Factura> findByProperty(String propertyName, Object value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), idCliente, tipoComprobante, tipoObra, timbradas, incluyeCanceladas, orderBy, limite);
			return this.ifzFactura.findByProperty(propertyName, value, idCliente, tipoComprobante, tipoObra, timbradas, incluyeCanceladas, orderBy, getIdEmpresa(), limite);
		} catch (Exception re) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findByProperty(propertyName, value, idCliente, tipoComprobante, tipoObra, timbradas, incluyeCanceladas, orderBy, limite)\n", re);
			throw re;
		} 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Factura> findList(List<Long> idFacturas) throws Exception {
		try {
			return this.ifzFactura.findList(idFacturas, "", getIdEmpresa());
		} catch (Exception e) {
			log.error("Ocurrio un problema con FacturaFac.findList(idFacturas)", e);
			throw e;
		} 
	}
	
	@Override
	public List<Factura> findLike(String value, String tipoComprobante, int tipoObra, boolean incluyeCanceladas, int limite) throws Exception {
		try {
			return this.findLike(value, 0L, tipoComprobante, tipoObra, false, incluyeCanceladas, "", limite);
		} catch (Exception re) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findLike(value, tipoComprobante, tipoObra, incluyeCanceladas, limite)\n", re);
			throw re;
		} 
	}

	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, null, 0, false, false, "", 0);
		} catch (Exception e) {
			log.error("Ocurrio un problema con FacturaFac.findLikeProperty(propertyName, value)", e);
			throw e;
		}
	}

	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, null, 0, false, false, "", limite);
		} catch (Exception e) {
			log.error("Ocurrio un problema con FacturaFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}	

	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value, int tipoObra, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, null, tipoObra, false, false, "", limite);
		} catch (Exception e) {
			log.error("Ocurrio un problema con FacturaFac.findLikeProperty(propertyName, value, tipoObra, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value, String tipoComprobante, int tipoObra, boolean incluyeCanceladas, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, tipoComprobante, tipoObra, false, incluyeCanceladas, "", limite);
		} catch (Exception e) {
			log.error("Ocurrio un problema con FacturaFac.findLikeProperty(propertyName, value, tipoComprobante, tipoObra, incluyeCanceladas, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Factura> findLikePropertySinProvision(String propertyName, Object value, int tipoObra, int limite) throws Exception {
		try {
			return this.ifzFactura.findLikePropertySinProvision(propertyName, value, tipoObra, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("Ocurrio un problema con FacturaFac.findLikePropertySinProvision(propertyName, value, tipoObra, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Factura> findByProperty(String propertyName, final Object value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0L, "", 0, false, false, "", 0);//(propertyName, value, null, 0, 0);
		} catch (Exception re) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findByProperty(propertyName, value)\n", re);
			throw re;
		}
	}
	
	@Override
	public List<Factura> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0L, "", 0, false, false, "", 0);//(propertyName, value, null, 0, limite);
		} catch (Exception re) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findByProperty(propertyName, value, limite)\n", re);
			throw re;
		}
	}
	
	@Override
	public List<Factura> findByProperty(String propertyName, Object value, int tipoObra, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0L, "", tipoObra, false, false, "", limite);//(propertyName, value, null, tipoObra, limite);
		} catch (Exception re) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findByProperty(propertyName, value, tipoObra, limite)\n", re);
			throw re;
		}
	}

	@Override
	public List<Factura> findByProperty(String propertyName, Object value, String tipoComprobante, int tipoObra, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0L, tipoComprobante, tipoObra, false, false, "", limite);
		} catch (Exception re) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findByProperty(propertyName, value, tipoComprobante, tipoObra, limite)\n", re);
			throw re;
		} 
	}

	@Override
	public List<Factura> findByPropertyPojoCompleto(String propertyName, Object value, int tipo) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0L, "", tipo, false, false, "", 0);//this.ifzFactura.findByPropertyPojoCompleto(propertyName, value, tipo, getIdEmpresa());
		} catch(Exception re) {
			log.error("Ocurrio un problema con FacturaFac.findByPropertyPojoCompleto(propertyName, value, tipo)", re);
			throw re;
		} 
	}

	@Override
	public List<Factura> findLikeProperties(HashMap<String, Object> params) throws Exception {
		try {
			return this.ifzFactura.findLikeProperties(params, getIdEmpresa());
		} catch (Exception re) { 
			log.error("Ocurrio un problema con FacturaFac.findLikeProperties(params)", re);
			throw re;
		} 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void facturaProvisionada(Factura entity, long usuarioId) throws Exception {
		try {
			entity.setProvisionada(1);
			entity.setProvisionadaPor(usuarioId);
			entity.setFechaProvisionada(Calendar.getInstance().getTime());
			this.update(entity);
		} catch (Exception e) { 
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.facturaProvisionada(entity, usuarioId)\n", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void cancelarProvision(Factura entity, long usuarioId) throws Exception {
		try {
			entity.setProvisionada(2);
			entity.setProvisionadaPor(usuarioId);
			entity.setFechaProvisionada(Calendar.getInstance().getTime());
			this.update(entity);
		} catch (Exception e) { 
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.cancelarProvision(entity, usuarioId)\n", e);
			throw e;
		}
	}

	@Override
	public BigDecimal calcularLiquidado(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception {
		try {
			this.ifzPagos.setInfoSesion(this.infoSesion);
			return this.ifzPagos.findLiquidado(idFactura, fechaDesde, fechaHasta);
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.findLiquidado(idFactura, fechaDesde, fechaHasta)", e);
			throw e;
		}
	}

	@Override
	public BigDecimal calcularLiquidadoPesos(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception {
		try {
			this.ifzPagos.setInfoSesion(this.infoSesion);
			return this.ifzPagos.findLiquidadoPesos(idFactura, fechaDesde, fechaHasta);
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.findLiquidadoPesos(idFactura, fechaDesde, fechaHasta)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public BigDecimal calcularEgresos(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception {
		List<Factura> notasCreditos = null;
		boolean validarFechas = false;
		double egresos = 0;
		
		try {
			// Validaciones
			if (fechaDesde != null && fechaHasta == null)
				fechaHasta = Calendar.getInstance().getTime();
			validarFechas = ((fechaDesde != null && fechaHasta != null) || (fechaDesde == null && fechaHasta != null));
			
			// Notas de Credito registrados
			notasCreditos = this.findNotasCreditoByFactura(idFactura); 
			if (notasCreditos != null && ! notasCreditos.isEmpty()) {
				for (Factura notasCredito : notasCreditos) {
					if (notasCredito.getEstatus() != FACTURA_ESTATUS.Activa.ordinal())
						continue;
					
					if (validarFechas) {
						if (fechaDesde != null && notasCredito.getFechaEmision().compareTo(fechaDesde) < 0)
							continue;
						if (fechaHasta != null && notasCredito.getFechaEmision().compareTo(fechaHasta) > 0)
							continue;
					}
					
					egresos += notasCredito.getTotal().doubleValue();
				}
			}
			
			return new BigDecimal(egresos);
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findEgresos(idFactura, fechaDesde, fechaHasta)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public BigDecimal calcularEgresosPesos(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception {
		List<Factura> notasCreditos = null;
		boolean validarFechas = false;
		double tipoCambio = 0;
		double egresos = 0;
		
		try {
			// Validaciones
			if (fechaDesde != null && fechaHasta == null)
				fechaHasta = Calendar.getInstance().getTime();
			validarFechas = ((fechaDesde != null && fechaHasta != null) || (fechaDesde == null && fechaHasta != null));
			
			// Pagos registrados
			notasCreditos = this.findNotasCreditoByFactura(idFactura); 
			if (notasCreditos != null && ! notasCreditos.isEmpty()) {
				for (Factura notasCredito : notasCreditos) {
					if (notasCredito.getEstatus() != FACTURA_ESTATUS.Activa.ordinal())
						continue;
					
					if (validarFechas) {
						if (fechaDesde != null && notasCredito.getFechaEmision().compareTo(fechaDesde) < 0)
							continue;
						if (fechaHasta != null && notasCredito.getFechaEmision().compareTo(fechaHasta) > 0)
							continue;
					}

					tipoCambio = (notasCredito.getTipoCambio() > 0) ? notasCredito.getTipoCambio() : 1;
					egresos += notasCredito.getTotal().doubleValue() * tipoCambio;
					/*if (! validarFechas) {
						tipoCambio = (notasCredito.getTipoCambio() > 0) ? notasCredito.getTipoCambio() : 1;
						egresos += notasCredito.getTotal().doubleValue() * tipoCambio;
						continue;
					}
					
					if (notasCredito.getFechaEmision().before(fechaDesde) && notasCredito.getFechaEmision().after(fechaHasta)) {
						tipoCambio = (notasCredito.getTipoCambio() > 0) ? notasCredito.getTipoCambio() : 1;
						egresos += notasCredito.getTotal().doubleValue() * tipoCambio;
					}*/
				}
			}
			
			return new BigDecimal(egresos);
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findEgresosPesos(idFactura, fechaDesde, fechaHasta)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public BigDecimal calcularSaldo(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception {
		BigDecimal resultado = BigDecimal.ZERO;
		Factura factura = null;
		double facturado = 0;
		double pagado = 0;
		double egresos = 0;
		double saldo = 0;
		double saldoOriginal = 0;
		double limiteFacturaPagada = 0;
		boolean validarFechas = false;
		
		try {
			// Validaciones
			if (fechaDesde != null && fechaHasta == null)
				fechaHasta = Calendar.getInstance().getTime();
			validarFechas = ((fechaDesde != null && fechaHasta != null) || (fechaDesde == null && fechaHasta != null));
			
			factura = this.findById(idFactura);
			if (factura == null || factura.getId() == null || factura.getId() <= 0L)
				return BigDecimal.ZERO;
			
			// Rango permitido para indicar factura pagada
			limiteFacturaPagada = obtenerLimiteFacturaPagada();
			saldoOriginal = factura.getSaldo().doubleValue();
			facturado = factura.getTotal().doubleValue();
			// Pagos
			pagado = this.calcularLiquidado(factura.getId(), fechaDesde, fechaHasta).doubleValue();
			// Notas de Credito
			egresos = this.calcularEgresos(factura.getId(), fechaDesde, fechaHasta).doubleValue();
			
			// Calculamos saldo 
			facturado = facturado - egresos;
			saldo = facturado - pagado;
			if (facturado > limiteFacturaPagada && saldo < limiteFacturaPagada)
				saldo = 0;
			resultado = new BigDecimal(saldo);
			
			// Actualizamos saldo en factura si corresponde
			if (saldo != saldoOriginal && ! validarFechas) {
				factura.setSaldo(resultado);
				this.update(factura);
			}
			
			return resultado;
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.calcularSaldo(idFactura, fechaDesde, fechaHasta)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public BigDecimal calcularSaldoPesos(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception {
		BigDecimal resultado = BigDecimal.ZERO;
		Factura factura = null;
		double facturado = 0;
		double pagado = 0;
		double egresos = 0;
		double saldo = 0;
		double saldoOriginal = 0;
		double limiteFacturaPagada = 0;
		boolean validarFechas = false;
		
		try {
			// Validaciones
			if (fechaDesde != null && fechaHasta == null)
				fechaHasta = Calendar.getInstance().getTime();
			validarFechas = ((fechaDesde != null && fechaHasta != null) || (fechaDesde == null && fechaHasta != null));
			
			factura = this.findById(idFactura);
			if (factura == null || factura.getId() == null || factura.getId() <= 0L)
				return BigDecimal.ZERO;
			
			// Rango permitido para indicar factura pagada
			limiteFacturaPagada = obtenerLimiteFacturaPagada();
			saldoOriginal = factura.getSaldo().doubleValue();
			facturado = factura.getTotal().doubleValue();
			
			// Pagos
			pagado = this.calcularLiquidadoPesos(factura.getId(), fechaDesde, fechaHasta).doubleValue();
			// Notas de Credito
			egresos = this.calcularEgresosPesos(factura.getId(), fechaDesde, fechaHasta).doubleValue();
			// calculasmos facturado
			facturado = facturado - egresos;
			
			// Calculamos saldo 
			saldo = facturado;
			saldo = saldo - pagado;
			if (facturado > limiteFacturaPagada && saldo < limiteFacturaPagada)
				saldo = 0;
			resultado = new BigDecimal(saldo);
			
			// Actualizamos saldo en factura si corresponde
			if (saldo != saldoOriginal && ! validarFechas) {
				factura.setSaldo(resultado);
				this.update(factura);
			}
			
			return resultado;
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.calcularSaldoPesos(idFactura, fechaDesde, fechaHasta)", e);
			throw e;
		}
	}

	@Override
	public BigDecimal calcularSaldoAFecha(Long idFactura, Date fecha) throws Exception {
		return this.calcularSaldo(idFactura, null, fecha);
	}

	@Override
	public BigDecimal calcularSaldoAFechaPesos(Long idFactura, Date fecha) throws Exception {
		return this.calcularSaldoPesos(idFactura, null, fecha);
	}

	@Override
	public List<Factura> comprobarFolioFacturacion(String serie, String folio) throws Exception {
		try {
			return this.ifzFactura.comprobarFolioFacturacion(serie, folio, "", getIdEmpresa());
		} catch (Exception re) {
			log.error("Error al comprobar Serie y Folio para Facturas CXC: " + serie + "-" + folio, re);
			throw re;
		} 
	}

	@Override
	public List<Factura> findTimbradas(String orderBy, int limite) throws Exception {
		try {
			return this.findTimbradas("", "", false, orderBy, limite);
		} catch (Exception re) { 
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.findTimbradas(orderBy, limite)", re);
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findTimbradas(String propertyName, Object value, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, "", 0, true, incluyeCanceladas, orderBy, limite);
		} catch (Exception re) { 
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.findTimbradas(orderBy, limite)", re);
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findProvisionadas(String orderBy, int limite) throws Exception {
		try {
			return this.ifzFactura.findProvisionadas(orderBy, getIdEmpresa(), limite);
		} catch (Exception re) { 
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.findProvisionadas(orderBy, limite)", re);
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Obra> ObrafindByProperty(String propertyName, final Object value) throws Exception {
		try {
			this.ifzObras.setInfoSesion(this.infoSesion);
			return this.ifzObras.findByProperty(propertyName, value, 1);
		} catch (Exception re) { 
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.ObrafindByProperty(propertyName, value)", re);
			throw re;
		} 
	}

	@Override
	public List<Factura> findNotasCreditoByFactura(long idFactura) throws Exception {
		try {
			return this.findByProperty("idFacturaRelacionada", idFactura, 0L, "E", 0, false, false, "", 0);
		} catch (Exception re) { 
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.findNotasCreditoByFactura(idFactura)", re);
			throw re;
		}
	}

	@Override
	public BigDecimal findMontoNotasCreditoByFactura(long idFactura) {
		List<Factura> notasCredito = null;
		BigDecimal monto = null;
		
		try {
			monto = BigDecimal.ZERO;
			notasCredito = this.findNotasCreditoByFactura(idFactura);
			if (notasCredito != null && ! notasCredito.isEmpty()) {
				for (Factura notaCredito : notasCredito)
					monto = monto.add(notaCredito.getTotal());
			}
		} catch (Exception re) { 
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.findMontoNotasCreditoByFactura(idFactura)", re);
		}
		
		return monto;
	}

	@Override
	public List<Factura> provisionMensual(int mes, int limite) throws Exception {
		SimpleDateFormat formatter = null;
		Calendar cal = null;
		Date fechaDesde = null;
		Date fechaHasta = null;
		String dateString = "";
		
		try {
			cal = Calendar.getInstance();
			formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			dateString = (mes < 10 ? "0" : "") + mes + "/01/" + cal.get(Calendar.YEAR) + " 00:00:00";
			cal.setTime(formatter.parse(dateString));
			
			fechaDesde = formatter.parse((mes < 10 ? "0" : "") + mes + "/01/" + cal.get(Calendar.YEAR) + " 00:00:00");
			fechaHasta = formatter.parse((mes < 10 ? "0" : "") + mes + "/" + cal.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR) + " 23:59:59");
			return this.provisionMensual(fechaDesde, fechaHasta, limite);
		} catch (Exception re) { 
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.provisionMensual(mes, limite)", re);
			throw re; 
		} 
	}
	
	@Override
	public List<Factura> provisionMensual(Date fechaDesde, Date fechaHasta, int limite) throws Exception {
		try {
			return this.ifzFactura.provisionMensual(fechaDesde, fechaHasta, "", getIdEmpresa(), limite);
		} catch (Exception re) { 
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.provisionMensual(fechaDesde, fechaHasta, limite)", re);
			throw re;
		} 
	}

	@Override
	public List<Factura> paraProvisionar(int year, int month, String orderBy) throws Exception {
		Calendar cal = Calendar.getInstance();
		Date fechaDesde = null;
		Date fechaHasta = null;
		
		try {
			// Fecha Inicial
			cal.set(year, month, 1);
			fechaDesde = cal.getTime();
			
			// Fecha Final
			cal.set(year, month, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			fechaHasta = cal.getTime();
			
			return this.ifzFactura.paraProvisionar(fechaDesde, fechaHasta, orderBy, getIdEmpresa());
		} catch (Exception re) { 
			log.error("Ocurrio un problema en FacturaFac.paraProvisionar(year, month)", re);
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public Respuesta timbrar(FacturaExt factura, String version, String usoCfdi, int cfdiRelacionado, String cfdiRelacionadoUuid, String cfdiRelacionadoTipoRelacion) throws Exception {
		return this.timbrar(factura, version, usoCfdi, cfdiRelacionado, cfdiRelacionadoUuid, cfdiRelacionadoTipoRelacion, false, false, false);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta timbrar(FacturaExt extendida, String version, String usoCfdi, int cfdiRelacionado, String cfdiRelacionadoUuid, String cfdiRelacionadoTipoRelacion, boolean debugging, boolean testing, boolean noTimbrar) throws Exception {
		Respuesta respuesta = new Respuesta();
		FactElectv33 timbre33 = null;
		FacturaTimbre timbre = null;
		String serie = "";
		String folio = "";
		boolean timbrada = false;
		
		try {
			serie = extendida.getSerie();
			folio = extendida.getFolio();
			
			if (debugging) log.info("Generando/Comprobando registro timbre ... ");
			timbre = extendida.getIdTimbre();
			if (timbre == null || timbre.getId() == null || timbre.getId() <= 0L) {
				this.ifzTimbres.setInfoSesion(this.infoSesion);
				timbre = this.ifzTimbres.comprobarTimbre(serie, folio);
				if (timbre == null) {
					timbre = new FacturaTimbre();
					timbre.setId(0L);
					timbre.setSerie(serie);
					timbre.setFolio(folio);
					timbre.setIdEmpresa(this.infoSesion.getEmpresa().getId());
					timbre.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
					timbre.setFechaCreacion(Calendar.getInstance().getTime());
				}
			}
			
			timbre.setTimbrado(0);
			timbre.setTimbradoPor(this.infoSesion.getAcceso().getUsuario().getId());
			timbre.setVersion(version);
			timbre.setUsoCfdi(usoCfdi);
			timbre.setCfdiRelacionado(cfdiRelacionado);
			timbre.setCfdiRelacionadoUuid(cfdiRelacionadoUuid);
			timbre.setCfdiTipoRelacion(cfdiRelacionadoTipoRelacion); 
			timbre.setPruebas((testing ? 1 : 0));
			timbre.setRfcReceptor(extendida.getRfc());
			timbre.setRfcEmisor(this.infoSesion.getEmpresa().getRfc());
			
			// Guardamos y asignamos a la factura
			if (debugging) log.info("Guardando registro timbre ... ");
			this.ifzTimbres.setInfoSesion(this.infoSesion);
			timbre = this.ifzTimbres.saveOrUpdate(timbre);
			
			// Actualizamos factura
			if (debugging) log.info("Relacionando registro timbre en la Factura ... ");
			extendida.setIdTimbre(timbre);
			this.update(extendida);

			/*
			 * FACTURAS 3.2
				timbre = new FactElect();
				resTimbre = timbre.foliar(this.pojoFactura.getId(), this.usuarioId);
				actualizaFactura(timbre.getData(), timbre.facturaActualizada());
				
				if (! "".equals(resTimbre.trim())) {
					log.info("ERROR 5: Error al timbrar: " + resTimbre.trim());
					control(5, "No se pudo timbrar.\n" + resTimbre.trim());
					return;
				}
			 */
			
			if (debugging) log.info("Timbrando ... ");
			timbre33 = new FactElectv33(this.infoSesion);
			timbre33.setDebugging(debugging);
			timbre33.setTesting(testing);
			timbre33.setNoTimbrar(noTimbrar);
			timbrada = timbre33.timbrarFactura(extendida, this.infoSesion.getAcceso().getUsuario().getId());
			if (noTimbrar && timbrada) {
				extendida.setTimbrado(timbrada ? 1 : 0);
				extendida.setUuid("uuid");
			}
			
			// Actualizacion de Cobranza si corresponde
			if (timbrada && "I".equals(extendida.getTipoComprobante())) {
				if ("PUE".equals(extendida.getMetodoPago()) && extendida.getSaldo().doubleValue() > 0) 
					this.generarPago(extendida.getId());
				
				if ("PPD".equals(extendida.getMetodoPago())) 
					this.actualizaCobranza(extendida.getId());
			}

			if (debugging) log.info("Terminando ... ");
			respuesta.getBody().addValor("factura", extendida);
			respuesta.getErrores().setCodigoError(timbre33.getErrorCodigo());
			respuesta.getErrores().setDescError(timbre33.getErrorDescripcion());
		} catch (Exception e) { 
			log.error("Ocurrio un problema al intentar timbrar la Factura indicada: " + extendida.getId(), e);
			throw e;
		}
		
		return respuesta;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta timbrar(FacturaExt extendida, String version, String usoCfdi, int cfdiRelacionado, List<FacturasRelacionadas> listFacturasRelacionadas, String cfdiRelacionadoTipoRelacion, boolean debugging, boolean testing, boolean noTimbrar) throws Exception {
		Respuesta respuesta = new Respuesta();
		FactElectv33 timbre33 = null;
		FacturaTimbre timbre = null;
		String serie = "";
		String folio = "";
		boolean timbrada = false;
		
		try {
			serie = extendida.getSerie();
			folio = extendida.getFolio();
			
			if (debugging) log.info("Generando/Comprobando registro timbre ... ");
			timbre = extendida.getIdTimbre();
			if (timbre == null || timbre.getId() == null || timbre.getId() <= 0L) {
				this.ifzTimbres.setInfoSesion(this.infoSesion);
				timbre = this.ifzTimbres.comprobarTimbre(serie, folio);
				if (timbre == null) {
					timbre = new FacturaTimbre();
					timbre.setId(0L);
					timbre.setSerie(serie);
					timbre.setFolio(folio);
					timbre.setIdEmpresa(this.infoSesion.getEmpresa().getId());
					timbre.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
					timbre.setFechaCreacion(Calendar.getInstance().getTime());
				}
			}
			
			timbre.setTimbrado(0);
			timbre.setTimbradoPor(this.infoSesion.getAcceso().getUsuario().getId());
			timbre.setVersion(version);
			timbre.setUsoCfdi(usoCfdi);
			timbre.setCfdiRelacionado(cfdiRelacionado);
			timbre.setCfdiTipoRelacion(cfdiRelacionadoTipoRelacion); 
			timbre.setPruebas((testing ? 1 : 0));
			timbre.setRfcReceptor(extendida.getRfc());
			timbre.setRfcEmisor(this.infoSesion.getEmpresa().getRfc());
			
			String cfdiRelacionadoUuid = "";
			for (FacturasRelacionadas var : listFacturasRelacionadas) {
				cfdiRelacionadoUuid += "["+var.getCfdiRelacionadoUuid()+"]";
			}
			timbre.setCfdiRelacionadoUuid(cfdiRelacionadoUuid);
			
			// Guardamos y asignamos a la factura
			if (debugging) log.info("Guardando registro timbre ... ");
			this.ifzTimbres.setInfoSesion(this.infoSesion);
			timbre = this.ifzTimbres.saveOrUpdate(timbre);
			
				
			// Actualizamos factura
			if (debugging) log.info("Relacionando registro timbre en la Factura ... ");
			extendida.setIdTimbre(timbre);
			this.update(extendida);

			/*
			 * FACTURAS 3.2
				timbre = new FactElect();
				resTimbre = timbre.foliar(this.pojoFactura.getId(), this.usuarioId);
				actualizaFactura(timbre.getData(), timbre.facturaActualizada());
				
				if (! "".equals(resTimbre.trim())) {
					log.info("ERROR 5: Error al timbrar: " + resTimbre.trim());
					control(5, "No se pudo timbrar.\n" + resTimbre.trim());
					return;
				}
			 */
			
			if (debugging) log.info("Timbrando ... ");
			timbre33 = new FactElectv33(this.infoSesion);
			timbre33.setDebugging(debugging);
			timbre33.setTesting(testing);
			timbre33.setNoTimbrar(noTimbrar);
			timbrada = timbre33.timbrarFactura(extendida,listFacturasRelacionadas, this.infoSesion.getAcceso().getUsuario().getId());
			if (noTimbrar && timbrada) {
				extendida.setTimbrado(timbrada ? 1 : 0);
				extendida.setUuid("uuid");
			}
			
			// Actualizacion de Cobranza si corresponde
			if (timbrada && "I".equals(extendida.getTipoComprobante())) {
				if ("PUE".equals(extendida.getMetodoPago()) && extendida.getSaldo().doubleValue() > 0) 
					this.generarPago(extendida.getId());
				
				if ("PPD".equals(extendida.getMetodoPago())) 
					this.actualizaCobranza(extendida.getId());
			}

			if (debugging) log.info("Terminando ... ");
			respuesta.getBody().addValor("factura", extendida);
			respuesta.getErrores().setCodigoError(timbre33.getErrorCodigo());
			respuesta.getErrores().setDescError(timbre33.getErrorDescripcion());
		} catch (Exception e) { 
			log.error("Ocurrio un problema al intentar timbrar la Factura indicada: " + extendida.getId(), e);
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public Respuesta consultarEstatus(long idFactura) throws Exception {
		Respuesta respuesta = null;
		FactElectv33 timbre33 = null;
		FacturaTimbre timbre = null;
		Factura factura = null;
		String total = "";
		String uuid = "";
		// --------------------------------------------------------
		Comprobante comprobante = null;
		String xmlBase = "";
		String xmlTimbre = "";
		
		try {
			respuesta = new Respuesta();
			factura = this.findById(idFactura);
			if (factura == null || factura.getId() == null || factura.getId() <= 0L) {
				return respuesta;
			}

			timbre = this.ifzTimbres.findById(factura.getIdTimbre());
			if (timbre == null || timbre.getId() == null || timbre.getId() <= 0L) {
				this.ifzTimbres.setInfoSesion(this.infoSesion);
				timbre = this.ifzTimbres.comprobarTimbre(factura.getSerie(), factura.getFolio());
				if (timbre == null) {
					timbre = new FacturaTimbre();
					timbre.setId(0L);
					timbre.setSerie(factura.getSerie());
					timbre.setFolio(factura.getFolio());
					timbre.setIdEmpresa(this.infoSesion.getEmpresa().getId());
					timbre.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
					timbre.setFechaCreacion(Calendar.getInstance().getTime());
				}
				
				timbre.setTimbrado(0);
				timbre.setTimbradoPor(this.infoSesion.getAcceso().getUsuario().getId());
				timbre.setVersion("3.3");
				timbre.setUsoCfdi("");
				timbre.setCfdiRelacionado(0);
				timbre.setCfdiRelacionadoUuid("");
				timbre.setCfdiTipoRelacion(""); 
				timbre.setPruebas(0);
				timbre.setRfcReceptor(factura.getRfc());
				timbre.setRfcEmisor(this.infoSesion.getEmpresa().getRfc());
				
				// Guardamos Timbre
				this.ifzTimbres.setInfoSesion(this.infoSesion);
				timbre = this.ifzTimbres.saveOrUpdate(timbre);
				
				// Asignamos Timbre a la Factura
				factura.setIdTimbre(timbre.getId());
				this.update(factura);
			}

			total = ((new DecimalFormat("0.00")).format(factura.getTotal()));
			uuid = (timbre.getUuid() != null && ! "".equals(timbre.getUuid().trim()) ? timbre.getUuid() : factura.getUuid());
			uuid = (uuid != null && ! "".equals(uuid.trim()) ? uuid.trim() : "");
			if ("".equals(uuid.trim())) {
				return respuesta;
			}
			
			timbre33 = new FactElectv33(this.infoSesion);
			timbre33.setDebugging(false);
			timbre33.setTesting(false); 
			timbre33.setDigestMethodSHA1();
			timbre33.setSoapRequestVersion11();
			
			respuesta = timbre33.estatus(timbre.getRfcEmisor(), timbre.getRfcReceptor(), total, uuid, timbre.getIdEmpresa());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				return respuesta;
			}

			respuesta = timbre33.acuseCFDI(uuid, (new SimpleDateFormat("yyyy-MM-dd")).format(factura.getFechaEmision()), timbre.getIdEmpresa());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				return respuesta;
			}

			xmlBase = (respuesta.getBody().getValor("xml") != null ? respuesta.getBody().getValor("xml").toString() : "");
			xmlTimbre = (respuesta.getBody().getValor("xmlTimbrado") != null ? respuesta.getBody().getValor("xmlTimbrado").toString() : "");
			comprobante = (Comprobante) respuesta.getBody().getValor("comprobante");
			timbre.setTimbrado(1);
			timbre.setVersion(comprobante.getVersion());
			timbre.setUsoCfdi(comprobante.getReceptor().getUsoCFDI().value());
			timbre.setXml(xmlBase.getBytes());
			timbre.setFechaTimbrado((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(respuesta.getBody().getValor("fecha_timbrado").toString()));
			timbre.setVersionTimbre(respuesta.getBody().getValor("version_timbre").toString());
			timbre.setUuid(respuesta.getBody().getValor("uuid").toString());
			timbre.setSelloSat(respuesta.getBody().getValor("sello_sat").toString());
			timbre.setCertificadoSat(respuesta.getBody().getValor("no_certificado_sat").toString());
			timbre.setCfdi(xmlTimbre.getBytes());
			timbre.setTimbre(xmlTimbre.getBytes());
			
			// Guardamos Timbre
			this.ifzTimbres.setInfoSesion(this.infoSesion);
			timbre = this.ifzTimbres.saveOrUpdate(timbre);
			
			// Guardamos Factura
			factura.setIdTimbre(timbre.getId());
			this.update(factura);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
			respuesta.getBody().addValor("idFactura", idFactura);
			respuesta.getBody().addValor("factura", factura);
			respuesta.getBody().addValor("timbre", timbre);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el CFDI de la Factura indicada: " + idFactura, e);
			throw e;
		}
		
		return respuesta;
	}
	
	@Override
	public void cobranza(Long idFactura) throws Exception {
		Factura factura = null;
		
		try {
			factura = this.findById(idFactura);
			if (factura == null || factura.getId() == null || factura.getId() <= 0L)
				return;
			
			if (factura.getTimbrado() != 1)
				return;
			if (! "I".equals(factura.getTipoComprobante()))
				return;
			
			if ("PUE".equals(factura.getMetodoPago()) && factura.getSaldo().doubleValue() > 0) 
				this.generarPago(idFactura);
			
			if ("PPD".equals(factura.getMetodoPago())) 
				this.actualizaCobranza(idFactura);
		} catch (Exception e) { 
			log.error("Ocurrio un problema al lanzar evento para la actualizacion de Cobranza con la Factura indicada: " + idFactura, e);
			throw e;
		}
	}

	@Override
	public void cobranzaUbicacionPrevia(Long idFactura) throws Exception {
		Factura factura = null;
		// ----------------------------------------------------
		MensajeTopic msgTopic = null;
		String comando = "";
		// ----------------------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (idFactura == null || idFactura <= 0L)
				idFactura = 0L;
			factura = this.findById(idFactura);
			if (factura == null || factura.getId() == null || factura.getId() <= 0L)
				return;
			
			// Generamos evento para Factura
			target = factura.getId().toString();
			referencia = String.valueOf(factura.getIdObra());

			// lanzamos evento
			log.info("\n\n>>>>> Evento para actualizar Cobranza... Factura PPD\n");
			msgTopic = new MensajeTopic(TopicEventosGP.COBRANZA_UBICACION_PREVIA, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/GP\n\n" + comando + "\n\n", e);
		}
	}

	@Override
	public BigDecimal pagado(long idFactura) {
		try {
			return this.calcularLiquidado(idFactura, null, null);
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.pagado(idFactura)", e);
		}
		
		return BigDecimal.ZERO;
		/*BigDecimal pagado = BigDecimal.ZERO;
		
		try {
			pagado = this.pagado(this.findById(idFactura));
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.pagado(idFactura)", e);
			pagado = BigDecimal.ZERO;
		}
		
		return pagado;*/
	}

	@Override
	public BigDecimal pagado(Factura factura) {
		try {
			return this.calcularLiquidado(factura.getId(), null, null);
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.pagado(factura)", e);
		}
		
		return BigDecimal.ZERO;
		/*List<FacturaPagos> pagos = null;
		// ------------------------------------
		BigDecimal pagado = BigDecimal.ZERO;
		double monto = 0;
		
		try {
			if (factura == null || factura.getId() == null || factura.getId() <= 0L)
				return BigDecimal.ZERO;

			// Recupero los pago realizados a la factura
			this.ifzPagos.setInfoSesion(this.infoSesion);
			pagos = this.ifzPagos.findAll(factura.getId());
			if (pagos != null && ! pagos.isEmpty()) {
				for (FacturaPagos var : pagos)
					monto += var.getPago().doubleValue();
				pagado = new BigDecimal(monto);
			} 
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.pagado(factura)", e);
		}
		
		return pagado;*/
	}

	@Override
	public BigDecimal notasCredito(long idFactura) {
		try {
			return this.calcularEgresos(idFactura, null, null);
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.saldo(factura)", e);
		}
		
		return BigDecimal.ZERO;
		/*BigDecimal notasCredito = BigDecimal.ZERO;
		
		try {
			notasCredito = this.notasCredito(this.findById(idFactura));
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.notasCredito(idFactura)", e);
			notasCredito = BigDecimal.ZERO;
		}
		
		return notasCredito;*/
	}

	@Override
	public BigDecimal notasCredito(Factura factura) {
		try {
			return this.calcularEgresos(factura.getId(), null, null);
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.saldo(factura)", e);
		}
		
		return BigDecimal.ZERO;
		/*List<Factura> listNotasCredito = null;
		// ------------------------------------
		BigDecimal notasCredito = BigDecimal.ZERO;
		double monto = 0;
		
		try {
			if (factura == null || factura.getId() == null || factura.getId() <= 0L)
				return BigDecimal.ZERO;
			
			// Recupera las notas de credito
			listNotasCredito = this.findNotasCreditoByFactura(factura.getId());
			if (listNotasCredito != null && ! listNotasCredito.isEmpty()) {
				for (Factura var : listNotasCredito)
					monto += var.getTotal().doubleValue();
				notasCredito = new BigDecimal(monto);
			}
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.notasCredito(factura)", e);
		}
		
		return notasCredito;*/
	}

	@Override
	public BigDecimal saldo(long idFactura) {
		try {
			return this.calcularSaldo(idFactura, null, null);
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.saldo(factura)", e);
		}
		
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal saldo(Factura factura) {
		try {
			return this.calcularSaldo(factura.getId(), null, null);
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.saldo(factura)", e);
		}
		
		return BigDecimal.ZERO;
		/*BigDecimal saldoOriginal = BigDecimal.ZERO;
		BigDecimal facturado = BigDecimal.ZERO;
		BigDecimal pagado = BigDecimal.ZERO;
		BigDecimal saldo = BigDecimal.ZERO;
		double monto = 0;
		double limiteFacturaPagada = 0;
		
		try {
			if (factura == null)
				return BigDecimal.ZERO;
			
			if (factura.getId() == null || factura.getId() <= 0L)
				return factura.getTotal();
			facturado = factura.getTotal();
			saldoOriginal = factura.getSaldo();
			
			// Rango permitido para indicar factura pagada
			limiteFacturaPagada = obtenerLimiteFacturaPagada();

			// Recupero los pago realizados a la factura
			this.ifzPagos.setInfoSesion(this.infoSesion);
			pagado = this.ifzPagos.findLiquidado(factura.getId(), null, null);
			
			// Calculamos saldo
			monto = facturado.doubleValue() - pagado.doubleValue();
			if (facturado.doubleValue() > limiteFacturaPagada && monto < limiteFacturaPagada)
				monto = 0;
			saldo = new BigDecimal(monto);
			
			// Actualizamos saldo en factura si corresponde
			if (saldo != saldoOriginal) {
				factura.setSaldo(saldo);
				this.update(factura);
			}
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.saldo(factura)", e);
		}
		
		return saldo;*/
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public BigDecimal recalculaSaldo(long idFactura, BigDecimal total) {
		BigDecimal pagado = BigDecimal.ZERO;
		BigDecimal saldo = BigDecimal.ZERO;
		double monto = 0;
		double facturado = 0;
		double limiteFacturaPagada = 0;
		
		try {
			if (idFactura <= 0L)
				return total;
			
			// Rango permitido para indicar factura pagada
			limiteFacturaPagada = obtenerLimiteFacturaPagada();
			facturado = total.doubleValue();

			// Recupero los pago realizados a la factura
			this.ifzPagos.setInfoSesion(this.infoSesion);
			pagado = this.ifzPagos.findLiquidado(idFactura, null, null);
			
			// Calculamos saldo
			monto = total.doubleValue() - pagado.doubleValue();
			if (facturado > limiteFacturaPagada && monto < limiteFacturaPagada)
				monto = 0;
			saldo = new BigDecimal(monto);
		} catch (Exception e) {
			log.error("Error en el método Logica_CuentasPorCobrar.FacturaFac.recalculaSaldo(factura, total)", e);
		}
		
		return saldo;
	}

	@Override
	public Comprobante generarComprobante(long idFactura) throws Exception {
		FacturaExt factura = null;
		List<FacturaDetalleExt> detalles = null;
		List<FacturaDetalleImpuestoExt> listImpuestos = null;
		EmpresaCertificado empCertificado = null;
		XMLGregorianCalendar date = null;
		DateFormat format = null;
		String FORMATER = "yyyy-MM-dd'T'HH:mm:ss";
		String sello = "";
		// -------------------------------------------------------------------------------------
		double totalImpuestosTrasladados = 0;
		double totalImpuestosRetenidos = 0;
		double porcentaje = 0;
		// -------------------------------------------------------------------------------------
		String serieFacturacion = "";
		String folioFacturacion = "";
		boolean actualizarFacturacion = false;
		// -------------------------------------------------------------------------------------
		String key = "";
		HashMap<String, Traslado> mapTraslados = null;
		HashMap<String, Retencion> mapRetenciones = null;
		// -------------------------------------------------------------------------------------
		Comprobante comprobante = null;
		Emisor emisor = null;
		Receptor receptor = null;
		Conceptos conceptos = null;
		Concepto concepto = null;
		Impuestos impuestos = null;
		Traslado traslado = null;
		Retencion retencion = null;
		// -------------------------------------------------------------------------------------
		Comprobante.Conceptos.Concepto.Impuestos conceptoImpuestos = null;
		Comprobante.Conceptos.Concepto.Impuestos.Traslados conceptoTraslados = null;
		Comprobante.Conceptos.Concepto.Impuestos.Retenciones conceptoRetenciones = null;
		Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado conceptoTraslado = null;
		Comprobante.Conceptos.Concepto.Impuestos.Retenciones.Retencion conceptoRetencion = null;
		
		try {
			// Validaciones
			// -----------------------------------------------------------------------------------------------------
			factura = this.findExtById(idFactura);
			if (factura == null || factura.getId() == null || factura.getId() <= 0L)
				return null;
			
			detalles = this.ifzFacturaDetalles.findAllExt(idFactura);
			if (detalles == null || detalles.isEmpty())
				return null;
			
			serieFacturacion = factura.getSerie();
			if (serieFacturacion == null || "".equals(serieFacturacion.trim())) {
				serieFacturacion = factura.getIdEmpresa().getSerieComplementoPago();
				factura.setSerie(serieFacturacion);
				actualizarFacturacion = true;
			}

			folioFacturacion = factura.getFolio();
			if (folioFacturacion == null || "".equals(folioFacturacion.trim())) {
				folioFacturacion = this.folioFacturacion(factura.getIdSucursal());
				factura.setFolio(folioFacturacion);
				actualizarFacturacion = true;
			}
			
			if ((serieFacturacion == null || "".equals(serieFacturacion.trim())) || (folioFacturacion == null || "".equals(folioFacturacion.trim()))) {
				log.error("Ocurrio un problema al generar el Comprobante.\nNo tiene asignado Serie o Folio para Facturacion");
				return null;
			}
			
			// Actualizamos Serie y Folio si corresponde
			if (actualizarFacturacion) 
				this.update(factura);
			
			// Recuperamos certificado de la empresa cargada
			// -----------------------------------------------------------------------------------------------------
			this.ifzEmpCertificado.setInfoSesion(this.infoSesion);
			empCertificado = this.ifzEmpCertificado.findByEmpresa(factura.getIdEmpresa().getId());
			if (empCertificado == null || empCertificado.getId() == null || empCertificado.getId() <= 0L){
				log.error("Ocurrio un problema al generar el Comprobante.\nNo se pudo recuperar el certificado para Facturacion");
				return null;
			}
			
			mapTraslados = new HashMap<String, Comprobante.Impuestos.Traslados.Traslado>();
			mapRetenciones = new HashMap<String, Comprobante.Impuestos.Retenciones.Retencion>();

			// Emisor
			// -----------------------------------------------------------------------------------------------------
			emisor = new Emisor();
			emisor.setRfc(factura.getIdEmpresa().getRfc());
			emisor.setNombre(factura.getIdEmpresa().getEmpresa());
			emisor.setRegimenFiscal(factura.getIdEmpresa().getCodigoRegimenFiscal());
			
			// Receptor
			// -----------------------------------------------------------------------------------------------------
			receptor = new Receptor();
			receptor.setRfc(factura.getRfc());
			receptor.setNombre(factura.getCliente());
			receptor.setUsoCFDI(usoCFDI(factura.getUsoCfdi())); // Por Definir

			// Conceptos
			// -----------------------------------------------------------------------------------------------------
			conceptos = new Conceptos();
			for (FacturaDetalleExt detalle : detalles) {
				concepto = new Concepto();
				concepto.setNoIdentificacion(detalle.getId().toString());
				concepto.setClaveProdServ(detalle.getClaveSat().trim());
				concepto.setClaveUnidad(detalle.getIdUnidadMedida().getValor().trim());
				concepto.setDescripcion(detalle.getIdConcepto().getDescripcion().trim());
				concepto.setCantidad(detalle.getCantidad());
				concepto.setValorUnitario(detalle.getCosto());
				concepto.setImporte(detalle.getImporte());
				
				// Impuestos del Concepto
				listImpuestos = detalle.getListImpuestos(); 
				if (listImpuestos != null && ! listImpuestos.isEmpty()) {
					conceptoImpuestos = new Comprobante.Conceptos.Concepto.Impuestos();
					for (FacturaDetalleImpuestoExt imp : listImpuestos) {
						porcentaje = Math.abs(imp.getTasa());
						if (porcentaje > 1)
							porcentaje = (porcentaje / 100);
						porcentaje = Double.parseDouble((new DecimalFormat("0.000000")).format(porcentaje));
						key = imp.getIdImpuesto().getAtributo4() + "-" + (new DecimalFormat("0.000000")).format(porcentaje);
						
						if ("DE".equals(imp.getIdImpuesto().getTipoCuenta())) {
							totalImpuestosTrasladados += imp.getImporte().doubleValue();
							conceptoTraslado = new net.giro.cxc.FEv33.Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado();
							conceptoTraslado.setImpuesto(imp.getIdImpuesto().getAtributo4());
							conceptoTraslado.setTasaOCuota(new BigDecimal(porcentaje));
							conceptoTraslado.setTipoFactor(tipoFactor("Tasa"));
							conceptoTraslado.setBase(imp.getBase()); // conceptoTraslado.setBase(detalle.getImporte());
							conceptoTraslado.setImporte(imp.getImporte());

							if (mapTraslados.containsKey(key)) 
								traslado = mapTraslados.get(key);
							else
								traslado = new net.giro.cxc.FEv33.Comprobante.Impuestos.Traslados.Traslado(imp.getIdImpuesto().getAtributo4(), tipoFactor("Tasa"), new BigDecimal(porcentaje), BigDecimal.ZERO);
							traslado.setImporte(traslado.getImporte().add(imp.getImporte()));
							mapTraslados.put(key, traslado);
						} else {
							totalImpuestosRetenidos += imp.getImporte().doubleValue();
							conceptoRetencion = new net.giro.cxc.FEv33.Comprobante.Conceptos.Concepto.Impuestos.Retenciones.Retencion();
							conceptoRetencion.setImpuesto(imp.getIdImpuesto().getAtributo4());
							conceptoRetencion.setTasaOCuota(new BigDecimal(porcentaje));
							conceptoRetencion.setTipoFactor(tipoFactor("Tasa"));
							conceptoRetencion.setBase(imp.getBase()); // conceptoRetencion.setBase(detalle.getImporte());
							conceptoRetencion.setImporte(imp.getImporte());

							if (mapRetenciones.containsKey(key)) 
								retencion = mapRetenciones.get(key);
							else
								retencion = new net.giro.cxc.FEv33.Comprobante.Impuestos.Retenciones.Retencion(imp.getIdImpuesto().getAtributo4(), BigDecimal.ZERO);
							retencion.setImporte(retencion.getImporte().add(imp.getImporte()));
							mapRetenciones.put(key, retencion);
						}
						
						if (conceptoTraslado != null) {
							if (conceptoTraslados == null)
								conceptoTraslados = new net.giro.cxc.FEv33.Comprobante.Conceptos.Concepto.Impuestos.Traslados();
							conceptoTraslados.getTraslado().add(conceptoTraslado);
							conceptoTraslado = null;
						}
						
						if (conceptoRetencion != null) {
							if (conceptoRetenciones == null)
								conceptoRetenciones = new net.giro.cxc.FEv33.Comprobante.Conceptos.Concepto.Impuestos.Retenciones();
							conceptoRetenciones.getRetencion().add(conceptoRetencion);
							conceptoRetencion = null;
						}
					}

					if (conceptoTraslados != null)
						conceptoImpuestos.setTraslados(conceptoTraslados);
					if (conceptoRetenciones != null)
						conceptoImpuestos.setRetenciones(conceptoRetenciones);
				}
				
				if (conceptoImpuestos != null)
					concepto.setImpuestos(conceptoImpuestos);
				conceptos.getConcepto().add(concepto);
				conceptoImpuestos = null;
				concepto = null;
			}	
			
			// Impuestos
			// -----------------------------------------------------------------------------------------------------
			impuestos = new Impuestos();
			// Traslados
			if (! mapTraslados.isEmpty()) {
				for (Entry<String, Traslado> item : mapTraslados.entrySet())
					impuestos.getTraslados().getTraslado().add(item.getValue());
				impuestos.setTotalImpuestosTrasladados(new BigDecimal(totalImpuestosTrasladados));
			}
			// Retenciones
			if (! mapRetenciones.isEmpty()) {
				for (Entry<String, Retencion> item : mapRetenciones.entrySet())
					impuestos.getRetenciones().getRetencion().add(item.getValue());
				impuestos.setTotalImpuestosRetenidos(new BigDecimal(totalImpuestosRetenidos));
			}
			
			// Fecha emision del Comprobante (Complemento Pago)
			format = new SimpleDateFormat(FORMATER);
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(factura.getFechaEmision()));
			sello = (factura.getSello() != null ? new String(factura.getSello()) : "");

			// Generamos comprobante
			comprobante = new Comprobante();
			comprobante.setFecha(date);
			comprobante.setVersion(factura.getVersion());
			comprobante.setTipoDeComprobante(tipoDeComprobante(factura.getTipoComprobante()));
			comprobante.setCertificado(new String(Base64.encodeBase64(empCertificado.getCertificadoData())));
			comprobante.setNoCertificado(empCertificado.getNoCertificado());
			comprobante.setSello(sello);
			comprobante.setLugarExpedicion(factura.getIdSucursal().getColonia().getCp());
			comprobante.setSerie(factura.getSerie()); 
			comprobante.setFolio(factura.getFolio()); 
			comprobante.setCondicionesDePago(factura.getCondicionesPago().trim());
			comprobante.setMetodoPago(metodoPago(factura.getIdMetodoPago().getValor()));
			comprobante.setFormaPago(claveFormaPago(factura.getIdFormaPago()));
			if (factura.getDescuento().doubleValue() > 0) 
				comprobante.setDescuento(factura.getDescuento());
			comprobante.setMoneda(moneda(factura.getAbreviaturaMoneda()));
			if (factura.getTipoCambio() > 1)
				comprobante.setTipoCambio(new BigDecimal(factura.getTipoCambio()));
			comprobante.setSubTotal(factura.getSubtotal());
			comprobante.setTotal(factura.getTotal());
			comprobante.setEmisor(emisor);
			comprobante.setReceptor(receptor);
			comprobante.setConceptos(conceptos);
			comprobante.setImpuestos(impuestos);
		} catch (Exception e) {
			log.error("Ocurrio un problema Logica_CuentasPorPagar.PagosGastosFac.generarComprobante(long idFacturaPago)", e);
			throw e;
		}
		
		return comprobante;
	}

	@Override
	public String formarXML(long idFactura) throws Exception {
		return formarXML(generarComprobante(idFactura));
	}

	@Override
	public String formarXML(Comprobante comprobante) throws Exception {
		FactElectv33 cfdi = null;
		String xml = null;
		
		try {
			if (comprobante == null || this.infoSesion == null)
				return null;
			cfdi = new FactElectv33(this.infoSesion);
			xml = cfdi.formarXML(comprobante);
		} catch (Exception e) {
			log.error("303L - No se pudo general el XML (CFDI)", e);
			return null;
		}
		
		return xml;
	}

	@Override
	public LinkedHashMap<String, String> auditoria(long idFactura) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		LinkedHashMap<String, String> auditoria = null;
		FacturaTimbre timbre = null;
		Factura factura = null;
		String descripcion = "";
		
		try {
			factura = this.findById(idFactura);
			if (factura == null || factura.getId() == null || factura.getId() <= 0L) {
				log.error("La Factura indicada no existe: " + idFactura);
				return null;
			}
			
			timbre = this.ifzTimbres.findById(factura.getIdTimbre());
			formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			auditoria = new LinkedHashMap<String, String>();
			
			descripcion = getUsername(factura.getCreadoPor());
			auditoria.put("Creado", (descripcion != null && ! "".equals(descripcion.trim()) ? formatter.format(factura.getFechaCreacion()) + " - " + descripcion : ""));

			auditoria.put("Timbrado", "");
			auditoria.put("Cancelado", "");
			if (timbre != null) {
				descripcion = getUsername(timbre.getTimbradoPor());
				auditoria.put("Timbrado", (descripcion != null && ! "".equals(descripcion.trim()) ? formatter.format(timbre.getFechaTimbrado()) + " - " + descripcion : ""));
	
				descripcion = getUsername(timbre.getCanceladoPor());
				auditoria.put("Cancelado", (descripcion != null && ! "".equals(descripcion.trim()) ? formatter.format(timbre.getFechaCancelacion()) + " - " + descripcion : ""));
				if (timbre.getCanceladoPor() > 0L) {
					descripcion = timbre.getMotivoCancelacion();
					auditoria.put("Motivo Cancelacion", (descripcion != null && ! "".equals(descripcion.trim()) ? descripcion : ""));
				}
			}

			descripcion = getUsername(factura.getModificadoPor());
			auditoria.put("Ultima Modificacion", (descripcion != null && ! "".equals(descripcion.trim()) ? formatter.format(factura.getFechaModificacion()) + " - " + descripcion : ""));
		} catch (Exception e) {
			log.error("No se pudo recuperar la Auditoria de la Factura indicada: " + idFactura, e);
			auditoria = null;
		}
		
		return auditoria;
	}

	// ------------------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// ------------------------------------------------------------------------------------------------------
	
	@Override
	public Factura convertir(FacturaExt pojoFactura) throws Exception {
		return this.convertidor.FacturaExtToFactura(pojoFactura);
	}
	
	@Override
	public FacturaExt convertir(Factura pojoFactura) throws Exception {
		return this.convertidor.FacturaToFacturaExt(pojoFactura);
	}

	// ------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------

	@Override
	public Long save(FacturaExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.FacturaExtToFactura(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaExt> saveExt(List<FacturaExt> extendidos) throws Exception {
		List<Factura> entities = new ArrayList<Factura>();
		
		try {
			for (FacturaExt extendido : extendidos)
				entities.add(this.convertidor.FacturaExtToFactura(extendido));
			entities = this.save(entities);
			extendidos.clear();
			for (Factura entity : entities) 
				extendidos.add(this.convertidor.FacturaToFacturaExt(entity));
		} catch (Exception e) {
			log.error("Ocurrio un problema al guardar la lista extendida de Facturas", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public void update(FacturaExt extendido) throws Exception {
		try {
			this.update(this.convertidor.FacturaExtToFactura(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public FacturaExt findExtById(long idFactura) throws Exception {
		FacturaExt extendido = new FacturaExt();
		Factura factura = null;
		
		try {
			factura = this.findById(idFactura);
			if (factura != null)
				extendido = this.convertir(factura);
		} catch (Exception re) { 
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findExtById(idFactura)\n", re);
			throw re;
		}
		
		return extendido;
	}
	
	@Override
	public Respuesta cancelarFactura(FacturaExt extendido, String motivoCancelacion, boolean debugging, boolean testing) throws Exception {
		try {
			return this.cancelarFactura(this.convertir(extendido), motivoCancelacion, debugging, testing);
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.cancelarFactura(extendido, usuarioId)\n", e);
			throw e;
		}
	}

	@Override
	public Respuesta provisionar(FacturaExt extendido, double montoProvision, long usuarioId) throws Exception {
		Respuesta respuesta = new Respuesta();
		Factura pojoFactura = null;
		
		try {
			pojoFactura = this.convertir(extendido);
			respuesta = this.provisionar(pojoFactura, montoProvision, usuarioId);
			pojoFactura = (Factura) respuesta.getBody().getValor("factura");
			respuesta.getBody().addValor("factura", this.convertir(pojoFactura));
			return respuesta;
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.provisionar(extendido, montoProvision, usuarioId)\n", e);
			throw e;
		}
	}

	// ------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------

	private Long getIdUsuario() {
		if (this.infoSesion != null) 
			this.idUsuario = this.infoSesion.getAcceso().getUsuario().getId();
		this.idUsuario = (this.idUsuario > 0L ? this.idUsuario : 1L);
		return this.idUsuario;
	}
	
	private Long getIdEmpresa() {
		if (this.infoSesion != null) 
			this.idEmpresa = this.infoSesion.getEmpresa().getId();
		this.idEmpresa = (this.idEmpresa > 0L ? this.idEmpresa : 1L);
		return this.idEmpresa;
	}
	
	private Long getCodigoEmpresa() {
		if (this.infoSesion != null) 
			this.codigoEmpresa = this.infoSesion.getEmpresa().getCodigo();
		this.codigoEmpresa = (this.codigoEmpresa > 0L ? this.codigoEmpresa : 1L);	
		return this.codigoEmpresa;
	}

	private String getEmpresa() {
		if (this.infoSesion != null)
			return this.infoSesion.getEmpresa().getEmpresa();
		return "";
	}

	private String valueToString(Object value) {
		return (value != null ? value.toString().trim() : "");
	}

	private double valueToDouble(Object value) {
		return ((! "".equals(valueToString(value))) ? Double.valueOf(valueToString(value)) : 0);
	}
	
	private int valueToInteger(Object value) {
		return ((! "".equals(valueToString(value))) ? Integer.valueOf(valueToString(value)) : 0);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Respuesta transaccionProvision(Factura entity, double montoProvision, Long idEmpresa) throws Exception {
		Respuesta respuesta = new Respuesta();
		Long idMensaje = 0L;
		Long idTransaccion = 0L;
		Long idFormaPago = 0L;
		Long idMoneda = 0L;
		Long idCliente = 0L;
		String descripcionOperacion = "";
		String descripcionFormaPago = "";
		String descMoneda = "";
		String referencia = "";
		String referenciaFormaPago = "";
		String nombreCliente = "";
		String tipoPersona = "";
		String tracking = "";
		Obra pojoObra = null;
		FormasPagos pojoFormaPago = null;
		boolean facturaCredito = false;
		
		try {
			if (entity == null) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No indico Factura");
				return respuesta;
			}
			
			if (idEmpresa == null || idEmpresa <= 0L)
				idEmpresa = 1L;

			this.ifzObras.setInfoSesion(this.infoSesion);
			pojoObra = this.ifzObras.findById(entity.getIdObra());
			if (pojoObra == null) {
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se pudo obtener los datos del Cliente");
				return respuesta;
			}

			// Forma de Pago y Referencia
			idFormaPago = entity.getIdMetodoPago();
			this.ifzFormasPagos.setInfoSesion(this.infoSesion);
			pojoFormaPago = this.ifzFormasPagos.findById(idFormaPago);
			if (pojoFormaPago == null) 
				pojoFormaPago = new FormasPagos();
			
			idCliente = pojoObra.getIdCliente();
			nombreCliente = pojoObra.getNombreCliente();
			tipoPersona = pojoObra.getTipoCliente();
			idMoneda = entity.getIdMoneda();
			descMoneda = entity.getDescripcionMoneda();
			descripcionFormaPago = pojoFormaPago.getFormaPago();
			referenciaFormaPago = "";
			referencia = "";
			
			if ("C".equals(entity.getTipo())) 
				facturaCredito = true;
			
			// Determinamos Transaccion
			tracking = "Transaccion 1014 disparada desde FacturaFac. Factura " + ((facturaCredito) ? " Credito " : " Contado ") + entity.getId();
			idTransaccion = 1014L; 
			descripcionOperacion = "Factura " + ((facturaCredito) ? "Credito" : "Contado");


			// Registramos el mensaje
			idMensaje = registraMensaje(idTransaccion, entity.getId(), descripcionOperacion, idCliente, nombreCliente, tipoPersona, referencia, 
					idMoneda, descMoneda, entity.getTotal(), idFormaPago, descripcionFormaPago, referenciaFormaPago, entity.getIdSucursal(), entity.getNombreSucursal(), 
					entity.getCreadoPor(), entity.getFechaEmision(), entity.getCreadoPor(), entity.getFechaCreacion());
			log.info("Tracking ID " + idMensaje + " : " + tracking);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("mensaje", idMensaje);
			respuesta.getErrores().setDescError("Factura Provisionada");
		} catch (Exception e) {
			log.error("Error Logica_CuentasPorPagar.PagosGastosFac.enviarTransaccion(PagosGastosEntity, idEmpresa)", e);
			respuesta.getErrores().setCodigoError(-1L);
			respuesta.getErrores().setDescError("Error. No se pudo provisionar la factura");
			respuesta.getBody().addValor("exception", e);
		} 
		
		return respuesta;
	}
	
	private Respuesta transacconCancelacionProvision(Factura entity, Long usuarioId, Long idEmpresa) {
		//TODO: Supongo debe causar transaccion 1031 o 1021 dependiendo del mes
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Respuesta transaccionCancelacionFactura(Factura entity, Long usuarioId, Long idEmpresa) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		Respuesta respuesta = new Respuesta();
		Long idMensaje = 0L;
		Long idTransaccion = 0L;
		Long idFormaPago = 0L;
		Long idMoneda = 0L;
		Long idCliente = 0L;
		String descripcionOperacion = "";
		String descripcionFormaPago = "";
		String descMoneda = "";
		String referencia = "";
		String referenciaFormaPago = "";
		String nombreCliente = "";
		String tipoPersona = "";
		String tracking = "";
		Obra pojoObra = null;
		FormasPagos pojoFormaPago = null;
		boolean facturaCredito = false;
		boolean facturaProvisionada = false;
		
		try {
			if (entity == null) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No indico Factura");
				return respuesta;
			}
			
			if (idEmpresa == null || idEmpresa <= 0L)
				idEmpresa = 1L;

			this.ifzObras.setInfoSesion(this.infoSesion);
			pojoObra = this.ifzObras.findById(entity.getIdObra());
			if (pojoObra == null) {
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se pudo obtener los datos del Cliente");
				return respuesta;
			}

			// Forma de Pago
			idFormaPago = entity.getIdFormaPago();
			this.ifzFormasPagos.setInfoSesion(this.infoSesion);
			pojoFormaPago = this.ifzFormasPagos.findById(idFormaPago);
			if (pojoFormaPago == null) 
				pojoFormaPago = new FormasPagos();
			
			idCliente = pojoObra.getIdCliente();
			nombreCliente = pojoObra.getNombreCliente();
			tipoPersona = pojoObra.getTipoCliente();
			idMoneda = entity.getIdMoneda();
			descMoneda = entity.getDescripcionMoneda();
			descripcionFormaPago = pojoFormaPago.getClaveSat() + " - " + pojoFormaPago.getFormaPago();
			referenciaFormaPago = "";
			referencia = "";
			
			if ("C".equals(entity.getTipo())) 
				facturaCredito = true;
			
			// Determinamos Transaccion
			if (Integer.parseInt(formatter.format(entity.getFechaEmision())) < Integer.parseInt(formatter.format(Calendar.getInstance().getTime()))) {
				tracking = "Transaccion 1031 (Cancelacion de Facturas de Ingresos de meses anteriores) disparada desde FacturaFac. Factura " + ((facturaCredito) ? " Credito " : " Contado ") + entity.getId();
				idTransaccion = 1031L; 
				descripcionOperacion = "Cancelacion de Facturas de Ingresos de meses anteriores";
			} else {
				tracking = "Transaccion 1021 (Cancelacion de Factura de Ingresos) disparada desde FacturaFac. Factura " + ((facturaCredito) ? " Credito " : " Contado ") + entity.getId();
				idTransaccion = 1021L;
				descripcionOperacion = "Cancelacion de Factura de Ingresos";
			}
			
			// Registramos el mensaje
			idMensaje = registraMensaje(idTransaccion, entity.getId(), descripcionOperacion, idCliente, nombreCliente, tipoPersona, referencia, 
					idMoneda, descMoneda, entity.getTotal(), idFormaPago, descripcionFormaPago, referenciaFormaPago, entity.getIdSucursal(), entity.getNombreSucursal(), 
					entity.getCreadoPor(), entity.getFechaEmision(), entity.getCreadoPor(), entity.getFechaCreacion());
			log.info("Tracking ID " + idMensaje + " : " + tracking);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("mensaje", idMensaje);
			respuesta.getErrores().setDescError("Factura enviada para Cancelacion");
			
			if (facturaProvisionada)
				transacconCancelacionProvision(entity, usuarioId, idEmpresa);
		} catch (Exception e) {
			log.error("Error Logica_CuentasPorPagar.PagosGastosFac.transaccionCancelacionFactura(Factura, idEmpresa)", e);
			respuesta.getErrores().setCodigoError(-1L);
			respuesta.getErrores().setDescError("Error. No se pudo lanzar la Transaccion para cancelar la factura");
			respuesta.getBody().addValor("exception", e);
		} 
		
		return respuesta;
	}
	
	/**
	 * Genera el mensaje (evento) para el contabilizador
	 * @param codigoTransaccion
	 * @param idOperacion
	 * @param descripcionOperacion
	 * @param idPersonaReferencia
	 * @param nombrePersonaReferencia
	 * @param tipoPersona
	 * @param referencia
	 * @param idMoneda
	 * @param descripcionMoneda
	 * @param importe
	 * @param idFormaPago
	 * @param descripcionFormaPago
	 * @param referenciaFormaPago
	 * @param idSucursal
	 * @param descripcionSucursal
	 * @param idUsuarioCreacionRegistro
	 * @param fechaRegistro
	 * @param creadoPor
	 * @param fechaCreacion
	 * @return
	 */
	private long registraMensaje(Long codigoTransaccion, Long idOperacion, String descripcionOperacion, 
			Long idPersonaReferencia, String nombrePersonaReferencia, String tipoPersona, String referencia, 
			Long idMoneda, String descripcionMoneda, BigDecimal importe, Long idFormaPago, String descripcionFormaPago, String referenciaFormaPago, 
			Long idSucursal, String descripcionSucursal, Long idUsuarioCreacionRegistro, Date fechaRegistro, long creadoPor, Date fechaCreacion) {
		MensajeTransaccion msg = null;
		long resultado = 0L;
		
		try {
			// Generamos el mensaje
			msg = new MensajeTransaccion();
			msg.setIdTransaccion(codigoTransaccion);
			msg.setIdOperacion(idOperacion);
			msg.setDescripcionOperacion(descripcionOperacion);
			msg.setIdPersonaReferencia(idPersonaReferencia);
			msg.setNombrePersonaReferencia(nombrePersonaReferencia);
			msg.setTipoPersona(tipoPersona);
			msg.setReferencia(referencia);
			msg.setIdMoneda(idMoneda);
			msg.setDescripcionMoneda(descripcionMoneda);
			msg.setImporte(importe);
			msg.setIdFormaPago(idFormaPago);
			msg.setDescripcionFormaPago(descripcionFormaPago);
			msg.setReferenciaFormaPago(referenciaFormaPago);
			msg.setIdSucursal(idSucursal);
			msg.setDescripcionSucursal(descripcionSucursal);
			msg.setIdUsuarioCreacionRegistro(idUsuarioCreacionRegistro);
			msg.setFechaRegistro(fechaRegistro);
			msg.setCreadoPor(creadoPor);
			msg.setFechaCreacion(fechaCreacion);
			msg.setIdEmpresa(getIdEmpresa());
			msg.setDescripcionEmpresa(getEmpresa());
			msg.setEstatus(0);
			msg.setAnuladoPor(0L);
			msg.setFechaAnulacion(null);
			// Guardamos el mensaje
			this.ifzMsgTransaccion.setInfoSesion(this.infoSesion);
			resultado = this.ifzMsgTransaccion.save(msg);
		} catch (Exception e) {
			log.error("Ocurrio un problema al registrar el Evento para TRANSACCIONES", e);
			resultado = 0L;
		}
		
		return resultado;
	}
	
	private void generarPago(Long idFactura) {
		MensajeTopic msgTopic = null;
		String comando = "";
		// ----------------------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (idFactura == null || idFactura <= 0L)
				idFactura = 0L;
			if (idFactura <= 0L)
				return;
			
			// Generamos evento para Factura
			target = idFactura.toString();

			// lanzamos evento
			log.info("\n\n>>>>> Evento para generar PAGO... Factura PUE\n");
			msgTopic = new MensajeTopic(TopicEventosCXC.PUE_PAGO, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS\n\n" + comando + "\n\n", e);
		}
	}

	private void actualizaCobranza(Long idFactura) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		String comando = "";
		// ----------------------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		// ----------------------------------------------------
		List<Long> facturas = null;
		
		try {
			if (idFactura == null || idFactura <= 0L)
				idFactura = 0L;
			if (idFactura <= 0L)
				return;
			
			// Generamos evento para Factura
			facturas = new ArrayList<Long>();
			facturas.add(idFactura);
			gson = new Gson();
			atributos = gson.toJson(facturas);

			// lanzamos evento
			log.info("\n\n>>>>> Evento para actualizar Cobranza... Factura PPD\n");
			msgTopic = new MensajeTopic(TopicEventosCXC.COBRANZA, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS\n\n" + comando + "\n\n", e);
		}
	}

	private void cancelacionFactura(Long idFactura) {
		MensajeTopic msgTopic = null;
		String comando = "";
		// ----------------------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (idFactura == null || idFactura <= 0L)
				return;
			
			// Generamos evento para Factura
			target = idFactura.toString();

			// lanzamos evento
			log.info("\n\n>>>>> Evento para generar Solicitud de Cancelacion de Factura: " + idFactura + "\n");
			msgTopic = new MensajeTopic(TopicEventosCXC.CANCELACION_FACTURA, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/CXC\n\n" + comando + "\n\n", e);
		}
	}

	@SuppressWarnings("unchecked")
	private String getUsername(Long idUsuario) {
		List<String> nativeResult = null;
		String queryString = "";
		String resultado = "";
		
		try {
			if (idUsuario != null && idUsuario.longValue() == 1L)
				return "AIR";
			queryString = "select '[' || af || '] ' || ag as usuario from dc8deac2731 where aa = :idUsuario";
			queryString = queryString.replace(":idUsuario", idUsuario.toString());

			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty())
				resultado = nativeResult.get(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar la descripcion de Usuario indicado: " + idUsuario, e);
		}
		
		return resultado;
	}
	
	private Properties getFacturacionProperties() {
		Properties properties = null;

		try {
			properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("facturacion.properties"));
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar cargar el archivo 'facturacion.properties'.", e);
			properties = null;
		}
		
		return properties;
	}
	
	@SuppressWarnings("unchecked")
	private double obtenerLimiteFacturaPagada() {
		List<String> nativeResult = null;
		String queryString = "";
		// -----------------------------
		Properties properties = null;
		String perfilName = "";
		
		try {
			properties = getFacturacionProperties();
			if (! properties.containsKey("perfil.diferencia.factura.pagada"))
				return 0;
			
			perfilName = valueToString(properties.getProperty("perfil.diferencia.factura.pagada"));
			queryString += "select b.ai from d7729f32ba7 a inner join b761110ccfe b on b.af = a.aa where a.af = ':perfilName' and b.ah = :idEmpresa ";
			queryString = queryString.replace(":perfilName", perfilName);
			queryString = queryString.replace(":idEmpresa", getIdEmpresa().toString());
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty())
				return valueToDouble(nativeResult.get(0));
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Perfil indicado: " + perfilName, e);
		}
		
		return 0;
	}
	
	private int obtenerMaximaLongitudFormatoFolio() {
		Properties properties = null;
		
		try {
			properties = getFacturacionProperties();
			if (properties != null && properties.containsKey("MAX_LENGTH_FOLIO_FACTURACION"))
				return valueToInteger(properties.getProperty("MAX_LENGTH_FOLIO_FACTURACION"));
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar la maxima longitud para formato de folio de Facturacion", e);
		}
		
		return 0;
	}
	
	private String folioFacturacion(Sucursal pojoSucursal) {
		Respuesta respuesta = null;
		Long folio = 0L;
		
		try {
			log.info("Obteniendo Folio desde Sucursal: " + pojoSucursal.getId() + " - " + pojoSucursal.getSucursal());
			respuesta = this.ifzSucursales.folioFacturacion(pojoSucursal);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				if (respuesta.getErrores().getCodigoError() == -1) {
					log.info("ERROR 8: Sucursal no existe");
					return "0";
				}
				
				if (respuesta.getErrores().getCodigoError() == -2) {
					log.info("ERROR 9: La Sucursal no tiene asignada Serie para facturacion");
					return "0";
				}
			}
			
			folio = (Long) respuesta.getBody().getValor("folioFacturacion");
		} catch (Exception e) {
			log.info("ERROR INTERNO: Ocurrio un problema al intentar recuperar el Folio para la sucursal seleccionada", e);
			folio = 0L;
		}
		
		return formatoFolio(folio.toString());
	}

	private String formatoFolio(String folio) {
		int maxLongitud = 0;
		
		maxLongitud = obtenerMaximaLongitudFormatoFolio();
		if (maxLongitud > 0)
			folio = String.format("%0" + maxLongitud + "d", folio);
		
		return folio;
	}

	@SuppressWarnings("unchecked")
	private String claveFormaPago(Long idFormaPago) {
		List<String> nativeResult = null;
		String queryString = "";
		String resultado = "";
		
		try {
			queryString = "SELECT clave_sat FROM formas_pagos WHERE forma_pago_id = :idFormaPago";
			queryString = queryString.replace(":idFormaPago", idFormaPago.toString());

			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty())
				resultado = nativeResult.get(0);
			if (resultado == null || "".equals(resultado.trim())) 
				resultado = "99";
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar obtener la clave de Forma de Pago: " + idFormaPago, e);
		}
		
		return resultado;
	}
	
	private CMetodoPago metodoPago(String valor) {
		return CMetodoPago.fromValue(valor);
	}
	
	private CMoneda moneda(String valor) {
		if (valor == null || "".equals(valor.trim()))
			return CMoneda.MXN;
		return CMoneda.fromValue(valor);
	}
	
	private CTipoFactor tipoFactor(String valor) {
		if (valor == null || "".equals(valor.trim()))
			return CTipoFactor.TASA;
		return CTipoFactor.fromValue(valor);
	}
	
	private CUsoCFDI usoCFDI(String valor) {
		if (valor == null || "".equals(valor.trim()))
			return CUsoCFDI.P_01;
		return CUsoCFDI.fromValue(valor);
	}
	
	private CTipoDeComprobante tipoDeComprobante(String valor) {
		return CTipoDeComprobante.fromValue(valor);
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.1 | 2017-04-06 | Javier Tirado 	| Implemento los metodos convertir. 
 */