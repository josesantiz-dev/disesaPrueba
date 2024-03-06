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
@ManagedBean(name="procesos")
public class ProcesosController implements Serializable {
	private static Logger log = Logger.getLogger(ProcesosController.class);
	private static final long serialVersionUID = 1L;
	private List<CProcesoConsulta> lista;
	private HashMap<String, Object> param;
	private SesionProc sesion;
	private CProcesoConsulta job;
	private FTPCliente ftp;
	private HttpSession httpSession;
	private String resOperacion;
	private int numPagina = 1;
	
	
	public ProcesosController() {
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
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void obtenerProcesos() {
		HashMap<String, Object> map = new HashMap();
		RespuestaProcesos resp = null;
		CProcesoConsulta job = null;
		Gson gson = new Gson();
		Socket socket = null;
		String resultado = "";
		String comando = "";
		
		try {
			log.info("Obteniendo Listado de Procesos ... ");
			this.resOperacion = "";
			this.lista = new ArrayList();
			
			map.put("tipo", "020100");
			map.put("usuario", this.sesion.getUsuario());
			map.put("delUsuario", this.sesion.getUsuario());
			
			comando = gson.toJson(map);
			log.info("------------------------- COMANDO: " + comando);
			socket = this.sesion.getSocket();
			socket.getOutputStream().write(comando.getBytes());
			socket.getOutputStream().flush();
			resultado = CSoctetStream.read(socket.getInputStream());
			log.info("----------------------- RESULTADO: " + resultado);
			
			resp = (RespuestaProcesos) gson.fromJson(resultado, RespuestaProcesos.class);
			for (Map.Entry<String, HashMap<String, Object>> m : resp.getProcesos().entrySet()) {
				job = new CProcesoConsulta();
				job.setId(Long.valueOf(Double.valueOf(((HashMap) m.getValue()).get("id").toString()).longValue()));
				job.setPrograma((String)((HashMap) m.getValue()).get("programa"));
				job.setSalida((String)((HashMap) m.getValue()).get("salida"));
				job.setStatus((String)((HashMap) m.getValue()).get("status"));
				job.setPriority(Double.valueOf(((HashMap) m.getValue()).get("priority").toString()).intValue());
				job.setPhase((String)((HashMap) m.getValue()).get("phase"));
				if (((HashMap) m.getValue()).get("nextFireTime") != null)
					job.setNextFireTime(new Date(Double.valueOf(((HashMap) m.getValue()).get("nextFireTime").toString()).longValue()));
				if ((((HashMap) m.getValue()).get("endTime") != null) && (Double.valueOf(((HashMap) m.getValue()).get("endTime").toString()).doubleValue() > 0.0D)) 
					job.setEndTime(new Date(Double.valueOf(((HashMap) m.getValue()).get("endTime").toString()).longValue()));
				job.setStartTime(new Date(Double.valueOf(((HashMap) m.getValue()).get("startTime").toString()).longValue()));
				if ((((HashMap) m.getValue()).get("prevFireTime") != null) && (Double.valueOf(((HashMap) m.getValue()).get("prevFireTime").toString()).doubleValue() > 0.0D)) 
					job.setPrevFireTime(new Date(Double.valueOf(((HashMap) m.getValue()).get("prevFireTime").toString()).longValue()));

				log.info("---------------------------------- " + job.getId() + " - " + job.getPrograma() + "." + job.getSalida());

				// Añado a listado de procesos
				this.lista.add(job);
			}
			
			this.numPagina = 1;
			Collections.sort(this.lista, new Comparator<CProcesoConsulta>() {
				public int compare(CProcesoConsulta o1, CProcesoConsulta o2) {
					return (int)(o2.getId().longValue() - o1.getId().longValue());
				}
			});
		} catch (Exception e) {
			log.error("Error al obtener los procesos", e);
			this.resOperacion = "Ocurrio un problema al obtener los procesos";
		} finally {
			log.info(this.lista.size() + " Procesos recuperados");
		}
	}
	
	public void obtenerSalida() {
		byte[] source = null;
		
		try {
			this.resOperacion = "";
			log.info("Obteniendo SALIDA " + this.job.getId() + " ... ");
			log.info(" ---> " + this.job.getId() + " - " + this.job.getPrograma() + "." + this.job.getSalida() + " <--- ");
			source = this.ftp.getArchivo(FTPCliente.Tipo.SALIDA, this.job.getId());
			if (source == null) {
				this.resOperacion = "No se pudo obtener la salida " + this.job.getId();
				log.info(this.resOperacion);
				return;
			}

			log.info("SALIDA " + this.job.getId() + " obtenida. Enviando a httpSession ... ");
			if (this.job.getSalida() == null) {
				this.job.setSalida("xls");
				log.info("La SALIDA " + this.job.getId() + " no tiene formato especificado ... asigno formato: " + this.job.getSalida());
			}
			
			this.httpSession.setAttribute("contenido", source);
			this.httpSession.setAttribute("nombreDocumento", String.valueOf(this.job.getId()));
			this.httpSession.setAttribute("formato", this.job.getSalida());
			log.info("SALIDA " + this.job.getId() + " envianda ... PROCESO TERMINADO");
		} catch (Exception e) {
			log.error("Error al obtener la salida del job", e);
		}
	}
	
	public void obtenerLog() {
		byte[] source = null;
		
		try {
			this.resOperacion = "";
			source = this.ftp.getArchivo(FTPCliente.Tipo.LOG, this.job.getId());
			if (source == null) {
				this.resOperacion = "No se pudo obtener la salida " + this.job.getId();
				return;
			}
			
			if (this.job.getSalida() == null)
				this.job.setSalida(this.job.getPrograma() + "-" + this.job.getId());
			
			this.httpSession.setAttribute("contenido", source);
			this.httpSession.setAttribute("archivo", String.valueOf(this.job.getId()));
			this.httpSession.setAttribute("formato", "txt");
		} catch (Exception e) {
			log.error("Error al obtener la salida del job", e);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void pausaRanudarJob() {
		HashMap<String, String> map = new HashMap();
		String resultado = null;
		
		try {
			this.resOperacion = "";
			map.put("tipo", "R".equals(this.job.getPhase()) ? "000300" : "000400");
			map.put("idPeticion", this.job.getId().toString());
			
			Gson gson = new Gson();
			String comando = gson.toJson(map);
			
			this.sesion.getSocket().getOutputStream().write(comando.getBytes());
			this.sesion.getSocket().getOutputStream().flush();
			resultado = CSoctetStream.read(this.sesion.getSocket().getInputStream());
			log.info(resultado);
		} catch (Exception e) {
			log.error("Error en pausaRanudarJob", e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void cancelarJob() {
		HashMap<String, String> map = new HashMap();
		String resultado = null;

		try {
			this.resOperacion = "";
			map.put("tipo", "000200");
			map.put("idPeticion", this.job.getId().toString());
			
			Gson gson = new Gson();
			String comando = gson.toJson(map);
			
			this.sesion.getSocket().getOutputStream().write(comando.getBytes());
			this.sesion.getSocket().getOutputStream().flush();
			resultado = CSoctetStream.read(this.sesion.getSocket().getInputStream());
			
			log.info(resultado);
		} catch (Exception e) {
			log.error("Error en cancelarJob", e);
		}
	}

	// --------------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------------
	
	public String getResOperacion() {
		return this.resOperacion;
	}
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}
	
	public HashMap<String, Object> getParam() {
		return this.param;
	}
	
	public void setParam(HashMap<String, Object> param) {
		this.param = param;
	}
	
	public List<CProcesoConsulta> getLista() {
		return this.lista;
	}
	
	public void setLista(List<CProcesoConsulta> lista) {
		this.lista = lista;
	}
	
	public CProcesoConsulta getJob() {
		return this.job;
	}
	
	public void setJob(CProcesoConsulta job) {
		this.job = job;
	}
	
	public int getNumPagina() {
		return this.numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}
}
