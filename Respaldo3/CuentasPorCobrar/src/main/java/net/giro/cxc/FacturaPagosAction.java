package net.giro.cxc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.beans.FacturaPagosExt;
import net.giro.cxc.beans.FacturaPagosTimbre;
import net.giro.cxc.logica.FacturaPagosRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.logica.BancosRem;
import net.giro.tyg.logica.CuentasBancariasRem;
import net.giro.tyg.logica.FormasPagosRem;
import net.giro.tyg.logica.MonedaRem;
import net.giro.tyg.logica.MonedasValoresRem;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="facPagosAction")
public class FacturaPagosAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FacturaPagosAction.class);
	private HttpSession httpSession;
	private LoginManager loginManager;
	private PropertyResourceBundle entornoProperties;
	// Interfaces 
	private ConGrupoValoresRem ifzGpoVal;
	private ConGrupoValores pojoGpoImpuestos;
	private MonedasValoresRem ifzMonValores;
	private ReportesRem ifzReportes;
	private FacturaPagosRem ifzPagos;
	private List<FacturaPagos> listPagos;
	private FacturaPagosExt pojoFacPago;
	// Formas Pago
	private FormasPagosRem ifzFormasPagos;
	private List<FormasPagos> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
    private long idFormaPago;
	// Bancos
	private BancosRem ifzBancos;
	private List<Banco> listBancos;
	private List<SelectItem> listBancosItems;
    private long idBancoOrigen;
	// Cuentas Bancarias
	private CuentasBancariasRem ifzCtas;
	private List<CuentaBancaria> listCuentas;
	private List<SelectItem> listCuentasItems;
    private long idCuentaBancaria;
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
	private long idClienteBase;
	private int numPaginaFacturas;
	private int numPagina;
    private long idPago;
    private long idFacturaQuitar;
    // Variables del pago 
    private List<FacturaPagosExt> listFacturasRelacionadas;
    private List<FacturaPagosExt> listFacturasRelacionadasEliminadas;
    private Date fechaPago;
    private double montoPago;
    private double diferencia;
    private long idMoneda;
    private double tipoCambio;
    private String referenciaPago;
    private String observaciones;
    //-----------------------------------
    private long monedaPagoId;
    private String monedaPago;
    private boolean deshabilitarMoneda;
    // Monedas
	private MonedaRem ifzMoneda;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	// Variables de operacion 
    private boolean band;
	private int tipoMensaje;
	private String mensaje;
	// Auditoria
	private List<SelectItem> listAuditoriaItems;
	private boolean hasId;
	// DEBUG 
	private boolean isDebug;
	private Map<String,String> paramsRequest;
	private boolean editando;
    
	public FacturaPagosAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		InitialContext ctx = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());

		    this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
		    	this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;

		    valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			ctx = new InitialContext();
			this.ifzFacturas = (FacturaRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzPagos = (FacturaPagosRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosFac!net.giro.cxc.logica.FacturaPagosRem");
			this.ifzGpoVal = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzFormasPagos = (FormasPagosRem) ctx.lookup("ejb:/Logica_TYG//FormasPagosFac!net.giro.tyg.logica.FormasPagosRem");
			this.ifzCtas = (CuentasBancariasRem) ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
			this.ifzBancos = (BancosRem) ctx.lookup("ejb:/Logica_TYG//BancosFac!net.giro.tyg.logica.BancosRem");
			this.ifzMoneda = (MonedaRem) ctx.lookup("ejb:/Logica_TYG//MonedaFac!net.giro.tyg.logica.MonedaRem");
			this.ifzMonValores = (MonedasValoresRem) ctx.lookup("ejb:/Logica_TYG//MonedasValoresFac!net.giro.tyg.logica.MonedasValoresRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			
			this.ifzFacturas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPagos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFormasPagos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzBancos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMoneda.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMonValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			
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

			// Busqueda principal
			this.tipoBusqueda = new ArrayList<SelectItem>();
			this.tipoBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tipoBusqueda.add(new SelectItem("model.folio", "Folio"));
			this.tipoBusqueda.add(new SelectItem("model.idFactura.folioFactura", "Factura"));
			this.tipoBusqueda.add(new SelectItem("fecha", "Fecha"));
			this.tipoBusqueda.add(new SelectItem("mes", "Mes"));
			this.tipoBusqueda.add(new SelectItem("model.idFactura.cliente", "Cliente"));
			this.tipoBusqueda.add(new SelectItem("model.referenciaFormaPago", "Referencia"));
			this.tipoBusqueda.add(new SelectItem("model.idFactura.id", "ID Factura"));
			this.tipoBusqueda.add(new SelectItem("model.id", "ID"));
			this.campoBusqueda = this.tipoBusqueda.get(0).getValue().toString();
			this.valBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda facturas
			this.tipoBusquedaFacturas = new ArrayList<SelectItem>();
			this.tipoBusquedaFacturas.add(new SelectItem("*", "Coincidencia"));
			this.tipoBusquedaFacturas.add(new SelectItem("folioFactura", "Folio"));
			this.tipoBusquedaFacturas.add(new SelectItem("nombreObra", "Obra"));
			this.tipoBusquedaFacturas.add(new SelectItem("cliente", "Cliente"));
			this.tipoBusquedaFacturas.add(new SelectItem("idObra", "Id Obra"));
			this.tipoBusquedaFacturas.add(new SelectItem("id", "ID"));
			nuevaBusquedaFacturas();
			
			this.listPagos = new ArrayList<FacturaPagos>();
			this.pojoFacPago = new FacturaPagosExt();
			this.pojoFacPago.setIdFactura(new FacturaExt());
			this.pojoFacPago.setFecha(Calendar.getInstance().getTime());
			this.pojoFacPago.setPago(new BigDecimal(0L));
			this.pojoFacPago.setObservaciones("");
			
			// IMPUESTOS
			// ---------------------------------------------------------------------------------------------------------
			this.pojoGpoImpuestos = this.ifzGpoVal.findByName("SYS_IMPTOS");
			if (this.pojoGpoImpuestos == null || this.pojoGpoImpuestos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
			
			// Cargamos datos 
		    this.listFacturasRelacionadas = new ArrayList<FacturaPagosExt>();
			cargarMonedas();
			fillLists();
			control();
		} catch (Exception e) {
			log.error("Error en constructor FacturaPagosAction", e);
		}
	}
	
	public void buscar() {
		try {
			control();
			if (this.campoBusqueda == null || "".equals(this.campoBusqueda))
				this.campoBusqueda = this.tipoBusqueda.get(0).getValue().toString();
			
			controlLog("FACTURA_PAGOS Buscando " + this.campoBusqueda + " : " + ("fecha".equals(this.campoBusqueda) ? this.busquedaFecha : this.valBusqueda) + " ... ");
			this.numPagina = 1;
			this.ifzPagos.setInfoSesion(this.loginManager.getInfoSesion());
			this.listPagos = this.ifzPagos.findLikeProperty(this.campoBusqueda, ("fecha".equals(this.campoBusqueda) ? this.busquedaFecha : this.valBusqueda), true, false, null, 0);
			if (this.listPagos == null || this.listPagos.isEmpty()) 
				control(2, "Busqueda sin resultados.\nNo se encontro ningun Pago a Facturas con los parametros indicados");
		} catch(Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Pagos de Facturas", e);
		} finally {
			this.numPagina = 1;
			this.listPagos = (this.listPagos != null ? this.listPagos : new ArrayList<FacturaPagos>());
			controlLog(this.listPagos.size() + " Pagos encontradas");
		}
	}
	
	public void nuevo() {
		Moneda pojoMoneda = null;
		
		try {
			control();
			this.hasId = false;
		    this.idMoneda = this.loginManager.getInfoSesion().getEmpresa().getMonedaId();
			this.ifzMoneda.setInfoSesion(this.loginManager.getInfoSesion());
			pojoMoneda = this.ifzMoneda.findById(this.idMoneda);
			this.monedaPagoId = pojoMoneda.getId();
			this.monedaPago = pojoMoneda.getNombre();
			this.idClienteBase = 0L;
		    this.fechaPago = Calendar.getInstance().getTime();
		    this.montoPago = 0;
		    this.diferencia = 0;
		    this.tipoCambio = 1.0;
		    this.idFormaPago = 0L;
		    this.referenciaPago = "000";
		    this.idBancoOrigen = 0L;
		    this.idCuentaBancaria = 0L;
		    this.observaciones = "";
			seleccionaTipoCambio();
		} catch(Exception e) {
			control("Ocurrio un problema al intentar generar un nuevo pago.", e);
		} finally {
		    this.listFacturasRelacionadas = new ArrayList<FacturaPagosExt>();
			this.listFacturasRelacionadasEliminadas = new ArrayList<FacturaPagosExt>();
		}
	}
		
	public void editar() {
		List<FacturaPagosExt> relacionadas = null;
		FacturaPagosExt pago = null;
		Moneda pojoMoneda = null;
		
		try {
			control();
			controlLog("\nFACTURA-PAGOS :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: Editando pago: " + this.idPago + " ... ");
			pago = this.ifzPagos.findByIdExt(this.idPago);
			pojoMoneda = pago.getIdMoneda();
			if (pojoMoneda == null) {
				this.idMoneda = pago.getIdFactura().getIdMoneda();
				this.ifzMoneda.setInfoSesion(this.loginManager.getInfoSesion());
				pojoMoneda = this.ifzMoneda.findById(this.idMoneda);
				pago.setIdMoneda(pojoMoneda);
			} 

			this.hasId = true;
			this.idClienteBase = pago.getIdBeneficiario();
		    this.fechaPago = pago.getFecha();
		    this.montoPago = pago.getPago().doubleValue();
		    this.diferencia = pago.getDiferencia().doubleValue();
		    this.idMoneda = pago.getIdMoneda().getId();
		    this.monedaPagoId = pago.getIdMoneda().getId();
		    this.monedaPago = pago.getIdMoneda().getNombre();
		    this.tipoCambio = pago.getTipoCambio();
		    this.idFormaPago = pago.getIdFormaPago().getId();
		    this.referenciaPago = pago.getReferenciaFormaPago();
		    this.idBancoOrigen = pago.getIdBancoOrigen().getId();
		    this.idCuentaBancaria = pago.getIdCuentaDeposito().getId();
		    this.observaciones = pago.getObservaciones();
			this.deshabilitarMoneda = true;
		    
		    this.listFacturasRelacionadas = new ArrayList<FacturaPagosExt>();
			this.listFacturasRelacionadas.add(pago);
			if (pago.getAgrupador() != null && ! "".equals(pago.getAgrupador().trim())) {
				controlLog("\nFACTURA-PAGOS :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: Recuperando agrupados : " + pago.getAgrupador() + " ... ");
				relacionadas = this.ifzPagos.findByPropertyExt("agrupador", pago.getAgrupador());
				if (relacionadas != null && ! relacionadas.isEmpty()) {
					controlLog("\nFACTURA-PAGOS :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: enlistando ... ");
					this.listFacturasRelacionadas.clear();
					this.listFacturasRelacionadas.addAll(relacionadas);
				}
			}
			
			totalizarPago();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Pago de Factura para editar.", e);
		} finally {
			this.listFacturasRelacionadasEliminadas = new ArrayList<FacturaPagosExt>();
			controlLog("\nFACTURA-PAGOS :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: edicion terminada!");
		}
	}
	
	public void guardar() {
		List<FacturaPagos> pagos = new ArrayList<FacturaPagos>();
		FacturaPagos pago = null;
		FormasPagos formaPago = null;
		CuentaBancaria cuentaBancaria = null;
		
		try {
			control();
			if (! validaciones())
				return;
			
			// Eliminamos pagos anteriores si corresponde
			if (this.listFacturasRelacionadasEliminadas != null && ! this.listFacturasRelacionadasEliminadas.isEmpty()) {
				for (FacturaPagosExt item : this.listFacturasRelacionadasEliminadas) 
					this.ifzPagos.cancelar(item.getId());
				this.listFacturasRelacionadasEliminadas.clear();
			}
			
			formaPago = this.ifzFormasPagos.findById(this.idFormaPago);
			cuentaBancaria = this.ifzCtas.findById(this.idCuentaBancaria);
			
			for (FacturaPagosExt item : this.listFacturasRelacionadas) {
				pago = this.ifzPagos.convertir(item);
				pago.setFecha(this.fechaPago);
				pago.setPagoRecibido(new BigDecimal(this.montoPago));
				pago.setDiferencia(new BigDecimal(this.diferencia));
				if (pago.getSaldoAnterior() == null || pago.getSaldoAnterior().doubleValue() == 0)
					pago.setSaldoAnterior(item.getIdFactura().getSaldo());
				pago.setTipoCambio(this.tipoCambio);
				pago.setIdFormaPago(formaPago.getId());
				pago.setFormaPago(formaPago.getFormaPago());
				pago.setReferenciaFormaPago(this.referenciaPago);
				pago.setIdBancoOrigen(this.idBancoOrigen);
				pago.setIdCuentaDeposito(cuentaBancaria.getId());
				pago.setCuentaBanco(cuentaBancaria.getInstitucionBancaria().getNombreCorto());
				pago.setCuentaNumero(cuentaBancaria.getNumeroDeCuenta());
				pago.setObservaciones(this.observaciones);
				if ("PPD".equals(item.getIdFactura().getIdMetodoPago().getValor()) && pago.getParcialidad() <= 0)
					pago.setParcialidad(this.ifzPagos.findParcialidad(item.getIdFactura().getId()));
				if (pago.getId() == null || pago.getId() <= 0L) {
					pago.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					pago.setFechaCreacion(Calendar.getInstance().getTime());
				}
				pago.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				pago.setFechaModificacion(Calendar.getInstance().getTime());
				pagos.add(pago);
			}
			
			// Guardamos y añadimos a la lista
			pagos = this.ifzPagos.saveOrUpdateList(pagos);
			this.listPagos.addAll(0, pagos);
			nuevo();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Guardar el Pago", e);
		} 
	}

	@SuppressWarnings("unchecked")
	public void eliminar() {
		Respuesta respuesta = null;
		// ------------------------------
		HashMap<Long, Integer> indices = null;
		List<FacturaPagos> pagos = null;
		FacturaPagos pago = null;
		
		try {
			control();
			pago = this.ifzPagos.findById(this.idPago);
			if (pago == null || pago.getId() == null || pago.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Pago indicado");
				return;
			}
			
			// Cancelacion de Pago: Multiples Facturas
			this.ifzPagos.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzPagos.cancelar(pago);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("Ocurrio un problema al intentar Cancelar el Pago indicado.\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError(), (Throwable) respuesta.getBody().getValor("exception"));
				control(-1, "Ocurrio un problema al intentar Cancelar el Pago indicado.");
				return;
			}
			
			if (this.listPagos == null || this.listPagos.isEmpty())
				return;

			indices = new HashMap<Long, Integer>();
			for (FacturaPagos var : this.listPagos) 
				indices.put(var.getId(), this.listPagos.indexOf(var));
			
			pago = (FacturaPagos) respuesta.getBody().getValor("pago");
			if (respuesta.getBody().getValor("pagos") != null)
				pagos = (List<FacturaPagos>) respuesta.getBody().getValor("pagos");
			if (pagos == null || pagos.isEmpty()) {
				pagos = new ArrayList<FacturaPagos>();
				pagos.add(pago);
			}
			
			// Actualizamos estatus en la lista
			for (FacturaPagos item : pagos) {
				if (! indices.containsKey(item.getId()))
					continue;
				this.listPagos.set(indices.get(item.getId()), item);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Pago de Factura.", e);
		} 
	}

	public void auditoria() {
		LinkedHashMap<String, String> auditoria = null;
		
		try {
			control();
			if (this.idPago <= 0L) {
				control(-1, "No se indico Factura o la Factura no ha sido guardada");
				return;
			}
			
			this.listAuditoriaItems = new ArrayList<SelectItem>();
			this.ifzPagos.setInfoSesion(this.loginManager.getInfoSesion());
			auditoria = this.ifzPagos.auditoria(this.idPago);
			if (auditoria == null || auditoria.isEmpty()) {
				control(-1, "No se indico Factura o la Factura no ha sido guardada");
				return;
			}
			
			for (Entry<String, String> entry : auditoria.entrySet()) 
				this.listAuditoriaItems.add(new SelectItem(entry.getValue(), entry.getKey()));
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar la informacion de Auditoria de la Factura", e);
		}
	}
	
	public void evaluaCancelar() {
		List<FacturaPagos> pagos = null;
		FacturaPagos pago = null;
		
		try {
			control();
			pago = this.ifzPagos.findById(this.idPago);
			if (pago == null || pago.getId() == null || pago.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Pago para evaluar la Cancelacion");
				return;
			}
			
			if (! this.editando && (pago.getAgrupador() != null && ! "".equals(pago.getAgrupador().trim()))) {
				pagos = this.ifzPagos.findByProperty("agrupador", pago.getAgrupador());
				if (pagos != null && ! pagos.isEmpty() && pagos.size() > 1) {
					control(false, -2, "Confirmar cancelacion de Pago (varias facturas)", null);
					return;
				}
			}
			
			if (this.editando && this.listFacturasRelacionadas != null && ! this.listFacturasRelacionadas.isEmpty() && this.listFacturasRelacionadas.size() > 1) {
				control(false, -2, "Confirmar cancelacion de Pago (varias facturas)", null);
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar evaluar la Cancelacion del Pago.", e);
		} 
	}
	
	public void populatePago() {
		double requerido = 0;
		double restante = 0;
		double aplicado = 0;
		
	    if (this.listFacturasRelacionadas == null || this.listFacturasRelacionadas.isEmpty()) {
	    	this.idClienteBase = 0L;
		    this.diferencia = 0;
		    this.montoPago = 0;
	    	return;
	    }
	    
	    restante = this.montoPago;
	    for (FacturaPagosExt facturaPago : this.listFacturasRelacionadas) {
    		facturaPago.setPago(BigDecimal.ZERO);
	    	if (restante <= 0) 
	    		continue;
	    	
	    	requerido = facturaPago.getSaldoAnterior().doubleValue();
    		restante = restante - requerido;
	    	if (restante >= 0) {
	    		facturaPago.setPago(new BigDecimal(requerido));
	    		aplicado += requerido;
	    		continue;
	    	}
	    	
	    	restante = requerido + restante;
	    	facturaPago.setPago(new BigDecimal(restante));
    		aplicado += restante;
	    }
	    
	    this.diferencia = (this.montoPago - aplicado);
	    totalizarPago();
	}
	
	public void totalizarPago() {
		double insoluto = 0;
		
	    if (this.listFacturasRelacionadas == null || this.listFacturasRelacionadas.isEmpty()) {
	    	this.idClienteBase = 0L;
		    this.diferencia = 0;
		    this.montoPago = 0;
	    	return;
	    }
	    
	    // Sumamos pagos
		this.montoPago = 0;
		for (FacturaPagosExt pago : this.listFacturasRelacionadas) {
			this.montoPago += pago.getPago().doubleValue();
			if (pago.getSaldoInsoluto().doubleValue() < 0) 
				insoluto += pago.getSaldoInsoluto().doubleValue();
		}
		
		if (insoluto < 0) {
			populatePago();
			return;
		}
		
		this.montoPago += this.diferencia;
	}
	
	public void seleccionaTipoCambio() {
		Calendar calendar = null;
		List<MonedasValores> valores = null;
		MonedasValores valor = null;
		Moneda mOrigen = null;
		Moneda mDestino = null;
		double tipoCambio = 1;
		
		try {
			control();
			if (this.loginManager.getInfoSesion().getEmpresa().getMonedaId() == this.idMoneda) 
				return;
			
			// Validamos monedas
			mOrigen = this.ifzMoneda.findById(this.loginManager.getInfoSesion().getEmpresa().getMonedaId());
			mDestino = this.ifzMoneda.findById(this.idMoneda);
			if (mOrigen == null || mDestino == null) {
				log.warn("Una o ambas Monedas involucradas nulas");
				control(-1, "Ocurrio un problema al intentar recuperar el Tipo de Cambio para las Monedas involucradas");
				return;
			}
			
			// Validamos fecha
			calendar = Calendar.getInstance();
			calendar.setTime(this.fechaPago);
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			
			// Comprobamos Tipo de Cambio registrado
			valores = this.ifzMonValores.findByFecha(mOrigen, mDestino, calendar.getTime(), 0);
			if (valores == null || valores.isEmpty()) {
				control(-1, "Ocurrio un problema al intentar recuperar el Tipo de Cambio para la fecha del Pago.\nPor favor indiquelo manualmente");
				return;
			}
			
			valor = valores.get(0);
			if (valor.getValor() == null || valor.getValor().doubleValue() <= 0) {
				control(-1, "No existe registro de Tipo de Cambio en la fecha del Pago.\nPor favor indiquelo manualmente");
				return;
			}
			
			// Recupero el Tipo de Cambio
			tipoCambio = valor.getValor().doubleValue();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el Tipo de Cambio con la fecha del Pago.\nPor favor indiquelo manualmente", e);
			tipoCambio = 1;
		} finally {
			// Asigno el tipo de cambio
			this.tipoCambio = tipoCambio;
		}
	}
	
	public void enviarMensajeTransaccion() {
		
	}
	
	private boolean validaciones() {
		if (this.listFacturasRelacionadas == null || this.listFacturasRelacionadas.isEmpty()) {
			control(-1, "No selecciono ninguna Factura");
			return false;
		}
		
		if (this.idFormaPago <= 0L) {
			control(-1, "Debe indicar la Forma de Pago");
			return false;
		}
		
		if (this.idBancoOrigen <= 0L) {
			control(-1, "Debe indicar el Bando Origen");
			return false;
		}
		
		if (this.idCuentaBancaria <= 0L) {
			control(-1, "Debe indicar la Cuenta de Deposito");
			return false;
		}
		
		if (this.montoPago == 0L) {
			control(4, "Debe indicar el monto de pago");
			return false;
		}
		
		return true;
	}

	private void fillLists() {
		// Formas de pagos
		try {
			this.listFormasPagoItems = new ArrayList<SelectItem>();
			this.listFormasPago = this.ifzFormasPagos.findAll("claveSat");
			if (this.listFormasPago != null && ! this.listFormasPago.isEmpty()) {
				for (FormasPagos var : this.listFormasPago)
					this.listFormasPagoItems.add(new SelectItem(var.getId(), var.getId() + " - " + var.getFormaPago()));
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el catalago de Formas de Pago", e);
		}
		
		// Cuentas Bancarias
		try {
			this.listCuentasItems = new ArrayList<SelectItem>();
			this.listCuentas = this.ifzCtas.findLikeColumnName("numeroDeCuenta", "");
			if (this.listCuentas != null && ! this.listCuentas.isEmpty()) {
				for (CuentaBancaria var : this.listCuentas) 
					this.listCuentasItems.add(new SelectItem(var.getId(),  var.getNumeroDeCuenta() + " - " + var.getInstitucionBancaria().getNombreCorto()));
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el catalago de Cuentas Bancarias", e);
		}
		
		// Bancos
		try {
			this.listBancosItems = new ArrayList<SelectItem>();
			this.listBancos = this.ifzBancos.findLikeColumnName("nombreCorto", "");
			if (this.listBancos != null && ! this.listBancos.isEmpty()) {
				for (Banco var : this.listBancos) 
					this.listBancosItems.add(new SelectItem(var.getId(), var.getId() + " - " + var.getNombreCorto()));
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el catalago de Bancos", e);
		}
	}

	private boolean validaRequest(String param) {
		return validaRequest(param, null);
	}
	
	private boolean validaRequest(String param, String refValue) {
		if (! this.isDebug && ! getPermisoAdmin())
			return false;
		
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		if (refValue == null || "".equals(refValue.trim()))
			return true;
		
		return (refValue.trim().equals(this.paramsRequest.get(param).trim()));
	}

	private void validaRequest(String param, boolean value) {
		if (! this.isDebug && ! getPermisoAdmin())
			return;
		
		param = param.trim().toUpperCase();
		if (! this.paramsRequest.containsKey(param) && value) 
			this.paramsRequest.put(param, "1");
		else if (this.paramsRequest.containsKey(param) && ! value) 
			this.paramsRequest.remove(param);
	}

	private void cargarMonedas() {
		try {
			this.listMonedasItems = new ArrayList<SelectItem>();
			this.listMonedas = this.ifzMoneda.findAll();
			if (this.listMonedas != null && ! this.listMonedas.isEmpty()) {
				for (Moneda mon : this.listMonedas)
					this.listMonedasItems.add(new SelectItem(mon.getId(), mon.getNombre() + " (" + mon.getAbreviacion() + ")"));
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.cargarMonedas", e);
		}
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
		if (mensaje == null)
			mensaje = "Ocurrio un problema al procesar esta solicitud";
		this.band = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje =  mensaje.replace("\n", "<br/>");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	private void controlLog(String mensaje) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "???");
		log.info("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje + "\n");
	}
	
	// --------------------------------------------------------------------------------
	// BUSQUEDA FACTURAS
	// --------------------------------------------------------------------------------
	
	public void nuevaBusquedaFacturas() {
		this.campoBusquedaFacturas = this.tipoBusquedaFacturas.get(0).getValue().toString();
		this.valorBusquedaFacturas = "";
		
		this.numPaginaFacturas = 1;
		this.listBusquedaFacturas = new ArrayList<Factura>();
		this.pojoFacturaSeleccionada = null;
	}
	
	public void buscarFacturas() {
		List<Long> facturasAgregadas = null;
		List<Factura> facturas = null;
		
		try {
			control();
			facturasAgregadas = new ArrayList<Long>();
			this.listFacturasRelacionadas = (this.listFacturasRelacionadas != null ? this.listFacturasRelacionadas : new ArrayList<FacturaPagosExt>());
			for (FacturaPagosExt item : this.listFacturasRelacionadas)
				facturasAgregadas.add(item.getIdFactura().getId());

			this.campoBusquedaFacturas = (this.campoBusquedaFacturas != null && ! "".equals(this.campoBusquedaFacturas.trim()) ? this.campoBusquedaFacturas.trim() : this.tipoBusquedaFacturas.get(0).getValue().toString());
			this.valorBusquedaFacturas = (this.valorBusquedaFacturas != null && ! "".equals(this.valorBusquedaFacturas.trim()) ? this.valorBusquedaFacturas.trim() : "");
			
			this.ifzFacturas.setInfoSesion(this.loginManager.getInfoSesion());
			facturas = this.ifzFacturas.findLikeProperty(this.campoBusquedaFacturas, this.valorBusquedaFacturas, this.idClienteBase, "I", 0, false, false, "extract(year from model.fechaEmision) desc, model.folioFactura desc", 0);
			if (facturas != null && ! facturas.isEmpty()) {
				if (! facturasAgregadas.isEmpty()) {
					for (Factura factura : facturas) {
						if (facturasAgregadas.contains(factura.getId()))
							continue;
						this.listBusquedaFacturas.add(factura);
					}
				} else {
					this.listBusquedaFacturas.addAll(facturas);
					facturas.clear();
				}
			}
			
			if (this.listBusquedaFacturas == null || this.listBusquedaFacturas.isEmpty())
				control(2, "Busqueda de Facturas sin resultado");
		} catch(Exception e) {
			control("Ocurrio un problema al intentar realizar la busqueda de Facturas", e);
		} finally {
			this.numPaginaFacturas = 1;
			this.listBusquedaFacturas = (this.listBusquedaFacturas != null ? this.listBusquedaFacturas : new ArrayList<Factura>());
			controlLog(this.listBusquedaFacturas.size() + " Facturas encontradas");
		} 
	}
	
	public void seleccionarFactura() {
		FacturaPagosExt pago = null;
		Moneda pojoMoneda = null;
		BigDecimal facturaSaldo = BigDecimal.ZERO;
		BigDecimal saldoAnterior = BigDecimal.ZERO;
		double tipoCambio = 1;
		int parcialidad = 0;
		
		try {
			control();
			controlLog("Seleccionando factura ... ");
			if (this.pojoFacturaSeleccionada == null || this.pojoFacturaSeleccionada.getId() == null || this.pojoFacturaSeleccionada.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar la Factura seleccionada");
				return;
			}
			
			// Comprobamos adicion previa
			this.listFacturasRelacionadas = (this.listFacturasRelacionadas != null ? this.listFacturasRelacionadas : new ArrayList<FacturaPagosExt>());
			for (FacturaPagosExt facturaPago : this.listFacturasRelacionadas) {
				if (facturaPago.getIdFactura().getId().longValue() == this.pojoFacturaSeleccionada.getId().longValue()) {
					control(-1, "La Factura seleccionada ya fue agregada previamente");
					return;
				}
			}
			
			// Recuperamos id Cliente si corresponde
			if (this.idClienteBase <= 0L)
				this.idClienteBase = this.pojoFacturaSeleccionada.getIdCliente();
			
			if (this.monedaPagoId <= 0L)
				this.monedaPagoId = this.pojoFacturaSeleccionada.getIdMoneda();
			
			// Validamos Saldo
			facturaSaldo = BigDecimal.ZERO;
			facturaSaldo = saldo(this.pojoFacturaSeleccionada.getId());
			if (facturaSaldo.doubleValue() <= 0) {
				control(-1, "La Factura seleccionada ya esta liquidada");
				this.pojoFacturaSeleccionada = null;
				facturaSaldo = BigDecimal.ZERO;
				return;
			}
			
			// Moneda y Tipo de Cambio
			if (this.pojoFacturaSeleccionada.getIdMoneda() != null && this.pojoFacturaSeleccionada.getIdMoneda() > 0L) {
				if (this.idMoneda != this.pojoFacturaSeleccionada.getIdMoneda().longValue()) 
					this.idMoneda = this.pojoFacturaSeleccionada.getIdMoneda();
			}

			this.ifzMoneda.setInfoSesion(this.loginManager.getInfoSesion());
			pojoMoneda = this.ifzMoneda.findById(this.idMoneda);
			this.monedaPagoId = pojoMoneda.getId();
			this.monedaPago = pojoMoneda.getNombre();
			this.deshabilitarMoneda = true;
			if (this.idMoneda != this.loginManager.getInfoSesion().getEmpresa().getMonedaId()) {
				tipoCambio = this.loginManager.getTipoCambioPrevio();
				this.deshabilitarMoneda = false;
			}
			parcialidad = this.ifzPagos.findParcialidad(this.pojoFacturaSeleccionada.getId());
			saldoAnterior = facturaSaldo;
			if (saldoAnterior.doubleValue() <= 0 && parcialidad <= 1)
				saldoAnterior = this.pojoFacturaSeleccionada.getTotal();
			
			// Extendemos la factura y obtenemos datos del Cliente/Beneficiario
			controlLog("Asignamos datos al Pago ... ");
			pago = new FacturaPagosExt();
			pago.setFecha(Calendar.getInstance().getTime());
			pago.setIdFactura(this.ifzFacturas.convertir(this.pojoFacturaSeleccionada));
			pago.setIdBeneficiario(this.pojoFacturaSeleccionada.getIdCliente());
			pago.setBeneficiario(this.pojoFacturaSeleccionada.getCliente());
			pago.setTipoBeneficiario(this.pojoFacturaSeleccionada.getTipoCliente());
			pago.setParcialidad(parcialidad);
			pago.setIdMoneda(pojoMoneda);
			pago.setTipoCambio(tipoCambio);
			pago.setPago(facturaSaldo);
			pago.setSaldoAnterior(saldoAnterior);
			pago.setSaldoInsoluto(BigDecimal.ZERO);
			pago.setReferenciaFormaPago("000");
			pago.setObservaciones("");
			
			// Añadimos a lista
			this.listFacturasRelacionadas = (this.listFacturasRelacionadas != null ? this.listFacturasRelacionadas : new ArrayList<FacturaPagosExt>());
			this.listFacturasRelacionadas.add(pago);
			totalizarPago();
			nuevaBusquedaFacturas();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar la Factura seleccionada", e);
		} finally {
			controlLog("Factura seleccionada y asignada. Proceso terminado");
			controlLog("Saldo de Factura: " + facturaSaldo.doubleValue());
		}
	}

	public void quitarFactura() {
		try {
			control();
			this.listFacturasRelacionadas = (this.listFacturasRelacionadas != null ? this.listFacturasRelacionadas : new ArrayList<FacturaPagosExt>());
			this.listFacturasRelacionadasEliminadas = (this.listFacturasRelacionadasEliminadas != null ? this.listFacturasRelacionadasEliminadas : new ArrayList<FacturaPagosExt>());
			for (FacturaPagosExt pago : this.listFacturasRelacionadas) {
				if (pago.getIdFactura().getId().longValue() == this.idFacturaQuitar) {
					if (pago.getId() != null && pago.getId() > 0L)
						this.listFacturasRelacionadasEliminadas.add(pago);
					this.listFacturasRelacionadas.remove(this.listFacturasRelacionadas.indexOf(pago));
					break;
				}
			}
			
			// Totalizamos
			totalizarPago();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar quitar la Factura seleccionada", e);
		} 
	}
	
	private BigDecimal saldo(long idFactura) {
		BigDecimal saldo = BigDecimal.ZERO;
		
		try {
			control();
			if (idFactura > 0L) {
				saldo = this.ifzFacturas.calcularSaldo(idFactura, null, null);
				saldo = saldo.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar obtener el Saldo de la Factura", e);
			saldo = BigDecimal.ZERO;
		}
		
		return saldo;
	}

	// --------------------------------------------------------------------------------
	// TIMBRAR
	// --------------------------------------------------------------------------------
	
	public void evaluaTimbrar() {
		String serieComplementoPago = "";
		int controlTimbres = 0;
		
		try {
			control();
			if (this.listFacturasRelacionadas == null || this.listFacturasRelacionadas.isEmpty()) {
				control(-1, "Debe indicar al menos una Factura para poder realizar el proceso de Timbrado de Pagos");
				return;
			}
			
			// Validamos timbre de Facturas
			for (FacturaPagosExt fPago : this.listFacturasRelacionadas) {
				if (fPago.getIdFactura().getUuid() == null || "".equals(fPago.getIdFactura().getUuid().trim())) {
					control(-1, "Todas las Facturas deben estar timbradas para poder Timbrar el Pago");
					return;
				}
				
				if (fPago.getSerie() != null && ! serieComplementoPago.equals(fPago.getSerie().trim())) 
					serieComplementoPago = fPago.getSerie();
				
				if (fPago.getTimbrado() == 1)
					controlTimbres++;
			}
			
			if (controlTimbres > 0) {
				control(-1, "El Pago ya fue Timbrado previamente");
				return;
			}
			
			if (serieComplementoPago == null || "".equals(serieComplementoPago.trim())) {
				serieComplementoPago = this.loginManager.getInfoSesion().getEmpresa().getSerieComplementoPago();
				if (serieComplementoPago == null || "".equals(serieComplementoPago.trim())) {
					control(-1, "La Empresa no tiene asignado una serie para el Timbrado de Pagos");
					return;
				}
				
				for (FacturaPagosExt fPago : this.listFacturasRelacionadas) 
					fPago.setSerie(serieComplementoPago);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al evaluar el Pago indicado", e);
		}
	}
	
	public void timbrarPago() {
		Respuesta respuesta = null;
		// ----------------------------
		FacturaPagosTimbre timbre = null;
		boolean cfdiTesting = false;
		boolean cfdiNoTimbrar = false;
		// ----------------------------
		String serie = "";
		String folio = "";
	
		try {
			control();
			if (this.listFacturasRelacionadas == null || this.listFacturasRelacionadas.isEmpty()) {
				control(-1, "Debe indicar al menos una Factura para poder realizar el proceso de Timbrado de Pagos");
				return;
			}

			serie = this.listFacturasRelacionadas.get(0).getSerie();
			folio = this.listFacturasRelacionadas.get(0).getFolio();
			cfdiTesting = validaRequest("CFDI_TEST");
			cfdiNoTimbrar = validaRequest("NOTIMBRAR");
			
			this.ifzPagos.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzPagos.timbrar(this.listFacturasRelacionadas, serie, folio, this.isDebug, cfdiTesting, cfdiNoTimbrar);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al timbrar el Complemento de Pago\n" + respuesta.getErrores().getDescError());
				return;
			}
			
			timbre = (FacturaPagosTimbre) respuesta.getBody().getValor("pojoTimbre");
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al procesar el timbrado del Complemento de Pago.\nNo se pudo recuperar el Timbre");
				return;
			}
			
			// Actualizo el Timbre en los Pagos 
			for (FacturaPagosExt pago : this.listFacturasRelacionadas)
				pago.setIdTimbre(timbre);
			// Guardamos los pagos
			this.ifzPagos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPagos.saveOrUpdateListExt(this.listFacturasRelacionadas);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Timbrar el Complemento de Pago indicado", e);
		}
	}
	
	public void obtenerXMLBase() {
		FacturaPagosExt pago = null;
		String nombreDocumento = "";
		byte[] contenidoDocumento = null;
		
		try {
			control();
			controlLog("Descargando XML Base ... ");
			this.ifzPagos.setInfoSesion(this.loginManager.getInfoSesion());
			pago = this.ifzPagos.findByIdExt(this.idPago);
			if (pago == null || pago.getId() == null || pago.getId() <= 0L) {
				control(-1, "100 - Ocurrio un problema al intentar recuperar el Pago.\nDescarga abortada");
				return;
			}

			// Recuperamos XML
			if (pago.getIdTimbre() == null || pago.getIdTimbre().getId() == null || pago.getIdTimbre().getId() <= 0L) 
				contenidoDocumento = this.ifzPagos.formarXML(this.idPago).getBytes();
			else if (pago.getIdTimbre() != null && pago.getIdTimbre().getXmlPrevio() != null) 
				contenidoDocumento = pago.getIdTimbre().getXmlPrevio();
			else 
				contenidoDocumento = this.ifzPagos.formarXML(this.idPago).getBytes();
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				control(-1, "200 - Ocurrio un problema al intentar recuperar el XML previo al timbrado del Pago");
				return;
			}

			nombreDocumento = "P-" + pago.getSerie() + "-" + pago.getFolio() + "-previo.xml";
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", "xml");
			controlLog("Descarga XML lanzada");
		} catch (Exception e) { 
			control("Ocurrio un problema al intentar recuperar el XML de la factura", e); 
		} 
	}
	
	public void obtenerXML() {
		FacturaPagosExt pago = null;
		String nombreDocumento = "";
		byte[] contenidoDocumento = null;
		
		try {
			control();
			controlLog("Descargando XML ... ");
			this.ifzPagos.setInfoSesion(this.loginManager.getInfoSesion());
			pago = this.ifzPagos.findByIdExt(this.idPago);
			if (pago == null || pago.getId() == null || pago.getId() <= 0L) {
				control(-1, "100 - Ocurrio un problema al recuperar el Pago indicado");
				return;
			}
			
			if (pago.getIdTimbre() != null && (pago.getIdTimbre().getXmlTimbrado() == null || pago.getIdTimbre().getXmlTimbrado().length == 0)) {
				control(-1, "100 - Ocurrio un problema al recuperar el XML o el Pago no ha sido timbrado");
				return;
			}

			nombreDocumento = "P-" + pago.getSerie() + "-" + pago.getFolio() + ".xml";
			contenidoDocumento = pago.getIdTimbre().getXmlTimbrado();
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				control(-1, "200 - Ocurrio un problema al intentar recuperar el XML del Pago");
				return;
			}
			
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", "xml");
			controlLog("Descarga XML lanzada");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el XML de la factura", e);
		}
	}
	
	public void obtenerPDF() {
		FacturaPagosExt pago = null;
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			pago = this.ifzPagos.findByIdExt(this.idPago);
			if (pago == null || pago.getId() == null || pago.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Pago indicado");
				return;
			}
			
			if (pago.getIdTimbre() != null && (pago.getIdTimbre().getXmlTimbrado() == null || pago.getIdTimbre().getXmlTimbrado().length == 0)) {
				control(-1, "Ocurrio un problema al recuperar el Timbre o el Pago no ha sido timbrado");
				return;
			}
			
			
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idFacturaPago", pago.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.facturaPago"));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				controlLog("ERROR INTERNO: No se pudo procesar el reporte de Complemento de Pago (PDF).\n" + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al procesar el PDF del Complemento de Pago.\nContacte a su Administrador");
				return;
			}

			nombreDocumento = "P-" + pago.getSerie() + "-" + pago.getFolio();
			nombreDocumento = nombreDocumento + ("".equals(pago.getAgrupador().trim()) ? "_" + pago.getId() : "_" + pago.getAgrupador().replace("/", "-"));
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				control(-1, "Ocurrio un problema al recuperar el PDF del Complemento de Pago.\n" + respuesta.getErrores().getDescError());
				return;
			}
			
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
		} catch (Exception e) {
    		control("Ocurrio un problema al procesar el Complemento de Pago", e);
		} 
	}
	
	// ---------------------------------------
	// PROPIEDADES
	// ---------------------------------------

	public boolean getPermisoAdmin() {
		return ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()));
	}
	
	public void setPermisoAdmin(boolean value) {}

	public String getTitulo() {
		if (this.listFacturasRelacionadas != null && this.listFacturasRelacionadas.size() == 1)
			return "Pago " + this.listFacturasRelacionadas.get(0).getId();
		if (this.listFacturasRelacionadas != null && this.listFacturasRelacionadas.size() > 1)
			return "Pago Multiple";
		return "Nuevo Pago";
	}
	
	public void setTitulo(String value) {}

	public String getTituloTimbre() {
		if (this.listFacturasRelacionadas != null && this.listFacturasRelacionadas.size() == 1)
			return "Factura " + this.listFacturasRelacionadas.get(0).getIdFactura().getFolioFactura();
		if (this.listFacturasRelacionadas != null && this.listFacturasRelacionadas.size() > 1)
			return "Pago para " + this.listFacturasRelacionadas.size() + " Facturas";
		return "Timbrar Pago";
	}
	
	public void setTituloTimbre(String value) {}
	
	public boolean getTimbrable() {
		if (this.listFacturasRelacionadas != null && this.listFacturasRelacionadas.size() > 0)
			return this.listFacturasRelacionadas.get(0).getTimbrado() == 0;
		return false;
	}
	
	public void setTimbrable(boolean value) {}

	public boolean getTimbrado() {
		if (this.listFacturasRelacionadas != null && this.listFacturasRelacionadas.size() > 0)
			return this.listFacturasRelacionadas.get(0).getTimbrado() == 1;
		return false;
	}
	
	public void setTimbrado(boolean value) {}

	public boolean getCancelable() {
		if (this.listFacturasRelacionadas != null && ! this.listFacturasRelacionadas.isEmpty()) {
			for (FacturaPagosExt pago : this.listFacturasRelacionadas) {
				if (pago.getId() != null && pago.getId() > 0L)
					return true;
			}
		}
		
		return false;
	}
	
	public void setCancelable(boolean value) {}

	public boolean getHasId() {
		return this.hasId;
	}
	
	public void setHasId(boolean value) {}
	
	public String getPagoUuid() {
		if (this.listFacturasRelacionadas != null && this.listFacturasRelacionadas.size() > 0)
			return this.listFacturasRelacionadas.get(0).getIdTimbre().getUuid();
		return "";
	}
	
	public void setPagoUuid(String value) {}
	
	public String getUuid() {
		if (this.pojoFacPago != null && this.pojoFacPago.getTimbrado() == 1)
			return this.pojoFacPago.getIdTimbre().getUuid();
		return "";
	}
	
	public void setUuid(String value) {}
	
	public String getPagoVersion() {
		if (this.listFacturasRelacionadas != null && ! this.listFacturasRelacionadas.isEmpty())
			return this.listFacturasRelacionadas.get(0).getIdTimbre().getVersionTimbre();
		return "3.3";
	}
	
	public void setPagoVersion(String value) {}
	
	public String getPagoParcialidad() {
		if (this.listFacturasRelacionadas != null && ! this.listFacturasRelacionadas.isEmpty() && this.listFacturasRelacionadas.size() == 1)
			return (new DecimalFormat("#00")).format(this.listFacturasRelacionadas.get(0).getParcialidad());
		if (this.listFacturasRelacionadas != null && ! this.listFacturasRelacionadas.isEmpty() && this.listFacturasRelacionadas.size() > 1)
			return "NA";
		/*if (this.pojoFacPago != null && this.pojoFacPago.getIdFactura() != null && this.pojoFacPago.getId() != null && this.pojoFacPago.getId() > 0L)
			return (new DecimalFormat("#00")).format(this.pojoFacPago.getParcialidad());*/
		return "01";
	}
	
	public void setPagoParcialidad(String value) {}
	
	public String getPagoSerie() {
		if (this.listFacturasRelacionadas != null && ! this.listFacturasRelacionadas.isEmpty() && this.listFacturasRelacionadas.get(0).getSerie() != null && ! "".equals(this.listFacturasRelacionadas.get(0).getSerie().trim()))
			return this.listFacturasRelacionadas.get(0).getSerie();
		return this.getPermisoAdmin() ? "SYSP" : "<Sin serie asignada>";
	}
	
	public void setPagoSerie(String seriePago) {
		if (this.getPermisoAdmin()) {
			seriePago = (("".equals(seriePago) || "<Sin serie asignada>".equals(seriePago)) ? "SYS" : seriePago);
			for (FacturaPagosExt pago : this.listFacturasRelacionadas)
				pago.setSerie(seriePago);
		}
	}
	
	public String getPagoFolio() {
		if (this.listFacturasRelacionadas != null && ! this.listFacturasRelacionadas.isEmpty() && this.listFacturasRelacionadas.get(0).getFolio() != null && ! "".equals(this.listFacturasRelacionadas.get(0).getFolio().trim()))
			return this.listFacturasRelacionadas.get(0).getFolio();
		return this.getPermisoAdmin() ? ((new SimpleDateFormat("MMdd")).format(Calendar.getInstance().getTime())) : "<Se genera al Timbrar>";
	}
	
	public void setPagoFolio(String folioPago) {
		if (this.getPermisoAdmin()) {
			folioPago = (("".equals(folioPago) || "<Se genera al Timbrar>".equals(folioPago)) ? ((new SimpleDateFormat("MMdd")).format(Calendar.getInstance().getTime())) : folioPago);
			for (FacturaPagosExt pago : this.listFacturasRelacionadas)
				pago.setFolio(folioPago);
		}
	}
	
	public BigDecimal getMonto() {
		if (this.pojoFacPago != null && this.pojoFacPago.getPago() != null) 
			return this.pojoFacPago.getPago();
		return BigDecimal.ZERO;
	}
	
	public void setMonto(BigDecimal value) {
		if (value == null) 
			value = new BigDecimal(0L);
		this.pojoFacPago.setPago(value);
	}
	
	public List<FacturaPagos> getListFacPagos() {
		return listPagos;
	}
	
	public void setListFacPagos(List<FacturaPagos> listFacPagos) {
		this.listPagos = listFacPagos;
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

	public boolean getTesting() {
		return validaRequest("CFDI_TEST");
	}
	
	public void setTesting(boolean value) {
		validaRequest("CFDI_TEST", value);
	}

	public boolean getNoTimbrar() {
		return validaRequest("NOTIMBRAR");
	}

	public void setNoTimbrar(boolean value) {
		validaRequest("NOTIMBRAR", value);
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

	public List<FacturaPagosExt> getListFacturasRelacionadas() {
		return listFacturasRelacionadas;
	}

	public void setListFacturasRelacionadas(List<FacturaPagosExt> listFacturasRelacionadas) {
		this.listFacturasRelacionadas = listFacturasRelacionadas;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public double getMontoPago() {
		return montoPago;
	}

	public void setMontoPago(double montoPago) {
		this.montoPago = montoPago;
	}

	public double getDiferencia() {
		return diferencia;
	}

	public void setDiferencia(double diferencia) {
		this.diferencia = diferencia;
	}

	public long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public long getMonedaPagoId() {
		return monedaPagoId;
	}

	public void setMonedaPagoId(long monedaPagoId) {
		this.monedaPagoId = monedaPagoId;
	}

	public String getMonedaPago() {
		return monedaPago;
	}

	public void setMonedaPago(String monedaPago) {
		this.monedaPago = monedaPago;
	}
	
	public boolean getDeshabilitarMoneda() {
		return this.deshabilitarMoneda;
	}
	
	public void setDeshabilitarMoneda(boolean value) {}
	
	public List<SelectItem> getListMonedasItems() {
		return listMonedasItems;
	}
	
	public void setListMonedasItems(List<SelectItem> listMonedasItems) {
		this.listMonedasItems = listMonedasItems;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public long getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(long idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public String getReferenciaPago() {
		return referenciaPago;
	}

	public void setReferenciaPago(String referenciaPago) {
		this.referenciaPago = referenciaPago;
	}

	public long getIdBancoOrigen() {
		return idBancoOrigen;
	}

	public void setIdBancoOrigen(long idBancoOrigen) {
		this.idBancoOrigen = idBancoOrigen;
	}

	public long getIdCuentaBancaria() {
		return idCuentaBancaria;
	}

	public void setIdCuentaBancaria(long idCuentaBancaria) {
		this.idCuentaBancaria = idCuentaBancaria;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
		
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public long getIdFacturaQuitar() {
		return idFacturaQuitar;
	}

	public void setIdFacturaQuitar(long idFacturaQuitar) {
		this.idFacturaQuitar = idFacturaQuitar;
	}

	public boolean isEditando() {
		return editando;
	}

	public void setEditando(boolean editando) {
		this.editando = editando;
	}

	public List<SelectItem> getListAuditoriaItems() {
		return listAuditoriaItems;
	}

	public void setListAuditoriaItems(List<SelectItem> listAuditoriaItems) {
		this.listAuditoriaItems = listAuditoriaItems;
	}
}
