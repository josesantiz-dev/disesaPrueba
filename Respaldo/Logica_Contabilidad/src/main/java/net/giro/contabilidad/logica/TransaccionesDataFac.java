package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.TransaccionesData;
import net.giro.contabilidad.dao.TransaccionesDataDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class TransaccionesDataFac implements TransaccionesDataRem {
	private static Logger log = Logger.getLogger(TransaccionesDataFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private TransaccionesDataDAO ifzTData;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public TransaccionesDataFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzTData = (TransaccionesDataDAO) this.ctx.lookup("ejb:/Model_Contabilidad//TransaccionesDataImp!net.giro.contabilidad.dao.TransaccionesDataDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("TransaccionesDataFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.TransaccionesDataFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		//this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void orderBy(String orderBy) {
		TransaccionesDataFac.orderBy = orderBy;
	}

	@Override
	public Long save(TransaccionesData entity) throws ExcepConstraint {
		try {
			return this.ifzTData.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.save(TransaccionesData)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(TransaccionesDataExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.TransaccionesDataExtToTransaccionesData(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.save(TransaccionesDataExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(TransaccionesData entity) throws ExcepConstraint {
		try {
			this.ifzTData.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.update(TransaccionesData)", e);
			throw e;
		}
	}

	/*@Override
	public void update(TransaccionesDataExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.TransaccionesDataExtToTransaccionesData(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.update(TransaccionesDataExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzTData.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public TransaccionesData findById(Long id) {
		try {
			return this.ifzTData.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public TransaccionesDataExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.TransaccionesDataToTransaccionesDataExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<TransaccionesData> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTData.orderBy(orderBy);
			return this.ifzTData.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionesDataExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<TransaccionesDataExt> listaExt = new ArrayList<TransaccionesDataExt>();
		
		try {
			List<TransaccionesData> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(TransaccionesData var : lista) {
					listaExt.add(this.convertidor.TransaccionesDataToTransaccionesDataExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionesData> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTData.orderBy(orderBy);
			return this.ifzTData.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionesDataExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<TransaccionesDataExt> listaExt = new ArrayList<TransaccionesDataExt>();
		
		try {
			List<TransaccionesData> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(TransaccionesData var : lista) {
					listaExt.add(this.convertidor.TransaccionesDataToTransaccionesDataExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionesData> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzTData.orderBy(orderBy);
			return this.ifzTData.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionesDataExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<TransaccionesDataExt> listaExt = new ArrayList<TransaccionesDataExt>();
		
		try {
			List<TransaccionesData> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(TransaccionesData var : lista) {
					listaExt.add(this.convertidor.TransaccionesDataToTransaccionesDataExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionesData> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTData.orderBy(orderBy);
			return this.ifzTData.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionesDataExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<TransaccionesDataExt> listaExt = new ArrayList<TransaccionesDataExt>();
		
		try {
			List<TransaccionesData> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(TransaccionesData var : lista) {
					listaExt.add(this.convertidor.TransaccionesDataToTransaccionesDataExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionesData> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTData.orderBy(orderBy);
			return this.ifzTData.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionesDataExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<TransaccionesDataExt> listaExt = new ArrayList<TransaccionesDataExt>();
		
		try {
			List<TransaccionesData> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(TransaccionesData var : lista) {
					listaExt.add(this.convertidor.TransaccionesDataToTransaccionesDataExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/
}