package net.giro.contabilidad.beans;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.giro.ne.logica.NQueryRem;

public class EjecutaBean {
	private static Logger log = Logger.getLogger(EjecutaBean.class);
	private InitialContext ctx;
	private NQueryRem ifzQuery;
	private String campoId;
	private String campoDescripcion;
	
	
	public EjecutaBean() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzQuery = (NQueryRem) this.ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.EjecutaBean", e);
			ctx = null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public LinkedHashMap<Object, String> ejecutaBean(String pEjb, String pMetodo, String pClasesParametros, String pValoresParametros) throws Exception {
		LinkedHashMap<Object, String> listValores = new LinkedHashMap<Object, String>();
		List<Object> lst = null;
		Properties jndiProps = null;
		Context context = null;
		Class<?>[] clasesParametros = null;
		Class<?>[] clasesParametrosMetodo = null;
		Class<?> oc = null;
		Class<?> c = null;
		Object pr = null;
		Method m = null;
		Object[] valoresParametros = null;
		String[] aClasesParametros = null;
		String[] aValoresParametros = null;
		Method omi = null;
		Method omd = null;
		String metodo = "";
		String sClasesParametrosMetodo = "";
		Object item = null;
		Object itemId = 0L;
		String itemDesc = "";
		/*Long itemAux = 0L;
		String itemPrefix = "";*/
		int j = 0;
		
		try {
			aClasesParametros = pClasesParametros.split(",");
			aValoresParametros = pValoresParametros.split(",");
	
			if (aClasesParametros.length != aValoresParametros.length)
				throw new Exception("Numero de valores y numero de tipos esperados no coinciden");
	
			clasesParametros = new Class<?>[aClasesParametros.length];
			valoresParametros = new Object[aClasesParametros.length];
	
			for(int i = 0; i != aClasesParametros.length; i++) {
				clasesParametros[i] = null;
				if ("".equals(aClasesParametros[i])) 
					continue;
				
				clasesParametros[i] = Class.forName(aClasesParametros[i].trim());
				if (clasesParametros[i] == java.lang.Long.class) {
					valoresParametros[i] = new Long(Long.valueOf(aValoresParametros[i]));
				} else if (clasesParametros[i] == java.lang.Float.class) {
					valoresParametros[i] = new Float(Float.valueOf(aValoresParametros[i]));
				} else if (clasesParametros[i] == java.lang.Double.class) {
					valoresParametros[i] = new Double(Double.valueOf(aValoresParametros[i]));
				} else if (clasesParametros[i] == java.lang.Integer.class) {
					valoresParametros[i] = new Integer(Integer.valueOf(aValoresParametros[i]));
				} else {
					valoresParametros[i] = aValoresParametros[i];
				}
			}
	
			metodo = pMetodo.substring(0, pMetodo.indexOf("("));
			if (pMetodo.indexOf("()") == -1) {
				clasesParametrosMetodo = new Class<?>[aClasesParametros.length];
				j = 0;
				sClasesParametrosMetodo = pMetodo.substring(pMetodo.indexOf("(")+1,pMetodo.indexOf(")"));
				for(String clase : sClasesParametrosMetodo.split(",")) {
					clasesParametrosMetodo[j] = null;
					clasesParametrosMetodo[j++] = Class.forName(clase.trim());
				}
			}
			
			jndiProps = new Properties();
			jndiProps.load(this.getClass().getResourceAsStream("jboss.properties"));
			context = new InitialContext(jndiProps);
			pr = context.lookup(pEjb);
			c = pr.getClass();
			m = c.getMethod(metodo, clasesParametrosMetodo);
			
			if (valoresParametros.length == 1 && valoresParametros[0] == null)
				valoresParametros = null;
	
			lst = (List<Object>) m.invoke(pr, valoresParametros);
			log.info((lst == null ? 0 : lst.size()) + " Resultados");
			
			item = null;
			itemId = 0L;
			itemDesc = "";
			
			for(Object o : lst) {
				oc = o.getClass();
				
				// Campos ID
				if(this.campoId == null || "".equals(this.campoId))
					omi = oc.getMethod("getId");
				else
					omi = oc.getMethod(this.campoId);
				
				// Campo DESCRIPCION
				if (this.campoDescripcion == null || "".equals(this.campoDescripcion))
					omd = oc.getMethod("getDescripcion");
				else
					omd = oc.getMethod(this.campoDescripcion);
				
				item = omi.invoke(o);
				if (item.getClass() == java.math.BigDecimal.class)
					itemId = ((BigDecimal) item).longValue();
				if (item.getClass() == java.lang.Long.class)
					itemId = (Long) item;

				itemDesc = omd.invoke(o).toString();
				itemDesc += getValorExtra(o);

				listValores.put(itemId, itemDesc);
			}
			
			return listValores;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public LinkedHashMap<Object, String> ejecutaQuery(String strQuery) throws Exception {
		LinkedHashMap<Object, String> listValores = new LinkedHashMap<Object, String>();
		Iterator<Object> it = null;
		@SuppressWarnings("rawtypes")
		List lista = null; 
		Object[] item = null;
		Long itemId = 0L;
		String itemDesc = "";
		
		try {
			lista = this.ifzQuery.findNativeQuery(strQuery);
			if (lista != null && ! lista.isEmpty()) {
				it = lista.iterator();
				while (it.hasNext()) {
					item = (Object[]) it.next();
					
					if (item[0].getClass() == java.math.BigDecimal.class)
						itemId = ((BigDecimal) item[0]).longValue();
					if (item[0].getClass() == java.lang.Long.class)
						itemId = (Long) item[0];
					
					itemDesc = item[1].toString();
					listValores.put(itemId, itemDesc);
				}
			}
			
			return listValores;
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposFac.ejecutaBean(pEjb, pMetodo, pClasesParametros, pValoresParametros)");
			throw e;
		}
	}
	
	public JsonArray ejecutaBeanToJSON(String pEjb, String pMetodo, String pClasesParametros, String pValoresParametros) throws Exception {
		JsonArray listValores = new JsonArray();
		JsonObject json = null;
		//LinkedHashMap<Object, String> listValores = new LinkedHashMap<Object, String>();
		Properties jndiProps = null;
		Context context = null;
		Class<?>[] clasesParametros = null;
		Class<?>[] clasesParametrosMetodo = null;
		Class<?> oc = null;
		Class<?> c = null;
		Object pr = null;
		Method m = null;
		Object[] valoresParametros = null;
		String[] aClasesParametros = null;
		String[] aValoresParametros = null;
		Method omi = null;
		Method omd = null;
		String metodo = "";
		String sClasesParametrosMetodo = "";
		Object item = null;
		Long itemId = 0L;
		String itemLabel = "";
		String itemDesc = "";
		Object itemAux = null;
		//Long itemAux = 0L;
		//String itemPrefix = "";
		int j = 0;
		
		try {
			aClasesParametros = pClasesParametros.split(",");
			aValoresParametros = pValoresParametros.split(",");
	
			if (aClasesParametros.length != aValoresParametros.length)
				throw new Exception("Numero de valores y numero de tipos esperados no coinciden");
	
			clasesParametros = new Class<?>[aClasesParametros.length];
			valoresParametros = new Object[aClasesParametros.length];
	
			for(int i = 0; i != aClasesParametros.length; i++) {
				clasesParametros[i] = null;
				if ("".equals(aClasesParametros[i])) 
					continue;
				
				clasesParametros[i] = Class.forName(aClasesParametros[i]);
				if (clasesParametros[i] == java.lang.Long.class) {
					valoresParametros[i] = new Long(Long.valueOf(aValoresParametros[i]));
				} else if (clasesParametros[i] == java.lang.Float.class) {
					valoresParametros[i] = new Float(Float.valueOf(aValoresParametros[i]));
				} else if (clasesParametros[i] == java.lang.Double.class) {
					valoresParametros[i] = new Double(Double.valueOf(aValoresParametros[i]));
				} else if (clasesParametros[i] == java.lang.Integer.class) {
					valoresParametros[i] = new Integer(Integer.valueOf(aValoresParametros[i]));
				} else {
					valoresParametros[i] = aValoresParametros[i];
				}
			}
	
			metodo = pMetodo.substring(0, pMetodo.indexOf("("));
			if (pMetodo.indexOf("()") == -1) {
				clasesParametrosMetodo = new Class<?>[aClasesParametros.length];
				j = 0;
				sClasesParametrosMetodo = pMetodo.substring(pMetodo.indexOf("(")+1,pMetodo.indexOf(")"));
				for(String clase : sClasesParametrosMetodo.split(",")) {
					clasesParametrosMetodo[j] = null;
					clasesParametrosMetodo[j++] = Class.forName(clase);
				}
			}
			
			jndiProps = new Properties();
			jndiProps.load(this.getClass().getResourceAsStream("jboss.properties"));
			context = new InitialContext(jndiProps);
			pr = context.lookup(pEjb);
			c = pr.getClass();
			m = c.getMethod(metodo, clasesParametrosMetodo);
			
			if (valoresParametros.length == 1 && valoresParametros[0] == null)
				valoresParametros = null;
	
			@SuppressWarnings("unchecked")
			List<Object> lst = (List<Object>) m.invoke(pr, valoresParametros);
			log.info((lst == null ? 0 : lst.size()) + " Resultados");
			
			item = null;
			itemId = 0L;
			itemLabel = "";
			itemDesc = "";
			
			for(Object o : lst) {
				oc = o.getClass();
				
				// Campos ID
				if(this.campoId == null || "".equals(this.campoId))
					omi = oc.getMethod("getId");
				else
					omi = oc.getMethod(this.campoId);
				
				// Campo DESCRIPCION
				if (this.campoDescripcion == null || "".equals(this.campoDescripcion))
					omd = oc.getMethod("getDescripcion");
				else
					omd = oc.getMethod(this.campoDescripcion);
				
				// Obtenemos valores
				item = omi.invoke(o);
				if (item.getClass() == java.math.BigDecimal.class)
					itemId = ((BigDecimal) item).longValue();
				if (item.getClass() == java.lang.Long.class)
					itemId = (Long) item;
				itemLabel = omd.invoke(o).toString();
				
				itemDesc = "";
				if (o.getClass().getName().equals("net.giro.clientes.beans.Persona")) {
					omi = oc.getMethod("getTipoPersona");
					item = omi.invoke(o);
					itemAux = (Long) item;

					itemDesc = "Negocio";
					if ((Long) itemAux == 1L) 
						itemDesc = "Persona";
				} 

				// Generamos ITEM
				json = new JsonObject();
				json.addProperty("id", itemId);
				json.addProperty("label", itemLabel);
				json.addProperty("description", itemDesc);
				
				// Añadimos al Listado
				listValores.add(json);
			}
			
			return listValores;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	
	private String getValorExtra(Object object) {
		Class<?> objClass = null;
		Method objMethod = null;
		Object objValue = null;
		String resultado = "";
		
		try {
			objClass = object.getClass();
			if (objClass.getName().equals("net.giro.clientes.beans.Persona")) {
				objMethod = objClass.getMethod("getTipoPersona");
				objValue = objMethod.invoke(object);
				
				if (((Long) objValue).equals(1L)) {
					resultado = " (Persona)";
				} if (((Long) objValue).equals(0L)) {
					resultado = " (Negocio)";
				}
			}
		} catch (Exception e) {
			log.error("Error al obtener valor extra", e);
		}
		
		return resultado;
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------------------------------------------------
	
	public String getCampoId() {
		return campoId;
	}
	
	public void setCampoId(String campoId) {
		this.campoId = campoId;
	}

	public String getCampoDescripcion() {
		return campoDescripcion;
	}

	public void setCampoDescripcion(String campoDescripcion) {
		this.campoDescripcion = campoDescripcion;
	}
}
