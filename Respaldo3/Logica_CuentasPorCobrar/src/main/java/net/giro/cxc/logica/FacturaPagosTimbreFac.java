package net.giro.cxc.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxc.beans.FacturaPagosTimbre;
import net.giro.cxc.dao.FacturaPagosTimbreDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class FacturaPagosTimbreFac implements FacturaPagosTimbreRem {
	private static Logger log = Logger.getLogger(FacturaPagosTimbreFac.class);
	private InitialContext ctx;
	private FacturaPagosTimbreDAO ifzFacturaPagosTimbre;
	private InfoSesion infoSesion;
	private long idUsuario;
	private long idEmpresa;
	private long codigoEmpresa;

	public FacturaPagosTimbreFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(environment);
			this.ifzFacturaPagosTimbre = (FacturaPagosTimbreDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturaPagosTimbreImp!net.giro.cxc.dao.FacturaPagosTimbreDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_CuentasPorCobrar.FacturaPagosTimbreFac", e);
			ctx = null;
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
	public long save(FacturaPagosTimbre entity) throws Exception {
		try {
			return this.ifzFacturaPagosTimbre.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public FacturaPagosTimbre saveOrUpdate(FacturaPagosTimbre entity) throws Exception {
		long idTimbre = 0L;
		
		try {
			if (entity == null)
				return entity;
			idTimbre = this.ifzFacturaPagosTimbre.saveOrUpdate(entity, getCodigoEmpresa());
			if (entity.getId() == null || entity.getId() <= 0L)
				entity.setId(idTimbre);
			return entity;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<FacturaPagosTimbre> saveOrUpdateList(List<FacturaPagosTimbre> entities) throws Exception {
		try {
			return this.ifzFacturaPagosTimbre.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) { 
			throw re; 
		} 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(FacturaPagosTimbre entity) throws Exception {
		try {
			this.ifzFacturaPagosTimbre.update(entity); 
		} catch (Exception re) { 
			throw re; 
		} 
	}

	/*@Override
	public void cancelar(FacturaPagosTimbre entity) throws Exception {
		try {
			this.ifzFacturaPagosTimbre.update(entity); 
		} catch (Exception re) { 
			throw re; 
		} 
	}

	@Override
	public Comprobante comprobanteCFDI(long idTimbre) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comprobante comprobanteCFDI(FacturaPagosTimbre timbre) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}*/

	// -------------------------------------------------------------------------------------------------------------------
	
	@Override
	public FacturaPagosTimbre findById(long idTimbre) throws Exception {
		try {
			return this.ifzFacturaPagosTimbre.findById(idTimbre);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaPagosTimbre> findAll() throws Exception {
		try {
			return this.ifzFacturaPagosTimbre.findAll(getIdEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaPagosTimbre> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzFacturaPagosTimbre.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaPagosTimbre> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzFacturaPagosTimbre.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public FacturaPagosTimbre comprobarTimbre(String serie, String folio) throws Exception {
		List<FacturaPagosTimbre> entities = null;
		FacturaPagosTimbre entity = null;
		
		try {
			entities = this.ifzFacturaPagosTimbre.comprobarTimbre(serie, folio, getIdEmpresa());
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
		if (this.infoSesion != null && this.infoSesion.getAcceso().getUsuario().getId() != this.idUsuario) 
			this.idUsuario = this.infoSesion.getAcceso().getUsuario().getId();
		this.idUsuario = (this.idUsuario > 0L ? this.idUsuario : 1L);
		return this.idUsuario;
	}
	
	private Long getIdEmpresa() {
		if (this.infoSesion != null && this.infoSesion.getEmpresa().getId().longValue() != this.idEmpresa) 
			this.idEmpresa = this.infoSesion.getEmpresa().getId();
		this.idEmpresa = (this.idEmpresa > 0L ? this.idEmpresa : 1L);
		return this.idEmpresa;
	}
	
	private Long getCodigoEmpresa() {
		if (this.infoSesion != null && this.infoSesion.getEmpresa().getCodigo() != this.codigoEmpresa) 
			this.codigoEmpresa = this.infoSesion.getEmpresa().getCodigo();
		this.codigoEmpresa = (this.codigoEmpresa > 0L ? this.codigoEmpresa : 1L);	
		return this.codigoEmpresa;
	}
}
