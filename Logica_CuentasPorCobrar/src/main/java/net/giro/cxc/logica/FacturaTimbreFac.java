package net.giro.cxc.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxc.beans.FacturaTimbre;
import net.giro.cxc.dao.FacturaTimbreDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class FacturaTimbreFac implements FacturaTimbreRem {
	private static Logger log = Logger.getLogger(FacturaTimbreFac.class);
	private InfoSesion infoSesion;
	private long idUsuario;
	private long idEmpresa;
	private long codigoEmpresa;
	// -------------------------------------------------------------------
	private FacturaTimbreDAO ifzFacturaTimbre;

	public FacturaTimbreFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzFacturaTimbre = (FacturaTimbreDAO) ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturaTimbreImp!net.giro.cxc.dao.FacturaTimbreDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_CuentasPorCobrar.FacturaTimbreFac", e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
		if (this.infoSesion != null) {
			this.idUsuario = this.infoSesion.getAcceso().getUsuario().getId();
			this.idEmpresa = this.infoSesion.getEmpresa().getId();
			this.codigoEmpresa = this.infoSesion.getEmpresa().getCodigo();
		}
	}

	@Override
	public void setInfoSesion(long idUsuario, long idEmpresa, long codigoEmpresa) {
		this.idUsuario = idUsuario;
		this.idEmpresa = idEmpresa;
		this.codigoEmpresa = codigoEmpresa;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(FacturaTimbre entity) throws Exception {
		try {
			return this.ifzFacturaTimbre.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public FacturaTimbre saveOrUpdate(FacturaTimbre entity) throws Exception {
		long idTimbre = 0L;
		
		try {
			if (entity == null)
				return entity;
			idTimbre = this.ifzFacturaTimbre.saveOrUpdate(entity, getCodigoEmpresa());
			if (entity.getId() == null || entity.getId() <= 0L)
				entity.setId(idTimbre);
			return entity;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<FacturaTimbre> saveOrUpdateList(List<FacturaTimbre> entities) throws Exception {
		try {
			return this.ifzFacturaTimbre.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) { 
			throw re; 
		} 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(FacturaTimbre entity) throws Exception {
		try {
			this.ifzFacturaTimbre.update(entity); 
		} catch (Exception re) { 
			throw re; 
		} 
	}

	// -------------------------------------------------------------------------------------------------------------------
	
	@Override
	public FacturaTimbre findById(long idTimbre) throws Exception {
		try {
			return this.ifzFacturaTimbre.findById(idTimbre);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaTimbre> findAll() throws Exception {
		try {
			return this.ifzFacturaTimbre.findAll(getIdEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaTimbre> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzFacturaTimbre.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaTimbre> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzFacturaTimbre.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public FacturaTimbre comprobarTimbre(String serie, String folio) throws Exception {
		List<FacturaTimbre> entities = null;
		FacturaTimbre entity = null;
		
		try {
			entities = this.ifzFacturaTimbre.comprobarTimbre(serie, folio, getIdEmpresa());
			if (entities != null && ! entities.isEmpty())
				entity = entities.get(0);
			return entity;
		} catch (Exception re) { 
			throw re;
		}
	}

	// ------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------

	@SuppressWarnings("unused")
	private Long getIdUsuario() {
		if (this.infoSesion != null) 
			this.idUsuario = this.infoSesion.getAcceso().getUsuario().getId();
		this.idUsuario = (this.idUsuario > 0L ? this.idUsuario : 1L);
		return this.idUsuario;
	}
	
	private Long getIdEmpresa() {
		if (this.infoSesion != null) 
			this.idEmpresa = this.infoSesion.getEmpresa().getId();
		this.idEmpresa = (this.idEmpresa > 0L ? this.idEmpresa : 1L);
		return this.idEmpresa;
	}
	
	private Long getCodigoEmpresa() {
		if (this.infoSesion != null) 
			this.codigoEmpresa = this.infoSesion.getEmpresa().getCodigo();
		this.codigoEmpresa = (this.codigoEmpresa > 0L ? this.codigoEmpresa : 1L);	
		return this.codigoEmpresa;
	}
}
