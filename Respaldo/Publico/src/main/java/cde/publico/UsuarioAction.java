package cde.publico;


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
import net.giro.publico.respuesta.Respuesta;







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
	private int numPagina;
	private int numPaginaBuscarEmpleado;
	private int numPaginaNivelResponsabilidad;
	private String passWordVacio;
	private String idEmpleado;
	private String problemInesp;
	private String busquedaVacia;
	private String resOperacion;
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
	
	
	public UsuarioAction() throws Exception {
		valTipoBusqueda="";
		campoBusqueda="";

		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		
		busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");
		passWordVacio = propPlataforma.getString("mensaje.validacion.password");		
		malInicioResp = propPlataforma.getString("mensaje.validacion.fechaIniResponsabilidad");
		malFinResp = propPlataforma.getString("mensaje.fechaFinResponsabilidad");		
		vigenciaIncorrecta = propPlataforma.getString("mensaje.validacion.vigenciaUsuario");
		contrasenaDiferente = propPlataforma.getString("mensaje.error.contrasenaDiferente");
		vigenciaFechasResp = propPlataforma.getString("mensaje.validacion.vigenciaResponsabilidad");
		
		pojoUsuarioExt = new UsuarioExt();
		pojoUsuarioResponsabilidad = new UsuarioResponsabilidad();
		pojoResponsabilidad = new Responsabilidad();
		
		listTmpResponsabilidad = new ArrayList<Responsabilidad>();
		listUsuarios = new ArrayList<UsuarioExt>();
		listTmpUsuarioResponsabilidad = new ArrayList<UsuarioResponsabilidad>();
		listUsuariot= new ArrayList<Usuario>();
		
		listNivelAcceso = new ArrayList<SelectItem>();
		listResponsabilidad = new ArrayList<SelectItem>();
		listTmpUsuarios= new ArrayList<SelectItem>();
		listUsuarioResponsabilidad=new ArrayList<SelectItem>();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		this.ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		
		ifzAdministracion = (AdministracionRem)ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
		ifzAdministracion.setInfoSesion(loginManager.getInfoSesion());

		numPagina = 1;
		numPaginaBuscarEmpleado = 1;
		numPaginaNivelResponsabilidad = 1;
		
		cargarListaResponsabilidades();
	}

	public String nuevo() {
		try{
			resOperacion= "";
			pojoUsuarioExt = new UsuarioExt();
			listTmpUsuarioResponsabilidad.clear();
			idEmpleado = "";
			contrasena = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("error al nuevo", e);
			return "ERROR";
		}
		return "OK";
	}

	public void agregarResponsabilidadNivelAcceso() {
		cargarListaResponsabilidades();
		this.pojoUsuarioResponsabilidad = new UsuarioResponsabilidad();
	}

	@SuppressWarnings("unchecked")
	public void buscar() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzAdministracion.buscarUsuarioExt(this.campoBusqueda,this.valTipoBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.listUsuarios= (List<UsuarioExt>) respuesta.getBody().getValor("listUsuarios");
				if (this.listUsuarios != null && this.listUsuarios.isEmpty()) 
					this.resOperacion = busquedaVacia;
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
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
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargarResponsabilidadNivelAcceso", e);
		}
	}
	
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
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo validarContrasena", e);			
		}
	}

	public void guardar() {
		try {
			this.resOperacion = "";	
			long id = pojoUsuarioExt.getId();
			pojoUsuarioExt.setUsuario(pojoUsuarioExt.getUsuario().toUpperCase());
			
			if(this.pojoUsuarioExt.getFechaTerm().before(this.pojoUsuarioExt.getFechaIni())){
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
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}
	}
	
	public void guardarResponsabilidadNivel() {
		try {
			this.resOperacion = "";
			long id = pojoUsuarioResponsabilidad.getId();
	
			if(this.pojoUsuarioResponsabilidad.getFechaTerm().before(this.pojoUsuarioResponsabilidad.getFechaIni())){
				this.resOperacion = vigenciaFechasResp;
			}
			
			if(this.pojoUsuarioResponsabilidad.getFechaIni().before(this.pojoUsuarioExt.getFechaIni())){
				this.resOperacion = malInicioResp;
				return;
			}
			
			if(this.pojoUsuarioResponsabilidad.getFechaTerm().after(this.pojoUsuarioExt.getFechaTerm())){
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

	public void eliminar() {
		this.resOperacion = "No se pueden eliminar Usuarios";
		return;
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

	public String editar(){
		this.setRegistroSeleccionado(true);
		return "OK";
	}
	
	private Responsabilidad getResponsabilidadById(long id, List<Responsabilidad> lista){
		for(Responsabilidad responsabilidad :lista){
			if(responsabilidad.getId() == id)
				return responsabilidad;
		}
		return null;
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

	public void setpojoUsuarioResponsabilidad(
			UsuarioResponsabilidad pojoUsuarioResponsabilidad) {
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

	public LoginManager getLoginManager() {
		return loginManager;
	}

	public void setLoginManager(LoginManager loginManager) {
		this.loginManager = loginManager;
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
		cargarResponsabilidadNivelAcceso();
	}

	public boolean isRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(boolean registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}
}
