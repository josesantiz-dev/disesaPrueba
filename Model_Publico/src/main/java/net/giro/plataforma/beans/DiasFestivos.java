package net.giro.plataforma.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * dias_festivos
 * @author javaz
 *
 */
public class DiasFestivos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private int dia;
	private int mes;
	private String descripcion;
	private double factor;
	private int frecuencia;
	private int feriado;
	private long idEmpresa;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public DiasFestivos() {
		this.descripcion = "";
		this.factor = 2;
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

	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public String getDescripcion() {
		if (this.descripcion == null)
			this.descripcion = "";
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		if (descripcion == null)
			descripcion = "";
		this.descripcion = descripcion;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	public int getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(int frecuencia) {
		this.frecuencia = frecuencia;
	}

	public int getFeriado() {
		return feriado;
	}

	public void setFeriado(int feriado) {
		if (feriado == 0 && this.dia > 0)
			feriado = this.dia;
		this.feriado = feriado;
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

	// ----------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------
	
	public Date getDiaFestivo() {
		try {
			this.dia = (this.dia > 0 ? this.dia : 1);
			return (new SimpleDateFormat("dd/MM/yyyy")).parse(this.dia + "/" + this.mes + "/" + Calendar.getInstance().get(Calendar.YEAR));
		} catch (Exception e) {
			return Calendar.getInstance().getTime();
		}
	}

	public void setDiaFestivo(Date diaFestivo) {
		Calendar calendar = null;
		
		try {
			calendar = Calendar.getInstance();
			calendar.setTime(diaFestivo);
			this.dia = calendar.get(Calendar.DAY_OF_MONTH);
			this.mes = calendar.get(Calendar.MONTH) + 1;
			this.feriado = (this.feriado == 0 && this.dia > 0 ? this.dia : this.feriado);
		} catch (Exception e) {
			this.dia = 1;
			this.mes = 1;
		}
	}
	
	public Date getDiaFeriado() {
		try {
			if (this.feriado <= 0 || this.feriado == this.dia)
				return getDiaFestivo();
			return (new SimpleDateFormat("dd/MM/yyyy")).parse(this.feriado + "/" + this.mes + "/" + Calendar.getInstance().get(Calendar.YEAR));
		} catch (Exception e) {
			return Calendar.getInstance().getTime();
		}
	}

	public void setDiaFeriado(Date diaFeriado) {}
}
