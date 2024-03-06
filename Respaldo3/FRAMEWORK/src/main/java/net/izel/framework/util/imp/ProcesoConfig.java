package net.izel.framework.util.imp;

import java.io.InputStream;
import java.util.Properties;

import net.izel.framework.util.cat.Modulos;

public class ProcesoConfig 
{
	private static String  hostFPT;
	private static String userFTP;
	private static String pswFTP;
	private static Integer portFTP; 
	private static String sockIp;
	private static String sockPort;
	private static String intentos;
	
	public static String getHostFPT() {
		return hostFPT;
	}

	public static String getUserFTP() {
		return userFTP;
	}

	public static String getPswFTP() {
		return pswFTP;
	}
	
	public static String getSockIp() {
		return sockIp;
	}
	
	public static String getSockPort() {
		return sockPort;
	}

	public static String getIntentos() {
		return intentos;
	}
	
	public static void procesoCucurrente(String strProperties) 
	{
		Properties prop = new Properties();
		Modulos m = new Modulos();
		System.out.println("Proceso:: procesoCucurrente  >>  OBTENER DATOS DE CONCURRENTE");
		try {
			// Conexion a Servidor del JBOSS
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("/scr/net/izel/framework/util/imp/procesos.properties");
			prop.load(in);
			sockIp = prop.getProperty("IPSOCK").toString();
			sockPort= prop.getProperty("PORTSOCK").toString();
			hostFPT= prop.getProperty("HOSTFTP").toString();
			userFTP= prop.getProperty("HOSTUSER").toString();
			pswFTP= prop.getProperty("HOSTPSW").toString();
			intentos= prop.getProperty("INTENTOS").toString();
			portFTP= Integer.parseInt(prop.getProperty("HOSTPORT").toString());
			
			/*System.out.println("procesoCucurrente::  IPSOCK " + sockIp);
			System.out.println("procesoCucurrente::  PORTSOCK " + sockPort);
			System.out.println("procesoCucurrente::  HOSTFTP " + hostFPT);
			System.out.println("procesoCucurrente::  HOSTUSER " + userFTP);
			System.out.println("procesoCucurrente::  HOSTPSW " + pswFTP);			
			System.out.println("procesoCucurrente::  HOSTPPORT " + portFTP);
			System.out.println("procesoCucurrente::  INTENTOS " + intentos); */
		} 
		catch (Exception e1) {
			// Version para conexion al servidor del JBOSS LOCAL
			System.out.println("OBTENER IPMODULOS LOCAL");
			try {
				InputStream in = m.getClass().getResourceAsStream("/net/izel/framework/util/imp/procesos.properties");
				prop.load(in);
				sockIp = prop.getProperty("IPSOCK").toString();
				sockPort= prop.getProperty("PORTSOCK").toString();
				hostFPT= prop.getProperty("HOSTFTP").toString();
				userFTP= prop.getProperty("HOSTUSER").toString();
				pswFTP= prop.getProperty("HOSTPSW").toString();
				intentos= prop.getProperty("INTENTOS").toString();
				portFTP= Integer.parseInt(prop.getProperty("HOSTPORT").toString());

				System.out.println("procesoCucurrente::  IPSOCK " + sockIp);
				System.out.println("procesoCucurrente::  PORTSOCK " + sockPort);
				System.out.println("procesoCucurrente::  HOSTFTP " + hostFPT);
				System.out.println("procesoCucurrente::  HOSTUSER " + userFTP);
				System.out.println("procesoCucurrente::  HOSTPSW " + pswFTP);
				System.out.println("procesoCucurrente::  HOSTPPORT " + portFTP);
				System.out.println("procesoCucurrente::  INTENTOS " + intentos);
				
			} 
			catch (Exception e2) {
				e2.printStackTrace();
			}

		} 
	}

	/*public static void procesoConcurrenteFTP() 
	{
		Properties prop = new Properties();
		Modulos m = new Modulos();
		System.out.println("Proceso:: procesoCucurrente  >>  OBTENER DATOS DE CONCURRENTE");
		try {
			// Conexion a Servidor del JBOSS
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("/scr/net/izel/framework/util/imp/procesos.properties");
			System.out.println("in prop " + prop.getProperty("IPSOCK").toString());
			prop.load(in);
			sockIp = prop.getProperty("IPSOCK_DOCTOS").toString();
			sockPort= prop.getProperty("PORTSOCK_DOCTOS").toString();
			hostFPT= prop.getProperty("HOSTFTP_DOCTOS").toString();
			userFTP= prop.getProperty("HOSTUSER_DOCTOS").toString();
			pswFTP= prop.getProperty("HOSTPSW_DOCTOS").toString();
			intentos= prop.getProperty("INTENTOS_DOCTOS").toString();
			portFTP= Integer.parseInt(prop.getProperty("HOSTPORT_DOCTOS").toString());
			
			System.out.println("procesoConcurrenteFTP::  IPSOCK " + sockIp);
			System.out.println("procesoConcurrenteFTP::  PORTSOCK " + sockPort);
			System.out.println("procesoConcurrenteFTP::  HOSTFTP " + hostFPT);
			System.out.println("procesoConcurrenteFTP::  HOSTUSER " + userFTP);
			System.out.println("procesoConcurrenteFTP::  HOSTPSW " + pswFTP);			
			System.out.println("procesoConcurrenteFTP::  HOSTPPORT " + portFTP);
			System.out.println("procesoConcurrenteFTP::  INTENTOS " + intentos);
		} 
		catch (Exception e1) {
			// Version para conexion al servidor del JBOSS LOCAL
			System.out.println("OBTENER IPMODULOS LOCAL");
			try {
				InputStream in = m.getClass().getResourceAsStream("/net/izel/framework/util/imp/procesos.properties");
				prop.load(in);
				sockIp = prop.getProperty("IPSOCK_DOCTOS").toString();
				sockPort= prop.getProperty("PORTSOCK_DOCTOS").toString();
				hostFPT= prop.getProperty("HOSTFTP_DOCTOS").toString();
				userFTP= prop.getProperty("HOSTUSER_DOCTOS").toString();
				pswFTP= prop.getProperty("HOSTPSW_DOCTOS").toString();
				intentos= prop.getProperty("INTENTOS_DOCTOS").toString();
				portFTP= Integer.parseInt(prop.getProperty("HOSTPORT_DOCTOS").toString());

				System.out.println("procesoConcurrenteFTP::  IPSOCK " + sockIp);
				System.out.println("procesoConcurrenteFTP::  PORTSOCK " + sockPort);
				System.out.println("procesoConcurrenteFTP1::  HOSTFTP " + hostFPT);
				System.out.println("procesoConcurrenteFTP::  HOSTUSER " + userFTP);
				System.out.println("procesoConcurrenteFTP::  HOSTPSW " + pswFTP);
				System.out.println("procesoConcurrenteFTP::  HOSTPPORT " + portFTP);
				System.out.println("procesoConcurrenteFTP::  INTENTOS " + intentos);
				
			} 
			catch (Exception e2) {
				e2.printStackTrace();
			}

		} 
	}*/

	public static Integer getPortFTP() {
		return portFTP;
	}

	public static void setPortFTP(Integer portFTP) {
		ProcesoConfig.portFTP = portFTP;
	}
	
	
}


/** !Proceso.java */

