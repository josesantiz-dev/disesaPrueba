package cde.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.beans.EstatusSeguimientoExt;
import net.giro.clientes.beans.NegocioExt;
import net.giro.clientes.beans.OficialExt;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.beans.ProspectoExt;
import net.giro.clientes.beans.Seguimiento;
import net.giro.clientes.logica.ClientesRem;
import net.giro.clientes.logica.NegociosRem;
import net.giro.clientes.logica.ProspectosRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.plataforma.beans.ConValores;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;

public class ProspectosAction implements Serializable{
	
	private static final long serialVersionUID = -4557410090846957603L;
	Logger log = Logger.getLogger(ProspectosAction.class);
	
	@SuppressWarnings("unused")
	private NegociosRem ifzNegocios;
	private ClientesRem ifzClientes;
	private ProspectosRem ifzProspectos;
	
	private ProspectoExt pojoProspecto;
	private Seguimiento pojoSeguimiento;
	private DomicilioExt pojoDomicilioExt;
	
	private List<SelectItem> listEstatus;
	private List<SelectItem> listModoContacto;
	private List<SelectItem> listModoContactoSiguiente;
	private List<SelectItem> listComoEntero;
	private List<SelectItem> listEspecialista;
	private List<SelectItem> listSucursales;
	private List<SelectItem> listTipoEstablecimiento;
	private List<SelectItem> listRangoFacturcion;
	private List<SelectItem> listCalificaciones;	
	
	private List<EstatusSeguimientoExt> listTmpEstatusSeguimiento;
	private List<ProspectoExt> listProspectos;
	@SuppressWarnings("unused")
	private List<ConValores> listTmpEstatus;
	private List<ConValores> listTmpModoContacto;
	private List<ConValores> listTmpComoEntero;
	private List<OficialExt> ListTmpEspecialista;
	private List<Sucursal> listTmpSucursales;
	private List<ConValores> listTmpTipoEstablecimiento;
	private List<ConValores> listTmpRangoFacturacion;
	private List<ConValores> listTmpCalificaciones;
	private List<PersonaExt> listPersona;
	private List<NegocioExt> listNegocio;
	
	private Long usuarioId;
	private int	numPagina;
	private int numPaginaNegocio;
	private int numPaginaBuscarPersona;
	
	private String problemInesp;
	private String busquedaVacia;
	private String resOperacion;
	private String valorBusqueda;
	private String tipoBusqueda;
	private String valorBusquedaPersona;
	private String tipoBusquedaPersona;
	private Long estatusSeguimiento;
	private String tipoBusquedaNegocio;
	private String valorBusquedaNegocio;
	private String OpcionBusquedaProspecto;
	
	private Context ctx;
	@SuppressWarnings("unused")
	private Object lookup;	
	LoginManager loginManager;
	
	@SuppressWarnings("unused")
	private boolean editaNacimiento;
	private boolean puedeEditar;
	
	public ProspectosAction() throws Exception{
		ctx = new InitialContext();		
		@SuppressWarnings({ "rawtypes", "unused" })
		HashMap params = new HashMap<String, String>();
		
		pojoProspecto = new ProspectoExt();
		pojoSeguimiento = new Seguimiento();
		pojoDomicilioExt = new DomicilioExt();
		
		listEstatus = new ArrayList<SelectItem>();			
		listModoContacto = new ArrayList<SelectItem>();	
		listModoContactoSiguiente = new ArrayList<SelectItem>();
		listComoEntero = new ArrayList<SelectItem>();	
		listEspecialista = new ArrayList<SelectItem>();	
		listSucursales = new ArrayList<SelectItem>();	
		listTipoEstablecimiento = new ArrayList<SelectItem>();	
		listRangoFacturcion = new ArrayList<SelectItem>();	
		listCalificaciones = new ArrayList<SelectItem>();	
		
		listProspectos = new ArrayList<ProspectoExt>();
		listTmpEstatus = new ArrayList<ConValores>();
		listTmpModoContacto = new ArrayList<ConValores>();
		listTmpComoEntero = new ArrayList<ConValores>();
		ListTmpEspecialista = new ArrayList<OficialExt>();
		listTmpSucursales = new ArrayList<Sucursal>();
		listTmpTipoEstablecimiento = new ArrayList<ConValores>();
		listTmpRangoFacturacion = new ArrayList<ConValores>();
		listTmpCalificaciones = new ArrayList<ConValores>();
		
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
		busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");
		
		resOperacion = "";
		OpcionBusquedaProspecto = "persona";
		
		numPagina = 1;
		numPaginaNegocio = 1;
		numPaginaBuscarPersona = 1;
		estatusSeguimiento = -1L;
		
		puedeEditar = false;//false = equivale a que s� puede editar (disbled=false)
		puedeEditar = false;
		
		ifzNegocios =  (NegociosRem)ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
		ifzClientes =  (ClientesRem)ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
		ifzProspectos = (ProspectosRem)ctx.lookup("ejb:/Logica_Clientes//ProspectosFac!net.giro.clientes.logica.ProspectosRem");
		
		cargarEstatusSeguimiento();
		cargarModosContacto();
		cargarComoEntero();
		cargarSucursales();
		cargarEspecialistas();
		cargarTiposEstablecimientos();
		cargarRangosFacturacion();
		cargarCalificaciones();
	}
	
	public void nuevo(){			
		this.pojoProspecto = new ProspectoExt();
		this.pojoSeguimiento = new Seguimiento();
		this.pojoDomicilioExt = new DomicilioExt();
	}
	
	public void buscar(){		
		try {
			this.resOperacion = "";
			if(OpcionBusquedaProspecto.equals("")){
				this.resOperacion = "Verifique, debe seleccionar un criterio Tipo Persona / Negocio";
			}else{
				if(listProspectos == null){
					listProspectos = new ArrayList<ProspectoExt>();
				}else{
					listProspectos.clear();
				}
				if(OpcionBusquedaProspecto.equals("persona")){
					this.listProspectos = ifzProspectos.buscarProspectosPorPersona(tipoBusqueda,valorBusqueda,estatusSeguimiento);
				}else{
					if(OpcionBusquedaProspecto.equals("negocio")){
						this.listProspectos = ifzProspectos.buscarProspectosPorNegocio(tipoBusqueda,valorBusqueda,estatusSeguimiento);
					}
				}
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
	
	public void guardar(){	
		try{
			this.resOperacion = "";
			
			if(pojoProspecto.getPersona() != null && pojoProspecto.getPersona().getId() > 0 ){
				this.pojoProspecto.setModificadoPor(Long.valueOf(this.usuarioId));
				this.pojoProspecto.setFechaModificacion(Calendar.getInstance().getTime());
				
				if(this.pojoProspecto.getId() <= 0){						
					this.pojoProspecto.setCreadoPor(Long.valueOf(this.usuarioId));
					this.pojoProspecto.setFechaCreacion(Calendar.getInstance().getTime());
					Respuesta respuesta = ifzProspectos.salvar(pojoProspecto);
					
					if(respuesta.getErrores().getCodigoError() == 0L){
						this.pojoProspecto = (ProspectoExt) respuesta.getBody().getValor("pojoProspecto");
						
						this.pojoSeguimiento.setCreadoPor(Long.valueOf(this.usuarioId));
						this.pojoSeguimiento.setFechaCreacion(Calendar.getInstance().getTime());
						this.pojoSeguimiento.setModificadoPor(Long.valueOf(this.usuarioId));
						this.pojoSeguimiento.setFechaModificacion(Calendar.getInstance().getTime());
						this.pojoSeguimiento.setProspectoId(ifzProspectos.convertProspectoExtToProspecto(pojoProspecto));
						this.pojoSeguimiento.setEstatusId(pojoProspecto.getEstatus().getId());
						this.pojoSeguimiento.setEspecialistaId(pojoProspecto.getOficial().getId());
						this.pojoSeguimiento.setId(this.ifzProspectos.salvar(pojoSeguimiento));
						
						this.listProspectos.add(0,pojoProspecto);
					} else
						this.resOperacion = respuesta.getErrores().getDescError();
				}else{
					this.ifzProspectos.salvar(this.pojoProspecto);
				}
			} else {
				this.resOperacion = "Debe seleccionar una persona";
			}
		}catch(Exception e){
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}
		
	}
	
	public void limpiarListas(){
		try{
			this.resOperacion = "";
			if(listNegocio != null)
				listNegocio.clear();
			if(listPersona != null)
				listPersona.clear();
		}catch(Exception e){
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo editar", e);
		}
	}
	
	public void editar(){
		try{
			this.resOperacion = "";
			pojoDomicilioExt = ifzProspectos.buscarDomicilioPrincipal(pojoProspecto);
		}catch(Exception e){
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo editar", e);
		}
	}
	
	public void eliminar(){
		try {		
			this.resOperacion = "";			
			
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar", e);
		}
	}
	
	public void buscarPersonas(){		
		try {
			this.resOperacion = "";
			
			if(pojoProspecto.getNegocio() != null){
				if(pojoProspecto.getNegocio().getId() > 0){
					this.listPersona = this.ifzClientes.buscarPersonaPorNegocio(this.tipoBusquedaPersona, valorBusquedaPersona,this.pojoProspecto.getNegocio());
				}else{
					this.listPersona = this.ifzClientes.buscarPersonaGeneral(this.tipoBusquedaPersona, valorBusquedaPersona);
				}
			}else{
				this.listPersona = this.ifzClientes.buscarPersonaGeneral(this.tipoBusquedaPersona, valorBusquedaPersona);
			}
			if (this.listPersona.isEmpty()){
				this.resOperacion=busquedaVacia;
			}	
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarPersonas", e);
		}
	}
	
	public void buscarNegocios(){
		try{
			this.resOperacion = "";
			if(pojoProspecto.getPersona() != null){
				if(pojoProspecto.getPersona().getId() > 0){
					this.listNegocio = ifzClientes.buscarNegociosExt(this.tipoBusquedaNegocio, this.valorBusquedaNegocio,this.pojoProspecto.getPersona());
				}else{
					this.listNegocio = ifzClientes.buscarNegociosExt(this.tipoBusquedaNegocio, this.valorBusquedaNegocio);
				}
			}else{
				this.listNegocio = ifzClientes.buscarNegociosExt(this.tipoBusquedaNegocio, this.valorBusquedaNegocio);
			}			
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarNegocios", e);
		}
	}
	
	private void cargarEstatusSeguimiento(){
		this.resOperacion = "";
		
		try {
			listTmpEstatusSeguimiento = ifzProspectos.buscarEstatusSeguimiento();
			if(this.listEstatus==null){ 
				this.listEstatus = new ArrayList<SelectItem>();
			}else{
				this.listEstatus.clear();
			}
			for(EstatusSeguimientoExt estatusSeguimiento : listTmpEstatusSeguimiento){
				listEstatus.add(new SelectItem(estatusSeguimiento.getId(), estatusSeguimiento.getEstatus()));
			}
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargarEstatusSeguimiento", e);
		}
		
	}
	
	private void cargarModosContacto(){
		this.resOperacion = "";			
		
		if(this.listTmpModoContacto!=null && !this.listTmpModoContacto.isEmpty())
			return;
		try {		
			this.listTmpModoContacto = ifzProspectos.buscarModosContacto();
			if(this.listModoContacto==null){ 
				this.listModoContacto = new ArrayList<SelectItem>();
			}else{
				this.listModoContacto.clear();
			}
			
			if(this.listModoContactoSiguiente==null){ 
				this.listModoContactoSiguiente = new ArrayList<SelectItem>();
			}else{
				this.listModoContactoSiguiente.clear();
			}
			for(ConValores cv : this.listTmpModoContacto){
				this.listModoContacto.add(new SelectItem(cv.getId(), cv.getValor()));
				this.listModoContactoSiguiente.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargarModosContacto", e);
		}
	}
	
	private void cargarComoEntero(){
		this.resOperacion = "";			
			
		if(this.listTmpComoEntero!=null && !this.listTmpComoEntero.isEmpty())
			return;
		try {		
			this.listTmpComoEntero = ifzProspectos.buscarComoEntero();
			if(this.listComoEntero==null) 
				this.listComoEntero = new ArrayList<SelectItem>();
			else
				this.listComoEntero.clear();
			for(ConValores cv : this.listTmpComoEntero){
				this.listComoEntero.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargarComoEntero", e);
		}
	}
	
	private void cargarEspecialistas(){
		this.resOperacion = "";			
			
		if(this.ListTmpEspecialista!=null && !this.ListTmpEspecialista.isEmpty())
			return;
		try {		
			this.ListTmpEspecialista = ifzProspectos.buscarEspecialistas();
			if(this.listEspecialista==null) 
				this.listEspecialista = new ArrayList<SelectItem>();
			else
				this.listEspecialista.clear();
			for(OficialExt o : this.ListTmpEspecialista){
				this.listEspecialista.add(new SelectItem(o.getId(), o.getUsuarioId().getNombre()));
			}
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargarEspecialistas", e);
		}
	}
	
	public void asignarPersona(){
		try {		
			if(pojoProspecto.getPersona() == null){
				pojoProspecto.setPersona(ifzClientes.asignarPersona(pojoProspecto.getNegocio()));
			}else{
				if(pojoProspecto.getPersona().getId() == 0){
					pojoProspecto.setPersona(ifzClientes.asignarPersona(pojoProspecto.getNegocio()));
				}
			}
			pojoDomicilioExt = ifzProspectos.buscarDomicilioPrincipal(pojoProspecto);
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo asignarPersona", e);
		}
	}
	
	private void cargarSucursales(){
		this.resOperacion = "";			
		
		if(this.listTmpSucursales!=null && !this.listTmpSucursales.isEmpty())
			return;
		try {		
			this.listTmpSucursales = ifzProspectos.buscarSucursales();
			if(this.listSucursales==null) 
				this.listSucursales = new ArrayList<SelectItem>();
			else
				this.listSucursales.clear();
			for(Sucursal s : this.listTmpSucursales){
				this.listSucursales.add(new SelectItem(s.getId(), s.getSucursal()));
			}
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargarSucursales", e);
		}
	}
	
	private void cargarTiposEstablecimientos(){
		this.resOperacion = "";			
		
		if(this.listTmpTipoEstablecimiento!=null && !this.listTmpTipoEstablecimiento.isEmpty())
			return;
		try {		
			this.listTmpTipoEstablecimiento = ifzProspectos.buscarTiposEstablecimiento();
			if(this.listTipoEstablecimiento==null) 
				this.listTipoEstablecimiento = new ArrayList<SelectItem>();
			else
				this.listTipoEstablecimiento.clear();
			for(ConValores cv : this.listTmpTipoEstablecimiento){
				this.listTipoEstablecimiento.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargarTiposEstablecimientos", e);
		}
	}
	
	private void cargarRangosFacturacion(){
		this.resOperacion = "";			
				
		if(this.listTmpRangoFacturacion!=null && !this.listTmpRangoFacturacion.isEmpty())
			return;
		try {		
			this.listTmpRangoFacturacion = ifzProspectos.buscarRangosFacturacion();
			if(this.listRangoFacturcion==null) 
				this.listRangoFacturcion = new ArrayList<SelectItem>();
			else
				this.listRangoFacturcion.clear();
			for(ConValores cv : this.listTmpRangoFacturacion){
				this.listRangoFacturcion.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargarRangosFacturacion", e);
		}
	}
	
	private void cargarCalificaciones(){
		this.resOperacion = "";			
		
		if(this.listTmpCalificaciones!=null && !this.listTmpCalificaciones.isEmpty())
			return;
		try {		
			this.listTmpCalificaciones = ifzProspectos.buscarCalificaciones();
			if(this.listCalificaciones==null) 
				this.listCalificaciones = new ArrayList<SelectItem>();
			else
				this.listCalificaciones.clear();
			for(ConValores cv : this.listTmpCalificaciones){
				this.listCalificaciones.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargarCalificaciones", e);
		}
	}
	
	private ConValores getValorById(Long id, List<ConValores> lista){
		for(ConValores cv :lista){
			if(cv.getId() == id.intValue())
				return cv;
		}
		return null;
	}
	
	private OficialExt getEspecialistaById(Long id, List<OficialExt> lista){
		for(OficialExt e :lista){
			if(e.getId() == id.intValue())
				return e;
		}
		return null;
	}
	
	private Sucursal getSucursalById(Long id, List<Sucursal> lista){
		for(Sucursal s :lista){
			if(s.getId() == id.intValue())
				return s;
		}
		return null;
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

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public List<SelectItem> getListEstatus() {
		return listEstatus;
	}

	public void setListEstatus(List<SelectItem> listEstatus) {
		this.listEstatus = listEstatus;
	}

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}
	
	public Long getIdModoContacto() {
		return pojoProspecto.getModoContacto() != null ? pojoProspecto.getModoContacto().getId() : -1L;
	}
	
	public void setIdModoContacto(Long idModoContacto) {
		this.pojoProspecto.setModoContacto(getValorById(idModoContacto, this.listTmpModoContacto));		
	}

	public Long getIdModoContactoSiguiente() {
		return pojoSeguimiento.getModoContactoId() != null ? pojoSeguimiento.getModoContactoId() : -1L;
	}
	
	public void setIdModoContactoSiguiente(Long idModoContacto) {
		this.pojoSeguimiento.setModoContactoId(getValorById(idModoContacto, this.listTmpModoContacto).getId());		
	}
	
	public Long getIdComoEntero() {
		return pojoProspecto.getModoContacto() != null ? pojoProspecto.getModoContacto().getId() : -1L;
	}
	
	public void setIdComoEntero(Long idComoEntero) {
		this.pojoProspecto.setModoContacto(getValorById(idComoEntero, this.listTmpComoEntero));		
	}
	
	public Long getIdEspecialista() {
		return pojoProspecto.getOficial() != null ? pojoProspecto.getOficial().getId() : -1L;
	}
	
	public void setIdEspecialista(Long idEspecialista) {
		this.pojoProspecto.setOficial(getEspecialistaById(idEspecialista, this.ListTmpEspecialista));		
	}
	
	public Long getIdSucursal() {
		return pojoProspecto.getSucursal() != null ? pojoProspecto.getSucursal().getId() : -1L;
	}
	
	public void setIdSucursal(Long idSucursal) {
		this.pojoProspecto.setSucursal(getSucursalById(idSucursal, this.listTmpSucursales));		
	}
	
	public Long getIdTipoEstablecimiento() {
		return pojoProspecto.getEstablecimiento() != null ? pojoProspecto.getEstablecimiento().getId() : -1L;
	}
	
	public void setIdTipoEstablecimiento(Long idTipoEstablecimiento) {
		this.pojoProspecto.setEstablecimiento(getValorById(idTipoEstablecimiento, this.listTmpTipoEstablecimiento));		
	}
	
	public Long getIdRangoFacturacion() {
		return pojoProspecto.getRangoFacturacion() != null ? pojoProspecto.getRangoFacturacion().getId() : -1L;
	}
	
	public void setIdRangoFacturacion(Long idRangoFacturacion) {
		this.pojoProspecto.setRangoFacturacion(getValorById(idRangoFacturacion, this.listTmpRangoFacturacion));		
	}
	
	public Long getIdCalificacion() {
		return pojoProspecto.getCalificacion() != null ? pojoProspecto.getCalificacion().getId() : -1L;
	}
	
	public void setIdCalificacion(Long idCalificacion) {
		this.pojoProspecto.setCalificacion(getValorById(idCalificacion, this.listTmpCalificaciones));		
	}
	
	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public List<ProspectoExt> getListProspectos() {
		return listProspectos;
	}

	public void setListProspectos(List<ProspectoExt> listProspectos) {
		this.listProspectos = listProspectos;
	}

	public ProspectoExt getPojoProspecto() {
		return pojoProspecto;
	}

	public void setPojoProspecto(ProspectoExt pojoProspecto) {
		this.pojoProspecto = pojoProspecto;
	}

	public Long getIdEstatusSeguimiento() {
		return estatusSeguimiento;
	}

	public void setIdEstatusSeguimiento(Long estatusSeguimiento) {
		this.estatusSeguimiento = estatusSeguimiento;		
	}

	public List<SelectItem> getListModoContacto() {
		return listModoContacto;
	}

	public void setListModoContacto(List<SelectItem> listModoContacto) {
		this.listModoContacto = listModoContacto;
	}

	public List<SelectItem> getListComoEntero() {
		return listComoEntero;
	}

	public void setListComoEntero(List<SelectItem> listComoEntero) {
		this.listComoEntero = listComoEntero;
	}

	public List<SelectItem> getListEspecialista() {
		return listEspecialista;
	}

	public void setListEspecialista(List<SelectItem> listEspecialista) {
		this.listEspecialista = listEspecialista;
	}

	public List<SelectItem> getListSucursales() {
		return listSucursales;
	}

	public void setListSucursales(List<SelectItem> listSucursales) {
		this.listSucursales = listSucursales;
	}

	public List<SelectItem> getListTipoEstablecimiento() {
		return listTipoEstablecimiento;
	}

	public void setListTipoEstablecimiento(List<SelectItem> listTipoEstablecimiento) {
		this.listTipoEstablecimiento = listTipoEstablecimiento;
	}

	public List<SelectItem> getListRangoFacturcion() {
		return listRangoFacturcion;
	}

	public void setListRangoFacturcion(List<SelectItem> listRangoFacturcion) {
		this.listRangoFacturcion = listRangoFacturcion;
	}

	public List<SelectItem> getListCalificaciones() {
		return listCalificaciones;
	}

	public void setListCalificaciones(List<SelectItem> listCalificaciones) {
		this.listCalificaciones = listCalificaciones;
	}

	public String getTipoBusquedaNegocio() {
		return tipoBusquedaNegocio;
	}

	public void setTipoBusquedaNegocio(String tipoBusquedaNegocio) {
		this.tipoBusquedaNegocio = tipoBusquedaNegocio;
	}

	public String getValorBusquedaNegocio() {
		return valorBusquedaNegocio;
	}

	public void setValorBusquedaNegocio(String valorBusquedaNegocio) {
		this.valorBusquedaNegocio = valorBusquedaNegocio;
	}

	public List<NegocioExt> getListNegocio() {
		return listNegocio;
	}

	public void setListNegocio(List<NegocioExt> listNegocio) {
		this.listNegocio = listNegocio;
	}

	public int getNumPaginaNegocio() {
		return numPaginaNegocio;
	}

	public void setNumPaginaNegocio(int numPaginaNegocio) {
		this.numPaginaNegocio = numPaginaNegocio;
	}

	public int getNumPaginaBuscarPersona() {
		return numPaginaBuscarPersona;
	}

	public void setNumPaginaBuscarPersona(int numPaginaBuscarPersona) {
		this.numPaginaBuscarPersona = numPaginaBuscarPersona;
	}

	public List<PersonaExt> getListPersona() {
		return listPersona;
	}

	public void setListPersona(List<PersonaExt> listPersona) {
		this.listPersona = listPersona;
	}

	public Seguimiento getPojoSeguimiento() {
		return pojoSeguimiento;
	}

	public void setPojoSeguimiento(Seguimiento pojoSeguimiento) {
		this.pojoSeguimiento = pojoSeguimiento;
	}

	public List<SelectItem> getListModoContactoSiguiente() {
		return listModoContactoSiguiente;
	}

	public void setListModoContactoSiguiente(
			List<SelectItem> listModoContactoSuiguiente) {
		this.listModoContactoSiguiente = listModoContactoSuiguiente;
	}

	public String getOpcionBusquedaProspecto() {
		return OpcionBusquedaProspecto;
	}

	public void setOpcionBusquedaProspecto(String opcionBusquedaProspecto) {
		OpcionBusquedaProspecto = opcionBusquedaProspecto;
	}

	public String getValorBusquedaPersona() {
		return valorBusquedaPersona;
	}

	public void setValorBusquedaPersona(String valorBusquedaPersona) {
		this.valorBusquedaPersona = valorBusquedaPersona;
	}

	public String getTipoBusquedaPersona() {
		return tipoBusquedaPersona;
	}

	public void setTipoBusquedaPersona(String tipoBusquedaPersona) {
		this.tipoBusquedaPersona = tipoBusquedaPersona;
	}

	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public DomicilioExt getPojoDomicilioExt() {
		return pojoDomicilioExt;
	}

	public void setPojoDomicilioExt(DomicilioExt pojoDomicilioExt) {
		this.pojoDomicilioExt = pojoDomicilioExt;
	}
}
