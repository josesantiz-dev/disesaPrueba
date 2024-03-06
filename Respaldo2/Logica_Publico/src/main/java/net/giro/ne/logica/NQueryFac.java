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
		try {
	    	Hashtable<String, Object> p = new Hashtable<String, Object>();
	        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");            
	        ctx = new InitialContext(p);
	        
			this.ifzNQuery = (NQueryDAO) ctx.lookup("ejb:/Model_Publico//NQueryImp!net.giro.ne.dao.NQueryDAO");
		} catch (Exception e) {
			log.error("error en constructor Logica_Publico.NQueryFac", e);
			throw e;
		}
	}

	public NQuery findByProperty( String sql ) {
		try {
			log.info(sql);
			return this.ifzNQuery.findByProperty(sql);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("rawtypes")
	public List findNativeQuery(String SrtQuery){
		try {
			return this.ifzNQuery.findNativeQuery(SrtQuery);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List findJPAQuery(String SrtQuery, HashMap<String, Object> parametros){
		try {
			return this.ifzNQuery.findJPAQuery(SrtQuery, parametros);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void ejecutaAccion(String strSentencia){
		try {
			this.ifzNQuery.ejecutaAccion(strSentencia);
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
