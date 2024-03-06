package net.giro.contabilidad.logica;

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

import net.giro.contabilidad.beans.Grupos;
import net.giro.contabilidad.dao.GruposDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class GruposFac implements GruposRem {
	private static Logger log = Logger.getLogger(GruposFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private GruposDAO ifzGrupos;
	private static String orderBy;
	
	public GruposFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzGrupos = (GruposDAO) this.ctx.lookup("ejb:/Model_Contabilidad//GruposImp!net.giro.contabilidad.dao.GruposDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.GruposFac", e);
			ctx = null;
		}
	}


	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void orderBy(String orderBy) {
		GruposFac.orderBy = orderBy;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(Grupos entity) throws Exception {
		try {
			return this.ifzGrupos.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.save(Grupos)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Grupos> saveOrUpdateList(List<Grupos> entities) throws Exception {
		try {
			return this.ifzGrupos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.saveOrUpdateList(List<Grupos> entities)", e);
			throw e;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(Grupos entity) throws Exception {
		try {
			this.ifzGrupos.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.update(Grupos)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idGrupo) throws Exception {
		try {
			this.delete(this.ifzGrupos.findById(idGrupo));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.delete(idGrupo)", e);
			throw e;
		}
	}

	@Override
	public void delete(Grupos entity) throws Exception {
		try {
			entity.setModificadoPor(1L);
			if (this.infoSesion != null && this.infoSesion.getAcceso() != null && this.infoSesion.getAcceso().getUsuario().getId() > 0L)
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setEstatus(1);
			this.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.delete(entity)", e);
			throw e;
		}
	}

	@Override
	public Grupos findById(Long id) {
		try {
			return this.ifzGrupos.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<Grupos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzGrupos.orderBy(orderBy);
			return this.ifzGrupos.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Grupos> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzGrupos.orderBy(orderBy);
			return this.ifzGrupos.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Grupos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzGrupos.orderBy(orderBy);
			return this.ifzGrupos.findInProperty(columnName, values, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Grupos> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzGrupos.orderBy(orderBy);
			return this.ifzGrupos.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Grupos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzGrupos.orderBy(orderBy);
			return this.ifzGrupos.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
}