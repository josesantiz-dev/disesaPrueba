package net.giro.ne.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.ne.beans.NQuery;
import net.giro.ne.dao.NQueryDAO;

import org.apache.log4j.Logger;

@Stateless
public class NQueryFac implements NQueryRem {
 	private static Logger  log = Logger.getLogger(NQueryFac.class);
	private InitialContext ctx = null;
	private NQueryDAO ifzNQuery;
	
	public NQueryFac() throws Exception {
    	Hashtable<String, Object> p = new Hashtable<String, Object>();
        
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");            
	        this.ctx = new InitialContext(p);
			this.ifzNQuery = (NQueryDAO) this.ctx.lookup("ejb:/Model_Publico//NQueryImp!net.giro.ne.dao.NQueryDAO");
		} catch (Exception e) {
			log.error("error en constructor Logica_Publico.NQueryFac", e);
			throw e;
		}
	}
	
	@Override
	public NQuery findByProperty(String sql) {
		try {
			log.info(sql);
			return this.ifzNQuery.findByProperty(sql);
		} catch (Exception re) {
			log.error("error en constructor Logica_Publico.NQueryFac.findByProperty", re);
			throw re;
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List findNativeQuery(String SrtQuery) {
		try {
			return this.ifzNQuery.findNativeQuery(SrtQuery);
		} catch (Exception re) {
			log.error("error en constructor Logica_Publico.NQueryFac.findNativeQuery", re);
			throw re;
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List findJPAQuery(String SrtQuery, HashMap<String, Object> parametros){
		try {
			return this.ifzNQuery.findJPAQuery(SrtQuery, parametros);
		} catch (Exception re) {
			log.error("error en constructor Logica_Publico.NQueryFac.findJPAQuery", re);
			throw re;
		}
	}

	@Override
	public void ejecutaAccion(String strSentencia){
		try {
			this.ifzNQuery.ejecutaAccion(strSentencia);
		} catch (Exception re) {
			log.error("error en constructor Logica_Publico.NQueryFac.ejecutaAccion", re);
			throw re;
		}
	}
}
