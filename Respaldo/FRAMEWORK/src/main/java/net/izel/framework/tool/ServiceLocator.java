 
package net.izel.framework.tool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

 
public class ServiceLocator {

    private InitialContext m_ic;
    private Map<String, Object> m_cache;
    private static ServiceLocator m_locator;


    public ServiceLocator() throws NamingException {   	

        
			ResourceBundle bundle= ResourceBundle.getBundle("config");
		
        
        Hashtable<String,Object> enviroment=new Hashtable<String, Object>();
        enviroment.put("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
        enviroment.put("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
        enviroment.put("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
              
        enviroment.put("org.omg.CORBA.ORBInitialHost", bundle.getString("sgci.ip"));
        enviroment.put("org.omg.CORBA.ORBInitialPort", bundle.getString("sgci.port"));

        m_ic = new InitialContext( enviroment );
        m_cache = Collections.synchronizedMap(new HashMap<String, Object>());
    }

    public static ServiceLocator getInstance() throws NamingException {
        if( m_locator==null )
        {
           m_locator = new ServiceLocator();
        }
        return m_locator;
    }

    private Object lookup(String jndiName) throws NamingException {
        Object cachedObj = m_cache.get(jndiName);
        
        if (cachedObj == null) {
            cachedObj = m_ic.lookup(jndiName);
            m_cache.put(jndiName, cachedObj);
        }
        return cachedObj;
    }

    public Object getRemoteHome(String jndiHomeName) throws NamingException {
        Object objref = lookup(jndiHomeName);
        return objref;
    }

    public DataSource getDataSource(String dataSourceName) throws NamingException {
        return (DataSource) lookup(dataSourceName);
    }
    
    
    public InitialContext getM_ic() {
		return m_ic;
	}

	public void setM_ic(InitialContext m_ic) {
		this.m_ic = m_ic;
	}
    
}
