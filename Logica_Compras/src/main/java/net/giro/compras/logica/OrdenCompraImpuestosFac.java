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
	private InfoSesion infoSesion;
	private ConvertExt convertidor;
	private OrdenCompraImpuestosDAO ifzOrdenCompraImpuestos;
	
	public OrdenCompraImpuestosFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzOrdenCompraImpuestos = (OrdenCompraImpuestosDAO) ctx.lookup("ejb:/Model_Compras//OrdenCompraImpuestosImp!net.giro.compras.dao.OrdenCompraImpuestosDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("OrdenCompraImpuestosFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.OrdenCompraImpuestosFac", e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(OrdenCompraImpuestos entity) throws Exception {
		try {
			return this.ifzOrdenCompraImpuestos.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.save(OrdenCompraImpuestos entity)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraImpuestos> saveOrUpdateList(List<OrdenCompraImpuestos> entities) throws Exception {
		try {
			return this.ifzOrdenCompraImpuestos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraImpuestosFac.saveOrUpdateList(List<OrdenCompraImpuestos> entities)", e);
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
	public void delete(long idOrdenCompraImpuestos) throws Exception {
		try {
			this.ifzOrdenCompraImpuestos.delete(idOrdenCompraImpuestos);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.delete(idOrdenCompraImpuestos)", e);
			throw e;
		}
	}
	
	@Override
	public OrdenCompraImpuestos findById(long idOrdenCompraImpuestos) throws Exception {
		try {
			return this.ifzOrdenCompraImpuestos.findById(idOrdenCompraImpuestos);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.findById(idOrdenCompraImpuestos)", e);
			throw e;
		}
	}

	public List<OrdenCompraImpuestos> findAll(long idOrdenCompra) throws Exception {
		try {
			return this.ifzOrdenCompraImpuestos.findAll(idOrdenCompra);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.findAll(idOrdenCompra)", e);
			throw e;
		}
	}
	
	/*@Override
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
	}*/

	// -----------------------------------------------------------------------------------------------------------------------
	// CONVERTIDOR 
	// -----------------------------------------------------------------------------------------------------------------------
	
	@Override
	public OrdenCompraImpuestos convertir(OrdenCompraImpuestosExt extendido) throws Exception {
		try {
			return this.convertidor.OrdenCompraImpuestosExtToOrdenCompraImpuestos(extendido);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.convertir(extendido)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraImpuestosExt convertir(OrdenCompraImpuestos entity) throws Exception {
		try {
			return this.convertidor.OrdenCompraImpuestosToOrdenCompraImpuestosExt(entity);
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.convertir(entity)", e);
			throw e;
		}
	}
	
	// -----------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// -----------------------------------------------------------------------------------------------------------------------

	@Override
	public Long save(OrdenCompraImpuestosExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.OrdenCompraImpuestosExtToOrdenCompraImpuestos(extendido));
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.save(extendido)", e);
			throw e;
		}
	}
	
	@Override
	public List<OrdenCompraImpuestosExt> saveOrUpdateListExt(List<OrdenCompraImpuestosExt> extendidos) throws Exception {
		List<OrdenCompraImpuestos> entities = new ArrayList<OrdenCompraImpuestos>();
		
		try {
			for (OrdenCompraImpuestosExt extendido : extendidos)
				entities.add(this.convertidor.OrdenCompraImpuestosExtToOrdenCompraImpuestos(extendido));
			entities = this.saveOrUpdateList(entities);
			extendidos.clear();
			for (OrdenCompraImpuestos entity : entities)
				extendidos.add(this.convertidor.OrdenCompraImpuestosToOrdenCompraImpuestosExt(entity));
		} catch (Exception e) {
			log.error("Error en CotizacionFac.saveOrUpdateListExt(extendidos)", e);
			throw e;
		} 
		
		return extendidos;
	}
	
	@Override
	public void update(OrdenCompraImpuestosExt extendido) throws Exception {
		try {
			this.update(this.convertidor.OrdenCompraImpuestosExtToOrdenCompraImpuestos(extendido));
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.update(extendido)", e);
			throw e;
		}
	}
	
	@Override
	public OrdenCompraImpuestosExt findExtById(long idOrdenCompraImpuestos) throws Exception {
		try {
			return this.convertidor.OrdenCompraImpuestosToOrdenCompraImpuestosExt(this.findById(idOrdenCompraImpuestos));
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.findExtById(idOrdenCompraImpuestos)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraImpuestosExt> findExtAll(long idOrdenCompra) throws Exception {
		List<OrdenCompraImpuestosExt> extendida = new ArrayList<OrdenCompraImpuestosExt>();
		List<OrdenCompraImpuestos> entities = null;

		try {
			entities = this.findAll(idOrdenCompra);
			if (entities != null && ! entities.isEmpty()) {
				for (OrdenCompraImpuestos var : entities)
					extendida.add(this.convertidor.OrdenCompraImpuestosToOrdenCompraImpuestosExt(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_Compras.OrdenCompraImpuestosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return extendida;
	}

	/*@Override
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
	}*/

	//------------------------------------------------------------------------------------------------------
	// PRIVADOS
	//------------------------------------------------------------------------------------------------------

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
