package net.giro.plataforma.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * manuales_procesos
 * @author javaz
 *
 */
public class ManualesProcesos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String formato;
	private String descripcion;
	private String nombreArchivo;
	private String storageFileName;
	private long tamano;
	private String extension;
	private long version;
	private long parent;
	private long idEmpresa;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	// Variables no mapeadas ----------
	private byte[] fileSrc;
	
	public ManualesProcesos() {
		this.formato = "";
		this.nombreArchivo = "";
		this.descripcion = "";
		this.extension = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
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

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getStorageFileName() {
		return this.storageFileName;
	}

	public void setStorageFileName(String storageFileName) {
		this.storageFileName = storageFileName;
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

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
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

	// --------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------

	/**
	 * Documento/Archivo digitalizado
	 * @return
	 */
	public byte[] getFileSrc() {
		return fileSrc;
	}

	/**
	 * Documento/Archivo digitalizado
	 * @param fileSrc
	 */
	public void setFileSrc(byte[] fileSrc) {
		this.fileSrc = fileSrc;
	}
}
