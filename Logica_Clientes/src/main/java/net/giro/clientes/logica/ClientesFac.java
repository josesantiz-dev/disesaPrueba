package net.giro.clientes.logica;

import java.lang.reflect.InvocationTargetException;
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

import net.giro.clientes.beans.CatBancosExt;
import net.giro.clientes.beans.ContactoNegocio;
import net.giro.clientes.beans.ContactoNegocioExt;
import net.giro.clientes.beans.ContactoPersona;
import net.giro.clientes.beans.ContactoPersonaExt;
import net.giro.clientes.beans.DirectorioTelefonicoExt;
import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.beans.DirectorioTelefonico;
import net.giro.clientes.beans.Domicilio;
import net.giro.clientes.beans.Identificacion;
import net.giro.clientes.beans.IdentificacionExt;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.NegocioExt;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaEstudio;
import net.giro.clientes.beans.PersonaEstudioExt;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.beans.PersonaNegocio;
import net.giro.clientes.beans.PersonaNegocioExt;
import net.giro.clientes.beans.PersonaNombresAlterno;
import net.giro.clientes.beans.PersonaNombresAlternoExt;
import net.giro.clientes.dao.ContactoNegocioDAO;
import net.giro.clientes.dao.ContactoPersonaDAO;
import net.giro.clientes.dao.DirectorioTelefonicoDAO;
import net.giro.clientes.dao.DomicilioDAO;
import net.giro.clientes.dao.IdentificacionDAO;
import net.giro.clientes.dao.NegocioDAO;
import net.giro.clientes.dao.PersonaDAO;
import net.giro.clientes.dao.PersonaEstudioDAO;
import net.giro.clientes.dao.PersonaNegocioDAO;
import net.giro.clientes.dao.PersonaNombresAlternoDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConGrupoValoresDAO;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.EstadoDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.plataforma.ubicacion.dao.MunicipioDAO;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.dao.BancosDAO;
import net.giro.util.clientes.Errores;

@Stateless
public class ClientesFac implements ClientesRem {
	private static Logger log = Logger.getLogger(ClientesFac.class);
	private InitialContext ctx = null;
	private PersonaDAO ifzPersona;
	private NegocioDAO ifzNegocio;
	private MunicipioDAO ifzMunicipio;
	private DomicilioDAO ifzDomicilios;
	private ConValoresDAO ifzConValores;
	private ConGrupoValoresDAO ifzConGrupoValores;
	private EstadoDAO ifzEstado;
	private LocalidadDAO ifzLocalidad;
	private ColoniaDAO ifzColonia;
	private DirectorioTelefonicoDAO	ifzDirectorio;
	private PersonaEstudioDAO ifzEstudio;
	private IdentificacionDAO ifzIDentificacion;
	private PersonaNombresAlternoDAO ifzNombresAlternos;
	private PersonaNegocioDAO ifzPersonaNegocio;
	private ContactoPersonaDAO ifzContactoPersona;
	private ContactoNegocioDAO ifzContactoNegocio;
	private BancosDAO ifzBancos;
	private InfoSesion infoSesion;
	private long personaAuxiliar = 0L;
	private String modulo = "CLIENTES";
	private static final String PATTERN_RFC_PF = "[A-Z]{4}[0-9]{6}[A-Z0-9]{3}";
	private static final String PATTERN_RFC_PM = "[A-Z]{3}[0-9]{6}[A-Z0-9]{3}";
	private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public ClientesFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		this.ctx = new InitialContext(p);
    		this.ifzPersona 		= (PersonaDAO)				 this.ctx.lookup("ejb:/Model_Clientes//PersonaImp!net.giro.clientes.dao.PersonaDAO");		
    		this.ifzNegocio 		= (NegocioDAO)				 this.ctx.lookup("ejb:/Model_Clientes//NegocioImp!net.giro.clientes.dao.NegocioDAO");
    		this.ifzNombresAlternos = (PersonaNombresAlternoDAO) this.ctx.lookup("ejb:/Model_Clientes//PersonaNombresAlternoImp!net.giro.clientes.dao.PersonaNombresAlternoDAO");		
    		this.ifzEstudio 		= (PersonaEstudioDAO)		 this.ctx.lookup("ejb:/Model_Clientes//PersonaEstudioImp!net.giro.clientes.dao.PersonaEstudioDAO");
    		this.ifzDomicilios 		= (DomicilioDAO)			 this.ctx.lookup("ejb:/Model_Clientes//DomicilioImp!net.giro.clientes.dao.DomicilioDAO");
    		this.ifzDirectorio 		= (DirectorioTelefonicoDAO)	 this.ctx.lookup("ejb:/Model_Clientes//DirectorioTelefonicoImp!net.giro.clientes.dao.DirectorioTelefonicoDAO");
    		this.ifzIDentificacion 	= (IdentificacionDAO)		 this.ctx.lookup("ejb:/Model_Clientes//IdentificacionImp!net.giro.clientes.dao.IdentificacionDAO");
    		this.ifzPersonaNegocio 	= (PersonaNegocioDAO)		 this.ctx.lookup("ejb:/Model_Clientes//PersonaNegocioImp!net.giro.clientes.dao.PersonaNegocioDAO");
    		this.ifzContactoPersona = (ContactoPersonaDAO)		 this.ctx.lookup("ejb:/Model_Clientes//ContactoPersonaImp!net.giro.clientes.dao.ContactoPersonaDAO");
    		this.ifzContactoNegocio = (ContactoNegocioDAO)		 this.ctx.lookup("ejb:/Model_Clientes//ContactoNegocioImp!net.giro.clientes.dao.ContactoNegocioDAO");
    		this.ifzLocalidad 		= (LocalidadDAO)			 this.ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
    		this.ifzColonia 		= (ColoniaDAO)				 this.ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");		
    		this.ifzConValores 		= (ConValoresDAO)			 this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
    		this.ifzConGrupoValores = (ConGrupoValoresDAO)		 this.ctx.lookup("ejb:/Model_Publico//ConGrupoValoresImp!net.giro.plataforma.dao.ConGrupoValoresDAO");
    		this.ifzEstado 			= (EstadoDAO)				 this.ctx.lookup("ejb:/Model_Publico//EstadoImp!net.giro.plataforma.ubicacion.dao.EstadoDAO");
    		this.ifzMunicipio 		= (MunicipioDAO)			 this.ctx.lookup("ejb:/Model_Publico//MunicipioImp!net.giro.plataforma.ubicacion.dao.MunicipioDAO");
    		this.ifzBancos 			= (BancosDAO)				 this.ctx.lookup("ejb:/Model_TYG//BancosImp!net.giro.tyg.dao.BancosDAO");
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}	
	}
	
	
	public void setInfoSecion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@SuppressWarnings("unchecked")
	public List<ConValores> buscarPantallas() {
		@SuppressWarnings("rawtypes")
		HashMap params = new HashMap<String, String>();
		params.put("valor", "Personas");
		return this.ifzConValores.findByGrupoNombreByParams("SYS_PANTALLAS", params);
	}
	
	public List<ConValores> buscarEstadosCiviles() {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_ESTADO_CIVIL");
		for(ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores) {
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}
	
	public List<ConValores> buscarNacionalidades() {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_NACIONALIDADES");
		for(ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores) {
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}
	
	public List<ConValores> buscarTiposDeSangre() {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_TIPOS_SANGRE");
		for(ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores) {
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}
	
	public List<Estado> buscarEstados() {
		return this.ifzEstado.findAll();
	}

	@Override
	public List<ConValores> tipoDocumentos() {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_TIPO_DOCUMENTO");
		for(ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores) {
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}

	@Override
	public List<ConValores> documentos() {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_DOCUMENTOS");
		for(ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores) {
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}

	@Override
	public List<ConValores> nivelEstudios() {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_ESTUDIOS");
		for(ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores) {
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}

	@Override
	public List<ConValores> tiposIdentificaciones() {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_IDENTIFICACIONES");
		for(ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores) {
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}

	@Override
	public void eliminar(Persona persona) throws Exception  {
		ifzPersona.delete(persona.getId());
	}
	
	@Override
	public void eliminar(PersonaExt personaExt) throws Exception  {
		ifzPersona.delete(personaExt.getId());
	}

	@Override
	public Respuesta eliminar(PersonaNombresAlternoExt nombreAlternoExt) throws Exception  {
		Respuesta respuesta = new Respuesta();
		try{ 
			ifzNombresAlternos.delete(nombreAlternoExt.getId());
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_NOMBRE_ALTERNO);
			log.error("Error en el metodo eliminarPersonaNombresAlternoExt", e);
		}		
		return respuesta;
	}

	@Override
	public void eliminar(PersonaEstudio estudio) throws Exception  {
		 ifzEstudio.delete(estudio.getId());
	}
	
	@Override
	public void eliminar(PersonaEstudioExt personaEstudioExt) throws Exception  {		
		ifzEstudio.delete(personaEstudioExt.getId());
	}
	
	@Override
	public long salvar(Domicilio domicilio) throws Exception {
		if (domicilio.getId() > 0)
			ifzDomicilios.update(domicilio);
		else
			return ifzDomicilios.save(domicilio, null);
		return domicilio.getId();
	}
	
	@Override
	public Respuesta salvar(DomicilioExt domicilioExt) throws Exception  {
		Respuesta respuesta = new Respuesta();
		
		try{
			if (domicilioExt.getId() > 0) {
				domicilioExt.setModificadoPor(infoSesion.getAcceso().getId());
				domicilioExt.setFechaModificacion(Calendar.getInstance().getTime());
			}else{
				domicilioExt.setCreadoPor(infoSesion.getAcceso().getId());
				domicilioExt.setFechaCreacion(Calendar.getInstance().getTime());
				domicilioExt.setModificadoPor(infoSesion.getAcceso().getId());
				domicilioExt.setFechaModificacion(Calendar.getInstance().getTime());
			}
			if (domicilioExt.getColonia() != null) {
				if (domicilioExt.getColonia().getId() > 0) {
					Domicilio domicilio =  new Domicilio();
					domicilio = convertDomicilioExToDomicilio(domicilioExt);
					if (domicilio.getId() > 0) {
						ifzDomicilios.update(domicilio);
					}else{
						domicilio.setId(ifzDomicilios.save(domicilio, null));
					}
					domicilioExt = convertDomicilioToDomicilioExt(domicilio);
					respuesta.getBody().addValor("domicilio", domicilioExt);
				}else{
					respuesta.setBody(null);
					respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FALTA_DEFINIR_COLONIA);
				}
			}else{
				respuesta.setBody(null);
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FALTA_DEFINIR_COLONIA);
			}
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_DOMICILIO);
			log.error("Error en el metodo salvarSolicitud", e);
		}
		return respuesta;
	}

	@Override
	public void eliminar(Identificacion identificacion) throws Exception  {
		ifzIDentificacion.delete(identificacion.getId());
	}
	
	@Override
	public Respuesta eliminar(IdentificacionExt identificacionExt) throws Exception  {
		Respuesta respuesta = new Respuesta();
		try{
			ifzIDentificacion.delete(identificacionExt.getId());
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_IDENTIFICACION);
			log.error("Error en el metodo eliminarIdentificacion", e);
		}
		return respuesta;
	}
	
	@Override
	public long salvar(Persona persona) throws Exception  {
		if (persona.getId() > 0) {
			this.ifzPersona.update(persona);
			return persona.getId();
		}
		
		return this.ifzPersona.save(persona, getCodigoEmpresa());
	}
	
	@Override
	public Respuesta salvar(PersonaExt personaExt) throws Exception  {
		Respuesta respuesta = new Respuesta();
		List<Persona> personas = null;
		Persona persona = new Persona();
		
		try {
			if (personaExt.getId() > 0) {
				personaExt.setModificadoPor(infoSesion.getAcceso().getId());
				personaExt.setFechaModificacion(Calendar.getInstance().getTime());
			} else {
				personaExt.setModificadoPor(infoSesion.getAcceso().getId());
				personaExt.setFechaModificacion(Calendar.getInstance().getTime());
				personaExt.setCreadoPor(infoSesion.getAcceso().getId());
				personaExt.setFechaCreacion(Calendar.getInstance().getTime());
				//personaExt.setEstatus(1L);   
			}
			
			if (personaExt.getConyuge() != null && personaExt.getId() == personaExt.getConyuge().getId()) {
				respuesta.setBody(null);
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_CONYUGE_IGUAL_PERSONA);
				return respuesta;
			} 

			if (personaExt.getTipoPersona() == 2 && ! validaRFCMoral(personaExt.getRfc())) {
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_RFC);
				respuesta.getErrores().setDescError("Verifique, El RFC no es valido para Persona Moral");
				respuesta.setBody(null);
				return respuesta;
			} 
				
			if (personaExt.getTipoPersona() == 1 && ! validaRFCFisico(personaExt.getRfc())) {
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_RFC);
				respuesta.getErrores().setDescError("Verifique, El RFC no es valido para Persona Fisica");
				respuesta.setBody(null);
				return respuesta;
			}
			
			if (personaExt.getCorreo() != null && ! validaEmail(personaExt.getCorreo().trim())) {
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_EMAIL_INVALIDO);
				respuesta.getErrores().setDescError("Verifique, El Email no es valido");
				respuesta.setBody(null);
				return respuesta;
			}
			
			// Comprobamos que RFC ingresado no exista, si corresponde
			if (! "XAXX010101000 XEXX010101000".contains(personaExt.getRfc().trim())) {
				if (personaExt.getRfc() != null && ! "".equals(personaExt.getRfc().trim())) {
					personas = this.ifzPersona.findByProperty("rfc", personaExt.getRfc(), false, false, "", 0); // .findByColumnName("rfc", personaExt.getRfc());
					if (personas != null && ! personas.isEmpty()) {
						for (Persona item : personas) {
							if (item.getId() == personaExt.getId()) 
								continue;
							
							respuesta.getErrores().setCodigoError(Errores.ERROR_RFC_EXISTE);
							respuesta.getErrores().setDescError("Verifique, Persona existente\nRFC: " + item.getRfc() + ", ID: " + String.valueOf(item.getId()));
							respuesta.setBody(null);
							return respuesta;
						}
					}
				}
			}
			
			persona = convertPersonaExtToPersona(personaExt);
			personaExt = convertPersonaToPersonaExt(persona);
			if (persona.getId() > 0)
				this.ifzPersona.update(persona);
			else
				personaExt.setId(this.ifzPersona.save(persona, getCodigoEmpresa()));
			respuesta.getBody().addValor("persona", personaExt);
		} catch (Exception e) {
			log.error("Error en el metodo salvarPersona", e);
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_PERSONA);
		}

		return respuesta;
	}

	@Override
	public long salvar(DirectorioTelefonico directorio) throws Exception  {
		if (directorio.getId() > 0)
			this.ifzDirectorio.update(directorio);			
		else			
			return this.ifzDirectorio.save(directorio, null);
		return directorio.getId();
	}
	
	@Override
	public Respuesta salvar(DirectorioTelefonicoExt directorioExt) throws Exception  {
		Respuesta respuesta = new Respuesta();
		try{
			DirectorioTelefonico directorio = new DirectorioTelefonico();
			if (directorioExt.getId() > 0) {
				directorioExt.setModificadoPor(infoSesion.getAcceso().getId());
				directorioExt.setFechaModificacion(Calendar.getInstance().getTime());
			}else{
				directorioExt.setModificadoPor(infoSesion.getAcceso().getId());
				directorioExt.setFechaModificacion(Calendar.getInstance().getTime());
				directorioExt.setCreadoPor(infoSesion.getAcceso().getId());
				directorioExt.setFechaCreacion(Calendar.getInstance().getTime());
			}
			
			directorio = convertDirectorioExtToDirectorio(directorioExt);
			if (directorio.getId() > 0) {
				this.ifzDirectorio.update(directorio);
				directorioExt = convertDirectorioToDirectorioExt(directorio);
			}else{			
				directorio.setId(this.ifzDirectorio.save(directorio, null));
				directorioExt = convertDirectorioToDirectorioExt(directorio);
			}
			respuesta.getBody().addValor("directorio", directorioExt);
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_DIRECTORIO);
			log.error("Error en el metodo salvarDirectorioTelefonico", e);
		}		
		return respuesta;
	}

	@Override
	public Respuesta salvar(PersonaNombresAlternoExt nombreAlternoExt) throws Exception {
		Respuesta respuesta = new Respuesta();
		try{
			
			PersonaNombresAlterno nombreAlterno = new PersonaNombresAlterno();
			if (nombreAlternoExt.getId() > 0) {
				nombreAlternoExt.setModificadoPor(infoSesion.getAcceso().getId());
				nombreAlternoExt.setFechaModificacion(Calendar.getInstance().getTime());
			}else{
				nombreAlternoExt.setModificadoPor(infoSesion.getAcceso().getId());
				nombreAlternoExt.setFechaModificacion(Calendar.getInstance().getTime());
				nombreAlternoExt.setCreadoPor(infoSesion.getAcceso().getId());
				nombreAlternoExt.setFechaCreacion(Calendar.getInstance().getTime());
			}
			nombreAlterno = convertNombreAlternoExtToNombreAlterno(nombreAlternoExt);
			if (nombreAlternoExt.getId() > 0) {
				this.ifzNombresAlternos.update(nombreAlterno);
				nombreAlternoExt = convertNombreAlternoToNombreAlternoExt(nombreAlterno);
			}else{
				nombreAlternoExt.setId(this.ifzNombresAlternos.save(nombreAlterno, null));
			}
			respuesta.getBody().addValor("nombreAlterno", nombreAlternoExt);
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_DIRECTORIO);
			log.error("Error en el metodo salvarDirectorioTelefonico", e);
		}		
		return respuesta;
	}

	@Override
	public long salvar(PersonaEstudio estudio) throws Exception  {
		if (estudio.getId() > 0) {
			this.ifzEstudio.update(estudio);
		}else{
			return this.ifzEstudio.save(estudio, null);
		}
		return estudio.getId();
	}
	
	@Override
	public long salvar(PersonaEstudioExt personaEstudioExt) throws Exception  {
		PersonaEstudio personaEstudio = new PersonaEstudio();
		
		try {
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(personaEstudio, personaEstudioExt);			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		/*personaEstudio.setId(personaEstudioExt.getId());
		personaEstudio.setCreadoPor(personaEstudioExt.getCreadoPor());
		personaEstudio.setFechaCreacion(personaEstudioExt.getFechaCreacion());
		personaEstudio.setModificadoPor(personaEstudioExt.getModificadoPor());
		personaEstudio.setFechaModificacion(personaEstudioExt.getFechaModificacion());
		personaEstudio.setPersona(personaEstudioExt.getPersona());
		personaEstudio.setEstudio(personaEstudioExt.getEstudio());
		personaEstudio.setAniosEstudio(personaEstudioExt.getAniosEstudio());
		
		if (personaEstudioExt.getTerminoEstudio() == true) {
			personaEstudio.setTerminoEstudio(1L);
		}else{
			personaEstudio.setTerminoEstudio(-1L);
		}*/
		
		/*personaEstudio.setCarrera(personaEstudioExt.getCarrera());
		personaEstudio.setEscuela(personaEstudioExt.getEscuela());
		personaEstudio.setFechaIngreso(personaEstudioExt.getFechaIngreso());
		personaEstudio.setFechaEgreso(personaEstudioExt.getFechaEgreso());*/
		
		if (personaEstudio.getId() > 0) {
			this.ifzEstudio.update(personaEstudio);
		}else{
			return this.ifzEstudio.save(personaEstudio, null);
		}
		return personaEstudio.getId();
	}

	@Override
	public long salvar(Identificacion identificacion) throws Exception  {
		if (identificacion.getId() > 0) {
			this.ifzIDentificacion.update(identificacion);
		}else{
			return this.ifzIDentificacion.save(identificacion, null) ;
		}
		return identificacion.getId();
	}
	
	@Override
	public Respuesta salvar(IdentificacionExt identificacionExt) {
		Respuesta respuesta = new Respuesta();
		Identificacion identificacion = new Identificacion();
		
		try {
			identificacionExt.setPersona(buscarPersona(identificacionExt.getPersona().getId()));
			
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(identificacion, identificacionExt);
			
			identificacion.setModificadoPor(infoSesion.getAcceso().getId());
			identificacion.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (identificacion.getId() == 0) {
				identificacion.setCreadoPor(infoSesion.getAcceso().getId());
				identificacion.setFechaCreacion(Calendar.getInstance().getTime());
				
				identificacion.setId(this.ifzIDentificacion.save(identificacion, null));
			} else
				this.ifzIDentificacion.update(identificacion);
			
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(identificacionExt, identificacion);
			
			if (identificacion.getIdentificacion() != null) {
				ConValores tipoIdentificacion = ifzConValores.findById(identificacion.getIdentificacion());
				identificacionExt.setIdentificacionDesc(tipoIdentificacion.getValor());
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoIdentificacion", identificacionExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_IDENTIFICACION);
			respuesta.setBody(null);
			log.error("Error a salvar para Identificacion", e);
		}
		
		return respuesta;
	}

	@Override
	public List<Persona> buscarPersona(String tipoBusqueda, String valorBusqueda) throws Exception {
		valorBusqueda = (valorBusqueda != null && ! "".equals(valorBusqueda.trim()) ? valorBusqueda.trim().replace(" ", "%") : "");
		return ifzPersona.findLikeProperty(tipoBusqueda, valorBusqueda, false, false, "", 0); // .findLikeColumnName(tipoBusqueda, valorBusqueda);
	}
	
	@Override
	public List<PersonaExt> buscarPersonaExt(String tipoBusqueda, String valorBusqueda,Long tipoPersonaBusqueda) throws Exception {
		List<Persona> listPersonas = this.buscarPersona(tipoBusqueda, valorBusqueda); // ifzPersona.findLikeProperty(tipoBusqueda, valorBusqueda, false, false, "", 0); // .findLikePersonaPropiedad(tipoBusqueda, valorBusqueda,tipoPersonaBusqueda);
		List<PersonaExt> listPersonasExt =  new ArrayList<PersonaExt>();
		for(Persona persona : listPersonas) {
			PersonaExt personaExt =  new PersonaExt();
			personaExt = convertPersonaToPersonaExt(persona);
			listPersonasExt.add(personaExt);
		}
		return listPersonasExt;
	}
	
	@Override
	public List<PersonaExt> buscarPersonaGeneral(String tipoBusqueda, String valorBusqueda) throws Exception {
		List<Persona> listPersonas = ifzPersona.findLikeColumnName(tipoBusqueda, valorBusqueda);
		List<PersonaExt> listPersonasExt =  new ArrayList<PersonaExt>();
		for(Persona persona : listPersonas) {
			PersonaExt personaExt =  new PersonaExt();
			personaExt = convertPersonaToPersonaExt(persona);
			listPersonasExt.add(personaExt);
		}
		return listPersonasExt;
	}	

	@Override
	public Persona buscarPersona(long idPersona) throws Exception {
		return ifzPersona.findById(idPersona);
	}
	
	@Override
	public PersonaExt buscarPersonaExt(long idPersona) throws Exception {
		Persona persona = ifzPersona.findById(idPersona);
		PersonaExt personaExt =  new PersonaExt();
		personaExt = convertPersonaToPersonaExt(persona);
		
		return personaExt;
	}

	@Override
	public List<Municipio> buscarMunicipio(Estado estado,String busquedaMunicipio) throws Exception  {
		return ifzMunicipio.findByPropertyLikeValor("estado", estado, "nombre", busquedaMunicipio);
	}
	
	@Override
	public Municipio buscarMunicipio(Long id) throws Exception  {
		return ifzMunicipio.findById(id);
	}

	@Override
	public List<Localidad> buscarLocalidad(Municipio municipio,String busquedaLocalidad) throws Exception  {
		return ifzLocalidad.findByPropertyLikeValor("municipio", municipio, "nombre", busquedaLocalidad);
	}

	@Override
	public List<Colonia> buscarColonia(Localidad localidad,String busquedaColonia) throws Exception  {
		return this.ifzColonia.findByPropertyLikeValor("localidad", localidad, "nombre", busquedaColonia);
	}

	@Override
	public List<Domicilio> buscarDomicilio(Persona persona) throws Exception  {
		return this.ifzDomicilios.findByColumnName("persona", persona);
	}
	
	@Override
	public List<Domicilio> buscarDomicilio(PersonaExt personaExt) throws Exception  {
		Persona persona = new Persona();
		persona = convertPersonaExtToPersona(personaExt);
		return this.ifzDomicilios.findByColumnName("persona", persona);
	}
	
	public Domicilio buscarDomicilio(Long id) throws Exception  {
		return this.ifzDomicilios.findById(id);
	}
	
	@Override
	public List<DomicilioExt> buscarDomicilioExt(Persona persona) throws Exception  {
		List<DomicilioExt> listDomiciliosExt = new ArrayList<DomicilioExt>();
		List<Domicilio> listDomicilios = new ArrayList<Domicilio>();
		DomicilioExt domExt = null;
		
		listDomicilios = this.ifzDomicilios.findByColumnName("persona", persona);
		if (listDomicilios != null && ! listDomicilios.isEmpty()) {
			for (Domicilio dom : listDomicilios) {
				domExt = new DomicilioExt();
				domExt = convertDomicilioToDomicilioExt(dom);
				listDomiciliosExt.add(domExt);
			}
		}
		
		return listDomiciliosExt;
	}
	
	@Override
	public List<DomicilioExt> buscarDomicilioExt(PersonaExt personaExt) throws Exception {
		List<Domicilio> listDomicilios = new ArrayList<Domicilio>();
		List<DomicilioExt> listDomiciliosExt = new ArrayList<DomicilioExt>();
		Persona persona =  new Persona();
		
		persona = convertPersonaExtToPersona(personaExt);
		listDomicilios = this.ifzDomicilios.findByColumnName("persona", persona);
		for(Domicilio dom : listDomicilios) {
			DomicilioExt domExt =  convertDomicilioToDomicilioExt(dom);
			listDomiciliosExt.add(domExt);
		}
		return listDomiciliosExt;
	}

	@Override
	public List<DirectorioTelefonico> buscarDirectorio(Persona persona) throws Exception  {
		return this.ifzDirectorio.findByColumnName("persona", persona);
	}
	
	@Override
	public List<DirectorioTelefonicoExt> buscarDirectorioExt(Persona persona) throws Exception  {
		List<DirectorioTelefonico> listDirectorioTelefonico = new ArrayList<DirectorioTelefonico>();
		List<DirectorioTelefonicoExt> listDirectorioTelefonicoExt = new ArrayList<DirectorioTelefonicoExt>(); 
		listDirectorioTelefonico = this.ifzDirectorio.findByColumnName("persona", persona);
		
		for(DirectorioTelefonico directorioTelefonico : listDirectorioTelefonico) {
			DirectorioTelefonicoExt directorioExt = new DirectorioTelefonicoExt();
			directorioExt = convertDirectorioToDirectorioExt(directorioTelefonico);
			listDirectorioTelefonicoExt.add(directorioExt);
		}
		
		return listDirectorioTelefonicoExt;
	}
	
	@Override
	public Respuesta buscarDirectorioExt(PersonaExt personaExt) throws Exception  {
		Respuesta respuesta = new Respuesta();
		try{
			Persona persona = new Persona();
			persona = convertPersonaExtToPersona(personaExt);
			
			List<DirectorioTelefonico> listDirectorioTelefonico = new ArrayList<DirectorioTelefonico>();
			List<DirectorioTelefonicoExt> listDirectorioTelefonicoExt = new ArrayList<DirectorioTelefonicoExt>(); 
			listDirectorioTelefonico = this.ifzDirectorio.findByColumnName("persona", persona);
			
			for(DirectorioTelefonico directorioTelefonico : listDirectorioTelefonico) {
				DirectorioTelefonicoExt directorioTelefonicoExt =  new DirectorioTelefonicoExt();
				
				directorioTelefonicoExt = convertDirectorioToDirectorioExt(directorioTelefonico);
				
				listDirectorioTelefonicoExt.add(directorioTelefonicoExt);
			}
			respuesta.getBody().addValor("directorio", listDirectorioTelefonicoExt);
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_DIRECTORIOS_PERSONA);
			log.error("Error en el metodo buscarDirectorioExt", e);
		}		
		return respuesta;
	}

	@Override
	public List<PersonaNombresAlterno> buscarNombreAlterno(Persona persona) throws Exception  {
		return this.ifzNombresAlternos.findByColumnName("persona", persona);
	}
	
	@Override
	public Respuesta buscarNombreAlterno(PersonaExt personaExt) throws Exception  {
		Respuesta respuesta = new Respuesta();
		try{
			Persona persona = new Persona();
			List<PersonaNombresAlterno> listPersonaNombresAlterno = new ArrayList<PersonaNombresAlterno>();
			List<PersonaNombresAlternoExt> listPersonaNombresAlternoExt = new ArrayList<PersonaNombresAlternoExt>();
			
			persona = convertPersonaExtToPersona(personaExt);
			listPersonaNombresAlterno = this.ifzNombresAlternos.findByColumnName("persona", persona);
			for(PersonaNombresAlterno personaNombresAlterno : listPersonaNombresAlterno) {
				PersonaNombresAlternoExt personaNombresAlternoExt = new PersonaNombresAlternoExt();
				personaNombresAlternoExt = convertNombreAlternoToNombreAlternoExt(personaNombresAlterno);
				listPersonaNombresAlternoExt.add(personaNombresAlternoExt);
			}
			respuesta.getBody().addValor("nombreAlterno", listPersonaNombresAlternoExt);
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_DIRECTORIOS_PERSONA);
			log.error("Error en el metodo buscarNombreAlterno", e);
		}		
		return respuesta;
	}

	@Override
	public List<PersonaEstudio> buscarEstudios(Persona persona) throws Exception  {
		return this.ifzEstudio.findByColumnName("persona", persona);
	}
	
	@Override
	public List<PersonaEstudioExt> buscarEstudiosExt(Persona persona) throws Exception  {
		List<PersonaEstudio> listPersonaEstudio = new ArrayList<PersonaEstudio>();
		List<PersonaEstudioExt> listPersonaEstudioExt = new ArrayList<PersonaEstudioExt>();
		listPersonaEstudio = this.ifzEstudio.findByColumnName("persona", persona);
		
		for(PersonaEstudio personaEstudio : listPersonaEstudio) {
			PersonaEstudioExt personaEstudioExt = new PersonaEstudioExt();
			
			try {
				BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
				BeanUtils.copyProperties(personaEstudioExt, personaEstudio);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			/*personaEstudioExt.setId(personaEstudio.getId());
			personaEstudioExt.setCreadoPor(personaEstudio.getCreadoPor());
			personaEstudioExt.setFechaCreacion(personaEstudio.getFechaCreacion());
			personaEstudioExt.setModificadoPor(personaEstudio.getModificadoPor());
			personaEstudioExt.setFechaModificacion(personaEstudio.getFechaModificacion());
			personaEstudioExt.setPersona(personaEstudio.getPersona());
			personaEstudioExt.setEstudio(personaEstudio.getEstudio());
			personaEstudioExt.setAniosEstudio(personaEstudio.getAniosEstudio());*/
			
			if (personaEstudio.getTerminoEstudio() > 0 && personaEstudio.getTerminoEstudio() == 1) {
				personaEstudioExt.setTerminoEstudio(true);
			}else{
				personaEstudioExt.setTerminoEstudio(false);
			}
			
			/*personaEstudioExt.setCarrera(personaEstudio.getCarrera());
			personaEstudioExt.setEscuela(personaEstudio.getEscuela());
			personaEstudioExt.setFechaIngreso(personaEstudio.getFechaIngreso());
			personaEstudioExt.setFechaEgreso(personaEstudio.getFechaEgreso());*/
			
			listPersonaEstudioExt.add(personaEstudioExt);
		}
		
		return listPersonaEstudioExt;
	}
	
	@Override
	public List<PersonaEstudioExt> buscarEstudiosExt(PersonaExt personaExt) throws Exception  {
		Persona persona =  new Persona();
		try {
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(persona, personaExt);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		List<PersonaEstudio> listPersonaEstudio = new ArrayList<PersonaEstudio>();
		List<PersonaEstudioExt> listPersonaEstudioExt = new ArrayList<PersonaEstudioExt>();
		listPersonaEstudio = this.ifzEstudio.findByColumnName("persona", persona);
		
		for(PersonaEstudio personaEstudio : listPersonaEstudio) {
			PersonaEstudioExt personaEstudioExt = new PersonaEstudioExt();
			
			try {
				BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
				BeanUtils.copyProperties(personaEstudioExt, personaEstudio);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			/*personaEstudioExt.setId(personaEstudio.getId());
			personaEstudioExt.setCreadoPor(personaEstudio.getCreadoPor());
			personaEstudioExt.setFechaCreacion(personaEstudio.getFechaCreacion());
			personaEstudioExt.setModificadoPor(personaEstudio.getModificadoPor());
			personaEstudioExt.setFechaModificacion(personaEstudio.getFechaModificacion());
			personaEstudioExt.setPersona(personaEstudio.getPersona());
			personaEstudioExt.setEstudio(personaEstudio.getEstudio());
			personaEstudioExt.setAniosEstudio(personaEstudio.getAniosEstudio());*/
			
			if (personaEstudio.getTerminoEstudio() > 0 && personaEstudio.getTerminoEstudio() == 1) {
				personaEstudioExt.setTerminoEstudio(true);
			}else{
				personaEstudioExt.setTerminoEstudio(false);
			}
			
			/*personaEstudioExt.setCarrera(personaEstudio.getCarrera());
			personaEstudioExt.setEscuela(personaEstudio.getEscuela());
			personaEstudioExt.setFechaIngreso(personaEstudio.getFechaIngreso());
			personaEstudioExt.setFechaEgreso(personaEstudio.getFechaEgreso());*/
			
			listPersonaEstudioExt.add(personaEstudioExt);
		}
		
		return listPersonaEstudioExt;
	}

	@Override
	public List<Identificacion> buscarIdentificaciones(Persona persona) throws Exception  {
		return this.ifzIDentificacion.findByColumnName("persona", persona);
	}
	
	@Override
	public List<Identificacion> buscarIdentificaciones(PersonaExt personaExt) throws Exception  {
		Persona persona = new Persona();
		persona = convertPersonaExtToPersona(personaExt);
		
		return this.ifzIDentificacion.findByColumnName("persona", persona);
	}
	
	@Override
	public List<IdentificacionExt> buscarIdentificacionesExt(PersonaExt personaExt) throws Exception  {
		List<Identificacion> listIdentificacion = new ArrayList<Identificacion>();
		List<IdentificacionExt> listIdentificacionExt = new ArrayList<IdentificacionExt>();
		
		Persona persona = new Persona();
		persona = convertPersonaExtToPersona(personaExt);
		
		listIdentificacion = this.ifzIDentificacion.findByColumnName("persona", persona); 
		
		for(Identificacion identificacion : listIdentificacion) {
			IdentificacionExt identificacionExt =  new IdentificacionExt();
			
			try {
				BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
				BeanUtils.copyProperties(identificacionExt, identificacion);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			if (identificacion != null) {
				if (identificacion.getIdentificacion() != null) {
					ConValores tipoIdentificacion = ifzConValores.findById(identificacion.getIdentificacion());
					identificacionExt.setIdentificacionDesc(tipoIdentificacion.getValor());
				}
			}
			listIdentificacionExt.add(identificacionExt);
		}
		
		return listIdentificacionExt;
	}

	public List<ConValores> buscarDomicilios(HashMap<String, String> params) throws Exception  {
		return ifzConValores.findByGrupoNombreByParams("SYS_DOMICILIOS", params);
	}
	
	@Override
	public List<ConValores> buscartTiposTelefono() throws Exception  {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_TELEFONOS");
		for(ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores) {
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}

	@Override
	public List<ConValores> buscarCompaniasTel() throws Exception  {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_COMPANIA_TELEFONICA");
		for(ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores) {
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}

	@Override
	public List<ConValores> buscarCarreras() throws Exception  {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_CARRERAS");
		for(ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores) {
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}

	@Override
	public Colonia buscarColonia(Long colonia) throws Exception  {
		return ifzColonia.findById(colonia);
	}

	@Override
	public Localidad buscarLocalidad(Long localidad) throws Exception  {
		return ifzLocalidad.findById(localidad);
	}

	@Override
	public List<Persona> buscarConyugePersona(String tipoBusquedaConyuge,String valorBusquedaConyuge) throws Exception  {
		return ifzPersona.findLikeColumnName(tipoBusquedaConyuge, valorBusquedaConyuge);
	}
	
	@Override
	public List<PersonaExt> buscarConyugePersonaExt(String tipoBusquedaConyuge,String valorBusquedaConyuge) throws Exception  {
		List<Persona> listPersonas =  ifzPersona.findLikeColumnName(tipoBusquedaConyuge, valorBusquedaConyuge);
		List<PersonaExt> listPersonasExt =  new ArrayList<PersonaExt>();
		for(Persona persona : listPersonas) {
			PersonaExt personaExt =  new PersonaExt();
			
			personaExt = convertPersonaToPersonaExt(persona);
			
			listPersonasExt.add(personaExt);
		}
		return listPersonasExt;
	}

	@Override
	public Respuesta buscarPersonaNegocio(PersonaExt personaExt) {
		Respuesta respuesta = new Respuesta();
		try{
			List<PersonaNegocio> listPersonasNegocio = new ArrayList<PersonaNegocio>();
			List<PersonaNegocioExt> listPersonasNegocioExt = new ArrayList<PersonaNegocioExt>();
			Persona persona = new Persona();
		
			persona = convertPersonaExtToPersona(personaExt);
			listPersonasNegocio = ifzPersonaNegocio.findByColumnName("persona", persona); 
			for(PersonaNegocio personaNegocio : listPersonasNegocio) {
				PersonaNegocioExt personaNegocioExt = new PersonaNegocioExt();
				personaNegocioExt = convertPersonaNegocioToPersonaNegocioExt(personaNegocio);
				listPersonasNegocioExt.add(personaNegocioExt);
			}
			respuesta.getBody().addValor("personaNegocio", listPersonasNegocioExt);
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_NEGOCIO_PERSONA);
			log.error("Error en el metodo buscarPersonaNegocio", e);
		}		
		return respuesta;
	}

	@Override
	public List<Negocio> buscarNegocios(String tipoBusquedaNegocio,String valorBusquedaNegocio) throws Exception  {
		return ifzNegocio.findLikeColumnName(tipoBusquedaNegocio, valorBusquedaNegocio);
	}
	
	@Override
	public List<NegocioExt> buscarNegociosExt(String tipoBusquedaNegocio,String valorBusquedaNegocio) throws Exception  {
		List<Negocio> listNegocios = ifzNegocio.findLikeColumnName(tipoBusquedaNegocio, valorBusquedaNegocio);
		List<NegocioExt> listNegociosExt = new ArrayList<NegocioExt>();
		for(Negocio negocio : listNegocios) {
			NegocioExt negocioExt = convertNegocioToNegocioExt(negocio);
			listNegociosExt.add(negocioExt);
		}
		return listNegociosExt;
	}

	@Override
	public Respuesta salvar(PersonaNegocioExt personaNegocioExt) throws Exception  {
		Respuesta respuesta = new Respuesta();
		try{
			PersonaNegocio personaNegocio = new PersonaNegocio();
			if (personaNegocioExt.getId() > 0) {
				personaNegocioExt.setModificadoPor(infoSesion.getAcceso().getId());
				personaNegocioExt.setFechaModificacion(Calendar.getInstance().getTime());
			}else{
				personaNegocioExt.setCreadoPor(infoSesion.getAcceso().getId());
				personaNegocioExt.setFechaCreacion(Calendar.getInstance().getTime());
				personaNegocioExt.setModificadoPor(infoSesion.getAcceso().getId());
				personaNegocioExt.setFechaModificacion(Calendar.getInstance().getTime());
			}
			
			personaNegocio = convertPersonaNegocioExtToPersonaNegocio(personaNegocioExt);
			if (personaNegocio.getId() > 0) {
				ifzPersonaNegocio.update(personaNegocio);
				personaNegocioExt = convertPersonaNegocioToPersonaNegocioExt(personaNegocio);
			}else{
				personaNegocioExt.setId(ifzPersonaNegocio.save(personaNegocio, null));
			}
			respuesta.getBody().addValor("personaNegocio", personaNegocioExt);
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_PERSONA_NEGOCIO);
			log.error("Error en el metodo salvarPersonaNegocio", e);
		}		
		return respuesta;
	}

	@Override
	public Negocio buscarNegocios(long id) throws Exception  {
		return this.ifzNegocio.findById(id);
	}
	
	@Override
	public NegocioExt buscarNegociosExt(long id) throws Exception  {
		Negocio negocio = this.ifzNegocio.findById(id);
		NegocioExt negocioExt = convertNegocioToNegocioExt(negocio);
		return negocioExt;
	}

	@Override
	public Respuesta eliminar(PersonaNegocioExt personaNegocioExt) throws Exception  {
		Respuesta respuesta = new Respuesta();
		try{
			PersonaNegocio personaNagocio = convertPersonaNegocioExtToPersonaNegocio(personaNegocioExt);
			ifzPersonaNegocio.delete(personaNagocio.getId());
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_NEGOCIO_PERSONA);
			log.error("Error en el metodo eliminarPersonaNegocio", e);
		}		
		return respuesta;
	}

	@Override
	public List<DomicilioExt> buscarDomicilioExt(Negocio negocio) throws Exception  { 
		List<DomicilioExt> listDomiciliosExt = new ArrayList<DomicilioExt>();
		List<Domicilio> listDomicilios = new ArrayList<Domicilio>();
		DomicilioExt domExt = null;
		
		listDomicilios = this.ifzDomicilios.findByColumnName("negocio", negocio);
		if (listDomicilios != null && ! listDomicilios.isEmpty()) {
			for (Domicilio dom : listDomicilios) {
				domExt = new DomicilioExt();
				domExt = convertDomicilioToDomicilioExt(dom);
				listDomiciliosExt.add(domExt);
			}
		}
		
		return listDomiciliosExt;
	}

	@Override
	public List<DirectorioTelefonicoExt> buscarDirectorioExt(Negocio negocio) throws Exception  {
		List<DirectorioTelefonico> listDirectorioTelefonico = new ArrayList<DirectorioTelefonico>();
		List<DirectorioTelefonicoExt> listDirectorioTelefonicoExt = new ArrayList<DirectorioTelefonicoExt>(); 
		listDirectorioTelefonico = this.ifzDirectorio.findByColumnName("negocio", negocio);
		
		for(DirectorioTelefonico directorioTelefonico : listDirectorioTelefonico) {
			DirectorioTelefonicoExt directorioTelefonicoExt =  new DirectorioTelefonicoExt();
			directorioTelefonicoExt = convertDirectorioToDirectorioExt(directorioTelefonico);
			
			/*try {
				BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
				BeanUtils.copyProperties(directorioTelefonicoExt, directorioTelefonico);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			
			ConValores tipoTelefono =  new ConValores();
			tipoTelefono = ifzConValores.findById(directorioTelefonico.getTipoTelefono());
			directorioTelefonicoExt.setTipoTelefonoDesc(tipoTelefono.getValor());
			
			ConValores companiaTelefonica =  new ConValores();
			companiaTelefonica = ifzConValores.findById(directorioTelefonico.getCompaniaTelefonica());
			directorioTelefonicoExt.setCompaniaTelefonoDesc(companiaTelefonica.getValor());
			
			if (directorioTelefonico.getPrincipal() > 0 && directorioTelefonico.getPrincipal() == 1) {
				directorioTelefonicoExt.setPrincipal(true);
			}else{
				directorioTelefonicoExt.setPrincipal(false);
			}*/
			
			listDirectorioTelefonicoExt.add(directorioTelefonicoExt);
		}
		
		return listDirectorioTelefonicoExt;
	}

	@Override
	public List<PersonaNegocio> buscarPersonaNegocio(Negocio negocio) throws Exception  {
		return ifzPersonaNegocio.findByColumnName("negocio", negocio);
	}
	
	@Override
	public List<Negocio> buscarNegocios(String tipoBusquedaNegocio,String valorBusquedaNegocio, PersonaExt personaExt)throws Exception  {
		List<Negocio> listNegocios = new ArrayList<Negocio>();
		List<PersonaNegocio> listPersonaNegocio = new ArrayList<PersonaNegocio>();
		listPersonaNegocio = ifzPersonaNegocio.findLikePropiedad(tipoBusquedaNegocio, valorBusquedaNegocio, personaExt.getId());
		for(PersonaNegocio personaNegocio : listPersonaNegocio) {
			if (personaNegocio.getNegocio() != null) {
				listNegocios.add(personaNegocio.getNegocio());
			}
		}
		return listNegocios;
	}

	@Override
	public List<NegocioExt> buscarNegociosExt(String tipoBusquedaNegocio,String valorBusquedaNegocio, PersonaExt personaExt)throws Exception  {
		List<NegocioExt> listNegocios = new ArrayList<NegocioExt>();
		List<PersonaNegocio> listPersonaNegocio = new ArrayList<PersonaNegocio>();
		listPersonaNegocio = ifzPersonaNegocio.findLikePropiedad(tipoBusquedaNegocio, valorBusquedaNegocio, personaExt.getId());
		for(PersonaNegocio personaNegocio : listPersonaNegocio) {
			if (personaNegocio.getNegocio() != null) {
				listNegocios.add(convertNegocioToNegocioExt(personaNegocio.getNegocio()));
			}
		}
		return listNegocios;
	}

	public PersonaExt asignarPersona(NegocioExt negocioExt) throws Exception  {
		Negocio negocio = convertNegocioExtToNegocio(negocioExt);
		List<PersonaNegocio> listPersonaNegocios =  new ArrayList<PersonaNegocio>();
		listPersonaNegocios =ifzPersonaNegocio.findByColumnName("negocio", negocio);
		PersonaExt personaExt =  new PersonaExt();
		for(PersonaNegocio personaNegocio : listPersonaNegocios) {
			if (personaNegocio.getPersona() != null) {
				if (personaNegocio.getRepresentante() != null) {
					if (personaNegocio.getRepresentante() > 0) {
						personaExt = convertPersonaToPersonaExt(personaNegocio.getPersona());
						return personaExt;
					}
				}
			}			
		}
		personaExt = convertPersonaToPersonaExt(listPersonaNegocios.get(0).getPersona());
		return personaExt;
	}

	@Override
	public List<PersonaExt> buscarPersonaPorNegocio(String tipoBusquedaPersona, String valorBusquedaPersona, NegocioExt negocio) throws Exception  {
		List<PersonaExt> listPersonasExt = new ArrayList<PersonaExt>();
		List<PersonaNegocio> listPersonaNegocio = new ArrayList<PersonaNegocio>();
		listPersonaNegocio = ifzPersonaNegocio.findLikeNegocioPropiedad(tipoBusquedaPersona, valorBusquedaPersona, negocio.getId());
		for(PersonaNegocio personaNegocio : listPersonaNegocio) {
			if (personaNegocio.getNegocio() != null) {
				listPersonasExt.add(convertPersonaToPersonaExt(personaNegocio.getPersona()));
			}
		}
		return listPersonasExt;
	}
	
	@Override
	public Respuesta buscarPersonasContacto(PersonaExt personaExt) throws Exception  {
		List<ContactoPersonaExt> listContactoPersonaExt = new ArrayList<ContactoPersonaExt>();
		Respuesta respuesta = new Respuesta();
		
		try{
			Persona persona = convertPersonaExtToPersona(personaExt);
			List<ContactoPersona> listContactoPersona = ifzContactoPersona.findByColumnName("idPersona", persona);
			if (listContactoPersona != null && ! listContactoPersona.isEmpty()) {
				for (ContactoPersona contactoPersona : listContactoPersona) {
					listContactoPersonaExt.add(convertContactoPersonaToContactoPersonaExt(contactoPersona));
				}
			}
			
			respuesta.getBody().addValor("personasContacto", listContactoPersonaExt);
		} catch (Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_CONTACTOS_PERSONA);
			log.error("Error en el metodo Logica_Clientes.ClientesFac.buscarPersonasContacto", e);
		}

		return respuesta;
	}

	@Override
	public Respuesta buscarNegociosContacto(PersonaExt personaExt) throws Exception  {
		List<ContactoNegocioExt> listContactoNegocioExt = new ArrayList<ContactoNegocioExt>();
		Respuesta respuesta = new Respuesta();
		
		try {
			Negocio pojoNegocio = this.ifzNegocio.findById(personaExt.getId());
			if (pojoNegocio != null) {
				List<ContactoNegocio> listContactos = ifzContactoNegocio.findByColumnName("idNegocio", pojoNegocio);
				if (listContactos != null && ! listContactos.isEmpty()) {
					for(ContactoNegocio pojoContacto : listContactos) {
						listContactoNegocioExt.add(this.convertContactoNegocioToContactoNegocioExt(pojoContacto));
					}
				}

				respuesta.getBody().addValor("negociosContacto", listContactoNegocioExt);
			}
		} catch (Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_CONTACTOS_PERSONA);
			log.error("Error en el metodo Logica_Clientes.ClientesFac.buscarNegociosContacto", e);
		}

		return respuesta;
	}

	@Override
	public Respuesta salvar(ContactoPersonaExt contactoPersonaExt) throws Exception  {
		Respuesta respuesta = new Respuesta();
		try{
			ContactoPersona contactoPersona = new ContactoPersona();
			
			if (contactoPersonaExt.getId() > 0) {
				contactoPersonaExt.setModificadoPor(infoSesion.getAcceso().getId());
				contactoPersonaExt.setFechaModificacion(Calendar.getInstance().getTime());
			}else{
				contactoPersonaExt.setCreadoPor(infoSesion.getAcceso().getId());
				contactoPersonaExt.setFechaCreacion(Calendar.getInstance().getTime());
				contactoPersonaExt.setModificadoPor(infoSesion.getAcceso().getId());
				contactoPersonaExt.setFechaModificacion(Calendar.getInstance().getTime());
				contactoPersonaExt.setEstatusId(1L);
			}
			
			contactoPersona = convertContactoPersonaExtToContactoPersona(contactoPersonaExt);
			if (contactoPersonaExt.getId() > 0) {
				ifzContactoPersona.update(contactoPersona);
				contactoPersonaExt = convertContactoPersonaToContactoPersonaExt(contactoPersona);
			}else{
				contactoPersonaExt.setId(ifzContactoPersona.save(contactoPersona, null));
			}
			respuesta.getBody().addValor("contactoPersonaExt", contactoPersonaExt);
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_CONTACTO_PERSONA);
			log.error("Error en el metodo salvarContactoPersona", e);
		}		
		return respuesta;
	}

	@Override
	public Respuesta eliminar(ContactoPersonaExt personaContacto) throws Exception  {
		Respuesta respuesta = new Respuesta();
		try{
			ifzContactoPersona.delete(personaContacto.getId());
		}catch(Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_CONTACTO_PERSONA);
			log.error("Error en el metodo eliminarContactoPersona", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta listarBancos() {
		Respuesta respuesta = new Respuesta();
		try{
			List<Banco> listBancos = ifzBancos.findAll();
			
			List<CatBancosExt> listBancosExt = new ArrayList<CatBancosExt>();
			for(Banco banco : listBancos) {
				CatBancosExt bancoExt = new CatBancosExt();
				
				BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
				BeanUtils.copyProperties(bancoExt, banco);
				
				listBancosExt.add(bancoExt);
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listBancos", listBancosExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_BANCOS);
			respuesta.setBody(null);
			log.error("Error en RecuperacionCarteraFac.listarBancos", e);
		}
		return respuesta;
	}

	@Override
	public DomicilioExt buscarDomicilioExt(Long id) throws Exception  {
		DomicilioExt result = new DomicilioExt();
		
		try {
			Domicilio pojoDomicilio = this.buscarDomicilio(id);
			result = this.convertDomicilioToDomicilioExt(pojoDomicilio);
		} catch (Exception e) {
			
		}
		
		return result;
	}

	// ----------------------------------------------------------------------------------------------------------------------------------
	// PRIVADAS
	// ----------------------------------------------------------------------------------------------------------------------------------

	private Persona convertPersonaExtToPersona(PersonaExt personaExt) {
		Persona persona = new Persona();
		persona.setId(personaExt.getId());
		persona.setCreadoPor(personaExt.getCreadoPor());
		persona.setFechaCreacion(personaExt.getFechaCreacion());
		persona.setModificadoPor(personaExt.getModificadoPor());
		persona.setFechaModificacion(personaExt.getFechaModificacion());
		if (personaExt.getHomonimo() != null) {
			if (personaExt.getHomonimo() == true) {
				persona.setHomonimo(1L);
			}else{
				persona.setHomonimo(-1L);
			}
		}
		
		String res = personaExt.getPrimerNombre() != null && personaExt.getPrimerNombre().length() > 0? personaExt.getPrimerNombre() : "";
		res += personaExt.getSegundoNombre()	!= null && personaExt.getSegundoNombre().length() > 0 ? " " + personaExt.getSegundoNombre() : "";
		res += personaExt.getNombresPropios()	!= null && personaExt.getNombresPropios().length() > 0 ? " " + personaExt.getNombresPropios() : "";
		res += personaExt.getPrimerApellido()	!= null && personaExt.getPrimerApellido().length() > 0 ? " " + personaExt.getPrimerApellido() : "";
		res += personaExt.getSegundoApellido()	!= null && personaExt.getSegundoApellido().length() > 0 ? " " + personaExt.getSegundoApellido() : "";
		
		persona.setNombre(res);
		persona.setPrimerNombre(personaExt.getPrimerNombre());
		persona.setSegundoNombre(personaExt.getSegundoNombre());
		persona.setNombresPropios(personaExt.getNombresPropios());
		persona.setPrimerApellido(personaExt.getPrimerApellido());
		persona.setSegundoApellido(personaExt.getSegundoApellido());
		persona.setRfc(personaExt.getRfc());
		persona.setSexo(personaExt.getSexo());
		persona.setFechaNacimiento(personaExt.getFechaNacimiento());
		if (personaExt.getEstadoCivil() != null) {
			persona.setEstadoCivil(personaExt.getEstadoCivil().getId());
		}
		if (personaExt.getLocalidad() != null) {
			persona.setLocalidad(personaExt.getLocalidad().getId());
		}
		if (personaExt.getNacionalidad() != null) {
			persona.setNacionalidad(personaExt.getNacionalidad().getId());
		}
		if (personaExt.getConyuge() != null)
			persona.setConyuge(convertPersonaExtToPersona(personaExt.getConyuge()));
		persona.setNumeroHijos(personaExt.getNumeroHijos());
		persona.setDomicilio(personaExt.getDomicilio());
		persona.setTelefono(personaExt.getTelefono());
		persona.setCorreo(personaExt.getCorreo());
		
		if (personaExt.getFinado() == true) {
			persona.setFinado(1L);
		}else{
			persona.setFinado(-1L);
		}
		
		if (personaExt.getTipoSangre() != null) {
			persona.setTipoSangre(personaExt.getTipoSangre().getId());
		}
		if (personaExt.getColonia() != null) {
			persona.setColonia(personaExt.getColonia().getId());
		}
		
		persona.setNumeroCuenta(personaExt.getNumeroCuenta());
		persona.setClabeInterbancaria(personaExt.getClabeInterbancaria());

		persona.setTipoPersona(personaExt.getTipoPersona());
		persona.setBanco(personaExt.getBanco());
		
		return persona;
	}
	
	private PersonaExt convertPersonaToPersonaExt(Persona persona) {
		PersonaExt personaExt =  new PersonaExt();
		
		personaExt.setId(persona.getId());
		personaExt.setCreadoPor(persona.getCreadoPor());
		personaExt.setFechaCreacion(persona.getFechaCreacion());
		personaExt.setModificadoPor(persona.getModificadoPor());
		personaExt.setFechaModificacion(persona.getFechaModificacion());
		if (persona.getHomonimo() > 0) {
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
		if (persona.getEstadoCivil() != null) {
			personaExt.setEstadoCivil(ifzConValores.findById(persona.getEstadoCivil()));
		}
		if (persona.getLocalidad() != null) {
			personaExt.setLocalidad(ifzLocalidad.findById(persona.getLocalidad()));
		}
		if (persona.getNacionalidad () != null) {
			personaExt.setNacionalidad(ifzConValores.findById(persona.getNacionalidad()));
		}
		if (persona.getConyuge() != null) {
			if (personaAuxiliar == 0L) {
				personaAuxiliar = persona.getId();
				personaExt.setConyuge(convertPersonaToPersonaExt(persona.getConyuge()));
			} else {
				if (persona.getConyuge().getId() != personaAuxiliar)
					personaExt.setConyuge(convertPersonaToPersonaExt(persona.getConyuge()));
			}
			personaAuxiliar = 0L;
		}
		personaExt.setNumeroHijos(persona.getNumeroHijos());
		personaExt.setDomicilio(persona.getDomicilio());
		personaExt.setTelefono(persona.getTelefono());
		personaExt.setCorreo(persona.getCorreo());
		
		if (persona.getFinado() > 0) {
			personaExt.setFinado(true);
		}else{
			personaExt.setFinado(false);
		}
		
		if (persona.getTipoSangre() != null) {
			personaExt.setTipoSangre(ifzConValores.findById(persona.getTipoSangre()));
		}
		if (persona.getColonia() != null) {
			personaExt.setColonia(ifzColonia.findById(persona.getColonia()));
		}
		
		personaExt.setTipoPersona(persona.getTipoPersona());
		
		String res = persona.getPrimerNombre() != null && persona.getPrimerNombre().length() > 0? persona.getPrimerNombre() : "";
		res += persona.getSegundoNombre()	!= null && persona.getSegundoNombre().length() > 0 ? " " + persona.getSegundoNombre() : "";
		res += persona.getNombresPropios()	!= null && persona.getNombresPropios().length() > 0 ? " " + persona.getNombresPropios() : "";
		res += persona.getPrimerApellido()	!= null && persona.getPrimerApellido().length() > 0 ? " " + persona.getPrimerApellido() : "";
		res += persona.getSegundoApellido()	!= null && persona.getSegundoApellido().length() > 0 ? " " + persona.getSegundoApellido() : "";
		
		personaExt.setNombreCompleto(res);
		
		personaExt.setNumeroCuenta(persona.getNumeroCuenta());
		personaExt.setClabeInterbancaria(persona.getClabeInterbancaria());
		personaExt.setBanco(persona.getBanco());

		return personaExt;
	}
	
	private Domicilio convertDomicilioExToDomicilio(DomicilioExt domicilioExt) {
		Domicilio domicilio =  new Domicilio();
		
		domicilio.setId(domicilioExt.getId());
		domicilio.setCreadoPor(domicilioExt.getCreadoPor());
		domicilio.setFechaCreacion(domicilioExt.getFechaCreacion());
		domicilio.setModificadoPor(domicilioExt.getModificadoPor());
		domicilio.setFechaModificacion(domicilioExt.getFechaModificacion());
		
		Persona persona = new Persona();
		if (domicilioExt.getPersona() != null) {
			persona = convertPersonaExtToPersona(domicilioExt.getPersona());
					
			domicilio.setPersona(persona);
		}
		
		domicilio.setColonia(domicilioExt.getColonia().getId());
		domicilio.setCatDomicilio1(domicilioExt.getCatDomicilio1());
		domicilio.setCatDomicilio2(domicilioExt.getCatDomicilio2());
		domicilio.setCatDomicilio3(domicilioExt.getCatDomicilio3());
		domicilio.setCatDomicilio4(domicilioExt.getCatDomicilio4());
		domicilio.setCatDomicilio5(domicilioExt.getCatDomicilio5());
		domicilio.setDescripcionDomicilio1(domicilioExt.getDescripcionDomicilio1());
		domicilio.setDescripcionDomicilio2(domicilioExt.getDescripcionDomicilio2());
		domicilio.setDescripcionDomicilio3(domicilioExt.getDescripcionDomicilio3());
		domicilio.setDescripcionDomicilio4(domicilioExt.getDescripcionDomicilio4());
		domicilio.setDescripcionDomicilio5(domicilioExt.getDescripcionDomicilio5());
		domicilio.setObservaciones(domicilioExt.getObservaciones());
		
		if (domicilioExt.getEstatus() == true) {
			domicilio.setEstatus(1L);
		}else{
			domicilio.setEstatus(-1L);
		}
		
		if (domicilioExt.getPrincipal() == true) {
			domicilio.setPrincipal(1L);
		}else{
			domicilio.setPrincipal(-1L);
		}	
		if (domicilioExt.getNegocio() != null) {
			domicilio.setNegocio(domicilioExt.getNegocio());
		}
		return domicilio;
	}
	
	private DomicilioExt convertDomicilioToDomicilioExt(Domicilio domicilio) {
		DomicilioExt domExt =  new DomicilioExt();
		
		domExt.setId(domicilio.getId());
		if (domicilio.getPersona() != null)
			domExt.setPersona(convertPersonaToPersonaExt(domicilio.getPersona()));
		domExt.setColonia(ifzColonia.findById(domicilio.getColonia()));
		
		domExt.setCreadoPor(domicilio.getCreadoPor());
		domExt.setFechaCreacion(domicilio.getFechaCreacion());
		domExt.setModificadoPor(domicilio.getModificadoPor());
		domExt.setFechaModificacion(domicilio.getFechaModificacion());
		
		Colonia col = new Colonia();
		col = ifzColonia.findById(domicilio.getColonia());
		if (col != null && col.getId() > 0L)
			domExt.setNombreColonia(col.getNombre());
		
		domExt.setCatDomicilio1(domicilio.getCatDomicilio1());
		domExt.setCatDomicilio2(domicilio.getCatDomicilio2());
		domExt.setCatDomicilio3(domicilio.getCatDomicilio3());
		domExt.setCatDomicilio4(domicilio.getCatDomicilio4());
		domExt.setCatDomicilio5(domicilio.getCatDomicilio5());
		
		domExt.setDescripcionDomicilio1(domicilio.getDescripcionDomicilio1());
		domExt.setDescripcionDomicilio2(domicilio.getDescripcionDomicilio2());
		domExt.setDescripcionDomicilio3(domicilio.getDescripcionDomicilio3());
		domExt.setDescripcionDomicilio4(domicilio.getDescripcionDomicilio4());
		domExt.setDescripcionDomicilio5(domicilio.getDescripcionDomicilio5());
		
		domExt.setObservaciones(domicilio.getObservaciones());
		
		String res = "";
		
		ConValores catDomicilio1 = new ConValores();
		catDomicilio1 = ifzConValores.findById(domExt.getCatDomicilio1());
		ConValores catDomicilio2 = new ConValores();
		catDomicilio2 = ifzConValores.findById(domExt.getCatDomicilio2());
		ConValores catDomicilio3 = new ConValores();
		catDomicilio3 = ifzConValores.findById(domExt.getCatDomicilio3());
		ConValores catDomicilio4 = new ConValores();
		catDomicilio4 = ifzConValores.findById(domExt.getCatDomicilio4());
		ConValores catDomicilio5 = new ConValores();
		catDomicilio5 = ifzConValores.findById(domExt.getCatDomicilio5());			
		
		if (domExt.getDescripcionDomicilio1() != null && !"".equals(domExt.getDescripcionDomicilio1().trim()) && catDomicilio1 != null)
			res += catDomicilio1.getAtributo1() + " " + domExt.getDescripcionDomicilio1() + " ";
		if (domExt.getDescripcionDomicilio2() != null && !"".equals(domExt.getDescripcionDomicilio2().trim()) && catDomicilio2 != null)
			res += catDomicilio2.getAtributo1() + " " + domExt.getDescripcionDomicilio2()  + " ";
		if (domExt.getDescripcionDomicilio3() != null && !"".equals(domExt.getDescripcionDomicilio3().trim()) && catDomicilio3 != null)
			res += catDomicilio3.getAtributo1() + " " + domExt.getDescripcionDomicilio3() + " ";
		if (domExt.getDescripcionDomicilio4() != null && !"".equals(domExt.getDescripcionDomicilio4().trim()) && catDomicilio4 != null)
			res += catDomicilio4.getAtributo1() + " " + domExt.getDescripcionDomicilio4() + " ";
		if (domExt.getDescripcionDomicilio5() != null && !"".equals(domExt.getDescripcionDomicilio5().trim()) && catDomicilio5 != null)
			res += catDomicilio5.getAtributo1() + " " + domExt.getDescripcionDomicilio5();
		
		if (domExt.getColonia() != null) {
			res += ", " + domExt.getColonia().getNombre();
			if (domExt.getColonia().getLocalidad() != null)
				res += ", " + domExt.getColonia().getLocalidad().getNombre();
			if (domExt.getColonia().getLocalidad().getMunicipio() != null)
				res += ", " + domExt.getColonia().getLocalidad().getMunicipio().getNombre();
			res += " CP " + domExt.getColonia().getCp();
		}
		
		domExt.setDomicilio(res);
		if (domicilio.getPrincipal() > 0 && domicilio.getPrincipal() == 1) 
			domExt.setPrincipal(true);
		else
			domExt.setPrincipal(false);
		
		if (domicilio.getEstatus() > 0 && domicilio.getEstatus() == 1) 
			domExt.setEstatus(true);
		else
			domExt.setEstatus(false);
		
		return domExt;
	}
	
	private DirectorioTelefonico convertDirectorioExtToDirectorio(DirectorioTelefonicoExt directorioExt) {
		DirectorioTelefonico directorio = new DirectorioTelefonico();
		
		directorio.setId(directorioExt.getId());
		directorio.setCreadoPor(directorioExt.getCreadoPor());
		directorio.setFechaCreacion(directorioExt.getFechaCreacion());
		directorio.setModificadoPor(directorioExt.getModificadoPor());
		directorio.setFechaModificacion(directorioExt.getFechaModificacion());
		directorio.setPersona(convertPersonaExtToPersona(directorioExt.getPersona()));
		directorio.setNegocio(directorioExt.getNegocio());
		directorio.setTipoTelefono(directorioExt.getTipoTelefono());
		directorio.setCompaniaTelefonica(directorioExt.getCompaniaTelefonica());
		directorio.setLada(directorioExt.getLada());
		directorio.setNumeroTelefono(directorioExt.getNumeroTelefono());
		directorio.setExtension(directorioExt.getExtension());
		directorio.setObservaciones(directorioExt.getObservaciones());
				
		if (directorioExt.getPrincipal()) {
			directorio.setPrincipal(1L);
		}else{
			directorio.setPrincipal(-1L);
		}
		return directorio;
	}
	
	private DirectorioTelefonicoExt convertDirectorioToDirectorioExt(DirectorioTelefonico directorioTelefonico) {
		DirectorioTelefonicoExt directorioTelefonicoExt =  new DirectorioTelefonicoExt();
				
		directorioTelefonicoExt.setId(directorioTelefonico.getId());
		directorioTelefonicoExt.setCreadoPor(directorioTelefonico.getCreadoPor());
		directorioTelefonicoExt.setFechaCreacion(directorioTelefonico.getFechaCreacion());
		directorioTelefonicoExt.setModificadoPor(directorioTelefonico.getModificadoPor());
		directorioTelefonicoExt.setFechaModificacion(directorioTelefonico.getFechaModificacion());
		if (directorioTelefonico.getPersona() != null)
			directorioTelefonicoExt.setPersona(convertPersonaToPersonaExt(directorioTelefonico.getPersona()));
		directorioTelefonicoExt.setNegocio(directorioTelefonico.getNegocio());
		
		directorioTelefonicoExt.setTipoTelefono(directorioTelefonico.getTipoTelefono());
		ConValores tipoTelefono =  new ConValores();
		tipoTelefono = ifzConValores.findById(directorioTelefonico.getTipoTelefono());
		directorioTelefonicoExt.setTipoTelefonoDesc(tipoTelefono.getValor());
		
		directorioTelefonicoExt.setCompaniaTelefonica(directorioTelefonico.getCompaniaTelefonica());
		ConValores companiaTelefonica =  new ConValores();
		companiaTelefonica = ifzConValores.findById(directorioTelefonico.getCompaniaTelefonica());
		directorioTelefonicoExt.setCompaniaTelefonoDesc(companiaTelefonica.getValor());
		
		directorioTelefonicoExt.setLada(directorioTelefonico.getLada());
		directorioTelefonicoExt.setNumeroTelefono(directorioTelefonico.getNumeroTelefono());
		directorioTelefonicoExt.setExtension(directorioTelefonico.getExtension());
		directorioTelefonicoExt.setObservaciones(directorioTelefonico.getObservaciones());
		
		if (directorioTelefonico.getPrincipal() > 0 && directorioTelefonico.getPrincipal() == 1) {
			directorioTelefonicoExt.setPrincipal(true);
		}else{
			directorioTelefonicoExt.setPrincipal(false);
		}
		
		return directorioTelefonicoExt;
	}
	
	private PersonaNegocio convertPersonaNegocioExtToPersonaNegocio(PersonaNegocioExt personaNegocioExt) {
		PersonaNegocio personaNegocio = new PersonaNegocio();
		personaNegocio.setId(personaNegocioExt.getId());
		personaNegocio.setCreadoPor(personaNegocioExt.getCreadoPor());
		personaNegocio.setFechaCreacion(personaNegocioExt.getFechaCreacion());
		personaNegocio.setModificadoPor(personaNegocioExt.getModificadoPor());
		personaNegocio.setFechaModificacion(personaNegocioExt.getFechaModificacion());
		if (personaNegocioExt.getPersona() != null) {
			personaNegocio.setPersona(convertPersonaExtToPersona(personaNegocioExt.getPersona()));
		}
		if (personaNegocioExt.getNegocio() != null) {
			personaNegocio.setNegocio(personaNegocioExt.getNegocio());
		}
		personaNegocio.setPersonalidadJuridica(personaNegocioExt.getPersonalidadJuridica());
		personaNegocio.setParticipacion(personaNegocioExt.getParticipacion());
		personaNegocio.setPuesto(personaNegocioExt.getPuesto());
		if (personaNegocioExt.getFirmaDocumento() != null) {
			if (personaNegocioExt.getFirmaDocumento() == true) {
				personaNegocio.setFirmaDocumento(1L);
			}else{
				personaNegocio.setFirmaDocumento(0L);
			}
		}
		if (personaNegocioExt.getApoderadoId() != null) {
			personaNegocio.setApoderadoId(convertPersonaExtToPersona(personaNegocioExt.getApoderadoId()));
		}
		if (personaNegocioExt.getRepresentante() != null) {
			if (personaNegocioExt.getRepresentante() == true) {
				personaNegocio.setRepresentante(1L);
			}else{
				personaNegocio.setRepresentante(0L);
			}
		}
		
		return personaNegocio;
	}

	private PersonaNegocioExt convertPersonaNegocioToPersonaNegocioExt(PersonaNegocio personaNegocio) {
		PersonaNegocioExt personaNegocioExt = new PersonaNegocioExt();
		personaNegocioExt.setId(personaNegocio.getId());
		personaNegocioExt.setCreadoPor(personaNegocio.getCreadoPor());
		personaNegocioExt.setFechaCreacion(personaNegocio.getFechaCreacion());
		personaNegocioExt.setModificadoPor(personaNegocio.getModificadoPor());
		personaNegocioExt.setFechaModificacion(personaNegocio.getFechaModificacion());
		if (personaNegocio.getPersona() != null) {
			personaNegocioExt.setPersona(convertPersonaToPersonaExt(personaNegocio.getPersona()));
		}
		if (personaNegocio.getNegocio() != null) {
			personaNegocioExt.setNegocio(personaNegocio.getNegocio());
		}
		personaNegocioExt.setPersonalidadJuridica(personaNegocio.getPersonalidadJuridica());
		personaNegocioExt.setParticipacion(personaNegocio.getParticipacion());
		personaNegocioExt.setPuesto(personaNegocio.getPuesto());
		if (personaNegocio.getFirmaDocumento() != null) {
			if (personaNegocio.getFirmaDocumento() > 0) {
				personaNegocioExt.setFirmaDocumento(true);
			}else{
				personaNegocioExt.setFirmaDocumento(false);
			}
		}
		if (personaNegocio.getApoderadoId() != null) {
			personaNegocioExt.setApoderadoId(convertPersonaToPersonaExt(personaNegocio.getApoderadoId()));
		}
		if (personaNegocio.getRepresentante() != null) {
			if (personaNegocio.getRepresentante() > 0) {
				personaNegocioExt.setRepresentante(true);
			}else{
				personaNegocioExt.setRepresentante(false);
			}
		}
		
		return personaNegocioExt;
	}
	
	private NegocioExt convertNegocioToNegocioExt(Negocio negocio) {
		NegocioExt negocioExt = new NegocioExt();
		
		negocioExt.setId(negocio.getId());
		negocioExt.setCreadoPor(negocio.getCreadoPor());
		negocioExt.setFechaCreacion(negocio.getFechaCreacion());
		negocioExt.setModificadoPor(negocio.getModificadoPor());
		negocioExt.setFechaModificacion(negocio.getFechaModificacion());
		negocioExt.setNombre(negocio.getNombre());
		negocioExt.setDomicilio(negocio.getDomicilio());
		negocioExt.setTelefono(negocio.getTelefono());
		if (negocio.getGiro() != null) {
			negocioExt.setGiro(ifzConValores.findById(negocio.getGiro()));
		}
		negocioExt.setInicioOperaciones(negocio.getInicioOperaciones());
		negocioExt.setFormaOperacion(negocio.getFormaOperacion());
		if (negocio.getSector() != null) {
			negocioExt.setSector(ifzConValores.findById(negocio.getSector()));
		}
		negocioExt.setReferencia(negocio.getReferencia());
		negocioExt.setNombreContacto(negocio.getNombreContacto());
		negocioExt.setBloqueado(negocio.getBloqueado());
		negocioExt.setTipoPersonalidad(negocio.getTipoPersonalidad());
		negocioExt.setRfc(negocio.getRfc());
		if (negocio.getColonia() != null) {
			negocioExt.setColonia(ifzColonia.findById(negocio.getColonia()));
		}
		negocioExt.setCorreoContacto(negocio.getCorreoContacto());
		negocioExt.setEstatus(negocio.getEstatus() > 0 ? true : false);
		negocioExt.setPaginaWeb(negocio.getPaginaWeb());
		negocioExt.setNumeroEmpleados(negocio.getNumeroEmpleados());
		negocioExt.setNumeroCuenta(negocio.getNumeroCuenta());
		negocioExt.setClabeInterbancaria(negocio.getClabeInterbancaria());
		negocioExt.setBanco(negocio.getBanco());
		
		return negocioExt;
	}
	
	private Negocio convertNegocioExtToNegocio(NegocioExt negocioExt) {
		Negocio negocio = new Negocio();
		
		negocio.setId(negocioExt.getId());
		negocio.setCreadoPor(negocioExt.getCreadoPor());
		negocio.setFechaCreacion(negocioExt.getFechaCreacion());
		negocio.setModificadoPor(negocioExt.getModificadoPor());
		negocio.setFechaModificacion(negocioExt.getFechaModificacion());
		negocio.setNombre(negocioExt.getNombre());
		negocio.setDomicilio(negocioExt.getDomicilio());
		negocio.setTelefono(negocioExt.getTelefono());
		if (negocioExt.getGiro() != null) {
			negocio.setGiro(negocioExt.getGiro().getId());
		}
		negocio.setInicioOperaciones(negocioExt.getInicioOperaciones());
		negocio.setFormaOperacion(negocioExt.getFormaOperacion());
		if (negocio.getSector() != null) {
			negocio.setSector(negocioExt.getSector().getId());
		}
		negocio.setReferencia(negocioExt.getReferencia());
		negocio.setNombreContacto(negocioExt.getNombreContacto());
		negocio.setBloqueado(negocioExt.getBloqueado());
		negocio.setTipoPersonalidad(negocioExt.getTipoPersonalidad());
		negocio.setRfc(negocioExt.getRfc());
		if (negocio.getColonia() != null) {
			negocio.setColonia(negocioExt.getColonia().getId());
		}
		
		negocio.setCorreoContacto(negocioExt.getCorreoContacto());
		negocio.setEstatus(negocioExt.getEstatus() ? 1L : 0L);
		negocio.setPaginaWeb(negocioExt.getPaginaWeb());
		negocio.setNumeroEmpleados(negocioExt.getNumeroEmpleados());
		negocio.setNumeroCuenta(negocioExt.getNumeroCuenta());
		negocio.setClabeInterbancaria(negocioExt.getClabeInterbancaria());
		negocio.setBanco(negocioExt.getBanco());
		return negocio;
	}
	
	private PersonaNombresAlternoExt convertNombreAlternoToNombreAlternoExt(PersonaNombresAlterno nombreAlterno) {
		PersonaNombresAlternoExt personaNombresAlternoExt = new PersonaNombresAlternoExt();
		personaNombresAlternoExt.setId(nombreAlterno.getId());
		personaNombresAlternoExt.setCreadoPor(nombreAlterno.getCreadoPor());
		personaNombresAlternoExt.setFechaCreacion(nombreAlterno.getFechaCreacion());
		personaNombresAlternoExt.setModificadoPor(nombreAlterno.getModificadoPor());
		personaNombresAlternoExt.setFechaModificacion(nombreAlterno.getFechaModificacion());
		if (nombreAlterno.getPersona() != null) {
			personaNombresAlternoExt.setPersona(convertPersonaToPersonaExt(nombreAlterno.getPersona()));
		}
		personaNombresAlternoExt.setNombre(nombreAlterno.getNombre());
		personaNombresAlternoExt.setPrimerApellido(nombreAlterno.getPrimerApellido());
		personaNombresAlternoExt.setSegundoApellido(nombreAlterno.getSegundoApellido());
		return personaNombresAlternoExt;
	}

	private PersonaNombresAlterno convertNombreAlternoExtToNombreAlterno(PersonaNombresAlternoExt nombreAlternoExt) {
		PersonaNombresAlterno nombresAlterno = new PersonaNombresAlterno();
		nombresAlterno.setId(nombreAlternoExt.getId());
		nombresAlterno.setCreadoPor(nombreAlternoExt.getCreadoPor());
		nombresAlterno.setFechaCreacion(nombreAlternoExt.getFechaCreacion());
		nombresAlterno.setModificadoPor(nombreAlternoExt.getModificadoPor());
		nombresAlterno.setFechaModificacion(nombreAlternoExt.getFechaModificacion());
		if (nombreAlternoExt.getPersona() != null) {
			nombresAlterno.setPersona(convertPersonaExtToPersona(nombreAlternoExt.getPersona()));
		}
		nombresAlterno.setNombre(nombreAlternoExt.getNombre());
		nombresAlterno.setPrimerApellido(nombreAlternoExt.getPrimerApellido());
		nombresAlterno.setSegundoApellido(nombreAlternoExt.getSegundoApellido());
		return nombresAlterno;
	}
	
	private ContactoPersonaExt convertContactoPersonaToContactoPersonaExt(ContactoPersona contactoPersona) {
		ContactoPersonaExt contactoPersonaExt = new ContactoPersonaExt();
		contactoPersonaExt.setId(contactoPersona.getId());
		contactoPersonaExt.setEstatusId(contactoPersona.getEstatusId());
		contactoPersonaExt.setCreadoPor(contactoPersona.getCreadoPor());
		contactoPersonaExt.setFechaCreacion(contactoPersona.getFechaCreacion());
		contactoPersonaExt.setModificadoPor(contactoPersona.getModificadoPor());
		contactoPersonaExt.setFechaModificacion(contactoPersona.getFechaModificacion());		
		contactoPersonaExt.setIdPersona(convertPersonaToPersonaExt(contactoPersona.getIdPersona()));
		contactoPersonaExt.setIdPersonaContacto(convertPersonaToPersonaExt(contactoPersona.getIdPersonaContacto()));
		contactoPersonaExt.setPuesto(contactoPersona.getPuesto());
		return contactoPersonaExt;
	}
	
	private ContactoPersona convertContactoPersonaExtToContactoPersona(ContactoPersonaExt contactoPersonaExt) {
		ContactoPersona contactoPersona = new ContactoPersona();
		contactoPersona.setId(contactoPersonaExt.getId());
		contactoPersona.setEstatusId(contactoPersonaExt.getEstatusId());
		contactoPersona.setCreadoPor(contactoPersonaExt.getCreadoPor());
		contactoPersona.setFechaCreacion(contactoPersonaExt.getFechaCreacion());
		contactoPersona.setModificadoPor(contactoPersonaExt.getModificadoPor());
		contactoPersona.setFechaModificacion(contactoPersonaExt.getFechaModificacion());		
		contactoPersona.setIdPersona(convertPersonaExtToPersona(contactoPersonaExt.getIdPersona()));
		contactoPersona.setIdPersonaContacto(convertPersonaExtToPersona(contactoPersonaExt.getIdPersonaContacto()));
		contactoPersona.setPuesto(contactoPersonaExt.getPuesto());
		return contactoPersona;
	}

	private ContactoNegocioExt convertContactoNegocioToContactoNegocioExt(ContactoNegocio pojoContactoNegocio) {
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
		} catch (Exception e) {
			log.error("Error en NegociosFac.convertContactoNegocioToContactoNegocioExt", e);
		}
		return pojoContactoNegocioExt;
	}

	private boolean validaRFCMoral(String rfc) {
		if (rfc == null || rfc.length() == 0 || "XAXX010101000 XEXX010101000".contains(rfc.toUpperCase().trim()))
			return true;
		
		Pattern p = Pattern.compile(PATTERN_RFC_PM);
		Matcher m = p.matcher(rfc.toUpperCase().trim());
		
		return m.find();
		/*Pattern p = Pattern.compile(PATTERN_RFC_PM); // "[A-Z]{3}[0-9]{6}[A-Z0-9]{3}");
		Matcher m = p.matcher(rfc.toUpperCase().trim());
		
		if ("".equals(rfc.trim()))
			return true;
		return m.find();*/
	}
	
	private boolean validaRFCFisico(String rfc) {
		if (rfc == null || rfc.length() == 0 || "XAXX010101000 XEXX010101000".contains(rfc.toUpperCase().trim()))
			return true;
		
		Pattern p = Pattern.compile(PATTERN_RFC_PF);
		Matcher m = p.matcher(rfc.toUpperCase().trim());
		
		return m.find();
		/*Pattern p = Pattern.compile(PATTERN_RFC_PF); // "[A-Z]{4}[0-9]{6}[A-Z0-9]{3}");
		Matcher m = p.matcher(rfc.toUpperCase().trim());
		
		if ("".equals(rfc.trim()))
			return true;
		return m.find();*/
	}
	
	private boolean validaEmail (String value) {
		Pattern p = Pattern.compile(PATTERN_EMAIL);
		Matcher m = p.matcher(value);
		
		if ("".equals(value.trim()))
			return true;
		return m.matches();
	}

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}