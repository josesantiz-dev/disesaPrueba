package net.giro.cxc.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxc.beans.FacturaDetalleImpuesto;
import net.giro.cxc.beans.FacturaDetalleImpuestoExt;
import net.giro.cxc.dao.FacturaDetalleImpuestoDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class FacturaDetalleImpuestoFac implements FacturaDetalleImpuestoRem {
	private static Logger log = Logger.getLogger(FacturaDetalleImpuestoFac.class);	
	private InitialContext ctx;	
	private FacturaDetalleImpuestoDAO ifzFacturaDetalleImpuesto;
	private InfoSesion infoSesion;
	private ConvertExt convertidor;
	
	public FacturaDetalleImpuestoFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(environment);
            this.ifzFacturaDetalleImpuesto = (FacturaDetalleImpuestoDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturaDetalleImpuestoImp!net.giro.cxc.dao.FacturaDetalleImpuestoDAO");
            this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaFac", e);
			this.ctx = null;
		}
	}
	
	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(FacturaDetalleImpuesto entity) throws Exception {
		try {
			return this.ifzFacturaDetalleImpuesto.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuesto> saveOrUpdateList(List<FacturaDetalleImpuesto> entities) throws Exception {
		try {
			return this.ifzFacturaDetalleImpuesto.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(FacturaDetalleImpuesto entity) throws Exception {
		try {
			this.ifzFacturaDetalleImpuesto.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(Long idFacturaDetalleImpuesto) throws Exception {
		try {
			this.ifzFacturaDetalleImpuesto.delete(idFacturaDetalleImpuesto);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public FacturaDetalleImpuesto findById(Long idFacturaDetalleImpuesto) {
		try {
			return this.ifzFacturaDetalleImpuesto.findById(idFacturaDetalleImpuesto);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuesto> findAll(Long idFacturaDetalle, String orderBy) throws Exception {
		try {
			return this.ifzFacturaDetalleImpuesto.findAll(idFacturaDetalle, orderBy);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuesto> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzFacturaDetalleImpuesto.findByProperty(propertyName, value, orderBy, limite);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuesto> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzFacturaDetalleImpuesto.findLikeProperty(propertyName, value, orderBy, limite);
		} catch (Exception re) {	
			throw re;
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// CONVERTIDOR
	// ------------------------------------------------------------------------------------------------------

	@Override
	public FacturaDetalleImpuesto convertir(FacturaDetalleImpuestoExt extendido) {
		try {
			return this.convertidor.FacturaDetalleImpuestoExtToFacturaDetalleImpuesto(extendido);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public FacturaDetalleImpuestoExt convertir(FacturaDetalleImpuesto target) throws Exception {
		try {
			return this.convertidor.FacturaDetalleImpuestoToFacturaDetalleImpuestoExt(target);
		} catch (Exception e) {
			throw e;
		}
	}

	// ----------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------------------------------------------------------
	
	@Override
	public Long save(FacturaDetalleImpuestoExt extendido) throws Exception {
		try {
			return this.save(this.convertir(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuestoExt> saveOrUpdateListExt(List<FacturaDetalleImpuestoExt> extendidos) throws Exception {
		List<FacturaDetalleImpuesto> entities = new ArrayList<FacturaDetalleImpuesto>();
		
		try {
			for (FacturaDetalleImpuestoExt extendido : extendidos)
				entities.add(this.convertir(extendido));
			entities = this.saveOrUpdateList(entities);
			extendidos.clear();
			for (FacturaDetalleImpuesto entity : entities) 
				extendidos.add(this.convertir(entity));
		} catch (Exception re) {
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public void update(FacturaDetalleImpuestoExt extendido) throws Exception {
		try {
			this.update(this.convertir(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public FacturaDetalleImpuestoExt findExtById(Long idFacturaDetalleImpuesto) throws Exception {
		try {
			return this.convertir(this.findById(idFacturaDetalleImpuesto));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuestoExt> findAllExt(Long idFacturaDetalle, String orderBy) throws Exception {
		List<FacturaDetalleImpuestoExt> extendidos = new ArrayList<FacturaDetalleImpuestoExt>();
		List<FacturaDetalleImpuesto> entities = null;
		
		try {
			entities = this.findAll(idFacturaDetalle, orderBy);
			if (entities == null || entities.isEmpty())
				return extendidos;
			for (FacturaDetalleImpuesto entity : entities)
				extendidos.add(this.convertir(entity));
		} catch (Exception re) {	
			throw re;
		}
		
		return extendidos;
	}
	
	// -----------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L;
	}
}
