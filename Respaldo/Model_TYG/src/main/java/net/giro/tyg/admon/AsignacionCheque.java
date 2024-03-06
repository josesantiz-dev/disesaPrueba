package net.giro.tyg.admon;

public class AsignacionCheque implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	java.lang.Long id;
	java.lang.Long sucursalId;
	java.lang.Long bancoId;
	java.lang.Long folioIni;
	java.lang.Long folioFin;
	java.lang.String tipo;
	java.lang.Long creadoPor;
	java.util.Date fechaCreacion;
	java.lang.Long modificadoPor;
	java.util.Date fechaModificacion;


	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.Long getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(java.lang.Long sucursalId) {
		this.sucursalId = sucursalId;
	}

	public java.lang.Long getBancoId() {
		return bancoId;
	}

	public void setBancoId(java.lang.Long bancoId) {
		this.bancoId = bancoId;
	}

	public java.lang.Long getFolioIni() {
		return folioIni;
	}

	public void setFolioIni(java.lang.Long folioIni) {
		this.folioIni = folioIni;
	}

	public java.lang.Long getFolioFin() {
		return folioFin;
	}

	public void setFolioFin(java.lang.Long folioFin) {
		this.folioFin = folioFin;
	}

	public java.lang.String getTipo() {
		return tipo;
	}

	public void setTipo(java.lang.String tipo) {
		this.tipo = tipo;
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
