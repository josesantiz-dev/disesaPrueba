package net.giro.rh.admon.catalogos;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.clientes.beans.Persona;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.plataforma.logica.EmpresasRem;
import net.giro.rh.admon.beans.Area;
import net.giro.rh.admon.beans.Categoria;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoBeneficiarioExt;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.beans.EmpleadoParienteExt;
import net.giro.rh.admon.beans.Puesto;
import net.giro.rh.admon.beans.PuestoCategoria;
import net.giro.rh.admon.logica.AreaRem;
import net.giro.rh.admon.logica.CategoriaRem;
import net.giro.rh.admon.logica.EmpleadoBeneficiarioRem;
import net.giro.rh.admon.logica.EmpleadoContratoRem;
import net.giro.rh.admon.logica.EmpleadoParienteRem;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.rh.admon.logica.PuestoCategoriaRem;
import net.giro.rh.admon.logica.PuestoRem;
import net.giro.clientes.logica.ClientesRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.richfaces.application.ServiceTracker;
import org.richfaces.focus.FocusManager;

@ViewScoped
@ManagedBean(name="empleadoAction")
public class EmpleadoAction implements Serializable {
	private static Logger log = Logger.getLogger(EmpleadoAction.class);
	private static final long serialVersionUID = 1L;
	private InitialContext ctx;
	private LoginManager loginManager;
    private long usuarioId;
    //private Empresa empresa;
    //private long idEmpresa;
	private int numPagina;
	private int numPaginaPersonas;
	private int numPaginaFamiliares;
	private int numPaginaBeneficiarios;
	private String mensajeErrorBeneficiario;
	private boolean beneficiarioValidado;
	private boolean operacionTerminada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private String valorBusqueda;
	private EmpleadoRem 			ifzEmpleado;
	private ClientesRem 			ifzClientes; 
	private EmpleadoContratoRem 	ifzContrato;
	private SucursalesRem 			ifzSucursal;
	private EmpleadoParienteRem 	ifzEmpleadoPariente;
	private ConValoresRem 			ifzConValores;
	private AreaRem 				ifzArea;
	private CategoriaRem 			ifzCategorias;
	private PuestoRem 				ifzPuestos;
	private PuestoCategoriaRem 		ifzPuestosCategorias;
	private EmpleadoBeneficiarioRem ifzEmpleadoBeneficiario;
	private EmpresasRem ifzEmpresas;
	private Empleado pojoEmpleadoMain;
	private EmpleadoExt pojoEmpleado;
	private Persona pojoPersona;
	private List<Empleado> listaEmpleadosGrid;
	// Busqueda Personas
	private List<Persona> listaPersonasGrid;
	private List<SelectItem> tipoBusquedaPersonas;	
	private String valBusquedaPersonas;
	private String campoBusquedaPersonas;
	//Valores para validacion
	private String nombreEmpleado;
	private Date fechaIngreso;
	private String numeroSeguridadSocial;
	private String emergencia;
	private String email;
	private boolean externo;
	// --------------- Los selects de nuevo/agregar
	// -----> Variables para el Select de Areas
	private List<SelectItem> listaCboAreas;
	private List<Area> listaAreas;
	private long idArea;
	private Area pojoArea;
	private String areaValidador;
	// -----> Variables para el Select de Puestos
	private List<SelectItem> listaCboPuestos;
	private List<Puesto> listaPuestos;
	private long idPuesto;
	private Puesto pojoPuesto;
	private String puestoValidador;
	// -----> Variables para el Select de Categorias
	private List<SelectItem> listaCboCategorias;
	private List<Categoria> listaCategorias;
	private long idCategoria;
	private Categoria pojoCategoria;
	private String categoriaValidador;
	//Sucursales
	private List<SelectItem> listaCboSucursales;
	private List<Sucursal> listaSucursales;
	// -----> Variables para familiares
	private EmpleadoParienteExt pojoFamiliar;
	private long idRelacionBeneficiario;
	private List<EmpleadoParienteExt> listaFamiliaresGrid;
	private List<EmpleadoParienteExt> listaFamiliaresEliminados;
	private Persona pojoFamiliarSeleccionado;
	private EmpleadoParienteExt pojoFamElimSeleccionado;
	private ConGrupoValoresRem ifzGpoVal;
	private List<ConValores> listaRelacionesFamiliares;
	private ConGrupoValores pojoGpoRelacion;
	private List<SelectItem> listaCboRelaciones;
	// Variables seleccion de FORMAS DE PAGO PARA EMPLEADOS
	private ConGrupoValores pojoFormasPagoEmpleados;
	private List<ConValores> listFormasPagoEmpleados;
	private List<SelectItem> listFormasPagoEmpleadosItems;
	private List<ConValores> listaRelacionesBeneficiarios;
	private List<SelectItem> listaCboRelacionesBeneficiarios;
	private long idRelacion;
	private ConValores pojoRelacion;
	private EmpleadoBeneficiarioExt pojoBeneficiario;
	private Persona pojoBeneficiarioSeleccionado;
	private List<EmpleadoBeneficiarioExt> listaBeneficiariosGrid;
	private List<EmpleadoBeneficiarioExt> listaBeneficiariosEliminados;
	// Campos para el contrato
	private List<EmpleadoContrato> listaContratosGrid;
	private EmpleadoContrato pojoContrato;
	private EmpleadoContrato pojoContratoBase;
	private EmpleadoContrato pojoContratoBorrar;
	private long idPeridiocidadPago;
	private List<ConValores> listaPeridiocidad;
	private List<SelectItem> listaCboPeridiocidad;
	private ConGrupoValores pojoGpoPeriodo;
	private int idDiaDescanso;
	private List<SelectItem> listaCboDias;
	private String horaEntrada;
	private String horaSalida;
	private String horaEntradaComplemento;
	private String horaSalidaComplemento;
	private boolean esContratoNuevo;
	private int contratosPaginacion;
	private TimeZone timeZone;
	private String inputNameFocused;
	
	
 	public EmpleadoAction() throws NamingException,Exception {
 		FacesContext fc = null;
 		Application app = null;
 		ValueExpression valExp = null;
 		
 		try {
 			fc = FacesContext.getCurrentInstance();
 			app = fc.getApplication();
 			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
 			this.loginManager =(LoginManager) valExp.getValue(fc.getELContext());

 			this.ctx = new InitialContext();
 			this.ifzEmpleado 				=(EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
 			this.ifzClientes 				=(ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
 			this.ifzArea 					=(AreaRem) this.ctx.lookup("ejb:/Logica_RecHum//AreaFac!net.giro.rh.admon.logica.AreaRem");
 			this.ifzCategorias 				=(CategoriaRem) this.ctx.lookup("ejb:/Logica_RecHum//CategoriaFac!net.giro.rh.admon.logica.CategoriaRem");
 			this.ifzPuestos 				=(PuestoRem) this.ctx.lookup("ejb:/Logica_RecHum//PuestoFac!net.giro.rh.admon.logica.PuestoRem");
 			this.ifzPuestosCategorias  		=(PuestoCategoriaRem) this.ctx.lookup("ejb:/Logica_RecHum//PuestoCategoriaFac!net.giro.rh.admon.logica.PuestoCategoriaRem");
 			this.ifzContrato 				=(EmpleadoContratoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
 			this.ifzEmpleadoPariente 		=(EmpleadoParienteRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoParienteFac!net.giro.rh.admon.logica.EmpleadoParienteRem");
 			this.ifzEmpleadoBeneficiario 	=(EmpleadoBeneficiarioRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoBeneficiarioFac!net.giro.rh.admon.logica.EmpleadoBeneficiarioRem");
 			this.ifzSucursal 				=(SucursalesRem) this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
 			this.ifzConValores 				=(ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
 			this.ifzGpoVal 					=(ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
 			this.ifzEmpresas 				=(EmpresasRem) this.ctx.lookup("ejb:/Logica_Publico//EmpresasFac!net.giro.plataforma.logica.EmpresasRem");

 			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzArea.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzCategorias.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzPuestos.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzPuestosCategorias.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzContrato.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzEmpleadoPariente.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzEmpleadoBeneficiario.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzEmpresas.setInfoSesion(this.loginManager.getInfoSesion());
 			
 			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
 			/*this.idEmpresa = 1;//this.loginManager.getUsuarioResponsabilidad().getUsuario().getIdEmpresa();
 			if (this.idEmpresa > 0)
 				this.empresa = this.ifzEmpresas.findById(this.idEmpresa);*/
 			
 			// CARGAMOS LAS RELACIONES
 			// ---------------------------------------------------------------------------------------------------------
 			this.pojoGpoRelacion = this.ifzGpoVal.findByName("SYS_RELACIONES");
 			if (this.pojoGpoRelacion == null || this.pojoGpoRelacion.getId() <= 0L)
 				log.warn("No se encontro encontro el grupo SYS_RELACIONES en con_grupo_valores");
 			this.listaRelacionesFamiliares =  this.ifzConValores.buscaValorGrupo("descripcion", "", pojoGpoRelacion);
 			this.listaRelacionesBeneficiarios = this.ifzConValores.buscaValorGrupo("descripcion", "", pojoGpoRelacion);

 			// CARGAMOS LOS TIPOS DE PERIODOS
 			// ---------------------------------------------------------------------------------------------------------
 			this.pojoGpoPeriodo = this.ifzGpoVal.findByName("SYS_TIPO_PERIODO");
 			if (this.pojoGpoPeriodo == null || this.pojoGpoPeriodo.getId() <= 0L)
 				log.warn("No se encontro encontro el grupo SYS_TIPO_PERIODO en con_grupo_valores");
 			this.listaPeridiocidad = this.ifzConValores.buscaValorGrupo("descripcion", "", pojoGpoPeriodo);
 			
 			// FORMAS DE PAGO PARA EMPLEADOS
 			// ---------------------------------------------------------------------------------------------------------
 			this.pojoFormasPagoEmpleados = this.ifzGpoVal.findByName("SYS_FORMA_PAGO_EMPLEADO");
 			if (this.pojoFormasPagoEmpleados == null || this.pojoFormasPagoEmpleados.getId() <= 0L)
 				log.warn("No se encontro encontro el grupo SYS_FORMA_PAGO_EMPLEADO en con_grupo_valores");
 			cargarFormasPagoEmpleados();
 			
 			// INICIALIZACIONES
 			this.pojoEmpleado = new EmpleadoExt();
 			this.tipoMensaje = 0;
 			this.operacionTerminada = false;
 			this.numPagina = 1;
 			this.numPaginaPersonas = 1;
 			this.numPaginaFamiliares = 1;
 			this.numPaginaBeneficiarios = 1;
 			
 			this.listaAreas = new ArrayList<Area>();
 			this.listaCategorias = new ArrayList<Categoria>();
 			this.listaPuestos = new ArrayList<Puesto>();
 			this.listaFamiliaresGrid = new ArrayList<>();
 			this.listaFamiliaresGrid = new ArrayList<>();
 			this.listaFamiliaresEliminados = new ArrayList<>();
 			this.listaBeneficiariosGrid = new ArrayList<>();
 			this.listaBeneficiariosEliminados = new ArrayList<>();
 			this.pojoBeneficiario = new EmpleadoBeneficiarioExt();

 			// Buaqueda Personas
 			this.tipoBusquedaPersonas = new ArrayList<SelectItem>();
 			this.tipoBusquedaPersonas.add(new SelectItem("nombre", "Nombre"));
 			this.tipoBusquedaPersonas.add(new SelectItem("id", "ID"));
 			this.campoBusquedaPersonas = this.tipoBusquedaPersonas.get(0).getValue().toString();
 			this.valBusquedaPersonas = "";
 			this.numPaginaPersonas = 1;
 			this.inputNameFocused = "txtvalorPersona";
 			
 			cargarAreas();
 			cargarCboAreas();
 			cargarCategorias();
 			cargarCboCategorias();
 			cargarPuestos();
 			cargarCboPuestos();
 			cargarSucursales();
 			cargarDiasDescanso();
 			cargarListaCboRelaciones();
 			cargarListaCboRelacionesBeneficiarios();

 			//para el contrato
 			cargarListaCboPeridiocidad();

 			this.pojoContrato = new EmpleadoContrato();
 			this.esContratoNuevo = true;
 			this.contratosPaginacion = 1;
 			this.inputNameFocused = "txtvalor";
 			this.timeZone = TimeZone.getTimeZone("America/Mazatlan");
 		} catch (Exception e) {
 			log.error("Ocurrio un problema al intanciar RecHum.EmpreadoAction", e);
 		}
	}
	

	public void buscar() {
		try {
			control();
			this.listaEmpleadosGrid = this.ifzEmpleado.findLike(this.valorBusqueda, "primerApellido, segundoApellido, primerNombre, segundoNombre", 0);
			if (this.listaEmpleadosGrid == null || this.listaEmpleadosGrid.isEmpty()) {
				control("La busqueda no devolvio resultados.", null);
				return;
			}
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Buscar los Empleados", e);
		} finally {
 			this.inputNameFocused = "txtvalor";
		}
	}

	public void nuevo() {
		try{
			control();
			this.setIdArea(0);
			this.setIdPuesto(0);
			this.setNumeroSeguridadSocial("");
			this.setIdCategoria(0);
			this.setEmergencia("");
			
			this.areaValidador = "";
			this.nombreEmpleado = "";
			this.categoriaValidador="";
			this.email = "";
			
			this.fechaIngreso = Calendar.getInstance().getTime();			
			this.pojoEmpleado = new EmpleadoExt();			
			this.pojoContrato = new EmpleadoContrato();	
 			this.inputNameFocused = "cmdSeleccionarPersona";
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al generar un nuevo Empleado", e);
		}
	}

	public void editar() {
		control();
		try {
			this.pojoEmpleado = this.ifzEmpleado.convertir(this.pojoEmpleadoMain);
			if (this.pojoEmpleado.getPersona() == null) {
				control("No se pudo encontrar la Persona para este registro", null);
				return;
			}
			
			this.pojoPersona = this.pojoEmpleado.getPersona();
			this.nombreEmpleado = this.pojoEmpleado.getPersona().getNombre(); 
			this.setDatosPojoEmpleado();

			this.fechaIngreso = this.pojoEmpleado.getFechaIngreso();
			this.numeroSeguridadSocial = pojoEmpleado.getNumeroSeguridadSocial();
			this.emergencia = this.pojoEmpleado.getNombreCasoEmergencia();
			this.externo = this.pojoEmpleado.getExterno()==1L ? true : false;
			this.email = this.pojoEmpleado.getEmail();
			this.pojoArea =  this.pojoEmpleado.getArea(); //pojoEmpleado.getArea();
			this.pojoPuesto = this.pojoEmpleado.getPuestoCategoria().getIdPuesto(); //pojoEmpleado.getPuestoCategoria().getPuesto();
			this.pojoCategoria = this.pojoEmpleado.getPuestoCategoria().getIdCategoria(); //pojoEmpleado.getPuestoCategoria().getCategoria();
			
			this.areaValidador = this.pojoArea.getDescripcion();
			this.setIdArea(this.pojoArea.getId());
			
			this.puestoValidador = this.pojoPuesto.getDescripcion();
			this.setIdPuesto(this.pojoPuesto.getId());
			
			this.categoriaValidador = this.pojoCategoria.getDescripcion();
			this.setIdCategoria(this.pojoCategoria.getId());
			
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el empleado indicado", e);
		}
	}
	
	public void guardar() {
		try {
			control();
			if (this.listaEmpleadosGrid == null)
				this.listaEmpleadosGrid = new ArrayList<Empleado>();
			
			if (! validaGuardarEmpleado())
				return;

			this.pojoEmpleado.setArea(this.pojoArea);
			this.pojoEmpleado.setPersona(this.pojoPersona);
			this.pojoEmpleado.setFechaIngreso(this.fechaIngreso);
			this.pojoEmpleado.setEmail(this.email);
			this.pojoEmpleado.setNumeroSeguridadSocial(this.numeroSeguridadSocial);
			this.pojoEmpleado.setNombreCasoEmergencia(this.emergencia);
			this.pojoEmpleado.setExterno(this.isExterno() == true ? 1L : 0L);
			this.pojoEmpleado.setEstatus(0);
			this.pojoEmpleado.setModificadoPor(this.usuarioId);
			this.pojoEmpleado.setFechaModificacion(Calendar.getInstance().getTime());
			
			List<PuestoCategoria> pce = this.ifzPuestosCategorias.findByPuestoCategoria(this.idPuesto, this.idCategoria);
			if (pce.size() > 0)
				this.pojoEmpleado.setPuestoCategoria(pce.get(0));
			setDatosPojoEmpleado();
			
			if (this.pojoEmpleado.getId() == null || this.pojoEmpleado.getId() <= 0L) {
				this.pojoEmpleado.setCreadoPor(this.usuarioId);
				this.pojoEmpleado.setFechaCreacion(Calendar.getInstance().getTime());
				
				this.pojoEmpleado.setId(this.ifzEmpleado.save(this.pojoEmpleado));
				this.pojoEmpleadoMain = this.ifzEmpleado.convertir(this.pojoEmpleado);
				this.listaEmpleadosGrid.add(0, this.pojoEmpleadoMain);
			} else {
				// Actualizamos en la BD
				this.ifzEmpleado.update(this.pojoEmpleado);
				this.pojoEmpleadoMain = this.ifzEmpleado.convertir(this.pojoEmpleado);

				// Conseguimos el id en el listado
				for (Empleado var : this.listaEmpleadosGrid) {
					if (var.getId().longValue() == this.pojoEmpleado.getId().longValue()) {
						var = this.pojoEmpleadoMain;
						break;
					}
				}
			}

			log.info("Guardar finalizado");
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Guardar el Empleado", e);
		} finally {
 			this.inputNameFocused = "txtvalor";
		}
	}

	public void eliminar() {
		try {
			control();
			this.pojoEmpleadoMain.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoEmpleadoMain.setFechaIngreso(Calendar.getInstance().getTime());
			this.pojoEmpleadoMain.setEstatus(1);
			
			this.ifzEmpleado.update(this.pojoEmpleadoMain);
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Eliminar el Empleado", e);
		}
	}

	private boolean validaGuardarEmpleado() {
		if (this.pojoEmpleado.getPersona() == null) {
			control("Debe seleccionar una persona", null);
			return false;
		}
		
		//validar si este empleado ya ha sido dado de alta
		if ((this.pojoEmpleado.getId() == null || this.pojoEmpleado.getId() <= 0L) && this.ifzEmpleado.findEmpleadoRepetido(this.pojoEmpleado.getPersona().getId())) {
			control("Este empleado ya ha sido registrado", null);
			return false;
		}
		
		if (this.pojoEmpleado.getSucursal() == null) {
			control("Seleccione sucursal", null);
			return false;
		}

		if (this.idPuesto == 0) {
			control("Debe seleccionar Puesto", null);
			return false;
		}
		
		if (this.numeroSeguridadSocial.trim().equals("")) {
			control("Introduzca número de Seguridad Social", null);
			return false;
		}
		
		if (this.pojoArea == null) {
			control("Debe seleccionar el área del empleado", null);
			return false;
		}
		
		if (this.fechaIngreso.toString().equals("")) {
			control("Debe seleccionar fecha de ingreso", null);
			return false;
		}

		if (this.idCategoria == 0) {
			control("Debe seleccionar Categoria", null);
			return false;
		}

		if (this.emergencia.trim().equals("")) {
			control("Introduzca a quien llamar en caso de emergencia", null);
			return false;
		}
		
		if (this.pojoEmpleado.getAltaSeguroSocial() == 1 && this.pojoEmpleado.getFechaAltaSeguroSocial() == null)
			this.pojoEmpleado.setFechaAltaSeguroSocial(Calendar.getInstance().getTime());
		
		if (this.pojoEmpleado.getIdEmpresa() == null)
			this.pojoEmpleado.setIdEmpresa(this.loginManager.getUsuarioEmpresa());
		
		return true;
	}
	
	private void setDatosPojoEmpleado() {
		this.pojoEmpleado.setHomonimo(this.pojoPersona.getHomonimo());
		this.pojoEmpleado.setNombre(this.pojoPersona.getNombre());
		this.pojoEmpleado.setPrimerNombre(this.pojoPersona.getPrimerNombre());
		this.pojoEmpleado.setSegundoNombre(this.pojoPersona.getSegundoNombre());
		this.pojoEmpleado.setNombresPropios(this.pojoPersona.getNombresPropios());
		this.pojoEmpleado.setPrimerApellido(this.pojoPersona.getPrimerApellido());
		this.pojoEmpleado.setSegundoApellido(this.pojoPersona.getSegundoApellido());
	}

	public void cargarSucursales() {
		try {
			if (this.listaCboSucursales == null)
				this.listaCboSucursales = new ArrayList<SelectItem>();
			this.listaCboSucursales.clear();
			
			this.listaSucursales = this.ifzSucursal.buscarSucursales("sucursal", "");
			if (this.listaSucursales != null && ! this.listaSucursales.isEmpty()) {
				for (Sucursal s : this.listaSucursales) {
					this.listaCboSucursales.add(new SelectItem(s.getId(), s.getSucursal()));
				}
			}
		} catch (Exception e) {
			log.error("Error en RecHum.EmpleadoAction.cargarSucursales", e);
		}
	}
	
	public void cargarDiasDescanso() {
		//Se iniciara con 1 en domingo
		this.listaCboDias = null;
		this.listaCboDias = new ArrayList<SelectItem>();
		this.listaCboDias.add(new SelectItem("1", "Domingo"));
		this.listaCboDias.add(new SelectItem("2", "Lunes"));
		this.listaCboDias.add(new SelectItem("3", "Martes"));
		this.listaCboDias.add(new SelectItem("4", "Miercoles"));
		this.listaCboDias.add(new SelectItem("5", "Jueves"));
		this.listaCboDias.add(new SelectItem("6", "Viernes"));
		this.listaCboDias.add(new SelectItem("7", "Sabado"));
	}
	
	public void cargarFormasPagoEmpleados() {
		try {
			if (pojoFormasPagoEmpleados == null)
				return;
			
			if (this.listFormasPagoEmpleadosItems == null)
				this.listFormasPagoEmpleadosItems = new ArrayList<SelectItem>();
			this.listFormasPagoEmpleadosItems.clear();
			
			this.listFormasPagoEmpleados = this.ifzConValores.findAll(this.pojoFormasPagoEmpleados);
			if (this.listFormasPagoEmpleados != null && ! this.listFormasPagoEmpleados.isEmpty()) {
				for (ConValores var : this.listFormasPagoEmpleados) {
					this.listFormasPagoEmpleadosItems.add(new SelectItem(var.getId(), var.getValor()));
				}
			}
		} catch (Exception e) {
			log.error("Error en cargarFormasPagoEmpleados", e);
		}
	}
		
	public void cargarListaCboPeridiocidad() {
		if (this.listaCboPeridiocidad == null)
			this.listaCboPeridiocidad = new ArrayList<SelectItem>();
		this.listaCboPeridiocidad.clear();
		
		for (ConValores cv : this.listaPeridiocidad) {
			this.listaCboPeridiocidad.add(new SelectItem(cv.getId(), cv.getDescripcion() + "(" + cv.getValor() + ")"));
		}
	}
	
	public void cargarAreas() {
		if (this.listaAreas.isEmpty()) {
			this.listaAreas = this.ifzArea.findAllActivos();
		}
	}
	
	public void cargarCboAreas() {
		if (! this.listaAreas.isEmpty()) {
			if (this.listaCboAreas == null)
				this.listaCboAreas = new ArrayList<SelectItem>();
			this.listaCboAreas.clear();
			
			for (Area a : this.listaAreas) {
				this.listaCboAreas.add(new SelectItem(a.getId().toString(), a.getDescripcion()));
			}
		}
	}
	
	public void cargarCategorias() {
		if (this.listaCategorias.isEmpty()) {
			this.listaCategorias = this.ifzCategorias.findAllActivos();
		}
	}
	
	public void cargarCboCategorias() {
		if (this.listaCboCategorias == null)
			this.listaCboCategorias = new ArrayList<SelectItem>();
		this.listaCboCategorias.clear();
	}
	
	public void cargarPuestos() {
		if (this.listaPuestos.isEmpty()) {
			this.listaPuestos = this.ifzPuestos.findAllActivos();
		}
	}
	
	public void cargarCboPuestos() {
		if (! this.listaPuestos.isEmpty()) {
			if (this.listaCboPuestos == null)
				this.listaCboPuestos = new ArrayList<SelectItem>();
			this.listaCboPuestos.clear();
			
			for (Puesto p : this.listaPuestos) {
				this.listaCboPuestos.add(new SelectItem(p.getId(), p.getDescripcion()));
			}
		}
	}

	public void cargarListaCboRelaciones() {
		if (this.listaCboRelaciones == null)
			this.listaCboRelaciones = new ArrayList<SelectItem>();
		this.listaCboRelaciones.clear();
		
		for (ConValores cv : this.listaRelacionesFamiliares) {
			this.listaCboRelaciones.add(new SelectItem(cv.getId(), cv.getValor()));
		}
	}
		
	public void cargarListaCboRelacionesBeneficiarios() {
		if (this.listaCboRelacionesBeneficiarios == null)
			this.listaCboRelacionesBeneficiarios = new ArrayList<SelectItem>();
		this.listaCboRelacionesBeneficiarios.clear();
		
		for (ConValores cv : this.listaRelacionesBeneficiarios) {
			this.listaCboRelacionesBeneficiarios.add(new SelectItem(cv.getId(), cv.getValor()));
		}
	}

	private void filtrarCategorias(long idPuesto) {
		if (idPuesto == 0) return;
		
		if (this.listaCboCategorias == null)
			this.listaCboCategorias = new ArrayList<SelectItem>();
		this.listaCboCategorias.clear();
		
		if (this.listaCategorias == null)
			this.listaCategorias = new ArrayList<Categoria>();
		this.listaCategorias.clear();
		
		List<PuestoCategoria> listaPuestosCategorias = this.ifzPuestosCategorias.findByIdPuesto(idPuesto);
		for (PuestoCategoria pc: listaPuestosCategorias) {
			this.listaCategorias.add(pc.getIdCategoria());
			this.listaCboCategorias.add(new SelectItem(pc.getIdCategoria().getId(), pc.getIdCategoria().getDescripcion()));
		}
	}

	public void cargaCategorias() {
		if (this.idPuesto > 0) {
			filtrarCategorias(this.idPuesto);
		}
	}

	public void preRenderView() {
        FocusManager focusManager = ServiceTracker.getService(FocusManager.class);
        focusManager.focus(this.inputNameFocused);
    }
	
	private void control() {
		control(false, 0, "", null);
	}
	
	private void control(String mensaje, Throwable t) {
		if (mensaje == null || "".equals(mensaje)) {
			control();
			return;
		}
		
		control(false, 1, mensaje, t);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable exception) {
		StringWriter sw = null;
		
		this.operacionTerminada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje;
		this.mensajeDetalles = "";
		
		if (this.mensaje != null && this.mensaje.contains("\n"))
			this.mensaje = this.mensaje.replace("\n", "<br>");
		
		if (exception != null) {
			sw = new StringWriter();
			exception.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
			log.error(mensaje, exception);
		}
	}

	// -----------------------------------------------------------------
	// BUSQUEDA PERSONAS
	// -----------------------------------------------------------------

	public void nuevaBusquedaPersona() {
		control();
		if (this.listaPersonasGrid == null)
			this.listaPersonasGrid = new ArrayList<Persona>();
		this.listaPersonasGrid.clear();
		
		this.campoBusquedaPersonas = this.tipoBusquedaPersonas.get(0).getValue().toString();
		this.valBusquedaPersonas = "";
		this.numPaginaPersonas = 1;
		this.inputNameFocused = "txtvalorPersona";
		this.operacionTerminada = true;
	}
	
	public void buscarPersonas() {
		List<Persona> listaTotalPersonas = new ArrayList<Persona>();
		int tipoPersonaFisica = 1;
		
		try {
			control();
			if (this.campoBusquedaPersonas == null || "".equals(this.campoBusquedaPersonas.trim()))
				this.campoBusquedaPersonas = this.tipoBusquedaPersonas.get(0).getValue().toString();
			
			listaTotalPersonas = this.ifzClientes.buscarPersona(this.campoBusquedaPersonas, this.valBusquedaPersonas);
			if (listaTotalPersonas != null && ! listaTotalPersonas.isEmpty()) {
				log.info(listaTotalPersonas.size() + " registros encontrados");
				for (Persona p : listaTotalPersonas) {
					if (p.getTipoPersona() == tipoPersonaFisica) {	//Si es una persona fisica
						this.listaPersonasGrid.add(p);
					}
				}
			}
			
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al buscar Personas", e);
		} finally {
			log.info(this.listaPersonasGrid.size() + " registros mostrados");
			this.inputNameFocused = "txtvalorPersona";
		}
	}

	public void seleccionarPersona() {
		control();
		this.nombreEmpleado = this.pojoPersona.getNombre();
		this.pojoEmpleado.setPersona(this.pojoPersona);
		nuevaBusquedaPersona();
		log.info("Selecciono " + this.pojoPersona.getId() + " - " + this.pojoPersona.getNombre());
		this.inputNameFocused = "txtClave";
		this.operacionTerminada = true;
	}

	// -----------------------------------------------------------------
	// CONTRATOS
	// -----------------------------------------------------------------

	public void cargarContratos() {
		try {
			this.pojoContratoBase = null;
			this.pojoContratoBorrar = null;
			if (this.listaContratosGrid == null)
				this.listaContratosGrid = new ArrayList<EmpleadoContrato>();
			this.listaContratosGrid.clear();
			
			this.listaContratosGrid = this.ifzContrato.findAllByIdEmpleado(this.pojoEmpleado.getId());
			Collections.sort(this.listaContratosGrid, new Comparator<EmpleadoContrato>() {
				@Override
				public int compare(EmpleadoContrato o1, EmpleadoContrato o2) {
					return o2.getId().compareTo(o1.getId());
				}
			});
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar los Contratos del Empleado seleccionado", e);
		}
	}

	public void nuevoContrato() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		EmpleadoContrato last = null;

		try {
			control();
			this.esContratoNuevo = true;
			this.pojoContrato = new EmpleadoContrato();
			this.pojoContrato.setTipoHorario(-1);
	
			if (this.listaContratosGrid != null && ! this.listaContratosGrid.isEmpty()) {
				// Recupero el ultimo contrato (primero porque el listado tiene orden descendente
				last = this.listaContratosGrid.get(0);
				this.pojoContratoBase = copiarContrato(last);
				this.pojoContrato = copiarContrato(last);
				this.pojoContrato.setEstatus(0);
				this.pojoContrato.setId(0L);
	
				this.horaEntrada = "00:00";
				this.horaSalida = "00:00";
				this.horaEntradaComplemento = "00:00";
				this.horaSalidaComplemento = "00:00";
				
				if (this.pojoContrato.getHoraEntrada() != null)
					this.horaEntrada = formatter.format(this.pojoContrato.getHoraEntrada());
				
				if (this.pojoContrato.getHoraSalida() != null)
					this.horaSalida = formatter.format(this.pojoContrato.getHoraSalida());
				
				if (this.pojoContrato.getHoraEntradaComplemento() != null)
					this.horaEntradaComplemento = formatter.format(this.pojoContrato.getHoraEntradaComplemento());
				
				if (this.pojoContrato.getHoraSalidaComplemento() != null)
					this.horaSalidaComplemento = formatter.format(this.pojoContrato.getHoraSalidaComplemento());
				
				if (this.pojoContrato.getTipoHorario() == 1) {
					this.horaEntradaComplemento = "";
					this.horaSalidaComplemento = "";
				}
			}
			
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al generar un nuevo Contrato", e);
		}
	}
	
	public void editarContrato() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		
		try {
			control();
			if (this.pojoContrato == null || this.pojoContrato.getId() == null || this.pojoContrato.getId() <= 0L) {
				control("Ocurrio un problema al intentar recuperar el Contrato", null);
				return;
			}
			
			this.esContratoNuevo = false;
			if (this.usuarioId == 1) {
				if (this.pojoEmpleado.getNombre().contains("TOTAL") && (this.pojoEmpleado.getNombre().contains("NOMINA") || this.pojoEmpleado.getNombre().contains("ADICIONALES")))
					this.esContratoNuevo = true;
			}
			
			this.idPeridiocidadPago = this.pojoContrato.getPeridiocidadPago();
			this.idDiaDescanso = this.pojoContrato.getDiaDescanso();
			this.horaEntrada = formatter.format(this.pojoContrato.getHoraEntrada());
			this.horaSalida = formatter.format(this.pojoContrato.getHoraSalida());
			if (this.pojoContrato.getTipoHorario() == 0) {
				this.horaEntradaComplemento = formatter.format(this.pojoContrato.getHoraEntradaComplemento());
				this.horaSalidaComplemento = formatter.format(this.pojoContrato.getHoraSalidaComplemento());
			}
			
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar el Contrato para edicion", e);
		}
	}
	
	public void guardarContrato() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		
		try {
			control();
			log.info("Guardando contrato" + (this.pojoContrato.getId() == null || this.pojoContrato.getId() <= 0L ? " nuevo" : ""));
			if (! validarContrato()) 
				return;

			this.pojoContrato.setIdEmpleado(this.ifzEmpleado.convertir(this.pojoEmpleado));
			this.pojoContrato.setHoraEntrada(formatter.parse(this.horaEntrada));
			this.pojoContrato.setHoraSalida(formatter.parse(this.horaSalida));
			if (this.pojoContrato.getTipoHorario() == 0) {
				this.pojoContrato.setHoraEntradaComplemento(formatter.parse(this.horaEntradaComplemento));
				this.pojoContrato.setHoraSalidaComplemento(formatter.parse(this.horaSalidaComplemento));
			}

			if (this.pojoContrato.getId() == null || this.pojoContrato.getId() <= 0L) {
				this.pojoContrato.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoContrato.setCreadoPor(this.usuarioId);
				this.pojoContrato.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoContrato.setModificadoPor(this.usuarioId);
				
				// Guardamos en la BD y asignamos ID.
				this.pojoContrato.setId(this.ifzContrato.save(this.pojoContrato));
				log.info("Contrato guardado");
			} else {
				// Actualizar en la BD.
				this.pojoContrato.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoContrato.setModificadoPor(this.usuarioId);
				this.ifzContrato.update(this.pojoContrato);
				log.info("Contrato actualizado");
			}

			// Recupero el ultimo contrato y lo damos de baja, si corresponde
			if (this.pojoContratoBase != null && this.pojoContratoBase.getId() != null && this.pojoContratoBase.getId() > 0L) {
				this.pojoContratoBorrar = copiarContrato(this.pojoContratoBase);
				this.pojoContratoBase = null;
				eliminarContrato();
				return;
			}
			
			cargarContratos();
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al guardar Contrato", e);
		}
	}
	
	public void eliminarContrato() {
		try {
			control();
			if (this.pojoContratoBorrar != null && this.pojoContratoBorrar.getId() != null && this.pojoContratoBorrar.getId() > 0L) {
				this.pojoContratoBorrar.setEstatus(1);
				this.pojoContratoBorrar.setModificadoPor(this.usuarioId);
				this.pojoContratoBorrar.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Borramos el contrato de la BD
				this.ifzContrato.update(this.pojoContratoBorrar);
				
				// Recargamos los contratos
				cargarContratos();
			}
			
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Contrato", e);
			return;
		}
	}

	private boolean validarContrato() {
		//area de la validacion para el contrato
		if (this.pojoEmpleado == null || this.pojoEmpleado.getId() == null || this.pojoEmpleado.getId() <= 0L) {
			control("Error al recuperar los datos del empleado.", null);
			return false;
		}
		
		if (this.pojoContrato.getFechaInicio() == null) {
			control("Debe indicar la fecha de inicio del contrato", null);
			return false;
		}
		
		if (this.pojoContrato.getPeridiocidadPago() <= 0) {
			control("Debe indicar la peridiocidad del pago", null);
			return false;
		}
		
		if (this.pojoContrato.getDeterminado() == 1) { // Es determinado, hay que incluir la fecha de termino: validar
			if (this.pojoContrato.getFechaFin() == null) {
				control("Debe indicar la fecha de termino del contrato", null);
				return false;
			}
			
			if (this.pojoContrato.getFechaInicio().compareTo(pojoContrato.getFechaFin()) >= 0) {
				control("La Fecha de Termino debe ser mayor a la Fecha de Inicio", null);
				return false;
			}
		}
		
		if (this.pojoContrato.getDiaDescanso() <= 0) {
			control("Debe indicar el día de descanso", null);
			return false;
		}
		
		if (this.pojoContrato.getCentroTrabajo().trim().equals("")) {
			control("Indique el centro de trabajo", null);
			return false;
		}
		
		if (this.pojoContrato.getSueldo() <= 0) {
			control("Debe indicar el sueldo", null);
			return false;
		}
		
		if (this.pojoContrato.getSueldoHora() == null || this.pojoContrato.getSueldoHora().doubleValue() <= 0) {
			control("Debe indicar el sueldo hora", null);
			return false;
		}
		
		if (this.pojoContrato.getSueldoHoraExtra() == null || this.pojoContrato.getSueldoHoraExtra().doubleValue() <= 0) {
			control("Debe indicar el sueldo hora extra", null);
			return false;
		}
		
		return true;
	}

	private EmpleadoContrato copiarContrato(EmpleadoContrato target) {
		EmpleadoContrato resultado = new EmpleadoContrato();
		
		try {
			if (target == null)
				return null;
			BeanUtils.copyProperties(resultado, target);
		} catch (Exception e) {
			resultado.setId(target.getId());
			resultado.setIdEmpleado(target.getIdEmpleado());
			resultado.setFechaInicio(target.getFechaInicio());
			resultado.setFechaFin(target.getFechaFin());
			resultado.setSueldo(target.getSueldo());
			resultado.setPeridiocidadPago(target.getPeridiocidadPago());
			resultado.setDiaDescanso(target.getDiaDescanso());
			resultado.setCentroTrabajo(target.getCentroTrabajo());
			resultado.setDeterminado(target.getDeterminado());
			resultado.setDescuentoInfonavit(target.getDescuentoInfonavit());
			resultado.setHoraEntrada(target.getHoraEntrada());
			resultado.setHoraSalida(target.getHoraSalida());
			resultado.setHoraEntradaComplemento(target.getHoraEntradaComplemento());
			resultado.setHoraSalidaComplemento(target.getHoraSalidaComplemento());
			resultado.setFormaPago(target.getFormaPago());
			resultado.setTipoHorario(target.getTipoHorario());
			resultado.setSueldoHora(target.getSueldoHora());
			resultado.setSueldoHoraExtra(target.getSueldoHoraExtra());
			resultado.setEstatus(target.getEstatus());
			resultado.setCreadoPor(target.getCreadoPor());
			resultado.setFechaCreacion(target.getFechaCreacion());
			resultado.setModificadoPor(target.getModificadoPor());
			resultado.setFechaModificacion(target.getFechaModificacion());
		} 
		
		return resultado;
	}
	
	public void calcularSueldoHora() {
		double sueldoHora = 0;
		
		try {
			control();
			if (this.pojoContrato == null) {
				control("Ocurrio un problema al intentar Calcular el Sueldo por Hora. Contrato no cargado", null);
				return;
			}
			
			// Calculando Sueldo Hora
			log.info("Calculando Sueldo Hora ... ");
			sueldoHora = this.pojoContrato.getSueldo();
			if (this.pojoContrato.getSueldo() > 0) {
				sueldoHora =((sueldoHora / 7) / 8);
				sueldoHora = Double.parseDouble((new DecimalFormat("###,###,###,##0.00")).format(sueldoHora));
			}
			
			this.pojoContrato.setSueldoHora(new BigDecimal(sueldoHora));
			log.info("Sueldo Hora calculado: " + sueldoHora);
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Calcular el Sueldo por Hora", null);
		}
	}
	
	public void reiniciaHorario() {
		if (this.pojoContrato == null)
			return;
		
		if (this.pojoContrato.getTipoHorario() == 1) {
			this.horaEntradaComplemento = "00:00";
			this.horaSalidaComplemento = "00:00";
			this.horaSalida = "16:00";
		} else {
			this.horaEntradaComplemento = "16:00";
			this.horaSalidaComplemento = "18:00";
			this.horaSalida = "14:00";
		}
	}
	
	// -----------------------------------------------------------------
	// BENEFICIARIOS 
	// -----------------------------------------------------------------

	public void cargarBeneficiarios() {
		try {
			if (this.listaBeneficiariosGrid == null)
				this.listaBeneficiariosGrid = new ArrayList<EmpleadoBeneficiarioExt>();
			this.listaBeneficiariosGrid.clear();
			this.numPaginaBeneficiarios = 1;
	
			if (this.listaBeneficiariosEliminados == null)
				this.listaBeneficiariosEliminados = new ArrayList<EmpleadoBeneficiarioExt>();
			this.listaBeneficiariosEliminados.clear();
			
			this.listaBeneficiariosGrid = this.ifzEmpleadoBeneficiario.findByIdEmpleado(this.pojoEmpleado.getId());
		} catch (Exception e) {
			log.error("Error en RecHum.EmpleadoAction.cargarBeneficiarios", e);
		}
	}

	public void nuevoBeneficiario() {
		control();
		this.pojoBeneficiario = new EmpleadoBeneficiarioExt();
		this.pojoBeneficiarioSeleccionado = new Persona();
		this.operacionTerminada = true;
	}
	
	public void editarBeneficiario() {
		control();
		this.operacionTerminada = true;
	}
	
	public void guardarBeneficiario() {
		try {
			control();
			if (pojoBeneficiario.getPersona() == null) {
				control("No ha seleccionado beneficiario", null);
				return;
			}
			
			if (this.pojoBeneficiario.getPorcentaje() == null || this.pojoBeneficiario.getPorcentaje() == 0) {
				control("El porcentaje debe ser mayor a cero", null);
				return;
			}
			
			if (this.getTotalPorcentajeBeneficiario() < 0) {
				control("El porcentaje que intenta agregar excede el 100%", null);
				return;
			}
			
			for (EmpleadoBeneficiarioExt e : this.listaBeneficiariosGrid) {
				if (this.pojoEmpleado.getPersona().getId() == e.getPersona().getId()) {
					control("El empleado no puede ser beneficiario", null);
					return;
				}
			}
			
			for (EmpleadoBeneficiarioExt e : this.listaBeneficiariosGrid) {
				if (this.pojoBeneficiario.getPersona().getId() == e.getPersona().getId()) {
					control("El beneficiario ya se encuentra en la lista", null);
					return;
				}
			}

			this.pojoBeneficiario.setEmpleado(this.pojoEmpleado);
			this.pojoBeneficiario.setModificadoPor(this.usuarioId);
			this.pojoBeneficiario.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.pojoBeneficiario.getId() == null) {	//nuevo beneficiario
				this.pojoBeneficiario.setCreadoPor(this.usuarioId);
				this.pojoBeneficiario.setFechaCreacion(Calendar.getInstance().getTime());
				
				this.listaBeneficiariosGrid.add(this.pojoBeneficiario);
				this.pojoBeneficiario.setId(this.ifzEmpleadoBeneficiario.save(this.pojoBeneficiario));
			} else {
				// Actualizamos beneficiario
				this.ifzEmpleadoBeneficiario.update(this.pojoBeneficiario);
			}
			
			cargarBeneficiarios();
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar el Beneficiario", e);
		}
	}

	public void eliminarBeneficiario() {
		try {
			control();
			this.ifzEmpleadoBeneficiario.delete(this.pojoBeneficiario);
			this.listaBeneficiariosEliminados.add(this.pojoBeneficiario);
			for (EmpleadoBeneficiarioExt ebe : this.listaBeneficiariosGrid) {
				if (ebe.getId() == this.pojoBeneficiario.getId()) {
					this.listaBeneficiariosGrid.remove(ebe);
					break;
				}
			}

			cargarBeneficiarios();
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Beneficiario", e);
		}
	}
	
	public void seleccionarBeneficiario() {
		control();
		if (this.pojoBeneficiarioSeleccionado == null || this.pojoBeneficiarioSeleccionado.getId() <= 0L) {
			control("Ocurrio un problema al seleccionar la persona.", null);
			return;
		}	
		
		if (this.pojoBeneficiarioSeleccionado.getId() == this.pojoEmpleado.getPersona().getId()) {
			control("No se permite seleccionar al mismo empleado como beneficiario.", null);
			this.pojoBeneficiarioSeleccionado = new Persona();
			return;
		}
		
		this.operacionTerminada = true;
	}

	public double getTotalPorcentajeBeneficiario() {	//regresará el total del porcentaje que se ha ocupado para los beneficiarios
		double porcentaje = 0;
		
		porcentaje += this.pojoBeneficiario.getPorcentaje()==null ? 0 : this.pojoBeneficiario.getPorcentaje();
		for (EmpleadoBeneficiarioExt ebe:this.listaBeneficiariosGrid)
			porcentaje += ebe.getPorcentaje();
		
		if (this.pojoBeneficiario.getId() != null)
			porcentaje = porcentaje - this.pojoBeneficiario.getPorcentaje(); //descontar el porcetaje que tenia para sumar el nuevo
		
		return 100 - porcentaje;
	}
	
	// -----------------------------------------------------------------
	// FAMILIAR
	// -----------------------------------------------------------------

	public void cargarFamiliares() {
		try {
			if (this.listaFamiliaresGrid == null)
				this.listaFamiliaresGrid = new ArrayList<EmpleadoParienteExt>();
			this.listaFamiliaresGrid.clear();
			this.numPaginaFamiliares = 1;
			
			if (this.listaFamiliaresEliminados == null)
				this.listaFamiliaresEliminados = new ArrayList<EmpleadoParienteExt>();
			this.listaFamiliaresEliminados.clear();
			
			this.pojoFamElimSeleccionado = new EmpleadoParienteExt();
			this.listaFamiliaresGrid = this.ifzEmpleadoPariente.findByIdEmpleadoParentesco(this.pojoEmpleado.getId());
		} catch (Exception e) {
			log.error("Error en RecHum.EmpleadoAction.cargarFamiliares", e);
		}
	}

	public void nuevoFamiliar() {
		control();
		this.pojoFamiliar = new EmpleadoParienteExt();
		this.operacionTerminada = true;
	}
	
	public void editarFamiliar() {
		control();
		this.operacionTerminada = true;
	}

	public void guardarFamiliar() {
		try {
			control();
			if (! validarGuardarFamiliar())
				return;
			
			this.pojoFamElimSeleccionado.setEmpleado(this.pojoEmpleado);			
			this.pojoFamElimSeleccionado.setRelacion(this.pojoRelacion);
			this.pojoFamElimSeleccionado.setModificadoPor(this.usuarioId);
			this.pojoFamElimSeleccionado.setFechaModificacion(Calendar.getInstance().getTime());

			if (this.pojoFamElimSeleccionado.getId() == null) {	
				this.pojoFamElimSeleccionado.setCreadoPor(this.usuarioId);
				this.pojoFamElimSeleccionado.setFechaCreacion(Calendar.getInstance().getTime());
				
				this.pojoFamElimSeleccionado.setId(this.ifzEmpleadoPariente.save(this.pojoFamElimSeleccionado));
				this.listaFamiliaresGrid.add(this.pojoFamElimSeleccionado);	
			} else {
				this.ifzEmpleadoPariente.update(this.pojoFamElimSeleccionado);
			}
			
			cargarFamiliares();
			this.operacionTerminada = true;
		} catch (Exception e1) {
			control("Ocurrio un problema al guardar el familiar", e1);
		}
	}

	public void eliminarFamiliar() {
		try {
			control();
			this.ifzEmpleadoPariente.delete(this.pojoFamElimSeleccionado);
			for (EmpleadoParienteExt fam : this.listaFamiliaresGrid) {
				if (fam.getPersona().getId() == pojoFamElimSeleccionado.getPersona().getId()) {
					this.listaFamiliaresGrid.remove(fam);
					break;
				}
			}

			cargarFamiliares();
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Error al eliminar el familiar", e);
		}
	}

	private boolean validarGuardarFamiliar() {
		if (this.pojoFamElimSeleccionado.getPersona() == null) {
			control("No se ha seleccionado persona", null);
			return false;
		}
		
		if (this.pojoFamElimSeleccionado.getPersona().getId() == this.pojoEmpleado.getPersona().getId()) {
			control("No se puede agregar como familiar al empleado mismo", null);
			return false;
		}
		
		for (EmpleadoParienteExt e : this.listaFamiliaresGrid) {
			if (this.pojoFamElimSeleccionado.getPersona().getId() == e.getPersona().getId()) {
				control("El familiar que pretende agregar ya se encuentra en la lista", null);
				return false;
			}
		}
		
		return true;
	}

	public void seleccionarFamiliar() {
		control();
		this.operacionTerminada = true;
	}
	
	// ----------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------
	
	public String getTitulo() {
		if (this.pojoEmpleado != null && this.pojoEmpleado.getId() != null && this.pojoEmpleado.getId() > 0L)
			return "Empleado " + this.pojoEmpleado.getId();
		return "Nuevo Empleado";
	}
	
	public void setTitulo(String value) {}
	
	public long getIdArea() {
		return idArea;
	}

	public void setIdArea(long idArea) {
		this.idArea = idArea;

		this.areaValidador = "";
		for (Area a : this.listaAreas) {
			if (a.getId() == idArea) {
				this.pojoArea = a;
				this.areaValidador = this.pojoArea.getDescripcion();
				break;
			}
		}
	}

	public long getIdPuesto() {
		return idPuesto;
	}

	public void setIdPuesto(long idPuesto) {
		this.idPuesto = idPuesto;
		
		this.puestoValidador = "";
		for (Puesto p: this.listaPuestos) {
			if (p.getId() == idPuesto) {
				this.pojoPuesto = p;
				this.puestoValidador = this.pojoPuesto.getDescripcion();
				break;
			}
		}
		
		//Recargar las categorias que cumplan con el puesto
		this.filtrarCategorias(idPuesto);
	}
	
	public long getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(long idCategoria) {
		this.idCategoria = idCategoria;
		
		this.categoriaValidador = "";
		for (Categoria c : this.listaCategorias) {
			if (c.getId() == idCategoria) {
				this.pojoCategoria = c;
				this.categoriaValidador = this.pojoCategoria.getDescripcion();
				break;
			}
		}
	}

	public String getFechaAltaSeguroSocial() {
		if (this.pojoEmpleado != null && this.pojoEmpleado.getFechaAltaSeguroSocial() != null)
			return "Alta el " +(new SimpleDateFormat("dd-MMM-yyyy")).format(this.pojoEmpleado.getFechaAltaSeguroSocial());
		return "";
	}
	
	public void setFechaAltaSeguroSocial(String value) {}
	
	public long getIdRelacion() {
		return idRelacion;
	}

	public void setIdRelacion(long idRelacion) {
		this.idRelacion = idRelacion;
		
		if (idRelacion <= 0)
			return;
		
		for (ConValores c : this.listaRelacionesFamiliares) {
			if (c.getId() == idRelacion) {
				this.pojoRelacion = c;
				if (this.pojoFamElimSeleccionado != null)
					this.pojoFamElimSeleccionado.setRelacion(c);
				break;
			}
		}
	}
	
	public long getIdRelacionBeneficiario() {
		return idRelacionBeneficiario;
	}

	public void setIdRelacionBeneficiario(long idRelacionBeneficiario) {
		this.idRelacionBeneficiario = idRelacionBeneficiario;
		
		if (idRelacionBeneficiario <= 0)
			return;
		
		for (ConValores c : this.listaRelacionesBeneficiarios) {
			if (c.getId() == idRelacionBeneficiario) {
				if (this.pojoBeneficiario != null)
					this.pojoBeneficiario.setRelacion(c);	
				break;
			}
		}
	}

	public long getIdPeridiocidadPago() {
		return idPeridiocidadPago;
	}

	public void setIdPeridiocidadPago(long idPeridiocidadPago) {
		this.idPeridiocidadPago = idPeridiocidadPago;
		
		for (ConValores p : this.listaPeridiocidad) {
			if (p.getId() == idPeridiocidadPago) {
				this.pojoContrato.setPeridiocidadPago(p.getId());
				break;
			}
		}
	}

	public long getIdSucursal() {
		return this.pojoEmpleado.getSucursal() == null ? 0 : this.pojoEmpleado.getSucursal().getId();
	}
	
	public void setIdSucursal(long idSucursal) {
		if (idSucursal > 0) {
			for (Sucursal s : this.listaSucursales) {
				if (s.getId() == idSucursal) {
					this.pojoEmpleado.setSucursal(s);
					break;
				}
			}
		}	
	}

	public TimeZone getTimeZone() {
		return this.timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public int getNumPaginaPersonas() {
		return numPaginaPersonas;
	}

	public void setNumPaginaPersonas(int numPaginaPersonas) {
		this.numPaginaPersonas = numPaginaPersonas;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public boolean isBand() {
		return operacionTerminada;
	}

	public void setBand(boolean band) {
		this.operacionTerminada = band;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
	}

	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}
	
	public Empleado getPojoEmpleadoMain() {
		return pojoEmpleadoMain;
	}

	public void setPojoEmpleadoMain(Empleado pojoEmpleadoMain) {
		this.pojoEmpleadoMain = pojoEmpleadoMain;
	}
	
	public EmpleadoExt getPojoEmpleado() {
		return pojoEmpleado;
	}

	public void setPojoEmpleado(EmpleadoExt pojoEmpleado) {
		this.pojoEmpleado = pojoEmpleado;
	}

	public List<Empleado> getListaEmpleadosGrid() {
		return listaEmpleadosGrid;
	}

	public void setListaEmpleadosGrid(List<Empleado> listaEmpleadosGrid) {
		this.listaEmpleadosGrid = listaEmpleadosGrid;
	}

	public List<Persona> getListaPersonasGrid() {
		return listaPersonasGrid;
	}

	public void setListaPersonasGrid(List<Persona> listaPersonasGrid) {
		this.listaPersonasGrid = listaPersonasGrid;
	}

	public Persona getPojoPersona() {
		return pojoPersona;
	}

	public void setPojoPersona(Persona pojoPersona) {
		
		this.pojoPersona = pojoPersona;
	}

	public List<SelectItem> getTipoBusquedaPersonas() {
		return tipoBusquedaPersonas;
	}

	public void setTipoBusquedaPersonas(List<SelectItem> tipoBusquedaPersonas) {
		this.tipoBusquedaPersonas = tipoBusquedaPersonas;
	}

	public String getValBusquedaPersonas() {
		return valBusquedaPersonas;
	}

	public void setValBusquedaPersonas(String valBusquedaPersonas) {
		this.valBusquedaPersonas = valBusquedaPersonas;
	}

	public String getCampoBusquedaPersonas() {
		return campoBusquedaPersonas;
	}

	public void setCampoBusquedaPersonas(String campoBusquedaPersonas) {
		this.campoBusquedaPersonas = campoBusquedaPersonas;
	}

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}

	public String getPuestoValidador() {
		return puestoValidador;
	}

	public void setPuestoValidador(String puestoValidador) {
		this.puestoValidador = puestoValidador;
	}

	public String getCategoriaValidador() {
		return categoriaValidador;
	}

	public void setCategoriaValidador(String categoriaValidador) {
		this.categoriaValidador = categoriaValidador;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getNumeroSeguridadSocial() {
		return numeroSeguridadSocial;
	}

	public void setNumeroSeguridadSocial(String numeroSeguridadSocial) {
		this.numeroSeguridadSocial = numeroSeguridadSocial;
	}

	public String getEmergencia() {
		return emergencia;
	}

	public void setEmergencia(String emergencia) {
		this.emergencia = emergencia;
	}

	public boolean isExterno() {
		return externo;
	}

	public void setExterno(boolean externo) {
		this.externo = externo;
	}
	
	public boolean getAltaSeguroSocial() {
		return (this.pojoEmpleado.getAltaSeguroSocial() == 1);
	}

	public void setAltaSeguroSocial(boolean altaSeguroSocial) {
		this.pojoEmpleado.setAltaSeguroSocial((altaSeguroSocial ? 1 : 0));
	}
	
	public List<SelectItem> getListaCboAreas() {
		return listaCboAreas;
	}

	public void setListaCboAreas(List<SelectItem> listaCboAreas) {
		this.listaCboAreas = listaCboAreas;
	}

	public List<Area> getListaAreas() {
		return listaAreas;
	}

	public void setListaAreas(List<Area> listaAreas) {
		this.listaAreas = listaAreas;
	}

	public Area getPojoArea() {
		return pojoArea;
	}

	public void setPojoArea(Area pojoArea) {
		this.pojoArea = pojoArea;
		this.areaValidador = this.pojoArea.getDescripcion();
	}

	public String getAreaValidador() {
		return areaValidador;
	}

	public void setAreaValidador(String areaValidador) {
		this.areaValidador = areaValidador;
	}

	public List<SelectItem> getListaCboPuestos() {
		return listaCboPuestos;
	}

	public void setListaCboPuestos(List<SelectItem> listaCboPuestos) {
		this.listaCboPuestos = listaCboPuestos;
	}

	public List<Puesto> getListaPuestos() {
		return listaPuestos;
	}

	public void setListaPuestos(List<Puesto> listaPuestos) {
		this.listaPuestos = listaPuestos;
	}

	public Puesto getPojoPuesto() {
		return pojoPuesto;
	}

	public void setPojoPuesto(Puesto pojoPuesto) {
		this.pojoPuesto = pojoPuesto;
	}

	public List<SelectItem> getListaCboCategorias() {
		return listaCboCategorias;
	}

	public void setListaCboCategorias(List<SelectItem> listaCboCategorias) {
		this.listaCboCategorias = listaCboCategorias;
	}

	public List<Categoria> getListaCategorias() {
		return listaCategorias;
	}

	public void setListaCategorias(List<Categoria> listaCategorias) {
		this.listaCategorias = listaCategorias;
	}

	public Categoria getPojoCategoria() {
		return pojoCategoria;
	}

	public void setPojoCategoria(Categoria pojoCategoria) {
		this.pojoCategoria = pojoCategoria;
	}

	public int getNumPaginaFamiliares() {
		return numPaginaFamiliares;
	}

	public void setNumPaginaFamiliares(int numPaginaFamiliares) {
		this.numPaginaFamiliares = numPaginaFamiliares;
	}

	public int getNumPaginaBeneficiarios() {
		return numPaginaBeneficiarios;
	}

	public void setNumPaginaBeneficiarios(int numPaginaBeneficiarios) {
		this.numPaginaBeneficiarios = numPaginaBeneficiarios;
	}

	public EmpleadoParienteExt getPojoFamiliar() {
		return pojoFamiliar;
	}

	public void setPojoFamiliar(EmpleadoParienteExt pojoFamiliar) {
		this.pojoFamiliar = pojoFamiliar;
	}

	public List<EmpleadoParienteExt> getListaFamiliaresGrid() {
		return listaFamiliaresGrid;
	}

	public void setListaFamiliaresGrid(List<EmpleadoParienteExt> listaFamiliaresGrid) {
		this.listaFamiliaresGrid = listaFamiliaresGrid;
	}

	public Persona getPojoFamiliarSeleccionado() {
		return pojoFamiliarSeleccionado;
	}

	public void setPojoFamiliarSeleccionado(Persona pojoFamiliarSeleccionado) {
		this.pojoFamElimSeleccionado.setPersona(pojoFamiliarSeleccionado);
		this.pojoFamiliarSeleccionado = pojoFamiliarSeleccionado;
	}

	public List<ConValores> getListRelacionesFamiliares() {
		return listaRelacionesFamiliares;
	}

	public void setListRelacionesFamiliares(List<ConValores> listRelacionesFamiliares) {
		this.listaRelacionesFamiliares = listRelacionesFamiliares;
	}

	public List<SelectItem> getListaCboRelaciones() {
		return listaCboRelaciones;
	}

	public void setListaCboRelaciones(List<SelectItem> listaCboRelaciones) {
		this.listaCboRelaciones = listaCboRelaciones;
	}

	public Persona getPojoBeneficiarioSeleccionado() {
		return pojoBeneficiarioSeleccionado;
	}

	public void setPojoBeneficiarioSeleccionado(Persona pojoBeneficiarioSeleccionado) {
		this.pojoBeneficiarioSeleccionado = pojoBeneficiarioSeleccionado;
		this.pojoBeneficiario.setPersona(pojoBeneficiarioSeleccionado);
	}

	public EmpleadoParienteExt getPojoFamElimSeleccionado() {
		return pojoFamElimSeleccionado;
	}

	public void setPojoFamElimSeleccionado(EmpleadoParienteExt pojoFamElimSeleccionado) {
		this.pojoFamElimSeleccionado = pojoFamElimSeleccionado;
		this.setIdRelacion(pojoFamElimSeleccionado.getRelacion().getId());
	}

	public List<EmpleadoBeneficiarioExt> getListaBeneficiariosGrid() {
		return listaBeneficiariosGrid;
	}

	public void setListaBeneficiariosGrid(List<EmpleadoBeneficiarioExt> listaBeneficiariosGrid) {
		this.listaBeneficiariosGrid = listaBeneficiariosGrid;
	}

	public EmpleadoBeneficiarioExt getPojoBeneficiario() {
		return pojoBeneficiario;
	}

	public void setPojoBeneficiario(EmpleadoBeneficiarioExt pojoBeneficiario) {
		this.pojoBeneficiario = pojoBeneficiario;
	}

	public String getMensajeErrorBeneficiario() {
		return mensajeErrorBeneficiario;
	}

	public void setMensajeErrorBeneficiario(String mensajeErrorBeneficiario) {
		this.mensajeErrorBeneficiario = mensajeErrorBeneficiario;
	}

	public boolean isBeneficiarioValidado() {
		return beneficiarioValidado;
	}

	public void setBeneficiarioValidado(boolean beneficiarioValidado) {
		this.beneficiarioValidado = beneficiarioValidado;
	}

	public EmpleadoContrato getPojoContrato() {
		return pojoContrato;
	}

	public void setPojoContrato(EmpleadoContrato pojoContrato) {
		this.pojoContrato = pojoContrato;
	}

	public int getIdDiaDescanso() {
		return idDiaDescanso;
	}

	public void setIdDiaDescanso(int idDiaDescanso) {
		this.idDiaDescanso = idDiaDescanso;
	}

	public List<SelectItem> getListaCboRelacionesBeneficiarios() {
		return listaCboRelacionesBeneficiarios;
	}

	public void setListaCboRelacionesBeneficiarios(List<SelectItem> listaCboRelacionesBeneficiarios) {
		this.listaCboRelacionesBeneficiarios = listaCboRelacionesBeneficiarios;
	}

	public List<SelectItem> getListaCboPeridiocidad() {
		return listaCboPeridiocidad;
	}

	public void setListaCboPeridiocidad(List<SelectItem> listaCboPeridiocidad) {
		this.listaCboPeridiocidad = listaCboPeridiocidad;
	}

	public List<SelectItem> getListaCboDias() {
		return listaCboDias;
	}

	public void setListaCboDias(List<SelectItem> listaCboDias) {
		this.listaCboDias = listaCboDias;
	}

	public List<EmpleadoContrato> getListaContratosGrid() {
		return listaContratosGrid;
	}

	public List<SelectItem> getListaCboSucursales() {
		return listaCboSucursales;
	}
	
	public void setListaCboSucursales(List<SelectItem> listaCboSucursales) {
		this.listaCboSucursales = listaCboSucursales;
	}
	
	public void setListaContratosGrid(List<EmpleadoContrato> listaContratosGrid) {
		this.listaContratosGrid = listaContratosGrid;
	}
	
	public String getClave() {
		return pojoEmpleado.getClave() == null ? "" : this.pojoEmpleado.getClave();
	}
	
	public void setClave(String clave) {
		this.pojoEmpleado.setClave(clave);
	}
	
	public List<SelectItem> getListFormasPagoEmpleadosItems() {
		return listFormasPagoEmpleadosItems;
	}

	public void setListFormasPagoEmpleadosItems(List<SelectItem> listFormasPagoEmpleadosItems) {
		this.listFormasPagoEmpleadosItems = listFormasPagoEmpleadosItems;
	}

	public String getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(String horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public String getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}

	public String getHoraEntradaComplemento() {
		return horaEntradaComplemento;
	}

	public void setHoraEntradaComplemento(String horaEntradaComplemento) {
		this.horaEntradaComplemento = horaEntradaComplemento;
	}

	public String getHoraSalidaComplemento() {
		return horaSalidaComplemento;
	}

	public void setHoraSalidaComplemento(String horaSalidaComplemento) {
		this.horaSalidaComplemento = horaSalidaComplemento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EmpleadoContrato getPojoContratoBorrar() {
		return pojoContratoBorrar;
	}

	public void setPojoContratoBorrar(EmpleadoContrato pojoContratoBorrar) {
		this.pojoContratoBorrar = pojoContratoBorrar;
	}

	public boolean isEsContratoNuevo() {
		return esContratoNuevo;
	}

	public void setEsContratoNuevo(boolean esContratoNuevo) {
		this.esContratoNuevo = esContratoNuevo;
	}

	public int getContratosPaginacion() {
		return contratosPaginacion;
	}

	public void setContratosPaginacion(int contratosPaginacion) {
		this.contratosPaginacion = contratosPaginacion;
	}
}
