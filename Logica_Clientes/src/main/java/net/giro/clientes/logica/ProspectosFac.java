package net.giro.clientes.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.clientes.beans.Domicilio;
import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.beans.EstatusSeguimientoExt;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.NegocioExt;
import net.giro.clientes.beans.OficialExt;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.beans.Prospecto;
import net.giro.clientes.beans.ProspectoExt;
import net.giro.clientes.beans.Seguimiento;
import net.giro.clientes.dao.DomicilioDAO;
import net.giro.clientes.dao.ProspectoDAO;
import net.giro.clientes.dao.SeguimientoDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.credito.admon.beans.Oficial;
import net.giro.credito.admon.dao.OficialDAO;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.dao.SucursalDAO;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConGrupoValoresDAO;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.seguridad.dao.UsuariosDAO;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.dao.EmpleadoDAO;
import net.giro.util.clientes.Errores;

import org.apache.log4j.Logger;

@Stateless
public class ProspectosFac implements ProspectosRem {
	private static Logger log = Logger.getLogger(ProspectosFac.class);
	private InitialContext ctx = null;
	@Resource
    private SessionContext sctx;
	private List<EstatusSeguimientoExt> listTmpEstatusSeguimiento =  new ArrayList<EstatusSeguimientoExt>();
	private EstatusSeguimientoExt pojoEstatusSeguimiento;
	private ProspectoDAO ifzProspectos;
	private ConGrupoValoresDAO ifzConGrupoValores;
	private ConValoresDAO ifzConValores;
	private SucursalDAO ifzSucursales;
	private OficialDAO ifzOficiales;
	private SeguimientoDAO ifzSeguimiento;
	private LocalidadDAO ifzLocalidad;
	private ColoniaDAO ifzColonia;
	private UsuariosDAO ifzUsuarios;
	private EmpleadoDAO ifzEmpleados;
	private DomicilioDAO ifzDomicilios;
	private String modulo = "CLIENTES";
	
	public ProspectosFac(){
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    		ifzProspectos =  (ProspectoDAO)ctx.lookup("ejb:/Model_Clientes//ProspectoImp!net.giro.clientes.dao.ProspectoDAO");
    		ifzConValores =  (ConValoresDAO)ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
    		ifzConGrupoValores =  (ConGrupoValoresDAO)ctx.lookup("ejb:/Model_Publico//ConGrupoValoresImp!net.giro.plataforma.dao.ConGrupoValoresDAO");
    		ifzSucursales =  (SucursalDAO)ctx.lookup("ejb:/Model_Publico//SucursalImp!net.giro.ne.dao.SucursalDAO");
    		ifzOficiales =  (OficialDAO)ctx.lookup("ejb:/Model_Credito//OficialImp!net.giro.credito.admon.dao.OficialDAO");
    		ifzSeguimiento =  (SeguimientoDAO)ctx.lookup("ejb:/Model_Clientes//SeguimientoImp!net.giro.clientes.dao.SeguimientoDAO");
    		ifzLocalidad = (LocalidadDAO)ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			ifzColonia = (ColoniaDAO)ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");
			ifzUsuarios = (UsuariosDAO)ctx.lookup("ejb:/Model_Publico//UsuariosImp!net.giro.plataforma.seguridad.dao.UsuariosDAO");
			ifzEmpleados = (EmpleadoDAO)ctx.lookup("ejb:/Model_RecHum//EmpleadoImp!net.giro.rh.admon.dao.EmpleadoDAO");
			ifzDomicilios = (DomicilioDAO)ctx.lookup("ejb:/Model_Clientes//DomicilioImp!net.giro.clientes.dao.DomicilioDAO");
			
			pojoEstatusSeguimiento = new EstatusSeguimientoExt(1L,"Seguimiento");
			listTmpEstatusSeguimiento.add(pojoEstatusSeguimiento);
			pojoEstatusSeguimiento = new EstatusSeguimientoExt(2L,"Expirado");
			listTmpEstatusSeguimiento.add(pojoEstatusSeguimiento);
			pojoEstatusSeguimiento = new EstatusSeguimientoExt(3L,"Rechazado");
			listTmpEstatusSeguimiento.add(pojoEstatusSeguimiento);
			pojoEstatusSeguimiento = new EstatusSeguimientoExt(4L,"Autorizado");
			listTmpEstatusSeguimiento.add(pojoEstatusSeguimiento);
			
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}	
	}
	
	
	
	private Persona convertPersonaExtToPersona(PersonaExt personaExt){
		Persona persona = new Persona();
		persona.setId(personaExt.getId());
		persona.setCreadoPor(personaExt.getCreadoPor());
		persona.setFechaCreacion(personaExt.getFechaCreacion());
		persona.setModificadoPor(personaExt.getModificadoPor());
		persona.setFechaModificacion(personaExt.getFechaModificacion());
		if(personaExt.getHomonimo() == true){
			persona.setHomonimo(1L);
		}else{
			persona.setHomonimo(-1L);
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
		if(personaExt.getEstadoCivil() != null){
			persona.setEstadoCivil(personaExt.getEstadoCivil().getId());
		}
		if(persona.getLocalidad() != null){
			persona.setLocalidad(personaExt.getLocalidad().getId());
		}
		if(persona.getNacionalidad() != null){
			persona.setNacionalidad(personaExt.getNacionalidad().getId());
		}
		
		if(personaExt.getConyuge() != null){
			persona.setConyuge(convertPersonaExtToPersona(personaExt.getConyuge()));
		}
		
		persona.setNumeroHijos(personaExt.getNumeroHijos());
		persona.setDomicilio(personaExt.getDomicilio());
		persona.setTelefono(personaExt.getTelefono());
		persona.setCorreo(personaExt.getCorreo());
		
		if(personaExt.getFinado() == true){
			persona.setFinado(1L);
		}else{
			persona.setFinado(-1L);
		}
		
		if(persona.getTipoSangre() != null){
			persona.setTipoSangre(personaExt.getTipoSangre().getId());
		}
		if(persona.getColonia() != null){
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
		if(persona.getHomonimo() > 0){
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
		if(persona.getEstadoCivil() != null){
			personaExt.setEstadoCivil(ifzConValores.findById(persona.getEstadoCivil()));
		}
		if(persona.getLocalidad() != null){
			personaExt.setLocalidad(ifzLocalidad.findById(persona.getLocalidad()));
		}
		if(persona.getNacionalidad() != null){
			personaExt.setNacionalidad(ifzConValores.findById(persona.getNacionalidad()));
		}
		
		if(persona.getConyuge() != null){
			personaExt.setConyuge(convertPersonaToPersonaExt(persona.getConyuge()));
		}
		
		personaExt.setNumeroHijos(persona.getNumeroHijos());
		personaExt.setDomicilio(persona.getDomicilio());
		personaExt.setTelefono(persona.getTelefono());
		personaExt.setCorreo(persona.getCorreo());
		
		if(persona.getFinado() > 0){
			personaExt.setFinado(true);
		}else{
			personaExt.setFinado(false);
		}
		
		if(persona.getTipoSangre() != null){
			personaExt.setTipoSangre(ifzConValores.findById(persona.getTipoSangre()));
		}
		if(persona.getColonia() != null){
			personaExt.setColonia(ifzColonia.findById(persona.getColonia()));
		}
		
		String res = persona.getPrimerNombre() != null ? " " + persona.getPrimerNombre() : "";
		res += persona.getSegundoNombre()	!= null ? " " + persona.getSegundoNombre() : "";
		res += persona.getNombresPropios()	!= null ? " " + persona.getNombresPropios() : "";
		res += persona.getPrimerApellido()	!= null ? " " + persona.getPrimerApellido() : "";
		res += persona.getSegundoApellido()	!= null ? " " + persona.getSegundoApellido() : "";
		
		personaExt.setNombreCompleto(res);
		
		return personaExt;
	}
	
	public ProspectoExt convertProspectoToProspectoExt(Prospecto prospecto) throws ExcepConstraint{
		ProspectoExt prospectoExt = new ProspectoExt();
		prospectoExt.setId(prospecto.getId());
		prospectoExt.setCreadoPor(prospecto.getCreadoPor());
		prospectoExt.setFechaCreacion(prospecto.getFechaCreacion());
		prospectoExt.setModificadoPor(prospecto.getModificadoPor());
		prospectoExt.setFechaModificacion(prospecto.getFechaModificacion());
		
		if(prospecto.getPersona() != null){
			PersonaExt personaExt = new PersonaExt();
			personaExt = convertPersonaToPersonaExt(prospecto.getPersona());		
			prospectoExt.setPersona(personaExt);
		}
		if(prospecto.getNegocio() != null){
			
			prospectoExt.setNegocio(convertNegocioToNegocioExt(prospecto.getNegocio()));
		}
		
		if(prospecto.getOficial() != null){
			Oficial oficial = new Oficial();
			oficial = ifzOficiales.findById(prospecto.getOficial());
			
			OficialExt oficialExt = new OficialExt();
			
			oficialExt.setId(oficial.getId());
			oficialExt.setCreadoPor(oficial.getCreadoPor());
			oficialExt.setFechaCreacion(oficial.getFechaCreacion());
			oficialExt.setModificadoPor(oficial.getModificadoPor());
			oficialExt.setFechaModificacion(oficial.getFechaModificacion());
			oficialExt.setUsuarioId(ifzUsuarios.findById(oficial.getUsuarioId()));
			if(oficial.getEmpleado() != null)
				oficialExt.setEmpleado(ifzEmpleados.findById(oficial.getEmpleado()));
			
			prospectoExt.setOficial(oficialExt);
		}
		if(prospecto.getSucursal() != null){
			prospectoExt.setSucursal(ifzSucursales.findById(prospecto.getSucursal()));
		}
		if(prospecto.getEstablecimiento() != null){
			prospectoExt.setEstablecimiento(ifzConValores.findById(prospecto.getEstablecimiento()));
		}
		if(prospecto.getModoContacto() != null){
			prospectoExt.setModoContacto(ifzConValores.findById(prospecto.getModoContacto()));
		}
		if(prospecto.getComoEntero() != null){
			prospectoExt.setComoEntero(ifzConValores.findById(prospecto.getComoEntero()));
		}
		if(prospecto.getRangoFacturacion() != null){
			prospectoExt.setRangoFacturacion(ifzConValores.findById(prospecto.getRangoFacturacion()));
		}
		
		prospectoExt.setFechaContacto(prospecto.getFechaContacto());
		
		if(prospecto.getEstatus() != null){
			for(EstatusSeguimientoExt estatus : listTmpEstatusSeguimiento){
				if(estatus.getId() == prospecto.getEstatus()){
					prospectoExt.setEstatus(estatus);
				}
			}
		}
		if(prospecto.getRazonRechazo() != null){
			prospectoExt.setRazonRechazo(ifzConValores.findById(prospecto.getRazonRechazo()));
		}
		if(prospecto.getEstatus() != null){
			prospectoExt.setCalificacion(ifzConValores.findById(prospecto.getEstatus()));
		}
		return prospectoExt;
	}
	
	public Prospecto convertProspectoExtToProspecto(ProspectoExt prospectoExt) throws ExcepConstraint{
		Prospecto prospecto = new Prospecto();
		
		prospecto.setId(prospectoExt.getId());
		prospecto.setCreadoPor(prospectoExt.getCreadoPor());
		prospecto.setFechaCreacion(prospectoExt.getFechaCreacion());
		prospecto.setModificadoPor(prospectoExt.getModificadoPor());
		prospecto.setFechaModificacion(prospectoExt.getFechaModificacion());
		
		if(prospectoExt.getPersona() != null){
			Persona persona = new Persona();
			persona = convertPersonaExtToPersona(prospectoExt.getPersona());
			prospecto.setPersona(persona);
		}
		if(prospectoExt.getNegocio() != null){
			prospecto.setNegocio(convertNegocioExtToNegocio(prospectoExt.getNegocio()));
		}
		if(prospectoExt.getOficial() != null){
			prospecto.setOficial(prospectoExt.getOficial().getId());
		}
		if(prospectoExt.getSucursal() != null){
			prospecto.setSucursal(prospectoExt.getSucursal().getId());
		}
		if(prospectoExt.getEstablecimiento() != null){
			prospecto.setEstablecimiento(prospectoExt.getEstablecimiento().getId());
		}
		if(prospectoExt.getModoContacto() != null){
			prospecto.setModoContacto(prospectoExt.getModoContacto().getId());
		}
		if(prospectoExt.getComoEntero() != null){
			prospecto.setComoEntero(prospectoExt.getComoEntero().getId());
		}
		if(prospectoExt.getRangoFacturacion() != null){
			prospecto.setRangoFacturacion(prospectoExt.getRangoFacturacion().getId());
		}
		prospecto.setFechaContacto(prospectoExt.getFechaContacto());
		if(prospectoExt.getEstatus() != null){
			prospecto.setEstatus(prospectoExt.getEstatus().getId());
		}
		if(prospectoExt.getRazonRechazo() != null){
			prospecto.setRazonRechazo(prospectoExt.getRazonRechazo().getId());
		}
		if(prospectoExt.getCalificacion() != null){
			prospecto.setCalificacion(prospectoExt.getCalificacion().getId());
		}
		
		return prospecto;
	}
	
	private NegocioExt convertNegocioToNegocioExt(Negocio negocio){
		NegocioExt negocioExt = new NegocioExt();
		
		negocioExt.setId(negocio.getId());
		negocioExt.setCreadoPor(negocio.getCreadoPor());
		negocioExt.setFechaCreacion(negocio.getFechaCreacion());
		negocioExt.setModificadoPor(negocio.getModificadoPor());
		negocioExt.setFechaModificacion(negocio.getFechaModificacion());
		negocioExt.setNombre(negocio.getNombre());
		negocioExt.setDomicilio(negocio.getDomicilio());
		negocioExt.setTelefono(negocio.getTelefono());
		if(negocio.getGiro() != null){
			negocioExt.setGiro(ifzConValores.findById(negocio.getGiro()));
		}
		negocioExt.setInicioOperaciones(negocio.getInicioOperaciones());
		negocioExt.setFormaOperacion(negocio.getFormaOperacion());
		if(negocio.getSector() != null){
			negocioExt.setSector(ifzConValores.findById(negocio.getSector()));
		}
		negocioExt.setReferencia(negocio.getReferencia());
		negocioExt.setNombreContacto(negocio.getNombreContacto());
		negocioExt.setBloqueado(negocio.getBloqueado());
		negocioExt.setTipoPersonalidad(negocio.getTipoPersonalidad());
		negocioExt.setRfc(negocio.getRfc());
		if(negocio.getColonia() != null){
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
	
	private Negocio convertNegocioExtToNegocio(NegocioExt negocioExt){
		Negocio negocio = new Negocio();
		
		negocio.setId(negocioExt.getId());
		negocio.setCreadoPor(negocioExt.getCreadoPor());
		negocio.setFechaCreacion(negocioExt.getFechaCreacion());
		negocio.setModificadoPor(negocioExt.getModificadoPor());
		negocio.setFechaModificacion(negocioExt.getFechaModificacion());
		negocio.setNombre(negocioExt.getNombre());
		negocio.setDomicilio(negocioExt.getDomicilio());
		negocio.setTelefono(negocioExt.getTelefono());
		if(negocioExt.getGiro() != null){
			negocio.setGiro(negocioExt.getGiro().getId());
		}
		negocio.setInicioOperaciones(negocioExt.getInicioOperaciones());
		negocio.setFormaOperacion(negocioExt.getFormaOperacion());
		if(negocio.getSector() != null){
			negocio.setSector(negocioExt.getSector().getId());
		}
		negocio.setReferencia(negocioExt.getReferencia());
		negocio.setNombreContacto(negocioExt.getNombreContacto());
		negocio.setBloqueado(negocioExt.getBloqueado());
		negocio.setTipoPersonalidad(negocioExt.getTipoPersonalidad());
		negocio.setRfc(negocioExt.getRfc());
		if(negocio.getColonia() != null){
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

	@Override
	public List<EstatusSeguimientoExt> buscarEstatusSeguimiento() throws ExcepConstraint {
		return listTmpEstatusSeguimiento;
	}


	@Override
	public List<ProspectoExt> buscarProspectosPorPersona(String tipoBusqueda,String valorBusqueda, Long estatusSeguimiento) throws ExcepConstraint {
		List<Prospecto> listProspectos = new ArrayList<Prospecto>();
		List<ProspectoExt> listProspectosExt = new ArrayList<ProspectoExt>();
		//listProspectos =  ifzProspectos.findAll();
		listProspectos = ifzProspectos.findLikePersonaPropiedad(tipoBusqueda, valorBusqueda, estatusSeguimiento);
		for(Prospecto prospecto : listProspectos){
			ProspectoExt prospectoExt = new ProspectoExt();
			prospectoExt = convertProspectoToProspectoExt(prospecto);
			listProspectosExt.add(prospectoExt);
		}
		return listProspectosExt;
	}
	
	@Override
	public List<ProspectoExt> buscarProspectosPorNegocio(String tipoBusqueda,String valorBusqueda, Long estatusSeguimiento) throws ExcepConstraint {
		List<Prospecto> listProspectos = new ArrayList<Prospecto>();
		List<ProspectoExt> listProspectosExt = new ArrayList<ProspectoExt>();
		//listProspectos =  ifzProspectos.findAll();
		listProspectos = ifzProspectos.findLikeNegocioPropiedad(tipoBusqueda, valorBusqueda, estatusSeguimiento);
		for(Prospecto prospecto : listProspectos){
			ProspectoExt prospectoExt = new ProspectoExt();
			prospectoExt = convertProspectoToProspectoExt(prospecto);
			listProspectosExt.add(prospectoExt);
		}
		return listProspectosExt;
	}


	@Override
	public List<ConValores> buscarModosContacto() throws ExcepConstraint {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_MODO_CONTACTO");
		for(ConGrupoValores cGV : listaConGrupoValores){
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores){
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}


	@Override
	public List<ConValores> buscarComoEntero() throws ExcepConstraint {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_COMO_ENTERO");
		for(ConGrupoValores cGV : listaConGrupoValores){
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores){
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}


	@Override
	public List<Sucursal> buscarSucursales() throws ExcepConstraint {
		return ifzSucursales.findAll();
	}


	@Override
	public List<OficialExt> buscarEspecialistas() throws ExcepConstraint {
		List<Oficial> listOficiales = new ArrayList<Oficial>();
		List<OficialExt> listOficialesExt = new ArrayList<OficialExt>();
		listOficiales = this.ifzOficiales.findAll();
		for(Oficial oficial : listOficiales){
			OficialExt oficialExt = new OficialExt();
			oficialExt.setId(oficial.getId());
			oficialExt.setCreadoPor(oficial.getCreadoPor());
			oficialExt.setFechaCreacion(oficial.getFechaCreacion());
			oficialExt.setModificadoPor(oficial.getModificadoPor());
			oficialExt.setFechaModificacion(oficial.getFechaModificacion());
			oficialExt.setUsuarioId(ifzUsuarios.findById(oficial.getUsuarioId()));
			//oficialExt.setEmpleado(ifzEmpleados.findById(oficial.getEmpleado()));
			
			listOficialesExt.add(oficialExt);
		}
		return listOficialesExt;
	}


	@Override
	public List<ConValores> buscarTiposEstablecimiento() throws ExcepConstraint {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_ESTABLECIMIENTO");
		for(ConGrupoValores cGV : listaConGrupoValores){
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores){
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}


	@Override
	public List<ConValores> buscarRangosFacturacion() throws ExcepConstraint {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_RANGOS_FACTURACION");
		for(ConGrupoValores cGV : listaConGrupoValores){
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores){
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}


	@Override
	public List<ConValores> buscarCalificaciones() throws ExcepConstraint {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_CALIFICACION");
		for(ConGrupoValores cGV : listaConGrupoValores){
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores){
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}


	@Override
	public Respuesta salvar(ProspectoExt prospectoExt){
		Respuesta respuesta = new Respuesta();
		try{
			if(prospectoExt.getNegocio() == null){
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FALTA_NEGOCIO);
				respuesta.setBody(null);
				return respuesta;
			}
				
			
			Prospecto prospecto = new Prospecto();
			prospecto = convertProspectoExtToProspecto(prospectoExt);
			
			if(prospecto.getId() > 0){			
				this.ifzProspectos.update(prospecto);
			}else{
				prospecto.setEstatus(1L);
				for(EstatusSeguimientoExt estatus : listTmpEstatusSeguimiento){
					if(estatus.getId() == 1L){
						prospectoExt.setEstatus(estatus);
					}
				}
				prospecto.setId(this.ifzProspectos.save(prospecto, null));
			}
			
			prospectoExt.setId(prospecto.getId());
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoProspecto", prospectoExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_PROSPECTO);
			respuesta.setBody(null);
			log.error("Error en ProspectosFac.salvar", e);
		}
		return respuesta;
	}


	@Override
	public Long salvar(Seguimiento seguimiento) throws ExcepConstraint {
		if(seguimiento.getId() > 0){
			this.ifzSeguimiento.update(seguimiento);
		}else{
			return this.ifzSeguimiento.save(seguimiento, null);
		}
		return seguimiento.getId();
	}



	@Override
	public Seguimiento buscarSeguimiento(ProspectoExt prospectoExt) throws ExcepConstraint {
		Prospecto prospecto = new Prospecto();
		List<Seguimiento> listSeguimiento = new ArrayList<Seguimiento>();
		prospecto = convertProspectoExtToProspecto(prospectoExt);
		listSeguimiento = ifzSeguimiento.findByColumnName("prospectoId", prospecto);
		return listSeguimiento.get(0);
	}



	@Override
	public DomicilioExt buscarDomicilioPrincipal(ProspectoExt prospecto) throws ExcepConstraint {
		List<Domicilio> listDomicilios = new ArrayList<Domicilio>();
		List<DomicilioExt> listDomiciliosExt = new ArrayList<DomicilioExt>();
		Persona persona =  new Persona();
		Negocio negocio = new Negocio();
		DomicilioExt domExt =  new DomicilioExt();
		if(prospecto.getPersona() != null){
			if(prospecto.getPersona().getId() > 0){
				persona = convertPersonaExtToPersona(prospecto.getPersona());
				listDomicilios = this.ifzDomicilios.findByColumnName("persona", persona);
			}else{
				if(prospecto.getNegocio() != null){
					negocio = convertNegocioExtToNegocio(prospecto.getNegocio());
					listDomicilios = this.ifzDomicilios.findByColumnName("negocio", negocio);
				}
			}
		}else{
			if(prospecto.getNegocio() != null){
				if(prospecto.getNegocio().getId() > 0){
					negocio = convertNegocioExtToNegocio(prospecto.getNegocio());
					listDomicilios = this.ifzDomicilios.findByColumnName("negocio", negocio);
				}
			}
		}
		
		for(Domicilio dom : listDomicilios){
			domExt =  new DomicilioExt();
			
			domExt.setId(dom.getId());
			
			domExt.setPersona(prospecto.getPersona());
			domExt.setNegocio(negocio);
			if(dom.getColonia() != null){
				domExt.setColonia(ifzColonia.findById(dom.getColonia()));
			}
			domExt.setCreadoPor(dom.getCreadoPor());
			domExt.setFechaCreacion(dom.getFechaCreacion());
			domExt.setModificadoPor(dom.getModificadoPor());
			domExt.setFechaModificacion(dom.getFechaModificacion());
			
			Colonia col = new Colonia();
			if(dom.getColonia() != null){
				col = ifzColonia.findById(dom.getColonia());
			}
			domExt.setNombreColonia(col.getNombre());
			
			domExt.setCatDomicilio1(dom.getCatDomicilio1());
			domExt.setCatDomicilio2(dom.getCatDomicilio2());
			domExt.setCatDomicilio3(dom.getCatDomicilio3());
			domExt.setCatDomicilio4(dom.getCatDomicilio4());
			domExt.setCatDomicilio5(dom.getCatDomicilio5());
			
			domExt.setDescripcionDomicilio1(dom.getDescripcionDomicilio1());
			domExt.setDescripcionDomicilio2(dom.getDescripcionDomicilio2());
			domExt.setDescripcionDomicilio3(dom.getDescripcionDomicilio3());
			domExt.setDescripcionDomicilio4(dom.getDescripcionDomicilio4());
			domExt.setDescripcionDomicilio5(dom.getDescripcionDomicilio5());
			
			domExt.setObservaciones(dom.getObservaciones());
			
			String res = "";
			
			ConValores catDomicilio1 = new ConValores();
			if(domExt.getCatDomicilio1() != null){
				catDomicilio1 = ifzConValores.findById(domExt.getCatDomicilio1());
			}
			
			ConValores catDomicilio2 = new ConValores();
			if(domExt.getCatDomicilio2() != null){
				catDomicilio2 = ifzConValores.findById(domExt.getCatDomicilio2());
			}
			
			ConValores catDomicilio3 = new ConValores();
			if(domExt.getCatDomicilio3() != null){
				catDomicilio3 = ifzConValores.findById(domExt.getCatDomicilio3());
			}
			
			ConValores catDomicilio4 = new ConValores();
			if(domExt.getCatDomicilio4() != null){
				catDomicilio4 = ifzConValores.findById(domExt.getCatDomicilio4());
			}
			
			ConValores catDomicilio5 = new ConValores();
			if(domExt.getCatDomicilio5() != null){
				catDomicilio5 = ifzConValores.findById(domExt.getCatDomicilio5());
			}
			
			if(domExt.getDescripcionDomicilio1() != null && !"".equals(domExt.getDescripcionDomicilio1().trim()) && catDomicilio1 != null)
				res += catDomicilio1.getAtributo1() + " " + domExt.getDescripcionDomicilio1() + " ";
			if(domExt.getDescripcionDomicilio2() != null && !"".equals(domExt) && catDomicilio2 != null)
				res += catDomicilio2.getAtributo1() + " " + domExt.getDescripcionDomicilio2()  + " ";
			if(domExt.getDescripcionDomicilio3() != null && !"".equals(domExt.getDescripcionDomicilio3().trim()) && catDomicilio3 != null)
				res += catDomicilio3.getAtributo1() + " " + domExt.getDescripcionDomicilio3() + " ";
			if(domExt.getDescripcionDomicilio4() != null && !"".equals(domExt.getDescripcionDomicilio4().trim()) && catDomicilio4 != null)
				res += catDomicilio4.getAtributo1() + " " + domExt.getDescripcionDomicilio4() + " ";
			if(domExt.getDescripcionDomicilio5() != null && !"".equals(domExt.getDescripcionDomicilio5().trim()) && catDomicilio5 != null)
				res += catDomicilio5.getAtributo1() + " " + domExt.getDescripcionDomicilio5();
			
			domExt.setDomicilio(res);
			
			if(dom.getPrincipal() > 0 && dom.getPrincipal() == 1){
				domExt.setPrincipal(true);
			}
			
			if(dom.getEstatus() > 0 && dom.getEstatus() == 1){
				domExt.setEstatus(true);
			}
			
			listDomiciliosExt.add(domExt);
			if(domExt.getPrincipal() != null){
				if(domExt.getPrincipal() == true){
					return domExt;
				}
			}
		}
		if(listDomiciliosExt.isEmpty()){
			return new DomicilioExt();
		}else{
			return listDomiciliosExt.get(0);
		}
	}
	
}
