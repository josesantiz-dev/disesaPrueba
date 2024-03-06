package net.giro.plataforma.seguridad.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * f6b18386375
 * @author javaz
 *
 */
public class Responsabilidad implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String responsabilidad;
	private String descripcion;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private long grupoReporteId;
	private Menu menu;
	private Aplicacion aplicacion;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getResponsabilidad() {
		return responsabilidad;
	}

	public void setResponsabilidad(String responsabilidad) {
		this.responsabilidad = responsabilidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public long getGrupoReporteId() {
		return grupoReporteId;
	}

	public void setGrupoReporteId(long grupoReporteId) {
		this.grupoReporteId = grupoReporteId;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

}
