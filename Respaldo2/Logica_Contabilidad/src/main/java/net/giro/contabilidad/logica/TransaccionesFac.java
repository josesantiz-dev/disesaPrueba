package net.giro.contabilidad.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.beans.TransaccionesExt;
import net.giro.contabilidad.dao.TransaccionesDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class TransaccionesFac implements TransaccionesRem {
	private static Logger log = Logger.getLogger(TransaccionesFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private TransaccionesDAO ifzTransacciones;
	private ConvertExt convertidor;
	private static String orderBy;
	
	
	public TransaccionesFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzTransacciones = (TransaccionesDAO) this.ctx.lookup("ejb:/Model_Contabilidad//TransaccionesImp!net.giro.contabilidad.dao.TransaccionesDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("TransaccionesFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.TransaccionesFac", e);
			ctx = null;
		}
	}


	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
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
	public void orderBy(String orderBy) {
		TransaccionesFac.orderBy = orderBy;
	}

	@Override
	public Long save(Transacciones entity) throws Exception {
		try {
			return this.ifzTransacciones.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.save(Transacciones)", e);
			throw e;
		}
	}

	@Override
	public List<Transacciones> saveOrUpdateList(List<Transacciones> entities) throws Exception {
		try {
			return this.ifzTransacciones.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.saveOrUpdateList(List<Transacciones> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(Transacciones entity) throws Exception {
		try {
			this.ifzTransacciones.setEmpresa(getIdEmpresa());
			this.ifzTransacciones.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.update(Transacciones)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws Exception {
		try {
			this.ifzTransacciones.setEmpresa(getIdEmpresa());
			this.ifzTransacciones.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public Transacciones findById(Long id) {
		try {
			this.ifzTransacciones.setEmpresa(getIdEmpresa());
			return this.ifzTransacciones.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public Transacciones findByCodigo(Long codigoTransaccion) throws Exception {
		try {
			this.ifzTransacciones.setEmpresa(getIdEmpresa());
			return this.ifzTransacciones.findByCodigo(codigoTransaccion);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findByCodigo(codigoTransaccion)", e);
			throw e;
		}
	}

	@Override
	public TransaccionesExt findExtByCodigo(Long codigoTransaccion) throws Exception {
		try {
			this.ifzTransacciones.setEmpresa(getIdEmpresa());
			return this.convertidor.TransaccionesToTransaccionesExt(this.findByCodigo(codigoTransaccion));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findExtByCodigo(codigoTransaccion)", e);
			throw e;
		}
	}

	@Override
	public List<Transacciones> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTransacciones.setEmpresa(getIdEmpresa());
			this.ifzTransacciones.orderBy(orderBy);
			return this.ifzTransacciones.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Transacciones> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTransacciones.setEmpresa(getIdEmpresa());
			this.ifzTransacciones.orderBy(orderBy);
			return this.ifzTransacciones.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Transacciones> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTransacciones.setEmpresa(getIdEmpresa());
			this.ifzTransacciones.orderBy(orderBy);
			return this.ifzTransacciones.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Transacciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTransacciones.setEmpresa(getIdEmpresa());
			this.ifzTransacciones.orderBy(orderBy);
			return this.ifzTransacciones.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Transacciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzTransacciones.setEmpresa(getIdEmpresa());
			this.ifzTransacciones.orderBy(orderBy);
			return this.ifzTransacciones.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public boolean comprobarCodigo(Long idTransaccion, Long codigo) throws Exception {
		try {
			this.ifzTransacciones.setEmpresa(getIdEmpresa());
			return this.ifzTransacciones.comprobarCodigo(idTransaccion, codigo);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.comprobarCodigo(idTransaccion, codigo)", e);
			throw e;
		}
	}

	@Override
	public Long findIdByCodigo(Long codigoTransaccion) throws Exception {
		try {
			List<Transacciones> lista = this.findByProperty("codigo", codigoTransaccion, 0);
			if (lista == null || lista.isEmpty())
				return 0L;
			return lista.get(0).getId();
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findIdByCodigo(codigoTransaccion)", e);
			throw e;
		}
	}

	@Override
	public Transacciones convertir(TransaccionesExt entityExt) throws Exception {
		try {
			return this.convertidor.TransaccionesExtToTransacciones(entityExt);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.convertir(TransaccionesExt)", e);
			throw e;
		}
	}

	@Override
	public TransaccionesExt convertir(Transacciones entity) throws Exception {
		try {
			return this.convertidor.TransaccionesToTransaccionesExt(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.convertir(Transacciones)", e);
			throw e;
		}
	}

	@Override
	public Long save(TransaccionesExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.TransaccionesExtToTransacciones(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.save(TransaccionesExt)", e);
			throw e;
		}
	}

	@Override
	public void update(TransaccionesExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.TransaccionesExtToTransacciones(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.update(TransaccionesExt)", e);
			throw e;
		}
	}

	@Override
	public TransaccionesExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.TransaccionesToTransaccionesExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findExtById(id)", e);
			throw e;
		}
	}
	
	@Override
	public List<TransaccionesExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<TransaccionesExt> listaExt = new ArrayList<TransaccionesExt>();
		
		try {
			List<Transacciones> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(Transacciones var : lista) {
					listaExt.add(this.convertidor.TransaccionesToTransaccionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<TransaccionesExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<TransaccionesExt> listaExt = new ArrayList<TransaccionesExt>();
		
		try {
			List<Transacciones> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(Transacciones var : lista) {
					listaExt.add(this.convertidor.TransaccionesToTransaccionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<TransaccionesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<TransaccionesExt> listaExt = new ArrayList<TransaccionesExt>();
		
		try {
			List<Transacciones> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(Transacciones var : lista) {
					listaExt.add(this.convertidor.TransaccionesToTransaccionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<TransaccionesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<TransaccionesExt> listaExt = new ArrayList<TransaccionesExt>();
		
		try {
			List<Transacciones> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(Transacciones var : lista) {
					listaExt.add(this.convertidor.TransaccionesToTransaccionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<TransaccionesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<TransaccionesExt> listaExt = new ArrayList<TransaccionesExt>();
		List<Transacciones> lista = null;
		
		try {
			lista = this.findLikeProperties(params, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (Transacciones var : lista)
					listaExt.add(this.convertidor.TransaccionesToTransaccionesExt(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}
}