package net.giro.clientes.logica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

import net.giro.clientes.beans.CanalCat;
import net.giro.clientes.beans.CanalesNegocio;
import net.giro.clientes.beans.CanalesNegocioExt;
import net.giro.clientes.beans.ContactoNegocio;
import net.giro.clientes.beans.ContactoNegocioExt;
import net.giro.clientes.beans.DirectorioTelefonico;
import net.giro.clientes.beans.DirectorioTelefonicoExt;
import net.giro.clientes.beans.Domicilio;
import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.beans.EstadosNegocio;
import net.giro.clientes.beans.EstadosNegocioExt;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.beans.PersonaNegocio;
import net.giro.clientes.beans.PersonaNegocioExt;
import net.giro.clientes.dao.CanalesNegocioDAO;
import net.giro.clientes.dao.ContactoNegocioDAO;
import net.giro.clientes.dao.DirectorioTelefonicoDAO;
import net.giro.clientes.dao.DomicilioDAO;
import net.giro.clientes.dao.EstadosNegocioDAO;
import net.giro.clientes.dao.NegocioDAO;
import net.giro.clientes.dao.PersonaDAO;
import net.giro.clientes.dao.PersonaNegocioDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConGrupoValoresDAO;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.EstadoDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.respuesta.Respuesta;
import net.giro.util.clientes.Errores;

@Stateless
public class NegociosFac implements NegociosRem {
	private static Logger log = Logger.getLogger(NegociosFac.class);
	private InitialContext ctx = null;
	private InfoSesion infoSesion;
	private NegocioDAO ifzNegocio;
	private ConValoresDAO ifzConValores;
	private DomicilioDAO ifzDomicilios;
	private ColoniaDAO ifzColonia;
	private LocalidadDAO ifzLocalidad;
	private PersonaDAO ifzPersonas;
	private PersonaNegocioDAO ifzPersonaNegocio;
	private DirectorioTelefonicoDAO	ifzDirectorio;
	private ConGrupoValoresDAO ifzConGrupoValores;
	private CanalesNegocioDAO ifzCanalesNegocio;
	private ContactoNegocioDAO ifzContactoNegocio;
	private EstadoDAO ifzEstados;
	private EstadosNegocioDAO ifzEstadosNegocio;
	private ClientesRem ifzClientes;
	private HashMap<Long, String> descEstatusNegocio;
	private HashMap<Long, String> descEstatusContacto;
	private static final long ACTIVO = 1L;
	private static final long INACTIVO = 2L;
	
	public NegociosFac() {
		Hashtable<String, Object> p = null;
		
		try {
    		p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		this.ctx = new InitialContext(p);
    		this.ifzConValores =  (ConValoresDAO) this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
    		this.ifzConGrupoValores =  (ConGrupoValoresDAO) this.ctx.lookup("ejb:/Model_Publico//ConGrupoValoresImp!net.giro.plataforma.dao.ConGrupoValoresDAO");
    		this.ifzNegocio = (NegocioDAO) this.ctx.lookup("ejb:/Model_Clientes//NegocioImp!net.giro.clientes.dao.NegocioDAO");
    		this.ifzDomicilios = (DomicilioDAO) this.ctx.lookup("ejb:/Model_Clientes//DomicilioImp!net.giro.clientes.dao.DomicilioDAO");
    		this.ifzPersonaNegocio = (PersonaNegocioDAO) this.ctx.lookup("ejb:/Model_Clientes//PersonaNegocioImp!net.giro.clientes.dao.PersonaNegocioDAO");
    		this.ifzPersonas = (PersonaDAO) this.ctx.lookup("ejb:/Model_Clientes//PersonaImp!net.giro.clientes.dao.PersonaDAO");
    		this.ifzLocalidad = (LocalidadDAO) this.ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
    		this.ifzColonia = (ColoniaDAO) this.ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");
    		this.ifzDirectorio = (DirectorioTelefonicoDAO) this.ctx.lookup("ejb:/Model_Clientes//DirectorioTelefonicoImp!net.giro.clientes.dao.DirectorioTelefonicoDAO");
    		this.ifzCanalesNegocio = (CanalesNegocioDAO) this.ctx.lookup("ejb:/Model_Clientes//CanalesNegocioImp!net.giro.clientes.dao.CanalesNegocioDAO");
    		this.ifzContactoNegocio = (ContactoNegocioDAO) this.ctx.lookup("ejb:/Model_Clientes//ContactoNegocioImp!net.giro.clientes.dao.ContactoNegocioDAO");
    		this.ifzEstados =  (EstadoDAO) this.ctx.lookup("ejb:/Model_Publico//EstadoImp!net.giro.plataforma.ubicacion.dao.EstadoDAO");
    		this.ifzEstadosNegocio = (EstadosNegocioDAO) this.ctx.lookup("ejb:/Model_Clientes//EstadosNegocioImp!net.giro.clientes.dao.EstadosNegocioDAO");
			this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
			
    		this.descEstatusNegocio = new HashMap<Long, String>();
    		this.descEstatusNegocio.put(ACTIVO, "Activo");
    		this.descEstatusNegocio.put(INACTIVO, "Inactivo");
    		
    		this.descEstatusContacto = new HashMap<Long, String>();
    		this.descEstatusContacto.put(ACTIVO, "Activo");
    		this.descEstatusContacto.put(INACTIVO, "Inactivo");
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		this.ctx = null;
    	}	
	}

	
	@Override
	public InfoSesion getInfoSesion() {
		return infoSesion;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(Negocio entity) throws Exception {
		try {
			return this.ifzNegocio.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Clientes.NegociosFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	public List<Negocio> saveOrUpdateList(List<Negocio> entities) throws Exception {
		try {
			return this.ifzNegocio.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Clientes.NegociosFac.saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	public void update(Negocio entity) throws Exception {
		try {
			entity.setEstatus(ACTIVO);
			this.ifzNegocio.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Clientes.NegociosFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(Negocio entity) throws Exception {
		try {
			entity.setEstatus(INACTIVO);
			entity.setModificadoPor(1L);
			if (this.infoSesion != null)
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzNegocio.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Clientes.NegociosFac.delete(entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(long idNegocio) throws Exception {
		try {
			this.delete(this.ifzNegocio.findById(idNegocio));
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public Negocio findById(long idNegocio) {
		try {
			return this.ifzNegocio.findById(idNegocio);
		} catch (Exception re) { 
			throw re;
		}
	}
	
	@Override
	public List<Negocio> findAll() {
		try {
			return this.ifzNegocio.findAll(false, false, "");
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Negocio> findAll(String nombre) throws Exception {
		try {
			return this.ifzNegocio.findLike(nombre, false, false, "", 0); //.findAll(nombre);
		} catch (Exception re) {
			throw re;
		}
	}
	
	public DirectorioTelefonico convertDirectorioTelefonicoExtToDirectoTelefonico(DirectorioTelefonicoExt pojoDirectorioExt){
		DirectorioTelefonico pojoDirectorio = new DirectorioTelefonico();
		
		pojoDirectorio.setId(pojoDirectorioExt.getId());
		pojoDirectorio.setCreadoPor(pojoDirectorioExt.getCreadoPor());
		pojoDirectorio.setFechaCreacion(pojoDirectorioExt.getFechaCreacion());
		pojoDirectorio.setModificadoPor(pojoDirectorioExt.getModificadoPor());
		pojoDirectorio.setFechaCreacion(pojoDirectorioExt.getFechaCreacion());
		if (pojoDirectorioExt.getPersona() != null)
			pojoDirectorio.setPersona(convertPersonaExtToPersona(pojoDirectorioExt.getPersona()));
		pojoDirectorio.setNegocio(pojoDirectorioExt.getNegocio());
		pojoDirectorio.setTipoTelefono(pojoDirectorioExt.getTipoTelefono());
		pojoDirectorio.setCompaniaTelefonica(pojoDirectorioExt.getCompaniaTelefonica());
		pojoDirectorio.setLada(pojoDirectorioExt.getLada());
		pojoDirectorio.setNumeroTelefono(pojoDirectorioExt.getNumeroTelefono());
		pojoDirectorio.setExtension(pojoDirectorioExt.getExtension());
		pojoDirectorio.setObservaciones(pojoDirectorioExt.getObservaciones());
		pojoDirectorio.setPrincipal(pojoDirectorioExt.getPrincipal() ? 1L : 0L);
		
		return pojoDirectorio;
	}
	
	public DirectorioTelefonicoExt convertDirectorioTelefonicoToDirectoTelefonicoExt(DirectorioTelefonico pojoDirectorio){
		DirectorioTelefonicoExt pojoDirectorioExt = new DirectorioTelefonicoExt();
		
		pojoDirectorioExt.setId(pojoDirectorio.getId());
		pojoDirectorioExt.setCreadoPor(pojoDirectorio.getCreadoPor());
		pojoDirectorioExt.setFechaCreacion(pojoDirectorio.getFechaCreacion());
		pojoDirectorioExt.setModificadoPor(pojoDirectorio.getModificadoPor());
		pojoDirectorioExt.setFechaCreacion(pojoDirectorio.getFechaCreacion());
		if (pojoDirectorio.getPersona() != null)
			pojoDirectorioExt.setPersona(convertPersonaToPersonaExt(pojoDirectorio.getPersona()));
		pojoDirectorioExt.setNegocio(pojoDirectorio.getNegocio());
		pojoDirectorioExt.setTipoTelefono(pojoDirectorio.getTipoTelefono());
		
		ConValores tipoTelefono =  new ConValores();
		tipoTelefono = ifzConValores.findById(pojoDirectorio.getTipoTelefono());
		pojoDirectorioExt.setTipoTelefonoDesc(tipoTelefono.getValor());
		
		pojoDirectorioExt.setCompaniaTelefonica(pojoDirectorio.getCompaniaTelefonica());
		
		ConValores companiaTelefonica =  new ConValores();
		companiaTelefonica = ifzConValores.findById(pojoDirectorio.getCompaniaTelefonica());
		pojoDirectorioExt.setCompaniaTelefonoDesc(companiaTelefonica.getValor());
		
		pojoDirectorioExt.setLada(pojoDirectorio.getLada());
		pojoDirectorioExt.setNumeroTelefono(pojoDirectorio.getNumeroTelefono());
		pojoDirectorioExt.setExtension(pojoDirectorio.getExtension());
		pojoDirectorioExt.setObservaciones(pojoDirectorio.getObservaciones());
		pojoDirectorioExt.setPrincipal(pojoDirectorio.getPrincipal() > 0L ? true : false);
		
		return pojoDirectorioExt;
	}
	
	@Override
	public Respuesta buscarCanales(Negocio pojoNegocio){
		Respuesta respuesta = new Respuesta();
		try{
			List<CanalesNegocioExt> listCanalesExt = new ArrayList<CanalesNegocioExt>();
			List<CanalesNegocio> listCanales = ifzCanalesNegocio.findByColumnName("idNegocio", pojoNegocio);
			
			for(CanalesNegocio pojoCanal : listCanales){
				listCanalesExt.add(convertCanalesNegocioToCanalesNegocioExt(pojoCanal));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listCanales", listCanalesExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_BUSCAR_CANALES_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.buscarCanales", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarContactos(Negocio pojoNegocio, long idEstatus) {
		Respuesta respuesta = new Respuesta();
		try{
			List<ContactoNegocio> listContactos = new ArrayList<ContactoNegocio>();
			List<ContactoNegocioExt> listContactosExt = new ArrayList<ContactoNegocioExt>();
			listContactos = ifzContactoNegocio.findByColumnName("idNegocio", pojoNegocio);
			
			if (idEstatus > 0L){
				for (ContactoNegocio pojoContacto : listContactos) {
					if (pojoContacto.getEstatusId() == idEstatus)
						listContactosExt.add(convertContactoNegocioToContactoNegocioExt(pojoContacto));
				}
			} else {
				for (ContactoNegocio pojoContacto : listContactos)
					listContactosExt.add(convertContactoNegocioToContactoNegocioExt(pojoContacto));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listContactos", listContactosExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_BUSCAR_CONTACTOS_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.buscarContactos", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarEstados(Negocio pojoNegocio){
		Respuesta respuesta = new Respuesta();
		try{
			List<EstadosNegocioExt> listEstadosExt = new ArrayList<EstadosNegocioExt>();
			List<EstadosNegocio> listEstados = ifzEstadosNegocio.findByColumnName("idNegocio", pojoNegocio);
			
			for(EstadosNegocio pojoEstado : listEstados){
				listEstadosExt.add(convertEstadosNegocioToEstadosNegocioExt(pojoEstado));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listEstados", listEstadosExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_CARGAR_ESTADOS_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.buscarEstados", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarGiro(long idGiro){
		Respuesta respuesta = new Respuesta();
		try {
			ConValores pojoGiro = this.ifzConValores.findById(idGiro);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoGiro", pojoGiro);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_BUSCAR_GIRO);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}
	
	public List<ConValores> buscarGiros() throws ExcepConstraint{
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		
		List<ConGrupoValores> listaConGrupoValores = this.ifzConGrupoValores.findByColumnName("nombre","SYS_GIROS");
		for (ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = this.ifzConValores.findByColumnName("grupoValorId", cGV);
			for (ConValores cv : listaTmpConValores) {
				listaConValores.add(cv);
			}
		}
		
		return listaConValores;
	}
	
	@Override
	public Respuesta buscarGiros(String nombre) {
		Respuesta respuesta = new Respuesta();
		HashMap<String, String> params = new HashMap<String, String>();
		List<ConValores> listGiros = null;
		
		try {
			params.put("valor", nombre);
			listGiros = this.ifzConValores.findByGrupoNombreLikeParams("SYS_GIROS", params);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listGiros", listGiros);
		} catch (Exception e) {
			log.error("Error en NegociosFac.buscarGiros");
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_BUSCAR_GIROS);
			respuesta.setBody(null);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarNegocios(String tipoBusquedaNegocio, String valorBusquedaNegocio) {
		Respuesta respuesta = new Respuesta();
		List<Negocio> listNegocios = null;
		
		try {
			listNegocios = this.ifzNegocio.findLikeProperty(tipoBusquedaNegocio, valorBusquedaNegocio, false, false, "", 0); 
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listNegocios", listNegocios);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_BUSCAR_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFAc.buscarNegocios", e);
		}
		
		return respuesta; 
	}
	
	@Override
	public Respuesta buscarPersonas(String tipoBusqueda, String valorBusqueda) {
		Respuesta respuesta = new Respuesta();
		List<PersonaExt> listPersonasExt =  new ArrayList<PersonaExt>();
		PersonaExt personaExt = null;
		List<Persona> listPersonas = null;
		
		try {
			listPersonas = this.ifzPersonas.findLikeProperty(tipoBusqueda, valorBusqueda, false, false, "", 0); 
			if (listPersonas != null && ! listPersonas.isEmpty()) {
				for (Persona persona : listPersonas) {
					personaExt = new PersonaExt();
					personaExt = convertPersonaToPersonaExt(persona);
					listPersonasExt.add(personaExt);
				}
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listPersonas", listPersonasExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_BUSCAR_PERSONA);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.buscarPersonas", e);
		}
		return respuesta;
	}	

	@Override
	public List<ConValores> buscarSectores() throws ExcepConstraint {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_SECTORES");
		for(ConGrupoValores cGV : listaConGrupoValores){
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores){
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}
	
	@Override
	public Respuesta cargarTiposCanales(){
		Respuesta respuesta = new Respuesta();
		try{
			List<CanalCat> listTiposCanales = new ArrayList<CanalCat>();
			List<ConValores> listValores = ifzConValores.findByGrupoNombre("SYS_TIPOS_CANALES");
			for(ConValores valor : listValores){
				listTiposCanales.add(new CanalCat(valor.getId(), valor.getValor()));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listTiposCanales", listTiposCanales);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_CARGAR_TIPOS_CANALES);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.cargarTiposCanales", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta cambiarEstatusContacto(ContactoNegocioExt pojoContactoNegocioExt){
		Respuesta respuesta = new Respuesta();
		try{
			ContactoNegocio pojoContactoNegocio = convertContactoNegocioExtToContactoNegocio(pojoContactoNegocioExt);
			
			if (pojoContactoNegocio.getIdNegocio().getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			if (pojoContactoNegocio.getEstatusId() == ACTIVO)
				pojoContactoNegocio.setEstatusId(INACTIVO);
			else
				pojoContactoNegocio.setEstatusId(ACTIVO);
			
			pojoContactoNegocio.setModificadoPor(infoSesion.getAcceso().getId());
			pojoContactoNegocio.setFechaModificacion(Calendar.getInstance().getTime());
			
			ifzContactoNegocio.update(pojoContactoNegocio);
			
			pojoContactoNegocioExt = convertContactoNegocioToContactoNegocioExt(pojoContactoNegocio);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoContactoNegocio", pojoContactoNegocioExt);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_CAMBIAR_ESTATUS_DOMICILIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.cambiarEstatusContacto", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta salvarCanal(CanalesNegocioExt pojoCanalExt){
		Respuesta respuesta = new Respuesta();
		try{
			if (pojoCanalExt.getIdNegocio().getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			CanalesNegocio pojoCanal = convertCanalesNegocioExtToCanalesNegocio(pojoCanalExt);
			
			pojoCanal.setModificadoPor(infoSesion.getAcceso().getId());
			pojoCanal.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (pojoCanal.getId() == 0L){
				pojoCanal.setCreadoPor(infoSesion.getAcceso().getId());
				pojoCanal.setFechaCreacion(Calendar.getInstance().getTime());
				
				pojoCanal.setId(ifzCanalesNegocio.save(pojoCanal, null));
			} else
				ifzCanalesNegocio.update(pojoCanal);
			
			pojoCanalExt = convertCanalesNegocioToCanalesNegocioExt(pojoCanal);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoCanal", pojoCanalExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_GUARDAR_CANAL_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.salvarCanal", e);
		}
		return respuesta; 
	}
	
	@Override
	public Respuesta salvarContacto(ContactoNegocioExt pojoContactoNegocioExt){
		Respuesta respuesta = new Respuesta();
		try{
			ContactoNegocio pojoContactoNegocio = convertContactoNegocioExtToContactoNegocio(pojoContactoNegocioExt);
			
			if (pojoContactoNegocio.getIdNegocio().getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			if (pojoContactoNegocio.getEstatusId() != null && pojoContactoNegocio.getEstatusId() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_CONTACTO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			pojoContactoNegocio.setModificadoPor(infoSesion.getAcceso().getId());
			pojoContactoNegocio.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (pojoContactoNegocio.getId() > 0)
				ifzContactoNegocio.update(pojoContactoNegocio);
			else{
				pojoContactoNegocio.setCreadoPor(infoSesion.getAcceso().getId());
				pojoContactoNegocio.setFechaCreacion(Calendar.getInstance().getTime());
				
				pojoContactoNegocio.setEstatusId(ACTIVO);
				
				pojoContactoNegocio.setId(ifzContactoNegocio.save(pojoContactoNegocio, null));
			}
			
			pojoContactoNegocioExt = convertContactoNegocioToContactoNegocioExt(pojoContactoNegocio);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoContactoNegocio", pojoContactoNegocioExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_GUARDAR_CONTACTOS_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.salvarContacto", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta salvarDomicilio(DomicilioExt domicilioExt){
		Domicilio domicilio =  new Domicilio();
		
		Respuesta respuesta = new Respuesta();		
		try {
			if (domicilio.getNegocio().getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(domicilio, domicilioExt);	
			domicilio.setColonia(domicilioExt.getColonia().getId());
			
			domicilio.setModificadoPor(infoSesion.getAcceso().getId());
			domicilio.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (domicilio.getId() > 0){
				ifzDomicilios.update(domicilio);
			}else{
				domicilio.setCreadoPor(infoSesion.getAcceso().getId());
				domicilio.setFechaCreacion(Calendar.getInstance().getTime());
				domicilio.setId(ifzDomicilios.save(domicilio, null));
			}
			
			domicilioExt = convertDomicilioToDomicilioExt(domicilio);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoDomicilio", domicilioExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_GUARDAR_DOMICILIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.salvar para DocilioExt", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta salvarEstadosNegocio(EstadosNegocioExt pojoEstadosNegocioExt){
		Respuesta respuesta = new Respuesta();
		try{
			if (pojoEstadosNegocioExt.getIdNegocio().getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			EstadosNegocio pojoEstadosNegocio = convertEstadosNegocioExtToEstadosNegocio(pojoEstadosNegocioExt);
			
			pojoEstadosNegocio.setModificadoPor(infoSesion.getAcceso().getId());
			pojoEstadosNegocio.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (pojoEstadosNegocio.getId() > 0){
				ifzEstadosNegocio.update(pojoEstadosNegocio);
			} else {
				pojoEstadosNegocio.setCreadoPor(infoSesion.getAcceso().getId());
				pojoEstadosNegocio.setFechaCreacion(Calendar.getInstance().getTime());
				
				pojoEstadosNegocio.setId(ifzEstadosNegocio.save(pojoEstadosNegocio, null));
			}
			
			pojoEstadosNegocioExt = convertEstadosNegocioToEstadosNegocioExt(pojoEstadosNegocio);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoEstadosNegocio", pojoEstadosNegocioExt);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_GUARDAR_ESTADOS_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.salvarEstadosNegocio", e);
		}
		return respuesta;
	}

	@Override
	public Respuesta salvarNegocio(Negocio negocio) {
		Respuesta respuesta = new Respuesta();
		List<Negocio> negocios = null;
		
		try {
			if (negocio.getEstatus() == INACTIVO) {
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			if (negocio.getTipoPersonalidad().toLowerCase().contains("m") && ! validaRFCMoral(negocio.getRfc())) {
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_RFC);
				respuesta.setBody(null);
				return respuesta;
			}
			
			if (negocio.getTipoPersonalidad().toLowerCase().contains("f") && ! validaRFCFisico(negocio.getRfc())) {
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_RFC);
				respuesta.setBody(null);
				return respuesta;
			}
			
			negocio.setModificadoPor(this.infoSesion.getAcceso().getId());
			negocio.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (! "XAXX010101000 XEXX010101000".contains(negocio.getRfc().trim())) {
				// Comprobamos que RFC ingresado no exista
				negocios = this.ifzNegocio.findByColumnName("rfc", negocio.getRfc());
				if (negocios != null && ! negocios.isEmpty()) {
					for (Negocio item : negocios) {
						if (item.getId().longValue() == negocio.getId().longValue()) 
							continue;

						respuesta.getBody().addValor("pojoNegocio", item);
						respuesta.getErrores().setCodigoError(Errores.ERROR_RFC_EXISTE);
						respuesta.getErrores().setDescError("Verifique, negocio existente\nRFC: " + item.getRfc() + ", ID: " + String.valueOf(item.getId()));
						respuesta.setBody(null);
						return respuesta;
					}
				}
			}
			
			if (negocio.getId() != null && negocio.getId() > 0) {
				this.ifzNegocio.update(negocio);
			} else {
				negocio.setEstatus(ACTIVO);
				negocio.setCreadoPor(infoSesion.getAcceso().getId());
				negocio.setFechaCreacion(Calendar.getInstance().getTime());
				negocio.setId(this.ifzNegocio.save(negocio, getCodigoEmpresa()));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoNegocio", negocio);
		} catch (Exception e) {
			log.error("Error en NegociosFac.salvarNegocio", e);
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_GUARDAR_NEGOCIO);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta salvarDirectorio(DirectorioTelefonicoExt pojoDirectorioExt){
		Respuesta respuesta = new Respuesta();
		try{
			DirectorioTelefonico pojoDirectorio = convertDirectorioTelefonicoExtToDirectoTelefonico(pojoDirectorioExt);
			
			if (pojoDirectorio.getNegocio().getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			pojoDirectorio.setModificadoPor(infoSesion.getAcceso().getId());
			pojoDirectorio.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (pojoDirectorio.getId() == 0L){
				pojoDirectorio.setCreadoPor(infoSesion.getAcceso().getId());
				pojoDirectorio.setFechaCreacion(Calendar.getInstance().getTime());
				
				pojoDirectorio.setId(ifzDirectorio.save(pojoDirectorio, null));
			} else
				ifzDirectorio.update(pojoDirectorio);
			
			pojoDirectorioExt = convertDirectorioTelefonicoToDirectoTelefonicoExt(pojoDirectorio);
			
			if (pojoDirectorioExt.getPrincipal()){
				Respuesta respuestaPrincipal = setTelefonoPrincipal(pojoDirectorioExt);
				
				if (respuestaPrincipal.getErrores().getCodigoError() == 0L){
					respuestaPrincipal.getBody().addValor("pojoDirectorioTelefonico", pojoDirectorioExt);
				}
				return respuestaPrincipal;
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoDirectorioTelefonico", pojoDirectorioExt);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_GUARDAR_DIRECTORIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.salvarDirectorio", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta salvarDomicilio(Negocio pojoNegocio, DomicilioExt pojoDomicilioExt){
		Respuesta respuesta = new Respuesta();
		try{
			if (pojoNegocio.getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			Domicilio pojoDomicilio = convertDomicilioExtToDomicilio(pojoDomicilioExt);
			
			pojoDomicilio.setNegocio(pojoNegocio);
			
			pojoDomicilio.setModificadoPor(infoSesion.getAcceso().getId());
			pojoDomicilio.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (pojoDomicilio.getId() > 0L){
				if (pojoDomicilio.getEstatus() == 0L){
					respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_DOMICILIO_INACTIVO);
					respuesta.setBody(null);
					return respuesta;
				}
				ifzDomicilios.update(pojoDomicilio);
			}else{
				pojoDomicilio.setCreadoPor(infoSesion.getAcceso().getId());
				pojoDomicilio.setFechaCreacion(Calendar.getInstance().getTime());
				
				pojoDomicilio.setId(ifzDomicilios.save(pojoDomicilio, null));
			}
			
			pojoDomicilioExt = convertDomicilioToDomicilioExt(pojoDomicilio);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoDomicilio", pojoDomicilioExt);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_GUARDAR_DOMICILIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.salvarDomicilio", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta salvarDomicilioPrincipal(Negocio pojoNegocio, DomicilioExt pojoDomicilioExt){
		Respuesta respuesta = new Respuesta();
		try{
			if (pojoNegocio.getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			Domicilio pojoDomicilio = convertDomicilioExtToDomicilio(pojoDomicilioExt);
			
			if (pojoDomicilio.getEstatus() == 0L){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_DOMICILIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			List<Domicilio> listDomicilios = ifzDomicilios.findByColumnName("negocio", pojoNegocio);
			for(Domicilio pojoDomicilioAux : listDomicilios){
				if (pojoDomicilioAux.getPrincipal() > 0){
					pojoDomicilioAux.setPrincipal(0L);
					
					pojoDomicilioAux.setModificadoPor(infoSesion.getAcceso().getId());
					pojoDomicilioAux.setFechaModificacion(Calendar.getInstance().getTime());
					
					ifzDomicilios.update(pojoDomicilioAux);
				}
			}
			
			pojoDomicilio.setPrincipal(1L);
			
			pojoDomicilio.setModificadoPor(infoSesion.getAcceso().getId());
			pojoDomicilio.setFechaModificacion(Calendar.getInstance().getTime());
			
			ifzDomicilios.update(pojoDomicilio);
			
			
			List<DomicilioExt> listDomiciliosExt = new ArrayList<DomicilioExt>();
			
			for(Domicilio pojoDomicilioAux : listDomicilios){
				if (pojoDomicilioAux.getId() != pojoDomicilio.getId())
					listDomiciliosExt.add(convertDomicilioToDomicilioExt(pojoDomicilioAux));
				else
					listDomiciliosExt.add(convertDomicilioToDomicilioExt(pojoDomicilio));
			}
			
			//pojoDomicilioExt = convertDomicilioToDomicilioExt(pojoDomicilio);
			
			pojoNegocio.setColonia(pojoDomicilio.getColonia());
			pojoNegocio.setDomicilio(pojoDomicilioExt.getDomicilio());
			
			pojoNegocio.setModificadoPor(infoSesion.getAcceso().getId());
			pojoNegocio.setFechaModificacion(Calendar.getInstance().getTime());
			
			ifzNegocio.update(pojoNegocio);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listDomicilios", listDomiciliosExt);
			respuesta.getBody().addValor("pojoDomicilio", pojoDomicilioExt);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_ESTABLECER_DOMICILIO_PRINCIPAL);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.salvarDomicilioPrincipal", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminarCanal(CanalesNegocioExt pojoCanalExt){
		Respuesta respuesta = new Respuesta();
		try{
			if (pojoCanalExt.getIdNegocio().getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			CanalesNegocio pojoCanal = convertCanalesNegocioExtToCanalesNegocio(pojoCanalExt);
			
			ifzCanalesNegocio.delete(pojoCanal.getId());
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.setBody(null);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_ELIMINAR_CANAL_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.eliminarCanal", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminarEstadosNegocio(EstadosNegocioExt pojoEstadosNegocioExt){
		Respuesta respuesta = new Respuesta();
		try{
			if (pojoEstadosNegocioExt.getIdNegocio().getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			ifzEstadosNegocio.delete(pojoEstadosNegocioExt.getId());
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.setBody(null);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_ELIMINAR_ESTADOS_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.eliminarEstadosNegocio", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminarNegocio(Negocio pojoNegocio) {
		Respuesta respuesta = new Respuesta();
		try{
			pojoNegocio.setModificadoPor(infoSesion.getAcceso().getId());
			pojoNegocio.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (pojoNegocio.getEstatus() == ACTIVO)
				pojoNegocio.setEstatus(INACTIVO);
			else
				pojoNegocio.setEstatus(ACTIVO);
			
			ifzNegocio.update(pojoNegocio);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoNegocio", pojoNegocio);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_ELIMINAR_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.eliminarNegocio", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta habilitarDomicilio(DomicilioExt pojoDomicilioExt){
		Respuesta respuesta = new Respuesta();
		try{
			if (pojoDomicilioExt.getNegocio().getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			pojoDomicilioExt.setEstatus(!pojoDomicilioExt.getEstatus());
			
			if (!pojoDomicilioExt.getEstatus() && pojoDomicilioExt.getPrincipal()){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_DESHABILITAR_DOMICILIO_PRINCIPAL);
				respuesta.setBody(null);
				return respuesta;
			}
			
			Domicilio pojoDomicilio = convertDomicilioExtToDomicilio(pojoDomicilioExt);
			
			pojoDomicilio.setModificadoPor(infoSesion.getAcceso().getId());
			pojoDomicilio.setFechaModificacion(Calendar.getInstance().getTime());
			
			ifzDomicilios.update(pojoDomicilio);			
			pojoDomicilioExt = convertDomicilioToDomicilioExt(pojoDomicilio);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoDomicilio", pojoDomicilioExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_CAMBIAR_ESTATUS_DOMICILIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.habilitarDomicilio", e);
		}
		return respuesta;
	}

	@Override
	public ConValores buscarActividad(Negocio negocio) throws ExcepConstraint {
		ConValores giro =  ifzConValores.findById(negocio.getGiro());
		return ifzConValores.findById(Long.valueOf(giro.getAtributo1().toString()));
	}

	@Override
	public Respuesta salvarPersonaNegocio(PersonaNegocioExt personaNegocioExt){
		Respuesta respuesta = new Respuesta();
		try{
			PersonaNegocio personaNegocio = convertPersonaNegocioExtToPersonaNegocio(personaNegocioExt);
			
			if (personaNegocioExt.getNegocio().getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			if (personaNegocioExt.getRepresentante()){
				if (personaNegocioExt.getParticipacion().doubleValue() == 0){
					respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_PARTICIPACION_REPRESENTANTE_MAYOR_A_CERO);
					respuesta.setBody(null);
					return respuesta;
				}
			}
			
			if (personaNegocioExt.getFirmaDocumento()){
				if (personaNegocioExt.getPersonalidadJuridica() != null && personaNegocioExt.getPersonalidadJuridica().length() == 0){
					respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_PERSONALIDAD_REQUERIDA);
					respuesta.setBody(null);
					return respuesta;
				}
			}
			
			BigDecimal sumaPorcentaje = new BigDecimal("0");
			List<PersonaNegocio> listPersonasNegocios = ifzPersonaNegocio.findByColumnName("negocio", personaNegocio.getNegocio());
			for(PersonaNegocio pojoPersonaNegocioAux : listPersonasNegocios){
				if (pojoPersonaNegocioAux.getId() != personaNegocio.getId())
					sumaPorcentaje = sumaPorcentaje.add(pojoPersonaNegocioAux.getParticipacion());
			}
			
			sumaPorcentaje = sumaPorcentaje.add(personaNegocio.getParticipacion());
			if (sumaPorcentaje.doubleValue() > 100){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_PARTICIPACION_SUPERA_CIEN);
				respuesta.setBody(null);
				return respuesta;
			}
			
			if (personaNegocio.getPersonalidadJuridica() == null){
				personaNegocio.setPersonalidadJuridica("");
			}
			
			if (personaNegocio.getPuesto() == null){
				personaNegocio.setPuesto("");
			}
			
			if (personaNegocio.getParticipacion() == null){
				personaNegocio.setParticipacion(new BigDecimal(0));
			}
			
			personaNegocio.setModificadoPor(infoSesion.getAcceso().getId());
			personaNegocio.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (personaNegocio.getId() > 0){
				ifzPersonaNegocio.update(personaNegocio);
			}else{
				personaNegocio.setCreadoPor(infoSesion.getAcceso().getId());
				personaNegocio.setFechaCreacion(Calendar.getInstance().getTime());
				
				personaNegocio.setId(ifzPersonaNegocio.save(personaNegocio, null));
			}
			
			personaNegocioExt = convertPersonaNegocioToPersonaNegocioExt(personaNegocio);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoPersonaNegocio", personaNegocioExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_GUARDAR_PERSONA_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegocioFac.salvarPersonaNegocio", e);
		}
		
		return respuesta;
	}

	@Override
	public Respuesta eliminarPersonaNegocio(PersonaNegocioExt personaNegocioExt) {
		Respuesta respuesta = new Respuesta();
		try{
			if (personaNegocioExt.getNegocio().getEstatus() == INACTIVO){
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_NEGOCIO_INACTIVO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			ifzPersonaNegocio.delete(personaNegocioExt.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_ELIMINAR_PERSONA_NEGOCIO);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.eliminarPersonaNegocio", e);
		}
		return respuesta;
	}

	@Override
	public List<PersonaNegocioExt> buscarPersonaNegocio(Negocio negocio) throws ExcepConstraint {
		List<PersonaNegocio> listPersonaNegocio = new ArrayList<PersonaNegocio>();
		List<PersonaNegocioExt> listPersonaNegocioExt = new ArrayList<PersonaNegocioExt>();
		@SuppressWarnings("unused")
		String res;
		listPersonaNegocio = ifzPersonaNegocio.findByColumnName("negocio", negocio);
		
		for(PersonaNegocio personaNegocio : listPersonaNegocio){
			PersonaNegocioExt personaNegocioExt = new PersonaNegocioExt();
			
			personaNegocioExt.setId(personaNegocio.getId());
			personaNegocioExt.setCreadoPor(personaNegocio.getCreadoPor());
			personaNegocioExt.setFechaCreacion(personaNegocio.getFechaCreacion());
			personaNegocioExt.setModificadoPor(personaNegocio.getModificadoPor());
			personaNegocioExt.setFechaModificacion(personaNegocio.getFechaModificacion());
			
			if (personaNegocio.getPersona() != null){
				PersonaExt personaExt = new PersonaExt();
				personaExt = convertPersonaToPersonaExt(personaNegocio.getPersona());
				personaNegocioExt.setPersona(personaExt);
			}
			personaNegocioExt.setNegocio(personaNegocio.getNegocio());
			personaNegocioExt.setPersonalidadJuridica(personaNegocio.getPersonalidadJuridica());
			personaNegocioExt.setParticipacion(personaNegocio.getParticipacion());
			personaNegocioExt.setPuesto(personaNegocio.getPuesto());
			
			if (personaNegocio.getApoderadoId() != null){
				PersonaExt personaExt = new PersonaExt();
				personaExt = convertPersonaToPersonaExt(personaNegocio.getApoderadoId());
				personaNegocioExt.setApoderadoId(personaExt);
			}
			if (personaNegocio.getFirmaDocumento() != null){
				if (personaNegocio.getFirmaDocumento() > 0 && personaNegocio.getFirmaDocumento() == 1){			
					personaNegocioExt.setFirmaDocumento(true);
				}else{
					personaNegocioExt.setFirmaDocumento(false);
				}
			}
			
			if (personaNegocio.getRepresentante() != null){
				if (personaNegocio.getRepresentante() > 0 && personaNegocio.getRepresentante() == 1){			
					personaNegocioExt.setRepresentante(true);
				}else{
					personaNegocioExt.setRepresentante(false);
				}
			}
			
			listPersonaNegocioExt.add(personaNegocioExt);
		}
		
		return listPersonaNegocioExt;
	}	
	
	@Override
	public HashMap<Long, String> getDescEstatusNegocio() {
		return descEstatusNegocio;
	}

	@Override
	public HashMap<Long, String> getDescEstatusContacto() {
		return descEstatusContacto;
	}
	
	public List<Negocio> findLikeColumnName(String propertyName, String value) {
		try {
			return this.ifzNegocio.findLikeColumnName(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Negocio> findByProperty(String propertyName, Object value, int limite) {
		try {
			return this.ifzNegocio.findByProperty(propertyName, value, false, false, "", limite); //.findByProperty(propertyName, value, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Negocio> findLikeProperty(String propertyName, String value, int limite) {
		try {
			return this.ifzNegocio.findLikeProperty(propertyName, value, false, false, "", limite); //.findLikeProperty(propertyName, value, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public Respuesta buscarDomicilio(long idNegocio) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<DomicilioExt> lista = null;
		DomicilioExt dom = null;
		Negocio negocio = null;

		try {
			// Recuperamos los domicilios registrados del Negocio
			negocio = this.findById(idNegocio);
			
			// Recuperamos el domicilio activo principal o el primer domicilio activo
			lista = this.ifzClientes.buscarDomicilioExt(negocio); 
			for (DomicilioExt var : lista) {
				if (dom == null && var.getEstatus())
					dom = var;
				if (var.getEstatus() && dom.getPrincipal()) {
					dom = var;
					break;
				}
			}

			// Validamos domicilio
			if (dom == null) {
				respuesta.getErrores().setDescError("El Negocio indicado no tiene capturado un Domicilio o ninguno esta activo");
				respuesta.getErrores().setCodigoError(1L);
				return respuesta;
			}
			
			respuesta.getBody().addValor("pojo", dom);
			respuesta.getBody().addValor("domicilio", getParte(dom, "calles", "Conocido"));
			respuesta.getBody().addValor("numero_exterior", getParte(dom, "numero_exterior", "SN"));
			respuesta.getBody().addValor("numero_interior", getParte(dom, "numero_interior", ""));
			
			if ("XEXX010101000".equals(negocio.getRfc())) {
				respuesta.getBody().addValor("colonia", "");
				respuesta.getBody().addValor("codigo_postal", "SN");
				respuesta.getBody().addValor("ciudad", "");
				respuesta.getBody().addValor("localidad", "");
				respuesta.getBody().addValor("municipio", "");
				respuesta.getBody().addValor("estado", "");
				respuesta.getBody().addValor("id_colonia", 0L);
				respuesta.getBody().addValor("id_ciudad", 0L);
				respuesta.getBody().addValor("id_localidad", 0L);
				respuesta.getBody().addValor("id_municipio", 0L);
				respuesta.getBody().addValor("id_estado", 0L);
				respuesta.getBody().addValor("pais", "");
			} else if (dom.getColonia() != null) {
				respuesta.getBody().addValor("colonia", dom.getColonia().getNombre());
				respuesta.getBody().addValor("codigo_postal", dom.getColonia().getCp());
				respuesta.getBody().addValor("ciudad", dom.getColonia().getLocalidad().getNombre());
				respuesta.getBody().addValor("localidad", dom.getColonia().getLocalidad().getNombre());
				respuesta.getBody().addValor("municipio", dom.getColonia().getLocalidad().getMunicipio().getNombre());
				respuesta.getBody().addValor("estado", dom.getColonia().getLocalidad().getMunicipio().getEstado().getNombre());
				respuesta.getBody().addValor("id_colonia", dom.getColonia().getId());
				respuesta.getBody().addValor("id_ciudad", dom.getColonia().getLocalidad().getId());
				respuesta.getBody().addValor("id_localidad", dom.getColonia().getLocalidad().getId());
				respuesta.getBody().addValor("id_municipio", dom.getColonia().getLocalidad().getMunicipio().getId());
				respuesta.getBody().addValor("id_estado", dom.getColonia().getLocalidad().getMunicipio().getEstado().getId());
				respuesta.getBody().addValor("pais", "Mexico");
			}
			
			return respuesta;
		} catch (Exception e) {
			throw e;
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	// PRIVADAS
	// ----------------------------------------------------------------------------------------------------------------------------

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();//.getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	private CanalesNegocio convertCanalesNegocioExtToCanalesNegocio(CanalesNegocioExt pojoCanalExt){
		CanalesNegocio pojoCanal = new CanalesNegocio();
		
		pojoCanal.setId(pojoCanalExt.getId());
		pojoCanal.setIdCanal(pojoCanalExt.getIdCanal().getId());
		pojoCanal.setIdNegocio(pojoCanalExt.getIdNegocio());
		
		pojoCanal.setCreadoPor(pojoCanalExt.getCreadoPor());
		pojoCanal.setFechaCreacion(pojoCanalExt.getFechaCreacion());
		pojoCanal.setModificadoPor(pojoCanalExt.getModificadoPor());
		pojoCanal.setFechaModificacion(pojoCanalExt.getFechaModificacion());
		
		return pojoCanal;
	}
	
	private CanalesNegocioExt convertCanalesNegocioToCanalesNegocioExt(CanalesNegocio pojoCanal){
		CanalesNegocioExt pojoCanalExt = new CanalesNegocioExt();
		
		pojoCanalExt.setId(pojoCanal.getId());
		pojoCanalExt.setIdCanal(findCanalCat(pojoCanal.getIdCanal()));
		pojoCanalExt.setIdNegocio(pojoCanal.getIdNegocio());
		
		pojoCanalExt.setCreadoPor(pojoCanal.getCreadoPor());
		pojoCanalExt.setFechaCreacion(pojoCanal.getFechaCreacion());
		pojoCanalExt.setModificadoPor(pojoCanal.getModificadoPor());
		pojoCanalExt.setFechaModificacion(pojoCanal.getFechaModificacion());
		
		return pojoCanalExt;
	}
	
	private ContactoNegocioExt convertContactoNegocioToContactoNegocioExt(ContactoNegocio pojoContactoNegocio){
		ContactoNegocioExt pojoContactoNegocioExt = new ContactoNegocioExt();
		try{
			pojoContactoNegocioExt.setId(pojoContactoNegocio.getId());
			pojoContactoNegocioExt.setEstatusId(pojoContactoNegocio.getEstatusId());
			pojoContactoNegocioExt.setFechaCreacion(pojoContactoNegocio.getFechaCreacion());
			pojoContactoNegocioExt.setCreadoPor(pojoContactoNegocio.getCreadoPor());
			pojoContactoNegocioExt.setFechaModificacion(pojoContactoNegocio.getFechaModificacion());
			pojoContactoNegocioExt.setModificadoPor(pojoContactoNegocio.getModificadoPor());
			pojoContactoNegocioExt.setIdNegocio(pojoContactoNegocio.getIdNegocio());
			pojoContactoNegocioExt.setIdPersonaContacto(convertPersonaToPersonaExt(pojoContactoNegocio.getIdPersonaContacto()));
			pojoContactoNegocioExt.setPuesto(pojoContactoNegocio.getPuesto());
		} catch (Exception e){
			log.error("Error en NegociosFac.convertContactoNegocioToContactoNegocioExt", e);
		}
		return pojoContactoNegocioExt;
	}
	
	private ContactoNegocio convertContactoNegocioExtToContactoNegocio(ContactoNegocioExt pojoContactoNegocioExt){
		ContactoNegocio pojoContactoNegocio = new ContactoNegocio();
		try{
			pojoContactoNegocio.setId(pojoContactoNegocioExt.getId());
			pojoContactoNegocio.setEstatusId(pojoContactoNegocioExt.getEstatusId());
			pojoContactoNegocio.setFechaCreacion(pojoContactoNegocioExt.getFechaCreacion());
			pojoContactoNegocio.setCreadoPor(pojoContactoNegocioExt.getCreadoPor());
			pojoContactoNegocio.setFechaModificacion(pojoContactoNegocioExt.getFechaModificacion());
			pojoContactoNegocio.setModificadoPor(pojoContactoNegocioExt.getModificadoPor());
			pojoContactoNegocio.setIdNegocio(pojoContactoNegocioExt.getIdNegocio());
			pojoContactoNegocio.setIdPersonaContacto(convertPersonaExtToPersona(pojoContactoNegocioExt.getIdPersonaContacto()));
			pojoContactoNegocio.setPuesto(pojoContactoNegocioExt.getPuesto());
		} catch (Exception e){
			log.error("Error en NegociosFac.convertContactoNegocioToContactoNegocioExt", e);
		}
		return pojoContactoNegocio;
	}
	
	private EstadosNegocioExt convertEstadosNegocioToEstadosNegocioExt(EstadosNegocio pojoEstadosNegocio){
		EstadosNegocioExt pojoEstadosNegocioExt = new EstadosNegocioExt();
		try{
			pojoEstadosNegocioExt.setId(pojoEstadosNegocio.getId());
			pojoEstadosNegocioExt.setFechaCreacion(pojoEstadosNegocio.getFechaCreacion());
			pojoEstadosNegocioExt.setCreadoPor(pojoEstadosNegocio.getCreadoPor());
			pojoEstadosNegocioExt.setFechaModificacion(pojoEstadosNegocio.getFechaModificacion());
			pojoEstadosNegocioExt.setModificadoPor(pojoEstadosNegocio.getModificadoPor());
			pojoEstadosNegocioExt.setIdNegocio(pojoEstadosNegocio.getIdNegocio());
			pojoEstadosNegocioExt.setIdEstado(ifzEstados.findById(pojoEstadosNegocio.getIdEstado()));
		} catch (Exception e) {
			log.error("Error en NegociosFac.convertEstadosNegocioToEstadosNegocioExt", e);
		}
		return pojoEstadosNegocioExt;
	}
	
	private EstadosNegocio convertEstadosNegocioExtToEstadosNegocio(EstadosNegocioExt pojoEstadosNegocioExt){
		EstadosNegocio pojoEstadosNegocio = new EstadosNegocio();
		try{
			pojoEstadosNegocio.setId(pojoEstadosNegocioExt.getId());
			pojoEstadosNegocio.setFechaCreacion(pojoEstadosNegocioExt.getFechaCreacion());
			pojoEstadosNegocio.setCreadoPor(pojoEstadosNegocioExt.getCreadoPor());
			pojoEstadosNegocio.setFechaModificacion(pojoEstadosNegocioExt.getFechaModificacion());
			pojoEstadosNegocio.setModificadoPor(pojoEstadosNegocioExt.getModificadoPor());
			pojoEstadosNegocio.setIdNegocio(pojoEstadosNegocioExt.getIdNegocio());
			pojoEstadosNegocio.setIdEstado(pojoEstadosNegocioExt.getIdEstado().getId());
		} catch (Exception e) {
			log.error("Error en NegociosFac.convertEstadosNegocioExtToEstadosNegocio", e);
		}
		return pojoEstadosNegocio;
	}
	
	private Persona convertPersonaExtToPersona(PersonaExt personaExt){
		Persona persona = new Persona();
		persona.setId(personaExt.getId());
		persona.setCreadoPor(personaExt.getCreadoPor());
		persona.setFechaCreacion(personaExt.getFechaCreacion());
		persona.setModificadoPor(personaExt.getModificadoPor());
		persona.setFechaModificacion(personaExt.getFechaModificacion());
		if (personaExt.getHomonimo() != null){
			if (personaExt.getHomonimo() == true){
				persona.setHomonimo(1L);
			}else{
				persona.setHomonimo(-1L);
			}
		}
		persona.setNombre(personaExt.getNombre());
		persona.setPrimerNombre(personaExt.getPrimerNombre());
		persona.setSegundoNombre(personaExt.getSegundoNombre());
		persona.setNombresPropios(personaExt.getNombresPropios());
		persona.setPrimerApellido(personaExt.getPrimerApellido());
		persona.setSegundoApellido(personaExt.getSegundoApellido());
		persona.setRfc(personaExt.getRfc());
		persona.setSexo(personaExt.getSexo());
		persona.setFechaNacimiento(personaExt.getFechaNacimiento());
		if (personaExt.getEstadoCivil() != null){
			persona.setEstadoCivil(personaExt.getEstadoCivil().getId());
		}
		if (persona.getLocalidad() != null){
			persona.setLocalidad(personaExt.getLocalidad().getId());
		}
		if (persona.getNacionalidad() != null){
			persona.setNacionalidad(personaExt.getNacionalidad().getId());
		}
		if (personaExt.getConyuge() != null)
			persona.setConyuge(convertPersonaExtToPersona(personaExt.getConyuge()));
		persona.setNumeroHijos(personaExt.getNumeroHijos());
		persona.setDomicilio(personaExt.getDomicilio());
		persona.setTelefono(personaExt.getTelefono());
		persona.setCorreo(personaExt.getCorreo());
		persona.setNumeroCuenta(personaExt.getNumeroCuenta());
		persona.setClabeInterbancaria(personaExt.getClabeInterbancaria());
		persona.setBanco(personaExt.getBanco());
		
		if (personaExt.getFinado() == true){
			persona.setFinado(1L);
		}else{
			persona.setFinado(-1L);
		}
		
		if (persona.getTipoSangre() != null){
			persona.setTipoSangre(personaExt.getTipoSangre().getId());
		}
		if (persona.getColonia() != null){
			persona.setColonia(personaExt.getColonia().getId());
		}
		return persona;
	}
	
	private PersonaExt convertPersonaToPersonaExt(Persona persona){
		PersonaExt personaExt =  new PersonaExt();
		
		personaExt.setId(persona.getId());
		personaExt.setCreadoPor(persona.getCreadoPor());
		personaExt.setFechaCreacion(persona.getFechaCreacion());
		personaExt.setModificadoPor(persona.getModificadoPor());
		personaExt.setFechaModificacion(persona.getFechaModificacion());
		if (persona.getHomonimo() > 0){
			personaExt.setHomonimo(true);
		}else{
			personaExt.setHomonimo(false);
		}
		personaExt.setNombre(persona.getNombre());
		personaExt.setPrimerNombre(persona.getPrimerNombre());
		personaExt.setSegundoNombre(persona.getSegundoNombre());
		personaExt.setNombresPropios(persona.getNombresPropios());
		personaExt.setPrimerApellido(persona.getPrimerApellido());
		personaExt.setSegundoApellido(persona.getSegundoApellido());
		personaExt.setRfc(persona.getRfc());
		personaExt.setSexo(persona.getSexo());
		personaExt.setFechaNacimiento(persona.getFechaNacimiento());
		if (persona.getEstadoCivil() != null){
			personaExt.setEstadoCivil(ifzConValores.findById(persona.getEstadoCivil()));
		}
		if (persona.getLocalidad() != null){
			personaExt.setLocalidad(ifzLocalidad.findById(persona.getLocalidad()));
		}
		if (persona.getNacionalidad() != null){
			personaExt.setNacionalidad(ifzConValores.findById(persona.getNacionalidad()));
		}
		if (persona.getConyuge() != null)
			personaExt.setConyuge(convertPersonaToPersonaExt(persona.getConyuge()));
		personaExt.setNumeroHijos(persona.getNumeroHijos());
		personaExt.setDomicilio(persona.getDomicilio());
		personaExt.setTelefono(persona.getTelefono());
		personaExt.setCorreo(persona.getCorreo());
		personaExt.setNumeroCuenta(persona.getNumeroCuenta());
		personaExt.setClabeInterbancaria(persona.getClabeInterbancaria());
		personaExt.setBanco(persona.getBanco());
		
		if (persona.getFinado() > 0){
			personaExt.setFinado(true);
		}else{
			personaExt.setFinado(false);
		}
		
		if (persona.getTipoSangre() != null){
			personaExt.setTipoSangre(ifzConValores.findById(persona.getTipoSangre()));
		}
		if (persona.getColonia() != null){
			personaExt.setColonia(ifzColonia.findById(persona.getColonia()));
		}
		
		String res = persona.getPrimerNombre() != null && persona.getPrimerNombre().length() > 0? " " + persona.getPrimerNombre() : "";
		res += persona.getSegundoNombre()	!= null && persona.getSegundoNombre().length() > 0 ? " " + persona.getSegundoNombre() : "";
		res += persona.getNombresPropios()	!= null && persona.getNombresPropios().length() > 0 ? " " + persona.getNombresPropios() : "";
		res += persona.getPrimerApellido()	!= null && persona.getPrimerApellido().length() > 0 ? " " + persona.getPrimerApellido() : "";
		res += persona.getSegundoApellido()	!= null && persona.getSegundoApellido().length() > 0 ? " " + persona.getSegundoApellido() : "";
		
		personaExt.setNombreCompleto(res);
		
		return personaExt;
	}
	
	private PersonaNegocio convertPersonaNegocioExtToPersonaNegocio(PersonaNegocioExt personaNegocioExt){
		PersonaNegocio personaNegocio = new PersonaNegocio();
		
		personaNegocio.setId(personaNegocioExt.getId());
		personaNegocio.setCreadoPor(personaNegocioExt.getCreadoPor());
		personaNegocio.setFechaCreacion(personaNegocioExt.getFechaCreacion());
		personaNegocio.setModificadoPor(personaNegocioExt.getModificadoPor());
		personaNegocio.setFechaModificacion(personaNegocioExt.getFechaModificacion());
		
		if (personaNegocioExt.getPersona() != null){
			Persona persona = new Persona();
			persona = convertPersonaExtToPersona(personaNegocioExt.getPersona());
			personaNegocio.setPersona(persona);
		}
		personaNegocio.setNegocio(personaNegocioExt.getNegocio());
		personaNegocio.setPersonalidadJuridica(personaNegocioExt.getPersonalidadJuridica());
		personaNegocio.setParticipacion(personaNegocioExt.getParticipacion());
		personaNegocio.setPuesto(personaNegocioExt.getPuesto());
		
		if (personaNegocioExt.getApoderadoId() != null){
			Persona persona = new Persona();
			persona = convertPersonaExtToPersona(personaNegocioExt.getApoderadoId());
			personaNegocio.setApoderadoId(persona);
			personaNegocio.setApoderadoId(persona);
		}
		
		if (personaNegocioExt.getFirmaDocumento() != null){
			if (personaNegocioExt.getFirmaDocumento()){			
				personaNegocio.setFirmaDocumento(1L);
			}else{
				personaNegocio.setFirmaDocumento(-1L);
			}
		}else{
			personaNegocio.setFirmaDocumento(-1L);
		}
		
		if (personaNegocioExt.getRepresentante() != null){
			if (personaNegocioExt.getRepresentante()){			
				personaNegocio.setRepresentante(1L);
			}else{
				personaNegocio.setRepresentante(-1L);
			}
		}
		
		return personaNegocio;
	}
	
	private PersonaNegocioExt convertPersonaNegocioToPersonaNegocioExt(PersonaNegocio personaNegocio){
		PersonaNegocioExt personaNegocioExt = new PersonaNegocioExt();
		
		personaNegocioExt.setId(personaNegocio.getId());
		personaNegocioExt.setCreadoPor(personaNegocio.getCreadoPor());
		personaNegocioExt.setFechaCreacion(personaNegocio.getFechaCreacion());
		personaNegocioExt.setModificadoPor(personaNegocio.getModificadoPor());
		personaNegocioExt.setFechaModificacion(personaNegocio.getFechaModificacion());
		
		if (personaNegocio.getPersona() != null){
			personaNegocioExt.setPersona(convertPersonaToPersonaExt(personaNegocio.getPersona()));
		}
		personaNegocioExt.setNegocio(personaNegocio.getNegocio());
		personaNegocioExt.setPersonalidadJuridica(personaNegocio.getPersonalidadJuridica());
		personaNegocioExt.setParticipacion(personaNegocio.getParticipacion());
		personaNegocioExt.setPuesto(personaNegocio.getPuesto());
		
		if (personaNegocio.getApoderadoId() != null){
			personaNegocioExt.setApoderadoId(convertPersonaToPersonaExt(personaNegocio.getApoderadoId()));
		}
		
		if (personaNegocio.getFirmaDocumento() > 0L){
			personaNegocioExt.setFirmaDocumento(true);
		}else{
			personaNegocioExt.setFirmaDocumento(false);
		}
		
		if (personaNegocio.getRepresentante() > 0L){
			personaNegocioExt.setRepresentante(true);
		} else{
			personaNegocioExt.setRepresentante(false);
		}
		
		return personaNegocioExt;
	}
	
	private Respuesta setTelefonoPrincipal(DirectorioTelefonicoExt pojoDirectorioExt){
		Respuesta respuesta = new Respuesta();
		try{
			Negocio pojoNegocio = pojoDirectorioExt.getNegocio();
			
			List<DirectorioTelefonicoExt> listDirectorioExt = new ArrayList<DirectorioTelefonicoExt>();
			List<DirectorioTelefonico> listDirectorio =  ifzDirectorio.findByColumnName("negocio", pojoNegocio);
			DirectorioTelefonico pojoDirectorioTelefonico = convertDirectorioTelefonicoExtToDirectoTelefonico(pojoDirectorioExt);
			
			for(DirectorioTelefonico pojoDirectorioAux : listDirectorio){
				if (pojoDirectorioAux.getId() == pojoDirectorioTelefonico.getId()){
					pojoDirectorioAux.setModificadoPor(infoSesion.getAcceso().getId());
					pojoDirectorioAux.setFechaModificacion(Calendar.getInstance().getTime());
					
					pojoDirectorioAux.setPrincipal(1L);
					
					ifzDirectorio.update(pojoDirectorioAux);
				} else{
					if (pojoDirectorioAux.getPrincipal() > 0L){
						pojoDirectorioAux.setModificadoPor(infoSesion.getAcceso().getId());
						pojoDirectorioAux.setFechaModificacion(Calendar.getInstance().getTime());
						
						pojoDirectorioAux.setPrincipal(0L);
						
						ifzDirectorio.update(pojoDirectorioAux);
					}
				}
				
				listDirectorioExt.add(convertDirectorioTelefonicoToDirectoTelefonicoExt(pojoDirectorioAux));
			}
			
			pojoNegocio.setTelefono(pojoDirectorioExt.getNumeroTelefono());
			
			pojoNegocio.setModificadoPor(infoSesion.getAcceso().getId());
			pojoNegocio.setFechaModificacion(Calendar.getInstance().getTime());
			
			ifzNegocio.update(pojoNegocio);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listDirectorios", listDirectorioExt);
			respuesta.getBody().addValor("pojoNegocio", pojoNegocio);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("modulo", Errores.ERROR_ESTABLECER_TELEFONO_PRINCIPAL);
			respuesta.setBody(null);
			log.error("Error en NegociosFac.setTelefonoPrincipal", e);
		}
		return respuesta;
	}

	private boolean validaRFCMoral(String rfc) {
		if (rfc == null || rfc.length() == 0 || "XAXX010101000 XEXX010101000".contains(rfc.trim()))
			return true;
		
		Pattern p = Pattern.compile("(^[\\w&]{3}\\d{6}[\\d\\w]{3})");
		Matcher m = p.matcher(rfc);
		
		return m.find();
	}
	
	private boolean validaRFCFisico(String rfc) {
		if (rfc == null || rfc.length() == 0 || "XAXX010101000 XEXX010101000".contains(rfc.trim()))
			return true;
		
		Pattern p = Pattern.compile("(^\\w{4}\\d{6}[\\d\\w]{3})");
		Matcher m = p.matcher(rfc);
		
		return m.find();
	}
	
	private Domicilio convertDomicilioExtToDomicilio(DomicilioExt pojoDomicilioExt) throws Exception{
		Domicilio pojoDomicilio = new Domicilio();
		
		try{
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(pojoDomicilio, pojoDomicilioExt);	
			if (pojoDomicilio.getColonia() != null)
				pojoDomicilio.setColonia(pojoDomicilioExt.getColonia().getId());
		} catch (Exception e){
			log.error("Error en NegociosFac.convertDomicilioExtToDomicilio");
			throw e;
		}
		
		return pojoDomicilio;
	}
	
	private DomicilioExt convertDomicilioToDomicilioExt(Domicilio pojoDomicilio) throws Exception{
		DomicilioExt pojoDomicilioExt = new DomicilioExt();
		
		try{
			pojoDomicilioExt.setCatDomicilio1(pojoDomicilio.getCatDomicilio1());
			pojoDomicilioExt.setCatDomicilio2(pojoDomicilio.getCatDomicilio2());
			pojoDomicilioExt.setCatDomicilio3(pojoDomicilio.getCatDomicilio3());
			pojoDomicilioExt.setCatDomicilio4(pojoDomicilio.getCatDomicilio4());
			pojoDomicilioExt.setCatDomicilio5(pojoDomicilio.getCatDomicilio5());
			
			Colonia col = new Colonia();
			col = ifzColonia.findById(pojoDomicilio.getColonia());
			
			pojoDomicilioExt.setColonia(col);
			
			pojoDomicilioExt.setCreadoPor(pojoDomicilio.getCreadoPor());
			pojoDomicilioExt.setDescripcionDomicilio1(pojoDomicilio.getDescripcionDomicilio1());
			pojoDomicilioExt.setDescripcionDomicilio2(pojoDomicilio.getDescripcionDomicilio2());
			pojoDomicilioExt.setDescripcionDomicilio3(pojoDomicilio.getDescripcionDomicilio3());
			pojoDomicilioExt.setDescripcionDomicilio4(pojoDomicilio.getDescripcionDomicilio4());
			pojoDomicilioExt.setDescripcionDomicilio5(pojoDomicilio.getDescripcionDomicilio5());
			String res = "";
			
			ConValores catDomicilio1 = new ConValores();
			catDomicilio1 = ifzConValores.findById(pojoDomicilioExt.getCatDomicilio1());
			ConValores catDomicilio2 = new ConValores();
			catDomicilio2 = ifzConValores.findById(pojoDomicilioExt.getCatDomicilio2());
			ConValores catDomicilio3 = new ConValores();
			catDomicilio3 = ifzConValores.findById(pojoDomicilioExt.getCatDomicilio3());
			ConValores catDomicilio4 = new ConValores();
			catDomicilio4 = ifzConValores.findById(pojoDomicilioExt.getCatDomicilio4());
			ConValores catDomicilio5 = new ConValores();
			catDomicilio5 = ifzConValores.findById(pojoDomicilioExt.getCatDomicilio5());		
			
			if (pojoDomicilioExt.getDescripcionDomicilio1() != null && !"".equals(pojoDomicilioExt.getDescripcionDomicilio1().trim()) && catDomicilio1 != null)
				res += catDomicilio1.getAtributo1() + " " + pojoDomicilioExt.getDescripcionDomicilio1() + " ";
			if (pojoDomicilioExt.getDescripcionDomicilio2() != null && !"".equals(pojoDomicilioExt.getDescripcionDomicilio2().trim()) && catDomicilio2 != null)
				res += catDomicilio2.getAtributo1() + " " + pojoDomicilioExt.getDescripcionDomicilio2()  + " ";
			if (pojoDomicilioExt.getDescripcionDomicilio3() != null && !"".equals(pojoDomicilioExt.getDescripcionDomicilio3().trim()) && catDomicilio3 != null)
				res += catDomicilio3.getAtributo1() + " " + pojoDomicilioExt.getDescripcionDomicilio3() + " ";
			if (pojoDomicilioExt.getDescripcionDomicilio4() != null && !"".equals(pojoDomicilioExt.getDescripcionDomicilio4().trim()) && catDomicilio4 != null)
				res += catDomicilio4.getAtributo1() + " " + pojoDomicilioExt.getDescripcionDomicilio4() + " ";
			if (pojoDomicilioExt.getDescripcionDomicilio5() != null && !"".equals(pojoDomicilioExt.getDescripcionDomicilio5().trim()) && catDomicilio5 != null)
				res += catDomicilio5.getAtributo1() + " " + pojoDomicilioExt.getDescripcionDomicilio5();
			
			if (pojoDomicilioExt.getColonia() != null){
				res += ", " + pojoDomicilioExt.getColonia().getNombre();
			}
			if (pojoDomicilioExt.getColonia().getLocalidad() != null){
				res += ", " + pojoDomicilioExt.getColonia().getLocalidad().getNombre();
			}
			if (pojoDomicilioExt.getColonia().getLocalidad().getMunicipio() != null){
				res += ", " + pojoDomicilioExt.getColonia().getLocalidad().getMunicipio().getNombre();
			}
			if (pojoDomicilioExt.getColonia() != null){
				res += " CP " + pojoDomicilioExt.getColonia().getCp();
			}
			
			pojoDomicilioExt.setDomicilio(res);
			
			pojoDomicilioExt.setEstatus(pojoDomicilio.getEstatus() > 0 ? true : false);
			pojoDomicilioExt.setFechaCreacion(pojoDomicilio.getFechaCreacion());
			pojoDomicilioExt.setId(pojoDomicilio.getId());
			pojoDomicilioExt.setModificadoPor(pojoDomicilio.getModificadoPor());
			pojoDomicilioExt.setNegocio(pojoDomicilio.getNegocio());
			pojoDomicilioExt.setObservaciones(pojoDomicilio.getObservaciones());
			pojoDomicilioExt.setPrincipal(pojoDomicilio.getPrincipal() > 0 ? true : false);
			
			if (pojoDomicilio.getPersona() != null)
				pojoDomicilioExt.setPersona(convertPersonaToPersonaExt(pojoDomicilio.getPersona()));
			
			if (pojoDomicilioExt.getColonia() != null)
				pojoDomicilioExt.setNombreColonia(pojoDomicilioExt.getColonia().getNombre());
		} catch (Exception e){
			log.error("Error en NegociosFac.convertDomicilioToDomicilioExt");
			throw e;
		}
		
		return pojoDomicilioExt;
	}
	
	private CanalCat findCanalCat(long id){
		ConValores valor = ifzConValores.findById(id);
		return new CanalCat(valor.getId(), valor.getValor());
	}

	/**
	 * Devuelve la direccion de acuerdo a la parte indicada
	 * @param dom Objeto extendido domicilio
	 * @param parte full, todo, calle, calles, domicilio, direccion, numero_exterior, numero_interior
	 * @param defaultValue 
	 * @return
	 */
	private String getParte(DomicilioExt dom, String parte, String defaultValue) {
		String resultado = "";
		
		if (parte != null && "calle,calles".contains(parte.trim())) {
			// Calle principal
			if (dom.getCatDomicilio1().longValue() == 20 || dom.getCatDomicilio1().longValue() == 10000778)
				resultado = dom.getDescripcionDomicilio1();

			// Otras Calles
			if (dom.getCatDomicilio2().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio2().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio2().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio2();
			
			if (dom.getCatDomicilio3().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio3().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio3().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio3();
			
			if (dom.getCatDomicilio4().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio4().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio4().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio4();
			
			if (dom.getCatDomicilio5().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio5();
			else if (dom.getCatDomicilio5().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio5();
			else if (dom.getCatDomicilio5().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio5();
		} else if (parte != null && "numero_exterior".equals(parte.trim())) {
			if (dom.getCatDomicilio1().longValue() == 10000098)
				resultado = dom.getDescripcionDomicilio1();
			else if (dom.getCatDomicilio2().longValue() == 10000098)
				resultado = dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio3().longValue() == 10000098)
				resultado = dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio4().longValue() == 10000098)
				resultado = dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio5().longValue() == 10000098)
				resultado = dom.getDescripcionDomicilio5();
			else
				resultado = defaultValue;
		} else if (parte != null && "numero_interior".equals(parte.trim())) {
			if (dom.getCatDomicilio1().longValue() == 45)
				resultado = dom.getDescripcionDomicilio1();
			else if (dom.getCatDomicilio2().longValue() == 45)
				resultado = dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio3().longValue() == 45)
				resultado = dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio4().longValue() == 45)
				resultado = dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio5().longValue() == 45)
				resultado = dom.getDescripcionDomicilio5();
			else
				resultado = defaultValue;
		} else if (parte == null || "".equals(parte.trim()) || "full,todo".contains(parte.trim())) {
			// Calle principal
			if (dom.getCatDomicilio1().longValue() == 20 || dom.getCatDomicilio1().longValue() == 10000778)
				resultado = dom.getDescripcionDomicilio1();

			// Numero Exterior
			if (dom.getCatDomicilio1().longValue() == 10000098)
				resultado += "#" + dom.getDescripcionDomicilio1();
			else if (dom.getCatDomicilio2().longValue() == 10000098)
				resultado += "#" + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio3().longValue() == 10000098)
				resultado += "#" + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio4().longValue() == 10000098)
				resultado += "#" + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio5().longValue() == 10000098)
				resultado += "#" + dom.getDescripcionDomicilio5();

			// Numero Interior
			if (dom.getCatDomicilio1().longValue() == 45)
				resultado += " Int. " + dom.getDescripcionDomicilio1();
			else if (dom.getCatDomicilio2().longValue() == 45)
				resultado += " Int. " + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio3().longValue() == 45)
				resultado += " Int. " + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio4().longValue() == 45)
				resultado += " Int. " + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio5().longValue() == 45)
				resultado = dom.getDescripcionDomicilio5();

			// Otras calles
			if (dom.getCatDomicilio2().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio2().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio2().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio2();
			
			if (dom.getCatDomicilio3().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio3().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio3().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio3();
			
			if (dom.getCatDomicilio4().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio4().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio4().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio4();
			
			if (dom.getCatDomicilio5().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio5();
			else if (dom.getCatDomicilio5().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio5();
			else if (dom.getCatDomicilio5().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio5();
		} else {
			resultado = defaultValue;
		}
		
		return resultado;
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.1 | 2016-11-04 | Javier Tirado		| Añado el metodo findAll y findAll(String)
 * 2.1 | 2016-11-09 | Javier Tirado 	| Añado los metodos findByProperty y findLikeProperty
 */