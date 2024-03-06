package net.giro.compras.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.compras.beans.OrdenCompraDetalleImpuestos;
import net.giro.compras.beans.OrdenCompraDetalleImpuestosExt;
import net.giro.compras.dao.OrdenCompraDetalleImpuestosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class OrdenCompraDetalleImpuestosFac implements OrdenCompraDetalleImpuestosRem {
	private static Logger log = Logger.getLogger(OrdenCompraDetalleImpuestosFac.class);
	private OrdenCompraDetalleImpuestosDAO ifzOrdenCompraDetalleImpuestos;
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ConvertExt convertidor;

	
	public OrdenCompraDetalleImpuestosFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzOrdenCompraDetalleImpuestos = (OrdenCompraDetalleImpuestosDAO) this.ctx.lookup("ejb:/Model_Compras//OrdenCompraDetalleImpuestosImp!net.giro.compras.dao.OrdenCompraDetalleImpuestosDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("OrdenCompraDetalleImpuestosFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.OrdenCompraDetalleImpuestosFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(OrdenCompraDetalleImpuestos entity) throws Exception {
		try {
			return this.ifzOrdenCompraDetalleImpuestos.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.save(OrdenCompraDetalleImpuestos entity)", e);
			throw e;
		}
	}

	@Override
	public void update(OrdenCompraDetalleImpuestos entity) throws Exception {
		try {
			this.ifzOrdenCompraDetalleImpuestos.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.update(OrdenCompraDetalleImpuestos entity)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraDetalleImpuestos> saveOrUpdateList(List<OrdenCompraDetalleImpuestos> entities) throws Exception {
		try {
			return this.ifzOrdenCompraDetalleImpuestos.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraDetalleImpuestosFac.saveOrUpdateList(List<OrdenCompraDetalleImpuestos> entities)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws Exception {
		try {
			this.ifzOrdenCompraDetalleImpuestos.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.delete(Long entityId)", e);
			throw e;
		}
	}
	
	@Override
	public OrdenCompraDetalleImpuestos findById(long entityId) throws Exception {
		try {
			return this.ifzOrdenCompraDetalleImpuestos.findById(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.findById(entityId)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraDetalleImpuestos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzOrdenCompraDetalleImpuestos.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraDetalleImpuestos> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		try {
			return this.ifzOrdenCompraDetalleImpuestos.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraDetalleImpuestos convertir(OrdenCompraDetalleImpuestosExt entityExt) throws Exception {
		try {
			return this.convertidor.OrdenCompraDetalleImpuestosExtToOrdenCompraDetalleImpuestos(entityExt);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.convertir(OrdenCompraDetalleImpuestosExt entityExt)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraDetalleImpuestosExt convertir(OrdenCompraDetalleImpuestos entity) throws Exception {
		try {
			return this.convertidor.OrdenCompraDetalleImpuestosToOrdenCompraDetalleImpuestosExt(entity);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.convertir(OrdenCompraDetalleImpuestos entity)", e);
			throw e;
		}
	}
	
	// -----------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// -----------------------------------------------------------------------------------------------------------------------

	@Override
	public Long save(OrdenCompraDetalleImpuestosExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.OrdenCompraDetalleImpuestosExtToOrdenCompraDetalleImpuestos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.save(OrdenCompraDetalleImpuestosExt entityExt)", e);
			throw e;
		}
	}

	@Override
	public void update(OrdenCompraDetalleImpuestosExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.OrdenCompraDetalleImpuestosExtToOrdenCompraDetalleImpuestos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.update(OrdenCompraDetalleImpuestosExt entityExt)", e);
			throw e;
		}
	}
	
	@Override
	public OrdenCompraDetalleImpuestosExt findExtById(long entityId) throws Exception {
		try {
			return this.convertidor.OrdenCompraDetalleImpuestosToOrdenCompraDetalleImpuestosExt(this.findById(entityId));
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraDetalleImpuestosExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<OrdenCompraDetalleImpuestosExt> listaExtendida = new ArrayList<OrdenCompraDetalleImpuestosExt>();
		List<OrdenCompraDetalleImpuestos> lista = new ArrayList<OrdenCompraDetalleImpuestos>();

		try {
			lista = this.findByProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (OrdenCompraDetalleImpuestos var : lista)
					listaExtendida.add(this.convertidor.OrdenCompraDetalleImpuestosToOrdenCompraDetalleImpuestosExt(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExtendida;
	}

	@Override
	public List<OrdenCompraDetalleImpuestosExt> findExtLikeProperty(String propertyName, String value, int limite) throws Exception {
		List<OrdenCompraDetalleImpuestosExt> listaExtendida = new ArrayList<OrdenCompraDetalleImpuestosExt>();
		List<OrdenCompraDetalleImpuestos> lista = new ArrayList<OrdenCompraDetalleImpuestos>();

		try {
			lista = this.findByProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (OrdenCompraDetalleImpuestos var : lista)
					listaExtendida.add(this.convertidor.OrdenCompraDetalleImpuestosToOrdenCompraDetalleImpuestosExt(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraDetalleImpuestosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExtendida;
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
