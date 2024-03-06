package cde.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.beans.GrupoReportes;
import net.giro.plataforma.seguridad.beans.Menu;
import net.giro.plataforma.seguridad.beans.Responsabilidad;
import net.giro.publico.respuesta.Respuesta;



import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.*;

@KeepAlive
public class ResponsabilidadAction {
	private static Logger log = Logger.getLogger(ResponsabilidadAction.class);

	private AdministracionRem ifzAdministracion;
	private Responsabilidad pojoResponsabilidad;
	private Menu pojoMenu;
	private Aplicacion pojoAplicacion;
	private GrupoReportes pojoGpoReporte;
	@SuppressWarnings("unused")
	private InitialContext ctx; 
	private String valBusqueda;
	private String resOperacion;
	private String problemInesp;
	@SuppressWarnings("unused")
	private String sugAplicacion;
	private String sugMenu;
	private String invalidReg;
	private String tituloVentana;
	private String errResponsabilidad;
	private long usuarioId;
	private LoginManager loginManager;
	private String campoBusqueda;
	/*
	 * Indicador de numero de pagina en la que se encuentra el paginador del
	 * jtable de resposnabildides
	 */
	private int numPagina;
	//inicializacion de lista de tipo lista o array select item
	private List<Aplicacion> listAplicaciones;
	private List<Responsabilidad>listResponsabilidad;
	private ArrayList<SelectItem> listTmpResponsabilidad;
	private List<SelectItem> listTmpAplicaciones;
	private List<SelectItem> listMenus;
	private List<Menu> listTmpMenus;
	private String valTipoBusqueda;
	private String busquedaVacia;
	private ArrayList<String> tipoBusqueda;
	
	///Contructor del action aqui deben ir inicializados todos los atributos declarados arriba"si no la pantalla no se dibuja"
	public ResponsabilidadAction() throws  Exception {
		setTipoBusqueda(new ArrayList<String>());
		//variable de busqueda y numero de paginas
		numPagina=1;
		valTipoBusqueda="";
		campoBusqueda="";
		//pojo que utiliza el action
		pojoResponsabilidad= new Responsabilidad();
		pojoAplicacion=new Aplicacion();
		pojoMenu = new Menu();
		//list utilizados por la pantalla 
		listAplicaciones= new ArrayList<Aplicacion>();
		listTmpAplicaciones=new ArrayList<SelectItem>();
		listTmpMenus= new ArrayList<Menu>();
		listMenus = new ArrayList<SelectItem>();
		listMenus=new ArrayList<SelectItem>();
		listResponsabilidad=new ArrayList<Responsabilidad>();
		listTmpResponsabilidad=new ArrayList<SelectItem>();
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		//setInvalidReg(propPlataforma.getString("mensajes.validacion.invalidReg"));
		//setErrResponsabilidad(propPlataforma.getString("mensajes.error.responsabilidadUtilizada"));
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		InitialContext ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		ifzAdministracion = (AdministracionRem)ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
		ifzAdministracion.setInfoSesion(loginManager.getInfoSesion());
		
		cargarMenus();
		cargarSelectAplicaciones();
	}

	
	public void nuevo() {
		try {
			pojoResponsabilidad = new Responsabilidad();
			cargarMenus();
			cargarSelectAplicaciones();
			this.sugAplicacion = "";
			this.sugMenu = "";
			this.resOperacion = "";
			this.tituloVentana = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo nuevo.", e);
		}
	}

	public void eliminar() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzAdministracion.eliminar(pojoResponsabilidad);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listResponsabilidad.remove(pojoResponsabilidad);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminaFuncion", e);
		}
	}

	public void guardar() {
		try {
			this.resOperacion = "";
			long id = pojoResponsabilidad.getId();
			
			this.pojoResponsabilidad.setGrupoReporteId(1);
			
			Respuesta respuesta = this.ifzAdministracion.salvar(this.pojoResponsabilidad);
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoResponsabilidad = (Responsabilidad)respuesta.getBody().getValor("pojoResponsabilidad");
				if(id <= 0)
					this.listResponsabilidad.add(0,pojoResponsabilidad);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}	
	}

	@SuppressWarnings("unchecked")
	public void buscar() {
		this.resOperacion = "";	
		try {
			Respuesta respuesta = this.ifzAdministracion.buscarResponsabilidad(  this.campoBusqueda, this.valTipoBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.listResponsabilidad = (List<Responsabilidad>)respuesta.getBody().getValor("listResponsabilidades");
				if (this.listResponsabilidad != null && this.listResponsabilidad.isEmpty()) 
					this.resOperacion = busquedaVacia;
			} else
				this.resOperacion = respuesta.getErrores().getDescError();			
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo buscar", e);
		}

	}

	public void editar() {
		cargarMenus();
		cargarSelectAplicaciones();
	}

	@SuppressWarnings("unchecked")
	private void cargarMenus() {
		//listas para llenar selectone menu en pantalla
		try{
			Respuesta respuesta = ifzAdministracion.autocompletarMenu();
			if(respuesta.getErrores().getCodigoError() == 0L){
				listTmpMenus = (List<Menu>)respuesta.getBody().getValor("listMenus");
				
				Collections.sort(this.listTmpMenus, new Comparator<Menu>() {
				    	@Override
				        public int compare(Menu o1, Menu o2) {
				    		return o1.getMenu().compareTo(o2.getMenu());
				        }
				});
				
				listMenus.clear();
				for (Menu i : listTmpMenus) {
					listMenus.add(new SelectItem(i.getId(),i.getMenu()));
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al cargar menus",e);
		}
		
	}

	@SuppressWarnings("unchecked")
	private void cargarSelectAplicaciones(){
		try{
			Respuesta respuesta = ifzAdministracion.autocompletarAplicacion();
			if(respuesta.getErrores().getCodigoError() == 0L){
				listAplicaciones = (List<Aplicacion>) respuesta.getBody().getValor("listAplicaciones");
				
				Collections.sort(this.listAplicaciones, new Comparator<Aplicacion>() {
				    	@Override
				        public int compare(Aplicacion o1, Aplicacion o2) {
				    		return o1.getAplicacion().compareTo(o2.getAplicacion());
				        }
				});
				
				listTmpAplicaciones.clear();
				for (Aplicacion i : listAplicaciones) {
					listTmpAplicaciones.add(new SelectItem(i.getId(), i.getAplicacion()));
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al cargar aplicaciones", e);
		}
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setValBusqueda(String valBusqueda) {
		this.valBusqueda = valBusqueda;
	}

	public String getValBusqueda() {
		return valBusqueda;
	}

	public void setPojoResponsabilidad(Responsabilidad pojoResponsabilidad) {
		this.pojoResponsabilidad = pojoResponsabilidad;
		this.tituloVentana = "Responsabilidad "
				+ this.pojoResponsabilidad.getId() + " - "
				+ this.pojoResponsabilidad.getResponsabilidad();
		if (this.pojoResponsabilidad.getAplicacion() != null)
			this.sugAplicacion = this.pojoResponsabilidad.getAplicacion()
					.getAplicacion()
					+ " - "
					+ this.pojoResponsabilidad.getAplicacion()
							.getAplicacion();
		else
			this.sugAplicacion = "";

		if (this.pojoResponsabilidad.getMenu() != null)
			this.sugMenu = this.pojoResponsabilidad.getMenu().getId()
					+ " - " + this.pojoResponsabilidad.getMenu().getMenu();
		else
			this.sugMenu = "";

	}
	
	private Menu getMenuById(Long id, List<Menu> lista){
		for(Menu cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}

	public void setSuggMenu(Long menu) {
		this.pojoResponsabilidad.setMenu(getMenuById(menu, this.listTmpMenus));
	}

	public Long getSuggMenu() {
		return pojoResponsabilidad.getMenu() != null ? pojoResponsabilidad.getMenu().getId() : -1L;
	}

	private Aplicacion getAplicacionById(Long id, List<Aplicacion> lista){
		for(Aplicacion cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}

	public void setSuggAplicacion(Long aplicacion) {
		this.pojoResponsabilidad.setAplicacion(getAplicacionById(aplicacion, this.listAplicaciones));
	}

	public Long getSuggAplicacion() {
		return pojoResponsabilidad.getAplicacion() != null ? pojoResponsabilidad.getAplicacion().getId() : -1L;
	}
	
	public Responsabilidad getPojoResponsabilidad() {
		return pojoResponsabilidad;
	}

	public void setEsNuevo(boolean esNuevo) {/* no hace nada */
	}

	public void setResponsabilidad(String responsabilidad) {
		this.pojoResponsabilidad.setResponsabilidad(responsabilidad);
	}

	public String getResponsabilidad() {
		return this.pojoResponsabilidad.getResponsabilidad();
	}

	public void setDescripcion(String descripcion) {
		this.pojoResponsabilidad.setDescripcion(descripcion);
	}

	public String getDescripcion() {
		return this.pojoResponsabilidad.getDescripcion();
	}

	public void setSugMenu(String sugMenu) {
		this.sugMenu = sugMenu;
	}

	public String getSugMenu() {
		return sugMenu;
	}

	public String getTituloVentana() {
		return tituloVentana;
	}

	public void setTituloVentana(String tituloVentana) {
		this.tituloVentana = tituloVentana;
	}

	public GrupoReportes getPojoGpoReporte() {
		return pojoGpoReporte;
	}

	public void setPojoGpoReporte(GrupoReportes pojoGpoReporte) {
		this.pojoGpoReporte = pojoGpoReporte;
	}

	public List<SelectItem> getListMenus() {
		return listMenus;
	}

	public void setListMenus(List<SelectItem> listMenus) {
		this.listMenus = listMenus;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getErrResponsabilidad() {
		return errResponsabilidad;
	}

	public void setErrResponsabilidad(String errResponsabilidad) {
		this.errResponsabilidad = errResponsabilidad;
	}

	public List<Menu> getListTmpMenus() {
		return listTmpMenus;
	}

	public void setListTmpMenus(List<Menu> listTmpMenus) {
		this.listTmpMenus = listTmpMenus;
	}

	public Aplicacion getPojoAplicacion() {
		return pojoAplicacion;
	}

	public void setPojoAplicacion(Aplicacion pojoAplicacion) {
		this.pojoAplicacion = pojoAplicacion;
	}

	public Menu getPojoMenu() {
		return pojoMenu;
	}

	public void setPojoMenu(Menu pojoMenu) {
		this.pojoMenu = pojoMenu;
	}

	public String getInvalidReg() {
		return invalidReg;
	}

	public void setInvalidReg(String invalidReg) {
		this.invalidReg = invalidReg;
	}

	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}

	public String getProblemInesp() {
		return problemInesp;
	}

	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}

	public List<Aplicacion> getListAplicaciones() {
		return listAplicaciones;
	}

	public void setListAplicaciones(List<Aplicacion> listAplicaciones) {
		this.listAplicaciones = listAplicaciones;
	}

	public List<Responsabilidad> getListResponsabilidad() {
		return listResponsabilidad;
	}

	public void setListResponsabilidad(List<Responsabilidad> listResponsabilidad) {
		this.listResponsabilidad = listResponsabilidad;
	}

	public ArrayList<SelectItem> getListTmpResponsabilidad() {
		return listTmpResponsabilidad;
	}

	public void setListTmpResponsabilidad(ArrayList<SelectItem> listTmpResponsabilidad) {
		this.listTmpResponsabilidad = listTmpResponsabilidad;
	}

	public List<SelectItem> getListTmpAplicaciones() {
		return listTmpAplicaciones;
	}

	public void setListTmpAplicaciones(List<SelectItem> listTmpAplicaciones) {
		this.listTmpAplicaciones = listTmpAplicaciones;
	}

	public ArrayList<String> getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(ArrayList<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}
}
