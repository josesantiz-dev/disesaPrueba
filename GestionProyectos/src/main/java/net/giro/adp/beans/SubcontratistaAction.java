package net.giro.adp.beans;

import java.io.Serializable;
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
import org.richfaces.event.FileUploadEvent;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.adp.logica.ObraSubcontratistaRem;
import net.giro.cargas.documentos.logica.ComprobanteCFDIRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="subcontratistaAction")
public class SubcontratistaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SubcontratistaAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private ReportesRem	ifzReportes;
	private ObraSubcontratistaRem ifzSubcontratista;
	private List<ObraSubcontratistaExt> listSubcontratistas;
	private ObraSubcontratistaExt pojoSubcontratista;
	private boolean guardarTodo;
	private Obra pojoObraBase;
	private int indexSubcontratista;
	private int indexImpuesto;
	private int paginacionSubcontratistas;
	private int paginacionImpuestos;
	private double porcentajeIva;
	private double porAnticipoGlobal;
	private double porRetencionGlobal;
	private double porAnticipoGlobalPrevio;
	private double porRetencionGlobalPrevio;
    private String decimalFormat;
    private String porcentajeFormat;
    private ConValores impuestoIva;
    // Impuestos
    private ConGrupoValoresRem ifzGrupoValores;
    private ConGrupoValores grupoImpuestos;
    private ConValoresRem ifzImpuestos;
    private List<ConValores> listImpuestos;
    private ConValores pojoImpuesto;
	private List<SelectItem> tiposBusquedaImpuestos;
	private String campoBusquedaImpuestos;
	private String valorBusquedaImpuestos;
	private int paginacionBusquedaImpuestos;
	private boolean incluyeImpuestosContables;
	// Busqueda Obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> opcionesBusquedaObra;
	private String campoBusquedaObra;
	private String valorBusquedaObra;
	private int paginacionObras;
	// Busqueda de Ordenes de Compra
	//private OrdenCompraRem ifzOrdenes;
	//private List<OrdenCompra> listOrdenes;
	//private OrdenCompra pojoOrden;
	private List<SelectItem> opcionesBusquedaOrdenes;
	private String campoBusquedaOrdenes;
	private String valorBusquedaOrdenes;
	private int paginacionOrdenes;
	// Carga Facturas XML
	private ComprobanteCFDIRem ifzComprobante;
	private byte[] fileSrc; 
	private String fileName;
	private String pathCFDI;
	private String prefixCFDI;
	// Variables de control
	private boolean operacion;
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
	
	public SubcontratistaAction() {
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
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// EVALUACION DE PERFILES
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.PERFIL_ADMINISTRATIVO = ((resValue != null && "S".equals(resValue)) ? true : false);
            
			// Porcentaje IVA
			resValue = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
			resValue = (resValue != null && ! "".equals(resValue.trim()) ? resValue.trim() : "1");
			this.porcentajeIva = Double.valueOf(resValue.trim());

            // Obtenemos las propiedades de entorno del modulo
            ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			this.pathCFDI = (this.entornoProperties.containsKey("CFDI_SERVER_PATH") ? this.entornoProperties.getString("CFDI_SERVER_PATH") : "/var/cargas/");
			this.prefixCFDI = (this.entornoProperties.containsKey("CFDI_PREFIX") ? this.entornoProperties.getString("CFDI_PREFIX") : "CXP-GP");
			
			// Interfaces
			ctx = new InitialContext();
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzComprobante = (ComprobanteCFDIRem) ctx.lookup("ejb:/Logica_Cargas_Documentos//ComprobanteCFDIFac!net.giro.cargas.documentos.logica.ComprobanteCFDIRem");
			this.ifzSubcontratista = (ObraSubcontratistaRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraSubcontratistaFac!net.giro.adp.logica.ObraSubcontratistaRem");
			this.ifzGrupoValores = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzImpuestos = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzComprobante.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzSubcontratista.setInfoSesion(this.loginManager.getInfoSesion());

			// IMPUESTOS 
			// ----------------------------------------------------------------------------
			this.grupoImpuestos = this.ifzGrupoValores.findByName("SYS_IMPTOS");
			if (this.grupoImpuestos == null || this.grupoImpuestos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");

			// Busqueda obras
			this.opcionesBusquedaObra = new ArrayList<SelectItem>();
			this.opcionesBusquedaObra.add(new SelectItem("*", "Coincidencia"));
			this.opcionesBusquedaObra.add(new SelectItem("nombre", "Obra"));
			this.opcionesBusquedaObra.add(new SelectItem("nombreCliente", "Cliente"));
			this.opcionesBusquedaObra.add(new SelectItem("id", "ID"));
			nuevaBusquedaObra();

			// Busqueda Ordenes
			this.opcionesBusquedaOrdenes = new ArrayList<SelectItem>();
			this.opcionesBusquedaOrdenes.add(new SelectItem("*", "Coincidencia"));
			this.opcionesBusquedaOrdenes.add(new SelectItem("folio", "Folio"));
			this.opcionesBusquedaOrdenes.add(new SelectItem("nombreObra", "Obra"));
			this.opcionesBusquedaOrdenes.add(new SelectItem("nombreCliente", "Cliente"));
			this.opcionesBusquedaOrdenes.add(new SelectItem("id", "ID"));
			nuevaBusquedaOrdenes();

			// Busqueda Retenciones
			this.tiposBusquedaImpuestos = new ArrayList<SelectItem>();
			this.tiposBusquedaImpuestos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaImpuestos.add(new SelectItem("valor", "Cuenta Contable"));
			this.tiposBusquedaImpuestos.add(new SelectItem("id", "ID"));
			nuevaBusquedaImpuestos();
			
			this.decimalFormat = generaFormato(13, 2, true);
			this.porcentajeFormat = generaFormato(3, 4, true);
			this.paginacionSubcontratistas = 1;
			this.porAnticipoGlobal = 0;
			this.porRetencionGlobal = 0;
			this.porAnticipoGlobalPrevio = 0;
			this.porRetencionGlobalPrevio = 0;
    		this.indexSubcontratista = -1;
		} catch (Exception e) {
			log.error("Error al inicializar BEAN", e);
		} finally {
			establecerPermisos();
		}
	}

	public void nuevo() {
		ObraSubcontratistaExt nuevo = null;
		
		try {
			control();
			nuevo = new ObraSubcontratistaExt();
			nuevo.setIdObra(this.pojoObraBase);
			nuevo.setPorcentajeAnticipo(this.porAnticipoGlobal);
			nuevo.setPorcentajeRetencion(this.porRetencionGlobal);
			nuevo = asignarImpuestoIva(nuevo, this.porcentajeIva, 0);
			this.listSubcontratistas = (this.listSubcontratistas != null ? this.listSubcontratistas : new ArrayList<ObraSubcontratistaExt>());
			this.listSubcontratistas.add(0, nuevo);
			reindexar();
		} catch (Exception e) {
			control("Ocurrio un problema al añadir un nuevo registro", e);
		}
	}
	
	public void ver() {
		List<ObraSubcontratistaExt> backup = null;
		
		try {
			control();
			if (this.pojoObraBase == null || this.pojoObraBase.getId() == null || this.pojoObraBase.getId() <= 0L) {
				control("Debe seleccionar una Obra");
				return;
			}
			
    		controlLog("Validando permiso de escritura");
    		validarPermisos();

			// Recuperamos cobranza existente
			controlLog("Subcontratistas - Preparando ... ");
			this.pojoSubcontratista = null;
			this.indexSubcontratista = -1;
			this.porAnticipoGlobal = 0;
			this.porRetencionGlobal = 0;
			this.porAnticipoGlobalPrevio = 0;
			this.porRetencionGlobalPrevio = 0;
			if (this.listSubcontratistas != null && ! this.listSubcontratistas.isEmpty()) {
				backup = new ArrayList<ObraSubcontratistaExt>();
				for (ObraSubcontratistaExt item : this.listSubcontratistas) {
					if (item.isNuevo())
						backup.add(item);
				}
			}
			
			controlLog("Subcontratistas Recuperando ... Obra: " + this.pojoObraBase.getId());
			this.ifzSubcontratista.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSubcontratistas = this.ifzSubcontratista.findAllExt(this.pojoObraBase.getId(), "");
			this.listSubcontratistas = (this.listSubcontratistas != null ? this.listSubcontratistas : new ArrayList<ObraSubcontratistaExt>());
			if (backup != null && ! backup.isEmpty()) 
				this.listSubcontratistas.addAll(0, backup);
			controlLog("Proceso Terminado. Subcontratistas: x" + this.listSubcontratistas.size());
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Facturas de la Obra seleccionada", e);
    	} finally {
			this.pojoSubcontratista = null;
    		this.indexSubcontratista = -1;
    		reindexar();
			controlLog("Subcontratistas Totalizando ... ");
			totalizar();
    	}
	}
	
	public void guardarTodo() {
		try {
			control();
			controlLog("Subcontratistas ... Preparando para guardar");
			if (! validaciones())
				return;
			for (ObraSubcontratistaExt item : this.listSubcontratistas) {
				if (item.getId() == null || item.getId() <= 0L) {
					item.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					item.setFechaCreacion(Calendar.getInstance().getTime());
				} 
				item.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				item.setFechaModificacion(Calendar.getInstance().getTime());
			}
			
			// Guardamos la cobranza
			controlLog("Subcontratistas ... guardando");
			this.ifzSubcontratista.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSubcontratistas = this.ifzSubcontratista.saveOrUpdateListExt(this.listSubcontratistas);
			ver();
			controlLog("Subcontratistas guardada!");
		} catch(Exception e) {
			control("Ocurrio un problema al intentar guardar los Subcontratistas", e);
    	} 
	}

	public void guardar() {
		try {
    		control();
    		this.listSubcontratistas = (this.listSubcontratistas != null ? this.listSubcontratistas : new ArrayList<ObraSubcontratistaExt>());
    		if (this.indexSubcontratista < 0 || this.indexSubcontratista > this.listSubcontratistas.size()) {
    			control(-1, "Ocurrio un problema al intentar guardar el registro indicado.\nIndice no valido.");
    			return;
    		}
    		
    		this.pojoSubcontratista = this.listSubcontratistas.get(this.indexSubcontratista);
			if (this.pojoSubcontratista.getId() == null || this.pojoSubcontratista.getId() <= 0L) {
				this.pojoSubcontratista.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoSubcontratista.setFechaCreacion(Calendar.getInstance().getTime());
			} 
			this.pojoSubcontratista.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoSubcontratista.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.ifzSubcontratista.setInfoSesion(this.loginManager.getInfoSesion());
			this.pojoSubcontratista = this.ifzSubcontratista.save(this.pojoSubcontratista);
    		this.listSubcontratistas.set(this.indexSubcontratista, this.pojoSubcontratista);
			this.pojoSubcontratista = null;
			this.indexSubcontratista = -1;
    		reindexar();
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar eliminar el registro", e);
    	}
	}
	
	public void desgloseImpuestos() {
		control();
		this.listSubcontratistas = (this.listSubcontratistas != null ? this.listSubcontratistas : new ArrayList<ObraSubcontratistaExt>());
		if (this.indexSubcontratista < 0 || this.indexSubcontratista > this.listSubcontratistas.size()) {
			control(-1, "Ocurrio un problema al intentar desglosar los impuestos del registro indicado.\nIndice no valido.");
			return;
		}
		
		this.pojoSubcontratista = this.listSubcontratistas.get(this.indexSubcontratista);
	}
	
	public void eliminar() {
		try {
    		control();
    		this.listSubcontratistas = (this.listSubcontratistas != null ? this.listSubcontratistas : new ArrayList<ObraSubcontratistaExt>());
    		if (this.indexSubcontratista < 0 || this.indexSubcontratista > this.listSubcontratistas.size()) {
    			control(-1, "No se puede eliminar el registro indicado.\nIndice no valido.");
    			return;
    		}

    		this.pojoSubcontratista = this.listSubcontratistas.get(this.indexSubcontratista);
    		if (this.pojoSubcontratista.getId() != null && this.pojoSubcontratista.getId() > 0L) {
    			this.ifzSubcontratista.setInfoSesion(this.loginManager.getInfoSesion());
    			this.ifzSubcontratista.delete(this.pojoSubcontratista.getId());
    		}
    		this.listSubcontratistas.remove(this.indexSubcontratista);
			this.pojoSubcontratista = null;
    		this.indexSubcontratista = -1;
    		reindexar();
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar eliminar el registro", e);
    	}
	}

	public void eliminarImpuesto() {
		try {
			control();
			if (this.pojoSubcontratista == null || this.pojoSubcontratista.getListImpuestos().isEmpty()) {
				control(-1, "Ocurrio un problema al intentar recuperar el registro indicado o no tiene impuestos asignados");
				return;
			}
			
			if (this.indexImpuesto < 0 || this.indexImpuesto > this.pojoSubcontratista.getListImpuestos().size()) {
				control(-1, "Ocurrio un problema al intentar recuperar el Impuesto indicado");
				return;
			}
			
			this.pojoSubcontratista.delImpuesto(this.indexImpuesto);
			this.listSubcontratistas.set(this.pojoSubcontratista.getIndex(), this.pojoSubcontratista);
			this.indexImpuesto = -1;
		} catch (Exception e) {
			control("Ocurrio un problema al eliminar el Impuesto indicado", e);
		} 
	}
	
	public void reporte() {
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		// ------------------------------------------
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			controlLog("Reporte SUBCONTRATISTAS. Preparando ... ");
			if (this.pojoObraBase == null || this.pojoObraBase.getId() == null || this.pojoObraBase.getId() <= 0L) {
				control("Debe seleccionar una Obra");
				return;
			}

			controlLog("Imprimiento reporte SUBCONTRATISTAS ... ");
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idObra", this.pojoObraBase.getId());

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
				control("Ocurrio un problema al intentar imprimir el reporte de SUBCONTRATISTAS\nError " + respuesta.getErrores().getCodigoError());
				return;
			} 
			
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "SUBCONTRATISTAS-" + this.pojoObraBase.getId() + "." + formatoDocumento;
			
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("ERROR INTERNO - No se recupero el contenido del reporte de SUBCONTRATISTAS");
				control("Ocurrio un problema al intentar imprimir el reporte de SUBCONTRATISTAS");
				return;
			}
			
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			controlLog("Reporte de SUBCONTRATISTAS lanzado: Obra " + this.pojoObraBase.getId() + ". Proceso terminado!");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir el reporte de SUBCONTRATISTAS", e);
		} 
	}

	public void reasignarPorcentajes() {
		for (ObraSubcontratistaExt item : this.listSubcontratistas) {
			if (item.getPorcentajeAnticipo() == this.porAnticipoGlobal || item.getPorcentajeAnticipo() == 0)
				item.setPorcentajeAnticipo(this.porAnticipoGlobal);
			if (item.getPorcentajeRetencion() == this.porRetencionGlobal || item.getPorcentajeRetencion() == 0)
				item.setPorcentajeRetencion(this.porRetencionGlobal);
			item.calcular();
		}
		
		this.porAnticipoGlobalPrevio = this.porAnticipoGlobal;
		this.porRetencionGlobalPrevio = this.porRetencionGlobal;
		totalizar();
	}
	
	public void calculoReversaPorcentajes(AjaxBehaviorEvent event) {
		String value = "";
		int index = -1;

		value = "";
		if (event.getComponent().getAttributes().get("targetMoneda") != null) 
			value = event.getComponent().getAttributes().get("targetMoneda").toString();
		if (value == null || "".equals(value.trim()))
			value = "0";

		value = "";
		if (event.getComponent().getAttributes().get("targetIndex") != null) 
			value = event.getComponent().getAttributes().get("targetIndex").toString();
		if (value == null || "".equals(value.trim()))
			value = "-1";
		index = Integer.valueOf(value);

		if (index >= 0) {
			/*if (10000001L == idMoneda) {
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
			}*/
		}
		
		totalizar();
	}
	
	public void recalcular(AjaxBehaviorEvent event) {
		String value = "";
		int index = -1;
		
		try {
			control();
			value = (event.getComponent().getAttributes().get("targetIndex") != null ? event.getComponent().getAttributes().get("targetIndex").toString() : "");
			value = (value != null && ! "".contains(value.trim()) ? value.trim() : "-1");
			index = Integer.valueOf(value);

			this.pojoSubcontratista = (this.pojoSubcontratista == null || this.pojoSubcontratista.getIndex() != index ? this.listSubcontratistas.get(index) : this.pojoSubcontratista);
			if (this.pojoSubcontratista != null && this.pojoSubcontratista.getModificado()) {
				this.pojoSubcontratista.setIndex(index);
				this.pojoSubcontratista.calcular();
				this.listSubcontratistas.set(this.pojoSubcontratista.getIndex(), this.pojoSubcontratista);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recalcular el registro", e);
		} finally {
			this.pojoSubcontratista = null;
			this.indexSubcontratista = -1;
			this.guardarTodo = false;
			for (ObraSubcontratistaExt item : this.listSubcontratistas) {
				if (item.getModificado()) {
					this.guardarTodo = true;
					break;
				}
			}
		}
	}
	
	public void recalcularMontos() {
		try {
			control();
			if (this.pojoSubcontratista != null) {
				this.pojoSubcontratista.recalcular();
				this.listSubcontratistas.set(this.pojoSubcontratista.getIndex(), this.pojoSubcontratista);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recalcular el registro", e);
		}
	}
	
	public void totalizar() {
		HashMap<Double, Integer> mapAnticipos = new HashMap<Double, Integer>();
		HashMap<Double, Integer> mapRetenciones = new HashMap<Double, Integer>();
		
		this.guardarTodo = false;
		for (ObraSubcontratistaExt item : this.listSubcontratistas) {
			item.setIndex(this.listSubcontratistas.indexOf(item));
			item.calcular();
			mapAnticipos = contadorLlaves(mapAnticipos, item.getPorcentajeAnticipo());
			mapRetenciones = contadorLlaves(mapRetenciones, item.getPorcentajeRetencion());
			if (item.getModificado())
				this.guardarTodo = true;
		}
		
		this.porAnticipoGlobal = maxLlave(mapAnticipos);
		this.porRetencionGlobal = maxLlave(mapRetenciones);
		this.porAnticipoGlobalPrevio = this.porAnticipoGlobal;
		this.porRetencionGlobalPrevio = this.porRetencionGlobal;
	}
	
	private void reindexar() {
		this.guardarTodo = false;
		for (ObraSubcontratistaExt item : this.listSubcontratistas) {
			item.setIndex(this.listSubcontratistas.indexOf(item));
			if (item.getModificado())
				this.guardarTodo = true;
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
	
	private boolean validaciones() {
		controlLog("Cobranza ... validando obra");
		if (this.pojoObraBase == null || this.pojoObraBase.getId() == null || this.pojoObraBase.getId() <= 0L) {
			control("Ocurrio un problema al recuperar la Obra seleccionada");
			return false;
		}
		
		controlLog("Cobranza ... inicializando");
		this.listSubcontratistas = (this.listSubcontratistas != null ? this.listSubcontratistas : new ArrayList<ObraSubcontratistaExt>());
		if (this.listSubcontratistas.size() <= 0) {
			control("Sin detalles para guardar");
			return false;
		}
		
		return true;
	}

	private ObraSubcontratistaExt asignarImpuestoIva(ObraSubcontratistaExt item, double porcentaje, double base) {
		boolean esRetencion = false;
		double tasaImpuesto = 0;
		
		try {
			if (this.impuestoIva == null || this.impuestoIva.getId() <= 0L) 
				this.impuestoIva = this.ifzImpuestos.findById(10001006L);
			
			tasaImpuesto = this.impuestoIva.getAtributo1() != null && ! "".equals(this.impuestoIva.getValor()) ? Double.parseDouble(this.impuestoIva.getValor()) : 0;
			tasaImpuesto = (tasaImpuesto > 0 && tasaImpuesto != porcentaje) ? (porcentaje > 0 ? porcentaje : tasaImpuesto) : tasaImpuesto;
			item.addImpuesto(this.impuestoIva.getId(), this.impuestoIva.getDescripcion(), tasaImpuesto, base, esRetencion, false);
		} catch (Exception e) {
			control(-1, "Ocurrio un problema al asignar Impuesto IVA");
		}
		
		return item;
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
	
	private void control() {
		this.operacion = false;
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
		this.operacion = operacionCancelada;
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
		this.pojoObra = null;
	}

	public void buscarObras() {
		try {
			control();
			this.campoBusquedaObra = (this.campoBusquedaObra != null && ! "".equals(this.campoBusquedaObra.trim()) ? this.campoBusquedaObra.trim() : this.opcionesBusquedaObra.get(0).getValue().toString());
			this.valorBusquedaObra = (this.valorBusquedaObra != null && ! "".equals(this.valorBusquedaObra.trim()) ? this.valorBusquedaObra.trim() : "");

			controlLog("Cobranza ... buscando obras");
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valorBusquedaObra, this.PERFIL_ADMINISTRATIVO, "", 0); 
			if (this.listObras == null || this.listObras.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
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
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "No se pudo recuperar la Obra seleccionada");
				return;
			}
			
			this.pojoObraBase = new Obra();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(this.pojoObraBase, this.pojoObra);
			ver();
			nuevaBusquedaObra();
		} catch(Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		}
	}

	public void quitarObra() {
		try {
			control();
			this.listSubcontratistas = new ArrayList<ObraSubcontratistaExt>();
			this.pojoSubcontratista = null;
			this.indexSubcontratista = -1;
			this.pojoObraBase = null;
			this.porAnticipoGlobal = 0;
			this.porRetencionGlobal = 0;
			this.porAnticipoGlobalPrevio = 0;
			this.porRetencionGlobalPrevio = 0;
		} catch(Exception e) {
			control("Ocurrio un problema al intentar quitar la Obra", e);
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// Busqueda Ordenes de Compra 
	// -------------------------------------------------------------------------------------------------------------------
	
	public void nuevaBusquedaOrdenes() {
		this.campoBusquedaOrdenes = this.opcionesBusquedaOrdenes.get(0).getValue().toString();
		this.valorBusquedaOrdenes = "";
		this.paginacionOrdenes = 1;
		
		//this.listOrdenes = new ArrayList<OrdenCompra>();
		//this.pojoOrden = null;
	}
	
	public void buscarOrdenes() {
		try {
			control();
			this.campoBusquedaOrdenes = (this.campoBusquedaOrdenes != null && ! "".equals(this.campoBusquedaOrdenes.trim()) ? this.campoBusquedaOrdenes.trim() : this.opcionesBusquedaOrdenes.get(0).getValue().toString());
			this.valorBusquedaOrdenes = (this.valorBusquedaOrdenes != null && ! "".equals(this.valorBusquedaOrdenes.trim()) ? this.valorBusquedaOrdenes.trim() : "");

			/*controlLog("Cobranza ... buscando obras");
			this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
			this.listOrdenes = this.ifzOrdenes.findLikeProperty(this.campoBusquedaOrdenes, this.valorBusquedaOrdenes, this.PERFIL_ADMINISTRATIVO, "", 0); 
			if (this.listOrdenes == null || this.listOrdenes.isEmpty()) 
				control(2, "Busqueda sin resultados");*/
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Ordenes", e);
    	} finally {
			this.paginacionOrdenes = 1;
    		//this.listOrdenes = (this.listOrdenes != null ? this.listOrdenes : new ArrayList<OrdenCompra>());
			//controlLog("Subcontratista ... se encontraron " + this.listOrdenes.size() + " Ordenes");
    	}
	}
	
	public void seleccionarOrden() {
		try {
			control();
			/*if (this.pojoOrden == null || this.pojoOrden.getId() == null || this.pojoOrden.getId() <= 0L) {
				control(-1, "No se pudo recuperar la Orden de Compra seleccionada");
				return;
			}
			
			this.pojoOrden = new OrdenCompra();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(this.pojoOrden, this.pojoOrden);
			ver();*/
			nuevaBusquedaOrdenes();
		} catch(Exception e) {
			control("Ocurrio un problema al recuperar la Orden seleccionada", e);
		}
	}
	
	// ------------------------------------------------------------
	// Carga Facturas XML
	// ------------------------------------------------------------

	public void nuevaCarga() {
		this.fileSrc = null;
		this.fileName = "";
	}
	
	public void cargaFactura(FileUploadEvent event) {
		try {
			control();
			this.fileSrc = event.getUploadedFile().getData();
			this.fileName = event.getUploadedFile().getName();
		} catch (Exception e) {
			control("Ocurrio un problema al leer el archivo de Asistencia", e);
		}
	}
	
	public void importarFactura() {
		Respuesta respuesta = null;
		CFDIRow cfdi = null;
		String serverFilename = "";
		
		try {
			control();
			if (this.fileSrc == null)
				return;

			this.fileName = (this.fileName != null && ! "".equals(this.fileName.trim()) ? this.fileName.trim() : "NO-NAME");
			serverFilename = this.pathCFDI + this.prefixCFDI;
			this.ifzSubcontratista.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzSubcontratista.procesarCFDI(this.fileSrc, serverFilename, this.fileName);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}

			cfdi = (CFDIRow) respuesta.getBody().getValor("cfdi");
			if (cfdi == null) {
				control("Ocurrio un problema al recuperar los datos del CFDI indicado");
				return;
			}
			
			this.pojoSubcontratista = this.listSubcontratistas.get(this.indexSubcontratista);
			this.pojoSubcontratista.setIdFactura(cfdi.getIdComprobante());
			this.pojoSubcontratista.setFolioFactura(cfdi.getFactura());
			this.pojoSubcontratista.setIdCliente(cfdi.getIdEmisor());
			this.pojoSubcontratista.setNombreCliente(cfdi.getEmisor());
			this.pojoSubcontratista.setRfcCliente(cfdi.getEmisorRfc());
			this.pojoSubcontratista.setConcepto(cfdi.getConcepto());
			this.pojoSubcontratista.setFacturaTotal(cfdi.getTotal());
			this.pojoSubcontratista.setFacturaTotalPesos(cfdi.getTotalPesos());
			this.listSubcontratistas.set(this.indexSubcontratista, this.pojoSubcontratista);
		} catch (Exception e) {
			control("Ocurrio un problema al procesar el CFDI indicado", e);
		} finally {
			nuevaCarga();
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// Impuestos
	// -------------------------------------------------------------------------------------------------------------------

	public void nuevaBusquedaImpuestos() {
		control();
		this.campoBusquedaImpuestos = this.tiposBusquedaImpuestos.get(0).getValue().toString();
		this.valorBusquedaImpuestos = "";
		this.paginacionBusquedaImpuestos = 1;
		
		this.listImpuestos = new ArrayList<ConValores>();
		this.pojoImpuesto = null;
	}
	
	public void buscarImpuestos() {
		List<ConValores> aux = null;
		String value = "";
		
		try {
			control();
			this.campoBusquedaImpuestos = (this.campoBusquedaImpuestos != null && ! "".equals(this.campoBusquedaImpuestos.trim())) ? this.campoBusquedaImpuestos.trim() : this.tiposBusquedaImpuestos.get(0).getValue().toString();
			this.valorBusquedaImpuestos = (this.valorBusquedaImpuestos != null && ! "".equals(this.valorBusquedaImpuestos.trim())) ? this.valorBusquedaImpuestos.trim() : "";
			
			value = this.valorBusquedaImpuestos;
			value = (value == null) ? "" : value.trim();
			if (value.contains(" "))
				value = value.replace(" ", "%");
	   		this.paginacionBusquedaImpuestos = 1;
			this.ifzImpuestos.setInfoSesion(this.loginManager.getInfoSesion());
			this.listImpuestos = this.ifzImpuestos.findLikeProperty(this.campoBusquedaImpuestos, value, this.grupoImpuestos, "", 0);
			this.listImpuestos = this.listImpuestos != null ? this.listImpuestos : new ArrayList<ConValores>();
			aux = new ArrayList<ConValores>();
			for (ConValores item : this.listImpuestos) {
				if (! this.incluyeImpuestosContables && item.getAtributo5() != null && "S".equals(item.getAtributo5()))
					continue;
				aux.add(item);
			}
			
			this.listImpuestos.clear();
			this.listImpuestos.addAll(aux);
			if (this.listImpuestos == null || this.listImpuestos.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Impuestos", e);
		}
	}
	
	public void seleccionarImpuesto() {
		boolean esRetencion = false;
		double tasaImpuesto = 0;
		double base = 0;
		
		try {
			control();
			if (this.pojoImpuesto == null || this.pojoImpuesto.getId() <= 0L) 
				return;
			if (! this.pojoSubcontratista.getListImpuestos().isEmpty()) {
				for (ObraSubcontratistaImpuestosExt item : this.pojoSubcontratista.getListImpuestos()) {
					if (this.pojoImpuesto.getId() == item.getIdImpuesto()) {
						nuevaBusquedaImpuestos();
						control(-1, "El impuesto seleccionado ya existe");
						return;
					}
				}
			}
			
			esRetencion = this.pojoImpuesto.getTipoCuenta() != null && ! "".equals(this.pojoImpuesto.getTipoCuenta().trim()) && "AC".equals(this.pojoImpuesto.getTipoCuenta().trim());
			tasaImpuesto = this.pojoImpuesto.getAtributo1() != null && ! "".equals(this.pojoImpuesto.getAtributo1()) ? Double.parseDouble(this.pojoImpuesto.getAtributo1()) : 0;
			tasaImpuesto = tasaImpuesto > 0 ? tasaImpuesto : 0;
			this.pojoSubcontratista.addImpuesto(this.pojoImpuesto.getId(), this.pojoImpuesto.getDescripcion(), tasaImpuesto, base, esRetencion, false);
			nuevaBusquedaImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al asignar el Impuesto seleccionado", e);
		} 
	}
	
	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

	public String getObra() {
		if (this.pojoObraBase != null && this.pojoObraBase.getId() != null && this.pojoObraBase.getId() > 0L)
			return this.pojoObraBase.getId() + " - " + this.pojoObraBase.getNombre();
		return "";
	}
	
	public void setObra(String value) {}

	public String getCliente() {
		if (this.pojoObraBase != null && this.pojoObraBase.getId() != null && this.pojoObraBase.getId() > 0L)
			return this.pojoObraBase.getNombreCliente() + " (" + this.pojoObraBase.getRfcCliente() + ")";
		return "";
	}
	
	public void setCliente(String value) {}

	public boolean getGuardarTodo() {
		return this.guardarTodo;
	}

	public void setGuardarTodo(String value) {}

	public List<ObraSubcontratistaExt> getListSubcontratistas() {
		return listSubcontratistas;
	}

	public void setListSubcontratistas(List<ObraSubcontratistaExt> listSubcontratistas) {
		this.listSubcontratistas = listSubcontratistas;
	}

	public int getIndexSubcontratista() {
		return indexSubcontratista;
	}

	public void setIndexSubcontratista(int indexSubcontratista) {
		this.indexSubcontratista = indexSubcontratista;
	}

	public int getIndexImpuesto() {
		return indexImpuesto;
	}

	public void setIndexImpuesto(int indexImpuesto) {
		this.indexImpuesto = indexImpuesto;
	}
	
	public int getPaginacionCobranza() {
		return paginacionSubcontratistas;
	}

	public void setPaginacionCobranza(int paginacionSubcontratistas) {
		this.paginacionSubcontratistas = paginacionSubcontratistas;
	}

	public int getPaginacionImpuestos() {
		return paginacionImpuestos;
	}

	public void setPaginacionImpuestos(int paginacionImpuestos) {
		this.paginacionImpuestos = paginacionImpuestos;
	}

	public double getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(double porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public double getPorAnticipoGlobal() {
		return porAnticipoGlobal;
	}

	public void setPorAnticipoGlobal(double porcentajeAnticipo) {
		if (this.porAnticipoGlobalPrevio <= 0 || this.porAnticipoGlobal != porcentajeAnticipo)
			this.porAnticipoGlobalPrevio = this.porAnticipoGlobal;
		this.porAnticipoGlobal = porcentajeAnticipo;
	}

	public double getPorRetencionGlobal() {
		return porRetencionGlobal;
	}

	public void setPorRetencionGlobal(double porcentajeRetencion) {
		if (this.porRetencionGlobalPrevio <= 0 || this.porRetencionGlobal != porcentajeRetencion)
			this.porRetencionGlobalPrevio = this.porRetencionGlobal;
		this.porRetencionGlobal = porcentajeRetencion;
	}

	public List<Obra> getListObras() {
		return listObras;
	}

	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}

	public Obra getPojoObraBase() {
		return pojoObraBase;
	}

	public void setPojoObraBase(Obra pojoObraBase) {
		this.pojoObraBase = pojoObraBase;
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

	public List<SelectItem> getOpcionesBusquedaOrdenes() {
		return opcionesBusquedaOrdenes;
	}

	public void setOpcionesBusquedaOrdenes(List<SelectItem> opcionesBusquedaOrdenes) {
		this.opcionesBusquedaOrdenes = opcionesBusquedaOrdenes;
	}

	public String getCampoBusquedaOrdenes() {
		return campoBusquedaOrdenes;
	}

	public void setCampoBusquedaOrdenes(String campoBusquedaOrdenes) {
		this.campoBusquedaOrdenes = campoBusquedaOrdenes;
	}

	public String getValorBusquedaOrdenes() {
		return valorBusquedaOrdenes;
	}

	public void setValorBusquedaOrdenes(String valorBusquedaOrdenes) {
		this.valorBusquedaOrdenes = valorBusquedaOrdenes;
	}

	public int getPaginacionOrdenes() {
		return paginacionOrdenes;
	}

	public void setPaginacionOrdenes(int paginacionOrdenes) {
		this.paginacionOrdenes = paginacionOrdenes;
	}

	public boolean isBand() {
		return operacion;
	}

	public void setBand(boolean operacionCancelada) {
		this.operacion = operacionCancelada;
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

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public List<ObraSubcontratistaImpuestosExt> getListImpuestos() {
		this.pojoSubcontratista = (this.pojoSubcontratista != null ? this.pojoSubcontratista : new ObraSubcontratistaExt());
		return this.pojoSubcontratista.getListImpuestos();
	}
	
	public void setListImpuestos(List<ObraSubcontratistaImpuestosExt> values) {}

	public double getTotalImpuestos() {
		this.pojoSubcontratista = (this.pojoSubcontratista != null ? this.pojoSubcontratista : new ObraSubcontratistaExt());
		return this.pojoSubcontratista.getTotalImpuestos();
	}
	
	public void setTotalImpuestos(double value) {}
	
	public List<ConValores> getListBusquedaImpuestos() {
		return listImpuestos;
	}
	
	public void setListBusquedaImpuestos(List<ConValores> listImpuestos) {
		this.listImpuestos = listImpuestos;
	}
	
	public ConValores getPojoImpuesto() {
		return pojoImpuesto;
	}

	public void setPojoImpuesto(ConValores pojoImpuesto) {
		this.pojoImpuesto = pojoImpuesto;
	}

	public List<SelectItem> getTiposBusquedaImpuestos() {
		return tiposBusquedaImpuestos;
	}

	public void setTiposBusquedaImpuestos(List<SelectItem> tiposBusquedaImpuestos) {
		this.tiposBusquedaImpuestos = tiposBusquedaImpuestos;
	}

	public String getCampoBusquedaImpuestos() {
		return campoBusquedaImpuestos;
	}

	public void setCampoBusquedaImpuestos(String campoBusquedaImpuestos) {
		this.campoBusquedaImpuestos = campoBusquedaImpuestos;
	}

	public String getValorBusquedaImpuestos() {
		return valorBusquedaImpuestos;
	}

	public void setValorBusquedaImpuestos(String valorBusquedaImpuestos) {
		this.valorBusquedaImpuestos = valorBusquedaImpuestos;
	}

	public int getPaginacionBusquedaImpuestos() {
		return paginacionBusquedaImpuestos;
	}

	public void setPaginacionBusquedaImpuestos(int paginacionBusquedaImpuestos) {
		this.paginacionBusquedaImpuestos = paginacionBusquedaImpuestos;
	}
	
	// ----------------------------------------------------------------------
	// PERMISOS
	// ----------------------------------------------------------------------
	
	/*public boolean getGuardar() {
		return this.permiteGuardarCobranza;
	}
	
	public void setGuardar(boolean value) {}*/

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
