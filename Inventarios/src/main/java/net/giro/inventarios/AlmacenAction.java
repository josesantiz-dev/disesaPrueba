package net.giro.inventarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.logica.ClientesRem;
import net.giro.ne.beans.Sucursal;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenExt;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.navegador.LoginManager;
import net.giro.navegador.comun.Permiso;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="almacenAction")
public class AlmacenAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AlmacenAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private Permiso permisos;
	// ------------------------------------------------------------------
	private AlmacenRem ifzAlmacen;
	private List<Almacen> listAlmacenes;
	private Almacen idAlmacen;
	private AlmacenExt pojoAlmacen;
	private AlmacenExt pojoAlmacenEliminar;
	private boolean puedeEditar;
	private List<SelectItem> tiposAlmacenes;
	private int resultadoValidacion;
	private int numPagina;
	// Busqueda
	private List<SelectItem> listTiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int tipoCampoBusqueda;
	// Sucursal
	private SucursalesRem ifzSucursal;
	private List<Sucursal> listSucursales;
	private List<SelectItem> listSucursalesItems;
	// Empleados
	private EmpleadoRem ifzEmpleados;
	private List<Empleado> listEmpleados;
	private List<SelectItem> listEmpleadosItems;
	private long idEncargado;
	// Domicilio
	private ClientesRem ifzDomicilios;
	private DomicilioExt pojoDomicilioExt;
	private List<Estado> listEstados;
	private List<SelectItem> listEstadosItems;
	private Estado pojoEstado;
	private List<Municipio> listMunicipios;
	private Municipio pojoMunicipio;
	private String busquedaMunicipio;
	private int numPaginaMunicipio;
	private List<Localidad> listLocalidades;
	private Localidad pojoLocalidad;
	private String busquedaLocalidad;
	private int numPaginaLocalidad; 
	private List<Colonia> listColonias;
	private Colonia pojoColonia;
	private String busquedaColonia;
	private int numPaginaColonia;
	private List<ConValores> listTmpCatDomicilios1;
	private List<SelectItem> listCatDomicilios1;
	private List<ConValores> listTmpCatDomicilios2;
	private List<SelectItem> listCatDomicilios2;
	// control
	private boolean operacionCompleta;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	// PERFILES
	private boolean perfilAdministrativo;
	private boolean limiteUbicacionGeografica;
	
	public AlmacenAction() {
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

			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilAdministrativo = ((resValue != null && "S".equals(resValue)) ? true : false);
			log.info("Perfil EGRESOS_OPERACION: " + (this.perfilAdministrativo ? "SI" : "NO"));

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			
			this.limiteUbicacionGeografica = false;
			resValue = this.entornoProperties.getString("limiteUbicacionGeografica");
			if (resValue != null && ! "".equals(resValue.trim())) 
				this.limiteUbicacionGeografica = Boolean.parseBoolean(resValue);
			
			// Permisos
			resValue = (this.entornoProperties.containsKey("ALMACEN") ? this.entornoProperties.getString("ALMACEN") : "");
			resValue = (resValue != null && ! "".equals(resValue.trim())) ? resValue.trim() : "0";
			this.permisos = this.loginManager.getPermisos(this.loginManager.getIdAplicacion(), Long.parseLong(resValue));
			
			ctx = new InitialContext();
			this.ifzSucursal = (SucursalesRem) ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzAlmacen = (AlmacenRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzDomicilios = (ClientesRem) ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
	
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDomicilios.setInfoSecion(this.loginManager.getInfoSesion());
			
			this.puedeEditar = true;
			this.operacionCompleta = true;
			this.numPaginaMunicipio = 1;
			this.numPaginaLocalidad = 1; 
			this.numPaginaColonia = 1;
			this.pojoAlmacen = new AlmacenExt();
			this.listAlmacenes = new ArrayList<Almacen>();
			this.listSucursales = new ArrayList<Sucursal>();
			this.listCatDomicilios1 = new ArrayList<SelectItem>();
			this.listCatDomicilios2 = new ArrayList<SelectItem>();
			this.listEstadosItems	= new ArrayList<SelectItem>();
			this.listTmpCatDomicilios1 = new ArrayList<ConValores>();
			this.listTmpCatDomicilios2 = new ArrayList<ConValores>();
			
			// Busqueda principal
			this.listTiposBusqueda = new ArrayList<SelectItem>();
			this.listTiposBusqueda.add(new SelectItem("*","Coincidencia"));
			this.listTiposBusqueda.add(new SelectItem("nombre", "Nombre"));
			this.listTiposBusqueda.add(new SelectItem("identificador", "Identificador"));
			this.listTiposBusqueda.add(new SelectItem("nombreEncargado", "Encargado"));
			this.listTiposBusqueda.add(new SelectItem("nombreSucursal", "Sucursal"));
			this.listTiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.listTiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Tipos de Almacenes
			this.tiposAlmacenes = new ArrayList<SelectItem>();
			this.tiposAlmacenes.add(new SelectItem(1, "General"));
			this.tiposAlmacenes.add(new SelectItem(2, "Obra"));
			if (this.perfilAdministrativo) {
				this.tiposAlmacenes.add(new SelectItem(3, "General (Administrativo)"));
				this.tiposAlmacenes.add(new SelectItem(4, "Obra (Administrativo)"));
			}
			
			cargarSucursales();
			cargaEstados();
			nuevoDomicilio();
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar el EJB", e);
		}
	}
	
	public void buscar() {
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			if (! this.permisos.getConsultar()) {
				control(301, "No tiene permitido consultar informacion");
				return;
			}
			
			this.numPagina = 1;
			this.campoBusqueda = (this.campoBusqueda != null && ! "".equals(this.campoBusqueda.trim())) ? this.campoBusqueda.trim() : this.listTiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = (this.valorBusqueda != null && ! "".equals(this.valorBusqueda.trim())) ? this.valorBusqueda.trim() : "";
			this.listAlmacenes = this.ifzAlmacen.findLikeProperty(this.campoBusqueda, this.valorBusqueda);
			this.listAlmacenes = (this.listAlmacenes != null) ? this.listAlmacenes : new ArrayList<Almacen>();
			if (this.listAlmacenes == null || this.listAlmacenes.isEmpty()) 
				control(2, "la busqueda no regreso resultados");
			
			Collections.sort(this.listAlmacenes, new Comparator<Almacen>() {
				@Override
				public int compare(Almacen o1, Almacen o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
		} catch (Exception e) {
    		control("Ocurrio un problema al realizar la consulta de Almacenes", e);
		}
	}

	public void nuevo() {
		control();
		// Validamos permiso de Lectura/Consulta
		if (! this.permisos.getEditar()) {
			control(-1, "No tiene permitido Añadir/Editar informacion");
			controlLog("301 - No tiene permitido Añadir/Editar informacion");
			return;
		}
		
		this.pojoAlmacen = new AlmacenExt();
		this.pojoDomicilioExt = new DomicilioExt();
		this.idEncargado = 0L;
		cargarEmpleados();
		nuevoDomicilio();
	}

	public void editar() {
		try {
			control();
			// Recuperamos el Almacen seleccionado
			this.pojoAlmacen = this.ifzAlmacen.convertir(this.idAlmacen);
			if (this.pojoAlmacen == null || this.pojoAlmacen.getId() == null || this.pojoAlmacen.getId() <= 0L) {
				control(-1, "Debe indicar un Almacen");
				return;
			}
			
			// Tipo de Almacen/Bodega
			if (this.pojoAlmacen.getTipo() > 2 && ! this.perfilAdministrativo) {
				control(-1, "No tienes los permisos suficientes para editar Almacenes Administrativos");
				return;
			}
			
			// Encargado de Almacen/Bodega
			this.idEncargado = 0L;
			if (this.pojoAlmacen.getIdEncargado() != null && this.pojoAlmacen.getIdEncargado().getId() != null && this.pojoAlmacen.getIdEncargado().getId() > 0L)
				this.idEncargado = this.pojoAlmacen.getIdEncargado().getId();
			cargarEmpleados();

			// Desglosamos domicilio
			nuevoDomicilio();
			if (this.pojoAlmacen.getPojoDomicilio() != null) {
				this.pojoDomicilioExt = this.pojoAlmacen.getPojoDomicilio();
				desglosaDomicilio();
			}
    	} catch (Exception e) {
    		control("Error en editar", e);
    	}
	}

	public void guardar() {
		int index = -1;
		
		try {
			control();
			if (! validaGuardarAlmacen()) 
				return;
			
			if (this.pojoAlmacen.getId() == null || this.pojoAlmacen.getId() <= 0L) {
				this.pojoAlmacen.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoAlmacen.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoAlmacen.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoAlmacen.setFechaModificacion(Calendar.getInstance().getTime());
				
				this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoAlmacen.setId(this.ifzAlmacen.save(this.pojoAlmacen));
				this.idAlmacen = this.ifzAlmacen.convertir(this.pojoAlmacen);
				this.listAlmacenes.add(0, this.idAlmacen);
			} else {
				this.pojoAlmacen.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoAlmacen.setFechaCreacion(Calendar.getInstance().getTime());
				this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzAlmacen.update(this.pojoAlmacen);

				// Actualizo en el listado si corresponde
				if (this.listAlmacenes != null && ! this.listAlmacenes.isEmpty()) {
					this.idAlmacen = this.ifzAlmacen.convertir(this.pojoAlmacen);
					for (Almacen item : this.listAlmacenes) {
						if (this.idAlmacen.getId().longValue() == item.getId().longValue()) {
							index = this.listAlmacenes.indexOf(item);
							this.listAlmacenes.remove(index);
							if (index >= this.listAlmacenes.size())
								this.listAlmacenes.add(0, this.idAlmacen);
							else
								this.listAlmacenes.add(index, this.idAlmacen);
							break;
						}
					}
				}
			}
			
			nuevo();
			nuevoDomicilio();
			this.idEncargado = 0L;
			this.operacionCompleta = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar el Almacen", e);
		}
	}
	
	public void eliminar() {
		int index = -1;
		
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			if (! this.permisos.getBorrar()) {
				control(301, "No tiene permitido Borrar/Eliminar informacion");
				return;
			}
			
			if (this.idAlmacen == null || this.idAlmacen.getId() == null || this.idAlmacen.getId() <= 0L) {
				control(-1, "Debe indicar un Almacen");
				return;
			}
			
			// Cancelamos el Almacen
			this.idAlmacen.setEstatus(1);
			this.idAlmacen.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.idAlmacen.setFechaCreacion(Calendar.getInstance().getTime());
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacen.update(this.idAlmacen);

			// Actualizo en el listado si corresponde
			if (this.listAlmacenes != null && ! this.listAlmacenes.isEmpty()) {
				for (Almacen item : this.listAlmacenes) {
					if (this.idAlmacen.getId().longValue() == item.getId().longValue()) {
						index = this.listAlmacenes.indexOf(item);
						this.listAlmacenes.remove(index);
						if (index >= this.listAlmacenes.size())
							this.listAlmacenes.add(0, this.idAlmacen);
						else
							this.listAlmacenes.add(index, this.idAlmacen);
						break;
					}
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Eliminar el Almacen", e);
		}
	}

	private boolean validaGuardarAlmacen() {
		List<Almacen> listAux = null;
		long idPrevio = 0L;
		
		try {
			// Validamos permiso de Lectura/Consulta
			if (! this.permisos.getEditar()) {
				control(-1, "No tiene permitido Añadir/Editar informacion");
				controlLog("301 - No tiene permitido Añadir/Editar informacion");
				return false;
			}
			
			if (this.pojoAlmacen.getNombre().trim().equals("")) {
				control(-10, "Indique el nombre del Almacen");
				return false;
			}
			
			if (this.pojoAlmacen.getIdentificador().trim().equals("")) {
				control(-11, "Indique el identificador del Almacen");
				return false;
			}

			if (this.pojoDomicilioExt.getDomicilio() == null) {
				control(-12, "Debe indicar el domicilio");
				return false;
			}
			
			if (this.pojoDomicilioExt.getDomicilio().trim().equals("")) {
				control(-12, "Debe indicar el domicilio");
				return false;
			}
			
			// Comprobamos almacen principal
			if (this.pojoAlmacen.getTipo() == 1 || this.pojoAlmacen.getTipo() == 3) {
				listAux = this.ifzAlmacen.findByProperty("idSucursal", this.pojoAlmacen.getSucursal().getId());
				if (listAux != null && ! listAux.isEmpty()) {
					for (Almacen var : listAux) {
						if (var.getTipo() != this.pojoAlmacen.getTipo())
							continue;
						if (this.pojoAlmacen.getId() != null && this.pojoAlmacen.getId() > 0L && this.pojoAlmacen.getId().longValue() == var.getId().longValue())
							continue;
						control(-13, "La sucursal seleccionada ya tiene un Almacen General asignado.");
						return false;
					}
				}
			}

			// Comprobamos nombre almacen
			idPrevio = (this.pojoAlmacen.getId() == null ? 0L : this.pojoAlmacen.getId());
			if (this.ifzAlmacen.comprobarNombre(this.pojoAlmacen.getNombre(), idPrevio)) {
				control(-14, "Ya existe un Almacen con este nombre asignado.");
				return false;
			}

			// Comprobamos identificador almacen
			listAux = this.ifzAlmacen.findByProperty("identificador", this.pojoAlmacen.getIdentificador());
			if (listAux != null && ! listAux.isEmpty()) {
				for (Almacen var : listAux) {
					if (this.pojoAlmacen.getId() != null && this.pojoAlmacen.getId() > 0L && this.pojoAlmacen.getId().longValue() == var.getId().longValue())
						continue;
					control(-15, "Ya existe un Almacen con este identificador asignado.");
					return false;
				}
			}

			this.pojoAlmacen.setIdEncargado(null);
			this.pojoAlmacen.setNombreEncargado("");
			if (this.idEncargado > 0L) {
				for (Empleado var : this.listEmpleados) {
					if (this.idEncargado == var.getId().longValue()) {
						this.pojoAlmacen.setIdEncargado(var);
						break;
					}
				}
			}
			
			// Asigno Empresa
			this.pojoAlmacen.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
		} catch (Exception e) {
			control("Ocurrio un problema al validar los datos para el Almacen", e);
			return false;
		}
		
		return true;
	}

	private void control() {
		this.operacionCompleta = true;
		this.operacionCancelada = false;
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
		this.operacionCompleta = false;
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim().replace("\n", "<br/>") : "Ocurrio un problema no controlado al realizar la operacion");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	private void controlLog(String mensaje) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "???");
		log.info("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje + "\n");
	}
	
	// ------------------------------------------------------------------------------------------
	// SUCURSALES
	// ------------------------------------------------------------------------------------------

	private void cargarSucursales() {
		try {
			control();
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSucursales = this.ifzSucursal.findAll();
			this.listSucursales = (this.listSucursales != null) ? this.listSucursales : new ArrayList<Sucursal>();

			this.listSucursalesItems = new ArrayList<SelectItem>();
			for (Sucursal s : this.listSucursales)
				this.listSucursalesItems.add(new SelectItem(s.getId(), s.getSucursal()));
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Sucursales", e);
		}
	}

	// ------------------------------------------------------------------------------------------
	// EMPLEADOS
	// ------------------------------------------------------------------------------------------

	private void cargarEmpleados() {
		try {
			control();
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.listEmpleados = this.ifzEmpleados.findAll();
			this.listEmpleados = (this.listEmpleados != null) ? this.listEmpleados : new ArrayList<Empleado>();
			
			Collections.sort(this.listEmpleados, new Comparator<Empleado>() {
				@Override
				public int compare(Empleado o1, Empleado o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
			
			filtrarEmpleados();
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Empleados", e);
		}
	}
	
	public void filtrarEmpleados() {
		this.listEmpleadosItems = new ArrayList<SelectItem>();
		for (Empleado e : this.listEmpleados) {
			if (this.limiteUbicacionGeografica) {
				if (this.pojoAlmacen != null && this.pojoAlmacen.getId() != null && this.pojoAlmacen.getId() > 0L) {
					if (this.pojoAlmacen.getSucursal() != null && this.pojoAlmacen.getSucursal().getId() > 0L) {
						if (this.pojoAlmacen.getSucursal().getId() != e.getIdSucursal().longValue())
							continue;
					}
				}
			}
			// Añadimos a listado
			this.listEmpleadosItems.add(new SelectItem(e.getId(), e.getNombre()));
		}
	}

	// ------------------------------------------------------------------------------------------
	// DOMICILIO
	// ------------------------------------------------------------------------------------------

	public void nuevoDomicilio() {
    	if (this.pojoDomicilioExt == null) {
			this.pojoDomicilioExt = new DomicilioExt();
			this.pojoDomicilioExt.setEstatus(true);
			this.pojoDomicilioExt.setDomicilio("");
			this.pojoDomicilioExt.setCatDomicilio1(0L);
			this.pojoDomicilioExt.setCatDomicilio2(0L);
			this.pojoDomicilioExt.setCatDomicilio3(0L);
			this.pojoDomicilioExt.setCatDomicilio4(0L);
			this.pojoDomicilioExt.setCatDomicilio5(0L);
			this.pojoDomicilioExt.setDescripcionDomicilio1("");
			this.pojoDomicilioExt.setDescripcionDomicilio2("");
			this.pojoDomicilioExt.setDescripcionDomicilio3("");
			this.pojoDomicilioExt.setDescripcionDomicilio4("");
			this.pojoDomicilioExt.setDescripcionDomicilio5("");
		} 
    	
		desglosaDomicilio();
	}
	
	public void desglosaDomicilio() {
    	try {
        	this.pojoColonia = null;
    		this.pojoLocalidad = null;
    		this.pojoMunicipio = null;
    		this.pojoEstado = null;
    		
    		if (this.pojoDomicilioExt != null) {
    			if (this.pojoDomicilioExt.getColonia() != null && this.pojoDomicilioExt.getColonia().getId() > 0) 
    				this.pojoColonia = this.pojoDomicilioExt.getColonia();
    			
    			if (this.pojoColonia != null)
    				this.pojoLocalidad = this.pojoColonia.getLocalidad();
    			
    			if (this.pojoLocalidad != null)
    				this.pojoMunicipio = this.pojoLocalidad.getMunicipio();
    			
    			if (this.pojoMunicipio != null)
    				this.pojoEstado = this.pojoMunicipio.getEstado();
    		}
			
			cargaTiposDomicilio();
		} catch (Exception e) {
			control("Ocurrio un problema al desglosar el Domicilio", e);
		}
    }
	
	private void cargaTiposDomicilio() {
		HashMap<String, String> params = null;
		
		try {
    		control();
			this.listTmpCatDomicilios1 = new ArrayList<ConValores>();
			this.listTmpCatDomicilios2 = new ArrayList<ConValores>();
			this.listCatDomicilios1 = new ArrayList<SelectItem>();
			this.listCatDomicilios2 = new ArrayList<SelectItem>();
			
			params = new HashMap<String, String>();
			params.put("atributo2", "1");
			cargaTipoDom(params, this.listCatDomicilios1, this.listTmpCatDomicilios1);
			log.info("cargaTiposDomicilio: listCatDomicilios1: " + listCatDomicilios1.size());
			
			params.clear();
			params.put("atributo2", "2");
			cargaTipoDom(params, this.listCatDomicilios2, this.listTmpCatDomicilios2);
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los tipos de Domicilio", e);
		}
	}
	
	private void cargaTipoDom(HashMap<String, String> params, List<SelectItem> items, List<ConValores> listDom) {
		try {
			control();
			listDom.addAll(this.ifzDomicilios.buscarDomicilios(params));
			for (ConValores cv : listDom)
				items.add(new SelectItem(cv.getId(), cv.getValor()));
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Tipos de Domicilio", e);
		}
	}

	public boolean cargaEstados() {
    	try {
			control();
			this.ifzDomicilios.setInfoSecion(this.loginManager.getInfoSesion());
			this.listEstados = this.ifzDomicilios.buscarEstados();
			this.listEstados = (this.listEstados != null) ? this.listEstados : new ArrayList<Estado>();
			if (this.listEstados == null || this.listEstados.isEmpty()) {
				control("No se encontro ningun Estado", null);
				return false;
			}

			Collections.sort(this.listEstados, new Comparator<Estado>() {
				@Override
				public int compare(Estado o1, Estado o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});

    		this.listEstadosItems = new ArrayList<SelectItem>();
			for (Estado e : this.listEstados) 
				this.listEstadosItems.add(new SelectItem(e.getId(), e.getNombre()));
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar los Estados", e);
			return false;
		}
    	
    	return true;
    }
	
	public void cambioEstadoDom() {
		try {
			this.listMunicipios = new ArrayList<Municipio>();
			this.listLocalidades = new ArrayList<Localidad>();
			this.listColonias = new ArrayList<Colonia>();
			this.pojoMunicipio = null;
			this.pojoLocalidad = null;
			this.pojoColonia = null;
		} catch (Exception e) {
			control("Error en metodo cambioEstadoDom", e);
		}
	}
	
	public void buscarMunicipios() {
    	try {
			control();
			this.ifzDomicilios.setInfoSecion(this.loginManager.getInfoSesion());
			this.listMunicipios = this.ifzDomicilios.buscarMunicipio(this.pojoEstado, this.busquedaMunicipio);
			this.listMunicipios = (this.listMunicipios != null) ? this.listMunicipios : new ArrayList<Municipio>();
			if (this.listMunicipios == null || this.listMunicipios.isEmpty()) {
	    		control(2, "Busqueda sin resultados");
	    		return;
			}

			Collections.sort(this.listMunicipios, new Comparator<Municipio>() {
				@Override
				public int compare(Municipio o1, Municipio o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
    	} catch (Exception e) {
    		control("Ocurrio un problema al realizar la consulta de Municipios", e);
    	}
    }
    
    public void buscarLocalidades() {
    	try {
			control();
			this.ifzDomicilios.setInfoSecion(this.loginManager.getInfoSesion());
			this.listLocalidades = this.ifzDomicilios.buscarLocalidad(this.pojoMunicipio, this.busquedaLocalidad);
			this.listLocalidades = (this.listLocalidades != null) ? this.listLocalidades : new ArrayList<Localidad>();
			if (this.listLocalidades == null || this.listLocalidades.isEmpty()) {
	    		control(2, "Busqueda sin resultados");
	    		return;
			}

			Collections.sort(this.listLocalidades, new Comparator<Localidad>() {
				@Override
				public int compare(Localidad o1, Localidad o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
    	} catch (Exception e) {
    		control("Ocurrio un problema al realizar la consulta de Localidades", e);
    	}
    }
    
    public void buscarColonias() {
    	try {
			control();
			this.ifzDomicilios.setInfoSecion(this.loginManager.getInfoSesion());
			this.listColonias = this.ifzDomicilios.buscarColonia(this.pojoLocalidad, this.busquedaColonia);
			this.listColonias = (this.listColonias != null) ? this.listColonias : new ArrayList<Colonia>();
			if (this.listColonias == null || this.listColonias.isEmpty()) {
	    		control(2, "Busqueda sin resultados");
	    		return;
			}
			
			Collections.sort(this.listColonias, new Comparator<Colonia>() {
				@Override
				public int compare(Colonia o1, Colonia o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
    	} catch (Exception e) {
    		control("Ocurrio un problema al realizar la consulta de Colonias", e);
    	}
    }
	
	public boolean guardarDomicilio() {
		Respuesta respuesta = new Respuesta();
		
		try {
			control();
			if (! comprobarDireccion() || pojoColonia == null) {
				control(-12, "Direccion no valida.");
				return false;
			}
			
			this.pojoDomicilioExt.setPrincipal(true);
			this.pojoDomicilioExt.setEstatus(true);
			this.pojoDomicilioExt.setColonia(pojoColonia);
			
			log.info("PojoDomicilio.id(): " + this.pojoDomicilioExt.getId());
			this.ifzDomicilios.setInfoSecion(this.loginManager.getInfoSesion());
			respuesta = this.ifzDomicilios.salvar(this.pojoDomicilioExt);
			if (respuesta.getErrores().getCodigoError() > 0) {
				control(-1, respuesta.getErrores().getDescError());
				return false;
			} 
			
			this.pojoDomicilioExt = (DomicilioExt) respuesta.getBody().getValor("domicilio");
			this.pojoAlmacen.setPojoDomicilio(this.pojoDomicilioExt);
			log.info("pojoDomicilioExt: " + this.pojoDomicilioExt.getDescripcionDomicilio1());
		} catch (Exception e) {
			control("Error en el metodo guardarDomicilio", e);
			return false;
		}
		
		return true;
	}
	
	public boolean comprobarDireccion() {
		if ("".equals(this.pojoDomicilioExt.getDescripcionDomicilio1().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio2().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio3().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio4().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio5().trim()))
			return false;
		
		return true;
	}

	private ConValores getValorById(Long id, List<ConValores> lista) {
		for (ConValores cv : lista) {
			if (cv.getId() == id.longValue())
				return cv;
		}
		
		return null;
	}

	// ------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------------

	public Almacen getIdAlmacen() {
		return this.idAlmacen;
	}
	
	public void setIdAlmacen(Almacen idAlmacen) {
		this.idAlmacen = idAlmacen;
	}
	
	public AlmacenExt getPojoAlmacen() {
		return pojoAlmacen;
	}

	public void setPojoAlmacen(AlmacenExt pojoAlmacen) {
		this.pojoAlmacen = pojoAlmacen;
	}
	
	public String getDomicilio() {
		if (this.pojoAlmacen != null && this.pojoAlmacen.getDomicilio() != null && ! "".equals(this.pojoAlmacen.getDomicilio().trim()))
			return this.pojoAlmacen.getDomicilio();
		return "";
	}
	
	public void setDomicilio(String domicilio) {
		if (domicilio == null || "".equals(domicilio.trim()))
			return;
		this.pojoAlmacen.setDomicilio(domicilio);
	}
	
	public Long getIdEstadoDom() {
		return pojoEstado != null && pojoEstado.getId() > 0 ? pojoEstado.getId() : 0L;
	}
	
	private Estado getEstadoById(Long id) {
		for (Estado e : this.listEstados) {
			if (e.getId() == id.longValue()) 
				return e;
		}
		
		return null;
	}
	
	public void setIdEstadoDom(Long idEstadoDom) {
		try {
			this.pojoEstado = getEstadoById(idEstadoDom);
		} catch (Exception e) {
			log.error("Error en metodo setIdEstadoDom",e);
		}
	}
	
	public long getIdSucursal() {
		if (this.pojoAlmacen != null && this.pojoAlmacen.getSucursal() != null && this.pojoAlmacen.getSucursal().getId() > 0L)
			return this.pojoAlmacen.getSucursal().getId();
		return 0L;
	}

	public void setIdSucursal(long idSucursal) {
		if (idSucursal <= 0L)
			return;
		
		for (Sucursal s : this.listSucursales) {
			if (s.getId() == idSucursal) {
				this.pojoAlmacen.setSucursal(s);
				break;
			}
		}
	}
	
	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public boolean isOperacionCompleta() {
		return operacionCompleta;
	}

	public void setOperacionCompleta(boolean operacionCompleta) {
		this.operacionCompleta = operacionCompleta;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public void setResOperacion(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getTipoBusqueda() {
		return campoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.campoBusqueda = tipoBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public List<SelectItem> getListaCampoBusqueda() {
		return listTiposBusqueda;
	}

	public void setListaCampoBusqueda(List<SelectItem> listaCampoBusqueda) {
		this.listTiposBusqueda = listaCampoBusqueda;
	}

	public int getCampoBusqueda() {
		return tipoCampoBusqueda;
	}

	public void setCampoBusqueda(int campoBusqueda) {
		this.tipoCampoBusqueda = campoBusqueda;
	}

	public List<Almacen> getListaAlmacenesGrid() {
		return listAlmacenes;
	}

	public void setListaAlmacenesGrid(List<Almacen> listaAlmacenesGrid) {
		this.listAlmacenes = listaAlmacenesGrid;
	}

	public List<SelectItem> getListaCboSucursales() {
		return listSucursalesItems;
	}

	public void setListaCboSucursales(List<SelectItem> listaCboSucursales) {
		this.listSucursalesItems = listaCboSucursales;
	}

	public AlmacenExt getPojoAlmacenEliminar() {
		return pojoAlmacenEliminar;
	}

	public void setPojoAlmacenEliminar(AlmacenExt pojoAlmacenEliminar) {
		this.pojoAlmacenEliminar = pojoAlmacenEliminar;
	}

	public int getNumPaginaMunicipio() {
		return numPaginaMunicipio;
	}

	public void setNumPaginaMunicipio(int numPaginaMunicipio) {
		this.numPaginaMunicipio = numPaginaMunicipio;
	}

	public int getNumPaginaLocalidad() {
		return numPaginaLocalidad;
	}

	public void setNumPaginaLocalidad(int numPaginaLocalidad) {
		this.numPaginaLocalidad = numPaginaLocalidad;
	}

	public int getNumPaginaColonia() {
		return numPaginaColonia;
	}

	public void setNumPaginaColonia(int numPaginaColonia) {
		this.numPaginaColonia = numPaginaColonia;
	}

	public String getBusquedaMunicipio() {
		return busquedaMunicipio;
	}

	public void setBusquedaMunicipio(String busquedaMunicipio) {
		this.busquedaMunicipio = busquedaMunicipio;
	}

	public String getBusquedaLocalidad() {
		return busquedaLocalidad;
	}

	public void setBusquedaLocalidad(String busquedaLocalidad) {
		this.busquedaLocalidad = busquedaLocalidad;
	}

	public String getBusquedaColonia() {
		return busquedaColonia;
	}

	public void setBusquedaColonia(String busquedaColonia) {
		this.busquedaColonia = busquedaColonia;
	}

	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}

	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}

	public List<Colonia> getListColonias() {
		return listColonias;
	}

	public void setListColonias(List<Colonia> listColonias) {
		this.listColonias = listColonias;
	}

	public List<Estado> getListTmpEstado() {
		return listEstados;
	}

	public void setListTmpEstado(List<Estado> listTmpEstado) {
		this.listEstados = listTmpEstado;
	}

	public List<ConValores> getListTmpCatDomicilios1() {
		return listTmpCatDomicilios1;
	}

	public void setListTmpCatDomicilios1(List<ConValores> listTmpCatDomicilios1) {
		this.listTmpCatDomicilios1 = listTmpCatDomicilios1;
	}

	public List<ConValores> getListTmpCatDomicilios2() {
		return listTmpCatDomicilios2;
	}

	public void setListTmpCatDomicilios2(List<ConValores> listTmpCatDomicilios2) {
		this.listTmpCatDomicilios2 = listTmpCatDomicilios2;
	}

	public DomicilioExt getPojoDomicilio() {
		return pojoDomicilioExt;
	}

	public void setPojoDomicilio(DomicilioExt pojoDomicilio) {
		this.pojoDomicilioExt = pojoDomicilio;
	}

	public List<SelectItem> getListEstado() {
		return listEstadosItems;
	}

	public void setListEstado(List<SelectItem> listEstado) {
		this.listEstadosItems = listEstado;
	}

	public Municipio getPojoMunicipio() {
		return pojoMunicipio;
	}

	public void setPojoMunicipio(Municipio pojoMunicipio) {
		this.pojoMunicipio = pojoMunicipio;
		this.pojoLocalidad = null;
		this.pojoColonia = null;
	}
	
	public Localidad getPojoLocalidad() {
		return pojoLocalidad;
	}

	public void setPojoLocalidad(Localidad pojoLocalidad) {
		this.pojoLocalidad = pojoLocalidad;
		this.pojoColonia = null;
	}

	public Colonia getPojoColonia() {
		return pojoColonia;
	}

	public void setPojoColonia(Colonia pojoColonia) {
		this.pojoColonia = pojoColonia;
	}

	public List<SelectItem> getListCatDomicilios1() {
		return listCatDomicilios1;
	}

	public void setListCatDomicilios1(List<SelectItem> listCatDomicilios1) {
		this.listCatDomicilios1 = listCatDomicilios1;
	}

	public List<SelectItem> getListCatDomicilios2() {
		return listCatDomicilios2;
	}

	public void setListCatDomicilios2(List<SelectItem> listCatDomicilios2) {
		this.listCatDomicilios2 = listCatDomicilios2;
	}

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}
	
	public Long getIdDom1() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio1() != null ? this.pojoDomicilioExt.getCatDomicilio1() : 0L;
	}
		
	public void setIdDom1(Long idDom1) {
		this.pojoDomicilioExt.setCatDomicilio1(getValorById(idDom1, this.listTmpCatDomicilios1).getId());
	}
	
	public Long getIdDom2() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio2() != null ? this.pojoDomicilioExt.getCatDomicilio2() : 0L;
	}
		
	public void setIdDom2(Long idDom2) {
		this.pojoDomicilioExt.setCatDomicilio2(getValorById(idDom2, this.listTmpCatDomicilios1).getId());
	}
		
	public Long getIdDom3() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio3() != null ? this.pojoDomicilioExt.getCatDomicilio3() : 0L;
	}
			
	public void setIdDom3(Long idDom3) {
		this.pojoDomicilioExt.setCatDomicilio3(getValorById(idDom3, this.listTmpCatDomicilios1).getId());
	}
		
	public Long getIdDom4() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio4() != null ? this.pojoDomicilioExt.getCatDomicilio4() : 0L;
	}
		
	public void setIdDom4(Long idDom4) {
		this.pojoDomicilioExt.setCatDomicilio4(getValorById(idDom4, this.listTmpCatDomicilios2).getId());
	}
	
	public Long getIdDom5() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio5() != null ? this.pojoDomicilioExt.getCatDomicilio5() : 0L;
	}
	
	public void setIdDom5(Long idDom5) {
		this.pojoDomicilioExt.setCatDomicilio5(getValorById(idDom5, listTmpCatDomicilios2).getId());
	}

	public Estado getPojoEstado() {
		return pojoEstado;
	}

	public void setPojoEstado(Estado pojoEstado) {
		this.pojoEstado = pojoEstado;
	}

	public boolean isBand() {
		return operacionCancelada;
	}

	public void setBand(boolean operacionCancelada) {
		this.operacionCancelada = operacionCancelada;
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
	
	public int getTipoAlmacen() {
		if (this.pojoAlmacen != null)
			return this.pojoAlmacen.getTipo();
		return 0;
	}

	public void setTipoAlmacen(int tipo) {
		if (this.pojoAlmacen != null)
			this.pojoAlmacen.setTipo(tipo);
	}
	
	public int getResultadoValidacion() {
		return resultadoValidacion;
	}

	public void setResultadoValidacion(int resultadoValidacion) {
		this.resultadoValidacion = resultadoValidacion;
	}
	
	public List<SelectItem> getListEmpleadosItems() {
		return listEmpleadosItems;
	}
	
	public void setListEmpleadosItems(List<SelectItem> listEmpleadosItems) {
		this.listEmpleadosItems = listEmpleadosItems;
	}
	
	public long getIdEncargado() {
		if (this.idEncargado > 0L && this.listEmpleadosItems.size() > 0)
			return this.idEncargado;
		return 0L;
	}
	
	public void setIdEncargado(long idEncargado) {
		this.idEncargado = idEncargado;
	}
	
	public String getMensajeDetalles() {
		return mensajeDetalles;
	}
	
	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

	public List<SelectItem> getTiposAlmacenes() {
		return tiposAlmacenes;
	}

	public void setTiposAlmacenes(List<SelectItem> tiposAlmacenes) {
		this.tiposAlmacenes = tiposAlmacenes;
	}

	public boolean isPerfilAdministrativo() {
		return perfilAdministrativo;
	}

	public void setPerfilAdministrativo(boolean perfilAdministrativo) {
		this.perfilAdministrativo = perfilAdministrativo;
	}

	// ----------------------------------------------------------------------
	// PERMISOS
	// ----------------------------------------------------------------------

	public boolean getLectura() { return this.permisos.getConsultar(); }

	public void setLectura(boolean value) {}
	
	public boolean getEscritura() { return this.permisos.getEditar(); }

	public void setEscritura(boolean value) {}
	
	public boolean getBorrar() { return this.permisos.getBorrar(); }

	public void setBorrar(boolean value) {}

	// ----------------------------------------------------------------------
	
	public boolean isPermisoConsultar() { return this.permisos.getConsultar(); }

	public void setPermisoConsultar(boolean value) {}
    
	public boolean isPermisoAgregar() { return this.permisos.getEditar(); }

	public void setPermisoAgregar(boolean value) {}

	public boolean isPermisoEditar() { return this.permisos.getEditar(); }

	public void setPermisoEditar(boolean value) {}

	public boolean isPermisoBorrar() { return this.permisos.getBorrar(); }

	public void setPermisoBorrar(boolean value) {}

	public boolean isPermisoImprimir() { return this.permisos.getConsultar(); }

	public void setPermisoImprimir(boolean value) {}

	public boolean isPermisoEscritura() { return this.permisos.getEditar(); }

	public void setPermisoEscritura(boolean value) { }
}
