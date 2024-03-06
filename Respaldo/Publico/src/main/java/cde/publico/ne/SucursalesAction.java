package cde.publico.ne;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;

import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Regiones;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.publico.respuesta.Respuesta;

import org.apache.log4j.Logger;

public class SucursalesAction implements Serializable {
	private static final long serialVersionUID = 985652410351472793L;
	Logger log = Logger.getLogger(SucursalesAction.class);
	
	private SucursalesRem ifzSucursales;
	
	private List<Sucursal> listSucursales;
	private List<ConValores> listTmpCatDomicilios1;
	private List<ConValores> listTmpCatDomicilios2;
	private List<Colonia> listColonias;
	private List<Empresa> listEmpresas;
	private List<Regiones> listRegiones;
	
	private List<SelectItem> listCatDomicilios1;
	private List<SelectItem> listCatDomicilios2;
	
	private List<Estado> listEstados;
	private List<Municipio> listMunicipios;
	private List<Localidad> listLocalidades;
	
	private List<SelectItem> listTmpEstados;
	
	private Sucursal pojoSucursal;
	
	private Estado pojoEstado;
	private Municipio pojoMunicipio;
	private Localidad pojoLocalidad;
	
	private int numPagina;
	private int numPaginaColonia;
	private int numPaginaEmpresa;
	private int numPaginaRegion;
	private int numPaginaMunicipios;
	private int numPaginaLocalidades;
	
	@SuppressWarnings("unused")
	private Long usuarioId;
	
	private Boolean puedeEditar;
	
	private String resOperacion;
	private String tipoBusqueda;
	private String valorBusqueda;
	
	private String valorBusquedaMunicipios;
	private String valorBusquedaLocalidades;
	
	private String problemInesp;
	private String busquedaColonia;
	private String busquedaEmpresa;
	private String busquedaRegiones;
	
	LoginManager loginManager;
	private Context ctx;
	
	public SucursalesAction() throws Exception{
		listSucursales = new ArrayList<Sucursal>();
		
		pojoSucursal =  new Sucursal();
		pojoMunicipio = new Municipio();
		pojoLocalidad = new Localidad();
		pojoEstado = new Estado();
		
		listTmpEstados = new ArrayList<SelectItem>();
		
		numPagina = 1;
		numPaginaColonia = 1;
		numPaginaRegion = 1;
		
		tipoBusqueda = "sucursal";
		valorBusqueda = "";
		busquedaColonia = "";
		busquedaRegiones = "";
		
		puedeEditar = true;
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		
		//obtengo los posibles mensajes a mostrar al usuario
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensajes.error.inesperado");
		
		ifzSucursales = (SucursalesRem)ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
		ifzSucursales.setInfoSesion(loginManager.getInfoSesion());
		
		cargaTiposDomicilio();
		cargaEstados();
	}
	
	public void buscar(){
		try {
			this.resOperacion = "";
			if (listSucursales == null)
				listSucursales = new ArrayList<Sucursal>();
			listSucursales.clear();
			
			listSucursales = ifzSucursales.buscarSucursales(tipoBusqueda, valorBusqueda);
			if (listSucursales.isEmpty()) {
				this.resOperacion = "La busqueda no regreso resultados";
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarLocalidades(){
		try{
			Respuesta respuesta = ifzSucursales.buscarLocalidades(pojoMunicipio, valorBusquedaMunicipios);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listLocalidades = (List<Localidad>) respuesta.getBody().getValor("listLocalidades");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al buscar localidades", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarMunicipios(){
		try{
			Respuesta respuesta = ifzSucursales.buscarMunicipios(pojoEstado, valorBusquedaMunicipios);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listMunicipios = (List<Municipio>) respuesta.getBody().getValor("listMunicipios");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al buscar municipios", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaEstados(){
		try{
			Respuesta respuesta = ifzSucursales.cargarEstados();
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listEstados = (List<Estado>)respuesta.getBody().getValor("listEstados");
				
				listTmpEstados = new ArrayList<SelectItem>();
				
				for(Estado pojoEstadoAux : listEstados){
					listTmpEstados.add(new SelectItem(pojoEstadoAux.getId(), pojoEstadoAux.getNombre()));
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al cargar estados", e);
			this.resOperacion = problemInesp;
		}
	}

	public void nuevo(){
		this.pojoSucursal = new Sucursal();
		this.pojoLocalidad = new Localidad();
		this.pojoMunicipio = new Municipio();
		this.pojoEstado = new Estado();
	}
	
	public void guardar(){
		Respuesta respuesta = new Respuesta();
		long id = 0;
		
		try {
			this.resOperacion = "";
			
			if (this.pojoSucursal == null)
				return;
			
			if (! comprobarSerie()) {
				this.resOperacion = "La Serie indicada ya esta siendo usada por otra Sucursal";
				return;
			}
			
			respuesta = this.ifzSucursales.salvar(this.pojoSucursal);
			id = this.pojoSucursal.getId();
			
			if(respuesta.getErrores().getCodigoError() != 0L) {
				this.resOperacion = respuesta.getErrores().getDescError();
				return;
			}
			
			this.pojoSucursal = (Sucursal) respuesta.getBody().getValor("pojoSucursal");
			if(id <= 0) {
				this.listSucursales.add(0, this.pojoSucursal);
				return;
			}
			
			List<Sucursal> listSucursalesAux = new ArrayList<Sucursal>();
			listSucursalesAux.add(this.pojoSucursal);
			
			for(Sucursal pojoSucursalAux : listSucursales){
				if(pojoSucursalAux.getId() != id)
					listSucursalesAux.add(pojoSucursalAux);
			}
			
			this.listSucursales = listSucursalesAux;
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}
	}
	
	public void editar(){
		try {
			this.resOperacion="";
			
			if(pojoSucursal.getColonia() != null){
				if(pojoSucursal.getColonia().getLocalidad() != null){
					pojoLocalidad = pojoSucursal.getColonia().getLocalidad();
					pojoMunicipio = pojoLocalidad.getMunicipio();
					pojoEstado = pojoMunicipio.getEstado();
				} else {
					pojoLocalidad = new Localidad();
					pojoMunicipio = new Municipio();
					pojoEstado = new Estado();
				}
			} else {
				pojoLocalidad = new Localidad();
				pojoMunicipio = new Municipio();
				pojoEstado = new Estado();
			}
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo editar", e);
		}	
	}
	
	public void eliminar(){
		try {
			this.ifzSucursales.eliminar(this.pojoSucursal);
			this.listSucursales.remove(pojoSucursal);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar", e);
		}
	}
	
	private boolean comprobarSerie() {
		boolean usado = false;
		
		try {
			this.resOperacion = "";
			if (this.pojoSucursal.getSerie() == null) {
				this.resOperacion = "Debe indicar una Serie para facturacion";
				return false;
			}
			
			List<Sucursal> items = this.ifzSucursales.findLikePropiedad("serie", this.pojoSucursal.getSerie());
			if (items != null && ! items.isEmpty()) {
				if (this.pojoSucursal.getId() <= 0L) {
					usado = true;
				} else {
					for (Sucursal item : items) {
						if (item.getId() != this.pojoSucursal.getId()) {
							usado = true;
							break;
						}	
					}
				}
			}
		} catch (Exception e) {
			log.error("Error al comprobar la serie asignada a la sucursal", e);
			this.resOperacion = "Ocurrio un problema al intentar comprobar la Seria asignada";
		}
		
		if (usado)
			this.resOperacion = "La Serie indicada ya esta siendo usada por otrao Sucursal";
		
		if (! "".equals(this.resOperacion))
			return false;
		return true;
	}
	
	public void limpiaColonias(){
		if(listColonias!=null)
			listColonias.clear();
		else
			listColonias = new ArrayList<Colonia>();
	}
	
	public void limpiaEmpresas(){
		if(listEmpresas!=null)
			listEmpresas.clear();
		else
			listEmpresas = new ArrayList<Empresa>();
	}
	
	public void limpiaEstados(){
		pojoLocalidad = new Localidad();
		pojoMunicipio = new Municipio();
		pojoSucursal.setColonia(null);
	}
	
	public void limpiaMunicipios(){
		if(listMunicipios != null)
			listMunicipios.clear();
		else
			listMunicipios = new ArrayList<Municipio>();
	}
	
	public void limpiaLocalidades(){
		if(listLocalidades != null)
			listLocalidades.clear();
		else
			listLocalidades = new ArrayList<Localidad>();
	}
	
	public void limpiaRegiones(){
		if(listRegiones!=null)
			listRegiones.clear();
		else
			listRegiones = new ArrayList<Regiones>();
	}
	
	@SuppressWarnings("unchecked")
	public void buscarColonias(){
		try {
			this.resOperacion = "";
			
			Respuesta respuesta = this.ifzSucursales.buscarColonias(pojoLocalidad, busquedaColonia);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listColonias = (List<Colonia>) respuesta.getBody().getValor("listColonias");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarColonias", e);
		}
	}
	
	public void buscarEmpresas(){
		try {
			listEmpresas = this.ifzSucursales.buscarEmpresas(busquedaEmpresa);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarColonias", e);
		}
	}
	
	public void buscarRegiones(){
		try {
			listRegiones = this.ifzSucursales.buscarRegiones(busquedaRegiones);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarColonias", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaTiposDomicilio(){
		@SuppressWarnings("rawtypes")
		HashMap params = null;
		try {
			if(listTmpCatDomicilios1 == null)
				listTmpCatDomicilios1 = new ArrayList<ConValores>();
			else
				listTmpCatDomicilios1.clear();
			
			if(listTmpCatDomicilios2 == null)
				listTmpCatDomicilios2 = new ArrayList<ConValores>();
			else
				listTmpCatDomicilios2.clear();
			
			if(listCatDomicilios1==null) 
				listCatDomicilios1 = new ArrayList<SelectItem>();
			else
				listCatDomicilios1.clear();
			if(listCatDomicilios2==null) 
				listCatDomicilios2 = new ArrayList<SelectItem>();
			else
				listCatDomicilios2.clear();
			
			params = new HashMap<String, String>();
			params.put("atributo2", "1");
			cargaTipoDom(params, listCatDomicilios1, listTmpCatDomicilios1);
			params.clear();
			params.put("atributo2", "2");
			cargaTipoDom(params, listCatDomicilios2, listTmpCatDomicilios2);
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaTiposDomicilio", e);
		}
	}
	
	private void cargaTipoDom(HashMap<String, String> params, List<SelectItem> items, List<ConValores> listDom){
		try {
			listDom.addAll(ifzSucursales.buscarDomicilios(params));			
			for(ConValores cv : listDom)
				items.add(new SelectItem(cv.getId(), cv.getValor()));
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaTipoDom", e);
		}
	}
	
	public void seleccionarLocalidad(){
		try{
			pojoSucursal.setColonia(null);
		} catch (Exception e) {
			log.error("Error al seleccionar localidad", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void seleccionarMunicipio(){
		try{
			pojoLocalidad = new Localidad();
			pojoSucursal.setColonia(null);
		} catch (Exception e) {
			log.error("Error al seleccionar municipio", e);
			this.resOperacion = problemInesp;
		}
	}
	
	private ConValores getValorById(Long id, List<ConValores> lista){
		for(ConValores cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public Boolean getPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(Boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public List<Sucursal> getListSucursales() {
		return listSucursales;
	}

	public void setListSucursales(List<Sucursal> listSucursales) {
		this.listSucursales = listSucursales;
	}

	public Sucursal getPojoSucursal() {
		if (pojoSucursal == null)
			pojoSucursal = new Sucursal();
		return pojoSucursal;
	}

	public void setPojoSucursal(Sucursal pojoSucursal) {
		this.pojoSucursal = new Sucursal();
		this.pojoSucursal.setId(pojoSucursal.getId());
		this.pojoSucursal.setCreadoPor(pojoSucursal.getCreadoPor());
		this.pojoSucursal.setModificadoPor(pojoSucursal.getModificadoPor());
		this.pojoSucursal.setFechaCreacion(pojoSucursal.getFechaCreacion());
		this.pojoSucursal.setFechaModificacion(pojoSucursal.getFechaModificacion());
		this.pojoSucursal.setSucursal(pojoSucursal.getSucursal());
		this.pojoSucursal.setColonia(pojoSucursal.getColonia());
		this.pojoSucursal.setDomicilio(pojoSucursal.getDomicilio());
		this.pojoSucursal.setDomicilio1(pojoSucursal.getDomicilio1());
		this.pojoSucursal.setDomicilio2(pojoSucursal.getDomicilio2());
		this.pojoSucursal.setDomicilio3(pojoSucursal.getDomicilio3());
		this.pojoSucursal.setDomicilio4(pojoSucursal.getDomicilio4());
		this.pojoSucursal.setDomicilio5(pojoSucursal.getDomicilio5());
		this.pojoSucursal.setDescDomicilio1(pojoSucursal.getDescDomicilio1());
		this.pojoSucursal.setDescDomicilio2(pojoSucursal.getDescDomicilio2());
		this.pojoSucursal.setDescDomicilio3(pojoSucursal.getDescDomicilio3());
		this.pojoSucursal.setDescDomicilio4(pojoSucursal.getDescDomicilio4());
		this.pojoSucursal.setDescDomicilio5(pojoSucursal.getDescDomicilio5());
		this.pojoSucursal.setEmpresa(pojoSucursal.getEmpresa());
		this.pojoSucursal.setRegion(pojoSucursal.getRegion());
		this.pojoSucursal.setCalendarioDiaInhabil(pojoSucursal.getCalendarioDiaInhabil());
		this.pojoSucursal.setSegmentoContable(pojoSucursal.getSegmentoContable());
		this.pojoSucursal.setSegmentoNegocio(pojoSucursal.getSegmentoNegocio());
		this.pojoSucursal.setSerie(pojoSucursal.getSerie());
		this.pojoSucursal.setFolio(pojoSucursal.getFolio());
	}
	
	public Long getIdPojoEstado(){
		return pojoEstado.getId();
	}
	
	public void setIdPojoEstado(Long id){
		for(Estado pojoEstadoAux : listEstados){
			if(pojoEstadoAux.getId() == id){
				pojoEstado = pojoEstadoAux;
			}
		}
	}

	public List<SelectItem> getListCatDomicilios1() {
		return listCatDomicilios1;
	}

	public void setListCatDomicilios1(List<SelectItem> listCatDomicilios1) {
		this.listCatDomicilios1 = listCatDomicilios1;
	}

	public List<SelectItem> getListCatDomicilios2() {
		return listCatDomicilios2;
	}

	public void setListCatDomicilios2(List<SelectItem> listCatDomicilios2) {
		this.listCatDomicilios2 = listCatDomicilios2;
	}
	
	public void setIdDom1(Long idDom1) {
		this.pojoSucursal.setDomicilio1(getValorById(idDom1, this.listTmpCatDomicilios1));
	}

	public Long getIdDom1() {
		return this.pojoSucursal.getDomicilio1() != null ? this.pojoSucursal.getDomicilio1().getId() : -1;
	}
	
	public String getResOperacion() {
		return resOperacion;
	}
	
	public void setIdDom2(Long idDom2) {
		this.pojoSucursal.setDomicilio2(getValorById(idDom2, this.listTmpCatDomicilios1));
	}

	public Long getIdDom2() {
		return this.pojoSucursal.getDomicilio2() != null ? this.pojoSucursal.getDomicilio2().getId() : -1L;
	}
	
	public void setIdDom3(Long idDom3) {
		this.pojoSucursal.setDomicilio3(getValorById(idDom3, this.listTmpCatDomicilios1));
	}

	public Long getIdDom3() {
		return this.pojoSucursal.getDomicilio3() != null ? this.pojoSucursal.getDomicilio3().getId() : -1L;
	}
	
	public void setIdDom4(Long idDom4) {
		this.pojoSucursal.setDomicilio4(getValorById(idDom4, this.listTmpCatDomicilios2));
	}

	public Long getIdDom4() {
		return this.pojoSucursal.getDomicilio4() != null ? this.pojoSucursal.getDomicilio4().getId() : -1L;
	}
	
	public void setIdDom5(Long idDom5) {
		this.pojoSucursal.setDomicilio5(getValorById(idDom5, this.listTmpCatDomicilios2));
	}

	public Long getIdDom5() {
		return this.pojoSucursal.getDomicilio5() != null ? this.pojoSucursal.getDomicilio5().getId() : -1L;
	}

	public String getBusquedaColonia() {
		return busquedaColonia;
	}

	public void setBusquedaColonia(String busquedaColonia) {
		this.busquedaColonia = busquedaColonia;
	}

	public List<Colonia> getListColonias() {
		return listColonias;
	}

	public void setListColonias(List<Colonia> listColonias) {
		this.listColonias = listColonias;
	}

	public int getNumPaginaColonia() {
		return numPaginaColonia;
	}

	public void setNumPaginaColonia(int numPaginaColonia) {
		this.numPaginaColonia = numPaginaColonia;
	}

	public List<Empresa> getListEmpresas() {
		return listEmpresas;
	}

	public void setListEmpresas(List<Empresa> listEmpresas) {
		this.listEmpresas = listEmpresas;
	}

	public int getNumPaginaEmpresa() {
		return numPaginaEmpresa;
	}

	public void setNumPaginaEmpresa(int numPaginaEmpresa) {
		this.numPaginaEmpresa = numPaginaEmpresa;
	}

	public String getBusquedaEmpresa() {
		return busquedaEmpresa;
	}

	public void setBusquedaEmpresa(String busquedaEmpresa) {
		this.busquedaEmpresa = busquedaEmpresa;
	}

	public List<Regiones> getListRegiones() {
		return listRegiones;
	}

	public void setListRegiones(List<Regiones> listRegiones) {
		this.listRegiones = listRegiones;
	}

	public String getBusquedaRegiones() {
		return busquedaRegiones;
	}

	public void setBusquedaRegiones(String busquedaRegiones) {
		this.busquedaRegiones = busquedaRegiones;
	}

	public int getNumPaginaRegion() {
		return numPaginaRegion;
	}

	public void setNumPaginaRegion(int numPaginaRegion) {
		this.numPaginaRegion = numPaginaRegion;
	}

	public Estado getPojoEstado() {
		return pojoEstado;
	}

	public void setPojoEstado(Estado pojoEstado) {
		this.pojoEstado = pojoEstado;
	}

	public Municipio getPojoMunicipio() {
		return pojoMunicipio;
	}

	public void setPojoMunicipio(Municipio pojoMunicipio) {
		this.pojoMunicipio = pojoMunicipio;
	}

	public Localidad getPojoLocalidad() {
		return pojoLocalidad;
	}

	public void setPojoLocalidad(Localidad pojoLocalidad) {
		this.pojoLocalidad = pojoLocalidad;
	}

	public int getNumPaginaMunicipios() {
		return numPaginaMunicipios;
	}

	public void setNumPaginaMunicipios(int numPaginaMunicipios) {
		this.numPaginaMunicipios = numPaginaMunicipios;
	}

	public int getNumPaginaLocalidades() {
		return numPaginaLocalidades;
	}

	public void setNumPaginaLocalidades(int numPaginaLocalidades) {
		this.numPaginaLocalidades = numPaginaLocalidades;
	}

	public List<SelectItem> getListTmpEstados() {
		return listTmpEstados;
	}

	public void setListTmpEstados(List<SelectItem> listTmpEstados) {
		this.listTmpEstados = listTmpEstados;
	}

	public String getValorBusquedaMunicipios() {
		return valorBusquedaMunicipios;
	}

	public void setValorBusquedaMunicipios(String valorBusquedaMunicipios) {
		this.valorBusquedaMunicipios = valorBusquedaMunicipios;
	}

	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}

	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}

	public String getValorBusquedaLocalidades() {
		return valorBusquedaLocalidades;
	}

	public void setValorBusquedaLocalidades(String valorBusquedaLocalidades) {
		this.valorBusquedaLocalidades = valorBusquedaLocalidades;
	}
	
	public String getSucursalEmpresa() {
		if (this.pojoSucursal != null && this.pojoSucursal.getEmpresa() != null && this.pojoSucursal.getEmpresa().getId() != null && this.pojoSucursal.getEmpresa().getId() > 0L)
			return this.pojoSucursal.getEmpresa().getId() + " - " + this.pojoSucursal.getEmpresa().getEmpresa();
		return "";
	}
	
	public void setSucursalEmpresa(String value) {}
	
	public String getSucursalMunicipio() {
		if (this.pojoMunicipio != null && this.pojoMunicipio.getId() > 0L)
			return this.pojoMunicipio.getId() + " - " + this.pojoMunicipio.getNombre();
		return "";
	}
	
	public void setSucursalMunicipio(String value) {}
	
	public String getSucursalLocalidad() {
		if (this.pojoLocalidad != null && this.pojoLocalidad.getId() > 0L)
			return this.pojoLocalidad.getId() + " - " + this.pojoLocalidad.getNombre();
		return "";
	}
	
	public void setSucursalLocalidad(String value) {}
	
	public String getSucursalColonia() {
		if (this.pojoSucursal != null && this.pojoSucursal.getColonia() != null && this.pojoSucursal.getColonia().getId() > 0L)
			return this.pojoSucursal.getColonia().getId() + " - " + this.pojoSucursal.getColonia().getNombre();
		return "";
	}
	
	public void setSucursalColonia(String value) {}
}
