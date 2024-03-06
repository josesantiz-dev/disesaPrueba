package cde.clientes;

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
import javax.servlet.http.HttpSession;

import net.giro.clientes.beans.EstatusSeguimientoExt;
import net.giro.clientes.beans.SeguimientoExt;
import net.giro.clientes.logica.SeguimientoRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConValores;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;

public class SeguimientoProspectosAction implements Serializable {
	private static final long serialVersionUID = -4557410090846957603L;
	Logger log = Logger.getLogger(SeguimientoProspectosAction.class);

	private String valTipoBusqueda;
	private String campoBusqueda;
	private String valEstatus;
	private String valor;
	private String fechaContactoSig;
	

	private SeguimientoRem ifzSeguimientos;

	private List<SeguimientoExt> listSeguimientos;
	private List<ConValores> listMotivosRechazo;
	
	private ArrayList<SelectItem> listTmpMotivosRechazo;
	private ArrayList<SelectItem> listTmpSeguimiento;

	private SeguimientoExt pojoSeguimiento;
	//private Seguimiento seguimientoAnt;

	private String resOperacion;

	private String problemInesp;

	private Long usuarioId;
	private int numPagina;

	private String suggModoContactoSig;
	private String suggMotivoRechazo;
	private String suggEstatus;
	
	private boolean puedeVerTodos;
	private boolean puedeAutorizarRechazar;

	private HttpSession httpSession;
	
	private Context ctx;

	private List<SelectItem> listTmpContactoSig;
	//private List<SelectItem> listTmpMotivoRechazo;
	private List<SelectItem> listEstatus;
	private List<SelectItem> listModoContactoSiguiente;

	private List<EstatusSeguimientoExt> listTmpEstatus;
	private String OpcionBusquedaProspecto;

	LoginManager loginManager;

	private String busquedaVacia;
	private String faltaCriterio;

	private boolean registroSeleccionado;

	private String msgFechas;

	private String tipoBusqueda;
	private String inicioOperaciones;
	private Long estatusSeguimiento;
	private String valorBusqueda;
	private List<ConValores> listTmpModoContacto;
	
	@SuppressWarnings("unused")
	private String ipWS;
	@SuppressWarnings("unused")
	private String ipVista;
	@SuppressWarnings("unused")
	private String modulo;
	private String socket_ip;
	private String socket_puerto;
	private String ftp_user;
	private String ftp_password;
	private String ftp_host;
	private String ftp_port;
	
	private String idReporteHistoricoSeguimiento;

	public SeguimientoProspectosAction() throws Exception {
		numPagina = 1;
		estatusSeguimiento = -1L;

		valTipoBusqueda = "";
		campoBusqueda = "";
		valEstatus = "";
		tipoBusqueda = "";
		setOpcionBusquedaProspecto("");

		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(
				fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager) ve.getValue(fc.getELContext());
		ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		
		this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);

		// obtengo los posibles mensajes a mostrar al usuario
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}",PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle) dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensajes.error.inesperado");
		busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");
		faltaCriterio = propPlataforma.getString("mensaje.error.faltaCriterio");
		
		dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}",PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle) dato.getValue(fc.getELContext());
		
		ipWS = propPlataforma.getString("ws_ip");
		ipVista = propPlataforma.getString("vista_ip");
		modulo = propPlataforma.getString("modulo");
		socket_ip = propPlataforma.getString("socket_ip");
		socket_puerto = propPlataforma.getString("socket_puerto");
		ftp_host = propPlataforma.getString("ftp_host");
		ftp_user = propPlataforma.getString("ftp_user");
		ftp_password = propPlataforma.getString("ftp_password");
		ftp_port = propPlataforma.getString("ftp_port");
		idReporteHistoricoSeguimiento = propPlataforma.getString("reporte.historicoSeguimiento");

		ifzSeguimientos = (SeguimientoRem) ctx.lookup("ejb:/Logica_Clientes//SeguimientoFac!net.giro.clientes.logica.SeguimientoRem");
		ifzSeguimientos.setInfoSesion(loginManager.getInfoSesion());
		
		pojoSeguimiento = new SeguimientoExt();
		listSeguimientos = new ArrayList<SeguimientoExt>();
		listTmpSeguimiento = new ArrayList<SelectItem>();
		listTmpEstatus = new ArrayList<EstatusSeguimientoExt>();
		listTmpMotivosRechazo = new ArrayList<SelectItem>();
		listEstatus = new ArrayList<SelectItem>();
		listModoContactoSiguiente = new ArrayList<SelectItem>();
		
		
		String auxPuedeVer = loginManager.getPerfil("VER_TODOS_SEGUIMIENTO");
		
		if(auxPuedeVer.equals("S")){
			puedeVerTodos = true;
			this.OpcionBusquedaProspecto = "todos";
		} else
			this.OpcionBusquedaProspecto = "asignados";
		

		puedeAutorizarRechazar = loginManager.getPerfil("AUTORIZAR_RECHAZAR_SEGUIMIENTO").equals("S");

		cargarEstatusSeguimiento();
		cargarModosContacto();
		cargarMotivosRechazo();
	}

	@SuppressWarnings("unchecked")
	public void buscar() {
		try {
			this.resOperacion = "";
			if (OpcionBusquedaProspecto.equals("")) {
				this.resOperacion = faltaCriterio;
			} else {
				Respuesta respuesta;
				if (OpcionBusquedaProspecto.toString().equals("todos")) 
					respuesta = ifzSeguimientos.buscarSeguimientoPorTodos(tipoBusqueda,valorBusqueda, estatusSeguimiento);
				else 
					respuesta = ifzSeguimientos.buscarSeguimientoPorAsignados(tipoBusqueda, valorBusqueda, estatusSeguimiento, usuarioId);
				
				if(respuesta.getErrores().getCodigoError() == 0)
					this.listSeguimientos = (List<SeguimientoExt>)respuesta.getBody().getValor("listSeguimientos");
				else
					this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}

	public void nuevo() {
		this.pojoSeguimiento = new SeguimientoExt();
	}

	public void guardarRespuesta() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = ifzSeguimientos.insertaSeguimientoProspecto(pojoSeguimiento);
			if(respuesta.getErrores().getCodigoError() == 0)
				pojoSeguimiento = (SeguimientoExt)respuesta.getBody().getValor("pojoSeguimiento");
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void cargarEstatusSeguimiento() {
		this.resOperacion = "";
		Respuesta respuesta;
		try {
			respuesta = ifzSeguimientos.buscarEstatusSeguimiento();
			
			if(respuesta.getErrores().getCodigoError() == 0){
				listTmpEstatus = (List<EstatusSeguimientoExt>)respuesta.getBody().getValor("listEstatusSeguimiento");
				this.listEstatus.clear();			
				for (EstatusSeguimientoExt estatusSeguimiento : listTmpEstatus) {
					listEstatus.add(new SelectItem(estatusSeguimiento.getId(), estatusSeguimiento.getEstatus()));
				}
			}else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargarEstatusSeguimiento", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void cargarModosContacto() {
		this.resOperacion = "";

		try {
			Respuesta respuesta = ifzSeguimientos.buscarModosContacto();
			if(respuesta.getErrores().getCodigoError() == 0){
				this.listTmpModoContacto = (List<ConValores>)respuesta.getBody().getValor("listModosContacto");

				this.listModoContactoSiguiente.clear();
				for (ConValores cv : this.listTmpModoContacto) {
					this.listModoContactoSiguiente.add(new SelectItem(cv.getId(),cv.getValor()));
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargarModosContacto", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarMotivosRechazo() {
		this.resOperacion = "";

		try {
			Respuesta respuesta = ifzSeguimientos.buscarMotivosRechazo();
			if(respuesta.getErrores().getCodigoError() == 0){
				this.listMotivosRechazo = (List<ConValores>)respuesta.getBody().getValor("listMotivosRechazo");

				this.listTmpMotivosRechazo.clear();
				for (ConValores cv : this.listMotivosRechazo) {
					this.listTmpMotivosRechazo.add(new SelectItem(cv.getId(),cv.getValor()));
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
			
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargarMotivosRechazo", e);
		}
	}
	
	/*
	public void copiarSeguimiento(){
		seguimientoAnt = new Seguimiento();
		seguimientoAnt = ifzSeguimientos.convertSeguimientoExtToSeguimiento(pojoSeguimiento);
	}*/

	public void rechazar() {
		try{
			if (pojoSeguimiento.getId() > 0) {
				Respuesta respuesta = ifzSeguimientos.rechazarSeguimiento(pojoSeguimiento.getId(), pojoSeguimiento.getProspectoId().getId(), pojoSeguimiento.getRazonRechazoId() != null ? pojoSeguimiento.getRazonRechazoId().getId() : -1L);
				if(respuesta.getErrores().getCodigoError() == 0)
					listSeguimientos.remove(pojoSeguimiento);
				else
					this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e) {
			log.error("Error al rechazar prospecto", e);
			resOperacion = "Error inesperado";
		}
	}
	
	private ConValores getValorById(Long id, List<ConValores> lista) {
		for (ConValores cv : lista) {
			if (cv.getId() == id.intValue())
				return cv;
		}
		return null;
	}

	public void responder() {

	}

	public void historico() {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("ftp_host", ftp_host);
			params.put("ftp_port", ftp_port);
			params.put("ftp_user", ftp_user);
			params.put("ftp_password", ftp_password);
			params.put("socket_ip", socket_ip);
			params.put("socket_puerto", socket_puerto);
			params.put("idPrograma", idReporteHistoricoSeguimiento);
			
			Respuesta respuesta = ifzSeguimientos.GenerarHistorico(pojoSeguimiento, params);
			
			httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));			
			httpSession.setAttribute("nombreDocumento", "reporte.pdf");
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error al generar historico", e);
		}
	}

	public void autorizar() {
		try{
			this.resOperacion = "";
			if (pojoSeguimiento.getId() > 0) {
				Respuesta respuesta = ifzSeguimientos.autorizarSeguimiento(pojoSeguimiento.getId(), pojoSeguimiento.getProspectoId().getId());
				
				if(respuesta.getErrores().getCodigoError() == 0)
					listSeguimientos.remove(pojoSeguimiento);
				else
					this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e) {
			resOperacion = problemInesp;
			log.error("Error en metodo autorizar", e);
		}
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

	public String getValEstatus() {
		return valEstatus;
	}

	public void setValEstatus(String valEstatus) {
		this.valEstatus = valEstatus;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public boolean isRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(boolean registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public String getMsgFechas() {
		return msgFechas;
	}

	public void setMsgFechas(String msgFechas) {
		this.msgFechas = msgFechas;
	}

	public ArrayList<SelectItem> getListTmpSeguimiento() {
		return listTmpSeguimiento;
	}

	public void setListTmpSeguimiento(ArrayList<SelectItem> listTmpSeguimiento) {
		this.listTmpSeguimiento = listTmpSeguimiento;
	}

	public String getSuggModoContactoSig() {
		return suggModoContactoSig;
	}

	public void setSuggModoContactoSig(String suggModoContactoSig) {
		this.suggModoContactoSig = suggModoContactoSig;
	}

	public List<SelectItem> getListTmpContactoSig() {
		return listTmpContactoSig;
	}

	public void setListTmpContactoSig(List<SelectItem> listTmpContactoSig) {
		this.listTmpContactoSig = listTmpContactoSig;
	}

	public String getSuggMotivoRechazo() {
		return suggMotivoRechazo;
	}

	public void setSuggMotivoRechazo(String suggMotivoRechazo) {
		this.suggMotivoRechazo = suggMotivoRechazo;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getSuggEstatus() {
		return suggEstatus;
	}

	public void setSuggEstatus(String suggEstatus) {
		this.suggEstatus = suggEstatus;
	}

	public List<SelectItem> getListEstatus() {
		return listEstatus;
	}

	public void setListEstatus(List<SelectItem> listEstatus) {
		this.listEstatus = listEstatus;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getBusquedaVacia() {
		return busquedaVacia;
	}

	public void setBusquedaVacia(String busquedaVacia) {
		this.busquedaVacia = busquedaVacia;
	}

	public List<EstatusSeguimientoExt> getListTmpEstatus() {
		return listTmpEstatus;
	}

	public void setListTmpEstatus(List<EstatusSeguimientoExt> listTmpEstatus) {
		this.listTmpEstatus = listTmpEstatus;
	}

	public String getOpcionBusquedaProspecto() {
		return OpcionBusquedaProspecto;
	}

	public void setOpcionBusquedaProspecto(String opcionBusquedaProspecto) {
		OpcionBusquedaProspecto = opcionBusquedaProspecto;
	}

	public Object getEstatusSeguimiento() {
		return estatusSeguimiento;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public String getInicioOperaciones() {
		return inicioOperaciones;
	}

	public void setInicioOperaciones(String inicioOperaciones) {
		this.inicioOperaciones = inicioOperaciones;
	}

	public void setEstatusSeguimiento(Long estatusSeguimiento) {
		this.estatusSeguimiento = estatusSeguimiento;
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

	public List<SeguimientoExt> getListSeguimientos() {
		return listSeguimientos;
	}

	public void setListSeguimientos(List<SeguimientoExt> listSeguimientos) {
		this.listSeguimientos = listSeguimientos;
	}

	public SeguimientoExt getPojoSeguimiento() {
		return pojoSeguimiento;
	}

	public void setPojoSeguimiento(SeguimientoExt pojoSeguimiento) {
		this.pojoSeguimiento = pojoSeguimiento;
	}

	public Long getIdEstatusSeguimiento() {
		return estatusSeguimiento;
	}

	public void setIdEstatusSeguimiento(Long estatusSeguimiento) {
		this.estatusSeguimiento = estatusSeguimiento;
	}

	public List<SelectItem> getListModoContactoSiguiente() {
		return listModoContactoSiguiente;
	}

	public void setListModoContactoSiguiente(List<SelectItem> listModoContactoSiguiente) {
		this.listModoContactoSiguiente = listModoContactoSiguiente;
	}

	public Long getIdModoContactoSiguiente() {
		return (Long) (pojoSeguimiento.getModoContactoId() != null ? pojoSeguimiento
				.getModoContactoId().getId() : -1L);
	}

	public void setIdModoContactoSiguiente(Long idModoContacto) {
		 this.pojoSeguimiento.setModoContactoId(getValorById(idModoContacto,
		 this.listTmpModoContacto));
	}
	
	public Long getIdRazonRechazo(){
		return (Long) (pojoSeguimiento.getRazonRechazoId() != null ? pojoSeguimiento.getRazonRechazoId().getId() : -1L);
	}
	
	public void setIdRazonRechazo(Long idRazonRechazo){
		this.pojoSeguimiento.setRazonRechazoId(getValorById(idRazonRechazo, this.listMotivosRechazo));
	}

	public List<ConValores> getListTmpModoContacto() {
		return listTmpModoContacto;
	}

	public void setListTmpModoContacto(List<ConValores> listTmpModoContacto) {
		this.listTmpModoContacto = listTmpModoContacto;
	}

	public String getFechaContactoSig() {
		return fechaContactoSig;
	}

	public void setFechaContactoSig(String fechaContactoSig) {
		this.fechaContactoSig = fechaContactoSig;
	}

	public ArrayList<SelectItem> getListTmpMotivosRechazo() {
		return listTmpMotivosRechazo;
	}

	public void setListTmpMotivosRechazo(ArrayList<SelectItem> listTmpMotivosRechazo) {
		this.listTmpMotivosRechazo = listTmpMotivosRechazo;
	}

	public boolean isPuedeVerTodos() {
		return puedeVerTodos;
	}

	public void setPuedeVerTodos(boolean puedeVerTodos) {
		this.puedeVerTodos = puedeVerTodos;
	}

	public boolean isPuedeAutorizarRechazar() {
		return puedeAutorizarRechazar;
	}

	public void setPuedeAutorizarRechazar(boolean puedeAutorizarRechazar) {
		this.puedeAutorizarRechazar = puedeAutorizarRechazar;
	}

}
