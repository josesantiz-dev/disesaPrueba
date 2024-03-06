package net.giro.plataforma.seguridad.beans;

import java.util.Date;

/**
 * ce97a8b6460
 * @author javaz
 *
 */
public class MenuFuncion implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	long id;
	private net.giro.plataforma.seguridad.beans.Menu menu;
	private java.lang.Long promptId;
	private java.lang.String prompt;
	private java.lang.String tipoAccion;
	private long  tipoId;
	private java.lang.String descripcion;
	private java.lang.Long creadoPor;
	private java.util.Date fechaCreacion;
	private java.lang.Long modificadoPor;
	private java.util.Date fechaModificacion;

	public MenuFuncion(long id, Menu menu, Long promptId, String prompt,
			String tipoAccion, long tipoId, String descripcion, Long creadoPor,
			Date fechaCreacion, Long modificadoPor, Date fechaModificacion) {
		
		this.id = id;
		this.menu = menu;
		this.promptId = promptId;
		this.prompt = prompt;
		this.tipoAccion = tipoAccion;
		this.tipoId = tipoId;
		this.descripcion = descripcion;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
	}

	public MenuFuncion() {
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public net.giro.plataforma.seguridad.beans.Menu getMenu() {
		return menu;
	}

	public void setMenu(net.giro.plataforma.seguridad.beans.Menu menu) {
		this.menu = menu;
	}

	public java.lang.Long getPromptId() {
		return promptId;
	}

	public void setPromptId(java.lang.Long promptId) {
		this.promptId = promptId;
	}

	public java.lang.String getPrompt() {
		return prompt;
	}

	public void setPrompt(java.lang.String prompt) {
		this.prompt = prompt;
	}

	public java.lang.String getTipoAccion() {
		return tipoAccion;
	}

	public void setTipoAccion(java.lang.String tipoAccion) {
		this.tipoAccion = tipoAccion;
	}

	public long getTipoId() {
		return tipoId;
	}

	public void setTipoId(long tipoId) {
		this.tipoId = tipoId;
	}

	public java.lang.String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(java.lang.String descripcion) {
		this.descripcion = descripcion;
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
