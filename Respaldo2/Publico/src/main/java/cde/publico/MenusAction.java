package cde.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.navegador.comun.NodoMenuFuncion;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.beans.Funcion;
import net.giro.plataforma.seguridad.beans.Menu;
import net.giro.plataforma.seguridad.beans.MenuFuncion;
import net.giro.respuesta.Respuesta;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;
import org.richfaces.model.TreeNodeImpl;

@KeepAlive
public class MenusAction {
	Logger log = Logger.getLogger(MenusAction.class);
	private LoginManager loginManager;
	private Context ctx;

	private AdministracionRem 	ifzAdministracion;
	private List<Aplicacion>	listAplicacion;
	private List<Menu>			listMenus;
	private List<SelectItem>	listTmpFunciones;
	private List<NodoMenuFuncion>	listEliminar;
	private NodoMenuFuncion rootMenu;
	private NodoMenuFuncion nodoActual;
	private Menu pojoMenu;
	//private Aplicacion pojoAplicacion;
	// Busqueda Menus
	private List<Menu> listMenusSelec;
	private Menu pojoMenuSelect;
	private String busquedaMenu;
	// Busqueda Funciones
	private ArrayList<SelectItem> listTmpAplicacion;
	private List<Funcion> listFunciones;
	private Funcion pojoFuncion;
	private String busquedaFuncion;
	
	private String busquedaNombre;
	private String cambiaNombre;
	private String resOperacion;
	private String errMenuRequ;
	private long idTmp;
	private int numPagina;
	private int numPaginaMenu;
	private int numPaginaFuncion;
	private Pattern pat = null;
	private Matcher match = null;
	private String problemInesp;
	private String campoBusqueda;
	private String valTipoBusqueda;
	
	
	public MenusAction() throws Exception {
		this.ctx = new InitialContext();
		numPagina=1;
		numPaginaFuncion=1;
		numPaginaMenu=1;
		idTmp=1L;
		
		problemInesp="";
		busquedaFuncion="";
		busquedaMenu="";
		busquedaNombre="";
		cambiaNombre="";
		valTipoBusqueda="";
		campoBusqueda="";
		
		pojoMenu= new Menu();
		//pojoAplicacion= new Aplicacion();
		
		listMenus=new ArrayList<Menu>();
		listFunciones=new ArrayList<Funcion>();
		listMenusSelec=new ArrayList<Menu>();
		listAplicacion=new ArrayList<Aplicacion>();
		listTmpAplicacion=new ArrayList<SelectItem>();
		
		setListMenus(new ArrayList<Menu>());
		this.setListEliminar(new ArrayList<NodoMenuFuncion>());
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");

		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		
		ifzAdministracion = (AdministracionRem)ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
		ifzAdministracion.setInfoSesion(loginManager.getInfoSesion());
		
		//InitialContext ctx = loginManager.getCtx();
		//usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		//PropertyResourceBundle prop = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		//errMenuRequ = prop.getString("mensajes.error.menuUtilizadoPorResponsabilidad");
		
		cargarSelectAplicaciones();
		nuevo();
	}


	public void nuevo(){
		this.rootMenu = new NodoMenuFuncion();
		this.rootMenu.setListSubMenus(new ArrayList<NodoMenuFuncion>());
		this.pojoMenu = new Menu();
		cargarSelectAplicaciones();
		idTmp = -1L;
		numPagina = 1;
		numPaginaMenu = 1;
		numPaginaFuncion = 1;
		resOperacion = "";
	}
	
	@SuppressWarnings("unchecked")
	public void buscar() {
		try {
			this.resOperacion="";
			Respuesta respuesta = this.ifzAdministracion.buscarMenu(this.campoBusqueda, this.valTipoBusqueda);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				this.resOperacion = respuesta.getErrores().getDescError();
				return;
			}

			this.listMenus = (List<Menu>) respuesta.getBody().getValor("listMenus");
			Collections.sort(this.listMenus, new Comparator<Menu>() {
				@Override
				public int compare(Menu o1, Menu o2) {
					return o1.getMenu().compareTo(o2.getMenu());
				}
			});
		} catch (Exception e) {
			log.error("Error al buscar ", e);
			this.resOperacion = this.problemInesp;
		}
	}
	
	public void editar() {
		this.listEliminar.clear();
		this.rootMenu = new NodoMenuFuncion();
		this.rootMenu.setPromt(pojoMenu.getMenu());
		this.rootMenu.setTipoId(pojoMenu.getId());
		this.rootMenu.setId(getIdTmp());
		this.rootMenu.setListSubMenus(getMenu(this.rootMenu));
	}
	
	public void eliminar(){
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzAdministracion.eliminar(pojoMenu);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listMenus.remove(pojoMenu);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminaFuncion", e);
		}
	}
	
	public void guardar(){
		MenuFuncion mf = null;
		
		try {
			this.resOperacion = "";
			long id = pojoMenu.getId();
			
			//this.pojoMenu.setAplicaciones(pojoAplicacion);
			Respuesta respuesta = this.ifzAdministracion.salvar(this.pojoMenu);
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.pojoMenu = (Menu)respuesta.getBody().getValor("pojoMenu");
				if(id <= 0)
					this.listMenus.add(this.pojoMenu);
				
				for(NodoMenuFuncion nodo : rootMenu.getListSubMenus()){
					if(nodo.getId() < 0){
						mf = new MenuFuncion();
						mf.setMenu(pojoMenu);
						mf.setPrompt(nodo.getPromt());
						mf.setPromptId(nodo.getNivel());
						mf.setDescripcion(nodo.getDescripcion());
						mf.setTipoAccion(nodo.getListSubMenus()==null || nodo.getListSubMenus().isEmpty() ? "F":"M");
						mf.setTipoId(nodo.getTipoId());
						
						respuesta = ifzAdministracion.salvar(mf);
						if(respuesta.getErrores().getCodigoError() == 0L){
							mf = (MenuFuncion)respuesta.getBody().getValor("pojoMenuFuncion");
							nodo.setId(mf.getId());
						} else
							this.resOperacion = respuesta.getErrores().getDescError();
					} else {
						respuesta = ifzAdministracion.findMenuFuncionById(nodo.getId());
						if(respuesta.getErrores().getCodigoError() == 0L) {
							mf = (MenuFuncion)respuesta.getBody().getValor("pojoMenuFuncion");
							mf.setPrompt(nodo.getPromt());
							respuesta = this.ifzAdministracion.salvar(mf);
							if(respuesta.getErrores().getCodigoError() > 0L)
								this.resOperacion = respuesta.getErrores().getDescError();
						} else
							this.resOperacion = respuesta.getErrores().getDescError();
						
					}
				}
				
				mf = new MenuFuncion();
				for(NodoMenuFuncion n : this.listEliminar) {
					if(n.getId()>0){
						mf.setId(n.getId());
						respuesta = ifzAdministracion.eliminar(mf);
						if(respuesta.getErrores().getCodigoError() > 0L)
							this.resOperacion = respuesta.getErrores().getDescError();
					}
				}
				listEliminar.clear();
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			resOperacion = problemInesp;
			log.error("Error en metodo guardar ", e);
		}
	}

	/*@SuppressWarnings("unchecked")
	public void buscarAplicacion(){
			try {
			resOperacion = "";
			Respuesta respuesta = ifzAdministracion.buscarAplicacion( this.campoBusqueda, this.valTipoBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0L)
				listAplicacion = (List<Aplicacion>)respuesta.getBody().getValor("listAplicaciones");
			else
				this.resOperacion = respuesta.getErrores().getDescError();
			
			Collections.sort(this.listAplicacion, new Comparator<Aplicacion>() {
				@Override
				public int compare(Aplicacion o1, Aplicacion o2) {
					return o1.getAplicacion().compareTo(o2.getAplicacion());
				}
			});
		} catch (Exception e) {
			resOperacion = problemInesp;
			log.error("Error al buscarAplicacion ", e);
		}
	}*/
	
	@SuppressWarnings("unchecked")
	private List<NodoMenuFuncion> getMenu(NodoMenuFuncion root){
		List<NodoMenuFuncion>	res = new ArrayList<NodoMenuFuncion>();
		List<MenuFuncion>	listGrpRep = null;
		NodoMenuFuncion nodo = null;
		Funcion pantalla = null;
		try {
			this.resOperacion = "";
			Respuesta respuesta = ifzAdministracion.findMenuFuncionByMenuId(root.getTipoId());
			if(respuesta.getErrores().getCodigoError() == 0L){
				listGrpRep = (List<MenuFuncion>)respuesta.getBody().getValor("listMenuFuncion");
				if(!listGrpRep.isEmpty()){
					for(MenuFuncion mf : listGrpRep){
						nodo = new NodoMenuFuncion();
						nodo.setPromt(mf.getPrompt());
						nodo.setDescripcion(mf.getDescripcion());
						nodo.setTipoAccion(mf.getTipoAccion());
						nodo.setActepDrop("M");
						nodo.setId(mf.getTipoId());
						nodo.setMenuVisible(true);
						nodo.setTipoId(mf.getTipoId());
						nodo.setId(mf.getId());
						if("M".equals(nodo.getTipoAccion())){
							nodo.setIcono(3L);
							nodo.setListSubMenus(getMenu(nodo));
						}else{
							pantalla = ifzAdministracion.findFuncionById(mf.getTipoId());
							if(pantalla != null){
								nodo.setUrl(pantalla.getForma());
								nodo.setIcono(pantalla.getIcono() != null ? pantalla.getIcono() : 3);
							} else {
								nodo.setUrl(mf.getPrompt() + " No existe");
								nodo.setIcono(3L); 
							}
							
						}
						
						res.add(nodo);
					}
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch(Exception e){
			resOperacion = problemInesp;
			log.error("Error en el metodo getMenu", e);
		}
		return res;
	}

	public TreeNodeImpl<NodoMenuFuncion> getArbolMenu(){
		TreeNodeImpl<NodoMenuFuncion> root = new TreeNodeImpl<NodoMenuFuncion>();
		root.setData(this.rootMenu);
		construyeArbol(root);
		
		return root;
	}
	
	private void construyeArbol(TreeNodeImpl<NodoMenuFuncion> root){
		int idx = 0;
		if(root.getData().getListSubMenus() == null)
			return ;
		
		for(NodoMenuFuncion p:root.getData().getListSubMenus()){
			TreeNodeImpl<NodoMenuFuncion> hijo = new TreeNodeImpl<NodoMenuFuncion>();
			hijo.setData(p);
			if(p.getListSubMenus() != null && !p.getListSubMenus().isEmpty())
				construyeArbol(hijo);
			
			root.addChild(idx, hijo);
			idx++;
		}
	}

	public void eliminarNodoSelect(){
		try {
			for(NodoMenuFuncion ngr : this.rootMenu.getListSubMenus()){
				if(ngr.getId().intValue() == this.nodoActual.getId().intValue()){
					listEliminar.add(ngr);
					rootMenu.getListSubMenus().remove(ngr);
					break;
				}
			}
			resOperacion = "";
		} catch (Exception e) {
			resOperacion = problemInesp;
			log.error("Error en metodo eliminarNodoSelect ", e);
		}
		
	}
	
	public void editarNodo(){
		try {
			this.cambiaNombre = this.nodoActual.getPromt();
			resOperacion = "";
		} catch (Exception e) {
			resOperacion = problemInesp;
			log.error("Error en metodo editarNodo", e);
		}
		
	}
	
	public void renombrar(){
		this.nodoActual.setPromt(this.cambiaNombre);
	}

	private Aplicacion getAplicacionById(Long id, List<Aplicacion> lista){
		for(Aplicacion cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void cargarSelectAplicaciones(){
		Respuesta respuesta = this.ifzAdministracion.autocompletarAplicacion();
		if(respuesta.getErrores().getCodigoError() == 0L){
			this.listAplicacion = (List<Aplicacion>)respuesta.getBody().getValor("listAplicaciones");

			Collections.sort(this.listAplicacion, new Comparator<Aplicacion>() {
			    	@Override
			        public int compare(Aplicacion o1, Aplicacion o2) {
			    		return o1.getAplicacion().compareTo(o2.getAplicacion());
			        }
			});
			
			this.listTmpAplicacion.clear();
			for (Aplicacion i : listAplicacion) {
				this.listTmpAplicacion.add(new SelectItem(i.getId(), i.getAplicacion()));
			}
		} else
			this.resOperacion = respuesta.getErrores().getDescError();
	}

	// --------------------------------------------------------------------------------------
	// BUSQUEDA MENUS
	// --------------------------------------------------------------------------------------

	public void busquedaMenus() {
		this.busquedaMenu = "";
		if (this.listMenusSelec == null)
			this.listMenusSelec = new ArrayList<Menu>();
		this.listMenusSelec.clear();
		this.pojoMenuSelect = null;
	}
	
	@SuppressWarnings("unchecked")
	public void buscarMenus() {
		try {
			this.resOperacion="";
			Respuesta respuesta = this.ifzAdministracion.buscarMenu("menu", this.busquedaMenu);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				this.resOperacion = respuesta.getErrores().getDescError();
				return;
			}

			this.listMenusSelec = (List<Menu>) respuesta.getBody().getValor("listMenus");
			Collections.sort(this.listMenusSelec, new Comparator<Menu>() {
				@Override
				public int compare(Menu o1, Menu o2) {
					return o1.getMenu().compareTo(o2.getMenu());
				}
			});
		} catch (Exception e) {
			log.error("Error al buscarMenus ", e);
			resOperacion = problemInesp;
		}
	}
	
	public void seleccionarMenu() {
		if (this.pojoMenuSelect == null || this.pojoMenuSelect.getId() <= 0L)
			return;
		
		NodoMenuFuncion nodo = new NodoMenuFuncion(this.getIdTmp());
		nodo.setPromt(this.pojoMenuSelect.getMenu());
		nodo.setDescripcion(this.pojoMenuSelect.getDescripcion());
		nodo.setTipoAccion("M");
		nodo.setActepDrop("N");
		nodo.setIcono(3L);
		nodo.setFuncionId(this.pojoMenuSelect.getId());
		nodo.setTipoId(this.pojoMenuSelect.getId());
		nodo.setMenuVisible(true);
		nodo.setId(getIdTmp());
		nodo.setListSubMenus(getMenu(nodo));
		
		this.rootMenu.getListSubMenus().add(nodo);
	}

	// --------------------------------------------------------------------------------------
	// BUSQUEDA FUNCIONES
	// --------------------------------------------------------------------------------------

	public void busquedaFunciones() {
		this.busquedaFuncion = "";
		this.pojoFuncion = null;
	}

	@SuppressWarnings("unchecked")
	public void buscarFuncion() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzAdministracion.findLikeNombreAplicacion(this.busquedaFuncion, this.pojoMenu.getAplicaciones(), 100);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listFunciones = (List<Funcion>)respuesta.getBody().getValor("listFunciones");
			else
				this.resOperacion = respuesta.getErrores().getDescError();
			
			Collections.sort(this.listFunciones, new Comparator<Funcion>() {
				@Override
				public int compare(Funcion o1, Funcion o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
		} catch (Exception e) {
			log.error("Error al buscarFuncion ", e);
			resOperacion = problemInesp;
		}
	}
	
	public void seleccionarFuncion() {
		if (this.pojoFuncion == null || this.pojoFuncion.getId() <= 0L)
			return;
		
		NodoMenuFuncion nodo = new NodoMenuFuncion(this.getIdTmp());
		nodo.setPromt(this.pojoFuncion.getNombre());
		nodo.setDescripcion(this.pojoFuncion.getDescripcion());
		nodo.setUrl(this.pojoFuncion.getForma());
		nodo.setTipoAccion("F");
		nodo.setActepDrop("M");
		nodo.setIcono(this.pojoFuncion.getIcono() != null ? this.pojoFuncion.getIcono() : 3);
		nodo.setId(this.pojoFuncion.getId());
		nodo.setMenuVisible(true);
		nodo.setTipoId(this.pojoFuncion.getId());
		nodo.setId(getIdTmp());
		this.rootMenu.getListSubMenus().add(nodo);
	}
	
	// --------------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------------

	public Menu getPojoMenu() {
		return pojoMenu;
	}
	
	public void setPojoMenu(Menu pojoMenu) {
		this.pojoMenu = pojoMenu;
	}

	public Menu getPojoMenuSelect() {
		return new Menu();
	}
	
	public void setPojoMenuSelect(Menu menuSelect) {
		this.pojoMenuSelect = menuSelect;
		/*NodoMenuFuncion nodo = new NodoMenuFuncion(this.getIdTmp());
		nodo.setPromt(menuSelect.getMenu());
		nodo.setDescripcion(menuSelect.getDescripcion());
		nodo.setTipoAccion("M");
		nodo.setActepDrop("N");
		nodo.setIcono(3L);
		nodo.setFuncionId(menuSelect.getId());
		nodo.setTipoId(menuSelect.getId());
		nodo.setMenuVisible(true);
		nodo.setId(getIdTmp());
		nodo.setListSubMenus(getMenu(nodo));
		rootMenu.getListSubMenus().add(nodo);*/
	}

	public void setSuggAplicacion(Long aplicacion) {
		if (this.pojoMenu != null && this.pojoMenu.getAplicaciones() != null) {
			if (this.pojoMenu.getAplicaciones().getId() == aplicacion.longValue())
				return;
		}
		
		this.pojoMenu.setAplicaciones(getAplicacionById(aplicacion, this.listAplicacion));
		if (this.listFunciones == null)
			this.listFunciones = new ArrayList<Funcion>();
		this.listFunciones.clear();
	}

	public Long getSuggAplicacion() {
		return pojoMenu.getAplicaciones() != null ? this.pojoMenu.getAplicaciones().getId() : -1L;
	}

	public void setPojoFuncion(Funcion funcion) {
		this.pojoFuncion = funcion;
		/*NodoMenuFuncion nodo = new NodoMenuFuncion(this.getIdTmp());
		nodo.setPromt(funcion.getNombre());
		nodo.setDescripcion(funcion.getDescripcion());
		nodo.setUrl(funcion.getForma());
		nodo.setTipoAccion("F");
		nodo.setActepDrop("M");
		nodo.setIcono(funcion.getIcono() != null ? funcion.getIcono() : 3);
		nodo.setId(funcion.getId());
		nodo.setMenuVisible(true);
		nodo.setTipoId(funcion.getId());
		nodo.setId(getIdTmp());
		this.rootMenu.getListSubMenus().add(nodo);*/
	}

	public List<Funcion> getListFunciones() {
		return listFunciones;
	}
	
	public void setListFunciones(List<Funcion> listFunciones) {
		this.listFunciones = listFunciones;
	}

	public List<Menu> getListMenus() {
		return listMenus;
	}
	
	public void setListMenus(List<Menu> listMenus) {
		this.listMenus = listMenus;
	}

	public List<NodoMenuFuncion> getListEliminar() {
		return listEliminar;
	}
	
	public void setListEliminar(List<NodoMenuFuncion> listEliminar) {
		this.listEliminar = listEliminar;
	}
	
	public List<Aplicacion> getListAplicacion() {
		return listAplicacion;
	}

	public NodoMenuFuncion getRootMenu() {
		return rootMenu;
	}
	
	public void setRootMenu(NodoMenuFuncion rootMenu) {
		this.rootMenu = rootMenu;
	}

	public NodoMenuFuncion getNodoActual() {
		return nodoActual;
	}
	
	public void setNodoActual(NodoMenuFuncion nodoActual) {
		this.nodoActual = nodoActual;
	}

	public Aplicacion getPojoAplicacion() {
		return this.pojoMenu.getAplicaciones();
	}
	
	public void setPojoAplicacion(Aplicacion pojoAplicacion) {
		this.pojoMenu.setAplicaciones(pojoAplicacion);
	}

	public String getBusquedaNombre() {
		return busquedaNombre;
	}
	
	public void setBusquedaNombre(String busquedaNombre) {
		this.busquedaNombre = busquedaNombre;
	}

	public String getBusquedaFuncion() {
		return busquedaFuncion;
	}
	
	public void setBusquedaFuncion(String busquedaFuncion) {
		this.busquedaFuncion = busquedaFuncion;
	}

	public String getBusquedaMenu() {
		return busquedaMenu;
	}
	
	public void setBusquedaMenu(String busquedaMenu) {
		this.busquedaMenu = busquedaMenu;
	}

	public String getCambiaNombre() {
		return cambiaNombre;
	}
	
	public void setCambiaNombre(String cambiaNombre) {
		this.cambiaNombre = cambiaNombre;
	}

	public String getResOperacion() {
		return resOperacion;
	}
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public int getNumPaginaMenu() {
		return numPaginaMenu;
	}
	
	public void setNumPaginaMenu(int numPaginaMenu) {
		this.numPaginaMenu = numPaginaMenu;
	}

	public int getNumPaginaFuncion() {
		return numPaginaFuncion;
	}
	
	public void setNumPaginaFuncion(int numPaginaFuncion) {
		this.numPaginaFuncion = numPaginaFuncion;
	}

	public Pattern getPat() {
		return pat;
	}
	
	public void setPat(Pattern pat) {
		this.pat = pat;
	}

	public void setListAplicacion(List<Aplicacion> listAplicacion) {
		this.listAplicacion = listAplicacion;
	}
	
	public List<SelectItem> getListTmpFunciones() {
		return listTmpFunciones;
	}
	
	public void setListTmpFunciones(List<SelectItem> listTmpFunciones) {
		this.listTmpFunciones = listTmpFunciones;
	}
	
	public String getErrMenuRequ() {
		return errMenuRequ;
	}
	
	public void setErrMenuRequ(String errMenuRequ) {
		this.errMenuRequ = errMenuRequ;
	}

	public long getIdTmp() {
		return idTmp;
	}
	
	public void setIdTmp(long idTmp) {
		this.idTmp = idTmp;
	}

	public Matcher getMatch() {
		return match;
	}
	
	public void setMatch(Matcher match) {
		this.match = match;
	}

	public String getProblemInesp() {
		return problemInesp;
	}
	
	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
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

	public List<Menu> getListMenusSelec() {
		return listMenusSelec;
	}
	
	public void setListMenusSelec(List<Menu> listMenusSelec) {
		this.listMenusSelec = listMenusSelec;
	}

	public ArrayList<SelectItem> getListTmpAplicacion() {
		return listTmpAplicacion;
	}
	
	public void setListTmpAplicacion(ArrayList<SelectItem> listTmpAplicacion) {
		this.listTmpAplicacion = listTmpAplicacion;
	}
}

