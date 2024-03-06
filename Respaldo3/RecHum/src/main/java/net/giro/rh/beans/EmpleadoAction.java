package net.giro.rh.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.ClientesRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.plataforma.logica.EmpresasRem;
import net.giro.respuesta.Respuesta;
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
import net.giro.rh.admon.logica.EmpleadoFiniquitoRem;
import net.giro.rh.admon.logica.EmpleadoParienteRem;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.rh.admon.logica.PuestoCategoriaRem;
import net.giro.rh.admon.logica.PuestoRem;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.richfaces.application.ServiceTracker;
import org.richfaces.focus.FocusManager;

@ViewScoped
@ManagedBean(name="empleadoAction")
public class EmpleadoAction implements Serializable {
	private static Logger log = Logger.getLogger(EmpleadoAction.class);
	private static final long serialVersionUID = 1L;
	private LoginManager loginManager;
	// ----------------------------------------------------------------
	private EmpleadoRem ifzEmpleado;
	private ClientesRem ifzClientes; 
	private ConValoresRem ifzConValores;
	private PuestoCategoriaRem ifzPuestosCategorias;
	private EmpleadoFiniquitoRem ifzFiniquitos;
	private EmpresasRem ifzEmpresas;
	private ConGrupoValoresRem ifzGpoVal;
	// ----------------------------------------------------------------
	private List<Empleado> listEmpleados;
	private Empleado pojoEmpleadoMain;
	private EmpleadoExt pojoEmpleado;
	private int numPagina;
	private boolean confirmaReingreso;
	private long idEmpleadoBajaPrevia;
	private String nombreEmpleado;
	private Date fechaIngreso;
	private String numeroSeguridadSocial;
	private String emergencia;
	private String email;
	private boolean externo;
	private boolean editable;
	// Busqueda principal
	private String valorBusqueda;
	private boolean mostrarEmpleadosSistema;
	private boolean empleadosSistema;
	// Busqueda Personas
	private List<Persona> listPersonas;
	private Persona pojoPersona;
	private List<SelectItem> tipoBusquedaPersonas;	
	private String valBusquedaPersonas;
	private String campoBusquedaPersonas;
	private int numPaginaPersonas;
	// Areas
	private AreaRem ifzArea;
	private List<Area> listAreas;
	private List<SelectItem> listAreasItems;
	private long idArea;
	private Area pojoArea;
	private String areaValidador;
	// Puestos
	private PuestoRem ifzPuestos;
	private List<Puesto> listPuestos;
	private List<SelectItem> listPuestosItems;
	private long idPuesto;
	private Puesto pojoPuesto;
	private String puestoValidador;
	// Categorias
	private CategoriaRem ifzCategorias;
	private List<Categoria> listCategorias;
	private List<SelectItem> listCategoriasItems;
	private long idCategoria;
	private Categoria pojoCategoria;
	private String categoriaValidador;
	// Sucursales
	private SucursalesRem ifzSucursal;
	private List<Sucursal> listSucursales;
	private List<SelectItem> listSucursalesItems;
	// Familiares
	private EmpleadoParienteRem ifzEmpleadoPariente;
	private ConGrupoValores grupoRelacion;
	private List<EmpleadoParienteExt> listFamiliares;
	private EmpleadoParienteExt pojoFamiliar;
	private Persona pojoFamiliarSeleccionado;
	private EmpleadoParienteExt pojoFamElimSeleccionado;
	private List<ConValores> listRelacionesFamiliares;
	private List<SelectItem> listRelacionesFamiliaresItems;
	private long idRelacion;
	private int numPaginaFamiliares;
	// Variables seleccion de FORMAS DE PAGO PARA EMPLEADOS
	private ConGrupoValores grupoFormasPagoEmpleados;
	private List<ConValores> listFormasPagoEmpleados;
	private List<SelectItem> listFormasPagoEmpleadosItems;
	// Beneficiarios
	private EmpleadoBeneficiarioRem ifzEmpleadoBeneficiario;
	private List<EmpleadoBeneficiarioExt> listBeneficiarios;
	private List<EmpleadoBeneficiarioExt> listBeneficiariosEliminados;
	private long idRelacionBeneficiario;
	private EmpleadoBeneficiarioExt pojoBeneficiario;
	private Persona pojoBeneficiarioSeleccionado;
	private int numPaginaBeneficiarios;
	private String mensajeErrorBeneficiario;
	private boolean beneficiarioValidado;
	// Beneficiarios Relaciones
	private List<ConValores> listRelacionesBeneficiarios;
	private List<SelectItem> listRelacionesBeneficiariosItems;
	private ConValores pojoRelacion;
	// Contrato
	private EmpleadoContratoRem ifzContrato;
	private List<EmpleadoContrato> listContratos;
	private EmpleadoContrato pojoContrato;
	private EmpleadoContrato pojoContratoBase;
	private EmpleadoContrato pojoContratoBorrar;
	private int idDiaDescanso;
	private List<SelectItem> listDiasItems;
	private String horaEntrada;
	private String horaSalida;
	private String horaEntradaComplemento;
	private String horaSalidaComplemento;
	private boolean esContratoNuevo;
	private boolean empleadosBajas;
	private boolean sueldoHoraBloqueado;
	private int contratosPaginacion;
	// Peridiocidad
	private ConGrupoValores grupoPeridiocidad;
	private List<ConValores> listPeridiocidad;
	private List<SelectItem> listPeridiocidadItems;
	private long idPeridiocidadPago;
	// Control
	private boolean operacionTerminada;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	// ----------------------------------------------
	private TimeZone timeZone;
	private String inputNameFocused;
	//DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
 	public EmpleadoAction() {
 		FacesContext fc = null;
 		Application app = null;
 		ValueExpression valExp = null;
 		InitialContext ctx = null;
 		
 		try {
 			fc = FacesContext.getCurrentInstance();
 			app = fc.getApplication();
 			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
 			this.loginManager =(LoginManager) valExp.getValue(fc.getELContext());
            
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
 			ctx = new InitialContext();
 			this.ifzEmpleado 			 = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
 			this.ifzClientes 			 = (ClientesRem) ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
 			this.ifzArea 				 = (AreaRem) ctx.lookup("ejb:/Logica_RecHum//AreaFac!net.giro.rh.admon.logica.AreaRem");
 			this.ifzCategorias 			 = (CategoriaRem) ctx.lookup("ejb:/Logica_RecHum//CategoriaFac!net.giro.rh.admon.logica.CategoriaRem");
 			this.ifzPuestos 			 = (PuestoRem) ctx.lookup("ejb:/Logica_RecHum//PuestoFac!net.giro.rh.admon.logica.PuestoRem");
 			this.ifzPuestosCategorias  	 = (PuestoCategoriaRem) ctx.lookup("ejb:/Logica_RecHum//PuestoCategoriaFac!net.giro.rh.admon.logica.PuestoCategoriaRem");
 			this.ifzContrato 			 = (EmpleadoContratoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
 			this.ifzEmpleadoPariente 	 = (EmpleadoParienteRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoParienteFac!net.giro.rh.admon.logica.EmpleadoParienteRem");
 			this.ifzEmpleadoBeneficiario = (EmpleadoBeneficiarioRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoBeneficiarioFac!net.giro.rh.admon.logica.EmpleadoBeneficiarioRem");
 			this.ifzFiniquitos 			 = (EmpleadoFiniquitoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFiniquitoFac!net.giro.rh.admon.logica.EmpleadoFiniquitoRem");
 			this.ifzSucursal 			 = (SucursalesRem) ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
 			this.ifzConValores 			 = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
 			this.ifzGpoVal 				 = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
 			this.ifzEmpresas 			 = (EmpresasRem) ctx.lookup("ejb:/Logica_Publico//EmpresasFac!net.giro.plataforma.logica.EmpresasRem");
 			
 			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzArea.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzCategorias.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzPuestos.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzPuestosCategorias.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzContrato.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzEmpleadoPariente.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzEmpleadoBeneficiario.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzFiniquitos.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
 			this.ifzEmpresas.setInfoSesion(this.loginManager.getInfoSesion());
 			
 			// CARGAMOS LAS RELACIONES (para Familiares y Beneficiarios)
 			// ---------------------------------------------------------------------------------------------------------
 			this.grupoRelacion = this.ifzGpoVal.findByName("SYS_RELACIONES");
 			if (this.grupoRelacion == null || this.grupoRelacion.getId() <= 0L)
 				log.warn("No se encontro encontro el grupo SYS_RELACIONES en con_grupo_valores");
 			this.listRelacionesFamiliares    = this.ifzConValores.buscaValorGrupo("descripcion", "", this.grupoRelacion);
 			this.listRelacionesBeneficiarios = this.ifzConValores.buscaValorGrupo("descripcion", "", this.grupoRelacion);
 			
 			// CARGAMOS LOS TIPOS DE PERIODOS
 			// ---------------------------------------------------------------------------------------------------------
 			this.grupoPeridiocidad = this.ifzGpoVal.findByName("SYS_TIPO_PERIODO");
 			if (this.grupoPeridiocidad == null || this.grupoPeridiocidad.getId() <= 0L)
 				log.warn("No se encontro encontro el grupo SYS_TIPO_PERIODO en con_grupo_valores");
 			this.listPeridiocidad = this.ifzConValores.buscaValorGrupo("descripcion", "", this.grupoPeridiocidad);
 			
 			// FORMAS DE PAGO PARA EMPLEADOS
 			// ---------------------------------------------------------------------------------------------------------
 			this.grupoFormasPagoEmpleados = this.ifzGpoVal.findByName("SYS_FORMA_PAGO_EMPLEADO");
 			if (this.grupoFormasPagoEmpleados == null || this.grupoFormasPagoEmpleados.getId() <= 0L)
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
 			
 			this.listAreas = new ArrayList<Area>();
 			this.listCategorias = new ArrayList<Categoria>();
 			this.listPuestos = new ArrayList<Puesto>();
 			this.listFamiliares = new ArrayList<EmpleadoParienteExt>();
 			this.listBeneficiarios = new ArrayList<EmpleadoBeneficiarioExt>();
 			this.listBeneficiariosEliminados = new ArrayList<EmpleadoBeneficiarioExt>();
 			this.pojoBeneficiario = new EmpleadoBeneficiarioExt();

 			// Busqueda Personas
 			this.tipoBusquedaPersonas = new ArrayList<SelectItem>();
 			this.tipoBusquedaPersonas.add(new SelectItem("nombre", "Nombre"));
 			this.tipoBusquedaPersonas.add(new SelectItem("id", "ID"));
 			this.campoBusquedaPersonas = this.tipoBusquedaPersonas.get(0).getValue().toString();
 			this.valBusquedaPersonas = "";
 			this.numPaginaPersonas = 1;
 			this.inputNameFocused = "txtvalorPersona";
 			
 			cargarAreas();
 			cargarPuestos();
 			cargarSucursales();
 			cargarDiasDescanso();
 			cargarListaCboRelaciones();
 			cargarListaCboRelacionesBeneficiarios();
 			cargarListaCboPeridiocidad();

			this.mostrarEmpleadosSistema = false;
 			if ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario()))
 				this.mostrarEmpleadosSistema = true;
 			
 			this.pojoContrato = new EmpleadoContrato();
 			this.esContratoNuevo = true;
 			this.contratosPaginacion = 1;
 			this.inputNameFocused = "txtvalor";
 			this.timeZone = TimeZone.getTimeZone("America/Mazatlan");
 		} catch (Exception e) {
 			log.error("Ocurrio un problema al instanciar el EJB", e);
 		}
	}
	
	public void buscar() {
		try {
			control();
			this.listEmpleados = this.ifzEmpleado.findLike(this.valorBusqueda, this.empleadosBajas, (this.mostrarEmpleadosSistema && this.empleadosSistema), null, 0);
			if (this.listEmpleados == null || this.listEmpleados.isEmpty()) {
				control("La busqueda no devolvio resultados.", null);
				return;
			}
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
			
			this.editable = true;
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
			
			this.editable = this.pojoEmpleado.getEstatus() == 0 || this.pojoEmpleado.getEstatus() == 3;
			this.pojoPersona = this.pojoEmpleado.getPersona();
			this.nombreEmpleado = this.pojoEmpleado.getPersona().getNombre(); 
			this.asignarDatosPersonaAEmpleado();

			this.fechaIngreso = this.pojoEmpleado.getFechaIngreso();
			this.numeroSeguridadSocial = pojoEmpleado.getNumeroSeguridadSocial();
			this.emergencia = this.pojoEmpleado.getNombreCasoEmergencia();
			this.externo = this.pojoEmpleado.getExterno()==1L ? true : false;
			this.email = this.pojoEmpleado.getEmail();
			this.pojoArea =  this.pojoEmpleado.getArea(); 
			this.pojoPuesto = this.pojoEmpleado.getPuestoCategoria().getIdPuesto(); 
			this.pojoCategoria = this.pojoEmpleado.getPuestoCategoria().getIdCategoria(); 
			
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
		int index = -1;
		
		try {
			control();
			controlLog("Validando empleado ... ");
			if (! validaGuardarEmpleado())
				return;

			controlLog("Guardando empleado ... ");
			if (this.pojoEmpleado.getAltaSeguroSocial() == 1 && this.pojoEmpleado.getFechaAltaSeguroSocial() == null)
				this.pojoEmpleado.setFechaAltaSeguroSocial(Calendar.getInstance().getTime());
			if (this.pojoEmpleado.getIdEmpresa() == null)
				this.pojoEmpleado.setIdEmpresa(this.loginManager.getUsuarioEmpresa());
			this.pojoEmpleado.setArea(this.pojoArea);
			this.pojoEmpleado.setFechaIngreso(this.fechaIngreso);
			this.pojoEmpleado.setEmail(this.email);
			this.pojoEmpleado.setNumeroSeguridadSocial(this.numeroSeguridadSocial);
			this.pojoEmpleado.setNombreCasoEmergencia(this.emergencia);
			this.pojoEmpleado.setExterno(this.isExterno() == true ? 1L : 0L);
			this.pojoEmpleado.setEstatus(0);
			asignarDatosPersonaAEmpleado();

			// Guardar en BD
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			if (this.pojoEmpleado.getId() == null || this.pojoEmpleado.getId() <= 0L) 
				this.pojoEmpleado.setId(this.ifzEmpleado.save(this.pojoEmpleado));
			else 
				this.ifzEmpleado.update(this.pojoEmpleado);

			controlLog("Empleado guardado. Actualizando lista ... ");
			this.pojoEmpleadoMain = this.ifzEmpleado.findById(this.pojoEmpleado.getId());
			if (this.listEmpleados == null)
				this.listEmpleados = new ArrayList<Empleado>();
			for (Empleado emp : this.listEmpleados) {
				if (emp.getId().longValue() == this.pojoEmpleadoMain.getId().longValue()) {
					index = this.listEmpleados.indexOf(emp);
					break;
				}
			}
			if (index == -1)
				this.listEmpleados.add(0, this.pojoEmpleadoMain);
			else
				this.listEmpleados.set(index, this.pojoEmpleadoMain);
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
			this.pojoEmpleadoMain.setEstatus(1);
			
			this.ifzEmpleado.update(this.pojoEmpleadoMain);
			this.operacionTerminada = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Eliminar el Empleado", e);
		}
	}

	public void reintegrarEmpleado() {
		try {
			control();
			if (this.idEmpleadoBajaPrevia <= 0L) {
				control(-1, "No se pudo recuperar el Empleado indicado");
				return;
			}
			
			this.pojoEmpleadoMain = this.ifzEmpleado.findById(this.idEmpleadoBajaPrevia);
			this.pojoEmpleado.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoEmpleado.setEstatus(0);
			this.idEmpleadoBajaPrevia = 0L;
			editar();
			this.editable = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar reeactivar al Empleado seleccionado", e);
		}
	}
	
	private boolean validaGuardarEmpleado() {
		List<PuestoCategoria> pce = null;
		
		try {
			// Validaciones
			if (this.pojoEmpleado.getPersona() == null || this.pojoEmpleado.getPersona().getId() <= 0L) {
				control("Debe seleccionar una persona", null);
				return false;
			}

			// Comprobamos si ya ha sido dado de alta
			if (this.pojoEmpleado.getId() == null || this.pojoEmpleado.getId() <= 0L) {
				if (! this.ifzEmpleado.validarEmpleado(this.pojoEmpleado.getPersona().getId())) {
					control(-1, "Este empleado ya ha sido registrado");
					return false;
				}
			}
			
			if (this.pojoEmpleado.getSucursal() == null) {
				control("Seleccione sucursal", null);
				return false;
			}
			
			if (this.numeroSeguridadSocial.trim().equals("")) {
				control("Introduzca número de Seguridad Social", null);
				return false;
			}
			
			if (this.fechaIngreso.toString().equals("")) {
				control("Debe seleccionar fecha de ingreso", null);
				return false;
			}

			if (this.emergencia.trim().equals("")) {
				control("Introduzca a quien llamar en caso de emergencia", null);
				return false;
			}
			
			if (this.pojoArea == null || this.pojoArea.getId() == null || this.pojoArea.getId() <= 0L) {
				control("Debe seleccionar el área del empleado", null);
				return false;
			}

			if (this.idPuesto == 0) {
				control("Debe seleccionar Puesto", null);
				return false;
			}

			if (this.idCategoria == 0) {
				control("Debe seleccionar Categoria", null);
				return false;
			}

			this.ifzPuestosCategorias.setInfoSesion(this.loginManager.getInfoSesion());
			pce = this.ifzPuestosCategorias.findByPuestoCategoria(this.idPuesto, this.idCategoria);
			if (pce == null || pce.isEmpty()) {
				control("No se pudo encontrar relacion con el Puesto y Categoria asignados", null);
				return false;
			}
			this.pojoEmpleado.setPuestoCategoria(pce.get(0));
			if (this.pojoEmpleado.getId() == null || this.pojoEmpleado.getId() <= 0L) {
				this.pojoEmpleado.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoEmpleado.setFechaCreacion(Calendar.getInstance().getTime());
			} 
			this.pojoEmpleado.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoEmpleado.setFechaModificacion(Calendar.getInstance().getTime());
		} catch (Exception e) {
			control("Ocurrio un problema al validar la informacion proporcionada", e);
			return false;
		}
		
		return true;
	}
	
	private void asignarDatosPersonaAEmpleado() {
		this.pojoEmpleado.setHomonimo(this.pojoPersona.getHomonimo());
		this.pojoEmpleado.setNombre(this.pojoPersona.getNombre());
		this.pojoEmpleado.setPrimerNombre(this.pojoPersona.getPrimerNombre());
		this.pojoEmpleado.setSegundoNombre(this.pojoPersona.getSegundoNombre());
		this.pojoEmpleado.setNombresPropios(this.pojoPersona.getNombresPropios());
		this.pojoEmpleado.setPrimerApellido(this.pojoPersona.getPrimerApellido());
		this.pojoEmpleado.setSegundoApellido(this.pojoPersona.getSegundoApellido());
	}

	public void cargarDiasDescanso() {
		//Se iniciara con 1 en domingo
		this.listDiasItems = null;
		this.listDiasItems = new ArrayList<SelectItem>();
		this.listDiasItems.add(new SelectItem("1", "Domingo"));
		this.listDiasItems.add(new SelectItem("2", "Lunes"));
		this.listDiasItems.add(new SelectItem("3", "Martes"));
		this.listDiasItems.add(new SelectItem("4", "Miercoles"));
		this.listDiasItems.add(new SelectItem("5", "Jueves"));
		this.listDiasItems.add(new SelectItem("6", "Viernes"));
		this.listDiasItems.add(new SelectItem("7", "Sabado"));
	}
	
	public void cargarSucursales() {
		String descripcion = "";
		
		try {
			this.listSucursalesItems = new ArrayList<SelectItem>();
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSucursales = this.ifzSucursal.findAll(); 
			if (this.listSucursales != null && ! this.listSucursales.isEmpty()) {
				for (Sucursal sucursal : this.listSucursales) {
					descripcion = sucursal.getSucursal();
					if (this.isDebug)
						descripcion += " (" + sucursal.getId() + ")";
					this.listSucursalesItems.add(new SelectItem(sucursal.getId(), descripcion));
				}
			}
		} catch (Exception e) {
			log.error("Error en RecHum.EmpleadoAction.cargarSucursales", e);
		}
	}
	
	public void cargarFormasPagoEmpleados() {
		String descripcion = "";
		
		try {
			if (this.grupoFormasPagoEmpleados == null)
				return;
			
			this.listFormasPagoEmpleadosItems = new ArrayList<SelectItem>();
			this.listFormasPagoEmpleados = this.ifzConValores.findAll(this.grupoFormasPagoEmpleados);
			if (this.listFormasPagoEmpleados != null && ! this.listFormasPagoEmpleados.isEmpty()) {
				for (ConValores var : this.listFormasPagoEmpleados) {
					descripcion = var.getValor();
					if (this.isDebug)
						descripcion += " (" + var.getId() + ")";
					this.listFormasPagoEmpleadosItems.add(new SelectItem(var.getId(), descripcion));
				}
			}
		} catch (Exception e) {
			log.error("Error en cargarFormasPagoEmpleados", e);
		}
	}
	
	public void cargarAreas() {
		String descripcion = "";
		
		this.listAreasItems = new ArrayList<SelectItem>();
		this.listAreas = this.ifzArea.findAllActivos();
		if (this.listAreas != null && ! this.listAreas.isEmpty()) {
			for (Area area : this.listAreas) {
				descripcion = area.getDescripcion();
				if (this.isDebug)
					descripcion += " (" + area.getId() + ")";
				this.listAreasItems.add(new SelectItem(area.getId().toString(), descripcion));
			}
		}
	}
	
	public void cargarPuestos() {
		String descripcion = "";
		
		try {
			this.listPuestosItems = new ArrayList<SelectItem>();
			this.listPuestos = this.ifzPuestos.findAll();
			if (this.listPuestos != null && ! this.listPuestos.isEmpty()) {
				for (Puesto puesto : this.listPuestos) {
					descripcion = puesto.getDescripcion();
					if (this.isDebug)
						descripcion += " (" + puesto.getId() + ")";
					this.listPuestosItems.add(new SelectItem(puesto.getId(), descripcion));
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Puestos", e);
		}
	}

	public void cargaCategorias() {
		if (this.idPuesto > 0) 
			filtrarCategorias(this.idPuesto);
	}

	private void filtrarCategorias(long idPuesto) {
		List<PuestoCategoria> listaPuestosCategorias = null;
		String descripcion = "";
		
		if (idPuesto == 0) 
			return;
		
		this.listCategorias = new ArrayList<Categoria>();
		this.listCategoriasItems = new ArrayList<SelectItem>();
		listaPuestosCategorias = this.ifzPuestosCategorias.findByIdPuesto(idPuesto);
		if (listaPuestosCategorias != null && ! listaPuestosCategorias.isEmpty()) {
			for (PuestoCategoria pc : listaPuestosCategorias) {
				this.listCategorias.add(pc.getIdCategoria());
				descripcion = pc.getIdCategoria().getDescripcion();
				if (this.isDebug)
					descripcion += " (" + pc.getIdCategoria().getId() + ")";
				this.listCategoriasItems.add(new SelectItem(pc.getIdCategoria().getId(), descripcion));
			}
		}
	}
	
	public void cargarListaCboRelaciones() {
		this.listRelacionesFamiliaresItems = new ArrayList<SelectItem>();
		for (ConValores relacion : this.listRelacionesFamiliares)
			this.listRelacionesFamiliaresItems.add(new SelectItem(relacion.getId(), relacion.getValor()));
	}
		
	public void cargarListaCboRelacionesBeneficiarios() {
		this.listRelacionesBeneficiariosItems = new ArrayList<SelectItem>();
		for (ConValores relacion : this.listRelacionesBeneficiarios) 
			this.listRelacionesBeneficiariosItems.add(new SelectItem(relacion.getId(), relacion.getValor()));
	}

	public void cargarListaCboPeridiocidad() {
		this.listPeridiocidadItems = new ArrayList<SelectItem>();
		for (ConValores peridiocidad : this.listPeridiocidad) 
			this.listPeridiocidadItems.add(new SelectItem(peridiocidad.getId(), peridiocidad.getDescripcion() + "(" + peridiocidad.getValor() + ")" + (this.isDebug ? " [" + peridiocidad.getId() + "]" : "")));
	}
	
	public void preRenderView() {
        FocusManager focusManager = ServiceTracker.getService(FocusManager.class);
        focusManager.focus(this.inputNameFocused);
    }
	
	private void control() {
		this.operacionTerminada = true;
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		this.operacionTerminada = false;
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n\n" + this.getClass().getCanonicalName() + " :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "\n" + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	private void controlLog(String mensaje) {
		log.info("\n\n" + this.getClass().getCanonicalName() + " :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje);
	}

	// -----------------------------------------------------------------
	// BUSQUEDA PERSONAS
	// -----------------------------------------------------------------

	public void nuevaBusquedaPersona() {
		control();
		this.campoBusquedaPersonas = this.tipoBusquedaPersonas.get(0).getValue().toString();
		this.valBusquedaPersonas = "";
		this.numPaginaPersonas = 1;

		this.listPersonas = new ArrayList<Persona>();
		this.inputNameFocused = "txtvalorPersona";
		this.confirmaReingreso = false;
	}
	
	public void buscarPersonas() {
		List<Persona> listaTotalPersonas = null;
		int tipoPersonaFisica = 1;
		
		try {
			control();
			if (this.campoBusquedaPersonas == null || "".equals(this.campoBusquedaPersonas.trim()))
				this.campoBusquedaPersonas = this.tipoBusquedaPersonas.get(0).getValue().toString();
			
			this.listPersonas = new ArrayList<Persona>();
			listaTotalPersonas = this.ifzClientes.buscarPersona(this.campoBusquedaPersonas, this.valBusquedaPersonas);
			if (listaTotalPersonas != null && ! listaTotalPersonas.isEmpty()) {
				controlLog(listaTotalPersonas.size() + " Personas encontrados");
				for (Persona p : listaTotalPersonas) {
					if (tipoPersonaFisica != p.getTipoPersona().intValue()) 
						continue; // Solo personas fisicas
					this.listPersonas.add(p);
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Personas", e);
		} finally {
			controlLog(this.listPersonas.size() + " personas devueltos");
			this.inputNameFocused = "txtvalorPersona";
		}
	}

	public void seleccionarPersona() {
		try {
			control();
			if (this.pojoPersona == null || this.pojoPersona.getId() <= 0L) {
				control(-1, "Debe seleccionar una Persona");
				return;
			}

			controlLog("Selecciono " + this.pojoPersona.getId() + " - " + this.pojoPersona.getNombre());
			this.nombreEmpleado = this.pojoPersona.getNombre();
			this.pojoEmpleado.setPersona(this.pojoPersona);
			this.inputNameFocused = "txtClave";
			
			// Comprobando baja previa
			if (comprobarBajaPrevia(this.pojoPersona.getId())) {
				control(-1, "Se detecto que la Persona seleccionada tiene una baja previa.\n\n¿Desea reingresarlo?");
				return;
			}
			
			nuevaBusquedaPersona();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar y/o comprobar la Persona seleccionada", e);
		} 
	}

	private boolean comprobarBajaPrevia(long idPersona) {
		Empleado empleado = null;
		
		try {
			this.confirmaReingreso = true;
			this.idEmpleadoBajaPrevia = 0;
			empleado = this.ifzEmpleado.comprobarBajaPrevia(idPersona);
			if (empleado == null) 
				return false;
			this.idEmpleadoBajaPrevia = empleado.getId();
			this.confirmaReingreso = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar comprobar si previamente hay una baja con la Persona seleccionada", e);
			return false;
		}
		
		return true;
	}
	
	// -----------------------------------------------------------------
	// CONTRATOS
	// -----------------------------------------------------------------

	public void cargarContratos() {
		try {
			control();
			this.pojoContratoBase = null;
			this.pojoContratoBorrar = null;
			this.listContratos = this.ifzContrato.findAll(this.pojoEmpleado.getId(), "id desc", true, false);
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Contratos del Empleado seleccionado", e);
		}
	}

	public void nuevoContrato() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		EmpleadoContrato last = null;
		String respuesta = "";

		try {
			control();
			this.esContratoNuevo = true;
			this.pojoContrato = new EmpleadoContrato();
			this.pojoContrato.setTipoHorario(-1);
			if (this.listContratos == null || this.listContratos.isEmpty())
				return;
	
			// Recupero el ultimo contrato activo
			last = this.listContratos.get(0);
			
			// Validamos contrato
			this.ifzContrato.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzContrato.validarCancelacion(last.getId());
			if (respuesta != null && ! "".equals(respuesta.trim())) {
				respuesta = respuesta.replace("El Contrato", "El Contrato actual");
				control(-1, respuesta.trim());
				return;
			}
			
			// Generamos nuevo Contrato con base al previo validado
			this.pojoContratoBase = copiarContrato(last);
			this.pojoContrato = copiarContrato(last);
			this.pojoContrato.setId(0L);
			this.pojoContrato.setDeterminado(0);
			this.pojoContrato.setFechaInicio(Calendar.getInstance().getTime());
			this.pojoContrato.setFechaFin(Calendar.getInstance().getTime());
			this.pojoContrato.setEstatus(0);

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
		} catch (Exception e) {
			control("Ocurrio un problema al generar un nuevo Contrato", e);
		}
	}
	
	public void editarContrato() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		
		try {
			control();
			if (this.pojoContrato == null || this.pojoContrato.getId() == null || this.pojoContrato.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar el Contrato");
				return;
			}
			
			this.esContratoNuevo = false;
			if (this.loginManager.getUsuarioResponsabilidad().getUsuario().getId() == 1) {
				if (this.pojoEmpleado.getNombre().contains("TOTAL") && (this.pojoEmpleado.getNombre().contains("NOMINA") || this.pojoEmpleado.getNombre().contains("ADICIONALES")))
					this.esContratoNuevo = true;
			}
			
			this.idPeridiocidadPago = this.pojoContrato.getPeriodicidadPago();
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
			controlLog("Guardando contrato" + (this.pojoContrato.getId() == null || this.pojoContrato.getId() <= 0L ? " nuevo" : ""));
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
				this.pojoContrato.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoContrato.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoContrato.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				
				// Guardamos en la BD y asignamos ID.
				this.pojoContrato.setId(this.ifzContrato.save(this.pojoContrato));
				controlLog("Contrato guardado");
			} else {
				// Actualizar en la BD.
				this.pojoContrato.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoContrato.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.ifzContrato.update(this.pojoContrato);
				controlLog("Contrato actualizado");
			}
			
			// Recupero el ultimo contrato y lo damos de baja, si corresponde
			if (this.pojoContratoBase != null && this.pojoContratoBase.getId() != null && this.pojoContratoBase.getId() > 0L) {
				controlLog("Cancelando contrato previo: " + this.pojoContratoBase.getId());
				this.pojoContratoBorrar = copiarContrato(this.pojoContratoBase);
				this.pojoContratoBase = null;
				cancelarContrato();
				return;
			}
			
			cargarContratos();
		} catch (Exception e) {
			control("Ocurrio un problema al guardar Contrato", e);
		}
	}
	
	public void validarCancelacion() {
		String respuesta = null; 
		
		try {
			control();
			if (this.pojoContratoBorrar == null || this.pojoContratoBorrar.getId() == null || this.pojoContratoBorrar.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar validar el Contrato indicado");
				return;
			}

			this.ifzContrato.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzContrato.validarCancelacion(this.pojoContratoBorrar.getId());
			if (respuesta != null && ! "".equals(respuesta.trim())) 
				control(-1, respuesta.trim());
		} catch (Exception e) {
			control("Ocurrio un problema al intentar validar el Contrato", e);
		}
	}
	
	public void cancelarContrato() {
		Respuesta respuesta = null;
		
		try {
			control();
			if (this.pojoContratoBorrar == null || this.pojoContratoBorrar.getId() == null || this.pojoContratoBorrar.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar validar el Contrato indicado");
				return;
			}
			
			this.ifzContrato.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzContrato.cancelar(this.pojoContratoBorrar.getId());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, respuesta.getErrores().getDescError());
				return;
			}
			
			// Recargamos los contratos
			cargarContratos();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Contrato", e);
			return;
		}
	}

	private boolean validarContrato() {
		if (this.pojoEmpleado == null || this.pojoEmpleado.getId() == null || this.pojoEmpleado.getId() <= 0L) {
			control("Error al recuperar los datos del empleado.", null);
			return false;
		}
		
		if (this.pojoContrato.getFechaInicio() == null) {
			control("Debe indicar la fecha de inicio del contrato", null);
			return false;
		}
		
		if (this.pojoContrato.getPeriodicidadPago() <= 0) {
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
		
		if ("".equals(this.pojoContrato.getCentroTrabajo().trim())) {
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
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(resultado, target);
		} catch (Exception e) {
			resultado.setId(target.getId());
			resultado.setIdEmpleado(target.getIdEmpleado());
			resultado.setFechaInicio(target.getFechaInicio());
			resultado.setFechaFin(target.getFechaFin());
			resultado.setSueldo(target.getSueldo());
			resultado.setPeriodicidadPago(target.getPeriodicidadPago());
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
		int dias = 0;
		
		try {
			control();
			this.sueldoHoraBloqueado = false;
			if (this.pojoContrato == null) {
				control(-1, "Ocurrio un problema al intentar Calcular el Sueldo por Hora. Contrato no cargado");
				return;
			}

			this.pojoContrato.setSueldoHora(BigDecimal.ZERO);
			if (this.pojoContrato.getSueldo() <= 0 || this.listPeridiocidad == null || this.listPeridiocidad.isEmpty()) 
				return;
			for (ConValores peridiocidad : this.listPeridiocidad) {
				if (this.pojoContrato.getPeriodicidadPago() == peridiocidad.getId() && NumberUtils.isNumber(peridiocidad.getValor().trim())) {
					dias = Integer.parseInt(peridiocidad.getValor().trim());
					break;
				}
			}

			// Calculamos y Asignando el Sueldo por Hora
			if (dias > 0) {
				controlLog("Calculando Sueldo Hora de " + this.pojoContrato.getSueldo() + " ... ");
				sueldoHora = ((this.pojoContrato.getSueldo() / dias) / 8);
				controlLog("Asignando sueldo hora " + sueldoHora + " ... ");
				this.pojoContrato.setSueldoHora(new BigDecimal((new DecimalFormat("#0.0000")).format(sueldoHora)));
				this.sueldoHoraBloqueado = true;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar calcular el Sueldo por Hora", e);
			if (this.pojoContrato != null)
				this.pojoContrato.setSueldoHora(BigDecimal.ZERO);
		}
	}
	
	public void reiniciaHorario() {
		if (this.pojoContrato == null)
			return;
		
		if (this.pojoContrato.getTipoHorario() == 1) {
			this.horaEntrada = "08:00";
			this.horaSalida  = "16:00";
			this.horaEntradaComplemento = "00:00";
			this.horaSalidaComplemento  = "00:00";
		} else {
			this.horaEntrada = "08:00";
			this.horaSalida  = "14:00";
			this.horaEntradaComplemento = "16:00";
			this.horaSalidaComplemento  = "18:00";
		}
	}
	
	// -----------------------------------------------------------------
	// BENEFICIARIOS 
	// -----------------------------------------------------------------

	public void cargarBeneficiarios() {
		try {
			this.listBeneficiarios = new ArrayList<EmpleadoBeneficiarioExt>();
			this.listBeneficiariosEliminados = new ArrayList<EmpleadoBeneficiarioExt>();
			this.numPaginaBeneficiarios = 1;
			this.listBeneficiarios = this.ifzEmpleadoBeneficiario.findByIdEmpleado(this.pojoEmpleado.getId());
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
			
			for (EmpleadoBeneficiarioExt e : this.listBeneficiarios) {
				if (this.pojoEmpleado.getPersona().getId() == e.getPersona().getId()) {
					control("El empleado no puede ser beneficiario", null);
					return;
				}
			}
			
			for (EmpleadoBeneficiarioExt e : this.listBeneficiarios) {
				if (this.pojoBeneficiario.getPersona().getId() == e.getPersona().getId()) {
					control("El beneficiario ya se encuentra en la lista", null);
					return;
				}
			}

			this.pojoBeneficiario.setEmpleado(this.pojoEmpleado);
			this.pojoBeneficiario.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoBeneficiario.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.pojoBeneficiario.getId() == null) {	//nuevo beneficiario
				this.pojoBeneficiario.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoBeneficiario.setFechaCreacion(Calendar.getInstance().getTime());
				
				this.listBeneficiarios.add(this.pojoBeneficiario);
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
			this.listBeneficiariosEliminados.add(this.pojoBeneficiario);
			for (EmpleadoBeneficiarioExt ebe : this.listBeneficiarios) {
				if (ebe.getId() == this.pojoBeneficiario.getId()) {
					this.listBeneficiarios.remove(ebe);
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
		for (EmpleadoBeneficiarioExt ebe:this.listBeneficiarios)
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
			this.pojoFamElimSeleccionado = new EmpleadoParienteExt();
			this.listFamiliares = new ArrayList<EmpleadoParienteExt>();
			this.numPaginaFamiliares = 1;
			this.listFamiliares = this.ifzEmpleadoPariente.findByIdEmpleadoParentesco(this.pojoEmpleado.getId());
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
			this.pojoFamElimSeleccionado.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoFamElimSeleccionado.setFechaModificacion(Calendar.getInstance().getTime());

			if (this.pojoFamElimSeleccionado.getId() == null) {	
				this.pojoFamElimSeleccionado.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoFamElimSeleccionado.setFechaCreacion(Calendar.getInstance().getTime());
				
				this.pojoFamElimSeleccionado.setId(this.ifzEmpleadoPariente.save(this.pojoFamElimSeleccionado));
				this.listFamiliares.add(this.pojoFamElimSeleccionado);	
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
			for (EmpleadoParienteExt fam : this.listFamiliares) {
				if (fam.getPersona().getId() == this.pojoFamElimSeleccionado.getPersona().getId()) {
					this.listFamiliares.remove(fam);
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
		
		for (EmpleadoParienteExt e : this.listFamiliares) {
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

	public String getEmpleado() {
		if (this.pojoEmpleado != null && this.pojoEmpleado.getNombre() != null && ! "".equals(this.pojoEmpleado.getNombre().trim()))
			return this.pojoEmpleado.getId() + " - " + this.pojoEmpleado.getNombre();
		return "";
	}
	
	public void setEmpleado(String value) {}
	
	public String getPersona() {
		if (this.pojoEmpleado != null && this.pojoEmpleado.getPersona() != null && this.pojoEmpleado.getPersona().getId() > 0L)
			return this.pojoEmpleado.getPersona().getId() + " - " + this.pojoEmpleado.getPersona().getNombre();
		return "";
	}
	
	public void setPersona(String value) {}
	
	public long getIdArea() {
		return idArea;
	}

	public void setIdArea(long idArea) {
		this.idArea = idArea;

		this.areaValidador = "";
		for (Area a : this.listAreas) {
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
		for (Puesto p: this.listPuestos) {
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
		for (Categoria c : this.listCategorias) {
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
		
		for (ConValores c : this.listRelacionesFamiliares) {
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
		
		for (ConValores c : this.listRelacionesBeneficiarios) {
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
		
		for (ConValores p : this.listPeridiocidad) {
			if (p.getId() == idPeridiocidadPago) {
				this.pojoContrato.setPeriodicidadPago(p.getId());
				break;
			}
		}
	}

	public long getIdSucursal() {
		return this.pojoEmpleado.getSucursal() == null ? 0 : this.pojoEmpleado.getSucursal().getId();
	}
	
	public void setIdSucursal(long idSucursal) {
		if (idSucursal > 0) {
			for (Sucursal s : this.listSucursales) {
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

	public boolean isOperacionCancelada() {
		return operacionCancelada;
	}

	public void setOperacionCancelada(boolean operacionCancelada) {
		this.operacionTerminada = operacionCancelada;
	}

	public boolean isBand() {
		return operacionTerminada;
	}

	public void setBand(boolean band) {
		this.operacionTerminada = band;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
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
		return listEmpleados;
	}

	public void setListaEmpleadosGrid(List<Empleado> listaEmpleadosGrid) {
		this.listEmpleados = listaEmpleadosGrid;
	}

	public List<Persona> getListaPersonasGrid() {
		return listPersonas;
	}

	public void setListaPersonasGrid(List<Persona> listaPersonasGrid) {
		this.listPersonas = listaPersonasGrid;
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
		/*return (this.pojoEmpleado.getAltaSeguroSocial() == 1);*/
		return true;
	}

	public void setAltaSeguroSocial(boolean altaSeguroSocial) {
		this.pojoEmpleado.setAltaSeguroSocial((altaSeguroSocial ? 1 : 0));
	}
	
	public List<SelectItem> getListaCboAreas() {
		return listAreasItems;
	}

	public void setListaCboAreas(List<SelectItem> listaCboAreas) {
		this.listAreasItems = listaCboAreas;
	}

	public List<Area> getListaAreas() {
		return listAreas;
	}

	public void setListaAreas(List<Area> listaAreas) {
		this.listAreas = listaAreas;
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
		return listPuestosItems;
	}

	public void setListaCboPuestos(List<SelectItem> listaCboPuestos) {
		this.listPuestosItems = listaCboPuestos;
	}

	public List<Puesto> getListaPuestos() {
		return listPuestos;
	}

	public void setListaPuestos(List<Puesto> listaPuestos) {
		this.listPuestos = listaPuestos;
	}

	public Puesto getPojoPuesto() {
		return pojoPuesto;
	}

	public void setPojoPuesto(Puesto pojoPuesto) {
		this.pojoPuesto = pojoPuesto;
	}

	public List<SelectItem> getListaCboCategorias() {
		return listCategoriasItems;
	}

	public void setListaCboCategorias(List<SelectItem> listaCboCategorias) {
		this.listCategoriasItems = listaCboCategorias;
	}

	public List<Categoria> getListaCategorias() {
		return listCategorias;
	}

	public void setListaCategorias(List<Categoria> listaCategorias) {
		this.listCategorias = listaCategorias;
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
		return listFamiliares;
	}

	public void setListaFamiliaresGrid(List<EmpleadoParienteExt> listaFamiliaresGrid) {
		this.listFamiliares = listaFamiliaresGrid;
	}

	public Persona getPojoFamiliarSeleccionado() {
		return pojoFamiliarSeleccionado;
	}

	public void setPojoFamiliarSeleccionado(Persona pojoFamiliarSeleccionado) {
		this.pojoFamElimSeleccionado.setPersona(pojoFamiliarSeleccionado);
		this.pojoFamiliarSeleccionado = pojoFamiliarSeleccionado;
	}

	public List<ConValores> getListRelacionesFamiliares() {
		return listRelacionesFamiliares;
	}

	public void setListRelacionesFamiliares(List<ConValores> listRelacionesFamiliares) {
		this.listRelacionesFamiliares = listRelacionesFamiliares;
	}

	public List<SelectItem> getListaCboRelaciones() {
		return listRelacionesFamiliaresItems;
	}

	public void setListaCboRelaciones(List<SelectItem> listaCboRelaciones) {
		this.listRelacionesFamiliaresItems = listaCboRelaciones;
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
		return listBeneficiarios;
	}

	public void setListaBeneficiariosGrid(List<EmpleadoBeneficiarioExt> listaBeneficiariosGrid) {
		this.listBeneficiarios = listaBeneficiariosGrid;
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
		return listRelacionesBeneficiariosItems;
	}

	public void setListaCboRelacionesBeneficiarios(List<SelectItem> listaCboRelacionesBeneficiarios) {
		this.listRelacionesBeneficiariosItems = listaCboRelacionesBeneficiarios;
	}

	public List<SelectItem> getListaCboPeridiocidad() {
		return listPeridiocidadItems;
	}

	public void setListaCboPeridiocidad(List<SelectItem> listaCboPeridiocidad) {
		this.listPeridiocidadItems = listaCboPeridiocidad;
	}

	public List<SelectItem> getListaCboDias() {
		return listDiasItems;
	}

	public void setListaCboDias(List<SelectItem> listaCboDias) {
		this.listDiasItems = listaCboDias;
	}

	public List<EmpleadoContrato> getListaContratosGrid() {
		return listContratos;
	}

	public List<SelectItem> getListaCboSucursales() {
		return listSucursalesItems;
	}
	
	public void setListaCboSucursales(List<SelectItem> listaCboSucursales) {
		this.listSucursalesItems = listaCboSucursales;
	}
	
	public void setListaContratosGrid(List<EmpleadoContrato> listaContratosGrid) {
		this.listContratos = listaContratosGrid;
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

	public boolean isMostrarEmpleadosSistema() {
		return mostrarEmpleadosSistema;
	}

	public void setMostrarEmpleadosSistema(boolean mostrarEmpleadosSistema) {
		this.mostrarEmpleadosSistema = mostrarEmpleadosSistema;
	}

	public boolean isEmpleadosSistema() {
		return empleadosSistema;
	}

	public void setEmpleadosSistema(boolean empleadosSistema) {
		this.empleadosSistema = empleadosSistema;
	}

	public boolean isConfirmaReingreso() {
		return confirmaReingreso;
	}

	public void setConfirmaReingreso(boolean confirmaReingreso) {
		this.confirmaReingreso = confirmaReingreso;
	}

	public boolean isEmpleadosBajas() {
		return empleadosBajas;
	}

	public void setEmpleadosBajas(boolean empleadosBajas) {
		this.empleadosBajas = empleadosBajas;
	}
	
	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
	
	
	public boolean getSelected() {
		return true;
	}

	public void setDebugging() {}

	public boolean isSueldoHoraBloqueado() {
		return sueldoHoraBloqueado;
	}

	public void setSueldoHoraBloqueado(boolean sueldoHoraBloqueado) {
		this.sueldoHoraBloqueado = sueldoHoraBloqueado;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
