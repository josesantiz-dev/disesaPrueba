package net.giro.plataforma.seguridad.beans;

/**
 * a15364ea8cf
 * @author javaz
 *
 */
public class Aplicacion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String aplicacion;
	java.lang.Long creadoPor;
	java.util.Date fechaCreacion;
	java.lang.Long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String codigo;
	java.lang.String reportes;
	java.lang.String binarios;
	java.lang.String sql;
	java.lang.String imagenes;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(java.lang.String aplicacion) {
		this.aplicacion = aplicacion;
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

	public java.lang.String getCodigo() {
		return codigo;
	}

	public void setCodigo(java.lang.String codigo) {
		this.codigo = codigo;
	}

	public java.lang.String getReportes() {
		return reportes;
	}

	public void setReportes(java.lang.String reportes) {
		this.reportes = reportes;
	}

	public java.lang.String getBinarios() {
		return binarios;
	}

	public void setBinarios(java.lang.String binarios) {
		this.binarios = binarios;
	}

	public java.lang.String getSql() {
		return sql;
	}

	public void setSql(java.lang.String sql) {
		this.sql = sql;
	}

	public java.lang.String getImagenes() {
		return imagenes;
	}

	public void setImagenes(java.lang.String imagenes) {
		this.imagenes = imagenes;
	}

}
