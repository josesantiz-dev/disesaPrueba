package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;
import net.giro.contabilidad.dao.OperacionesIntegradasConceptosSqlDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class OperacionesIntegradasConceptosSqlFac implements OperacionesIntegradasConceptosSqlRem {
	private static Logger log = Logger.getLogger(OperacionesIntegradasConceptosSqlFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private OperacionesIntegradasConceptosSqlDAO ifzOperInterConceptosSQL;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public OperacionesIntegradasConceptosSqlFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzOperInterConceptosSQL = (OperacionesIntegradasConceptosSqlDAO) this.ctx.lookup("ejb:/Model_Contabilidad//OperacionesIntegradasConceptosSqlImp!net.giro.contabilidad.dao.OperacionesIntegradasConceptosSqlDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("OperacionesIntegradasConceptosSqlFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { /*this.convertidor.setMostrarSystemOut(value);*/ }

	@Override
	public void orderBy(String orderBy) { OperacionesIntegradasConceptosSqlFac.orderBy = orderBy; }

	@Override
	public Long save(OperacionesIntegradasConceptosSql entity) throws ExcepConstraint {
		try {
			return this.ifzOperInterConceptosSQL.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.save(TransaccionConceptosSql)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(TransaccionConceptosSqlExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.TransaccionConceptosSqlExtToTransaccionConceptosSql(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.save(TransaccionConceptosSqlExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(OperacionesIntegradasConceptosSql entity) throws ExcepConstraint {
		try {
			this.ifzOperInterConceptosSQL.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.update(TransaccionConceptosSql)", e);
			throw e;
		}
	}

	/*@Override
	public void update(TransaccionConceptosSqlExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.TransaccionConceptosSqlExtToTransaccionConceptosSql(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.update(TransaccionConceptosSqlExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzOperInterConceptosSQL.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public OperacionesIntegradasConceptosSql findById(Long id) {
		try {
			return this.ifzOperInterConceptosSQL.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public TransaccionConceptosSqlExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.TransaccionConceptosSqlToTransaccionConceptosSqlExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<OperacionesIntegradasConceptosSql> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOperInterConceptosSQL.orderBy(orderBy);
			return this.ifzOperInterConceptosSQL.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionConceptosSqlExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<TransaccionConceptosSqlExt> listaExt = new ArrayList<TransaccionConceptosSqlExt>();
		
		try {
			List<TransaccionConceptosSql> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(TransaccionConceptosSql var : lista) {
					listaExt.add(this.convertidor.TransaccionConceptosSqlToTransaccionConceptosSqlExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasConceptosSql> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOperInterConceptosSQL.orderBy(orderBy);
			return this.ifzOperInterConceptosSQL.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionConceptosSqlExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<TransaccionConceptosSqlExt> listaExt = new ArrayList<TransaccionConceptosSqlExt>();
		
		try {
			List<TransaccionConceptosSql> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(TransaccionConceptosSql var : lista) {
					listaExt.add(this.convertidor.TransaccionConceptosSqlToTransaccionConceptosSqlExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasConceptosSql> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzOperInterConceptosSQL.orderBy(orderBy);
			return this.ifzOperInterConceptosSQL.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionConceptosSqlExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<TransaccionConceptosSqlExt> listaExt = new ArrayList<TransaccionConceptosSqlExt>();
		
		try {
			List<TransaccionConceptosSql> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(TransaccionConceptosSql var : lista) {
					listaExt.add(this.convertidor.TransaccionConceptosSqlToTransaccionConceptosSqlExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasConceptosSql> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOperInterConceptosSQL.orderBy(orderBy);
			return this.ifzOperInterConceptosSQL.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionConceptosSqlExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<TransaccionConceptosSqlExt> listaExt = new ArrayList<TransaccionConceptosSqlExt>();
		
		try {
			List<TransaccionConceptosSql> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(TransaccionConceptosSql var : lista) {
					listaExt.add(this.convertidor.TransaccionConceptosSqlToTransaccionConceptosSqlExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasConceptosSql> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOperInterConceptosSQL.orderBy(orderBy);
			return this.ifzOperInterConceptosSQL.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionConceptosSqlExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<TransaccionConceptosSqlExt> listaExt = new ArrayList<TransaccionConceptosSqlExt>();
		
		try {
			List<TransaccionConceptosSql> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(TransaccionConceptosSql var : lista) {
					listaExt.add(this.convertidor.TransaccionConceptosSqlToTransaccionConceptosSqlExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasConceptosSqlFac