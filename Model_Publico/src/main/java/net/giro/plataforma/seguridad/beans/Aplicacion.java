package net.giro.plataforma.seguridad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * a15364ea8cf
 * @author javaz
 *
 */
public class Aplicacion implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String aplicacion;
	private String codigo;
	private String reportes;
	private String binarios;
	private String sql;
	private String imagenes;
	private String rutaManuales;
	private int listable; // LISTABLE: 0 - NO, 1 - SI
	private int estatus; // ESTATUS: 0 - Activo, 1 - ELiminado/Cancelado
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;

	public Aplicacion() {
		this.aplicacion = "";
		this.codigo = "";
		this.reportes = "";
		this.binarios = "";
		this.sql = "";
		this.imagenes = "";
		this.rutaManuales = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getReportes() {
		return reportes;
	}

	public void setReportes(String reportes) {
		this.reportes = reportes;
	}

	public String getBinarios() {
		return binarios;
	}

	public void setBinarios(String binarios) {
		this.binarios = binarios;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getImagenes() {
		return imagenes;
	}

	public void setImagenes(String imagenes) {
		this.imagenes = imagenes;
	}

	public String getRutaManuales() {
		return rutaManuales;
	}

	public void setRutaManuales(String rutaManuales) {
		this.rutaManuales = rutaManuales;
	}

	/**
	 * ESTATUS: 0 - Activo, 1 - Eliminado/Cancelado
	 * @return
	 */
	public int getListable() {
		return listable;
	}

	/**
	 * ESTATUS: 0 - Activo, 1 - Eliminado/Cancelado
	 * @param estatus
	 */
	public void setListable(int listable) {
		this.listable = listable;
	}

	/**
	 * ESTATUS: 0 - Activo, 1 - Eliminado/Cancelado
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * ESTATUS: 0 - Activo, 1 - Eliminado/Cancelado
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
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
}
