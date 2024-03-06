package net.giro.rh.beans;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.navegador.LoginManager;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoInfonavit;
import net.giro.rh.admon.logica.EmpleadoInfonavitRem;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="empInfoAction")
public class InfonavitAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(InfonavitAction.class);
	private InitialContext ctx;
	private LoginManager loginManager;
	// Interfaces
	private EmpleadoInfonavitRem ifzEmpInfonavit;
	private EmpleadoRem ifzEmpleados;
	// Listas
	private List<EmpleadoInfonavit> listEmpleadoInfonavit;
	// POJO's
	private EmpleadoInfonavit pojoEmpleadoInfonavit;
	private EmpleadoInfonavit pojoEmpleadoInfonavitBorrar;
	// Busqueda Empleados
	private List<Empleado> listBusquedaEmpleados;
	private List<SelectItem> tiposBusquedaEmpleados;	
	private String campoBusquedaEmpleados;
	private String valorBusquedaEmpleados;
	private int paginaBusquedaEmpleados;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Variables de operacion
	private long usuarioId;
	private Date fecha; 
	//private String usuario;
	private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	
	public InfonavitAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null; 
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager 		= (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId 			= this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();

			// Interfaces
			this.ctx = new InitialContext();
			this.ifzEmpInfonavit = (EmpleadoInfonavitRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoInfonavitFac!net.giro.rh.admon.logica.EmpleadoInfonavitRem");
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");

			this.ifzEmpInfonavit.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Listas
			this.listEmpleadoInfonavit = new ArrayList<EmpleadoInfonavit>();
			
			// POJO's
			this.pojoEmpleadoInfonavit = new EmpleadoInfonavit();
			this.pojoEmpleadoInfonavitBorrar = null;
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("idEmpleado.nombre", "Nombre"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda Empleados
			this.listBusquedaEmpleados = new ArrayList<Empleado>();
			this.tiposBusquedaEmpleados = new ArrayList<SelectItem>();
			this.tiposBusquedaEmpleados.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaEmpleados.add(new SelectItem("id", "ID"));
			this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getDescription();
			this.valorBusquedaEmpleados = "";
			this.paginaBusquedaEmpleados = 1;
			
			nuevo();
		} catch (Exception e) {
			log.error("Error en constructor EmpleadoInfonavitAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();

			// Realizamos la busqueda
			this.numPagina = 1;
			this.ifzEmpInfonavit.orderBy("idEmpleado.nombre, fechaDesde, fechaHasta");
			this.ifzEmpInfonavit.setInfoSesion(this.loginManager.getInfoSesion());
			this.listEmpleadoInfonavit = this.ifzEmpInfonavit.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 0);
			if (this.listEmpleadoInfonavit == null || this.listEmpleadoInfonavit.isEmpty()) 
				control(2, "La busqueda no regreso resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar los registros de Infonavit", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.pojoEmpleadoInfonavitBorrar = null;
			this.pojoEmpleadoInfonavit = new EmpleadoInfonavit();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar generar un nuevo registro de Infonavit", e);
		}
	}
	
	public void editar() {
		try {
			control();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el registro indicado", e);
		}
	}
	
	public void borrar() {
		try {
			control();
			if (this.pojoEmpleadoInfonavitBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoEmpleadoInfonavitBorrar.getId() != null && this.pojoEmpleadoInfonavitBorrar.getId() > 0L) {
					this.ifzEmpInfonavit.setInfoSesion(this.loginManager.getInfoSesion());
					this.ifzEmpInfonavit.cancelar(this.pojoEmpleadoInfonavitBorrar);
				}
				
				// Borramos del listado
				this.listEmpleadoInfonavit.remove(pojoEmpleadoInfonavitBorrar);
				this.pojoEmpleadoInfonavitBorrar = null;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el registro de Infonavit indicado", e);
		}
	}

	public void salvar() {
		int index = -1;
		
		try {
			control();
			if (! validar())
				return;
			
			this.pojoEmpleadoInfonavit.setModificadoPor(this.usuarioId);
			this.pojoEmpleadoInfonavit.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.pojoEmpleadoInfonavit.getId() == null || this.pojoEmpleadoInfonavit.getId() <= 0L) {
				this.pojoEmpleadoInfonavit.setCreadoPor(this.usuarioId);
				this.pojoEmpleadoInfonavit.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD
				this.ifzEmpInfonavit.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoEmpleadoInfonavit.setId(this.ifzEmpInfonavit.save(this.pojoEmpleadoInfonavit));
				index = 0;
			} else {
				// Actualizamos en la BD
				this.ifzEmpInfonavit.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzEmpInfonavit.update(this.pojoEmpleadoInfonavit);
				
				// Actualizamos en el listado
				for (EmpleadoInfonavit var : this.listEmpleadoInfonavit) {
					if (this.pojoEmpleadoInfonavit.getId().equals(var.getId())) {
						index = this.listEmpleadoInfonavit.indexOf(var);
						break;
					}
				}
			}
			
			// Agregamos a la lista si corresponde				
			if (index >= 0)
				this.listEmpleadoInfonavit.add(index, this.pojoEmpleadoInfonavit);

			nuevo();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar el registro de Infonavit", e);
		}
	}
	
	public boolean validar() {
		try {
			if (this.pojoEmpleadoInfonavit == null) {
				control(-1, "Debe indicar el Empleado");
				return false;
			}
			
			if (this.pojoEmpleadoInfonavit.getMonto().doubleValue() <= 0) {
				control(4, "El monto no es valido, este debe ser mayor a cero");
				return false;
			}
			
			if (this.ifzEmpInfonavit.comprobarRegistro(this.pojoEmpleadoInfonavit)) {
				control(5, "Ya existe un registro con el Empleado, Periodo y Año indicados");
				return false;
			}
			
			if (this.pojoEmpleadoInfonavit.getFechaDesde().compareTo(this.pojoEmpleadoInfonavit.getFechaHasta()) >= 0) {
				control(6, "La fecha 'Hasta' debe ser posterior a fecha 'Desde'");
				return false;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al validar los datos", e);
			return false;
		}
		
		return true;
	}

	private void control() {
		this.operacion = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Se ha producido un error desconocido";
		
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			StringWriter sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = "<br><br>" + sw.toString();
		}
		
		log.error("\nSOLICITUD A BODEGA :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "\n" + this.mensaje + this.mensajeDetalles, throwable);
		//backtracePrint();
	}

	// ----------------------------------------------------------------------------------------
	// Empleados
	// ----------------------------------------------------------------------------------------
	
	public void nuevaBusquedaEmpleados() {
		control();
		if (this.listBusquedaEmpleados == null)
			this.listBusquedaEmpleados = new ArrayList<Empleado>();
		this.listBusquedaEmpleados.clear();
		this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getValue().toString();
		this.valorBusquedaEmpleados = "";
		this.paginaBusquedaEmpleados = 1;
	}

	public void buscarEmpleados() throws Exception {
		try {
			control();
			if (this.listBusquedaEmpleados == null)
				this.listBusquedaEmpleados = new ArrayList<Empleado>();
			this.listBusquedaEmpleados.clear();
			this.paginaBusquedaEmpleados = 1;

			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.listBusquedaEmpleados = this.ifzEmpleados.findLike(this.valorBusquedaEmpleados, "nombre", 0, 0);
			if (this.listBusquedaEmpleados == null || this.listBusquedaEmpleados.isEmpty()){
				control(2, "La busqueda no regreso resultados");
				return;
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar consultar los Empleados", e);
    	}
	}

	public void seleccionarEmpleado() {
		try {
			control();
			nuevaBusquedaEmpleados();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Empleado seleccionado", e);
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------------

	public String getTitulo() {
		if (this.pojoEmpleadoInfonavit != null && this.pojoEmpleadoInfonavit.getId() != null && this.pojoEmpleadoInfonavit.getId() > 0L)
			return "Registro " + this.pojoEmpleadoInfonavit.getId();
		return "Nuevo Registro";
	}
	
	public void setTitulo(String value) {}
	
	public String getEmpleado() {
		if (this.pojoEmpleadoInfonavit.getIdEmpleado() != null && this.pojoEmpleadoInfonavit.getIdEmpleado().getId() != null && this.pojoEmpleadoInfonavit.getIdEmpleado().getId() > 0L)
			return this.pojoEmpleadoInfonavit.getIdEmpleado().getId() + " - " + this.pojoEmpleadoInfonavit.getIdEmpleado().getNombre();
		return "";
	}
	
	public void setEmpleado(String value) {}
	
	public List<EmpleadoInfonavit> getListEmpleadoInfonavit() {
		return listEmpleadoInfonavit;
	}

	public void setListEmpleadoInfonavit(List<EmpleadoInfonavit> listEmpInfonavit) {
		this.listEmpleadoInfonavit = listEmpInfonavit;
	}

	public EmpleadoInfonavit getPojoEmpleadoInfonavit() {
		return pojoEmpleadoInfonavit;
	}

	public void setPojoEmpleadoInfonavit(EmpleadoInfonavit pojoEmpleadoInfonavit) {
		this.pojoEmpleadoInfonavit = pojoEmpleadoInfonavit;
	}

	public EmpleadoInfonavit getPojoEmpleadoInfonavitBorrar() {
		return pojoEmpleadoInfonavitBorrar;
	}

	public void setPojoEmpleadoInfonavitBorrar(EmpleadoInfonavit pojoEmpleadoInfonavitBorrar) {
		this.pojoEmpleadoInfonavitBorrar = pojoEmpleadoInfonavitBorrar;
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
	
	public List<Empleado> getListBusquedaEmpleados() {
		return listBusquedaEmpleados;
	}
	
	public void setListBusquedaEmpleados(List<Empleado> listBusquedaEmpleados) {
		this.listBusquedaEmpleados = listBusquedaEmpleados;
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

	public int getPaginaBusquedaEmpleados() {
		return paginaBusquedaEmpleados;
	}

	public void setPaginaBusquedaEmpleados(int paginaBusquedaEmpleados) {
		this.paginaBusquedaEmpleados = paginaBusquedaEmpleados;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN |   FECHA    | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadosInfonavitAction