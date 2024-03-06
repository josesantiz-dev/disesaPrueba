package net.giro.plataforma.logica;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConValoresBlacklistDAO;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.respuesta.Respuesta;
import net.giro.publico.util.Errores;

@Stateless
public class ConValoresFac implements ConValoresRem {
	private static Logger log = Logger.getLogger(ConValoresFac.class);
	private InitialContext ctx = null;
	private InfoSesion infoSesion;
	private ConValoresDAO ifzConValores;
	private ConValoresBlacklistDAO ifzConValoresBlacklist;
	
	public ConValoresFac() {
		Hashtable<String, Object> environment = null;
		
		try {
    		environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		this.ctx = new InitialContext(environment);
    	    this.ifzConValores = (ConValoresDAO) this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");	
    	    this.ifzConValoresBlacklist = (ConValoresBlacklistDAO) this.ctx.lookup("ejb:/Model_Publico//ConValoresBlacklistImp!net.giro.plataforma.dao.ConValoresBlacklistDAO");	
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ConValoresFac", e);
    		ctx = null;
    	}	
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta delete(ConValores entity) {
		Respuesta respuesta = new Respuesta();
		
		try {
			if (entity == null || entity.getId() <= 0L) {
				respuesta.getErrores().addCodigo("PUBLICO", Errores.ERROR_ELIMINAR_VALOR);
				respuesta.setBody(null);
				return respuesta;
			}
			
			// Actualizamos registro
			if (entity.getId() > 0L) {
				entity.setModificadoPor(getIdUsuario());
				entity.setFechaModificacion(Calendar.getInstance().getTime());
				this.update(entity);
			}
			
			// Añadimos a eliminados
			this.ifzConValoresBlacklist.save(entity, getIdUsuario(), getIdEmpresa(), getCodigoEmpresa());
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e) {
			log.error("Ocurrio un problema al borrar de ConValores", e);
			respuesta.getErrores().addCodigo("PUBLICO", Errores.ERROR_ELIMINAR_VALOR);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta salvar(ConValores pojoValores) {
		Respuesta respuesta = new Respuesta();
		
		try {
			pojoValores.setModificadoPor(infoSesion.getAcceso().getId());
			pojoValores.setFechaModificacion(Calendar.getInstance().getTime());
			if (pojoValores.getId() > 0) {
				this.ifzConValores.update(pojoValores);
			} else {
				pojoValores.setCreadoPor(infoSesion.getAcceso().getId());
				pojoValores.setFechaCreacion(Calendar.getInstance().getTime());
				pojoValores.setId(this.ifzConValores.save(pojoValores, null));
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
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(ConValores pojoValores) {
		try {
			return this.ifzConValores.save(pojoValores, null);
		} catch (ExcepConstraint e) {
			log.error("Error en ConValoresFac.salvar", e);
		}
		return 0;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(ConValores pojoValores) {
		try {
			this.ifzConValores.update(pojoValores);
		} catch (ExcepConstraint e) {
			log.error("Error en ConValoresFac.salvar", e);
		}
	}

	@Override
	public ConValores findById(Long id) {
		return this.ifzConValores.findById(id);
	}

	@Override
	public List<ConValores> findByProperty(String propertyName, final Object value) {
		return this.ifzConValores.findByColumnName(propertyName, value);
	}

	@Override
	public ConValores findByPropertyPojoSolito(String propertyName,final Object value) {
		return this.ifzConValores.findByPropertyPojoSolito(propertyName, value);
	}
	
	@Override
	public ConValores findByValorGrupo(String valor, ConGrupoValores grupo) {
		return this.ifzConValores.findByValorGrupo(valor, grupo);
	}
	
	@Override
	public List<ConValores> findAll(ConGrupoValores grupo) {
		return this.findAll(grupo, "", 0);
	}

	@Override
	public List<ConValores> findAll(ConGrupoValores grupo, String orderBy, int limite) {
		return this.ifzConValores.findAll(grupo, orderBy, limite);
	}
	
	@Override
	public List<ConValores> buscaValorGrupo(String campo, String valor, ConGrupoValores grupo) {
		return this.ifzConValores.buscaValorGrupo(campo, valor, grupo);
	}

	@Override
	public List<ConValores> findLikeValorIdPropiedadGrupo(String dato, ConGrupoValores grupo, int limit){
		return this.ifzConValores.findLikeValorIdPropiedadGrupo(dato, grupo, limit);
	}

	@Override
	public Respuesta findLikeClaveValorGrupo(String dato, ConGrupoValores grupo,int limit){
		Respuesta respuesta = new Respuesta();
		List<ConValores> listValores = null;
		
		try {
			listValores = this.ifzConValores.findLikeClaveValorGrupo(dato, grupo, limit);
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
		return this.ifzConValores.findByGrupoNombre(grupoNombre);
	}

	@Override
	public List<ConValores> findByGrupoNombreByParams(String grupoNombre, HashMap<String, String> params) {
		return this.ifzConValores.findByGrupoNombreByParams(grupoNombre, params);
	}

	@Override
	public List<ConValores> findByProperties(String propertyName1,final Object value1,String propertyName2,final Object value2) {
		return this.ifzConValores.findByProperties(propertyName1, value1, propertyName2, value2);
	}

	@Override
	public List<ConValores> findLikeByProperties(final Object valueDescripcion,final Object valueCuenta,final Object valueGrupo,int max){
		return this.ifzConValores.findLikeByProperties(valueDescripcion, valueCuenta, valueGrupo, max);
	}

	@Override
	public List<ConValores> findByGrupoEntreValores(String propertyName1,final Object valueInicial,String propertyName2,final Object valueFinal,final Object grupo) {
		return this.ifzConValores.findByGrupoEntreValores(propertyName1, valueInicial, propertyName2, valueFinal, grupo);
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
			return this.findLikeProperty(propertyName, value, grupo, null, limit);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConValores> findLikeProperty(String propertyName, String value, ConGrupoValores grupo, String tipoCuenta, int limit) throws Exception {
		try {
			return this.ifzConValores.findLikeProperty(propertyName, value, grupo, tipoCuenta, limit);
		} catch (Exception e) {
			throw e;
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// PRIVADAS
	// ----------------------------------------------------------------------------------------------------------------
	
	private Long getIdUsuario() {
		if (this.infoSesion != null) 
			return this.infoSesion.getAcceso().getUsuario().getId();
		return 1L;
	}

	private Long getIdEmpresa() {
		if (this.infoSesion != null) 
			return this.infoSesion.getEmpresa().getId();
		return 1L;
	}

	private Long getCodigoEmpresa() {
		if (this.infoSesion != null) 
			return this.infoSesion.getEmpresa().getCodigo();
		return 1L;
	}
}