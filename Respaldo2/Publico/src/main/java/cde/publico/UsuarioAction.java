package cde.publico;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;  

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.MD5;
import net.giro.plataforma.beans.UsuarioExt;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.plataforma.seguridad.beans.Responsabilidad;
import net.giro.plataforma.seguridad.beans.Usuario;
import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;
import net.giro.respuesta.Respuesta;

import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;

@KeepAlive
public class UsuarioAction  {
	private static Logger log = Logger.getLogger(UsuarioAction.class);
	private LoginManager loginManager;
	private Context ctx;
	
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
	private Long usuarioId;
	private String usuario;
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
	
	
	public UsuarioAction() throws Exception {
		FacesContext fc = null;
		Application app = null;
		PropertyResourceBundle propPlataforma = null;
		ValueExpression valExp = null;

		// Contexto de app
		fc = FacesContext.getCurrentInstance();
		app = fc.getApplication();
		
		// Login Manager
		valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
		this.usuarioId = this.loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		this.usuario   = this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario();

		// EJB's
		this.ctx = loginManager.getCtx();
		this.ifzAdministracion = (AdministracionRem) this.ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
		this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
		
		this.ifzAdministracion.setInfoSesion(this.loginManager.getInfoSesion());

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
		
		// Inicializaciones
		this.pojoUsuarioExt = new UsuarioExt();
		this.pojoUsuarioResponsabilidad = new UsuarioResponsabilidad();
		this.pojoResponsabilidad = new Responsabilidad();
		
		this.listTmpResponsabilidad = new ArrayList<Responsabilidad>();
		this.listUsuarios = new ArrayList<UsuarioExt>();
		this.listTmpUsuarioResponsabilidad = new ArrayList<UsuarioResponsabilidad>();
		this.listUsuariot= new ArrayList<Usuario>();
		
		this.listNivelAcceso = new ArrayList<SelectItem>();
		this.listResponsabilidad = new ArrayList<SelectItem>();
		this.listTmpUsuarios= new ArrayList<SelectItem>();
		this.listUsuarioResponsabilidad=new ArrayList<SelectItem>();
		
		this.valTipoBusqueda="";
		this.campoBusqueda="";

		this.numPagina = 1;
		this.numPaginaBuscarEmpleado = 1;
		this.numPaginaNivelResponsabilidad = 1;

		// Busqueda Empleados
		this.empBusquedaTipos = new ArrayList<SelectItem>();
		this.empBusquedaTipos.add(new SelectItem("nombre", "Nombre"));
		this.empBusquedaTipos.add(new SelectItem("clave", "Clave"));
		this.empBusquedaTipos.add(new SelectItem("id", "ID"));
		this.empBusquedaCampo = this.empBusquedaTipos.get(0).getValue().toString();
		this.empBusquedaValor = "";
		this.empBusquedaPagina = 1;
		
		cargarListaResponsabilidades();
	}


	@SuppressWarnings("unchecked")
	public void buscar() {
		Respuesta respuesta = null;
		
		try {
			control();
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
			this.resOperacion= "";
			this.pojoUsuarioExt = new UsuarioExt();
			if (this.listTmpUsuarioResponsabilidad == null)
				this.listTmpUsuarioResponsabilidad = new ArrayList<UsuarioResponsabilidad>();
			this.listTmpUsuarioResponsabilidad.clear();
			this.usuarioEmpleado = false;
			this.usuarioEmpleadoLock = false;
			this.idEmpleado = "";
			this.contrasena = "";
		} catch (Exception e) {
			log.error("error al nuevo", e);
			this.resOperacion = this.problemInesp;
		}
	}

	public void editar() {
		control();
		// Inicializamos
		this.registroSeleccionado = true;
		this.usuarioEmpleadoLock = false;
		this.usuarioEmpleado = false;
		if (this.pojoUsuarioExt.getIdEmpleado() != null && this.pojoUsuarioExt.getIdEmpleado() > 1L) {
			this.usuarioEmpleado = true;
			this.usuarioEmpleadoLock = true;
		}
		
		// Cargamos responsabilidades
		cargarResponsabilidadNivelAcceso();
	}
	
	public void guardar() {
		try {
			this.resOperacion = "";	
			long id = pojoUsuarioExt.getId();
			pojoUsuarioExt.setUsuario(pojoUsuarioExt.getUsuario().toUpperCase());
			
			if(this.pojoUsuarioExt.getFechaTerminacion().before(this.pojoUsuarioExt.getFechaInicio())){
				this.resOperacion = vigenciaIncorrecta;
				return;
			}

			this.pojoUsuarioExt.setUltimoAcceso(Calendar.getInstance().getTime());
			this.pojoUsuarioExt.setFechaCambio(Calendar.getInstance().getTime());
			if(pojoUsuarioExt.getPassword() == null)
				this.pojoUsuarioExt.setPassword(".....");
			if(id == 0L)
				this.pojoUsuarioExt.setExpirado(1L);
			
			Respuesta respuesta = this.ifzAdministracion.salvar(this.pojoUsuarioExt);
			if (respuesta.getErrores().getCodigoError() == 0L) {
				pojoUsuarioExt = (UsuarioExt) respuesta.getBody().getValor("pojoUsuario");
				if(id == 0L)
					this.listUsuarios.add(0, pojoUsuarioExt);
			}  else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error en el metodo guardar", e);
			this.resOperacion = this.problemInesp;
		}
	}

	public void eliminar() {
		control("No se pueden eliminar Usuarios", null);
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
	
	private void control() {
		control(false, 0, "", null);
	}

	private void control(int value, String mensaje) { 
		control(true, value, mensaje, null); 
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, 1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";
		
		codigo = "Codigo: " + formatter.format(Calendar.getInstance().getTime());
		if (operacionCancelada && (mensaje == null || "".equals(mensaje.trim())))
			mensaje = "ERROR";

		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.resOperacion = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.resOperacion;
			this.mensajeDetalles += "<br><br>" + sw.toString();
			log.error(this.usuario + " :: " + codigo + " :: " + mensaje, throwable);
		}
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
	// BUSQUEDA EMPLEADOS
	// --------------------------------------------------------------------------------------------------------------------------------
	
	public void busquedaEmpleados() {
		if (this.listEmpleados == null)
			this.listEmpleados = new ArrayList<Empleado>();
		this.listEmpleados.clear();
		this.pojoEmpleadoSeleccionado = null;
		
		this.empBusquedaCampo = this.empBusquedaTipos.get(0).getValue().toString();
		this.empBusquedaValor = "";
		this.empBusquedaPagina = 1;
	}
	
	public void buscarEmpleados() {
		try {
    		control();
			this.listEmpleados = this.ifzEmpleados.findLike(this.empBusquedaValor, "nombre", 0);
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
	// PROPIEDADES
	// --------------------------------------------------------------------------------------------------------------------------------

	public String getNombre() {
		if (this.pojoUsuarioExt.getNombre() != null) {
			if (this.pojoUsuarioExt.getIdEmpleado() != null && this.pojoUsuarioExt.getIdEmpleado() > 1L)
				return this.pojoUsuarioExt.getIdEmpleado() + " - " + this.pojoUsuarioExt.getNombre();
			else
				return this.pojoUsuarioExt.getNombre();
		}
		
		return "";
	}
	
	public void setNombre(String value) {}

	public String getCorreo() {
		if (this.pojoUsuarioExt.getCorreo() != null)
			return this.pojoUsuarioExt.getCorreo();
		return "";
	}
	
	public void setCorreo(String value) {}
	
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

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

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
}
