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
import net.giro.comun.ExcepConstraint;
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
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try{
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
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
	public Long save(OrdenCompraDetalle entity) throws ExcepConstraint {
		try {
			return this.ifzOrdenCompraDetalles.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.save(OrdenCompraDetalle)", e);
			throw e;
		}
	}

	@Override
	public Long save(OrdenCompraDetalleExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.OrdenCompraDetalleExtToOrdenCompraDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.save(OrdenCompraDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public void update(OrdenCompraDetalle entity) throws ExcepConstraint {
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
			return this.ifzOrdenCompraDetalles.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionDetalleFac.saveOrUpdateList(List<CotizacionDetalle> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(OrdenCompraDetalleExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.OrdenCompraDetalleExtToOrdenCompraDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.save(OrdenCompraDetalle)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long id) throws ExcepConstraint {
		try {
			this.ifzOrdenCompraDetalles.delete(id);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.delete(id)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraDetalle findById(Long id) {
		try {
			return this.ifzOrdenCompraDetalles.findById(id);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraDetalleExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(this.findById(id));
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findExtById(id)", e);
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
	public List<OrdenCompraDetalle> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOrdenCompraDetalles.OrderBy(orderBy);
			return this.ifzOrdenCompraDetalles.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompraDetalleExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<OrdenCompraDetalleExt> listaExt = new ArrayList<OrdenCompraDetalleExt>();
		
		try {
			List<OrdenCompraDetalle> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(OrdenCompraDetalle var : lista) {
					listaExt.add(this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompraDetalle> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOrdenCompraDetalles.OrderBy(orderBy);
			return this.ifzOrdenCompraDetalles.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompraDetalleExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<OrdenCompraDetalleExt> listaExt = new ArrayList<OrdenCompraDetalleExt>();
		
		try {
			List<OrdenCompraDetalle> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(OrdenCompraDetalle var : lista) {
					listaExt.add(this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompraDetalle> findInProperty(String columnName, List<Object> values) throws Exception {
		try {
			this.ifzOrdenCompraDetalles.OrderBy(orderBy);
			return this.ifzOrdenCompraDetalles.findInProperty(columnName, values);
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findInProperty(propertyName, values)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompraDetalleExt> findExtInProperty(String columnName, List<Object> values) throws Exception {
		List<OrdenCompraDetalleExt> listaExt = new ArrayList<OrdenCompraDetalleExt>();
		
		try {
			List<OrdenCompraDetalle> lista = this.findInProperty(columnName, values);
			if (lista != null) {
				for(OrdenCompraDetalle var : lista) {
					listaExt.add(this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraDetalleFac.findExtInProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public OrdenCompraDetalle convertir(OrdenCompraDetalleExt pojoTarget) throws Exception {
		return this.convertidor.OrdenCompraDetalleExtToOrdenCompraDetalle(pojoTarget);
	}

	@Override
	public OrdenCompraDetalleExt convertir(OrdenCompraDetalle pojoTarget) throws Exception {
		return this.convertidor.OrdenCompraDetalleToOrdenCompraDetalleExt(pojoTarget);
	}

	private Long getIdEmpresa() {
		Long idEmpresa = 1L;
		
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getId();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}
}
