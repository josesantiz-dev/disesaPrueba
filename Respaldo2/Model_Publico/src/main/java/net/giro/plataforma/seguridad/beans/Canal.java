package net.giro.plataforma.seguridad.beans;

/**
 * ead2b0079d
 * @author javaz
 *
 */
public class Canal {

	long id;
	String canal;
	String tipoCanal;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCanal() {
		return canal;
	}
	public void setCanal(String canal) {
		this.canal = canal;
	}
	public String getTipoCanal() {
		return tipoCanal;
	}
	public void setTipoCanal(String tipoCanal) {
		this.tipoCanal = tipoCanal;
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
	
}
