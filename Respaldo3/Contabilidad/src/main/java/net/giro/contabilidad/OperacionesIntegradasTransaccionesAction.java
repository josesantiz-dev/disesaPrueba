package net.giro.contabilidad;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.Operaciones;
import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;
import net.giro.contabilidad.beans.OperacionesIntegradasSql;
import net.giro.contabilidad.beans.OperacionesIntegradasTransacciones;
import net.giro.contabilidad.beans.TransaccionConceptos;
import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.logica.OperacionesIntegradasConceptosSqlRem;
import net.giro.contabilidad.logica.OperacionesIntegradasSqlRem;
import net.giro.contabilidad.logica.OperacionesIntegradasTransaccionesRem;
import net.giro.contabilidad.logica.OperacionesRem;
import net.giro.contabilidad.logica.TransaccionConceptosRem;
import net.giro.contabilidad.logica.TransaccionesRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="operIntTransAction")
public class OperacionesIntegradasTransaccionesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(OperacionesIntegradasTransaccionesAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	private OperacionesIntegradasTransaccionesRem ifzOperIntegradasTrans;
	private TransaccionConceptosRem ifzTransConceptos;
	// Conceptos SQL
	private OperacionesIntegradasConceptosSqlRem ifzOIConceptosSQL;
	private List<OperacionesIntegradasConceptosSql> listOperIntegradasConceptosSql;
	private OperacionesIntegradasConceptosSql pojoOperIntegradasConceptosSql;
	private int numPaginaOperIntegradasConceptosSql;
	// Campos SQL
	private OperacionesIntegradasSqlRem ifzOICamposSQL;
	private List<OperacionesIntegradasSql> listCamposSQL;
	private OperacionesIntegradasSql pojoCamposSQL;
	private OperacionesIntegradasSql pojoCamposSQLBorrar;
	private int numPaginaCamposSQL;
	// POJO's
	private OperacionesIntegradasTransacciones pojoOperacion;
	private OperacionesIntegradasTransacciones pojoOperacionBorrar;
	// Busqueda principal
	private List<OperacionesIntegradasTransacciones> listOperIntegradasTrans;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Busqueda Operaciones
	private OperacionesRem ifzOperaciones;
	private List<Operaciones> listBusquedaOperaciones;
	private Operaciones pojoOperacionSeleccionada;
	private List<SelectItem> tiposBusquedaOperaciones;	
	private String campoBusquedaOperaciones;
	private String valorBusquedaOperaciones;
	private int numPaginaBusquedaOperaciones;
	// Busqueda Operaciones Integradas
	/*private OperacionesIntegradasRem ifzOperIntegradas;
	private List<OperacionesIntegradas> listBusquedaOperacionesIntegradas;
	private List<SelectItem> tiposBusquedaOperacionesIntegradas;	
	private String campoBusquedaOperacionesIntegradas;
	private String valorBusquedaOperacionesIntegradas;
	private int numPaginaBusquedaOperacionesIntegradas;*/
	// Busqueda Transacciones
	private TransaccionesRem ifzTransacciones;
	private List<Transacciones> listBusquedaTransacciones;
	private Transacciones pojoTransaccionSeleccionada;
	private List<SelectItem> tiposBusquedaTransacciones;
	private String campoBusquedaTransacciones;
	private String valorBusquedaTransacciones;
	private int numPaginaBusquedaTransacciones;
	// Variables de operacion
    private long usuarioId;
    private String usuario;
    private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	
	public OperacionesIntegradasTransaccionesAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			valExp  = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario   = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// ----------------------------------------------------------------------------------
			// Inicializaciones
			// ----------------------------------------------------------------------------------
			
			this.ctx = new InitialContext();
			this.ifzOperIntegradasTrans = (OperacionesIntegradasTransaccionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesIntegradasTransaccionesFac!net.giro.contabilidad.logica.OperacionesIntegradasTransaccionesRem");
			this.ifzOperaciones = (OperacionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesFac!net.giro.contabilidad.logica.OperacionesRem");
			this.ifzTransacciones = (TransaccionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionesFac!net.giro.contabilidad.logica.TransaccionesRem");
			this.ifzTransConceptos = (TransaccionConceptosRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionConceptosFac!net.giro.contabilidad.logica.TransaccionConceptosRem");
			this.ifzOIConceptosSQL = (OperacionesIntegradasConceptosSqlRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesIntegradasConceptosSqlFac!net.giro.contabilidad.logica.OperacionesIntegradasConceptosSqlRem");
			this.ifzOICamposSQL = (OperacionesIntegradasSqlRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesIntegradasSqlFac!net.giro.contabilidad.logica.OperacionesIntegradasSqlRem");

			this.ifzOperIntegradasTrans.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOperaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTransacciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTransConceptos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOIConceptosSQL.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOICamposSQL.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Inicializaciones
			this.pojoOperacion = new OperacionesIntegradasTransacciones();
			this.pojoOperacionBorrar = new OperacionesIntegradasTransacciones();
			this.listOperIntegradasConceptosSql = new ArrayList<OperacionesIntegradasConceptosSql>();
			this.pojoOperIntegradasConceptosSql = new OperacionesIntegradasConceptosSql();
			this.numPaginaOperIntegradasConceptosSql = 1;
			this.listCamposSQL = new ArrayList<OperacionesIntegradasSql>();
			this.pojoCamposSQL = new OperacionesIntegradasSql();
			this.numPaginaCamposSQL = 1;
			
			// Busqueda Principal
			this.listOperIntegradasTrans = new ArrayList<OperacionesIntegradasTransacciones>();
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("idTransaccion.idOperacion.descripcion", "Operacion"));
			this.tiposBusqueda.add(new SelectItem("idTransaccion.descripcion", "Transaccion"));
			this.tiposBusqueda.add(new SelectItem("idTransaccion.codigo", "Codigo"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda Transacciones
			this.tiposBusquedaTransacciones = new ArrayList<SelectItem>();
			this.tiposBusquedaTransacciones.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaTransacciones.add(new SelectItem("coigo", "Codigo"));
			this.tiposBusquedaTransacciones.add(new SelectItem("id", "ID"));
			nuevaBusquedaTransacciones();
			
			// Busqueda Operaciones
			this.tiposBusquedaOperaciones = new ArrayList<SelectItem>();
			this.tiposBusquedaOperaciones.add(new SelectItem("descripcion", "Operacion"));
			this.tiposBusquedaOperaciones.add(new SelectItem("id", "ID"));
			nuevaBusquedaOperaciones();
		} catch (Exception e) {
			log.error("Error en constructor OperacionesIntegradasTransaccionesAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.ifzOperIntegradasTrans.orderBy("id");
			this.listOperIntegradasTrans = this.ifzOperIntegradasTrans.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 1000);
			if (this.listOperIntegradasTrans == null || this.listOperIntegradasTrans.isEmpty()) 
				control(2, "Busqueda vacia");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Operaciones Integradas", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.pojoOperacionBorrar = null;
			this.pojoOperacion = new OperacionesIntegradasTransacciones();
			this.pojoOperIntegradasConceptosSql = new OperacionesIntegradasConceptosSql();
			this.pojoCamposSQL = new OperacionesIntegradasSql();
			
			if (this.listOperIntegradasConceptosSql == null)
				this.listOperIntegradasConceptosSql = new ArrayList<OperacionesIntegradasConceptosSql>();
			this.listOperIntegradasConceptosSql.clear();
			
			if (this.listCamposSQL == null)
				this.listCamposSQL = new ArrayList<OperacionesIntegradasSql>();
			this.listCamposSQL.clear();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar generar una nueva Operacion Integrada", e);
		}
	}

	public void editar() {
		try {
			control();
			if (this.pojoOperacion == null || this.pojoOperacion.getId() == null || this.pojoOperacion.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar la Operacion Integrada indicada");
				return;
			}
			
			if (this.pojoOperacion.getIdOperacion() == null || this.pojoOperacion.getIdOperacion().getId() == null || this.pojoOperacion.getIdOperacion().getId() <= 0L)
				this.pojoOperacion.setIdOperacion(this.pojoOperacion.getIdTransaccion().getIdOperacion());
			
			// Cargamos los Conceptos
			cargarConceptosSQL();
			// Cargamos los Campos
			cargarCamposSQL();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar editar la Operacion Integrada seleccionada", e);
		}
	}

	public void guardar() {
		try {
			control();
			if (this.pojoOperacion != null) {
				this.pojoOperacion.setModificadoPor(this.usuarioId);
				this.pojoOperacion.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Comprobamos que todos los conceptos de la transaccion elegida tendan asignado su consulta SQL
				if (this.listOperIntegradasConceptosSql != null && ! this.listOperIntegradasConceptosSql.isEmpty()) {
					for (OperacionesIntegradasConceptosSql var : this.listOperIntegradasConceptosSql) {
						if (var.getSql().isEmpty()) {
							control(4, "El Concepto '" + var.getIdConcepto().getDescripcion() + "' no tiene consulta asignada");
							return;
						}
					}
				}
				
				if (this.pojoOperacion.getId() == null || this.pojoOperacion.getId() <= 0L) {
					this.pojoOperacion.setCreadoPor(this.usuarioId);
					this.pojoOperacion.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoOperacion.setId(this.ifzOperIntegradasTrans.save(this.pojoOperacion));
					
					// Agregamos a la lista
					this.listOperIntegradasTrans.add(this.pojoOperacion);
				} else {
					// Actualizamos en la BD
					this.ifzOperIntegradasTrans.update(this.pojoOperacion);

					// Actualizamos en la lista
					for (OperacionesIntegradasTransacciones var : this.listOperIntegradasTrans) {
						if (var.getId().equals(this.pojoOperacion.getId())) {
							var.setIdOperacion(this.pojoOperacion.getIdOperacion());
							var.setIdTransaccion(this.pojoOperacion.getIdTransaccion());
							var.setModificadoPor(this.pojoOperacion.getModificadoPor());
							var.setFechaModificacion(this.pojoOperacion.getFechaModificacion());
							break;
						}
					}
				}
				
				// Guardamos los Conceptos con su consulta SQL respectiva en OperacionIntegradasConceptosSql
				if (this.listOperIntegradasConceptosSql != null && ! this.listOperIntegradasConceptosSql.isEmpty()) {
					for (OperacionesIntegradasConceptosSql var : this.listOperIntegradasConceptosSql) {
						
						// Guardamos en la BD
						if (var.getId() == null || var.getId() <= 0L) {
							var.setIdOperacionIntegrada(this.pojoOperacion);
							var.setModificadoPor(this.pojoOperacion.getModificadoPor());
							var.setFechaModificacion(this.pojoOperacion.getFechaModificacion());
							var.setCreadoPor(this.pojoOperacion.getModificadoPor());
							var.setFechaCreacion(this.pojoOperacion.getFechaModificacion());
							
							var.setId(this.ifzOIConceptosSQL.save(var));
						}
					}
				}
			}
			
			nuevo();
		} catch (Exception e) {
			control("Ocurrio un problema al guardar la Operacion Intgrada Transaccion", e);
		}
	}
	
	public void borrar() {
		try {
			control();
			if (this.pojoOperacionBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoOperacionBorrar.getId() != null && this.pojoOperacionBorrar.getId() > 0L)
					this.ifzOperIntegradasTrans.delete(this.pojoOperacionBorrar.getId());
				
				// Borramos de la lista
				this.listOperIntegradasTrans.remove(this.pojoOperacionBorrar);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Eliminar la Operacion Integrada Transaccion seleccionada", e);
		}
	}

	private void cargarConceptosSQL() throws Exception {
		if (this.listOperIntegradasConceptosSql == null)
			this.listOperIntegradasConceptosSql = new ArrayList<OperacionesIntegradasConceptosSql>();
		this.listOperIntegradasConceptosSql.clear();
		this.pojoOperIntegradasConceptosSql = new OperacionesIntegradasConceptosSql();
		
		// Cargamos los datos
		this.listOperIntegradasConceptosSql = this.ifzOIConceptosSQL.findByOperacionIntegrada(this.pojoOperacion.getId());
		if (this.listOperIntegradasConceptosSql != null && ! this.listOperIntegradasConceptosSql.isEmpty()) {
			Collections.sort(this.listOperIntegradasConceptosSql, new Comparator<OperacionesIntegradasConceptosSql>() {
				@Override
				public int compare(OperacionesIntegradasConceptosSql o1, OperacionesIntegradasConceptosSql o2) {
					return o1.getId().compareTo(o2.getId());
				}
			});
		}
	}

	private void cargarCamposSQL() throws Exception {
		if (this.listCamposSQL == null)
			this.listCamposSQL = new ArrayList<OperacionesIntegradasSql>();
		this.listCamposSQL.clear();
		this.pojoCamposSQL = new OperacionesIntegradasSql();
		
		// Cargamos los datos
		this.listCamposSQL = this.ifzOICamposSQL.findByOperacionIntegrada(this.pojoOperacion.getId());
		if (this.listCamposSQL != null && ! this.listCamposSQL.isEmpty()) {
			Collections.sort(this.listCamposSQL, new Comparator<OperacionesIntegradasSql>() {
				@Override
				public int compare(OperacionesIntegradasSql o1, OperacionesIntegradasSql o2) {
					return o1.getId().compareTo(o2.getId());
				}
			});
		}
	}
	
	private void control() {
		this.operacion = false;
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

		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		codigo = "Ex" + formatter.format(Calendar.getInstance().getTime());
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
		}
		
		log.error("\n\nEQUIVALENCIA IMPUESTOS :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n", throwable);
	}

	// ------------------------------------------------------------------
	// OPERACIONES INTEGRADAS
	// ------------------------------------------------------------------
	
	public void nuevaBusquedaOperacionesIntegradas() {
	}
	
	public void buscarOperacionesIntegradas() {
		try {
			control();
			control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar las Operaciones", e);
		}
	}
	
	public void seleccionarOperacionIntegradas() {
		try {
			control();
			nuevaBusquedaOperaciones();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Operacion seleccionada", e);
		}
	}

	// ------------------------------------------------------------------
	// TRANSACCIONES
	// ------------------------------------------------------------------
	
	public void nuevaBusquedaTransacciones() {
		if (this.listBusquedaTransacciones == null)
			this.listBusquedaTransacciones = new ArrayList<Transacciones>();
		this.listBusquedaTransacciones.clear();
		this.pojoTransaccionSeleccionada = null;
		this.campoBusquedaTransacciones = this.tiposBusquedaTransacciones.get(0).getValue().toString();
		this.valorBusquedaTransacciones = "";
		this.numPaginaBusquedaTransacciones = 1;
	}
	
	public void buscarTransacciones() {
		try {
			control();
			if ("".equals(this.campoBusquedaTransacciones))
				this.campoBusquedaTransacciones = this.tiposBusquedaTransacciones.get(0).getValue().toString();
			
			this.listBusquedaTransacciones = this.ifzTransacciones.findLikeProperty(this.campoBusquedaTransacciones, this.valorBusquedaTransacciones, 120);
			if (this.listBusquedaTransacciones == null || this.listBusquedaTransacciones.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Transacciones", e);
		}
	}
	
	public void seleccionarTransaccion() {
		List<TransaccionConceptos> lista = null;
		OperacionesIntegradasConceptosSql pojoAux = null;
		
		try {
			control();
			if (this.pojoTransaccionSeleccionada == null || this.pojoTransaccionSeleccionada.getId() == null || this.pojoTransaccionSeleccionada.getId() <= 0L) {
				control(-1, "Debe seleccionar una Transaccion");
				return;
			}
			
			lista = this.ifzTransConceptos.findByTransaccion(this.pojoTransaccionSeleccionada.getId());
			if (lista == null || lista.isEmpty()) {
				control(-1, "La Transaccion seleccionada no tiene asignado ningun concepto");
				return;
			}

			this.pojoOperacion.setIdTransaccion(this.pojoTransaccionSeleccionada);
			this.pojoOperacion.setIdOperacion(this.pojoTransaccionSeleccionada.getIdOperacion());
			if (this.listOperIntegradasConceptosSql == null)
				this.listOperIntegradasConceptosSql = new ArrayList<OperacionesIntegradasConceptosSql>();
			this.listOperIntegradasConceptosSql.clear();
			
			for (TransaccionConceptos var : lista) {
				// Generamos la Operacion-Integrada-Concepto
				pojoAux = new OperacionesIntegradasConceptosSql();
				pojoAux.setIdOperacionIntegrada(this.pojoOperacion);
				pojoAux.setIdConcepto(var.getIdConcepto());
				pojoAux.setSql("");
				pojoAux.setCreadoPor(this.usuarioId);
				pojoAux.setFechaCreacion(Calendar.getInstance().getTime());
				pojoAux.setModificadoPor(this.usuarioId);
				pojoAux.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Añadimos a listado
				this.listOperIntegradasConceptosSql.add(pojoAux);
			}
			
			nuevaBusquedaTransacciones();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Transaccion seleccionada", e);
		}
	}

	// ------------------------------------------------------------------
	// OPERACIONES 
	// ------------------------------------------------------------------
	
	public void nuevaBusquedaOperaciones() {
		if (this.listBusquedaOperaciones == null)
			this.listBusquedaOperaciones = new ArrayList<Operaciones>();
		this.listBusquedaOperaciones.clear();
		this.campoBusquedaOperaciones = this.tiposBusquedaOperaciones.get(0).getValue().toString();
		this.valorBusquedaOperaciones = "";
		this.numPaginaBusquedaOperaciones = 1;
	}
	
	public void buscarOperaciones() {
		try {
			control();
			if (this.campoBusquedaOperaciones == null || "".equals(this.campoBusquedaOperaciones))
				this.campoBusquedaOperaciones = this.tiposBusquedaOperaciones.get(0).getValue().toString();
			
			this.listBusquedaOperaciones = this.ifzOperaciones.findLikeProperty(this.campoBusquedaOperaciones, this.valorBusquedaOperaciones, 120);
			if (this.listBusquedaOperaciones == null || this.listBusquedaOperaciones.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar las Operaciones", e);
		}
	}
	
	public void seleccionarOperacion() {
		try {
			control();
			if (this.pojoOperacionSeleccionada == null || this.pojoOperacionSeleccionada.getId() == null || this.pojoOperacionSeleccionada.getId() <= 0L) {
				control(-1, "Debe seleccionar una Operacion");
				return;
			}
			
			this.pojoOperacion.setIdOperacion(this.pojoOperacionSeleccionada);
			nuevaBusquedaOperaciones();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Operacion seleccionada", e);
		}
	}

	// --------------------------------------------------------------------------------------
	// CAMPOS
	// --------------------------------------------------------------------------------------

	public void aplicarOperacionIntegradaConceptoSQL() {
		int indexConceptoSQL = -1;
		
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoOperIntegradasConceptosSql != null && this.pojoOperIntegradasConceptosSql.getSql().isEmpty()) {
				this.operacion = true;
				this.mensaje = this.pojoOperIntegradasConceptosSql.getIdConcepto().getDescripcion();
				this.tipoMensaje = 4;
				log.info("Busqueda vacia");
				return;
			}
			
			indexConceptoSQL = this.listOperIntegradasConceptosSql.indexOf(this.pojoOperIntegradasConceptosSql);
			this.pojoOperIntegradasConceptosSql.setIdOperacionIntegrada(this.pojoOperacion);
			this.pojoOperIntegradasConceptosSql.setModificadoPor(this.pojoOperacion.getModificadoPor());
			this.pojoOperIntegradasConceptosSql.setFechaModificacion(this.pojoOperacion.getFechaModificacion());
			
			// Guardamos en la BD si corresponde
			if (this.pojoOperacion.getId() != null && pojoOperacion.getId() > 0L) {
				if (this.pojoOperIntegradasConceptosSql.getId() == null || this.pojoOperIntegradasConceptosSql.getId() <= 0L) {
					this.pojoOperIntegradasConceptosSql.setCreadoPor(this.pojoOperacion.getModificadoPor());
					this.pojoOperIntegradasConceptosSql.setFechaCreacion(this.pojoOperacion.getFechaModificacion());
					
					// Guardamos y asignamos ID
					this.pojoOperIntegradasConceptosSql.setId(this.ifzOIConceptosSQL.save(this.pojoOperIntegradasConceptosSql));
				} else {
					this.ifzOIConceptosSQL.update(this.pojoOperIntegradasConceptosSql);
				}
			
				cargarConceptosSQL();
			} else {
				this.pojoOperIntegradasConceptosSql.setCreadoPor(this.pojoOperacion.getModificadoPor());
				this.pojoOperIntegradasConceptosSql.setFechaCreacion(this.pojoOperacion.getFechaModificacion());
				
				if (indexConceptoSQL == -1)
					this.listOperIntegradasConceptosSql.add(this.pojoOperIntegradasConceptosSql);
				else
					this.listOperIntegradasConceptosSql.set(indexConceptoSQL, this.pojoOperIntegradasConceptosSql);
				
				this.pojoOperIntegradasConceptosSql = new OperacionesIntegradasConceptosSql();
			}
		} catch (Exception e) {
			log.error("Error en OperacionesIntegradasTransaccionesAction.aplicarOperacionIntegradaConceptoSQL", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}

	public void nuevaOperacionIntegradaSQL() {
		try {
			control();
			this.pojoCamposSQLBorrar = null;
			this.pojoCamposSQL = new OperacionesIntegradasSql();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar inicializar un Campo nuevo", e);
		}
	}
	
	public void salvarOperacionIntegradaSQL() {
		try {
			control();
			if (this.pojoCamposSQL == null)
				this.pojoCamposSQL = new OperacionesIntegradasSql();
			
			if (this.pojoCamposSQL.getCampo() == null || "".equals(this.pojoCamposSQL.getCampo().trim())) {
				control(-1, "Debe indicar el Campo");
				return;
			}
			
			if (this.pojoCamposSQL.getSql() == null || "".equals(this.pojoCamposSQL.getSql().trim())) {
				control(-1, "Debe indicar la consulta SQL");
				return;
			}

			this.pojoCamposSQL.setIdOperacionIntegrada(this.pojoOperacion);
			this.pojoCamposSQL.setCampo(this.pojoCamposSQL.getCampo().trim());
			this.pojoCamposSQL.setSql(this.pojoCamposSQL.getSql().trim());
			
			// Guardamos en la BD segun corresponda
			if (this.pojoCamposSQL.getId() == null || this.pojoCamposSQL.getId() <= 0L) {
				this.pojoCamposSQL.setCreadoPor(this.usuarioId);
				this.pojoCamposSQL.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoCamposSQL.setModificadoPor(this.usuarioId);
				this.pojoCamposSQL.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoCamposSQL.setId(this.ifzOICamposSQL.save(this.pojoCamposSQL));
			} else {
				this.pojoCamposSQL.setModificadoPor(this.usuarioId);
				this.pojoCamposSQL.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzOICamposSQL.update(this.pojoCamposSQL);
			}
			
			nuevaOperacionIntegradaSQL();
			cargarCamposSQL();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar el Campo", e);
		}
	}
	
	public void borrarOperacionIntegradaSQL() {
		try {
			control();
			if (this.pojoCamposSQLBorrar == null) {
				control(-1, "Debe indicar un Campo para eliminar");
				return;
			}

			// Borramos de la BD si corresponde
			if (this.pojoCamposSQLBorrar.getId() != null && this.pojoCamposSQLBorrar.getId() > 0L)
				this.ifzOICamposSQL.delete(this.pojoCamposSQLBorrar.getId());
			this.listCamposSQL.remove(this.pojoCamposSQLBorrar);
			this.pojoCamposSQLBorrar = null;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Campo indicado", e);
		}
	}
	
	// --------------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------------

	public String getTransaccion() {
		if (this.pojoOperacion != null && this.pojoOperacion.getIdTransaccion() != null && this.pojoOperacion.getIdTransaccion().getId() != null && this.pojoOperacion.getIdTransaccion().getId() > 0L)
			return this.pojoOperacion.getIdTransaccion().getId() + " - " + this.pojoOperacion.getIdTransaccion().getDescripcion();
		return "";
	}
	
	public void setTransaccion(String value) {}
	
	public String getIdOperacion() {
		if (this.pojoOperacion != null && this.pojoOperacion.getIdOperacion() != null && this.pojoOperacion.getIdOperacion().getId() != null && this.pojoOperacion.getIdOperacion().getId() > 0L)
			return this.pojoOperacion.getIdOperacion().getId() + " - " + this.pojoOperacion.getIdOperacion().getDescripcion();
		return "";
	}
	
	public void setIdOperacion(String value) {}
	
	public String getConceptoDescripcion() {
		if (this.pojoOperIntegradasConceptosSql != null && this.pojoOperIntegradasConceptosSql.getIdConcepto() != null)
			return this.pojoOperIntegradasConceptosSql.getIdConcepto().getId() + " - " + this.pojoOperIntegradasConceptosSql.getIdConcepto().getDescripcion();
		return "";
	}
	
	public void setConceptoDescripcion(String value) {}
	
	public String getOperIntegradaTransaccionDescripcion() {
		if (this.pojoOperacion == null)
			return "";
		if (this.pojoOperacion.getIdOperacion() == null || this.pojoOperacion.getIdOperacion() == null)
			return "";
		if (this.pojoOperacion.getIdTransaccion() == null)
			return "";
		
		return this.pojoOperacion.getIdOperacion().getDescripcion() + " - " + this.pojoOperacion.getIdTransaccion().getDescripcion();
	}
	
	public void setOperIntegradaTransaccionDescripcion(String value) {}
	
	public String getTitulo() {
		if (this.pojoOperacion != null && this.pojoOperacion.getId() != null && this.pojoOperacion.getId() > 0L)
			return " " + this.pojoOperacion.getId();
		return "";
	}
	
	public void setTitulo(String value) {}
	
	public List<OperacionesIntegradasTransacciones> getListOperIntegradasTrans() {
		return listOperIntegradasTrans;
	}

	public void setListOperIntegradasTrans(List<OperacionesIntegradasTransacciones> listOperIntegradasTrans) {
		this.listOperIntegradasTrans = listOperIntegradasTrans;
	}

	public OperacionesIntegradasTransacciones getPojoOperacion() {
		return pojoOperacion;
	}

	public void setPojoOperacion(OperacionesIntegradasTransacciones pojoOperacion) {
		this.pojoOperacion = pojoOperacion;
	}

	public OperacionesIntegradasTransacciones getPojoOperacionBorrar() {
		return pojoOperacionBorrar;
	}

	public void setPojoOperacionBorrar(OperacionesIntegradasTransacciones pojOperacionBorrar) {
		this.pojoOperacionBorrar = pojOperacionBorrar;
	}

	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public boolean isOperacion() {
		return operacion;
	}

	public void setOperacion(boolean operacion) {
		this.operacion = operacion;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
	
	public List<Transacciones> getListBusquedaTransacciones() {
		return listBusquedaTransacciones;
	}

	public void setListBusquedaTransacciones(List<Transacciones> listBusquedaTransacciones) {
		this.listBusquedaTransacciones = listBusquedaTransacciones;
	}

	public Transacciones getPojoTransaccionSeleccionada() {
		return pojoTransaccionSeleccionada;
	}

	public void setPojoTransaccionSeleccionada(Transacciones pojoTransaccionSeleccionada) {
		this.pojoTransaccionSeleccionada = pojoTransaccionSeleccionada;
	}

	public List<SelectItem> getTiposBusquedaTransacciones() {
		return tiposBusquedaTransacciones;
	}

	public void setTiposBusquedaTransacciones(List<SelectItem> tiposBusquedaTransacciones) {
		this.tiposBusquedaTransacciones = tiposBusquedaTransacciones;
	}

	public String getCampoBusquedaTransacciones() {
		return campoBusquedaTransacciones;
	}

	public void setCampoBusquedaTransacciones(String campoBusquedaTransacciones) {
		this.campoBusquedaTransacciones = campoBusquedaTransacciones;
	}

	public String getValorBusquedaTransacciones() {
		return valorBusquedaTransacciones;
	}

	public void setValorBusquedaTransacciones(String valorBusquedaTransacciones) {
		this.valorBusquedaTransacciones = valorBusquedaTransacciones;
	}

	public int getNumPaginaBusquedaTransacciones() {
		return numPaginaBusquedaTransacciones;
	}

	public void setNumPaginaBusquedaTransacciones(int numPaginaBusquedaTransacciones) {
		this.numPaginaBusquedaTransacciones = numPaginaBusquedaTransacciones;
	}
	
	public List<OperacionesIntegradasConceptosSql> getListOperIntegradasConceptosSql() {
		return listOperIntegradasConceptosSql;
	}

	public void setListOperIntegradasConceptosSql(List<OperacionesIntegradasConceptosSql> listOperIntegradasConceptosSql) {
		this.listOperIntegradasConceptosSql = listOperIntegradasConceptosSql;
	}

	public int getNumPaginaOperIntegradasConceptosSql() {
		return numPaginaOperIntegradasConceptosSql;
	}

	public void setNumPaginaOperIntegradasConceptosSql(int numPaginaOperIntegradasConceptosSql) {
		this.numPaginaOperIntegradasConceptosSql = numPaginaOperIntegradasConceptosSql;
	}

	public OperacionesIntegradasConceptosSql getPojoOperIntegradasConceptosSql() {
		return pojoOperIntegradasConceptosSql;
	}

	public void setPojoOperIntegradasConceptosSql(OperacionesIntegradasConceptosSql pojoOperIntegradasConceptosSql) {
		if (pojoOperIntegradasConceptosSql.getSql().isEmpty())
			pojoOperIntegradasConceptosSql.setSql("");
		this.pojoOperIntegradasConceptosSql = pojoOperIntegradasConceptosSql;
	}

	public List<OperacionesIntegradasSql> getListCamposSQL() {
		return listCamposSQL;
	}
	
	public void setListCamposSQL(List<OperacionesIntegradasSql> listCamposSQL) {
		this.listCamposSQL = listCamposSQL;
	}

	public OperacionesIntegradasSql getPojoCampoSQL() {
		return pojoCamposSQL;
	}

	public void setPojoCampoSQL(OperacionesIntegradasSql pojoCampoSQL) {
		this.pojoCamposSQL = pojoCampoSQL;
	}

	public OperacionesIntegradasSql getPojoCampoSQLBorrar() {
		return pojoCamposSQLBorrar;
	}

	public void setPojoCampoSQLBorrar(OperacionesIntegradasSql pojoCampoSQLBorrar) {
		this.pojoCamposSQLBorrar = pojoCampoSQLBorrar;
	}

	public int getNumPaginaCamposSQL() {
		return numPaginaCamposSQL;
	}

	public void setNumPaginaCamposSQL(int numPaginaCamposSQL) {
		this.numPaginaCamposSQL = numPaginaCamposSQL;
	}
	
	public List<Operaciones> getListBusquedaOperaciones() {
		return listBusquedaOperaciones;
	}
	
	public void setListBusquedaOperaciones(List<Operaciones> listBusquedaOperaciones) {
		this.listBusquedaOperaciones = listBusquedaOperaciones;
	}

	public Operaciones getPojoOperacionSeleccionada() {
		return pojoOperacionSeleccionada;
	}

	public void setPojoOperacionSeleccionada(Operaciones pojoOperacionSeleccionada) {
		this.pojoOperacionSeleccionada = pojoOperacionSeleccionada;
	}

	public List<SelectItem> getTiposBusquedaOperaciones() {
		return tiposBusquedaOperaciones;
	}

	public void setTiposBusquedaOperaciones(List<SelectItem> tiposBusquedaOperaciones) {
		this.tiposBusquedaOperaciones = tiposBusquedaOperaciones;
	}

	public String getCampoBusquedaOperaciones() {
		return campoBusquedaOperaciones;
	}

	public void setCampoBusquedaOperaciones(String campoBusquedaOperaciones) {
		this.campoBusquedaOperaciones = campoBusquedaOperaciones;
	}

	public String getValorBusquedaOperaciones() {
		return valorBusquedaOperaciones;
	}

	public void setValorBusquedaOperaciones(String valorBusquedaOperaciones) {
		this.valorBusquedaOperaciones = valorBusquedaOperaciones;
	}

	public int getNumPaginaBusquedaOperaciones() {
		return numPaginaBusquedaOperaciones;
	}

	public void setNumPaginaBusquedaOperaciones(int numPaginaBusquedaOperaciones) {
		this.numPaginaBusquedaOperaciones = numPaginaBusquedaOperaciones;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
}
