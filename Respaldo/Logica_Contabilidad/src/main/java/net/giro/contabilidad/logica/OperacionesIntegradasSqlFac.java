package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.OperacionesIntegradasSql;
import net.giro.contabilidad.dao.OperacionesIntegradasSqlDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class OperacionesIntegradasSqlFac implements OperacionesIntegradasSqlRem {
	private static Logger log = Logger.getLogger(OperacionesIntegradasSqlFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private OperacionesIntegradasSqlDAO ifzOperacionesIntegradasSqls;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public OperacionesIntegradasSqlFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzOperacionesIntegradasSqls = (OperacionesIntegradasSqlDAO) this.ctx.lookup("ejb:/Model_Contabilidad//OperacionesIntegradasSqlImp!net.giro.contabilidad.dao.OperacionesIntegradasSqlDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("OperacionesIntegradasSqlFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.OperacionesIntegradasSqlFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { /*this.convertidor.setMostrarSystemOut(value);*/ }

	@Override
	public void orderBy(String orderBy) { OperacionesIntegradasSqlFac.orderBy = orderBy; }

	@Override
	public Long save(OperacionesIntegradasSql entity) throws ExcepConstraint {
		try {
			return this.ifzOperacionesIntegradasSqls.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.save(OperacionesIntegradasSql)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(OperacionesIntegradasSqlExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.OperacionesIntegradasSqlExtToOperacionesIntegradasSql(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.save(OperacionesIntegradasSqlExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(OperacionesIntegradasSql entity) throws ExcepConstraint {
		try {
			this.ifzOperacionesIntegradasSqls.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.update(OperacionesIntegradasSql)", e);
			throw e;
		}
	}

	/*@Override
	public void update(OperacionesIntegradasSqlExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.OperacionesIntegradasSqlExtToOperacionesIntegradasSql(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.update(OperacionesIntegradasSqlExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzOperacionesIntegradasSqls.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public OperacionesIntegradasSql findById(Long id) {
		try {
			return this.ifzOperacionesIntegradasSqls.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public OperacionesIntegradasSqlExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.OperacionesIntegradasSqlToOperacionesIntegradasSqlExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<OperacionesIntegradasSql> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOperacionesIntegradasSqls.orderBy(orderBy);
			return this.ifzOperacionesIntegradasSqls.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<OperacionesIntegradasSqlExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<OperacionesIntegradasSqlExt> listaExt = new ArrayList<OperacionesIntegradasSqlExt>();
		
		try {
			List<OperacionesIntegradasSql> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(OperacionesIntegradasSql var : lista) {
					listaExt.add(this.convertidor.OperacionesIntegradasSqlToOperacionesIntegradasSqlExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasSql> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOperacionesIntegradasSqls.orderBy(orderBy);
			return this.ifzOperacionesIntegradasSqls.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<OperacionesIntegradasSqlExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<OperacionesIntegradasSqlExt> listaExt = new ArrayList<OperacionesIntegradasSqlExt>();
		
		try {
			List<OperacionesIntegradasSql> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(OperacionesIntegradasSql var : lista) {
					listaExt.add(this.convertidor.OperacionesIntegradasSqlToOperacionesIntegradasSqlExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasSql> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzOperacionesIntegradasSqls.orderBy(orderBy);
			return this.ifzOperacionesIntegradasSqls.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<OperacionesIntegradasSqlExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<OperacionesIntegradasSqlExt> listaExt = new ArrayList<OperacionesIntegradasSqlExt>();
		
		try {
			List<OperacionesIntegradasSql> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(OperacionesIntegradasSql var : lista) {
					listaExt.add(this.convertidor.OperacionesIntegradasSqlToOperacionesIntegradasSqlExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasSql> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOperacionesIntegradasSqls.orderBy(orderBy);
			return this.ifzOperacionesIntegradasSqls.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<OperacionesIntegradasSqlExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<OperacionesIntegradasSqlExt> listaExt = new ArrayList<OperacionesIntegradasSqlExt>();
		
		try {
			List<OperacionesIntegradasSql> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(OperacionesIntegradasSql var : lista) {
					listaExt.add(this.convertidor.OperacionesIntegradasSqlToOperacionesIntegradasSqlExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasSql> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOperacionesIntegradasSqls.orderBy(orderBy);
			return this.ifzOperacionesIntegradasSqls.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<OperacionesIntegradasSqlExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<OperacionesIntegradasSqlExt> listaExt = new ArrayList<OperacionesIntegradasSqlExt>();
		
		try {
			List<OperacionesIntegradasSql> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(OperacionesIntegradasSql var : lista) {
					listaExt.add(this.convertidor.OperacionesIntegradasSqlToOperacionesIntegradasSqlExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasSqlFac