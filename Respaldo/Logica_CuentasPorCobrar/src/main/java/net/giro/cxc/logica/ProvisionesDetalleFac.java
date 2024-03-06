package net.giro.cxc.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxc.beans.Provisiones;
import net.giro.cxc.beans.ProvisionesDetalle;
import net.giro.cxc.beans.ProvisionesDetalleExt;
import net.giro.cxc.dao.ProvisionesDetalleDAO;

@Stateless
public class ProvisionesDetalleFac implements ProvisionesDetalleRem {
	private static Logger log = Logger.getLogger(ProvisionesDetalleFac.class);
	private ProvisionesDetalleDAO ifzProvisionesDetalle;
	private InitialContext ctx;
	private ConvertExt convertidor;
	
	
	public ProvisionesDetalleFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzProvisionesDetalle = (ProvisionesDetalleDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//ProvisionesDetalleImp!net.giro.cxc.dao.ProvisionesDetalleDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ProvisionesDetalleFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear MODULO.ProvisionesDetalleFac", e);
			ctx = null;
		}
	}

	
	@Override
	public Long save(ProvisionesDetalle entity) throws Exception {
		try {
			return this.ifzProvisionesDetalle.save(entity);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	public void update(ProvisionesDetalle entity) throws Exception {
		try {
			this.ifzProvisionesDetalle.update(entity);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws Exception {
		try {
			this.ifzProvisionesDetalle.delete(entityId);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.delete(entityId)", e);
			throw e;
		}
	}

	@Override
	public void delete(ProvisionesDetalle entity) throws Exception {
		try {
			this.delete(entity.getId());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.delete(entity)", e);
			throw e;
		}
	}

	@Override
	public ProvisionesDetalle findById(Long entityId) throws Exception {
		try {
			return this.ifzProvisionesDetalle.findById(entityId);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.delete(entityId)", e);
			throw e;
		}
	}
	
	@Override
	public List<ProvisionesDetalle> findAll(Long idProvisiones) throws Exception {
		try {
			return this.ifzProvisionesDetalle.findAll(idProvisiones);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findAll(idProvisiones)", e);
			throw e;
		}
	}

	@Override
	public List<ProvisionesDetalle> findAll(Provisiones entity) throws Exception {
		try {
			return this.findAll(entity.getId());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findAll(entity)", e);
			throw e;
		}
	}

	@Override
	public List<ProvisionesDetalle> findByProperty(String propertyName, Object value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findByProperty(propertyName, value)", e);
			throw e;
		}
	}

	@Override
	public List<ProvisionesDetalle> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzProvisionesDetalle.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<ProvisionesDetalle> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			return this.findByProperties(params, null, 0);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findByProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<ProvisionesDetalle> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception {
		try {
			return this.ifzProvisionesDetalle.findByProperties(params, orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findByProperties(params, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<ProvisionesDetalle> findLikeProperty(String propertyName, String value) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findLikeProperty(propertyName, value)", e);
			throw e;
		}
	}

	@Override
	public List<ProvisionesDetalle> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		try {
			return this.ifzProvisionesDetalle.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<ProvisionesDetalle> findLikeProperties(HashMap<String, String> params) throws Exception {
		try {
			return this.findLikeProperties(params, null, 0);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findLikeProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<ProvisionesDetalle> findLikeProperties(HashMap<String, String> params, String orderBy, int limite) throws Exception {
		try {
			return this.ifzProvisionesDetalle.findLikeProperties(params, orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findLikeProperties(params, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<ProvisionesDetalle> findInProperty(String propertyName, List<Object> values) throws Exception {
		try {
			return this.findInProperty(propertyName, values, 0);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findInProperty(propertyName, values)", e);
			throw e;
		}
	}

	@Override
	public List<ProvisionesDetalle> findInProperty(String propertyName, List<Object> values, int limite) throws Exception {
		try {
			return this.ifzProvisionesDetalle.findInProperty(propertyName, values, limite);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findInProperty(propertyName, values, limite)", e);
			throw e;
		}
	}

	@Override
	public ProvisionesDetalle convertir(ProvisionesDetalleExt entityExt) throws Exception {
		try {
			return this.convertidor.ProvisionesDetalleExtToProvisionesDetalle(entityExt);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.convertir(entityExt)", e);
			throw e;
		}
	}

	@Override
	public ProvisionesDetalleExt convertir(ProvisionesDetalle entity) throws Exception {
		try {
			return this.convertidor.ProvisionesDetalleToProvisionesDetalleExt(entity);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.convertir(entity)", e);
			throw e;
		}
	}

	// EXTENDIDOS
	
	@Override
	public Long save(ProvisionesDetalleExt entityExt) throws Exception {
		try {
			return this.save(this.convertir(entityExt));
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.save(entityExt)", e);
			throw e;
		}
	}
	
	@Override
	public void update(ProvisionesDetalleExt entityExt) throws Exception {
		try {
			this.update(this.convertir(entityExt));
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.update(entityExt)", e);
			throw e;
		}
	}
	
	@Override
	public void delete(ProvisionesDetalleExt entityExt) throws Exception {
		try {
			this.delete(entityExt.getId());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.delete(entityExt)", e);
			throw e;
		}
	}

	@Override
	public ProvisionesDetalleExt findExtById(Long entityId) throws Exception {
		ProvisionesDetalleExt pojoExt = new ProvisionesDetalleExt();
		ProvisionesDetalle pojoTarget = new ProvisionesDetalle();
		
		try {
			pojoTarget = this.findById(entityId);
			if (pojoTarget != null)
				pojoExt = this.convertir(pojoTarget);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findExtById(entityId)", e);
			throw e;
		}
		
		return pojoExt;
	}

	@Override
	public List<ProvisionesDetalleExt> findExtAll(Long idProvisiones) throws Exception {
		List<ProvisionesDetalleExt> listaExt = new ArrayList<ProvisionesDetalleExt>();
		List<ProvisionesDetalle> lista = null;
		
		try {
			lista = this.findAll(idProvisiones);
			if (lista != null && ! lista.isEmpty()) {
				for (ProvisionesDetalle var : lista)
					listaExt.add(this.convertir(var));
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findExtAll(idProvisiones)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ProvisionesDetalleExt> findExtAll(Provisiones entity) throws Exception {
		List<ProvisionesDetalleExt> listaExt = new ArrayList<ProvisionesDetalleExt>();
		List<ProvisionesDetalle> lista = null;
		
		try {
			lista = this.findAll(entity);
			if (lista != null && ! lista.isEmpty()) {
				for (ProvisionesDetalle var : lista)
					listaExt.add(this.convertir(var));
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findExtAll(entity)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ProvisionesDetalleExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<ProvisionesDetalleExt> listaExt = new ArrayList<ProvisionesDetalleExt>();
		List<ProvisionesDetalle> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (ProvisionesDetalle var : lista)
					listaExt.add(this.convertir(var));
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ProvisionesDetalleExt> findExtLikeProperty(String propertyName, String value, int limite) throws Exception {
		List<ProvisionesDetalleExt> listaExt = new ArrayList<ProvisionesDetalleExt>();
		List<ProvisionesDetalle> lista = null;
		
		try {
			lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (ProvisionesDetalle var : lista)
					listaExt.add(this.convertir(var));
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ProvisionesDetalleExt> extenderLista(List<ProvisionesDetalle> lista) throws Exception {
		List<ProvisionesDetalleExt> listaExt = new ArrayList<ProvisionesDetalleExt>();
		
		try {
			if (lista != null && ! lista.isEmpty()) {
				for (ProvisionesDetalle var : lista)
					listaExt.add(this.convertir(var));
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.extenderLista(lista)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ProvisionesDetalle> normalizarLista(List<ProvisionesDetalleExt> listaExt) throws Exception {
		List<ProvisionesDetalle> lista = new ArrayList<ProvisionesDetalle>();
		
		try {
			if (listaExt != null && ! listaExt.isEmpty()) {
				for (ProvisionesDetalleExt var : listaExt)
					lista.add(this.convertir(var));
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.ProvisionesDetalleFac.normalizarLista(listaExt)", e);
			throw e;
		}
		
		return lista;
	}
}
