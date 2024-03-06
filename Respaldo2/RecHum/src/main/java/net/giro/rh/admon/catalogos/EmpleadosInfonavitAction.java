package net.giro.rh.admon.catalogos;

import java.io.Serializable;
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
public class EmpleadosInfonavitAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(EmpleadosInfonavitAction.class);
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
	//private String usuario;
	private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	private Date fecha; 
	
	
	public EmpleadosInfonavitAction() {
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
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();

			// Realizamos la busqueda
			this.numPagina = 1;
			this.ifzEmpInfonavit.orderBy("idEmpleado.nombre, fechaDesde, fechaHasta");
			this.listEmpleadoInfonavit = this.ifzEmpInfonavit.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 0);
			if (this.listEmpleadoInfonavit == null || this.listEmpleadoInfonavit.isEmpty()) {
				this.operacion = true;
				this.tipoMensaje = 2;
				this.mensaje = "ERROR";
				return;
			}
		} catch (Exception e) {
			log.error("Error en constructor EmpleadoInfonavitAction.buscar", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public void nuevo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoEmpleadoInfonavitBorrar = null;
			this.pojoEmpleadoInfonavit = new EmpleadoInfonavit();
		} catch (Exception e) {
			log.error("Error en constructor EmpleadoInfonavitAction.nuevo", e);
			this.operacion = true;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
		}
	}
	
	public void editar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
		} catch (Exception e) {
			log.error("Error en constructor EmpleadoInfonavitAction.editar", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public void borrar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoEmpleadoInfonavitBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoEmpleadoInfonavitBorrar.getId() != null && this.pojoEmpleadoInfonavitBorrar.getId() > 0L)
					this.ifzEmpInfonavit.delete(this.pojoEmpleadoInfonavitBorrar.getId());
				
				// Borramos del listado
				this.listEmpleadoInfonavit.remove(pojoEmpleadoInfonavitBorrar);

				this.pojoEmpleadoInfonavitBorrar = null;
			}
		} catch (Exception e) {
			log.error("Error en constructor EmpleadoInfonavitAction.borrar", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}

	/*public void cancelar() {
    	try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			int index = -1;
			if (this.pojoEmpleadoInfonavitBorrar != null) {
				// Cancelamos en la BD si corresponde
				if (this.pojoEmpleadoInfonavitBorrar.getId() != null && this.pojoEmpleadoInfonavitBorrar.getId() > 0L) {
					index = this.listEmpInfonavit.indexOf(this.pojoEmpleadoInfonavitBorrar);
					this.pojoEmpleadoInfonavitBorrar = this.ifzEmpInfonavit.cancelar(this.pojoEmpleadoInfonavitBorrar);
				}
				
				// Reemplazamos en la lista si corresponde
				if (index >= 0)
					this.listEmpInfonavit.set(index, this.pojoEmpleadoInfonavitBorrar);

				this.pojoEmpleadoInfonavitBorrar = null;
			}
    	} catch (Exception e) {
			log.error("Error en constructor EmpleadoInfonavitAction.cancelar", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    	}
    }*/
	
	public void salvar() {
		int index = -1;
		
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (! validar())
				return;
			
			this.pojoEmpleadoInfonavit.setModificadoPor(this.usuarioId);
			this.pojoEmpleadoInfonavit.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.pojoEmpleadoInfonavit.getId() == null || this.pojoEmpleadoInfonavit.getId() <= 0L) {
				this.pojoEmpleadoInfonavit.setCreadoPor(this.usuarioId);
				this.pojoEmpleadoInfonavit.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD
				this.pojoEmpleadoInfonavit.setId(this.ifzEmpInfonavit.save(this.pojoEmpleadoInfonavit));
				index = 0;
			} else {
				// Actualizamos en la BD
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
			log.error("Error en constructor EmpleadoInfonavitAction.salvar", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public boolean validar() {
		try {
			if (this.pojoEmpleadoInfonavit == null) {
				log.error("Error en EmpleadoInfonavitAction.validar(): pojo (EmpleadoInfonavit) nulo");
				this.mensaje = "No indico ningun Empleado";
				this.operacion = true;
				this.tipoMensaje = -1;
				return false;
			}
			
			if (this.pojoEmpleadoInfonavit.getMonto().doubleValue() <= 0) {
				log.error("Error en constructor EmpleadoInfonavitAction.salvar: El monto no es valido, este debe ser mayor a cero");
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 4;
				return false;
			}
			
			if (this.ifzEmpInfonavit.comprobarRegistro(this.pojoEmpleadoInfonavit)) {
				log.error("Error en constructor EmpleadoInfonavitAction.salvar: Ya existe un registro con el Empleado, Periodo y Año seleccionados");
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 5;
				return false;
			}
			
			if (this.pojoEmpleadoInfonavit.getFechaDesde().compareTo(this.pojoEmpleadoInfonavit.getFechaHasta()) >= 0) {
				log.error("Error en constructor EmpleadoInfonavitAction.salvar: La fecha 'Hasta' debe ser mayor a fecha 'Desde'");
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 6;
				return false;
			}
		} catch (Exception e) {
			log.error("Error en EmpleadoInfonavitAction.validar()", e);
			this.mensaje = "Ocurrio un problema al validar los datos";
			this.operacion = true;
			this.tipoMensaje = 1;
			return false;
		}
		
		return true;
	}

	// ----------------------------------------------------------------------------------------
	// Empleados
	// ----------------------------------------------------------------------------------------
	
	public void nuevaBusquedaEmpleados() {
		if (this.listBusquedaEmpleados == null)
			this.listBusquedaEmpleados = new ArrayList<Empleado>();
		this.listBusquedaEmpleados.clear();
		
		this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getValue().toString();
		this.valorBusquedaEmpleados = "";
		this.paginaBusquedaEmpleados = 1;
	}

	public void buscarEmpleados() throws Exception {
		try {
    		this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.listBusquedaEmpleados == null)
				this.listBusquedaEmpleados = new ArrayList<Empleado>();
			this.listBusquedaEmpleados.clear();
			this.paginaBusquedaEmpleados = 1;
			
			this.listBusquedaEmpleados = this.ifzEmpleados.findLike(this.valorBusquedaEmpleados, "nombre", 0);
			if (this.listBusquedaEmpleados == null || this.listBusquedaEmpleados.isEmpty()){
	    		this.operacion = true;
				this.tipoMensaje = 2;
				this.mensaje = "ERROR";
				return;
			}
    	} catch (Exception e) {
    		log.error("Error en RecHum.EmpleadosDescuentoAction.buscarEmpleados", e);
    		this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    	}
	}

	public void seleccionarEmpleado() {
		try {
    		this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			nuevaBusquedaEmpleados();
		} catch (Exception e) {
			log.error("Error en RecHum.EmpleadosDescuentoAction.seleccionarEmpleado", e);
    		this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
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