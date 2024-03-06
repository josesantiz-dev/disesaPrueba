package net.izel.framework.util;

import java.io.InputStream;
import java.util.Properties;

public class ReferenciaSPEI 
{
	protected static String clavePlaza;
	protected static String claveInstitucion;
	
	public static void procesa() 
	{
		Properties prop = new Properties();
		ReferenciaSPEI m = new ReferenciaSPEI();
		System.out.println("ReferenciaSPEI:: procesa  >>  OBTENER PARAMETROS SPEI");
		
		try {
			// Conexion a Servidor del JBOSS
			System.out.println("OBTENER PARAMETROS JBOSS SERVIDOR");
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(
							                                             "/src/net/izel/framework/util/referencia.properties");			
			prop.load(in);
			clavePlaza = prop.getProperty("CLAVE_PLAZA").toString();
			claveInstitucion= prop.getProperty("CLABE_INST").toString();
			System.out.println("referencia:: procesa: valores leidos");
			
		} 
		catch (Exception e1) {
			// Version para conexion al servidor del JBOSS LOCAL
			System.out.println("OBTENER PARAMETROS JBOSS LOCAL");
			try {
				InputStream in = m.getClass().getResourceAsStream("/net/izel/framework/util/referencia.properties");
				prop.load(in);
				clavePlaza = prop.getProperty("CLAVE_PLAZA").toString();
				claveInstitucion= prop.getProperty("CLABE_INST").toString();
				System.out.println("ReferenciaSPEI:: procesa: valores leidos");
			} 
			catch (Exception e2) {
				e2.printStackTrace();
			}

		} 
		
	}

	public static String getClavePlaza() {
		return clavePlaza;
	}

	public static void setClavePlaza(String clavePlaza) {
		ReferenciaSPEI.clavePlaza = clavePlaza;
	}

	public static String getClaveInstitucion() {
		return claveInstitucion;
	}

	public static void setClaveInstitucion(String claveInstitucion) {
		ReferenciaSPEI.claveInstitucion = claveInstitucion;
	}
	
}

/** !Referencia */



