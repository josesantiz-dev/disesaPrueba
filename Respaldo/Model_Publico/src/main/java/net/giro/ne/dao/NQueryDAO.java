package net.giro.ne.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAO;
import net.giro.ne.beans.NQuery;

public interface NQueryDAO extends DAO<NQuery> {
	public NQuery findByProperty( String sql );
	
	@SuppressWarnings("rawtypes")
	public List findNativeQuery(String SrtQuery);
	
	@SuppressWarnings("rawtypes")
	public List findJPAQuery(String SrtQuery, HashMap<String, Object> parametros);
	
	public void ejecutaAccion(String strSentencia);
}
