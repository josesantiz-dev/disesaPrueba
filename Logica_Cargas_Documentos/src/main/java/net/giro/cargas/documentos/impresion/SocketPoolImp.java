package net.giro.cargas.documentos.impresion;

import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import net.giro.comun.Errores;

public class SocketPoolImp {
	private static Logger log = Logger.getLogger(SocketPoolImp.class);
	protected ParametrosSocket paramSocket;
	protected Long ID;
	protected Resultado resultado;
	enum Tipo { SALIDA, LOG };
	
	public Long getID() {
		return ID;
	}
	
	public Resultado getResultado() {
		return resultado;
	}
	
	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}
	
	public void setParamSocket(ParametrosSocket paramSocket) {
		this.paramSocket = paramSocket;
	}
	
	public void SocketPoolImpresion() {		
		ID = 0L;
		resultado = new Resultado();		
		if (paramSocket!= null) {
			try {
				Socket socket = new Socket(paramSocket.getSocketIp(), paramSocket.getSocketPort());								
				resultado= DocumentWord(socket);				
				if (resultado.getCodigo()== 0L) {
					ID = resultado.getID();
					resultado= WaitSock();
				}				
			} catch (Exception e) {
				resultado.setCodigo(Errores.ERROR_INESPERADO);				
				resultado.setError_descr(e.toString());
				log.error(String.format("SocketPoolImp::SocketPoolImpresion  Exception: %s", e.toString() ) );
				e.printStackTrace();
			}
		} else {
			resultado.setCodigo(Errores.ERROR_INESPERADO);		
			log.error("Error en SocketPoolImp::ResultadoJob Parametros de conexion socket vacios");
		}		
	}
	
	private Resultado ResultadoJob(String json) {
		Resultado resultadoC;
		RespuestaCrearJob resp;
		Gson gson;
		Long loperacion;
		
		gson = new Gson();
		resultadoC= new Resultado();
		json= json.trim();
		try {
			if (json!= null && !"".equals(json)) {

				resp = gson.fromJson(json, RespuestaCrearJob.class);
				loperacion = "OK".equals(resp.getMensaje()) ? 0L : 1L;

				resultadoC.setCodigo(loperacion);
				resultadoC.setError_descr(resp.getMensaje());			
				resultadoC.setID(resp.getID());			
			}
		} catch (Exception ex) {
			resultadoC.setCodigo(Errores.ERROR_INESPERADO);
			resultadoC.setError_descr(ex.toString());
			log.error(String.format("Error en SocketPoolImp::ResultadoJob : %s", ex.toString() ) );
		}
		
		return resultadoC;
	}
	
	private Resultado ResultadoConsulta(String json) {
		Resultado resultadoC;
		Gson gson;
		RespuestaProcesos  respuesta;
		CProcesoConsulta job;
		Long fase;

		fase = -1L;
		gson = new Gson();
		resultadoC = new Resultado();
		job = new CProcesoConsulta();
		json = json.trim();
		try {
			if (json!= null && !"".equals(json)) {
				respuesta = gson.fromJson(json, RespuestaProcesos.class);	
				for (Map.Entry<String, HashMap<String, Object>> m :   respuesta.getProceso().entrySet()) { 
					job.setPrograma((String)m.getValue().get("programa"));
					job.setStatus((String)m.getValue().get("status"));
					job.setPriority(Double.valueOf(m.getValue().get("priority").toString()).intValue());
					job.setPhase((String)m.getValue().get("phase"));

					if (m.getValue().get("nextFireTime") != null) {
						job.setNextFireTime( new Date(Double.valueOf(m.getValue().get("nextFireTime").toString()).longValue())); // gson intenta castear a double el tipo long :/
					}
					if (m.getValue().get("endTime") != null && Double.valueOf(m.getValue().get("endTime").toString()) > 0) {
						job.setEndTime( new Date(Double.valueOf(m.getValue().get("endTime").toString()).longValue()));
					}
					job.setStartTime(new Date(Double.valueOf(m.getValue().get("startTime").toString()).longValue()));
					job.setId(Double.valueOf(m.getValue().get("id").toString()).longValue());

					if (m.getValue().get("prevFireTime") != null && Double.valueOf(m.getValue().get("prevFireTime").toString()) > 0) {
						job.setPrevFireTime( new Date(Double.valueOf(m.getValue().get("prevFireTime").toString()).longValue()));
					}
				}

				if ("Q".equals(job.getPhase()) || "R".equals(job.getPhase())) 
					fase = 1L;
				if ("T".equals(job.getPhase())) {
					fase = 2L;
					if ("N".equals(job.getStatus())) 
						fase = 0L;
				}		

				resultadoC.setCodigo(fase);
				resultadoC.setError_descr(job.getPhaseCompleto());
				resultadoC.setID(ID);			
			}
		} catch (Exception ex) {
			resultadoC.setCodigo(Errores.ERROR_INESPERADO);
			resultadoC.setError_descr(ex.toString());
			log.error(String.format("SocketPoolImp::ResultadoConsulta  Exception: %s", ex.toString() ) );
		}
		
		return resultadoC;
	}
	
	private Resultado DocumentWord(Socket socket) throws Exception {
		HashMap<String, Object> map =  new HashMap<String, Object>();
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		HashMap<String, Object> programacion = new HashMap<String, Object>();
		Resultado resultadoC;
		String  keyParam = "";
		String  valParam = "";
		String json = "";
		
		resultadoC = new Resultado(); 		

		try {
			map.put("tipo", "000100");
			map.put("idAplicacion", paramSocket.getAplicacionId());
			map.put("idPrograma", paramSocket.getProgramaId());
			map.put("usuario", paramSocket.getUsuario());
			map.put("role", paramSocket.getRol());

			for (Map.Entry<String, String> p : paramSocket.getParametrosReporte().entrySet()) {
				keyParam= p.getKey();
				valParam= p.getValue();			
				parametros.put(String.valueOf(keyParam), String.valueOf(valParam));	
			}

			map.put( "parametros", parametros);
			map.put("programacion", programacion);

			Gson gson = new Gson();
			String comando = gson.toJson(map);
			socket.getOutputStream().write(comando.getBytes());

			byte[] b = new byte[4096];
			socket.getInputStream().read(b);
			json= new String(b);			
			System.out.println(json);
			resultadoC= ResultadoJob(json);
			
			if (resultadoC.getID()== null) {
				resultadoC.setCodigo(Errores.ERROR_INESPERADO);
			}			
		} catch (Exception ex) {
			resultadoC.setCodigo(Errores.ERROR_INESPERADO);
			resultadoC.setError_descr(ex.toString());
			log.error(String.format("SocketPoolImp::DocumentWord  Exception: %s", ex.toString() ) );
		}
		
		return resultadoC;
	}	
	
	private Resultado procesosById(Socket socket, Long id) throws Exception {
		HashMap<String, Object> map =  new HashMap<String, Object>();
		String json;
		Resultado resultadoC;

		resultadoC = new Resultado();

		try {
			map.put("tipo",  paramSocket.getTipo());
			map.put("usuario", paramSocket.getUsuario());
			map.put("role", paramSocket.getRol());
			map.put("ID", String.valueOf(ID));

			Gson gson = new Gson();
			String comando = gson.toJson(map);

			socket.getOutputStream().write(comando.getBytes());

			byte[] b = new byte[4096];
			socket.getInputStream().read(b);
			json = new String(b);
			System.out.println(json);

			resultadoC = ResultadoConsulta(json);
		} catch (Exception ex) {
			resultadoC.setCodigo(Errores.ERROR_INESPERADO);
			resultadoC.setError_descr(ex.toString());
			log.error(String.format("SocketPoolImp::procesosById  Exception: %s", ex.toString() ) );
		}
		
		return resultadoC;		
	}
	
	private Resultado WaitSock() {
		Resultado resultado_c;
		@SuppressWarnings("unused")
		Resultado resultado_l;
		int cont= 0;
		
		resultado_c= new Resultado(); 
		resultado_l= new Resultado();
		try {
			while (cont < paramSocket.getIntentos()) {
				Socket socket = new Socket(paramSocket.getSocketIp(), paramSocket.getSocketPort());	
				resultado_c= procesosById(socket,ID);
				// 1: En cola o ejecucion
				if (resultado_c.getCodigo()== 0L || resultado_c.getCodigo()== 2L) {
					resultado_c.setCodigo(0L);
					break;
				}				
				Thread.sleep(1000);
				cont ++;
			}
			
			if (resultado_c.getCodigo()==0L) {
				resultado_c= ObtenDocumento();
			}
		} catch (Exception ex) {
			resultado_c.setCodigo(Errores.ERROR_INESPERADO);
			resultado_c.setError_descr(ex.toString());
			log.error(String.format("SocketPoolImp::WaitSock  Exception: %s", ex.toString() ) );
		}
		
		return resultado_c;
	}
	
	private void AgregaSession(byte[] streamBytes,  String attr) {
		/*FacesContext fc = null;
		Application app = null;
		HttpSession httpSession = null;
		ValueExpression dato = null;
	
		fc = FacesContext.getCurrentInstance();
		app = fc.getApplication();
		dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{sesionPlataforma}", SesionPlataforma.class);
		httpSession = (HttpSession) fc.getExternalContext().getSession(false);		
		httpSession.setAttribute(attr, streamBytes);
		httpSession.setAttribute("nombreDocumento",  String.format("%s.%s", ID, paramSocket.getNombreDocumento()));		
		*/
	}
	
	public Resultado ObtenLog() {
		Resultado resultadoC;
		Ftp   ftp;
		boolean hr;
		
		hr = false;
		ftp = new Ftp();
		resultadoC = new Resultado();
		
		try {
			ftp.setID(ID);
			ftp.setHostFtp(paramSocket.getHostFTP());
			ftp.setUserFTP(paramSocket.getUserFTP());
			ftp.setPswFTP(paramSocket.getPswFTP());
			ftp.setPortFTP(paramSocket.getPortFTP());
			hr = ftp.FTPObtenLog();
			if (hr) {
				//AgregaSession(ftp.getResultadoDoc(), "log");	
			} else {
				resultadoC.setCodigo(Errores.ERROR_INESPERADO);
			}
			
		} catch (Exception ex) {
			resultadoC.setCodigo(Errores.ERROR_INESPERADO);
			resultadoC.setError_descr(ex.toString());
			log.error(String.format("SocketPoolImp::ObtenLog  Exception: %s", ex.toString()));
		}
		
		return resultadoC;
	}
	
	public Resultado ObtenDocumento() {
		Resultado resultadoC;
		Ftp   ftp;
		boolean hr;
		
		ftp = new Ftp();
		resultadoC= new Resultado();
		
		try {
			ftp.setID(ID);
			ftp.setHostFtp(paramSocket.getHostFTP());
			ftp.setUserFTP(paramSocket.getUserFTP());
			ftp.setPswFTP(paramSocket.getPswFTP());
			ftp.setPortFTP(paramSocket.getPortFTP());
			hr= ftp.FTPObtenDocumento();
			if (hr) {
				AgregaSession(ftp.getResultadoDoc(), "documento");
			} else {
				resultadoC.setCodigo(Errores.ERROR_INESPERADO);
			}
		} catch (Exception ex) {
			resultadoC.setCodigo(Errores.ERROR_INESPERADO);
			resultadoC.setError_descr(ex.toString());
			log.error(String.format("SocketPoolImp::ObtenDocumento  Exception: %s", ex.toString() ) );
		}
		
		return resultadoC;
	}
}
