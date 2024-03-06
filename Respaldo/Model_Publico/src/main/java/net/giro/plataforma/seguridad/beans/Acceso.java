package net.giro.plataforma.seguridad.beans;

public class Acceso implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	net.giro.plataforma.seguridad.beans.Usuario usuario;
	java.util.Date inicio;
	java.util.Date terminacion;
	String terminal;
	String ipTerminal;
	net.giro.plataforma.seguridad.beans.Canal canal;
	net.giro.plataforma.seguridad.beans.Aplicacion aplicacion;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.util.Date getInicio() {
		return inicio;
	}

	public void setInicio(java.util.Date inicio) {
		this.inicio = inicio;
	}

	public java.util.Date getTerminacion() {
		return terminacion;
	}

	public void setTerminacion(java.util.Date terminacion) {
		this.terminacion = terminacion;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getIpTerminal() {
		return ipTerminal;
	}

	public void setIpTerminal(String ipTerminal) {
		this.ipTerminal = ipTerminal;
	}

	public net.giro.plataforma.seguridad.beans.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(net.giro.plataforma.seguridad.beans.Usuario usuario) {
		this.usuario = usuario;
	}

	public net.giro.plataforma.seguridad.beans.Canal getCanal() {
		return canal;
	}

	public void setCanal(net.giro.plataforma.seguridad.beans.Canal canal) {
		this.canal = canal;
	}

	public net.giro.plataforma.seguridad.beans.Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(
			net.giro.plataforma.seguridad.beans.Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	

}
