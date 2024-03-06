package net.proc.controller;

import java.util.HashMap;

public class RespuestaProcesos
{
  private HashMap<String, HashMap<String, Object>> proceso;
  private String error;
  private String tipo;
  private String sesion;
  private Long resultado;
  private String msgHost;
  private String mensaje;
  private String respuesta;
  
  public HashMap<String, HashMap<String, Object>> getProceso()
  {
    return this.proceso;
  }
  
  public void setProceso(HashMap<String, HashMap<String, Object>> proceso)
  {
    this.proceso = proceso;
  }
  
  public String getError()
  {
    return this.error;
  }
  
  public void setError(String error)
  {
    this.error = error;
  }
  
  public String getTipo()
  {
    return this.tipo;
  }
  
  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }
  
  public String getSesion()
  {
    return this.sesion;
  }
  
  public void setSesion(String sesion)
  {
    this.sesion = sesion;
  }
  
  public Long getResultado()
  {
    return this.resultado;
  }
  
  public void setResultado(Long resultado)
  {
    this.resultado = resultado;
  }
  
  public String getMsgHost()
  {
    return this.msgHost;
  }
  
  public void setMsgHost(String msgHost)
  {
    this.msgHost = msgHost;
  }
  
  public String getMensaje()
  {
    return this.mensaje;
  }
  
  public void setMensaje(String mensaje)
  {
    this.mensaje = mensaje;
  }
  
  public String getRespuesta()
  {
    return this.respuesta;
  }
  
  public void setRespuesta(String respuesta)
  {
    this.respuesta = respuesta;
  }
}
