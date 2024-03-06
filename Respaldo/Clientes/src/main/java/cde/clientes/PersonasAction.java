package cde.clientes;

/**
 * Gestion de personas, para manupular la informacion gral ademas de sus domicilios, nombres alternos, identificaciones, documentos, etc...
 * @author Omar Magdiel Aguayo Garcia
 * 
 * 
 */
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import javax.servlet.http.HttpSession;

import net.giro.clientes.beans.CatBancosExt;
import net.giro.clientes.beans.ContactoPersonaExt;
import net.giro.clientes.beans.DirectorioTelefonicoExt;
import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.beans.Domicilio;
import net.giro.clientes.beans.IdentificacionExt;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaDocumento;
import net.giro.clientes.beans.PersonaEstudioExt;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.beans.PersonaNegocioExt;
import net.giro.clientes.beans.PersonaNombresAlternoExt;
import net.giro.clientes.logica.ClientesRem;
import net.giro.comun.ExcepConstraint;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.beans.DocumentoDigitalizado;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.plataforma.seguridad.beans.Perfil;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;




public class PersonasAction implements Serializable {
	private static final long serialVersionUID = -59226872113117994L;
	Logger log = Logger.getLogger(PersonasAction.class);
	
	private AdministracionRem 	ifzAdministracion;
	private ClientesRem ifzClientes;

	//private List<Persona> listPersona;
	//private List<Persona> listConyuge;
	private List<PersonaExt> listPersonaExt;
	private List<PersonaExt> listPersonaApoderadoNegocioExt;
	private List<PersonaExt> listBusquedaPersonaContacto;
	private List<PersonaExt> listConyugeExt;
	private List<ConValores> listTmpEdoCivil;
	private List<ConValores> listTmpNacionalidad;
	private List<ConValores> listTmpTipoSangre;
	private List<ConValores> listTmpCatDomicilios1;
	private List<ConValores> listTmpCatDomicilios2;
	private List<ConValores> listTmpTiposTelefono;
	private List<ConValores> listTmpCompaniaTel;
	private List<ConValores> listTmpCarrera;
	private List<ConValores> listTmpNivelEstudios;
	private List<ConValores> listTmpTipoIdentificacion;
	private List<ConValores> listTmpTipoDocumento;
	private List<ConValores> listTmpDocumentos;
	private List<Estado> listTmpEstado;
	private List<Localidad> listLocalidades;
	private List<Municipio> listMunicipios;
	private List<Domicilio>	listDomicilios;
	private List<DomicilioExt>	listDomiciliosExt;
	private List<Colonia> listColonias;
	//private List<DirectorioTelefonico> listDirectorio;
	private List<DirectorioTelefonicoExt> listDirectorioExt;
	private List<PersonaNombresAlternoExt> listNombreAlterno;
	//private List<PersonaEstudio>	listEstudios;
	private List<PersonaEstudioExt>	listEstudiosExt;
	private List<IdentificacionExt>	listIdentificacionesExt;
	private List<PersonaDocumento>	listDocumentosPersona;
	private List<DocumentoDigitalizado>	listDocumentosPersonaVer;
	@SuppressWarnings("unused")
	private List<Perfil> listPerfiles;
	private List<PersonaNegocioExt> listPersonaNegocio;
	private List<Negocio> listNegocio;
	private List<ContactoPersonaExt> listPersonaContacto;
	private List<CatBancosExt> listBancos;

	private List<SelectItem> listSexo;
	private List<SelectItem> listEdoCivil;
	private List<SelectItem> listEstado;
	private List<SelectItem> listNacionalidad;
	private List<SelectItem> listTipoSangre;
	private List<SelectItem> listCatDomicilios1;
	private List<SelectItem> listCatDomicilios2;
	private List<SelectItem> listTiposTelefono;
	private List<SelectItem> listCompaniaTel;
	private List<SelectItem> listCarrera;
	private List<SelectItem> listNivelEstudios;
	private List<SelectItem> listTipoIdentificacion;
	private List<SelectItem> listTipoDocumento;
	private List<SelectItem> listDocumentos;
	private List<SelectItem> listTmpBancos;
		
	private PersonaExt	pojoPersonaExt;
	//private Domicilio	pojoDomicilio;
	private DomicilioExt	pojoDomicilioExt;
	private Estado pojoEstadoNac;
	private Estado pojoEstadoDom;
	private Localidad pojoLocalidadNac;
	private Localidad pojoLocalidadDom;
	private Municipio pojoMunicipioNac;
	private Municipio pojoMunicipioDom;
	private Colonia pojoColonia;
	//private DirectorioTelefonico pojoDirectorio;
	private DirectorioTelefonicoExt pojoDirectorioExt;
	private PersonaNombresAlternoExt pojoNombreAlterno;
	//private PersonaEstudio	pojoEstudio;
	private PersonaEstudioExt	pojoEstudioExt;
	private IdentificacionExt pojoIdentificacionExt;
	private DocumentoDigitalizado pojoDigitalizado;
	private PersonaNegocioExt pojoPersonaNagocio;
	private Negocio pojoNegocio;
	private PersonaExt pojoApoderadoPersonaNegocio;
	private ContactoPersonaExt pojoPersonaContacto;
	 
	
	private HttpSession httpSession;
	
	private byte[] archivo;
	
	private Context ctx; 
	
	private Long usuarioId;
	
	private int	numPagina;
	private int numPaginaDomicilio;
	private int numPaginaDirectorio;
	private int numPaginaNomAlt;
	private int numPaginaEstudio;
	private int numPaginaIdentificacion;
	private int numDoctoDigit;
	private int numDoctoDigitVersion;
	private int numPaginaLocalidad;
	private int numPaginaMunicipio;
	private int numPaginaColonia;
	private int numPaginaPersonaNegocio;
	private int numPaginaNegocio;
	private int numPaginaApoderado;
	private int numPaginaPersonaContacto;
	private int numPaginaBusquedaContacto;
	
	//variables de mensajes
	private String resOperacion;
	private String problemInesp;
	private String problemGetFtp;
	private String problemPutFtp;
	private String problemArchivoNec;
	
	private String estadoMensaje;
	private String valorBusqueda;
	private Long tipoPersonaBusqueda;
	private String valorBusquedaConyuge;
	private String tipoBusqueda;
	private String tipoBusquedaConyuge;
	@SuppressWarnings("unused")
	private String ftpHost;
	@SuppressWarnings("unused")
	private String ftpUser;
	@SuppressWarnings("unused")
	private String ftpPassword;
	@SuppressWarnings("unused")
	private String ftpRuta;
	private String busquedaLocalidad;
	private String busquedaMunicipio;
	private String busquedaColonia;
	private String tipoBusquedaNegocio;
	private String valorBusquedaNegocio;
	private String valorBusquedaApoderado;
	private String tipoBusquedaApoderado;
	private String tipoBusquedaContacto;
	private String valorBusquedaContacto;
	
	private boolean puedeEditar;
	private boolean editaNacimiento;
	
	LoginManager loginManager;
	
	@SuppressWarnings("unused")
	private Pattern pat = null;
	@SuppressWarnings("unused")
	private Matcher match = null;
	
	
	public PersonasAction() throws Exception{
		String tmp = "";
		String masculino, femenino = null;
		@SuppressWarnings("unused")
		List<ConValores> tmpPantalla = null;
		
		editaNacimiento = true;
		
		ctx = new InitialContext();
		numPagina = 1;
		numPaginaDomicilio = 1;
		numPaginaDirectorio = 1;
		numPaginaNomAlt = 1;
		numPaginaEstudio = 1;
		numPaginaIdentificacion = 1;
		numDoctoDigit = 1;
		numDoctoDigitVersion = 1;
		numPaginaLocalidad = 1;
		numPaginaColonia = 1;
		numPaginaMunicipio = 1;
		numPaginaPersonaNegocio = 1;
		numPaginaNegocio = 1;
		numPaginaApoderado = 1;
		tipoPersonaBusqueda = 1L;
		numPaginaPersonaContacto = 1;
		numPaginaBusquedaContacto = 1;
		
		pojoPersonaExt = new PersonaExt();
		//pojoDomicilio = new Domicilio();
		pojoDomicilioExt = new DomicilioExt();
		pojoEstadoNac = new Estado();
		pojoEstadoDom = new Estado();
		//pojoDirectorio = new DirectorioTelefonico();
		pojoDirectorioExt = new DirectorioTelefonicoExt();
		pojoNombreAlterno = new PersonaNombresAlternoExt();
		//pojoEstudio = new PersonaEstudio();
		pojoEstudioExt = new PersonaEstudioExt();
		pojoIdentificacionExt = new IdentificacionExt();
		pojoDigitalizado = new DocumentoDigitalizado();
		pojoPersonaNagocio = new PersonaNegocioExt();
		pojoPersonaContacto = new ContactoPersonaExt();
		
		listPersonaExt = new ArrayList<PersonaExt>();
		
		listSexo		= new ArrayList<SelectItem>();				
		listEdoCivil	= new ArrayList<SelectItem>();		
		listNacionalidad	= new ArrayList<SelectItem>();
		listTipoSangre	= new ArrayList<SelectItem>();
		listEstado		= new ArrayList<SelectItem>();
		listTipoDocumento	= new ArrayList<SelectItem>();
		listCarrera =  new ArrayList<SelectItem>();
		listTmpBancos = new ArrayList<SelectItem>();
		
		
		listTmpCatDomicilios1 =  new ArrayList<ConValores>();
		listTmpCatDomicilios2 =  new ArrayList<ConValores>();
		listTmpCarrera = new ArrayList<ConValores>();
		listPersonaNegocio = new ArrayList<PersonaNegocioExt>();
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
		problemGetFtp = propPlataforma.getString("mensajes.error.obtenerArchivoFpt");
		problemPutFtp = propPlataforma.getString("mensajes.error.enviarArchivoFpt");
		problemArchivoNec =  propPlataforma.getString("mensajes.error.archivoNecesario");
		masculino  =  propPlataforma.getString("masculino");
		femenino  =  propPlataforma.getString("femenino");
		
		ifzAdministracion =  (AdministracionRem)ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
		ifzClientes =  (ClientesRem)ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
		
		ifzClientes.setInfoSecion(loginManager.getInfoSesion());
		//busco el perfil para editar
		tmp = ifzAdministracion.buscarPerfilPersona("editar_personas");
		puedeEditar = !"s".equals(tmp);

		//obtengo el id de la pantalla para buscar los campos a habilitar 
		//tmpPantalla = ifzClientes.buscarPantallas();
		//loginManager.obtenerCamposHabilitados(tmpPantalla.isEmpty() || !puedeEditar ? null : tmpPantalla.get(0).getId(), Calendar.getInstance().getTime());
		
		//busco el perfil para editar
		tmp = ifzAdministracion.buscarPerfilPersona("alta_personas");		
		puedeEditar = !"s".equals(tmp);
		
		//rellenando listas selection
		this.listSexo.add(new SelectItem(1,femenino ));
		this.listSexo.add(new SelectItem(2,masculino ));
		
		this.listTmpEdoCivil = ifzClientes.buscarEstadosCiviles();
		for(ConValores cv : this.listTmpEdoCivil){
			this.listEdoCivil.add(new SelectItem(cv.getId(), cv.getValor()));
		}
		
		this.listTmpNacionalidad =  ifzClientes.buscarNacionalidades();
		for(ConValores cv : this.listTmpNacionalidad){
			this.listNacionalidad.add(new SelectItem(cv.getId(), cv.getValor()));
		}
		
		this.listTmpTipoSangre =  ifzClientes.buscarTiposDeSangre();
		for(ConValores cv : this.listTmpTipoSangre){
			this.listTipoSangre.add(new SelectItem(cv.getId(), cv.getValor()));
		}
		
		this.listTmpEstado = this.ifzClientes.buscarEstados();
		this.listEstado = new ArrayList<SelectItem>();
		for(Estado e : listTmpEstado){
			if(pojoEstadoNac == null)
				pojoEstadoNac = e;
			listEstado.add(new SelectItem(e.getId(), e.getNombre()));
		}
				
		cargarBancos();
		cargaInfoEstudios();
		cargaInfoDomicilio();
		cargaInfoDirectorio();		
		nuevoIdentificacion();
	}


	public void buscar(){
		try {
			this.resOperacion = "";
			this.listPersonaExt = this.ifzClientes.buscarPersonaExt(this.tipoBusqueda, valorBusqueda, 1L);
			//this.listPersonaExt = this.ifzClientes.buscarPersonaExt(this.tipoBusqueda, valorBusqueda, tipoPersonaBusqueda);
			if(this.listPersonaExt.isEmpty())
				this.resOperacion = "La busqueda no genero resultados";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
		
	}

	public void nuevo(){
		this.pojoPersonaExt = new PersonaExt();
		this.pojoPersonaExt.setTipoPersona(1L);
		pojoEstadoNac = null;
		pojoMunicipioNac = null;
		pojoLocalidadNac = null;
		pojoLocalidadDom = null;
		cargaInfoDirectorio();
		this.resOperacion = "";
	}

	public void guardar(){
		Respuesta respuesta = new Respuesta();
		this.resOperacion = "";
		long id = 0L;
		
		try {
			if(pojoLocalidadNac != null)
				pojoPersonaExt.setLocalidad(pojoLocalidadNac);

			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			id = pojoPersonaExt.getId();
			respuesta = ifzClientes.salvar(pojoPersonaExt);
			if(respuesta.getErrores().getCodigoError() > 0) {
				resOperacion = respuesta.getErrores().getDescError();
			} else {
				if(id > 0) {
					for(PersonaExt pojoPersonaAux : listPersonaExt){
						if(pojoPersonaAux.getId() == id){
							listPersonaExt.remove(pojoPersonaAux);
							break;
						}
					}
				}
				
				this.pojoPersonaExt = (PersonaExt)respuesta.getBody().getValor("persona");
				this.listPersonaExt.add(0, pojoPersonaExt);
			}
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}		
	}
	
	public void eliminar(){
		try {
			this.resOperacion = "";
			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			this.ifzClientes.eliminar(this.pojoPersonaExt);
			this.listPersonaExt.remove(this.pojoPersonaExt);
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminaFuncion", e);
		}
	}
	
	public void eliminarNombreAlt(){
		Respuesta respuesta = new Respuesta();
		try {
			this.resOperacion = "";
			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			respuesta = this.ifzClientes.eliminar(this.pojoNombreAlterno);
			if(respuesta.getErrores().getCodigoError() > 0){
				resOperacion = respuesta.getErrores().getDescError();
			}else{
				this.listNombreAlterno.remove(this.pojoNombreAlterno);
			}
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminarNombreAlt", e);
		}
	}
	
	public void eliminarEstudio(){
		try {
			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			this.ifzClientes.eliminar(this.pojoEstudioExt);
			this.listEstudiosExt.remove(this.pojoEstudioExt);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminarEstudio", e);
		}
	}
	
	public void eliminarIdentificacion(){
		Respuesta respuesta = new Respuesta();
		try {
			this.resOperacion = "";
			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			respuesta = this.ifzClientes.eliminar(this.pojoIdentificacionExt);
			if(respuesta.getErrores().getCodigoError() > 0){
				resOperacion = respuesta.getErrores().getDescError();
			}else{
				this.listIdentificacionesExt.remove(this.pojoIdentificacionExt);
				this.resOperacion = "Identificacion eliminada";
			}
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminarIdentificacion", e);
		}
	}
	
	public void deshabilitaDom(){
		Respuesta respuesta = new Respuesta();
		resOperacion = "";
		try {
			pojoDomicilioExt.setEstatus(!pojoDomicilioExt.getEstatus());
			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			respuesta = this.ifzClientes.salvar(pojoDomicilioExt);
			if(respuesta.getErrores().getCodigoError() > 0){
				resOperacion = respuesta.getErrores().getDescError();
			}else{
				this.pojoDomicilioExt = (DomicilioExt)respuesta.getBody().getValor("domicilio");
			}
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo deshabilitaDom", e);
		}
	}
	
	public void guardarDomicilio(){
		Respuesta respuesta = new Respuesta();
		this.resOperacion = "";
		
		try {
			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			if(! comprobarDireccion() || pojoColonia == null) {
				this.resOperacion = "Direccion no valida.";
				return;
			}
			
			this.pojoDomicilioExt.setColonia(pojoColonia);
			if (this.pojoDomicilioExt.getPrincipal()) {
				for (DomicilioExt var : this.listDomiciliosExt) {
					if (var.getPrincipal() && var.getId() != this.pojoDomicilioExt.getId()) {
						var.setPrincipal(false);
						ifzClientes.salvar(var);
					}
				}
			}
			
			if(pojoDomicilioExt.getId() <= 0) {
				respuesta = ifzClientes.salvar(this.pojoDomicilioExt);
				if(respuesta.getErrores().getCodigoError() > 0){
					resOperacion = respuesta.getErrores().getDescError();
				} else {
					this.pojoDomicilioExt = (DomicilioExt) respuesta.getBody().getValor("domicilio");
					this.listDomiciliosExt.add(0,this.pojoDomicilioExt);
				}
			}else{
				respuesta = ifzClientes.salvar(this.pojoDomicilioExt);
				if(respuesta.getErrores().getCodigoError() > 0){
					resOperacion = respuesta.getErrores().getDescError();
				} else {
					this.listDomiciliosExt.remove(this.pojoDomicilioExt);
					this.pojoDomicilioExt = (DomicilioExt) respuesta.getBody().getValor("domicilio");
					this.listDomiciliosExt.add(0, this.pojoDomicilioExt);
				}
			}
			
			if(pojoDomicilioExt.getPrincipal()){
				this.pojoPersonaExt.setDomicilio(pojoDomicilioExt.getDomicilio());
				this.pojoPersonaExt.setColonia(pojoDomicilioExt.getColonia());
				respuesta = this.ifzClientes.salvar(pojoPersonaExt);
				if(respuesta.getErrores().getCodigoError() > 0){
					resOperacion = respuesta.getErrores().getDescError();
				}else{
					this.pojoPersonaExt = (PersonaExt)respuesta.getBody().getValor("persona");
				}
			}
			
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarDomicilio", e);
		}
	}
	
	public boolean comprobarDireccion() {
		if ("".equals(this.pojoDomicilioExt.getDescripcionDomicilio1().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio2().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio3().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio4().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio5().trim()))
			return false;
		
		return true;
	}
	
	public void guardarDirectorio(){
		Respuesta respuesta = new Respuesta();
		this.resOperacion = "";
		try {

			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			if(this.pojoDirectorioExt.getId() <= 0){
				respuesta = this.ifzClientes.salvar(this.pojoDirectorioExt);
				if(respuesta.getErrores().getCodigoError() > 0){
					resOperacion = respuesta.getErrores().getDescError();
					return;
				}else{
					this.pojoDirectorioExt = (DirectorioTelefonicoExt)respuesta.getBody().getValor("directorio");
					this.listDirectorioExt.add(0, pojoDirectorioExt);
				}
			}else{
				respuesta = this.ifzClientes.salvar(this.pojoDirectorioExt);
				if(respuesta.getErrores().getCodigoError() > 0){
					resOperacion = respuesta.getErrores().getDescError();
					return;
				}else{
					this.listDirectorioExt.remove(pojoDirectorioExt);
					this.pojoDirectorioExt = (DirectorioTelefonicoExt)respuesta.getBody().getValor("directorio");
					this.listDirectorioExt.add(0, pojoDirectorioExt);
				}
			}
			
			if(pojoDirectorioExt.getPrincipal()){
				pojoPersonaExt.setTelefono(Long.valueOf(pojoDirectorioExt.getLada()).toString() + pojoDirectorioExt.getNumeroTelefono());
				respuesta = this.ifzClientes.salvar(pojoPersonaExt);
				if(respuesta.getErrores().getCodigoError() > 0){
					resOperacion = respuesta.getErrores().getDescError();
				}else{
					this.pojoPersonaExt = (PersonaExt)respuesta.getBody().getValor("persona");
				}
			}
			
			/*if(pojoDirectorio.isTraPrincipal()){
				this.ifzDirectorio.definePrincipal(pojoDirectorio);
				this.pojoPersona.setTelefono(pojoDirectorio.getNumeroTelefono());
				this.ifzPersona.update(pojoPersona);
				cargaDirectorioPersona();
			}*/
			
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarDirectorio", e);
		}
	}
	
	public void guardarNombreAlterno(){
		Respuesta respuesta = new Respuesta();
		try {
			this.resOperacion = "";

			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			if(this.pojoNombreAlterno.getId() <= 0){
				
				respuesta = this.ifzClientes.salvar(this.pojoNombreAlterno);
				if(respuesta.getErrores().getCodigoError() > 0){
					resOperacion = respuesta.getErrores().getDescError();
				}else{
					this.pojoNombreAlterno = (PersonaNombresAlternoExt)respuesta.getBody().getValor("nombreAlterno");
				}
				this.listNombreAlterno.add(0, pojoNombreAlterno);
			}else
				this.ifzClientes.salvar(this.pojoNombreAlterno);
			
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarNombreAlterno", e);
		}
	}
	
	public void guardarEstudio(){
		try {
			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			this.pojoEstudioExt.setModificadoPor(Long.valueOf(this.usuarioId));
			this.pojoEstudioExt.setFechaModificacion(Calendar.getInstance().getTime());
			
			if(this.pojoEstudioExt.getId() <= 0){
				this.pojoEstudioExt.setCreadoPor(Long.valueOf(this.usuarioId));
				this.pojoEstudioExt.setFechaCreacion(Calendar.getInstance().getTime());
				Persona persona = ifzClientes.buscarPersona(pojoPersonaExt.getId());
				this.pojoEstudioExt.setPersona(persona);
				this.pojoEstudioExt.setId(this.ifzClientes.salvar(this.pojoEstudioExt));
				this.listEstudiosExt.add(0, pojoEstudioExt);
			}else{
				this.ifzClientes.salvar(this.pojoEstudioExt);
			}
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarEstudio", e);
		}
	}
	
	public void guardarIdentificacion(){
		try {
			this.resOperacion = "";

			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			this.pojoIdentificacionExt.setPersona(new Persona());
			this.pojoIdentificacionExt.getPersona().setId(pojoPersonaExt.getId());
			
			long id = pojoIdentificacionExt.getId();
			
			Respuesta respuesta = ifzClientes.salvar(this.pojoIdentificacionExt);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.pojoIdentificacionExt = (IdentificacionExt) respuesta.getBody().getValor("pojoIdentificacion");
				
				if(id == 0L){
					this.listIdentificacionesExt.add(0, pojoIdentificacionExt);
				} else {
					List<IdentificacionExt> listIdentificacionesAux = new ArrayList<IdentificacionExt>();
					for(IdentificacionExt pojoIdentificacionAux : listIdentificacionesExt){
						if(pojoIdentificacionAux.getId() != id){
							listIdentificacionesAux.add(pojoIdentificacionAux);
						}
					}
					listIdentificacionesAux.add(pojoIdentificacionExt);
					
					listIdentificacionesExt = listIdentificacionesAux;
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarIdentificacion", e);
		}
	}
	
	public void guardarPersonaNegocio(){
		Respuesta respuesta = new Respuesta();
		try{
			this.resOperacion = "";
			
			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			if(this.pojoPersonaNagocio.getId() <= 0){
				respuesta = this.ifzClientes.salvar(this.pojoPersonaNagocio);
				if(respuesta.getErrores().getCodigoError() > 0){
					resOperacion = respuesta.getErrores().getDescError();
				}else{
					this.pojoPersonaNagocio = (PersonaNegocioExt)respuesta.getBody().getValor("personaNegocio");
					this.listPersonaNegocio.add(0, pojoPersonaNagocio);
				}
			}else{
				respuesta = this.ifzClientes.salvar(this.pojoPersonaNagocio);
				if(respuesta.getErrores().getCodigoError() > 0){
					resOperacion = respuesta.getErrores().getDescError();
				}else{
					this.listPersonaNegocio.remove(pojoPersonaNagocio);
					this.pojoPersonaNagocio = (PersonaNegocioExt)respuesta.getBody().getValor("personaNegocio");
					this.listPersonaNegocio.add(0, pojoPersonaNagocio);
				}
			}
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarPersonaNegocio", e);
		}
	}
	
	public void guardarPersonaContacto(){
		Respuesta respuesta = new Respuesta();
		try{
			this.resOperacion = "";
			ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			if(this.pojoPersonaContacto.getId() <= 0){
				respuesta = this.ifzClientes.salvar(this.pojoPersonaContacto);
				if(respuesta.getErrores().getCodigoError() > 0){
					resOperacion = respuesta.getErrores().getDescError();
				}else{
					this.pojoPersonaContacto = (ContactoPersonaExt)respuesta.getBody().getValor("contactoPersonaExt");
					this.listPersonaContacto.add(0, pojoPersonaContacto);
				}
			}else{
				respuesta = this.ifzClientes.salvar(this.pojoPersonaContacto);
				if(respuesta.getErrores().getCodigoError() > 0){
					resOperacion = respuesta.getErrores().getDescError();
				}else{
					this.listPersonaContacto.remove(pojoPersonaContacto);
					this.pojoPersonaContacto = (ContactoPersonaExt)respuesta.getBody().getValor("contactoPersonaExt");
					this.listPersonaContacto.add(0, pojoPersonaContacto);
				}
			}
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarPersonaNegocio", e);
		}
	}
	
	@SuppressWarnings("null")
	public void guardarDocumentoPersona(){
		@SuppressWarnings("unused")
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		@SuppressWarnings("unused")
		byte[] archivoSrc = null;
		@SuppressWarnings("unused")
		Long numeroDoc = null;
		try {
			if(archivo == null){
				this.resOperacion = problemArchivoNec;
				return;
			}
			
			//this.pojoDocumento.setModificadoPor(Long.valueOf(this.usuarioId));
			//this.pojoDocumento.setFechaModificacion(Calendar.getInstance().getTime());
			
			/*if(ifzDigitalizado == null){
				this.lookup = ctx.lookup("DocumentoDigitalizadoFacade/remote");
				ifzDigitalizado = (DocumentoDigitalizadoFacadeRemote) PortableRemoteObject.narrow(lookup, DocumentoDigitalizadoFacadeRemote.class);
			}*/
			
			//guardo primero el documento digitalizado para despues usarlo en el documentoPersona
			pojoDigitalizado.setModificadoPor(Long.valueOf(this.usuarioId));
			pojoDigitalizado.setFechaModificacion(Calendar.getInstance().getTime());
			pojoDigitalizado.setCreadoPor(Long.valueOf(this.usuarioId));
			pojoDigitalizado.setFechaCreacion(Calendar.getInstance().getTime());
			pojoDigitalizado.setTamano(archivo.length);

			/*if(pojoDigitalizado.getNumero() != null)
				pojoDigitalizado.setVersion(this.ifzDocumento.getNextVersion(this.pojoPersona, this.pojoDocumento.getDocumentoDigit().getNumero()));
			else{
				pojoDigitalizado.setVersion(1L);
				numeroDoc = ifzDocumento.getSecuenciaGrupo();
				pojoDigitalizado.setNumero(numeroDoc);
			}*/
			//this.pojoDigitalizado.setId(this.ifzDigitalizado.save(this.pojoDigitalizado));
			
			//guardo el documento de la persona
			/*this.pojoDocumento.setDocumentoDigit(pojoDigitalizado);
			if(pojoDocumento.getId()==null){
				pojoDocumento.setCreadoPor(Long.valueOf(usuarioId));
				pojoDocumento.setFechaCreacion(Calendar.getInstance().getTime());
				pojoDocumento.setPersona(pojoPersona);
				pojoDocumento.setId(ifzDocumento.save(pojoDocumento));
				listDocumentosPersona.add(0, this.pojoDocumento);
			}else{
				ifzDocumento.update(pojoDocumento);
			}*/
			
			
			
			bis.read(archivo);
			bis.close();
			
			//envio el archivo al servidor ftp
			try {
				//ifzFtp.setInfo(this.ftpHost, this.ftpUser, this.ftpPassword);
				//ifzFtp.putArchivo(archivoSrc, ftpRuta + pojoDigitalizado.getId().toString() + pojoDigitalizado.getExtension());
				this.resOperacion = "";
			} catch (Exception e) {
				this.resOperacion = problemPutFtp;
				log.error("Error en el metodo guardarDocumento", e);
			}
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardarDocumento", e);
		}
	}
	
	public void guardarDocumentoPersonaObservacion(){
		try {
			/*this.pojoDocumento.setModificadoPor(Long.valueOf(this.usuarioId));
			this.pojoDocumento.setFechaModificacion(Calendar.getInstance().getTime());
			
			if(ifzDigitalizado == null){
				this.lookup = ctx.lookup("DocumentoDigitalizadoFacade/remote");
				ifzDigitalizado = (DocumentoDigitalizadoFacadeRemote) PortableRemoteObject.narrow(lookup, DocumentoDigitalizadoFacadeRemote.class);
			}*/
			resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en metodo guardarDocumentoPersonaObservacion");
		}
	}
	
	@SuppressWarnings("unused")
	public void descargarArchivo(){
		byte[] flujo = null;
		String appView = null;
		String nombre = null;
		String ext = null;
		try {
			//ifzFtp.setInfo(this.ftpHost, this.ftpUser, this.ftpPassword);
			//flujo = ifzFtp.getArchivo(this.ftpRuta + pojoDigitalizado.getId().toString() + pojoDigitalizado.getExtension());
			if(flujo == null){
				this.resOperacion = this.problemGetFtp;
				return;
			}
			
			httpSession.setAttribute("contenido", flujo);
			
			//obtengo el tipo de aplicacion a mostrar en el explorador, el nombre y lo almaceno en la session
			appView = pojoDigitalizado.getTipoDocumento().getAtributo2();
			ext = pojoDigitalizado.getExtension();
			if("image/".equals(appView) && pojoDigitalizado.getExtension()!=null)
				appView+=ext.replace(".", "");
			
			if(!"".equals(appView) && appView!=null)
				httpSession.setAttribute("aplicacion", appView);
			
			nombre = pojoDigitalizado.getDocumento().getValor();
			if("".equals(nombre) || nombre == null )
				nombre = "archivo";
			nombre+=ext;
			
			httpSession.setAttribute("nombre", nombre);
			
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo descargarArchivo", e);
		}
	}
	
	public void editarNacimiento(boolean valor){
		editaNacimiento = valor;
	}
	
	public void nuevoDomicilio() throws ExcepConstraint{
		this.resOperacion = "";
		try{
			this.pojoDomicilioExt = new DomicilioExt();
			this.pojoDomicilioExt.setEstatus(true);
			this.pojoDomicilioExt.setPersona(this.pojoPersonaExt);
			pojoEstadoDom = null;
			pojoMunicipioDom = null;
			pojoLocalidadDom = null;
			pojoColonia = null;
			cargaInfoDomicilio();
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo nuevoDomicilio", e);
		}
	}
	
	public void nuevoDirectorio(){
		//this.pojoDirectorio = new DirectorioTelefonico();
		this.pojoDirectorioExt = new DirectorioTelefonicoExt();
		this.pojoDirectorioExt.setPersona(pojoPersonaExt);
		cargaInfoDirectorio();
		this.resOperacion = "";
	}
	
	public void nuevoNombreAlterno(){
		this.pojoNombreAlterno = new PersonaNombresAlternoExt();
		this.pojoNombreAlterno.setPersona(pojoPersonaExt);
		this.resOperacion = "";
	}
	
	public void nuevoEstudio(){
		this.pojoEstudioExt = new PersonaEstudioExt();
		cargaInfoEstudios();
		this.resOperacion = "";
	}
	
	public void nuevoIdentificacion(){
		this.pojoIdentificacionExt = new IdentificacionExt();
		cargaInfoIdentificaciones();
		this.resOperacion = "";
	}
	
	public void nuevoDocumentoPersona(){
		this.pojoDigitalizado = new DocumentoDigitalizado();
		cargaInfoDocumentos();
		this.resOperacion = "";
	}
	
	public void nuevoDocumentoVersionPersona(){
////		Long numero = this.pojoDocumento.getDocumentoDigit().getNumero();
////		ConValores tipoDocto = this.pojoDocumento.getDocumentoDigit().getTipoDocumento();
////		ConValores docto = this.pojoDocumento.getDocumentoDigit().getDocumento();
//		this.pojoDigitalizado = new DocumentoDigitalizado();
//		this.pojoDigitalizado.setNumero(numero);
//		this.pojoDigitalizado.setTipoDocumento(tipoDocto);
//		this.pojoDigitalizado.setDocumento(docto);
		cargaInfoDocumentos();
		this.resOperacion = "";
	}
	
	public void nuevoPersonaNegocio(){
		pojoPersonaNagocio =  new PersonaNegocioExt();
		pojoPersonaNagocio.setPersona(pojoPersonaExt);
		pojoNegocio = new Negocio();
		pojoApoderadoPersonaNegocio = new PersonaExt();
		this.resOperacion = "";
	}
	
	public void nuevoPersonaContacto(){
		pojoPersonaContacto = new ContactoPersonaExt();
		pojoPersonaContacto.setIdPersona(pojoPersonaExt);
		this.resOperacion = "";
	}
	
	public void cargaDatosPersona(){
		try{
			pojoLocalidadDom = null;
			pojoMunicipioNac = null;
			pojoEstadoNac = null;
			if(pojoPersonaExt != null){
				if(pojoPersonaExt.getLocalidad() != null){
					this.pojoLocalidadDom = this.pojoPersonaExt.getLocalidad();
					if(pojoLocalidadDom != null){
						if(pojoLocalidadDom.getMunicipio() != null){
							this.pojoMunicipioNac = this.pojoLocalidadDom.getMunicipio();
							if(pojoMunicipioNac != null){
								if(pojoMunicipioNac.getEstado() != null){
									pojoEstadoNac = pojoMunicipioNac.getEstado();
								}
							}
						}
					}
				}
			}
		}catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}

	public void buscarConyuge(){
		try {
			this.listConyugeExt = this.ifzClientes.buscarConyugePersonaExt(this.tipoBusquedaConyuge, this.valorBusquedaConyuge);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarConyuge", e);
		}
	}
	
	public void buscarApoderado(){
		try {
			this.listPersonaApoderadoNegocioExt = this.ifzClientes.buscarPersonaGeneral(this.tipoBusquedaApoderado, this.valorBusquedaApoderado);
			//this.listPersonaApoderadoNegocioExt = this.ifzClientes.buscarPersonaGeneral(this.tipoBusqueda, this.valorBusqueda);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarConyuge", e);
		}
	}
	
	public void buscarContacto(){
		try {
			this.listBusquedaPersonaContacto = this.ifzClientes.buscarPersonaGeneral(this.tipoBusquedaContacto, this.valorBusquedaContacto);
			//this.listBusquedaPersonaContacto = this.ifzClientes.buscarPersonaGeneral(this.tipoBusqueda, this.valorBusqueda);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarConyuge", e);
		}
	}
	
	public void buscarNegocios(){
		try{
			this.listNegocio = ifzClientes.buscarNegocios(this.tipoBusquedaNegocio, this.valorBusquedaNegocio);
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarNegocios", e);
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
	
	@SuppressWarnings("unchecked")
	public void buscarPersonaNegocio(){
		Respuesta respuesta = new Respuesta(); 
		listPersonaNegocio = new ArrayList<PersonaNegocioExt>();
		try{
			resOperacion = "";
			respuesta = ifzClientes.buscarPersonaNegocio(pojoPersonaExt);
			if(respuesta.getErrores().getCodigoError() > 0){
				resOperacion = respuesta.getErrores().getDescError();
			}else{
				listPersonaNegocio =  (List<PersonaNegocioExt>)respuesta.getBody().getValor("personaNegocio");
			}			
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscarPersonaNegocio", e);
		}
	}
	
	public void cargaDomiciliosPersona(){
		try {
			this.listDomicilios = this.ifzClientes.buscarDomicilio(pojoPersonaExt);
			this.listDomiciliosExt = this.ifzClientes.buscarDomicilioExt(pojoPersonaExt);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaDomiciliosPersona", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaDirectorioPersona(){
		Respuesta respuesta = new Respuesta();
		try {
			this.resOperacion = "";
			respuesta = this.ifzClientes.buscarDirectorioExt(this.pojoPersonaExt);
			if(respuesta.getErrores().getCodigoError() > 0){
				resOperacion = respuesta.getErrores().getDescError();
			}else{
				this.listDirectorioExt = (List<DirectorioTelefonicoExt>)respuesta.getBody().getValor("directorio");
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaDirectorioPersona", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaNombresPersona(){
		Respuesta respuesta = new Respuesta();
		try {
			this.resOperacion = "";
			respuesta = this.ifzClientes.buscarNombreAlterno(this.pojoPersonaExt);
			if(respuesta.getErrores().getCodigoError() > 0){
				resOperacion = respuesta.getErrores().getDescError();
			}else{
				this.listNombreAlterno = (List<PersonaNombresAlternoExt>)respuesta.getBody().getValor("nombreAlterno");
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaNombresPersona", e);
		}
	}
	
	public void cargaEstudios(){
		try {
			this.listEstudiosExt = ifzClientes.buscarEstudiosExt(this.pojoPersonaExt);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaEstudios", e);
		}
	}
	
	public void cargaIdentificaciones(){
		try {
			this.listIdentificacionesExt = this.ifzClientes.buscarIdentificacionesExt(this.pojoPersonaExt);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaIdentificaciones", e);
		}
	}
	
	public void cargaDoctosPersona(){
		Application app = null;
		ValueExpression ve = null;
		PropertyResourceBundle prop = null;
		
		try {
				app = FacesContext.getCurrentInstance().getApplication();
				ve = app.getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(), "#{entornoAcc}", PropertyResourceBundle.class);
				prop = (PropertyResourceBundle)ve.getValue(FacesContext.getCurrentInstance().getELContext());
				ftpHost = prop.getString("ftp.host");
				ftpUser = prop.getString("ftp.user");
				ftpPassword = prop.getString("ftp.password");
				ftpRuta = prop.getString("ftp.ruta");
				
			//this.listDocumentosPersona = this.ifzDocumento.findAgrupadoPersona(this.pojoPersona);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaDoctosDigitalizados", e);
		}
	}
	
	public void cargaDoctosPersonaVersiones(){
		try {
			//this.listDocumentosPersonaVer = this.ifzDigitalizado.findPersonaNumero(this.pojoDocumento.getDocumentoDigit().getNumero());
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaDoctosDigitalizados", e);
		}
	}
	
	public void cargaInfoDomicilio() throws ExcepConstraint{
		if(pojoDomicilioExt != null){
			//pojoDomicilio = ifzClientes.buscarDomicilio(pojoDomicilioExt.getId());
			if(pojoDomicilioExt.getColonia() != null){
				if(pojoDomicilioExt.getColonia().getId() > 0){
					pojoColonia = pojoDomicilioExt.getColonia();
					if(pojoColonia != null){
						pojoLocalidadDom = pojoColonia.getLocalidad();
						if(pojoLocalidadDom != null){
							pojoMunicipioDom = pojoLocalidadDom.getMunicipio();
							if(pojoMunicipioDom != null){
								pojoEstadoDom = pojoMunicipioDom.getEstado();
							}
						}
					}
				}
			}
		}
		cargaTiposDomicilio();
	}
	
	public void cargaInfoDirectorio(){
		cargaTiposTelefono();
		cargaCompaniasTel();
	}
	
	public void cargaInfoEstudios(){
		cargaNivelEstudios();
		cargaCarreras();
	}
	
	public void cargaInfoIdentificaciones(){
		cargaTiposIdentifiaciones();
	}
	
	
	
	public void cargaInfoPersonaNegocio(){
		try{
			if(this.pojoPersonaNagocio.getNegocio() != null){
				if(this.pojoPersonaNagocio.getNegocio().getId() > 0){
					pojoNegocio = ifzClientes.buscarNegocios(this.pojoPersonaNagocio.getNegocio().getId());
				}
			}
			
			if(this.pojoPersonaNagocio.getApoderadoId() != null){
				if(this.pojoPersonaNagocio.getApoderadoId().getId() > 0){
					pojoApoderadoPersonaNegocio = ifzClientes.buscarPersonaExt(this.pojoPersonaNagocio.getApoderadoId().getId());
				}
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaInfoPersonaNegocio", e);
		}
	}
	
	public void eliminarPersonaNegocio(){
		Respuesta respuesta = new Respuesta();
		try {
			respuesta = this.ifzClientes.eliminar(this.pojoPersonaNagocio);
			if(respuesta.getErrores().getCodigoError() > 0){
				resOperacion = respuesta.getErrores().getDescError();
			}else{
				this.listPersonaNegocio.remove(pojoPersonaNagocio);
			}
		} catch (ExcepConstraint e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo eliminarPersonaNegocio", e);
		}
	}
	
	public void eliminarPersonaContacto(){
		Respuesta respuesta = new Respuesta();
		try {
			respuesta = this.ifzClientes.eliminar(this.pojoPersonaContacto);
			if(respuesta.getErrores().getCodigoError() > 0){
				resOperacion = respuesta.getErrores().getDescError();
			}else{
				this.listPersonaContacto.remove(pojoPersonaContacto);
			}
		} catch (ExcepConstraint e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo eliminarPersonaContacto", e);
		}
	}
	
	public void cargaInfoDocumentos(){
		cargaTiposDocumento();
		cargaDocumentos();
	}
	
	public void cargaInfoFtp() throws Exception{
		Application app = null;
		ValueExpression ve = null;
		PropertyResourceBundle prop = null;
		
		app = FacesContext.getCurrentInstance().getApplication();
		ve = app.getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(), "#{entornoAcc}", PropertyResourceBundle.class);
		prop = (PropertyResourceBundle)ve.getValue(FacesContext.getCurrentInstance().getELContext());
		ftpHost = prop.getString("ftp.host");
		ftpUser = prop.getString("ftp.user");
		ftpPassword = prop.getString("ftp.password");
		ftpRuta = prop.getString("ftp.ruta");
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
			this.listTmpCompaniaTel =  this.ifzClientes.buscarCompaniasTel();
			
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
	
	public void cargaCarreras(){
		if(this.listTmpCarrera!=null && !this.listTmpCarrera.isEmpty())
			return;
		try {
			this.listTmpCarrera =  this.ifzClientes.buscarCarreras();
			this.listCarrera = new ArrayList<SelectItem>();
			
			if(this.listCarrera==null) 
				this.listCarrera = new ArrayList<SelectItem>();
			else
				this.listCarrera.clear();
			for(ConValores cv : this.listTmpNivelEstudios){
				this.listCarrera.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaCarreras", e);
		}
	}
	
	public void cargaNivelEstudios(){
		if(this.listTmpNivelEstudios!=null && !this.listTmpNivelEstudios.isEmpty())
			return;
		try {
			this.listTmpNivelEstudios =  this.ifzClientes.nivelEstudios();
			
			if(this.listNivelEstudios==null) 
				this.listNivelEstudios = new ArrayList<SelectItem>();
			else
				this.listNivelEstudios.clear();
			for(ConValores cv : this.listTmpNivelEstudios){
				this.listNivelEstudios.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaEstudios", e);
		}
	}
	
	public void cargaTiposIdentifiaciones(){
		if(this.listTmpTipoIdentificacion!=null && !this.listTmpTipoIdentificacion.isEmpty())
			return;
		try {
			this.listTmpTipoIdentificacion =  ifzClientes.tiposIdentificaciones();
			
			if(this.listTipoIdentificacion==null) 
				this.listTipoIdentificacion = new ArrayList<SelectItem>();
			else
				this.listTipoIdentificacion.clear();
			for(ConValores cv : this.listTmpTipoIdentificacion){
				this.listTipoIdentificacion.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaTiposIdentifiaciones", e);
		}
	}
	
	public void cargaTiposDocumento(){
		if(this.listTmpTipoDocumento!=null && !this.listTmpTipoDocumento.isEmpty())
			return;
		try {
			this.listTmpTipoDocumento = ifzClientes.tipoDocumentos();
			
			if(this.listTipoDocumento==null) 
				this.listTipoDocumento = new ArrayList<SelectItem>();
			else
				this.listTipoDocumento.clear();
			for(ConValores cv : this.listTmpTipoDocumento)
				this.listTipoDocumento.add(new SelectItem(cv.getId(), cv.getValor()));
		
			this.pojoDigitalizado.setTipoDocumento(listTipoDocumento.size() > 0 ? listTmpTipoDocumento.get(0) : null);
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaTiposDocumento", e);
		}
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
	
	public void cargaDocumentos(){
		if(this.listTmpDocumentos!=null && !this.listTmpDocumentos.isEmpty())
			return;
		try {
			this.listTmpDocumentos =  ifzClientes.documentos();
			
			if(this.listDocumentos==null) 
				this.listDocumentos = new ArrayList<SelectItem>();
			else
				this.listDocumentos.clear();
			for(ConValores cv : this.listTmpDocumentos){
				this.listDocumentos.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaDocumentos", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaInfoPersonaContacto(){
		Respuesta respuesta = new Respuesta();
		this.resOperacion = "";
		try {
			respuesta = this.ifzClientes.buscarPersonasContacto(this.pojoPersonaExt);
			if(respuesta.getErrores().getCodigoError() > 0){
				resOperacion = respuesta.getErrores().getDescError();
			}else{
				this.listPersonaContacto = (List<ContactoPersonaExt>)respuesta.getBody().getValor("personasContacto");
			}
		} catch (ExcepConstraint e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo eliminarPersonaNegocio", e);
		}
	}
	
	public void cambioEstadoNac() {
		try {
			if(listMunicipios!=null)
				listMunicipios.clear();
			if(listLocalidades!=null)
				listLocalidades.clear();
			
			pojoMunicipioNac = null;
			pojoLocalidadNac = null;
			if(pojoPersonaExt != null){
				pojoPersonaExt.setLocalidad(null);
			}
			editarNacimiento(true);
		} catch (Exception e) {
			log.error("Error en metodo cambioEstadoNac", e);
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
			editarNacimiento(false);
		} catch (Exception e) {
			log.error("Error en metodo cambioEstadoDom", e);
		}
	}
	
	public void inicializaMunicipiosNac(){
		try {
			if(listLocalidades!=null)
				listLocalidades.clear();
			if(listColonias!=null)
				listColonias.clear();
			
			pojoMunicipioNac = null;
			pojoLocalidadNac = null;
			
		} catch (Exception e) {
			log.error("Error en metodo inicializaEstadoNac", e);
		}
	}
	
	public void inicializaEstadoDom(){
		try {
			pojoLocalidadDom = null;
			pojoDomicilioExt.setColonia(null);
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
		editarNacimiento(false);
	}
	
	public void uploadListener(FileUploadEvent event){
		try{
	        this.archivo = event.getUploadedFile().getData();
	        //this.pojoDigitalizado.setExtension(Utilerias.getExtension(event.getUploadItem().getFileName().toLowerCase()));
		}catch(Exception re){log.error("Error en el metodo uploadListener", re);}
	}
	
	private ConValores getValorById(Long id, List<ConValores> lista){
		for(ConValores cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}
	
	private Estado getEstadoById(Long id){
		for(Estado e : this.listTmpEstado){
			if(e.getId() == id.longValue()) 
				return e;
		}
		return null;
	}
	
	public List<PersonaExt> getListPersona() {
		return listPersonaExt;
	}

	public void setListPersona(List<PersonaExt> listPersonaExt) {
		this.listPersonaExt = listPersonaExt;
	}

	public List<SelectItem> getListSexo() {
		return listSexo;
	}

	public void setListSexo(List<SelectItem> listSexo) {
		this.listSexo = listSexo;
	}

	public List<SelectItem> getListEdoCivil() {
		return listEdoCivil;
	}

	public void setListEdoCivil(List<SelectItem> listEdoCivil) {
		this.listEdoCivil = listEdoCivil;
	}

	public List<SelectItem> getListNacionalidad() {
		return listNacionalidad;
	}

	public void setListNacionalidad(List<SelectItem> listNacionalidad) {
		this.listNacionalidad = listNacionalidad;
	}

	public List<SelectItem> getListEstado() {
		return listEstado;
	}

	public void setListEstado(List<SelectItem> listEstado) {
		this.listEstado = listEstado;
	}

	public List<SelectItem> getListTipoSangre() {
		return listTipoSangre;
	}

	public void setListTipoSangre(List<SelectItem> listTipoSangre) {
		this.listTipoSangre = listTipoSangre;
	}

	public PersonaExt getPojoPersona() {
		return pojoPersonaExt;
	}

	public void setPojoPersona(PersonaExt pojoPersonaExt) {
		this.pojoPersonaExt = pojoPersonaExt;
		
		pojoEstadoNac  = pojoMunicipioNac != null ? pojoMunicipioNac.getEstado() : null;
		editarNacimiento(true);
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

	public String getProblemInesp() {
		return problemInesp;
	}

	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}

//get set de los campos a desplegar

	public String getEstadoMensaje() {
		return estadoMensaje;
	}
	
	public void setEstadoMensaje(String estadoMensaje) {
		//nada
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

	public void setIdEdoCivil(Long idEdoCivil) {
		this.pojoPersonaExt.setEstadoCivil(getValorById(idEdoCivil, this.listTmpEdoCivil));
	}

	public Long getIdEdoCivil() {
		return  pojoPersonaExt.getEstadoCivil() != null ? (pojoPersonaExt.getEstadoCivil().getId() > 0 ? pojoPersonaExt.getEstadoCivil().getId() : -1L) : -1L;
	}

	public void setIdTipoSangre(Long tipoSangre) {
		this.pojoPersonaExt.setTipoSangre(getValorById(tipoSangre, this.listTmpTipoSangre));
	}

	public Long getIdTipoSangre() {
		return pojoPersonaExt.getTipoSangre() != null ? (pojoPersonaExt.getTipoSangre().getId() > 0 ? pojoPersonaExt.getTipoSangre().getId() : -1L) : -1L;
	}
	
	public void setIdNacionalidad(Long nacionalidad) {
		this.pojoPersonaExt.setNacionalidad(getValorById(nacionalidad, this.listTmpNacionalidad));
	}

	public Long getIdNacionalidad() {
		return pojoPersonaExt.getNacionalidad() != null ? (pojoPersonaExt.getNacionalidad().getId() > 0 ? pojoPersonaExt.getNacionalidad().getId() : -1L) : -1L;
	}

	public void setListDomicilios(List<Domicilio> listDomicilios) {
		this.listDomicilios = listDomicilios;
	}

	public List<Domicilio> getListDomicilios() {
		return listDomicilios;
	}

	public void setNumPaginaDomicilio(int numPaginaDomicilio) {
		this.numPaginaDomicilio = numPaginaDomicilio;
	}

	public int getNumPaginaDomicilio() {
		return numPaginaDomicilio;
	}

	public void setPojoDomicilio(DomicilioExt pojoDomicilioExt) {
		try {
			this.pojoDomicilioExt = pojoDomicilioExt;
			pojoColonia = pojoDomicilioExt.getColonia();
							
			//pojoLocalidadDom = pojoColonia != null ? pojoColonia.getlocalidad() : null;
			pojoLocalidadDom = pojoColonia != null ? pojoPersonaExt.getLocalidad() : null;
			pojoMunicipioDom = pojoLocalidadDom != null ? pojoLocalidadDom.getMunicipio() : null;
			pojoEstadoDom = pojoMunicipioDom != null ? pojoMunicipioDom.getEstado() : null;
			
			editarNacimiento(false);
		} catch (Exception e) {
			log.error("Error en metodo setPojoDomicilio",e);
		}
			
	}

	public DomicilioExt getPojoDomicilio() {
		return pojoDomicilioExt;
	}

	public void setIdDom1(Long idDom1) {
		this.pojoDomicilioExt.setCatDomicilio1(getValorById(idDom1, this.listTmpCatDomicilios1).getId());
	}

	public Long getIdDom1() {
		return this.pojoDomicilioExt.getCatDomicilio1() != null ? this.pojoDomicilioExt.getCatDomicilio1() : -1L;
	}

	public void setIdDom2(Long idDom2) {
		this.pojoDomicilioExt.setCatDomicilio2(getValorById(idDom2, this.listTmpCatDomicilios1).getId());
	}

	public Long getIdDom2() {
		return this.pojoDomicilioExt.getCatDomicilio2() != null ? this.pojoDomicilioExt.getCatDomicilio2() : -1L;
	}
	
	public void setIdDom3(Long idDom3) {
		this.pojoDomicilioExt.setCatDomicilio3(getValorById(idDom3, this.listTmpCatDomicilios1).getId());
	}

	public Long getIdDom3() {
		return this.pojoDomicilioExt.getCatDomicilio3() != null ? this.pojoDomicilioExt.getCatDomicilio3() : -1L;
	}
	
	public void setIdDom4(Long idDom4) {
		this.pojoDomicilioExt.setCatDomicilio4(getValorById(idDom4, this.listTmpCatDomicilios2).getId());
	}

	public Long getIdDom4() {
		return this.pojoDomicilioExt.getCatDomicilio4() != null ? this.pojoDomicilioExt.getCatDomicilio4() : -1L;
	}
	
	public void setIdDom5(Long idDom5) {
		this.pojoDomicilioExt.setCatDomicilio5(getValorById(idDom5, listTmpCatDomicilios2).getId());
	}

	public Long getIdDom5() {
		return this.pojoDomicilioExt.getCatDomicilio5() != null ? this.pojoDomicilioExt.getCatDomicilio5() : -1L;
	}
	
	public void setValorBusquedaConyuge(String valorBusquedaConyuge) {
		this.valorBusquedaConyuge = valorBusquedaConyuge;
	}

	public String getValorBusquedaConyuge() {
		return valorBusquedaConyuge;
	}

	public void setTipoBusquedaConyuge(String tipoBusquedaConyuge) {
		this.tipoBusquedaConyuge = tipoBusquedaConyuge;
	}

	public String getTipoBusquedaConyuge() {
		return tipoBusquedaConyuge;
	}

	public void setPojoDirectorio(DirectorioTelefonicoExt pojoDirectorioExt) {
		this.pojoDirectorioExt = pojoDirectorioExt;
	}

	public DirectorioTelefonicoExt getPojoDirectorio() {
		return pojoDirectorioExt;
	}

	public void setListCompaniaTel(List<SelectItem> listCompaniaTel) {
		this.listCompaniaTel = listCompaniaTel;
	}

	public List<SelectItem> getListCompaniaTel() {
		return listCompaniaTel;
	}

	public void setIdCompaniaTel(Long idCompaniaTel) {
		this.pojoDirectorioExt.setCompaniaTelefonica(getValorById(idCompaniaTel, listTmpCompaniaTel).getId());
	}

	public Long getIdCompaniaTel() {
		return this.pojoDirectorioExt.getCompaniaTelefonica()!=null ? this.pojoDirectorioExt.getCompaniaTelefonica() : -1l;
	}

	public void setIdTipoTel(Long idTipoTel) {
		this.pojoDirectorioExt.setTipoTelefono(getValorById(idTipoTel, listTmpTiposTelefono).getId());
	}

	public Long getIdTipoTel() {
		return this.pojoDirectorioExt.getTipoTelefono()!=null ? this.pojoDirectorioExt.getTipoTelefono() : -1l;
	}

	public void setNumPaginaDirectorio(int numPaginaDirectorio) {
		this.numPaginaDirectorio = numPaginaDirectorio;
	}

	public int getNumPaginaDirectorio() {
		return numPaginaDirectorio;
	}

	public void setListTiposTelefono(List<SelectItem> listTiposTelefono) {
		this.listTiposTelefono = listTiposTelefono;
	}

	public List<SelectItem> getListTiposTelefono() {
		return listTiposTelefono;
	}

	public void setNumPaginaNomAlt(int numPaginaNomAlt) {
		this.numPaginaNomAlt = numPaginaNomAlt;
	}

	public int getNumPaginaNomAlt() {
		return numPaginaNomAlt;
	}

	public void setPojoEstudio(PersonaEstudioExt pojoEstudioExt) {
		this.pojoEstudioExt = pojoEstudioExt;
	}

	public PersonaEstudioExt getPojoEstudio() {
		return pojoEstudioExt;
	}

	public void setListNivelEstudios(List<SelectItem> listNivelEstudios) {
		this.listNivelEstudios = listNivelEstudios;
	}

	public List<SelectItem> getListNivelEstudios() {
		return listNivelEstudios;
	}

	public void setNumPaginaEstudio(int numPaginaEstudio) {
		this.numPaginaEstudio = numPaginaEstudio;
	}

	public int getNumPaginaEstudio() {
		return numPaginaEstudio;
	}

	public void setIdEstudio(Long idEstudio) {
		this.pojoEstudioExt.setEstudio(getValorById(idEstudio, listTmpNivelEstudios).getId());
	}

	public Long getIdEstudio() {
		return this.pojoEstudioExt.getEstudio() != null ? this.pojoEstudioExt.getEstudio() : -1l;
	}

	public void setIdCarrera(Long idCarrera) {
		this.pojoEstudioExt.setEstudio(getValorById(idCarrera, listTmpNivelEstudios).getId());
	}

	public Long getIdCarrera() {
		return this.pojoEstudioExt.getCarrera() != null ? this.pojoEstudioExt.getCarrera() : -1l;
	}

	public void setIdEscuela(Long idEscuela) {
		this.pojoEstudioExt.setEstudio(getValorById(idEscuela, listTmpNivelEstudios).getId());
	}

	public Long getIdEscuela() {
		return this.pojoEstudioExt.getEstudio() != null ? this.pojoEstudioExt.getEstudio() : -1l;
	}

	public void setListTmpTipoIdentificacion(
			List<ConValores> listTmpTipoIdentificacion) {
		this.listTmpTipoIdentificacion = listTmpTipoIdentificacion;
	}

	public List<ConValores> getListTmpTipoIdentificacion() {
		return listTmpTipoIdentificacion;
	}

	public void setListTipoIdentificacion(List<SelectItem> listTipoIdentificacion) {
		this.listTipoIdentificacion = listTipoIdentificacion;
	}

	public List<SelectItem> getListTipoIdentificacion() {
		return listTipoIdentificacion;
	}

	public void setPojoIdentificacion(IdentificacionExt pojoIdentificacionExt) {
		this.pojoIdentificacionExt = pojoIdentificacionExt;
	}

	public IdentificacionExt getPojoIdentificacion() {
		return pojoIdentificacionExt;
	}

	public void setNumPaginaIdentificacion(int numPaginaIdentificacion) {
		this.numPaginaIdentificacion = numPaginaIdentificacion;
	}

	public int getNumPaginaIdentificacion() {
		return numPaginaIdentificacion;
	}

	public void setIdTipoIdentificacion(Long idTipoIdentificacion) {
		this.pojoIdentificacionExt.setIdentificacion(getValorById(idTipoIdentificacion, listTmpTipoIdentificacion).getId());
	}

	public Long getIdTipoIdentificacion() {
		return pojoIdentificacionExt.getIdentificacion() != null ? pojoIdentificacionExt.getIdentificacion() : -1l;
	}

	public void setListTmpTipoDocumento(List<ConValores> listTmpTipoDocumento) {
		this.listTmpTipoDocumento = listTmpTipoDocumento;
	}

	public List<ConValores> getListTmpTipoDocumento() {
		return listTmpTipoDocumento;
	}

	public void setPojoDigitalizado(DocumentoDigitalizado pojoDigitalizado) {
		this.pojoDigitalizado = pojoDigitalizado;
	}

	public DocumentoDigitalizado getPojoDigitalizado() {
		return pojoDigitalizado;
	}

	public void setListDocumentosPersona(List<PersonaDocumento> listDocumentosPersona) {
		this.listDocumentosPersona = listDocumentosPersona;
	}

	public List<PersonaDocumento> getListDocumentosPersona() {
		return listDocumentosPersona;
	}

	public void setListTipoDocumento(List<SelectItem> listTipoDocumento) {
		this.listTipoDocumento = listTipoDocumento;
	}

	public List<SelectItem> getListTipoDocumento() {
		return listTipoDocumento;
	}

	public void setNumDoctoDigit(int numDoctoDigit) {
		this.numDoctoDigit = numDoctoDigit;
	}

	public int getNumDoctoDigit() {
		return numDoctoDigit;
	}
	
	public void setIdTipoDocumento(Long tipoSangre) {
		this.pojoDigitalizado.setTipoDocumento(getValorById(tipoSangre, this.listTmpTipoDocumento));
	}

	public Long getIdTipoDocumento() {
		return pojoDigitalizado.getTipoDocumento() != null ? pojoDigitalizado.getTipoDocumento().getId() : -1L;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setIdDocumento(Long idDocumento) {
		this.pojoDigitalizado.setDocumento(getValorById(idDocumento, this.listTmpDocumentos));
	}

	public Long getIdDocumento() {
		return pojoDigitalizado.getDocumento() != null ? pojoDigitalizado.getDocumento().getId() : -1L;
	}

	public void setListDocumentos(List<SelectItem> listDocumentos) {
		this.listDocumentos = listDocumentos;
	}

	public List<SelectItem> getListDocumentos() {
		return listDocumentos;
	}

	public void setExtArchivos(String extArchivos) { /* nanai */}

	public String getExtArchivos() {
		return this.pojoDigitalizado.getTipoDocumento()!=null ? this.pojoDigitalizado.getTipoDocumento().getAtributo1() : "";
	}

	public void setListDocumentosPersonaVer(List<DocumentoDigitalizado> listDocumentosPersonaVer) {
		this.listDocumentosPersonaVer = listDocumentosPersonaVer;
	}

	public List<DocumentoDigitalizado> getListDocumentosPersonaVer() {
		return listDocumentosPersonaVer;
	}

	public void setNumDoctoDigitVersion(int numDoctoDigitVersion) {
		this.numDoctoDigitVersion = numDoctoDigitVersion;
	}

	public int getNumDoctoDigitVersion() {
		return numDoctoDigitVersion;
	}

	public void setPojoLocalidad(Localidad localidad) {
		if(editaNacimiento){
			pojoLocalidadNac = localidad;
			pojoPersonaExt.setLocalidad(localidad);
		} else{
			pojoLocalidadDom = localidad;
			pojoColonia = null;
		}
	}

	public Localidad getPojoLocalidad() {
		return  pojoLocalidadDom;
	}
	
	public void setPojoLocalidadNac(Localidad localidad){
		
	}
	
	public Localidad getPojoLocalidadNac(){
		resOperacion = "";
		try{
			
		} catch (Exception e) {
			resOperacion = problemInesp;
			log.error("Error al obtener localidad de nacimiento", e);
		}
		return pojoLocalidadNac;
	}

	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}

	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}

	public void setBusquedaLocalidad(String busquedaLocalidad) {
		this.busquedaLocalidad = busquedaLocalidad;
	}

	public String getBusquedaLocalidad() {
		return busquedaLocalidad;
	}

	public void setBusquedaColonia(String busquedaColonia) {
		this.busquedaColonia = busquedaColonia;
	}

	public String getBusquedaColonia() {
		return busquedaColonia;
	}

	public void setPojoEstado(Estado pojoEstadotNac) {/* nanai */}

	public Estado getPojoEstado() {
		return editaNacimiento ? pojoEstadoNac : pojoEstadoDom;
	}
	
	public void setPojoEstadoNac(Estado pojoEstadotNac) {
		this.pojoEstadoNac = pojoEstadotNac;
	}

	public Estado getPojoEstadoNac() {
		return pojoEstadoNac;
	}

	public void setPojoEstadoDom(Estado pojoEstadoDom) {
		this.pojoEstadoDom = pojoEstadoDom;
	}

	public Estado getPojoEstadoDom() {
		return pojoEstadoDom;
	}

	public void setIdEstadoNac(Long idEstadoNac) {
		try {
			pojoEstadoNac = getEstadoById(idEstadoNac);
			//pojoEstado = pojoEstadoNac;
		} catch (Exception e) {
			log.error("Error en setIdEstadoNac", e);
		}
	}

	public Long getIdEstadoNac() {
		return pojoEstadoNac !=null && pojoEstadoNac.getId() > 0 ? pojoEstadoNac.getId() : pojoPersonaExt.getLocalidad() != null ? pojoPersonaExt.getLocalidad().getMunicipio().getEstado().getId() : 0l;
	}

	public void setIdEstadoDom(Long idEstadoDom) {
		try {
			pojoEstadoDom = getEstadoById(idEstadoDom);
		} catch (Exception e) {
			log.error("Error en metodo setIdEstadoDom",e);
		}
	}

	public Long getIdEstadoDom() {
		return pojoEstadoDom!=null && pojoEstadoDom.getId() > 0 ? pojoEstadoDom.getId() : 0l;
	}

	public void setNumPaginaLocalidad(int numPaginaLocalidad) {
		this.numPaginaLocalidad = numPaginaLocalidad;
	}

	public int getNumPaginaLocalidad() {
		return numPaginaLocalidad;
	}

	public void setNumPaginaColonia(int numPaginaColonia) {
		this.numPaginaColonia = numPaginaColonia;
	}

	public int getNumPaginaColonia() {
		return numPaginaColonia;
	}

	public void setListColonias(List<Colonia> listColonias) {
		this.listColonias = listColonias;
	}

	public List<Colonia> getListColonias() {
		return listColonias;
	}

	public void setListCatDomicilios1(List<SelectItem> listCatDomicilios1) {
		this.listCatDomicilios1 = listCatDomicilios1;
	}

	public List<SelectItem> getListCatDomicilios1() {
		return listCatDomicilios1;
	}

	public void setListCatDomicilios2(List<SelectItem> listCatDomicilios2) {
		this.listCatDomicilios2 = listCatDomicilios2;
	}

	public List<SelectItem> getListCatDomicilios2() {
		return listCatDomicilios2;
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

	public Municipio getPojoMunicipio() {
		return editaNacimiento ? pojoMunicipioNac : pojoMunicipioDom;
	}

	

	public Localidad getPojoLocalidadDom() {
		return pojoLocalidadDom;
	}

	public void setPojoLocalidadDom(Localidad pojoLocalidadDom) {
		this.pojoLocalidadDom = pojoLocalidadDom;
	}

	public Municipio getPojoMunicipioNac() {
		return pojoMunicipioNac;
	}

	public void setPojoMunicipioNac(Municipio pojoMunicipioNac) {
		this.pojoMunicipioNac = pojoMunicipioNac;
	}

	public Municipio getPojoMunicipioDom() {
		return pojoMunicipioDom;
	}

	public void setPojoMunicipioDom(Municipio pojoMunicipioDom) {
		this.pojoMunicipioDom = pojoMunicipioDom;
	}

	public void setListMunicipios(List<Municipio> listMunicipio) {
		this.listMunicipios = listMunicipio;
	}

	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setPojoColonia(Colonia pojoColonia) {
		this.pojoColonia = pojoColonia;
	}

	public Colonia getPojoColonia() {
		return pojoColonia;
	}

	public void setEditaNacimiento(boolean editaNacimiento) {
		this.editaNacimiento = editaNacimiento;
	}

	public boolean isEditaNacimiento() {
		return editaNacimiento;
	}

	public void setBusquedaMunicipio(String busquedaMunicipio) {
		this.busquedaMunicipio = busquedaMunicipio;
	}

	public String getBusquedaMunicipio() {
		return busquedaMunicipio;
	}

	public void setNumPaginaMunicipio(int numPaginaMunicipio) {
		this.numPaginaMunicipio = numPaginaMunicipio;
	}

	public int getNumPaginaMunicipio() {
		return numPaginaMunicipio;
	}

	public List<DomicilioExt> getListDomiciliosExt() {
		return listDomiciliosExt;
	}

	public void setListDomiciliosExt(List<DomicilioExt> listDomiciliosExt) {
		this.listDomiciliosExt = listDomiciliosExt;
	}

	public DomicilioExt getPojoDomicilioExt() {
		return pojoDomicilioExt;
	}

	public void setPojoDomicilioExt(DomicilioExt pojoDomicilioExt) {
		this.pojoDomicilioExt = pojoDomicilioExt;
	}

	public List<DirectorioTelefonicoExt> getListDirectorio() {
		return listDirectorioExt;
	}

	public void setListDirectorio(List<DirectorioTelefonicoExt> listDirectorioExt) {
		this.listDirectorioExt = listDirectorioExt;
	}

	public List<PersonaExt> getListConyuge() {
		return listConyugeExt;
	}

	public void setListConyuge(List<PersonaExt> listConyugeExt) {
		this.listConyugeExt = listConyugeExt;
	}

	public List<PersonaEstudioExt> getListEstudiosExt() {
		return listEstudiosExt;
	}

	public void setListEstudiosExt(List<PersonaEstudioExt> listEstudiosExt) {
		this.listEstudiosExt = listEstudiosExt;
	}

	public List<SelectItem> getListCarrera() {
		return listCarrera;
	}

	public void setListCarrera(List<SelectItem> listCarrera) {
		this.listCarrera = listCarrera;
	}

	public List<IdentificacionExt> getListIdentificaciones() {
		return listIdentificacionesExt;
	}

	public void setListIdentificaciones(List<IdentificacionExt> listIdentificacionesExt) {
		this.listIdentificacionesExt = listIdentificacionesExt;
	}

	public PersonaNegocioExt getPojoPersonaNagocio() {
		return pojoPersonaNagocio;
	}

	public void setPojoPersonaNagocio(PersonaNegocioExt pojoPersonaNagocio) {
		this.pojoPersonaNagocio = pojoPersonaNagocio;
	}

	public int getNumPaginaPersonaNegocio() {
		return numPaginaPersonaNegocio;
	}

	public void setNumPaginaPersonaNegocio(int numPaginaPersonaNegocio) {
		this.numPaginaPersonaNegocio = numPaginaPersonaNegocio;
	}

	public List<PersonaNegocioExt> getListPersonaNegocio() {
		return listPersonaNegocio;
	}

	public void setListPersonaNegocio(List<PersonaNegocioExt> listPersonaNegocio) {
		this.listPersonaNegocio = listPersonaNegocio;
	}

	public Negocio getPojoNegocio() {
		return pojoNegocio;
	}

	public void setPojoNegocio(Negocio pojoNegocio) {
		this.pojoNegocio = pojoNegocio;
	}

	public PersonaExt getPojoApoderadoPersonaNegocio() {
		return pojoApoderadoPersonaNegocio;
	}

	public void setPojoApoderadoPersonaNegocio(
			PersonaExt pojoApoderadoPersonaNegocio) {
		this.pojoApoderadoPersonaNegocio = pojoApoderadoPersonaNegocio;
	}

	public List<Negocio> getListNegocio() {
		return listNegocio;
	}

	public void setListNegocio(List<Negocio> listNegocio) {
		this.listNegocio = listNegocio;
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

	public int getNumPaginaNegocio() {
		return numPaginaNegocio;
	}

	public void setNumPaginaNegocio(int numPaginaNegocio) {
		this.numPaginaNegocio = numPaginaNegocio;
	}

	public List<PersonaExt> getListPersonaApoderadoNegocioExt() {
		return listPersonaApoderadoNegocioExt;
	}

	public void setListPersonaApoderadoNegocioExt(List<PersonaExt> listPersonaApoderadoNegocioExt) {
		this.listPersonaApoderadoNegocioExt = listPersonaApoderadoNegocioExt;
	}

	public int getNumPaginaApoderado() {
		return numPaginaApoderado;
	}

	public void setNumPaginaApoderado(int numPaginaApoderado) {
		this.numPaginaApoderado = numPaginaApoderado;
	}

	public Long getTipoPersonaBusqueda() {
		return tipoPersonaBusqueda;
	}

	public void setTipoPersonaBusqueda(Long tipoPersonaBusqueda) {
		this.tipoPersonaBusqueda = tipoPersonaBusqueda;
	}

	public String getValorBusquedaApoderado() {
		return valorBusquedaApoderado;
	}

	public void setValorBusquedaApoderado(String valorBusquedaApoderado) {
		this.valorBusquedaApoderado = valorBusquedaApoderado;
	}

	public String getTipoBusquedaApoderado() {
		return tipoBusquedaApoderado;
	}

	public void setTipoBusquedaApoderado(String tipoBusquedaApoderado) {
		this.tipoBusquedaApoderado = tipoBusquedaApoderado;
	}

	public List<PersonaNombresAlternoExt> getListNombreAlterno() {
		return listNombreAlterno;
	}

	public void setListNombreAlterno(List<PersonaNombresAlternoExt> listNombreAlterno) {
		this.listNombreAlterno = listNombreAlterno;
	}

	public PersonaNombresAlternoExt getPojoNombreAlterno() {
		return pojoNombreAlterno;
	}

	public void setPojoNombreAlterno(PersonaNombresAlternoExt pojoNombreAlterno) {
		this.pojoNombreAlterno = pojoNombreAlterno;
	}

	public List<ContactoPersonaExt> getListPersonaContacto() {
		return listPersonaContacto;
	}

	public void setListPersonaContacto(List<ContactoPersonaExt> listPersonaContacto) {
		this.listPersonaContacto = listPersonaContacto;
	}

	public int getNumPaginaPersonaContacto() {
		return numPaginaPersonaContacto;
	}

	public void setNumPaginaPersonaContacto(int numPaginaPersonaContacto) {
		this.numPaginaPersonaContacto = numPaginaPersonaContacto;
	}

	public ContactoPersonaExt getPojoPersonaContacto() {
		return pojoPersonaContacto;
	}

	public void setPojoPersonaContacto(ContactoPersonaExt pojoPersonaContacto) {
		this.pojoPersonaContacto = pojoPersonaContacto;
	}

	public List<PersonaExt> getListBusquedaPersonaContacto() {
		return listBusquedaPersonaContacto;
	}

	public void setListBusquedaPersonaContacto(
			List<PersonaExt> listBusquedaPersonaContacto) {
		this.listBusquedaPersonaContacto = listBusquedaPersonaContacto;
	}

	public int getNumPaginaBusquedaContacto() {
		return numPaginaBusquedaContacto;
	}

	public void setNumPaginaBusquedaContacto(int numPaginaBusquedaContacto) {
		this.numPaginaBusquedaContacto = numPaginaBusquedaContacto;
	}

	public String getTipoBusquedaContacto() {
		return tipoBusquedaContacto;
	}

	public void setTipoBusquedaContacto(String tipoBusquedaContacto) {
		this.tipoBusquedaContacto = tipoBusquedaContacto;
	}

	public String getValorBusquedaContacto() {
		return valorBusquedaContacto;
	}

	public void setValorBusquedaContacto(String valorBusquedaContacto) {
		this.valorBusquedaContacto = valorBusquedaContacto;
	}

	public List<CatBancosExt> getListBancos() {
		return listBancos;
	}

	public void setListBancos(List<CatBancosExt> listBancos) {
		this.listBancos = listBancos;
	}

	public List<SelectItem> getListTmpBancos() {
		return listTmpBancos;
	}

	public void setListTmpBancos(List<SelectItem> listTmpBancos) {
		this.listTmpBancos = listTmpBancos;
	}
}	
	
	