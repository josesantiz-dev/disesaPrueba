package net.giro.tyg.admon;

public class CatBancos implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String nombreCorto;
	java.lang.String nombreLargo;
	java.lang.Long creadoPor;
	java.util.Date fechaCreacion;
	java.lang.Long modificadoPor;
	java.util.Date fechaModificacion;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(java.lang.String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public java.lang.String getNombreLargo() {
		return nombreLargo;
	}

	public void setNombreLargo(java.lang.String nombreLargo) {
		this.nombreLargo = nombreLargo;
	}

	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public java.lang.Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(java.lang.Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

}
