package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.TransaccionesDataDetalle;
import net.giro.contabilidad.dao.TransaccionesDataDetalleDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class TransaccionesDataDetalleFac implements TransaccionesDataDetalleRem {
	private static Logger log = Logger.getLogger(TransaccionesDataDetalleFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private TransaccionesDataDetalleDAO ifzTransaccionesDataDetalles;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public TransaccionesDataDetalleFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzTransaccionesDataDetalles = (TransaccionesDataDetalleDAO) this.ctx.lookup("ejb:/Model_Contabilidad//TransaccionesDataDetalleImp!net.giro.contabilidad.dao.TransaccionesDataDetalleDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("TransaccionesDataDetalleFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.TransaccionesDataDetalleFac", e);
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
		TransaccionesDataDetalleFac.orderBy = orderBy;
	}

	@Override
	public Long save(TransaccionesDataDetalle entity) throws ExcepConstraint {
		try {
			return this.ifzTransaccionesDataDetalles.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.save(TransaccionesDataDetalle)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(TransaccionesDataDetalleExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.TransaccionesDataDetalleExtToTransaccionesDataDetalle(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.save(TransaccionesDataDetalleExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(TransaccionesDataDetalle entity) throws ExcepConstraint {
		try {
			this.ifzTransaccionesDataDetalles.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.update(TransaccionesDataDetalle)", e);
			throw e;
		}
	}

	/*@Override
	public void update(TransaccionesDataDetalleExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.TransaccionesDataDetalleExtToTransaccionesDataDetalle(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.update(TransaccionesDataDetalleExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzTransaccionesDataDetalles.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public TransaccionesDataDetalle findById(Long id) {
		try {
			return this.ifzTransaccionesDataDetalles.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public TransaccionesDataDetalleExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.TransaccionesDataDetalleToTransaccionesDataDetalleExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<TransaccionesDataDetalle> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.orderBy(orderBy);
			return this.ifzTransaccionesDataDetalles.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionesDataDetalleExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<TransaccionesDataDetalleExt> listaExt = new ArrayList<TransaccionesDataDetalleExt>();
		
		try {
			List<TransaccionesDataDetalle> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(TransaccionesDataDetalle var : lista) {
					listaExt.add(this.convertidor.TransaccionesDataDetalleToTransaccionesDataDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionesDataDetalle> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.orderBy(orderBy);
			return this.ifzTransaccionesDataDetalles.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionesDataDetalleExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<TransaccionesDataDetalleExt> listaExt = new ArrayList<TransaccionesDataDetalleExt>();
		
		try {
			List<TransaccionesDataDetalle> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(TransaccionesDataDetalle var : lista) {
					listaExt.add(this.convertidor.TransaccionesDataDetalleToTransaccionesDataDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionesDataDetalle> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.orderBy(orderBy);
			return this.ifzTransaccionesDataDetalles.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionesDataDetalleExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<TransaccionesDataDetalleExt> listaExt = new ArrayList<TransaccionesDataDetalleExt>();
		
		try {
			List<TransaccionesDataDetalle> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(TransaccionesDataDetalle var : lista) {
					listaExt.add(this.convertidor.TransaccionesDataDetalleToTransaccionesDataDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionesDataDetalle> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.orderBy(orderBy);
			return this.ifzTransaccionesDataDetalles.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionesDataDetalleExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<TransaccionesDataDetalleExt> listaExt = new ArrayList<TransaccionesDataDetalleExt>();
		
		try {
			List<TransaccionesDataDetalle> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(TransaccionesDataDetalle var : lista) {
					listaExt.add(this.convertidor.TransaccionesDataDetalleToTransaccionesDataDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<TransaccionesDataDetalle> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.orderBy(orderBy);
			return this.ifzTransaccionesDataDetalles.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionesDataDetalleExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<TransaccionesDataDetalleExt> listaExt = new ArrayList<TransaccionesDataDetalleExt>();
		
		try {
			List<TransaccionesDataDetalle> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(TransaccionesDataDetalle var : lista) {
					listaExt.add(this.convertidor.TransaccionesDataDetalleToTransaccionesDataDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/
}