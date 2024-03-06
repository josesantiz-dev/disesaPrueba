package net.giro.adp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;

import net.giro.adp.beans.ConceptoPresupuesto;
import net.giro.adp.beans.InsumosExt;
import net.giro.adp.beans.Obra;
import net.giro.adp.beans.Presupuesto;
import net.giro.adp.beans.PresupuestoDetalleExt;
import net.giro.adp.beans.PresupuestoRow;
import net.giro.adp.logica.ConceptoPresupuestoRem;
import net.giro.adp.logica.InsumosRem;
import net.giro.adp.logica.ObraRem;
import net.giro.adp.logica.PresupuestoDetalleRem;
import net.giro.adp.logica.PresupuestoRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="pptoAction")
public class PresupuestoAction implements Serializable, Runnable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PresupuestoAction.class);	
	private LoginManager loginManager; 
	private PropertyResourceBundle entornoProperties;
	private HttpSession httpSession;	
	// Interfaces
	private ReportesRem	ifzReportes;
	private EmpleadoRem ifzEmpleados;
	// --------------------------------------------------
	private PresupuestoRem ifzPPT;
	private PresupuestoDetalleRem ifzPPTConceptos;
	private Presupuesto pojoPPT;
	private List<PresupuestoDetalleExt> pptConceptos;
	private boolean cargaInsumosValidada;
	private boolean avisoInsumos;
	private boolean saveToPrint;
    private double porcentajeIva; 
	// Obras
	private ObraRem ifzObras;	
	private List<Obra> listObras;
	private Obra pojoObra;
	// Explosion de Insumos
	private InsumosRem ifzInsumos;
	private InsumosExt explosionInsumos;
	// Concepto PPT
	private ConceptoPresupuestoRem ifzConceptosPPT;
	private List<ConceptoPresupuesto> listConceptosPPT;	
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
	// CARGA
	private PropertyResourceBundle pptProperties;
	private HashMap<String, String> layoutPPT;
	private LinkedHashMap<String, String> listErrores;
	private LinkedHashMap<String, byte[]> listPPTs;
	private List<PresupuestoRow> listPPTrow;
	private List<PresupuestoRow> listPPTfail;
	private List<String> bitacora;
	private String fileName;
	private byte[] fileSrc; 
	private boolean procesando;
	private int maxCargas;
	// PERMISOS
	private boolean permisoAgregar;
	private boolean permisoEditar;
	private boolean permisoBorrar;
	private boolean permisoImprimir;
    // PERFILES
	private boolean PERFIL_ADMINISTRATIVO;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public PresupuestoAction() {
		InitialContext ctx = null; 
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String resValue = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			this.porcentajeIva = Double.valueOf(this.loginManager.getAutentificacion().getPerfil("VALOR_IVA"));

			// EVALUACION DE PERFILES
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.PERFIL_ADMINISTRATIVO = ((resValue != null && "S".equals(resValue)) ? true : false);
			
			// Inicializaciones
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());

			// Interfaces
			ctx = new InitialContext();
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzInsumos = (InsumosRem) ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
			this.ifzPPT = (PresupuestoRem) ctx.lookup("ejb:/Logica_GestionProyectos//PresupuestoFac!net.giro.adp.logica.PresupuestoRem");
			this.ifzPPTConceptos = (PresupuestoDetalleRem) ctx.lookup("ejb:/Logica_GestionProyectos//PresupuestoDetalleFac!net.giro.adp.logica.PresupuestoDetalleRem");
			this.ifzConceptosPPT = (ConceptoPresupuestoRem) ctx.lookup("ejb:/Logica_GestionProyectos//ConceptoPresupuestoFac!net.giro.adp.logica.ConceptoPresupuestoRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzInsumos.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzPPT.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzPPTConceptos.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzConceptosPPT.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion()); 
			
			// Propiedades y carga de layout para empleados
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{ppt}", PropertyResourceBundle.class);
			this.pptProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("*", "Coincidencia"));
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("id", "ID"));
			this.busquedaCampo = this.busquedaOpciones.get(0).getValue().toString();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
			
			this.maxCargas = 1;
			if (this.isDebug && this.paramsRequest.containsKey("MULTI_CARGA"))
				this.maxCargas = 0; 
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
		} finally {
			establecerPermisos();
		}
	}
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = "nombre";
			
			if (this.listObras != null)
				this.listObras.clear();

			this.busquedaTipo = (this.busquedaAdministrativas ? 4 : 0);
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo, false, "", 0);
			if (this.listObras == null || this.listObras.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch(Exception e) {
			control("Ocurrio un problema al intentar realizar la busqueda solicitada", e);
    	}
	}
	
	public void ver() {
		double montoInsumo = 0;
		double montoPPT01 = 0;
		String stepTrace = "";
		
		try {
			control();
			inicializar();
			this.saveToPrint = false;
			this.avisoInsumos = false;
			stepTrace += "|comprobando-obra";
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "ERROR: Debe seleccionar una Obra");
				return;
			}
			
    		controlLog("Validando permiso de escritura");
    		validarPermisos();

			//Primero, se debería revisar si ya existe un presupuesto cargado para esta obra: Se hace obvio que hay obra, puesto que solo cuando se edita es posible acceder a la forma
			stepTrace += "|comprobando-presupuesto";
			controlLog("Ver PPT-01 para " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
			this.ifzPPT.setInfoSesion(this.loginManager.getInfoSesion());
			this.pojoPPT = this.ifzPPT.findActual(this.pojoObra.getId());
			if (this.pojoPPT != null && this.pojoPPT.getId() != null && this.pojoPPT.getId() > 0L) {
    			stepTrace += "|obteniendo-detalles-presupuesto-" + this.pojoPPT.getId();
        		this.pptConceptos = this.ifzPPTConceptos.findAllExt(this.pojoPPT.getId()); 
    			stepTrace += "|comprobando-detalles-x" + this.pptConceptos.size();
        		if (this.pptConceptos.size() == 16) {
            		montoPorcentajeCapturables();
            		
        			// A14 = A5 + A13
            		if (this.pptConceptos.get(13).getMonto() == null || this.pptConceptos.get(13).getMonto().doubleValue() <= 0)
            			this.pptConceptos.get(13).setMonto(this.pptConceptos.get(4).getMonto().add(this.pptConceptos.get(12).getMonto()));
            		
            		// porcentaje de IVA: Tomado por perfil
            		if (this.pptConceptos.get(14).getMonto().doubleValue() <= 0)
            			this.pptConceptos.get(14).setPorcentaje(this.porcentajeIva);
            		
        			this.cargaInsumosValidada = true;
        			stepTrace += "|carga-presupuesto-terminada|recuperando-explosion";
        			
        			if (obtenerExplosionInsumos()) {
            			stepTrace += "|explosion-recuperada-comprobando-valores";
        				montoInsumo = this.explosionInsumos.getTotal().setScale(0, BigDecimal.ROUND_FLOOR).doubleValue();
        		    	montoPPT01 = this.pptConceptos.get(6).getMonto().setScale(0, BigDecimal.ROUND_FLOOR).doubleValue(); // 07 - TOTAL COSTO DIRECTO 1 (SIN IVA)
        		    	
        		    	if (montoPPT01 != montoInsumo) {
                			stepTrace += "|explosion-modificada-avisar";
                			this.avisoInsumos = true;
                			//montoPPT01 = this.explosionInsumos.getMontoHerramientas().doubleValue() + this.explosionInsumos.getMontoEquipos().doubleValue();
        		    		this.pptConceptos.get(0).setMonto(this.explosionInsumos.getMontoMateriales());
        		    		this.pptConceptos.get(2).setMonto(this.explosionInsumos.getMontoManoDeObra());
        		    		this.pptConceptos.get(3).setMonto(this.explosionInsumos.getMontoHerramientas().add(this.explosionInsumos.getMontoEquipos()));
        		    		this.pptConceptos.get(4).setMonto(BigDecimal.ZERO);
        		    		this.pptConceptos.get(5).setMonto(BigDecimal.ZERO);
        		    		calcularMontos();
        					this.saveToPrint = true;
        		    		control(false, -1, "Se detecto un cambio en la Explosion de Insumos\nVerifique que los montos en este PPT esten correctos", null);
        		    	}

            			stepTrace += "|comprobacion-explosion-terminada";
        				return;
        			}
        		}
    		} 
    		
    		//Si se llegó a este punto, quiere decir que se cargará un nuevo presupuesto
			stepTrace += "|generando-nuevo-presupuesto";
			if (this.pojoPPT == null)
				this.pojoPPT = new Presupuesto();
			
			if (! obtenerExplosionInsumos())
				return;
    		
    		if (this.explosionInsumos != null)
				cargarConceptosPresupuesto();
		} catch(Exception e) {
    		log.error("Error en GestionProyectos.PresupuestoAction.ver().\n\nTRACE " + stepTrace + "\n\n", e);
			control(true, -1, e.getMessage(), e);
    	} finally {
			controlLog("Proceso Terminado!.\n\nTRACE\n" + stepTrace + "\n\n");
    	}
	}
	
	public void guardar() {
		String stepTrace = "";
		
		try {
			control();
			controlLog("Guardar PPT-01 para " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
			stepTrace += "|validando";
			if (! validarCamposCalculo()) {
				control(-1, this.mensaje);
				return;
			}

			stepTrace += "|asignando";
			this.pojoPPT.setMontoTotal(this.calcularMontoTotal());
    		this.pojoPPT.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoPPT.setFechaModificacion(Calendar.getInstance().getTime());
			
			//Guardar; se compara con cero porque es un objeto de tipo primitivo
    		if (this.pojoPPT.getId() == null || this.pojoPPT.getId() <= 0L) {
    			this.pojoPPT.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
    			this.pojoPPT.setFechaCreacion(Calendar.getInstance().getTime());
    			this.pojoPPT.setIdObra(this.ifzObras.findById(this.pojoObra.getId()));
    			//this.pojoPresupuesto.setIdObra(this.pojoObra.getId());
    			
    			// Guardamos en la BD
    			controlLog("Guardando en BD ... ");
    			stepTrace += "|guardando";
    			this.pojoPPT.setId(this.ifzPPT.save(this.pojoPPT));
    			stepTrace += "-OK-" + this.pojoPPT.getId();
    			controlLog("Guardado en BD. Guardando detalles ... ");
    		} else {
    			// Actualizamos en la BD
    			controlLog("Actualizando en BD ... ");
    			stepTrace += "|actualizando-" + this.pojoPPT.getId();
    			this.ifzPPT.update(this.pojoPPT);
    			stepTrace += "-OK-" + this.pojoPPT.getId();
    			controlLog("Actualizado en BD. Actualizando detalles ... ");
    		}

			// Guardamos los detalles
			if (this.pptConceptos != null && ! this.pptConceptos.isEmpty()) {
    			stepTrace += "|guardando-detalles-x" + this.pptConceptos.size();
    			for (PresupuestoDetalleExt var : this.pptConceptos) {
	    			stepTrace += "\n|guardando-detalle-" + this.pptConceptos.indexOf(var);
    				var.setIdPresupuesto(this.pojoPPT.getId());
        			var.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
        			var.setFechaCreacion(Calendar.getInstance().getTime());
        			var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
        			var.setFechaModificacion(Calendar.getInstance().getTime());
        			var.setMonto(var.getMonto().setScale(2, BigDecimal.ROUND_CEILING)); // redondeamos a 2 digitos
        			var.setPorcentaje(new BigDecimal(var.getPorcentaje()).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
        			var.setPorcentajeTotales(new BigDecimal(var.getPorcentajeTotales()).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
        			
        			// Guardamos en la BD
        			if (var.getId() == null || var.getId() <= 0L)
        				var.setId(this.ifzPPTConceptos.save(var));
        			else
        				this.ifzPPTConceptos.update(var);
	    			stepTrace += "-OK-" + var.getId();
        		}
			}
		} catch(Exception e) {
    		log.error("Error en GestionProyectos.PresupuestoAction.guardar", e);
			control(true, 1, e.getMessage(), e);
    	} finally {
			controlLog("Guardando PPT-01 Terminado!.\n\nTRACE\n" + stepTrace + "\n\n");
    	}
	}

    public void reporte() {
    	Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		String nombreDocumento = "";
		
		try {
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al imprimir PPT01.\nObra no encontrada");
				return;
			}
			
			if (this.saveToPrint) {
				this.recalcularMontos();
				this.guardar();
				this.saveToPrint = false;
				if (this.incompleto)
					return;
			}
			
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idObra", this.pojoObra.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("REPORTE_PPT01"));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al imprimir el PPT01.\n" + respuesta.getErrores().getDescError());
				return;
			}

			nombreDocumento = "PPT01-O-" + this.pojoObra.getId() + "." + respuesta.getBody().getValor("formatoReporte");
			this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
			this.httpSession.setAttribute("formato", respuesta.getBody().getValor("formatoReporte")); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
		} catch (Exception e) {
			control("Ocurrio un problema al imprimir el PPT01", e);
		}
	}

	private boolean obtenerExplosionInsumos() {
		try {
			this.cargaInsumosValidada = false;
			this.explosionInsumos = this.ifzInsumos.findExtActual(this.pojoObra.getId());
			if (this.explosionInsumos == null || this.explosionInsumos.getId() == null || this.explosionInsumos.getId() <= 0L) {
				control(-1, "La Obra indicada no tiene Explosion de Insumos activa");
				return false;
			}

			this.cargaInsumosValidada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar la Explosion de Insumos de la Obra seleccionada", e);
			return false;
		}

		return true;
	}

	private void inicializar() {
		this.explosionInsumos = null;
		this.pojoPPT = new Presupuesto();
		this.pptConceptos = new ArrayList<PresupuestoDetalleExt>();
		this.listConceptosPPT = new ArrayList<ConceptoPresupuesto>();
	}

 	private void control() { 
		this.incompleto = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(int tipo, String mensaje) {
		control(true, tipo, mensaje, null);
	}

	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		this.incompleto = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n\n" + this.getClass().getCanonicalName() + " :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "\n" + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}
	
	private void controlLog(String mensaje) {
		log.info("\n\n" + this.getClass().getCanonicalName() + " :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + " :: " + mensaje);
	}

	// -------------------------------------------------------------------------------------------------------------------
	// CONCEPTOS
	// -------------------------------------------------------------------------------------------------------------------

	private void cargarConceptosPresupuesto() {
		PresupuestoDetalleExt detPPTO = null;

		this.listConceptosPPT = new ArrayList<ConceptoPresupuesto>();
    	this.pptConceptos = new ArrayList<PresupuestoDetalleExt>();
		this.ifzConceptosPPT.setInfoSesion(this.loginManager.getInfoSesion());
    	this.listConceptosPPT = this.ifzConceptosPPT.findAllActivos();
    	if (this.listConceptosPPT == null || this.listConceptosPPT.isEmpty())
    		return;
    	
    	for (ConceptoPresupuesto concepto : this.listConceptosPPT) {
    		detPPTO = new PresupuestoDetalleExt();
    		if (this.pojoPPT.getId() != null && this.pojoPPT.getId() > 0L)
    			detPPTO.setIdPresupuesto(this.pojoPPT.getId());
    		detPPTO.setConceptoPresupuesto(concepto);
    		detPPTO.setMonto(BigDecimal.ZERO);
    		detPPTO.setFormula(concepto.getFormula());
    		this.pptConceptos.add(detPPTO);
    	}
    	
    	if (this.pptConceptos.get(14).getPorcentaje() <= 0)
			this.pptConceptos.get(14).setPorcentaje(this.porcentajeIva);
    	
    	montoPorcentajeCapturables();
    	
    	//Mostramos los primeros 4 valores, que ya sabemos como se calculan
    	int indice = 0;
    	// ------------------------------------------------------------------------ A2
		BigDecimal A2 = this.explosionInsumos.getMontoMateriales();
		
		//Materiales Explosión de Insumos (sin IVA) = insumosPresupuesto.totalMateriales.
    	indice = 1;
    	this.pptConceptos.get(indice-1).setMonto(A2);
    	
    	//Costo de Material (sin IVA y sin Oculto) = insumosPresupuesto.totalMateriales.
    	indice = 2;
    	this.pptConceptos.get(indice-1).setMonto(A2);

    	// ------------------------------------------------------------------------ A3
    	//Costo de Mano de Obra (sin IVA) = insumosPresupuesto.totalManoObra.
    	BigDecimal A3 = this.explosionInsumos.getMontoManoDeObra();
    	indice = 3;
    	this.pptConceptos.get(indice-1).setMonto(A3);

    	// ------------------------------------------------------------------------ A4
    	//Costo Herramienta y equipo (sin IVA) = insumosPresupuesto.totalHerramientas + insumosPresupuesto.totalEquipos
    	BigDecimal A4 = this.explosionInsumos.getMontoHerramientas().add(this.explosionInsumos.getMontoEquipos());
    	indice = 4;
    	this.pptConceptos.get(indice-1).setMonto(A4);
    	
    	// ------------------------------------------------------------------------ A5
    	//A5 --> A2 + A3 + A4 :: totalMateriales + costoManoObra + totalHerramientas;
    	indice = 5;
    	BigDecimal A5 = BigDecimal.ZERO;
    	A5 = A5.add(A2);
    	A5 = A5.add(A3);
    	A5 = A5.add(A4);
    	this.pptConceptos.get(indice-1).setMonto(A5);
    	
    	// ------------------------------------------------------------------------ A6
    	//A6 --> A1 - A2 :: OCULTO: Materiales Explosión de Insumos - Costo de Material --- por ahora sera 0 (cero)
    	indice = 6;
    	BigDecimal A6 = BigDecimal.ZERO;
    	this.pptConceptos.get(indice-1).setMonto(A6);
    	
    	calcularMontos();
    }

	public void montoPorcentajeCapturables() {
    	/* CAPTURABLES
    	 * -----------------------------------------------------------------
    	 * MONTOS 				: A14
    	 * PORCENTAJES 			: A8, A9, A10, A11, A12, A15
    	 * PORCENTAJES TOTALES  : A1, A7, A15, A16
    	 */

    	int index = -1;
    	for(PresupuestoDetalleExt item : this.pptConceptos) {
    		index = this.pptConceptos.indexOf(item);
    		
    		item.setMontoCapturable(false);
    		item.setPorcentajeCapturable(false);
    		item.setPorcentajeTotalesCapturable(true);
    		
    		if (index >= 0) {
            	if (index == 1 || index == 13)
            		item.setMontoCapturable(true);
            	
            	if (index == 7 || index == 8 || index == 9 || index == 10 || index == 11 || index == 14)
            		item.setPorcentajeCapturable(true);
            	
            	if (index == 0 || index == 6 || index == 14 || index == 15)
            		item.setPorcentajeTotalesCapturable(false);
    		}
    	}
    }

    public void recalcularMontos() {
    	if (! validarCamposCalculo()) 
    		return;
    	calcularMontos();
    }

	private boolean validarCamposCalculo() {
    	int indice = 0;
    	
    	// Verificar que se cuente con los campos requeridos: 
		// Oculto, Indirectos de Campo, Indirectos de Oficina Central, Cargos Adicionales,  Financiamiento, Utilidad
    	this.mensaje = "";
    	this.tipoMensaje = -1;
    	this.cargaInsumosValidada = false; // Se ocupará esta variable para no consumir mas
    	
		controlLog("Validando campos");

    	indice = 5;
    	if (this.pptConceptos.get(indice - 1).getMonto() == null || this.pptConceptos.get(indice - 1).getMonto().doubleValue() == 0) {
    		this.mensaje = "Indique cantidad para concepto: " + this.pptConceptos.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		controlLog(this.mensaje);
    		return false;
    	}
    	
    	indice = 7;
    	if (this.pptConceptos.get(indice - 1).getMonto().doubleValue() == 0) {
    		this.mensaje = "Indique cantidad para concepto: " + this.pptConceptos.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		controlLog(this.mensaje);
    		return false;
    	}
    	
    	//8,9,10,11,12, 15
    	indice = 8;
    	if (this.pptConceptos.get(indice - 1).getMonto().doubleValue() > 0 && this.pptConceptos.get(indice - 1).getPorcentaje() == 0) {
    		this.mensaje = "Indique porcentaje para concepto: " + this.pptConceptos.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		controlLog(this.mensaje);
    		return false;
    	}
    	
    	indice = 9;
    	if (this.pptConceptos.get(indice - 1).getMonto().doubleValue() > 0 && this.pptConceptos.get(indice - 1).getPorcentaje() == 0) {
    		this.mensaje = "Indique porcentaje para concepto: " + this.pptConceptos.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		controlLog(this.mensaje);
    		return false;
    	}

    	indice = 10;
    	if (this.pptConceptos.get(indice - 1).getMonto().doubleValue() > 0 && this.pptConceptos.get(indice - 1).getPorcentaje() == 0) {
    		this.mensaje = "Indique porcentaje para concepto: " + this.pptConceptos.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		controlLog(this.mensaje);
    		return false;
    	}

    	indice = 11;
    	if (this.pptConceptos.get(indice - 1).getMonto().doubleValue() > 0 && this.pptConceptos.get(indice - 1).getPorcentaje() == 0) {
    		this.mensaje = "Indique porcentaje para concepto: " + this.pptConceptos.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		controlLog(this.mensaje);
    		return false;
    	}

    	indice = 12;
    	if (this.pptConceptos.get(indice - 1).getMonto().doubleValue() > 0 && this.pptConceptos.get(indice - 1).getPorcentaje() == 0) {
    		this.mensaje = "Indique porcentaje para concepto: " + this.pptConceptos.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		controlLog(this.mensaje);
    		return false;
    	}
    	
    	indice = 14;
		if (this.pptConceptos.get(indice - 1).getMonto().doubleValue() <= 0) {
    		this.mensaje = "Indique cantidad para concepto: " + this.pptConceptos.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		controlLog(this.mensaje);
    		return false;
		}
    	
    	indice = 15;
    	if (this.pptConceptos.get(indice - 1).getMonto().doubleValue() > 0 && this.pptConceptos.get(indice - 1).getPorcentaje() == 0) {
    		this.mensaje = "Indique porcentaje para concepto: IVA";
    		controlLog(this.mensaje);
    		return false;
    	}

		controlLog("Validacion terminada. Guardando ... ");
    	this.cargaInsumosValidada = true;	//Se ocupará esta variable para no consumir mas
    	this.mensaje = "";
    	this.tipoMensaje = 0;
    	
    	return true;
    }

	public void calcularMontos() {
    	BigDecimal porcentaje = BigDecimal.ZERO;
    	BigDecimal A1  = BigDecimal.ZERO;
    	BigDecimal A2  = BigDecimal.ZERO;
    	BigDecimal A3  = BigDecimal.ZERO;
    	BigDecimal A4  = BigDecimal.ZERO;
    	BigDecimal A5  = BigDecimal.ZERO;
    	BigDecimal A6  = BigDecimal.ZERO;
    	BigDecimal A7  = BigDecimal.ZERO;
    	BigDecimal A8  = BigDecimal.ZERO;
    	BigDecimal A9  = BigDecimal.ZERO;
    	BigDecimal A10 = BigDecimal.ZERO;
    	BigDecimal A11 = BigDecimal.ZERO;
    	BigDecimal A12 = BigDecimal.ZERO;
    	BigDecimal A13 = BigDecimal.ZERO;
    	BigDecimal A14 = BigDecimal.ZERO;
    	BigDecimal A15 = BigDecimal.ZERO;
    	BigDecimal A16 = BigDecimal.ZERO;
    	String stepTrace = "";
    	int indice = 0;
    	
    	try {
    		if (this.pptConceptos.get(14).getPorcentaje() <= 0)
    			this.pptConceptos.get(14).setPorcentaje(this.porcentajeIva);

        	stepTrace += "|calcularMontos";
        	// ------------------------------------------------------------------------ A1
    		// MATERIALES EXPLOSION DE INSUMOS (CON OCULTO Y SIN IVA) --> insumosPresupuesto.totalMateriales
        	indice = 1;
    		A1 = this.pptConceptos.get(indice - 1).getMonto();
        	A1 = A1.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	stepTrace += "|calculo-A1-" + A1.toString();

        	// ------------------------------------------------------------------------ A2
    		// COSTO DE MATERIAL (SIN IVA Y SIN OCULTO) --> insumosPresupuesto.totalMateriales :: capturable
        	indice = 2;
        	A2 = this.pptConceptos.get(indice - 1).getMonto();
        	A2 = A2.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	if (A2.doubleValue() <= 0) {
        		A2 = this.explosionInsumos.getMontoMateriales();
            	A2 = A2.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            	this.pptConceptos.get(indice - 1).setMonto(A2);
        	}
        	stepTrace += "|calculo-A2-" + A2.toString();

        	// ------------------------------------------------------------------------ A3
        	// COSTO DE MANO DE OBRA (SIN IVA) --> insumosPresupuesto.totalManoObra
        	indice = 3;
        	A3 = this.pptConceptos.get(indice - 1).getMonto();
        	A3 = A3.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	stepTrace += "|calculo-A3-" + A3.toString();

        	// ------------------------------------------------------------------------ A4
        	// COSTO HERRAMIENTA Y EQUIPO (SIN IVA) --> insumosPresupuesto.totalHerramientas + insumosPresupuesto.totalEquipos
        	indice = 4;
        	A4 = this.pptConceptos.get(indice - 1).getMonto(); // this.explosionInsumos.getMontoHerramientas().add(this.explosionInsumos.getMontoEquipos());
        	A4 = A4.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	stepTrace += "|calculo-A4-" + A4.toString();

        	// ------------------------------------------------------------------------ A5
        	// SUBTOTAL COSTO DIRECTO 1 (SIN IVA Y SIN OCULTO) --> = A2 + A3 + A4
        	indice = 5;
        	A5 = this.pptConceptos.get(indice - 1).getMonto();
        	A5 = A5.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	if (A5.doubleValue() <= 0) {
        		A5 = A5.add(A2);
        		A5 = A5.add(A3);
        		A5 = A5.add(A4);
            	A5 = A5.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        		this.pptConceptos.get(indice - 1).setMonto(A5);
        	}
        	stepTrace += "|calculo-A5-" + A5.toString();
        	
        	// ------------------------------------------------------------------------ A6
        	// OCULTO --> = A1-A2
        	indice = 6;
        	A6 = this.pptConceptos.get(indice - 1).getMonto();
        	A6 = A6.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	if (A6.doubleValue() <= 0) {
        		A6 = new BigDecimal(Math.abs(A1.subtract(A2).doubleValue()));
            	A6 = A6.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        		this.pptConceptos.get(indice - 1).setMonto(A6);
        	}
        	stepTrace += "|calculo-A6-" + A6.toString();

        	// ------------------------------------------------------------------------ A7
        	// TOTAL COSTO DIRECTO 1 (SIN IVA) --> =A5+A6
        	indice = 7;
        	A7 = A7.add(A5);
        	A7 = A7.add(A6);
        	A7 = A7.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.pptConceptos.get(indice - 1).setMonto(A7);
        	stepTrace += "|calculo-A7-" + A7.toString();
        	
        	// ------------------------------------------------------------------------ A8
        	// INDIRECTOS DE CAMPO --> =A7 * %INDIRECTOS DE CAMPO
        	indice = 8;
        	if (this.pptConceptos.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.pptConceptos.get(indice - 1).getPorcentaje() * 0.01);
        		A8 = A7.multiply(porcentaje);
        	}
        	A8 = A8.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.pptConceptos.get(indice - 1).setMonto(A8);
        	stepTrace += "|calculo-A8-" + A8.toString();
        	
        	// ------------------------------------------------------------------------ A9
        	// INDIRECTOS DE OFICINA CENTRAL --> =A7 * %INDIRECTOS DE OFICINA CENTRAL 
        	indice = 9;
        	if (this.pptConceptos.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.pptConceptos.get(indice - 1).getPorcentaje() * 0.01);
        		A9 = A7.multiply(porcentaje);
        	}
        	A9 = A9.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.pptConceptos.get(indice - 1).setMonto(A9);
        	stepTrace += "|calculo-A9-" + A9.toString();
        	
        	//------------------------------------------------------------------------ A10
        	//CARGOS ADICIONALES --> =A5 * % CARGOS ADICIONALES
        	indice = 10;
        	if (this.pptConceptos.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.pptConceptos.get(indice - 1).getPorcentaje() * 0.01);
            	A10 = A5.multiply(porcentaje);
        	}
        	A10 = A10.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.pptConceptos.get(indice - 1).setMonto(A10);
        	stepTrace += "|calculo-A10-" + A10.toString();
        	
        	//------------------------------------------------------------------------ A11
        	//A11 -->	financiamiento = (A5 + A8 + A9) * %
        	indice = 11;
        	if (this.pptConceptos.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.pptConceptos.get(indice - 1).getPorcentaje() * 0.01);
            	A11 = A11.add(A5);
            	A11 = A11.add(A8);
            	A11 = A11.add(A9);
            	A11 = A11.multiply(porcentaje);
        	}
        	A11 = A11.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.pptConceptos.get(indice - 1).setMonto(A11);
        	stepTrace += "|calculo-A11-" + A11.toString();
			
			//------------------------------------------------------------------------ A14
			//A14 --> CAPTURABLE (subtotal2 = A6 + A13)
			indice = 14;
			A14 = this.pptConceptos.get(indice - 1).getMonto();
        	A14 = A14.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			this.pptConceptos.get(indice - 1).setMonto(A14);
        	stepTrace += "|calculo-A14-" + A14.toString();
        	
        	//------------------------------------------------------------------------ A12
        	// UTILIDAD --> =A14-(A7+A8+A9+A10+A11)
        	indice = 12;
        	if (this.pptConceptos.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.pptConceptos.get(indice - 1).getPorcentaje() * 0.01);
            	A12 = A12.add( A7);
            	A12 = A12.add( A8);
            	A12 = A12.add( A9);
            	A12 = A12.add(A10);
            	A12 = A12.add(A11);
            	A12 = A14.subtract(A12);
        	}
        	A12 = A12.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.pptConceptos.get(indice - 1).setMonto(A12);
        	stepTrace += "|calculo-A12-" + A12.toString();
        	
        	//------------------------------------------------------------------------ A13
        	// MARGEN TOTAL --> =A6+A8+A9+A10+A11+A12
        	indice = 13;
        	A13 = A13.add( A6);
        	A13 = A13.add( A8);
        	A13 = A13.add( A9);
        	A13 = A13.add(A10);
        	A13 = A13.add(A11);
        	A13 = A13.add(A12);
        	A13 = A13.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.pptConceptos.get(indice - 1).setMonto(A13);
        	stepTrace += "|calculo-A13-" + A13.toString();
        	
        	//------------------------------------------------------------------------ A14 --> CAPTURABLE
        	
        	//------------------------------------------------------------------------ A15
        	//A15 --> iva 16% = A14  * %
        	indice = 15;
    		porcentaje = new BigDecimal(this.pptConceptos.get(14).getPorcentaje() * 0.01);
        	A15 = A14.multiply(porcentaje);
        	A15 = A15.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.pptConceptos.get(indice - 1).setMonto(A15);
        	stepTrace += "|calculo-A15-" + A15.toString();
        	
        	//------------------------------------------------------------------------ A16
        	//A16 --> total con iva = A14 + A15
        	indice = 16;
        	A16 = A16.add(A14);
        	A16 = A16.add(A15);
        	A16 = A16.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.pptConceptos.get(indice - 1).setMonto(A16.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        	stepTrace += "|calculo-A16-" + A16.toString();
        	
        	// Redondeo a 2 decimales :)
        	for (PresupuestoDetalleExt var : this.pptConceptos)
        		var.setMonto(var.getMonto().setScale(2, BigDecimal.ROUND_HALF_EVEN));

        	stepTrace += "|calcularPorcentajesRestantes";
    		calcularPorcentajesRestantes(); 
    	} catch(Exception e) {
    		log.error("Ocurrio un problema al calcular los montos", e);
    	} finally {
    		controlLog("Calculo Terminado!\n\nTRACE\n\n" + stepTrace);
    	}
    }

    private void calcularPorcentajesRestantes() {
    	DecimalFormat formatter = new DecimalFormat("#0.00");
    	double relacionPorcentual = 100;
    	double porcentaje = 0;
    	double monto = 0;
    	int indice = -1;
    	
    	/*
    		Calcular todos los porcentajes que no son obligatorios: (Monto entre subtotal1) * 100
    		Los porcentajes obligatorios son: 8, 9, 10, 11, 12 y 15
    		Por tanto, los que se deben calcular son: 1, 2, 3, 4, 5, 6, 7, 13 y 14. --> Si es que no tienen porcentaje asignado.
    	*/
    	
    	for (int i = 0; i < 16; i++) {
    		if (i < 7 || (i > 11 && i != 14))
    			this.pptConceptos.get(i).setPorcentaje(0);
    		this.pptConceptos.get(i).setPorcentajeTotales(0);
    	}
    	
    	for (int item = 1; item <= 16; item++) {
    		indice = item - 1;
    		monto = this.pptConceptos.get(indice).getMonto().doubleValue();
    		if (monto <= 0)
    			continue;
    		
			// Porcentaje INSUMOS: A8, A9, A10, A11, A12 y A15 no se calculan
    		if (item < 8 || (item > 12 && item != 15)) {
				porcentaje = interpretaFormula(indice, false);
				porcentaje = porcentaje * relacionPorcentual;
        		if (Double.isNaN(porcentaje) || Double.isInfinite(porcentaje) || porcentaje < 0)
        			porcentaje = 0;
        		porcentaje = Double.parseDouble(formatter.format(porcentaje));
        		this.pptConceptos.get(indice).setPorcentaje(porcentaje);
    		}

			// Porcentaje SUBTOTAL: A1, A7, A15 y A16 no se calculan
    		if (item != 1 && item != 7 && item != 15 && item != 16) {
				porcentaje = interpretaFormula(indice, true);
				porcentaje = porcentaje * relacionPorcentual;
        		if (Double.isNaN(porcentaje) || Double.isInfinite(porcentaje) || porcentaje < 0)
        			porcentaje = 0;
        		if (porcentaje > 100) 
        			porcentaje = 100;
        		porcentaje = Double.parseDouble(formatter.format(porcentaje));
        		this.pptConceptos.get(indice).setPorcentajeTotales(porcentaje);
    		}
    	}
    }
    
    private double interpretaFormula(int itemIndex, boolean porcentajeTotales) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String formula = "";
        String expression = "";
        String val = "";
        int indexVal = -1;
        String[] split = null;
        Object result = null;
        double resultado = 0;
        boolean relacionPorcentual = false;
        
    	try {
			expression = this.pptConceptos.get(itemIndex).getConceptoPresupuesto().getFormulaPorcentajeInsumos();
    		if (porcentajeTotales)
    			expression = this.pptConceptos.get(itemIndex).getConceptoPresupuesto().getFormulaPorcentajeTotal();
            if (expression != null && ! "".equals(expression.trim())) {
            	// Validamos valores
            	formula = expression;
            	if (expression.contains("[A")) {
            		relacionPorcentual = false;
	            	split = expression.split("/");
	            	for (int x = 0; x < split.length; x++) {
	            		val = split[x];
	            		val = val.replace("[A", "").replace("]", "");
	            		indexVal = Integer.parseInt(val.trim());
	            		if (indexVal > 0 && indexVal <= this.pptConceptos.size()) {
	                		indexVal -= 1;
	                		expression = expression.replace(split[x], this.pptConceptos.get(indexVal).getMonto().toString());
	            		}
	            	}
            	} else if (expression.contains("[B")) {
            		relacionPorcentual = true;
	            	split = expression.split("\\+");
	            	for (int x = 0; x < split.length; x++) {
	            		val = split[x];
	            		val = val.replace("[B", "").replace("]", "");
	            		indexVal = Integer.parseInt(val.trim());
	            		if (indexVal > 0 && indexVal <= this.pptConceptos.size()) {
	                		indexVal -= 1;
	                		expression = expression.replace(split[x], String.valueOf(this.pptConceptos.get(indexVal).getPorcentaje()));
	            		}
	            	}
            	} else {
            		controlLog("No se pudo interpretar la formula. Variables desconocidas");
            		return 0;
            	}
            	
            	// Evaluamos la formula
                result = engine.eval(expression);
                resultado = (double) result;
                if (relacionPorcentual)
                	resultado = resultado / 100;
                return resultado;
            }
        } catch(ScriptException se) {
        	log.error("Ocurrio un problema al intentar interpretar la Formula para el ItemIndex " + itemIndex, se);
        } finally {
        	controlLog("Evaluacion de Formula\n" + formula + "\n" + result + " = " + expression);
        }
    	
    	return 0;
    }
    
    private BigDecimal calcularMontoTotal(){
    	double total = 0;
    	
    	for (PresupuestoDetalleExt var : this.pptConceptos)
    		total = var.getMonto().doubleValue();
    	
    	return (new BigDecimal(total)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

	// -------------------------------------------------------------------------------------------------------------------
	// CARGA PPT-01
	// -------------------------------------------------------------------------------------------------------------------

    public void nuevo() {
    	try {
			control();

			if (this.listPPTs == null)
				this.listPPTs = new LinkedHashMap<String, byte[]>();
			this.listPPTs.clear();
			
			if (this.layoutPPT == null)
				this.layoutPPT = new HashMap<String, String>();
			this.layoutPPT.clear();
			
			for(String key : this.pptProperties.keySet()) {
				this.layoutPPT.put(key.trim(), this.pptProperties.getString(key).trim());
			}
		} catch (Exception e) {
			control(true, 1, e.getMessage(), e);
			log.error("Ocurrio un error en GestionProyectos.PresupuestoAction.nuevo", e);
		}
    }
    
    public void carga(FileUploadEvent event) {
    	try {
    		control();
			iniciarBitacora();
			
			// Recuperamos el nuevo archivo
			this.fileName = event.getUploadedFile().getName();
			this.fileSrc = event.getUploadedFile().getData();
			
			// Añadimos al listado de PPTs
			if (this.listPPTs == null)
				this.listPPTs = new LinkedHashMap<String, byte[]>();
			this.listPPTs.put(this.fileName, this.fileSrc);
		} catch (Exception e) {
			control(true, 1, e.getMessage(), e);
			log.error("Ocurrio un error en GestionProyectos.PresupuestoAction.carga", e);
		}
    }
    
	public void procesar() {
    	try {
			control();
			if (this.listPPTs == null || this.listPPTs.isEmpty()) {
				control(-1, "Falta indicar el archivo");
				return;
			}
			
			// Lanzamos hilo con el procesamiento del archivo para desocupar el hilo principal y usarlo para reportar el avance
			this.procesando = true;
			new Thread(this).start();
		} catch (Exception e) {  
			control(true, 1, e.getMessage(), e);
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.procesar", e);
		}
    }
    
    @Override
	public void run() {
    	boolean multiCarga = false;
		
		try {
			control();
			controlLog("proceso iniciado");
			multiCarga = (this.listPPTs.size() > 1) ? true : false;

			for (Entry<String, byte[]> entry : this.listPPTs.entrySet()) {
				this.fileName = entry.getKey();
				this.fileSrc = entry.getValue();
				
				controlLog("---> Procesando... " + this.fileName + " <---");
				if (multiCarga) 
					bitacora("---> Procesando... " + this.fileName + " <---", false); 
				this.doProcesar();
				this.fileName = (multiCarga) ? "" : this.fileName;
				this.fileSrc = (multiCarga) ? null : this.fileSrc;
			}
			
			controlLog("---------------------------------------------------------");
			log.warn(this.listErrores.toString());
			controlLog("---------------------------------------------------------");
			this.listPPTs.clear();
			this.listErrores.clear();
		} catch (Exception e) {
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.run", e);
			control(true, 1, e.getMessage(), e);
		} finally {
			this.procesando = false;
			controlLog("proceso finalizado");
		}
	}
    
    @SuppressWarnings("unchecked")
	private void doProcesar() {
    	Respuesta result = null;
    	// ------------------------------------------------------
    	List<Presupuesto> lista = new ArrayList<Presupuesto>();
    	Presupuesto ppto = null;
    	BigDecimal montoTotal = BigDecimal.ZERO;
    	int indexDetails = 0;
    	
    	try {
    		control();
			
    		// Leemos listado PPTs: Comprobamos Estructura y datos
			bitacora("Comprobando estructura ... ", false);
			this.ifzPPT.setInfoSesion(this.loginManager.getInfoSesion());
			result = this.ifzPPT.procesarLayout(this.fileSrc, this.layoutPPT);
			if (result.getErrores().getCodigoError() != 0L) {
				log.error("--- ---> Error al realizar PROCESO DE LECTURA DE ARCHIVO");
				bitacora("Comprobando estructura ... ERROR", true); 
				bitacora("Carga fallida ...", false);
				control(-1, result.getErrores().getDescError());
				return;
			}
			
			this.listPPTrow = (List<PresupuestoRow>) result.getBody().getValor("procesados");
			bitacora("Comprobando estructura ... OK", true);
    		
			// Generamos Presupuestos
			for (PresupuestoRow var : this.listPPTrow) {
				bitacora("Validando " + var.getSheetName() + " ... ", false);
				ppto = null;
				if (! var.registroCompleto()) {
					bitacora("Validando " + var.getSheetName() + " ... ERROR", true);
					controlLog("--- ---> Validando " + var.getSheetName() + " ... Informacion faltante >>> " + var.informacionFaltante());
					this.listErrores.put(var.getSheetName(), var.informacionFaltante());
					this.listPPTfail.add(var);
					continue;
				}
				
				// Obtenemos datos
				bitacora("Comprobando " + var.getSheetName() + "... ", false);

	    		// Comprobamos PPTs: Obra
				this.pojoObra = null;
				controlLog("--- ---> Comprobando Obra ... --> " + var.getIdObra());
				this.pojoObra = this.ifzObras.findById(var.getIdObra());
				if (this.pojoObra == null) {
					bitacora("Comprobando " + var.getSheetName() + " ... ERROR", true);
					controlLog("--- ---> Comprobando Obra no valida [" + var.getIdObra() + "]");
					this.listErrores.put(var.getSheetName(), "Obra no valida [" + var.getIdObra() + "]");
					continue;
				}
				
				// Comprobamos PPTs: Presupuesto previo
				controlLog("--- ---> Comprobando Presupuesto previo ... ");
				this.ifzPPT.setInfoSesion(this.loginManager.getInfoSesion());
				ppto = this.ifzPPT.findActual(var.getIdObra());
				if (ppto != null && ppto.getId() != null && ppto.getId() > 0L) {
					ppto.setNombreArchivo(this.fileName + ">" + var.getSheetName());
	    			ppto.setTema(var.getTema().trim());
	    			ppto.setRuta(var.getRuta().trim());
					controlLog("--- ---> Presupuesto previo " + ppto.getId() + " ... ");
					if (! actualizarPPT01(ppto, var))
						continue;
				}

				if (ppto == null || ppto.getId() == null || ppto.getId() <= 0L) {
					try {
						controlLog("--- ---> Comprobando Conceptos PPTO ...");
						this.listConceptosPPT = new ArrayList<ConceptoPresupuesto>();
						this.pptConceptos = new ArrayList<PresupuestoDetalleExt>();

						this.ifzConceptosPPT.setInfoSesion(this.loginManager.getInfoSesion());
						this.listConceptosPPT = this.ifzConceptosPPT.findAllActivos();
						if (this.listConceptosPPT == null || this.listConceptosPPT.isEmpty()) {
							bitacora("Comprobando " + var.getSheetName() + " ... ERROR", true);
							controlLog("--- ---> No se pudo obtener los conceptos de presupuestos");
							this.listErrores.put(var.getSheetName(), "Presupuesto previo [" + ppto.getId() + "]");
							continue;
						}
				    	
				    	for (ConceptoPresupuesto con : this.listConceptosPPT) {
				    		PresupuestoDetalleExt detPPTO = new PresupuestoDetalleExt();
				    		detPPTO.setConceptoPresupuesto(con);
				    		detPPTO.setMonto(BigDecimal.ZERO);
				    		detPPTO.setFormula(con.getFormula());
				    		this.pptConceptos.add(detPPTO);
				    	}
						controlLog("--- ---> Comprobando Conceptos PPTO ... OK");
					} catch (Exception e) {
						bitacora("Comprobando " + var.getSheetName() + " ... ERROR", true);
						log.error("--- ---> ERROR al obtener los conceptos de presupuestos", e);
						this.listErrores.put(var.getSheetName(), "Presupuesto previo ");
						continue;
					}
					
		    		// Generamos PPT-01
					try {
						controlLog("--- ---> Guardando PPTO " + var.getSheetName() + " ... ");
						ppto = new Presupuesto();
		    			ppto.setTema(var.getTema().trim());
		    			ppto.setRuta(var.getRuta().trim());
		    			ppto.setIdObra(this.pojoObra);
						ppto.setMontoTotal(this.calcularMontoTotal());
						ppto.setNombreArchivo(this.fileName + ">" + var.getSheetName());
						ppto.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
		    			ppto.setFechaCreacion(Calendar.getInstance().getTime());
			    		ppto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						ppto.setFechaModificacion(Calendar.getInstance().getTime());
						
						// Guardamos en la BD
						this.ifzPPT.setInfoSesion(this.loginManager.getInfoSesion());
		    			ppto.setId(this.ifzPPT.save(ppto));
						lista.add(ppto);
						controlLog("--- ---> Guardando PPTO " + var.getSheetName() + " ... OK : " + ppto.getId());
					} catch (Exception e) {
						log.error("--- ---> Error Guardando PPTO " + var.getSheetName(), e);
						this.listErrores.put(var.getSheetName(), e.getMessage());
						continue;
					}
	    			
	    			// Guardamos los detalles
					try {
						montoTotal = BigDecimal.ZERO;
		    			for (PresupuestoDetalleExt det : this.pptConceptos) {
		    				indexDetails = this.pptConceptos.indexOf(det);
		    				det.setIdPresupuesto(ppto.getId());det.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
		        			det.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
		        			det.setFechaCreacion(Calendar.getInstance().getTime());
		        			det.setFechaModificacion(Calendar.getInstance().getTime());
	
		        			// Insertamos los valores de concepto obtenidos del EXCEL
	        				det.setMonto(getASBigDecimal(var.getConceptoByIndex(indexDetails).get("J")));
	        				det.setPorcentaje(getAsDouble(var.getConceptoByIndex(indexDetails).get("L")));
	        				det.setPorcentajeTotales(getAsDouble(var.getConceptoByIndex(indexDetails).get("M")));
	        				det.setNotas(var.getConceptoByIndex(indexDetails).get("N"));
		        			montoTotal = montoTotal.add(det.getMonto());
		        			
		        			// Guardamos en la BD
							this.ifzPPTConceptos.setInfoSesion(this.loginManager.getInfoSesion());
		        			this.ifzPPTConceptos.save(det);
		    				controlLog("--- ---> PPTO Concepto " + (indexDetails + 1) + " ... OK");
		        		}
					} catch (Exception e) {
						log.error("--- ---> PPTO Concepto " + indexDetails + " ... ERROR", e);
						this.listErrores.put(var.getSheetName(), e.getMessage());
						continue;
					}
				} else {
					montoTotal = BigDecimal.ZERO;
	    			for (PresupuestoDetalleExt det : this.pptConceptos)
	        			montoTotal = montoTotal.add(det.getMonto());
				}
				
				// Actualizo monto total de presupuesto
	    		ppto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				ppto.setFechaModificacion(Calendar.getInstance().getTime());
				ppto.setMontoTotal(montoTotal);
				this.ifzPPT.update(ppto);
    			
				bitacora("Comprobando " + var.getSheetName() + " ... OK", false);
				this.listErrores.put(var.getSheetName(), "OK");
			}
			
			bitacora(lista.size() + " PPTO-01 Guardados", false);
    	} catch (Exception e) {
			log.error("Ocurrio un error en GestionProyectos.PresupuestoAction.dpProcesar", e);
			control(true, 1, e.getMessage(), e);
		}
    }
    
    private boolean actualizarPPT01(Presupuesto ppto, PresupuestoRow ppt) {
		BigDecimal montoTotal = BigDecimal.ZERO;
    	int indexDetails = 0;

		try {
			this.ifzPPTConceptos.setInfoSesion(this.loginManager.getInfoSesion());
    		this.pptConceptos = this.ifzPPTConceptos.findByPropertyExt("idPresupuesto", ppto.getId(), 0);
    		if (this.pptConceptos == null || this.pptConceptos.isEmpty())
    			return false;
    		
    		Collections.sort(this.pptConceptos, new Comparator<PresupuestoDetalleExt>() {
	    			@Override
	    			public int compare(PresupuestoDetalleExt o1, PresupuestoDetalleExt o2) {
	    				return o1.getConceptoPresupuesto().getId().compareTo(o2.getConceptoPresupuesto().getId());
	    			}
	    	});
    		
			for (PresupuestoDetalleExt det : this.pptConceptos) {
				indexDetails = this.pptConceptos.indexOf(det);
				det.setIdPresupuesto(ppto.getId());
				det.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				det.setFechaModificacion(Calendar.getInstance().getTime());
	
				// Insertamos los valores de concepto obtenidos del EXCEL
				det.setMonto(getASBigDecimal(ppt.getConceptoByIndex(indexDetails).get("J")));
				det.setPorcentaje(getAsDouble(ppt.getConceptoByIndex(indexDetails).get("L")));
				det.setPorcentajeTotales(getAsDouble(ppt.getConceptoByIndex(indexDetails).get("M")));
				det.setNotas(ppt.getConceptoByIndex(indexDetails).get("N"));
				montoTotal = montoTotal.add(det.getMonto());
				
				// Guardamos en la BD
				this.ifzPPTConceptos.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzPPTConceptos.update(det);
				
				controlLog("--- ---> PPTO Concepto " + (indexDetails + 1) + " ... OK");
			}
		} catch (Exception e) {
			log.error("--- ---> PPTO Concepto " + indexDetails + " ... ERROR", e);
			return false;
		}
		
    	return true;
    }
    
    private void iniciarBitacora() {
		this.bitacora = new ArrayList<String>();
		this.listErrores = new LinkedHashMap<String, String>();
		this.listPPTrow = new ArrayList<PresupuestoRow>();
		this.listPPTfail = new ArrayList<PresupuestoRow>();
	}
	
	private void bitacora(String mensaje, boolean replacePrev) {
		this.bitacora.add(mensaje);
		if (replacePrev) 
			this.bitacora.remove(this.bitacora.size() - 2);
		controlLog("---> " + mensaje);
	}
    
	private double getAsDouble(String value) {
		if (value != null && ! "".equals(value))
			return new BigDecimal(value).doubleValue();
		return 0;
	}
	
	private BigDecimal getASBigDecimal(String value) {
		if (value != null && ! "".equals(value))
			return new BigDecimal(value);
		return BigDecimal.ZERO;
	}

	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------
	
	public String getObra() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L)
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
		return "";
	}
	
	public void setObra(String value) {}
	
	public boolean getPermitirCargaPPT() {
		return (this.isDebug && "ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()));
	}
	
	public void setPermitirCargaPPT(boolean value) {}
	
	public String getBitacora() {
		if (this.bitacora != null && ! this.bitacora.isEmpty())
			return this.bitacora.get(this.bitacora.size() - 1);
		return "";
	}
	
	public void setBitacora(String bitacora) { }

	public String getInfoFaltante() {
		String resultado = "";
		if (this.procesando || this.listPPTfail == null || this.listPPTfail.isEmpty())
			return "";
		for (PresupuestoRow var : this.listPPTfail)
			resultado += var.getSheetName() + ". " + var.informacionFaltante() + "\n";
		return resultado;
	}
	
	public void setInfoFaltante(String value) { }
	
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

	public List<PresupuestoDetalleExt> getListDetalles() {
		return pptConceptos;
	}

	public void setListDetalles(List<PresupuestoDetalleExt> listDetalles) {
		this.pptConceptos = listDetalles;
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

	public Presupuesto getPojoPresupuesto() {
		if (this.pojoPPT == null) 
			this.pojoPPT = new Presupuesto();
		return this.pojoPPT;
	}

	public void setPojoPresupuesto(Presupuesto pojoPresupuesto) {
		this.pojoPPT = pojoPresupuesto;
	}
	
	public boolean isCargaInsumosValidada() {
		return this.cargaInsumosValidada;
	}

	public void setCargaInsumosValidada(boolean cargaInsumosValidada) {
		this.cargaInsumosValidada = cargaInsumosValidada;
	}
	
	public boolean isDebugging() { 
		if (this.paramsRequest.containsKey("DEBUG") && "1".equals(this.paramsRequest.get("DEBUG"))) 
			return true;
		return false;
	}
	
	public void setDebugging(boolean isDebug) { }

	public int getMaxCargas() {
		return this.maxCargas;
	}

	public void setMaxCargas(int maxCargas) {
		this.maxCargas = maxCargas;
	}
	
	public boolean getProcesando() {
		return this.procesando;
	}

	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}

	public boolean isAvisoInsumos() {
		return avisoInsumos;
	}
	
	public void setAvisoInsumos(boolean avisoInsumos) {
		this.avisoInsumos = avisoInsumos;
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

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado carga de PPT-01
 */