package net.giro.adp;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

@ViewScoped
@ManagedBean(name="pptoAction")
public class PresupuestoAction implements Serializable, Runnable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PresupuestoAction.class);	
	private InitialContext ctx; 
	private LoginManager loginManager; 
	private PropertyResourceBundle entornoProperties;
	private HttpSession httpSession;	
	// Interfaces
	private ObraRem ifzObras;
	private InsumosRem ifzInsumos;
	private PresupuestoRem ifzPresupuesto;
	private PresupuestoDetalleRem ifzPresupuestoDetalle;
	private ConceptoPresupuestoRem ifzConceptosPresupuesto;
	private ReportesRem	ifzReportes;	
	private List<Obra> listObras;
	private InsumosExt pojoExplosionInsumos;
	private List<PresupuestoDetalleExt> listDetalles;
	private List<ConceptoPresupuesto> listConceptosPresupuesto;	
	private Obra pojoObra;
	private Presupuesto pojoPresupuesto;
	private boolean cargaInsumosValidada;
	private boolean avisoInsumos;
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
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
    // PERFILES
	private boolean PERFIL_ADMINISTRATIVO;

	
	public PresupuestoAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String resValue = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			this.porcentajeIva = Double.valueOf(this.loginManager.getAutentificacion().getPerfil("VALOR_IVA"));

			// EVALUACION DE PERFILES
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.PERFIL_ADMINISTRATIVO = ((resValue != null && "S".equals(resValue)) ? true : false);
			
			// Inicializaciones
			Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());

			// Interfaces
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzInsumos = (InsumosRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
			this.ifzPresupuesto = (PresupuestoRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//PresupuestoFac!net.giro.adp.logica.PresupuestoRem");
			this.ifzPresupuestoDetalle = (PresupuestoDetalleRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//PresupuestoDetalleFac!net.giro.adp.logica.PresupuestoDetalleRem");
			this.ifzConceptosPresupuesto = (ConceptoPresupuestoRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ConceptoPresupuestoFac!net.giro.adp.logica.ConceptoPresupuestoRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			
			// Propiedades y carga de layout para empleados
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{ppt}", PropertyResourceBundle.class);
			this.pptProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "ID"));
			this.busquedaCampo = busquedaOpciones.get(0).getDescription();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
			
			this.maxCargas = 1;
			if (this.isDebug && this.paramsRequest.containsKey("MULTI_CARGA"))
				this.maxCargas = 0; 
		} catch (Exception e) {
			this.ctx = null;
			log.error("Error en constructor GestionProyectos.PresupuestoAction", e);
		}
	}
	

	public void buscar() {
		try {
			control();
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = "nombre";
			
			if (this.listObras != null)
				this.listObras.clear();

			this.busquedaTipo = 0;
			if (this.busquedaAdministrativas)
				this.busquedaTipo = 4;

			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo);
			if (this.listObras == null || this.listObras.isEmpty()) {
				control(true, 2, "Busqueda sin resultados");
				return;
			}
		} catch(Exception e) {
			control(true, -1, "Ocurrio un problema al intentar realizar la busqueda solicitada", e);
    	}
	}
	
	public void ver() {
		List<Presupuesto> presupuestos = null;
		double montoInsumo = 0;
		double montoPPT01 = 0;
		String stepTrace = "";
		
		try {
			control();
			this.avisoInsumos = false;
			stepTrace += "|comprobando-obra";
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				return;
			}

			//Primero, se debería revisar si ya existe un presupuesto cargado para esta obra: Se hace obvio que hay obra, puesto que solo cuando se edita es posible acceder a la forma
			stepTrace += "|comprobando-presupuesto";
			log.info("Ver PPT-01 para " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre() + " por " + this.usuario);
    		presupuestos = this.ifzPresupuesto.findByProperty("idObra.id", this.pojoObra.getId(), 1);
    		if (presupuestos != null && ! presupuestos.isEmpty()) {	//debe tener uno para considerar que ya se tiene un presupuesto agregado a esta obra
    			this.pojoPresupuesto = getLastPresupuesto(presupuestos);
    			
    			stepTrace += "|obteniendo-detalles-presupuesto-" + this.pojoPresupuesto.getId();
        		this.listDetalles = this.ifzPresupuestoDetalle.findByPropertyExt("idPresupuesto", this.pojoPresupuesto.getId(), 0);

    			stepTrace += "|comprobando-detalles";
        		if (this.listDetalles.size() == 16) {
            		montoPorcentajeCapturables();

        			// A14 = A5 + A13
            		if (this.listDetalles.get(13).getMonto() == null || this.listDetalles.get(13).getMonto().doubleValue() <= 0)
            			this.listDetalles.get(13).setMonto(this.listDetalles.get(4).getMonto().add(this.listDetalles.get(12).getMonto()));
            		
            		// porcentaje de IVA: Tomado por perfil
            		if (this.listDetalles.get(14).getMonto().doubleValue() <= 0)
            			this.listDetalles.get(14).setPorcentaje(this.porcentajeIva);
            		
        			this.cargaInsumosValidada = true;
        			stepTrace += "|carga-presupuesto-terminada|recuperando-explosion";
        			
        			if (obtenerExplosionInsumos()) {
            			stepTrace += "|explosion-recuperada-comprobando-valores";
        				montoInsumo = this.pojoExplosionInsumos.getMontoMateriales().setScale(0, BigDecimal.ROUND_FLOOR).doubleValue();
        		    	montoPPT01 = this.listDetalles.get(0).getMonto().setScale(0, BigDecimal.ROUND_FLOOR).doubleValue();
        		    	
        		    	if (montoPPT01 != montoInsumo) {
                			stepTrace += "|explosion-modificada-avisar";
                			this.avisoInsumos = true;
        		    		this.listDetalles.get(0).setMonto(this.pojoExplosionInsumos.getMontoMateriales());
        		    		this.listDetalles.get(2).setMonto(this.pojoExplosionInsumos.getMontoManoDeObra());
        		    		this.listDetalles.get(3).setMonto(this.pojoExplosionInsumos.getMontoHerramientas());
        		    		recalcularMontos();
        		    		control(false, -1, "Se detecto un cambio en la Explosion de Insumos\nVerifique que los montos en este PPT esten correctos");
        		    	}

            			stepTrace += "|comprobacion-explosion-terminada";
        				return;
        			}
        		}
    		} 
    		
    		//Si se llegó a este punto, quiere decir que se cargará un nuevo presupuesto
			stepTrace += "|generando-nuevo-presupuesto";
			if (this.pojoPresupuesto == null)
				this.pojoPresupuesto = new Presupuesto();
			
			if (! obtenerExplosionInsumos())
				return;
    		
    		if (this.pojoExplosionInsumos != null)
				cargarConceptosPresupuesto();
		} catch(Exception e) {
    		log.error("Error en GestionProyectos.PresupuestoAction.ver().\n\nTRACE " + stepTrace + "\n\n", e);
			control(true, 1, e.getMessage(), e);
    	} finally {
			log.info("Proceso Terminado!.\n\nTRACE\n" + stepTrace + "\n\n");
    	}
	}
	
	public void guardar() {
		String stepTrace = "";
		
		try {
			control();
			log.info("Guardar PPT-01 para " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre() + " por " + this.usuario);
			stepTrace += "|validando";
			if (! validarCamposCalculo()) {
				control(true, -1, this.mensaje);
				return;
			}

			stepTrace += "|asignando";
			this.pojoPresupuesto.setMontoTotal(this.calcularMontoTotal());
    		this.pojoPresupuesto.setModificadoPor(this.usuarioId);
			this.pojoPresupuesto.setFechaModificacion(Calendar.getInstance().getTime());
			
			//Guardar; se compara con cero porque es un objeto de tipo primitivo
    		if (this.pojoPresupuesto.getId() == null || this.pojoPresupuesto.getId() <= 0L) {
    			this.pojoPresupuesto.setCreadoPor(this.usuarioId);
    			this.pojoPresupuesto.setFechaCreacion(Calendar.getInstance().getTime());
    			this.pojoPresupuesto.setIdObra(this.ifzObras.findById(this.pojoObra.getId()));
    			//this.pojoPresupuesto.setIdObra(this.pojoObra.getId());
    			
    			// Guardamos en la BD
    			log.info("Guardando en BD ... ");
    			stepTrace += "|guardando";
    			this.pojoPresupuesto.setId( this.ifzPresupuesto.save(this.pojoPresupuesto));
    			stepTrace += "-OK-" + this.pojoPresupuesto.getId();
    			log.info("Guardado en BD. Guardando detalles ... ");
    		} else {
    			// Actualizamos en la BD
    			log.info("Actualizando en BD ... ");
    			stepTrace += "|actualizando-" + this.pojoPresupuesto.getId();
    			this.ifzPresupuesto.update(this.pojoPresupuesto);
    			stepTrace += "-OK-" + this.pojoPresupuesto.getId();
    			log.info("Actualizado en BD. Actualizando detalles ... ");
    		}

			// Guardamos los detalles
			if (this.listDetalles != null && ! this.listDetalles.isEmpty()) {
    			stepTrace += "|guardando-detalles-x" + this.listDetalles.size();
    			for (PresupuestoDetalleExt var : this.listDetalles) {
	    			stepTrace += "\n|guardando-detalle-" + this.listDetalles.indexOf(var);
    				var.setIdPresupuesto(this.pojoPresupuesto.getId());
        			var.setCreadoPor(this.usuarioId);
        			var.setFechaCreacion(Calendar.getInstance().getTime());
        			var.setModificadoPor(this.usuarioId);
        			var.setFechaModificacion(Calendar.getInstance().getTime());
        			var.setMonto(var.getMonto().setScale(2, BigDecimal.ROUND_CEILING)); // redondeamos a 2 digitos
        			var.setPorcentaje(new BigDecimal(var.getPorcentaje()).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
        			var.setPorcentajeTotales(new BigDecimal(var.getPorcentajeTotales()).setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
        			
        			// Guardamos en la BD
        			if (var.getId() == null || var.getId() <= 0L)
        				var.setId(this.ifzPresupuestoDetalle.save(var));
        			else
        				this.ifzPresupuestoDetalle.update(var);
	    			stepTrace += "-OK-" + var.getId();
        		}
			}
		} catch(Exception e) {
    		log.error("Error en GestionProyectos.PresupuestoAction.guardar", e);
			control(true, 1, e.getMessage(), e);
    	} finally {
			log.info("Guardando PPT-01 Terminado!.\n\nTRACE\n" + stepTrace + "\n\n");
    	}
	}

    public void reporte() {
    	Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		String nombreDocumento = "";
		
		try{
			if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L) {
				// Parametros del reporte
				paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idObra", this.pojoObra.getId());

				// Parametros para la ejecucion del reporte
				params = new HashMap<String, String>();
				params.put("idPrograma", 	  this.entornoProperties.getString("REPORTE_PPT01"));
				//params.put("nombreDocumento", this.entornoProperties.getString("REPORTE_PPT01_NOMBRE"));
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);

				respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if(respuesta.getErrores().getCodigoError() != 0L) {
					control(true, -1, respuesta.getErrores().getDescError());
					return;
				}

				nombreDocumento = "PPT01-O-" + this.pojoObra.getId() + "." + respuesta.getBody().getValor("formatoReporte");
				this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
				this.httpSession.setAttribute("formato", respuesta.getBody().getValor("formatoReporte")); 	
				this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			}
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al lanzar el reporte de PPT01", e);
		}
	}

	private Presupuesto getLastPresupuesto(List<Presupuesto> presupuestos) {
		if (presupuestos.size() > 1) {
			Collections.sort(presupuestos, new Comparator<Presupuesto>() {
					@Override
					public int compare(Presupuesto o1, Presupuesto o2) {
						return o2.getId().compareTo(o1.getId());
					}
			});
		}
		
		return presupuestos.get(0);
	}
	
	private boolean obtenerExplosionInsumos() {
		List<InsumosExt> listaInsumos = null;
		
		try {
			this.pojoExplosionInsumos = null;
			listaInsumos = this.ifzInsumos.findByPropertyExt("idObra", this.pojoObra.getId(), 0);
			if (listaInsumos == null || listaInsumos.isEmpty()) {
				control(true, -1, "El archivo de insumos debe cargarse previamente");
		    	this.cargaInsumosValidada = false;
				return false;
			}
			
			// Recuperamos la explosion de insumos activa
			for (InsumosExt var : listaInsumos) {
				if (var.getEstatus() == 1)
					continue;
				
				this.cargaInsumosValidada = true;
				this.pojoExplosionInsumos = var;
				break;
			}
			
			return true;
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar recuperar la Explosion de Insumos para la Obra seleccionada", e);
			return false;
		}
	}
	
 	private void control() { 
		control(false, 0, "", null);
	}
	
	private void control(boolean procesoInterrumpido, int tipo, String mensaje) {
		control(procesoInterrumpido, tipo, mensaje, null);
	}

	private void control(boolean val1, int val2, String val3, Throwable t) {
		this.incompleto = val1;
		this.tipoMensaje = val2;
		this.mensaje = val3;
		
		if (this.mensaje != null && this.mensaje.contains("\n"))
			this.mensaje = this.mensaje.replace("\n", "<br>");
		
		this.mensajeDetalles = "";
		if (t != null) {
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// CONCEPTOS
	// -------------------------------------------------------------------------------------------------------------------

	private void cargarConceptosPresupuesto() {
		if (this.listConceptosPresupuesto == null)
			this.listConceptosPresupuesto = new ArrayList<ConceptoPresupuesto>();
    	this.listConceptosPresupuesto = this.ifzConceptosPresupuesto.findAllActivos();
    	
    	if (this.listConceptosPresupuesto == null || this.listConceptosPresupuesto.isEmpty())
    		return;
    	
    	//Recorrer la lista de Conceptos de Presupuesto e ir cargando a la lista del Presupuesto --> this.listDetalles
    	if (this.listDetalles == null)
    		this.listDetalles = new ArrayList<PresupuestoDetalleExt>();
    	this.listDetalles.clear();
    	
    	for(ConceptoPresupuesto var : this.listConceptosPresupuesto) {
    		PresupuestoDetalleExt detPPTO = new PresupuestoDetalleExt();
    		if (this.pojoPresupuesto.getId() != null && this.pojoPresupuesto.getId() > 0L)
    			detPPTO.setIdPresupuesto(this.pojoPresupuesto.getId());
    		detPPTO.setConceptoPresupuesto(var);
    		detPPTO.setMonto(BigDecimal.ZERO);
    		detPPTO.setFormula(var.getFormula());
    		
    		this.listDetalles.add(detPPTO);
    	}
    	
    	if (this.listDetalles.get(14).getPorcentaje() <= 0)
			this.listDetalles.get(14).setPorcentaje(this.porcentajeIva);
    	
    	montoPorcentajeCapturables();
    	
    	//Mostramos los primeros 4 valores, que ya sabemos como se calculan
    	int indice = 0;
    	// ------------------------------------------------------------------------ A2
		BigDecimal A2 = this.pojoExplosionInsumos.getMontoMateriales();
		
		//Materiales Explosión de Insumos (sin IVA) = insumosPresupuesto.totalMateriales.
    	indice = 1;
    	this.listDetalles.get(indice-1).setMonto(A2);
    	
    	//Costo de Material (sin IVA y sin Oculto) = insumosPresupuesto.totalMateriales.
    	indice = 2;
    	this.listDetalles.get(indice-1).setMonto(A2);

    	// ------------------------------------------------------------------------ A3
    	//Costo de Mano de Obra (sin IVA) = insumosPresupuesto.totalManoObra.
    	BigDecimal A3 = this.pojoExplosionInsumos.getMontoManoDeObra();
    	indice = 3;
    	this.listDetalles.get(indice-1).setMonto(A3);

    	// ------------------------------------------------------------------------ A4
    	//Costo Herramienta y equipo (sin IVA) = insumosPresupuesto.totalHerramientas
    	BigDecimal A4 = this.pojoExplosionInsumos.getMontoHerramientas();
    	indice = 4;
    	this.listDetalles.get(indice-1).setMonto(A4);
    	
    	// ------------------------------------------------------------------------ A5
    	//A5 --> A2 + A3 + A4
    	//montoTotalMateriales + costoManoObra + totalHerramientas;
    	indice = 5;
    	BigDecimal A5 = BigDecimal.ZERO;
    	A5 = A5.add(A2);
    	A5 = A5.add(A3);
    	A5 = A5.add(A4);
    	this.listDetalles.get(indice-1).setMonto(A5);
    	
    	// ------------------------------------------------------------------------ A6
    	//A6 --> A1 - A2
    	// OCULTO: Materiales Explosión de Insumos - Costo de Material --- por ahora sera 0 (cero)
    	indice = 6;
    	BigDecimal A6 = BigDecimal.ZERO;
    	/*A6 = A6.add(A2);
    	A6 = A6.add(A3);
    	A6 = A6.add(A4);*/
    	this.listDetalles.get(indice-1).setMonto(A6);
    	
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
    	for(PresupuestoDetalleExt item : this.listDetalles) {
    		index = this.listDetalles.indexOf(item);
    		
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

	private boolean validarCamposCalculo(){
    	//Verificar que se cuente con los campos requeridos: Oculto, Indirectos de Campo, Indirectos de Oficina Central, Cargos Adicionales,  Financiamiento, Utilidad
    	this.mensaje = "";
    	this.cargaInsumosValidada = false;	//Se ocupará esta variable para no consumir mas
    	this.tipoMensaje = -1;
    	int indice = 0;
    	
		log.info("Validando campos");

    	indice = 5;
    	if(this.listDetalles.get(indice - 1).getMonto() == null || this.listDetalles.get(indice - 1).getMonto().equals(BigDecimal.ZERO) ){
    		this.mensaje = "Indique cantidad para concepto: " + this.listDetalles.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		return false;
    	}
    	
    	indice = 7;
    	if( this.listDetalles.get(indice - 1).getMonto().equals(BigDecimal.ZERO) ){
    		this.mensaje = "Indique cantidad para concepto: " + this.listDetalles.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		return false;
    	}
    	
    	//8,9,10,11,12, 15
    	indice = 8;
    	if( this.listDetalles.get(indice - 1).getMonto().doubleValue() > 0 && this.listDetalles.get(indice - 1).getPorcentaje() == 0 ){
    		this.mensaje = "Indique porcentaje para concepto: " + this.listDetalles.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		return false;
    	}
    	
    	indice = 9;
    	if( this.listDetalles.get(indice - 1).getMonto().doubleValue() > 0 && this.listDetalles.get(indice - 1).getPorcentaje() == 0 ){
    		this.mensaje = "Indique porcentaje para concepto: " + this.listDetalles.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		return false;
    	}

    	indice = 10;
    	if( this.listDetalles.get(indice - 1).getMonto().doubleValue() > 0 && this.listDetalles.get(indice - 1).getPorcentaje() == 0 ){
    		this.mensaje = "Indique porcentaje para concepto: " + this.listDetalles.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		return false;
    	}

    	indice = 11;
    	if( this.listDetalles.get(indice - 1).getMonto().doubleValue() > 0 && this.listDetalles.get(indice - 1).getPorcentaje() == 0 ){
    		this.mensaje = "Indique porcentaje para concepto: " + this.listDetalles.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		return false;
    	}

    	indice = 12;
    	if( this.listDetalles.get(indice - 1).getMonto().doubleValue() > 0 && this.listDetalles.get(indice - 1).getPorcentaje() == 0 ){
    		this.mensaje = "Indique porcentaje para concepto: " + this.listDetalles.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		return false;
    	}
    	
    	indice = 14;
		if (this.listDetalles.get(indice - 1).getMonto().doubleValue() <= 0) {
    		this.mensaje = "Indique cantidad para concepto: " + this.listDetalles.get(indice - 1).getConceptoPresupuesto().getConcepto();
    		return false;
		}
    	
    	indice = 15;
    	if( this.listDetalles.get(indice - 1).getMonto().doubleValue() > 0 && this.listDetalles.get(indice - 1).getPorcentaje() == 0 ){
    		this.mensaje = "Indique porcentaje para concepto: IVA";
    		return false;
    	}

		log.info("Validacion terminada. Guardando ... ");
    	this.cargaInsumosValidada = true;	//Se ocupará esta variable para no consumir mas
    	this.mensaje = "";
    	this.tipoMensaje = 0;
    	
    	return true;
    }

    public void recalcularMontos() {
    	if(! validarCamposCalculo()) 
    		return;
    	
    	calcularMontos();
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
    		if (this.listDetalles.get(14).getPorcentaje() <= 0)
    			this.listDetalles.get(14).setPorcentaje(this.porcentajeIva);

        	stepTrace += "|calcularMontos";
        	// ------------------------------------------------------------------------ A1
    		// MATERIALES EXPLOSION DE INSUMOS (CON OCULTO Y SIN IVA) --> insumosPresupuesto.totalMateriales
        	indice = 1;
    		A1 = this.listDetalles.get(indice - 1).getMonto();
        	A1 = A1.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	stepTrace += "|calculo-A1-" + A1.toString();

        	// ------------------------------------------------------------------------ A2
    		// COSTO DE MATERIAL (SIN IVA Y SIN OCULTO) --> insumosPresupuesto.totalMateriales :: capturable
        	indice = 2;
        	A2 = this.listDetalles.get(indice - 1).getMonto();
        	A2 = A2.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	if (this.listDetalles.get(indice - 1).getMonto().doubleValue() <= 0) {
        		A2 = this.pojoExplosionInsumos.getMontoMateriales();
            	A2 = A2.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            	this.listDetalles.get(indice - 1).setMonto(A2);
        	}
        	stepTrace += "|calculo-A2-" + A2.toString();

        	// ------------------------------------------------------------------------ A3
        	// COSTO DE MANO DE OBRA (SIN IVA) --> insumosPresupuesto.totalManoObra
        	indice = 3;
        	A3 = this.listDetalles.get(indice - 1).getMonto();
        	A3 = A3.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	stepTrace += "|calculo-A3-" + A3.toString();

        	// ------------------------------------------------------------------------ A4
        	// COSTO HERRAMIENTA Y EQUIPO (SIN IVA) --> insumosPresupuesto.totalHerramientas
        	indice = 4;
        	A4 = this.listDetalles.get(indice - 1).getMonto();
        	A4 = A4.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	stepTrace += "|calculo-A4-" + A4.toString();

        	// ------------------------------------------------------------------------ A5
        	// SUBTOTAL COSTO DIRECTO 1 (SIN IVA Y SIN OCULTO) --> = A2 + A3 + A4
        	indice = 5;
        	A5 = this.listDetalles.get(indice - 1).getMonto();
        	A5 = A5.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	if (this.listDetalles.get(indice - 1).getMonto() == null || this.listDetalles.get(indice - 1).getMonto().equals(BigDecimal.ZERO)) {
        		A5 = A5.add(A2);
        		A5 = A5.add(A3);
        		A5 = A5.add(A4);
            	A5 = A5.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        		this.listDetalles.get(indice - 1).setMonto(A5);
        	}
        	stepTrace += "|calculo-A5-" + A5.toString();
        	
        	// ------------------------------------------------------------------------ A6
        	// OCULTO --> = A1-A2
        	indice = 6;
        	A6 = this.listDetalles.get(indice - 1).getMonto();
        	A6 = A6.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	if (this.listDetalles.get(indice - 1).getMonto() == null || this.listDetalles.get(indice - 1).getMonto().equals(BigDecimal.ZERO)) {
        		A6 = new BigDecimal(Math.abs(A1.subtract(A2).doubleValue()));
            	A6 = A6.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        		this.listDetalles.get(indice - 1).setMonto(A6);
        	}
        	stepTrace += "|calculo-A6-" + A6.toString();

        	// ------------------------------------------------------------------------ A7
        	// TOTAL COSTO DIRECTO 1 (SIN IVA) --> =A5+A6
        	indice = 7;
        	A7 = A7.add(A5);
        	A7 = A7.add(A6);
        	A7 = A7.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.listDetalles.get(indice - 1).setMonto(A7);
        	stepTrace += "|calculo-A7-" + A7.toString();
        	
        	// ------------------------------------------------------------------------ A8
        	// INDIRECTOS DE CAMPO --> =A7 * %INDIRECTOS DE CAMPO
        	indice = 8;
        	if (this.listDetalles.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listDetalles.get(indice - 1).getPorcentaje() * 0.01);
        		A8 = A7.multiply(porcentaje);
        	}
        	A8 = A8.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.listDetalles.get(indice - 1).setMonto(A8);
        	stepTrace += "|calculo-A8-" + A8.toString();
        	
        	// ------------------------------------------------------------------------ A9
        	// INDIRECTOS DE OFICINA CENTRAL --> =A7 * %INDIRECTOS DE OFICINA CENTRAL 
        	indice = 9;
        	if (this.listDetalles.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listDetalles.get(indice - 1).getPorcentaje() * 0.01);
        		A9 = A7.multiply(porcentaje);
        	}
        	A9 = A9.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.listDetalles.get(indice - 1).setMonto(A9);
        	stepTrace += "|calculo-A9-" + A9.toString();
        	
        	//------------------------------------------------------------------------ A10
        	//CARGOS ADICIONALES --> =A5 * % CARGOS ADICIONALES
        	indice = 10;
        	if (this.listDetalles.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listDetalles.get(indice - 1).getPorcentaje() * 0.01);
            	A10 = A5.multiply(porcentaje);
        	}
        	A10 = A10.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.listDetalles.get(indice - 1).setMonto(A10);
        	stepTrace += "|calculo-A10-" + A10.toString();
        	
        	//------------------------------------------------------------------------ A11
        	//A11 -->	financiamiento = (A5 + A8 + A9) * %
        	indice = 11;
        	if (this.listDetalles.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listDetalles.get(indice - 1).getPorcentaje() * 0.01);
            	A11 = A11.add(A5);
            	A11 = A11.add(A8);
            	A11 = A11.add(A9);
            	A11 = A11.multiply(porcentaje);
        	}
        	A11 = A11.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.listDetalles.get(indice - 1).setMonto(A11);
        	stepTrace += "|calculo-A11-" + A11.toString();
			
			//------------------------------------------------------------------------ A14
			//A14 --> CAPTURABLE (subtotal2 = A6 + A13)
			indice = 14;
			A14 = this.listDetalles.get(indice - 1).getMonto();
        	A14 = A14.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			if (this.listDetalles.get(indice - 1).getMonto() == null) {
				A14 = BigDecimal.ZERO;
				this.listDetalles.get(indice - 1).setMonto(A14);
			}
        	stepTrace += "|calculo-A14-" + A14.toString();
        	
        	//------------------------------------------------------------------------ A12
        	// UTILIDAD --> =A14-(A7+A8+A9+A10+A11)
        	indice = 12;
        	if (this.listDetalles.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listDetalles.get(indice - 1).getPorcentaje() * 0.01);
            	A12 = A12.add( A7);
            	A12 = A12.add( A8);
            	A12 = A12.add( A9);
            	A12 = A12.add(A10);
            	A12 = A12.add(A11);
            	A12 = A14.subtract(A12);
        	}
        	A12 = A12.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.listDetalles.get(indice - 1).setMonto(A12);
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
        	this.listDetalles.get(indice - 1).setMonto(A13);
        	stepTrace += "|calculo-A13-" + A13.toString();
        	
        	//------------------------------------------------------------------------ A14 --> CAPTURABLE
        	
        	//------------------------------------------------------------------------ A15
        	//A15 --> iva 16% = A14  * %
        	indice = 15;
    		porcentaje = new BigDecimal(this.listDetalles.get(14).getPorcentaje() * 0.01);
        	A15 = A14.multiply(porcentaje);
        	A15 = A15.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.listDetalles.get(indice - 1).setMonto(A15);
        	stepTrace += "|calculo-A15-" + A15.toString();
        	
        	//------------------------------------------------------------------------ A16
        	//A16 --> total con iva = A14 + A15
        	indice = 16;
        	A16 = A16.add(A14);
        	A16 = A16.add(A15);
        	A16 = A16.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        	this.listDetalles.get(indice - 1).setMonto(A16.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        	stepTrace += "|calculo-A16-" + A16.toString();
        	
        	// Redondeo a 2 decimales :)
        	for (PresupuestoDetalleExt var : this.listDetalles)
        		var.setMonto(var.getMonto().setScale(2, BigDecimal.ROUND_HALF_EVEN));

        	stepTrace += "|calcularPorcentajesRestantes";
    		calcularPorcentajesRestantes(); 
    	} catch(Exception e) {
    		log.error("Ocurrio un problema al calcular los montos", e);
    	} finally {
    		log.info("Calculo Terminado!\n\nTRACE\n\n" + stepTrace);
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
    			this.listDetalles.get(i).setPorcentaje(0);
    		this.listDetalles.get(i).setPorcentajeTotales(0);
    	}
    	
    	for (int item = 1; item <= 16; item++) {
    		indice = item - 1;
    		monto = this.listDetalles.get(indice).getMonto().doubleValue();
    		if (monto <= 0)
    			continue;
    		
			// Porcentaje INSUMOS: A8, A9, A10, A11, A12 y A15 no se calculan
    		if (item < 8 || (item > 12 && item != 15)) {
				porcentaje = interpretaFormula(indice, false);
				porcentaje = porcentaje * relacionPorcentual;
        		if (Double.isNaN(porcentaje) || Double.isInfinite(porcentaje) || porcentaje < 0)
        			porcentaje = 0;
        		porcentaje = Double.parseDouble(formatter.format(porcentaje));
        		this.listDetalles.get(indice).setPorcentaje(porcentaje);
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
        		this.listDetalles.get(indice).setPorcentajeTotales(porcentaje);
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
			expression = this.listDetalles.get(itemIndex).getConceptoPresupuesto().getFormulaPorcentajeInsumos();
    		if (porcentajeTotales)
    			expression = this.listDetalles.get(itemIndex).getConceptoPresupuesto().getFormulaPorcentajeTotal();
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
	            		if (indexVal > 0 && indexVal <= this.listDetalles.size()) {
	                		indexVal -= 1;
	                		expression = expression.replace(split[x], this.listDetalles.get(indexVal).getMonto().toString());
	            		}
	            	}
            	} else if (expression.contains("[B")) {
            		relacionPorcentual = true;
	            	split = expression.split("\\+");
	            	for (int x = 0; x < split.length; x++) {
	            		val = split[x];
	            		val = val.replace("[B", "").replace("]", "");
	            		indexVal = Integer.parseInt(val.trim());
	            		if (indexVal > 0 && indexVal <= this.listDetalles.size()) {
	                		indexVal -= 1;
	                		expression = expression.replace(split[x], String.valueOf(this.listDetalles.get(indexVal).getPorcentaje()));
	            		}
	            	}
            	} else {
            		log.info("No se pudo interpretar la formula. Variables desconocidas");
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
        	log.info("Evaluacion de Formula\n" + formula + "\n" + result + " = " + expression);
        }
    	
    	return 0;
    }
    
    private BigDecimal calcularMontoTotal(){
    	double total = 0;
    	
    	for (PresupuestoDetalleExt var : this.listDetalles)
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
			if(this.listPPTs == null || this.listPPTs.isEmpty()) {
				control(true, -1, "Falta indicar el archivo");
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
			log.info("proceso iniciado");
			multiCarga = (this.listPPTs.size() > 1) ? true : false;

			for (Entry<String, byte[]> entry : this.listPPTs.entrySet()) {
				this.fileName = entry.getKey();
				this.fileSrc = entry.getValue();
				
				log.info("---> Procesando... " + this.fileName + " <---");
				if (multiCarga) bitacora("---> Procesando... " + this.fileName + " <---", false); 
				this.doProcesar();
				this.fileName = (multiCarga) ? "" : this.fileName;
				this.fileSrc = (multiCarga) ? null : this.fileSrc;
			}
			
			log.info("---------------------------------------------------------");
			log.warn(this.listErrores.toString());
			log.info("---------------------------------------------------------");
			this.listPPTs.clear();
			this.listErrores.clear();
		} catch (Exception e) {
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.run", e);
			control(true, 1, e.getMessage(), e);
		} finally {
			this.procesando = false;
			log.info("proceso finalizado");
		}
	}
    
    @SuppressWarnings("unchecked")
	private void doProcesar() {
    	List<Presupuesto> lista = new ArrayList<Presupuesto>();
    	BigDecimal montoTotal = BigDecimal.ZERO;
    	int indexDetails = 0;
    	
    	try {
    		control();
			
    		// Leemos listado PPTs: Comprobamos Estructura y datos
			bitacora("Comprobando estructura ... ", false);
			Respuesta result = this.ifzPresupuesto.procesarLayout(this.fileSrc, this.layoutPPT);
			if (result.getErrores().getCodigoError() != 0L) {
				log.error("--- ---> Error al realizar PROCESO DE LECTURA DE ARCHIVO");
				bitacora("Comprobando estructura ... ERROR", true); 
				bitacora("Carga fallida ...", false);
				control(true, -1, result.getErrores().getDescError());
				return;
			}
			
			this.listPPTrow = (List<PresupuestoRow>) result.getBody().getValor("procesados");
			bitacora("Comprobando estructura ... OK", true);
    		
			// Generamos Presupuestos
			for (PresupuestoRow var : this.listPPTrow) {
				bitacora("Validando " + var.getSheetName() + " ... ", false);
				if (! var.registroCompleto()) {
					bitacora("Validando " + var.getSheetName() + " ... ERROR", true);
					log.info("--- ---> Validando " + var.getSheetName() + " ... Informacion faltante >>> " + var.informacionFaltante());
					this.listErrores.put(var.getSheetName(), var.informacionFaltante());
					this.listPPTfail.add(var);
					continue;
				}
				
				// Obtenemos datos
				bitacora("Comprobando " + var.getSheetName() + "... ", false);

	    		// Comprobamos PPTs: Obra
				this.pojoObra = null;
				log.info("--- ---> Comprobando Obra ... --> " + var.getIdObra());
				this.pojoObra = this.ifzObras.findById(var.getIdObra());
				if (this.pojoObra == null) {
					bitacora("Comprobando " + var.getSheetName() + " ... ERROR", true);
					log.info("--- ---> Comprobando Obra no valida [" + var.getIdObra() + "]");
					this.listErrores.put(var.getSheetName(), "Obra no valida [" + var.getIdObra() + "]");
					continue;
				}
				
				// Comprobamos PPTs: Presupuesto previo
				log.info("--- ---> Comprobando Presupuesto previo ... ");
				List<Presupuesto> listPPTOAux = this.ifzPresupuesto.findByProperty("idObra.id", var.getIdObra(), 0);
				if (listPPTOAux != null && ! listPPTOAux.isEmpty()) {
					this.pojoPresupuesto = getLastPresupuesto(listPPTOAux);
					if (! actualizarPPT01(var))
						continue;
					/*bitacora("Comprobando " + var.getSheetName() + " ... ERROR", true);
					log.info("--- ---> Comprobando Presupuesto previo [" + listPPTOAux.get(0).getId() + "]");
					this.listErrores.put(var.getSheetName(), "Presupuesto previo [" + listPPTOAux.get(0).getId() + "]");
					continue;*/
				}

				if (this.pojoPresupuesto == null || this.pojoPresupuesto.getId() == null || this.pojoPresupuesto.getId() <= 0L) {
					try {
						log.info("--- ---> Comprobando Conceptos PPTO ...");
						if (this.listConceptosPresupuesto == null)
							this.listConceptosPresupuesto = new ArrayList<ConceptoPresupuesto>();
						this.listConceptosPresupuesto.clear();
				    	
				    	if (this.listDetalles == null)
				    		this.listDetalles = new ArrayList<PresupuestoDetalleExt>();
				    	this.listDetalles.clear();
						
						this.listConceptosPresupuesto = this.ifzConceptosPresupuesto.findAllActivos();
						if (this.listConceptosPresupuesto == null || this.listConceptosPresupuesto.isEmpty()) {
							bitacora("Comprobando " + var.getSheetName() + " ... ERROR", true);
							log.info("--- ---> No se pudo obtener los conceptos de presupuestos");
							this.listErrores.put(var.getSheetName(), "Presupuesto previo [" + listPPTOAux.get(0).getId() + "]");
							continue;
						}
				    	
				    	for (ConceptoPresupuesto con : this.listConceptosPresupuesto) {
				    		PresupuestoDetalleExt detPPTO = new PresupuestoDetalleExt();
				    		detPPTO.setConceptoPresupuesto(con);
				    		detPPTO.setMonto(BigDecimal.ZERO);
				    		detPPTO.setFormula(con.getFormula());
				    		this.listDetalles.add(detPPTO);
				    	}
						log.info("--- ---> Comprobando Conceptos PPTO ... OK");
					} catch (Exception e) {
						bitacora("Comprobando " + var.getSheetName() + " ... ERROR", true);
						log.error("--- ---> ERROR al obtener los conceptos de presupuestos", e);
						this.listErrores.put(var.getSheetName(), "Presupuesto previo [" + listPPTOAux.get(0).getId() + "]");
						continue;
					}
					
		    		// Generamos PPT-01
					try {
						log.info("--- ---> Guardando PPTO " + var.getSheetName() + " ... ");
						this.pojoPresupuesto = new Presupuesto();
		    			this.pojoPresupuesto.setTema(var.getTema().trim());
		    			this.pojoPresupuesto.setRuta(var.getRuta().trim());
		    			this.pojoPresupuesto.setIdObra(this.pojoObra);
						this.pojoPresupuesto.setMontoTotal(this.calcularMontoTotal());
						this.pojoPresupuesto.setNombreArchivo(this.fileName + ">" + var.getSheetName());
						this.pojoPresupuesto.setCreadoPor(this.usuarioId);
		    			this.pojoPresupuesto.setFechaCreacion(Calendar.getInstance().getTime());
			    		this.pojoPresupuesto.setModificadoPor(this.usuarioId);
						this.pojoPresupuesto.setFechaModificacion(Calendar.getInstance().getTime());
						
						// Guardamos en la BD
		    			this.pojoPresupuesto.setId(this.ifzPresupuesto.save(this.pojoPresupuesto));
						lista.add(this.pojoPresupuesto);
						log.info("--- ---> Guardando PPTO " + var.getSheetName() + " ... OK");
					} catch (Exception e) {
						log.error("--- ---> Error Guardando PPTO " + var.getSheetName(), e);
						this.listErrores.put(var.getSheetName(), e.getMessage());
						continue;
					}
	    			
	    			// Guardamos los detalles
					try {
						montoTotal = BigDecimal.ZERO;
		    			for (PresupuestoDetalleExt det : this.listDetalles) {
		    				indexDetails = this.listDetalles.indexOf(det);
		    				det.setIdPresupuesto(this.pojoPresupuesto.getId());det.setCreadoPor(this.usuarioId);
		        			det.setModificadoPor(this.usuarioId);
		        			det.setFechaCreacion(Calendar.getInstance().getTime());
		        			det.setFechaModificacion(Calendar.getInstance().getTime());
	
		        			// Insertamos los valores de concepto obtenidos del EXCEL
	        				det.setMonto(getASBigDecimal(var.getConceptoByIndex(indexDetails).get("J")));
	        				det.setPorcentaje(getAsDouble(var.getConceptoByIndex(indexDetails).get("L")));
	        				det.setPorcentajeTotales(getAsDouble(var.getConceptoByIndex(indexDetails).get("M")));
	        				det.setNotas(var.getConceptoByIndex(indexDetails).get("N"));
		        			montoTotal = montoTotal.add(det.getMonto());
		        			
		        			// Guardamos en la BD
		        			this.ifzPresupuestoDetalle.save(det);
		    				log.info("--- ---> PPTO Concepto " + (indexDetails + 1) + " ... OK");
		        		}
					} catch (Exception e) {
						log.error("--- ---> PPTO Concepto " + indexDetails + " ... ERROR", e);
						this.listErrores.put(var.getSheetName(), e.getMessage());
						continue;
					}
				} else {
					montoTotal = BigDecimal.ZERO;
	    			for (PresupuestoDetalleExt det : this.listDetalles)
	        			montoTotal = montoTotal.add(det.getMonto());
				}
				
				// Actualizo monto total de presupuesto
	    		this.pojoPresupuesto.setModificadoPor(this.usuarioId);
				this.pojoPresupuesto.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoPresupuesto.setMontoTotal(montoTotal);
				this.ifzPresupuesto.update(this.pojoPresupuesto);
    			
				bitacora("Comprobando " + var.getSheetName() + " ... OK", false);
				this.listErrores.put(var.getSheetName(), "OK");
			}
			
			bitacora(lista.size() + " PPTO-01 Guardados", false);
    	} catch (Exception e) {
			log.error("Ocurrio un error en GestionProyectos.PresupuestoAction.dpProcesar", e);
			control(true, 1, e.getMessage(), e);
		}
    }
    
    private boolean actualizarPPT01(PresupuestoRow ppt) {
		BigDecimal montoTotal = BigDecimal.ZERO;
    	int indexDetails = 0;

		try {
    		this.listDetalles = this.ifzPresupuestoDetalle.findByPropertyExt("idPresupuesto", this.pojoPresupuesto.getId(), 0);
    		if (this.listDetalles == null || this.listDetalles.isEmpty())
    			return false;
    		
    		Collections.sort(this.listDetalles, new Comparator<PresupuestoDetalleExt>() {
	    			@Override
	    			public int compare(PresupuestoDetalleExt o1, PresupuestoDetalleExt o2) {
	    				return o1.getConceptoPresupuesto().getId().compareTo(o2.getConceptoPresupuesto().getId());
	    			}
	    	});
    		
			for (PresupuestoDetalleExt det : this.listDetalles) {
				indexDetails = this.listDetalles.indexOf(det);
				det.setIdPresupuesto(this.pojoPresupuesto.getId());
				det.setModificadoPor(this.usuarioId);
				det.setFechaModificacion(Calendar.getInstance().getTime());
	
				// Insertamos los valores de concepto obtenidos del EXCEL
				det.setMonto(getASBigDecimal(ppt.getConceptoByIndex(indexDetails).get("J")));
				det.setPorcentaje(getAsDouble(ppt.getConceptoByIndex(indexDetails).get("L")));
				det.setPorcentajeTotales(getAsDouble(ppt.getConceptoByIndex(indexDetails).get("M")));
				det.setNotas(ppt.getConceptoByIndex(indexDetails).get("N"));
				montoTotal = montoTotal.add(det.getMonto());
				
				// Guardamos en la BD
				this.ifzPresupuestoDetalle.update(det);
				
				log.info("--- ---> PPTO Concepto " + (indexDetails + 1) + " ... OK");
			}
		} catch (Exception e) {
			log.error("--- ---> PPTO Concepto " + indexDetails + " ... ERROR", e);
			return false;
		}
		
    	return true;
    }
    
    private void iniciarBitacora() {
		if (this.bitacora == null)
			this.bitacora = new ArrayList<String>();
		this.bitacora.clear();
		
		if (this.listErrores == null)
			this.listErrores = new LinkedHashMap<String, String>();
		this.listErrores.clear();

		if (this.listPPTrow == null)
			this.listPPTrow = new ArrayList<PresupuestoRow>();
		this.listPPTrow.clear();

		if (this.listPPTfail == null)
			this.listPPTfail = new ArrayList<PresupuestoRow>();
		this.listPPTfail.clear();
	}
	
	private void bitacora(String mensaje, boolean replacePrev) {
		this.bitacora.add(mensaje);
		if (replacePrev) 
			this.bitacora.remove(this.bitacora.size() - 2);
		log.info("---> " + mensaje);
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
	
	public boolean getScrolling() {
		if (this.listDetalles != null && ! this.listDetalles.isEmpty() && this.listDetalles.size() > 9)
			return true;
		return false;
	}
	
	public void setScrolling(boolean value) {}
	
	public String getObra() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L)
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
		return "";
	}
	
	public void setObra(String value) {}
	
	public boolean getPermitirCargaPPT() {
		return (this.isDebug && "ADMINISTRADOR".equals(this.usuario));
	}
	
	public void setPermitirCargaPPT(boolean value) {}
	
	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

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
		return listDetalles;
	}

	public void setListDetalles(List<PresupuestoDetalleExt> listDetalles) {
		this.listDetalles = listDetalles;
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
		if (this.pojoPresupuesto == null) this.pojoPresupuesto = new Presupuesto();
		return this.pojoPresupuesto;
	}

	public void setPojoPresupuesto(Presupuesto pojoPresupuesto) {
		this.pojoPresupuesto = pojoPresupuesto;
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado carga de PPT-01
 */