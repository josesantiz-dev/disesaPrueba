package cde.publico;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;  

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;

import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.EmpresasUsuarios;
import net.giro.plataforma.MD5;
import net.giro.plataforma.beans.UsuarioExt;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.plataforma.logica.EmpresasRem;
import net.giro.plataforma.logica.EmpresasUsuariosRem;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.beans.Funcion;
import net.giro.plataforma.seguridad.beans.PerfilValor;
import net.giro.plataforma.seguridad.beans.PermisosUsuario;
import net.giro.plataforma.seguridad.beans.Responsabilidad;
import net.giro.plataforma.seguridad.beans.Usuario;
import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;
import net.giro.plataforma.seguridad.logica.PermisosRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;

@KeepAlive
public class UsuarioAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(UsuarioAction.class);
	private LoginManager loginManager;
	//private PermisosUsuario permisos;
	private AdministracionRem ifzAdministracion;
	private UsuarioExt pojoUsuarioExt;
	private UsuarioResponsabilidad pojoUsuarioResponsabilidad;
	private Responsabilidad pojoResponsabilidad;
	private List<UsuarioExt> listUsuarios;
	private List<UsuarioResponsabilidad> listTmpsuarioResponsabilidad;
	private List<Responsabilidad> listTmpResponsabilidad;
	private List<UsuarioResponsabilidad> listTmpUsuarioResponsabilidad;
	private List<SelectItem> listNivelAcceso;
	private List<SelectItem> listResponsabilidad;
	private List<SelectItem> listTmpUsuarios;
	private List<SelectItem> listUsuarioResponsabilidad;
	private int numPagina;
	private int numPaginaBuscarEmpleado;
	private int numPaginaNivelResponsabilidad;
	private String passWordVacio;
	private String idEmpleado;
	private String problemInesp;
	private String busquedaVacia;
	private String campoBusqueda;
	private String valTipoBusqueda;
	private String valorBusqueda;
	private String tipoBusqueda;
	private String contrasena;
	private String contrasenaOriginal;
	private String contrasena2;	
	private String malInicioResp;
	private String malFinResp;
	private String vigenciaIncorrecta;
	private String contrasenaDiferente;
	private String vigenciaFechasResp;
	private List<Usuario>listUsuariot;
	private boolean registroSeleccionado;
	// control
	private boolean operacion;
	private int tipoMensaje;
	private String resOperacion;
	private String mensajeDetalles;
	// Empleados
	private EmpleadoRem ifzEmpleados;
	private List<Empleado> listEmpleados;
	//private Empleado pojoEmpleado;
	private Empleado pojoEmpleadoSeleccionado;
	private List<SelectItem> empBusquedaTipos;	
	private String empBusquedaCampo;
	private String empBusquedaValor;
	private int empBusquedaPagina;
	private boolean usuarioEmpleado;
	private boolean usuarioEmpleadoLock;
	private String usuarioPrevio;
	// EMPRESAS
	private EmpresasRem ifzEmpresas;
	private List<Empresa> listEmpresas;
	private List<SelectItem> listEmpresasItems;
	private long idEmpresa;
	// USUARIOS EMPRESAS
	private EmpresasUsuariosRem ifzUsuEmpresas;
	private List<EmpresasUsuarios> listUsuarioEmpresas;
	private EmpresasUsuarios pojoUsuarioEmpresa;
	private int numUsuarioEmpresas;
	// PERMISOS
	private PermisosRem ifzPermisos;
	private List<PermisosUsuario> listPermisos;
	private List<PermisosUsuario> listPermisosEliminados;
	private int indexPermisoBorrar;
	private List<SelectItem> listPermisosItems;
	private int valorPermiso;
	private int paginacionPermisos;
	private List<Aplicacion> listAplicaciones;
	private List<SelectItem> listAplicacionesItems;
	private long idAplicacion;
	private List<Funcion> listFunciones;
	private List<SelectItem> listFuncionesItems;
	private long idFuncion;
	// PERFILES
	private LinkedHashMap<PerfilValor, String> listPerfilValor;
	private List<SelectItem> listPerfilValorItems;
	private HashMap<String,String> sessiones;
	private List<SelectItem> sessionesItems;
	private List<String> usuarios;
	// QUARTZ
	private HashMap<String, String> paramsQuartz;
	private Connection conn;
	// Debug
	private HashMap<String, String> paramsRequest;
	private boolean isDebug;

	public UsuarioAction() {
		FacesContext fc = null;
		Application app = null;
		PropertyResourceBundle propPlataforma = null;
		ValueExpression valExp = null;
		Context ctx = null;

		try {
			// Contexto de app
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			//this.permisos = this.loginManager.getPermiso(null, 0L);
	
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = this.paramsRequest.containsKey("DEBUG") ? true : false;
	
			// EJB's
			ctx = this.loginManager.getCtx();
			this.ifzAdministracion = (AdministracionRem) ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzEmpresas = (EmpresasRem) ctx.lookup("ejb:/Logica_Publico//EmpresasFac!net.giro.plataforma.logica.EmpresasRem");
			this.ifzUsuEmpresas = (EmpresasUsuariosRem) ctx.lookup("ejb:/Logica_Publico//EmpresasUsuariosFac!net.giro.plataforma.logica.EmpresasUsuariosRem");
			this.ifzPermisos = (PermisosRem) ctx.lookup("ejb:/Logica_Publico//PermisosFac!net.giro.plataforma.seguridad.logica.PermisosRem");
	
			this.ifzAdministracion.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpresas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzUsuEmpresas.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.paramsQuartz = new HashMap<String, String>();
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			propPlataforma = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			if (propPlataforma.containsKey("pgDriver"))
				this.paramsQuartz.put("pgDriver", propPlataforma.getString("pgDriver"));
			if (propPlataforma.containsKey("pgUrl"))
				this.paramsQuartz.put("pgUrl", propPlataforma.getString("pgUrl"));
			if (propPlataforma.containsKey("pgUsuario"))
				this.paramsQuartz.put("pgUsuario", propPlataforma.getString("pgUsuario"));
			if (propPlataforma.containsKey("pgPassword"))
				this.paramsQuartz.put("pgPassword", propPlataforma.getString("pgPassword"));
	
			// Mensajes
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
			propPlataforma = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			this.problemInesp = propPlataforma.getString("mensaje.error.inesperado");
			this.busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");
			this.passWordVacio = propPlataforma.getString("mensaje.validacion.password");		
			this.malInicioResp = propPlataforma.getString("mensaje.validacion.fechaIniResponsabilidad");
			this.malFinResp = propPlataforma.getString("mensaje.fechaFinResponsabilidad");		
			this.vigenciaIncorrecta = propPlataforma.getString("mensaje.validacion.vigenciaUsuario");
			this.contrasenaDiferente = propPlataforma.getString("mensaje.error.contrasenaDiferente");
			this.vigenciaFechasResp = propPlataforma.getString("mensaje.validacion.vigenciaResponsabilidad");
	
			// Busqueda Empleados
			this.empBusquedaTipos = new ArrayList<SelectItem>();
			this.empBusquedaTipos.add(new SelectItem("nombre", "Nombre"));
			this.empBusquedaTipos.add(new SelectItem("clave", "Clave"));
			this.empBusquedaTipos.add(new SelectItem("id", "ID"));
			busquedaEmpleados();
			
			this.listPermisosItems = new ArrayList<SelectItem>();
			this.listPermisosItems.add(new SelectItem(0, "Ninguno"));
			this.listPermisosItems.add(new SelectItem(1, "Consultar"));
			this.listPermisosItems.add(new SelectItem(3, "Consultar/Modificar"));
			this.listPermisosItems.add(new SelectItem(5, "Consultar/Borrar"));
			this.listPermisosItems.add(new SelectItem(7, "Todos"));
			this.valorPermiso = 0;
			
			// Inicializaciones
			this.pojoUsuarioExt = new UsuarioExt();
			this.pojoUsuarioResponsabilidad = new UsuarioResponsabilidad();
			this.pojoResponsabilidad = new Responsabilidad();
			this.listTmpResponsabilidad = new ArrayList<Responsabilidad>();
			this.listUsuarios = new ArrayList<UsuarioExt>();
			this.listTmpUsuarioResponsabilidad = new ArrayList<UsuarioResponsabilidad>();
			this.listUsuariot = new ArrayList<Usuario>();
			this.listNivelAcceso = new ArrayList<SelectItem>();
			this.listResponsabilidad = new ArrayList<SelectItem>();
			this.listTmpUsuarios = new ArrayList<SelectItem>();
			this.listUsuarioResponsabilidad = new ArrayList<SelectItem>();
			this.valTipoBusqueda = "";
			this.campoBusqueda = "";
			this.numPagina = 1;
			this.numPaginaBuscarEmpleado = 1;
			this.numPaginaNivelResponsabilidad = 1;
			this.listEmpresasItems = new ArrayList<SelectItem>();
	
			cargarEmpresas();
			cargarListaResponsabilidades();
			cargarAplicaciones();
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar el EJB", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscar() {
		Respuesta respuesta = null;
		
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			/*if (! this.ifzPermisos.validarPermiso(this.permisos, TipoPermiso.Leer)) {
				control(301, "No tiene permitido consultar informacion");
				return;
			}*/
			
			respuesta = this.ifzAdministracion.buscarUsuarioExt(this.campoBusqueda, this.valTipoBusqueda);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(respuesta.getErrores().getDescError(), null);
				return;
			}
			
			this.listUsuarios = (List<UsuarioExt>) respuesta.getBody().getValor("listUsuarios");
			if (this.listUsuarios == null || this.listUsuarios.isEmpty()) {
				control(this.busquedaVacia, null);
				return;
			}
			
			// Ordenamos for usuario
			Collections.sort(this.listUsuarios, new Comparator<UsuarioExt>() {
				@Override
				public int compare(UsuarioExt o1, UsuarioExt o2) {
					return o1.getUsuario().compareTo(o2.getUsuario());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al realizar la Busqueda", e);
		}
	}

	public void nuevo() {
		try{
			this.resOperacion = "";
			this.pojoUsuarioExt = new UsuarioExt();
			this.pojoUsuarioExt.setCorreo("nomail@grupodisesa.com.mx");
			this.usuarioEmpleado = false;
			this.usuarioEmpleadoLock = false;
			this.usuarioPrevio = "";
			this.idEmpleado = "";
			this.contrasena = "";
			
			this.listTmpUsuarioResponsabilidad = new ArrayList<UsuarioResponsabilidad>();
			this.listUsuarioEmpresas = new ArrayList<EmpresasUsuarios>();
			this.listPermisos = new ArrayList<PermisosUsuario>();
			this.indexPermisoBorrar = -1;
			cargarEmpresas();
			cargarListaResponsabilidades();
		} catch (Exception e) {
			control("Ocurrio un problema al inicializar el nuevo Usuario", e);
		}
	}

	public void editar() {
		try {
			control();
			// Inicializamos
			this.registroSeleccionado = true;
			this.usuarioEmpleadoLock = false;
			this.usuarioEmpleado = false;
			this.usuarioPrevio = this.pojoUsuarioExt.getUsuario();
			if (this.pojoUsuarioExt.getIdEmpleado() != null && this.pojoUsuarioExt.getIdEmpleado() > 1L) {
				this.usuarioEmpleado = true;
				this.usuarioEmpleadoLock = true;
			}
			
			cargarEmpresas();
			
			// Cargamos responsabilidades
			cargarResponsabilidadNivelAcceso();
			
			// Cargamos empresas asigandas
			cargarUsuarioEmpresas();
			
			// Cargamos Perfiles
			cargarPerfiles();
			
			// Cargamos permisos
			this.listPermisos = new ArrayList<PermisosUsuario>();
			cargarPermisos();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la informacion del Usuario seleccionado", e);
		}
	}
	
	public void guardar() {
		boolean autoAgregaEmpresa = false;
		Respuesta respuesta = null;
		Usuario usuario = null;
		
		try {
			this.resOperacion = "";	
			long id = this.pojoUsuarioExt.getId();
			this.pojoUsuarioExt.setUsuario(this.pojoUsuarioExt.getUsuario().toUpperCase());
			if(this.pojoUsuarioExt.getFechaTerminacion().before(this.pojoUsuarioExt.getFechaInicio())){
				this.resOperacion = this.vigenciaIncorrecta;
				return;
			}
			
			if (! comprobarUsuario())
				return;

			this.pojoUsuarioExt.setUltimoAcceso(Calendar.getInstance().getTime());
			this.pojoUsuarioExt.setFechaCambio(Calendar.getInstance().getTime());
			if (this.pojoUsuarioExt.getPassword() == null || "".equals(this.pojoUsuarioExt.getPassword()))
				this.pojoUsuarioExt.setPassword(this.pojoUsuarioExt.getUsuario().toUpperCase());
			if (id == 0L) {
				this.pojoUsuarioExt.setExpirado(1L);
				autoAgregaEmpresa = true;
			}
			
			respuesta = this.ifzAdministracion.salvar(this.pojoUsuarioExt);
			if (respuesta.getErrores().getCodigoError() == 0L) {
				this.pojoUsuarioExt = (UsuarioExt) respuesta.getBody().getValor("pojoUsuario");
				usuario = (Usuario) respuesta.getBody().getValor("entity");
				if (id == 0L) {
					this.listUsuarios.add(0, this.pojoUsuarioExt);
					// Cargamos Perfiles
					cargarPerfiles();
					// Cargamos permisos
					this.listPermisos = new ArrayList<PermisosUsuario>();
					cargarPermisos();
				} else {
					// Actualizamos valores de perfiles
					for (SelectItem item : this.listPerfilValorItems) {
						for (Entry<PerfilValor, String> perfil : this.listPerfilValor.entrySet()) {
							if (perfil.getKey().getId() == Long.valueOf(item.getDescription()).longValue()) {
								perfil.getKey().setValorPerfil((boolean) item.getValue() ? "S" : "");
								break;
							}
						}
					}
					
					// Guardamos valores de perfiles
					for (Entry<PerfilValor, String> perfil : this.listPerfilValor.entrySet())
						this.ifzAdministracion.salvar(perfil.getKey());
				}
				
				// Guardamos permisos
				if (this.listPermisos != null && ! this.listPermisos.isEmpty()) {
					for (PermisosUsuario item : this.listPermisos) 
						item.setIdUsuario(usuario);
					this.ifzPermisos.setInfoSesion(this.loginManager.getInfoSesion());
					this.listPermisos = this.ifzPermisos.saveListByUsuario(this.listPermisos);
				}
				
				// Borramos permisos previos si corresponde
				if (this.listPermisosEliminados != null && ! this.listPermisosEliminados.isEmpty()) {
					this.ifzPermisos.setInfoSesion(this.loginManager.getInfoSesion());
					this.ifzPermisos.deleteListFromUsuario(this.listPermisosEliminados);
					this.listPermisosEliminados.clear();
				}
				
				// Actualizamos quartz
				actualizarUsuarioReportes(this.pojoUsuarioExt.getUsuario(), this.usuarioPrevio);
				this.usuarioPrevio = "";
			} else {
				this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e) {
			log.error("Error en el metodo guardar", e);
			this.resOperacion = this.problemInesp;
		}
		
		if (autoAgregaEmpresa) {
			this.idEmpresa = this.loginManager.getInfoSesion().getEmpresa().getId();
			agregarEmpresa();
		}
	}

	public void eliminar() {
		if (this.pojoUsuarioExt != null && this.pojoUsuarioExt.getId() > 0L) {
			this.pojoUsuarioExt.setBloqueado(true);
			this.pojoUsuarioExt.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			this.pojoUsuarioExt.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzAdministracion.salvar(this.pojoUsuarioExt);
		}
	}

	public void validarContrasena() {
		try {
			this.resOperacion = "";
			
			if (this.contrasena2 != null && !("".equals(this.contrasena2))) {
				if (! this.contrasena.equals(this.contrasena2)){
					this.resOperacion = contrasenaDiferente;
					return;
				} else {
					this.pojoUsuarioExt.setPassword(MD5.getHashString(contrasena.toUpperCase()));
					this.pojoUsuarioExt.setUltimoAcceso(new Date());
					this.pojoUsuarioExt.setBloqueado(false);
					this.pojoUsuarioExt.setFechaCambio(new Date());
					this.pojoUsuarioExt.setExpirado(0L);
					Respuesta respuesta  = this.ifzAdministracion.salvar(this.pojoUsuarioExt);
					if(respuesta.getErrores().getCodigoError() == 0L)
						pojoUsuarioExt = (UsuarioExt)respuesta.getBody().getValor("pojoUsuario");
					else
						this.resOperacion = respuesta.getErrores().getDescError();
				}
			} else {
				this.resOperacion = passWordVacio;				
				return;
			}		
		} catch (Exception e) {
			log.error("Error en el metodo validarContrasena", e);
			this.resOperacion = this.problemInesp;			
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarResponsabilidadNivelAcceso() { 
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzAdministracion.listarUsuarioResponsabilidad(pojoUsuarioExt);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listTmpUsuarioResponsabilidad = (List<UsuarioResponsabilidad>) respuesta.getBody().getValor("listUsuarioResponsabilidad");
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error en el metodo cargarResponsabilidadNivelAcceso", e);
			this.resOperacion = problemInesp;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void conectados() {
		this.usuarios = new ArrayList<String>();
		this.sessiones = new HashMap<String, String>();
		this.sessionesItems = new ArrayList<SelectItem>();
		
		this.usuarios = this.loginManager.getUsuarios();
		this.usuarios = (this.usuarios != null) ? this.usuarios : new ArrayList<String>();
		Collections.sort(this.usuarios);

		this.sessiones = this.loginManager.getSessiones();
		this.sessiones = (this.sessiones != null) ? this.sessiones : new HashMap<String, String>();
		Collections.sort(new ArrayList(this.sessiones.keySet()));
		
		for (Entry<String,String> item : this.sessiones.entrySet())
			this.sessionesItems.add(new SelectItem(item.getKey(), item.getValue()));
	}
	
	private boolean comprobarUsuario() {
		List<Usuario> usuarios = null;
		
		try {
			if (this.pojoUsuarioExt.getId() > 0L) 
				return true;
			usuarios = this.ifzAdministracion.buscarUsuario("usuario", this.pojoUsuarioExt.getUsuario());
			if (usuarios != null && ! usuarios.isEmpty()) {
				log.warn("El usuario indicado ya existe");
				this.resOperacion = "El usuario indicado ya existe";
				return false;
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar consultar el usuario", e);
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private void cargarPerfiles() {
		Respuesta respuesta = null;
		List<PerfilValor> perfiles = null;
		long nivel = 0L;
		long nivelValor = 0L;
		
		this.listPerfilValor = new LinkedHashMap<PerfilValor, String>();
		this.listPerfilValorItems = new ArrayList<SelectItem>();

		nivel = 2L;
		if (this.pojoUsuarioExt != null && this.pojoUsuarioExt.getId() > 0L)
			nivelValor = this.pojoUsuarioExt.getId();
		
		if (nivelValor <= 0L)
			return;
		
		respuesta = this.ifzAdministracion.buscarPerfilesNivel(nivel, nivelValor, 0L);
		if (respuesta.getErrores().getCodigoError() != 0L) {
			log.error("NO SE ENCONTRARON PERFILES");
			return;
		}
		
		perfiles = (List<PerfilValor>) respuesta.getBody().getValor("perfiles");
		if (perfiles == null || perfiles.isEmpty()) {
			log.error("NO SE ENCONTRARON PERFILES");
			return;
		}
		
		/*respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("EGRESOS_OPERACION", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Perfil Administrativo");
		}
			
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("CODIFICAR_INSUMOS", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir codificar productos de operacion (Insumos)");
		}
		
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("CODIFICAR_EGRESOS", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir codificar productos Administrativos");
		}
		
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("FINIQUITOS_VOBO", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir asignar Visto Bueno en Finiquitos");
		}
		
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("FINIQUITOS_APROBACION", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir aprobacion Finiquitos");
		}
		
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("FACTURAS_FOLIO_MANUAL", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir asignar serie y folio a Facturas en Cuentas por Cobrar");
		}
		
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("REGISTRO_GASTOS_EDITAR", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir editar Registro de Gastos");
		}
		
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("CAJA_CHICA_EDITAR", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir editar Caja Chica");
		}
		
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("CAJA_CHICA_VOBO", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir asignar Visto Bueno en Caja Chica");
		}
		
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("CAJA_CHICA_AUTORIZACION", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir autorizar Cajas Chicas");
		}
		
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("CAJA_CHICA_ABONOS", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir asignar Cheque a Caja Chica");
		}
		
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("AUTORIZAR", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir autorizar Obras, Requisicion y Ordenes de Compra");
		}
		
		respuesta = this.ifzAdministracion.buscarPerfilValorUsuario("SYS_CONFIG_REQ_SIN_LIMITE", nivel, nivelValor);
		if (respuesta.getErrores().getCodigoError() == 0L) {
			valorPerfil = (PerfilValor) respuesta.getBody().getValor("perfilValor");
			this.listPerfilValor.put(valorPerfil, "Permitir autorizar Obras, Requisicion y Ordenes de Compra");
		}*/

		for (PerfilValor valorPerfil : perfiles) 
			this.listPerfilValor.put(valorPerfil, valorPerfil.getPerfil().getDescripcion());
		if (! this.listPerfilValor.isEmpty()) {
			for (Entry<PerfilValor, String> item : this.listPerfilValor.entrySet()) {
				this.listPerfilValorItems.add(new SelectItem(("S".equals(item.getKey().getValorPerfil()) ? true : false), item.getValue(), String.valueOf(item.getKey().getId())));
			}
		}
	}
	
	private void control() {
		this.operacion = false;
		this.tipoMensaje = 0;
		this.resOperacion = "";
		this.mensajeDetalles = "";
	}

	private void control(int value, String mensaje) { 
		control(true, value, mensaje, null); 
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, 1, mensaje, throwable);
	}
	
	private void control(boolean incompleta, int tipoMensaje, String mensaje, Throwable throwable) {
		this.operacion = incompleta;
		this.tipoMensaje = tipoMensaje;
		this.resOperacion = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim().replace("\n", "<br/>") : "Ocurrio un problema no controlado al realizar la operacion");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	// --------------------------------------------------------------------------------------------------------------------------------
	// RESPONSABILIDADES
	// --------------------------------------------------------------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	public void cargarListaResponsabilidades(){
		try{
			this.resOperacion = "";
			Respuesta respuesta = ifzAdministracion.autocompletarResponsabilidad();
			if(respuesta.getErrores().getCodigoError() == 0L){
				listTmpResponsabilidad = (List<Responsabilidad>)respuesta.getBody().getValor("listResponsabilidades");
				
				Collections.sort(this.listTmpResponsabilidad, new Comparator<Responsabilidad>() {
			    	@Override
			        public int compare(Responsabilidad o1, Responsabilidad o2) {
			    		return o1.getResponsabilidad().compareTo(o2.getResponsabilidad());
			        }
				});
				
				listResponsabilidad.clear();
				for (Responsabilidad i : listTmpResponsabilidad) {
					listResponsabilidad.add(new SelectItem(i.getId(), i.getResponsabilidad()));
				}
			} else 
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e){
			log.error("Error en el metodo cargarListaResponsabilidades", e);
		}
	}

	public void agregarResponsabilidadNivelAcceso() {
		cargarListaResponsabilidades();
		this.pojoUsuarioResponsabilidad = new UsuarioResponsabilidad();
	}

	public void guardarResponsabilidadNivel() {
		try {
			this.resOperacion = "";
			long id = pojoUsuarioResponsabilidad.getId();
	
			if(this.pojoUsuarioResponsabilidad.getFechaTerm().before(this.pojoUsuarioResponsabilidad.getFechaIni())){
				this.resOperacion = vigenciaFechasResp;
			}
			
			if(this.pojoUsuarioResponsabilidad.getFechaIni().before(this.pojoUsuarioExt.getFechaInicio())){
				this.resOperacion = malInicioResp;
				return;
			}
			
			if(this.pojoUsuarioResponsabilidad.getFechaTerm().after(this.pojoUsuarioExt.getFechaTerminacion())){
				this.resOperacion = malFinResp;
				return;
			}

			if (id == 0) {
				Usuario usuario = new Usuario();
				usuario.setId(pojoUsuarioExt.getId());
				this.pojoUsuarioResponsabilidad.setUsuario(usuario);
			} 
			
			Respuesta respuesta = this.ifzAdministracion.salvar(this.pojoUsuarioResponsabilidad);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoUsuarioResponsabilidad = (UsuarioResponsabilidad) respuesta.getBody().getValor("pojoUsuarioResponsabilidad");
				this.listTmpUsuarioResponsabilidad.add(pojoUsuarioResponsabilidad);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarResponsabilidadNivel", e);
		}
	}

	public void eliminarResponsabilidadNivel() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzAdministracion.eliminar(this.pojoUsuarioResponsabilidad);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listTmpUsuarioResponsabilidad.remove(this.pojoUsuarioResponsabilidad);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminarResponsabilidadNivel", e);
		}
	}

	private Responsabilidad getResponsabilidadById(long id, List<Responsabilidad> lista) {
		for (Responsabilidad responsabilidad : lista) {
			if (responsabilidad.getId() == id)
				return responsabilidad;
		}
		return null;
	}

	// --------------------------------------------------------------------------------------------------------------------------------
	// USUARIO EMPRESAS
	// --------------------------------------------------------------------------------------------------------------------------------

	private void cargarEmpresas() {
		try {
			if (this.listEmpresas == null)
				this.listEmpresas = new ArrayList<Empresa>();
			this.listEmpresas.clear();
			
			if (this.listEmpresasItems == null)
				this.listEmpresasItems = new ArrayList<SelectItem>();
			this.listEmpresasItems.clear();
			
			this.listEmpresas = this.ifzEmpresas.findAll();
			if (this.listEmpresas != null && ! this.listEmpresas.isEmpty()) {
				for (Empresa item : this.listEmpresas)
					this.listEmpresasItems.add(new SelectItem(item.getId(), item.getEmpresa()));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el catalogo de Empresas", e);
		}
	}

	private void cargarUsuarioEmpresas() {
		try {
			if (this.listUsuarioEmpresas == null)
				this.listUsuarioEmpresas = new ArrayList<EmpresasUsuarios>();
			this.listUsuarioEmpresas.clear();
			
			this.listUsuarioEmpresas = this.ifzUsuEmpresas.findByProperty("idUsuario.id", this.pojoUsuarioExt.getId(), 0);
			this.numUsuarioEmpresas = 1;
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar las Empresas asignadas al Usuario", e);
		}
	}
	
	public void agregarEmpresa() {
		Usuario user = null;
		Empresa emp = null;
		
		try {
			if (this.idEmpresa <= 0L) {
				control(-1, "Debe seleccionar una Empresa");
				return;
			}
			
			for (Empresa item : this.listEmpresas) {
				if (this.idEmpresa == item.getId().longValue()) {
					emp = item;
					break;
				}
			}

			if (emp == null || emp.getId() <= 0L) {
				control(-1, "Ocurrio un problema al obtener la Empresa seleccionada");
				return;
			}
			
			user = this.ifzAdministracion.convertirUsuario(this.pojoUsuarioExt);
			if (user == null || user.getId() <= 0L) {
				control(-1, "Ocurrio un problema al obtener el Usuario seleccionado");
				return;
			}
			
			this.pojoUsuarioEmpresa = new EmpresasUsuarios();
			this.pojoUsuarioEmpresa.setIdEmpresa(emp);
			this.pojoUsuarioEmpresa.setIdUsuario(user);
			this.pojoUsuarioEmpresa.setEstatus(0);
			this.pojoUsuarioEmpresa.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			this.pojoUsuarioEmpresa.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoUsuarioEmpresa.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			this.pojoUsuarioEmpresa.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.pojoUsuarioEmpresa.setId(this.ifzUsuEmpresas.save(this.pojoUsuarioEmpresa));
			this.listUsuarioEmpresas.add(this.pojoUsuarioEmpresa);
			this.pojoUsuarioEmpresa = null;
			this.idEmpresa = 0L;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar asignar la Empresa al Usuario", e);
		}
	}
	
	public void quitarEmpresa() {
		try {
			if (this.pojoUsuarioEmpresa == null) {
				control(-1, "Debe seleccionar una Empresa de las asignadas al Usuario");
				return;
			}
			
			if (this.pojoUsuarioEmpresa.getId() != null && this.pojoUsuarioEmpresa.getId() > 0L)
				this.ifzUsuEmpresas.delete(this.pojoUsuarioEmpresa.getId());
			this.listUsuarioEmpresas.remove(this.pojoUsuarioEmpresa);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar quitar la Empresa del Usuario", e);
		}
	}
	
	// --------------------------------------------------------------------------------------------------------------------------------
	// BUSQUEDA EMPLEADOS
	// --------------------------------------------------------------------------------------------------------------------------------
	
	public void busquedaEmpleados() {
		this.listEmpleados = new ArrayList<Empleado>();
		this.pojoEmpleadoSeleccionado = null;
		
		this.empBusquedaCampo = this.empBusquedaTipos.get(0).getValue().toString();
		this.empBusquedaValor = "";
		this.empBusquedaPagina = 1;
	}
	
	public void buscarEmpleados() {
		try {
    		control();
			this.listEmpleados = this.ifzEmpleados.findLike(this.empBusquedaValor, "nombre", 0, 0);
			if (this.listEmpleados == null || this.listEmpleados.isEmpty()) {
	    		control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Empleados", e);
    	}
	}
	
	public void seleccionarEmpleado() {
		String emailEmpleado = "";
		String emailUsuario = "";
		
		try {
    		control();
			if (this.pojoEmpleadoSeleccionado != null && this.pojoEmpleadoSeleccionado.getId() != null && this.pojoEmpleadoSeleccionado.getId() > 0L) {
				this.pojoUsuarioExt.setIdEmpleado(this.pojoEmpleadoSeleccionado.getId());
				this.pojoUsuarioExt.setNombre(this.pojoEmpleadoSeleccionado.getNombre());
				
				emailUsuario = this.pojoUsuarioExt.getCorreo();
				emailEmpleado = this.pojoEmpleadoSeleccionado.getEmail();
				
				if ((emailUsuario != null && ! "".equals(emailUsuario.trim())) && (emailEmpleado == null || "".equals(emailEmpleado.trim()))) {
					this.pojoEmpleadoSeleccionado.setEmail(emailUsuario);
					this.ifzEmpleados.update(this.pojoEmpleadoSeleccionado);
				} else if ((emailUsuario == null || "".equals(emailUsuario.trim())) && (emailEmpleado != null && ! "".equals(emailEmpleado.trim()))) {
					this.pojoUsuarioExt.setCorreo(emailEmpleado);
				}
			}
			
			busquedaEmpleados();
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Empleados", e);
    	}
	}
	
	public void quitarEmpleado() {
		this.usuarioEmpleado = false;
		this.usuarioEmpleadoLock = false;
		this.pojoUsuarioExt.setIdEmpleado(1L);
	}

	// --------------------------------------------------------------------------------------------------------------------------------
	// PERMISOS
	// --------------------------------------------------------------------------------------------------------------------------------
	
	private void cargarAplicaciones() {
		try {
			this.listAplicacionesItems = new ArrayList<SelectItem>();
			this.ifzPermisos.setInfoSesion(this.loginManager.getInfoSesion());
			this.listAplicaciones = this.ifzPermisos.getAplicaciones();
			this.listAplicaciones = (this.listAplicaciones != null) ? this.listAplicaciones: new ArrayList<Aplicacion>();
			if (this.listAplicaciones.isEmpty())
				return;

			for (Aplicacion app : this.listAplicaciones) 
				this.listAplicacionesItems.add(new SelectItem(app.getId(), app.getAplicacion()));
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar las Aplicaciones disponibles", e);
		}
	}
	
	public void cargarFunciones() {
		try {
			control();
			this.listFuncionesItems = new ArrayList<SelectItem>();
			this.ifzPermisos.setInfoSesion(this.loginManager.getInfoSesion());
			this.listFunciones = this.ifzPermisos.getFunciones(this.idAplicacion);
			this.listFunciones = (this.listFunciones != null) ? this.listFunciones: new ArrayList<Funcion>();
			if (this.listFunciones.isEmpty())
				return;

			//this.listFuncionesItems.add(new SelectItem(-1L, "Todos"));
			for (Funcion app : this.listFunciones) 
				this.listFuncionesItems.add(new SelectItem(app.getId(), app.getNombre()));
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar las Aplicaciones disponibles", e);
		}
	}
	
	public void cargarPermisos() {
		List<PermisosUsuario> nuevos = null;
		
		try {
			control();
			if (this.listPermisos != null && ! this.listPermisos.isEmpty()) {
				for (PermisosUsuario item : this.listPermisos) {
					if (item.getId() != null && item.getId() > 0L)
						continue;
					nuevos = (nuevos != null) ? nuevos : new ArrayList<PermisosUsuario>();
					nuevos.add(item);
				}
			}

			this.paginacionPermisos = 1;
			this.indexPermisoBorrar = -1;
			this.ifzPermisos.setInfoSesion(this.loginManager.getInfoSesion());
			this.listPermisos = this.ifzPermisos.findByUsuario(this.pojoUsuarioExt.getId(), "");
			this.listPermisos = (this.listPermisos != null) ? this.listPermisos : new ArrayList<PermisosUsuario>();
			if (nuevos != null && ! nuevos.isEmpty()) {
				nuevos.addAll(this.listPermisos);
				this.listPermisos.clear();
				this.listPermisos.addAll(nuevos);
				nuevos = null;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los Permisos asignados", e);
		}
	}
	
	public void agregarPermiso() {
		PermisosUsuario permiso = null;
		Aplicacion aplicacion = null;
		Funcion funcion = null;

		try {
			control();
			// Validamos, si la asignacion ya existe modificamos el permiso
			if (! this.listPermisos.isEmpty()) {
				for (PermisosUsuario item : this.listPermisos) {
					if (this.idAplicacion == item.getIdAplicacion().getId() && this.idFuncion == item.getIdFuncion().getId() && this.valorPermiso != item.getPermiso()) {
						item.setPermiso(this.valorPermiso);
						item.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
						item.setFechaModificacion(Calendar.getInstance().getTime());
						log.warn("La asignacion ya existe, se actualizo el valor de permiso");
						this.idAplicacion = 0L;
						this.idFuncion = 0L;
						this.valorPermiso = 0;
						return;
					}
				}
			}
			
			
			// Recuperamos Aplicacion
			for (Aplicacion app : this.listAplicaciones) {
				if (this.idAplicacion == app.getId()) {
					aplicacion = app;
					break;
				}
			}
			if (aplicacion == null || aplicacion.getId() == 0L) {
				control(-1, "Ocurrio un problema al recuperar la Aplicacion asignada");
				return;
			}
			
			// Recuperamos Funcion
			for (Funcion fun : this.listFunciones) {
				if (this.idFuncion == fun.getId()) {
					funcion = fun;
					break;
				}
			}
			if (funcion == null || funcion.getId() == 0L) {
				control(-1, "Ocurrio un problema al recuperar la Funcion asignada");
				return;
			}
			
			// Generamos permiso
			permiso = new PermisosUsuario();
			permiso.setIdAplicacion(aplicacion);
			permiso.setIdFuncion(funcion);
			permiso.setPermiso(this.valorPermiso);
			permiso.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			permiso.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			permiso.setFechaCreacion(Calendar.getInstance().getTime());
			permiso.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			permiso.setFechaModificacion(Calendar.getInstance().getTime());

			this.paginacionPermisos = 1;
			this.indexPermisoBorrar = -1;
			this.listPermisos = (this.listPermisos != null) ? this.listPermisos : new ArrayList<PermisosUsuario>();
			this.listPermisos.add(0, permiso);
			this.idAplicacion = 0L;
			this.idFuncion = 0L;
			this.valorPermiso = 0;
		} catch (Exception e) {
			control("Ocurrio un problema al añadir el Permiso asignado", e);
		}
	}
	
	public void quitarPermiso() {
		try {
			if (this.indexPermisoBorrar < 0 || this.indexPermisoBorrar > this.listPermisos.size()) {
				control(-1, "Indice no valido");
				this.indexPermisoBorrar = -1;
				return;
			}
			
			this.listPermisosEliminados = (this.listPermisosEliminados != null) ? this.listPermisosEliminados : new ArrayList<PermisosUsuario>();
			this.listPermisosEliminados.add(this.listPermisos.get(this.indexPermisoBorrar));
			this.listPermisos.remove(this.indexPermisoBorrar);
			this.indexPermisoBorrar = -1;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar quitar el Permiso indicado", e);
		}
	}
	
	// --------------------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------------------------------------------------------

	private void actualizarUsuarioReportes(String usuario, String usuarioPrevio) {
        ResultSet rsQuartz = null;
		String strQuery = "";
		
		try {
    		if (this.paramsQuartz == null || this.paramsQuartz.isEmpty()) 
    			throw new Exception("404 Not Found");
            if (! this.getConnection())
            	throw new Exception("403 Forbidden");
            strQuery = "select fn_user_save(':usuario',':usuarioPrevio') as result";
            strQuery = strQuery.replace(":usuarioPrevio", usuarioPrevio);
            strQuery = strQuery.replace(":usuario", usuario);
            rsQuartz = this.conn.createStatement().executeQuery(strQuery);
            rsQuartz.next();
            strQuery = "QUARTZ :: " + rsQuartz.getString("result");
            log.info(strQuery);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar sincronizar el Usuario en la base de datos de Reportes", e);
		} finally {
			closeConnection();
		}
	}
	
    private boolean getConnection() {
		try {
			if (this.conn == null) {
				Class.forName(this.paramsQuartz.get("pgDriver"));
				this.conn = DriverManager.getConnection(this.paramsQuartz.get("pgUrl"), this.paramsQuartz.get("pgUsuario") , this.paramsQuartz.get("pgPassword"));
			}
		} catch (Exception e) {
			log.error("Error al obtener la coneccion", e);
			return false;
		}
		
		return true;
	}
    
    private void closeConnection() {
		try {
			if (this.conn == null)
				return;
			if (! this.conn.isClosed()) 
				this.conn.close();
			this.conn = null;
		} catch (SQLException e) {
			log.error("Error al cerrar la coneccion", e);
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------------------------------------------------------

	public List<PermisosUsuario> getListPermisos() {
		this.listPermisos = (this.listPermisos != null) ? this.listPermisos : new ArrayList<PermisosUsuario>();
		return listPermisos;
	}

	public void setListPermisos(List<PermisosUsuario> listPermisos) {
		this.listPermisos = listPermisos;
	}

	public List<SelectItem> getListPermisosItems() {
		this.listPermisosItems = (this.listPermisosItems != null) ? this.listPermisosItems : new ArrayList<SelectItem>();
		return listPermisosItems;
	}

	public void setListPermisosItems(List<SelectItem> listPermisosItems) {
		this.listPermisosItems = listPermisosItems;
	}

	public int getValorPermiso() {
		return valorPermiso;
	}

	public void setValorPermiso(int valorPermiso) {
		this.valorPermiso = valorPermiso;
	}

	public int getPaginacionPermisos() {
		return paginacionPermisos;
	}

	public void setPaginacionPermisos(int paginacionPermisos) {
		this.paginacionPermisos = paginacionPermisos;
	}

	public List<SelectItem> getListAplicacionesItems() {
		this.listAplicacionesItems = (this.listAplicacionesItems != null) ? this.listAplicacionesItems : new ArrayList<SelectItem>();
		return listAplicacionesItems;
	}

	public void setListAplicacionesItems(List<SelectItem> listAplicacionesItems) {
		this.listAplicacionesItems = listAplicacionesItems;
	}

	public long getIdAplicacion() {
		return idAplicacion;
	}

	public void setIdAplicacion(long idAplicacion) {
		this.idAplicacion = idAplicacion;
	}

	public List<SelectItem> getListFuncionesItems() {
		this.listFuncionesItems = (this.listFuncionesItems != null) ? this.listFuncionesItems : new ArrayList<SelectItem>();
		return listFuncionesItems;
	}

	public void setListFuncionesItems(List<SelectItem> listFuncionesItems) {
		this.listFuncionesItems = listFuncionesItems;
	}

	public long getIdFuncion() {
		return idFuncion;
	}

	public void setIdFuncion(long idFuncion) {
		this.idFuncion = idFuncion;
	}
	
	public int getIndexPermisoBorrar() {
		return indexPermisoBorrar;
	}

	public void setIndexPermisoBorrar(int indexPermisoBorrar) {
		this.indexPermisoBorrar = indexPermisoBorrar;
	}

	public String getNombre() {
		if (this.pojoUsuarioExt.getNombre() != null) {
			if (this.pojoUsuarioExt.getIdEmpleado() != null && this.pojoUsuarioExt.getIdEmpleado() > 1L)
				return this.pojoUsuarioExt.getIdEmpleado() + " - " + this.pojoUsuarioExt.getNombre();
			else
				return this.pojoUsuarioExt.getNombre();
		}
		
		return "";
	}
	
	public void setNombre(String value) {
		this.pojoUsuarioExt.setNombre(value);
	}

	public String getCorreo() {
		if (this.pojoUsuarioExt.getCorreo() != null)
			return this.pojoUsuarioExt.getCorreo();
		return "";
	}
	
	public void setCorreo(String value) {
		this.pojoUsuarioExt.setCorreo(value);
	}
	
	public void setSuggResponsabilidad(Long usuarioresponsabilidad) {
		this.pojoUsuarioResponsabilidad.setResponsabilidad(getResponsabilidadById(usuarioresponsabilidad, this.listTmpResponsabilidad));
	}

	public Long getSuggResponsabilidad() {
		return pojoUsuarioResponsabilidad.getResponsabilidad() != null ? pojoUsuarioResponsabilidad.getResponsabilidad().getId(): -1L;
	}

	public Responsabilidad getPojoResponsabilidad() {
		return pojoResponsabilidad;
	}

	public void setPojoResponsabilidad(Responsabilidad pojoResponsabilidad) {
		this.pojoResponsabilidad = pojoResponsabilidad;
	}

	public UsuarioResponsabilidad getpojoUsuarioResponsabilidad() {
		return pojoUsuarioResponsabilidad;
	}

	public void setpojoUsuarioResponsabilidad(UsuarioResponsabilidad pojoUsuarioResponsabilidad) {
		this.pojoUsuarioResponsabilidad = pojoUsuarioResponsabilidad;
	}

	public void asociarEmpleado(){
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public Long getIdResponsabilidad() {
		return pojoUsuarioResponsabilidad.getResponsabilidad() != null ? pojoUsuarioResponsabilidad.getResponsabilidad().getId() : -1L;
	}
	 
	public int getNumPaginaBuscarEmpleado() {
		return numPaginaBuscarEmpleado;
	}

	public void setNumPaginaBuscarEmpleado(int numPaginaBuscarEmpleado) {
		this.numPaginaBuscarEmpleado = numPaginaBuscarEmpleado;
	}

	public List<SelectItem> getListNivelAcceso() {
		return listNivelAcceso;
	}

	public void setListNivelAcceso(List<SelectItem> listNivelAcceso) {
		this.listNivelAcceso = listNivelAcceso;
	}
	
	public int getNumPaginaNivelResponsabilidad() {
		return numPaginaNivelResponsabilidad;
	}

	public void setNumPaginaNivelResponsabilidad(int numPaginaNivelResponsabilidad) {
		this.numPaginaNivelResponsabilidad = numPaginaNivelResponsabilidad;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getContrasena2() {
		return contrasena2;
	}

	public void setContrasena2(String contrasena2) {
		this.contrasena2 = contrasena2;
	}
	
	public String getContrasenaOriginal() {
		return contrasenaOriginal;
	}

	public void setContrasenaOriginal(String contrasenaOriginal) {
		this.contrasenaOriginal = contrasenaOriginal;
	}

	public String getPassWordVacio() {
		return passWordVacio;
	}

	public void setPassWordVacio(String passWordVacio) {
		this.passWordVacio = passWordVacio;
	}

	public String getProblemInesp() {
		return problemInesp;
	}

	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}

	public String getBusquedaVacia() {
		return busquedaVacia;
	}

	public void setBusquedaVacia(String busquedaVacia) {
		this.busquedaVacia = busquedaVacia;
	}

	public String getMalInicioResp() {
		return malInicioResp;
	}

	public void setMalInicioResp(String malInicioResp) {
		this.malInicioResp = malInicioResp;
	}

	public String getMalFinResp() {
		return malFinResp;
	}

	public void setMalFinResp(String malFinResp) {
		this.malFinResp = malFinResp;
	}

	public String getVigenciaIncorrecta() {
		return vigenciaIncorrecta;
	}

	public void setVigenciaIncorrecta(String vigenciaIncorrecta) {
		this.vigenciaIncorrecta = vigenciaIncorrecta;
	}

	public String getContrasenaDiferente() {
		return contrasenaDiferente;
	}

	public void setContrasenaDiferente(String contrasenaDiferente) {
		this.contrasenaDiferente = contrasenaDiferente;
	}

	public String getVigenciaFechasResp() {
		return vigenciaFechasResp;
	}

	public void setVigenciaFechasResp(String vigenciaFechasResp) {
		this.vigenciaFechasResp = vigenciaFechasResp;
	}

	public List<UsuarioResponsabilidad> getListTmpUsuarioResponsabilidad() {
		return listTmpUsuarioResponsabilidad;
	}

	public void setListTmpUsuarioResponsabilidad(List<UsuarioResponsabilidad> listTmpUsuarioResponsabilidad) {
		this.listTmpUsuarioResponsabilidad = listTmpUsuarioResponsabilidad;
	}

	public List<SelectItem> getListUsuarioResponsabilidad() {
		return listUsuarioResponsabilidad;
	}

	public void setListUsuarioResponsabilidad(List<SelectItem> listUsuarioResponsabilidad) {
		this.listUsuarioResponsabilidad = listUsuarioResponsabilidad;
	}

	public List<SelectItem> getListTmpUsuarios() {
		return listTmpUsuarios;
	}

	public void setListTmpUsuarios(List<SelectItem> listTmpUsuarios) {
		this.listTmpUsuarios = listTmpUsuarios;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}

	public UsuarioResponsabilidad getPojoUsuarioResponsabilidad() {
		return pojoUsuarioResponsabilidad;
	}

	public void setPojoUsuarioResponsabilidad(UsuarioResponsabilidad pojoUsuarioResponsabilidad) {
		this.pojoUsuarioResponsabilidad = pojoUsuarioResponsabilidad;
	}

	public List<Usuario> getListUsuariot() {
		return listUsuariot;
	}

	public void setListUsuariot(List<Usuario> listUsuariot) {
		this.listUsuariot = listUsuariot;
	}

	public List<UsuarioExt> getListUsuarios() {
		return listUsuarios;
	}

	public void setListUsuarios(List<UsuarioExt> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}

	/*public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}*/

	public List<SelectItem> getListResponsabilidad() {
		return listResponsabilidad;
	}

	public void setListResponsabilidad(List<SelectItem> listResponsabilidad) {
		this.listResponsabilidad = listResponsabilidad;
	}

	public List<UsuarioResponsabilidad> getListTmpsuarioResponsabilidad() {
		return listTmpsuarioResponsabilidad;
	}

	public void setListTmpsuarioResponsabilidad(List<UsuarioResponsabilidad> listTmpsuarioResponsabilidad) {
		this.listTmpsuarioResponsabilidad = listTmpsuarioResponsabilidad;
	}

	public List<Responsabilidad> getListTmpResponsabilidad() {
		return listTmpResponsabilidad;
	}

	public void setListTmpResponsabilidad(List<Responsabilidad> listTmpResponsabilidad) {
		this.listTmpResponsabilidad = listTmpResponsabilidad;
	}

	public UsuarioExt getPojoUsuarioExt() {
		return pojoUsuarioExt;
	}

	public void setPojoUsuarioExt(UsuarioExt pojoUsuarioExt) {
		this.pojoUsuarioExt = pojoUsuarioExt;
	}

	public boolean isRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(boolean registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public Empleado getPojoEmpleadoSeleccionado() {
		return pojoEmpleadoSeleccionado;
	}

	public void setPojoEmpleadoSeleccionado(Empleado pojoEmpleadoSeleccionado) {
		this.pojoEmpleadoSeleccionado = pojoEmpleadoSeleccionado;
	}

	public List<SelectItem> getEmpBusquedaTipos() {
		return empBusquedaTipos;
	}

	public void setEmpBusquedaTipos(List<SelectItem> empBusquedaTipos) {
		this.empBusquedaTipos = empBusquedaTipos;
	}

	public String getEmpBusquedaCampo() {
		return empBusquedaCampo;
	}

	public void setEmpBusquedaCampo(String empBusquedaCampo) {
		this.empBusquedaCampo = empBusquedaCampo;
	}

	public String getEmpBusquedaValor() {
		return empBusquedaValor;
	}

	public void setEmpBusquedaValor(String empBusquedaValor) {
		this.empBusquedaValor = empBusquedaValor;
	}

	public int getEmpBusquedaPagina() {
		return empBusquedaPagina;
	}

	public void setEmpBusquedaPagina(int empBusquedaPagina) {
		this.empBusquedaPagina = empBusquedaPagina;
	}
	
	public boolean isOperacion() {
		return operacion;
	}
	
	public void setOperacion(boolean operacion) {
		this.operacion = operacion;
	}
	
	public int getTipoMensaje() {
		return tipoMensaje;
	}
	
	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

	public boolean isUsuarioEmpleado() {
		return usuarioEmpleado;
	}

	public void setUsuarioEmpleado(boolean usuarioEmpleado) {
		this.usuarioEmpleado = usuarioEmpleado;
	}

	public List<Empleado> getListEmpleados() {
		return listEmpleados;
	}

	public void setListEmpleados(List<Empleado> listEmpleados) {
		this.listEmpleados = listEmpleados;
	}

	public boolean isUsuarioEmpleadoLock() {
		return usuarioEmpleadoLock;
	}

	public void setUsuarioEmpleadoLock(boolean usuarioEmpleadoLock) {
		this.usuarioEmpleadoLock = usuarioEmpleadoLock;
	}

	public List<SelectItem> getListEmpresasItems() {
		return listEmpresasItems;
	}

	public void setListEmpresasItems(List<SelectItem> listEmpresasItems) {
		this.listEmpresasItems = listEmpresasItems;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public List<EmpresasUsuarios> getListUsuarioEmpresas() {
		return listUsuarioEmpresas;
	}

	public void setListUsuarioEmpresas(List<EmpresasUsuarios> listUsuarioEmpresas) {
		this.listUsuarioEmpresas = listUsuarioEmpresas;
	}

	public EmpresasUsuarios getPojoUsuarioEmpresa() {
		return pojoUsuarioEmpresa;
	}

	public void setPojoUsuarioEmpresa(EmpresasUsuarios pojoUsuarioEmpresa) {
		this.pojoUsuarioEmpresa = pojoUsuarioEmpresa;
	}

	public int getNumUsuarioEmpresas() {
		return numUsuarioEmpresas;
	}

	public void setNumUsuarioEmpresas(int numUsuarioEmpresas) {
		this.numUsuarioEmpresas = numUsuarioEmpresas;
	}

	public LinkedHashMap<PerfilValor, String> getListPerfilValor() {
		return listPerfilValor;
	}

	public void setListPerfilValores(LinkedHashMap<PerfilValor, String> listPerfilValor) {
		this.listPerfilValor = listPerfilValor;
	}

	public List<SelectItem> getListPerfilValorItems() {
		return listPerfilValorItems;
	}

	public void setListPerfilValorItems(List<SelectItem> listPerfilValorItems) {
		this.listPerfilValorItems = listPerfilValorItems;
	}

	public List<SelectItem> getSesiones() {
		if (this.sessionesItems == null)
			this.sessionesItems = new ArrayList<SelectItem>();
		return sessionesItems;
	}

	public void setSesiones(List<SelectItem> sessionesItems) {
		this.sessionesItems = sessionesItems;
	}

	public List<String> getUsuarios() {
		if (this.usuarios == null)
			this.usuarios = new ArrayList<String>();
		return usuarios;
	}

	public void setUsuarios(List<String> usuarios) {
		this.usuarios = usuarios;
	}
	
	public boolean isDebugging() { 
		if (this.paramsRequest.containsKey("DEBUG")) 
			return true;
		return this.isDebug;
	}
	
	public void setDebugging(boolean value) { }
}
