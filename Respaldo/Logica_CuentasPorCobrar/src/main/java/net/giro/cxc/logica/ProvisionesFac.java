package net.giro.cxc.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.Provisiones;
import net.giro.cxc.dao.ProvisionesDAO;

@Stateless
public class ProvisionesFac implements ProvisionesRem {
	private static Logger log = Logger.getLogger(ProvisionesFac.class);
	private ProvisionesDAO ifzProvisiones;
	private InitialContext ctx;
	
	
	public ProvisionesFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzProvisiones = (ProvisionesDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//ProvisionesImp!net.giro.cxc.dao.ProvisionesDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_CuentasPorCobrar.ProvisionesFac", e);
			ctx = null;
		}
	}
	

	@Override
	public Provisiones saveWithGrupo(Provisiones entity) throws Exception {
		try {
			if (entity.getGrupo() <= 0)
				entity.setGrupo(this.ifzProvisiones.findNextGrupo());
			entity.setId(this.ifzProvisiones.save(entity));
			return entity;
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	public Long save(Provisiones entity) throws Exception {
		try {
			return this.ifzProvisiones.save(entity);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	public Provisiones saveOrUpdate(Provisiones entity) throws Exception {
		try {
			if (entity.getId() == null || entity.getId() <= 0L)
				entity.setId(this.save(entity));
			else
				this.update(entity);
			return entity;
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	public List<Provisiones> save(List<Provisiones> listEntities) throws Exception {
		int grupo = 0;
		
		try {
			grupo = this.ifzProvisiones.findNextGrupo();
			for (Provisiones entity : listEntities) {
				if (entity.getGrupo() <= 0)
					entity.setGrupo(grupo);
			}
			
			return this.ifzProvisiones.saveOrUpdateList(listEntities);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.save(listEntities)", e);
			throw e;
		}
	}

	@Override
	public void update(Provisiones entity) throws Exception {
		try {
			this.ifzProvisiones.update(entity);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(Provisiones entity) throws Exception {
		try {
			this.delete(entity.getId());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.delete(entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws Exception {
		try {
			this.ifzProvisiones.delete(entityId);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.delete(entityId)", e);
			throw e;
		}
	}

	@Override
	public List<Provisiones> findAll() throws Exception {
		try {
			return this.ifzProvisiones.findAll();
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findAll()", e);
			throw e;
		}
	}

	@Override
	public List<Provisiones> findAllInactives() throws Exception {
		try {
			return this.ifzProvisiones.findAllInactives();
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findAllInactives()", e);
			throw e;
		}
	}

	@Override
	public List<Provisiones> findByProperty(String propertyName, Object value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findByProperty(propertyName, value)", e);
			throw e;
		}
	}

	@Override
	public List<Provisiones> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzProvisiones.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<Provisiones> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			return this.findByProperties(params, null, 0);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findByProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<Provisiones> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception {
		try {
			return this.ifzProvisiones.findByProperties(params, orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findByProperties(params, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<Provisiones> findLikeProperty(String propertyName, String value) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findLikeProperty(propertyName, value)", e);
			throw e;
		}
	}

	@Override
	public List<Provisiones> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		try {
			return this.ifzProvisiones.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<Provisiones> findLikeProperties(HashMap<String, String> params) throws Exception {
		try {
			return this.findLikeProperties(params, null, 0);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findLikeProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<Provisiones> findLikeProperties(HashMap<String, String> params, String orderBy, int limite) throws Exception {
		try {
			return this.ifzProvisiones.findLikeProperties(params, orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findLikeProperties(params, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public Provisiones findProvision(Provisiones entityProvision) throws Exception {
		try {
			List<Provisiones> listProvisiones = this.ifzProvisiones.findByProperty("idFactura.id", entityProvision.getIdFactura().getId(), 0);
			if (listProvisiones != null && ! listProvisiones.isEmpty())
				return listProvisiones.get(0);
			return null;
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findProvision(entityProvision)", e);
			throw e;
		}
	}

	@Override
	public Provisiones findProvision(Long entityIdFactura) throws Exception {
		try {
			List<Provisiones> listProvisiones = this.ifzProvisiones.findByProperty("idFactura.id", entityIdFactura, 0);
			if (listProvisiones != null && ! listProvisiones.isEmpty())
				return listProvisiones.get(0);
			return null;
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findProvision(entityIdFactura)", e);
			throw e;
		}
	}

	@Override
	public Provisiones findProvision(Factura entityFactura) throws Exception {
		try {
			List<Provisiones> listProvisiones = this.ifzProvisiones.findByProperty("idFactura.id", entityFactura.getId(), 0);
			if (listProvisiones != null && ! listProvisiones.isEmpty())
				return listProvisiones.get(0);
			return null;
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesFac.findProvision(entityIdFactura)", e);
			throw e;
		}
	}
}
