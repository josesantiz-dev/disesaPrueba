package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * recursos_documentos
 * @author javaz
 */
public class RecursosDocumentos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private int tipo; // 0 - Todos, 1 - Procesos, 2 - Formatos, 3 - DPs
	private String nombre;
	private String descripcion;
	private String extension;
	private String filename;
	private long idEmpresa;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public RecursosDocumentos() {
		this.nombre = "";
		this.descripcion = "";
		this.extension = "";
		this.filename = "";
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

	/**
	 * TIPO: 0 - Todos, 1 - Procesos, 2 - Formatos, 3 - DPs
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * TIPO: 0 - Todos, 1 - Procesos, 2 - Formatos, 3 - DPs
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
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
}
