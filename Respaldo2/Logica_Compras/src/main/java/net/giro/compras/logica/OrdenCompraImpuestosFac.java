package net.giro.compras.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.compras.beans.OrdenCompraImpuestos;
import net.giro.compras.beans.OrdenCompraImpuestosExt;
import net.giro.compras.dao.OrdenCompraImpuestosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class OrdenCompraImpuestosFac implements OrdenCompraImpuestosRem {
	private static Logger log = Logger.getLogger(OrdenCompraImpuestosFac.class);
	private OrdenCompraImpuestosDAO ifzOrdenCompraImpuestos;
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ConvertExt convertidor;

	
	public OrdenCompraImpuestosFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzOrdenCompraImpuestos = (OrdenCompraImpuestosDAO) this.ctx.lookup("ejb:/Model_Compras//OrdenCompraImpuestosImp!net.giro.compras.dao.OrdenCompraImpuestosDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("OrdenCompraImpuestosFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.OrdenCompraImpuestosFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(OrdenCompraImpuestos entity) throws Exception {
		try {
			return this.ifzOrdenCompraImpuestos.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.save(OrdenCompraImpuestos entity)", e);
			throw e;
		}
	}

	@Override
	public void update(OrdenCompraImpuestos entity) throws Exception {
		try {
			this.ifzOrdenCompraImpuestos.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.update(OrdenCompraImpuestos entity)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraImpuestos> saveOrUpdateList(List<OrdenCompraImpuestos> entities) throws Exception {
		try {
			return this.ifzOrdenCompraImpuestos.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraImpuestosFac.saveOrUpdateList(List<OrdenCompraImpuestos> entities)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws Exception {
		try {
			this.ifzOrdenCompraImpuestos.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.delete(Long entityId)", e);
			throw e;
		}
	}
	
	@Override
	public OrdenCompraImpuestos findById(long entityId) throws Exception {
		try {
			return this.ifzOrdenCompraImpuestos.findById(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.findById(entityId)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraImpuestos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzOrdenCompraImpuestos.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraImpuestos> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		try {
			return this.ifzOrdenCompraImpuestos.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraImpuestos convertir(OrdenCompraImpuestosExt entityExt) throws Exception {
		try {
			return this.convertidor.OrdenCompraImpuestosExtToOrdenCompraImpuestos(entityExt);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.convertir(OrdenCompraImpuestosExt entityExt)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraImpuestosExt convertir(OrdenCompraImpuestos entity) throws Exception {
		try {
			return this.convertidor.OrdenCompraImpuestosToOrdenCompraImpuestosExt(entity);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.convertir(OrdenCompraImpuestos entity)", e);
			throw e;
		}
	}
	
	// -----------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// -----------------------------------------------------------------------------------------------------------------------

	@Override
	public Long save(OrdenCompraImpuestosExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.OrdenCompraImpuestosExtToOrdenCompraImpuestos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.save(OrdenCompraImpuestosExt entityExt)", e);
			throw e;
		}
	}

	@Override
	public void update(OrdenCompraImpuestosExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.OrdenCompraImpuestosExtToOrdenCompraImpuestos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.update(OrdenCompraImpuestosExt entityExt)", e);
			throw e;
		}
	}
	
	@Override
	public OrdenCompraImpuestosExt findExtById(long entityId) throws Exception {
		try {
			return this.convertidor.OrdenCompraImpuestosToOrdenCompraImpuestosExt(this.findById(entityId));
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraImpuestosExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<OrdenCompraImpuestosExt> listaExtendida = new ArrayList<OrdenCompraImpuestosExt>();
		List<OrdenCompraImpuestos> lista = new ArrayList<OrdenCompraImpuestos>();

		try {
			lista = this.findByProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (OrdenCompraImpuestos var : lista)
					listaExtendida.add(this.convertidor.OrdenCompraImpuestosToOrdenCompraImpuestosExt(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExtendida;
	}

	@Override
	public List<OrdenCompraImpuestosExt> findExtLikeProperty(String propertyName, String value, int limite) throws Exception {
		List<OrdenCompraImpuestosExt> listaExtendida = new ArrayList<OrdenCompraImpuestosExt>();
		List<OrdenCompraImpuestos> lista = new ArrayList<OrdenCompraImpuestos>();

		try {
			lista = this.findByProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (OrdenCompraImpuestos var : lista)
					listaExtendida.add(this.convertidor.OrdenCompraImpuestosToOrdenCompraImpuestosExt(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.findByProperty(propertyName, value, limite)", e);
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
