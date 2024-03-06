package net.giro.plataforma.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class DiasFestivosNegociacion implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private DiasFestivos idDiaFestivo;
	private long idObra;
	private Date fecha;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	public DiasFestivosNegociacion() {
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

	public DiasFestivos getIdDiaFestivo() {
		return idDiaFestivo;
	}

	public void setIdDiaFestivo(DiasFestivos idDiaFestivo) {
		this.idDiaFestivo = idDiaFestivo;
	}

	public long getIdObra() {
		return idObra;
	}

	public void setIdObra(long idObra) {
		this.idObra = idObra;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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
	
}
