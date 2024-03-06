package net.giro.compras.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.compras.beans.ComparativaDetalle;
import net.giro.compras.dao.ComparativaDetalleDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ComparativaDetalleFac implements ComparativaDetalleRem {
	private static Logger log = Logger.getLogger(ComparativaDetalleFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private ComparativaDetalleDAO ifzComparativaDetalles;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public ComparativaDetalleFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
            this.ifzComparativaDetalles = (ComparativaDetalleDAO) this.ctx.lookup("ejb:/Model_Compras//ComparativaDetalleImp!net.giro.compras.dao.ComparativaDetalleDAO");
            
           /*this.convertidor = new ConvertExt();
           this.convertidor.setFrom("ComparativaDetalleFac");
           this.convertidor.setMostrarSystemOut(false);*/
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.ComparativaDetalleFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSecion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		//this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void OrderBy(String orderBy) {
		ComparativaDetalleFac.orderBy = orderBy;
	}

	
	@Override
	public Long save(ComparativaDetalle entity) throws ExcepConstraint {
		try {
			return this.ifzComparativaDetalles.save(entity);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.save(ComparativaDetalle)", e);
			throw e;
		}
	}

	@Override
	public void update(ComparativaDetalle entity) throws ExcepConstraint {
		try {
			this.ifzComparativaDetalles.update(entity);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.update(ComparativaDetalle)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long id) throws ExcepConstraint {
		try {
			this.ifzComparativaDetalles.delete(id);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.delete(id)", e);
			throw e;
		}
	}

	
	@Override
	public ComparativaDetalle findById(Long id) {
		try {
			return this.ifzComparativaDetalles.findById(id);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<ComparativaDetalle> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzComparativaDetalles.OrderBy(orderBy);
			return this.ifzComparativaDetalles.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ComparativaDetalle> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzComparativaDetalles.OrderBy(orderBy);
			return this.ifzComparativaDetalles.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ComparativaDetalle> findInProperty(String columnName, List<Object> values) throws Exception {
		try {
			this.ifzComparativaDetalles.OrderBy(orderBy);
			return this.ifzComparativaDetalles.findInProperty(columnName, values);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.findInProperty(columnName, values)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
}
