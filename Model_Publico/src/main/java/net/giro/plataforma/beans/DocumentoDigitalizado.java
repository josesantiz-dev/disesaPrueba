package net.giro.plataforma.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * e010c5bea9
 * @author javaz
 *
 */
public class DocumentoDigitalizado implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private ConValores tipoDocumento;
	private ConValores documento;
	private long tamano;
	private String extension;
	private long numero;
	private long version;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;


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

	public ConValores getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(ConValores tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public ConValores getDocumento() {
		return documento;
	}

	public void setDocumento(ConValores documento) {
		this.documento = documento;
	}

	public long getTamano() {
		return tamano;
	}

	public void setTamano(long tamano) {
		this.tamano = tamano;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
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
