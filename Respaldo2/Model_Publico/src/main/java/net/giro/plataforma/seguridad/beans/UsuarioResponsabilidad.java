package net.giro.plataforma.seguridad.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * b2a12997b2b
 * @author javaz
 *
 */
public class UsuarioResponsabilidad implements Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	Date fechaCreacion;
	long modificadoPor;
	Date fechaModificacion;
	Date fechaIni;
	Date fechaTerm;
	Usuario usuario;
	Responsabilidad responsabilidad;
	PerfilAcceso perfilAcceso;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaTerm() {
		return fechaTerm;
	}

	public void setFechaTerm(Date fechaTerm) {
		this.fechaTerm = fechaTerm;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Responsabilidad getResponsabilidad() {
		return responsabilidad;
	}

	public void setResponsabilidad(Responsabilidad responsabilidad) {
		this.responsabilidad = responsabilidad;
	}

	public PerfilAcceso getPerfilAcceso() {
		return perfilAcceso;
	}

	public void setPerfilAcceso(PerfilAcceso perfilAcceso) {
		this.perfilAcceso = perfilAcceso;
	}

}
