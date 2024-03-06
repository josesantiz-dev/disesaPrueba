package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.TransaccionConceptos;
import net.giro.contabilidad.dao.TransaccionConceptosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class TransaccionConceptosFac implements TransaccionConceptosRem {
	private static Logger log = Logger.getLogger(TransaccionConceptosFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private TransaccionConceptosDAO ifzTransaccionConceptoss;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public TransaccionConceptosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzTransaccionConceptoss = (TransaccionConceptosDAO) this.ctx.lookup("ejb:/Model_Contabilidad//TransaccionConceptosImp!net.giro.contabilidad.dao.TransaccionConceptosDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("TransaccionConceptosFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.TransaccionConceptosFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { /*this.convertidor.setMostrarSystemOut(value);*/ }

	@Override
	public void orderBy(String orderBy) { TransaccionConceptosFac.orderBy = orderBy; }

	@Override
	public Long save(TransaccionConceptos entity) throws ExcepConstraint {
		try {
			return this.ifzTransaccionConceptoss.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.save(TransaccionConceptos)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(TransaccionConceptosExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.TransaccionConceptosExtToTransaccionConceptos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.save(TransaccionConceptosExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(TransaccionConceptos entity) throws ExcepConstraint {
		try {
			this.ifzTransaccionConceptoss.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.update(TransaccionConceptos)", e);
			throw e;
		}
	}

	/*@Override
	public void update(TransaccionConceptosExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.TransaccionConceptosExtToTransaccionConceptos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.update(TransaccionConceptosExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzTransaccionConceptoss.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public TransaccionConceptos findById(Long id) {
		try {
			return this.ifzTransaccionConceptoss.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public TransaccionConceptosExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.TransaccionConceptosToTransaccionConceptosExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<TransaccionConceptos> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);
			return this.ifzTransaccionConceptoss.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionConceptosExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<TransaccionConceptosExt> listaExt = new ArrayList<TransaccionConceptosExt>();
		
		try {
			List<TransaccionConceptos> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(TransaccionConceptos var : lista) {
					listaExt.add(this.convertidor.TransaccionConceptosToTransaccionConceptosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionConceptos> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);
			return this.ifzTransaccionConceptoss.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionConceptosExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<TransaccionConceptosExt> listaExt = new ArrayList<TransaccionConceptosExt>();
		
		try {
			List<TransaccionConceptos> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(TransaccionConceptos var : lista) {
					listaExt.add(this.convertidor.TransaccionConceptosToTransaccionConceptosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionConceptos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);			
			return this.ifzTransaccionConceptoss.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionConceptosExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<TransaccionConceptosExt> listaExt = new ArrayList<TransaccionConceptosExt>();
		
		try {
			List<TransaccionConceptos> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(TransaccionConceptos var : lista) {
					listaExt.add(this.convertidor.TransaccionConceptosToTransaccionConceptosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionConceptos> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);
			return this.ifzTransaccionConceptoss.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionConceptosExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<TransaccionConceptosExt> listaExt = new ArrayList<TransaccionConceptosExt>();
		
		try {
			List<TransaccionConceptos> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(TransaccionConceptos var : lista) {
					listaExt.add(this.convertidor.TransaccionConceptosToTransaccionConceptosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionConceptos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);
			return this.ifzTransaccionConceptoss.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionConceptosExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<TransaccionConceptosExt> listaExt = new ArrayList<TransaccionConceptosExt>();
		
		try {
			List<TransaccionConceptos> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(TransaccionConceptos var : lista) {
					listaExt.add(this.convertidor.TransaccionConceptosToTransaccionConceptosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de TransaccionConceptosFac