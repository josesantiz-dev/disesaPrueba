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
import net.giro.respuesta.Respuesta;
import net.giro.scheduler.TipoCambio;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.logica.TygRem;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.*;

@KeepAlive
public class MonedasValoresAction  {
	private Logger log = Logger.getLogger(MonedasValoresAction.class);
	private TygRem ifzTyg;
	private LoginManager loginManager;

	private List<SelectItem> listMonedas;
	private List<SelectItem> listMonedasDestinos;
	private List<Moneda> listTmpMonedas;
	private List<SelectItem> listMonedasValor;
	private List<MonedasValores> listTmpMonedasValor;
	private MonedasValores	pojoMonedaValor;
	private String valorBusqueda;
	private String resOperacion;
	private String problemInesp;
	private String msgFechas;
	private String msgRango;
	private String msgTiposMoneda;
	private String monedaBase;
	private Long usuarioId;
	private int	numPagina;
	private List<MonedasValores> listValores;
	private String valTipoBusqueda;
	private Moneda pojoMoneda;
	private ArrayList<String> tipoBusqueda;
	private String campoBusqueda;
	private boolean registroSeleccionado;
	@SuppressWarnings("unused")
	private String monV;
	private String busquedaVacia;
	private Long idMonedaBase;
	private Long idMonedaDestino;

	
	public MonedasValoresAction() throws Exception {
		InitialContext ctx = null;
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle) dato.getValue(fc.getELContext());
		
		listValores = new ArrayList<MonedasValores>();
		this.numPagina=1;
		//numDoctoDigit = 1;
		valTipoBusqueda=""; 
		tipoBusqueda = new ArrayList<String>();
		tipoBusqueda.add("numeroDeCuenta");
		tipoBusqueda.add("clave");
		
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");		
		dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle) dato.getValue(fc.getELContext());
		busquedaVacia= propPlataforma.getString("mensaje.info.busquedaVacia");
		
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		
		ifzTyg =  (TygRem) ctx.lookup("ejb:/Logica_TYG//TygFacade!net.giro.tyg.logica.TygRem");
		ifzTyg.setInfoSesion(loginManager.getInfoSesion());

		pojoMonedaValor = new MonedasValores();
		pojoMoneda = new Moneda();

		listTmpMonedas = new ArrayList<Moneda>();
		listTmpMonedasValor = new ArrayList<MonedasValores>();
		
		listMonedas=new ArrayList<SelectItem>();
		listMonedasDestinos = new ArrayList<SelectItem>();
		listMonedasValor = new ArrayList<SelectItem>();

		idMonedaBase = 0L;
		idMonedaDestino = 0L;
		cargarSelectMoneda();
		monedasDestino();
		//listar();
	}
	
	public void nuevo (){
		try {
			this.pojoMonedaValor = new MonedasValores();
			this.setRegistroSeleccionado(false);
			monedasDestino();
			this.resOperacion = "";
			log.info("nuevo " + resOperacion);
		} catch (Exception e) {
			log.error("Error en el metodo nuevo.", e);
			this.resOperacion = this.problemInesp;
		}
	}
	
	public void guardar(){
		try{
			Respuesta respuesta;
			this.resOperacion = "";
			long id = this.pojoMonedaValor.getId();
			
			respuesta = this.ifzTyg.salvar(pojoMonedaValor);
			if(respuesta.getErrores().getCodigoError() == 0){
				pojoMonedaValor = (MonedasValores) respuesta.getBody().getValor("pojoMonedasValores");
				if(id <= 0)
					this.listValores.add(0,pojoMonedaValor);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}	
	}
	
	@SuppressWarnings("unchecked")
	public void buscar() {
		Respuesta respuesta = null;
		
		try {			
			this.resOperacion = "";
			if ((idMonedaBase == null || idMonedaBase <= 0L) && (idMonedaDestino == null || idMonedaDestino <= 0L))
				respuesta = this.ifzTyg.listarMonedasValores();
			else
				respuesta = this.ifzTyg.buscarMonedaV(idMonedaBase, idMonedaDestino);
			
			if (respuesta.getErrores().getCodigoError() == 0) {
				this.listValores = (List<MonedasValores>) respuesta.getBody().getValor("listMonedasValores");
				if (this.listValores == null || this.listValores.isEmpty()) 
					this.resOperacion = busquedaVacia;
				
				Collections.sort(this.listValores, new Comparator<MonedasValores>() {
			    	@Override
			        public int compare(MonedasValores o1, MonedasValores o2) {
			    		return o2.getId().compareTo(o1.getId());
			        }
				});
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void listar(){
		try {			
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.listarMonedasValores();
			if(respuesta.getErrores().getCodigoError() == 0){
				this.listValores = (List<MonedasValores>)respuesta.getBody().getValor("listMonedasValores");
				if (this.listValores != null && this.listValores.isEmpty()) 
					this.resOperacion = busquedaVacia;
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
	
	public void obtenerTipoCambio() {
		try {			
			TipoCambio tc = new TipoCambio();
			tc.ejecutar();
			buscar();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo obtenerTipoCambio", e);
		}
	}
	
	public void eliminar() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.eliminarMonedaValor(this.pojoMonedaValor);
			if(respuesta.getErrores().getCodigoError() == 0)
				this.listValores.remove(this.pojoMonedaValor);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar.", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargarSelectMoneda(){
		try {
			Respuesta respuesta = ifzTyg.autocompletarMoneda();
			if(respuesta.getErrores().getCodigoError() != 0) {
				this.resOperacion = problemInesp;
				return;
			} 

			this.listTmpMonedas = (List<Moneda>) respuesta.getBody().getValor("listMonedas");
			this.listMonedas.clear();
			for (Moneda i : this.listTmpMonedas) 
				this.listMonedas.add(new SelectItem(i.getId(), i.getNombre() + " (" + i.getAbreviacion() + ")"));
		} catch (Exception e) {
			log.error("Error en cargarSelectMoneda", e);
			this.resOperacion = problemInesp;
		}
	}
	
	private Moneda getMonedaDesById(Long id, List<Moneda> lista) {
		for (Moneda cv : lista) {
			if(cv.getId() == id.longValue())
				return cv;
		}
		
		return null;
	}

	private Moneda getMonedaById(Long id, List<Moneda> lista) {
		for (Moneda cv : lista) {
			if(cv.getId() == id.longValue())
				return cv;
		}
		
		return null;
	}
	
	public void monedasDestinoBusqueda() {
		if (this.listMonedasDestinos == null)
			this.listMonedasDestinos = new ArrayList<SelectItem>();
		this.listMonedasDestinos.clear();
		
		if (this.idMonedaBase == null || this.idMonedaBase <= 0L) 
			this.idMonedaBase = (Long) this.listMonedas.get(0).getValue();
		
		for (SelectItem item : this.listMonedas) {
			if (this.idMonedaBase.equals((Long) item.getValue())) continue;
			this.listMonedasDestinos.add(item);
		}
	}
	
	public void monedasDestino() {
		if (this.listMonedasDestinos == null)
			this.listMonedasDestinos = new ArrayList<SelectItem>();
		this.listMonedasDestinos.clear();
		
		if (this.pojoMonedaValor == null || this.pojoMonedaValor.getMonedaBase() == null)
			setSuggMoneda((Long) this.listMonedas.get(0).getValue());
		
		for (SelectItem item : this.listMonedas) {
			if (this.pojoMonedaValor.getMonedaBase().getId() == ((Long) item.getValue()).longValue()) continue;
			this.listMonedasDestinos.add(item);
		}
	}

	// -------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------
	
	public TygRem getIfzTyg() {
		return ifzTyg;
	}

	public void setIfzTyg(TygRem ifzTyg) {
		this.ifzTyg = ifzTyg;
	}

	public List<SelectItem> getListMonedas() {
		return listMonedas;
	}

	public void setListMonedas(List<SelectItem> listMonedas) {
		this.listMonedas = listMonedas;
	}

	public List<Moneda> getListTmpMonedas() {
		return listTmpMonedas;
	}

	public void setListTmpMonedas(List<Moneda> listTmpMonedas) {
		this.listTmpMonedas = listTmpMonedas;
	}

	public List<SelectItem> getListMonedasValor() {
		return listMonedasValor;
	}

	public void setListMonedasValor(List<SelectItem> listMonedasValor) {
		this.listMonedasValor = listMonedasValor;
	}

	public List<MonedasValores> getListTmpMonedasValor() {
		return listTmpMonedasValor;
	}

	public void setListTmpMonedasValor(List<MonedasValores> listTmpMonedasValor) {
		this.listTmpMonedasValor = listTmpMonedasValor;
	}

	public MonedasValores getPojoMonedaValor() {
		return pojoMonedaValor;
	}

	public void setPojoMonedaValor(MonedasValores pojoMonedaValor) {
		this.pojoMonedaValor = pojoMonedaValor;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
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

	public List<MonedasValores> getListValores() {
		return listValores;
	}

	public void setListValores(ArrayList<MonedasValores> listValores) {
		this.listValores = listValores;
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

	public ArrayList<String> getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(ArrayList<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getMsgFechas() {
		return msgFechas;
	}

	public void setMsgFechas(String msgFechas) {
		this.msgFechas = msgFechas;
	}

	public String getMsgRango() {
		return msgRango;
	}

	public void setMsgRango(String msgRango) {
		this.msgRango = msgRango;
	}

	public String getMsgTiposMoneda() {
		return msgTiposMoneda;
	}

	public void setMsgTiposMoneda(String msgTiposMoneda) {
		this.msgTiposMoneda = msgTiposMoneda;
	}

	public String getMonedaBase() {
		return monedaBase;
	}

	public void setMonedaBase(String monedaBase) {
		this.monedaBase = monedaBase;
	}

	public void setListValores(List<MonedasValores> listValores) {
		this.listValores = listValores;
	}

	public boolean isRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(boolean registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public void setSuggMoneda(Long moneda) {
		this.pojoMonedaValor.setMonedaBase(getMonedaById(moneda, this.listTmpMonedas));
	}

	public Long getSuggMoneda() {
		return pojoMonedaValor.getMonedaBase() != null ? pojoMonedaValor.getMonedaBase().getId() : -1L;
	}

	public void setSuggMonedaDestino(Long moneda) {
		this.pojoMonedaValor.setMonedaDestino(getMonedaDesById(moneda, this.listTmpMonedas));
	}

	public Long getSuggMonedaDestino() {
		return pojoMonedaValor.getMonedaDestino() != null ? pojoMonedaValor.getMonedaDestino().getId() : -1L;
	}

	public Long getIdMonedaBase() {
		return idMonedaBase;
	}

	public void setIdMonedaBase(Long idMonedaBase) {
		this.idMonedaBase = idMonedaBase;
	}

	public Long getIdMonedaDestino() {
		return idMonedaDestino;
	}

	public void setIdMonedaDestino(Long idMonedaDestino) {
		this.idMonedaDestino = idMonedaDestino;
	}

	public List<SelectItem> getListMonedasDestinos() {
		return listMonedasDestinos;
	}

	public void setListMonedasDestinos(List<SelectItem> listMonedasDestinos) {
		this.listMonedasDestinos = listMonedasDestinos;
	}
}
