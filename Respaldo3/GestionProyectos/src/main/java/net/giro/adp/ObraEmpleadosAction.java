package net.giro.adp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraEmpleadoExt;
import net.giro.adp.beans.ObraExt;
import net.giro.adp.logica.ObraEmpleadoRem;
import net.giro.adp.logica.ObraRem;
import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="empObraAction")
public class ObraEmpleadosAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ObraEmpleadosAction.class);
	private LoginManager loginManager; 
	// Obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	// Obras Empleados
	private ObraEmpleadoRem ifzObraEmpleados;
	private List<ObraEmpleadoExt> listObraEmpleados;
	private ObraEmpleadoExt pojoObraEmpleado;
	private int numPaginaObraEmpleados;
	// Busqueda
	private List<SelectItem> busquedaOpciones;	
	private String busquedaCampo;
	private String busquedaValor;
	private int busquedaTipo;
	private int busquedaPaginas;
	private boolean busquedaAdministrativas;
	// Busqueda empleados
	private EmpleadoRem ifzEmpleados;
	private List<Empleado> listEmpleados;
	private Empleado pojoEmpleado;
	private List<SelectItem> busquedaEmpleadosOpciones;	
	private String busquedaEmpleadosCampo;
	private String busquedaEmpleadosValor;
	private int busquedaEmpleadosPagina;
	private Date fechaBase;
	private boolean activarFechaBase;
	// Variables de control
	private boolean incompleto;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
    private boolean permiteModificar;
	// PERMISOS
	private boolean permisoAgregar;
	private boolean permisoEditar;
	private boolean permisoBorrar;
	private boolean permisoImprimir;
    // PERFILES
	private boolean PERFIL_ADMINISTRATIVO;
	// DEBUG
	private HashMap<String, String> paramsRequest;
	private boolean isDebug;
	
	public ObraEmpleadosAction() {
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

			// EVALUACION DE PERFILES
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.PERFIL_ADMINISTRATIVO = ((resValue != null && "S".equals(resValue)) ? true : false);

			// DEBUG
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = this.paramsRequest.containsKey("DEBUG") ? true : false;
			
			// Interfaces
			ctx = new InitialContext();
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzObraEmpleados = (ObraEmpleadoRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraEmpleadoFac!net.giro.adp.logica.ObraEmpleadoRem");
			
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzObraEmpleados.setInfoSesion(this.loginManager.getInfoSesion()); 
			
			this.ifzObras.showSystemOuts(false);
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("*", "Coincidencia"));
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "ID"));
			this.busquedaCampo = this.busquedaOpciones.get(0).getValue().toString();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
			this.numPaginaObraEmpleados = 1;
			
			// Busquera empleados
			this.busquedaEmpleadosOpciones = new ArrayList<SelectItem>();
			this.busquedaEmpleadosOpciones.add(new SelectItem("*", "Coincidencia"));
			this.busquedaEmpleadosOpciones.add(new SelectItem("nombre", "Nombre"));
			this.busquedaEmpleadosOpciones.add(new SelectItem("id", "ID"));
			nuevaBusquedaEmpleados();
		} catch (Exception e) {
			log.error("Error en constructor GestionProyectos.EmpleadosObrasAction", e);
		} finally {
			establecerPermisos();
		}
	}
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = "nombre";
			
			if (this.listObras == null)
				this.listObras = new ArrayList<Obra>();
			this.listObras.clear();

			this.busquedaTipo = 0;
			if (this.busquedaAdministrativas)
				this.busquedaTipo = 4;

			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo, false, "", 0);
			if (this.listObras == null || this.listObras.isEmpty())
				control(2, "La Busqueda no genero resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
    	}
	}
	
	public void ver() {
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "No selecciono una Obra");
				return;
			}
    		controlLog("Validando permiso de escritura");
    		validarPermisos();
			
			this.permiteModificar = true;
			if(this.pojoObra.getEstatus().equals(10000798L)) 
				this.permiteModificar = false;
			
			this.ifzObraEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObraEmpleados = this.ifzObraEmpleados.findExtAll(this.pojoObra.getId()); //.findByPropertyExt("idObra.id", this.pojoObra.getId(), 0);
			if (this.listObraEmpleados == null || this.listObraEmpleados.isEmpty()) 
				return;
			
			Collections.sort(this.listObraEmpleados, new Comparator<ObraEmpleadoExt>() {
		    	@Override
		        public int compare(ObraEmpleadoExt o1, ObraEmpleadoExt o2) {
		    		return o1.getEmpleadoNombreApellidos().compareTo(o2.getEmpleadoNombreApellidos());
		        }
			});
		} catch(Exception e) {
			control("Ocurrio un problema al consultar los Empleados de la Obra seleccionada", e);
    	}
	}
	
	public void eliminar() {
		try {
			control();
			// Eliminamos de la BD
			if (this.pojoObraEmpleado != null && this.pojoObraEmpleado.getId() != null && this.pojoObraEmpleado.getId() > 0L) 
				this.ifzObraEmpleados.delete(this.pojoObraEmpleado.getId());
			// Eliminamos de la lista
			this.listObraEmpleados.remove(this.pojoObraEmpleado);
	    	// Recargamos empleados
	    	ver();
		} catch(Exception e) {
			control(true, 1, e.getMessage(), e);
    		log.error("Error en GestionProyectos.EmpleadosObrasAction.eliminar", e);
    	}
	}

	private void control() {
		this.incompleto = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
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
		log.info("\n\n" + this.getClass().getCanonicalName() + " :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje);
	}

	// -------------------------------------------------------------------------------------------------------------------
	// Busqueda Empleados
	// -------------------------------------------------------------------------------------------------------------------

    public void nuevaBusquedaEmpleados() {
    	control();
		this.busquedaEmpleadosCampo = this.busquedaEmpleadosOpciones.get(0).getValue().toString();
		this.busquedaEmpleadosValor = "";
		this.busquedaEmpleadosPagina = 1;
		
    	this.pojoObraEmpleado = new ObraEmpleadoExt();
    	this.listEmpleados = new ArrayList<Empleado>();
    	this.pojoEmpleado = new Empleado();
    	this.fechaBase = asignarFechaBase();
    }
    
    public void buscarEmpleados() {
    	try {
    		control();
			if (this.busquedaEmpleadosCampo == null || "".equals(this.busquedaEmpleadosCampo.trim()))
				this.busquedaEmpleadosCampo = this.busquedaEmpleadosOpciones.get(0).getValue().toString();
			if ("*".equals(this.busquedaEmpleadosCampo.trim()))
				this.busquedaEmpleadosCampo = "id";

			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.listEmpleados = this.ifzEmpleados.findLikeSemanales(this.busquedaEmpleadosValor, this.busquedaEmpleadosCampo);
			if (this.listEmpleados == null || this.listEmpleados.isEmpty())
				control(2, "La busqueda no genero resultados");
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar los Empleados", e);
    	}
    }

    public void seleccionarEmpleado() {
    	try {
    		control();
    		if (this.pojoEmpleado == null || this.pojoEmpleado.getId() == null || this.pojoEmpleado.getId() <= 0L) {
        		control(-1, "Ocurrio un problema al recuperar el Empleado seleccionado");
    			return;
    		}
    		
    		controlLog("Comprobando empleado en Obra ... ");
        	if (this.listObraEmpleados == null) 
        		this.listObraEmpleados = new ArrayList<ObraEmpleadoExt>();
        	for (ObraEmpleadoExt var : this.listObraEmpleados) {
        		if (var.getEmpleadoId().longValue() == this.pojoEmpleado.getId().longValue()) {
            		controlLog("El Empleado seleccionado ya existe en la lista");
            		control(-1, "Este empleado ya existe en " + (this.pojoObra.getTipoObra() == 1 ? "la obra" : "el proyecto"));
            		return;
        		}
        	}
        	
        	guardarEmpleadoObra();
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar añadir a la Obra el Empleado especificado", e);
    	} finally {
        	nuevaBusquedaEmpleados();
    	}
    }
    
    private void guardarEmpleadoObra() {
    	Respuesta respuesta = null;
    	EmpleadoExt extendido = null;
    	ObraExt extendida = null;
    	
    	try {
			controlLog("Extiendo Empleado seleccionado ... ");
			extendido = this.ifzEmpleados.convertir(this.pojoEmpleado);
			
			controlLog("Extiendo Obra ... ");
			extendida = this.ifzObras.findByIdExt(this.pojoObra.getId());

			controlLog("Generando asignacion ... ");
    		this.pojoObraEmpleado = new ObraEmpleadoExt();
    		this.pojoObraEmpleado.setIdEmpleado(extendido);
    		this.pojoObraEmpleado.setNombreEmpleado(extendido.getNombre());
    		this.pojoObraEmpleado.setIdObra(extendida);
    		this.pojoObraEmpleado.setNombreObra(extendida.getNombre());
    		this.pojoObraEmpleado.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
    		this.pojoObraEmpleado.setFechaCreacion(Calendar.getInstance().getTime());
    		this.pojoObraEmpleado.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
    		this.pojoObraEmpleado.setFechaModificacion(Calendar.getInstance().getTime());
    		
    		// Guardamos en la BD
			controlLog("Guardando asignacion ... ");
			this.ifzObraEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
    		this.pojoObraEmpleado.setId(this.ifzObraEmpleados.save(this.pojoObraEmpleado));
    		this.listObraEmpleados.add(this.pojoObraEmpleado);
    		
    		if (! this.activarFechaBase)
    			this.fechaBase = Calendar.getInstance().getTime();

    		respuesta = this.ifzObraEmpleados.altaRetroactiva(this.pojoObraEmpleado.getId(), this.fechaBase);
    		if (respuesta.getErrores().getCodigoError() != 0L) 
    			control(-1, respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar agregar el Empleado seleccionado a la Obra", e);
    	}
    }

    private Date asignarFechaBase() {
    	Calendar cal = null;
    	
    	cal = Calendar.getInstance();
    	cal.setTime(Calendar.getInstance().getTime());
    	if (this.isPermisoAdmin()) {
	    	if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) 
	    		cal.add(Calendar.DAY_OF_MONTH, -1);
	    	if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) 
	    		cal.add(Calendar.DAY_OF_MONTH, -2);
	    	if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) 
	    		cal.add(Calendar.DAY_OF_MONTH, -3);
	    	if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) 
	    		cal.add(Calendar.DAY_OF_MONTH, -4);
    	}
    	
    	return cal.getTime();
    }
    
	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

	public boolean getScrolling() {
		if (this.listObraEmpleados != null && ! this.listObraEmpleados.isEmpty() && this.listObraEmpleados.size() > 9)
			return true;
		return false;
	}
	
	public void setScrolling(boolean value) {}
	
	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

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

	public List<ObraEmpleadoExt> getListObraEmpleados() {
		return listObraEmpleados;
	}

	public void setListObraEmpleados(List<ObraEmpleadoExt> listObraEmpleados) {
		this.listObraEmpleados = listObraEmpleados;
	}

	public ObraEmpleadoExt getPojoObraEmpleado() {
		if (pojoObraEmpleado == null) pojoObraEmpleado = new ObraEmpleadoExt();
		return pojoObraEmpleado;
	}

	public void setPojoObraEmpleado(ObraEmpleadoExt pojoObraEmpleado) {
		this.pojoObraEmpleado = pojoObraEmpleado;
	}

	public int getNumPaginaObraEmpleados() {
		return numPaginaObraEmpleados;
	}

	public void setNumPaginaObraEmpleados(int numPaginaObraEmpleados) {
		this.numPaginaObraEmpleados = numPaginaObraEmpleados;
	}

	public List<Empleado> getListEmpleados() {
		return listEmpleados;
	}

	public void setListEmpleados(List<Empleado> listEmpleados) {
		this.listEmpleados = listEmpleados;
	}

	public Empleado getPojoEmpleado() {
		if (pojoEmpleado == null) pojoEmpleado = new Empleado();
		return pojoEmpleado;
	}

	public void setPojoEmpleado(Empleado pojoEmpleado) {
		this.pojoEmpleado = pojoEmpleado;
	}

	public List<SelectItem> getBusquedaEmpleadosOpciones() {
		return busquedaEmpleadosOpciones;
	}

	public void setBusquedaEmpleadosOpciones(List<SelectItem> busquedaEmpleadosOpciones) {
		this.busquedaEmpleadosOpciones = busquedaEmpleadosOpciones;
	}

	public String getBusquedaEmpleadosCampo() {
		return busquedaEmpleadosCampo;
	}

	public void setBusquedaEmpleadosCampo(String busquedaEmpleadosCampo) {
		this.busquedaEmpleadosCampo = busquedaEmpleadosCampo;
	}

	public String getBusquedaEmpleadosValor() {
		return busquedaEmpleadosValor;
	}

	public void setBusquedaEmpleadosValor(String busquedaEmpleadosValor) {
		this.busquedaEmpleadosValor = busquedaEmpleadosValor;
	}

	public int getBusquedaEmpleadosPagina() {
		return busquedaEmpleadosPagina;
	}

	public void setBusquedaEmpleadosPagina(int busquedaEmpleadosPagina) {
		this.busquedaEmpleadosPagina = busquedaEmpleadosPagina;
	}

	public boolean isPermiteModificar() {
		return permiteModificar;
	}

	public void setPermiteModificar(boolean permiteModificar) {
		this.permiteModificar = permiteModificar;
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
	
	public boolean isDebugging() { 
		if (this.paramsRequest.containsKey("DEBUG")) 
			return true;
		return this.isDebug;
	}
	
	public void setDebugging(boolean value) { }

	public boolean isPermisoAdmin() {
		return ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()));
	}

	public void setPermisoAdmin(boolean permisoAdmin) {}

	public Date getFechaBase() {
		return fechaBase;
	}

	public void setFechaBase(Date fechaBase) {
		this.fechaBase = fechaBase;
	}
	
	public boolean isActivarFechaBase() {
		return activarFechaBase;
	}
	
	public void setActivarFechaBase(boolean activarFechaBase) {
		this.activarFechaBase = activarFechaBase;
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
