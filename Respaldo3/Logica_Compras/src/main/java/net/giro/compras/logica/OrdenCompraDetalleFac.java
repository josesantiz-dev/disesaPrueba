package net.giro.compras.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.OrdenCompraDetalleExt;
import net.giro.compras.dao.OrdenCompraDetalleDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class OrdenCompraDetalleFac implements OrdenCompraDetalleRem {
	private static Logger log = Logger.getLogger(OrdenCompraDetalleFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private OrdenCompraDetalleDAO ifzOrdenCompraDetalles;
	private ConvertExt convertidor;
	private static String orderBy;
	
	public OrdenCompraDetalleFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(environment);
            this.ifzOrdenCompraDetalles = (OrdenCompraDetalleDAO) this.ctx.lookup("ejb:/Model_Compras//OrdenCompraDetalleImp!net.giro.compras.dao.OrdenCompraDetalleDAO");
            
           this.convertidor = new ConvertExt();
           this.convertidor.setFrom("OrdenCompraFac");
           this.convertidor.setMostrarSystemOut(false);
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.OrdenCompraDetalleFac", e);
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
		OrdenCompraDetalleFac.orderBy = orderBy;
	}

	@Override
	public Long save(OrdenCompraDetalle entity) throws Exception {
		try {
			return this.ifzOrdenCompraDetalles.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.save(OrdenCompraDetalle)", e);
			throw e;
		}
	}

	@Override
	public void update(OrdenCompraDetalle entity) throws Exception {
		try {
			this.ifzOrdenCompraDetalles.update(entity);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.save(OrdenCompraDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraDetalle> saveOrUpdateList(List<OrdenCompraDetalle> entities) throws Exception {
		try {
			return this.ifzOrdenCompraDetalles.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.saveOrUpdateList(List<CotizacionDetalle> entities)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idOrdenCompraDetalle) throws Exception {
		try {
			this.ifzOrdenCompraDetalles.delete(idOrdenCompraDetalle);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.delete(idOrdenCompraDetalle)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraDetalle findById(Long idOrdenCompraDetalle) {
		try {
			return this.ifzOrdenCompraDetalles.findById(idOrdenCompraDetalle);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findById(idOrdenCompraDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraDetalle> findAll(Long idOrdenCompra) throws Exception {
		try {
			this.ifzOrdenCompraDetalles.OrderBy("id");
			return this.ifzOrdenCompraDetalles.findByProperty("idOrdenCompra", idOrdenCompra, 0);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findAll(Long idOrdenCompra)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompraDetalle> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzOrdenCompraDetalles.OrderBy(orderBy);
			return this.ifzOrdenCompraDetalles.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompraDetalle> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzOrdenCompraDetalles.OrderBy(orderBy);
			return this.ifzOrdenCompraDetalles.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompraDetalle> findInProperty(String propertyName, List<Object> values) throws Exception {
		try {
			this.ifzOrdenCompraDetalles.OrderBy(orderBy);
			return this.ifzOrdenCompraDetalles.findInProperty(propertyName, values);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findInProperty(propertyName, values)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public OrdenCompraDetalle convertir(OrdenCompraDetalleExt pojoTarget) throws Exception {
		return this.convertidor.OrdenCompraDetalleExtToOrdenCompraDetalle(pojoTarget);
	}

	@Override
	public OrdenCompraDetalleExt convertir(OrdenCompraDetalle pojoTarget) throws Exception {
		return this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(pojoTarget);
	}

	// ------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// ------------------------------------------------------------------------------------------------------------------

	@Override
	public Long save(OrdenCompraDetalleExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.OrdenCompraDetalleExtToOrdenCompraDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.save(OrdenCompraDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraDetalleExt> saveOrUpdateListExt(List<OrdenCompraDetalleExt> extendidos) throws Exception {
		List<OrdenCompraDetalle> entities = new ArrayList<OrdenCompraDetalle>();
		
		try {
			for (OrdenCompraDetalleExt item : extendidos)
				entities.add(this.convertidor.OrdenCompraDetalleExtToOrdenCompraDetalle(item));
			entities = this.saveOrUpdateList(entities);
			extendidos.clear();
			for (OrdenCompraDetalle item : entities) 
				extendidos.add(this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(item));
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.saveOrUpdateListExt(extendidos)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public void update(OrdenCompraDetalleExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.OrdenCompraDetalleExtToOrdenCompraDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.save(OrdenCompraDetalle)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraDetalleExt findExtById(Long idOrdenCompraDetalle) throws Exception {
		try {
			return this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(this.findById(idOrdenCompraDetalle));
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findExtById(idOrdenCompraDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraDetalleExt> findExtAll(Long idOrdenCompra) throws Exception {
		List<OrdenCompraDetalleExt> listaExt = new ArrayList<OrdenCompraDetalleExt>();
		List<OrdenCompraDetalle> lista = null;
		
		try {
			lista = this.findAll(idOrdenCompra);
			if (lista != null && ! lista.isEmpty()) {
				for (OrdenCompraDetalle var : lista) {
					listaExt.add(this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findExtAll(Long idOrdenCompra)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompraDetalleExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<OrdenCompraDetalleExt> listaExt = new ArrayList<OrdenCompraDetalleExt>();
		
		try {
			List<OrdenCompraDetalle> lista = this.findByProperty(propertyName, value, limite);
			if (lista != null) {
				for(OrdenCompraDetalle var : lista) {
					listaExt.add(this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompraDetalleExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<OrdenCompraDetalleExt> listaExt = new ArrayList<OrdenCompraDetalleExt>();
		
		try {
			List<OrdenCompraDetalle> lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null) {
				for(OrdenCompraDetalle var : lista) {
					listaExt.add(this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompraDetalleExt> findExtInProperty(String propertyName, List<Object> values) throws Exception {
		List<OrdenCompraDetalleExt> listaExt = new ArrayList<OrdenCompraDetalleExt>();
		
		try {
			List<OrdenCompraDetalle> lista = this.findInProperty(propertyName, values);
			if (lista != null) {
				for (OrdenCompraDetalle var : lista) 
					listaExt.add(this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(var));
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findExtInProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	// ------------------------------------------------------------------------------------------------------------------
	// PRIVATE 
	// ------------------------------------------------------------------------------------------------------------------
	
	private Long getCodigoEmpresa() {
		Long idEmpresa = 1L;
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getCodigo();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}
}
