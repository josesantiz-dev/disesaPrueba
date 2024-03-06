package net.giro.tyg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosTYG;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.logica.TygRem;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.*;

import com.google.gson.Gson;

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
	private MonedasValores pojoMonedaValor;
	private String valorBusqueda;
	private String resOperacion;
	private String problemInesp;
	private String msgFechas;
	private String msgRango;
	private String msgTiposMoneda;
	private String monedaBase;
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
	// Fecha Tipo Cambio
	private Date tcFechaDesde;
	private Date tcFechaHasta;
	// BANXICO
	private HashMap<String, String> banxicoParams = null;

	public MonedasValoresAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		PropertyResourceBundle properties = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager)ve.getValue(fc.getELContext());
			this.ifzTyg = (TygRem) this.loginManager.getCtx().lookup("ejb:/Logica_TYG//TygFacade!net.giro.tyg.logica.TygRem");
			this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			properties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			
			this.banxicoParams = new HashMap<String, String>();
			if (properties.containsKey("banxico.token"))
				this.banxicoParams.put("token", properties.getString("banxico.token"));
			if (properties.containsKey("banxico.serie"))
				this.banxicoParams.put("serie", properties.getString("banxico.serie"));
			if (properties.containsKey("banxico.datos"))
				this.banxicoParams.put("datos", properties.getString("banxico.datos"));
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
			properties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			this.problemInesp = properties.getString("mensaje.error.inesperado");	
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
			properties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			this.busquedaVacia = properties.getString("mensaje.info.busquedaVacia");

			this.listValores = new ArrayList<MonedasValores>();
			this.pojoMonedaValor = new MonedasValores();
			this.pojoMoneda = new Moneda();
			this.listTmpMonedas = new ArrayList<Moneda>();
			this.listTmpMonedasValor = new ArrayList<MonedasValores>();
			this.listMonedas = new ArrayList<SelectItem>();
			this.listMonedasDestinos = new ArrayList<SelectItem>();
			this.listMonedasValor = new ArrayList<SelectItem>();
			this.idMonedaBase = 0L;
			this.idMonedaDestino = 0L;

			this.tipoBusqueda = new ArrayList<String>();
			this.tipoBusqueda.add("numeroDeCuenta");
			this.tipoBusqueda.add("clave");
			this.valTipoBusqueda = ""; 
			this.numPagina = 1;
			
			cargarSelectMoneda();
			monedasDestino();
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar MonedasValoresAction", e);
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
			    		return o2.getFechaDesde().compareTo(o1.getFechaDesde());
			        }
				});
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
	
	public void nuevo () {
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
	
	public void guardar() {
		Respuesta respuesta = null;
		long id = 0L;
		
		try {
			this.resOperacion = "";
			id = (this.pojoMonedaValor.getId() != null && this.pojoMonedaValor.getId() > 0L ? this.pojoMonedaValor.getId() : 0L);
			
			this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzTyg.salvar(this.pojoMonedaValor);
			if (respuesta.getErrores().getCodigoError() != 0) {
				this.resOperacion = respuesta.getErrores().getDescError();
				return;
			} 

			this.pojoMonedaValor = (MonedasValores) respuesta.getBody().getValor("pojoMonedasValores");
			if (id <= 0)
				this.listValores.add(0, this.pojoMonedaValor);
		} catch (Exception e) {
			log.error("Error en el metodo guardar", e);
			this.resOperacion = this.problemInesp;
		}	
	}
	
	@SuppressWarnings("unchecked")
	public void listar() {
		Respuesta respuesta = null;
		
		try {			
			this.resOperacion = "";
			this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzTyg.listarMonedasValores();
			if (respuesta.getErrores().getCodigoError() == 0) {
				this.listValores = (List<MonedasValores>)respuesta.getBody().getValor("listMonedasValores");
				if (this.listValores != null && this.listValores.isEmpty()) 
					this.resOperacion = busquedaVacia;
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}

	public void eliminar() {
		try {
			this.resOperacion = "";
			this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = this.ifzTyg.eliminarMonedaValor(this.pojoMonedaValor);
			if (respuesta.getErrores().getCodigoError() == 0)
				this.listValores.remove(this.pojoMonedaValor);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar.", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargarSelectMoneda() {
		Respuesta respuesta = null;
		
		try {
			this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = ifzTyg.autocompletarMoneda();
			if (respuesta.getErrores().getCodigoError() != 0) {
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
			if (cv.getId() == id.longValue())
				return cv;
		}
		
		return null;
	}

	private Moneda getMonedaById(Long id, List<Moneda> lista) {
		for (Moneda cv : lista) {
			if (cv.getId() == id.longValue())
				return cv;
		}
		
		return null;
	}
	
	public void monedasDestinoBusqueda() {
		this.listMonedasDestinos = new ArrayList<SelectItem>();
		if (this.idMonedaBase == null || this.idMonedaBase <= 0L) 
			this.idMonedaBase = (Long) this.listMonedas.get(0).getValue();
		
		for (SelectItem item : this.listMonedas) {
			if (this.idMonedaBase.equals((Long) item.getValue())) continue;
			this.listMonedasDestinos.add(item);
		}
	}
	
	public void monedasDestino() {
		this.listMonedasDestinos = new ArrayList<SelectItem>();
		if (this.pojoMonedaValor == null || this.pojoMonedaValor.getMonedaBase() == null)
			setSuggMoneda((Long) this.listMonedas.get(0).getValue());
		
		for (SelectItem item : this.listMonedas) {
			if (this.pojoMonedaValor.getMonedaBase().getId() == ((Long) item.getValue()).longValue()) continue;
			this.listMonedasDestinos.add(item);
		}
	}

	// -------------------------------------------------------------------------------------
	// ACTUALIZAR TIPO CAMBIO
	// -------------------------------------------------------------------------------------
	
	public void validarTC() {
		this.tcFechaDesde = Calendar.getInstance().getTime();
		this.tcFechaHasta = Calendar.getInstance().getTime();
	}
	
	public void obtenerTipoCambio() {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		SimpleDateFormat formatter = null;
		HashMap<String, String> params = null;
		Gson gson = null;
		String datos = "";
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			this.tcFechaDesde = (this.tcFechaDesde != null ? this.tcFechaDesde : Calendar.getInstance().getTime());
			this.tcFechaHasta = (this.tcFechaHasta != null ? this.tcFechaHasta : Calendar.getInstance().getTime());

			formatter = new SimpleDateFormat("yyyy-MM-dd");
			if (! formatter.format(this.tcFechaDesde).equals(formatter.format(this.tcFechaHasta)) && ! formatter.format(this.tcFechaDesde).equals(formatter.format(Calendar.getInstance().getTime())))
				datos = formatter.format(this.tcFechaDesde) + "/" + formatter.format(this.tcFechaHasta);
			
			gson = new Gson();
			params = new HashMap<String, String>();
			if (this.banxicoParams != null && ! this.banxicoParams.isEmpty())
				params.putAll(this.banxicoParams);
			if (datos != null && ! "".equals(datos.trim()))
				params.put("datos", datos);
			
			target = "USD";
			referencia = "MXN";
			atributos = gson.toJson(params);
			
			msgTopic = new MensajeTopic(TopicEventosTYG.TIPO_CAMBIO, target, referencia, atributos, this.loginManager.getInfoSesion());
			msgTopic.enviar();
		} catch (Exception e) {
			log.error("Error en el metodo obtenerTipoCambio", e);
			this.resOperacion = problemInesp;
		}
	}
	
	// -------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------
	
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

	public Date getTcFechaDesde() {
		return tcFechaDesde;
	}

	public void setTcFechaDesde(Date tcFechaDesde) {
		this.tcFechaDesde = tcFechaDesde;
	}

	public Date getTcFechaHasta() {
		return tcFechaHasta;
	}

	public void setTcFechaHasta(Date tcFechaHasta) {
		this.tcFechaHasta = tcFechaHasta;
	}
}
