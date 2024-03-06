package net.giro.clientes.logica;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;


import net.giro.clientes.beans.EstatusSeguimientoExt;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.NegocioExt;
import net.giro.clientes.beans.OficialExt;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.beans.Prospecto;
import net.giro.clientes.beans.ProspectoExt;
import net.giro.clientes.beans.Seguimiento;
import net.giro.clientes.beans.SeguimientoExt;
import net.giro.clientes.dao.ProspectoDAO;
import net.giro.clientes.dao.SeguimientoDAO;
import net.giro.clientes.impresion.Ftp;
import net.giro.clientes.impresion.RespuestaCrearJob;
import net.giro.plataforma.InfoSesion;
import net.giro.credito.admon.beans.Oficial;
import net.giro.credito.admon.dao.OficialDAO;

import net.giro.ne.dao.SucursalDAO;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConGrupoValoresDAO;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.seguridad.dao.UsuariosDAO;
import net.giro.plataforma.seguridad.logica.AutentificacionRem;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.respuesta.Respuesta;
import net.giro.util.clientes.Errores;
import net.giro.rh.admon.dao.EmpleadoDAO;

import org.apache.log4j.Logger;

import com.google.gson.Gson;


@Stateless
public class SeguimientoFac implements SeguimientoRem{

	
	private static Logger log = Logger.getLogger(SeguimientoFac.class);
	private InitialContext ctx = null;
	private InfoSesion infoSesion;
	@Resource
    private SessionContext sctx;
	
	private ProspectoDAO ifzProspectos;
	private SeguimientoDAO ifzSeguimiento;
	//private ProspectosRem ifzProspectoRem;
	private List<EstatusSeguimientoExt> listTmpEstatusSeguimiento =  new ArrayList<EstatusSeguimientoExt>();
	private ConValoresDAO ifzConValores;
	private SucursalDAO ifzSucursales;
	private OficialDAO ifzOficiales;
	private EstatusSeguimientoExt pojoEstatusSeguimiento;
	private ConGrupoValoresDAO ifzConGrupoValores;
	private LocalidadDAO ifzLocalidad;
	private ColoniaDAO ifzColonia;
	private UsuariosDAO ifzUsuarios;
	@SuppressWarnings("unused")
	private EmpleadoDAO ifzEmpleados;
	private AutentificacionRem autentificacion;
	
	private Ftp ftp;
	
	public SeguimientoFac(){
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    		ifzProspectos =  (ProspectoDAO)ctx.lookup("ejb:/Model_Clientes//ProspectoImp!net.giro.clientes.dao.ProspectoDAO");
    		ifzSeguimiento=(SeguimientoDAO)ctx.lookup("ejb:/Model_Clientes//SeguimientoImp!net.giro.clientes.dao.SeguimientoDAO");
    		ifzConGrupoValores =  (ConGrupoValoresDAO)ctx.lookup("ejb:/Model_Publico//ConGrupoValoresImp!net.giro.plataforma.dao.ConGrupoValoresDAO");
    		ifzConValores =  (ConValoresDAO)ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
    		ifzSucursales =  (SucursalDAO)ctx.lookup("ejb:/Model_Publico//SucursalImp!net.giro.ne.dao.SucursalDAO");
    		ifzOficiales =  (OficialDAO)ctx.lookup("ejb:/Model_Credito//OficialImp!net.giro.credito.admon.dao.OficialDAO");
    		ifzLocalidad = (LocalidadDAO)ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			ifzColonia = (ColoniaDAO)ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");
			ifzUsuarios = (UsuariosDAO)ctx.lookup("ejb:/Model_Publico//UsuariosImp!net.giro.plataforma.seguridad.dao.UsuariosDAO");
			ifzEmpleados = (EmpleadoDAO)ctx.lookup("ejb:/Model_RecHum//EmpleadoImp!net.giro.rh.admon.dao.EmpleadoDAO");
			autentificacion = (AutentificacionRem)ctx.lookup("ejb:/Logica_Clientes//AutentificacionFac!net.giro.clientes.plataforma.seguridad.logica.AutentificacionRem");
			
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
	@Override
	public Respuesta autorizarSeguimiento(Long idSeguimiento, Long idProspecto){
		Seguimiento pojoSeguimiento;
		Prospecto pojoProspecto;
		Respuesta respuesta = new Respuesta();
		
		try{
			if(autentificacion.getPerfil("AUTORIZAR_RECHAZAR_SEGUIMIENTO").equals("S")){
				pojoSeguimiento = ifzSeguimiento.findById(idSeguimiento);
				pojoSeguimiento.setEstatusId(4L);
				pojoSeguimiento.setModificadoPor(infoSesion.getAcceso().getId());
				pojoSeguimiento.setFechaModificacion(Calendar.getInstance().getTime());
				ifzSeguimiento.update(pojoSeguimiento);
				
				pojoProspecto = ifzProspectos.findById(idProspecto);
				pojoProspecto.setEstatus(4L);
				pojoProspecto.setModificadoPor(infoSesion.getAcceso().getId());
				pojoProspecto.setFechaModificacion(Calendar.getInstance().getTime());
				ifzProspectos.update(pojoProspecto);
				
				respuesta.getErrores().setCodigoError(0L);
			} else {
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_FALTA_PERMISO);
				respuesta.setBody(null);
			}
			
		}catch(Exception e){
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_AUTORIZAR_SEGUIMIENTO);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.autorizarSeguimiento", e);
		}
		
		return respuesta;
	}
	

	@Override
	public Respuesta buscarEstatusSeguimiento(){
		Respuesta respuesta = new Respuesta();
		List<EstatusSeguimientoExt> listTmpEstatusSeguimiento;
		EstatusSeguimientoExt pojoEstatusSeguimiento;
		
		try {
			listTmpEstatusSeguimiento =  new ArrayList<EstatusSeguimientoExt>();
			pojoEstatusSeguimiento = new EstatusSeguimientoExt(1L,"Seguimiento");
			listTmpEstatusSeguimiento.add(pojoEstatusSeguimiento);
			pojoEstatusSeguimiento = new EstatusSeguimientoExt(2L,"Expirado");
			listTmpEstatusSeguimiento.add(pojoEstatusSeguimiento);
			pojoEstatusSeguimiento = new EstatusSeguimientoExt(3L,"Rechazado");
			listTmpEstatusSeguimiento.add(pojoEstatusSeguimiento);
			pojoEstatusSeguimiento = new EstatusSeguimientoExt(4L,"Autorizado");
			listTmpEstatusSeguimiento.add(pojoEstatusSeguimiento);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listEstatusSeguimiento", listTmpEstatusSeguimiento);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_LISTAR_ESTATUS_SEGUIMIENTO);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.buscarEstatusSeguimiento", e);
		}

		return respuesta;
	}
	
	
	
	@Override
	public Respuesta buscarModosContacto() {
		Respuesta respuesta = new Respuesta();
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		
		try{
			List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_MODO_CONTACTO");
			for(ConGrupoValores cGV : listaConGrupoValores){
				listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
				for(ConValores cv : listaTmpConValores){
					listaConValores.add(cv);
				}
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listModosContacto", listaConValores);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_LISTAR_VALORES);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.buscarModosContacto", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta buscarMotivosRechazo() {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		Respuesta respuesta = new Respuesta();
		
		try{
			List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_RAZON_RECHAZO");
			for(ConGrupoValores cGV : listaConGrupoValores){
				listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
				for(ConValores cv : listaTmpConValores){
					listaConValores.add(cv);
				}
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMotivosRechazo", listaConValores);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_LISTAR_VALORES);
			respuesta.setBody(null);
			log.error("Errpr em SeguimientoFac.buscarMotivosRechazo", e);
		}
		
		return respuesta;
	}	
	@Override
	public Respuesta buscarSeguimientoPorTodos(String tipoBusqueda,String valorBusqueda, Long estatusSeguimiento) {
		Respuesta respuesta = new Respuesta();
		List<Seguimiento> listSeguimientos = new ArrayList<Seguimiento>();
		List<SeguimientoExt> listSeguimientoExt = new ArrayList<SeguimientoExt>();
		
		try{
			listSeguimientos = ifzSeguimiento.findLikePropiedadYEstatus(tipoBusqueda, valorBusqueda, estatusSeguimiento);
			
			for(Seguimiento seguimiento : listSeguimientos){
				SeguimientoExt seguimientoExt = new SeguimientoExt();
				seguimientoExt = convertSeguimientoToSeguimientoExt(seguimiento);
				if(tipoBusqueda.equals("especialista") && valorBusqueda != null) {
					if(seguimientoExt.getEspecialistaId().getUsuarioId().getNombre().toLowerCase().contains(valorBusqueda.toLowerCase()))
						listSeguimientoExt.add(seguimientoExt);
				}else
					listSeguimientoExt.add(seguimientoExt);
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listSeguimientos", listSeguimientoExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_BUSCAR_SEGUIMIENTO);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.buscarSeguimientoPorTodos", e);
		}
		
		return respuesta;
	}
	@Override
	public Respuesta buscarSeguimientoPorAsignados(String tipoBusqueda,String valorBusqueda, Long estatusSeguimiento, Long idUsuario) {
		List<Seguimiento> listSeguimientos = new ArrayList<Seguimiento>();
		List<SeguimientoExt> listSeguimientoExt = new ArrayList<SeguimientoExt>();
		Respuesta respuesta = new Respuesta();
		
		try{
			listSeguimientos = ifzSeguimiento.findLikePropiedadYEstatus(tipoBusqueda, valorBusqueda, estatusSeguimiento);
			for(Seguimiento seguimiento : listSeguimientos){
				SeguimientoExt seguimientoExt = new SeguimientoExt();
				seguimientoExt = convertSeguimientoToSeguimientoExt(seguimiento);
				if(tipoBusqueda.equals("especialista") && valorBusqueda != null) {
					if(seguimientoExt.getEspecialistaId().getUsuarioId().getNombre().toLowerCase().contains(valorBusqueda.toLowerCase()) && seguimientoExt.getEspecialistaId().getId() == idUsuario)
						listSeguimientoExt.add(seguimientoExt);
				}else if(seguimientoExt.getEspecialistaId().getId() == idUsuario)
					listSeguimientoExt.add(seguimientoExt);
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listSeguimientos", listSeguimientoExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_BUSCAR_SEGUIMIENTO);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.buscarSeguimientoProAsignados", e);
		}
		
		return respuesta;
	}
	
	
	@Override
	public Respuesta expirarSeguimiento(SeguimientoExt seguimientoExt) {
		Seguimiento seguimiento = new Seguimiento();
		Respuesta respuesta = new Respuesta();
		
		try {
			seguimientoExt.setEstatusId(2L);
			seguimiento = convertSeguimientoExtToSeguimiento(seguimientoExt);
			
			if(seguimiento.getId() > 0){			
				this.ifzSeguimiento.update(seguimiento);
			}else{
				seguimientoExt.setId(this.ifzSeguimiento.save(seguimiento));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoSeguimiento", seguimientoExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_EXPIRAR_SEGUIMIENTO);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.expirarSeguimiento", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta insertaSeguimientoProspecto(SeguimientoExt pojoSeguimientoExt) {
		Seguimiento pojoSeguimientoAnterior;
		Seguimiento pojoSeguimientoResponder;
		Respuesta respuesta = new Respuesta();
		
		try{
			pojoSeguimientoAnterior = ifzSeguimiento.findById(pojoSeguimientoExt.getId());
			pojoSeguimientoResponder = convertSeguimientoExtToSeguimiento(pojoSeguimientoExt);
			
			pojoSeguimientoResponder.setEstatusId(1L);
			pojoSeguimientoResponder.setCreadoPor(infoSesion.getAcceso().getId());
			pojoSeguimientoResponder.setFechaCreacion(Calendar.getInstance().getTime());
			pojoSeguimientoResponder.setModificadoPor(infoSesion.getAcceso().getId());
			pojoSeguimientoResponder.setFechaModificacion(Calendar.getInstance().getTime());
			pojoSeguimientoResponder.setId(0L);
			
			pojoSeguimientoExt.setId(ifzSeguimiento.save(pojoSeguimientoResponder));
			
			pojoSeguimientoAnterior.setEstatusId(2L);
			pojoSeguimientoAnterior.setModificadoPor(infoSesion.getAcceso().getId());
			pojoSeguimientoAnterior.setFechaModificacion(Calendar.getInstance().getTime());
			ifzSeguimiento.update(pojoSeguimientoAnterior);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoSeguimiento", pojoSeguimientoExt);
		}catch(Exception e){
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_INSERTAR_SEGUIMIENTO);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.insertaSeguimientoProspecto", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta rechazarSeguimiento(Long idSeguimiento, Long idProspecto, Long idMotivoRechazo){
		Seguimiento pojoSeguimiento;
		Prospecto pojoProspecto;
		Respuesta respuesta = new Respuesta();

		try{
			if(autentificacion.getPerfil("AUTORIZAR_RECHAZAR_SEGUIMIENTO").equals("S")){
			pojoSeguimiento = ifzSeguimiento.findById(idSeguimiento);
			pojoSeguimiento.setEstatusId(3L);
			pojoSeguimiento.setRazonRechazoId(idMotivoRechazo);
			ifzSeguimiento.update(pojoSeguimiento);
			
			pojoProspecto = ifzProspectos.findById(idProspecto);
			pojoProspecto.setEstatus(3L);
			pojoProspecto.setRazonRechazo(idMotivoRechazo);
			ifzProspectos.update(pojoProspecto);
			
			respuesta.getErrores().setCodigoError(0L);
			} else {
				respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_FALTA_PERMISO);
				respuesta.setBody(null);
			}
		}catch(Exception e){
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_RECHAZAR_SEGUIMIENTO);
			respuesta.setBody(null);
			log.error("Error en rechazarSeguimiento", e);
		}
		
		return respuesta;
	}
	
	private SeguimientoExt convertSeguimientoToSeguimientoExt(Seguimiento seguimiento){
		SeguimientoExt seguimientoExt = new SeguimientoExt();
		seguimientoExt.setId(seguimiento.getId());
		seguimientoExt.setCreadoPor(seguimiento.getCreadoPor());
		seguimientoExt.setFechaCreacion(seguimiento.getFechaCreacion());
		seguimientoExt.setModificadoPor(seguimiento.getModificadoPor());
		seguimientoExt.setFechaModificacion(seguimiento.getFechaModificacion());
		
		if(seguimiento.getProspectoId() != null){
			Prospecto pojoProspecto = seguimiento.getProspectoId();
			seguimientoExt.setProspectoId(convertProspectoToProspectoExt(pojoProspecto));	
		}
		
		seguimientoExt.setFechaContacto(seguimiento.getFechaContacto());
		
		if(seguimiento.getModoContactoId() != null){
			seguimientoExt.setModoContactoId(ifzConValores.findById(seguimiento.getModoContactoId()));
		}
		
		if(seguimiento.getEspecialistaId() != null){
			OficialExt oficialExt = new OficialExt();
			Oficial oficial = ifzOficiales.findById(seguimiento.getEspecialistaId());
			
			oficialExt.setId(oficial.getId());
			oficialExt.setCreadoPor(oficial.getCreadoPor());
			oficialExt.setModificadoPor(oficial.getModificadoPor());
			oficialExt.setFechaCreacion(oficial.getFechaCreacion());
			oficialExt.setFechaModificacion(oficial.getFechaModificacion());
			oficialExt.setUsuarioId(ifzUsuarios.findById(oficial.getUsuarioId()));
			//oficialExt.setEmpleado(ifzEmpleados.findById(oficial.getEmpleado()));
			
			seguimientoExt.setEspecialistaId(oficialExt);
		}
		
		seguimientoExt.setObservaciones(seguimiento.getObservaciones());
		
		if(seguimiento.getRazonRechazoId() != null){
			seguimientoExt.setRazonRechazoId(ifzConValores.findById(seguimiento.getRazonRechazoId()));
		}
		
		if(seguimiento.getEstatusId() != null){
			for(EstatusSeguimientoExt estatus : listTmpEstatusSeguimiento){
				if(estatus.getId() == seguimiento.getEstatusId()){
					seguimientoExt.setEstatus(estatus);
				}
			}
		}
		
		return seguimientoExt;
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
		if(persona.getConyuge() != null)
			personaExt.setConyuge(convertPersonaToPersonaExt(persona.getConyuge()));
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
	
	private ProspectoExt convertProspectoToProspectoExt(Prospecto prospecto){
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
			//oficialExt.setEmpleado(ifzEmpleados.findById(oficial.getEmpleado()));
			
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
	
	private Seguimiento convertSeguimientoExtToSeguimiento(SeguimientoExt seguimientoExt){
		Seguimiento seguimiento = new Seguimiento();
		
		seguimiento.setId(seguimientoExt.getId());
		seguimiento.setFechaContacto(seguimientoExt.getFechaContacto());
		seguimiento.setObservaciones(seguimientoExt.getObservaciones());
		seguimiento.setCreadoPor(seguimientoExt.getCreadoPor());
		seguimiento.setFechaCreacion(seguimientoExt.getFechaCreacion());
		seguimiento.setModificadoPor(seguimientoExt.getModificadoPor());
		seguimiento.setFechaModificacion(seguimientoExt.getFechaModificacion());
		
		if(seguimientoExt.getEstatus() != null)
			seguimiento.setEstatusId(seguimientoExt.getEstatus().getId());
		
		if(seguimientoExt.getProspectoId() != null){
			Prospecto prospecto = ifzProspectos.findById(seguimientoExt.getProspectoId().getId());
			seguimiento.setProspectoId(prospecto);
		}
		
		if(seguimientoExt.getModoContactoId() != null)
			seguimiento.setModoContactoId(seguimientoExt.getModoContactoId().getId());
		
		if(seguimientoExt.getEspecialistaId() != null)
			seguimiento.setEspecialistaId(seguimientoExt.getEspecialistaId().getId());
		
		if(seguimientoExt.getRazonRechazoId() != null)
			seguimiento.setRazonRechazoId(seguimientoExt.getRazonRechazoId().getId());

		return seguimiento;
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
	
	@Override
	public Respuesta GenerarHistorico(SeguimientoExt pojoSeguimiento, HashMap<String, String> params)
	{
		Respuesta respuesta = new Respuesta();
		
		HashMap<String, Object> map =  new HashMap<String, Object>();
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		HashMap<String, Object> programacion = new HashMap<String, Object>();
		
		String socket_ip = params.get("socket_ip");
		String socket_puerto = params.get("socket_puerto");
		String idPrograma = params.get("idPrograma");
		
		String resultado;
		RespuestaCrearJob resp;
		
		byte[] b = new byte[64000];
		
		try{
			@SuppressWarnings("resource")
			Socket socket = new Socket(socket_ip, Integer.valueOf(socket_puerto));
			
			map.put("tipo", "000100");
			map.put("idAplicacion", "1");
			map.put("idPrograma", idPrograma);
			map.put("usuario", "JPEREZ");
			map.put("role", "PLATAFORMA");
			
			parametros.put("idProspecto", String.valueOf(pojoSeguimiento.getProspectoId().getId()));
			
			map.put("programacion", programacion);
			
			map.put( "parametros", parametros);
			
			Gson gson = new Gson();
			String comando = gson.toJson(map);
			
			socket.getOutputStream().write(comando.getBytes());
			
			socket.getInputStream().read(b);
			
			resultado = new String(b);
			resultado = resultado.trim();
			System.out.println(resultado);
			resp = gson.fromJson(resultado, RespuestaCrearJob.class);
			
			byte[] contenidoReporte = obtenerReporte(params, resp.getID());
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("contenidoReporte", contenidoReporte);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_OBTENER_HISTORICO);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.GenerarHistorico", e);
		}
		return respuesta;
	}
	
	private byte[] obtenerReporte(HashMap<String, String> params, long jobId) throws Exception{
		byte[] source = null;
		try {
			String host = params.get("ftp_host");
			String port = params.get("ftp_port");
			String user = params.get("ftp_user");
			String password = params.get("ftp_password");
			
			ftp = new Ftp(user,password, host, port);
			
			source = ftp.getArchivo("SALIDA", jobId); //Double.valueOf(job.getId()).longValue()
//			/httpSession.setAttribute("contenido", source);
		} catch (Exception e) {
			log.error("Error en SeguimientoFac.obtenerReport", e);
			throw e;
		}
		return source;
	}
	
	@Override
	public InfoSesion getInfoSesion() {
		return infoSesion;
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	/*
	 *************METODOS COMENTADOS POR QUE NO SE UTILIZAN******************
	 *
	@Override
	public Respuesta buscarEspecialistas() {
		Respuesta respuesta = new Respuesta();
		List<Oficial> listOficiales = new ArrayList<Oficial>();
		List<OficialExt> listOficialesExt = new ArrayList<OficialExt>();
		try{
			listOficiales = this.ifzOficiales.findAll();
			for(Oficial oficial : listOficiales){
				OficialExt oficialExt = new OficialExt();
				try {
					BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
					BeanUtils.copyProperties(oficialExt, oficial);			
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				listOficialesExt.add(oficialExt);
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listOficiales", listOficialesExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_LISTAR_ESPECIALISTAS);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.buscarEspecialistas", e);
		}
		
		return respuesta;
	}
	 *
	 *
	@Override
	public Respuesta buscarProspectos(String tipoBusqueda,String valorBusqueda, String estatusSeguimiento) {
		Respuesta respuesta = new Respuesta();
		List<Prospecto> listProspectos;
		
		try{
			listProspectos = ifzProspectos.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listProspectos", listProspectos);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_BUSCAR_PROSPECTO);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.buscarProspectos", e);
		}
		
		return respuesta;
	}
	 *
	 *
	@Override
	public Respuesta buscarSeguimientos(String tipoBusqueda, String valorBusqueda){
		List<Seguimiento> listSeguimientos;
		Respuesta respuesta = new Respuesta();
		try{
			listSeguimientos = ifzSeguimiento.findLikeColumnName(tipoBusqueda, valorBusqueda);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listSeguimientos", listSeguimientos);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CLIENTES", Errores.ERROR_BUSCAR_SEGUIMIENTO);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.buscarSeguimientos", e);
		}
		return respuesta;
	}
	 *
	 *
	@Override
	public Respuesta detallesSeguimiento(Long id){
		Respuesta respuesta = new Respuesta();
		try{
			Seguimiento pojoSeguimiento = ifzSeguimiento.findById(id);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoSeguimiento", pojoSeguimiento);
		} catch (Exception e) {
			respuesta.getErrores().setCodigoError(0L);
			respuesta.setBody(null);
			log.error("Error en SeguimientoFac.detallesSeguimiento");
		}
		
		return respuesta; 
	}
	 *
	 *
	@Override
	public long salvar(SeguimientoExt seguimientoExt){
		Seguimiento seguimiento = new Seguimiento();
		try {
			seguimiento= convertSeguimientoExtToSeguimiento(seguimientoExt);
			
			if(seguimiento.getId() > 0){			
				this.ifzSeguimiento.update(seguimiento);
			}else{
				seguimiento.setEstatusId(1L);
				return this.ifzSeguimiento.save(seguimiento);
			}
		} catch (Exception e) {
			log.error("Error en SeguimientoFac.salvar", e);
		}
		
		return seguimiento.getId();
	}
	 */
}
