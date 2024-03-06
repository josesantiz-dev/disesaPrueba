package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.Grupos;
import net.giro.contabilidad.dao.GruposDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class GruposFac implements GruposRem {
	private static Logger log = Logger.getLogger(GruposFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private GruposDAO ifzGrupos;
	//private NQueryRem ifzQuery;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public GruposFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzGrupos = (GruposDAO) this.ctx.lookup("ejb:/Model_Contabilidad//GruposImp!net.giro.contabilidad.dao.GruposDAO");
			//this.ifzQuery = (NQueryRem) this.ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("GruposFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.GruposFac", e);
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
		GruposFac.orderBy = orderBy;
	}

	@Override
	public Long save(Grupos entity) throws ExcepConstraint {
		try {
			return this.ifzGrupos.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.save(Grupos)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(GruposExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.GruposExtToGrupos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.save(GruposExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(Grupos entity) throws ExcepConstraint {
		try {
			this.ifzGrupos.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.update(Grupos)", e);
			throw e;
		}
	}

	/*@Override
	public void update(GruposExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.GruposExtToGrupos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.update(GruposExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzGrupos.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public Grupos findById(Long id) {
		try {
			return this.ifzGrupos.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public GruposExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.GruposToGruposExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<Grupos> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzGrupos.orderBy(orderBy);
			return this.ifzGrupos.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<GruposExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<GruposExt> listaExt = new ArrayList<GruposExt>();
		
		try {
			List<Grupos> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(Grupos var : lista) {
					listaExt.add(this.convertidor.GruposToGruposExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Grupos> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzGrupos.orderBy(orderBy);
			return this.ifzGrupos.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<GruposExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<GruposExt> listaExt = new ArrayList<GruposExt>();
		
		try {
			List<Grupos> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(Grupos var : lista) {
					listaExt.add(this.convertidor.GruposToGruposExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Grupos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzGrupos.orderBy(orderBy);
			return this.ifzGrupos.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<GruposExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<GruposExt> listaExt = new ArrayList<GruposExt>();
		
		try {
			List<Grupos> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(Grupos var : lista) {
					listaExt.add(this.convertidor.GruposToGruposExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Grupos> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzGrupos.orderBy(orderBy);
			return this.ifzGrupos.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<GruposExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<GruposExt> listaExt = new ArrayList<GruposExt>();
		
		try {
			List<Grupos> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(Grupos var : lista) {
					listaExt.add(this.convertidor.GruposToGruposExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Grupos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzGrupos.orderBy(orderBy);
			return this.ifzGrupos.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	
	/*@Override
	public List<GruposExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<GruposExt> listaExt = new ArrayList<GruposExt>();
		
		try {
			List<Grupos> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(Grupos var : lista) {
					listaExt.add(this.convertidor.GruposToGruposExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/
	
	/*@Override
	public HashMap<Long, String> ejecutaQuery(String strQuery) throws Exception {
		try {
			HashMap<Long, String> items = new HashMap<Long, String>();
			
			@SuppressWarnings({ "rawtypes" })
			List lista = this.ifzQuery.findNativeQuery(strQuery);
			if (lista != null && ! lista.isEmpty()) {
				@SuppressWarnings("unchecked")
				Iterator<Object> it = lista.iterator();
				Long itemId = 0L;
				String itemDesc = "";
				while (it.hasNext()) {
					Object[] item = (Object[]) it.next();
					
					if (item[0].getClass() == java.math.BigDecimal.class)
						itemId = ((BigDecimal) item[0]).longValue();
					if (item[0].getClass() == java.lang.Long.class)
						itemId = (Long) item[0];
					
					itemDesc = item[1].toString();
					items.put(itemId, itemDesc);
				}
			}
			
			return items;
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.ejecutaQuery(params, limite)", e);
			throw e;
		}
	}*/
}