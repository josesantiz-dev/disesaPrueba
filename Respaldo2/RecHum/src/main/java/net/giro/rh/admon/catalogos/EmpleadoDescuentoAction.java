package net.giro.rh.admon.catalogos;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoDescuento;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.rh.admon.logica.EmpleadoDescuentoRem;

@ViewScoped
@ManagedBean(name="empdesAction")
public class EmpleadoDescuentoAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(EmpleadoDescuentoAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	
	// Interfaces
	private EmpleadoRem ifzEmpleados;
	private EmpleadoDescuentoRem ifzEmpDescuentos;
	private ConGrupoValoresRem ifzConGruposValores;
	private ConValoresRem ifzConValores;
	// Listas
	private List<Empleado> listEmpleados;
	private List<Empleado> listBusquedaEmpleados;
	private List<EmpleadoDescuento> listEmpDescuentos;
	private List<EmpleadoDescuento> listEmpDescuentosFull;
	private List<EmpleadoDescuento> listEmpDescuentosPagos;
	private List<EmpleadoDescuento> listEmpDescuentosPagosBorrados;
	private List<ConValores> listDescuentos;
	private List<ConValores> listPeriodos;
	private List<SelectItem> listDescuentosItems;
	private List<SelectItem> listPeriodosItems;
	// POJO's
	private Empleado pojoEmpleado;
	private EmpleadoDescuento pojoEmpleadoDescuento;
	private EmpleadoDescuento pojoEmpleadoDescuentoBorrar;
	private ConGrupoValores pojoGrupoDescuentos;
	private ConGrupoValores pojoGrupoEstatusDescuentos;
	private ConGrupoValores pojoGrupoPeriodos;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Busqueda empleados
	private List<SelectItem> tiposBusquedaEmpleados;	
	private String campoBusquedaEmpleados;
	private String valorBusquedaEmpleados;
	private int numPaginaEmpleados;
    private Date fechaBusqueda;
	private int numPaginaEmpDescuentos;
	// Datos descuento
    private long descuentoId;
    private double montoDescuentos;
    private double descuentoSumado;
	private int numeroPagos;
	private long periodo;
	private Date fecha;
	private String observaciones;
	private int numPaginaPagos;
	// Variables de operacion
    private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
    private long usuarioId;
    private String usuario;
    boolean isEdit = false;
    private boolean sinEmpleado;
    
    
    public EmpleadoDescuentoAction() {
    	FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
    	
    	try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzEmpDescuentos = (EmpleadoDescuentoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoDescuentoFac!net.giro.rh.admon.logica.EmpleadoDescuentoRem");
			this.ifzConGruposValores = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");

			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpDescuentos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConGruposValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Listas
			this.listEmpDescuentos = new ArrayList<EmpleadoDescuento>();
			this.numPaginaEmpDescuentos = 1;
			
			// POJO's
			this.pojoEmpleadoDescuento = new EmpleadoDescuento();
			this.pojoEmpleadoDescuentoBorrar = null;
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusqueda.add(new SelectItem("id", "Clave"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda Empleados
			this.tiposBusquedaEmpleados = new ArrayList<SelectItem>();
			this.tiposBusquedaEmpleados.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaEmpleados.add(new SelectItem("id", "Clave"));
			this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getValue().toString();
			this.valorBusquedaEmpleados = "";
			this.numPaginaEmpleados = 1;

 			// GRUPO DESCUENTOS
 			// ---------------------------------------------------------------------------------------------------------
 			this.pojoGrupoDescuentos = this.ifzConGruposValores.findByName("SYS_DESCUENTOS_EMPLEADO");
 			if (this.pojoGrupoDescuentos == null || this.pojoGrupoDescuentos.getId() <= 0L)
 				log.warn("No se encontro encontro el grupo SYS_DESCUENTOS_EMPLEADO en con_grupo_valores");
 			
			// GRUPO ESTATUS DESCUENTOS
 			// ---------------------------------------------------------------------------------------------------------
 			this.pojoGrupoEstatusDescuentos = this.ifzConGruposValores.findByName("SYS_DESCUENTOS_EMPLEADO_ESTATUS");
 			if (this.pojoGrupoEstatusDescuentos == null || this.pojoGrupoEstatusDescuentos.getId() <= 0L)
 				log.warn("No se encontro encontro el grupo SYS_DESCUENTOS_EMPLEADO_ESTATUS en con_grupo_valores");
			
			// GRUPO PERIODOS
 			// ---------------------------------------------------------------------------------------------------------
 			this.pojoGrupoPeriodos = this.ifzConGruposValores.findByName("SYS_TIPO_PERIODO");
 			if (this.pojoGrupoPeriodos == null || this.pojoGrupoPeriodos.getId() <= 0L)
 				log.warn("No se encontro encontro el grupo SYS_TIPO_PERIODO en con_grupo_valores");
			
			this.numPaginaPagos = 1;
			nuevoSinEmpleado();
    	} catch (Exception e) {
			log.error("Error en constructor EmpleadoDescuentoAction", e);
			this.ctx = null;
    	}
    }
    
    
    public void buscar() {
    	try {
			control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = "id";

			// Realizamos la busqueda
			this.listEmpleados = this.ifzEmpleados.findLike(this.valorBusqueda, "primerApellido, segundoApellido, primerNombre, segundoNombre", 0);
			if (this.listEmpleados == null && this.listEmpleados.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al consultar los Empleados con Descuentos", e);
    	}
    }
    
    public void nuevoSinEmpleado() {
    	try {
			control();
			this.sinEmpleado = true;
			this.pojoEmpleado = new Empleado();
			nuevo();
    	} catch (Exception e) {
			control("Ocurrio un problema al intenrar Generar un Nuevo Descuento", e);
    	}
    }
    
    public void nuevo() {
    	try {
			control();
			this.isEdit = false;
			this.pojoEmpleadoDescuento = null;
			this.pojoEmpleadoDescuento = new EmpleadoDescuento();
			this.pojoEmpleadoDescuentoBorrar = null;
			
			// Inicializamos listas
			if (this.listEmpDescuentosFull == null)
				this.listEmpDescuentosFull = new ArrayList<EmpleadoDescuento>();
			this.listEmpDescuentosFull.clear();
			
			if (this.listEmpDescuentos == null)
				this.listEmpDescuentos = new ArrayList<EmpleadoDescuento>();
			this.listEmpDescuentos.clear();
			
			if (this.listEmpDescuentosPagos == null)
				this.listEmpDescuentosPagos = new ArrayList<EmpleadoDescuento>();
			this.listEmpDescuentosPagos.clear();
			
			//this.idRegistro = 0L;
			inicializarDescuento();
			cargarDescuentos();
			cargarPeriodos();
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar generar un Nuevo Descuento", e);
    	}
    }
    
    public void ver() {
    	try {
			control();
			this.sinEmpleado = false;
			this.fechaBusqueda = null;
			inicializarDescuento();
			
			if (this.pojoEmpleado == null || this.pojoEmpleado.getId() == null || this.pojoEmpleado.getId() <= 0L) {
				control(-1, "No selecciono ningun Empleado");
				return;
			}
			
			// Inicializamos listas
			if (this.listEmpDescuentosFull == null)
				this.listEmpDescuentosFull = new ArrayList<EmpleadoDescuento>();
			this.listEmpDescuentosFull.clear();
			
			if (this.listEmpDescuentos == null)
				this.listEmpDescuentos = new ArrayList<EmpleadoDescuento>();
			this.listEmpDescuentos.clear();
			
			if (this.listEmpDescuentosPagos == null)
				this.listEmpDescuentosPagos = new ArrayList<EmpleadoDescuento>();
			this.listEmpDescuentosPagos.clear();
			
			this.ifzEmpDescuentos.orderBy("estatus DESC, idDescuento, fecha");
			this.listEmpDescuentosFull = this.ifzEmpDescuentos.findByProperty("idEmpleado.id", this.pojoEmpleado.getId(), 0);
			if (this.listEmpDescuentosFull == null || this.listEmpDescuentosFull.isEmpty())
				return;

			for (EmpleadoDescuento var : this.listEmpDescuentosFull)
				this.listEmpDescuentos.add(var);
    	} catch (Exception e) {
			control("Ocurrio un problema al consultar los Descuentos del Empleado seleccionado", e);
    	}
    }

    public void editar() {
    	try {
			control();
			this.isEdit = true;
			this.fechaBusqueda = null;
			inicializarDescuento();
			cargarDescuentos();
			cargarPeriodos();
			
			// Recuperamos los descuentos del empleado
			if (this.listEmpDescuentosPagos == null)
				this.listEmpDescuentosPagos = new ArrayList<EmpleadoDescuento>();
			this.listEmpDescuentosPagos.clear();

			this.ifzEmpDescuentos.orderBy("idDescuento, fecha");
			if (this.pojoEmpleadoDescuento.getNumeroPagos() == 1) {
				this.listEmpDescuentosPagos.add(this.pojoEmpleadoDescuento); 
			} else {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("idEmpleado.id", this.pojoEmpleado.getId().toString());
				params.put("idDescuento", this.pojoEmpleadoDescuento.getIdDescuento().toString());
				params.put("idPeriodo", this.pojoEmpleadoDescuento.getIdPeriodo().toString());
				params.put("numeroPagos", String.valueOf(this.pojoEmpleadoDescuento.getNumeroPagos()));
				params.put("estatus", "1");
				this.listEmpDescuentosPagos = this.ifzEmpDescuentos.findLikeProperties(params, 0);
			}
			
			if (this.listEmpDescuentosPagos == null || this.listEmpDescuentosPagos.isEmpty())
				return;
			
			for (EmpleadoDescuento var : this.listEmpDescuentosPagos) {
				this.montoDescuentos += var.getMonto().doubleValue();
				if (this.listEmpDescuentosPagos.indexOf(var) == 0) {
					this.descuentoId = this.pojoEmpleadoDescuento.getIdDescuento();
					this.numeroPagos = this.pojoEmpleadoDescuento.getNumeroPagos();
					this.periodo = this.pojoEmpleadoDescuento.getIdPeriodo();
					this.observaciones = this.pojoEmpleadoDescuento.getObservaciones();
					this.fecha = var.getFecha();
				}
			}
			
			// redondeamos
			this.montoDescuentos =  Math.round(this.montoDescuentos * 100) / 100;
			this.descuentoSumado = this.montoDescuentos;
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar generar un Nuevo Descuento", e);
    	}
    }
    
    public void salvar() {
    	try {
			control();
			if (this.descuentoSumado != this.montoDescuentos) {
				control(4, "La sumatoria de los Pagos no coincide con el Monto del Descuento");
				return;
			}
			
			if (this.listEmpDescuentosPagosBorrados != null && ! this.listEmpDescuentosPagosBorrados.isEmpty()) {
				for (EmpleadoDescuento var : this.listEmpDescuentosPagosBorrados)
					this.ifzEmpDescuentos.delete(var.getId());
			}
			
			for (EmpleadoDescuento var : this.listEmpDescuentosPagos) {
				var.setNumeroPagos(this.numeroPagos);
				var.setIdPeriodo(this.periodo);
				var.setObservaciones(this.observaciones);
				var.setFechaModificacion(Calendar.getInstance().getTime());
				var.setModificadoPor(this.usuarioId);
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setCreadoPor(this.usuarioId);
					
					// Guardamos en la BD
					this.ifzEmpDescuentos.setInfoSesion(this.loginManager.getInfoSesion());
					var.setId(this.ifzEmpDescuentos.save(var));
				} else {
					// Actualizamos en la BD
					this.ifzEmpDescuentos.setInfoSesion(this.loginManager.getInfoSesion());
					this.ifzEmpDescuentos.update(var);
				}
			}
			
			nuevo();
			ver();
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar Guardar el Descuento", e);
    	}
    }

    public void borrar() {
    	try {
			control();
			if (this.pojoEmpleadoDescuentoBorrar != null) {
				EmpleadoDescuento pojoAux = new EmpleadoDescuento();
				// Cancelamos el descuento en la BD si corresponde
				if (this.pojoEmpleadoDescuentoBorrar.getId() != null && this.pojoEmpleadoDescuentoBorrar.getId() > 0L) {
					this.ifzEmpDescuentos.setInfoSesion(this.loginManager.getInfoSesion());
					pojoAux = this.ifzEmpDescuentos.cancelar(this.pojoEmpleadoDescuentoBorrar);
				}
				
				// Actualizamos en el listado si corresponde
				if (pojoAux != null)
					this.listEmpDescuentosFull.set(this.listEmpDescuentosFull.indexOf(pojoEmpleadoDescuentoBorrar), pojoAux);
				
				// Aplicamos filtro si corresponde
				this.filtrarDescuentosPorFecha();
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Descuento seleccionado", e);
    	}
    }

	public void limpiarFiltroFecha() {
		this.fechaBusqueda = null;
		filtrarDescuentosPorFecha();
	}
	
	public void filtrarDescuentosPorFecha() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		// Limpiamos lista
		this.listEmpDescuentos.clear();
		
		// Filtramos descuentos por fecha
		for (EmpleadoDescuento var : this.listEmpDescuentosFull) { 
			if (this.fechaBusqueda != null) {
				if (formatter.format(this.fechaBusqueda).equals(formatter.format(var.getFecha())))
					this.listEmpDescuentos.add(var);
			} else {
				this.listEmpDescuentos.add(var);
			}
		}
	}
	
	public void filtrarDescuentosPorDescuento() {
		// Limpiamos lista
		this.listEmpDescuentos.clear();
		
		// Filtramos descuentos por fecha
		for (EmpleadoDescuento var : this.listEmpDescuentosFull) { 
			if (this.descuentoId > 0L && this.descuentoId == var.getIdDescuento())
				this.listEmpDescuentos.add(var);
			else
				this.listEmpDescuentos.add(var);
		}
	}

	public void cargarDescuentos() {
		if (this.listDescuentos == null)
			this.listDescuentos = new ArrayList<ConValores>();
		this.listDescuentos.clear();
		
		if (this.listDescuentosItems == null)
			this.listDescuentosItems = new ArrayList<SelectItem>();
		this.listDescuentosItems.clear();
		
		if (this.pojoGrupoDescuentos == null)
			return;
		
		this.listDescuentos = this.ifzConValores.findAll(this.pojoGrupoDescuentos);
		if (this.listDescuentos == null || this.listDescuentos.isEmpty())
			return;
		
		for (ConValores var : this.listDescuentos) 
			this.listDescuentosItems.add(new SelectItem(var.getId(), var.getValor()));
	}
	
	public void cargarPeriodos() {
		if (this.listPeriodos == null)
			this.listPeriodos = new ArrayList<ConValores>();
		this.listPeriodos.clear();
		
		if (this.listPeriodosItems == null)
			this.listPeriodosItems = new ArrayList<SelectItem>();
		this.listPeriodosItems.clear();
		
		if (this.pojoGrupoPeriodos == null)
			return;
		
		this.listPeriodos = this.ifzConValores.findAll(this.pojoGrupoPeriodos);
		if (this.listPeriodos == null || this.listPeriodos.isEmpty())
			return;
		
		for (ConValores var : this.listPeriodos) 
			this.listPeriodosItems.add(new SelectItem(var.getId(), var.getDescripcion()));
	}
    
	public void generarPagos() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		Date d1 = null;
		double descuento = 0;
		int dias = 0;
		
		if (this.fecha != null && this.numeroPagos > 0 && this.periodo > 0 && this.montoDescuentos > 0) {
			descuento = (this.montoDescuentos / this.numeroPagos);

			this.descuentoSumado = 0;
			descuento = Math.round(descuento * 100.0) / 100.0;
			calendar.setTime(this.fecha);
			
			for (ConValores var : this.listPeriodos) {
				if (this.periodo == var.getId()) {
					dias = Integer.valueOf(var.getValor());
					break;
				}
			}
			
			if (dias <= 0)
				return;
			
			this.descuentoSumado = 0;
			if (this.listEmpDescuentosPagos == null)
				this.listEmpDescuentosPagos = new ArrayList<EmpleadoDescuento>();
			this.listEmpDescuentosPagos.clear();
			
			if (this.isEdit && this.listEmpDescuentosPagos.size() == this.numeroPagos) {
				d1 = calendar.getTime();
				for (EmpleadoDescuento var : this.listEmpDescuentosPagos) {
					try {
						d1 = df.parse(df.format(calendar.getTime()));
						if (! d1.equals(var.getFecha()) && var.getEstatus() == 1)
							var.setFecha(calendar.getTime());
						if (descuento != var.getMonto().doubleValue() && var.getEstatus() == 1)
							var.setMonto(new BigDecimal(descuento));
						calendar.add(Calendar.DAY_OF_YEAR, dias);
						if (var.getEstatus() == 1)
							this.descuentoSumado += descuento;
						else
							this.descuentoSumado += (Math.round(var.getMonto().doubleValue() * 100.0) / 100.0);
					} catch (ParseException e) {
						log.error("Error generando pagos. PerseException. ", e);
					}
				}
				
				if (this.descuentoSumado < this.montoDescuentos && (this.montoDescuentos - this.descuentoSumado) <= 1) {
					descuento = this.montoDescuentos - this.descuentoSumado;
					descuento = Math.round(descuento * 100.0) / 100.0;
					this.descuentoSumado += descuento;
					
					for (int i = this.listEmpDescuentosPagos.size() -1; i >= 0; i--) {
						if (this.listEmpDescuentosPagos.get(i).getEstatus() == 1) {
							descuento = this.listEmpDescuentosPagos.get(i).getMonto().doubleValue() + descuento;
							this.listEmpDescuentosPagos.get(i).setMonto(new BigDecimal(descuento));
							break;
						}
					}
				} else if (this.descuentoSumado > this.montoDescuentos && (this.descuentoSumado - this.montoDescuentos) <= 1) {
					descuento = this.descuentoSumado - this.montoDescuentos;
					this.descuentoSumado -= descuento;
					
					for (int i = this.listEmpDescuentosPagos.size() -1; i >= 0; i--) {
						if (this.listEmpDescuentosPagos.get(i).getEstatus() == 1) {
							descuento = this.listEmpDescuentosPagos.get(i).getMonto().doubleValue() - descuento;
							this.listEmpDescuentosPagos.get(i).setMonto(new BigDecimal(descuento));
							break;
						}
					}
				}
				
				return;
			}
			
			if (this.listEmpDescuentosPagos.size() > 0) {
				this.isEdit = false;
				if(this.listEmpDescuentosPagosBorrados == null)
					this.listEmpDescuentosPagosBorrados = new ArrayList<EmpleadoDescuento>();
				this.listEmpDescuentosPagosBorrados.clear();
				
				for (EmpleadoDescuento var : this.listEmpDescuentosPagos) {
					if (var.getId() != null && var.getId() > 0L)
						this.listEmpDescuentosPagosBorrados.add(var);
				}
			}

			EmpleadoDescuento pojoAux;
			this.listEmpDescuentosPagos.clear();
			for (int i = 0; i < this.numeroPagos; i++) {
				this.descuentoSumado += descuento;
				pojoAux = new EmpleadoDescuento(0L);
				
				pojoAux.setIdEmpleado(this.pojoEmpleado);
				pojoAux.setIdDescuento(this.descuentoId);
				pojoAux.setNumeroPagos(this.numeroPagos);
				pojoAux.setIdPeriodo(this.periodo);
				pojoAux.setFecha(calendar.getTime());
				pojoAux.setMonto(new BigDecimal(descuento));
				pojoAux.setObservaciones(this.observaciones);
				pojoAux.setCreadoPor(this.usuarioId);
				pojoAux.setFechaCreacion(Calendar.getInstance().getTime());
				pojoAux.setModificadoPor(this.usuarioId);
				pojoAux.setFechaModificacion(Calendar.getInstance().getTime());
				pojoAux.setEstatus(1);
				
				this.listEmpDescuentosPagos.add(pojoAux);
				calendar.add(Calendar.DAY_OF_YEAR, dias);
			}
			
			if (this.descuentoSumado < this.montoDescuentos && (this.montoDescuentos - this.descuentoSumado) <= 1) {
				descuento = this.montoDescuentos - this.descuentoSumado;
				this.descuentoSumado += descuento;
				
				for (int i = this.listEmpDescuentosPagos.size() -1; i >= 0; i--) {
					if (this.listEmpDescuentosPagos.get(i).getEstatus() == 1) {
						descuento = this.listEmpDescuentosPagos.get(i).getMonto().doubleValue() + descuento;
						this.listEmpDescuentosPagos.get(i).setMonto(new BigDecimal(descuento));
						break;
					}
				}
			} else if (this.descuentoSumado > this.montoDescuentos && (this.descuentoSumado - this.montoDescuentos) <= 1) {
				descuento = this.descuentoSumado - this.montoDescuentos;
				this.descuentoSumado -= descuento;
				
				for (int i = this.listEmpDescuentosPagos.size() -1; i >= 0; i--) {
					if (this.listEmpDescuentosPagos.get(i).getEstatus() == 1) {
						descuento = this.listEmpDescuentosPagos.get(i).getMonto().doubleValue() - descuento;
						this.listEmpDescuentosPagos.get(i).setMonto(new BigDecimal(descuento));
						break;
					}
				}
			}
			
			totalizarPagos();
		}
	}
	
	public void recalcularPagos(AjaxBehaviorEvent event) {
		int currentIndex = -1;
		double descuento = 0;
		int numPagosTmp = 0;
		double montoDescuentosTmp = 0;
		
		if (this.listEmpDescuentosPagos == null || this.listEmpDescuentosPagos.isEmpty()) {
			generarPagos();
			return;
		}
		
		if (event.getComponent().getAttributes().get("currentIndex") == null)
			currentIndex = (int) event.getComponent().getAttributes().get("currentIndex");
		if (currentIndex >= 0) {
			if (this.listEmpDescuentosPagos.get(currentIndex).getId() != null && this.listEmpDescuentosPagos.get(currentIndex).getId() > 0L)
				return;
			
			this.listEmpDescuentosPagos.get(currentIndex).setModificadoPor(-1L);
			for (EmpleadoDescuento item : this.listEmpDescuentosPagos) {
				if (currentIndex >= 0 && currentIndex == this.listEmpDescuentosPagos.indexOf(item))
					continue;
				if (item.getModificadoPor() >= 0L)
					continue;
				montoDescuentosTmp += item.getMonto().doubleValue();
				numPagosTmp += 1;
			}
	
			montoDescuentosTmp = this.montoDescuentos - montoDescuentosTmp;
			numPagosTmp = this.numeroPagos - numPagosTmp;
			descuento = (montoDescuentosTmp / numPagosTmp);
			
			for (EmpleadoDescuento item : this.listEmpDescuentosPagos) {
				if (item.getModificadoPor() >= -1L)
					item.setMonto(new BigDecimal(descuento));
			}
		}
		
		totalizarPagos();
	}
	
	public void totalizarPagos() {
		this.descuentoSumado = 0;
		for (EmpleadoDescuento var : this.listEmpDescuentosPagos)
			this.descuentoSumado +=  Math.round(var.getMonto().doubleValue() * 100.0) / 100.0;
		this.descuentoSumado =  Math.round(this.descuentoSumado * 100.0) / 100.0;
	}
	
	private void inicializarDescuento() {
		this.descuentoId = 0L;
		this.numeroPagos = 0;
		this.periodo = 0;
		this.fecha = Calendar.getInstance().getTime();
		this.observaciones = "";
		this.montoDescuentos = 0;
		this.descuentoSumado = 0;
	}

	private void control() {
		this.operacion = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}

	private void control(int value, String mensaje) {
		control(true, value, mensaje, null);
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
		
		log.error("\n\nEMPLEADO-DESCUENTOS :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n", throwable);
	}

	//-----------------------------------------------------------------------------------
	// BUSQUEDA EMPLEADOS
	//-----------------------------------------------------------------------------------
	
	public void nuevaBusquedaEmpleados() {
		this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getDescription();
		this.valorBusquedaEmpleados = "";
		
		if (this.listBusquedaEmpleados != null)
			this.listBusquedaEmpleados.clear();
	}

	public void buscarEmpleados() throws Exception {
		try {
    		this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.listBusquedaEmpleados = this.ifzEmpleados.findLike(this.valorBusquedaEmpleados, "nombre", 0);
			if(this.listBusquedaEmpleados.isEmpty()){
	    		this.operacion = true;
				this.tipoMensaje = 2;
				this.mensaje = "ERROR";
				return;
			}
    	} catch (Exception e) {
    		this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    		log.error("Error en RecHum.EmpleadoDescuentoAction.buscarEmpleados", e);
    		throw e;
    	}
	}

	public void seleccionarEmpleado() throws Exception {
		try {
    		this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			nuevaBusquedaEmpleados();
			ver();
		} catch (Exception e) {
    		this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en RecHum.EmpleadoDescuentoAction.seleccionarEmpleado", e);
			throw e;
		}
	}
	
    // ----------------------------------------------------------------------------------------
    // PROPIEDADES
    // ----------------------------------------------------------------------------------------

	public String getIdEmpleado() {
		if (this.pojoEmpleado != null && this.pojoEmpleado.getId() != null)
			return this.pojoEmpleado.getId().toString();
		return "";
	}
	
	public void setIdEmpleado(String value) {}
	
	public String getNombreEmpleado() {
		if (this.pojoEmpleado != null && this.pojoEmpleado.getId() != null)
			return this.pojoEmpleado.getId() + " " + this.pojoEmpleado.getNombre();
		return "0";
	}
	
	public void setNombreEmpleado(String value) {}

    public List<EmpleadoDescuento> getListEmpDescuentos() {
		return listEmpDescuentos;
	}

	public void setListEmpDescuentos(List<EmpleadoDescuento> listEmpDescuentos) {
		this.listEmpDescuentos = listEmpDescuentos;
	}

	public EmpleadoDescuento getPojoEmpleadoDescuento() {
		return pojoEmpleadoDescuento;
	}

	public void setPojoEmpleadoDescuento(EmpleadoDescuento pojoEmpleadoDescuento) {
		this.pojoEmpleadoDescuento = pojoEmpleadoDescuento;
	}

	public EmpleadoDescuento getPojoEmpleadoDescuentoBorrar() {
		return pojoEmpleadoDescuentoBorrar;
	}

	public void setPojoEmpleadoDescuentoBorrar(EmpleadoDescuento pojoEmpleadoDescuentoBorrar) {
		this.pojoEmpleadoDescuentoBorrar = pojoEmpleadoDescuentoBorrar;
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

	public List<SelectItem> getTiposBusquedaEmpleados() {
		return tiposBusquedaEmpleados;
	}

	public void setTiposBusquedaEmpleados(List<SelectItem> tiposBusquedaEmpleados) {
		this.tiposBusquedaEmpleados = tiposBusquedaEmpleados;
	}

	public String getCampoBusquedaEmpleados() {
		return campoBusquedaEmpleados;
	}

	public void setCampoBusquedaEmpleados(String campoBusquedaEmpleados) {
		this.campoBusquedaEmpleados = campoBusquedaEmpleados;
	}

	public String getValorBusquedaEmpleados() {
		return valorBusquedaEmpleados;
	}

	public void setValorBusquedaEmpleados(String valorBusquedaEmpleados) {
		this.valorBusquedaEmpleados = valorBusquedaEmpleados;
	}

	public int getNumPaginaEmpleados() {
		return numPaginaEmpleados;
	}

	public void setNumPaginaEmpleados(int numPaginaEmpleados) {
		this.numPaginaEmpleados = numPaginaEmpleados;
	}

	public List<Empleado> getListEmpleados() {
		return listEmpleados;
	}

	public void setListEmpleados(List<Empleado> listEmpleados) {
		this.listEmpleados = listEmpleados;
	}
	
	public Empleado getPojoEmpleado() {
		return pojoEmpleado;
	}
	
	public void setPojoEmpleado(Empleado pojoEmpleado) {
		this.pojoEmpleado = pojoEmpleado;
		this.sinEmpleado = false;
	}

	public int getNumPaginaEmpDescuentos() {
		return numPaginaEmpDescuentos;
	}

	public void setNumPaginaEmpDescuentos(int numPaginaEmpDescuentos) {
		this.numPaginaEmpDescuentos = numPaginaEmpDescuentos;
	}

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}

	public List<Empleado> getListBusquedaEmpleados() {
		return listBusquedaEmpleados;
	}

	public void setListBusquedaEmpleados(List<Empleado> listBusquedaEmpleados) {
		this.listBusquedaEmpleados = listBusquedaEmpleados;
	}

	public List<ConValores> getListDescuentos() {
		return listDescuentos;
	}

	public void setListDescuentos(List<ConValores> listDescuentos) {
		this.listDescuentos = listDescuentos;
	}
	
	public List<SelectItem> getListDescuentosItems() {
		return listDescuentosItems;
	}
	
	public void setListDescuentosItems(List<SelectItem> listDescuentosItems) {
		this.listDescuentosItems = listDescuentosItems;
	}

	public List<SelectItem> getListPeriodosItems() {
		return listPeriodosItems;
	}

	public void setListPeriodosItems(List<SelectItem> listPeriodosItems) {
		this.listPeriodosItems = listPeriodosItems;
	}
	
	public long getDescuentoId() {
		return descuentoId;
	}
	
	public void setDescuentoId(long descuentoId) {
		this.descuentoId = descuentoId;
	}

	public double getMontoDescuentos() {
		return montoDescuentos;
	}

	public void setMontoDescuentos(double montoDescuentos) {
		this.montoDescuentos = montoDescuentos;
	}

	public int getNumeroPagos() {
		return numeroPagos;
	}

	public void setNumeroPagos(int numeroPagos) {
		this.numeroPagos = numeroPagos;
	}

	public long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(long periodo) {
		this.periodo = periodo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getNumPaginaPagos() {
		return numPaginaPagos;
	}

	public void setNumPaginaPagos(int numPaginaPagos) {
		this.numPaginaPagos = numPaginaPagos;
	}

	public List<EmpleadoDescuento> getListEmpDescuentosPagos() {
		return listEmpDescuentosPagos;
	}

	public void setListEmpDescuentosPagos(List<EmpleadoDescuento> listEmpDescuentosPagos) {
		this.listEmpDescuentosPagos = listEmpDescuentosPagos;
	}
	
	public double getDescuentoSumado() {
		return descuentoSumado;
	}
	
	public void setDescuentoSumado(double descuentoSumado) {
		this.descuentoSumado = descuentoSumado;
	}
	
	public boolean isSinEmpleado() {
		return sinEmpleado;
	}

	public void setSinEmpleado(boolean sinEmpleado) {
		this.sinEmpleado = sinEmpleado;
	}


	public String getMensajeDetalles() {
		return mensajeDetalles;
	}


	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}	
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN |   FECHA    | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| DD/MM/YYYY | Javier Tirado	| Creacion de EmpleadoDescuentoAction