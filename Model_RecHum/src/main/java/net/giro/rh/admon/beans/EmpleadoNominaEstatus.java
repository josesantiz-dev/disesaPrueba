package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * empleado_nomina_estatus
 * @author javaz
 *
 */
public class EmpleadoNominaEstatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date fechaDesde;
	private Date fechaHasta;
	private int preliminar;
	private int estatus; // 0-Activo, 1-Cancelado, 2-Completo 
	private String mensaje;
	private long idEmpresa;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public EmpleadoNominaEstatus() {
		this.id = 0L;
		this.estatus = 0;
		this.mensaje = "";
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
	
	public Date getFechaDesde() {
		return fechaDesde;
	}
	
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	
	public Date getFechaHasta() {
		return fechaHasta;
	}
	
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	public int getPreliminar() {
		return preliminar;
	}
	
	public void setPreliminar(int preliminar) {
		this.preliminar = preliminar;
	}
	
	/**
	 * ESTATUS: 0-Activo, 1-Cancelado, 2-Completo
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}
	
	/**
	 * ESTATUS: 0-Activo, 1-Cancelado, 2-Completo
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
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

	// ----------------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------------
	
	public boolean preliminar() {
		return (this.preliminar == 1);
	}
}
