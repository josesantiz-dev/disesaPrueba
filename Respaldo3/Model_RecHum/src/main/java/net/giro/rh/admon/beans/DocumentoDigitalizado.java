package net.giro.rh.admon.beans;

import java.util.Calendar;
import java.util.Date;

//Este tenia relacion a la tabla convalores
public class DocumentoDigitalizado implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long tamano;
	private String extension;
	private Long numero;
	private Long version;
	private Long creadoPor;		
	private Date fechaCreacion;		
	private Long modificadoPor;
	private Date fechaModificacion;
	//private ConValores tipoDocumento;
	//private ConValores documento;
	// Constructors
	
	public DocumentoDigitalizado() {
		this.id = 0L;
		this.tamano = 0L;
		this.numero = 0L;
		this.version = 0L;
		this.extension = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public DocumentoDigitalizado(Long id, Long creadoPor, Date fechaCreacion, Long modificadoPor, Date fechaModificacion, Long tamano,
			String extension, Long numero, Long version) {
		this.id = id;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.tamano = tamano;
		this.extension = extension;
		this.numero = numero;
		this.version = version;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Long getTamano() {
		return tamano;
	}

	public void setTamano(Long tamano) {
		this.tamano = tamano;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	//public DocumentoDigitalizado(Long aa, Long ab, Date ac, Long ad, Date ae/*, ConValores tipoDocumento*/) {

}