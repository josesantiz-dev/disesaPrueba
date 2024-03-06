package net.giro.tyg;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.admon.SucursalBancaria;
import net.giro.tyg.logica.TygRem;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;

@KeepAlive
public class SucursalesBancariasAction  {
	private Logger log = Logger.getLogger(SucursalesBancariasAction.class);
	private TygRem	 ifzTyg;
	private LoginManager loginManager;
	private boolean registroSeleccionado;
	private SucursalBancaria pojoSucursalBancaria;
	private List<SucursalBancaria> listTmpSucursalBancaria;
	private List<SucursalBancaria> listValores;
	private List<Banco> listTmpBancos;
	private List<SelectItem> listBancos;
	private List<Estado> listTmpEstados;
	private List<Banco> listTmpCatBancos;
	private List<SelectItem> listSucursalBancaria;
	private ArrayList<SelectItem> listInstitucionesBancarias;
	private List<SelectItem> listEstados;
	private Long usuarioId;
	private int numPagina;
	private String campoBusqueda;	
	private List<String> tipoBusqueda;
    private String resOperacion;
    private String problemInesp;
    private String ctaBancariaDep;
	private String valTipoBusqueda;
	private String suggEstado;
	private Banco pojoBanco;
	private String busquedaVacia;
	
	public SucursalesBancariasAction() throws Exception {
		
		numPagina = 1;
		pojoSucursalBancaria = new SucursalBancaria();

		valTipoBusqueda=""; 
		tipoBusqueda = new ArrayList<String>();
		tipoBusqueda.add("nombre");
	
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		InitialContext ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		
		dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		busquedaVacia= propPlataforma.getString("mensaje.info.busquedaVacia");
		
		
		ifzTyg =  (TygRem)ctx.lookup("ejb:/Logica_TYG//TygFacade!net.giro.tyg.logica.TygRem");
		ifzTyg.setInfoSesion(loginManager.getInfoSesion());
		
		listTmpSucursalBancaria = new ArrayList<SucursalBancaria>();
		listSucursalBancaria = new ArrayList<SelectItem>();
		
		listTmpEstados = new ArrayList<Estado>();
		listEstados = new ArrayList<SelectItem>();
		
		listBancos = new ArrayList<SelectItem>();
		
		listTmpBancos = new ArrayList<Banco>();
		listValores=new ArrayList<SucursalBancaria>();
		
		cargarSelectBancos();
		cargarSelectEstados();
	}
	
	public void nuevo (){
		try{
			this.pojoSucursalBancaria = new SucursalBancaria();
			cargarSelectEstados();
			this.registroSeleccionado=false;
			this.resOperacion = "";
		}catch(Exception e){
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo nuevo.", e);
			
		}
		System.out.println("nuevo " + resOperacion);
	}

	public void guardar (){
		try {		
			this.resOperacion = "";
			long id = pojoSucursalBancaria.getId();
			this.pojoSucursalBancaria.setEstado(Long.valueOf(suggEstado));
			this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = this.ifzTyg.salvar(this.pojoSucursalBancaria);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.pojoSucursalBancaria = (SucursalBancaria)respuesta.getBody().getValor("pojoSucursalBancaria");
				if(id <= 0)
					this.listValores.add(0,pojoSucursalBancaria);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}	
	}
	
	@SuppressWarnings("unchecked")
	public void buscar(){
		try {			
			this.resOperacion = "";
			this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = this.ifzTyg.buscarSucursal( this.campoBusqueda, this.valTipoBusqueda);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.listValores = (List<SucursalBancaria>)respuesta.getBody().getValor("listSucursalesBancarias");
				if (this.listValores != null && this.listValores.isEmpty())
					this.resOperacion = busquedaVacia;
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
		
	public void eliminarSucursal(){
		try {
			this.resOperacion = "";
			this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = this.ifzTyg.eliminarSucursalBancaria(this.pojoSucursalBancaria);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listValores.remove(this.pojoSucursalBancaria);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar.", e);
		}
	}
	
	public void editar(){
		this.registroSeleccionado=true;
		cargarSelectEstados();
	}
	
	@SuppressWarnings("unchecked")
	public void cargarSelectBancos(){
		try{
			this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = ifzTyg.autoacompletarBanco();
			if(respuesta.getErrores().getCodigoError() == 0){
				listTmpCatBancos = (List<Banco>)respuesta.getBody().getValor("listBancos");
				listBancos.clear();
				for (Banco i : listTmpCatBancos) 
					listBancos.add(new SelectItem(i.getId(), i.getNombreCorto()));
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en cargarSelectBancos", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargarSelectEstados(){
		try{
			this.resOperacion = "";
			this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = ifzTyg.autoacompletarEstado();
			if(respuesta.getErrores().getCodigoError() == 0){
				listTmpEstados = (List<Estado>) respuesta.getBody().getValor("listEstados");
				listEstados.clear();
				for (Estado i : listTmpEstados) {
					listEstados.add(new SelectItem(i.getId(), i.getNombre()));
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e){
			this.resOperacion = problemInesp;
			log.error("Error al cargar lista de estados", e);
		}
	}
	
	public String getNombreEstado(long id){
		String res="";
		for(Estado i : listTmpEstados){
			if(i.getId() == id){
				res = i.getNombre();
				break;
			}
		}
		return res;
	}

	// -------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------
	
	public SucursalBancaria getPojoSucursalBancaria() {
		return pojoSucursalBancaria;
	}
	
	public void setPojoSucursalBancaria(SucursalBancaria pojoSucursalBancaria) {
		this.pojoSucursalBancaria = pojoSucursalBancaria;
	}
	
	public boolean isRegistroSeleccionado() {
		return registroSeleccionado;
	}
	
	public void setRegistroSeleccionado(boolean registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}
	
	public List<SucursalBancaria> getListTmpSucursalBancaria() {
		return listTmpSucursalBancaria;
	}
	
	public void setListTmpSucursalBancaria(List<SucursalBancaria> listTmpSucursalBancaria) {
		this.listTmpSucursalBancaria = listTmpSucursalBancaria;
	}
	
	public List<SelectItem> getListSucursalBancaria() {
		return listSucursalBancaria;
	}
	
	public void setListSucursalBancaria(List<SelectItem> listSucursalBancaria) {
		this.listSucursalBancaria = listSucursalBancaria;
	}

	public long getUsuarioId() {
		return usuarioId;
	}
	
	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}
	
	public List<SucursalBancaria> getListValores() {
		return listValores;
	}
	
	public void setListValores(List<SucursalBancaria> listValores) {
		this.listValores = listValores;
	}
	
	public String getCampoBusqueda() {
		return campoBusqueda;
	}
	
	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getResOperacion() {
		return resOperacion;
	}
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
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
	
	public List<Estado> getListTmpEstados() {
		return listTmpEstados;
	}
	
	public void setListTmpEstados(List<Estado> listTmpEstados) {
		this.listTmpEstados = listTmpEstados;
	}
	
	public List<SelectItem> getListEstados() {
		return listEstados;
	}
	
	public void setListEstados(List<SelectItem> listEstados) {
		this.listEstados = listEstados;
	}
	
	public List<Banco> getListTmpBancos() {
		return listTmpBancos;
	}
	
	public void setListTmpBancos(List<Banco> listTmpBancos) {
		this.listTmpBancos = listTmpBancos;
	}
	
	public List<SelectItem> getListBancos() {
		return listBancos;
	}
	
	public void setListBancos(List<SelectItem> listBancos) {
		this.listBancos = listBancos;
	}
	
	public String getCtaBancariaDep() {
		return ctaBancariaDep;
	}
	
	public void setCtaBancariaDep(String ctaBancariaDep) {
		this.ctaBancariaDep = ctaBancariaDep;
	}
	
	public ArrayList<SelectItem> getListInstitucionesBancarias() {
		return listInstitucionesBancarias;
	}
	
	public void setListInstitucionesBancarias(ArrayList<SelectItem> listInstitucionesBancarias) {
		this.listInstitucionesBancarias = listInstitucionesBancarias;
	}
	
	private Banco getBancosById(Long id, List<Banco> lista){
		for(Banco cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}
	
	public void setSuggBanco(Long catbancos) {
		this.pojoSucursalBancaria.setCatBancoId(getBancosById(catbancos, this.listTmpCatBancos));
	}

	public Long getSuggBanco() {
		return pojoSucursalBancaria.getCatBancoId() != null ? pojoSucursalBancaria.getCatBancoId().getId() : -1L;
	}
	
	public String getSuggEstado() {
		return suggEstado;
	}
	
	public void setSuggEstado(String suggEstado) {
		this.suggEstado = suggEstado;
	}
	
	public List<Banco> getListTmpCatBancos() {
		return listTmpCatBancos;
	}
	
	public void setListTmpCatBancos(List<Banco> listTmpCatBancos) {
		this.listTmpCatBancos = listTmpCatBancos;
	}
	
	public Banco getPojoBanco() {
		return pojoBanco;
	}
	
	public void setPojoBanco(Banco pojoBanco) {
		this.pojoBanco = pojoBanco;
	}
	
	@SuppressWarnings("rawtypes")
	public List getTipoBusqueda() {
		return tipoBusqueda;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setTipoBusqueda(List tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}
	
	public String getBusquedaVacia() {
		return busquedaVacia;
	}
	
	public void setBusquedaVacia(String busquedaVacia) {
		this.busquedaVacia = busquedaVacia;
	}
}


	