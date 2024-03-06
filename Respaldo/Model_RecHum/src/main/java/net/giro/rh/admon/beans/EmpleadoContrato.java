package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoContrato implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long idEmpleado;
	private Date fechaInicio;
	private Date fechaFin;
	private double sueldo;
	private Long peridiocidadPago;
	private int diaDescanso;
	private String centroTrabajo;
	private int determinado;
	private int estatus;
	private double descuentoInfonavit;
	private Date horaEntrada;
	private Date horaSalida;
	private Date horaEntradaComplemento;
	private Date horaSalidaComplemento;
	private Long formaPago;
	private int tipoHorario;
	private BigDecimal sueldoHora;
	private BigDecimal sueldoHoraExtra;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public EmpleadoContrato() {
		this.fechaInicio = Calendar.getInstance().getTime();
		this.fechaFin = Calendar.getInstance().getTime();
		this.sueldoHora = BigDecimal.ZERO;
		this.sueldoHoraExtra = BigDecimal.ZERO;
	}
		
	public EmpleadoContrato(Long id) {
		super();
		this.id = id;
		this.fechaInicio = Calendar.getInstance().getTime();
		this.fechaFin = Calendar.getInstance().getTime();
		this.sueldoHora = BigDecimal.ZERO;
		this.sueldoHoraExtra = BigDecimal.ZERO;
	}
	
	public EmpleadoContrato(Long id, Long idEmpleado, Date fechaInicio, Date fechaFin, double sueldo,
			Long peridiocidadPago, int diaDescanso, String centroTrabajo, int determinado, int estatus,
			double descuentoInfonavit, Date horaEntrada, Date horaSalida, Date horaEntradaComplemento,
			Date horaSalidaComplemento, Long formaPago, int tipoHorario, BigDecimal sueldoHora,
			BigDecimal sueldoHoraExtra) {
		super();
		this.id = id;
		this.idEmpleado = idEmpleado;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.sueldo = sueldo;
		this.peridiocidadPago = peridiocidadPago;
		this.diaDescanso = diaDescanso;
		this.centroTrabajo = centroTrabajo;
		this.determinado = determinado;
		this.estatus = estatus;
		this.descuentoInfonavit = descuentoInfonavit;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
		this.horaEntradaComplemento = horaEntradaComplemento;
		this.horaSalidaComplemento = horaSalidaComplemento;
		this.formaPago = formaPago;
		this.tipoHorario = tipoHorario;
		this.sueldoHora = sueldoHora;
		this.sueldoHoraExtra = sueldoHoraExtra;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setId(long id) {	//meotodo necesatio para tomar el correlativo
		this.id = id;
	}

	public Long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public double getSueldo() {
		return sueldo;
	}

	public void setSueldo(double sueldo) {
		this.sueldo = sueldo;
	}

	public Long getPeridiocidadPago() {
		return peridiocidadPago;
	}

	public void setPeridiocidadPago(Long peridiocidadPago) {
		this.peridiocidadPago = peridiocidadPago;
	}

	public int getDiaDescanso() {
		return diaDescanso;
	}

	public void setDiaDescanso(int diaDescanso) {
		this.diaDescanso = diaDescanso;
	}

	public String getCentroTrabajo() {
		return centroTrabajo;
	}

	public void setCentroTrabajo(String centroTrabajo) {
		this.centroTrabajo = centroTrabajo;
	}

	public int getDeterminado() {
		return determinado;
	}

	public void setDeterminado(int determinado) {
		this.determinado = determinado;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public double getDescuentoInfonavit() {
		return descuentoInfonavit;
	}

	public void setDescuentoInfonavit(double descuentoInfonavit) {
		this.descuentoInfonavit = descuentoInfonavit;
	}
	
	public Date getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(Date horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public Date getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(Date horaSalida) {
		this.horaSalida = horaSalida;
	}

	public Date getHoraEntradaComplemento() {
		return horaEntradaComplemento;
	}

	public void setHoraEntradaComplemento(Date horaEntradaComplemento) {
		this.horaEntradaComplemento = horaEntradaComplemento;
	}

	public Date getHoraSalidaComplemento() {
		return horaSalidaComplemento;
	}

	public void setHoraSalidaComplemento(Date horaSalidaComplemento) {
		this.horaSalidaComplemento = horaSalidaComplemento;
	}

	public Long getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(Long formaPago) {
		this.formaPago = formaPago;
	}

	public int getTipoHorario() {
		return tipoHorario;
	}

	public void setTipoHorario(int tipoHorario) {
		this.tipoHorario = tipoHorario;
	}


	
	public BigDecimal getSueldoHora() {
		return sueldoHora;
	}
	


	public void setSueldoHora(BigDecimal sueldoHora) {
		this.sueldoHora = sueldoHora;
	}

	public BigDecimal getSueldoHoraExtra() {
		return sueldoHoraExtra;
	}

	public void setSueldoHoraExtra(BigDecimal sueldoHoraExtra) {
		this.sueldoHoraExtra = sueldoHoraExtra;
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
