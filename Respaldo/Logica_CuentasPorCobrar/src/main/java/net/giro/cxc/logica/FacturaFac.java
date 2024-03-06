package net.giro.cxc.logica;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.Obra;
import net.giro.adp.dao.ObraDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.logica.MensajeTransaccionRem;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.Provisiones;
import net.giro.cxc.dao.FacturaDAO;
import net.giro.respuesta.Respuesta;

@Stateless
public class FacturaFac implements FacturaRem {
	private static Logger log = Logger.getLogger(FacturaFac.class);
	private InitialContext ctx;
	private FacturaDAO ifzfactura;
	private FacturaPagosRem ifzPagos;
	private ProvisionesRem ifzProvision;
	private ObraDAO ifzObras;
	private MensajeTransaccionRem ifzMsgTransaccion;
	private ConvertExt convertidor;
	private static String orderBy;
	
	
	public FacturaFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
            this.ifzfactura = (FacturaDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturaImp!net.giro.cxc.dao.FacturaDAO");
            this.ifzPagos = (FacturaPagosRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosFac!net.giro.cxc.logica.FacturaPagosRem");
            this.ifzProvision = (ProvisionesRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//ProvisionesFac!net.giro.cxc.logica.ProvisionesRem");
            this.ifzObras = (ObraDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraImp!net.giro.adp.dao.ObraDAO");
            this.ifzMsgTransaccion = (MensajeTransaccionRem) this.ctx.lookup("ejb:/Logica_Contabilidad//MensajeTransaccionFac!net.giro.contabilidad.logica.MensajeTransaccionRem");
			
			this.ifzMsgTransaccion.showSystemOuts(false);
			
            this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void showSystemOuts(boolean value) { this.convertidor.setMostrarSystemOut(value); }
	
	@Override
	public void orderBy(String orderBy) { FacturaFac.orderBy = orderBy; }

	@Override
	public Long save(Factura entity) throws ExcepConstraint {
		try {
			return this.ifzfactura.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public Long save(FacturaExt entityExt) throws ExcepConstraint {
		try {
			Factura entity = this.convertidor.FacturaExtToFactura(entityExt);
			return this.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public List<Factura> save(List<Factura> listEntities) throws Exception {
		try {
			return this.ifzfactura.saveOrUpdateList(listEntities);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.FacturaFac.save(listEntities)", e);
			throw e;
		}
	}

	@Override
	public void delete(Factura entity) throws ExcepConstraint {
		try {
			this.ifzfactura.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public void delete(FacturaExt entityExt) throws ExcepConstraint {
		try {
			Factura entity = this.convertidor.FacturaExtToFactura(entityExt);
			this.delete(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void update(Factura entity) throws ExcepConstraint {
		try {
			this.ifzfactura.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(FacturaExt entityExt) throws ExcepConstraint {
		try {
			Factura entity = this.convertidor.FacturaExtToFactura(entityExt);
			this.update(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public Factura findById(Long id) {
		try {
			return this.ifzfactura.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public List<Factura> findAll() {
		try {
			this.ifzfactura.orderBy(orderBy);
			return this.ifzfactura.findAll();
		} catch (RuntimeException re) {		
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findByProperty(String propertyName, final Object value) {
		try {
			this.ifzfactura.orderBy(orderBy);
			return this.ifzfactura.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	public List<Factura> findByProperty(String propertyName, Object value, int max) {
		try {
			this.ifzfactura.orderBy(orderBy);
			return this.ifzfactura.findByProperty(propertyName, value, max);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	public List<Factura> findByProperty(String propertyName, Object value, int tipoObra, int max) {
		try {
			this.ifzfactura.orderBy(orderBy);
			return this.ifzfactura.findByProperty(propertyName, value, tipoObra, max);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<FacturaExt> findByPropertyExt(String propertyName, final Object value) throws Exception {
		List<FacturaExt> listaExt = new ArrayList<FacturaExt>();
		
		try {
			List<Factura> lista = this.findByProperty(propertyName, value);
			for (Factura var : lista) {
				FacturaExt aux = this.convertidor.FacturaToFacturaExt(var);
				listaExt.add(aux);
			}
		} catch (RuntimeException re) {
			throw re;
		}
		
		return listaExt;
	}
	
	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value) {
		try {
			this.ifzfactura.orderBy(orderBy);
			return this.ifzfactura.findLikeProperty(propertyName, value.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value, int max) {
		try {
			this.ifzfactura.orderBy(orderBy);
			return this.ifzfactura.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			throw e;
		} finally {
			orderBy = null;
		}
	}	

	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value, int tipoObra, int max) {
		try {
			this.ifzfactura.orderBy(orderBy);
			return this.ifzfactura.findLikeProperty(propertyName, value.toString(), tipoObra, max);
		} catch (Exception e) {
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<FacturaExt> findLikePropertyExt(String propertyName, Object value, int tipoObra, int max) throws Exception {
		List<FacturaExt> listaExt = new ArrayList<FacturaExt>();
		
		try {
			List<Factura> lista = this.findLikeProperty(propertyName, value, tipoObra, max);
			for (Factura var : lista) {
				FacturaExt pojoExt = this.convertidor.FacturaToFacturaExt(var);
				listaExt.add(pojoExt);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Factura> findByPropertyPojoCompleto(String propertyName, Object value, int tipo){
		try{
			this.ifzfactura.orderBy(orderBy);
			return this.ifzfactura.findByPropertyPojoCompleto(propertyName, value, tipo);
		} catch(RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<FacturaExt> findByPropertyPojoCompletoExt(String propertyName, Object value, int tipo) throws Exception{
		List<FacturaExt> listaExt = new ArrayList<FacturaExt>();
		
		try{
			List<Factura> lista = this.findByPropertyPojoCompleto(propertyName, value, tipo);
			for (Factura factura : lista) {
				FacturaExt pojoAux = this.convertidor.FacturaToFacturaExt(factura);
				listaExt.add(pojoAux);
			}
		} catch(RuntimeException re) {
			throw re;
		}

		return listaExt;
	}

	@Override
	public List<Obra> ObrafindByProperty(String propertyName, final Object value) throws Exception {
		try {
			this.ifzfactura.orderBy(orderBy);
			return this.ifzObras.findByProperty(propertyName, value, 1);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	public List<Factura> findLikeProperties(HashMap<String, Object> params) throws Exception {
		try {
			this.ifzfactura.orderBy(orderBy);
			return this.ifzfactura.findLikeProperties(params);
		} catch (Exception re) {		
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<FacturaExt> findLikePropertiesExt(HashMap<String, Object> params) throws Exception {
		List<FacturaExt> listaExt = new ArrayList<FacturaExt>();
		
		try {
			List<Factura> lista = this.findLikeProperties(params);
			for (Factura var : lista) {
				FacturaExt pojoExt = this.convertidor.FacturaToFacturaExt(var);
				listaExt.add(pojoExt);
			}
			
			return listaExt;
		} catch (Exception re) {		
			throw re;
		}
	}
	
	@Override
	public Factura convertir(FacturaExt pojoFactura) throws Exception {
		return this.convertidor.FacturaExtToFactura(pojoFactura);
	}
	
	@Override
	public FacturaExt convertir(Factura pojoFactura) throws Exception {
		return this.convertidor.FacturaToFacturaExt(pojoFactura);
	}

	@Override
	public Respuesta cancelarFactura(Long entityId, long usuarioId) throws Exception {
		try {
			return this.cancelarFactura(this.ifzfactura.findById(entityId), usuarioId);
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.cancelarFactura(entityId, usuarioId)\n", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta cancelarFactura(Factura entity, long usuarioId) throws Exception {
		Respuesta respuesta = new Respuesta();
		
		try {
			// proceso para cancelar timbre si corresponde
			if (validarTimbre(entity)) {
				// TODO Proceso de Cancelacion
			}
			
			entity.setEstatus(0);
			entity.setCanceladoPor(usuarioId);
			entity.setFechaCancelacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(usuarioId);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			this.update(entity);
			
			respuesta = transaccionCancelacionFactura(entity, usuarioId, 1L);
			
			// Añadimos la factura al resultado
			respuesta.getBody().addValor("factura", entity);
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.cancelarFactura(entity, usuarioId)\n", e);
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public Respuesta cancelarFactura(FacturaExt entity, long usuarioId) throws Exception {
		try {
			return this.cancelarFactura(this.convertir(entity), usuarioId);
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.cancelarFactura(entityExt, usuarioId)\n", e);
			throw e;
		}
	}

	@Override
	public Respuesta provisionar(Long entityId, double montoProvision, long usuarioId) throws Exception {
		Factura pojoFactura = null;
		
		try {
			pojoFactura = this.ifzfactura.findById(entityId);
			return this.provisionar(pojoFactura, montoProvision, usuarioId);
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
	public Respuesta provisionar(FacturaExt entityExt, double montoProvision, long usuarioId) throws Exception {
		Respuesta respuesta = new Respuesta();
		Factura pojoFactura = null;
		
		try {
			pojoFactura = this.convertir(entityExt);
			respuesta = this.provisionar(pojoFactura, montoProvision, usuarioId);
			pojoFactura = (Factura) respuesta.getBody().getValor("factura");
			respuesta.getBody().addValor("factura", this.convertir(pojoFactura));
			return respuesta;
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.provisionar(entityExt, montoProvision, usuarioId)\n", e);
			throw e;
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
	public BigDecimal findSaldoFactura(Long entityId) throws Exception {
		try {
			return this.ifzPagos.findSaldoByFactura(entityId);
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findSaldoFactura(entityId)\n", e);
			throw e;
		}
	}

	@Override
	public BigDecimal findSaldoFactura(Factura entity) throws Exception {
		try {
			return this.ifzPagos.findSaldoByFactura(entity);
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findSaldoFactura(entity)\n", e);
			throw e;
		}
	}

	@Override
	public BigDecimal findSaldoFactura(FacturaExt entityExt) throws Exception {
		try {
			return this.ifzPagos.findSaldoByFactura(entityExt);
		} catch (Exception e) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturaFac.findSaldoFactura(entityExt)\n", e);
			throw e;
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Respuesta transaccionProvision(Factura entity, double montoProvision, Long idEmpresa) throws Exception {
		Respuesta respuesta = new Respuesta();
		MensajeTransaccion msg = null;
		Long idMensaje = 0L;
		Long idTransaccion = 0L;
		Long idFormaPago = 0L;
		Long idMoneda = 0L;
		Long idCliente = 0L;
		String descMoneda = "";
		String referencia = "";
		String nombreCliente = "";
		String tipoPersona = "";
		String tracking = "";
		Obra pojoObra = null;
		boolean facturaCredito = false;
		
		try {
			if (entity == null) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No indico Factura");
				return respuesta;
			}
			
			if (idEmpresa == null || idEmpresa <= 0L)
				idEmpresa = 1L;
			
			pojoObra = this.ifzObras.findById(entity.getIdObra());
			if (pojoObra == null) {
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se pudo obtener los datos del Cliente");
				return respuesta;
			}
			
			idCliente = pojoObra.getIdCliente();
			nombreCliente = pojoObra.getNombreCliente();
			tipoPersona = pojoObra.getTipoCliente();
			idMoneda = entity.getIdMoneda();
			descMoneda = entity.getDescripcionMoneda();
			
			if ("C".equals(entity.getTipo())) 
				facturaCredito = true;
			
			// Determinamos Transaccion
			tracking = "Transaccion 1014 disparada desde FacturaFac. Factura " + ((facturaCredito) ? " Credito " : " Contado ") + entity.getId();
			idTransaccion = 1014L; 

			// Forma de Pago y Referencia
			idFormaPago = entity.getIdMetodoPago();
			referencia = "";

			msg = new MensajeTransaccion();
			msg.setIdTransaccion(idTransaccion);
			msg.setIdOperacion(entity.getId());
			msg.setIdMoneda(idMoneda);
			msg.setDescripcionMoneda(descMoneda);
			msg.setImporte(new BigDecimal(montoProvision));
			msg.setIdPersonaReferencia(idCliente);
			msg.setNombrePersonaReferencia(nombreCliente);
			msg.setTipoPersona(tipoPersona);
			msg.setReferencia("");
			msg.setIdFormaPago(idFormaPago);
			msg.setReferenciaFormaPago(referencia);
			msg.setIdUsuarioCreacionRegistro(entity.getCreadoPor());
			msg.setIdSucursal(entity.getIdSucursal());
			msg.setCreadoPor(entity.getCreadoPor());
			msg.setFechaCreacion(entity.getFechaCreacion());
			msg.setAnuladoPor(0L);
			msg.setFechaAnulacion(null);
			msg.setEstatus(0);
			msg.setFechaRegistro(entity.getFechaEmision());
			msg.setIdEmpresa(idEmpresa);
			
			// Registramos el mensaje
			idMensaje = this.ifzMsgTransaccion.save(msg);
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
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Respuesta transaccionCancelacionFactura(Factura entity, Long usuarioId, Long idEmpresa) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		Respuesta respuesta = new Respuesta();
		MensajeTransaccion msg = null;
		Long idMensaje = 0L;
		Long idTransaccion = 0L;
		Long idFormaPago = 0L;
		Long idMoneda = 0L;
		Long idCliente = 0L;
		String descMoneda = "";
		String referencia = "";
		String nombreCliente = "";
		String tipoPersona = "";
		String tracking = "";
		Obra pojoObra = null;
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
			
			pojoObra = this.ifzObras.findById(entity.getIdObra());
			if (pojoObra == null) {
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se pudo obtener los datos del Cliente");
				return respuesta;
			}
			
			idCliente = pojoObra.getIdCliente();
			nombreCliente = pojoObra.getNombreCliente();
			tipoPersona = pojoObra.getTipoCliente();
			idMoneda = entity.getIdMoneda();
			descMoneda = entity.getDescripcionMoneda();
			
			if ("C".equals(entity.getTipo())) 
				facturaCredito = true;
			
			// Determinamos Transaccion
			if (Integer.parseInt(formatter.format(entity.getFechaTimbrado())) < Integer.parseInt(formatter.format(Calendar.getInstance().getTime()))) {
				tracking = "Transaccion 1031 (Cancelacion de Facturas de Ingresos de meses anteriores) disparada desde FacturaFac. Factura " + ((facturaCredito) ? " Credito " : " Contado ") + entity.getId();
				idTransaccion = 1031L; 
			} else {
				tracking = "Transaccion 1021 (Cancelacion de Factura de Ingresos) disparada desde FacturaFac. Factura " + ((facturaCredito) ? " Credito " : " Contado ") + entity.getId();
				idTransaccion = 1021L;
			}
			
			// Forma de Pago y Referencia
			idFormaPago = entity.getIdMetodoPago();
			referencia = "";

			msg = new MensajeTransaccion();
			msg.setIdTransaccion(idTransaccion);
			msg.setIdOperacion(entity.getId());
			msg.setIdMoneda(idMoneda);
			msg.setDescripcionMoneda(descMoneda);
			msg.setImporte(entity.getTotal());
			msg.setIdPersonaReferencia(idCliente);
			msg.setNombrePersonaReferencia(nombreCliente);
			msg.setTipoPersona(tipoPersona);
			msg.setReferencia("");
			msg.setIdFormaPago(idFormaPago);
			msg.setReferenciaFormaPago(referencia);
			msg.setIdUsuarioCreacionRegistro(usuarioId);
			msg.setIdSucursal(entity.getIdSucursal());
			msg.setCreadoPor(entity.getCreadoPor());
			msg.setFechaCreacion(entity.getFechaCreacion());
			msg.setAnuladoPor(0L);
			msg.setFechaAnulacion(null);
			msg.setEstatus(0);
			msg.setFechaRegistro(entity.getFechaEmision());
			msg.setIdEmpresa(idEmpresa);
			
			// Registramos el mensaje
			idMensaje = this.ifzMsgTransaccion.save(msg);
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
	
	private boolean validarTimbre(Factura entity) {
		return true;
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