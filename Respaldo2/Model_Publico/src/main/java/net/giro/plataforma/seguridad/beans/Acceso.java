package net.giro.plataforma.seguridad.beans;

import java.util.Date;

/**
 * df3c8761e3a
 * @author javaz
 */
public class Acceso implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private Usuario usuario;
	private Date inicio;
	private Date terminacion;
	private String terminal;
	private String ipTerminal;
	private Canal canal;
	private Aplicacion aplicacion;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getTerminacion() {
		return terminacion;
	}

	public void setTerminacion(Date terminacion) {
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Canal getCanal() {
		return canal;
	}

	public void setCanal(Canal canal) {
		this.canal = canal;
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(
			Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}
}
