package net.giro.plataforma.seguridad.beans;

import java.util.Date;

/**
 * d7729f32ba7
 * @author javaz
 *
 */
public class Perfil implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String perfil;
	long nivelPerfil;
	java.lang.String descripcion;
	java.lang.String tipoComponente;
	java.lang.String componenteSrc;
	java.util.Date validoDesde;
	java.util.Date validoHasta;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	net.giro.plataforma.seguridad.beans.Aplicacion aplicacion;


	public Perfil(long id, String perfil, long nivelPerfil, String descripcion,
			String tipoComponente, String componenteSrc, Date validoDesde,
			Date validoHasta, long creadoPor, Date fechaCreacion,
			long modificadoPor, Date fechaModificacion, Aplicacion aplicacion) {
		this.id = id;
		this.perfil = perfil;
		this.nivelPerfil = nivelPerfil;
		this.descripcion = descripcion;
		this.tipoComponente = tipoComponente;
		this.componenteSrc = componenteSrc;
		this.validoDesde = validoDesde;
		this.validoHasta = validoHasta;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.aplicacion = aplicacion;
	}

	public Perfil() {
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getPerfil() {
		return perfil;
	}

	public void setPerfil(java.lang.String perfil) {
		this.perfil = perfil;
	}

	public long getNivelPerfil() {
		return nivelPerfil;
	}

	public void setNivelPerfil(long nivelPerfil) {
		this.nivelPerfil = nivelPerfil;
	}

	public java.lang.String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(java.lang.String descripcion) {
		this.descripcion = descripcion;
	}

	public java.lang.String getTipoComponente() {
		return tipoComponente;
	}

	public void setTipoComponente(java.lang.String tipoComponente) {
		this.tipoComponente = tipoComponente;
	}

	public java.lang.String getComponenteSrc() {
		return componenteSrc;
	}

	public void setComponenteSrc(java.lang.String componenteSrc) {
		this.componenteSrc = componenteSrc;
	}

	public java.util.Date getValidoDesde() {
		return validoDesde;
	}

	public void setValidoDesde(java.util.Date validoDesde) {
		this.validoDesde = validoDesde;
	}

	public java.util.Date getValidoHasta() {
		return validoHasta;
	}

	public void setValidoHasta(java.util.Date validoHasta) {
		this.validoHasta = validoHasta;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public net.giro.plataforma.seguridad.beans.Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(net.giro.plataforma.seguridad.beans.Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}



}
