package net.giro.tyg;


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
import net.giro.ne.beans.Empresa;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.SucursalBancaria;
import net.giro.tyg.logica.TygRem;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;


@KeepAlive
public class CuentasBancariasAction{
	private static Logger log = Logger.getLogger(CuentasBancariasAction.class);
	private TygRem	 ifzTyg;
	private LoginManager loginManager;
	private boolean registroSeleccionado;
	//listas que seran utilizadas para traer todos los elementos de un pojo
	private List<CuentaBancaria> listCuentasBancarias;
	private List<Moneda> listMonedas;
	private List<Banco> listCatBancos;
	private List<SucursalBancaria> listSucursalesBancarias;
	private List<Empresa> listEmpresas;
	private List<CuentaBancaria> listValores;
	// este tipo de list no permitira seleccionar un atributo en especifico
	private ArrayList<SelectItem> listTmpCuentasBancarias;
	private List<SelectItem> listInstitucionesBancarias;
	private List<SelectItem> listTmpMonedas;
	private List<SelectItem> listTmpSucursalesBancarias;
	private List<SelectItem> listTmpEmpresas;
	private List<SelectItem> listTmpCatBancos;
	/*Estos son los pojos que nos permitiran interactuar con las clases necesarias 
	para que cumpla su funcion la pantalla*/
	private CuentaBancaria pojoCuentasBancarias;
	private Banco pojoInstitucionesBancarias;
	private SucursalBancaria pojoSucursalesBancarias;
	private Empresa pojoEmpresas;
	private Moneda pojoMoneda;
	//variables utilizadas en la operacion de la pantalla
	private String problemInesp;
	private String resOperacion;
	private String valTipoBusqueda;
	private String campoBusqueda; 
	/* estas variables son utilizadas para traernos en un selectOneMenu los resultados de los list
	en este caso el que se relacione con el de la busqueda especifica*/
	private String suggEmpresa;
	private String suggSucursal;
	//private String suggMoneda;
	//Estas son variables que se utilizan en la operacion de busqueda especifica
	private String busquedaVacia;
	//variables que permitiran la operacion de la pantalla
	private long usuarioId;
	private int numPagina;	
	private String monV;


	//contructor de la clase action en este caso
	public CuentasBancariasAction() throws Exception {
		/* no olvidar inicializar todas las variables que tengan alguna funcion en la pantalla
		y sobre todo si esas variables estan instanciadas desde el jsp*/
		listValores = new ArrayList<CuentaBancaria>();
		this.numPagina=1;
		//numDoctoDigit = 1;
		valTipoBusqueda=""; 
		campoBusqueda="";

		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		//esta nos permite mandar el mensaje de mensaje inesperado
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		
		//esta nos permite mandar el mensaje de mensaje busqueda vacia en el caso del boton busqueda
		dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		busquedaVacia= propPlataforma.getString("mensaje.info.busquedaVacia");
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		InitialContext ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		
		// parte del codigo que nos dira a donde esta haciendo referencia la interfaz
		ifzTyg =  (TygRem)ctx.lookup("ejb:/Logica_TYG//TygFacade!net.giro.tyg.logica.TygRem");
		ifzTyg.setInfoSesion(loginManager.getInfoSesion());
	
		//inicializacion de todos los atributos o variables declaradas antes del constructor
		pojoCuentasBancarias = new CuentaBancaria();
		pojoInstitucionesBancarias = new Banco();
		pojoSucursalesBancarias = new SucursalBancaria();
		pojoEmpresas = new Empresa();

		listCuentasBancarias = new ArrayList<CuentaBancaria>();
		listCatBancos = new ArrayList<Banco>();
		listSucursalesBancarias = new ArrayList<SucursalBancaria>();
		listEmpresas = new ArrayList<Empresa>();
		listMonedas=new ArrayList<Moneda>();
		
		listTmpMonedas=new ArrayList<SelectItem>();
		listTmpCuentasBancarias = new ArrayList<SelectItem>();
		listInstitucionesBancarias = new ArrayList<SelectItem>();		
		listTmpEmpresas = new ArrayList<SelectItem>();
		listTmpSucursalesBancarias = new ArrayList<SelectItem>();
		listTmpCuentasBancarias = new ArrayList<SelectItem>();
		
		/*esto nos permite recorrer toda la tabla con sus valores de algun pojo especifico
		pero su funcionalidad es llenar el selectOneMenu*/
		cargarSelectBancos();
		cargarSelectEmpresa();
		cargarSelectMoneda();
		cargarSelectSucursal();

	} //aqui acaba nuestro contructor
	
	
	// se empieza a construir los metodos altas, bajas, cambios entre otros que podrian llegar a ser necesarios
	public String nuevo() {		
		try{
			resOperacion= "";
			pojoCuentasBancarias = new CuentaBancaria();
			cargarSelectBancos();
			cargarSelectEmpresa();
			cargarSelectMoneda();
			cargarSelectSucursal();
		} catch(Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("error al nuevo", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public void guardar() {
		try {
			Respuesta respuesta = this.ifzTyg.salvar(this.pojoCuentasBancarias);
			long id = pojoCuentasBancarias.getId();
			this.resOperacion = "";
			if (respuesta.getErrores().getCodigoError() == 0) {
				pojoCuentasBancarias = (CuentaBancaria)respuesta.getBody().getValor("pojoCuentaBanco");
				if (id <= 0) {
					this.listCuentasBancarias.add(pojoCuentasBancarias);
					this.listValores.add(0,pojoCuentasBancarias);
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}	
	}

	public void eliminar() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = ifzTyg.eliminarCtasBancarias(this.pojoCuentasBancarias);
			if (respuesta.getErrores().getCodigoError() == 0)
				this.listCuentasBancarias.remove(this.pojoCuentasBancarias);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar.", e);
		}
	}

	@SuppressWarnings("unchecked")
	public void buscar() {
		try {			
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.buscarCtasBancos(this.campoBusqueda, this.valTipoBusqueda);
			if (respuesta.getErrores().getCodigoError() == 0) {
				this.listCuentasBancarias = (List<CuentaBancaria>)respuesta.getBody().getValor("listCuentaBancos");
				if (this.listCuentasBancarias != null && this.listCuentasBancarias.isEmpty()) {
					this.resOperacion = busquedaVacia;
					return;
				}
				
				Collections.sort(this.listCuentasBancarias, new Comparator<CuentaBancaria>() {
					@Override
					public int compare(CuentaBancaria o1, CuentaBancaria o2) {
						return ((Long) o1.getId()).compareTo((Long) o2.getId());
					}
				});
			} else 
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
	
	public String editar() {
		this.registroSeleccionado=true;
		cargarSelectBancos();
		cargarSelectEmpresa();
		cargarSelectMoneda();
		cargarSelectSucursal();
		return "OK";
	}
	
	public void filtrarSucursales() {
		listTmpSucursalesBancarias.clear();
		for (SucursalBancaria i : listSucursalesBancarias) {
			if (i.getCatBancoId().getId() == pojoCuentasBancarias.getInstitucionBancaria().getId())
				listTmpSucursalesBancarias.add(new SelectItem(i.getId(), i.getDomicilio()));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargarSelectBancos() {
		try{
			Respuesta respuesta = ifzTyg.autoacompletarBanco();
			if (respuesta.getErrores().getCodigoError() == 0) {
				listCatBancos = (List<Banco>)respuesta.getBody().getValor("listBancos");
				listInstitucionesBancarias.clear();
				for (Banco i : listCatBancos) 
					listInstitucionesBancarias.add(new SelectItem(i.getId(), i.getNombreCorto()));
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en cargarSelectBancos", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargarSelectEmpresa() {
		try {
			Respuesta respuesta = ifzTyg.autoacompletarEmpresa();
			if (respuesta.getErrores().getCodigoError() == 0) {
				listEmpresas = (List<Empresa>)respuesta.getBody().getValor("listEmpresas");
				listTmpEmpresas.clear();
				for (Empresa i : listEmpresas) 
					listTmpEmpresas.add(new SelectItem(i.getId(), i.getEmpresa()));
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en cargarSelectEmpresa", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargarSelectMoneda() {
		try {
			Respuesta respuesta = ifzTyg.autocompletarMoneda();
			if (respuesta.getErrores().getCodigoError() == 0) {
				listMonedas = (List<Moneda>)respuesta.getBody().getValor("listMonedas");
				listTmpMonedas.clear();
				for (Moneda i : listMonedas) 
					listTmpMonedas.add(new SelectItem(i.getId(), i.getNombre()));
			} else
				this.resOperacion = problemInesp;
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en cargarSelectMoneda", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargarSelectSucursal() {
		try {
			Respuesta respuesta = ifzTyg.autoacompletarSucursal();
			if (respuesta.getErrores().getCodigoError() == 0) {
				listSucursalesBancarias = (List<SucursalBancaria>)respuesta.getBody().getValor("listSucursales");
				listTmpSucursalesBancarias.clear();
				for (SucursalBancaria i : listSucursalesBancarias) 
					listTmpSucursalesBancarias.add(new SelectItem(i.getId(), i.getDomicilio()));
			} else
				this.resOperacion = problemInesp;
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en cargarSelectSucursal", e);
		}
	}

	public List<SelectItem> getListInstitucionesBancarias() {
		return listInstitucionesBancarias;
	}
	
	public void setListInstitucionesBancarias(
			List<SelectItem> listInstitucionesBancarias) {
		this.listInstitucionesBancarias = listInstitucionesBancarias;
	}

	public CuentaBancaria getPojoCuentasBancarias() {
		return pojoCuentasBancarias;
	}
	
	public void setPojoCuentasBancarias(CuentaBancaria pojoCuentasBancarias) {
		this.pojoCuentasBancarias = pojoCuentasBancarias;
	}
	
	public Banco getPojoInstitucionesBancarias() {
		return pojoInstitucionesBancarias;
	}
	
	public void setPojoInstitucionesBancarias(Banco pojoInstitucionesBancarias) {
		this.pojoInstitucionesBancarias = pojoInstitucionesBancarias;
	}
	
	public SucursalBancaria getPojoSucursalesBancarias() {
		return pojoSucursalesBancarias;
	}
	
	public void setPojoSucursalesBancarias(SucursalBancaria pojoSucursalesBancarias) {
		this.pojoSucursalesBancarias = pojoSucursalesBancarias;
	}
	
	public Empresa getPojoEmpresas() {
		return pojoEmpresas;
	}
	
	public void setPojoEmpresas(Empresa pojoEmpresas) {
		this.pojoEmpresas = pojoEmpresas;
	}
	
	public List<CuentaBancaria> getListValores() {
		return listValores;
	}
	
	public void setListValores(List<CuentaBancaria> listValores) {
		this.listValores = listValores;
	}
	
	public String getProblemInesp() {
		return problemInesp;
	}
	
	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}
	
	public String getResOperacion() {
		return resOperacion;
	}
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}
	
	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}
	
	public String getSuggEmpresa() {
		return suggEmpresa;
	}
	
	public void setSuggEmpresa(String suggEmpresa) {
		this.suggEmpresa = suggEmpresa;
	}
	
	public String getSuggSucursal() {
		return suggSucursal;
	}
	
	public void setSuggSucursal(String suggSucursal) {
		this.suggSucursal = suggSucursal;
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
	
	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}
	
	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}

	public Moneda getPojoMoneda() {
		return pojoMoneda;
	}
	
	public void setPojoMoneda(Moneda pojoMoneda) {
		this.pojoMoneda = pojoMoneda;
	}
	
	private Moneda getMonedaById(Long id, List<Moneda> lista) {
		for (Moneda cv :lista) {
			if (cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}

	public void setSuggMoneda(Long moneda) {
		this.pojoCuentasBancarias.setMoneda(getMonedaById(moneda, this.listMonedas));
	}

	public Long getSuggMoneda() {
		return pojoCuentasBancarias.getMoneda() != null ? pojoCuentasBancarias.getMoneda().getId() : -1L;
	}

	private SucursalBancaria getSucursalBancariaById(Long id, List<SucursalBancaria> lista) {
		for (SucursalBancaria cv :lista) {
			if (cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}

	public void setSuggSucBancaria(Long sucursalbancaria) {
		this.pojoCuentasBancarias.setSucursalBancaria(getSucursalBancariaById(sucursalbancaria, this.listSucursalesBancarias));
	}

	public Long getSuggSucBancaria() {
		return pojoCuentasBancarias.getSucursalBancaria() != null ? pojoCuentasBancarias.getSucursalBancaria().getId() : -1L;
	}
	
	private Banco getBancosById(Long id, List<Banco> lista) {
		for (Banco cv :lista) {
			if (cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}

	public void setSuggBanco(Long catbancos) {
		this.pojoCuentasBancarias.setInstitucionBancaria(getBancosById(catbancos, this.listCatBancos));
	}

	public Long getSuggBanco() {
		return pojoCuentasBancarias.getInstitucionBancaria() != null ? pojoCuentasBancarias.getInstitucionBancaria().getId() : -1L;
	}
	
	public boolean isRegistroSeleccionado() {
		return registroSeleccionado;
	}
	
	public void setRegistroSeleccionado(boolean registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}
	
	public TygRem getIfzTyg() {
		return ifzTyg;
	}
	
	public void setIfzTyg(TygRem ifzTyg) {
		this.ifzTyg = ifzTyg;
	}
	
	public String getMonV() {
		return monV;
	}
	
	public void setMonV(String monV) {
		this.monV = monV;
	}
	
	public String getBusquedaVacia() {
		return busquedaVacia;
	}
	
	public void setBusquedaVacia(String busquedaVacia) {
		this.busquedaVacia = busquedaVacia;
	}

	public List<Banco> getListCatBancos() {
		return listCatBancos;
	}

	public void setListCatBancos(List<Banco> listCatBancos) {
		this.listCatBancos = listCatBancos;
	}

	public List<CuentaBancaria> getListCuentasBancarias() {
		return listCuentasBancarias;
	}

	public void setListCuentasBancarias(List<CuentaBancaria> listCuentasBancarias) {
		this.listCuentasBancarias = listCuentasBancarias;
	}

	public List<Moneda> getListMonedas() {
		return listMonedas;
	}

	public void setListMonedas(List<Moneda> listMonedas) {
		this.listMonedas = listMonedas;
	}

	public List<SucursalBancaria> getListSucursalesBancarias() {
		return listSucursalesBancarias;
	}

	public void setListSucursalesBancarias(
			List<SucursalBancaria> listSucursalesBancarias) {
		this.listSucursalesBancarias = listSucursalesBancarias;
	}

	public List<Empresa> getListEmpresas() {
		return listEmpresas;
	}

	public void setListEmpresas(List<Empresa> listEmpresas) {
		this.listEmpresas = listEmpresas;
	}

	public List<SelectItem> getListTmpMonedas() {
		return listTmpMonedas;
	}

	public void setListTmpMonedas(List<SelectItem> listTmpMonedas) {
		this.listTmpMonedas = listTmpMonedas;
	}

	public List<SelectItem> getListTmpSucursalesBancarias() {
		return listTmpSucursalesBancarias;
	}

	public void setListTmpSucursalesBancarias(
			List<SelectItem> listTmpSucursalesBancarias) {
		this.listTmpSucursalesBancarias = listTmpSucursalesBancarias;
	}

	public List<SelectItem> getListTmpEmpresas() {
		return listTmpEmpresas;
	}

	public void setListTmpEmpresas(List<SelectItem> listTmpEmpresas) {
		this.listTmpEmpresas = listTmpEmpresas;
	}

	public List<SelectItem> getListTmpCatBancos() {
		return listTmpCatBancos;
	}

	public void setListTmpCatBancos(List<SelectItem> listTmpCatBancos) {
		this.listTmpCatBancos = listTmpCatBancos;
	}

	public ArrayList<SelectItem> getListTmpCuentasBancarias() {
		return listTmpCuentasBancarias;
	}

	public void setListTmpCuentasBancarias(
			ArrayList<SelectItem> listTmpCuentasBancarias) {
		this.listTmpCuentasBancarias = listTmpCuentasBancarias;
	}
}
	


