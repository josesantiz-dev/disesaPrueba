package net.giro.plataforma.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.credito.admon.beans.Oficial;
import net.giro.credito.admon.dao.OficialDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.OficialExt;
import net.giro.plataforma.seguridad.beans.Usuario;
import net.giro.plataforma.seguridad.dao.UsuariosDAO;
import net.giro.respuesta.Respuesta;
import net.giro.publico.util.Errores;

import org.apache.log4j.Logger;

@Stateless
public class EspecialistasFac implements EspecialistasRem{
	private static Logger log = Logger.getLogger(EspecialistasFac.class);
	private InitialContext ctx = null;
	
	private InfoSesion infoSesion;
	
	OficialDAO ifzOficiales;
	UsuariosDAO ifzUsuarios;
	
	private String modulo = "PUBLICO";
	
	public EspecialistasFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    		ifzOficiales = (OficialDAO)ctx.lookup("ejb:/Model_Credito//OficialImp!net.giro.credito.admon.dao.OficialDAO");
    		ifzUsuarios =  (UsuariosDAO)ctx.lookup("ejb:/Model_Publico//UsuariosImp!net.giro.plataforma.seguridad.dao.UsuariosDAO");
		} catch (Exception e) {
			log.error("Error en el metodo contexto , no se pudo crear", e);
    		ctx = null;
		}
	}
	
	private OficialExt convertOficialToOficialExt(Oficial pojoOficial){
		OficialExt pojoOficialExt = new OficialExt();
		
		pojoOficialExt.setId(pojoOficial.getId());
		
		pojoOficialExt.setCreadoPor(pojoOficial.getCreadoPor());
		pojoOficialExt.setFechaCreacion(pojoOficial.getFechaCreacion());
		pojoOficialExt.setModificadoPor(pojoOficial.getModificadoPor());
		pojoOficialExt.setFechaModificacion(pojoOficial.getFechaModificacion());
		
		pojoOficialExt.setUsuarioId(ifzUsuarios.findById(pojoOficial.getUsuarioId()));
		
		return pojoOficialExt;
	}
	
	private Oficial convertOficialExtToOficial(OficialExt pojoOficialExt){
		Oficial pojoOficial = new Oficial();
		
		pojoOficial.setId(pojoOficialExt.getId());
		
		pojoOficial.setCreadoPor(pojoOficialExt.getCreadoPor());
		pojoOficial.setFechaCreacion(pojoOficialExt.getFechaCreacion());
		pojoOficial.setModificadoPor(pojoOficialExt.getModificadoPor());
		pojoOficial.setFechaModificacion(pojoOficialExt.getFechaModificacion());
		
		pojoOficial.setUsuarioId(pojoOficialExt.getUsuarioId().getId());
		
		return pojoOficial;
	}
	
	@Override
	public Respuesta cargarOficiales(){
		Respuesta respuesta = new Respuesta();
		try{
			List<Oficial> listOficiales = ifzOficiales.findAll();
			
			List<OficialExt> listOficialesExt = new ArrayList<OficialExt>();
			for(Oficial pojoOficial : listOficiales){
				listOficialesExt.add(convertOficialToOficialExt(pojoOficial));
			}	
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listOficiales", listOficialesExt);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_CARGAR_OFICIALES);
			respuesta.setBody(null);
			log.error("Error en EspecialistasFac.cargarOficiales", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta cargarUsuarios(){
		Respuesta respuesta = new Respuesta();
		try{
			List<Usuario> listUsuarios = ifzUsuarios.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listUsuarios", listUsuarios);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_USUARIO);
			respuesta.setBody(null);
			log.error("Error en EspecialistasFac.cargarUsuarios", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta guardarOficial(OficialExt pojoOficialExt){
		Respuesta respuesta = new Respuesta();
		try{
			Oficial pojoOficial = convertOficialExtToOficial(pojoOficialExt);
			
			pojoOficial.setModificadoPor(infoSesion.getAcceso().getId());
			pojoOficial.setFechaModificacion(Calendar.getInstance().getTime());
			
			if(pojoOficial.getId() == 0L){
				pojoOficial.setCreadoPor(infoSesion.getAcceso().getId());
				pojoOficial.setFechaCreacion(Calendar.getInstance().getTime());
				
				pojoOficial.setId(ifzOficiales.save(pojoOficial, null));
			} else 
				ifzOficiales.update(pojoOficial);
			
			pojoOficialExt = convertOficialToOficialExt(pojoOficial);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoOficial", pojoOficialExt);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_OFICIAL);
			respuesta.setBody(null);
			log.error("Error en EspecialistasFac.guardarOficial", e);
		}
		return respuesta;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
}
