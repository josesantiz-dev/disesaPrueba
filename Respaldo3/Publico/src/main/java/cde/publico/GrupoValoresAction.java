package cde.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.security.auth.login.LoginException;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.respuesta.Respuesta;


import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.*;

@KeepAlive
public class GrupoValoresAction {
	private static Logger log = Logger.getLogger(GrupoValoresAction.class);
	private AdministracionRem ifzAdministracion;
	private List<ConGrupoValores> listConGpoVal;
	private List<SelectItem> listTipoEtiqueta;
	private int numPagina;
	private Long usuarioId; 
	private String sucursalesVisibles;
	private String resOperacion;
	private String campoBusqueda;		
	private String errInesperado;
	private String busquedaVacia;
	private LoginManager loginManager;
	private String problemInesp;
	private ConGrupoValores pojoConGpoVal;
	private String valTipoBusqueda;
	
	public GrupoValoresAction() throws NamingException, LoginException{
		//ctx = new InitialContext();
		
		valTipoBusqueda="";
		campoBusqueda="";
		pojoConGpoVal = new ConGrupoValores();
		listConGpoVal = new ArrayList<ConGrupoValores>();
		
		
		listTipoEtiqueta = new ArrayList<SelectItem>();
		listTipoEtiqueta.add(new SelectItem("input", "inputText"));
		listTipoEtiqueta.add(new SelectItem("combo", "ComboBox"));
		listTipoEtiqueta.add(new SelectItem("date", "DateTime"));
		listTipoEtiqueta.add(new SelectItem("check", "CheckBox"));
		
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		setProblemInesp(propPlataforma.getString("mensaje.error.inesperado"));
		

		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		InitialContext ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();

		ifzAdministracion = (AdministracionRem)ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
		ifzAdministracion.setInfoSesion(loginManager.getInfoSesion());
		
		numPagina = 1;
		resOperacion = "";		
		
	}
	
	@SuppressWarnings("unchecked")
	public void buscar(){
		this.resOperacion = "";
		
		try{
			Respuesta respuesta = this.ifzAdministracion.buscarConGroVal( this.campoBusqueda,this.valTipoBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.listConGpoVal = (List<ConGrupoValores>)respuesta.getBody().getValor("listGrupoValores");
				if (this.listConGpoVal != null && this.listConGpoVal.isEmpty()) 
					this.resOperacion = busquedaVacia;
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
	
	public void nuevo (){
		try{			
			this.pojoConGpoVal = new ConGrupoValores();
			resOperacion = "";
		}catch(Exception e){
			log.error("Error en el metodo nuevo.", e);
		}
	}
	
	public void guardar (){
		try{
			this.resOperacion = "";
			long id = pojoConGpoVal.getId();
			Respuesta respuesta = this.ifzAdministracion.salvar(this.pojoConGpoVal);
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoConGpoVal = (ConGrupoValores)respuesta.getBody().getValor("pojoGrupoValores");
				if(id == 0)
					listConGpoVal.add(this.pojoConGpoVal);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		}catch(Exception e){
			this.resOperacion = this.errInesperado;
			log.error("error al guardar", e);
		}
	}
	
	
	public void eliminar(){
		
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzAdministracion.eliminar(this.pojoConGpoVal);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listConGpoVal.remove(this.pojoConGpoVal);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar.", e);
		}
	}

	public ConGrupoValores getPojoConGpoVal() {
		return pojoConGpoVal;
	}
	
	public void setPojoConGpoVal(ConGrupoValores pojoConGpoVal) {
		this.pojoConGpoVal = pojoConGpoVal;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getSucursalesVisibles() {
		return sucursalesVisibles;
	}

	public void setSucursalesVisibles(String sucursalesVisibles) {
		this.sucursalesVisibles = sucursalesVisibles;
	}

	
	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public List<ConGrupoValores> getListConGpoVal() {
		return listConGpoVal;
	}

	public void setListConGpoVal(List<ConGrupoValores> listConGpoVal) {
		this.listConGpoVal = listConGpoVal;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}


	public void setListTipoEtiqueta(List<SelectItem> listTipoEtiqueta) {
		this.listTipoEtiqueta = listTipoEtiqueta;
	}

	public List<SelectItem> getListTipoEtiqueta() {
		return listTipoEtiqueta;
	}

	public String getProblemInesp() {
		return problemInesp;
	}

	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}

	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}	

}
