package net.giro.plataforma.beans;

public class DocumentoDigitalizado implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	net.giro.plataforma.beans.ConValores tipoDocumento;
	net.giro.plataforma.beans.ConValores documento;
	long tamano;
	java.lang.String extension;
	long numero;
	long version;


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

	public net.giro.plataforma.beans.ConValores getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(net.giro.plataforma.beans.ConValores tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public net.giro.plataforma.beans.ConValores getDocumento() {
		return documento;
	}

	public void setDocumento(net.giro.plataforma.beans.ConValores documento) {
		this.documento = documento;
	}

	public long getTamano() {
		return tamano;
	}

	public void setTamano(long tamano) {
		this.tamano = tamano;
	}

	public java.lang.String getExtension() {
		return extension;
	}

	public void setExtension(java.lang.String extension) {
		this.extension = extension;
	}

	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
