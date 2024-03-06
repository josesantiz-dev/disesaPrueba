package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

import net.giro.adp.beans.CobranzaMoneda;
import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraCobranza;
import net.giro.adp.logica.ObraCobranzaRem;
import net.giro.adp.logica.ObraRem;
import net.giro.cxc.logica.FacturaDetalleRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="edoCuentaAction")
public class EstadoCuentaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(EstadoCuentaAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	// Cobranza
	private FacturaRem ifzFacturas;
	private FacturaDetalleRem ifzFacConceptos;
	private ReportesRem	ifzReportes;
	private Obra pojoObra;
	private ObraCobranzaRem ifzCobranza;
	private List<CobranzaMoneda> listCobranza;
	private List<ObraCobranza> listObraCobranza;
	private List<ObraCobranza> listObraCobranzaMXN;
	private List<ObraCobranza> listObraCobranzaUSD;
	private int paginacionCobranza;
	private double porcentajeAnticipo;
	private double porcentajeRetencion;
	private double porcentajeAnticipoPrevio;
	private double porcentajeRetencionPrevio;
    private double porcentajeIva;
	private boolean permiteGuardarCobranza;
    private String decimalFormat;
    private String porcentajeFormat;
    private HashMap<Long, Double> mapTotalFactura;
	// Busqueda Obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObraSeleccionada;
	private List<SelectItem> opcionesBusquedaObra;
	private String campoBusquedaObra;
	private String valorBusquedaObra;
	private int paginacionObras;
	// Variables de control
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	// PERMISOS
	private EmpleadoRem ifzEmpleados;
	private boolean permisoAgregar;
	private boolean permisoEditar;
	private boolean permisoBorrar;
	private boolean permisoImprimir;
    // PERFILES
	private boolean PERFIL_ADMINISTRATIVO;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public EstadoCuentaAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String resValue = "";
		
		try {
			// Inicializaciones
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);

			// EVALUACION DE PERFILES
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.PERFIL_ADMINISTRATIVO = ((resValue != null && "S".equals(resValue)) ? true : false);
            
            // Obtenemos las propiedades de entorno del modulo
            ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			
			// Porcentaje IVA
			this.porcentajeIva = 1;
			resValue = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
			if (resValue != null && ! "".equals(resValue))
				this.porcentajeIva = Double.valueOf(resValue);
			
			// Interfaces
			ctx = new InitialContext();
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzCobranza = (ObraCobranzaRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraCobranzaFac!net.giro.adp.logica.ObraCobranzaRem");
			this.ifzFacturas = (FacturaRem)	ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacConceptos = (FacturaDetalleRem)	ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzCobranza.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzFacturas.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzFacConceptos.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion()); 
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;

			// Busqueda obras
			this.opcionesBusquedaObra = new ArrayList<SelectItem>();
			this.opcionesBusquedaObra.add(new SelectItem("*", "Coincidencia"));
			this.opcionesBusquedaObra.add(new SelectItem("nombre", "Obra"));
			this.opcionesBusquedaObra.add(new SelectItem("nombreCliente", "Cliente"));
			this.opcionesBusquedaObra.add(new SelectItem("id", "ID"));
			nuevaBusquedaObra();
			
			this.decimalFormat = generaFormato(13, 2, true);
			this.porcentajeFormat = generaFormato(3, 4, true);
			this.paginacionCobranza = 1;
			this.porcentajeAnticipo = 0;
			this.porcentajeRetencion = 0;
		} catch (Exception e) {
			log.error("Error al inicilizar " + this.getClass().getCanonicalName(), e);
		} finally {
			establecerPermisos();
		}
	}

	public void ver() {
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control("Debe seleccionar una Obra");
				return;
			}
			
    		controlLog("Validando permiso de escritura");
    		validarPermisos();

			// Recuperamos cobranza existente
			controlLog("Cobranza - Preparando ... ");
			this.porcentajeAnticipo = 0;
			this.porcentajeRetencion = 0;
			
			controlLog("Obra " + this.pojoObra.getId() + ". Recuperando Cobranza ...");
			this.ifzCobranza.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObraCobranza = this.ifzCobranza.findAll(this.pojoObra.getId(), "", 0);
			controlLog("Cobranza previa recuperada. Facturas x" + this.listObraCobranza.size());

			this.mapTotalFactura = new HashMap<Long, Double>();
			for (ObraCobranza item : this.listObraCobranza) {
				if (! this.mapTotalFactura.containsKey(item.getIdFactura()))
					this.mapTotalFactura.put(item.getIdFactura(), item.getTotal().doubleValue());
			}

			separarCobranza();
			controlLog("Cobranza - Proceso Terminado");
		} catch  (Exception e) {
			control("Ocurrio un problema al consultar las Facturas de la Obra seleccionada", e);
    	} finally {
			controlLog("Cobranza - Totalizando ... ");
			totalizarCobranza();
			guardarCobranza();
			this.permiteGuardarCobranza = false;
    	}

		// *** desde el 2018/01/22 se agregan todas las facturas (pagadas, abonadas, sin abono) y se añade una columna para mostrar lo abonado
		// *** desde el 2019/06/24 las facturas de egreso disminuyen el total facturado
	}
	
	public void guardar() {
		try {
			control();
			controlLog("Cobranza ... Preparando para guardar");
			if (! validaciones())
				return;
			
			controlLog("Cobranza ... actualizando listado");
			for (ObraCobranza var : this.listObraCobranza) {
				var.setIdObra(this.pojoObra);
				var.setNombreObra(this.pojoObra.getNombre());
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaCreacion(Calendar.getInstance().getTime());
				} 
				var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				var.setFechaModificacion(Calendar.getInstance().getTime());
			}
			
			// Guardamos la cobranza
			controlLog("Cobranza ... guardando");
			this.ifzCobranza.saveOrUpdateList(this.listObraCobranza);
			controlLog("Cobranza guardada!");
			ver();
		} catch(Exception e) {
			control("Ocurrio un problema al intentar guardar la Cobranza", e);
    	} 
	}

	public void quitarObra() {
		try {
			control();
			this.pojoObra = null;
			this.listObraCobranzaMXN = new ArrayList<ObraCobranza>();
			this.listObraCobranzaUSD = new ArrayList<ObraCobranza>();
			this.listCobranza = new ArrayList<CobranzaMoneda>();
			this.porcentajeAnticipo = 0;
			this.porcentajeRetencion = 0;
			this.porcentajeAnticipoPrevio = this.porcentajeAnticipo;
			this.porcentajeRetencionPrevio = this.porcentajeRetencion;
		} catch(Exception e) {
			control("Ocurrio un problema al intentar quitar la Obra", e);
		}
	}
	
	public void reporte() {
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			controlLog("Reporte COBRANZA. Preparando ... ");
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control("Debe seleccionar una Obra");
				return;
			}

			controlLog("Imprimiento reporte COBRANZA ... ");
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idObra", this.pojoObra.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  "159");
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO - " + respuesta.getErrores().getDescError());
				control("Ocurrio un problema al intentar imprimir la COBRANZA\nError " + respuesta.getErrores().getCodigoError());
				return;
			} 
			
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "COBRANZA-" + this.pojoObra.getId() + "." + formatoDocumento;
			
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("ERROR INTERNO - No se recupero el contenido de la COBRANZA");
				control("Ocurrio un problema al intentar imprimir la COBRANZA");
				return;
			}
			
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			controlLog("Reporte Ordenes de Compra de Exp Insumos lanzado. Proceso terminado!");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la COBRANZA", e);
		} 
	}

	public void reasignarPorcentajes() {
		// Cobranza PESOS
		for (ObraCobranza var : this.listObraCobranzaMXN) {
			if (var.getPorcentajeAnticipo().doubleValue() == this.porcentajeAnticipoPrevio || var.getPorcentajeAnticipo().doubleValue() == 0)
				var.setPorcentajeAnticipo(new BigDecimal(this.porcentajeAnticipo));
			if (var.getPorcentajeRetencion().doubleValue() == this.porcentajeRetencionPrevio || var.getPorcentajeRetencion().doubleValue() == 0)
				var.setPorcentajeRetencion(new BigDecimal(this.porcentajeRetencion));
		}

		// Cobranza DOLARES
		for (ObraCobranza var : this.listObraCobranzaUSD) {
			if (var.getPorcentajeAnticipo().doubleValue() == this.porcentajeAnticipoPrevio || var.getPorcentajeAnticipo().doubleValue() == 0)
				var.setPorcentajeAnticipo(new BigDecimal(this.porcentajeAnticipo));
			if (var.getPorcentajeRetencion().doubleValue() == this.porcentajeRetencionPrevio || var.getPorcentajeRetencion().doubleValue() == 0)
				var.setPorcentajeRetencion(new BigDecimal(this.porcentajeRetencion));
		}

		this.porcentajeAnticipoPrevio = this.porcentajeAnticipo;
		this.porcentajeRetencionPrevio = this.porcentajeRetencion;
		totalizarCobranza();
	}
	
	public void calculoReversaPorcentajes(AjaxBehaviorEvent event) {
		String value = "";
		// ------------------------------
		ObraCobranza item = null;
		double anticipo = 0;
		double retencion = 0;
		long idMoneda = 0;
		int index = -1;

		value = "";
		if (event.getComponent().getAttributes().get("targetMoneda") != null) 
			value = event.getComponent().getAttributes().get("targetMoneda").toString();
		if (value == null || "".equals(value.trim()))
			value = "0";
		idMoneda = Long.valueOf(value);

		value = "";
		if (event.getComponent().getAttributes().get("targetIndex") != null) 
			value = event.getComponent().getAttributes().get("targetIndex").toString();
		if (value == null || "".equals(value.trim()))
			value = "-1";
		index = Integer.valueOf(value);

		if (index >= 0) {
			if (10000001L == idMoneda) {
				item = this.listObraCobranzaMXN.get(index);
				anticipo  = item.getAmortizacion().doubleValue()  / item.getEstimacion().doubleValue();
				retencion = item.getFondoGarantia().doubleValue() / item.getEstimacion().doubleValue();
				anticipo  = (anticipo  > 0 ? anticipo  * 100 : 0);
				retencion = (retencion > 0 ? retencion * 100 : 0);
				item.setPorcentajeAnticipo(new  BigDecimal((new DecimalFormat(this.porcentajeFormat)).format(anticipo)));
				item.setPorcentajeRetencion(new BigDecimal((new DecimalFormat(this.porcentajeFormat)).format(retencion)));
				this.listObraCobranzaMXN.set(index, item);
			}
			
			if (10000002L == idMoneda) {
				item = this.listObraCobranzaUSD.get(index);
				anticipo  = item.getAmortizacion().doubleValue()  / item.getEstimacion().doubleValue();
				retencion = item.getFondoGarantia().doubleValue() / item.getEstimacion().doubleValue();
				anticipo  = (anticipo  > 0 ? anticipo  * 100 : 0);
				retencion = (retencion > 0 ? retencion * 100 : 0);
				item.setPorcentajeAnticipo(new  BigDecimal((new DecimalFormat(this.porcentajeFormat)).format(anticipo)));
				item.setPorcentajeRetencion(new BigDecimal((new DecimalFormat(this.porcentajeFormat)).format(retencion)));
				this.listObraCobranzaUSD.set(index, item);
			}
		}
		
		totalizarCobranza();
	}
	
	public void totalizarCobranza() {
		HashMap<Double, Integer> promPorAnticipo = new HashMap<Double, Integer>();
		HashMap<Double, Integer> promPorRetencion = new HashMap<Double, Integer>();
		
		totalizarCobranzaMXN();
		totalizarCobranzaUSD();

		for (ObraCobranza item : this.listObraCobranzaMXN) {
			promPorAnticipo  = contadorLlaves(promPorAnticipo,  item.getPorcentajeAnticipo().doubleValue());
			promPorRetencion = contadorLlaves(promPorRetencion, item.getPorcentajeRetencion().doubleValue());
		}
		
		for (ObraCobranza item : this.listObraCobranzaUSD) {
			promPorAnticipo  = contadorLlaves(promPorAnticipo,  item.getPorcentajeAnticipo().doubleValue());
			promPorRetencion = contadorLlaves(promPorRetencion, item.getPorcentajeRetencion().doubleValue());
		}

		this.porcentajeAnticipo  = maxLlave(promPorAnticipo);
		this.porcentajeRetencion = maxLlave(promPorRetencion);
		this.porcentajeAnticipoPrevio = this.porcentajeAnticipo;
		this.porcentajeRetencionPrevio = this.porcentajeRetencion;
	}
	
	private void guardarCobranza() {
		try {
			controlLog("Cobranza ... inicializando");
			this.listObraCobranza = (this.listObraCobranza != null ? this.listObraCobranza : new ArrayList<ObraCobranza>());
			this.listObraCobranzaMXN = (this.listObraCobranzaMXN != null ? this.listObraCobranzaMXN : new ArrayList<ObraCobranza>());
			this.listObraCobranzaUSD = (this.listObraCobranzaUSD != null ? this.listObraCobranzaUSD : new ArrayList<ObraCobranza>());

			controlLog("Cobranza ... unificando MXN");
			for (ObraCobranza itemMXN : this.listObraCobranzaMXN) {
				for (ObraCobranza item : this.listObraCobranza) {
					if (item.getIdFactura().longValue() == itemMXN.getIdFactura().longValue() && item.getIdConcepto().longValue() == itemMXN.getIdConcepto().longValue()) {
						item = itemMXN;
						break;
					}
				}
			}

			controlLog("Cobranza ... unificando USD");
			for (ObraCobranza itemMXN : this.listObraCobranzaUSD) {
				for (ObraCobranza item : this.listObraCobranza) {
					if (item.getIdFactura().longValue() == itemMXN.getIdFactura().longValue() && item.getIdConcepto().longValue() == itemMXN.getIdConcepto().longValue()) {
						item = itemMXN;
						break;
					}
				}
			}

			controlLog("Cobranza ... consolidando");
			for (ObraCobranza var : this.listObraCobranza) {
				var.setIdObra(this.pojoObra);
				var.setNombreObra(this.pojoObra.getNombre());
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaCreacion(Calendar.getInstance().getTime());
				} 
				var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				var.setFechaModificacion(Calendar.getInstance().getTime());
			}

			controlLog("Cobranza ... guardando");
			this.ifzCobranza.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCobranza.actualizarCobranza(this.pojoObra.getId(), this.listObraCobranza);
			controlLog("Cobranza ... terminado!");
		} catch (Exception e) {
			log.error("Ocurrio un problema al guardar los cambios automaticos de Cobranza", e);
		}
	}
	
	private void totalizarCobranzaMXN() {
		boolean baseEstimacion = false;
		Double monto = 0D;
		double porcentaje = 0;
		int index = 0;
		
		this.permiteGuardarCobranza = true;
		if (this.listObraCobranzaMXN == null) {
			this.listObraCobranzaMXN = new ArrayList<ObraCobranza>();
			this.permiteGuardarCobranza = false;
		}
		
		controlLog("Cobranza detalles ... Totalizamos");
		for (ObraCobranza var : this.listObraCobranzaMXN) {
			// Amortizacion
			controlLog("Cobranza detalles ... Calculamos amortizacion");
			porcentaje = getMonto(var.getPorcentajeAnticipo(), 4);
			porcentaje = porcentaje / 100;
			monto = (var.getEstimacion().doubleValue() * porcentaje);
			var.setAmortizacion(getMonto(monto, 2)); 
			
			// Fondo Garantia
			controlLog("Cobranza detalles ... Calculamos fondo garantia");
			porcentaje = getMonto(var.getPorcentajeRetencion(), 4);
			porcentaje = porcentaje / 100;
			monto = (var.getEstimacion().doubleValue() * porcentaje);
			var.setFondoGarantia(getMonto(monto, 2));
			
			// Subtotal
			if (var.getEstimacion().doubleValue() > 0) {
				baseEstimacion = true;
				controlLog("Cobranza detalles ... Obtenemos estimacion y calculamos subtotal");
				monto = getMonto(var.getEstimacion(), 2) - (getMonto(var.getAmortizacion(), 2) + getMonto(var.getFondoGarantia(), 2));
				controlLog("Cobranza detalles ... Asignamos monto (Subtotal)");
				var.setSubtotal(getMonto(monto, 2)); 
			} else if (var.getAnticipo().doubleValue() > 0) {
				baseEstimacion = false;
				controlLog("Cobranza detalles ... Obtenemos anticipo y lo asignamos al Subtotal");
				var.setAmortizacion(BigDecimal.ZERO); 
				var.setFondoGarantia(BigDecimal.ZERO);
				var.setSubtotal(var.getAnticipo()); 
			} else {
				controlLog("Cobranza detalles ... descartado");
				var.setAmortizacion(BigDecimal.ZERO); 
				var.setFondoGarantia(BigDecimal.ZERO);
				var.setSubtotal(BigDecimal.ZERO);
				var.setIva(BigDecimal.ZERO);
				var.setCargos(BigDecimal.ZERO);
				var.setTotal(BigDecimal.ZERO);
				continue;
			}
			
			// IVA y tasa IVA
			controlLog("Cobranza detalles ... Calculamos IVA");
			porcentaje = getMonto(var.getPorcentajeIva(), 4);
			porcentaje = (porcentaje > 0 && porcentaje > 1 ? (porcentaje / 100) : porcentaje);
			monto = (var.getSubtotal().doubleValue() * porcentaje);
			var.setPorcentajeIva(getMonto(getMonto(var.getPorcentajeIva(), 4), 4));
			var.setIva(getMonto(monto, 2)); 
			
			// Total
			controlLog("Cobranza detalles ... Calculamos total ((Subtotal + IVA) - cargosCliente)");
			monto = (var.getSubtotal().doubleValue() + var.getIva().doubleValue()) - var.getCargos().doubleValue();
			monto = (baseEstimacion && var.getAnticipo().doubleValue() > 0 ? (monto + (var.getAnticipo().doubleValue() * (1 + porcentaje))) : monto);
			var.setTotal(getMonto(monto, 2));
			
			// Si asignamos valor a Anticipo o Estimacion, validamos si el total corresponde al monto de la factura
			/* ESTA VALIDACION HA SIDO DESHABILITADA POR AUTORIZACION DE LA ARQ. CAROLINA EL 2018/01/22 ... Movido a ciclo de guardado
			if (this.permiteGuardarCobranza && (var.getAnticipo().doubleValue() > 0 || var.getEstimacion().doubleValue() > 0))
				this.permiteGuardarCobranza = validaCobranza(var.getMonto(), var.getTotal());*/
		}

		if (! this.listObraCobranzaMXN.isEmpty()) {
			for (CobranzaMoneda item : this.listCobranza) {
				if (item.getIdMoneda() == this.listObraCobranzaMXN.get(0).getIdMoneda()) {
					index = this.listCobranza.indexOf(item);
					item.setListCobranza(this.listObraCobranzaMXN);
					this.listCobranza.set(index, item);
					this.listCobranza.get(index).calcular();
					break;
				}
			}
		}
	}
	
	private void totalizarCobranzaUSD() {
		boolean baseEstimacion = false;
		Double monto = 0D;
		double porcentaje = 0;
		int index = 0;
		
		this.permiteGuardarCobranza = true;
		if (this.listObraCobranzaUSD == null) {
			this.listObraCobranzaUSD = new ArrayList<ObraCobranza>();
			this.permiteGuardarCobranza = false;
		}
		
		controlLog("Cobranza detalles ... Totalizamos");
		for (ObraCobranza var : this.listObraCobranzaUSD) {
			// Amortizacion
			controlLog("Cobranza detalles ... Calculamos amortizacion");
			porcentaje = getMonto(var.getPorcentajeAnticipo(), 4);
			porcentaje = porcentaje / 100;
			monto = (var.getEstimacion().doubleValue() * porcentaje);
			var.setAmortizacion(getMonto(monto, 2)); 
			
			// Fondo Garantia
			controlLog("Cobranza detalles ... Calculamos fondo garantia");
			porcentaje = getMonto(var.getPorcentajeRetencion(), 4);
			porcentaje = porcentaje / 100;
			monto = ((var.getEstimacion().doubleValue() * porcentaje) / 100);
			var.setFondoGarantia(getMonto(monto, 2));

			// Subtotal
			if (var.getEstimacion().doubleValue() > 0) {
				baseEstimacion = true;
				controlLog("Cobranza detalles ... Obtenemos estimacion y calculamos subtotal");
				monto = getMonto(var.getEstimacion(), 2) - (getMonto(var.getAmortizacion(), 2) + getMonto(var.getFondoGarantia(), 2));
				controlLog("Cobranza detalles ... Asignamos monto (Subtotal)");
				var.setSubtotal(getMonto(monto, 2)); 
			} else if (var.getAnticipo().doubleValue() > 0) {
				baseEstimacion = false;
				var.setAmortizacion(BigDecimal.ZERO); 
				var.setFondoGarantia(BigDecimal.ZERO);
				controlLog("Cobranza detalles ... Obtenemos anticipo y lo asignamos al Subtotal");
				var.setSubtotal(var.getAnticipo()); 
			} else {
				controlLog("Cobranza detalles ... descartado");
				var.setAmortizacion(BigDecimal.ZERO); 
				var.setFondoGarantia(BigDecimal.ZERO);
				var.setSubtotal(BigDecimal.ZERO);
				var.setIva(BigDecimal.ZERO);
				var.setCargos(BigDecimal.ZERO);
				var.setTotal(BigDecimal.ZERO);
				continue;
			}

			// IVA y tasa IVA
			controlLog("Cobranza detalles ... Calculamos IVA");
			porcentaje = getMonto(var.getPorcentajeIva(), 4);
			porcentaje = (porcentaje > 0 && porcentaje > 1 ? (porcentaje / 100) : porcentaje);
			monto = (var.getSubtotal().doubleValue() * porcentaje);
			var.setPorcentajeIva(getMonto(getMonto(var.getPorcentajeIva(), 4), 4));
			var.setIva(getMonto(monto, 2)); 

			// Total
			controlLog("Cobranza detalles ... Calculamos total ((Subtotal + IVA) - cargosCliente)");
			monto = (var.getSubtotal().doubleValue() + var.getIva().doubleValue()) - var.getCargos().doubleValue();
			monto = (baseEstimacion && var.getAnticipo().doubleValue() > 0 ? (monto + (var.getAnticipo().doubleValue() * (1 + porcentaje))) : monto);
			var.setTotal(getMonto(monto, 2));

			// Si asignamos valor a Anticipo o Estimacion, validamos si el total corresponde al monto de la factura
			/*
			 * ESTA VALIDACION HA SIDO DESHABILITADA POR AUTORIZACION DE LA ARQ. CAROLINA EL 2018/01/22 ... Movido a ciclo de guardado
			if (this.permiteGuardarCobranza && (var.getAnticipo().doubleValue() > 0 || var.getEstimacion().doubleValue() > 0))
				this.permiteGuardarCobranza = validaCobranza(var.getMonto(), var.getTotal());*/
		}
		
		if (! this.listObraCobranzaUSD.isEmpty()) {
			for (CobranzaMoneda item : this.listCobranza) {
				if (item.getIdMoneda() == this.listObraCobranzaUSD.get(0).getIdMoneda()) {
					index = this.listCobranza.indexOf(item);
					item.setListCobranza(this.listObraCobranzaUSD);
					this.listCobranza.set(index, item);
					this.listCobranza.get(index).calcular();
					break;
				}
			}
		}
	}
	
	private String generaFormato(int numeros, int decimales, boolean forzarDecimales) {
		String separador = ",";
		String formatResult = "";
		String reverse = "";
		
		for (int i = 0; i < numeros; i++) {
			if (i > 0 && (i % 3) == 0)
				formatResult += separador;
			formatResult += (i == 0) ? "0" : "#";
		}
		
		for (int i = formatResult.length() - 1; i >= 0; i--)
            reverse = reverse + formatResult.charAt(i);
		formatResult = reverse;
		
		if (decimales > 0) {
			formatResult += ".";
			for (int i = 0; i < decimales; i++) 
				formatResult += (forzarDecimales ? "0" : "#");
			formatResult = formatResult.replace("#.##", "0.00");
		}
		
		return formatResult;
	}

	private Double getMonto(BigDecimal value, int decimales) {
		return value.setScale(decimales, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
	private BigDecimal getMonto(Double value, int decimales) {
		return new BigDecimal(value).setScale(decimales, BigDecimal.ROUND_HALF_EVEN);
	}

	private boolean validaciones() {
		controlLog("Cobranza ... validando obra");
		if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
			control("Ocurrio un problema al recuperar la Obra seleccionada");
			return false;
		}
		
		controlLog("Cobranza ... inicializando");
		this.listObraCobranza = (this.listObraCobranza != null ? this.listObraCobranza : new ArrayList<ObraCobranza>());
		this.listObraCobranzaMXN = (this.listObraCobranzaMXN != null ? this.listObraCobranzaMXN : new ArrayList<ObraCobranza>());
		this.listObraCobranzaUSD = (this.listObraCobranzaUSD != null ? this.listObraCobranzaUSD : new ArrayList<ObraCobranza>());

		// Pesos
		controlLog("Cobranza ... Unificando MXN");
		for (ObraCobranza itemMXN : this.listObraCobranzaMXN) {
			for (ObraCobranza item : this.listObraCobranza) {
				if (item.getIdFactura().longValue() == itemMXN.getIdFactura().longValue() && item.getIdConcepto().longValue() == itemMXN.getIdConcepto().longValue()) {
					item = itemMXN;
					break;
				}
			}
		}
		
		// Dolares
		controlLog("Cobranza ... Unificando USD");
		for (ObraCobranza itemMXN : this.listObraCobranzaUSD) {
			for (ObraCobranza item : this.listObraCobranza) {
				if (item.getIdFactura().longValue() == itemMXN.getIdFactura().longValue() && item.getIdConcepto().longValue() == itemMXN.getIdConcepto().longValue()) {
					item = itemMXN;
					break;
				}
			}
		}
		
		if (this.listObraCobranza.size() <= 0) {
			control("Sin detalles para guardar");
			return false;
		}
		
		/*
		 * ESTA VALIDACION HA SIDO DESHABILITADO POR AUTORIZACION DE LA ARQ. CAROLINA EL 2018/01/22
		if (! this.permiteGuardarCobranza) {
			control(6, "Verifique, la columna Monto debe coincidir con la columna Total");
			return false;
		} */
		
		return true;
	}

	private HashMap<Double, Integer> contadorLlaves(HashMap<Double, Integer> mapa, Double llave) {
		int contador = 0;
		
		if (mapa.containsKey(llave))
			contador = mapa.get(llave);
		contador += 1;
		mapa.put(llave, contador);
		
		return mapa;
	}
	
	private double maxLlave(HashMap<Double, Integer> mapa) {
		double llave = 0;
		int contador = 0;
		
		for (Entry<Double, Integer> item : mapa.entrySet()) {
			if (item.getValue() > contador) {
				contador = item.getValue();
				llave = item.getKey();
			} if (item.getValue() == contador && llave < item.getKey()) {
				contador = item.getValue();
				llave = item.getKey();
			}
		}
		
		return llave;
	}
	
	private void separarCobranza() {
		this.listObraCobranzaMXN = new ArrayList<ObraCobranza>();
		this.listObraCobranzaUSD = new ArrayList<ObraCobranza>();
		
		controlLog("Separando cobranza por monedas ... ");
		for (ObraCobranza item : this.listObraCobranza) {
			if (item.getIdMoneda() == 10000001L)
				this.listObraCobranzaMXN.add(item);
			else if (item.getIdMoneda() == 10000002L)
				this.listObraCobranzaUSD.add(item);
		}

		this.paginacionCobranza = 1;
		this.listCobranza = new ArrayList<CobranzaMoneda>();
		this.listCobranza.add(new CobranzaMoneda(10000001, "PESOS",   "MXN", this.listObraCobranzaMXN));
		this.listCobranza.add(new CobranzaMoneda(10000002, "DOLARES", "USD", this.listObraCobranzaUSD));
	}
	
	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(String mensaje) {
		control(true, -1, mensaje, null);
	}
	
	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim().replace("\n", "<br/>") : "");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	private void controlLog(String mensaje) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "???");
		log.info("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje + "\n");
	}
	
	// -------------------------------------------------------------------------------------------------------------------
	// Busqueda Obra 
	// -------------------------------------------------------------------------------------------------------------------
	
	public void nuevaBusquedaObra() {
		this.campoBusquedaObra = this.opcionesBusquedaObra.get(0).getValue().toString();
		this.valorBusquedaObra = "";
		this.paginacionObras = 1;

		this.listObras = new ArrayList<Obra>();
		this.pojoObraSeleccionada = null;
	}

	public void buscarObras() {
		try {
			control();
			if ("".equals(this.campoBusquedaObra))
				this.campoBusquedaObra = this.opcionesBusquedaObra.get(0).getValue().toString();

			controlLog("Cobranza ... buscando obras");
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valorBusquedaObra, this.PERFIL_ADMINISTRATIVO, "", 0); 
			if (this.listObras == null || this.listObras.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch(Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
    	} finally {
			this.paginacionObras = 1;
    		this.listObras = (this.listObras != null ? this.listObras : new ArrayList<Obra>());
			controlLog("Cobranza ... se encontraron " + this.listObras.size() + " obras");
    	}
	}
	
	public void seleccionarObra() {
		try {
			control();
			this.pojoObra = new Obra();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(this.pojoObra, this.pojoObraSeleccionada);
			ver();
		} catch(Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

	public String getNombreObra() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L)
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
		return "";
	}
	
	public void setNombreObra(String value) {}

	public String getNombreCliente() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L)
			return this.pojoObra.getNombreCliente();
		return "";
	}
	
	public void setNombreCliente(String value) {}
	
	public List<CobranzaMoneda> getListCobranza() {
		return listCobranza;
	}

	public void setListCobranza(List<CobranzaMoneda> listCobranza) {
		this.listCobranza = listCobranza;
	}

	public int getPaginacionCobranza() {
		return paginacionCobranza;
	}

	public void setPaginacionCobranza(int paginacionCobranza) {
		this.paginacionCobranza = paginacionCobranza;
	}

	public double getPorcentajeAnticipo() {
		return porcentajeAnticipo;
	}

	public void setPorcentajeAnticipo(double porcentajeAnticipo) {
		if (this.porcentajeAnticipoPrevio <= 0 || this.porcentajeAnticipo != porcentajeAnticipo)
			this.porcentajeAnticipoPrevio = this.porcentajeAnticipo;
		this.porcentajeAnticipo = porcentajeAnticipo;
	}

	public double getPorcentajeRetencion() {
		return porcentajeRetencion;
	}

	public void setPorcentajeRetencion(double porcentajeRetencion) {
		if (this.porcentajeRetencionPrevio <= 0 || this.porcentajeRetencion != porcentajeRetencion)
			this.porcentajeRetencionPrevio = this.porcentajeRetencion;
		this.porcentajeRetencion = porcentajeRetencion;
	}

	public double getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(double porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public List<Obra> getListObras() {
		return listObras;
	}

	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public List<SelectItem> getOpcionesBusquedaObra() {
		return opcionesBusquedaObra;
	}

	public void setOpcionesBusquedaObra(List<SelectItem> opcionesBusquedaObra) {
		this.opcionesBusquedaObra = opcionesBusquedaObra;
	}

	public String getCampoBusquedaObra() {
		return campoBusquedaObra;
	}

	public void setCampoBusquedaObra(String campoBusquedaObra) {
		this.campoBusquedaObra = campoBusquedaObra;
	}

	public String getValorBusquedaObra() {
		return valorBusquedaObra;
	}

	public void setValorBusquedaObra(String valorBusquedaObra) {
		this.valorBusquedaObra = valorBusquedaObra;
	}

	public int getPaginacionObras() {
		return paginacionObras;
	}

	public void setPaginacionObras(int paginacionObras) {
		this.paginacionObras = paginacionObras;
	}

	public boolean isBand() {
		return operacionCancelada;
	}

	public void setBand(boolean operacionCancelada) {
		this.operacionCancelada = operacionCancelada;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getDecimalFormat() {
		return decimalFormat;
	}

	public void setDecimalFormat(String decimalFormat) {
		this.decimalFormat = decimalFormat;
	}

	public String getPorcentajeFormat() {
		return porcentajeFormat;
	}

	public void setPorcentajeFormat(String porcentajeFormat) {
		this.porcentajeFormat = porcentajeFormat;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public boolean isEsAdministrativo() {
		return PERFIL_ADMINISTRATIVO;
	}

	public void setEsAdministrativo(boolean esAdministrativo) {}

	public Obra getPojoObraSeleccionada() {
		return pojoObraSeleccionada;
	}

	public void setPojoObraSeleccionada(Obra pojoObraSeleccionada) {
		this.pojoObraSeleccionada = pojoObraSeleccionada;
	}

	// ----------------------------------------------------------------------
	// PERMISOS
	// ----------------------------------------------------------------------
	
	public boolean getGuardar() {
		return this.permiteGuardarCobranza;
	}
	
	public void setGuardar(boolean value) {}

    private void establecerPermisos() {
    	this.permisoAgregar = true;
    	this.permisoEditar = true;
    	this.permisoBorrar = true;
    	this.permisoImprimir = true;
    }
    
    private void validarPermisos() {
    	this.permisoAgregar = true;
    	this.permisoEditar = true;
    	this.permisoBorrar = true;
    	this.permisoImprimir = true;
    }
    
	public boolean isPermisoEscritura() {
		return true;
	}

	public void setPermisoEscritura(boolean permisoAgregar) {}

	public boolean isPermisoAgregar() {
		return permisoAgregar;
	}

	public void setPermisoAgregar(boolean permisoAgregar) {
		this.permisoAgregar = permisoAgregar;
	}

	public boolean isPermisoEditar() {
		return permisoEditar;
	}

	public void setPermisoEditar(boolean permisoEditar) {
		this.permisoEditar = permisoEditar;
	}

	public boolean isPermisoBorrar() {
		return permisoBorrar;
	}

	public void setPermisoBorrar(boolean permisoBorrar) {
		this.permisoBorrar = permisoBorrar;
	}

	public boolean isPermisoImprimir() {
		return permisoImprimir;
	}

	public void setPermisoImprimir(boolean permisoImprimir) {
		this.permisoImprimir = permisoImprimir;
	}
}
