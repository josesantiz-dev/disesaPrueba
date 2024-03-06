package net.proc.obj;

import java.io.Serializable;
import java.util.Date;

public class CProcesoConsulta
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  Long id;
  String programa;
  String descripcion;
  Date nextFireTime;
  Date prevFireTime;
  int priority;
  Date startTime;
  Date endTime;
  String phase;
  String status;
  String usuario;
  String salida;
  
  public Long getId()
  {
    return this.id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public String getPrograma()
  {
    return this.programa;
  }
  
  public void setPrograma(String programa)
  {
    this.programa = programa;
  }
  
  public String getDescripcion()
  {
    return this.descripcion;
  }
  
  public void setDescripcion(String descripcion)
  {
    this.descripcion = descripcion;
  }
  
  public Date getNextFireTime()
  {
    return this.nextFireTime;
  }
  
  public void setNextFireTime(Date nextFireTime)
  {
    this.nextFireTime = nextFireTime;
  }
  
  public Date getPrevFireTime()
  {
    return this.prevFireTime;
  }
  
  public void setPrevFireTime(Date prevFireTime)
  {
    this.prevFireTime = prevFireTime;
  }
  
  public int getPriority()
  {
    return this.priority;
  }
  
  public void setPriority(int priority)
  {
    this.priority = priority;
  }
  
  public Date getStartTime()
  {
    return this.startTime;
  }
  
  public void setStartTime(Date startTime)
  {
    this.startTime = startTime;
  }
  
  public Date getEndTime()
  {
    return this.endTime;
  }
  
  public void setEndTime(Date endTime)
  {
    this.endTime = endTime;
  }
  
  public String getPhase()
  {
    return this.phase;
  }
  
  public void setPhase(String phase)
  {
    this.phase = phase;
  }
  
  public String getStatus()
  {
    return this.status;
  }
  
  public void setStatus(String status)
  {
    this.status = status;
  }
  
  public String getPhaseCompleto()
  {
    String resultado = "";
    if ("X".equals(this.phase)) {
      resultado = "Cancelado";
    } else if ("Q".equals(this.phase)) {
      resultado = "En cola programado";
    } else if ("S".equals(this.phase)) {
      resultado = "Suspendido";
    } else if ("E".equals(this.phase)) {
      resultado = "Error";
    } else if ("T".equals(this.phase)) {
      resultado = "Terminado";
    } else if ("P".equals(this.phase)) {
      resultado = "Pendiente";
    } else if ("R".equals(this.phase)) {
      resultado = "Ejecucion";
    }
    if ("N".equals(this.status)) {
      resultado = resultado + " Normal";
    } else if ("E".equals(this.status)) {
      resultado = resultado + " en Error";
    } else if ("X".equals(this.status)) {
      resultado = resultado + " por Cancelacion";
    }
    return resultado;
  }
  
  public String getUsuario()
  {
    return this.usuario;
  }
  
  public void setUsuario(String usuarios)
  {
    this.usuario = usuarios;
  }
  
  public void setPhaseCompleto(String phaseCompleto) {}
  
  public String getSalida()
  {
    return this.salida;
  }
  
  public void setSalida(String salida)
  {
    this.salida = salida;
  }
}
