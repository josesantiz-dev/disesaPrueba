package net.giro.contabilidad;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.Conceptos;
import net.giro.contabilidad.beans.OperacionesExt;
import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;
import net.giro.contabilidad.beans.OperacionesIntegradasTransacciones;
import net.giro.contabilidad.beans.TransaccionConceptos;
import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.beans.TransaccionesExt;
import net.giro.contabilidad.logica.ConceptosRem;
import net.giro.contabilidad.logica.OperacionesIntegradasConceptosSqlRem;
import net.giro.contabilidad.logica.OperacionesIntegradasTransaccionesRem;
import net.giro.contabilidad.logica.OperacionesRem;
import net.giro.contabilidad.logica.TransaccionConceptosRem;
import net.giro.contabilidad.logica.TransaccionesRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="transAction")
public class TransaccionesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TransaccionesAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	// Interfaces
	private TransaccionesRem ifzTransacciones;
	private TransaccionConceptosRem ifzTransConceptos;
	// Listas
	private List<TransaccionesExt> listTransacciones;
	private List<TransaccionConceptos> listTransConceptos;
	private List<TransaccionConceptos> listTransConceptosBorrados;
	// POJO's
	private TransaccionesExt pojoTransaccion;
	private TransaccionesExt pojoTransaccionBorrar;
	private TransaccionConceptos pojoTransConcepto;
	private TransaccionConceptos pojoTransConceptoOri;
	private TransaccionConceptos pojoTransConceptoBorrar;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	private int numPaginaTransConceptos;
	// Busqueda Operaciones
	private OperacionesRem ifzOperaciones;
	private List<OperacionesExt> listOperaciones;
	private List<SelectItem> tiposBusquedaOperaciones;	
	private String campoBusquedaOperaciones;
	private String valorBusquedaOperaciones;
	private int numPaginaOperaciones;
	// Busqueda Conceptos
	private ConceptosRem ifzConceptos;
	private List<Conceptos> listConceptos;
	private List<SelectItem> tiposBusquedaConceptos;	
	private String campoBusquedaConceptos;
	private String valorBusquedaConceptos;
	private int numPaginaConceptos;
	// Busqueda Operaciones Integradas Transacciones
	private OperacionesIntegradasTransaccionesRem ifzOperIntegradasTrans;
	private OperacionesIntegradasConceptosSqlRem ifzOperIntegradasConceptosSQL;
	// Variables de operacion
    private long usuarioId;
    private String usuario;
    private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private boolean codigoOcupado;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	
	public TransaccionesAction() {
		Map<String, String> params = new HashMap<String, String>();
		ValueExpression ve = null;
		FacesContext fc = null;
		Application app = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

			// DEBUGGING
			this.paramsRequest = new HashMap<String, String>();
			params = fc.getExternalContext().getRequestParameterMap();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// EJB
			this.ctx = new InitialContext();
			this.ifzTransacciones 	= (TransaccionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionesFac!net.giro.contabilidad.logica.TransaccionesRem");
			this.ifzOperaciones 	= (OperacionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesFac!net.giro.contabilidad.logica.OperacionesRem");
			this.ifzTransConceptos 	= (TransaccionConceptosRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionConceptosFac!net.giro.contabilidad.logica.TransaccionConceptosRem");
			this.ifzConceptos 		= (ConceptosRem) this.ctx.lookup("ejb:/Logica_Contabilidad//ConceptosFac!net.giro.contabilidad.logica.ConceptosRem");
			this.ifzOperIntegradasTrans = (OperacionesIntegradasTransaccionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesIntegradasTransaccionesFac!net.giro.contabilidad.logica.OperacionesIntegradasTransaccionesRem");
			this.ifzOperIntegradasConceptosSQL = (OperacionesIntegradasConceptosSqlRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesIntegradasConceptosSqlFac!net.giro.contabilidad.logica.OperacionesIntegradasConceptosSqlRem");

			this.ifzTransacciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOperaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTransConceptos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConceptos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOperIntegradasTrans.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOperIntegradasConceptosSQL.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.ifzOperIntegradasTrans.showSystemOuts(false);
			
			// ----------------------------------------------------------------------------------
			// Inicializaciones
			// ----------------------------------------------------------------------------------
			
			this.listTransacciones 	= new ArrayList<TransaccionesExt>();
			this.listOperaciones 	= new ArrayList<OperacionesExt>();
			this.listTransConceptos = new ArrayList<TransaccionConceptos>();
			this.listConceptos 		= new ArrayList<Conceptos>();
			this.pojoTransaccion = new TransaccionesExt();
			this.pojoTransaccionBorrar = new TransaccionesExt();
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			this.numPaginaTransConceptos = 1;
			
			// Busqueda operaciones
			this.tiposBusquedaOperaciones = new ArrayList<SelectItem>();
			this.tiposBusquedaOperaciones.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaOperaciones.add(new SelectItem("id", "ID"));
			this.campoBusquedaOperaciones = this.tiposBusquedaOperaciones.get(0).getValue().toString();
			this.valorBusquedaOperaciones = "";
			this.numPaginaOperaciones = 1;
			
			// Busqueda Conceptos
			this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
			this.tiposBusquedaConceptos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaConceptos.add(new SelectItem("id", "ID"));
			this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
			this.valorBusquedaConceptos = "";
			this.numPaginaConceptos = 1;
		} catch (Exception e) {
			log.error("Error en constructor TransaccionesAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = tiposBusqueda.get(0).getValue().toString();
			
			this.ifzTransacciones.orderBy("codigo");
			this.listTransacciones = this.ifzTransacciones.findExtLikeProperty(this.campoBusqueda, this.valorBusqueda, 1000);
			if (this.listTransacciones.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Transacciones", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.pojoTransaccion = new TransaccionesExt();
			this.pojoTransaccionBorrar = null;
			
			if (this.listTransConceptos == null)
				this.listTransConceptos = new ArrayList<TransaccionConceptos>();
			this.listTransConceptos.clear();

			if (this.listTransConceptosBorrados == null)
				this.listTransConceptosBorrados = new ArrayList<TransaccionConceptos>();
			this.listTransConceptosBorrados.clear();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar crear una nueva Transaccion", e);
		}
	}
	
	public void editar() {
		try {
			control();
			if (this.listTransConceptos == null)
				this.listTransConceptos = new ArrayList<TransaccionConceptos>();
			this.listTransConceptos.clear();

			if (this.listTransConceptosBorrados == null)
				this.listTransConceptosBorrados = new ArrayList<TransaccionConceptos>();
			this.listTransConceptosBorrados.clear();
			
			// cargamos los conceptos asignados a la transaccion
			this.listTransConceptos = this.ifzTransConceptos.findByProperty("idTransaccion.id", this.pojoTransaccion.getId(), 120);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar la Transaccion seleccionada", e);
		}
	}
	
	public void guardar() {
		try {
			control();
			if (this.pojoTransaccion != null) {
				comprobarCodigo();
				if (this.codigoOcupado) {
					control(9, "El codigo [" + this.pojoTransaccion.getCodigo().toString() + "] ya existe");
					return;
				}
				
				this.pojoTransaccion.setModificadoPor(this.usuarioId);
				this.pojoTransaccion.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoTransaccion.getId() == null || this.pojoTransaccion.getId() <= 0L) {
					this.pojoTransaccion.setCreadoPor(this.usuarioId);
					this.pojoTransaccion.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoTransaccion.setId(this.ifzTransacciones.save(this.pojoTransaccion));
					// Agregamos a la lista
					this.listTransacciones.add(this.pojoTransaccion);
				} else {
					// Actualizamos en la BD
					this.ifzTransacciones.update(this.pojoTransaccion);
					
					// Actualizamo en la lista
					for(TransaccionesExt var : this.listTransacciones) {
						if (var.getId().equals(this.pojoTransaccion.getId())) {
							var.setDescripcion(this.pojoTransaccion.getDescripcion());
							var.setIdOperacion(this.pojoTransaccion.getIdOperacion());
							var.setModificadoPor(this.pojoTransaccion.getModificadoPor());
							var.setFechaModificacion(this.pojoTransaccion.getFechaModificacion());
							break;
						}
					}
				}
				
				// Quitamos los conceptos borrados si corresponde
				if (! this.listTransConceptosBorrados.isEmpty()) {
					for(TransaccionConceptos var : this.listTransConceptosBorrados) {
						if (var.getId() != null && var.getId() > 0L)
							this.ifzTransConceptos.delete(var.getId());
					}
					
					this.listTransConceptosBorrados.clear();
				}
				
				if (! this.listTransConceptos.isEmpty()) {
					// Obtenemos el pojo de la transaccion y la asignamos al pojo Transaccion-Concepto
					Transacciones pojoAux = this.ifzTransacciones.findById(this.pojoTransaccion.getId());
					for(TransaccionConceptos var : this.listTransConceptos) {
						// Asignamos la transaccion
						var.setIdTransaccion(pojoAux);
						
						// Guardamos en la BD
						if (var.getId() == null || var.getId() <= 0L)
							var.setId(this.ifzTransConceptos.save(var));
						else
							this.ifzTransConceptos.update(var);
					}
					
					this.listTransConceptos.clear();
				}
			}
		} catch (Exception e) {
			control("Error en TransaccionesAction.guardar", e);
		}
	}
	
	public void borrar() {
		try {
			control();
			if (this.pojoTransaccionBorrar != null && this.pojoTransaccionBorrar.getId() > 0L) {
				// Borramos de la bd
				this.ifzTransacciones.delete(this.pojoTransaccionBorrar.getId());
				// Borramos de la lista
				this.listTransacciones.remove(this.pojoTransaccionBorrar);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Borrar la Transaccion", e);
		}
	}
	
	public void comprobarCodigo() {
		try {
			control();
			this.codigoOcupado = false;
			if (this.pojoTransaccion != null && this.pojoTransaccion.getCodigo() > 0L) {
				if (! this.ifzTransacciones.comprobarCodigo(this.pojoTransaccion.getId(), this.pojoTransaccion.getCodigo())) {
					this.codigoOcupado = true;
					control(9, "El codigo [" + this.pojoTransaccion.getCodigo().toString() + "] ya existe");
					return;
				}
			}
		} catch (Exception e) {
			control("Error en TransaccionesAction.comprobarPosicion", e);
		}
	}
	
	public void nuevoTransaccionConcepto() {
		try {
			control();
			this.pojoTransConcepto = new TransaccionConceptos();
			this.pojoTransConceptoBorrar = null;
			nuevaBusquedaConceptos();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar crear una Nueva Transaccion", e);
		}
	}
	
	public void borrarTransaccionConcepto() {
		try {
			control();
			if (this.pojoTransConceptoBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoTransConceptoBorrar.getId() != null && this.pojoTransConceptoBorrar.getId() > 0L) 
					this.ifzTransConceptos.delete(this.pojoTransConceptoBorrar.getId()); 
				
				// Borramos de la lista
				this.listTransConceptos.remove(this.pojoTransConceptoBorrar);
				this.pojoTransConceptoBorrar = null;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar borrar la Transaccion seleccionada", e);
		}
	}

	private void control() {
		this.operacion = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}

	private void control(int value, String mensaje) { 
		control(true, value, mensaje, null); 
	}
	
	private void control(String mensaje) {
		control(true, 1, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, 1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.operacion = operacionCancelada;
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
		
		log.error("\n\nTRANSACCIONES :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n" + this.mensajeDetalles, throwable);
	}

	// --------------------------------------------------
	// BUSQUEDA OPERACIONES
	// --------------------------------------------------
	
	public void nuevaBusquedaOperaciones() {
		if (this.listOperaciones == null)
			this.listOperaciones = new ArrayList<OperacionesExt>();
		this.listOperaciones.clear();
		
		this.campoBusquedaOperaciones = tiposBusquedaOperaciones.get(0).getValue().toString();
		this.valorBusquedaOperaciones = "";
		numPaginaOperaciones = 1;
	}
	
	public void buscarOperaciones() {
		try {
			control();
			if ("".equals(this.campoBusquedaOperaciones))
				this.campoBusquedaOperaciones = tiposBusquedaOperaciones.get(0).getValue().toString();
			
			this.listOperaciones = this.ifzOperaciones.findExtLikeProperty(this.campoBusquedaOperaciones, this.valorBusquedaOperaciones, 0);
			if (this.listOperaciones == null || this.listOperaciones.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Operaciones", e);
		}
	}
	
	public void seleccionarOperacion() {
		try {
			control();
			nuevaBusquedaOperaciones();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Operacion seleccionada", e);
		}
	}

	// --------------------------------------------------
	// BUSQUEDA CONCEPTOS
	// --------------------------------------------------
	
	public void nuevaBusquedaConceptos() {
		this.pojoTransConceptoOri = null;
		this.pojoTransConcepto = new TransaccionConceptos();
		if (this.listConceptos == null)
			this.listConceptos = new ArrayList<Conceptos>();
		this.listConceptos.clear();
		
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
		this.valorBusquedaConceptos = "";
		this.numPaginaConceptos = 1;
	}
	
	public void nuevaBusquedaConceptosEditar() {
		if (this.listConceptos == null)
			this.listConceptos = new ArrayList<Conceptos>();
		this.listConceptos.clear();
		
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
		this.valorBusquedaConceptos = "";
		this.numPaginaConceptos = 1;
		
		try {
			this.pojoTransConceptoBorrar = null;
			this.pojoTransConceptoOri = new TransaccionConceptos();
			BeanUtils.copyProperties(this.pojoTransConceptoOri, this.pojoTransConcepto);
		} catch (Exception e) {
			this.pojoTransConceptoOri.setId(this.pojoTransConcepto.getId());
			this.pojoTransConceptoOri.setIdTransaccion(this.pojoTransConcepto.getIdTransaccion());
			this.pojoTransConceptoOri.setIdConcepto(this.pojoTransConcepto.getIdConcepto());
			this.pojoTransConceptoOri.setCreadoPor(this.pojoTransConcepto.getCreadoPor());
			this.pojoTransConceptoOri.setFechaCreacion(this.pojoTransConcepto.getFechaCreacion());
			this.pojoTransConceptoOri.setModificadoPor(this.pojoTransConcepto.getModificadoPor());
			this.pojoTransConceptoOri.setFechaModificacion(this.pojoTransConcepto.getFechaModificacion());
		}
	}
	
	public void buscarConceptos() {
		try {
			control();
			if ("".equals(this.campoBusquedaConceptos))
				this.campoBusquedaConceptos = tiposBusquedaConceptos.get(0).getValue().toString();
			
			this.listConceptos = this.ifzConceptos.findLikeProperty(this.campoBusquedaConceptos, this.valorBusquedaConceptos, 0);
			if (this.listConceptos == null || this.listConceptos.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Conceptos", e);
		}
	}
	
	public void seleccionarConcepto() {
		Transacciones pojoAux = null;
		int index = -1;
		
		try {
			control();
			if (this.pojoTransConceptoOri != null) {
				index = this.listTransConceptos.indexOf(this.pojoTransConceptoOri);
				if (index <= -1)
					return;
				
				this.pojoTransConcepto.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoTransConcepto.setModificadoPor(this.usuarioId);
				
				this.ifzTransConceptos.update(this.pojoTransConcepto);
				this.listTransConceptos.set(index, this.pojoTransConcepto);
			} else {
				if (this.pojoTransaccion.getId() != null && this.pojoTransaccion.getId() > 0L) {
					pojoAux = this.ifzTransacciones.convertir(this.pojoTransaccion);
					if (pojoAux == null || pojoAux.getId() == null || pojoAux.getId() <= 0L) {
						control("No existe Transaccion seleccionada");
						return;
					}
				}
				
				// Asignamos la transaccion, lo guardamos y lo agregamos al listado
				this.pojoTransConcepto.setId(0L);
				this.pojoTransConcepto.setIdTransaccion(pojoAux);
				this.pojoTransConcepto.setCreadoPor(this.usuarioId);
				this.pojoTransConcepto.setModificadoPor(this.usuarioId);
				this.pojoTransConcepto.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoTransConcepto.setFechaModificacion(Calendar.getInstance().getTime());
				
				this.pojoTransConcepto.setId(this.ifzTransConceptos.save(this.pojoTransConcepto));
				this.listTransConceptos.add(0, this.pojoTransConcepto);
			}
			
			/*if (this.pojoTransaccion.getId() != null && this.pojoTransaccion.getId() > 0L) {
				// Obtenemos el pojo de la transaccion y la asignamos al pojo Transaccion-Concepto
				pojoAux = this.ifzTransacciones.convertir(this.pojoTransaccion);
				if (pojoAux == null || pojoAux.getId() == null || pojoAux.getId() <= 0L) {
					this.operacion = true;
					this.mensaje = "No existe Transaccion seleccionada";
					this.tipoMensaje = -1;
					return;
				}
				
				// Asignamos la transaccion, lo guardamos y lo agregamos al listado
				this.pojoTransConcepto.setIdTransaccion(pojoAux);
				this.pojoTransConcepto.setId(this.ifzTransConceptos.save(this.pojoTransConcepto));
				this.listTransConceptos.add(this.pojoTransConcepto);
			} */
			
			if (! this.isDebug)
				nuevaBusquedaConceptos();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el Concepto seleccionado", e);
		}
	}

	public void actualizarConceptos() {
		List<OperacionesIntegradasTransacciones> listOperaciones = null;
		List<OperacionesIntegradasConceptosSql> listOperIntegradasConceptosSql = null;
		HashMap<String, String> params = null;
		
		try {
			if (this.pojoTransConceptoOri != null) {
				listOperaciones = this.ifzOperIntegradasTrans.findByProperty("idTransaccion.id", this.pojoTransConceptoOri.getIdTransaccion().getId(), 0);
				if (listOperaciones == null || listOperaciones.isEmpty())
					return;
				
				params = new HashMap<String, String>();
				params.put("idOperacionIntegradaTransaccion.id", String.valueOf(listOperaciones.get(0).getId()));
				params.put("idConcepto.id", String.valueOf(this.pojoTransConceptoOri.getIdConcepto().getId()));
				listOperIntegradasConceptosSql = this.ifzOperIntegradasConceptosSQL.findByProperties(params, 0);
				if (listOperIntegradasConceptosSql == null || listOperIntegradasConceptosSql.isEmpty())
					return;
				
				for (OperacionesIntegradasConceptosSql val : listOperIntegradasConceptosSql) {
					val.setIdConcepto(this.pojoTransConcepto.getIdConcepto());
					val.setFechaModificacion(Calendar.getInstance().getTime());
					val.setModificadoPor(this.usuarioId);
					
					// Actualizo el concepto en la Operacion Integrada Transaccion
					this.ifzOperIntegradasConceptosSQL.update(val);
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar actulizar los COnceptos en Operaciones Integradas Transacciones", e);
		}
	}
	
	// --------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------
	
	public String getTransaccionOperacion() {
		if (this.pojoTransaccion != null && this.pojoTransaccion.getIdOperacion() != null && this.pojoTransaccion.getIdOperacion().getId() != null && this.pojoTransaccion.getIdOperacion().getId() > 0L)
			return pojoTransaccion.getIdOperacion().getId() + " - " + this.pojoTransaccion.getOperacionNombre();
		return "";
	}
	
	public void setTransaccionOperacion(String value) {}
	
	public List<OperacionesExt> getListOperaciones() {
		return listOperaciones;
	}

	public void setListOperaciones(List<OperacionesExt> listOperaciones) {
		this.listOperaciones = listOperaciones;
	}

	public List<TransaccionesExt> getListTransacciones() {
		return listTransacciones;
	}

	public void setListTransacciones(List<TransaccionesExt> listAplicaciones) {
		this.listTransacciones = listAplicaciones;
	}

	public TransaccionesExt getPojoTransaccion() {
		return pojoTransaccion;
	}

	public void setPojoTransaccion(TransaccionesExt pojoTransaccion) {
		this.pojoTransaccion = pojoTransaccion;
	}

	public TransaccionesExt getPojoTransaccionBorrar() {
		return pojoTransaccionBorrar;
	}

	public void setPojoTransaccionBorrar(TransaccionesExt pojoTransaccionBorrar) {
		this.pojoTransaccionBorrar = pojoTransaccionBorrar;
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

	public int getNumPaginaOperaciones() {
		return numPaginaOperaciones;
	}

	public void setNumPaginaOperaciones(int numPaginaOperaciones) {
		this.numPaginaOperaciones = numPaginaOperaciones;
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

	public List<TransaccionConceptos> getListTransConceptos() {
		return listTransConceptos;
	}

	public void setListTransConceptos(List<TransaccionConceptos> listTransConceptos) {
		this.listTransConceptos = listTransConceptos;
	}
	
	public List<Conceptos> getListConceptos() {
		return listConceptos;
	}
	
	public void setListConceptos(List<Conceptos> listConceptos) {
		this.listConceptos = listConceptos;
	}

	public List<SelectItem> getTiposBusquedaConceptos() {
		return tiposBusquedaConceptos;
	}

	public void setTiposBusquedaConceptos(List<SelectItem> tiposBusquedaConceptos) {
		this.tiposBusquedaConceptos = tiposBusquedaConceptos;
	}

	public String getCampoBusquedaConceptos() {
		return campoBusquedaConceptos;
	}

	public void setCampoBusquedaConceptos(String campoBusquedaConceptos) {
		this.campoBusquedaConceptos = campoBusquedaConceptos;
	}

	public String getValorBusquedaConceptos() {
		return valorBusquedaConceptos;
	}

	public void setValorBusquedaConceptos(String valorBusquedaConceptos) {
		this.valorBusquedaConceptos = valorBusquedaConceptos;
	}

	public int getNumPaginaConceptos() {
		return numPaginaConceptos;
	}

	public void setNumPaginaConceptos(int numPaginaConceptos) {
		this.numPaginaConceptos = numPaginaConceptos;
	}

	public TransaccionConceptos getPojoTransConcepto() {
		return pojoTransConcepto;
	}

	public void setPojoTransConcepto(TransaccionConceptos pojoTransConcepto) {
		this.pojoTransConcepto = pojoTransConcepto;
	}

	public TransaccionConceptos getPojoTransConceptoBorrar() {
		return pojoTransConceptoBorrar;
	}

	public void setPojoTransConceptoBorrar(TransaccionConceptos pojoTransConceptoBorrar) {
		this.pojoTransConceptoBorrar = pojoTransConceptoBorrar;
	}

	public int getNumPaginaTransConceptos() {
		return numPaginaTransConceptos;
	}

	public void setNumPaginaTransConceptos(int numPaginaTransConceptos) {
		this.numPaginaTransConceptos = numPaginaTransConceptos;
	}

	public boolean isCodigoOcupado() {
		return codigoOcupado;
	}

	public void setCodigoOcupado(boolean codigoOcupado) {
		this.codigoOcupado = codigoOcupado;
	}

	public boolean isDebugging() {
		return isDebug;
	}
	
	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
}
