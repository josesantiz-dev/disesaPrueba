/**
 * @author Omar Magdiel Aguayo Garcia
 *
 * AKA -> Guitate
 */

package net.giro.plataforma;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;


import org.apache.log4j.*;
/**
 * 
 * @author Guitate
 *
 * @param <T> clase que se utilizara
 * @param <U> tipo de dato que devolveran las invocaciones
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReversaUtil<T, U>{
	private static Logger log = Logger.getLogger(ReversaUtil.class);
	private InitialContext ctx = null;
	private Object 		lookup = null;
	private T Interfaz;
	private U obj;
	
	Method metodo;
	Class<T> clase;
	
	public ReversaUtil(){}
	
	/**
	 * 
	 * @param c Clase que se utilizara para invocar sus metodos
	 */
	public ReversaUtil(T c){
		this.Interfaz = (T)c;
		this.clase = (Class<T>)c.getClass();
	}
	
	public ReversaUtil(T c, U u){
		this.Interfaz = (T)c;
		this.clase = (Class<T>)c.getClass();
		this.obj = u;
	}
	
	/**
	 * 
	 * @param JNDI la url para buscar el bean "FidMovimientoFacade/remote"
	 * @param c interfaz de acceso "FidMovimientoFacadeRemote"
	 */
	public ReversaUtil(String JNDI, Class<?> c) throws Exception{
			if(ctx == null)
				ctx = new InitialContext(); 
			this.lookup = ctx.lookup(JNDI);
			//this.Interfaz = (T)PortableRemoteObject.narrow(lookup, c);
			this.clase = (Class<T>)this.Interfaz.getClass();
	}
	
	/**
	 * 
	 * @param JNDI la url para buscar el bean "FidMovimientoFacade/remote"
	 * @param c interfaz de acceso "FidMovimientoFacadeRemote"
	 */
	public ReversaUtil(String JNDI, Class<?> c, U u) throws Exception{
			if(ctx == null)
				ctx = new InitialContext(); 
			this.lookup = ctx.lookup(JNDI);
			//this.Interfaz = (T)PortableRemoteObject.narrow(lookup, c);
			this.clase = (Class<T>)this.Interfaz.getClass();
			this.obj = u;
	}
	
	/**
	 * 
	 * @param JNDI la url para buscar el bean "FidMovimientoFacade/remote"
	 * @param c interfaz de acceso "FidMovimientoFacadeRemote"
	 * @param env properties que definen la coneccion
	 */
	public ReversaUtil(String JNDI, Class<?> c, Properties env) throws Exception{
			if(ctx == null)
				ctx = new InitialContext(env); 
			this.lookup = ctx.lookup(JNDI);
			//this.Interfaz = (T)PortableRemoteObject.narrow(lookup, c);
			this.clase = (Class<T>)this.Interfaz.getClass();
	}
	
	/**
	 * 
	 * @param invocacion nombre del metodo a ejecutar
	 * @param parametros los tipos de datos de los param.. deben de coincidir para el metodo que invocan
	 * @return resultado de la invocacion al metodo de tipo U
	 */
	public U ejecutaMetodo(String invocacion, List<Object> parametros){
		try{
			metodo = null;
			
			Class [] param = new Class[parametros.size()];
			Object [] args = new Object[parametros.size()];
			int i=0;
			
			for(Object o:parametros){
				param[i] = o.getClass();
				args[i] = o;
				i++;
			}
			
			metodo = clase.getDeclaredMethod(invocacion, param);
			this.obj = (U) metodo.invoke(this.Interfaz, args);
			
			return this.obj;
		}catch(Exception re){
			log.error("Error al ejecutar el metodo ejecutaMetodo", re);
		}
		return null;
	}
	
	/**
	 * 
	 * @param invocacion nombre de la propiedad a obtener
	 * @return resultado de del get de la propiedad
	 */
	public U getPropiedad(String campo){
		try{
			metodo = null;
			
			metodo = clase.getDeclaredMethod(campo);
			this.obj = (U) metodo.invoke(this.Interfaz);
			
			return this.obj;
		}catch(Exception re){
			log.error("Error al ejecutar el metodo ejecutaMetodo", re);
		}
		return null;
	}
	
	/**
	 * se elimina la variable que recibia el dato del metodo puesto que no existe un tipo de dato void
	 * @param invocacion nombre del metodo a ejecutar
	 * @param parametros los tipos de datos de los param.. deben de coincidir para el metodo que invocan
	 */
	public void ejecutaMetodoVoid(String invocacion, List<Object> parametros){
		try{
			metodo = null;
			
			Class [] param = new Class[parametros.size()];
			Object [] args = new Object[parametros.size()];
			int i=0;
			
			for(Object o:parametros){
				param[i] = o.getClass();
				args[i] = o;
				i++;
			}
			
			metodo = clase.getDeclaredMethod(invocacion, param);
			metodo.invoke(this.Interfaz, args);
			
		}catch(Exception re){
			log.error("Error al ejecutar el metodo ejecutaMetodo", re);
		}
	}
	
	/**
	 * se elimina la variable que recibia el dato del metodo puesto que no existe un tipo de dato void
	 * @param invocacion nombre del metodo a ejecutar
	 * @param parametros los tipos de datos de los param.. deben de coincidir para el metodo que invocan
	 * @param omitirTipos anade el tipo de clase Object a los parametros
	 */
	public void ejecutaMetodoVoid(String invocacion, List<Object> parametros, boolean omitirTipos){
		try{
			metodo = null;
			
			Class [] param = new Class[parametros.size()];
			Object [] args = new Object[parametros.size()];
			int i=0;
			
			for(Object o:parametros){
				param[i] = omitirTipos ? Object.class : o.getClass();
				args[i] = o;
				i++;
			}
			
			metodo = clase.getDeclaredMethod(invocacion, param);
			metodo.invoke(this.Interfaz, args);
			
		}catch(Exception re){
			log.error("Error al ejecutar el metodo ejecutaMetodo", re);
		}
	}
	
	/**
	 * 
	 * @return devuelve el objeto que se instancio
	 */
	public T getClase(){
		return this.Interfaz;
	}
}
