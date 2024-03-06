package net.giro.inventarios;

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

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.logica.ClientesRem;
import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Sucursal;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenExt;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.navegador.LoginManager;
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
	private LoginManager loginManager;
	private InitialContext ctx;
	//INTERFACES
	private AlmacenRem ifzAlmacen;
	private SucursalesRem ifzSucursal;
	private ClientesRem ifzClientes;
	private ObraRem ifzObras;
    private long usuarioId;
    private String usuario;
	private int numPagina;
	private int numPaginaObras;
	private String tipoBusqueda;
	private String valorBusqueda;
	private List<SelectItem> listaCampoBusqueda;	
	private int campoBusqueda;
	private AlmacenExt pojoAlmacen;
	private AlmacenExt pojoAlmacenEliminar;
	private List<AlmacenExt> listaAlmacenesGrid;
	private List<SelectItem> listaCboSucursales;
	private List<Sucursal> listaSucursales;
	private DomicilioExt pojoDomicilioExt;
	private Estado pojoEstado;
	private Municipio pojoMunicipio;
	private Localidad pojoLocalidad;
	private Colonia pojoColonia;
	private boolean puedeEditar;
	private List<SelectItem> tiposAlmacenes;
	// Domicilio
	private String busquedaMunicipio;
	private String busquedaLocalidad;
	private String busquedaColonia;
	private int numPaginaMunicipio;
	private int numPaginaLocalidad; 
	private int numPaginaColonia;
	private List<SelectItem> listCatDomicilios1;
	private List<SelectItem> listCatDomicilios2;
	private List<SelectItem> listEstado;
	private List<Municipio> listMunicipios;
	private List<Localidad> listLocalidades;
	private List<Colonia> listColonias;
	private List<Estado> listTmpEstado;
	private List<ConValores> listTmpCatDomicilios1;
	private List<ConValores> listTmpCatDomicilios2;
	private boolean operacionCompleta;
	//private String mensaje;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private int resultadoValidacion;
	// Busqueda obras
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorOpcionBusquedaObras;
	private List<Obra> listObrasPrincipales;
	private Obra pojoObra;
	// Busqueda Empleados
	private EmpleadoRem ifzEmpleados;
	private List<Empleado> listEmpleados;
	private List<SelectItem> listEmpleadosItems;
	private long idEncargado;
	// PERFILES
	private boolean perfilAdministrativo;
	
	
	public AlmacenAction() throws NamingException,Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = null;
		String valPerfil = "";

		try {
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario   = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilAdministrativo = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			log.info("Perfil EGRESOS_OPERACION: " + (this.perfilAdministrativo ? "SI" : "NO"));
	
			this.ctx = new InitialContext();
			this.ifzSucursal = (SucursalesRem) ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzAlmacen = (AlmacenRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
	
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.puedeEditar = true;
			this.operacionCompleta = true;
			this.numPaginaMunicipio = 1;
			this.numPaginaLocalidad = 1; 
			this.numPaginaColonia = 1;
			this.pojoAlmacen = new AlmacenExt();
			this.pojoObra = new Obra();
			this.listaAlmacenesGrid = new ArrayList<AlmacenExt>();
			this.listaSucursales = new ArrayList<Sucursal>();
			this.listCatDomicilios1 = new ArrayList<SelectItem>();
			this.listCatDomicilios2 = new ArrayList<SelectItem>();
			this.listEstado	= new ArrayList<SelectItem>();
			this.listTmpCatDomicilios1 = new ArrayList<ConValores>();
			this.listTmpCatDomicilios2 = new ArrayList<ConValores>();
			
			// Busqueda principal
			this.listaCampoBusqueda = new ArrayList<SelectItem>();
			this.listaCampoBusqueda.add(new SelectItem("nombre", "Nombre"));
			this.listaCampoBusqueda.add(new SelectItem("id", "ID"));
			this.tipoBusqueda = this.listaCampoBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;

			// Busqueda obras
			this.listObrasPrincipales 	= new ArrayList<Obra>();
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaObras.add(new SelectItem("Clave", "Clave"));
			this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();
			this.valorBusquedaObras = "";
			this.valorOpcionBusquedaObras = 1;
			
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
			log.error("Error en constructor Inventarios.AlmacenAction", e);
		}
	}
	

	public void buscar() {
		try {
			control();
			if (this.tipoBusqueda == null || "".equals(this.tipoBusqueda.trim())) 
				this.tipoBusqueda = this.listaCampoBusqueda.get(0).getValue().toString();
			
			this.listaAlmacenesGrid = this.ifzAlmacen.findExtLikeProperty(this.tipoBusqueda, this.valorBusqueda);
			if (this.listaAlmacenesGrid == null || this.listaAlmacenesGrid.isEmpty()) {
				control(2, "la busqueda no regreso resultados");
				return;
			}
			
			Collections.sort(this.listaAlmacenesGrid, new Comparator<AlmacenExt>() {
				@Override
				public int compare(AlmacenExt o1, AlmacenExt o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
		} catch (Exception e) {
    		control("Ocurrio un problema al realizar la consulta de Almacenes", e);
		}
	}

	public void nuevo() {
		this.pojoAlmacen = new AlmacenExt();
		this.pojoDomicilioExt = new DomicilioExt();
		this.idEncargado = 0L;
		cargarEmpleados();
		nuevoDomicilio();
	}

	public void editar() {
		try {
			control();
			this.idEncargado = 0L;
			nuevoDomicilio();
			
			if (this.pojoAlmacen.getTipo() > 2 && ! this.perfilAdministrativo) {
				control(-1, "No tienes los permisos suficientes para editar Almacenes Administrativos");
				return;
			}
			
			this.setIdSucursal(this.pojoAlmacen.getSucursal().getId());

			if (this.pojoAlmacen.getIdEncargado() != null)
				this.idEncargado = this.pojoAlmacen.getIdEncargado().getId();

			if (this.pojoAlmacen.getPojoDomicilio() != null) {
				this.pojoDomicilioExt = this.pojoAlmacen.getPojoDomicilio();
				desglosaDomicilio();
			}
			
			cargarEmpleados();
    	} catch (Exception e) {
    		control("Error en editar", e);
    	}
	}

	public void guardar() throws Exception{
		this.operacionCompleta = false;
		this.mensaje = "";
		this.tipoMensaje = 0;
		
		try {
			if (! validaGuardarAlmacen()) 
				return;
			
			if (this.idEncargado > 0L) {
				for (Empleado var : this.listEmpleados) {
					if (this.idEncargado == var.getId().longValue()) {
						this.pojoAlmacen.setIdEncargado(var);
						break;
					}
				}
			}
			
			if (this.pojoAlmacen.getId() == null || this.pojoAlmacen.getId() <= 0L) {
				this.pojoAlmacen.setCreadoPor(this.usuarioId);
				this.pojoAlmacen.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoAlmacen.setModificadoPor(this.usuarioId);
				this.pojoAlmacen.setFechaModificacion(Calendar.getInstance().getTime());
				
				this.pojoAlmacen.setId(this.ifzAlmacen.save(this.pojoAlmacen));
				this.listaAlmacenesGrid.add(0, this.pojoAlmacen);
			} else {
				this.pojoAlmacen.setModificadoPor(this.usuarioId);
				this.pojoAlmacen.setFechaCreacion(Calendar.getInstance().getTime());
				
				this.ifzAlmacen.update(this.pojoAlmacen);
			}
			
			nuevo();
			nuevoDomicilio();
			this.idEncargado = 0L;
			this.operacionCompleta = true;
		} catch (ExcepConstraint e) {
			control("Ocurrio un problema al intentar guardar el Almacen", e);
		}
	}
	
	public void eliminar() {
		try {
			control();
			this.pojoAlmacenEliminar.setEstatus(1);
			this.pojoAlmacenEliminar.setModificadoPor(this.usuarioId);
			this.pojoAlmacenEliminar.setFechaCreacion(Calendar.getInstance().getTime());
			
			this.ifzAlmacen.update(this.pojoAlmacenEliminar);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Eliminar el Almacen", e);
		}
	}

	private boolean validaGuardarAlmacen() throws Exception {
		List<Almacen> listAux = null;
		
		try {
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
					
					/*if (this.ifzAlmacen.comprobarPrincipal(Long.valueOf(this.pojoAlmacen.getSucursal().getId()), this.pojoAlmacen.getId())) {
						control(-13, "La sucursal seleccionada ya tiene un Almacen General asignado.");
						return false;
					}*/
				}
			}

			// Comprobamos nombre almacen
			if (this.ifzAlmacen.comprobarNombre(this.pojoAlmacen.getNombre(), this.pojoAlmacen.getId())) {
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
		} catch (ExcepConstraint e) {
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
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.operacionCompleta = false;
		this.operacionCancelada = operacionCancelada;
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
		
		if (this.operacionCancelada)
			log.error("\n\nALMACEN :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n", throwable);
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
    		this.operacionCompleta = true;
			this.mensaje = "";
			
			if (listTmpCatDomicilios1 == null)
				listTmpCatDomicilios1 = new ArrayList<ConValores>();
			else
				listTmpCatDomicilios1.clear();
			
			if (listTmpCatDomicilios2 == null)
				listTmpCatDomicilios2 = new ArrayList<ConValores>();
			else
				listTmpCatDomicilios2.clear();
			
			if (listCatDomicilios1==null) 
				listCatDomicilios1 = new ArrayList<SelectItem>();
			else
				listCatDomicilios1.clear();
			
			if (listCatDomicilios2==null) 
				listCatDomicilios2 = new ArrayList<SelectItem>();
			else
				listCatDomicilios2.clear();
			
			params = new HashMap<String, String>();
			params.put("atributo2", "1");
			cargaTipoDom(params, listCatDomicilios1, listTmpCatDomicilios1);
			
			log.info("cargaTiposDomicilio: listCatDomicilios1: "+listCatDomicilios1.size());
			
			
			params.clear();
			params.put("atributo2", "2");
			cargaTipoDom(params, listCatDomicilios2, listTmpCatDomicilios2);
		} catch (Exception e) {
    		this.operacionCompleta = false;
			this.mensaje = "ERROR";
			log.error("Error en el metodo cargaTiposDomicilio", e);
		}
	}
	
	private void cargaTipoDom(HashMap<String, String> params, List<SelectItem> items, List<ConValores> listDom) {
		try {
			this.operacionCompleta = true;
			this.mensaje = "";
			
			listDom.addAll(ifzClientes.buscarDomicilios(params));
			for (ConValores cv : listDom)
				items.add(new SelectItem(cv.getId(), cv.getValor()));
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Tipos de Domicilio", e);
		}
	}

	public boolean cargaEstados() {
    	try {
			control();
    		if (this.listEstado == null)
    			this.listEstado = new ArrayList<SelectItem>();
			this.listEstado.clear();
			
			this.listTmpEstado = this.ifzClientes.buscarEstados();
			if (this.listTmpEstado == null || this.listTmpEstado.isEmpty()) {
				control("No se encontro ningun Estado", null);
				return false;
			}

			Collections.sort(this.listTmpEstado, new Comparator<Estado>() {
				@Override
				public int compare(Estado o1, Estado o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});

			for (Estado e : this.listTmpEstado) {
				this.listEstado.add(new SelectItem(e.getId(), e.getNombre()));
			}
		} catch (Exception e) {
			control("Error en eliminar", e);
			return false;
		}
    	
    	return true;
    }
	
	public void limpiaMunicipio() {
		if (this.listMunicipios != null)
			this.listMunicipios.clear();
		else
			this.listMunicipios = new ArrayList<Municipio>();
	}
	
	public void limpiaLocalidades() {
		if (this.listLocalidades != null)
			this.listLocalidades.clear();
		else
			this.listLocalidades = new ArrayList<Localidad>();
	}
	
	public void limpiaColonias() {
		if (this.listColonias != null)
			this.listColonias.clear();
		else
			this.listColonias = new ArrayList<Colonia>();
	}
	
	public void cambioEstadoDom() {
		try {
			if (this.listMunicipios != null)
				this.listMunicipios.clear();
			if (this.listLocalidades != null)
				this.listLocalidades.clear();
			if (this.listColonias != null)
				this.listColonias.clear();
			
			this.pojoMunicipio = null;
			this.pojoLocalidad = null;
			this.pojoColonia = null;
		} catch (Exception e) {
			control("Error en metodo cambioEstadoDom", e);
		}
	}
	
	public void buscarMunicipios() throws Exception {
    	try {
			control();
			this.operacionCompleta = true;
			this.listMunicipios = ifzClientes.buscarMunicipio(this.pojoEstado, this.busquedaMunicipio);
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
    
    public void buscarLocalidades() throws Exception {
    	try {
			control();
			this.operacionCompleta = true;
			this.listLocalidades = this.ifzClientes.buscarLocalidad(this.pojoMunicipio, this.busquedaLocalidad);
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
    
    public void buscarColonias() throws Exception {
    	try {
			control();
			this.operacionCompleta = true;
			this.listColonias = this.ifzClientes.buscarColonia(this.pojoLocalidad, this.busquedaColonia);
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
			this.operacionCompleta = true;
			if (! comprobarDireccion() || pojoColonia == null) {
				control(-12, "Direccion no valida.");
				return false;
			}
			
			this.pojoDomicilioExt.setPrincipal(true);
			this.pojoDomicilioExt.setEstatus(true);
			this.pojoDomicilioExt.setColonia(pojoColonia);
			
			log.info("PojoDomicilio.id(): "+this.pojoDomicilioExt.getId());
			respuesta = this.ifzClientes.salvar(this.pojoDomicilioExt);
			if (respuesta.getErrores().getCodigoError() > 0) {
				control(-1, respuesta.getErrores().getDescError());
				return false;
			} 
			
			this.pojoDomicilioExt = (DomicilioExt) respuesta.getBody().getValor("domicilio");
			this.pojoAlmacen.setPojoDomicilio(pojoDomicilioExt);
			log.info("pojoDomicilioExt: "+pojoDomicilioExt.getDescripcionDomicilio1());
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

	// ------------------------------------------------------------------------------------------
	// SUCURSALES
	// ------------------------------------------------------------------------------------------

	private void cargarSucursales() {
		try {
			control();
			if (this.listaCboSucursales == null)
				this.listaCboSucursales = new ArrayList<SelectItem>();
			this.listaCboSucursales.clear();
			
			this.listaSucursales = this.ifzSucursal.findAll();
			if (this.listaSucursales != null && ! this.listaSucursales.isEmpty()) {
				for (Sucursal s : this.listaSucursales)
					this.listaCboSucursales.add(new SelectItem(s.getId(), s.getSucursal()));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Sucursales", e);
		}
	}

	// ------------------------------------------------------------------------------------------
	// OBRAS
	// ------------------------------------------------------------------------------------------

	public void buscarObras() {
    	try {
			control();
			this.operacionCompleta = true;
			if ("".equals(this.valorOpcionBusquedaObras))
				this.valorOpcionBusquedaObras = 1;
			
			if ("".equals(this.campoBusquedaObras)) {
				this.listObrasPrincipales = this.ifzObras.findObraPrincipalLikeProperty("id", this.valorBusquedaObras, this.valorOpcionBusquedaObras, (this.pojoObra.getId() == null ? 0 : this.pojoObra.getId()));
			} else {
				this.listObrasPrincipales = this.ifzObras.findObraPrincipalLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.valorOpcionBusquedaObras, (this.pojoObra.getId() == null ? 0 : this.pojoObra.getId()));
			}
			
			if (this.listObrasPrincipales == null || this.listObrasPrincipales.isEmpty()) {
	    		control(2, "Busqueda sin resultados");
			}
    	} catch (Exception e) {
    		control("Error en buscarObras", e);
    	}
    }

	public void seleccionarObra() {
		
	}

	// ------------------------------------------------------------------------------------------
	// EMPLEADOS
	// ------------------------------------------------------------------------------------------

	private void cargarEmpleados() {
		try {
			control();
			if (this.listEmpleados == null)
				this.listEmpleados = new ArrayList<Empleado>();
			this.listEmpleados.clear();
			
			this.listEmpleados = this.ifzEmpleados.findAllActivos();
			if (this.listEmpleados != null && ! this.listEmpleados.isEmpty()) {
				Collections.sort(this.listEmpleados, new Comparator<Empleado>() {
					@Override
					public int compare(Empleado o1, Empleado o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
				});
			}
			
			filtrarEmpleados();
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Empleados", e);
		}
	}
	
	public void filtrarEmpleados() {
		if (this.listEmpleadosItems == null)
			this.listEmpleadosItems = new ArrayList<SelectItem>();
		this.listEmpleadosItems.clear();
		
		for (Empleado e : this.listEmpleados) {
			if (this.pojoAlmacen != null && this.pojoAlmacen.getSucursal() != null) {
				if (this.pojoAlmacen.getSucursal().getId() == e.getIdSucursal().longValue())
					this.listEmpleadosItems.add(new SelectItem(e.getId(), e.getNombre()));
			} else {
				this.listEmpleadosItems.add(new SelectItem(e.getId(), e.getNombre()));
			}
		}
	}
	
	// ------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------------

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
		for (Estado e : this.listTmpEstado) {
			if (e.getId() == id.longValue()) 
				return e;
		}
		
		return null;
	}
	
	public void setIdEstadoDom(Long idEstadoDom) {
		try {
			pojoEstado = getEstadoById(idEstadoDom);
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
		
		for (Sucursal s : this.listaSucursales) {
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
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public List<SelectItem> getListaCampoBusqueda() {
		return listaCampoBusqueda;
	}

	public void setListaCampoBusqueda(List<SelectItem> listaCampoBusqueda) {
		this.listaCampoBusqueda = listaCampoBusqueda;
	}

	public int getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(int campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public List<AlmacenExt> getListaAlmacenesGrid() {
		return listaAlmacenesGrid;
	}

	public void setListaAlmacenesGrid(List<AlmacenExt> listaAlmacenesGrid) {
		this.listaAlmacenesGrid = listaAlmacenesGrid;
	}

	public List<SelectItem> getListaCboSucursales() {
		return listaCboSucursales;
	}

	public void setListaCboSucursales(List<SelectItem> listaCboSucursales) {
		this.listaCboSucursales = listaCboSucursales;
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
		return listTmpEstado;
	}

	public void setListTmpEstado(List<Estado> listTmpEstado) {
		this.listTmpEstado = listTmpEstado;
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

	public Colonia getPojoColonia() {
		return pojoColonia;
	}

	public void setPojoColonia(Colonia pojoColonia) {
		this.pojoColonia = pojoColonia;
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
	
	private ConValores getValorById(Long id, List<ConValores> lista) {
		for (ConValores cv :lista) {
			if (cv.getId() == id.longValue())
				return cv;
		}
		
		return null;
	}

	public List<SelectItem> getListEstado() {
		return listEstado;
	}

	public void setListEstado(List<SelectItem> listEstado) {
		this.listEstado = listEstado;
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
	
	public long getIdObra() {
		return 0L;
	}
	
	public void setIdObra(int idObra) {
		log.info("Seteando obra");
	}

	public int getNumPaginaObras() {
		return numPaginaObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
	}

	public List<Obra> getListObrasPrincipales() {
		return listObrasPrincipales;
	}

	public void setListObrasPrincipales(List<Obra> listObrasPrincipales) {
		this.listObrasPrincipales = listObrasPrincipales;
	}
	
	public List<SelectItem> getTiposBusquedaObras() {
		return tiposBusquedaObras;
	}
	
	public void setTiposBusquedaObras(List<SelectItem> tiposBusquedaObras) {
		this.tiposBusquedaObras = tiposBusquedaObras;
	}
	
	public String getCampoBusquedaObras() {
		return campoBusquedaObras;
	}
	
	public void setCampoBusquedaObras(String campoBusquedaObras) {
		this.campoBusquedaObras = campoBusquedaObras;
	}
	
	public String getValorBusquedaObras() {
		return valorBusquedaObras;
	}
	
	public void setValorBusquedaObras(String valorBusquedaObras) {
		this.valorBusquedaObras = valorBusquedaObras;
	}
	
	public int getValorOpcionBusquedaObras() {
		return valorOpcionBusquedaObras;
	}
	
	public void setValorOpcionBusquedaObras(int valorOpcionBusquedaObras) {
		this.valorOpcionBusquedaObras = valorOpcionBusquedaObras;
	}

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
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
	
	/*public void setTipoAlmacen(boolean tipo) {
		if (tipo == true) 		//1, principal
			this.pojoAlmacen.setTipo(1);
		else
			this.pojoAlmacen.setTipo(2);	//2, obra
	}
	
	public boolean isTipoAlmacen() {
		return this.pojoAlmacen.getTipo() == 1 ? true : false;
	}*/

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
}
