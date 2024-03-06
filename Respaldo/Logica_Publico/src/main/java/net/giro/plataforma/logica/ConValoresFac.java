package net.giro.plataforma.logica;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.publico.respuesta.Respuesta;
import net.giro.publico.util.Errores;

@Stateless
public class ConValoresFac implements ConValoresRem {
	private static Logger log = Logger.getLogger(ConValoresFac.class);
	private InitialContext ctx = null;
	private InfoSesion infoSesion;
	private ConValoresDAO ifzConValores;
	
	public ConValoresFac() {
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    	    this.ifzConValores = (ConValoresDAO) ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");	
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ConValoresFac", e);
    		ctx = null;
    	}	
	}

	@Override
	public Respuesta delete(ConValores entity) {
		Respuesta respuesta = new Respuesta();
		try {
			ifzConValores.delete(entity.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (ExcepConstraint e) {
			respuesta.getErrores().addCodigo("PUBLICO", Errores.ERROR_ELIMINAR_VALOR);
			respuesta.setBody(null);
			log.info("error al borrar valores");
		}
		return respuesta;
	}

	@Override
	public Respuesta salvar(ConValores pojoValores) {
		Respuesta respuesta = new Respuesta();
		try {
			pojoValores.setModificadoPor(infoSesion.getAcceso().getId());
			pojoValores.setFechaModificacion(Calendar.getInstance().getTime());
			if(pojoValores.getId() > 0)
				ifzConValores.update(pojoValores);
			else{
				pojoValores.setCreadoPor(infoSesion.getAcceso().getId());
				pojoValores.setFechaCreacion(Calendar.getInstance().getTime());
				pojoValores.setId(ifzConValores.save(pojoValores));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoValores", pojoValores);
		} catch (ExcepConstraint e) {
			respuesta.getErrores().addCodigo("PUBLICO", Errores.ERROR_GUARDAR_VALOR);
			respuesta.setBody(null);
			log.error("Error en ConValoresFac.salvar", e);
		}
		return respuesta;
	}

	@Override
	public long save(ConValores pojoValores) {
		try {
			return ifzConValores.save(pojoValores);
		} catch (ExcepConstraint e) {
			log.error("Error en ConValoresFac.salvar", e);
		}
		return 0;
	}

	@Override
	public void update(ConValores pojoValores) {
		try {
			ifzConValores.update(pojoValores);
		} catch (ExcepConstraint e) {
			log.error("Error en ConValoresFac.salvar", e);
		}
	}

	@Override
	public ConValores findById(Long id) {
		return ifzConValores.findById(id);
	}

	@Override
	public List<ConValores> findByProperty(String propertyName, final Object value) {
		return ifzConValores.findByColumnName(propertyName, value);
	}

	@Override
	public ConValores findByPropertyPojoSolito(String propertyName,final Object value) {
		return ifzConValores.findByPropertyPojoSolito(propertyName, value);
	}
	
	@Override
	public ConValores findByValorGrupo(String valor, ConGrupoValores grupo) {
		return ifzConValores.findByValorGrupo(valor, grupo);
	}
	
	@Override
	public List<ConValores> findAll(ConGrupoValores grupo) {
		return this.ifzConValores.findLikeValorIdPropiedadGrupo("", grupo, 20);
	}

	@Override
	public List<ConValores> buscaValorGrupo(String campo, String valor, ConGrupoValores grupo) {
		return ifzConValores.buscaValorGrupo(campo, valor, grupo);
	}

	@Override
	public List<ConValores> findLikeValorIdPropiedadGrupo(String dato, ConGrupoValores grupo, int limit){
		return ifzConValores.findLikeValorIdPropiedadGrupo(dato, grupo, limit);
	}

	@Override
	public Respuesta findLikeClaveValorGrupo(String dato, ConGrupoValores grupo,int limit){
		Respuesta respuesta = new Respuesta();
		try{
			List<ConValores> listValores = ifzConValores.findLikeClaveValorGrupo(dato, grupo, limit);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listValores", listValores);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("PUBLICO", Errores.ERROR_BUSCAR_VALOR);
			respuesta.setBody(null);
			log.error("Error en ConValoresFac.findLikeClaveValorGrupo", e);
		}
		return respuesta;
	}

	@Override
	public List<ConValores> findLikeByProperty(String dato, String valor, int max) throws Exception{
		try {
			return this.ifzConValores.findLikeByProperty(dato, valor, max);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConValores> findLikeValorAgenteEstatus(String dato, ConGrupoValores grupo, String estatus, int max) throws Exception {
		try {
			return this.ifzConValores.findLikeClaveValorGrupo(dato, grupo, max);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConValores> findByGrupoNombre(String grupoNombre) {
		return ifzConValores.findByGrupoNombre(grupoNombre);
	}

	@Override
	public List<ConValores> findByGrupoNombreByParams(String grupoNombre, HashMap<String, String> params) {
		return ifzConValores.findByGrupoNombreByParams(grupoNombre, params);
	}

	@Override
	public List<ConValores> findByProperties(String propertyName1,final Object value1,String propertyName2,final Object value2) {
		return ifzConValores.findByProperties(propertyName1, value1, propertyName2, value2);
	}

	@Override
	public List<ConValores> findLikeByProperties(final Object valueDescripcion,final Object valueCuenta,final Object valueGrupo,int max){
		return ifzConValores.findLikeByProperties(valueDescripcion, valueCuenta, valueGrupo, max);
	}

	@Override
	public List<ConValores> findByGrupoEntreValores(String propertyName1,final Object valueInicial,String propertyName2,final Object valueFinal,final Object grupo) {
		return ifzConValores.findByGrupoEntreValores(propertyName1, valueInicial, propertyName2, valueFinal, grupo);
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public List<ConValores> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			return this.ifzConValores.findByProperties(params, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConValores> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			return this.ifzConValores.findLikeProperties(params, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConValores> findByProperty(String propertyName, Object value, ConGrupoValores grupo, int limit) throws Exception {
		try {
			return this.ifzConValores.findByProperty(propertyName, value, grupo, limit);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConValores> findLikeProperty(String propertyName, String value, ConGrupoValores grupo, int limit) throws Exception {
		try {
			return this.ifzConValores.findLikeProperty(propertyName, value, grupo, limit);
		} catch (Exception e) {
			throw e;
		}
	}
}