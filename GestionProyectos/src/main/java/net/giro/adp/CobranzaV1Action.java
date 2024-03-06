package net.giro.adp;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

import org.apache.log4j.Logger;

import net.giro.adp.beans.CobranzaMoneda;
import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraCobranza;
import net.giro.adp.logica.ObraCobranzaRem;
import net.giro.adp.logica.ObraRem;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.logica.FacturaDetalleRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.cxc.util.FACTURA_ESTATUS;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.MonedaDAO;

@ViewScoped
@ManagedBean(name="cobranzaAction")
public class CobranzaV1Action implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CobranzaV1Action.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private InitialContext ctx; 
	// Interfaces
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private ReportesRem	ifzReportes;
	private FacturaRem ifzFacturas;
	private FacturaDetalleRem ifzFacConceptos;
	// Cobranza
	private List<CobranzaMoneda> listCobranza;
	private int paginacionCobranza;
	private ObraCobranzaRem ifzCobranza;
	private List<ObraCobranza> listObraCobranza;
	private double porcentajeAnticipo;
	private double porcentajeRetencion;
	private int numPaginaObraCobranzaMXN;
	private int numPaginaObraCobranzaUSD;
	private boolean permiteGuardarCobranza;
	private Obra pojoObra;
	private long idObra;
	// Cobranza MXN
	private List<ObraCobranza> listObraCobranzaMXN;
	private double totalCobranza;
    private double sumFacturado;
    private double sumAnticipo;
    private double sumEstimacion;
    private double sumAmortizacion;
    private double sumGarantia;
    private double sumSubtotal;
    private double sumIva;
    private double sumEstimacionTotal;
    private double sumCobrado;
	// Cobranza USD
	private List<ObraCobranza> listObraCobranzaUSD;
	private double totalCobranzaUSD;
    private double sumFacturadoUSD;
    private double sumAnticipoUSD;
    private double sumEstimacionUSD;
    private double sumAmortizacionUSD;
    private double sumGarantiaUSD;
    private double sumSubtotalUSD;
    private double sumIvaUSD;
    private double sumEstimacionTotalUSD;
    private double sumCobradoUSD;
	// Busqueda
	private List<SelectItem> busquedaOpciones;	
	private String busquedaCampo;
	private String busquedaValor;
	private int busquedaTipo;
	private int busquedaPaginas;
	private boolean busquedaAdministrativas;
	// Variables de control
	private boolean incompleto;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
    private long usuarioId;
    private String usuario;
    private double porcentajeIva;
    private int decimales;
    private String decimalFormat;
    private String porcentajeFormat;
    private HashMap<Long, Double> mapTotalFactura;
    // Moneda
    private MonedaDAO ifzMonedas;
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
	
	public CobranzaV1Action() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		Map<String, String> params = null;
		String resValue = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			// Inicializaciones
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

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
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzCobranza = (ObraCobranzaRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraCobranzaFac!net.giro.adp.logica.ObraCobranzaRem");
			this.ifzFacturas = (FacturaRem)	this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacConceptos = (FacturaDetalleRem)	this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			this.ifzReportes 	= (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzCobranza.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzFacturas.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzFacConceptos.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion()); 
			
			this.paramsRequest = new HashMap<String, String>();
			params = fc.getExternalContext().getRequestParameterMap();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("*", "Coincidencia"));
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "ID"));
			this.busquedaCampo = this.busquedaOpciones.get(0).getValue().toString();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
			this.numPaginaObraCobranzaMXN = 1;
			this.numPaginaObraCobranzaUSD = 1;
			this.decimales = 2;
			this.decimalFormat = generaFormato(12, this.decimales);//"###,###,###,##0.0#";
			this.porcentajeFormat = generaFormato(3, this.decimales);//"##0.0#";
		} catch (Exception e) {
			log.error("Error en constructor CobranzaAction", e);
			this.ctx = null;
		} finally {
			establecerPermisos();
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = this.busquedaOpciones.get(0).getValue().toString();

			this.busquedaPaginas = 1;
			if (this.listObras != null)
				this.listObras.clear();

			this.busquedaTipo = 0;
			if (this.busquedaAdministrativas)
				this.busquedaTipo = 4;

			log.info("Cobranza ... buscando obras");
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo, false, "", 0);
			if (this.listObras == null || this.listObras.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}

			log.info("Cobranza ... se encontraron " + this.listObras.size() + " obras");
			this.pojoObra = null;
		} catch(Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
    	}
	}
	
	public void ver() {
		BigDecimal saldo = BigDecimal.ZERO;
		BigDecimal cobrado = BigDecimal.ZERO;
		
		try {
			control();
			this.pojoObra = this.ifzObras.findById(this.idObra);
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control("Debe seleccionar una Obra");
				return;
			}
    		log.info("Validando permiso de escritura");
    		validarPermisos();

			// Recuperamos cobranza existente
			log.info("Cobranza - Preparando ... ");
			this.porcentajeAnticipo = 0;
			this.porcentajeRetencion = 0;
			this.mapTotalFactura = new HashMap<Long, Double>();
			log.info("Obra " + this.pojoObra.getId() + ". Recuperando Cobranza ...");
			this.ifzCobranza.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObraCobranza = this.ifzCobranza.findAll(this.pojoObra.getId(), "", 0);
			log.info("Cobranza previa recuperada. Facturas x" + this.listObraCobranza.size());
			
			// Obtenemos porcentajes de cobranza y genero mapa de facturas
			for (ObraCobranza item : this.listObraCobranza) {
				// Tomamos los porcentajes de Anticipos y Retenciones del item
				if (item.getPorcentajeAnticipo().doubleValue() > 0 && item.getPorcentajeAnticipo().doubleValue() != this.porcentajeAnticipo)
					this.porcentajeAnticipo = item.getPorcentajeAnticipo().doubleValue();
				if (item.getPorcentajeRetencion().doubleValue() > 0 && item.getPorcentajeRetencion().doubleValue() != this.porcentajeRetencion)
					this.porcentajeRetencion = item.getPorcentajeRetencion().doubleValue();
				if (! this.mapTotalFactura.containsKey(item.getIdFactura()))
					this.mapTotalFactura.put(item.getIdFactura(), item.getTotal().doubleValue());
				saldo = this.ifzFacturas.calcularSaldo(item.getIdFactura(), null, null);
				saldo = saldo.setScale(decimales, BigDecimal.ROUND_HALF_EVEN);
				cobrado = new BigDecimal((item.getFacturaTotal().doubleValue() - saldo.doubleValue()));
				cobrado = cobrado.setScale(decimales, BigDecimal.ROUND_HALF_EVEN);
				item.setFacturaSaldo(saldo);
				item.setFacturaCobrado(cobrado);
			}
			
			// Recuperamos nueva cobranza
			/*cobranza = nuevaCobranza();
			if (cobranza != null && ! cobranza.isEmpty())
				this.listObraCobranza.addAll(cobranza);*/

			// Asignamos los porcentajes previamente recuperados
			for (ObraCobranza item : this.listObraCobranza) {
				item.setPorcentajeAnticipo(new BigDecimal(this.porcentajeAnticipo));
				item.setPorcentajeRetencion(new BigDecimal(this.porcentajeRetencion));
			}
			
			// Ordenamos por fecha mayor a menor
			Collections.sort(this.listObraCobranza, new Comparator<ObraCobranza>() {
				@Override
				public int compare(ObraCobranza o1, ObraCobranza o2) {
					return o2.getFecha().compareTo(o1.getFecha());
				}
			});

			separarCobranza();
			log.info("Cobranza - Proceso Terminado");
		} catch  (Exception e) {
			control("Ocurrio un problema al consultar las Facturas de la Obra seleccionada", e);
    	} finally {
			if (this.isDebug) 
				log.info("Cobranza - Totalizando ... ");
			totalizarCobranza();
    	}
		
		// *** desde el 2018/01/22 se agregan todas las facturas (pagadas, abonadas, sin abono) y se añade una columna para mostrar lo abonado
	}
	
	public void guardar() {
		try {
			control();
			log.info("Cobranza ... Preparando para guardar");
			if (! validaciones())
				return;
			
			log.info("Cobranza ... actualizando listado");
			for (ObraCobranza var : this.listObraCobranza) {
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance().getTime());
				} 

				var.setIdObra(this.pojoObra);
				var.setNombreObra(this.pojoObra.getNombre());
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance().getTime());
			}
			
			// Guardamos la cobranza
			log.info("Cobranza ... guardando");
			this.listObraCobranza = this.ifzCobranza.saveOrUpdateList(this.listObraCobranza);
			log.info("Cobranza guardada!");
			
			/*log.info("Cobranza ... Guardando");
			for (ObraCobranza var : this.listObraCobranza) {
				/ * Se guardar todos los registros encontradas y obtenidos
				if ((var.getAnticipo().doubleValue() <= 0 && var.getEstimacion().doubleValue() <= 0))
					continue;* /
				
				var.setIdObra(this.pojoObra);
				var.setNombreObra(this.pojoObra.getNombre());
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					var.setId(this.ifzCobranza.save(var));
					log.info("Cobranza ... Registro " + var.getId() + " guardando");
				} else {
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					// Actualizamos en la BD
					this.ifzCobranza.update(var);
					log.info("Cobranza ... Registro " + var.getId() + " actualizado");
				}
			}*/
			
			// Borramos los facturas canceladas con previa carga en cobranza si corresponde
			/*if (! this.listObraCobranzaBorrados.isEmpty()) {
				log.info("Cobranza ... Quitando Facturas previamente borradas y/o canceladas");
				for (ObraCobranza var : this.listObraCobranzaBorrados)
					this.ifzCobranza.delete(var.getId());
				this.listObraCobranzaBorrados.clear();
				log.info("Cobranza ... Facturas Canceladas borradas");
			}*/
		} catch(Exception e) {
			control("Ocurrio un problema al intentar guardar la Cobranza", e);
    	} finally {
			log.info("Cobranza ... Terminado");
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
			log.info("Reporte COBRANZA. Preparando ... ");
			this.pojoObra = this.ifzObras.findById(this.idObra);
			if (this.idObra <= 0L) {
				control("Debe seleccionar una Obra");
				return;
			}

			log.info("Imprimiento reporte COBRANZA ... ");
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idObra", this.idObra);

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  "159");
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.usuario);

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO - " + respuesta.getErrores().getDescError());
				control("Ocurrio un problema al intentar imprimir la COBRANZA\nError " + respuesta.getErrores().getCodigoError());
				return;
			} 
			
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "COBRANZA-" + this.idObra + "." + formatoDocumento;
			
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("ERROR INTERNO - No se recupero el contenido de la COBRANZA");
				control("Ocurrio un problema al intentar imprimir la COBRANZA");
				return;
			}
			
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			log.info("Reporte Ordenes de Compra de Exp Insumos lanzado. Proceso terminado!");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la COBRANZA", e);
		} 
	}
	
	public void totalizarCobranza() {
		totalizarCobranzaMXN();
		totalizarCobranzaUSD();
	}
	
	public void totalizarCobranzaMXN() {
		Double monto = 0D;
		double porcentaje = 0;
		double factor = 0;
		// -----------------------------------
		int index = 0;
		
		this.totalCobranza = 0;
		this.sumFacturado = 0;
	    this.sumAnticipo = 0;
	    this.sumEstimacion = 0;
	    this.sumAmortizacion = 0;
	    this.sumGarantia = 0;
	    this.sumSubtotal = 0;
	    this.sumIva = 0;
	    this.sumEstimacionTotal = 0;
	    this.sumCobrado = 0;
	    
		this.permiteGuardarCobranza = true;
		if (this.listObraCobranzaMXN == null) {
			this.listObraCobranzaMXN = new ArrayList<ObraCobranza>();
			this.permiteGuardarCobranza = false;
		}
		
		if (this.isDebug) 
			log.info("Cobranza detalles ... Totalizamos");
		for (ObraCobranza var : this.listObraCobranzaMXN) {
			// Amortizacion
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos amortizacion");
			porcentaje = getMonto(var.getPorcentajeAnticipo());
			factor = porcentaje / 100;
			monto = (var.getEstimacion().doubleValue() * factor);
			//montoAux = getMonto(monto);
			var.setAmortizacion(getMonto(monto)); 
			
			// Fondo Garantia
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos fondo garantia");
			porcentaje = getMonto(var.getPorcentajeRetencion());
			factor = porcentaje / 100;
			monto = (var.getEstimacion().doubleValue() * factor);
			var.setFondoGarantia(getMonto(monto));

			// Subtotal
			if (var.getAnticipo().doubleValue() > 0) {
				if (this.isDebug) log.info("Cobranza detalles ... Obtenemos anticipo y lo asignamos al Subtotal");
				var.setSubtotal(var.getAnticipo()); 
			} else if (var.getEstimacion().doubleValue() > 0) {
				if (this.isDebug) log.info("Cobranza detalles ... Obtenemos estimacion y calculamos subtotal");
				monto = getMonto(var.getEstimacion()) - (getMonto(var.getAmortizacion()) + getMonto(var.getFondoGarantia()));
				if (this.isDebug) log.info("Cobranza detalles ... Asignamos monto (Subtotal)");
				var.setSubtotal(getMonto(monto)); 
			} else {
				if (this.isDebug) log.info("Cobranza detalles ... descartado");
				var.setSubtotal(BigDecimal.ZERO);
				var.setIva(BigDecimal.ZERO);
				var.setTotal(BigDecimal.ZERO);
			    this.sumCobrado += var.getFacturaCobrado().doubleValue();
				continue;
			}

			// IVA y tasa IVA
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos IVA");
			porcentaje = getMonto(var.getPorcentajeIva());
			factor = porcentaje / 100;
			monto = (var.getSubtotal().doubleValue() * factor);
			var.setPorcentajeIva(getMonto(porcentaje));
			var.setIva(getMonto(monto)); 

			// Total
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos total (Subtotal + IVA)");
			monto = var.getSubtotal().doubleValue() + var.getIva().doubleValue();
			//montoAux = getMonto(monto);
			var.setTotal(getMonto(monto));

			// Sumatorias
			if (this.isDebug) log.info("Cobranza detalles ... Sumatorias");
			this.sumFacturado += var.getFacturaTotal().doubleValue();
		    this.sumCobrado += var.getFacturaCobrado().doubleValue();
			this.sumAnticipo += var.getAnticipo().doubleValue(); // CAPTURABLE
		    this.sumEstimacion += var.getEstimacion().doubleValue(); // CAPTURABLE
		    // -------------------------------------------------------------------
			this.sumAmortizacion += var.getAmortizacion().doubleValue();
		    this.sumGarantia += var.getFondoGarantia().doubleValue();
		    this.sumSubtotal += var.getSubtotal().doubleValue();
		    this.sumIva += var.getIva().doubleValue();
		    this.sumEstimacionTotal += var.getTotal().doubleValue();
			this.totalCobranza += var.getTotal().doubleValue();
			
			// Si asignamos valor a Anticipo o Estimacion, validamos si el total corresponde al monto de la factura
			/*
			 * ESTA VALIDACION HA SIDO DESHABILITADA POR AUTORIZACION DE LA ARQ. CAROLINA EL 2018/01/22 ... Movido a ciclo de guardado
			if (this.permiteGuardarCobranza && (var.getAnticipo().doubleValue() > 0 || var.getEstimacion().doubleValue() > 0))
				this.permiteGuardarCobranza = validaCobranza(var.getMonto(), var.getTotal());*/
		}

		if (! this.listObraCobranzaMXN.isEmpty()) {
			for (CobranzaMoneda item : this.listCobranza) {
				if (item.getIdMoneda() == this.listObraCobranzaMXN.get(0).getIdMoneda()) {
					index = this.listCobranza.indexOf(item);
					item.setListCobranza(this.listObraCobranzaMXN);
					this.listCobranza.set(index, item);
					break;
				}
			}
		}
	}
	
	public void totalizarCobranzaUSD() {
		Double monto = 0D;
		double porcentaje = 0;
		double factor = 0;
		// -----------------------------------
		int index = 0;
		
		this.totalCobranzaUSD = 0;
		this.sumFacturadoUSD = 0;
	    this.sumAnticipoUSD = 0;
	    this.sumEstimacionUSD = 0;
	    this.sumAmortizacionUSD = 0;
	    this.sumGarantiaUSD = 0;
	    this.sumSubtotalUSD = 0;
	    this.sumIvaUSD = 0;
	    this.sumEstimacionTotalUSD = 0;
	    this.sumCobradoUSD = 0;
	    
		this.permiteGuardarCobranza = true;
		if (this.listObraCobranzaUSD == null) {
			this.listObraCobranzaUSD = new ArrayList<ObraCobranza>();
			this.permiteGuardarCobranza = false;
		}
		
		if (this.isDebug) log.info("Cobranza detalles ... Totalizamos");
		for (ObraCobranza var : this.listObraCobranzaUSD) {
			// Amortizacion
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos amortizacion");
			porcentaje = getMonto(var.getPorcentajeAnticipo());
			factor = porcentaje / 100;
			monto = (var.getEstimacion().doubleValue() * factor);
			var.setAmortizacion(getMonto(monto)); 
			
			// Fondo Garantia
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos fondo garantia");
			porcentaje = getMonto(var.getPorcentajeRetencion());
			factor = porcentaje / 100;
			monto = ((var.getEstimacion().doubleValue() * porcentaje) / 100);
			var.setFondoGarantia(getMonto(monto));

			// Subtotal
			if (var.getAnticipo().doubleValue() > 0) {
				if (this.isDebug) log.info("Cobranza detalles ... Obtenemos anticipo y lo asignamos al Subtotal");
				var.setSubtotal(var.getAnticipo()); 
			} else if (var.getEstimacion().doubleValue() > 0) {
				if (this.isDebug) log.info("Cobranza detalles ... Obtenemos estimacion y calculamos subtotal");
				monto = getMonto(var.getEstimacion()) - (getMonto(var.getAmortizacion()) + getMonto(var.getFondoGarantia()));
				if (this.isDebug) log.info("Cobranza detalles ... Asignamos monto (Subtotal)");
				var.setSubtotal(getMonto(monto)); 
			} else {
				if (this.isDebug) log.info("Cobranza detalles ... descartado");
				var.setSubtotal(BigDecimal.ZERO);
				var.setIva(BigDecimal.ZERO);
				var.setTotal(BigDecimal.ZERO);
				continue;
			}

			// IVA y tasa IVA
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos IVA");
			porcentaje = getMonto(var.getPorcentajeIva());
			factor = porcentaje / 100;
			monto = (var.getSubtotal().doubleValue() * factor);
			var.setPorcentajeIva(getMonto(porcentaje));
			var.setIva(getMonto(monto)); 

			// Total
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos total (Subtotal + IVA)");
			monto = var.getSubtotal().doubleValue() + var.getIva().doubleValue();
			//montoAux = getMonto(monto);
			var.setTotal(getMonto(monto));

			// Sumatorias
			if (this.isDebug) log.info("Cobranza detalles ... Sumamos total y asignamos");
			this.sumFacturadoUSD += var.getFacturaTotal().doubleValue();
		    this.sumCobradoUSD += var.getFacturaCobrado().doubleValue();
			this.sumAnticipoUSD += var.getAnticipo().doubleValue(); // CAPTURABLE
		    this.sumEstimacionUSD += var.getEstimacion().doubleValue(); // CAPTURABLE
			// ----------------------------------------------------------------------------
			this.sumAmortizacionUSD += var.getAmortizacion().doubleValue();
		    this.sumGarantiaUSD += var.getFondoGarantia().doubleValue();
		    this.sumSubtotalUSD += var.getSubtotal().doubleValue();
		    this.sumIvaUSD += var.getIva().doubleValue();
		    this.sumEstimacionTotalUSD += var.getTotal().doubleValue();
			this.totalCobranzaUSD += var.getTotal().doubleValue();
			
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
					break;
				}
			}
		}
	}
	
	private boolean validaciones() {
		log.info("Cobranza ... unificando");
		unificarCobranza();
		
		if (this.listObraCobranza.size() <= 0) {
			control("Sin detalles para guardar");
			return false;
		}
		
		if (this.totalCobranza <= 0) {
			log.info("Cobranza ... Totalizando MXN");
			totalizarCobranzaMXN();
		}
		
		if (this.totalCobranzaUSD <= 0) {
			log.info("Cobranza ... Totalizando USD");
			totalizarCobranzaUSD();
		}
		
		/*
		 * ESTA VALIDACION HA SIDO DESHABILITADO POR AUTORIZACION DE LA ARQ. CAROLINA EL 2018/01/22
		if (! this.permiteGuardarCobranza) {
			control(6, "Verifique, la columna Monto debe coincidir con la columna Total");
			return false;
		} */
		
		if (this.idObra <= 0L) {
			control("Ocurrio un problema al recuperar la Obra seleccionada");
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unused")
	private List<ObraCobranza> nuevaCobranza() {
		List<ObraCobranza> resultado = new ArrayList<ObraCobranza>();
		ObraCobranza itemCobranza = null;
		List<Factura> listFacturas = null;
		List<FacturaDetalle> lista = null;
		FacturaDetalle pojoDetalle = null;
		BigDecimal facturaSaldo = BigDecimal.ZERO;
		BigDecimal facturaCobrado = BigDecimal.ZERO;
		Moneda moneda = null;
		double monto = 0;
		
		try {
			// Recuperamos facturas
			log.info("Comprobando nueva Cobranza ...");
			this.ifzFacturas.setInfoSesion(this.loginManager.getInfoSesion());
			listFacturas = this.ifzFacturas.findByProperty("idObra", this.pojoObra.getId(), "I", 0, 0);
			if (listFacturas == null || listFacturas.isEmpty()) {
				log.info("Cobranza ... No hay registros de Facturacion para la Obra indicada");
				return resultado;
			}
			
			// Generamos el listado de cobranza
			log.info("Cobranza ... Iteramos listado de facturas ... ");
			for (Factura pojoFactura : listFacturas) {
				if (this.isDebug) 
					log.info("Cobranza ... Comprobando Factura " + pojoFactura.getFolioFactura() + " (" + pojoFactura.getId() + ") ... ");
				
				// Excluimos facturas canceladas
				if (pojoFactura.getEstatus() == FACTURA_ESTATUS.Cancelada.ordinal()) {
					if (this.isDebug) 
						log.info(" -----> descartada (CANCELADA)");
					continue;
				}
				
				// Excluimos facturas canceladas
				/*if (! "I".equals(pojoFactura.getTipoComprobante())) {
					if (this.isDebug) 
						log.info(" -----> descartada (NO ES INGRESO)");
					continue;
				}*/
				
				// Comprobamos si ya existe en Cobranza
				if (this.mapTotalFactura.containsKey(pojoFactura.getId())) {
					if (this.isDebug) 
						log.info(" -----> descartada (EXISTE)");
					continue;
				}
				
				// Recupero el Saldo
				if (this.isDebug) 
					log.info(" -----> Comprobamos saldo ... ");
				facturaSaldo = this.ifzFacturas.calcularSaldo(pojoFactura.getId(), null, null);
				facturaSaldo = facturaSaldo.setScale(this.decimales, BigDecimal.ROUND_HALF_EVEN);
				monto = pojoFactura.getTotal().doubleValue() - facturaSaldo.doubleValue();
				this.mapTotalFactura.put(pojoFactura.getId(), monto);

				// calculo lo Cobrado
				if (this.isDebug) 
					log.info(" -----> Determinando Cobrado ... ");
				facturaCobrado = new BigDecimal(monto);
				facturaCobrado = facturaCobrado.setScale(this.decimales, BigDecimal.ROUND_HALF_EVEN);
				
				// Recuperamos moneda
				moneda = this.ifzMonedas.findById(pojoFactura.getIdMoneda());
				if (moneda == null || pojoFactura.getIdMoneda() == null || pojoFactura.getIdMoneda() <= 0L)
					moneda = this.ifzMonedas.findById(this.loginManager.getInfoSesion().getEmpresa().getMonedaId());

				// Recuperamos el primer concepto de la factura
				if (this.isDebug) 
					log.info(" -----> Recuperando primer concepto ... ");
				this.ifzFacConceptos.setInfoSesion(this.loginManager.getInfoSesion());
				lista = this.ifzFacConceptos.findAll(pojoFactura.getId()); 
				if (lista == null || lista.isEmpty()) {
					if (this.isDebug) 
						log.info(" -----> descartada (Sin conceptos)");
					continue;
				}
				pojoDetalle = lista.get(0);
				
				// Generamos el item para Cobranza
				itemCobranza = new ObraCobranza();
				itemCobranza.setIdObra(this.pojoObra);
				itemCobranza.setNombreObra(this.pojoObra.getNombre());
				itemCobranza.setIdFactura(pojoFactura.getId());
				itemCobranza.setFolio(pojoFactura.getFolioFactura());
				itemCobranza.setFecha(pojoFactura.getFechaEmision());
				itemCobranza.setIdConcepto(pojoDetalle.getId());
				itemCobranza.setConcepto(pojoDetalle.getDescripcionConcepto());
				itemCobranza.setTotal(BigDecimal.ZERO); 
				itemCobranza.setFacturaTotal(pojoFactura.getTotal().setScale(this.decimales, BigDecimal.ROUND_HALF_EVEN)); 
				itemCobranza.setFacturaSaldo(facturaSaldo);
				itemCobranza.setFacturaCobrado(facturaCobrado);
				itemCobranza.setTipoCambio(pojoFactura.getTipoCambio());
				itemCobranza.setIdMoneda(moneda.getId());
				itemCobranza.setMonedaNombre(moneda.getNombre());
				itemCobranza.setMonedaAbreviacion(moneda.getAbreviacion());
				// Asignamos porcentajes de la cobranza previa
				itemCobranza.setPorcentajeIva(getMonto(this.porcentajeIva));
				itemCobranza.setPorcentajeAnticipo(getMonto(this.porcentajeAnticipo));
				itemCobranza.setPorcentajeRetencion(getMonto(this.porcentajeRetencion));
				
				// Agregamos al listado
				if (this.isDebug) 
					log.info(" -----> Añado Factura");
				resultado.add(itemCobranza);
			}
			log.info("Comprobando nueva Cobranza ... " + resultado.size() + " facturas agregadas");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Facturas que no estan en la Cobranza de la Obra seleccionada", e);
		}
		
		return resultado;
	}
	
	private String generaFormato(int numeros, int decimales) {
		String separador = ",";
		String formatResult = "";
		
		for (int i = 0; i < numeros; i++) {
			if (i > 0 && (i % 3) == 0)
				formatResult += separador;
			formatResult += "#";
		}
		
		if (decimales > 0) {
			formatResult += ".";
			for (int i = 0; i < decimales; i++) 
				formatResult += "#";
			formatResult = formatResult.replace("#.##", "0.00");
		}
		
		return formatResult;
	}

	private Double getMonto(BigDecimal value) {
		return value.setScale(this.decimales, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
	private BigDecimal getMonto(Double value) {
		return new BigDecimal(value).setScale(this.decimales, BigDecimal.ROUND_HALF_EVEN);
	}

	private void separarCobranza() {
		this.numPaginaObraCobranzaMXN = 1;
		if (this.listObraCobranzaMXN == null)
			this.listObraCobranzaMXN = new ArrayList<ObraCobranza>();
		this.listObraCobranzaMXN.clear();
		this.numPaginaObraCobranzaUSD = 1;
		if (this.listObraCobranzaUSD == null)
			this.listObraCobranzaUSD = new ArrayList<ObraCobranza>();
		this.listObraCobranzaUSD.clear();
		
		log.info("Separando cobranza por monedas ... ");
		for (ObraCobranza item : this.listObraCobranza) {
			if (item.getIdMoneda() == 10000001L)
				this.listObraCobranzaMXN.add(item);
			else if (item.getIdMoneda() == 10000002L)
				this.listObraCobranzaUSD.add(item);
		}

		this.paginacionCobranza = 1;
		if (this.listCobranza == null)
			this.listCobranza = new ArrayList<CobranzaMoneda>();
		this.listCobranza.clear();
		this.listCobranza.add(new CobranzaMoneda(10000001, "PESOS",   "MXN", this.listObraCobranzaMXN));
		this.listCobranza.add(new CobranzaMoneda(10000002, "DOLARES", "USD", this.listObraCobranzaUSD));
	}
	
	private void unificarCobranza() {
		if (this.listObraCobranzaMXN == null)
			this.listObraCobranzaMXN = new ArrayList<ObraCobranza>();
		for (ObraCobranza itemMXN : this.listObraCobranzaMXN) {
			for (ObraCobranza item : this.listObraCobranza) {
				if (item.getIdFactura().longValue() == itemMXN.getIdFactura().longValue() && item.getIdConcepto().longValue() == itemMXN.getIdConcepto().longValue()) {
					item = itemMXN;
					break;
				}
			}
		}

		if (this.listObraCobranzaUSD == null)
			this.listObraCobranzaUSD = new ArrayList<ObraCobranza>();
		for (ObraCobranza itemMXN : this.listObraCobranzaUSD) {
			for (ObraCobranza item : this.listObraCobranza) {
				if (item.getIdFactura().longValue() == itemMXN.getIdFactura().longValue() && item.getIdConcepto().longValue() == itemMXN.getIdConcepto().longValue()) {
					item = itemMXN;
					break;
				}
			}
		}
	}

	private void control() {
		this.incompleto = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
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
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.incompleto = operacionCancelada;
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
		
		log.error("\n\nGP-COBRANZA :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n", throwable);
	}

	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

	public String getAuditoriaCreado() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L)
			return this.pojoObra.getCreadoPor() + " - " + this.pojoObra.getFechaCreacion();
		return "";
	}
	
	public void setAuditoriaCreado(String value) {}

	public String getAuditoriaModificado() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L)
			return this.pojoObra.getModificadoPor() + " - " + this.pojoObra.getFechaModificacion();
		return "";
	}
	
	public void setAuditoriaModificado(String value) {}
	
	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

	public String getCobranzaTitulo() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L) {
			if (this.pojoObra.getDescripcionMoneda() != null && ! "".equals(this.pojoObra.getDescripcionMoneda().trim()))
				return this.pojoObra.getId() + " - " + this.pojoObra.getNombre() + " - " + this.pojoObra.getDescripcionMoneda();
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
		}
		return "No selecciono OBRA";
	}
	
	public void setCobranzaTitulo(String value) {}

	public double getPorcentajeAnticipo() {
		return this.porcentajeAnticipo;
	}
	
	public void setPorcentajeAnticipo(double value) {
		if (this.porcentajeAnticipo != value) {
			this.porcentajeAnticipo = value;
			unificarCobranza();
			if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())  {
				for (ObraCobranza var : this.listObraCobranza)
					var.setPorcentajeAnticipo(new BigDecimal(value));
			}

			separarCobranza();
			totalizarCobranza();
		}
	}
	
	public double getPorcentajeRetencion() {
		return this.porcentajeRetencion;
	}
	
	public void setPorcentajeRetencion(double value) {
		if (this.porcentajeRetencion != value) {
			this.porcentajeRetencion = value;
			unificarCobranza();
			if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())  {
				for (ObraCobranza var : this.listObraCobranza) 
					var.setPorcentajeRetencion(new BigDecimal(value));
			}

			separarCobranza();
			totalizarCobranza();
		}
	}

	public boolean getOperacion() {
		return incompleto;
	}

	public void setOperacion(boolean incompleto) {
		this.incompleto = incompleto;
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
	
	public List<SelectItem> getBusquedaOpciones() {
		return busquedaOpciones;
	}

	public void setBusquedaOpciones(List<SelectItem> busquedaOpciones) {
		this.busquedaOpciones = busquedaOpciones;
	}

	public String getBusquedaCampo() {
		return busquedaCampo;
	}

	public void setBusquedaCampo(String busquedaCampo) {
		this.busquedaCampo = busquedaCampo;
	}

	public String getBusquedaValor() {
		return busquedaValor;
	}

	public void setBusquedaValor(String busquedaValor) {
		this.busquedaValor = busquedaValor;
	}

	public int getBusquedaTipo() {
		return busquedaTipo;
	}

	public void setBusquedaTipo(int busquedaTipo) {
		this.busquedaTipo = busquedaTipo;
	}
	
	public int getBusquedaPaginas() {
		return busquedaPaginas;
	}

	public void setBusquedaPaginas(int busquedaPaginas) {
		this.busquedaPaginas = busquedaPaginas;
	}

	public List<Obra> getListObras() {
		return listObras;
	}
	
	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}
	
	public long getIdObra() {
		return idObra;
	}
	
	public void setIdObra(long idObra) {
		this.idObra = idObra;
	}
	
	public int getNumPaginaObraCobranzaMXN() {
		return numPaginaObraCobranzaMXN;
	}

	public void setNumPaginaObraCobranzaMXN(int numPaginaObraCobranzaMXN) {
		this.numPaginaObraCobranzaMXN = numPaginaObraCobranzaMXN;
	}

	public int getNumPaginaObraCobranzaUSD() {
		return numPaginaObraCobranzaUSD;
	}

	public void setNumPaginaObraCobranzaUSD(int numPaginaObraCobranzaUSD) {
		this.numPaginaObraCobranzaUSD = numPaginaObraCobranzaUSD;
	}
	
	public double getTotalCobranza() {
		return totalCobranza;
	}

	public void setTotalCobranza(double totalCobranza) {
		this.totalCobranza = totalCobranza;
	}

	public boolean isPermiteGuardarCobranza() {
		return permiteGuardarCobranza;
	}

	public void setPermiteGuardarCobranza(boolean permiteGuardarCobranza) {
		this.permiteGuardarCobranza = permiteGuardarCobranza;
	}
	
	public boolean getDebugging() {
		if (this.paramsRequest.containsKey("DEBUG") && "1".equals(this.paramsRequest.get("DEBUG"))) 
			return true;
		return false;
	}
	
	public void setDebugging(boolean value) { }

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

	public double getSumFacturado() {
		return sumFacturado;
	}

	public void setSumFacturado(double sumFacturado) {
		this.sumFacturado = sumFacturado;
	}

	public double getSumAnticipo() {
		return sumAnticipo;
	}

	public void setSumAnticipo(double sumAnticipo) {
		this.sumAnticipo = sumAnticipo;
	}

	public double getSumEstimacion() {
		return sumEstimacion;
	}

	public void setSumEstimacion(double sumEstimacion) {
		this.sumEstimacion = sumEstimacion;
	}

	public double getSumAmortizacion() {
		return sumAmortizacion;
	}

	public void setSumAmortizacion(double sumAmortizacion) {
		this.sumAmortizacion = sumAmortizacion;
	}

	public double getSumGarantia() {
		return sumGarantia;
	}

	public void setSumGarantia(double sumGarantia) {
		this.sumGarantia = sumGarantia;
	}

	public double getSumSubtotal() {
		return sumSubtotal;
	}

	public void setSumSubtotal(double sumSubtotal) {
		this.sumSubtotal = sumSubtotal;
	}

	public double getSumIva() {
		return sumIva;
	}

	public void setSumIva(double sumIva) {
		this.sumIva = sumIva;
	}

	public double getSumEstimacionTotal() {
		return sumEstimacionTotal;
	}

	public void setSumEstimacionTotal(double sumEstimacionTotal) {
		this.sumEstimacionTotal = sumEstimacionTotal;
	}

	public double getSumCobrado() {
		return sumCobrado;
	}

	public void setSumCobrado(double sumCobrado) {
		this.sumCobrado = sumCobrado;
	}

	public boolean getPerfilAdministrativo() {
		return PERFIL_ADMINISTRATIVO;
	}
	
	public void setPerfilAdministrativo(boolean perfilAdministrativo) {
		PERFIL_ADMINISTRATIVO = perfilAdministrativo;
	}

	public boolean getBusquedaAdministrativas() {
		return busquedaAdministrativas;
	}

	public void setBusquedaAdministrativas(boolean busquedaAdministrativas) {
		this.busquedaAdministrativas = busquedaAdministrativas;
	}
	
	public boolean getEditarGuardado() {
		return (this.isDebug && this.paramsRequest.containsKey("EDIT_SAVED"));
	}
	
	public void setEditarGuardado(boolean value) {}

	public List<ObraCobranza> getListObraCobranzaMXN() {
		return listObraCobranzaMXN;
	}

	public void setListObraCobranzaMXN(List<ObraCobranza> listObraCobranzaMXN) {
		this.listObraCobranzaMXN = listObraCobranzaMXN;
	}

	public List<ObraCobranza> getListObraCobranzaUSD() {
		return listObraCobranzaUSD;
	}

	public void setListObraCobranzaUSD(List<ObraCobranza> listObraCobranzaUSD) {
		this.listObraCobranzaUSD = listObraCobranzaUSD;
	}

	public double getTotalCobranzaUSD() {
		return totalCobranzaUSD;
	}

	public void setTotalCobranzaUSD(double totalCobranzaUSD) {
		this.totalCobranzaUSD = totalCobranzaUSD;
	}

	public double getSumFacturadoUSD() {
		return sumFacturadoUSD;
	}

	public void setSumFacturadoUSD(double sumFacturadoUSD) {
		this.sumFacturadoUSD = sumFacturadoUSD;
	}

	public double getSumAnticipoUSD() {
		return sumAnticipoUSD;
	}

	public void setSumAnticipoUSD(double sumAnticipoUSD) {
		this.sumAnticipoUSD = sumAnticipoUSD;
	}

	public double getSumEstimacionUSD() {
		return sumEstimacionUSD;
	}

	public void setSumEstimacionUSD(double sumEstimacionUSD) {
		this.sumEstimacionUSD = sumEstimacionUSD;
	}

	public double getSumAmortizacionUSD() {
		return sumAmortizacionUSD;
	}

	public void setSumAmortizacionUSD(double sumAmortizacionUSD) {
		this.sumAmortizacionUSD = sumAmortizacionUSD;
	}

	public double getSumGarantiaUSD() {
		return sumGarantiaUSD;
	}

	public void setSumGarantiaUSD(double sumGarantiaUSD) {
		this.sumGarantiaUSD = sumGarantiaUSD;
	}

	public double getSumSubtotalUSD() {
		return sumSubtotalUSD;
	}

	public void setSumSubtotalUSD(double sumSubtotalUSD) {
		this.sumSubtotalUSD = sumSubtotalUSD;
	}

	public double getSumIvaUSD() {
		return sumIvaUSD;
	}

	public void setSumIvaUSD(double sumIvaUSD) {
		this.sumIvaUSD = sumIvaUSD;
	}

	public double getSumEstimacionTotalUSD() {
		return sumEstimacionTotalUSD;
	}

	public void setSumEstimacionTotalUSD(double sumEstimacionTotalUSD) {
		this.sumEstimacionTotalUSD = sumEstimacionTotalUSD;
	}

	public double getSumCobradoUSD() {
		return sumCobradoUSD;
	}

	public void setSumCobradoUSD(double sumCobradoUSD) {
		this.sumCobradoUSD = sumCobradoUSD;
	}

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

	// ----------------------------------------------------------------------
	// PERMISOS
	// ----------------------------------------------------------------------

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
