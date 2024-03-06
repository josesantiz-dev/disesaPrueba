package net.giro.cxc;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeFactory;

import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.beans.FacturaPagosExt;
import net.giro.cxc.fe.FactElectv33;
import net.giro.cxc.fe.v33.Comprobante;
import net.giro.cxc.logica.FacturaPagosRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.logica.BancosRem;
import net.giro.tyg.logica.CuentasBancariasRem;
import net.giro.tyg.logica.FormasPagosRem;
import net.giro.tyg.logica.MonedaRem;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="facPagosAction")
public class FacturaPagosAction implements Serializable, Runnable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FacturaPagosAction.class);
	private LoginManager loginManager;
	private HttpSession httpSession;
	private InitialContext ctx;
	// Interfaces
	private FacturaPagosRem 	ifzFacPagos;
	private ConGrupoValoresRem 	ifzGpoVal;
	private FormasPagosRem		ifzFormasPagos;
	private CuentasBancariasRem 		ifzCtas;
	private BancosRem		ifzBancos;
	private MonedaRem ifzMoneda;
	// Listas
	private List<FacturaPagos> listFacPagos;
	private List<FormasPagos> listFormasPago;
	private List<CuentaBancaria> listCuentas;
	private List<Banco> listBancos;
	// Listas auxiliares
	private List<SelectItem> listFormasPagoItems;
	private List<SelectItem> listCuentasItems;
	private List<SelectItem> listBancosItems;
	// POJO's
	private List<FacturaPagosExt> listMultiPagos;
	private FacturaPagosExt currentItem;
	private FacturaPagosExt pojoFacPago;
	private ConGrupoValores pojoGpoImpuestos;
	// Busqueda principal
	private List<SelectItem> tipoBusqueda;
	private List<SelectItem> listMeses;
	private String valBusqueda;
	private String campoBusqueda;
	private Date busquedaFecha;
	private int busquedaMes;
	// Busqueda facturas
	private FacturaRem ifzFacturas;
	private List<Factura> listBusquedaFacturas;
	private Factura pojoFacturaSeleccionada;
	private List<SelectItem> tipoBusquedaFacturas;	
	private String campoBusquedaFacturas;
	private String valorBusquedaFacturas;
	private int numPaginaFacturas;
	// Variables de operacion
    private boolean band;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private int numPagina;
    private long usuarioId;
    private String usuario;
    private long idPago;
    // Variables del pago
    private double facturaLiquidado;
    private BigDecimal facturaSaldo;
    private BigDecimal montoOriginal;
    private boolean multiPago;
	// DEBUG
	private boolean isDebug;
	private Map<String,String> paramsRequest;
    
    
	public FacturaPagosAction() {
		FacesContext fc = null;
		Application app = null;
		Map<String,String> params = null;
		ValueExpression valExp = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario   = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

			params = fc.getExternalContext().getRequestParameterMap();
		    this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : params.entrySet())
		    	this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			this.ctx = new InitialContext();
			this.ifzFacturas 	= (FacturaRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacPagos 	= (FacturaPagosRem) 	this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosFac!net.giro.cxc.logica.FacturaPagosRem");
			this.ifzGpoVal 		= (ConGrupoValoresRem) 	this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzFormasPagos = (FormasPagosRem)		this.ctx.lookup("ejb:/Logica_TYG//FormasPagosFac!net.giro.tyg.logica.FormasPagosRem");
			this.ifzCtas 		= (CuentasBancariasRem)	this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
			this.ifzBancos		= (BancosRem)			this.ctx.lookup("ejb:/Logica_TYG//BancosFac!net.giro.tyg.logica.BancosRem");
			this.ifzMoneda		= (MonedaRem)			this.ctx.lookup("ejb:/Logica_TYG//MonedaFac!net.giro.tyg.logica.MonedaRem");
			//this.ifzConValores	= (ConValoresRem) 	 	this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
			this.ifzFacturas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFacPagos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFormasPagos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzBancos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMoneda.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.ifzFacturas.showSystemOuts(false);
			this.ifzFacPagos.showSystemOuts(false);
			
			this.listFacPagos = new ArrayList<FacturaPagos>();
			this.listMultiPagos = new ArrayList<FacturaPagosExt>();
			this.listFormasPago = new ArrayList<FormasPagos>();
			
			// Busqueda principal
			this.tipoBusqueda = new ArrayList<SelectItem>();
			this.tipoBusqueda.add(new SelectItem("fac.folioFactura", "Factura"));
			this.tipoBusqueda.add(new SelectItem("fac.cliente", "Cliente"));
			this.tipoBusqueda.add(new SelectItem("model.referenciaFormaPago", "Referencia"));
			this.tipoBusqueda.add(new SelectItem("fecha", "Fecha"));
			this.tipoBusqueda.add(new SelectItem("mes", "Mes"));
			this.tipoBusqueda.add(new SelectItem("model.id", "ID"));
			this.campoBusqueda = this.tipoBusqueda.get(0).getValue().toString();
			this.valBusqueda = "";
			
			this.listMeses = new ArrayList<SelectItem>();
			this.listMeses.add(new SelectItem( 1, "Enero"));
			this.listMeses.add(new SelectItem( 2, "Febrero"));
			this.listMeses.add(new SelectItem( 3, "Marzo"));
			this.listMeses.add(new SelectItem( 4, "Abril"));
			this.listMeses.add(new SelectItem( 5, "Mayo"));
			this.listMeses.add(new SelectItem( 6, "Junio"));
			this.listMeses.add(new SelectItem( 7, "Julio"));
			this.listMeses.add(new SelectItem( 8, "Agosto"));
			this.listMeses.add(new SelectItem( 9, "Septiembre"));
			this.listMeses.add(new SelectItem(10, "Octubre"));
			this.listMeses.add(new SelectItem(11, "Noviembre"));
			this.listMeses.add(new SelectItem(12, "Diciembre"));
			
			// Busqueda facturas
			this.tipoBusquedaFacturas = new ArrayList<SelectItem>();
			this.tipoBusquedaFacturas.add(new SelectItem("folioFactura", "Folio"));
			this.tipoBusquedaFacturas.add(new SelectItem("cliente", "Cliente"));
			this.tipoBusquedaFacturas.add(new SelectItem("id", "ID"));
			this.campoBusquedaFacturas = this.tipoBusquedaFacturas.get(0).getValue().toString();
			this.valorBusquedaFacturas = "";
			this.numPaginaFacturas = 1;
			
			this.listFormasPagoItems = new ArrayList<SelectItem>();
		    this.listCuentasItems = new ArrayList<SelectItem>();
		    this.listBancosItems = new ArrayList<SelectItem>();

			this.facturaSaldo = new BigDecimal(0L);
			this.pojoFacPago = new FacturaPagosExt();
			this.pojoFacPago.setIdFactura(new FacturaExt());
			this.pojoFacPago.setFecha(Calendar.getInstance().getTime());
			this.pojoFacPago.setMonto(new BigDecimal(0L));
			this.pojoFacPago.setObservaciones("");
			
			// IMPUESTOS
			// ---------------------------------------------------------------------------------------------------------
			this.pojoGpoImpuestos = this.ifzGpoVal.findByName("SYS_IMPTOS");
			if (this.pojoGpoImpuestos == null || this.pojoGpoImpuestos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
			
			// Cargamos datos 
			fillLists();

			this.numPagina = 1;
			this.facturaSaldo = BigDecimal.ZERO;
			this.montoOriginal = BigDecimal.ZERO;
			control();
		} catch (Exception e) {
			log.error("Error en constructor FacturaPagosAction", e);
		}
	}
	
	
	public void buscar() {
		String campo = "";
		
		try {
			control();
			if (this.campoBusqueda == null || "".equals(this.campoBusqueda))
				this.campoBusqueda = this.tipoBusqueda.get(0).getValue().toString();
			
			this.numPagina = 1;
			if (this.listFacPagos != null)
				this.listFacPagos.clear();
			
			log.info("FACTURA_PAGOS: Buscando ... ");
			if ("fecha".equals(this.campoBusqueda)) {
				this.listFacPagos = this.ifzFacPagos.findByProperty(this.campoBusqueda, this.busquedaFecha);
			} else {
				campo = this.campoBusqueda;
				if ("mes".equals(this.campoBusqueda)) {
					campo = "cast(date(fecha) as string)";
					this.valBusqueda = (new SimpleDateFormat("YYYY")).format(Calendar.getInstance().getTime()) + "-" + (this.busquedaMes < 10 ? "0" : "") + this.busquedaMes;
				}
				
				this.listFacPagos = this.ifzFacPagos.findLikeProperty(campo, this.valBusqueda);
			}
			
			if (this.listFacPagos == null || this.listFacPagos.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
			
			// Ordeno por fecha descendente y campo de busqueda
			log.info("FACTURA_PAGOS: Ordenando ... ");
			Collections.sort(this.listFacPagos, new Comparator<FacturaPagos>() {
				@Override
				public int compare(FacturaPagos o1, FacturaPagos o2) {
					return o2.getFecha().compareTo(o1.getFecha());
				}
			});
			
			log.info("FACTURA_PAGOS: Busqueda terminada.");
		} catch(Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Pagos de Facturas", e);
		}
	}
	
	public void nuevo() {
		try{
			control();
			this.multiPago = false;
			this.facturaSaldo = BigDecimal.ZERO;
			this.montoOriginal = BigDecimal.ZERO;
			this.pojoFacPago = new FacturaPagosExt();
			this.pojoFacPago.setFecha(Calendar.getInstance().getTime());
			this.pojoFacPago.setMonto(BigDecimal.ZERO);
			this.pojoFacPago.setSaldoAnterior(BigDecimal.ZERO);
			this.pojoFacPago.setSaldoInsoluto(BigDecimal.ZERO);
			this.pojoFacPago.setObservaciones("");
			
			if (this.listBusquedaFacturas == null)
				this.listBusquedaFacturas = new ArrayList<Factura>();
			this.listBusquedaFacturas.clear();
			
			if (this.listMultiPagos == null)
				this.listMultiPagos = new ArrayList<FacturaPagosExt>();
			this.listMultiPagos.clear();
		} catch(Exception e) {
			control("Ocurrio un problema al intentar generar un nuevo pago.", e);
		}
	}
		
	public void editar() {
		BigDecimal montoAux = BigDecimal.ZERO;
		
		try {
			control();
			this.multiPago = false;
			this.pojoFacPago = this.ifzFacPagos.findByIdExt(this.idPago);
			this.montoOriginal = this.pojoFacPago.getMonto();
			this.montoOriginal = this.montoOriginal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			
			montoAux = this.ifzFacPagos.findLiquidado(this.pojoFacPago.getIdFactura().getId());
			this.facturaLiquidado = montoAux.setScale(BigDecimal.ROUND_HALF_EVEN, 2).doubleValue();
			
			// Comprobamos el Saldo de la Factura del Pago seleccionado
			saldo();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Pago de Factura para editar.", e);
		}
	}
		
	public void eliminar() {
		try {
			control();
			// recorremos la lista
			for (FacturaPagos var : this.listFacPagos) {
				if (var.getId() == this.idPago) {
					// Actualizamos los datos del pojo en la lista
					var.setEstatus(1);
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					// Actualizamos en la BD
					this.ifzFacPagos.update(var);
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Pago de Factura.", e);
		}
	}
		
	public void guardar() {
		double insoluto = 0;
		int index = -1;
		
		try {
			control();
			if (! validaciones())
				return;

			this.pojoFacPago.setSaldoInsoluto(BigDecimal.ZERO);
			insoluto = this.pojoFacPago.getSaldoAnterior().doubleValue() - this.pojoFacPago.getMonto().doubleValue();
			if (insoluto > 0)
				this.pojoFacPago.setSaldoInsoluto(new BigDecimal(insoluto));
			if (this.pojoFacPago.getObservaciones() == null) 
				this.pojoFacPago.setObservaciones("");
			
			if (this.pojoFacPago.getId() == null || this.pojoFacPago.getId() <= 0L) {
				// determinamos parcialidad si corresponde
				if ("PPD".equals(this.pojoFacPago.getIdFactura().getIdMetodoPago().getValor()))
					this.pojoFacPago.setParcialidad(this.ifzFacPagos.findParcialidad(this.pojoFacPago.getIdFactura().getId()));
				
				this.pojoFacPago.setCreadoPor(this.usuarioId);
				this.pojoFacPago.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoFacPago.setModificadoPor(this.usuarioId);
				this.pojoFacPago.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD y añadimos a la lista
				this.pojoFacPago.setId(this.ifzFacPagos.save(this.pojoFacPago));
				this.listFacPagos.add(this.ifzFacPagos.convertir(this.pojoFacPago));
			} else {
				this.pojoFacPago.setModificadoPor(this.usuarioId);
				this.pojoFacPago.setFechaModificacion(Calendar.getInstance().getTime());

				// Actualizamos en la BD y actualizamos en la lista
				this.ifzFacPagos.update(this.pojoFacPago);
				for (FacturaPagos var : this.listFacPagos) {
					if (var.getId() == this.pojoFacPago.getId()) {
						index = this.listFacPagos.indexOf(var);
						break;
					}
				}
				
				if (index >= 0) 
					this.listFacPagos.set(index, this.ifzFacPagos.convertir(this.pojoFacPago));
			}
			
			// Actualizo saldo de factura
			this.pojoFacPago.getIdFactura().setSaldo(this.facturaSaldo);
			this.ifzFacturas.update(this.pojoFacPago.getIdFactura());
			nuevo();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Guardar el Pago", e);
		} finally {
			// Enviamos transaccion
			enviarMensajeTransaccion();
		}
	}
	
	public void saldo() {
		try {
			control();
			this.facturaSaldo = BigDecimal.ZERO;
			if (this.pojoFacPago != null && this.pojoFacPago.getIdFactura() != null && this.pojoFacPago.getIdFactura().getId() != null && this.pojoFacPago.getIdFactura().getId() > 0L)
				this.facturaSaldo = saldo(this.pojoFacPago.getIdFactura().getId());
		} catch (Exception e) {
			control("Ocurrio un problema al intentar obtener el Saldo de la Factura", e);
		}
	}
	
	public void saldoCurrent() {
		try {
			control();
			this.facturaSaldo = BigDecimal.ZERO;
			if (this.currentItem != null && this.currentItem.getIdFactura() != null && this.currentItem.getIdFactura().getId() != null && this.currentItem.getIdFactura().getId() > 0L)
				this.facturaSaldo = saldo(this.currentItem.getIdFactura().getId());
		} catch (Exception e) {
			control("Ocurrio un problema al intentar obtener el Saldo de la Factura", e);
		}
	}

	public void enviarMensajeTransaccion() {
		try {
			if (this.idPago <= 0L)
				return;
	
			this.pojoFacPago = this.ifzFacPagos.findByIdExt(this.idPago);
			if ("X".equals(this.pojoFacPago.getIdFactura().getTipo()))
				return;
			
			// Lanzamos mensajeTransaccion en un hilo distinto al principal si corresponde
			new Thread(this).start();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar lanzar el evento transaccion", e);
		}
	}
	
	@Override
	public void run() {
		Respuesta respuesta = null;
		
		try {
			Thread.sleep(1000);
			log.info("Enviamos evento: Pago de Factura ... ");
			this.ifzFacPagos.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzFacPagos.enviarTransaccion(this.pojoFacPago);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error(respuesta.getErrores().getDescError(), (Throwable) respuesta.getBody().getValor("exception"));
				return;
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar enviar la Factura a Provision", e);
		}
	}

	private boolean validaciones() {
		DecimalFormat formatter = new DecimalFormat("#.00");
		BigDecimal liquidado = BigDecimal.ZERO;
		double pago = 0;
		double saldo = 0;
		
		if (this.pojoFacPago.getIdFactura() == null) {
			control(-1, "No selecciono ninguna Factura");
			return false;
		}
		
		if (this.pojoFacPago.getIdFormaPago() == null) {
			control(-2, "Debe indicar la Forma de Pago");
			return false;
		}
		
		if (this.pojoFacPago.getIdCuentaDeposito() == null) {
			control(-3, "Debe indicar la Cuenta de Deposito");
			return false;
		}
		
		if (this.pojoFacPago.getIdBancoOrigen() == null) {
			control(-4, "Debe indicar el Bando Origen");
			return false;
		}
		
		if (this.pojoFacPago.getMonto().doubleValue() <= 0L) {
			control(4, "Monto menor o igual a cero. No permitido");
			return false;
		}
		
		try {
			if (this.pojoFacPago.getId() != null && this.pojoFacPago.getId() > 0L) {
				saldo = Double.parseDouble(formatter.format(this.facturaSaldo.doubleValue()));
				liquidado = this.ifzFacPagos.findLiquidado(this.pojoFacPago.getIdFactura().getId());
				liquidado.setScale(2, BigDecimal.ROUND_HALF_UP);
				liquidado = liquidado.subtract(this.montoOriginal);
				
				saldo = Double.parseDouble(formatter.format(this.pojoFacPago.getIdFactura().getTotal()));
				saldo = saldo - Double.parseDouble(formatter.format(liquidado.doubleValue()));
				
				pago = Double.parseDouble(formatter.format(this.pojoFacPago.getMonto().doubleValue()));
			} else {
				saldo = Double.parseDouble(formatter.format(this.facturaSaldo.doubleValue()));
				pago = Double.parseDouble(formatter.format(this.pojoFacPago.getMonto().doubleValue()));
			}
			
			if (pago > saldo) {
				log.info("ERROR 5 - Monto mayor al saldo. No permitido");
				control(5, "Monto mayor al saldo. No permitido");
				return false;
			}
			
			saldo = saldo - pago;
			saldo = Double.parseDouble(formatter.format(saldo));
		} catch (Exception e) {
			control("Ocurrio un problema al validar el monto del Pago", e);
			return false;
		}
		
		this.pojoFacPago.setMonto(new BigDecimal(pago));
		this.facturaSaldo = new BigDecimal(saldo);
		
		return true;
	}

	private void fillLists() {
		// Formas de pagos
		this.listFormasPagoItems.clear();
		this.listFormasPago = this.ifzFormasPagos.findLikeColumnName("formaPago", "");
		for (FormasPagos var : this.listFormasPago) {
			this.listFormasPagoItems.add(new SelectItem(var.getId(), var.getId() + " - " + var.getFormaPago()));
		}
		
		// Cuentas Bancarias
		this.listCuentasItems.clear();
		this.listCuentas = this.ifzCtas.findLikeColumnName("numeroDeCuenta", "");
		for (CuentaBancaria var : listCuentas) {
			this.listCuentasItems.add(new SelectItem(var.getId(),  var.getNumeroDeCuenta() + " - " + var.getInstitucionBancaria().getNombreCorto()));
		}
		
		// Bancos
		this.listBancosItems.clear();
		this.listBancos = this.ifzBancos.findLikeColumnName("nombreCorto", "");
		for (Banco var : listBancos) {
			this.listBancosItems.add(new SelectItem(var.getId(), var.getId() + " - " + var.getNombreCorto()));
		}
	}
	
	private BigDecimal saldo(long idFactura) {
		BigDecimal saldo = BigDecimal.ZERO;
		
		try {
			control();
			if (idFactura > 0L) {
				saldo = this.ifzFacturas.findSaldoFactura(idFactura);
				saldo = saldo.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar obtener el Saldo de la Factura", e);
			saldo = BigDecimal.ZERO;
		}
		
		return saldo;
	}

	private boolean validaRequest(String param, String refValue) {
		if (! this.isDebug)
			return false;
		
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		if (refValue == null || "".equals(refValue.trim()))
			return true;
		
		return (refValue.trim().equals(this.paramsRequest.get(param).trim()));
	}
	
	private void control() {
		this.band = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.band = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		codigo = "Ex" + formatter.format(Calendar.getInstance().getTime());
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		log.error("\n\nFACTURA-PAGOS :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n" + this.mensajeDetalles, throwable);
	}
	
	// --------------------------------------------------------------------------------
	// BUSQUEDA FACTURAS
	// --------------------------------------------------------------------------------
	
	public void nuevaBusquedaFacturas() {
		this.campoBusquedaFacturas = this.tipoBusquedaFacturas.get(0).getValue().toString();
		this.valorBusquedaFacturas = "";
		this.numPaginaFacturas = 1;

		if (this.listBusquedaFacturas == null)
			this.listBusquedaFacturas = new ArrayList<Factura>();
		this.listBusquedaFacturas.clear();
		this.pojoFacturaSeleccionada = null;
	}
	
	public void buscarFacturas() {
		HashMap<String, Object> params = null;
		List<Factura> aux = null;
		BigDecimal saldo = BigDecimal.ZERO;
		
		try {
			control();
			if (this.campoBusquedaFacturas == null || "".equals(this.campoBusquedaFacturas))
				this.campoBusquedaFacturas = this.tipoBusquedaFacturas.get(0).getValue().toString();
			
			params = new HashMap<String, Object>();
			params.put(this.campoBusquedaFacturas, this.valorBusquedaFacturas);
			params.put("estatus", 1);
			
			this.ifzFacturas.orderBy("id DESC");
			this.listBusquedaFacturas = this.ifzFacturas.findLikeProperties(params);
			if (this.listBusquedaFacturas != null && ! this.listBusquedaFacturas.isEmpty()) {
				aux = new ArrayList<Factura>();
				for (Factura item : this.listBusquedaFacturas) {
					saldo = saldo(item.getId());
					// Solo facturas con saldo
					if (saldo.doubleValue() > 0)
						aux.add(item);
				}
				
				this.listBusquedaFacturas.clear();
				this.listBusquedaFacturas.addAll(aux);
			}
			
			if (this.listBusquedaFacturas == null || this.listBusquedaFacturas.isEmpty()) {
				control(2, "Busqueda de Facturas sin resultado");
			}
		} catch(Exception e) {
			control("Ocurrio un problema al intentar realizar la busqueda de Facturas", e);
		} finally {
			if (this.listBusquedaFacturas == null)
				this.listBusquedaFacturas = new ArrayList<Factura>();
			log.info(this.listBusquedaFacturas.size() + " Facturas encontradas");
		}
	}
	
	public void seleccionarFactura() {
		try {
			control();
			log.info("Seleccionando factura ... ");
			if (this.pojoFacturaSeleccionada == null) {
				control(-1, "Ocurrio un problema al intentar recuperar la Factura seleccionada");
				return;
			}
			
			// Extendemos la factura, la asignamos al Pago y recuperamos datos extras
			log.info("Extendemos la factura y la asignamos al Pago ... ");
			this.pojoFacPago.setIdFactura(this.ifzFacturas.convertir(this.pojoFacturaSeleccionada));
			this.pojoFacPago.setTipoCambio(this.pojoFacturaSeleccionada.getTipoCambio());
			this.pojoFacPago.setSaldoAnterior(this.pojoFacturaSeleccionada.getTotal());
			this.pojoFacPago.setSaldoInsoluto(this.pojoFacturaSeleccionada.getTotal());
			if (this.pojoFacturaSeleccionada.getIdMoneda() != null && this.pojoFacturaSeleccionada.getIdMoneda() > 0L) {
				Moneda pojoMoneda = this.ifzMoneda.findById(this.pojoFacturaSeleccionada.getIdMoneda());
				this.pojoFacPago.setIdMoneda(pojoMoneda);
			}
			
			// Obtenemos datos del Cliente/Beneficiario
			log.info("Obtenemos datos del Cliente/Beneficiario ... ");
			this.pojoFacPago.setIdBeneficiario(this.pojoFacPago.getIdFactura().getIdBeneficiario());
			this.pojoFacPago.setBeneficiario(this.pojoFacPago.getIdFactura().getBeneficiario());
			this.pojoFacPago.setTipoBeneficiario(this.pojoFacPago.getIdFactura().getTipoBeneficiario());
			
			if (this.multiPago) {
				this.listMultiPagos.add(this.pojoFacPago);
				return;
			}
			
			// Obtenemos su saldo
			log.info("Obteniendo saldo de factura ... ");
			saldo();
			log.info("Saldo de Factura: " + this.facturaSaldo.doubleValue());
			
			if (this.facturaSaldo.doubleValue() <= 0) {
				this.pojoFacturaSeleccionada = null;
				this.pojoFacPago.setIdFactura(null);
				buscarFacturas();
				control(-1, "La Factura seleccionada ya esta liquidada");
			} else {
				if (! this.multiPago)
					nuevaBusquedaFacturas();
				log.info("Factura seleccionada y asignada. Proceso terminado");
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar obtener datos de la factura seleccionada", e);
		} 
	}

	// --------------------------------------------------------------------------------
	// TIMBRAR
	// --------------------------------------------------------------------------------
	
	public void evaluaTimbrar() {
		try {
			control();
			if (this.pojoFacPago == null || this.pojoFacPago.getId() == null || this.pojoFacPago.getId() <= 0L) {
				control(-1, "No selecciono ningun Pago");
				return;
			}
			
			if (this.pojoFacPago.getTimbrado() == 1) {
				control(-1, "El Pago seleccionado ya fue timbrado previamente");
				return;
			}
			
			if (! obtenerSerieFolioComplementoPago()) {
				control(-1, "No se pudo asignar Serie/Folio para el Comprobante de Pago");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al evaluar el Pago indicado", e);
		}
	}
	
	public void timbrarPago() {
		Respuesta respuesta = null;
		Comprobante comprobante = null;
		FactElectv33 facComprobante = null;
		String fileName = "";
	
		try {
			control();
			if (this.pojoFacPago == null || this.pojoFacPago.getId() == null || this.pojoFacPago.getId() <= 0L) {
				control(-1, "No selecciono ningun Pago");
				return;
			}
			
			comprobante = this.ifzFacPagos.generarComprobante(this.pojoFacPago.getId());
			if (comprobante == null) {
				control(-1, "Ocurrio un problema al generar el Complemento de Pago");
				return;
			}
			
			comprobante.setFecha(DatatypeFactory.newInstance().newXMLGregorianCalendar((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).format(Calendar.getInstance().getTime())));
			
			facComprobante = new FactElectv33();
			facComprobante.setDebugging(this.isDebug);
			facComprobante.setTesting(validaRequest("CFDI_TEST", "1"));
			fileName = "P-" + comprobante.getSerie() + comprobante.getFolio();
			respuesta = facComprobante.timbrar(comprobante, fileName, this.pojoFacPago.getIdFactura().getIdEmpresa().getId());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al timbrar el Complemento de Pago\n" + respuesta.getErrores().getDescError());
				return;
			}
			
			// Actualizo los datos del Timbrado en el Pago
			this.pojoFacPago.setTimbrado(1);
			this.pojoFacPago.setFechaTimbrado((new SimpleDateFormat("MM-dd-yyyy HH:mm:ss")).parse(respuesta.getBody().getValor("fechaTimbrado").toString()));
			this.pojoFacPago.setCodigo(Long.parseLong(respuesta.getBody().getValor("codigo").toString()));
			this.pojoFacPago.setMensaje(respuesta.getBody().getValor("mensaje").toString());
			this.pojoFacPago.setSerie(respuesta.getBody().getValor("serie").toString());
			this.pojoFacPago.setFolio(respuesta.getBody().getValor("folio").toString());
			this.pojoFacPago.setUuid(respuesta.getBody().getValor("uuid").toString());
			this.pojoFacPago.setCadenaOriginal(new String((byte[]) respuesta.getBody().getValor("cadenaOriginal")));
			this.pojoFacPago.setXmlPrevio((byte[]) respuesta.getBody().getValor("xmlPrevio"));
			this.pojoFacPago.setXmlTimbrado((byte[]) respuesta.getBody().getValor("xmlTimbrado"));
			this.ifzFacPagos.update(this.pojoFacPago);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Timbrar el Complemento de Pago indicado", e);
		}
	}
	
	private boolean obtenerSerieFolioComplementoPago() {
		long folio = 0L;
		long idEmpresa = 0L;
				
		try {
			idEmpresa = this.pojoFacPago.getIdFactura().getIdEmpresa().getId();
			if (this.pojoFacPago.getSerie() == null || "".equals(this.pojoFacPago.getSerie()))
				this.pojoFacPago.setSerie(this.pojoFacPago.getIdFactura().getIdEmpresa().getSerieComplementoPago());
			
			if (this.pojoFacPago.getFolio() == null || "".equals(this.pojoFacPago.getFolio())) {
				folio = this.ifzFacPagos.folioActualComplementoPago(this.pojoFacPago.getSerie());
				if (folio > 0L) {
					if (idEmpresa > 10000000)
						idEmpresa = idEmpresa - 10000000;
					folio += 1L;
					this.pojoFacPago.setFolio(idEmpresa + (new DecimalFormat("#00000")).format(folio));
				}
			}
			
			if (this.pojoFacPago.getSerie() == null || "".equals(this.pojoFacPago.getSerie())) 
				return false;
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar asignar Serie/Folio al Pago", e);
			return false;
		}
		
		return true;
	}
	
	public void obtenerXML() {
		String nombreDocumento = "";
		byte[] contenidoDocumento = null;
		
		try {
			control();
			log.info("Descargando XML ... ");
			if (this.pojoFacPago == null || this.pojoFacPago.getId() == null || this.pojoFacPago.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Pago indicado");
				return;
			}

			nombreDocumento = "P-" + this.pojoFacPago.getSerie() + "-" + this.pojoFacPago.getFolio() + ".xml";
			contenidoDocumento = this.pojoFacPago.getXmlTimbrado();

			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", "xml");
			log.info("Descarga XML lanzada");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el XML de la factura", e);
		}
	}
	
	public void obtenerPDF() {
		
	}
	
	// ---------------------------------------
	// PROPIEDADES
	// ---------------------------------------
	
	public String getTituloFactura() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdFactura() != null && this.pojoFacPago.getId() != null && this.pojoFacPago.getId() > 0L)
			return  "Factura " + this.pojoFacPago.getIdFactura().getFolioFactura();
		return "";
	}
	
	public void setTituloFactura(String value) {}
	
	public String getUuid() {
		if (this.pojoFacPago != null && this.pojoFacPago.getTimbrado() == 1)
			return this.pojoFacPago.getUuid();
		return "";
	}
	
	public void setUuid(String value) {}
	
	public String getPagoVersion() {
		return "3.3";
	}
	
	public void setPagoVersion(String value) {}
	
	public String getPagoParcialidad() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdFactura() != null && this.pojoFacPago.getId() != null && this.pojoFacPago.getId() > 0L)
			return (new DecimalFormat("#00")).format(this.pojoFacPago.getParcialidad());
		return "01";
	}
	
	public void setPagoParcialidad(String value) {}
	
	public String getPagoSerie() {
		if (this.pojoFacPago != null && this.pojoFacPago.getSerie() != null && ! "".equals(this.pojoFacPago.getSerie()))
			return this.pojoFacPago.getSerie();
		return "<Sin serie asignada>";
	}
	
	public void setPagoSerie(String value) {}
	
	public String getPagoFolio() {
		if (this.pojoFacPago != null && this.pojoFacPago.getFolio() != null && ! "".equals(this.pojoFacPago.getFolio()))
			return this.pojoFacPago.getFolio();
		return "<Se genera al Timbrar>";
	}
	
	public void setPagoFolio(String value) {}
	
	public boolean getEditable() {
		if (this.pojoFacPago != null && pojoFacPago.getId() != null && this.pojoFacPago.getId() > 0L)
			return (this.pojoFacPago.getTimbrado() == 0);
		return true;
	}
	
	public void setEditable(boolean value) {}

	public boolean getTimbrar() {
		if (this.pojoFacPago != null && pojoFacPago.getId() != null && this.pojoFacPago.getId() > 0L)
			return (this.pojoFacPago.getTimbrado() == 0);
		return true;
	}
	
	public void setTimbrar(boolean value) {}
	
	public String getFacturaDescripcion() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdFactura() != null && this.pojoFacPago.getIdFactura().getId() != null && this.pojoFacPago.getIdFactura().getId() > 0L)
			return this.pojoFacPago.getIdFactura().getId() + " - " + this.pojoFacPago.getIdFactura().getFolioFactura() + " - " + (new SimpleDateFormat("yyyy-MMM-dd")).format(this.pojoFacPago.getIdFactura().getFechaEmision()).toUpperCase();
		return "";
	}
	
	public void setFacturaDescripcion(String value) {}

	public String getFacturaObra() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdFactura() != null && this.pojoFacPago.getIdFactura().getId() != null && this.pojoFacPago.getIdFactura().getId() > 0L)
			return this.pojoFacPago.getIdFactura().getIdObra().getId() + " - " + this.pojoFacPago.getIdFactura().getNombreObra();
		return "";
	}
	
	public void setFacturaObra(String value) {}

	public String getFacturaCliente() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdFactura() != null && this.pojoFacPago.getIdFactura().getId() != null && this.pojoFacPago.getIdFactura().getId() > 0L)
			return this.pojoFacPago.getIdFactura().getIdBeneficiario() + " - " + this.pojoFacPago.getIdFactura().getCliente();
		return "";
	}
	
	public void setFacturaCliente(String value) {}

	public double getFacturaTotal() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdFactura() != null && this.pojoFacPago.getIdFactura().getId() != null && this.pojoFacPago.getIdFactura().getId() > 0L)
			return this.pojoFacPago.getIdFactura().getTotal();//(new DecimalFormat("$ ###,###,##0.00")).format(this.pojoFacPago.getIdFactura().getTotal());
		return 0;
	}
	
	public void setFacturaTotal(double value) {}
	
	public BigDecimal getMonto() {
		if (this.pojoFacPago != null && this.pojoFacPago.getMonto() != null) 
			return this.pojoFacPago.getMonto();
		return BigDecimal.ZERO;
	}
	
	public void setMonto(BigDecimal value) {
		if (value == null) 
			value = new BigDecimal(0L);
		this.pojoFacPago.setMonto(value);
	}
	
	public long getFormaPago() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdFormaPago() != null) 
			return this.pojoFacPago.getIdFormaPago().getId();
		return 0L;
	}
	
	public void setFormaPago(long idFormaPago) {
		if (idFormaPago <= 0)
			return;
		
		for (FormasPagos var : this.listFormasPago) {
			if (var.getId() == idFormaPago) {
				this.pojoFacPago.setIdFormaPago(var);
				break;
			}
		}
	}
		
	public long getCuentaBancaria() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdCuentaDeposito() != null) 
			return this.pojoFacPago.getIdCuentaDeposito().getId();
		return 0L;
	}
		
	public void setCuentaBancaria(long idCuentaBancaria) {
		if (idCuentaBancaria <= 0)
			return;
		
		for (CuentaBancaria var : this.listCuentas) {
			if (var.getId() == idCuentaBancaria) {
				this.pojoFacPago.setIdCuentaDeposito(var);
				break;
			}
		}
	}
	
	public long getBancoOrigen() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdBancoOrigen() != null) 
			return this.pojoFacPago.getIdBancoOrigen().getId();
		return 0L;
	}
	
	public void setBancoOrigen(long bancoOrigen) {
		if (bancoOrigen <= 0)
			return;
		
		for (Banco var : this.listBancos) {
			if (var.getId() == bancoOrigen) {
				this.pojoFacPago.setIdBancoOrigen(var);
				break;
			}
		}
	}
		
	public List<FacturaPagos> getListFacPagos() {
		return listFacPagos;
	}
	
	public void setListFacPagos(List<FacturaPagos> listFacPagos) {
		this.listFacPagos = listFacPagos;
	}
	
	public FacturaPagosExt getPojoFacPago() {
		return pojoFacPago;
	}
	
	public void setPojoFacPago(FacturaPagosExt pojoFacPago) {
		this.pojoFacPago = pojoFacPago;
	}
	
	public String getResOperacion() {
		return mensaje;
	}
	
	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
	}
	
	public int getTipoMensaje() {
		return tipoMensaje;
	}
	
	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
	
	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}
	
	public List<SelectItem> getTipoBusqueda() {
		return tipoBusqueda;
	}
	
	public void setTipoBusqueda(List<SelectItem> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}
	
	public String getValBusqueda() {
		return valBusqueda;
	}
	
	public void setValBusqueda(String valBusqueda) {
		this.valBusqueda = valBusqueda;
	}
	
	public String getCampoBusqueda() {
		return campoBusqueda;
	}
	
	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}
		
	public boolean getBand() {
		return band;
	}
	
	public void setBand(boolean band) {
		this.band = band;
	}
	
	public List<FormasPagos> getListFormasPago() {
		return listFormasPago;
	}
	
	public void setListFormasPago(List<FormasPagos> listFormasPago) {
		this.listFormasPago = listFormasPago;
	}
		
	public List<Factura> getListaFacturas() {
		return listBusquedaFacturas;
	}
	
	public void setListaProyObras(List<Factura> listaFacturas) {
		this.listBusquedaFacturas = listaFacturas;
	}
	
	public List<SelectItem> getTipoBusquedaFacturas() {
		return tipoBusquedaFacturas;
	}
	
	public void setTipoBusquedaFacturas(List<SelectItem> tipoBusquedaFacturas) {
		this.tipoBusquedaFacturas = tipoBusquedaFacturas;
	}
	
	public String getValBusquedaFacturas() {
		return valorBusquedaFacturas;
	}
	
	public void setValBusquedaFacturas(String valBusquedaFacturas) {
		this.valorBusquedaFacturas = valBusquedaFacturas;
	}
	
	public String getCampoBusquedaFacturas() {
		return campoBusquedaFacturas;
	}
	
	public void setCampoBusquedaFacturas(String campoBusquedaFacturas) {
		this.campoBusquedaFacturas = campoBusquedaFacturas;
	}
	
	public int getNumPaginaFacturas() {
		return numPaginaFacturas;
	}
	
	public void setNumPaginaFacturas(int numPaginaFacturas) {
		this.numPaginaFacturas = numPaginaFacturas;
	}
	
	public Date getFecha() {
		return this.pojoFacPago != null && this.pojoFacPago.getFecha() != null ? this.pojoFacPago.getFecha() : new Date();
	}
	
	public void setFecha(Date fecha) {
		this.pojoFacPago.setFecha(fecha);
	}
		
	public String getObservaciones() {
		return this.pojoFacPago == null || this.pojoFacPago.getObservaciones() == null ? "" : this.pojoFacPago.getObservaciones();
	}
		
	public void setObservaciones(String observaciones) {
		this.pojoFacPago.setObservaciones(observaciones);
	}
	
 	public List<SelectItem> getListFormasPagoItems() {
		return listFormasPagoItems;
	}
 	
	public void setListFormasPagoItems(List<SelectItem> listFormasPagoItems) {
		this.listFormasPagoItems = listFormasPagoItems;
	}
	
	public List<SelectItem> getListCuentasItems() {
		return listCuentasItems;
	}
	
	public void setListCuentasItems(List<SelectItem> listCuentasItems) {
		this.listCuentasItems = listCuentasItems;
	}
	
	public List<SelectItem> getListBancosItems() {
		return listBancosItems;
	}
	
	public void setListBancosItems(List<SelectItem> listBancosItems) {
		this.listBancosItems = listBancosItems;
	}

	public BigDecimal getFacturaSaldo() {
		return facturaSaldo;
	}
	
	public void setFacturaSaldo(BigDecimal facturaSaldo) {
		this.facturaSaldo = facturaSaldo;
	}

	public String getSaldo() {
		if (this.facturaSaldo.doubleValue() == 0 && this.facturaLiquidado > 0)
			return "PAGADA";
		return (new DecimalFormat("$ ###,###,###,##0.00")).format(this.facturaSaldo);
	}
	
	public void setSaldo(String facturaSaldo) {}
	
	public String getParcialidad() {
		if (this.pojoFacPago != null && this.pojoFacPago.getId() != null && this.pojoFacPago.getId() <= 0L)
			return (new DecimalFormat("#00")).format(this.pojoFacPago.getParcialidad());
		return "";
	}
	
	public void setParcialidad(String value) {}

	public long getIdPago() {
		return idPago;
	}

	public void setIdPago(long idPago) {
		this.idPago = idPago;
	}

	public Factura getPojoFacturaSeleccionada() {
		return pojoFacturaSeleccionada;
	}
	
	public void setPojoFacturaSeleccionada(Factura pojoFacturaSeleccionada) {
		this.pojoFacturaSeleccionada = pojoFacturaSeleccionada;
	}
	
	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public boolean isMultiPago() {
		return multiPago;
	}
	
	public void setMultiPago(boolean multiPago) {
		this.multiPago = multiPago;
	}

	public List<FacturaPagosExt> getListMultiPagos() {
		return listMultiPagos;
	}

	public void setListMultiPagos(List<FacturaPagosExt> listMultiPagos) {
		this.listMultiPagos = listMultiPagos;
	}

	public FacturaPagosExt getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(FacturaPagosExt currentItem) {
		this.currentItem = currentItem;
	}


	public List<SelectItem> getListMeses() {
		return listMeses;
	}


	public void setListMeses(List<SelectItem> listMeses) {
		this.listMeses = listMeses;
	}


	public Date getBusquedaFecha() {
		return busquedaFecha;
	}


	public void setBusquedaFecha(Date busquedaFecha) {
		this.busquedaFecha = busquedaFecha;
	}


	public int getBusquedaMes() {
		return busquedaMes;
	}


	public void setBusquedaMes(int busquedaMes) {
		this.busquedaMes = busquedaMes;
	}
}
