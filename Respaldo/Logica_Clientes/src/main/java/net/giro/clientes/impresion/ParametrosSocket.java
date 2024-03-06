package net.giro.clientes.impresion;

import java.io.Serializable;
import java.util.HashMap;

public class ParametrosSocket implements Serializable
{
	private static final long serialVersionUID = 1L;
		
	private String socketIp;
	private int socketPort;	
	private String hostFTP;
	private String userFTP;
	private String pswFTP;
	private Integer portFTP;
	private int intentos;
	private String nombreDocumento;
	private String programaId;
	private String aplicacionId;
	
	private String tipo;
	private String usuario;
	private String rol;
	private String delUsuario;
	private String delRol;
	private Long codigo;
	
	private HashMap<String, String> parametrosReporte;
	
	public ParametrosSocket()
	{
		String ssocketPort;
		String sintentos;

		codigo= 0L;
		parametrosReporte = new HashMap<String, String>();
		socketIp= "192.168.11.234";
		ssocketPort= "2099";
		hostFTP= "192.168.11.252";
		userFTP= "loginuser";
		pswFTP= "2244edc1";
		portFTP = 21;
		sintentos= "40";

		if (socketIp== null || "".equals(socketIp)) {
			//codigo= Errores.ERROR_OBTENER_PARAMETROS_CONFIG;
		}
		if (ssocketPort== null || "".equals(ssocketPort)) {
			//codigo= Errores.ERROR_OBTENER_PARAMETROS_CONFIG;
		}		
		if (hostFTP== null || "".equals(hostFTP)) {
			//codigo= Errores.ERROR_OBTENER_PARAMETROS_CONFIG;
		}
		if (userFTP== null || "".equals(userFTP)) {
			//codigo= Errores.ERROR_OBTENER_PARAMETROS_CONFIG;
		}		
		if (pswFTP== null || "".equals(pswFTP)) {
			//codigo= Errores.ERROR_OBTENER_PARAMETROS_CONFIG;
		}

		socketPort = (int)Integer.parseInt(ssocketPort);
		intentos = (int)Integer.parseInt(sintentos);	
	}
	
	public String getSocketIp() {
		return socketIp;
	}

	public int getSocketPort() {
		return socketPort;
	}

	public String getHostFTP() {
		return hostFTP;
	}

	public String getUserFTP() {
		return userFTP;
	}

	public String getPswFTP() {
		return pswFTP;
	}
	
	public Integer getPortFTP() {
		return portFTP;
	}

	public void setPortFTP(Integer portFTP) {
		this.portFTP = portFTP;
	}

	public int getIntentos() {
		return intentos;
	}

	public String getNombreDocumento() {
		return nombreDocumento;
	}

	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}

	public String getAplicacionId() {
		return aplicacionId;
	}
	
	public void setAplicacionId(String aplicacionId) {
		this.aplicacionId = aplicacionId;
	}
	
	public String getProgramaId() {
		return programaId;
	}
	
	public void setProgramaId(String programaId) {
		this.programaId = programaId;
	}	
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getRol() {
		return rol;
	}
	
	public void setRol(String rol) {
		this.rol = rol;
	}
	
	public String getDelUsuario() {
		return delUsuario;
	}
	
	public void setDelUsuario(String delUsuario) {
		this.delUsuario = delUsuario;
	}
	
	public String getDelRol() {
		return delRol;
	}
	
	public void setDelRol(String delRol) {
		this.delRol = delRol;
	}

	public void Add(String key, String value) {
		parametrosReporte.put(key, value);
	}
	
	public HashMap<String, String> getParametrosReporte() {
		return parametrosReporte;
	}

	public void setParametrosReporte(HashMap<String, String> parametrosReporte) {
		this.parametrosReporte = parametrosReporte;
	}

	public Long getCodigo() {
		return codigo;
	}
	
}

/**  !ParametrosSocket.java */


