package net.proc.sesion;

import java.io.InputStream;

import org.apache.log4j.Logger;

public class CSoctetStream
{
  private static Logger log = Logger.getLogger(CSoctetStream.class);
  
  public static String read(InputStream inputS)
  {
    String respuesta = null;
    try
    {
      StringBuffer s = new StringBuffer();
      for (;;)
      {
        if (inputS.available() > 20)
        {
          byte[] buffer = new byte[inputS.available()];
          
          @SuppressWarnings("unused")
		int read = inputS.read(buffer);
          String sx = new String(buffer);
          s.append(sx);
          if (s.indexOf("\"respuesta\":") != -1) {
            break;
          }
          buffer = null;
        }
      }
      respuesta = s.toString();
      respuesta = respuesta.trim();
    }
    catch (Exception e)
    {
      respuesta = null;
      log.error("Error al leer el stream del socket", e);
    }
    return respuesta;
  }
}
