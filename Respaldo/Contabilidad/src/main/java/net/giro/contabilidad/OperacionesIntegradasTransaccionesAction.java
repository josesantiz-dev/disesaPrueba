package net.giro.contabilidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.OperacionesIntegradas;
import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;
import net.giro.contabilidad.beans.OperacionesIntegradasSql;
import net.giro.contabilidad.beans.OperacionesIntegradasTransacciones;
import net.giro.contabilidad.beans.TransaccionConceptos;
import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.logica.OperacionesIntegradasConceptosSqlRem;
import net.giro.contabilidad.logica.OperacionesIntegradasRem;
import net.giro.contabilidad.logica.OperacionesIntegradasSqlRem;
import net.giro.contabilidad.logica.OperacionesIntegradasTransaccionesRem;
import net.giro.contabilidad.logica.TransaccionConceptosRem;
import net.giro.contabilidad.logica.TransaccionesRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="operIntTransAction")
public class OperacionesIntegradasTransaccionesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(OperacionesIntegradasTransaccionesAction.class);
	private InitialContext ctx;
	
	// Interfaces
	private OperacionesIntegradasTransaccionesRem ifzOperIntegradasTrans;
	private OperacionesIntegradasRem ifzOperIntegradas;
	private TransaccionesRem ifzTransacciones;
	private TransaccionConceptosRem ifzTransConceptos;
	private OperacionesIntegradasConceptosSqlRem ifzOperIntegradasConceptosSQL;
	private OperacionesIntegradasSqlRem ifzOperIntegradasSQL;
	
	// Operaciones Integradas Conceptos SQL
	private List<OperacionesIntegradasConceptosSql> listOperIntegradasConceptosSql;
	private OperacionesIntegradasConceptosSql pojoOperIntegradasConceptosSql;
	private int numPaginaOperIntegradasConceptosSql;
	
	// Operaciones Integradas SQL
	private List<OperacionesIntegradasSql> listOperIntegradasSQL;
	private OperacionesIntegradasSql pojoOperIntegradaSQL;
	private OperacionesIntegradasSql pojoOperIntegradaSQLBorrar;
	private int numPaginaOperIntegradasSQL;

	// POJO's
	private OperacionesIntegradasTransacciones pojoOperacion;
	private OperacionesIntegradasTransacciones pojoOperacionBorrar;
	
	// Busqueda principal
	private List<OperacionesIntegradasTransacciones> listOperIntegradasTrans;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Busqueda Operaciones Integradas
	private List<OperacionesIntegradas> listBusquedaOperacionesIntegradas;
	private List<SelectItem> tiposBusquedaOperacionesIntegradas;	
	private String campoBusquedaOperacionesIntegradas;
	private String valorBusquedaOperacionesIntegradas;
	private int numPaginaBusquedaOperacionesIntegradas;
	// Busqueda Transacciones
	private List<Transacciones> listBusquedaTransacciones;
	private List<SelectItem> tiposBusquedaTransacciones;
	private String campoBusquedaTransacciones;
	private String valorBusquedaTransacciones;
	private int numPaginaBusquedaTransacciones;
	// Variables de operacion
    private long usuarioId;
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	
	public OperacionesIntegradasTransaccionesAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		LoginManager loginManager = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			valExp  = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			loginManager = (LoginManager) valExp.getValue(fc.getELContext());
	
			this.usuarioId = loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
			Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// ----------------------------------------------------------------------------------
			// Inicializaciones
			// ----------------------------------------------------------------------------------
			
			this.ctx = new InitialContext();
			this.ifzOperIntegradasTrans = (OperacionesIntegradasTransaccionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesIntegradasTransaccionesFac!net.giro.contabilidad.logica.OperacionesIntegradasTransaccionesRem");
			this.ifzOperIntegradas = (OperacionesIntegradasRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesIntegradasFac!net.giro.contabilidad.logica.OperacionesIntegradasRem");
			this.ifzTransacciones = (TransaccionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionesFac!net.giro.contabilidad.logica.TransaccionesRem");
			this.ifzTransConceptos = (TransaccionConceptosRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionConceptosFac!net.giro.contabilidad.logica.TransaccionConceptosRem");
			this.ifzOperIntegradasConceptosSQL = (OperacionesIntegradasConceptosSqlRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesIntegradasConceptosSqlFac!net.giro.contabilidad.logica.OperacionesIntegradasConceptosSqlRem");
			this.ifzOperIntegradasSQL = (OperacionesIntegradasSqlRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesIntegradasSqlFac!net.giro.contabilidad.logica.OperacionesIntegradasSqlRem");

			this.ifzTransConceptos.setInfoSesion(loginManager.getInfoSesion());
			this.ifzTransConceptos.showSystemOuts(false);
			this.ifzTransacciones.setInfoSesion(loginManager.getInfoSesion());
			this.ifzTransacciones.showSystemOuts(false);
			this.ifzOperIntegradas.setInfoSesion(loginManager.getInfoSesion());
			this.ifzOperIntegradas.showSystemOuts(false);
			this.ifzOperIntegradasTrans.setInfoSesion(loginManager.getInfoSesion());
			this.ifzOperIntegradasTrans.showSystemOuts(false);
			this.ifzOperIntegradasConceptosSQL.setInfoSesion(loginManager.getInfoSesion());
			this.ifzOperIntegradasConceptosSQL.showSystemOuts(false);
			
			// POJO's
			this.pojoOperacion = new OperacionesIntegradasTransacciones();
			this.pojoOperacionBorrar = new OperacionesIntegradasTransacciones();
			
			// Busqueda Principal
			this.listOperIntegradasTrans = new ArrayList<OperacionesIntegradasTransacciones>();
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("idOperacionIntegrada.idOperacion.descripcion", "Operacion"));
			this.tiposBusqueda.add(new SelectItem("idTransaccion.descripcion", "Transaccion"));
			this.tiposBusqueda.add(new SelectItem("idTransaccion.codigo", "Codigo"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			this.listOperIntegradasConceptosSql = new ArrayList<OperacionesIntegradasConceptosSql>();
			this.pojoOperIntegradasConceptosSql = new OperacionesIntegradasConceptosSql();
			this.numPaginaOperIntegradasConceptosSql = 1;
			
			this.listOperIntegradasSQL = new ArrayList<OperacionesIntegradasSql>();
			this.pojoOperIntegradaSQL = new OperacionesIntegradasSql();
			this.numPaginaOperIntegradasSQL = 1;
			
			// Busqueda Operaciones Integradas
			this.listBusquedaOperacionesIntegradas = new ArrayList<OperacionesIntegradas>();
			this.tiposBusquedaOperacionesIntegradas = new ArrayList<SelectItem>();
			this.tiposBusquedaOperacionesIntegradas.add(new SelectItem("idOperacion.descripcion", "Operacion"));
			this.tiposBusquedaOperacionesIntegradas.add(new SelectItem("id", "ID"));
			this.campoBusquedaOperacionesIntegradas = this.tiposBusquedaOperacionesIntegradas.get(0).getValue().toString();
			this.valorBusquedaOperacionesIntegradas = "";
			this.numPaginaBusquedaOperacionesIntegradas = 1;
			
			// Busqueda Transacciones
			this.listBusquedaTransacciones = new ArrayList<Transacciones>();
			this.tiposBusquedaTransacciones = new ArrayList<SelectItem>();
			this.tiposBusquedaTransacciones.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaTransacciones.add(new SelectItem("id", "ID"));
			this.campoBusquedaTransacciones = this.tiposBusquedaTransacciones.get(0).getValue().toString();
			this.valorBusquedaTransacciones = "";
			this.numPaginaBusquedaTransacciones = 1;
		} catch (Exception e) {
			log.error("Error en constructor OperacionesIntegradasTransaccionesAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			
			this.ifzOperIntegradasTrans.orderBy("idTransaccion.codigo");
			this.listOperIntegradasTrans = this.ifzOperIntegradasTrans.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 1000);
			if (this.listOperIntegradasTrans.isEmpty()) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				log.info("Busqueda vacia");
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.buscar", e);
		}
	}
	
	public void nuevo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoOperacionBorrar = null;
			this.pojoOperacion = new OperacionesIntegradasTransacciones();
			this.pojoOperIntegradasConceptosSql = new OperacionesIntegradasConceptosSql();
			this.pojoOperIntegradaSQL = new OperacionesIntegradasSql();
			
			if (this.listOperIntegradasConceptosSql == null)
				this.listOperIntegradasConceptosSql = new ArrayList<OperacionesIntegradasConceptosSql>();
			this.listOperIntegradasConceptosSql.clear();
			
			if (this.listOperIntegradasSQL == null)
				this.listOperIntegradasSQL = new ArrayList<OperacionesIntegradasSql>();
			this.listOperIntegradasSQL.clear();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.nuevo", e);
		}
	}
	
	public void editar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			String editFor = "";
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
			
			if (params.containsKey("editFor")) {
				editFor = params.get("editFor");
				if (editFor == null) editFor = "";
			}
			
			if ("OperacionIntegradaTransaccion".equals(editFor)) {
				cargarOperacionIntegradasConceptosSQL();
			} else if ("OperacionIntegradasSQL".equals(editFor)) {
				cargarOperacionIntegradasSQL();
			} else {
				cargarOperacionIntegradasConceptosSQL();
				cargarOperacionIntegradasSQL();
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.nuevo", e);
		}
	}
	
	public void guardar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoOperacion != null) {
				this.pojoOperacion.setModificadoPor(this.usuarioId);
				this.pojoOperacion.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Comprobamos que todos los conceptos de la transaccion elegida tendan asignado su consulta SQL
				if (this.listOperIntegradasConceptosSql != null && ! this.listOperIntegradasConceptosSql.isEmpty()) {
					for (OperacionesIntegradasConceptosSql var : this.listOperIntegradasConceptosSql) {
						if (var.getSql().isEmpty()) {
							this.operacion = true;
							this.mensaje = var.getIdConcepto().getDescripcion();
							this.tipoMensaje = 4;
							log.error("Error: Concepto '" + this.mensaje + "' sin SQL asignado");
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
							var.setIdOperacionIntegrada(this.pojoOperacion.getIdOperacionIntegrada());
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
							var.setIdOperacionIntegradaTransaccion(this.pojoOperacion);
							var.setModificadoPor(this.pojoOperacion.getModificadoPor());
							var.setFechaModificacion(this.pojoOperacion.getFechaModificacion());
							var.setCreadoPor(this.pojoOperacion.getModificadoPor());
							var.setFechaCreacion(this.pojoOperacion.getFechaModificacion());
							
							var.setId(this.ifzOperIntegradasConceptosSQL.save(var));
						} /*else {
							this.ifzOperIntConceptosSql.update(var);
						}*/
					}
				}
			}
			
			nuevo();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.guardar", e);
		}
	}
	
	public void borrar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoOperacionBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoOperacionBorrar.getId() != null && this.pojoOperacionBorrar.getId() > 0L)
					this.ifzOperIntegradasTrans.delete(this.pojoOperacionBorrar.getId());
				
				// Borramos de la lista
				this.listOperIntegradasTrans.remove(this.pojoOperacionBorrar);
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.borrar", e);
		}
	}

	// ------------------------------------------------------------------
	// OPERACIONES INTEGRADAS
	// ------------------------------------------------------------------
	
	public void nuevaBusquedaOperacionesIntegradas() {
		if (this.listBusquedaOperacionesIntegradas == null)
			this.listBusquedaOperacionesIntegradas = new ArrayList<OperacionesIntegradas>();
		this.listBusquedaOperacionesIntegradas.clear();
		
		this.campoBusquedaOperacionesIntegradas = this.tiposBusquedaOperacionesIntegradas.get(0).getValue().toString();
		this.valorBusquedaOperacionesIntegradas = "";
		this.numPaginaBusquedaOperacionesIntegradas = 1;
	}
	
	public void buscarOperacionesIntegradas() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaOperacionesIntegradas))
				this.campoBusquedaOperacionesIntegradas = this.tiposBusquedaOperacionesIntegradas.get(0).getValue().toString();
			
			this.listBusquedaOperacionesIntegradas = this.ifzOperIntegradas.findLikeProperty(this.campoBusquedaOperacionesIntegradas, this.valorBusquedaOperacionesIntegradas, 120);
			if (this.listBusquedaOperacionesIntegradas == null || this.listBusquedaOperacionesIntegradas.isEmpty()) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				log.info("Busqueda vacia");
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.buscarOperacionesIntegradas", e);
		}
	}
	
	public void seleccionarOperacionIntegrada() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			nuevaBusquedaOperacionesIntegradas();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.seleccionarOperacionIntegrada", e);
		}
	}

	// ------------------------------------------------------------------
	// TRANSACCIONES
	// ------------------------------------------------------------------
	
	public void nuevaBusquedaTransacciones() {
		if (this.listBusquedaTransacciones == null)
			this.listBusquedaTransacciones = new ArrayList<Transacciones>();
		this.listBusquedaTransacciones.clear();
		
		this.campoBusquedaTransacciones = tiposBusquedaTransacciones.get(0).getValue().toString();
		this.valorBusquedaTransacciones = "";
		numPaginaBusquedaTransacciones = 1;
	}
	
	public void buscarTransacciones() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaTransacciones))
				this.campoBusquedaTransacciones = tiposBusquedaTransacciones.get(0).getValue().toString();
			
			this.listBusquedaTransacciones = this.ifzTransacciones.findLikeProperty(this.campoBusquedaTransacciones, this.valorBusquedaTransacciones, 120);
			if (this.listBusquedaTransacciones == null || this.listBusquedaTransacciones.isEmpty()) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				log.info("Busqueda vacia");
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.buscarTransacciones", e);
		}
	}
	
	public void seleccionarTransaccion() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			// Recuperamos los conceptos de la transaccion si corresponde
			if (this.pojoOperacion.getIdTransaccion() != null) {
				List<TransaccionConceptos> lista = this.ifzTransConceptos.findByProperty("idTransaccion", this.pojoOperacion.getIdTransaccion(), 120);
				if (lista != null && ! lista.isEmpty()) {
					OperacionesIntegradasConceptosSql pojoAux = null;
					if (this.listOperIntegradasConceptosSql == null)
						this.listOperIntegradasConceptosSql = new ArrayList<OperacionesIntegradasConceptosSql>();
					this.listOperIntegradasConceptosSql.clear();
					
					for (TransaccionConceptos var : lista) {
						// Generamos la Operacion-Integrada-Concepto
						pojoAux = new OperacionesIntegradasConceptosSql();
						pojoAux.setIdOperacionIntegradaTransaccion(this.pojoOperacion);
						pojoAux.setIdConcepto(var.getIdConcepto());
						pojoAux.setSql("");
						pojoAux.setCreadoPor(this.usuarioId);
						pojoAux.setFechaCreacion(Calendar.getInstance().getTime());
						pojoAux.setModificadoPor(this.usuarioId);
						pojoAux.setFechaModificacion(Calendar.getInstance().getTime());
						
						// Añadimos a listado
						this.listOperIntegradasConceptosSql.add(pojoAux);
					}
					lista.clear();
				}
				lista = null;
			}

			nuevaBusquedaTransacciones();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.seleccionarTransaccion", e);
		}
	}


	public void cargarOperacionIntegradasConceptosSQL() throws Exception {
		// Inicializamos
		this.pojoOperIntegradasConceptosSql = new OperacionesIntegradasConceptosSql();
		
		if (this.listOperIntegradasConceptosSql == null)
			this.listOperIntegradasConceptosSql = new ArrayList<OperacionesIntegradasConceptosSql>();
		this.listOperIntegradasConceptosSql.clear();
		
		// Cargamos los datos
		this.listOperIntegradasConceptosSql = this.ifzOperIntegradasConceptosSQL.findByProperty("idOperacionIntegradaTransaccion", this.pojoOperacion, 120);
	}
	
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
			this.pojoOperIntegradasConceptosSql.setIdOperacionIntegradaTransaccion(this.pojoOperacion);
			this.pojoOperIntegradasConceptosSql.setModificadoPor(this.pojoOperacion.getModificadoPor());
			this.pojoOperIntegradasConceptosSql.setFechaModificacion(this.pojoOperacion.getFechaModificacion());
			
			// Guardamos en la BD si corresponde
			if (this.pojoOperacion.getId() != null && pojoOperacion.getId() > 0L) {
				if (this.pojoOperIntegradasConceptosSql.getId() == null || this.pojoOperIntegradasConceptosSql.getId() <= 0L) {
					this.pojoOperIntegradasConceptosSql.setCreadoPor(this.pojoOperacion.getModificadoPor());
					this.pojoOperIntegradasConceptosSql.setFechaCreacion(this.pojoOperacion.getFechaModificacion());
					
					// Guardamos y asignamos ID
					this.pojoOperIntegradasConceptosSql.setId(this.ifzOperIntegradasConceptosSQL.save(this.pojoOperIntegradasConceptosSql));
				} else {
					this.ifzOperIntegradasConceptosSQL.update(this.pojoOperIntegradasConceptosSql);
				}
			
				cargarOperacionIntegradasConceptosSQL();
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

	
	public void cargarOperacionIntegradasSQL() throws Exception {
		// Inicializamos
		this.pojoOperIntegradaSQL = new OperacionesIntegradasSql();
		
		if (this.listOperIntegradasSQL == null)
			this.listOperIntegradasSQL = new ArrayList<OperacionesIntegradasSql>();
		this.listOperIntegradasSQL.clear();
		
		// Cargamos los datos
		this.listOperIntegradasSQL = this.ifzOperIntegradasSQL.findByProperty("idOperacionIntegradaTransaccion", this.pojoOperacion, 120);
	}
	
	public void nuevaOperacionIntegradaSQL() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			this.pojoOperIntegradaSQL = new OperacionesIntegradasSql();
			this.pojoOperIntegradaSQLBorrar = null;
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.borrar", e);
		}
	}
	
	public void salvarOperacionIntegradaSQL() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoOperIntegradaSQL != null) {
				this.pojoOperIntegradaSQL.setIdOperacionIntegradaTransaccion(this.pojoOperacion);
				this.pojoOperIntegradaSQL.setModificadoPor(this.usuarioId);
				this.pojoOperIntegradaSQL.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD segun corresponda
				if (this.pojoOperIntegradaSQL.getId() == null || this.pojoOperIntegradaSQL.getId() <= 0L) {
					this.pojoOperIntegradaSQL.setCreadoPor(this.usuarioId);
					this.pojoOperIntegradaSQL.setFechaCreacion(Calendar.getInstance().getTime());
					
					this.pojoOperIntegradaSQL.setId(this.ifzOperIntegradasSQL.save(this.pojoOperIntegradaSQL));
				} else {
					this.ifzOperIntegradasSQL.update(this.pojoOperIntegradaSQL);
				}
				
				nuevaOperacionIntegradaSQL();
			}
			
			cargarOperacionIntegradasSQL();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.borrar", e);
		}
	}
	
	public void borrarOperacionIntegradaSQL() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoOperIntegradaSQLBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoOperIntegradaSQLBorrar.getId() != null && this.pojoOperIntegradaSQLBorrar.getId() > 0L)
					this.ifzOperIntegradasSQL.delete(this.pojoOperIntegradaSQLBorrar.getId());
				
				this.listOperIntegradasSQL.remove(this.pojoOperIntegradaSQLBorrar);
				this.pojoOperIntegradaSQLBorrar = null;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasTransaccionesAction.borrarOperacionIntegradaSQL", e);
		}
	}
	
	
	// --------------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------------

	public String getOperacionIntegrada() {
		if (this.pojoOperacion != null && this.pojoOperacion.getIdOperacionIntegrada() != null && this.pojoOperacion.getIdOperacionIntegrada().getIdOperacion() != null && this.pojoOperacion.getIdOperacionIntegrada().getIdOperacion().getDescripcion() != null)
			return this.pojoOperacion.getIdOperacionIntegrada().getId() + " - " + this.pojoOperacion.getIdOperacionIntegrada().getIdOperacion().getDescripcion();
		return "";
	}
	
	public void setOperacionIntegrada(String value) {}
	
	public String getTransaccion() {
		if (this.pojoOperacion != null && this.pojoOperacion.getIdTransaccion() != null && this.pojoOperacion.getIdTransaccion().getDescripcion() != null)
			return this.pojoOperacion.getIdTransaccion().getId() + " - " + this.pojoOperacion.getIdTransaccion().getDescripcion();
		return "";
	}
	
	public void setTransaccion(String value) {}
	
	public String getConceptoDescripcion() {
		if (this.pojoOperIntegradasConceptosSql != null && this.pojoOperIntegradasConceptosSql.getIdConcepto() != null)
			return this.pojoOperIntegradasConceptosSql.getIdConcepto().getId() + " - " + this.pojoOperIntegradasConceptosSql.getIdConcepto().getDescripcion();
		return "";
	}
	
	public void setConceptoDescripcion(String value) {}
	
	public String getOperIntegradaTransaccionDescripcion() {
		if (this.pojoOperacion == null)
			return "";
		if (this.pojoOperacion.getIdOperacionIntegrada() == null || this.pojoOperacion.getIdOperacionIntegrada().getIdOperacion() == null)
			return "";
		if (this.pojoOperacion.getIdTransaccion() == null)
			return "";
		
		return this.pojoOperacion.getIdOperacionIntegrada().getIdOperacion().getDescripcion() + " - " + this.pojoOperacion.getIdTransaccion().getDescripcion();
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
	
	public List<OperacionesIntegradas> getListBusquedaOperacionesIntegradas() {
		return listBusquedaOperacionesIntegradas;
	}
	
	public void setListBusquedaOperacionesIntegradas(List<OperacionesIntegradas> listBusquedaOperacionesIntegradas) {
		this.listBusquedaOperacionesIntegradas = listBusquedaOperacionesIntegradas;
	}

	public List<SelectItem> getTiposBusquedaOperacionesIntegradas() {
		return tiposBusquedaOperacionesIntegradas;
	}

	public void setTiposBusquedaOperacionesIntegradas(List<SelectItem> tiposBusquedaOperacionesIntegradas) {
		this.tiposBusquedaOperacionesIntegradas = tiposBusquedaOperacionesIntegradas;
	}

	public String getCampoBusquedaOperacionesIntegradas() {
		return campoBusquedaOperacionesIntegradas;
	}

	public void setCampoBusquedaOperacionesIntegradas(String campoBusquedaOperacionesIntegradas) {
		this.campoBusquedaOperacionesIntegradas = campoBusquedaOperacionesIntegradas;
	}

	public String getValorBusquedaOperacionesIntegradas() {
		return valorBusquedaOperacionesIntegradas;
	}

	public void setValorBusquedaOperacionesIntegradas(String valorBusquedaOperacionesIntegradas) {
		this.valorBusquedaOperacionesIntegradas = valorBusquedaOperacionesIntegradas;
	}

	public int getNumPaginaBusquedaOperacionesIntegradas() {
		return numPaginaBusquedaOperacionesIntegradas;
	}

	public void setNumPaginaBusquedaOperacionesIntegradas(int numPaginaBusquedaOperacionesIntegradas) {
		this.numPaginaBusquedaOperacionesIntegradas = numPaginaBusquedaOperacionesIntegradas;
	}

	public List<Transacciones> getListBusquedaTransacciones() {
		return listBusquedaTransacciones;
	}

	public void setListBusquedaTransacciones(List<Transacciones> listBusquedaTransacciones) {
		this.listBusquedaTransacciones = listBusquedaTransacciones;
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

	public List<OperacionesIntegradasSql> getListOperIntegradasSQL() {
		return listOperIntegradasSQL;
	}
	
	public void setListOperIntegradasSQL(List<OperacionesIntegradasSql> listOperIntegradasSQL) {
		this.listOperIntegradasSQL = listOperIntegradasSQL;
	}

	public OperacionesIntegradasSql getPojoOperIntegradaSQL() {
		return pojoOperIntegradaSQL;
	}

	public void setPojoOperIntegradaSQL(OperacionesIntegradasSql pojoOperIntegradaSQL) {
		this.pojoOperIntegradaSQL = pojoOperIntegradaSQL;
	}

	public OperacionesIntegradasSql getPojoOperIntegradaSQLBorrar() {
		return pojoOperIntegradaSQLBorrar;
	}

	public void setPojoOperIntegradaSQLBorrar(OperacionesIntegradasSql pojoOperIntegradaSQLBorrar) {
		this.pojoOperIntegradaSQLBorrar = pojoOperIntegradaSQLBorrar;
	}

	public int getNumPaginaOperIntegradasSQL() {
		return numPaginaOperIntegradasSQL;
	}

	public void setNumPaginaOperIntegradasSQL(int numPaginaOperIntegradasSQL) {
		this.numPaginaOperIntegradasSQL = numPaginaOperIntegradasSQL;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
}
