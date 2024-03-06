package net.giro.cxc.logica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
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
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.beans.FacturaPagosExt;
import net.giro.cxc.dao.FacturaPagosDAO;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.logica.CtasBancoRem;

@Stateless
public class FacturaPagosFac implements FacturaPagosRem {
	private static Logger log = Logger.getLogger(FacturaPagosFac.class);
	private InitialContext ctx;
	private FacturaPagosDAO ifzFacturaPagos;
	private FacturaRem ifzFacturas;
	private CtasBancoRem ifzCtas;
	private ObraDAO ifzObras;
	private MensajeTransaccionRem ifzMsgTransaccion;
	private ConvertExt convertidor;
	
	
 	public FacturaPagosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            
            this.ctx = new InitialContext(p);
            this.ifzFacturas 	 	= (FacturaRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
            this.ifzFacturaPagos 	= (FacturaPagosDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturaPagosImp!net.giro.cxc.dao.FacturaPagosDAO");
            this.ifzCtas 			= (CtasBancoRem) this.ctx.lookup("ejb:/Logica_TYG//CtasBancoFac!net.giro.tyg.logica.CtasBancoRem");
            this.ifzObras 			= (ObraDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraImp!net.giro.adp.dao.ObraDAO");
            this.ifzMsgTransaccion 	= (MensajeTransaccionRem) this.ctx.lookup("ejb:/Logica_Contabilidad//MensajeTransaccionFac!net.giro.contabilidad.logica.MensajeTransaccionRem");
			
			this.ifzMsgTransaccion.showSystemOuts(false);
			
            this.convertidor = new ConvertExt();
            this.convertidor.setFrom("FacturaPagosFac");
            this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaPagosFac", e);
			ctx = null;
		}
	}

 	
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}
	
	@Override
	public long save(FacturaPagos entity) throws ExcepConstraint {
		try {
			return this.ifzFacturaPagos.save(entity);
		} catch (RuntimeException re) {	
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public long save(FacturaPagosExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.FacturaPagosExtToFacturaPagos(entityExt));
		} catch (RuntimeException re) {	
			log.error("Error en el método save", re);
			throw re;
		}
	}
	
	@Override
	public void update(FacturaPagos entity) throws ExcepConstraint {
		try {
			this.ifzFacturaPagos.update(entity);
		} catch (RuntimeException re) {	
			log.error("Error en el método update", re);
			throw re;
		}
	}

	@Override
	public void update(FacturaPagosExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.FacturaPagosExtToFacturaPagos(entityExt));
		} catch (RuntimeException re) {	
			log.error("Error en el método update", re);
			throw re;
		}
	}
	
	@Override
	public void delete(FacturaPagos entity) throws ExcepConstraint {
		try {
			this.ifzFacturaPagos.delete(entity.getId());
		} catch (RuntimeException re) {	
			log.error("Error en el método delete", re);
			throw re;
		}
	}
	
	@Override
	public void delete(FacturaPagosExt entity) throws ExcepConstraint {
		try {
			this.ifzFacturaPagos.delete(entity.getId());
		} catch (RuntimeException re) {	
			log.error("Error en el método delete", re);
			throw re;
		}
	}

	@Override
	public FacturaPagos findById(long id) {
		try {
			return this.ifzFacturaPagos.findById(id);
		} catch (RuntimeException re) {	
			log.error("Error en el método findById", re);
			throw re;
		}
	}

	@Override
	public FacturaPagosExt findByIdExt(long id) throws Exception {
		try {
			return this.convertidor.FacturaPagosToFacturaPagosExt(this.findById(id));
		} catch (RuntimeException re) {	
			log.error("Error en el método findByIdExt", re);
			throw re;
		}
	}
	
	@Override
	public List<FacturaPagos> findAll() {
		try {
			return this.ifzFacturaPagos.findAll();
		} catch (RuntimeException re) {	
			log.error("Error en el método findAll", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagos> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzFacturaPagos.findByProperty(propertyName, value, 0);
		} catch (RuntimeException re) {	
			log.error("Error en el método safindByPropertyve", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagosExt> findByPropertyExt(String propertyName, Object value) throws Exception {
		List<FacturaPagosExt> listaExt = new ArrayList<FacturaPagosExt>();
		
		try {
			List<FacturaPagos> lista = this.findByProperty(propertyName, value);
			for (FacturaPagos var : lista) {
				FacturaPagosExt pojoExt = this.convertidor.FacturaPagosToFacturaPagosExt(var);
				listaExt.add(pojoExt);
			}
		} catch (Exception e) {
			log.error("Error en el método findByPropertyExt", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<FacturaPagos> findLikeProperty(String propertyName, String value) {
		try {
			return this.ifzFacturaPagos.findLikeProperty(propertyName, value, 0);
		} catch (RuntimeException re) {	
			log.error("Error en el método findLikeProperty", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagosExt> findLikePropertyExt(String propertyName, String value) throws Exception {
		List<FacturaPagosExt> listaExt = new ArrayList<FacturaPagosExt>();
		
		try {
			List<FacturaPagos> lista = this.findLikeProperty(propertyName, value);
			for (FacturaPagos var : lista) {
				FacturaPagosExt pojoExt = this.convertidor.FacturaPagosToFacturaPagosExt(var);
				listaExt.add(pojoExt);
			}
		} catch (Exception e) {
			log.error("Error en el método findLikePropertyExt", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<FacturaPagosExt> findLikeFolioFactura(String value) throws Exception {
		List<FacturaPagosExt> listaExt = new ArrayList<FacturaPagosExt>();
		
		try {
			List<Factura> listaFacturas = this.ifzFacturas.findLikeProperty("fac.folioFactura", value);
			if (listaFacturas != null && ! listaFacturas.isEmpty()) {
				List<Long> values = new ArrayList<Long>();
				for (Factura var : listaFacturas) {
					values.add(var.getId());
				}
				
				List<FacturaPagos> listPagos = this.ifzFacturaPagos.findInProperty("idFactura", values, 0);
				for (FacturaPagos var : listPagos) {
					FacturaPagosExt pojoExt = this.convertidor.FacturaPagosToFacturaPagosExt(var);
					listaExt.add(pojoExt);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public BigDecimal findSaldoByFactura(Long facturaId) throws Exception {
		try {
			return this.findSaldoByFactura(this.ifzFacturas.findById(facturaId)); 
		} catch (Exception e) {
			log.error("Error en el método findSaldoByFactura(facturaId)", e);
			throw e;
		}
	}

	@Override
	public BigDecimal findSaldoByFactura(Factura factura) throws Exception {
		double saldoAux = 0D;
		
		try {
			List<FacturaPagos> listaPagos = this.ifzFacturaPagos.findByProperty("fac.id", factura.getId(), 0);
			for (FacturaPagos var : listaPagos) {
				saldoAux += var.getMonto().doubleValue();
			} 
		} catch (Exception e) {
			log.error("Error en el método findSaldoByFactura", e);
			throw e;
		}
		
		return new BigDecimal((factura.getSubtotal().doubleValue() + factura.getImpuestos()) - saldoAux);
	}

	@Override
	public BigDecimal findSaldoByFactura(FacturaExt factura) throws Exception {
		try {
			return this.findSaldoByFactura(this.ifzFacturas.convertir(factura)); 
		} catch (Exception e) {
			log.error("Error en el método findSaldoByFactura(facturaId)", e);
			throw e;
		}
	}

	@Override
	public List<FacturaPagosExt> findLikeBeneficiario(String value) throws Exception {
		List<FacturaPagosExt> listaExt = new ArrayList<FacturaPagosExt>();
		
		try {
			List<FacturaPagos> listPagos = this.findLikeProperty("beneficiario", value);
			for (FacturaPagos var : listPagos) {
				FacturaPagosExt pojoExt = this.convertidor.FacturaPagosToFacturaPagosExt(var);
				listaExt.add(pojoExt);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<FacturaPagosExt> findLikeCuentaBancaria(String value) throws Exception {
		List<FacturaPagosExt> listaExt = new ArrayList<FacturaPagosExt>();
		
		try {
			List<CtasBanco> listaCuentas = this.ifzCtas.findLikeColumnName("numeroDeCuenta", value);
			if (! listaCuentas.isEmpty()) {
				List<Long> values = new ArrayList<Long>();
				for (CtasBanco var : listaCuentas) {
					values.add(var.getId());
				}
				
				List<FacturaPagos> listPagos = this.ifzFacturaPagos.findInProperty("idCuentaDeposito", values, 0);
				for (FacturaPagos var : listPagos) {
					FacturaPagosExt pojoExt = this.convertidor.FacturaPagosToFacturaPagosExt(var);
					listaExt.add(pojoExt);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public Respuesta enviarTransaccion(Long entityId, Long idEmpresa) throws Exception {
		try {
			return this.enviarTransaccion(this.findByIdExt(entityId), idEmpresa);
		} catch (Exception e) {
			log.error("Error Logica_CuentasPorCobrar.FacturaPagosFac.enviarTransaccion(entityId, idEmpresa)", e);
			throw e;
		}
	}

	@Override
	public Respuesta enviarTransaccion(FacturaPagos entity, Long idEmpresa) throws Exception {
		try {
			return this.enviarTransaccion(this.convertidor.FacturaPagosToFacturaPagosExt(entity), idEmpresa);
		} catch (Exception e) {
			log.error("Error Logica_CuentasPorCobrar.FacturaPagosFac.enviarTransaccion(entity, idEmpresa)", e);
			throw e;
		}
	}
	
	@Override
	public Respuesta enviarTransaccion(FacturaPagosExt entity, Long idEmpresa) throws Exception {
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
		
		try {
			if (entity == null) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No indico Factura");
				return respuesta;
			}
			
			if (idEmpresa == null || idEmpresa <= 0L)
				idEmpresa = 1L;
			pojoObra = this.ifzObras.findById(entity.getIdFactura().getIdObra().getId());
			if (pojoObra == null) {
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se pudo obtener los datos del Cliente");
				return respuesta;
			}
			
			idCliente = pojoObra.getIdCliente();
			nombreCliente = pojoObra.getNombreCliente();
			tipoPersona = pojoObra.getTipoCliente();
			idMoneda = entity.getIdCuentaDeposito().getMoneda().getId();
			descMoneda = entity.getIdCuentaDeposito().getMoneda().getNombre();
			
			// Determinamos Transaccion
			tracking = "Transaccion 1013 disparada desde FacturaPagosFac. Pago " + entity.getId() + " a Factura " + entity.getIdFactura().getId();
			idTransaccion = 1013L; 

			// Forma de Pago y Referencia
			idFormaPago = entity.getIdFormaPago().getId();
			referencia = entity.getReferenciaFormaPago();

			msg = new MensajeTransaccion();
			msg.setIdTransaccion(idTransaccion);
			msg.setIdOperacion(entity.getId());
			msg.setIdMoneda(idMoneda);
			msg.setDescripcionMoneda(descMoneda);
			msg.setImporte(entity.getMonto());
			msg.setIdPersonaReferencia(idCliente);
			msg.setNombrePersonaReferencia(nombreCliente);
			msg.setTipoPersona(tipoPersona);
			msg.setReferencia("");
			msg.setIdFormaPago(idFormaPago);
			msg.setReferenciaFormaPago(referencia);
			msg.setIdUsuarioCreacionRegistro(entity.getCreadoPor());
			msg.setIdSucursal(entity.getIdFactura().getIdSucursal().getId());
			msg.setCreadoPor(entity.getCreadoPor());
			msg.setFechaCreacion(entity.getFechaCreacion());
			msg.setAnuladoPor(0L);
			msg.setFechaAnulacion(null);
			msg.setEstatus(0);
			msg.setFechaRegistro(entity.getIdFactura().getFechaEmision());
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
}
