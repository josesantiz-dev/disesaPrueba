package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * recursos_documentos_modulos
 * @author javaz
 */
public class RecursosDocumentosModulos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idRecursoDocumento;
	private long idModulo;
	private String moduloNombre;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public RecursosDocumentosModulos() {
		this.moduloNombre = "";
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

	public long getIdRecursoDocumento() {
		return idRecursoDocumento;
	}

	public void setIdRecursoDocumento(long idRecursoDocumento) {
		this.idRecursoDocumento = idRecursoDocumento;
	}

	public long getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(long idModulo) {
		this.idModulo = idModulo;
	}

	public String getModuloNombre() {
		return moduloNombre;
	}

	public void setModuloNombre(String moduloNombre) {
		this.moduloNombre = moduloNombre;
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
