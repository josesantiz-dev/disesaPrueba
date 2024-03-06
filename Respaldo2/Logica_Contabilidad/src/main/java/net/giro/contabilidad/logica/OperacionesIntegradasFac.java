package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.OperacionesIntegradas;
import net.giro.contabilidad.dao.OperacionesIntegradasDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class OperacionesIntegradasFac implements OperacionesIntegradasRem {
	private static Logger log = Logger.getLogger(OperacionesIntegradasFac.class);
	private InitialContext ctx;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private OperacionesIntegradasDAO ifzOIntegradas;
	private static String orderBy;
	
	
	public OperacionesIntegradasFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try{
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzOIntegradas = (OperacionesIntegradasDAO) this.ctx.lookup("ejb:/Model_Contabilidad//OperacionesIntegradasImp!net.giro.contabilidad.dao.OperacionesIntegradasDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.OperacionesIntegradasFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		//this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void orderBy(String orderBy) {
		OperacionesIntegradasFac.orderBy = orderBy;
	}

	@Override
	public Long save(OperacionesIntegradas entity) throws Exception {
		try {
			return this.ifzOIntegradas.save(entity, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasFac.save(OperacionesIntegradas)", e);
			throw e;
		}
	}

	@Override
	public List<OperacionesIntegradas> saveOrUpdateList(List<OperacionesIntegradas> entities) throws Exception {
		try {
			return this.ifzOIntegradas.saveOrUpdateList(entities, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasFac.saveOrUpdateList(List<OperacionesIntegradas> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(OperacionesIntegradas entity) throws Exception {
		try {
			this.ifzOIntegradas.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasFac.update(OperacionesIntegradas)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws Exception {
		try {
			this.ifzOIntegradas.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public OperacionesIntegradas findById(Long id) {
		try {
			return this.ifzOIntegradas.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<OperacionesIntegradas> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOIntegradas.orderBy(orderBy);
			return this.ifzOIntegradas.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradas> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOIntegradas.orderBy(orderBy);
			return this.ifzOIntegradas.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradas> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzOIntegradas.orderBy(orderBy);
			return this.ifzOIntegradas.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradas> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOIntegradas.orderBy(orderBy);
			return this.ifzOIntegradas.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradas> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOIntegradas.orderBy(orderBy);
			return this.ifzOIntegradas.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	
	@Override
	public OperacionesIntegradas comprobarOperacionIntegrada(OperacionesIntegradas entity) throws Exception {
		List<OperacionesIntegradas> list = null;
		OperacionesIntegradas resultado = null;
		
		try {
			if (entity == null)
				return null;
			
			if (entity.getIdOperacion() == null || entity.getIdOperacion().getId() == null || entity.getIdOperacion().getId() <= 0L)
				return null;
			
			list = this.findByProperty("idOperacion", entity.getIdOperacion(), 0);
			if (list != null && ! list.isEmpty()) {
				if (entity.getId() != null && entity.getId().longValue() > 0L) {
					for (OperacionesIntegradas var : list) {
						if (entity.getId().longValue() != var.getId().longValue()) {
							resultado = var;
							break;
						}
					}
				} else {
					resultado = list.get(0);
				}
			}
			
			return resultado;
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasFac.comprobarOperacionIntegrada(OperacionesIntegradas)", e);
			throw e;
		}
	}
}