package cde.publico;

import java.util.ArrayList;

import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.beans.Perfil;
import net.giro.respuesta.Respuesta;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.*;

@KeepAlive
public class PerfilesAction {
	private static Logger log = Logger.getLogger(PerfilesAction.class);
	private LoginManager loginManager;
	private Context ctx; 
	private AdministracionRem ifzAdministracion;
	private String valTipoBusqueda;
	private String campoBusqueda;
	private List<Perfil>listPerfiles;
	private Perfil pojoPerfil;
	private Aplicacion pojoAplicacion;
	private String busquedaAplicacion;
	private String busquedaFuncion;
	private String tipoFuncion;
	private String resOperacion;
	private String problemInesp;
	private Long usuarioId;
	private int	numPagina;
	private boolean nuevoReg;
	private String[] tiposFunciones;
	private String busquedaVacia;
	private boolean registroSeleccionado;
	private boolean nivelTerminal;
	private boolean nivelUsuario;
    private boolean nivelResponsabilidad;
    //private boolean nivelPuesto;
    private boolean nivelSucursal;
    //private boolean nivelMenu;
    private boolean nivelEmpresa;
    private boolean nivelSitio;
	private String aplicacion;
	private String msgFechas;
	private List<Aplicacion> listAplicaciones;
	private ArrayList<SelectItem> listTmpAplicaciones;
	private ArrayList<SelectItem> listPerfil;
	private ArrayList<SelectItem> listTmpValores;
	
	
	public PerfilesAction() throws Exception {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		PropertyResourceBundle propPlataforma = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getInfoSesion().getAcceso().getUsuario().getId();
			
			this.ctx = this.loginManager.getCtx();
			this.ifzAdministracion =  (AdministracionRem) this.ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
			this.ifzAdministracion.setInfoSesion(this.loginManager.getInfoSesion());
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
			propPlataforma = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			this.problemInesp = propPlataforma.getString("mensaje.error.inesperado");
			
			this.numPagina = 1;
			this.pojoAplicacion = new Aplicacion();
			this.pojoPerfil = new Perfil();
			this.listTmpAplicaciones = new ArrayList<SelectItem>();
			this.listAplicaciones = new ArrayList<Aplicacion>();
			this.listPerfiles = new ArrayList<Perfil>();
			
			this.valTipoBusqueda = "";
			this.campoBusqueda = "";
			
			cargarSelectAplicaciones();
		} catch (Exception e) {
			log.error("No se pudo instanciar PerfilesAction", e);
		}
	}
	

	@SuppressWarnings("unchecked")
	public void buscar(){
		this.resOperacion = "";	
		try {
			Respuesta respuesta = this.ifzAdministracion.buscarPerfil(this.campoBusqueda, this.valTipoBusqueda);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				this.resOperacion = respuesta.getErrores().getDescError();
				return;
			}
			
			this.listPerfiles = (List<Perfil>) respuesta.getBody().getValor("listPerfiles");
			if (this.listPerfiles == null || this.listPerfiles.isEmpty()) 
				this.resOperacion = busquedaVacia;
		} catch (Exception e) {
			log.error("Error en el metodo buscar", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void nuevo() {
		try {
			resOperacion= "";
			pojoPerfil = new Perfil();
			this.nivelTerminal = false;
			this.nivelUsuario = false;
			this.nivelResponsabilidad = false;
			this.nivelSucursal = false; 
			this.nivelEmpresa = false;
			this.nivelSitio = false;
		} catch (Exception e) {
			log.error("error al nuevo", e);
			this.resOperacion = this.problemInesp;
		}
	}
	
	public void editar(){
		String niveles = null;
		try {
			niveles = ifzAdministracion.getNiveles(this.pojoPerfil.getId() > 0 ?  (int)this.pojoPerfil.getNivelPerfil() : 0);
			this.nivelTerminal = false;
			this.nivelUsuario = false;
			this.nivelResponsabilidad = false;
			//this.nivelPuesto = false;
			this.nivelSucursal = false; 
			this.nivelEmpresa = false;
			this.nivelSitio = false;
			
			for(String s:niveles.split(",")){
				switch(Integer.valueOf(s)){
				    case 1: this.nivelTerminal = true;break;
					case 2: this.nivelUsuario = true;break;
					case 4: this.nivelResponsabilidad = true;break;
					//case 8: this.nivelPuesto = true;break;
					case 16: this.nivelSucursal = true;break;
					case 32: this.nivelEmpresa = true;break;
					case 64: this.nivelSitio = true;break;
				}
			}
			
			this.setAplicacion(this.pojoPerfil.getAplicacion() != null ? this.pojoPerfil.getAplicacion().getId() + " - " + this.pojoPerfil.getAplicacion() : "");  
			
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("error al editar", e);
		}
	}
	
	public void guardar() {
		long numNivel = 0;
		long id = pojoPerfil.getId();
		this.resOperacion = "";
		try{
			numNivel += this.nivelTerminal	? 1 : 0;
			numNivel += this.nivelUsuario	? 2 : 0;
			numNivel += this.nivelResponsabilidad ? 4 : 0;
			//numNivel += this.nivelPuesto	? 8 : 0;
			numNivel += this.nivelSucursal		? 16 : 0;
			numNivel += this.nivelEmpresa	? 32 : 0;
			numNivel += this.nivelSitio		? 64 : 0;

			if(pojoPerfil.getValidoDesde().after(pojoPerfil.getValidoHasta())){
				resOperacion = msgFechas;
			} else{
				this.pojoPerfil.setNivelPerfil(numNivel);
				
				Respuesta respuesta = this.ifzAdministracion.salvar(this.pojoPerfil);
				if(respuesta.getErrores().getCodigoError() == 0L){
					this.pojoPerfil = (Perfil)respuesta.getBody().getValor("pojoPerfil");
					if(id <= 0L)
						this.listPerfiles.add(pojoPerfil);
					else{
						for(Perfil var:this.listPerfiles){
							if(var.getId() == this.pojoPerfil.getId())
								var = this.pojoPerfil;
						}
					}
				} else
					this.resOperacion = respuesta.getErrores().getDescError();
			}
		}catch(Exception e){
			this.resOperacion = this.problemInesp;
			log.error("error al guardar", e);
		}
	}

	public void eliminar(){
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzAdministracion.eliminar(pojoPerfil);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listPerfiles.remove(pojoPerfil);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminaPerfil", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarSelectAplicaciones(){
		Respuesta respuesta = ifzAdministracion.autocompletarAplicacion();
		if(respuesta.getErrores().getCodigoError() == 0L){
			listAplicaciones = (List<Aplicacion>)respuesta.getBody().getValor("listAplicaciones");
			listTmpAplicaciones.clear();
			for (Aplicacion i : listAplicaciones) {
				listTmpAplicaciones.add(new SelectItem(i.getId(), i.getAplicacion()));
			}
		} else
			this.resOperacion = respuesta.getErrores().getDescError();
	}
	
	private Aplicacion getAplicacionById(Long id, List<Aplicacion> lista){
		for(Aplicacion cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}
	
	// ------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------
	
	public void setSuggAplicacion(Long aplicacion) {
		this.pojoPerfil.setAplicacion(getAplicacionById(aplicacion, this.listAplicaciones));
	}

	public Long getSuggAplicacion() {
		return pojoPerfil.getAplicacion() != null ? pojoPerfil.getAplicacion().getId() : -1L;
	}
	
	public String getCampoBusqueda() {
		return campoBusqueda;
	}
	
	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public Perfil getpojoPerfil() {
		return pojoPerfil;
	}

	public void setpojoPerfil(Perfil pojoPerfil) {
		this.resOperacion = "";
		this.pojoPerfil = pojoPerfil;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setPerfilId(Long perfilId) {/*nanai*/}
	
	public Aplicacion getPojoAplicacion() {
		return pojoAplicacion;
	}
	
	public void setPojoAplicacion(Aplicacion pojoAplicacion) {
		this.pojoAplicacion = pojoAplicacion;
	}

	public List<Perfil> getListPerfiles() {
		return listPerfiles;
	}
	
	public void setListPerfiles(List<Perfil> listPerfiles) {
		this.listPerfiles = listPerfiles;
	}
	
	public Perfil getPojoPerfil() {
		return pojoPerfil;
	}
	
	public void setPojoPerfil(Perfil pojoPerfil) {
		this.pojoPerfil = pojoPerfil;
	}
	
	public String getBusquedaAplicacion() {
		return busquedaAplicacion;
	}
	
	public void setBusquedaAplicacion(String busquedaAplicacion) {
		this.busquedaAplicacion = busquedaAplicacion;
	}
	
	public String getBusquedaFuncion() {
		return busquedaFuncion;
	}
	
	public void setBusquedaFuncion(String busquedaFuncion) {
		this.busquedaFuncion = busquedaFuncion;
	}
	
	public String getTipoFuncion() {
		return tipoFuncion;
	}
	
	public void setTipoFuncion(String tipoFuncion) {
		this.tipoFuncion = tipoFuncion;
	}
	
	public String getProblemInesp() {
		return problemInesp;
	}
	
	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}
	
	public Long getUsuarioId() {
		return usuarioId;
	}
	
	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	public boolean isNuevoReg() {
		return nuevoReg;
	}
	
	public void setNuevoReg(boolean nuevoReg) {
		this.nuevoReg = nuevoReg;
	}
	
	public String[] getTiposFunciones() {
		return tiposFunciones;
	}
	
	public void setTiposFunciones(String[] tiposFunciones) {
		this.tiposFunciones = tiposFunciones;
	}
	
	public String getBusquedaVacia() {
		return busquedaVacia;
	}
	
	public void setBusquedaVacia(String busquedaVacia) {
		this.busquedaVacia = busquedaVacia;
	}
	
	public boolean isRegistroSeleccionado() {
		return registroSeleccionado;
	}
	
	public void setRegistroSeleccionado(boolean registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public String getAplicacion() {
		return aplicacion;
	}
	
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	
	public boolean isNivelTerminal() {
		return nivelTerminal;
	}
	
	public void setNivelTerminal(boolean nivelTerminal) {
		this.nivelTerminal = nivelTerminal;
	}
	
	public boolean isNivelUsuario() {
		return nivelUsuario;
	}
	
	public void setNivelUsuario(boolean nivelUsuario) {
		this.nivelUsuario = nivelUsuario;
	}
	
	public boolean isNivelResponsabilidad() {
		return nivelResponsabilidad;
	}
	
	public void setNivelResponsabilidad(boolean nivelResponsabilidad) {
		this.nivelResponsabilidad = nivelResponsabilidad;
	}
	
	/*public boolean isNivelPuesto() {
		return nivelPuesto;
	}
	
	public void setNivelPuesto(boolean nivelPuesto) {
		this.nivelPuesto = nivelPuesto;
	}*/
	
	public boolean isNivelSucursal() {
		return nivelSucursal;
	}
	
	public void setNivelSucursal(boolean nivelArea) {
		this.nivelSucursal = nivelArea;
	}
	
	public boolean isNivelEmpresa() {
		return nivelEmpresa;
	}
	
	public void setNivelEmpresa(boolean nivelEmpresa) {
		this.nivelEmpresa = nivelEmpresa;
	}
	
	public boolean isNivelSitio() {
		return nivelSitio;
	}
	
	public void setNivelSitio(boolean nivelSitio) {
		this.nivelSitio = nivelSitio;
	}
	
	public String getMsgFechas() {
		return msgFechas;
	}
	
	public void setMsgFechas(String msgFechas) {
		this.msgFechas = msgFechas;
	}

	public ArrayList<SelectItem> getListPerfil() {
		return listPerfil;
	}
	
	public void setListPerfil(ArrayList<SelectItem> listPerfil) {
		this.listPerfil = listPerfil;
	}
	
	public AdministracionRem getIfzAdministracion() {
		return ifzAdministracion;
	}
	
	public void setIfzAdministracion(AdministracionRem ifzAdministracion) {
		this.ifzAdministracion = ifzAdministracion;
	}
	
	public List<Perfil> getlistPerfiles() {
		return listPerfiles;
	}
	
	public void setlistPerfiles(List<Perfil> listPerfiles) {
		this.listPerfiles = listPerfiles;
	}
	
	public ArrayList<SelectItem> getListTmpValores() {
		return listTmpValores;
	}
	
	public void setListTmpValores(ArrayList<SelectItem> listTmpValores) {
		this.listTmpValores = listTmpValores;
	}
	
	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}
	
	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}
	
	public List<Aplicacion> getListAplicaciones() {
		return listAplicaciones;
	}
	
	public void setListAplicaciones(List<Aplicacion> listAplicaciones) {
		this.listAplicaciones = listAplicaciones;
	}
	
	public ArrayList<SelectItem> getListTmpAplicaciones() {
		return listTmpAplicaciones;
	}
	
	public void setListTmpAplicaciones(ArrayList<SelectItem> listTmpAplicaciones) {
		this.listTmpAplicaciones = listTmpAplicaciones;
	}
}
