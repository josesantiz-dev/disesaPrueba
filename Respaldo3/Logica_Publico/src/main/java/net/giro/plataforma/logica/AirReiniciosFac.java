package net.giro.plataforma.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.AirReinicios;
import net.giro.plataforma.dao.AirReiniciosDAO;

import org.apache.log4j.Logger;

@Stateless
public class AirReiniciosFac implements AirReiniciosRem {
	private static Logger log = Logger.getLogger(AirReiniciosFac.class);
	private InfoSesion infoSesion;
	private AirReiniciosDAO ifzDAO;

	public AirReiniciosFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzDAO = (AirReiniciosDAO) ctx.lookup("ejb:/Model_Publico//AirReiniciosImp!net.giro.plataforma.dao.AirReiniciosDAO");
		} catch(Exception e) {
			log.error("Ocurrio un problema al inicilizar " + this.getClass().getCanonicalName(), e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion; 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(AirReinicios entity) throws Exception {
		try {
			return this.ifzDAO.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.AirReiniciosFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<AirReinicios> saveOrUpdateList(List<AirReinicios> entities) throws Exception {
		try {
			return this.ifzDAO.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.AirReiniciosFac.saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(AirReinicios entity) throws Exception {
		try {
			this.ifzDAO.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.AirReiniciosFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(long idEntity) throws Exception {
		try {
			this.ifzDAO.delete(idEntity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.AirReiniciosFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public AirReinicios findById(long idEntity) throws Exception {
		try {
			return this.ifzDAO.findById(idEntity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.AirReiniciosFac.findById(idEntity)", e);
			throw e;
		}
	}

	@Override
	public List<AirReinicios> findAll(String orderBy) throws Exception {
		try {
			return this.ifzDAO.findAll(orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Publico.AirReiniciosFac.findAll(orderBy)", e);
			throw e;
		}
	}

	@Override
	public List<AirReinicios> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzDAO.findLikeProperty(propertyName, value, orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.AirReiniciosFac.findLikeProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<AirReinicios> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzDAO.findByProperty(propertyName, value, orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.AirReiniciosFac.findByProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public AirReinicios comprobarProgramado(String comando) throws Exception {
		List<AirReinicios> entities = null;
		
		try {
			entities = this.findByProperty("comando", comando, null, 0);
			if (entities != null && ! entities.isEmpty())
				return entities.get(0);
			return null;
		} catch (Exception e) {
			log.error("error en Logica_Publico.AirReiniciosFac.comprobarProgramado(comando)", e);
			throw e;
		}
	}
	
	// ---------------------------------------------------------------------------------------------------------------
	// PRIVADOS 
	// ---------------------------------------------------------------------------------------------------------------
	
	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
