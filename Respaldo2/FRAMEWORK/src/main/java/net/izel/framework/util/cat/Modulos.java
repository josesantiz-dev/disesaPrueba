
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de estatus en servicio Clientes
*
*/

package net.izel.framework.util.cat;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class Modulos { 
	
	/*
	public static final String PUBLICO = "PUBLICO";
	public static final String CLIENTES = "CLIENTES";
	public static final String CREDITO = "CREDITO";
	public static final String AHORRO = "AHORRO";
	public static final String SOLICITUD = "SOLICITUD";
	public static final String GARANTIAS = "GARANTIAS";
	public static final String PLATAFORMA = "PLATAFORMA";
	*/
	
	public static final Long CLIENTES = 1l;
	public static final Long CREDITOS = 2l;
	public static final Long AHORRO = 3l;
	public static final Long GARANTIAS = 4l;
	public static final Long ADMINISTRACION = 5l;
	public static final Long PLATAFORMA = 6l;
	public static final Long PUBLICO = 7l;
	public static final Long SOLICITUDES = 8l;
	public static final Long SWITCH = 9l;
	public static final Long BURO = 10l;
	public static final Long BPM = 11l;
	public static final Long IMP = 12l;
	public static final Long OM = 13l;
	public static final Long STI = 13l;
	public static final Long OI = 14l;
	
	
	public static HashMap<Long, String> nombreModulo = new HashMap<Long, String>();
	
	static {
		nombreModulo.put(CLIENTES,"CLIENTES");
		nombreModulo.put(CREDITOS,"CREDITOS");
		nombreModulo.put(AHORRO,"AHORRO");
		nombreModulo.put(GARANTIAS,"GARANTIAS");
		nombreModulo.put(ADMINISTRACION,"ADMINISTRACION");
		nombreModulo.put(PLATAFORMA,"PLATAFORMA");
		nombreModulo.put(PUBLICO,"PUBLICO");
		nombreModulo.put(SOLICITUDES,"SOLICITUDES");
		nombreModulo.put(SWITCH,"SWITCH");
		nombreModulo.put(BURO,"BURO");
		nombreModulo.put(BPM,"BPM");
		nombreModulo.put(IMP,"IMP");
		nombreModulo.put(OM,"OM");
		nombreModulo.put(STI,"STI");
		nombreModulo.put(OI,"OI");
	}
	
	public static String obtenerIpModulo(String modulo){
		String idWS = "";
		Properties prop = new Properties();
		Modulos m = new Modulos();
		System.out.println("obtenerIpModulo:: OBTENER IPMODULOS");
		try {
		    //Conexion a Servidor del JBOSS 
		    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("/net/izel/framework/util/cat/ip_modulos.properties");		    
			prop.load(in);			
			idWS = prop.getProperty(modulo).toString();
			System.out.println("obtenerIpModulo:: OBTENER InputStream Servidor JBOSS " + idWS );
			return idWS;
		} 
		catch (Exception e1)  {		
			 //Version para conexion al servidor del JBOSS LOCAL
			System.out.println("OBTENER IPMODULOS LOCAL");
			try {
				InputStream in = m.getClass().getResourceAsStream("/net/izel/framework/util/cat/ip_modulos.properties");
			    prop.load(in);
				idWS = prop.getProperty(modulo).toString();
				System.out.println("obtenerIpModulo:: OBTENER InputStream Local" + idWS);
				return idWS;
			}
			catch (IOException e2)  {
				e2.printStackTrace();
				return "localhost";
			}
			
		}  //!IOException e1

	}
	
	/**
	 *  Obtiene la descripcion del erro por modulo.
	 * @param modulo
	 * @param id
	 * @return
	 */
	public static String ObtenerDescripcionError(String modulo, long id) {
		String descError= "";
		
		if (modulo.equals("PUBLICO")) {
			descError = net.izel.framework.util.cat.Errores.descError.get(id);
		}
		if (modulo.equals("CLIENTES")) {
			descError = net.izel.framework.util.cl.Errores.descError.get(id);
		}
		if (modulo.equals("CREDITO")) {
			descError = net.izel.framework.util.cr.Errores.descError.get(id);
		}
		if (modulo.equals("AHORRO")) {
			descError = net.izel.framework.util.ah.Errores.descError.get(id);
		}		
		if (modulo.equals("SOLICITUD")) {
			descError = net.izel.framework.util.so.Errores.descError.get(id);
		}
		if (modulo.equals("GARANTIAS")) {
			descError = net.izel.framework.util.gt.Errores.descError.get(id);
		}
		if (modulo.equals("PLATAFORMA")) {
			descError = net.izel.framework.util.pl.Errores.descError.get(id);
		}	
		if (modulo.equals("SWITCH")) {
			descError = net.izel.framework.util.swt.Errores.descError.get(id);
		}		
		if (modulo.equals("TELLER")) {
			descError = net.izel.framework.util.te.Errores.descError.get(id);
		}		
		if (modulo.equals("OPERACION_INTEGRADA")) {
			descError = net.izel.framework.util.oi.Errores.descError.get(id);
		}		
		if (modulo.equals("COMPRA_VENTA_MONEDA")) {
			descError = net.izel.framework.util.cvm.Errores.descError.get(id);
		}		
		if (modulo.equals("GIRO")) {
		//	descError = net.izel.framework.util.gi.Errores.descError.get(id);
		}		
		if (modulo.equals("STI")) {
			descError = net.izel.framework.util.sti.Errores.descError.get(id);
		}
		if (modulo.equals("BP")) {			
			descError = net.izel.framework.util.sti.Errores.descError.get(id);
		}
		if (modulo.equals("IMP")) {			
			descError = net.izel.framework.util.imp.Errores.descError.get(id);
		}
		if (modulo.equals("OM")) {			
			descError = net.izel.framework.util.om.Errores.descError.get(id);
		}
		if (modulo.equals("OI")) {			
			descError = net.izel.framework.util.oi.Errores.descError.get(id);
		}
		return descError;
	}

	

}