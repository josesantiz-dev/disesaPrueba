package net.giro.compras.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.compras.beans.Comparativa;
import net.giro.compras.dao.ComparativaDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ComparativaFac implements ComparativaRem {
	private static Logger log = Logger.getLogger(ComparativaFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ComparativaDAO ifzComparativas;
	private static String orderBy;
	
	public ComparativaFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(environment);
            this.ifzComparativas = (ComparativaDAO) this.ctx.lookup("ejb:/Model_Compras//ComparativaImp!net.giro.compras.dao.ComparativaDAO");
       } catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.ComparativaFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void OrderBy(String orderBy) {
		ComparativaFac.orderBy = orderBy;
	}

	@Override
	public Long save(Comparativa entity) throws Exception {
		try {
			return this.ifzComparativas.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en ComparativaFac.save(Comparativa)", e);
			throw e;
		}
	}

	@Override
	public void update(Comparativa entity) throws Exception {
		try {
			this.ifzComparativas.update(entity);
		} catch (Exception e) {
			log.error("Error en ComparativaFac.update(Comparativa)", e);
			throw e;
		}
	}

	@Override
	public List<Comparativa> saveOrUpdateList(List<Comparativa> entities) throws Exception {
		try {
			return this.ifzComparativas.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en ComparativaFac.saveOrUpdateList(List<Comparativa> entities)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idComparativa) throws Exception {
		try {
			this.ifzComparativas.delete(idComparativa);
		} catch (Exception e) {
			log.error("Error en ComparativaFac.delete(idComparativa)", e);
			throw e;
		}
	}

	@Override
	public Comparativa findById(Long idComparativa) {
		try {
			return this.ifzComparativas.findById(idComparativa);
		} catch (Exception e) {
			log.error("Error en ComparativaFac.findById(idComparativa)", e);
			throw e;
		}
	}

	@Override
	public List<Comparativa> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzComparativas.OrderBy(orderBy);
			return this.ifzComparativas.findByProperty(propertyName, value, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en ComparativaFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Comparativa> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzComparativas.OrderBy(orderBy);
			return this.ifzComparativas.findLikeProperty(propertyName, value, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en ComparativaFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Comparativa> findInProperty(String propertyName, List<Object> values) throws Exception {
		try {
			this.ifzComparativas.OrderBy(orderBy);
			return this.ifzComparativas.findInProperty(propertyName, values, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en ComparativaFac.findInProperty(propertyName, values)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	//------------------------------------------------------------------------------------------------------
	// PRIVADOS
	//------------------------------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		Long idEmpresa = 1L;
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getId();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}
	
	private Long getCodigoEmpresa() {
		Long idEmpresa = 1L;
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getCodigo();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}
}
