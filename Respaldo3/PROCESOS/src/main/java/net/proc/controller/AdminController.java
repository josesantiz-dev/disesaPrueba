package net.proc.controller;

import com.google.gson.Gson;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import net.proc.ftp.FTPCliente;
import net.proc.obj.CProcesoConsulta;
import net.proc.sesion.CSoctetStream;
import net.proc.sesion.SesionProc;
import net.proc.socket.respuesta.RespuestaProcesos;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="adminController")
public class AdminController
  implements Serializable
{
  Logger logger = Logger.getLogger(getClass());
  private static final long serialVersionUID = 1L;
  private List<CProcesoConsulta> lista;
  private HashMap<String, Object> param;
  private SesionProc sesion;
  private CProcesoConsulta job;
  private FTPCliente ftp;
  private HttpSession httpSession;
  private String resOperacion;
  private int numPagina = 1;
  
  public AdminController()
  {
    FacesContext fc = FacesContext.getCurrentInstance();
    Application app = fc.getApplication();
    
    ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{sesionProc}", SesionProc.class);
    this.sesion = ((SesionProc)dato.getValue(fc.getELContext()));
    
    dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgAdminProc}", PropertyResourceBundle.class);
    PropertyResourceBundle prop = (PropertyResourceBundle)dato.getValue(fc.getELContext());
    
    this.httpSession = ((HttpSession)fc.getExternalContext().getSession(false));
    
    this.ftp = new FTPCliente(prop.getString("ftp_user"), prop.getString("ftp_password"), prop.getString("ftp_host"));
    obtenerProcesos();
  }
  
  @SuppressWarnings("rawtypes")
public void obtenerProcesos()
  {
    HashMap<String, Object> map = new HashMap<String, Object>();
    this.lista = new ArrayList<CProcesoConsulta>();
    try
    {
      this.resOperacion = "";
      map.put("tipo", "020200");
      
      Gson gson = new Gson();
      String comando = gson.toJson(map);
      Socket socket = this.sesion.getSocket();
      socket.getOutputStream().write(comando.getBytes());
      socket.getOutputStream().flush();
      
      String resultado = CSoctetStream.read(socket.getInputStream());
      RespuestaProcesos resp = (RespuestaProcesos)gson.fromJson(resultado, RespuestaProcesos.class);
      for (Map.Entry<String, HashMap<String, Object>> m : resp.getProcesos().entrySet())
      {
        CProcesoConsulta job = new CProcesoConsulta();
        
        job.setPrograma((String)((HashMap)m.getValue()).get("programa"));
        job.setStatus((String)((HashMap)m.getValue()).get("status"));
        job.setPriority(Double.valueOf(((HashMap)m.getValue()).get("priority").toString()).intValue());
        job.setPhase((String)((HashMap)m.getValue()).get("phase"));
        job.setUsuario((String)((HashMap)m.getValue()).get("usuario"));
        job.setSalida((String)((HashMap)m.getValue()).get("salida"));
        if (((HashMap)m.getValue()).get("nextFireTime") != null) {
          job.setNextFireTime(new Date(Double.valueOf(((HashMap)m.getValue()).get("nextFireTime").toString()).longValue()));
        }
        if ((((HashMap)m.getValue()).get("endTime") != null) && (Double.valueOf(((HashMap)m.getValue()).get("endTime").toString()).doubleValue() > 0.0D)) {
          job.setEndTime(new Date(Double.valueOf(((HashMap)m.getValue()).get("endTime").toString()).longValue()));
        }
        job.setStartTime(new Date(Double.valueOf(((HashMap)m.getValue()).get("startTime").toString()).longValue()));
        job.setId(Long.valueOf(Double.valueOf(((HashMap)m.getValue()).get("id").toString()).longValue()));
        if ((((HashMap)m.getValue()).get("prevFireTime") != null) && (Double.valueOf(((HashMap)m.getValue()).get("prevFireTime").toString()).doubleValue() > 0.0D)) {
          job.setPrevFireTime(new Date(Double.valueOf(((HashMap)m.getValue()).get("prevFireTime").toString()).longValue()));
        }
        this.lista.add(job);
      }
      Collections.sort(this.lista, new Comparator<CProcesoConsulta>()
      {
    	@Override
        public int compare(CProcesoConsulta o1, CProcesoConsulta o2)
        {
          return (int)(o2.getId().longValue() - o1.getId().longValue());
        }

	 
      });
    }
    catch (Exception e)
    {
      this.resOperacion = "Ocurrio un problema al obtener los procesos";
      this.logger.error("Error al obtener los procesos", e);
    }
  }
  
  public void obtenerSalida()
  {
    byte[] source = null;
    try
    {
      source = this.ftp.getArchivo(FTPCliente.Tipo.SALIDA, Double.valueOf(this.job.getId().longValue()).longValue());
      this.httpSession.setAttribute("contenido", source);
      this.httpSession.setAttribute("archivo", String.valueOf(this.job.getId()));
      this.httpSession.setAttribute("formato", this.job.getSalida());
    }
    catch (Exception e)
    {
      this.logger.error("Error al obtener la salida del job", e);
    }
  }
  
  public void obtenerLog()
  {
    byte[] source = null;
    try
    {
      source = this.ftp.getArchivo(FTPCliente.Tipo.LOG, Double.valueOf(this.job.getId().longValue()).longValue());
      this.httpSession.setAttribute("contenido", source);
      this.httpSession.setAttribute("archivo", String.valueOf(this.job.getId()));
      this.httpSession.setAttribute("formato", "txt");
    }
    catch (Exception e)
    {
      this.logger.error("Error al obtener la salida del job", e);
    }
  }
  
  public void pausaRanudarJob()
  {
    HashMap<String, String> map = new HashMap<String, String>();
    String resultado = null;
    try
    {
      this.resOperacion = "";
      map.put("tipo", "R".equals(this.job.getPhase()) ? "000300" : "000400");
      map.put("idPeticion", this.job.getId().toString());
      
      Gson gson = new Gson();
      String comando = gson.toJson(map);
      Socket socket = this.sesion.getSocket();
      socket.getOutputStream().write(comando.getBytes());
      resultado = CSoctetStream.read(socket.getInputStream());
      
      System.out.println(resultado);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void cancelarJob()
  {
    HashMap<String, String> map = new HashMap<String, String>();
    String resultado = null;
    try
    {
      this.resOperacion = "";
      map.put("tipo", "000200");
      map.put("idPeticion", this.job.getId().toString());
      
      Gson gson = new Gson();
      String comando = gson.toJson(map);
      Socket socket = this.sesion.getSocket();
      socket.getOutputStream().write(comando.getBytes());
      socket.getOutputStream().flush();
      resultado = CSoctetStream.read(socket.getInputStream());
      
      System.out.println(resultado);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public String getResOperacion()
  {
    return this.resOperacion;
  }
  
  public void setResOperacion(String resOperacion)
  {
    this.resOperacion = resOperacion;
  }
  
  public HashMap<String, Object> getParam()
  {
    return this.param;
  }
  
  public void setParam(HashMap<String, Object> param)
  {
    this.param = param;
  }
  
  public List<CProcesoConsulta> getLista()
  {
    return this.lista;
  }
  
  public void setLista(List<CProcesoConsulta> lista)
  {
    this.lista = lista;
  }
  
  public CProcesoConsulta getJob()
  {
    return this.job;
  }
  
  public void setJob(CProcesoConsulta job)
  {
    this.job = job;
  }
  
  public int getNumPagina()
  {
    return this.numPagina;
  }
  
  public void setNumPagina(int numPagina)
  {
    this.numPagina = numPagina;
  }
}
