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

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraCobranza;
import net.giro.adp.logica.ObraCobranzaRem;
import net.giro.adp.logica.ObraRem;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.logica.FacturaDetalleRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.respuesta.Respuesta;

@ViewScoped
@ManagedBean(name="cobranzaAction")
public class CobranzaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CobranzaAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private InitialContext ctx; 
	// Interfaces
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private ReportesRem	ifzReportes;
	private Obra pojoObra;
	private FacturaRem ifzFacturas;
	private FacturaDetalleRem ifzFacConceptos;
	private ObraCobranzaRem ifzCobranza;
	private List<ObraCobranza> listObraCobranza;
	private List<ObraCobranza> listObraCobranzaBorrados;
	private double porcentajeAnticipo;
	private double porcentajeRetencion;
	private int numPaginaObraCobranza;
	private double totalCobranza;
	private boolean permiteGuardarCobranza;
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
    private double sumFacturado;
    private double sumAnticipo;
    private double sumEstimacion;
    private double sumAmortizacion;
    private double sumGarantia;
    private double sumSubtotal;
    private double sumIva;
    private double sumEstimacionTotal;
    private double sumCobrado;
    private HashMap<Long, Double> mapTotalFactura;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
    // PERFILES
	private boolean PERFIL_ADMINISTRATIVO;
	
	
	public CobranzaAction() {
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
			
			// perfiles
			this.porcentajeIva = Double.valueOf(this.loginManager.getAutentificacion().getPerfil("VALOR_IVA"));
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzCobranza = (ObraCobranzaRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraCobranzaFac!net.giro.adp.logica.ObraCobranzaRem");
			this.ifzFacturas = (FacturaRem)	this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacConceptos = (FacturaDetalleRem)	this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			this.ifzReportes 	= (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			
			this.paramsRequest = new HashMap<String, String>();
			params = fc.getExternalContext().getRequestParameterMap();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "ID"));
			this.busquedaCampo = this.busquedaOpciones.get(0).getValue().toString();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
			this.numPaginaObraCobranza = 1;
			this.decimales = 2;
			this.decimalFormat = generaFormato(12, this.decimales);//"###,###,###,##0.0#";
			this.porcentajeFormat = generaFormato(3, this.decimales);//"##0.0#";
		} catch (Exception e) {
			log.error("Error en constructor CobranzaAction", e);
			this.ctx = null;
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
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo);
			if (this.listObras == null || this.listObras.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}

			log.info("Cobranza ... se encontraron " + this.listObras.size() + " obras");
		} catch(Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
    	}
	}
	
	public void ver() {
		List<ObraCobranza> listCobranzaNueva = null;
		
		try {
			control();
			this.porcentajeAnticipo = 0;
			this.porcentajeRetencion = 0;
			this.totalCobranza = 0;
			log.info("Cobranza - Preparando ... ");
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control("Debe seleccionar una Obra");
				return;
			}

			if (this.listObraCobranza == null)
				this.listObraCobranza = new ArrayList<ObraCobranza>();
			this.listObraCobranza.clear();
			this.numPaginaObraCobranza = 1;
			
			if (this.mapTotalFactura == null)
				this.mapTotalFactura = new HashMap<Long, Double>();
			this.mapTotalFactura.clear();
			
			// Recuperamos cobranza existente
			log.info("Obra " + this.pojoObra.getId() + ". Recuperando Cobranza ...");
			listCobranzaNueva = this.ifzCobranza.findByProperty("idObra.id", this.pojoObra.getId(), 0);
			if (this.listObraCobranza == null) 
				this.listObraCobranza = new ArrayList<ObraCobranza>();
			log.info("Cobranza previa recuperada. Facturas x" + this.listObraCobranza.size());
			
			if (this.listObraCobranzaBorrados == null) 
				this.listObraCobranzaBorrados = new ArrayList<ObraCobranza>();
			this.listObraCobranzaBorrados.clear();
			
			for (ObraCobranza item : listCobranzaNueva) {
				log.info("Comprobando Factura " + item.getIdFactura() + " ...");
				if (! comprobarFactura(item.getIdFactura())) {
					log.info("Comprobando Factura " + item.getIdFactura() + " ... Descartada");
					this.listObraCobranzaBorrados.add(item);
					continue;
				}

				log.info("Comprobando Factura " + item.getIdFactura() + " ... OK");
				this.listObraCobranza.add(item);
				
				if (! this.mapTotalFactura.containsKey(item.getIdFactura()))
					this.mapTotalFactura.put(item.getIdFactura(), item.getMonto().doubleValue());
				this.porcentajeAnticipo = item.getPorcentajeAnticipo().doubleValue();
				this.porcentajeRetencion = item.getPorcentajeRetencion().doubleValue();
			}
			
			// Recuperamos nueva cobranza
			if (listCobranzaNueva != null)
				listCobranzaNueva.clear();

			log.info("Comprobando nueva Cobranza ...");
			listCobranzaNueva = nuevaCobranza();
			if (listCobranzaNueva != null && ! listCobranzaNueva.isEmpty())
				this.listObraCobranza.addAll(listCobranzaNueva);
			log.info("Comprobando nueva Cobranza ... " + listCobranzaNueva.size() + " facturas agregadas");
			
			// Ordenamos por fecha mayor a menor
			Collections.sort(this.listObraCobranza, new Comparator<ObraCobranza>() {
				@Override
				public int compare(ObraCobranza o1, ObraCobranza o2) {
					return o2.getFecha().compareTo(o1.getFecha());
				}
			});
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

			log.info("Cobranza ... Guardando");
			for (ObraCobranza var : this.listObraCobranza) {
				/* Se guardar todos los registros encontradas y obtenidos
				if ((var.getAnticipo().doubleValue() <= 0 && var.getEstimacion().doubleValue() <= 0))
					continue;*/
				
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
			}
			
			// Borramos los facturas canceladas con previa carga en cobranza si corresponde
			if (! this.listObraCobranzaBorrados.isEmpty()) {
				log.info("Cobranza ... Borrando Facturas Canceladas con registro en Cobranza");
				for (ObraCobranza var : this.listObraCobranzaBorrados)
					this.ifzCobranza.delete(var.getId());
				this.listObraCobranzaBorrados.clear();
				log.info("Cobranza ... Facturas Canceladas borradas");
			}
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
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control("Debe seleccionar una Obra");
				return;
			}

			log.info("Imprimiento reporte COBRANZA ... ");
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
			params.put("usuario", 		  this.usuario);

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
			log.info("Reporte Ordenes de Compra de Exp Insumos lanzado. Proceso terminado!");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la COBRANZA", e);
		} 
	}
	
	public void totalizarCobranza() {
		BigDecimal montoAux = BigDecimal.ZERO;
		Double monto = 0D;
		double porcentaje = 0;
		
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
		if (this.listObraCobranza == null) {
			this.listObraCobranza = new ArrayList<ObraCobranza>();
			this.permiteGuardarCobranza = false;
		}
		
		if (this.isDebug) 
			log.info("Cobranza detalles ... Totalizamos");
		for (ObraCobranza var : this.listObraCobranza) {
			// Calculamos Amortizacion
			if (this.isDebug) 
				log.info("Cobranza detalles ... Calculamos amortizacion");
			monto = getMonto(var.getEstimacion());
			porcentaje = getMonto(var.getPorcentajeAnticipo());
			monto = ((monto * porcentaje) / 100);
			montoAux = getMonto(monto);
			var.setAmortizacion(montoAux); 
			
			// Calculamos Fondo Garantia
			if (this.isDebug) 
				log.info("Cobranza detalles ... Calculamos fondo garantia");
			monto = getMonto(var.getEstimacion());
			porcentaje = getMonto(var.getPorcentajeRetencion());
			monto = ((monto * porcentaje) / 100);
			montoAux = getMonto(monto);
			var.setFondoGarantia(montoAux);
			
			if (var.getAnticipo().doubleValue() > 0) {
				if (this.isDebug) 
					log.info("Cobranza detalles ... Obtenemos anticipo");
				monto = getMonto(var.getAnticipo());
			} else {
				if (this.isDebug) 
					log.info("Cobranza detalles ... Obtenemos estimacion");
				monto = getMonto(var.getEstimacion()) - (getMonto(var.getAmortizacion()) + getMonto(var.getFondoGarantia()));
			}

			if (this.isDebug) 
				log.info("Cobranza detalles ... Asignamos monto (Subtotal)");
			montoAux = getMonto(monto);
			var.setSubtotal(montoAux); 
			monto = montoAux.doubleValue();
			
			// Caculamos el IVA del subtotal (monto)
			if (this.isDebug) 
				log.info("Cobranza detalles ... Calculamos IVA");
			porcentaje = ((monto * this.porcentajeIva) / 100);
			montoAux = getMonto(porcentaje);
			var.setIva(montoAux); 
			porcentaje = montoAux.doubleValue();

			// Calculamos el total redondeado a 2 decimales
			if (this.isDebug) 
				log.info("Cobranza detalles ... Calculamos total (Subtotal + IVA)");
			monto = monto + porcentaje;
			montoAux = getMonto(monto);
			var.setTotal(montoAux);
			monto = montoAux.doubleValue();

			if (this.isDebug) 
				log.info("Cobranza detalles ... Sumamos total y asignamos");
			this.totalCobranza += monto;
			montoAux = applyFormat(var.getMonto());
			if (this.isDebug) 
				log.info("Cobranza detalles ... Comprobamos si debemos permitir guardar la cobranza (diferencia en total)");
			
			if (this.mapTotalFactura.containsKey(var.getIdFactura()))
				this.sumFacturado += this.mapTotalFactura.get(var.getIdFactura());
		    this.sumAnticipo += var.getAnticipo().doubleValue();
		    this.sumEstimacion += var.getEstimacion().doubleValue();
		    this.sumAmortizacion += var.getAmortizacion().doubleValue();
		    this.sumGarantia += var.getFondoGarantia().doubleValue();
		    this.sumSubtotal += var.getSubtotal().doubleValue();
		    this.sumIva += var.getIva().doubleValue();
		    this.sumEstimacionTotal += var.getTotal().doubleValue();
		    this.sumCobrado += var.getFacturaCobrado().doubleValue();
			
			// Si asignamos valor a Anticipo o Estimacion, validamos si el total corresponde al monto de la factura
			/*
			 * ESTA VALIDACION HA SIDO DESHABILITADA POR AUTORIZACION DE LA ARQ. CAROLINA EL 2018/01/22 ... Movido a ciclo de guardado
			if (this.permiteGuardarCobranza && (var.getAnticipo().doubleValue() > 0 || var.getEstimacion().doubleValue() > 0))
				this.permiteGuardarCobranza = validaCobranza(var.getMonto(), var.getTotal());*/
		}
	}
	
	private boolean validaciones() {
		if (this.listObraCobranza.size() <= 0) {
			control("Sin detalles para guardar");
			return false;
		}
		
		if (this.totalCobranza <= 0) {
			log.info("Cobranza ... Totalizamos");
			totalizarCobranza();
		}
		
		/*
		 * ESTA VALIDACION HA SIDO DESHABILITADO POR AUTORIZACION DE LA ARQ. CAROLINA EL 2018/01/22
		if (! this.permiteGuardarCobranza) {
			control(6, "Verifique, la columna Monto debe coincidir con la columna Total");
			return false;
		} */
		
		if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getNombre() == "") {
			log.info("Cobranza ... Obra NULL en pojo, ID o Nombre");
			control("Hubo un problema con la obra seleccionada.");
			return false;
		}
		
		return true;
	}
	
	private boolean comprobarFactura(Long idFactura) {
		Factura factura = null;
		
		try {
			factura = this.ifzFacturas.findById(idFactura);
			if (factura.getEstatus() == 0)
				return false;
		} catch (Exception e) {
			log.error("Ocurrio un problema al verificar el estatus de la Factura indicada: " + idFactura, e);
		}
		
		return true;
	}
	
	private List<ObraCobranza> nuevaCobranza() {
		List<ObraCobranza> resultado = new ArrayList<ObraCobranza>();
		ObraCobranza itemCobranza = null;
		List<Factura> listFacturas = null;
		List<FacturaDetalle> lista = null;
		FacturaDetalle pojoDetalle = null;
		BigDecimal facturaSaldo = BigDecimal.ZERO;
		BigDecimal facturaCobrado = BigDecimal.ZERO;
		double monto = 0;
		boolean existe = false;
		
		try {
			// Recuperamos facturas
			this.ifzFacturas.orderBy("fechaEmision desc");
			listFacturas = this.ifzFacturas.findByProperty("idObra", this.pojoObra.getId(), 0);
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
				if (pojoFactura.getEstatus() == 0) {
					if (this.isDebug) 
						log.info(" -----> descartada (CANCELADA)");
					continue;
				}
				
				// Comprobamos si ya existe en Cobranza
				existe = false;
				for (ObraCobranza item : this.listObraCobranza) {
					if (item.getIdFactura().longValue() == pojoFactura.getId().longValue()) {
						existe = true;
						break;
					}
				}
				
				// Ya factura ya esta en Cobranza, la ignoramos
				if (existe) {
					if (this.isDebug) 
						log.info(" -----> descartada (EXISTE)");
					continue;
				}
				
				// Recuperamos los detalles de la factura
				if (this.isDebug) 
					log.info(" -----> Recuperando conceptos ... ");
				lista = this.ifzFacConceptos.findByProperty("idFactura", pojoFactura.getId());
				if (lista == null || lista.isEmpty()) {
					if (this.isDebug) 
						log.info(" -----> descartada (Sin conceptos)");
					continue;
				}
				
				// Recuperamos la cobranza si corresponde
				if (this.isDebug) 
					log.info("Cobranza - Comprobando cobranza previa ... ");
				
				// Recupero el Saldo
				if (this.isDebug) 
					log.info(" -----> Comprobamos saldo ... ");
				facturaSaldo = this.ifzFacturas.findSaldoFactura(pojoFactura);
				facturaSaldo = facturaSaldo.setScale(this.decimales, BigDecimal.ROUND_HALF_EVEN);
				
				monto = pojoFactura.getTotal().doubleValue() - facturaSaldo.doubleValue();
				if (! this.mapTotalFactura.containsKey(pojoFactura.getId()))
					this.mapTotalFactura.put(pojoFactura.getId(), monto);

				// calculo lo Cobrado
				if (this.isDebug) 
					log.info(" -----> Determinando Cobrado ... ");
				facturaCobrado = new BigDecimal(monto);
				facturaCobrado = facturaCobrado.setScale(this.decimales, BigDecimal.ROUND_HALF_EVEN);
				
				if (this.isDebug) 
					log.info(" -----> Recupero primer concepto ... ");
				pojoDetalle = lista.get(0);
				
				itemCobranza = new ObraCobranza();
				itemCobranza.setIdObra(this.pojoObra);
				itemCobranza.setNombreObra(this.pojoObra.getNombre());
				itemCobranza.setIdFactura(pojoFactura.getId());
				itemCobranza.setFolio(pojoFactura.getFolioFactura());
				itemCobranza.setFecha(pojoFactura.getFechaEmision());
				itemCobranza.setIdConcepto(pojoDetalle.getId());
				itemCobranza.setConcepto(pojoDetalle.getDescripcionConcepto());
				itemCobranza.setMonto(pojoFactura.getTotal().setScale(this.decimales, BigDecimal.ROUND_HALF_EVEN)); 
				itemCobranza.setFacturaSaldo(facturaSaldo);
				itemCobranza.setFacturaCobrado(facturaCobrado);
				
				// Agregamos al listado
				if (this.isDebug) 
					log.info(" -----> Añado Factura");
				resultado.add(itemCobranza);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Facturas que no estan en la Cobranza de la Obra seleccionada", e);
		}
		
		return resultado;
	}
	
	private String generaFormato(int numeros, int decimales) {
		String separador = ",";
		String formatResult = "";
		
		for (int i = 0; i < numeros; i++) {
			if ((i % 3) == 0)
				formatResult += separador;
			formatResult += "#";
		}
		
		if (decimales > 0) {
			formatResult += ".";
			for (int i = 0; i < decimales; i++) 
				formatResult += "#";
			formatResult = formatResult.replace("#.#", "0.0");
		}
		
		return formatResult;
	}

	private BigDecimal applyFormat(BigDecimal value) {
		return value.setScale(this.decimales, BigDecimal.ROUND_HALF_EVEN);
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
	
	public boolean getScrolling() {
		if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty() && this.listObraCobranza.size() > 5)
			return true;
		return false;
	}
	
	public void setScrolling(boolean value) {}
	
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
		/*if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())
			return this.listObraCobranza.get(0).getPorcentajeAnticipo().doubleValue();
		return 0;*/
		return this.porcentajeAnticipo;
	}
	
	public void setPorcentajeAnticipo(double value) {
		if (this.porcentajeAnticipo != value) {
			this.porcentajeAnticipo = value;
			if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())  {
				for (ObraCobranza var : this.listObraCobranza)
					var.setPorcentajeAnticipo(new BigDecimal(value));
			}
			
			totalizarCobranza();
		}
	}
	
	public double getPorcentajeRetencion() {
		/*if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())
			return this.listObraCobranza.get(0).getPorcentajeRetencion().doubleValue();
		return 0;*/
		return this.porcentajeRetencion;
	}
	
	public void setPorcentajeRetencion(double value) {
		if (this.porcentajeRetencion != value) {
			this.porcentajeRetencion = value;
			if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())  {
				for (ObraCobranza var : this.listObraCobranza) 
					var.setPorcentajeRetencion(new BigDecimal(value));
			}
				
			totalizarCobranza();
		}
	}

	private Double getMonto(BigDecimal value) {
		return value.setScale(this.decimales, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
	private BigDecimal getMonto(Double value) {
		return new BigDecimal(value).setScale(this.decimales, BigDecimal.ROUND_HALF_EVEN);
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
	
	public Obra getPojoObra() {
		return pojoObra;
	}
	
	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}
	
	public List<ObraCobranza> getListObraCobranza() {
		return listObraCobranza;
	}

	public void setListObraCobranza(List<ObraCobranza> listObraCobranza) {
		this.listObraCobranza = listObraCobranza;
	}

	public int getNumPaginaObraCobranza() {
		return numPaginaObraCobranza;
	}

	public void setNumPaginaObraCobranza(int numPaginaObraCobranza) {
		this.numPaginaObraCobranza = numPaginaObraCobranza;
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
}
