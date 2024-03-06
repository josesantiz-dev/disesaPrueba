package net.giro.cxc.logica;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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

import com.google.gson.Gson;

import net.giro.adp.beans.Obra;
import net.giro.adp.dao.ObraDAO;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.logica.MensajeTransaccionRem;
import net.giro.cxc.FEv33.CMetodoPago;
import net.giro.cxc.FEv33.CMoneda;
import net.giro.cxc.FEv33.CTipoDeComprobante;
import net.giro.cxc.FEv33.CUsoCFDI;
import net.giro.cxc.FEv33.Comprobante;
import net.giro.cxc.FEv33.Comprobante.Complemento;
import net.giro.cxc.FEv33.Comprobante.Conceptos;
import net.giro.cxc.FEv33.Comprobante.Emisor;
import net.giro.cxc.FEv33.Comprobante.Receptor;
import net.giro.cxc.FEv33.Comprobante.Conceptos.Concepto;
import net.giro.cxc.FEv33.complementos.pagos10.DoctoRelacionado;
import net.giro.cxc.FEv33.complementos.pagos10.Pago;
import net.giro.cxc.FEv33.complementos.pagos10.Pagos;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.beans.FacturaPagosExt;
import net.giro.cxc.beans.FacturaPagosTimbre;
import net.giro.cxc.beans.FacturasAplicarPagos;
import net.giro.cxc.dao.FacturaPagosDAO;
import net.giro.cxc.dao.FacturasAplicarPagosDAO;
import net.giro.cxc.realvirtual.beans.FactElectv33;
import net.giro.cxc.util.FACTURA_PAGO_ESTATUS;
import net.giro.ne.beans.EmpresaCertificado;
import net.giro.ne.logica.NQueryFac;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.logica.EmpresaCertificadoRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosCXC;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.logica.CuentasBancariasRem;

@Stateless
public class FacturaPagosFac implements FacturaPagosRem {
	private static Logger log = Logger.getLogger(FacturaPagosFac.class);
	private InfoSesion infoSesion;
	private long idUsuario;
	private long idEmpresa;
	private long codigoEmpresa;
	// -------------------------------------------------------------------
	private FacturaPagosDAO ifzFacturaPagos;
	private FacturaPagosTimbreRem ifzTimbres;
	private FacturasAplicarPagosDAO ifzPagosAplicables;
	private FacturaRem ifzFacturas;
	private EmpresaCertificadoRem ifzEmpCertificado;
	private CuentasBancariasRem ifzCtas;
	private ObraDAO ifzObras;
	private MensajeTransaccionRem ifzMsgTransaccion;
	private NQueryRem ifzQuery;
	private ConvertExt convertidor;
	
 	public FacturaPagosFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environment);
            this.ifzFacturas = (FacturaRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
            this.ifzFacturaPagos = (FacturaPagosDAO) ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturaPagosImp!net.giro.cxc.dao.FacturaPagosDAO");
            this.ifzTimbres = (FacturaPagosTimbreRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosTimbreFac!net.giro.cxc.logica.FacturaPagosTimbreRem");
            this.ifzPagosAplicables = (FacturasAplicarPagosDAO) ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturasAplicarPagosImp!net.giro.cxc.dao.FacturasAplicarPagosDAO");
            this.ifzCtas = (CuentasBancariasRem) ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
            this.ifzObras = (ObraDAO) ctx.lookup("ejb:/Model_GestionProyectos//ObraImp!net.giro.adp.dao.ObraDAO");
            this.ifzMsgTransaccion = (MensajeTransaccionRem) ctx.lookup("ejb:/Logica_Contabilidad//MensajeTransaccionFac!net.giro.contabilidad.logica.MensajeTransaccionRem");
            this.ifzEmpCertificado = (EmpresaCertificadoRem) ctx.lookup("ejb:/Logica_Publico//EmpresaCertificadoFac!net.giro.plataforma.logica.EmpresaCertificadoRem");
            this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
            
            this.convertidor = new ConvertExt();
            this.convertidor.setFrom("FacturaPagosFac");
            this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaPagosFac", e);
		}
	}
 	
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
	public long save(FacturaPagos entity) throws Exception {
		long idPago = 0L;
		
		try {
			idPago = this.ifzFacturaPagos.save(entity, getCodigoEmpresa());
			actualizaSaldo(idPago);
			return idPago;
		} catch (Exception re) {	
			log.error("Ocurrio un problema al intentar guardar el Pago", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<FacturaPagos> saveOrUpdateList(List<FacturaPagos> entities) throws Exception {
		try {
			if (entities == null || entities.isEmpty())
				return entities;
			return this.saveList(entities, true);
		} catch (Exception re) { 
			log.error("Ocurrio un problema al intentar guardar la lista de pagos", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(FacturaPagos entity) throws Exception {
		try {
			this.ifzFacturaPagos.update(entity);
			actualizaSaldo(entity.getId());
		} catch (Exception re) {	
			log.error("Ocurrio un problema al intentar actualizar el Pago", re);
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta cancelar(long idFacturaPago) throws Exception {
		Respuesta respuesta = new Respuesta();
		// -----------------------------------
		List<FacturaPagos> pagos = null;
		FACTURA_PAGO_ESTATUS estatus = null;
		// -----------------------------------
		FacturaPagos pago = null;
		FacturaPagosExt extendido = null;
		FacturaPagosTimbre timbre = null;
		int pagosRegistrados = 0;
		
		try {
			// Evaluamos Pago para cancelacion
			respuesta = this.evaluaCancelacion(idFacturaPago);
			if (respuesta.getErrores().getCodigoError() != 0L)
				return respuesta;
			
			pago = (FacturaPagos) respuesta.getBody().getValor("pago");
			pagosRegistrados = (int) respuesta.getBody().getValor("pagosRegistrados");
			if (pagosRegistrados > 1)
				pagos = (List<FacturaPagos>) respuesta.getBody().getValor("pagos");
			
			estatus = FACTURA_PAGO_ESTATUS.Cancelado;
			if (pago.getTimbrado() == 1 && pago.getIdTimbre() > 0L) {
				estatus = FACTURA_PAGO_ESTATUS.PendienteCancelacion;
				
				timbre = this.ifzTimbres.findById(pago.getIdTimbre());
				if (timbre != null && timbre.getId() != null && timbre.getId() > 0L) {
					if (timbre.getCanceladoPor() <= 0L)
						timbre.setCanceladoPor(getIdUsuario());
					if (timbre.getMotivoCancelacion() == null || "".equals(timbre.getMotivoCancelacion().trim()))
						timbre.setMotivoCancelacion("Sin Especificar");
					if (timbre.getFechaSolicitudCancelacion() == null)
						timbre.setFechaSolicitudCancelacion(Calendar.getInstance().getTime());
					if (timbre.getIdEmpresa() <= 0L)
						timbre.setIdEmpresa(getIdEmpresa());
					timbre.setModificadoPor(getIdUsuario());
					timbre.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzTimbres.setInfoSesion(this.infoSesion);
					this.ifzTimbres.update(timbre);
					// enviamos peticion de cancelacion
					cancelacionPago(timbre.getId());
				}
			}
			
			// Actualizo Pago o listado de pagos segun corresponda 
			if (pagosRegistrados <= 1) {
				pago.setEstatus(estatus);
				pago.setCanceladoPor(getIdUsuario());
				pago.setFechaCancelacion(Calendar.getInstance().getTime());
				pago.setModificadoPor(getIdUsuario());
				pago.setFechaModificacion(Calendar.getInstance().getTime());
				this.update(pago);
				actualizaSaldo(pago.getId());
			
			// Actualizo listado de pagos 
			} else {
				for (FacturaPagos item : pagos) {
					item.setEstatus(estatus);
					item.setCanceladoPor(getIdUsuario());
					item.setFechaCancelacion(Calendar.getInstance().getTime());
					item.setModificadoPor(getIdUsuario());
					item.setFechaModificacion(Calendar.getInstance().getTime());
				}
				pagos = this.saveList(pagos, true);
				actualizaSaldo(pagos);
			} 

			extendido = this.findByIdExt(pago.getId());
			
			// Añadimos la factura al resultado
			respuesta.getBody().addValor("pago", pago);
			respuesta.getBody().addValor("extendido", extendido);
			respuesta.getBody().addValor("timbre", timbre);
			respuesta.getBody().addValor("pagos", pagos);
		} catch (Exception re) {	
			log.error("Ocurrio un problema al intentar cancelar el Pago", re);
			respuesta.getBody().addValor("exception", re);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar Cancelar el Pago indicado");
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta cancelar(FacturaPagos pago) throws Exception {
		Respuesta respuesta = new Respuesta();
		
		try {
			if (pago == null || pago.getId() == null || pago.getId() <= 0L) {
				respuesta.getBody().addValor("pago", pago);
				respuesta.getErrores().setCodigoError(0);
				respuesta.getErrores().setDescError("Pago no registrado");
				return respuesta;
			}
			
			respuesta = this.cancelar(pago.getId());
		} catch (Exception re) {	
			log.error("Ocurrio un problema al intentar cancelar el Pago", re);
			respuesta.getBody().addValor("exception", re);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar Cancelar el Pago indicado");
		}
		
		return respuesta;
	}

	@Override
	public Respuesta evaluaCancelacion(long idFacturaPago) throws Exception {
		Respuesta respuesta = null;
		List<FacturaPagos> pagos = null;
		FacturaPagosExt extendido = null;
		FacturaPagos pago = null;
		int pagosRegistrados = 1;
		
		try {
			respuesta = new Respuesta();
			pago = this.findById(idFacturaPago);
			if (pago == null || pago.getId() == null || pago.getId() <= 0L) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No se pudo recuperar el Pago indicado");
				return respuesta;
			}
			
			// Validamos estatus de Factura
			if (pago.getEstatus() != FACTURA_PAGO_ESTATUS.Activo.ordinal()) {
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("El Pago indicado ya esta Cancelado o Pendiente de Cancelacion");
				return respuesta;
			}
			
			if (pago.getTimbrado() == 1 && pago.getIdTimbre() > 0L) 
				pagos = this.findByTimbre(pago.getIdTimbre(), false, ""); // findByTimbre(pago.getIdTimbre());
			else if (pago.getAgrupador() != null && ! "".equals(pago.getAgrupador())) 
				pagos = this.findByAgrupador(pago.getAgrupador());
			pagosRegistrados = ((pagos != null && ! pagos.isEmpty()) ? pagos.size() : pagosRegistrados);
			// Recuperamos Factura extendida
			extendido = this.convertir(pago);
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.evaluaCancelacion(idFactura)\n", e);
			throw e;
		} finally {
			respuesta.getBody().addValor("pago", pago);
			respuesta.getBody().addValor("extendido", extendido);
			respuesta.getBody().addValor("pagosRegistrados", pagosRegistrados);
			respuesta.getBody().addValor("pagos", pagos);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta cancelarByFactura(long idFactura) throws Exception {
		return this.cancelarByFactura(this.ifzFacturas.findById(idFactura));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta cancelarByFactura(Factura idFactura) throws Exception {
		Respuesta respuesta = new Respuesta();
		// -----------------------------------
		boolean timbrado = false;
		List<String> cancelarTimbres = null;
		List<FacturaPagos> agrupados = null;
		List<FacturaPagos> pagos = null;
		double saldoNuevo = 0;
		String agrupador = "";
		
		try {
			if (idFactura == null || idFactura.getId() == null || idFactura.getId() <= 0L) {
				respuesta.getErrores().setCodigoError(3L);
				respuesta.getErrores().setDescError("No se pudo enviar la solicitud de cancelacion de timbre");
				return respuesta;
			}
			
			// Recuperamos pagos
			pagos = this.findAll(idFactura.getId(), false, false, ""); //findAll(idFactura.getId());
			if (pagos == null || pagos.isEmpty()) {
				respuesta.getBody().addValor("factura", idFactura);
				respuesta.getErrores().setCodigoError(0);
				respuesta.getErrores().setDescError("Factura sin pagos");
				return respuesta;
			}
			
			// comprobamos si hay al menos un pago timbrado
			cancelarTimbres = new ArrayList<String>();
			for (FacturaPagos pago : pagos) {
				timbrado = (pago.getTimbrado() == 1 && pago.getIdTimbre() > 0L);
				if (timbrado && ! cancelarTimbres.contains(pago.getUuid())) 
					cancelarTimbres.add(pago.getUuid());
			}
			
			if (! cancelarTimbres.isEmpty()) {
				respuesta.getBody().addValor("factura", idFactura);
				respuesta.getBody().addValor("timbres", cancelarTimbres);
				respuesta.getErrores().setCodigoError(2);
				respuesta.getErrores().setDescError("Factura con pagos timbrados");
				return respuesta;
			}
			
			for (FacturaPagos pago : pagos) {
				agrupador = pago.getAgrupador();
				agrupador = (agrupador == null) ? "" : agrupador.trim();
				if (! "".equals(agrupador)) {
					agrupados = this.findByProperty("agrupador", agrupador, false, false, "", 0); // findByProperty("agrupador", agrupador);
					if (agrupados != null && ! agrupados.isEmpty()) {
						for (FacturaPagos item : agrupados) {
							item.setEstatus(FACTURA_PAGO_ESTATUS.PendienteCancelacion);
							item.setCanceladoPor(getIdUsuario());
							item.setModificadoPor(getIdUsuario());
							item.setFechaCancelacion(Calendar.getInstance().getTime());
							item.setFechaModificacion(Calendar.getInstance().getTime());
							if (item.getId().longValue() == pago.getId().longValue()) {
								pago.setEstatus(FACTURA_PAGO_ESTATUS.PendienteCancelacion);
								pago.setCanceladoPor(getIdUsuario());
								pago.setFechaCancelacion(Calendar.getInstance().getTime());
								pago.setModificadoPor(getIdUsuario());
								pago.setFechaModificacion(Calendar.getInstance().getTime());
								saldoNuevo += pago.getPago().doubleValue();
							}
						}
						
						this.saveOrUpdateList(agrupados);
						continue;
					}
				} 

				pago.setEstatus(FACTURA_PAGO_ESTATUS.PendienteCancelacion);
				pago.setCanceladoPor(getIdUsuario());
				pago.setFechaCancelacion(Calendar.getInstance().getTime());
				pago.setModificadoPor(getIdUsuario());
				pago.setFechaModificacion(Calendar.getInstance().getTime());
				saldoNuevo += pago.getPago().doubleValue();
			}
			
			this.saveList(pagos, false);
			saldoNuevo += idFactura.getSaldoFactura();
			idFactura.setSaldo(new BigDecimal(saldoNuevo));
			respuesta.getBody().addValor("factura", idFactura);
			respuesta.getErrores().setCodigoError(0);
			respuesta.getErrores().setDescError("Pagos Cancelados");
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar cancelar los pagos de la Factura indicada", e);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("No se pudo cancelar el Pago de la Factura indicada");
		}
		
		return respuesta;
	}

	@Override
	public void delete(long idFacturaPagos) throws Exception {
		try {
			this.ifzFacturaPagos.delete(idFacturaPagos);
		} catch (Exception re) {	
			log.error("Ocurrio un problema en el método delete", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public FacturaPagos findById(long idFacturaPagos) {
		try {
			return this.ifzFacturaPagos.findById(idFacturaPagos);
		} catch (Exception re) {	
			log.error("Ocurrio un problema en el método findById", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagos> findLike(String value, boolean incluyeCanceladas, boolean soloTimbrado, String orderBy, int limite) throws Exception {
		try {
			value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			return this.ifzFacturaPagos.findLike(value, incluyeCanceladas, soloTimbrado, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaPagosFac.findLike(value, incluyeCanceladas, soloTimbrado, limite)\n", re);
			throw re;
		} 
	}

	@Override
	public List<FacturaPagos> findLikeProperty(String propertyName, Object value, boolean incluyeCanceladas, boolean soloTimbrado, String orderBy, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), incluyeCanceladas, soloTimbrado, orderBy, limite);
			if (value.getClass() == java.lang.String.class)
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			return this.ifzFacturaPagos.findLikeProperty(propertyName, value, incluyeCanceladas, soloTimbrado, getIdEmpresa(), propertyName, limite);
		} catch (Exception re) {	
			log.error("Ocurrio un problema en el método findLikeProperty(propertyName, value, incluyeCanceladas, soloTimbrado, limite)", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagos> findLikeProperty(String propertyName, String value) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, false, false, "", 0);
		} catch (Exception re) {	
			log.error("Ocurrio un problema en el método findLikeProperty", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagos> findByProperty(String propertyName, Object value, boolean incluyeCanceladas, boolean soloTimbrado, String orderBy, int limite) throws Exception {
		try {
			return this.ifzFacturaPagos.findByProperty(propertyName, value, incluyeCanceladas, soloTimbrado, getIdEmpresa(), orderBy, 0);
		} catch (Exception re) {	
			log.error("Ocurrio un problema en el método findByProperty(propertyName, value)", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagos> findByProperty(String propertyName, Object value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, false, false, "", 0);
		} catch (Exception re) {	
			log.error("Ocurrio un problema en el método findByProperty(propertyName, value)", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagos> findAll(long idFactura) throws Exception {
		try {
			return this.findAll(idFactura, false, false, "");
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findAll(idFactura)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<FacturaPagos> findAll(long idFactura, boolean incluyeCanceladas, boolean soloTimbrado, String orderBy) throws Exception {
		try {
			return this.ifzFacturaPagos.findAll(idFactura, incluyeCanceladas, soloTimbrado, orderBy);
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findAll(idFactura, incluyeCanceladas, soloTimbrado, orderBy)", e);
			throw e;
		}
	}

	@Override
	public List<FacturaPagos> findByTimbre(long idTimbre) throws Exception {
		try {
			return this.findByTimbre(idTimbre, false, "");
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findByFactura(Factura idFactura)", e);
			throw e;
		}
	}

	@Override
	public List<FacturaPagos> findByTimbre(long idTimbre, boolean incluyeCanceladas, String orderBy) throws Exception {
		try {
			return this.ifzFacturaPagos.findByTimbre(idTimbre, incluyeCanceladas, orderBy);
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findByTimbre(idTimbre, incluyeCanceladas, orderBy)", e);
			throw e;
		}
	}
	
	@Override
	public List<FacturaPagos> findByAgrupador(String agrupador) throws Exception {
		try {
			return this.ifzFacturaPagos.findByProperty("agrupador", agrupador, false, false, getIdEmpresa(), "", 0);
		} catch (Exception re) {	
			log.error("Ocurrio un problema en el método findByAgrupador(agrupador)", re);
			throw re;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public BigDecimal findLiquidado(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception {
		List<FacturaPagos> pagos = null;
		boolean validarFechas = false;
		double pagado = 0;
		
		try {
			// Validaciones
			if (fechaDesde != null && fechaHasta == null)
				fechaHasta = Calendar.getInstance().getTime();
			validarFechas = ((fechaDesde != null && fechaHasta != null) || (fechaDesde == null && fechaHasta != null));
			
			// Pagos registrados
			pagos = this.findAll(idFactura, false, false, "");
			if (pagos != null && ! pagos.isEmpty()) {
				for (FacturaPagos pago : pagos) {
					if (pago.getEstatus() != FACTURA_PAGO_ESTATUS.Activo.ordinal())
						continue;
					
					if (validarFechas) {
						if (fechaDesde != null && pago.getFecha().compareTo(fechaDesde) < 0)
							continue;
						if (fechaHasta != null && pago.getFecha().compareTo(fechaHasta) > 0)
							continue;
					}
					
					pagado += pago.getPago().doubleValue();
				}
			}
			
			return new BigDecimal(pagado);
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findLiquidado(idFactura, fechaDesde, fechaHasta)", e);
			throw e;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public BigDecimal findLiquidadoPesos(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception {
		List<FacturaPagos> pagos = null;
		boolean validarFechas = false;
		double tipoCambio = 0;
		double pagado = 0;
		
		try {
			// Validaciones
			if (fechaDesde != null && fechaHasta == null)
				fechaHasta = Calendar.getInstance().getTime();
			validarFechas = ((fechaDesde != null && fechaHasta != null) || (fechaDesde == null && fechaHasta != null));
			
			// Pagos registrados
			pagos = this.findAll(idFactura, false, false, "");
			if (pagos != null && ! pagos.isEmpty()) {
				for (FacturaPagos pago : pagos) {
					if (pago.getEstatus() != FACTURA_PAGO_ESTATUS.Activo.ordinal())
						continue;
					
					if (validarFechas) {
						if (fechaDesde != null && pago.getFecha().compareTo(fechaDesde) < 0)
							continue;
						if (fechaHasta != null && pago.getFecha().compareTo(fechaHasta) > 0)
							continue;
					}
					
					tipoCambio = (pago.getTipoCambio() > 0) ? pago.getTipoCambio() : 1;
					pagado += pago.getPago().doubleValue() * tipoCambio;
				}
			}
			
			return new BigDecimal(pagado);
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findLiquidadoPesos(idFactura, fechaDesde, fechaHasta)", e);
			throw e;
		}
	}

	@Override
	public Respuesta enviarTransaccion(Long entityId) throws Exception {
		try {
			return this.enviarTransaccion(this.findByIdExt(entityId));
		} catch (Exception e) {
			log.error("Ocurrio un problema Logica_CuentasPorCobrar.FacturaPagosFac.enviarTransaccion(entityId, idEmpresa)", e);
			throw e;
		}
	}

	@Override
	public Respuesta enviarTransaccion(String agrupador) throws Exception {
		List<FacturaPagosExt> extendidos = null;
		FacturaPagosExt extendido = null;
		double monto = 0;
		
		try {
			extendidos = this.findExtByAgrupador(agrupador);
			if (extendidos == null || extendidos.isEmpty())
				return null;
			for (FacturaPagosExt pago : extendidos)
				monto += pago.getPago().doubleValue();
			extendido = extendidos.get(0);
			extendido.setPago(new BigDecimal(monto));
			return this.enviarTransaccion(extendido);
		} catch (Exception e) {
			log.error("Ocurrio un problema Logica_CuentasPorCobrar.FacturaPagosFac.enviarTransaccion(entityId, idEmpresa)", e);
			throw e;
		}
	}

	@Override
	public Respuesta enviarTransaccion(FacturaPagos entity) throws Exception {
		try {
			return this.enviarTransaccion(this.convertidor.FacturaPagosToFacturaPagosExt(entity));
		} catch (Exception e) {
			log.error("Ocurrio un problema Logica_CuentasPorCobrar.FacturaPagosFac.enviarTransaccion(entity, idEmpresa)", e);
			throw e;
		}
	}
	
	@Override
	public int findParcialidad(long idFactura) throws Exception {
		try {
			return obtenerParcialidad(idFactura);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar la parcialidad de la Factura indicada: " + idFactura, e);
			throw e;
		}
	}

	@Override
	public Comprobante generarComprobante(long idFacturaPago, String serie, String folio) throws Exception {
		List<FacturaPagosExt> pagos = null;
		FacturaPagosExt pago = null;
		
		try {
			pago = this.findByIdExt(idFacturaPago);
			if (pago == null || pago.getId() == null || pago.getId() <= 0L || this.infoSesion == null)
				return null;
			
			pagos = new ArrayList<FacturaPagosExt>();
			pagos.add(pago);
			return this.genComprobante(pagos, serie, folio);
		} catch (Exception e) {
			log.error("Ocurrio un problema Logica_CuentasPorPagar.PagosGastosFac.generarComprobante(idFacturaPago)", e);
			throw e;
		}
	}

	@Override
	public Comprobante generarComprobante(String agrupador, String serie, String folio) throws Exception {
		List<FacturaPagosExt> pagos = null;
		
		try {
			if (agrupador == null || "".equals(agrupador.trim()) || this.infoSesion == null)
				return null;
			pagos = this.findByPropertyExt("agrupador", agrupador);
			if (pagos == null || pagos.isEmpty())
				return null;
			return this.genComprobante(pagos, serie, folio);
		} catch (Exception e) {
			log.error("Ocurrio un problema Logica_CuentasPorPagar.PagosGastosFac.generarComprobante(agrupador)", e);
			throw e;
		}
	}

	@Override
	public Comprobante generarComprobante(List<FacturaPagosExt> pagos, String serie, String folio) throws Exception {
		if (pagos == null || pagos.isEmpty() || this.infoSesion == null)
			return null;
		return this.genComprobante(pagos, serie, folio);
	}

	@Override
	public String formarXML(long idFacturaPago) throws Exception {
		return formarXML(generarComprobante(idFacturaPago, "", ""));
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
	public Long folioComplementoPago() throws Exception {
		try {
			return this.folioComplementoPago(getCodigoEmpresa(), getSerieComplementoPago());
		} catch (Exception e) {
			log.error("Ocurrio un problema en Logica_CuentasPorCobrar.FacturaPagosFac.folioComplementoPago()", e);
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Long folioComplementoPago(long codigoEmpresa, String serieComplementoPago) throws Exception {
		NQueryFac query = null;
		List<Object> lista = null;
		Iterator<Object> it = null;
		BigInteger val = null;
		String secuencia = "";
		String queryString = "";
		Long folio = 0L;
		
		try {
			if (codigoEmpresa <= 0L && this.infoSesion != null)
				codigoEmpresa = this.infoSesion.getEmpresa().getCodigo();
			if ((serieComplementoPago == null || "".equals(serieComplementoPago)) && this.infoSesion != null)
				serieComplementoPago = getSerieComplementoPago();
			secuencia += "seq_comprobante_pago_serie_" + serieComplementoPago.trim().toLowerCase() + "_emp_" + codigoEmpresa;
			queryString = "select 1 from pg_class where relkind = 'S' and relname = '" + secuencia + "'";
			query = new NQueryFac();
			lista = query.findNativeQuery(queryString);
			if (lista == null || lista.isEmpty()) {
				queryString = "CREATE SEQUENCE " + secuencia + " INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;";
				query.ejecutaAccion(queryString);
				folio = 0L;
			}
			
			if (folio <= 0) {
				queryString = "select nextval('" + secuencia + "') AS folio ";
				lista = query.findNativeQuery(queryString);
				it = lista.iterator();
				while (it.hasNext()) {
					val = (BigInteger) it.next();
					folio = val.longValue();
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema en Logica_CuentasPorCobrar.FacturaPagosFac.folioComplementoPago(codigoEmpresa, serieComplementoPago)", e);
			throw e;
		}
		
		secuencia = String.format("%06d", folio.intValue());
		folio = Long.valueOf(secuencia);
		
		return folio;
	}

	@Override
	public Respuesta timbrar(FacturaPagos pago, String serie, String folio) throws Exception {
		List<FacturaPagosExt> pagos = new ArrayList<FacturaPagosExt>();
		pagos.add(this.convertir(pago));
		return this.timbrar(pagos, serie, folio, false, false, false);
	}

	@Override
	public Respuesta timbrar(FacturaPagosExt pago, String serie, String folio) throws Exception {
		List<FacturaPagosExt> pagos = new ArrayList<FacturaPagosExt>();
		pagos.add(pago);
		return this.timbrar(pagos, serie, folio, false, false, false);
	}

	@Override
	public Respuesta timbrar(List<FacturaPagosExt> pagos, String serie, String folio) throws Exception {
		return this.timbrar(pagos, serie, folio, false, false, false);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta timbrar(List<FacturaPagosExt> pagos, String serie, String folio, boolean debugging, boolean testing, boolean noTimbrar) throws Exception {
		Respuesta respuesta = new Respuesta();
		//String folioComplementoPago = "";
		Long folioAux = 0L;
		FacturaPagosExt pagoProv = null;
		FacturaPagosTimbre timbre = null;
		double total = 0;
		// -----------------------------------------
		Comprobante comprobante = null;
		FactElectv33 facComprobante = null;
		String fileName = "";
		
		try {
			if (pagos == null || pagos.isEmpty()) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No indico ningun Pago de Facturas");
				log.warn("No indico ningun Pago de Facturas");
				return respuesta;
			}
			
			pagos = this.saveOrUpdateListExt(pagos);
			for (FacturaPagosExt pago : pagos) {
				total += pago.getPago().doubleValue();
				if (pagoProv == null)
					pagoProv = pago;
			}

			serie = (serie == null || "".equals(serie.trim()) ? pagoProv.getSerie() : serie);
			folio = (folio == null || "".equals(folio.trim()) ? pagoProv.getFolio() : folio);
			if (folio == null || "".equals(folio.trim())) {
			/*folioComplementoPago = pagoProv.getFolio();
			if (folioComplementoPago == null || "".equals(folioComplementoPago.trim())) {*/
				folioAux = this.folioComplementoPago(getCodigoEmpresa(), getSerieComplementoPago()); // folioComplementoPago();
				if (folioAux == null || folioAux <= 0L) {
					respuesta.getErrores().setCodigoError(2L);
					respuesta.getErrores().setDescError("No se pudo asignar un folio para Complemento de Pago");
					log.error("Ocurrio un problema al intentar asignar el folio para Timbrar el Pago");
					return respuesta;
				}
				
				folio = folioAux.toString(); // folioComplementoPago = folioAux.toString();
				for (FacturaPagosExt fPago : pagos) 
					fPago.setFolio(folio); // (folioComplementoPago);
				pagos = this.saveOrUpdateListExt(pagos);
			}
			
			// Obtenemos Comprobante
			if (pagos.size() > 1)
				comprobante = this.generarComprobante(pagoProv.getAgrupador(), serie, folio);
			else
				comprobante = this.generarComprobante(pagoProv.getId(), serie, folio);
			if (comprobante == null) {
				respuesta.getErrores().setCodigoError(3L);
				respuesta.getErrores().setDescError("No se pudo generar el Complemento de Pago");
				log.error("Ocurrio un problema al generar el Complemento de Pago");
				return respuesta;
			}
			
			// Proceso de timbrado
			facComprobante = new FactElectv33(this.infoSesion);
			facComprobante.setDebugging(debugging);
			facComprobante.setTesting(testing);
			facComprobante.setNoTimbrar(noTimbrar);
			fileName = "P-" + comprobante.getSerie() + comprobante.getFolio();
			respuesta = facComprobante.timbrarPago(comprobante, total, fileName, pagoProv.getIdFactura().getIdEmpresa().getId(), this.infoSesion.getAcceso().getUsuario().getId());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				respuesta.getErrores().setCodigoError(4L);
				respuesta.getErrores().setDescError(respuesta.getErrores().getDescError());
				log.error("Ocurrio un problema al timbrar el Complemento de Pago\n" + respuesta.getErrores().getDescError());
				return respuesta;
			}
			
			timbre = (FacturaPagosTimbre) respuesta.getBody().getValor("pojoTimbre");
			if (respuesta.getErrores().getCodigoError() != 0L) {
				respuesta.getErrores().setCodigoError(4L);
				respuesta.getErrores().setDescError("No se pudo recuperar el Timbre");
				log.error("Ocurrio un problema al procesar el timbrado del Complemento de Pago.\nNo se pudo recuperar el Timbre");
				return respuesta;
			}

			timbre.setRfcReceptor(comprobante.getReceptor().getRfc());
			timbre.setRfcEmisor(comprobante.getEmisor().getRfc());
			this.ifzTimbres.update(timbre);
			
			// Actualizo el Timbre en los Pagos 
			for (FacturaPagosExt pago : pagos)
				pago.setIdTimbre(timbre);
			// Guardamos los pagos
			pagos = this.saveOrUpdateListExt(pagos);

			respuesta.getBody().addValor("pagos", pagos);
			respuesta.getBody().addValor("timbre", timbre);
			respuesta.getBody().addValor("total", total);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método timbrarPago(Long idFactura)", e);
			throw e;
		}
		
		return respuesta;
	}
	
	@Override
	public boolean guardarPagoAplicable(long idFacturaOriginal, long idFactura) throws Exception {
		FacturasAplicarPagos aplicable = null;
		
		try {
			if (idFacturaOriginal <= 0L || idFactura <= 0L)
				return true;
			aplicable = this.ifzPagosAplicables.comprobarPagoAplicable(idFacturaOriginal, idFactura);
			if (aplicable == null || aplicable.getId() == null || aplicable.getId() <= 0L) 
				aplicable = new FacturasAplicarPagos(idFacturaOriginal, idFactura, this.infoSesion.getAcceso().getUsuario().getId(), this.infoSesion.getAcceso().getUsuario().getId());
			if (aplicable.getId() == null || aplicable.getId() <= 0L)
				aplicable.setId(this.ifzPagosAplicables.save(aplicable, getCodigoEmpresa()));
			return true;
		} catch (Exception e) {
			log.error("Ocurrio un problema al mover los pagos a la Factura " + idFactura, e);
			return false;
		}
	}

	@Override
	public boolean aplicarPagos(long idFactura) throws Exception {
		List<FacturasAplicarPagos> aplicables = null;
		List<FacturaPagos> pagos = null;
		List<Long> facturas = null;
		Factura factura = null;
		
		try {
			if (idFactura <= 0L)
				return true;
			aplicables = this.ifzPagosAplicables.pagosAplicables(idFactura);
			if (aplicables == null || aplicables.isEmpty())
				return true;
			
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			factura = this.ifzFacturas.findById(idFactura);
			if (factura == null || factura.getId() == null || factura.getId() <= 0L)
				throw new Exception("No se pudo recuperar la Factura indicada");
			facturas = new ArrayList<Long>();
			facturas.add(idFactura);
			for (FacturasAplicarPagos aplicable : aplicables) {
				pagos = this.findAll(aplicable.getIdFacturaOriginal(), false, false, "");//findAll(aplicable.getIdFacturaOriginal());
				if (pagos == null || pagos.isEmpty())
					continue;
				for (FacturaPagos pago : pagos)
					pago.setIdFactura(factura);
				this.saveOrUpdateList(pagos);
				
				aplicable.setAplicado(1);
				aplicable.setFechaModificacion(Calendar.getInstance().getTime());
				aplicable.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			}
			
			// Actualizamos cambios
			this.ifzPagosAplicables.saveOrUpdateList(aplicables, getCodigoEmpresa());
			actualizaSaldoByFacturas(facturas);
			return true;
		} catch (Exception e) {
			log.error("Ocurrio un problema al mover los pagos a la Factura " + idFactura, e);
			return false;
		}
	}
	
	@Override
	public int validarPagosMultiples(long idFactura) throws Exception {
		List<FacturaPagos> pagos = null;
		int pagosMultiples = 0;
		
		try {
			pagos = this.findAll(idFactura, false, false, ""); //findAll(idFactura);
			if (pagos == null || pagos.isEmpty())
				return pagosMultiples;
			
			for (FacturaPagos pago : pagos) {
				if (pago.getEstatus() != FACTURA_PAGO_ESTATUS.Activo.ordinal())
					continue;
				if (pago.getAgrupador() != null && ! "".equals(pago.getAgrupador().trim()))
					pagosMultiples++;
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar los pagos de la Factura indicada: " + idFactura, e);
			pagosMultiples = 0;
		}
		
		return pagosMultiples;
	}

	@Override
	public LinkedHashMap<String, String> auditoria(long idFacturaPago) throws Exception {
		LinkedHashMap<String, String> auditoria = null;
		SimpleDateFormat formatter = null;
		FacturaPagosTimbre timbre = null;
		FacturaPagos pago = null;
		String descripcion = "";
		
		try {
			pago = this.findById(idFacturaPago);
			if (pago == null || pago.getId() == null || pago.getId() <= 0L) {
				log.error("El Pago indicada no existe: " + idFacturaPago);
				return null;
			}
			
			timbre = this.ifzTimbres.findById(pago.getIdTimbre());
			formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			auditoria = new LinkedHashMap<String, String>();
			
			descripcion = getUsername(pago.getCreadoPor());
			auditoria.put("Creado", (descripcion != null && ! "".equals(descripcion.trim()) ? formatter.format(pago.getFechaCreacion()) + " - " + descripcion : ""));

			auditoria.put("Timbrado", "");
			auditoria.put("Cancelado", "");
			if (timbre != null) {
				if (pago.getTimbradoPor() > 0L) {
					descripcion = getUsername(timbre.getTimbradoPor());
					auditoria.put("Timbrado", (descripcion != null && ! "".equals(descripcion.trim()) ? formatter.format(timbre.getFecha()) + " - " + descripcion : ""));
				}

				if (timbre != null && timbre.getCanceladoPor() > 0L) {
					descripcion = getUsername(pago.getCanceladoPor());
					auditoria.put("Cancelado", (descripcion != null && ! "".equals(descripcion.trim()) ? formatter.format(pago.getFechaCancelacion()) + " - " + descripcion : ""));
					
					descripcion = timbre.getMotivoCancelacion();
					auditoria.put("Motivo Cancelacion", (descripcion != null && ! "".equals(descripcion.trim()) ? descripcion : ""));
				}
			}

			descripcion = getUsername(pago.getModificadoPor());
			auditoria.put("Ultima Modificacion", (descripcion != null && ! "".equals(descripcion.trim()) ? formatter.format(pago.getFechaModificacion()) + " - " + descripcion : ""));
		} catch (Exception e) {
			log.error("No se pudo recuperar la Auditoria del Pago indicado: " + idFacturaPago, e);
			auditoria = null;
		}
		
		return auditoria;
	}

	// ------------------------------------------------------------------------------------------------
	// CONVERTIDORES 
	// ------------------------------------------------------------------------------------------------

	@Override
	public FacturaPagos convertir(FacturaPagosExt pojoTarget) throws Exception {
		return this.convertidor.FacturaPagosExtToFacturaPagos(pojoTarget);
	}

	@Override
	public FacturaPagosExt convertir(FacturaPagos pojoTarget) throws Exception {
		return this.convertidor.FacturaPagosToFacturaPagosExt(pojoTarget);
	}
	
	// ------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// ------------------------------------------------------------------------------------------------

	@Override
	public long save(FacturaPagosExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.FacturaPagosExtToFacturaPagos(extendido));
		} catch (Exception re) {	
			log.error("Ocurrio un problema en el método save", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagosExt> saveOrUpdateListExt(List<FacturaPagosExt> extendidos) throws Exception {
		List<FacturaPagos> entities = null;
		
		try {
			entities = new ArrayList<FacturaPagos>();
			for (FacturaPagosExt extendido : extendidos)
				entities.add(this.convertidor.FacturaPagosExtToFacturaPagos(extendido));
			entities = this.saveOrUpdateList(entities);
			extendidos.clear();
			for (FacturaPagos entity : entities)
				extendidos.add(this.convertidor.FacturaPagosToFacturaPagosExt(entity));
		} catch (Exception re) {	
			log.error("Ocurrio un problema en el método saveOrUpdateListExt", re);
			throw re;
		}
		
		return extendidos;
	}
	
	@Override
	public void update(FacturaPagosExt extendido) throws Exception {
		try {
			this.update(this.convertidor.FacturaPagosExtToFacturaPagos(extendido));
		} catch (Exception re) {	
			log.error("Ocurrio un problema en el método update", re);
			throw re;
		}
	}
	
	@Override
	public FacturaPagosExt findByIdExt(long idFacturaPagos) throws Exception {
		try {
			return this.convertidor.FacturaPagosToFacturaPagosExt(this.findById(idFacturaPagos));
		} catch (Exception re) {	
			log.error("Ocurrio un problema en el método findByIdExt", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagosExt> findByPropertyExt(String propertyName, Object value) throws Exception {
		List<FacturaPagosExt> extendidos = null;
		List<FacturaPagos> entities = null;
		
		try {
			extendidos = new ArrayList<FacturaPagosExt>();
			entities = this.findByProperty(propertyName, value, false, false, "", 0); // findByProperty(propertyName, value);
			if (entities == null || entities.isEmpty()) 
				return extendidos;
			for (FacturaPagos entity : entities)
				extendidos.add(this.convertidor.FacturaPagosToFacturaPagosExt(entity));
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findByPropertyExt", e);
			throw e;
		}
		
		return extendidos;
	}
	
	@Override
	public List<FacturaPagosExt> findLikePropertyExt(String propertyName, String value) throws Exception {
		List<FacturaPagosExt> extendidos = null;
		List<FacturaPagos> entities = null;
		
		try {
			extendidos = new ArrayList<FacturaPagosExt>();
			entities = this.findLikeProperty(propertyName, value, false, false, "", 0); // findLikeProperty(propertyName, value);
			if (entities == null || entities.isEmpty()) 
				return extendidos;
			for (FacturaPagos entity : entities)
				extendidos.add(this.convertidor.FacturaPagosToFacturaPagosExt(entity));
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findLikePropertyExt", e);
			throw e;
		}
		
		return extendidos;
	}
	
	@Override
	public List<FacturaPagosExt> findLikeFolioFactura(String value) throws Exception {
		List<FacturaPagosExt> extendidos = null;
		List<FacturaPagos> entities = null;
		List<Factura> facturas = null;
		List<Long> values = null;
		
		try {
			extendidos = new ArrayList<FacturaPagosExt>();
			facturas = this.ifzFacturas.findLikeProperty("fac.folioFactura", value);
			if (facturas == null || facturas.isEmpty()) 
				return extendidos;
			
			values = new ArrayList<Long>();
			for (Factura var : facturas)
				values.add(var.getId());
			
			entities = this.ifzFacturaPagos.findInProperty("idFactura", values, false, getIdEmpresa(), "", 0);
			if (entities == null || entities.isEmpty()) 
				return extendidos;
			for (FacturaPagos entity : entities)
				extendidos.add(this.convertidor.FacturaPagosToFacturaPagosExt(entity));
		} catch (Exception e) {
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<FacturaPagosExt> findExtByFactura(long idFactura) throws Exception {
		List<FacturaPagosExt> extendidos = null;
		List<FacturaPagos> entities = null;
		
		try {
			extendidos = new ArrayList<FacturaPagosExt>();
			entities = this.findAll(idFactura, false, false, ""); //findAll(idFactura);
			if (entities == null || entities.isEmpty()) 
				return extendidos;
			for (FacturaPagos entity : entities)
				extendidos.add(this.convertidor.FacturaPagosToFacturaPagosExt(entity));
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findExtByFactura(idFactura)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<FacturaPagosExt> findExtByTimbre(long idTimbre) throws Exception {
		List<FacturaPagosExt> extendidos = null;
		List<FacturaPagos> entities = null;
		
		try {
			extendidos = new ArrayList<FacturaPagosExt>();
			entities = this.findByTimbre(idTimbre, false, ""); // findByTimbre(idTimbre);
			if (entities == null || entities.isEmpty())
				return extendidos;
			for (FacturaPagos entity : entities)
				extendidos.add(this.convertidor.FacturaPagosToFacturaPagosExt(entity));
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findExtByTimbre(idTimbre)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<FacturaPagosExt> findExtByAgrupador(String agrupador) throws Exception {
		List<FacturaPagosExt> extendidos = null;
		List<FacturaPagos> entities = null;
		
		try {
			extendidos = new ArrayList<FacturaPagosExt>();
			entities = this.findByAgrupador(agrupador);
			if (entities == null || entities.isEmpty())
				return extendidos;
			for (FacturaPagos entity : entities)
				extendidos.add(this.convertidor.FacturaPagosToFacturaPagosExt(entity));
		} catch (Exception e) {
			log.error("Ocurrio un problema en el método findExtByAgrupador(agrupador)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<FacturaPagosExt> findLikeBeneficiario(String value) throws Exception {
		List<FacturaPagosExt> extendidos = null;
		List<FacturaPagos> entities = null;
		
		try {
			extendidos = new ArrayList<FacturaPagosExt>();
			entities = this.findLikeProperty("beneficiario", value, false, false, "", 0); // findLikeProperty("beneficiario", value);
			if (entities == null || entities.isEmpty())
				return extendidos;
			for (FacturaPagos entity : entities)
				extendidos.add(this.convertidor.FacturaPagosToFacturaPagosExt(entity));
		} catch (Exception e) {
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<FacturaPagosExt> findLikeCuentaBancaria(String value) throws Exception {
		List<FacturaPagosExt> extendidos = null;
		List<FacturaPagos> entities = null;
		List<CuentaBancaria> cuentas = null;
		List<Long> values = null;
		
		try {
			extendidos = new ArrayList<FacturaPagosExt>();
			this.ifzCtas.setInfoSesion(this.infoSesion);
			cuentas = this.ifzCtas.findLikeColumnName("numeroDeCuenta", value);
			if (cuentas == null || cuentas.isEmpty())
				return extendidos;

			values = new ArrayList<Long>();
			for (CuentaBancaria var : cuentas)
				values.add(var.getId());
			
			entities = this.ifzFacturaPagos.findInProperty("idCuentaDeposito", values, false, getIdEmpresa(), "", 0);
			if (entities == null || entities.isEmpty())
				return extendidos;
			for (FacturaPagos entity : entities)
				extendidos.add(this.convertidor.FacturaPagosToFacturaPagosExt(entity));
		} catch (Exception e) {
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public Respuesta enviarTransaccion(FacturaPagosExt extendido) throws Exception {
		Respuesta respuesta = new Respuesta();
		MensajeTransaccion msg = null;
		Obra pojoObra = null;
		BigDecimal monto = BigDecimal.ZERO;
		Long idMensaje = 0L;
		Long idTransaccion = 0L;
		Long idFormaPago = 0L;
		Long idMoneda = 0L;
		Long idCliente = 0L;
		String descOperacion = "";
		String descMoneda = "";
		String referencia = "";
		String descFormaPago = "";
		String nombreCliente = "";
		String tipoPersona = "";
		String tracking = "";
		
		try {
			if (extendido == null) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No indico Factura");
				return respuesta;
			}

			pojoObra = this.ifzObras.findById(extendido.getIdFactura().getIdObra().getId());
			if (pojoObra == null) {
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se pudo obtener los datos del Cliente");
				return respuesta;
			}
			
			// Determinamos Transaccion
			if ("PPD".equals(extendido.getIdFactura().getIdMetodoPago().getValor())) {
				// Transaccion 1015: PAGO DE CLIENTES - VENTAS DE CREDITO
				tracking = "Transaccion 1015 disparada desde FacturaPagosFac. Pago " + extendido.getId() + " a Factura " + extendido.getIdFactura().getFolioFactura() + " (" + extendido.getIdFactura().getId() + ")";
				idTransaccion = 1015L; 
				descOperacion = "PAGO DE CLIENTES - VENTAS DE CREDITO";
			} else {
				// Transaccion 1013: PAGO DE CLIENTES - VENTAS DE CONTADO
				tracking = "Transaccion 1013 disparada desde FacturaPagosFac. Pago " + extendido.getId() + " a Factura " + extendido.getIdFactura().getFolioFactura() + " (" + extendido.getIdFactura().getId() + ")";
				idTransaccion = 1013L; 
				descOperacion = "PAGO DE CLIENTES - VENTAS DE CONTADO";
			}
			
			idCliente = pojoObra.getIdCliente();
			nombreCliente = pojoObra.getNombreCliente();
			tipoPersona = pojoObra.getTipoCliente();
			idMoneda = extendido.getIdCuentaDeposito().getMoneda().getId();
			descMoneda = extendido.getIdCuentaDeposito().getMoneda().getNombre();
			monto = extendido.getPago();
			idFormaPago = extendido.getIdFormaPago().getId();
			descFormaPago = extendido.getIdFormaPago().getFormaPago();
			referencia = extendido.getReferenciaFormaPago();

			msg = new MensajeTransaccion();
			msg.setIdTransaccion(idTransaccion);
			msg.setIdOperacion(extendido.getId());
			msg.setDescripcionOperacion(descOperacion);
			msg.setIdMoneda(idMoneda);
			msg.setDescripcionMoneda(descMoneda);
			msg.setImporte(monto);
			msg.setIdPersonaReferencia(idCliente);
			msg.setNombrePersonaReferencia(nombreCliente);
			msg.setTipoPersona(tipoPersona);
			msg.setReferencia("");
			msg.setIdFormaPago(idFormaPago);
			msg.setDescripcionFormaPago(descFormaPago);
			msg.setReferenciaFormaPago(referencia);
			msg.setIdSucursal(extendido.getIdFactura().getIdSucursal().getId());
			msg.setDescripcionSucursal(extendido.getIdFactura().getIdSucursal().getSucursal());
			msg.setIdEmpresa(getIdEmpresa());
			msg.setDescripcionEmpresa(getEmpresa());
			msg.setIdUsuarioCreacionRegistro(extendido.getCreadoPor());
			msg.setFechaRegistro(extendido.getIdFactura().getFechaEmision());
			msg.setCreadoPor(extendido.getCreadoPor());
			msg.setFechaCreacion(extendido.getFechaCreacion());
			msg.setAnuladoPor(0L);
			msg.setFechaAnulacion(null);
			msg.setEstatus(0);
			
			// Registramos el mensaje
			this.ifzMsgTransaccion.setInfoSesion(this.infoSesion);
			idMensaje = this.ifzMsgTransaccion.save(msg);
			log.info("Tracking ID " + idMensaje + " : " + tracking);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("mensaje", idMensaje);
			respuesta.getErrores().setDescError("Factura Provisionada");
		} catch (Exception e) {
			log.error("Ocurrio un problema Logica_CuentasPorPagar.PagosGastosFac.enviarTransaccion(PagosGastosEntity, idEmpresa)", e);
			respuesta.getErrores().setCodigoError(-1L);
			respuesta.getErrores().setDescError("Error. No se pudo provisionar la factura");
			respuesta.getBody().addValor("exception", e);
		} 
		
		return respuesta;
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

	private String getSerieComplementoPago() {
		if (this.infoSesion != null)
			return this.infoSesion.getEmpresa().getSerieComplementoPago();
		return "";
	}

	private String valueToString(Object value) {
		return (value != null ? value.toString().trim() : "");
	}

	private BigDecimal valueToBigDecimal(Object value) {
		return ((! "".equals(valueToString(value))) ? new BigDecimal(valueToString(value)) : BigDecimal.ZERO);
	}
	
	private String agrupador() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
		List<FacturaPagos> pagos = null;
		String agrupador = "";
		int num = 0;
		
		try {
			do {
				num++;
				agrupador = formatter.format(Calendar.getInstance().getTime()) + "/" + String.format("%02d", num);
				pagos = this.findByProperty("agrupador", agrupador, false, false, "", 0); // findByProperty("agrupador", agrupador);
			} while (pagos != null && ! pagos.isEmpty());
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar generar un agrupador para Pagos", e);
			agrupador = "";
		}
		
		return agrupador;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Comprobante genComprobante(List<FacturaPagosExt> pagos, String serie, String folio) throws Exception {
		FacturaExt factura = null;
		FacturaPagosExt pojoPago = null;
		EmpresaCertificado empCertificado = null;
		double pagado = 0;
		// -------------------------------------------------
		Comprobante comprobante = null;
		Emisor emisor = null;
		Receptor receptor = null;
		Conceptos conceptos = null;
		Concepto concepto = null;
		Complemento complemento = null;
		Pagos pagos10 = null;
		Pago pago10 = null;
		DoctoRelacionado doctoRelacionado = null;
		List<DoctoRelacionado> doctosRelacionados = null;
		// -------------------------------------------------
		XMLGregorianCalendar date = null;
		DateFormat format = null;
		String FORMATER = "yyyy-MM-dd'T'HH:mm:ss";
		// -------------------------------------------------
		Properties properties = null;
		String claveProdServ = "84111506";
		String claveUnidad = "ACT";
		String descripcion = "Pago";
		BigDecimal cantidad = new BigDecimal(1.0);
		BigDecimal valorUnitario = BigDecimal.ZERO;
		BigDecimal importe = BigDecimal.ZERO;
		
		try {
			if (pagos == null || pagos.isEmpty())
				return comprobante;
			
			properties = getFacturacionProperties();
			claveProdServ = (properties != null && properties.containsKey("pago.concepto.claveProdServ") ? valueToString(properties.getProperty("pago.concepto.claveProdServ")) : claveProdServ);
			claveUnidad = (properties != null && properties.containsKey("pago.concepto.claveUnidad") ? valueToString(properties.getProperty("pago.concepto.claveUnidad")) : claveUnidad);
			descripcion = (properties != null && properties.containsKey("pago.concepto.descripcion") ? valueToString(properties.getProperty("pago.concepto.descripcion")) : descripcion);
			cantidad = (properties != null && properties.containsKey("pago.concepto.cantidad") ? valueToBigDecimal(properties.getProperty("pago.concepto.cantidad")) : cantidad);
			valorUnitario = (properties != null && properties.containsKey("pago.concepto.valorUnitario") ? valueToBigDecimal(properties.getProperty("pago.concepto.valorUnitario")) : valorUnitario);
			importe = (properties != null && properties.containsKey("pago.concepto.importe") ? valueToBigDecimal(properties.getProperty("pago.concepto.importe")) : importe);
			
			this.ifzEmpCertificado.setInfoSesion(this.infoSesion);
			empCertificado = this.ifzEmpCertificado.findByEmpresa(getIdEmpresa());
			if (empCertificado == null || empCertificado.getId() == null || empCertificado.getId() <= 0L)
				return comprobante;
			
			pojoPago = pagos.get(0);
			factura = pojoPago.getIdFactura();
			serie = (serie == null || "".equals(serie.trim()) ? pojoPago.getSerie() : serie);
			folio = (folio == null || "".equals(folio.trim()) ? pojoPago.getFolio() : folio);
			if (serie == null || "".equals(serie)) {
				log.error("No se puede timbrar el pago, No ha sido configurado ninguna serie para el complemento de pago");
				return comprobante;
			}
			
			emisor = new Emisor();
			emisor.setRfc(factura.getIdEmpresa().getRfc());
			emisor.setNombre(factura.getIdEmpresa().getEmpresa());
			emisor.setRegimenFiscal(factura.getIdEmpresa().getCodigoRegimenFiscal());
			
			receptor = new Receptor();
			receptor.setRfc(factura.getRfc());
			receptor.setNombre(factura.getCliente());
			receptor.setUsoCFDI(CUsoCFDI.P_01); // Por Definir
			
			concepto = new Concepto();
			concepto.setClaveProdServ(claveProdServ);
			concepto.setClaveUnidad(claveUnidad);
			concepto.setDescripcion(descripcion);
			concepto.setCantidad(cantidad);
			concepto.setValorUnitario(valorUnitario);
			concepto.setImporte(importe);
			
			conceptos = new Conceptos();
			conceptos.getConcepto().add(concepto);
			
			pagado = 0;
			for (FacturaPagosExt pago : pagos) {
				pagado += pago.getPago().doubleValue();
				factura = pago.getIdFactura();
				
				doctoRelacionado = new DoctoRelacionado();
				doctoRelacionado.setIdDocumento(factura.getUuid());
				doctoRelacionado.setMonedaDR(CMoneda.fromValue(factura.getAbreviaturaMoneda()));
				doctoRelacionado.setMetodoDePagoDR(CMetodoPago.fromValue(factura.getIdMetodoPago().getValor()));
				doctoRelacionado.setSerie(factura.getSerie());
				doctoRelacionado.setFolio(factura.getFolio());
				if (! pago.getIdMoneda().getAbreviacion().equals(factura.getAbreviaturaMoneda())) 
					doctoRelacionado.setTipoCambioDR(formatoNumerico(factura.getTipoCambio()));
				doctoRelacionado.setNumParcialidad(BigInteger.valueOf(pago.getParcialidad()));
				doctoRelacionado.setImpSaldoAnt(formatoNumerico(pago.getSaldoAnterior()));
				doctoRelacionado.setImpPagado(formatoNumerico(pago.getPago()));
				doctoRelacionado.setImpSaldoInsoluto(formatoNumerico(pago.getSaldoInsoluto()));

				if (doctosRelacionados == null)
					doctosRelacionados = new ArrayList<DoctoRelacionado>();
				doctosRelacionados.add(doctoRelacionado);
			}
			
			// Fecha del Pago
			format = new SimpleDateFormat(FORMATER);
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(pojoPago.getFecha()));
			
			pago10 = new Pago();
			pago10.setFechaPago(date);
			pago10.setFormaDePagoP(pojoPago.getIdFormaPago().getClaveSat());
			pago10.setMonedaP(CMoneda.fromValue(pojoPago.getIdMoneda().getAbreviacion()));
			pago10.setMonto(formatoNumerico(pagado));
			if (! "MXN".equals(pojoPago.getIdMoneda().getAbreviacion()))
				pago10.setTipoCambioP(formatoNumerico(pojoPago.getTipoCambio()));
			pago10.setNumOperacion(pojoPago.getReferenciaFormaPago());
			if ("01".equals(pojoPago.getIdFormaPago().getClaveSat()) || pojoPago.getReferenciaFormaPago() == null || "".equals(pojoPago.getReferenciaFormaPago().trim()))
				pago10.setNumOperacion(pojoPago.getIdFormaPago().getClaveSat());
			pago10.getDoctoRelacionado().addAll(doctosRelacionados);
			
			pagos10 = new Pagos();
			pagos10.setVersion("1.0");
			pagos10.getPago().add(pago10);
			
			complemento = new Complemento();
			complemento.getAny().add(pagos10);
			
			// Fecha emision del Comprobante (Complemento Pago)
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(Calendar.getInstance().getTime())); // factura.getFechaEmision()));
			
			comprobante = new Comprobante();
			comprobante.setVersion("3.3");
			comprobante.setTipoDeComprobante(CTipoDeComprobante.P);
			comprobante.setFecha(date);
			comprobante.setCertificado(new String(Base64.encodeBase64(empCertificado.getCertificadoData())));
			comprobante.setLugarExpedicion(empCertificado.getIdEmpresa().getColonia().getCp());
			comprobante.setNoCertificado(empCertificado.getNoCertificado());
			comprobante.setSello("");
			comprobante.setSerie(serie); // comprobante.setSerie(serieComplementoPago); 
			comprobante.setFolio(folio); // comprobante.setFolio(folioComplementoPago); 
			comprobante.setMoneda(CMoneda.XXX);
			comprobante.setSubTotal(BigDecimal.ZERO);
			comprobante.setTotal(BigDecimal.ZERO);
			comprobante.setEmisor(emisor);
			comprobante.setReceptor(receptor);
			comprobante.setConceptos(conceptos);
			comprobante.getComplemento().add(complemento);
		} catch (Exception e) {
			log.error("Ocurrio un problema Logica_CuentasPorPagar.PagosGastosFac.genComprobante(List pagos)", e);
			throw e;
		}
		
		return comprobante;
	}

	private Properties getFacturacionProperties() {
		Properties properties = null;

		try {
			properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("/net/giro/cxc/logica/facturacion.properties"));
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar cargar el archivo 'facturacion.properties'.", e);
			properties = null;
		}
		
		return properties;
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
	
	private BigDecimal formatoNumerico(double value) {
		return formatoNumerico(new BigDecimal(value));
	}
	
	private BigDecimal formatoNumerico(BigDecimal value) {
		return ((value != null) ? new BigDecimal((new DecimalFormat("#0.00####")).format(value)) : BigDecimal.ZERO);
	}

	private void cancelacionPago(Long idTimbre) {
		MensajeTopic msgTopic = null;
		String comando = "";
		// ----------------------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (idTimbre == null || idTimbre <= 0L)
				return;
			
			// Generamos evento para Factura
			target = idTimbre.toString();

			// lanzamos evento
			log.info("\n\n>>>>> Evento para generar Solicitud de Cancelacion de Pagos, Timbre: " + idTimbre + "\n");
			msgTopic = new MensajeTopic(TopicEventosCXC.CANCELACION_PAGO, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/CXC\n\n" + comando + "\n\n", e);
		}
	}
	
	private List<FacturaPagos> saveList(List<FacturaPagos> entities, boolean actualizarSaldos) throws Exception {
		String agrupadores = "";
		String agrupador = "";
		int agrupar = 0;
		
		try {
			if (entities.size() > 1) {
				for (FacturaPagos entity : entities) {
					if (entity.getAgrupador() != null && ! "".equals(entity.getAgrupador().trim())) {
						agrupador = entity.getAgrupador();
						agrupadores += (! agrupadores.contains(agrupador)) ? (! "".equals(agrupador) ? "|" : "") + agrupador : "";
						continue;
					}
					agrupar++;
				}
				
				if (agrupar > 0) {
					agrupador = this.agrupador();
					for (FacturaPagos entity : entities) {
						if (entity.getAgrupador() != null && ! "".equals(entity.getAgrupador()))
							continue;
						entity.setAgrupador(agrupador);
					}
				}
			}
			
			entities = this.ifzFacturaPagos.saveOrUpdateList(entities, getCodigoEmpresa());
			if (entities != null && ! entities.isEmpty() && actualizarSaldos) {
				agrupadores = "";
				for (FacturaPagos entity : entities) {
					if (entity.getAgrupador() != null && ! "".equals(entity.getAgrupador()) && agrupadores.contains(entity.getAgrupador()))
						continue;
					agrupadores += "|" + entity.getAgrupador();
					actualizaSaldo(entity.getId());
				}
			}
			
			return entities;
		} catch (Exception re) { 
			log.error("Ocurrio un problema al intentar guardar la lista de pagos", re);
			throw re;
		}
	}
	
	private void actualizaSaldo(Long idFacturaPago) {
		MensajeTopic msgTopic = null;
		String comando = "";
		// ----------------------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (idFacturaPago == null || idFacturaPago <= 0L)
				return;
			
			// Generamos evento para Factura
			target = idFacturaPago.toString();
			
			// lanzamos evento
			msgTopic = new MensajeTopic(TopicEventosCXC.SALDO, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/CXC\n\n" + comando + "\n\n", e);
		}
	}
	
	private void actualizaSaldo(List<FacturaPagos> pagos) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		String comando = "";
		List<Long> facturas = null;
		// ----------------------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (pagos == null || pagos.isEmpty())
				return;
			
			facturas = new ArrayList<Long>();
			for (FacturaPagos pago : pagos) {
				if ("0".equals(referencia))
					referencia = pago.getAgrupador();
				facturas.add(pago.getIdFactura().getId());
			}
			
			// Generamos evento para Factura
			gson = new Gson();
			atributos = gson.toJson(facturas);
			
			// lanzamos evento
			msgTopic = new MensajeTopic(TopicEventosCXC.SALDO, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/CXC\n\n" + comando + "\n\n", e);
		}
	}

	private void actualizaSaldoByFacturas(List<Long> facturas) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		String comando = "";
		// ----------------------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (facturas == null || facturas.isEmpty())
				return;
			
			// Generamos evento para Factura
			gson = new Gson();
			atributos = gson.toJson(facturas);
			
			// lanzamos evento
			msgTopic = new MensajeTopic(TopicEventosCXC.SALDO, target, referencia, atributos, this.infoSesion);
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
	public int obtenerParcialidad(Long idFactura) throws Exception {
		List<Object> nativeResult = null;
		String queryString = "";
		int parcialidad = 0;
		
		try {
			queryString += "select coalesce(max(parcialidad),0) + 1 as parcialidad from factura_pagos ";
			queryString += "where id_factura = :idFactura and estatus = 0;";
			queryString = queryString.replace(":idFactura", idFactura.toString());
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult == null || nativeResult.isEmpty())
				return parcialidad;
			parcialidad = (Integer) nativeResult.get(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al obtener la prcialidad para el pago de la Factura indicada: " + idFactura, e);
			parcialidad = 0;
		}
		
		return (parcialidad <= 0 ? 1 : parcialidad);
	}
}
