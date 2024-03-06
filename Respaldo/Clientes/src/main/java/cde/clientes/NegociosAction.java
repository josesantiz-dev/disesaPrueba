package cde.clientes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.clientes.beans.CanalCat;
import net.giro.clientes.beans.CanalesNegocioExt;
import net.giro.clientes.beans.CatBancosExt;
import net.giro.clientes.beans.ContactoNegocioExt;
import net.giro.clientes.beans.DirectorioTelefonicoExt;
import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.beans.EstadosNegocioExt;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.beans.PersonaNegocioExt;
import net.giro.clientes.logica.ClientesRem;
import net.giro.clientes.logica.NegociosRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.respuesta.Respuesta;
import net.giro.util.clientes.Errores;

import org.apache.log4j.Logger;

public class NegociosAction implements Serializable {
	Logger log = Logger.getLogger(NegociosAction.class);
	private static final long serialVersionUID = -4557410090846957603L;
	
	private NegociosRem ifzNegocios;
	private ClientesRem ifzClientes;
	
	@SuppressWarnings("unused")
	private ConValores pojoConValores;
	private ConValores pojoGiro;
	private Negocio pojoNegocio;	
	private Negocio pojoNegocioElim;
	private DirectorioTelefonicoExt pojoDirectorio;
	private DomicilioExt pojoDomicilio;
	private PersonaNegocioExt pojoPersonaNegocio;
	private PersonaExt	pojoPersona;
	private PersonaExt	pojoPersonaApoderado;
	private Colonia pojoColonia;
	private Localidad pojoLocalidadDom;
	private Municipio pojoMunicipioDom;
	private Estado pojoEstadoDom;
	private Estado pojoEstadoNac;
	private Municipio pojoMunicipioNac;
	private Localidad pojoLocalidadNac;
	private CanalesNegocioExt pojoCanalesNegocio;
	private ContactoNegocioExt pojoContactoNegocio;
	private ContactoNegocioExt pojoContactoNegocioEst;
	private EstadosNegocioExt pojoEstadosNegocio;
	
	private List<DirectorioTelefonicoExt> listDirectorio;
	private List<DomicilioExt>	listDomicilios;
	private List<Negocio> listNegocios; 
	private List<PersonaNegocioExt> listPersonasAsociadas;
	private List<PersonaExt> listPersona;
	private List<PersonaExt> listPersonaContacto;
	private List<CanalesNegocioExt> listCanalesNegocio;
	private List<ContactoNegocioExt> listContactos;
	private List<EstadosNegocioExt> listEstadosNegocio;
	
	
	
	private List<CanalCat> listTiposCanales;
	private List<SelectItem> listTmpTiposCanales;
	private List<Colonia> listTmpColonia;	
	private List<ConValores> listTmpGiros;	
	private List<ConValores> listTmpSectores;	
	private List<ConValores> listTmpTiposTelefono;
	private List<ConValores> listTmpCompaniaTel;
	private List<SelectItem> listGiros;
	private List<ConValores> listGirosC;
	private List<SelectItem> listSectores;
	private List<SelectItem> listEstados;
	private List<SelectItem> listColonia;	
	private List<SelectItem> listTiposTelefono;	
	private List<SelectItem> listCompaniaTel;	
	private List<SelectItem> listCatDomicilios;
	private List<ConValores> listTmpCatDomicilios1;
	private List<ConValores> listTmpCatDomicilios2;
	private List<SelectItem> listCatDomicilios1;
	private List<SelectItem> listCatDomicilios2;
	private List<Estado> listTmpEstado;
	private List<Localidad> listLocalidades;
	private List<Municipio> listMunicipios;	
	private List<Colonia> listColonias;
	private List<SelectItem> listEstado;
	private List<CatBancosExt> listBancos;
	private List<SelectItem> listTmpBancos;
	
	private HashMap<Long, String> descEstatusNegocio;
	private HashMap<Long, String> descEstatusContacto;
	
	private Long usuarioId;
	private int	numPagina;
	private int numPaginaDomicilio;
	private int numPaginaDirectorio;
	private int numPaginaBuscarPersona;
	private int numPaginaBuscarApoderado;
	private int numPaginaPersonaAsociada;
	private int numPaginaLocalidad;
	private int numPaginaMunicipio;
	private int numPaginaColonia;
	private int numPaginaCanales;
	private int numPaginaContactos;
	private int numPaginaPersonasContactos;
	private int numPaginaEstadosNegocio;
	private int numPaginaGiro;
	
	private String problemInesp;
	private String busquedaVacia;
	private String participacionSocios;
	private String participacionSocio;
	@SuppressWarnings("unused")
	private String sinSocios;
	@SuppressWarnings("unused")
	private String inicioOperaciones;	
	private String etiquetaAntiguedad;
	private String etiquetaActividad;
	private String resOperacion;	
	private String valorBusqueda;
	private String valorBusquedaGiro;
	private String valorBusquedaPersonas;
	private String valorBusquedaPersonasContacto;
	private String valorBusquedaApoderados;
	private Long valorBusquedaEstatusContacto;
	private String tipoBusqueda;
	private String tipoBusquedaContacto;
	private String tipoBusquedaPersona;
	private String tipoBusquedaApoderado;
	private String idEstado;
	private String idApoderado;
	private String busquedaLocalidad;
	private String busquedaMunicipio;
	private String busquedaColonia;
	
	private Context ctx;
	@SuppressWarnings("unused")
	private Object lookup;	
	LoginManager loginManager;
	
	private boolean editaNacimiento;
	private boolean puedeEditar;
	private boolean preguntaEliminar;
	
	
	public NegociosAction() throws Exception{
		ctx = new InitialContext();		
		@SuppressWarnings("unused")
		HashMap<String, String> params = new HashMap<String, String>();
		
		pojoNegocio = new Negocio();
		pojoGiro = new ConValores();
		pojoConValores = new ConValores(); 
		pojoDomicilio = new DomicilioExt();		
		pojoDirectorio = new DirectorioTelefonicoExt();
		pojoPersonaNegocio = new PersonaNegocioExt();
		pojoPersona = new PersonaExt();
		pojoEstadoDom = new Estado();
		pojoCanalesNegocio = new CanalesNegocioExt();
		pojoContactoNegocio = new ContactoNegocioExt();
		pojoContactoNegocioEst = new ContactoNegocioExt();
		pojoEstadosNegocio = new EstadosNegocioExt();
	
		listGiros = new ArrayList<SelectItem>();				
		listSectores = new ArrayList<SelectItem>();
		listColonia = new ArrayList<SelectItem>();
		listEstados = new ArrayList<SelectItem>();
		listNegocios = new ArrayList<Negocio>();	
		listPersonasAsociadas = new ArrayList<PersonaNegocioExt>();
		listPersona = new ArrayList<PersonaExt>();
		listPersonaContacto = new ArrayList<PersonaExt>();
		listContactos = new ArrayList<ContactoNegocioExt>();
		listEstadosNegocio = new ArrayList<EstadosNegocioExt>();
		listGirosC = new ArrayList<ConValores>();
		listBancos = new ArrayList<CatBancosExt>();
		
		
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
		
		sinSocios = propPlataforma.getString("mensaje.validacion.sinSocios");
		inicioOperaciones = propPlataforma.getString("mensaje.validacion.inicioOperacion");
		participacionSocios =  propPlataforma.getString("mensaje.validacion.participacionSocios");
		participacionSocio =  propPlataforma.getString("mensaje.validacion.participacionSocio");
		
		numPagina = 1;
		numPaginaDomicilio = 1;
		numPaginaDirectorio = 1;
		numPaginaBuscarApoderado = 1;
		numPaginaBuscarPersona = 1;
		numPaginaPersonaAsociada = 1;
		numPaginaLocalidad = 1;
		numPaginaMunicipio = 1;
		numPaginaColonia = 1;
		numPaginaCanales = 1;
		numPaginaPersonasContactos = 1;
		numPaginaEstadosNegocio = 1;
		numPaginaGiro = 1;
		
		puedeEditar = false;//false = equivale a que sï¿½ puede editar (disbled=false)
		puedeEditar = false;
		
		ifzNegocios =  (NegociosRem)ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
		ifzNegocios.setInfoSesion(loginManager.getInfoSesion());
		
		ifzClientes =  (ClientesRem)ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
		
		this.listTmpGiros =  this.ifzNegocios.buscarGiros();
		for(ConValores cv : this.listTmpGiros){
			this.listGiros.add(new SelectItem(cv.getId(), cv.getValor()));
		}
		
		this.listTmpSectores =  this.ifzNegocios.buscarSectores();
		for(ConValores cv : this.listTmpSectores){
			this.listSectores.add(new SelectItem(cv.getId(), cv.getValor()));
		}
		
		this.listTmpEstado = this.ifzClientes.buscarEstados();
		this.listEstado = new ArrayList<SelectItem>();
		for(Estado e : listTmpEstado){
			if(pojoEstadoNac == null)
				pojoEstadoNac = e;
			listEstado.add(new SelectItem(e.getId(), e.getNombre()));
		}
		
		descEstatusNegocio = ifzNegocios.getDescEstatusNegocio();
		descEstatusContacto = ifzNegocios.getDescEstatusContacto();
		
		cargarBancos();
		cargaInfoDomicilio();
		cargaInfoDirectorio();
		cargarTiposCanales();
	}
	
	
	public String getPersonaContactoString() {
		// #{negociosAction.pojoContactoNegocio != null ? (negociosAction.pojoContactoNegocio.idPersonaContacto != null ? (negociosAction.pojoContactoNegocio.idPersonaContacto.id > 0 ? '1' : '') : '') : ''}
		if (this.pojoContactoNegocio != null && this.pojoContactoNegocio.getIdPersonaContacto() != null && this.pojoContactoNegocio.getIdPersonaContacto().getId() > 0L)
			return String.valueOf(this.pojoContactoNegocio.getIdPersonaContacto().getId());
		return "";
	}
	
	public void setPersonaContactoString(String value) {}
	
	public void nuevo(){			
		this.pojoNegocio = new Negocio();	
		this.pojoNegocio.setTipoPersonalidad("F");
		this.listPersonasAsociadas = new ArrayList<PersonaNegocioExt>();
		pojoGiro = new ConValores();
		idApoderado="";		
	}
	
	public void nuevoContacto(){
		this.pojoContactoNegocio = new ContactoNegocioExt();
		this.pojoContactoNegocio.setIdNegocio(pojoNegocio);
	}
	
	public void nuevoDomicilio(){
		this.pojoDomicilio = new DomicilioExt();
		this.pojoDomicilio.setEstatus(true);
		this.pojoMunicipioDom = new Municipio();
		this.pojoLocalidadDom = new Localidad();
		this.pojoColonia = new Colonia();
		cargaInfoDomicilio();
		this.resOperacion = "";
	}
	
	public void nuevoDirectorio(){
		this.pojoDirectorio = new DirectorioTelefonicoExt();
		cargaInfoDirectorio();
		this.resOperacion = "";
	}
	
	public void nuevaPersona(){
		this.pojoPersonaNegocio = new PersonaNegocioExt();
		this.resOperacion = "";
	}
	
	@SuppressWarnings("unchecked")
	public void cargarBancos(){
		try{
			this.resOperacion = "";
			
			Respuesta respuesta = ifzClientes.listarBancos();
			if(respuesta.getErrores().getCodigoError() == 0L){
				listBancos = (List<CatBancosExt>) respuesta.getBody().getValor("listBancos");
				
				if(listTmpBancos == null)
					listTmpBancos = new ArrayList<SelectItem>();
				
				if(listTmpBancos.size() > 0)
					listTmpBancos.clear();
				
				for(CatBancosExt banco : listBancos){
					listTmpBancos.add(new SelectItem(banco.getId(), banco.getNombreCorto()));
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al cargar bancos", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void cargaInfoDomicilio(){
		cargaTiposDomicilio();		
	}
	
	public void deshabilitaDom(){
		try {
			resOperacion = "";
			pojoDomicilio.setNegocio(pojoNegocio);
			
			Respuesta respuesta = ifzNegocios.habilitarDomicilio(pojoDomicilio);
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoDomicilio = (DomicilioExt) respuesta.getBody().getValor("pojoDomicilio");
			} else {
				this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo deshabilitaDom", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaCanalesNegocio(){
		try{
			Respuesta respuesta = ifzNegocios.buscarCanales(pojoNegocio);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listCanalesNegocio = (List<CanalesNegocioExt>) respuesta.getBody().getValor("listCanales");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e){
			log.error("Error al cargar canales de negocio", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaContactosNegocio(){
		try{
			Respuesta respuesta = ifzNegocios.buscarContactos(pojoNegocio, 1L);
			
			if(respuesta.getErrores().getCodigoError() == 0L)
				listContactos = (List<ContactoNegocioExt>) respuesta.getBody().getValor("listContactos");
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al cargar contactos de negocio", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaEstadosNegocio(){
		try{
			Respuesta respuesta = ifzNegocios.buscarEstados(pojoNegocio);
			
			if(respuesta.getErrores().getCodigoError() == 0L)
				listEstadosNegocio = (List<EstadosNegocioExt>) respuesta.getBody().getValor("listEstados");
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al cargar estados de negocio", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void cargaTiposDomicilio(){
		HashMap<String, String> params = null;
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
			listDom.addAll(ifzClientes.buscarDomicilios(params));			
			for(ConValores cv : listDom)
				items.add(new SelectItem(cv.getId(), cv.getValor()));
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaTipoDom", e);
		}
	}
	
	private Estado getEstadoById(Long id){
		for(Estado e : this.listTmpEstado){
			if(e.getId() == id.intValue())
				return e;
		}
		return null;
	}
	
	public DomicilioExt getPojoDomicilio() {
		return pojoDomicilio;
	}
	
	public void setPojoDomicilio(DomicilioExt pojoDomicilio) {
		try {
			this.pojoDomicilio = pojoDomicilio;
			pojoColonia = pojoDomicilio.getColonia();
							
			pojoLocalidadDom = pojoColonia != null ? pojoColonia.getLocalidad() : null;
			pojoMunicipioDom = pojoLocalidadDom != null ? pojoLocalidadDom.getMunicipio() : null;
			pojoEstadoDom = pojoMunicipioDom != null ? pojoMunicipioDom.getEstado() : null;
			
			
		} catch (Exception e) {
			log.error("Error en metodo setPojoDomicilio",e);
		}
			
	}
	
	public Municipio getPojoMunicipio() {
		return editaNacimiento ? pojoMunicipioNac : pojoMunicipioDom;
	}
	
	public void setPojoMunicipio(Municipio pojoMunicipio) {
		if(editaNacimiento){
			pojoMunicipioNac = pojoMunicipio;
			pojoLocalidadNac = null;
		}else{
			pojoMunicipioDom = pojoMunicipio;
			pojoLocalidadDom = null;
			pojoColonia = null;
		}
	}	
	
	public void setIdDom1(Long idDom1) {
		this.pojoDomicilio.setCatDomicilio1(getValorById(idDom1, this.listTmpCatDomicilios1).getId());
	}

	public Long getIdDom1() {
		return this.pojoDomicilio.getCatDomicilio1() != null ? this.pojoDomicilio.getCatDomicilio1() : -1L;
	}

	public void setIdDom2(Long idDom2) {
		this.pojoDomicilio.setCatDomicilio2(getValorById(idDom2, this.listTmpCatDomicilios1).getId());
	}

	public Long getIdDom2() {
		return this.pojoDomicilio.getCatDomicilio2() != null ? this.pojoDomicilio.getCatDomicilio2() : -1L;
	}
	
	public void setIdDom3(Long idDom3) {
		this.pojoDomicilio.setCatDomicilio3(getValorById(idDom3, this.listTmpCatDomicilios1).getId());
	}

	public Long getIdDom3() {
		return this.pojoDomicilio.getCatDomicilio3() != null ? this.pojoDomicilio.getCatDomicilio3() : -1L;
	}
	
	public void setIdDom4(Long idDom4) {
		this.pojoDomicilio.setCatDomicilio4(getValorById(idDom4, this.listTmpCatDomicilios2).getId());
	}

	public Long getIdDom4() {
		return this.pojoDomicilio.getCatDomicilio4() != null ? this.pojoDomicilio.getCatDomicilio4() : -1L;
	}
	
	public void setIdDom5(Long idDom5) {
		this.pojoDomicilio.setCatDomicilio5(getValorById(idDom5, listTmpCatDomicilios2).getId());
	}

	public Long getIdDom5() {
		return this.pojoDomicilio.getCatDomicilio5() != null ? this.pojoDomicilio.getCatDomicilio5() : -1L;
	}
	
	public void cargaInfoDirectorio(){
		cargaTiposTelefono();
		cargaCompaniasTel();
	}
	
	public void cargaTiposTelefono(){
		if(this.listTmpTiposTelefono!=null && !this.listTmpTiposTelefono.isEmpty())
			return;
		try {
			this.listTmpTiposTelefono = this.ifzClientes.buscartTiposTelefono();
			
			if(this.listTiposTelefono==null) 
				this.listTiposTelefono = new ArrayList<SelectItem>();
			else
				this.listTiposTelefono.clear();
			for(ConValores cv : this.listTmpTiposTelefono) {
				this.listTiposTelefono.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaTiposTelefono", e);
		}
	}
	
	public void cargaCompaniasTel(){
		if(this.listTmpCompaniaTel!=null && !this.listTmpCompaniaTel.isEmpty())
			return;
		try {
			this.listTmpCompaniaTel = this.ifzClientes.buscarCompaniasTel();
			
			if(this.listCompaniaTel==null) 
				this.listCompaniaTel = new ArrayList<SelectItem>();
			else
				this.listCompaniaTel.clear();
			for(ConValores cv : this.listTmpCompaniaTel){
				this.listCompaniaTel.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaCompaniasTel", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscar(){		
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzNegocios.buscarNegocios(this.tipoBusqueda,this.valorBusqueda);// this.ifzNegocio.findLikePojoCompleto(this.tipoBusqueda,this.valorBusqueda);		
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.listNegocios = (List<Negocio>) respuesta.getBody().getValor("listNegocios");
				if (this.listNegocios.isEmpty()){
					this.resOperacion=busquedaVacia;
				}	
			} else {
				this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarContactos(){
		try{
			this.resOperacion = "";
			
			Respuesta respuesta = ifzNegocios.buscarContactos(pojoNegocio, valorBusquedaEstatusContacto);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listContactos = (List<ContactoNegocioExt>) respuesta.getBody().getValor("listContactos");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("error al buscar contactos", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarGiro(){
		try{
			this.resOperacion = "";
			
			Respuesta respuesta = ifzNegocios.buscarGiros(valorBusquedaGiro);
			
			if(respuesta.getErrores().getCodigoError() == 0L)
				listGirosC = (List<ConValores>)respuesta.getBody().getValor("listGiros");
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e){
			this.resOperacion = problemInesp;
			log.error("Error al buscar giros", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarPersonas(){		
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzNegocios.buscarPersonas(this.tipoBusquedaPersona, valorBusquedaPersonas);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.listPersona = (List<PersonaExt>) respuesta.getBody().getValor("listPersonas");
				if (this.listPersona.isEmpty()){
					this.resOperacion=busquedaVacia;
				}	
			} else {
				this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarPersonas", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarPersonasContacto(){		
		try {
			this.resOperacion = "";
			
			Respuesta respuesta = this.ifzNegocios.buscarPersonas(this.tipoBusquedaContacto, valorBusquedaPersonasContacto);
			try{
				this.listPersonaContacto = (List<PersonaExt>) respuesta.getBody().getValor("listPersonas");
				if (this.listPersonaContacto.isEmpty()){
					this.resOperacion=busquedaVacia;
				}	
			} catch (Exception e) {
				this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarPersonas", e);
		}
	}
	
	public void editarNegocio(){
		try{
			this.resOperacion = "";
			
			if(pojoNegocio.getGiro() != null && pojoNegocio.getGiro() > 0){
				Respuesta respuesta = ifzNegocios.buscarGiro(pojoNegocio.getGiro());
				
				if(respuesta.getErrores().getCodigoError() == 0L)
					pojoGiro = (ConValores) respuesta.getBody().getValor("pojoGiro");
				else
					this.resOperacion = respuesta.getErrores().getDescError();
			} 
		} catch (Exception e){
			this.resOperacion = problemInesp;
			log.error("Error al editar negocio", e);
		}
	}
	
	public void editarPersonaNegocioAsociada(){
		try {
			//pojoPersona = pojoPersonaNegocio.getApoderadoId();
			if(pojoPersonaNegocio.getApoderadoId() != null){
				if(pojoPersonaNegocio.getApoderadoId().getId() > 0){
					idApoderado = pojoPersonaNegocio.getApoderadoId().getId() + "-" + pojoPersonaNegocio.getApoderadoId().getNombreCompleto();
				}
			}
			if(pojoPersonaNegocio.getNegocio() != null){
				if(pojoPersonaNegocio.getNegocio().getId() > 0){
					pojoNegocio = pojoPersonaNegocio.getNegocio();
				}
			}
			this.resOperacion = "";			
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo editarPersonaNegocioAsociada", e);
		}
	}
	
	public void asociarPersona() {
		try {		
			//Persona persona = ifzClientes.buscarPersona(pojoPersona.getId());
			this.pojoPersonaNegocio = new PersonaNegocioExt();
			this.pojoPersonaNegocio.setPersona(pojoPersona);			
			if (this.pojoNegocio.getId() > 0){
				this.pojoPersonaNegocio.setNegocio(pojoNegocio);
				Respuesta respuesta = this.ifzNegocios.salvarPersonaNegocio(this.pojoPersonaNegocio);
				
				if(respuesta.getErrores().getCodigoError() == 0L){
					pojoPersonaNegocio = (PersonaNegocioExt) respuesta.getBody().getValor("pojoPersonaNegocio");
					
					this.listPersonasAsociadas.add(pojoPersonaNegocio);	
				} else
					this.resOperacion = respuesta.getErrores().getDescError();
			}			
		}		
		 catch (Exception e ) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo asociarPersona", e);
		}
	}
	
	public void asociarApoderado(){		
		try {
			//Persona persona = ifzClientes.buscarPersona(this.pojoPersona.getId());
			//this.pojoPersonaNegocio.setApoderadoId(pojoPersona);	
			this.idApoderado= this.pojoPersonaNegocio.getApoderadoId().getId()+" - "+ this.pojoPersonaNegocio.getApoderadoId().getNombreCompleto();
		}
		 catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo asociarApoderado", e);
		}
	}
	
	public void quitarApoderado(){		
		try {		
			
			this.pojoPersonaNegocio.setApoderadoId(null);	
			this.idApoderado= "";
		}
		 catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo quitarApoderado", e);
		}
	}
	
	public void estatusNegocio() {
		try{
			this.resOperacion = "";
			
			if(this.pojoNegocioElim != null) {
				preguntaEliminar = true;
				if(pojoNegocioElim.getEstatus() == 2L) {
					eliminarNegocio();
					preguntaEliminar = false;
				}
			} 
		} catch (Exception e){
			this.resOperacion = problemInesp;
			log.error("Error al editar negocio", e);
		}
	}
	
	public void guardar(){
		try {
			long id = 0L;
			Calendar t = Calendar.getInstance();
			this.resOperacion = "";
			
			id = pojoNegocio.getId();
			t.setTime(this.pojoNegocio.getInicioOperaciones());
			this.etiquetaAntiguedad= String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - t.get(Calendar.YEAR));
			
			this.ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = this.ifzNegocios.salvarNegocio(pojoNegocio);
			if(respuesta.getErrores().getCodigoError() != 0L){
				this.resOperacion = Errores.descError.get(respuesta.getErrores().getCodigoError());
				return;
			}
			
			this.pojoNegocio = (Negocio) respuesta.getBody().getValor("pojoNegocio");
			if(id == 0L) 
				this.listNegocios.add(0,pojoNegocio);
			guardarListaAsociados();
			
			/*if(respuesta.getErrores().getCodigoError() == 0L){
				pojoNegocio = (Negocio) respuesta.getBody().getValor("pojoNegocio");
				
				if(id == 0L) 
					this.listNegocios.add(0,pojoNegocio);
				
				guardarListaAsociados();
			} else {
				this.resOperacion = Errores.descError.get(respuesta.getErrores().getCodigoError());
				//this.resOperacion = Errores.descError.get(respuesta.getErrores().getErrores().get(0).getCodigoError());
				//this.resOperacion = respuesta.getErrores().getDescError();
			}*/
		}catch(Exception e){
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}
	}
	
	public void guardarCanal() {
		try {
			this.resOperacion = "";
		
			for (CanalesNegocioExt var : this.listCanalesNegocio) {
				if (var.getIdCanal().getId() == pojoCanalesNegocio.getIdCanal().getId()) {
					this.resOperacion = "El canal de venta indicado ya existe en la lista";
					return;
				}
			}
			
			CanalesNegocioExt pojoCanalesNegocioAux = new CanalesNegocioExt();
			pojoCanalesNegocioAux.setIdNegocio(pojoNegocio);
			pojoCanalesNegocioAux.setIdCanal(pojoCanalesNegocio.getIdCanal());

			ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = ifzNegocios.salvarCanal(pojoCanalesNegocioAux);
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoCanalesNegocioAux = (CanalesNegocioExt) respuesta.getBody().getValor("pojoCanal");
				
				listCanalesNegocio.add(pojoCanalesNegocioAux);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error al guardar canala", e);
		}
	}
	
	public void guardarContacto(){
		try{
			this.resOperacion = "";
			
			long id = pojoContactoNegocio.getId();
			
			if(pojoContactoNegocio.getIdNegocio() == null)
				pojoContactoNegocio.setIdNegocio(pojoNegocio);
			
			ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = ifzNegocios.salvarContacto(pojoContactoNegocio);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoContactoNegocio = (ContactoNegocioExt) respuesta.getBody().getValor("pojoContactoNegocio");
				
				if(id == 0)
					listContactos.add(pojoContactoNegocio);
				else{
					for(ContactoNegocioExt pojoContactoAux : listContactos){
						if(pojoContactoAux.getId() == pojoContactoNegocio.getId()){
							pojoContactoAux = pojoContactoNegocio;
						}
					}
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al guardar contacto", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void guardarEstadosNegocio(){
		try{
			this.resOperacion = "";
		
			for (EstadosNegocioExt var : this.listEstadosNegocio) {
				if (var.getIdEstado().getId() == pojoEstadosNegocio.getIdEstado().getId()) {
					this.resOperacion = "El estado indicado ya existe en la lista";
					return;
				}
			}
			
			EstadosNegocioExt pojoEstadosNegocioAux = new EstadosNegocioExt();
			pojoEstadosNegocioAux.setIdEstado(pojoEstadosNegocio.getIdEstado());
			pojoEstadosNegocioAux.setIdNegocio(pojoNegocio);

			ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = ifzNegocios.salvarEstadosNegocio(pojoEstadosNegocioAux);
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoEstadosNegocioAux = (EstadosNegocioExt) respuesta.getBody().getValor("pojoEstadosNegocio");
				listEstadosNegocio.add(pojoEstadosNegocioAux);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al guardar estados de negocio", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void guardarListaAsociados(){	
		try{
			this.resOperacion = "";

			ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			if(! this.listPersonasAsociadas.isEmpty()){
			
				for(PersonaNegocioExt personaNegocio : this.listPersonasAsociadas){
					if(personaNegocio.getNegocio()==null){
						personaNegocio.setNegocio(pojoNegocio);
						Respuesta respuesta = this.ifzNegocios.salvarPersonaNegocio(personaNegocio);
						
						if(respuesta.getErrores().getCodigoError() == 0L){
							personaNegocio = (PersonaNegocioExt) respuesta.getBody().getValor("pojoPersonaNegocio");
						} else
							this.resOperacion = respuesta.getErrores().getDescError();
					}
				}
			}				
			
		}catch(Exception e){
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarListaAsociados", e);
		}
		
	}
	
	public void guardarRelacionPersonaNegocio(){	
		try{
			this.resOperacion = "";

			ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			if(this.pojoPersonaNegocio.getParticipacion().intValue() > 100) {
				this.resOperacion=participacionSocio;
			} else {
				validarParticipacionSocios();
				if(this.pojoNegocio.getId() > 0){
					long id = pojoPersonaNegocio.getId();
					
					if (this.pojoPersonaNegocio.getPersona() == null) {
						this.resOperacion = "Debe seleccionar una persona";
						return;
					}
					
					this.pojoPersonaNegocio.setNegocio(pojoNegocio);
					
					Respuesta respuesta = ifzNegocios.salvarPersonaNegocio(pojoPersonaNegocio);
					if(respuesta.getErrores().getCodigoError() == 0L){
						pojoPersonaNegocio = (PersonaNegocioExt) respuesta.getBody().getValor("pojoPersonaNegocio");
						if(id == 0L)
							this.listPersonasAsociadas.add(pojoPersonaNegocio);
						
						// Copiamos los datos de NUMERO DE CUENTA, CLABE INTERBANCARIA y BANCO del representante si corresponde
						if (pojoPersonaNegocio.getRepresentante()) {
							if (this.pojoNegocio.getNumeroCuenta() == null || "".equals(this.pojoNegocio.getNumeroCuenta()))
								this.pojoNegocio.setNumeroCuenta(pojoPersonaNegocio.getPersona().getNumeroCuenta());

							if (this.pojoNegocio.getClabeInterbancaria() == null || "".equals(this.pojoNegocio.getClabeInterbancaria()))
								this.pojoNegocio.setClabeInterbancaria(pojoPersonaNegocio.getPersona().getClabeInterbancaria());

							if (this.pojoNegocio.getBanco() == null || this.pojoNegocio.getBanco() > 0L)
								this.pojoNegocio.setBanco(pojoPersonaNegocio.getPersona().getBanco());
						}
					} else
						this.resOperacion = respuesta.getErrores().getDescError();
				}
			}	
		}catch(Exception e){
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarRelacionPersonaNegocio", e);
		}
		
	}
	
	
	public void eliminarNegocio(){
		try {		
			this.resOperacion = "";

			ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = this.ifzNegocios.eliminarNegocio(this.pojoNegocioElim);	
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoNegocioElim = (Negocio) respuesta.getBody().getValor("pojoNegocio");
				for(Negocio pojoNegocioAux : listNegocios){
					if(pojoNegocioAux.getId() == pojoNegocioElim.getId())
						pojoNegocioAux.setEstatus(pojoNegocioElim.getEstatus());
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar", e);
		}
	}
	
	public void eliminarCanal(){
		try{
			this.resOperacion = "";

			ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = this.ifzNegocios.eliminarCanal(pojoCanalesNegocio);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listCanalesNegocio.remove(pojoCanalesNegocio);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al eliminar Canal", e);
		}
	}
	
	public void eliminarEstadosNegocio(){
		try{
			this.resOperacion = "";

			ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = this.ifzNegocios.eliminarEstadosNegocio(pojoEstadosNegocio);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listEstadosNegocio.remove(pojoEstadosNegocio);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al eliminar Estados de negocio", e);
		}
	}
	
	public void quitarPersonaAsociadaNegocio(){
		try {
			this.resOperacion = "";
			
			ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			if(pojoPersonaNegocio.getId() > 0){
				Respuesta respuesta = this.ifzNegocios.eliminarPersonaNegocio(this.pojoPersonaNegocio);
				
				if (respuesta.getErrores().getCodigoError() == 0L){
					this.listPersonasAsociadas.remove(this.pojoPersonaNegocio);
				} else
					this.resOperacion = respuesta.getErrores().getDescError();
			} else
				this.listPersonasAsociadas.remove(this.pojoPersonaNegocio);
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo quitarPersonaAsociadaNegocio", e);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void guardarDomicilio(){
		try {
			this.resOperacion = "";
			
			ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			if(! comprobarDireccion() || pojoColonia == null) {
				this.resOperacion = "Direccion no valida.";
				return;
			}
			
			pojoDomicilio.setColonia(pojoColonia);
			
			if(this.pojoDomicilio.getColonia() == null){
				this.resOperacion = "Es necesario seleccionar una colonia!";
				return;
			}
			
			long id = pojoDomicilio.getId();
			
			Respuesta respuesta = ifzNegocios.salvarDomicilio(pojoNegocio, pojoDomicilio);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoDomicilio = (DomicilioExt) respuesta.getBody().getValor("pojoDomicilio");
				
				if(id == 0L)
					this.listDomicilios.add(0, this.pojoDomicilio);
				else{
					for(DomicilioExt pojoDomicilioAux : listDomicilios)
						if(pojoDomicilioAux.getId() == id)
							pojoDomicilioAux.setDomicilio(pojoDomicilio.getDomicilio());
				}
				
				if(pojoDomicilio.getPrincipal()){
					Respuesta respuestaPrincipal = ifzNegocios.salvarDomicilioPrincipal(pojoNegocio, pojoDomicilio);
					
					if(respuestaPrincipal.getErrores().getCodigoError() == 0L){
						pojoDomicilio = (DomicilioExt)respuestaPrincipal.getBody().getValor("pojoDomicilio");
						
						this.pojoNegocio.setDomicilio(pojoDomicilio.getDomicilio());
						this.pojoNegocio.setColonia(pojoDomicilio.getColonia().getId());
						
						this.listDomicilios = (List<DomicilioExt>) respuestaPrincipal.getBody().getValor("listDomicilios");
					} else
						this.resOperacion = respuestaPrincipal.getErrores().getDescError();
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarDomicilio", e);
		}
	}
	
	public boolean comprobarDireccion() {
		if ("".equals(this.pojoDomicilio.getDescripcionDomicilio1().trim()) && 
				"".equals(this.pojoDomicilio.getDescripcionDomicilio2().trim()) && 
				"".equals(this.pojoDomicilio.getDescripcionDomicilio3().trim()) && 
				"".equals(this.pojoDomicilio.getDescripcionDomicilio4().trim()) && 
				"".equals(this.pojoDomicilio.getDescripcionDomicilio5().trim()))
			return false;
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void guardarDirectorio(){
		try {
			this.resOperacion = "";
			
			long id = pojoDirectorio.getId();
			pojoDirectorio.setTipoTelefonoDesc(getValorById(pojoDirectorio.getTipoTelefono(), listTmpTiposTelefono).getValor());
			
			if(id == 0)
				this.pojoDirectorio.setNegocio(pojoNegocio);

			ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta respuesta = ifzNegocios.salvarDirectorio(pojoDirectorio);
			if(respuesta.getErrores().getCodigoError() == 0L){
				if(pojoDirectorio.getPrincipal()) {
					listDirectorio = (List<DirectorioTelefonicoExt>) respuesta.getBody().getValor("listDirectorios");
					pojoNegocio = (Negocio) respuesta.getBody().getValor("pojoNegocio");
				} else {
					pojoDirectorio = (DirectorioTelefonicoExt) respuesta.getBody().getValor("pojoDirectorioTelefonico");
				}
			
				if(id == 0L) {
					this.listDirectorio.add(0, pojoDirectorio);
				} else {
					int index = -1;
					boolean encontrado = false;
					for (DirectorioTelefonicoExt t : this.listDirectorio) {
						index += 1;
						if (t.getId() == this.pojoDirectorio.getId()) {
							encontrado = true;
							break;
						}
					}
					
					if (encontrado)
						this.listDirectorio.set(index, this.pojoDirectorio);
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarDirectorio", e);
		}
	}
	
	public void cargaDomiciliosNegocio(){
		try {
			this.listDomicilios = ifzClientes.buscarDomicilioExt(this.pojoNegocio);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaDomiciliosNegocio", e);
		}
	}
	
	public void cargaDirectorioNegocio(){
		try {
			this.listDirectorio = this.ifzClientes.buscarDirectorioExt(pojoNegocio);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaDomiciliosNegocio", e);
		}
	}
	
	public void cargaPersonasAsociadasNegocio(){
		try {
			if(this.pojoNegocio.getId() > 0){
				this.listPersonasAsociadas = ifzNegocios.buscarPersonaNegocio(this.pojoNegocio);//this.ifzPersonaNegocio.findByProperty("negocio", this.pojoNegocio);
				this.resOperacion = "";
			}			
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaPersonasAsociadasNegocio", e);
		}
	}
	
	public void antiguedadNegocio(){
		try{
			Calendar t = Calendar.getInstance();
			t.setTime(this.pojoNegocio.getInicioOperaciones() != null ? this.pojoNegocio.getInicioOperaciones() : Calendar.getInstance().getTime());
			this.etiquetaAntiguedad = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - t.get(Calendar.YEAR)) + " AÑOS";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo antiguedadNegocio", e);
		}
	}
	
	public void buscarActividad(){
	    try{	
	    	this.etiquetaActividad = "";
	    	/*
	    	this.pojoConValores= this.ifzNegocios.buscarActividad(pojoNegocio);
			if (this.pojoConValores != null){				
				this.etiquetaActividad = "ACTIVIDAD: " + this.pojoConValores.getValor();
			}
			else{
				this.etiquetaActividad = "SIN ACTIVIDAD";
			}	
			*/
	    } catch (Exception e) {
	    	this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarActividad", e);
	    }				
		
	}
	
	public void buscarMunicipios(){
		try {
			if(editaNacimiento){
				listMunicipios = ifzClientes.buscarMunicipio(pojoEstadoNac,busquedaMunicipio);
			}else{
				listMunicipios = ifzClientes.buscarMunicipio(pojoEstadoDom,busquedaMunicipio);
			}
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarLocalidades", e);
		}
	}
	
	public void buscarLocalidades(){
		try {
			if(editaNacimiento){
				listLocalidades = ifzClientes.buscarLocalidad(pojoMunicipioNac,busquedaLocalidad);
			}else{
				listLocalidades = ifzClientes.buscarLocalidad(pojoMunicipioDom, busquedaLocalidad);
			}			
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarLocalidades", e);
		}
	}
	
	public void buscarColonias(){
		try {
			listColonias = this.ifzClientes.buscarColonia(pojoLocalidadDom, busquedaColonia);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarColonias", e);
		}
	}
	
	public void cambioEstadoDom(){
		try {
			if(listMunicipios!=null)
				listMunicipios.clear();
			if(listLocalidades!=null)
				listLocalidades.clear();
			if(listColonias!=null)
				listColonias.clear();
			
			pojoMunicipioDom = null;
			pojoLocalidadDom = null;
			pojoColonia = null;
			
		} catch (Exception e) {
			log.error("Error en metodo cambioEstadoDom", e);
		}
	}
	
	public void cambiarEstatusContacto(){
		try{
			Respuesta respuesta = ifzNegocios.cambiarEstatusContacto(pojoContactoNegocioEst);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				ContactoNegocioExt pojoContactoNegocioAux = (ContactoNegocioExt) respuesta.getBody().getValor("pojoContactoNegocio");
				
				pojoContactoNegocioEst.setEstatusId(pojoContactoNegocioAux.getEstatusId());
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al cambiar estatus de contacto", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void inicializaEstadoDom(){
		try {
			pojoLocalidadDom = null;
			pojoDomicilio.setColonia(null);
			if(listLocalidades!=null)
				listLocalidades.clear();
			if(listColonias!=null)
				listColonias.clear();
		} catch (Exception e) {
			log.error("Error en metodo inicializaEstadoDom", e);
		}
	}
	
	public void limpiaLocalidades(){
		if(listLocalidades!=null)
			listLocalidades.clear();
		else
			listLocalidades = new ArrayList<Localidad>();
	}
	
	public void limpiaMunicipio(){
		if(listMunicipios!=null)
			listMunicipios.clear();
		else
			listMunicipios = new ArrayList<Municipio>();
	}
	
	public void limpiaColonias(){
		if(listColonias!=null)
			listColonias.clear();
		else
			listColonias = new ArrayList<Colonia>();
		
	}
	
	
	public void validarParticipacionSocios(){
		BigDecimal sum= new BigDecimal(0);
		 try{	 
			 	this.resOperacion="";
			 	
		    	for(PersonaNegocioExt var:  this.listPersonasAsociadas){
		    		if (var.getParticipacion() != null)
		    			sum = var.getParticipacion().add(sum);
		    	}
				
		    	if ( sum.floatValue() > 100 ){		    		
					this.resOperacion = participacionSocios;
				}
		    	
		    } catch (Exception e) {
		    	this.resOperacion = problemInesp;
				log.error("Error en el metodo validarParticipacionSocios", e);
		    }	
	}
	
	public void filtrarLocalidades(){
	    try{	    	
	    	this.resOperacion="";
				
	    } catch (Exception e) {
	    	this.resOperacion = problemInesp;
			log.error("Error en el metodo filtrarLocalidades", e);
	    }				
		
	}
	
	@SuppressWarnings("unchecked")
	public void cargarTiposCanales(){
		this.resOperacion = "";
		try{
			Respuesta respuesta = ifzNegocios.cargarTiposCanales();
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listTiposCanales = (List<CanalCat>) respuesta.getBody().getValor("listTiposCanales");
				
				if(listTmpTiposCanales == null)
					listTmpTiposCanales = new ArrayList<SelectItem>();
				if(listTmpTiposCanales.size() > 0)
					listTmpTiposCanales.clear();
				
				for(CanalCat tipoCanal : listTiposCanales){
					listTmpTiposCanales.add(new SelectItem(tipoCanal.getId(), tipoCanal.getNombre()));
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al cargar tipos de canales", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public String getEstatusNegocio(Long idEstatus){
		return descEstatusNegocio.get(idEstatus);
	}
	
	public String getEstatusContacto(Long idEstatus){
		return descEstatusContacto.get(idEstatus);
	}
	
	private ConValores getValorById(Long id, List<ConValores> lista){
		for(ConValores cv :lista){
			if(cv.getId() == id.intValue())
				return cv;
		}
		return null;
	}
	
	private Colonia getColoniaById(Long id){
		for(Colonia c : this.listTmpColonia){
			if(c.getId() == id.intValue())
				return c;
		}
		return null;
	}
	
	public BigDecimal getParticipacion() {
		return this.pojoPersonaNegocio.getParticipacion() != null ? this.pojoPersonaNegocio.getParticipacion() :new BigDecimal(0);
	}
		
	public void setParticipacion(BigDecimal participacion) {
		this.pojoPersonaNegocio.setParticipacion(participacion.setScale(2, BigDecimal.ROUND_HALF_UP));
	}

	public Long getIdGiro() {
		return pojoNegocio.getGiro() != null ? pojoNegocio.getGiro() : -1L;
	}
	
	public void setIdGiro(Long idGiro) {
		this.pojoNegocio.setGiro(getValorById(idGiro, this.listTmpGiros).getId());
		
	}
	
	public Long getIdSector() {
		return pojoNegocio.getSector() != null ? pojoNegocio.getSector() : -1L;
	}
	
	public void setIdSector(Long idSector) {
		this.pojoNegocio.setSector(getValorById(idSector, this.listTmpSectores).getId());
	}
	
	public Long getIdColoniaDom() {
		return this.pojoDomicilio.getColonia().getId() > 0 ? this.pojoDomicilio.getColonia().getId() : -1;
	}
	
	public void setIdColoniaDom(Long idColoniaDom) {
		this.pojoDomicilio.setColonia(getColoniaById(idColoniaDom));
	}

	
	public Long getIdCompaniaTel() {
		return this.pojoDirectorio.getCompaniaTelefonica()!=null ? this.pojoDirectorio.getCompaniaTelefonica() : -1l;
	}
	
	public void setIdCompaniaTel(Long idCompaniaTel) {
		this.pojoDirectorio.setCompaniaTelefonica(getValorById(idCompaniaTel, listTmpCompaniaTel).getId());
	}
	
	public Long getIdTipoTel() {
		return this.pojoDirectorio.getTipoTelefono()!=null ? this.pojoDirectorio.getTipoTelefono() : -1l;
	}
	
	public void setIdTipoTel(Long idTipoTel) {
		this.pojoDirectorio.setTipoTelefono(getValorById(idTipoTel, listTmpTiposTelefono).getId());
	}
	
	public Negocio getPojoNegocio() {
		return pojoNegocio;
	}

	public void setPojoNegocio(Negocio pojoNegocio) {
		try{
			this.pojoNegocio = pojoNegocio;	
			if(pojoPersonaNegocio != null){
				if(pojoPersonaNegocio.getApoderadoId() != null){
					if(pojoPersonaNegocio.getApoderadoId().getId() > 0){
						PersonaExt personaExt = ifzClientes.buscarPersonaExt(this.pojoPersonaNegocio.getApoderadoId().getId());
						this.idApoderado= this.pojoPersonaNegocio.getApoderadoId() != null ? (this.pojoPersonaNegocio.getApoderadoId().getId() +" - "+ personaExt.getNombreCompleto() ):"" ;
					}
				}
			}
			buscarActividad();
			antiguedadNegocio();
			cargaPersonasAsociadasNegocio();
		} catch (Exception e) {
	    	this.resOperacion = problemInesp;
			log.error("Error en el metodo setPojoNegocio", e);
	    }	
	}
	
	

	public Date getInicioOperaciones() {
		return this.pojoNegocio.getInicioOperaciones() != null ? this.pojoNegocio.getInicioOperaciones() : Calendar.getInstance().getTime();
	}

	public void setInicioOperaciones(Date inicioOperaciones) {
		this.pojoNegocio.setInicioOperaciones(inicioOperaciones);		
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

	public String getEtiquetaAntiguedad() {
		return etiquetaAntiguedad;
	}

	public void setEtiquetaAntiguedad(String etiquetaAntiguedad) {
		this.etiquetaAntiguedad = etiquetaAntiguedad;
	}

	public String getEtiquetaActividad() {
		return etiquetaActividad;
	}

	public void setEtiquetaActividad(String etiquetaActividad) {
		this.etiquetaActividad = etiquetaActividad;
	}

	
	public Long getIdEstadoDom() {
		return pojoEstadoDom!=null && pojoEstadoDom.getId() > 0 ? pojoEstadoDom.getId() : 0l;
	}
	
	public void setIdEstadoDom(Long idEstadoDom) {
	
		try {
			pojoEstadoDom = getEstadoById(idEstadoDom);
		} catch (Exception e) {
			log.error("Error en metodo setIdEstadoDom",e);
		}
	}	

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	
	public List<Negocio> getListNegocios() {
		return listNegocios;
	}

	public void setListNegocios(List<Negocio> listNegocios) {
		this.listNegocios = listNegocios;
	}

	public List<SelectItem> getListGiros() {
		return listGiros;
	}

	public void setListGiros(List<SelectItem> listGiros) {
		this.listGiros = listGiros;
	}

	public List<SelectItem> getListSectores() {
		return listSectores;
	}

	public void setListSectores(List<SelectItem> listSectores) {
		this.listSectores = listSectores;
	}

	public List<DirectorioTelefonicoExt> getListDirectorio() {
		return listDirectorio;
	}

	public void setListDirectorio(List<DirectorioTelefonicoExt> listDirectorio) {
		this.listDirectorio = listDirectorio;
	}

	public List<DomicilioExt> getListDomicilios() {
		return listDomicilios;
	}

	public void setListDomicilios(List<DomicilioExt> listDomicilios) {
		this.listDomicilios = listDomicilios;
	}

	public List<SelectItem> getListCatDomicilios() {
		return listCatDomicilios;
	}

	public void setListCatDomicilios(List<SelectItem> listCatDomicilios) {
		this.listCatDomicilios = listCatDomicilios;
	}

	public int getNumPaginaDomicilio() {
		return numPaginaDomicilio;
	}

	public void setNumPaginaDomicilio(int numPaginaDomicilio) {
		this.numPaginaDomicilio = numPaginaDomicilio;
	}

	public int getNumPaginaDirectorio() {
		return numPaginaDirectorio;
	}

	public void setNumPaginaDirectorio(int numPaginaDirectorio) {
		this.numPaginaDirectorio = numPaginaDirectorio;
	}

	public DirectorioTelefonicoExt getPojoDirectorio() {
		return pojoDirectorio;
	}

	public void setPojoDirectorio(DirectorioTelefonicoExt pojoDirectorio) {
		this.pojoDirectorio = pojoDirectorio;
	}

    List<SelectItem> getListColonia() {
		return listColonia;
	}

	public void setListColonia(List<SelectItem> listColonia) {
		this.listColonia = listColonia;
	}

	public List<SelectItem> getListTiposTelefono() {
		return listTiposTelefono;
	}

	public void setListTiposTelefono(List<SelectItem> listTiposTelefono) {
		this.listTiposTelefono = listTiposTelefono;
	}

	public List<SelectItem> getListCompaniaTel() {
		return listCompaniaTel;
	}

	public void setListCompaniaTel(List<SelectItem> listCompaniaTel) {
		this.listCompaniaTel = listCompaniaTel;
	}

	public List<SelectItem> getListEstados() {
		return listEstados;
	}

	public void setListEstados(List<SelectItem> listEstados) {
		this.listEstados = listEstados;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

	public List<PersonaNegocioExt> getListPersonasAsociadas() {
		return listPersonasAsociadas;
	}

	public void setListPersonasAsociadas(List<PersonaNegocioExt> listPersonasAsociadas) {
		this.listPersonasAsociadas = listPersonasAsociadas;
	}

	public List<PersonaExt> getListPersona() {
		return listPersona;
	}

	public void setListPersona(List<PersonaExt> listPersona) {
		this.listPersona = listPersona;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
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

	public PersonaNegocioExt getPojoPersonaNegocio() {
		return pojoPersonaNegocio;
	}

	public void setPojoPersonaNegocio(PersonaNegocioExt pojoPersonaNegocio) {
		this.pojoPersonaNegocio = pojoPersonaNegocio;
		
	}

	public PersonaExt getPojoPersona() {
		return pojoPersona;
	}

	public void setPojoPersona(PersonaExt pojoPersona) {
		this.pojoPersona = pojoPersona;
	}

	public String getIdApoderado() {		
		return this.idApoderado;
	}

	public void setIdApoderado(String idApoderado) {
		this.idApoderado = idApoderado;
	}

	public int getNumPaginaBuscarPersona() {
		return numPaginaBuscarPersona;
	}

	public void setNumPaginaBuscarPersona(int numPaginaBuscarPersona) {
		this.numPaginaBuscarPersona = numPaginaBuscarPersona;
	}

	public int getNumPaginaBuscarApoderado() {
		return numPaginaBuscarApoderado;
	}

	public void setNumPaginaBuscarApoderado(int numPaginaBuscarApoderado) {
		this.numPaginaBuscarApoderado = numPaginaBuscarApoderado;
	}

	public int getNumPaginaPersonaAsociada() {
		return numPaginaPersonaAsociada;
	}

	public void setNumPaginaPersonaAsociada(int numPaginaPersonaAsociada) {
		this.numPaginaPersonaAsociada = numPaginaPersonaAsociada;
	}
	
	public void setPojoColonia(Colonia pojoColonia) {
		this.pojoColonia = pojoColonia;
	}

	public Colonia getPojoColonia() {
		return pojoColonia;
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

	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}

	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}

	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public List<Colonia> getListColonias() {
		return listColonias;
	}

	public void setListColonias(List<Colonia> listColonias) {
		this.listColonias = listColonias;
	}

	public List<SelectItem> getListEstado() {
		return listEstado;
	}

	public void setListEstado(List<SelectItem> listEstado) {
		this.listEstado = listEstado;
	}

	public Localidad getPojoLocalidad() {
		return editaNacimiento ? pojoLocalidadNac : pojoLocalidadDom;
	}
	
	public void setPojoLocalidad(Localidad localidad) {
		if(editaNacimiento)
			pojoLocalidadNac = localidad;
		else{
			pojoLocalidadDom = localidad;
			pojoColonia = null;
		}
	}


	public Localidad getPojoLocalidadDom() {
		return pojoLocalidadDom;
	}

	public void setPojoLocalidadDom(Localidad pojoLocalidadDom) {
		this.pojoLocalidadDom = pojoLocalidadDom;
	}

	public Municipio getPojoMunicipioDom() {
		return pojoMunicipioDom;
	}

	public void setPojoMunicipioDom(Municipio pojoMunicipioDom) {
		this.pojoMunicipioDom = pojoMunicipioDom;
	}

	public Estado getPojoEstadoDom() {
		return pojoEstadoDom;
	}

	public void setPojoEstadoDom(Estado pojoEstadoDom) {
		this.pojoEstadoDom = pojoEstadoDom;
	}

	
	public Estado getPojoEstado() {
		return editaNacimiento ? pojoEstadoNac : pojoEstadoDom;
	}
	
	public void setPojoEstado(Estado pojoEstadotNac) {/* nanai */}
	
	
	public Estado getPojoEstadoNac() {
		return pojoEstadoNac;
	}

	public void setPojoEstadoNac(Estado pojoEstadoNac) {
		this.pojoEstadoNac = pojoEstadoNac;
	}

	public Municipio getPojoMunicipioNac() {
		return pojoMunicipioNac;
	}

	public void setPojoMunicipioNac(Municipio pojoMunicipioNac) {
		this.pojoMunicipioNac = pojoMunicipioNac;
	}

	public int getNumPaginaLocalidad() {
		return numPaginaLocalidad;
	}

	public void setNumPaginaLocalidad(int numPaginaLocalidad) {
		this.numPaginaLocalidad = numPaginaLocalidad;
	}

	public int getNumPaginaMunicipio() {
		return numPaginaMunicipio;
	}

	public void setNumPaginaMunicipio(int numPaginaMunicipio) {
		this.numPaginaMunicipio = numPaginaMunicipio;
	}

	public int getNumPaginaColonia() {
		return numPaginaColonia;
	}

	public void setNumPaginaColonia(int numPaginaColonia) {
		this.numPaginaColonia = numPaginaColonia;
	}

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public String getBusquedaLocalidad() {
		return busquedaLocalidad;
	}

	public void setBusquedaLocalidad(String busquedaLocalidad) {
		this.busquedaLocalidad = busquedaLocalidad;
	}

	public String getBusquedaMunicipio() {
		return busquedaMunicipio;
	}

	public void setBusquedaMunicipio(String busquedaMunicipio) {
		this.busquedaMunicipio = busquedaMunicipio;
	}

	public String getBusquedaColonia() {
		return busquedaColonia;
	}

	public void setBusquedaColonia(String busquedaColonia) {
		this.busquedaColonia = busquedaColonia;
	}

	public Localidad getPojoLocalidadNac() {
		return pojoLocalidadNac;
	}

	public void setPojoLocalidadNac(Localidad pojoLocalidadNac) {
		this.pojoLocalidadNac = pojoLocalidadNac;
	}

	public boolean isEditaNacimiento() {
		return editaNacimiento;
	}

	public void setEditaNacimiento(boolean editaNacimiento) {
		this.editaNacimiento = editaNacimiento;
	}

	public PersonaExt getPojoPersonaApoderado() {
		return pojoPersonaApoderado;
	}

	public void setPojoPersonaApoderado(PersonaExt pojoPersonaApoderado) {
		this.pojoPersonaApoderado = pojoPersonaApoderado;
	}

	public String getValorBusquedaPersonas() {
		return valorBusquedaPersonas;
	}

	public void setValorBusquedaPersonas(String valorBusquedaPersonas) {
		this.valorBusquedaPersonas = valorBusquedaPersonas;
	}

	public String getValorBusquedaApoderados() {
		return valorBusquedaApoderados;
	}

	public void setValorBusquedaApoderados(String valorBusquedaApoderados) {
		this.valorBusquedaApoderados = valorBusquedaApoderados;
	}

	public Negocio getPojoNegocioElim() {
		return pojoNegocioElim;
	}

	public void setPojoNegocioElim(Negocio pojoNegocioElim) {
		this.pojoNegocioElim = pojoNegocioElim;
	}

	public HashMap<Long, String> getDescEstatusNegocio() {
		return descEstatusNegocio;
	}

	public void setDescEstatusNegocio(HashMap<Long, String> descEstatusNegocio) {
		this.descEstatusNegocio = descEstatusNegocio;
	}

	public CanalesNegocioExt getPojoCanalesNegocio() {
		return pojoCanalesNegocio;
	}

	public void setPojoCanalesNegocio(CanalesNegocioExt pojoCanalesNegocio) {
		this.pojoCanalesNegocio = pojoCanalesNegocio;
	}

	public List<CanalesNegocioExt> getListCanalesNegocio() {
		return listCanalesNegocio;
	}

	public void setListCanalesNegocio(List<CanalesNegocioExt> listCanalesNegocio) {
		this.listCanalesNegocio = listCanalesNegocio;
	}

	public List<SelectItem> getListTmpTiposCanales() {
		return listTmpTiposCanales;
	}

	public void setListTmpTiposCanales(List<SelectItem> listTmpTiposCanales) {
		this.listTmpTiposCanales = listTmpTiposCanales;
	}

	public int getNumPaginaCanales() {
		return numPaginaCanales;
	}

	public void setNumPaginaCanales(int numPaginaCanales) {
		this.numPaginaCanales = numPaginaCanales;
	}

	public List<ContactoNegocioExt> getListContactos() {
		return listContactos;
	}

	public void setListContactos(List<ContactoNegocioExt> listContactos) {
		this.listContactos = listContactos;
	}

	public Long getValorBusquedaEstatusContacto() {
		return valorBusquedaEstatusContacto;
	}

	public void setValorBusquedaEstatusContacto(Long valorBusquedaEstatusContacto) {
		this.valorBusquedaEstatusContacto = valorBusquedaEstatusContacto;
	}

	public ContactoNegocioExt getPojoContactoNegocio() {
		return pojoContactoNegocio;
	}

	public void setPojoContactoNegocio(ContactoNegocioExt pojoContactoNegocio) {
		this.pojoContactoNegocio = pojoContactoNegocio;
	}

	public int getNumPaginaContactos() {
		return numPaginaContactos;
	}

	public void setNumPaginaContactos(int numPaginaContactos) {
		this.numPaginaContactos = numPaginaContactos;
	}

	public HashMap<Long, String> getDescEstatusContacto() {
		return descEstatusContacto;
	}

	public void setDescEstatusContacto(HashMap<Long, String> descEstatusContacto) {
		this.descEstatusContacto = descEstatusContacto;
	}

	public String getValorBusquedaPersonasContacto() {
		return valorBusquedaPersonasContacto;
	}

	public void setValorBusquedaPersonasContacto(
			String valorBusquedaPersonasContacto) {
		this.valorBusquedaPersonasContacto = valorBusquedaPersonasContacto;
	}

	public String getTipoBusquedaContacto() {
		return tipoBusquedaContacto;
	}

	public void setTipoBusquedaContacto(String tipoBusquedaContacto) {
		this.tipoBusquedaContacto = tipoBusquedaContacto;
	}

	public int getNumPaginaPersonasContactos() {
		return numPaginaPersonasContactos;
	}

	public void setNumPaginaPersonasContactos(int numPaginaPersonasContactos) {
		this.numPaginaPersonasContactos = numPaginaPersonasContactos;
	}

	public List<PersonaExt> getListPersonaContacto() {
		return listPersonaContacto;
	}

	public void setListPersonaContacto(List<PersonaExt> listPersonaContacto) {
		this.listPersonaContacto = listPersonaContacto;
	}

	public ContactoNegocioExt getPojoContactoNegocioEst() {
		return pojoContactoNegocioEst;
	}

	public void setPojoContactoNegocioEst(ContactoNegocioExt pojoContactoNegocioEst) {
		this.pojoContactoNegocioEst = pojoContactoNegocioEst;
	}

	public List<EstadosNegocioExt> getListEstadosNegocio() {
		return listEstadosNegocio;
	}

	public void setListEstadosNegocio(List<EstadosNegocioExt> listEstadosNegocio) {
		this.listEstadosNegocio = listEstadosNegocio;
	}
	
	public void setEstadoAEstadosNegocio(long id){
		for(Estado estado : listTmpEstado){
			if(estado.getId() == id)
				pojoEstadosNegocio.setIdEstado(estado);
		}
	}
	
	public long getEstadoAEstadosNegocio(){
		return pojoEstadosNegocio.getIdEstado() != null ? pojoEstadosNegocio.getIdEstado().getId() : 0L;
	}

	public EstadosNegocioExt getPojoEstadosNegocio() {
		return pojoEstadosNegocio;
	}

	public void setPojoEstadosNegocio(EstadosNegocioExt pojoEstadosNegocio) {
		this.pojoEstadosNegocio = pojoEstadosNegocio;
	}

	public int getNumPaginaEstadosNegocio() {
		return numPaginaEstadosNegocio;
	}

	public void setNumPaginaEstadosNegocio(int numPaginaEstadosNegocio) {
		this.numPaginaEstadosNegocio = numPaginaEstadosNegocio;
	}

	public String getTipoBusquedaPersona() {
		return tipoBusquedaPersona;
	}

	public void setTipoBusquedaPersona(String tipoBusquedaPersona) {
		this.tipoBusquedaPersona = tipoBusquedaPersona;
	}

	public String getTipoBusquedaApoderado() {
		return tipoBusquedaApoderado;
	}

	public void setTipoBusquedaApoderado(String tipoBusquedaApoderado) {
		this.tipoBusquedaApoderado = tipoBusquedaApoderado;
	}

	public String getValorBusquedaGiro() {
		return valorBusquedaGiro;
	}

	public void setValorBusquedaGiro(String valorBusquedaGiro) {
		this.valorBusquedaGiro = valorBusquedaGiro;
	}

	public int getNumPaginaGiro() {
		return numPaginaGiro;
	}

	public void setNumPaginaGiro(int numPaginaGiro) {
		this.numPaginaGiro = numPaginaGiro;
	}

	public List<ConValores> getListGirosC() {
		return listGirosC;
	}

	public void setListGirosC(List<ConValores> listGirosC) {
		this.listGirosC = listGirosC;
	}

	public ConValores getPojoGiro() {
		return pojoGiro;
	}

	public void setPojoGiro(ConValores pojoGiro) {
		this.pojoGiro = pojoGiro;
	}

	public List<SelectItem> getListTmpBancos() {
		return listTmpBancos;
	}

	public void setListTmpBancos(List<SelectItem> listTmpBancos) {
		this.listTmpBancos = listTmpBancos;
	}


	public boolean isPreguntaEliminar() {
		return preguntaEliminar;
	}


	public void setPreguntaEliminar(boolean preguntaEliminar) {
		this.preguntaEliminar = preguntaEliminar;
	}
}

// HISTORIAL DE MODIFICACIONES 
//-------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA   |        AUTOR       | DESCRIPCION 
//-------------------------------------------------------------------------------------------------------------------
//   1.0   | 2016-09-20 | Javier Tirado      | Se corrigio el mensaje que muestra al habilitar un Negocio 
