package net.giro.adp.logica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.ObraAlmacenesExt;
import net.giro.adp.dao.ObraAlmacenesDAO;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraAlmacenesFac implements ObraAlmacenesRem {
	private static Logger log = Logger.getLogger(ObraAlmacenesFac.class);
	private InfoSesion infoSesion;
	private ObraAlmacenesDAO ifzObraAlmacenes;
	private NQueryRem ifzQuery;
	private ConvertExt convertidor;
	
	public ObraAlmacenesFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzObraAlmacenes = (ObraAlmacenesDAO) ctx.lookup("ejb:/Model_GestionProyectos//ObraAlmacenesImp!net.giro.adp.dao.ObraAlmacenesDAO");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ObraAlmacenesFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_GestionProyectos.ObraAlmacenesFac", e);
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
	public Long save(ObraAlmacenes entity) throws Exception {
		try {
			return this.ifzObraAlmacenes.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.save(ObraAlmacenes)", e);
			throw e;
		}
	}

	@Override
	public List<ObraAlmacenes> saveOrUpdateList(List<ObraAlmacenes> entities) throws Exception {
		try {
			return this.ifzObraAlmacenes.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.saveOrUpdateList(List<ObraAlmacenes> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(ObraAlmacenes entity) throws Exception {
		try {
			this.ifzObraAlmacenes.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.update(ObraAlmacenes)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idObraAlmacen) throws Exception {
		try {
			this.ifzObraAlmacenes.delete(idObraAlmacen);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.delete(idObraAlmacen)", e);
			throw e;
		}
	}

	@Override
	public ObraAlmacenes findById(Long idObraAlmacen) {
		try {
			return this.ifzObraAlmacenes.findById(idObraAlmacen);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findById(idObraAlmacen)", e);
			throw e;
		}
	}

	@Override
	public List<ObraAlmacenes> findAll(long idObra) throws Exception {
		try {
			return this.findAll(idObra, null);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findAll(idObra)", e);
			throw e;
		}
	}

	@Override
	public List<ObraAlmacenes> findAll(long idObra, String orderBy) throws Exception {
		try {
			return this.ifzObraAlmacenes.findAll(idObra, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findAll(idObra, orderBy)", e);
			throw e;
		}
	}

	@Override
	public List<ObraAlmacenes> findLike(String propertyValue, long idAlmacen, String orderBy, int limite) throws Exception {
		try {
			return this.ifzObraAlmacenes.findLike(propertyValue, idAlmacen, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findLike(propertyValue, idAlmacen, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<ObraAlmacenes> findLikeProperty(String propertyName, Object propertyValue, long idAlmacen, String orderBy, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(propertyValue.toString(), idAlmacen, orderBy, limite);
			return this.ifzObraAlmacenes.findLikeProperty(propertyName, propertyValue, idAlmacen, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findLikeProperty(propertyName, propertyValue, idAlmacen, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<ObraAlmacenes> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, null, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<ObraAlmacenes> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzObraAlmacenes.findByProperty(propertyName, value, 0L, getIdEmpresa(), null, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<ObraAlmacenes> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			return this.ifzObraAlmacenes.findByProperties(params, getIdEmpresa(), null, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findByProperties(params, limite)", e);
			throw e;
		} 
	}

	/*@Override
	public List<ObraAlmacenes> findInProperty(String propertyName, List<Object> values, int limite) throws Exception {
		try {
			return this.ifzObraAlmacenes.findInProperty(propertyName, values, getIdEmpresa(), null, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findInProperty(propertyName, values, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<ObraAlmacenes> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			return this.ifzObraAlmacenes.findByProperties(params, getIdEmpresa(), null, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findLikeProperties(params, limite)", e);
			throw e;
		} 
	}*/

	@Override
	public ObraAlmacenes findAlmacenPrincipal(long idBodega) {
		try {
			return almacenByBodega(idBodega);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findAlmacenPrincipal(long idObra, long idSucursal)", e);
			throw e;
		} 
	}

	@Override
	public ObraAlmacenes findAlmacenPrincipal(long idObra, long idSucursal) {
		try {
			return this.ifzObraAlmacenes.findAlmacenPrincipal(idObra, idSucursal, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findAlmacenPrincipal(long idObra, long idSucursal)", e);
			throw e;
		} 
	}

	@Override
	public ObraAlmacenes findBodega(long idObra) {
		try {
			return this.ifzObraAlmacenes.findBodega(idObra, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findAlmacenPrincipal(long idObra, long idSucursal)", e);
			throw e;
		} 
	}

	// --------------------------------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// --------------------------------------------------------------------------------------------------------------------
	
	@Override
	public ObraAlmacenes convertir(ObraAlmacenesExt extendido) throws Exception {
		return this.convertidor.getPojo(extendido);
	}

	@Override
	public ObraAlmacenesExt convertir(ObraAlmacenes entity) throws Exception {
		return this.convertidor.getExtendido(entity);
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------------------------
	
	@Override
	public Long save(ObraAlmacenesExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(extendido));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.save(extendido)", e);
			throw e;
		}
	}

	@Override
	public void update(ObraAlmacenesExt extendido) throws Exception {
		try {
			this.update(this.convertidor.getPojo(extendido));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.update(extendido)", e);
			throw e;
		}
	}

	@Override
	public ObraAlmacenesExt findExtById(Long idObraAlmacen) throws Exception {
		try {
			return this.convertidor.getExtendido(this.findById(idObraAlmacen));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtById(idObraAlmacen)", e);
			throw e;
		}
	}

	@Override
	public List<ObraAlmacenesExt> findExtAll(long idObra, String orderBy) throws Exception {
		List<ObraAlmacenesExt> extendidos = new ArrayList<ObraAlmacenesExt>();
		List<ObraAlmacenes> entities = null;
		
		try {
			entities = this.findAll(idObra, orderBy);
			if (entities == null || entities.isEmpty()) 
				return extendidos;
			for (ObraAlmacenes entity : entities) 
				extendidos.add(this.convertidor.getExtendido(entity));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtAll(idObra, orderBy)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<ObraAlmacenesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ObraAlmacenesExt> listaExt = new ArrayList<ObraAlmacenesExt>();
		List<ObraAlmacenes> lista = null;
		
		try {
			lista = this.findByProperties(params, limite);
			if (lista != null && ! lista.isEmpty()) {
				for(ObraAlmacenes var : lista) 
					listaExt.add(this.convertidor.getExtendido(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public ObraAlmacenesExt findExtAlmacenPrincipal(long idObra, long idSucursal) throws Exception {
		try {
			return this.convertidor.getExtendido(this.findAlmacenPrincipal(idObra, idSucursal));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtAlmacenPrincipal(long idObra, long idSucursal)", e);
			throw e;
		}
	}

	@Override
	public ObraAlmacenesExt findExtBodega(long idObra) throws Exception {
		try {
			return this.convertidor.getExtendido(this.findBodega(idObra));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtBodega(long idObra)", e);
			throw e;
		}
	}
	
	/*@Override
	public List<ObraAlmacenesExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<ObraAlmacenesExt> listaExt = new ArrayList<ObraAlmacenesExt>();
		List<ObraAlmacenes> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				for(ObraAlmacenes var : lista) {
					listaExt.add(this.convertidor.ObraAlmacenesToObraAlmacenesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ObraAlmacenesExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<ObraAlmacenesExt> listaExt = new ArrayList<ObraAlmacenesExt>();
		List<ObraAlmacenes> lista = null;
		
		try {
			lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				for(ObraAlmacenes var : lista) 
					listaExt.add(this.convertidor.ObraAlmacenesToObraAlmacenesExt(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ObraAlmacenesExt> findExtInProperty(String propertyName, List<Object> values, int limite) throws Exception {
		List<ObraAlmacenesExt> listaExt = new ArrayList<ObraAlmacenesExt>();
		List<ObraAlmacenes> lista = null;
		
		try {
			lista = this.findInProperty(propertyName, values, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (ObraAlmacenes var : lista) 
					listaExt.add(this.convertidor.ObraAlmacenesToObraAlmacenesExt(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtInProperty(propertyName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ObraAlmacenesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ObraAlmacenesExt> listaExt = new ArrayList<ObraAlmacenesExt>();
		List<ObraAlmacenes> lista = null;
		
		try {
			lista = this.findLikeProperties(params, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (ObraAlmacenes var : lista) 
					listaExt.add(this.convertidor.ObraAlmacenesToObraAlmacenesExt(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	// -----------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	private ObraAlmacenes almacenByBodega(Long idBodega) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long idResultado = 0;
		ObraAlmacenes resultado = null;
		
		try {
			if (idBodega == null || idBodega <= 0L)
				return null;
			queryString += "select max(id) as id from obra_almacenes ";
			queryString += "where id_obra in (select a.id_obra from obra_almacenes a inner join almacen b on b.id = a.id_almacen where a.id_almacen = :idBodega and b.id_empresa = :idEmpresa) and tipo = 1 and almacen_principal = 1 ";
			queryString += "group by id_almacen, tipo, almacen_principal ";
			queryString = queryString.replace(":idBodega", idBodega.toString());
			queryString = queryString.replace(":idEmpresa", getIdEmpresa().toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				idResultado = ((BigDecimal) rows.get(0)).longValue();
			if (idResultado > 0L)
				resultado = this.ifzObraAlmacenes.findById(idResultado);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el Almacen de la Bodega " + idBodega, e);
		}
		
		return resultado;
	}

	private Long getIdEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getId() : 1L);
	}
	
	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|19/05/2016		|Javier Tirado	|Creando el facade ObraAlmacenesFac