package net.giro.compras.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.CotizacionDetalleExt;
import net.giro.compras.dao.CotizacionDetalleDAO;
import net.giro.plataforma.InfoSesion;

import org.apache.log4j.Logger;

@Stateless
public class CotizacionDetalleFac implements CotizacionDetalleRem {
	private static Logger log = Logger.getLogger(CotizacionDetalleFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private CotizacionDetalleDAO ifzCotizacionDetalles;
	private ConvertExt convertidor;
	private static String orderBy;
	
	public CotizacionDetalleFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(environment);
			this.ifzCotizacionDetalles = (CotizacionDetalleDAO) this.ctx.lookup("ejb:/Model_Compras//CotizacionDetalleImp!net.giro.compras.dao.CotizacionDetalleDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("CotizacionDetalleFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.CotizacionDetalleFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void OrderBy(String orderBy) {
		CotizacionDetalleFac.orderBy = orderBy;
	}

	// ---------------------------------------------------------------------------------------------------------------------
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public Long save(CotizacionDetalle entity) throws Exception {
		try {
			return this.ifzCotizacionDetalles.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.save(CotizacionDetalle)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public void update(CotizacionDetalle entity) throws Exception {
		try {
			this.ifzCotizacionDetalles.update(entity);
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.update(CotizacionDetalle)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public List<CotizacionDetalle> saveOrUpdateList(List<CotizacionDetalle> entities) throws Exception {
		try {
			return this.ifzCotizacionDetalles.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.saveOrUpdateList(List<CotizacionDetalle> entities)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idCotizacionDetalle) throws Exception {
		try {
			this.ifzCotizacionDetalles.delete(idCotizacionDetalle);
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.delete(idCotizacionDetalle)", e);
			throw e;
		}
	}

	@Override
	public CotizacionDetalle findById(Long idCotizacionDetalle) {
		try {
			return this.ifzCotizacionDetalles.findById(idCotizacionDetalle);
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findById(idCotizacionDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<CotizacionDetalle> findAll(Long idCotizacion) throws Exception {
		try {
			return this.ifzCotizacionDetalles.findByProperty("idCotizacion", idCotizacion, 0);
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findAll(Long idCotizacion)", e);
			throw e;
		} 
	}

	@Override
	public List<CotizacionDetalle> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzCotizacionDetalles.OrderBy(orderBy);
			return this.ifzCotizacionDetalles.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<CotizacionDetalle> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzCotizacionDetalles.OrderBy(orderBy);
			return this.ifzCotizacionDetalles.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<CotizacionDetalle> findInProperty(String propertyName, List<Object> values) throws Exception {
		try {
			this.ifzCotizacionDetalles.OrderBy(orderBy);
			return this.ifzCotizacionDetalles.findInProperty(propertyName, values);
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findInProperty(propertyName, values)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<CotizacionDetalle> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			return this.ifzCotizacionDetalles.findByProperties(params);
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findByProperties(HashMap<String, Object> params)", e);
			throw e;
		} 
	}

	@Override
	public List<CotizacionDetalle> findLikeProperties(HashMap<String, String> params) throws Exception {
		try {
			return this.ifzCotizacionDetalles.findLikeProperties(params);
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findLikeProperties(HashMap<String, Object> params)", e);
			throw e;
		} 
	}

	@Override
	public CotizacionDetalle convertir(CotizacionDetalleExt pojoTarget) throws Exception {
		try {
			return this.convertidor.CotizacionDetalleExtToCotizacionDetalle(pojoTarget);
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.convertir(CotizacionDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public CotizacionDetalleExt convertir(CotizacionDetalle pojoTarget) throws Exception {
		try {
			return this.convertidor.CotizacionDetalleToCotizacionDetalleExt(pojoTarget);
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.convertir(CotizacionDetalle)", e);
			throw e;
		}
	}
	
	// ---------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------------------------------------------------

	@Override
	public Long save(CotizacionDetalleExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.CotizacionDetalleExtToCotizacionDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.save(CotizacionDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public List<CotizacionDetalleExt> saveOrUpdateListExt( List<CotizacionDetalleExt> extendidos) throws Exception {
		List<CotizacionDetalle> entities = new ArrayList<CotizacionDetalle>();
		
		try {
			for (CotizacionDetalleExt item : extendidos)
				entities.add(this.convertidor.CotizacionDetalleExtToCotizacionDetalle(item));
			entities = this.saveOrUpdateList(entities);
			extendidos.clear();
			for (CotizacionDetalle item : entities)
				extendidos.add(this.convertidor.CotizacionDetalleToCotizacionDetalleExt(item));
			return extendidos;
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.save(CotizacionDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public void update(CotizacionDetalleExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.CotizacionDetalleExtToCotizacionDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.update(CotizacionDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public CotizacionDetalleExt findExtById(Long idCotizacionDetalle) throws Exception {
		try {
			return this.convertidor.CotizacionDetalleToCotizacionDetalleExt(this.findById(idCotizacionDetalle));
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findExtById(idCotizacionDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<CotizacionDetalleExt> findExtAll(Long idCotizacion) throws Exception {
		List<CotizacionDetalleExt> resultado = new ArrayList<CotizacionDetalleExt>();
		List<CotizacionDetalle> lista = null;
		
		try {
			lista = this.findAll(idCotizacion);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo " + lista.size() + " resultados");
				for (CotizacionDetalle det : lista)
					resultado.add(this.convertidor.CotizacionDetalleToCotizacionDetalleExt(det));
			}
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findExtByProperties(HashMap<String, Object> params)", e);
			throw e;
		}
		
		return resultado;
	}

	@Override
	public List<CotizacionDetalleExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<CotizacionDetalleExt> listaExt = new ArrayList<CotizacionDetalleExt>();
		
		try {
			List<CotizacionDetalle> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(CotizacionDetalle var : lista) {
					listaExt.add(this.convertidor.CotizacionDetalleToCotizacionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<CotizacionDetalleExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<CotizacionDetalleExt> listaExt = new ArrayList<CotizacionDetalleExt>();
		
		try {
			List<CotizacionDetalle> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(CotizacionDetalle var : lista) {
					listaExt.add(this.convertidor.CotizacionDetalleToCotizacionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<CotizacionDetalleExt> findExtInProperty(String propertyName, List<Object> values) throws Exception {
		List<CotizacionDetalleExt> listaExt = new ArrayList<CotizacionDetalleExt>();
		
		try {
			List<CotizacionDetalle> lista = this.findInProperty(propertyName, values);
			if (lista != null) {
				for(CotizacionDetalle var : lista) {
					listaExt.add(this.convertidor.CotizacionDetalleToCotizacionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findExtInProperty(propertyName, values)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<CotizacionDetalleExt> findExtByProperties(HashMap<String, Object> params) throws Exception {
		List<CotizacionDetalleExt> resultado = new ArrayList<CotizacionDetalleExt>();
		List<CotizacionDetalle> lista = null;
		
		try {
			lista = this.findByProperties(params);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo " + lista.size() + " resultados");
				for (CotizacionDetalle det : lista)
					resultado.add(this.convertidor.CotizacionDetalleToCotizacionDetalleExt(det));
			}
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findExtByProperties(HashMap<String, Object> params)", e);
			throw e;
		}
		
		return resultado;
	}

	@Override
	public List<CotizacionDetalleExt> findExtLikeProperties(HashMap<String, String> params) throws Exception {
		List<CotizacionDetalleExt> resultado = new ArrayList<CotizacionDetalleExt>();
		List<CotizacionDetalle> lista = null;
		
		try {
			lista = this.findLikeProperties(params);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo " + lista.size() + " resultados");
				for (CotizacionDetalle det : lista)
					resultado.add(this.convertidor.CotizacionDetalleToCotizacionDetalleExt(det));
			}
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.findExtByProperties(HashMap<String, Object> params)", e);
			throw e;
		}
		
		return resultado;
	}

	// ---------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ---------------------------------------------------------------------------------------------------------------------
	
	private Long getCodigoEmpresa() {
		Long idEmpresa = 1L;
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getCodigo();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}
}
