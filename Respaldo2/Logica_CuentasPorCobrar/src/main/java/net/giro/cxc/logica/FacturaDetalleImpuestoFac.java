package net.giro.cxc.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.beans.FacturaDetalleExt;
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
		try {
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
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
			return this.ifzFacturaDetalleImpuesto.save(entity, getIdEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuesto> saveOrUpdateList(List<FacturaDetalleImpuesto> entities) throws Exception {
		try {
			return this.ifzFacturaDetalleImpuesto.saveOrUpdateList(entities, getIdEmpresa());
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
	public void delete(Long entityId) throws Exception {
		try {
			this.ifzFacturaDetalleImpuesto.delete(entityId);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(FacturaDetalleImpuesto entity) throws Exception {
		try {
			this.delete(entity.getId());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public FacturaDetalleImpuesto findById(Long id) {
		try {
			return this.ifzFacturaDetalleImpuesto.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuesto> findAll() {
		try {
			return this.ifzFacturaDetalleImpuesto.findAll();
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuesto> findAll(String orderBy) throws Exception {
		try {
			return this.ifzFacturaDetalleImpuesto.findAll(orderBy);
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

	@Override
	public List<FacturaDetalleImpuesto> findByFacturaDetalle(Long idFacturaDetalle) throws Exception {
		try {
			return this.ifzFacturaDetalleImpuesto.findByProperty("idFacturaDetalle", idFacturaDetalle, null, 0);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuesto> findByFacturaDetalle(FacturaDetalle idFacturaDetalle) throws Exception {
		try {
			return this.findByFacturaDetalle(idFacturaDetalle.getId());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuesto> findByFacturaDetalle(FacturaDetalleExt idFacturaDetalle) throws Exception {
		try {
			return this.findByFacturaDetalle(idFacturaDetalle.getId());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public FacturaDetalleImpuesto convertir(FacturaDetalleImpuestoExt target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FacturaDetalleImpuestoExt convertir(FacturaDetalleImpuesto target) {
		// TODO Auto-generated method stub
		return null;
	}

	// ----------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------------------------------------------------------
	
	@Override
	public Long save(FacturaDetalleImpuestoExt entity) throws Exception {
		try {
			return this.save(this.convertidor.FacturaDetalleImpuestoExtToFacturaDetalleImpuesto(entity));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(FacturaDetalleImpuestoExt entity) throws Exception {
		try {
			this.update(this.convertidor.FacturaDetalleImpuestoExtToFacturaDetalleImpuesto(entity));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(FacturaDetalleImpuestoExt entity) throws Exception {
		try {
			this.delete(entity.getId());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public FacturaDetalleImpuestoExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.FacturaDetalleImpuestoToFacturaDetalleImpuestoExt(this.findById(id));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuestoExt> findExtAll() throws Exception {
		List<FacturaDetalleImpuestoExt> resultado = new ArrayList<FacturaDetalleImpuestoExt>();
		
		try {
			List<FacturaDetalleImpuesto> lista = this.findAll();
			if (lista != null && ! lista.isEmpty()) {
				for (FacturaDetalleImpuesto var : lista)
					resultado.add(this.convertidor.FacturaDetalleImpuestoToFacturaDetalleImpuestoExt(var));
			}
		} catch (Exception re) {	
			throw re;
		}
		
		return resultado;
	}

	@Override
	public List<FacturaDetalleImpuestoExt> findExtAll(String orderBy) throws Exception {
		List<FacturaDetalleImpuestoExt> resultado = new ArrayList<FacturaDetalleImpuestoExt>();
		
		try {
			List<FacturaDetalleImpuesto> lista = this.findAll(orderBy);
			if (lista != null && ! lista.isEmpty()) {
				for (FacturaDetalleImpuesto var : lista)
					resultado.add(this.convertidor.FacturaDetalleImpuestoToFacturaDetalleImpuestoExt(var));
			}
		} catch (Exception re) {	
			throw re;
		}
		
		return resultado;
	}

	@Override
	public List<FacturaDetalleImpuestoExt> findExtByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		List<FacturaDetalleImpuestoExt> resultado = new ArrayList<FacturaDetalleImpuestoExt>();
		
		try {
			List<FacturaDetalleImpuesto> lista = this.findByProperty(propertyName, value, orderBy, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (FacturaDetalleImpuesto var : lista)
					resultado.add(this.convertidor.FacturaDetalleImpuestoToFacturaDetalleImpuestoExt(var));
			}
		} catch (Exception re) {	
			throw re;
		}
		
		return resultado;
	}

	@Override
	public List<FacturaDetalleImpuestoExt> findExtLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception {
		List<FacturaDetalleImpuestoExt> resultado = new ArrayList<FacturaDetalleImpuestoExt>();
		
		try {
			List<FacturaDetalleImpuesto> lista = this.findLikeProperty(propertyName, value, orderBy, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (FacturaDetalleImpuesto var : lista)
					resultado.add(this.convertidor.FacturaDetalleImpuestoToFacturaDetalleImpuestoExt(var));
			}
		} catch (Exception re) {	
			throw re;
		}
		
		return resultado;
	}

	@Override
	public List<FacturaDetalleImpuestoExt> findExtByFacturaDetalle(Long idFacturaDetalle) throws Exception {
		List<FacturaDetalleImpuestoExt> resultado = new ArrayList<FacturaDetalleImpuestoExt>();
		
		try {
			List<FacturaDetalleImpuesto> lista = this.findByFacturaDetalle(idFacturaDetalle);
			if (lista != null && ! lista.isEmpty()) {
				for (FacturaDetalleImpuesto var : lista)
					resultado.add(this.convertidor.FacturaDetalleImpuestoToFacturaDetalleImpuestoExt(var));
			}
		} catch (Exception re) {	
			throw re;
		}
		
		return resultado;
	}

	@Override
	public List<FacturaDetalleImpuestoExt> findExtByFacturaDetalle(FacturaDetalle idFacturaDetalle) throws Exception {
		List<FacturaDetalleImpuestoExt> resultado = new ArrayList<FacturaDetalleImpuestoExt>();
		
		try {
			List<FacturaDetalleImpuesto> lista = this.findByFacturaDetalle(idFacturaDetalle);
			if (lista != null && ! lista.isEmpty()) {
				for (FacturaDetalleImpuesto var : lista)
					resultado.add(this.convertidor.FacturaDetalleImpuestoToFacturaDetalleImpuestoExt(var));
			}
		} catch (Exception re) {	
			throw re;
		}
		
		return resultado;
	}

	@Override
	public List<FacturaDetalleImpuestoExt> findExtByFacturaDetalle(FacturaDetalleExt idFacturaDetalle) throws Exception {
		List<FacturaDetalleImpuestoExt> resultado = new ArrayList<FacturaDetalleImpuestoExt>();
		
		try {
			List<FacturaDetalleImpuesto> lista = this.findByFacturaDetalle(idFacturaDetalle);
			if (lista != null && ! lista.isEmpty()) {
				for (FacturaDetalleImpuesto var : lista)
					resultado.add(this.convertidor.FacturaDetalleImpuestoToFacturaDetalleImpuestoExt(var));
			}
		} catch (Exception re) {	
			throw re;
		}
		
		return resultado;
	}

	// -----------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}
