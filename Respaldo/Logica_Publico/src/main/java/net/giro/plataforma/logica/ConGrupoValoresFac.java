package net.giro.plataforma.logica;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.dao.ConGrupoValoresDAO;
import net.giro.publico.respuesta.Respuesta;
import net.giro.publico.util.Errores;

@Stateless
public class ConGrupoValoresFac implements ConGrupoValoresRem {
	private static Logger log = Logger.getLogger(ConGrupoValoresFac.class);
	private InitialContext ctx = null;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private ConGrupoValoresDAO ifzGroConValores;
	
	
	public ConGrupoValoresFac() {
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    	    ifzGroConValores=(ConGrupoValoresDAO)ctx.lookup("ejb:/Model_Publico//ConGrupoValoresImp!net.giro.plataforma.dao.ConGrupoValoresDAO");
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}	
	}

	@Override
	public Long save(ConGrupoValores entity) {
		try {
			return ifzGroConValores.save(entity);
		} catch (ExcepConstraint e) {
			log.info("Error al guardar grupo valores");
			return null;
		}
	}

	@Override
	public void delete(ConGrupoValores entity) throws ExcepConstraint, RuntimeException, IOException {
		ifzGroConValores.delete(entity.getId());
	}

	@Override
	public ConGrupoValores update(ConGrupoValores entity) {
		try {
			ifzGroConValores.update(entity);
		} catch (ExcepConstraint e) {
			log.info("error en update Grupo Valores");
		}
		return entity;
	}

	@Override
	public ConGrupoValores findById(Long id) {
		return ifzGroConValores.findById(id);
	}

	@Override
	public Respuesta findByProperty(String propertyName, final Object value) {
		Respuesta respuesta = new Respuesta();
		try{
			List<ConGrupoValores> listGrupoValores = ifzGroConValores.findByColumnName(propertyName, value);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listGrupoValores", listGrupoValores);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("PUBLICO", Errores.ERROR_BUSCAR_GRUPO_VALORES);
			respuesta.setBody(null);
			log.error("Error en ConGrupoValoresFac.findByProperty", e);
		}
		return respuesta;
	}

	@Override
	public List<ConGrupoValores> findLikeClaveNombre(Object value,int max) {
		return ifzGroConValores.findLikeClaveNombre(value, max);
	}

	@Override
	public List<ConGrupoValores> findAll() {
		return ifzGroConValores.findAll();
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	
	@Override
	public ConGrupoValores findByName(String nombre) {
		ConGrupoValores resultado = null;
		
		try{
			List<ConGrupoValores> listGrupoValores = ifzGroConValores.findByColumnName("nombre", nombre);
			if (listGrupoValores != null && ! listGrupoValores.isEmpty())
				resultado = listGrupoValores.get(0);
		} catch (Exception e){
			log.error("Error en ConGrupoValoresFac.findByName", e);
		}
		
		return resultado;
	}
}