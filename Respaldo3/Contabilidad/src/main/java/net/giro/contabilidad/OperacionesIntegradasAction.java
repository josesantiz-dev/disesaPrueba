package net.giro.contabilidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.Operaciones;
import net.giro.contabilidad.beans.OperacionesIntegradas;
import net.giro.contabilidad.logica.OperacionesIntegradasRem;
import net.giro.contabilidad.logica.OperacionesRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="operIntAction")
public class OperacionesIntegradasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(OperacionesIntegradasAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	// Interfaces
	private OperacionesIntegradasRem ifzOperIntegradas;
	private OperacionesRem ifzOperaciones;
	// POJO's
	private OperacionesIntegradas pojoOperacion;
	private OperacionesIntegradas pojoOperacionBorrar;
	// Busqueda principal
	private List<OperacionesIntegradas> listOperaciones;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Busqueda Operaciones
	private List<Operaciones> listBusquedaOperaciones;
	private List<SelectItem> tiposBusquedaOperaciones;	
	private String campoBusquedaOperaciones;
	private String valorBusquedaOperaciones;
	private int numPaginaBusquedaOperaciones;
	// Variables de operacion
    private long usuarioId;
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	
	
	public OperacionesIntegradasAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();

			// ----------------------------------------------------------------------------------
			// Inicializaciones
			// ----------------------------------------------------------------------------------

			this.ctx = new InitialContext();
			this.ifzOperIntegradas = (OperacionesIntegradasRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesIntegradasFac!net.giro.contabilidad.logica.OperacionesIntegradasRem");
			this.ifzOperaciones = (OperacionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesFac!net.giro.contabilidad.logica.OperacionesRem");
			
			this.ifzOperIntegradas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOperaciones.setInfoSesion(this.loginManager.getInfoSesion());
			
			// POJO's
			this.pojoOperacion = new OperacionesIntegradas();
			this.pojoOperacionBorrar = new OperacionesIntegradas();
			
			// Busqueda Principal
			this.listOperaciones = new ArrayList<OperacionesIntegradas>();
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("codigoOperacion", "Codigo"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda operaciones
			this.listBusquedaOperaciones = new ArrayList<Operaciones>();
			this.tiposBusquedaOperaciones = new ArrayList<SelectItem>();
			this.tiposBusquedaOperaciones.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaOperaciones.add(new SelectItem("id", "ID"));
			this.campoBusquedaOperaciones = this.tiposBusquedaOperaciones.get(0).getValue().toString();
			this.valorBusquedaOperaciones = "";
			this.numPaginaBusquedaOperaciones = 1;
		} catch (Exception e) {
			log.error("Error en constructor OperacionesIntegradasAction", e);
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
			
			this.ifzOperIntegradas.orderBy("id desc");
			this.listOperaciones = this.ifzOperIntegradas.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 1000);
			if (this.listOperaciones.isEmpty()) {
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
			log.error("Error en OperacionesIntegradasAction.buscar", e);
		}
	}
	
	public void nuevo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoOperacion = new OperacionesIntegradas();
			this.pojoOperacionBorrar = null;
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasAction.buscar", e);
		}
	}
	
	public void editar() {}
	
	public void guardar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (! validarOperacionIntegrada())
				return;

			this.pojoOperacion.setModificadoPor(this.usuarioId);
			this.pojoOperacion.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.pojoOperacion.getId() == null || this.pojoOperacion.getId() <= 0L) {
				this.pojoOperacion.setCreadoPor(this.usuarioId);
				this.pojoOperacion.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD
				this.pojoOperacion.setId(this.ifzOperIntegradas.save(this.pojoOperacion));
				// Agregamos a la lista
				this.listOperaciones.add(this.pojoOperacion);
			} else {
				// Actualizamos en la BD
				this.ifzOperIntegradas.update(this.pojoOperacion);
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasAction.guardar", e);
		}
	}
	
	public void borrar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoOperacionBorrar != null) {
				// Borramos de la bd si corresponde
				if (this.pojoOperacionBorrar.getId() != null && this.pojoOperacionBorrar.getId() > 0L)
					this.ifzOperIntegradas.delete(this.pojoOperacionBorrar.getId());
				
				// Borramos de la lista
				this.listOperaciones.remove(this.pojoOperacionBorrar);
				this.pojoOperacionBorrar = null;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasAction.borrar", e);
		}
	}

	private boolean validarOperacionIntegrada() {
		try {
			this.operacion = false;
			this.mensaje = "";
			this.tipoMensaje = 0;

			if (this.pojoOperacion == null) {
				this.operacion = true;
				this.mensaje = "No lleno los datos correspondientes";
				this.tipoMensaje = -1;
				return false;
			}

			if (this.pojoOperacion.getIdOperacion() == null) {
				this.operacion = true;
				this.mensaje = "Debe seleccionar una operacion";
				this.tipoMensaje = -1;
				return false;
			}
			
			if (null != this.ifzOperIntegradas.comprobarOperacionIntegrada(this.pojoOperacion)) {
				this.operacion = true;
				this.mensaje = "Ya existe la Operacion seleccionada en el Listado";
				this.tipoMensaje = -1;
				return false;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasAction.validarOperacionIntegrada", e);
		}
		return true;
	}

	// ---------------------------------------------------------------------------------
	// OPERACIONES
	// ---------------------------------------------------------------------------------

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
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaOperaciones))
				this.campoBusquedaOperaciones = tiposBusquedaOperaciones.get(0).getValue().toString();
			
			this.listBusquedaOperaciones = this.ifzOperaciones.findLikeProperty(this.campoBusquedaOperaciones, this.valorBusquedaOperaciones, 120);
			
			if (this.listBusquedaOperaciones == null || this.listBusquedaOperaciones.isEmpty()) {
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
			log.error("Error en OperacionesIntegradasAction.buscarOperaciones", e);
		}
	}
	
	public void seleccionarOperacion() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			nuevaBusquedaOperaciones();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasAction.seleccionarOperacion", e);
		}
	}

	// ---------------------------------------------------------------------------------
	// PROPIEDADES
	// ---------------------------------------------------------------------------------
	
	public String getOperacionDesc() {
		if(this.pojoOperacion != null && this.pojoOperacion.getIdOperacion() != null && this.pojoOperacion.getIdOperacion().getId() != null && this.pojoOperacion.getIdOperacion().getId() > 0L)
			return this.pojoOperacion.getIdOperacion().getDescripcion();
		return "";
	}
	
	public void setOperacionDesc(String value) {}
	
	public List<OperacionesIntegradas> getListOperaciones() {
		return listOperaciones;
	}

	public void setListOperaciones(List<OperacionesIntegradas> listOperaciones) {
		this.listOperaciones = listOperaciones;
	}

	public OperacionesIntegradas getPojoOperacion() {
		return pojoOperacion;
	}

	public void setPojoOperacion(OperacionesIntegradas pojoOperacion) {
		this.pojoOperacion = pojoOperacion;
	}

	public OperacionesIntegradas getPojoOperacionBorrar() {
		return pojoOperacionBorrar;
	}

	public void setPojoOperacionBorrar(OperacionesIntegradas pojOperacionBorrar) {
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

	public List<Operaciones> getListBusquedaOperaciones() {
		return listBusquedaOperaciones;
	}

	public void setListBusquedaOperaciones(List<Operaciones> listBusquedaOperaciones) {
		this.listBusquedaOperaciones = listBusquedaOperaciones;
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
}
