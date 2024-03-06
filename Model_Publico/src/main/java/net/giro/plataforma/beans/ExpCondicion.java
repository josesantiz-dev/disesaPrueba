package net.giro.plataforma.beans;

/**
 * ed8491486c
 * @author javaz
 *
 */
public class ExpCondicion implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String nombre;
	java.lang.String tipoOp1;
	java.lang.String idOp1;
	java.lang.String tipoCondicion;
	java.lang.String tipoOp2;
	java.lang.String idOp2;
	java.util.Date fechaCreacion;
	java.util.Date fechaModificacion;
	long creadoPor;
	long modificadoPor;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getNombre() {
		return nombre;
	}

	public void setNombre(java.lang.String nombre) {
		this.nombre = nombre;
	}

	public java.lang.String getTipoOp1() {
		return tipoOp1;
	}

	public void setTipoOp1(java.lang.String tipoOp1) {
		this.tipoOp1 = tipoOp1;
	}

	public java.lang.String getIdOp1() {
		return idOp1;
	}

	public void setIdOp1(java.lang.String idOp1) {
		this.idOp1 = idOp1;
	}

	public java.lang.String getTipoCondicion() {
		return tipoCondicion;
	}

	public void setTipoCondicion(java.lang.String tipoCondicion) {
		this.tipoCondicion = tipoCondicion;
	}

	public java.lang.String getTipoOp2() {
		return tipoOp2;
	}

	public void setTipoOp2(java.lang.String tipoOp2) {
		this.tipoOp2 = tipoOp2;
	}

	public java.lang.String getIdOp2() {
		return idOp2;
	}

	public void setIdOp2(java.lang.String idOp2) {
		this.idOp2 = idOp2;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

}
