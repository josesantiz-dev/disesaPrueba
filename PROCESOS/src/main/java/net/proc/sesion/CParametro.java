package net.proc.sesion;

import java.io.Serializable;

public class CParametro
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private long parametroId;
  private String parametro;
  private String tipo;
  private String tipoEntrada;
  private String formatoEntrada;
  private String formatoSalida;
  private String usuario;
  private int requerido;
  private String valores;
  private String descripcion;
  private String ayuda;
  private String valorDefault;
  private String mensajeError;
  private String etiqueta;
  private int orden;
  private long grupo;
  
  public long getGrupo()
  {
    return this.grupo;
  }
  
  public void setGrupo(long grupo)
  {
    this.grupo = grupo;
  }
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long id)
  {
    this.id = id;
  }
  
  public long getParametroId()
  {
    return this.parametroId;
  }
  
  public void setParametroId(long parametroId)
  {
    this.parametroId = parametroId;
  }
  
  public String getTipo()
  {
    return this.tipo;
  }
  
  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }
  
  public String getTipoEntrada()
  {
    return this.tipoEntrada;
  }
  
  public void setTipoEntrada(String tipoEntrada)
  {
    this.tipoEntrada = tipoEntrada;
  }
  
  public int getRequerido()
  {
    return this.requerido;
  }
  
  public void setRequerido(int requerido)
  {
    this.requerido = requerido;
  }
  
  public String getValores()
  {
    return this.valores;
  }
  
  public void setValores(String valores)
  {
    this.valores = valores;
  }
  
  public String getDescripcion()
  {
    return this.descripcion;
  }
  
  public void setDescripcion(String descripcion)
  {
    this.descripcion = descripcion;
  }
  
  public String getAyuda()
  {
    return this.ayuda;
  }
  
  public void setAyuda(String ayuda)
  {
    this.ayuda = ayuda;
  }
  
  public String getMensajeError()
  {
    return this.mensajeError;
  }
  
  public void setMensajeError(String mensajeError)
  {
    this.mensajeError = mensajeError;
  }
  
  public String getEtiqueta()
  {
    return this.etiqueta;
  }
  
  public void setEtiqueta(String etiqueta)
  {
    this.etiqueta = etiqueta;
  }
  
  public String getParametro()
  {
    return this.parametro;
  }
  
  public void setParametro(String parametro)
  {
    this.parametro = parametro;
  }
  
  public String getFormatoEntrada()
  {
    return this.formatoEntrada;
  }
  
  public void setFormatoEntrada(String formatoEntrada)
  {
    this.formatoEntrada = formatoEntrada;
  }
  
  public String getFormatoSalida()
  {
    return this.formatoSalida;
  }
  
  public void setFormatoSalida(String formatoSalida)
  {
    this.formatoSalida = formatoSalida;
  }
  
  public String getValorDefault()
  {
    return this.valorDefault;
  }
  
  public void setValorDefault(String valorDefault)
  {
    this.valorDefault = valorDefault;
  }
  
  public String getUsuario()
  {
    return this.usuario;
  }
  
  public int getOrden()
  {
    return this.orden;
  }
  
  public void setOrden(int orden)
  {
    this.orden = orden;
  }
  
  public void setUsuario(String usuario)
  {
    this.usuario = usuario;
  }
}
