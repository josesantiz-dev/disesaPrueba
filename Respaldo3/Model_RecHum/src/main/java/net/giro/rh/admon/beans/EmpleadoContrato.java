package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * empleado_contrato
 * @author javaz
 *
 */
public class EmpleadoContrato implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Empleado idEmpleado;
	private Date fechaInicio;
	private Date fechaFin;
	private double sueldo;
	private long periodicidadPago;
	private int diaDescanso;
	private String centroTrabajo;
	private int determinado;
	private int estatus;
	private double descuentoInfonavit;
	private Date horaEntrada;
	private Date horaSalida;
	private Date horaEntradaComplemento;
	private Date horaSalidaComplemento;
	private long formaPago;
	private int tipoHorario;
	private BigDecimal sueldoHora;
	private BigDecimal sueldoHoraExtra;
	private int horasJornada;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public EmpleadoContrato() {
		this.id = 0L;
		this.centroTrabajo = "";
		this.sueldoHora = BigDecimal.ZERO;
		this.sueldoHoraExtra = BigDecimal.ZERO;
		this.fechaInicio = Calendar.getInstance().getTime();
		this.fechaFin = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.horasJornada = 8;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setId(long id) { // metodo necesatio para tomar el correlativo
		this.id = id;
	}

	public Empleado getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Empleado idEmpleado) {
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

	public long getPeriodicidadPago() {
		return periodicidadPago;
	}

	public void setPeriodicidadPago(long periodicidadPago) {
		this.periodicidadPago = periodicidadPago;
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
		if (horaEntrada != null)
			horasJornada();
	}

	public Date getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(Date horaSalida) {
		this.horaSalida = horaSalida;
		if (horaSalida != null)
			horasJornada();
	}

	public Date getHoraEntradaComplemento() {
		return horaEntradaComplemento;
	}

	public void setHoraEntradaComplemento(Date horaEntradaComplemento) {
		this.horaEntradaComplemento = horaEntradaComplemento;
		if (horaEntradaComplemento != null)
			horasJornada();
	}

	public Date getHoraSalidaComplemento() {
		return horaSalidaComplemento;
	}

	public void setHoraSalidaComplemento(Date horaSalidaComplemento) {
		this.horaSalidaComplemento = horaSalidaComplemento;
		if (horaSalidaComplemento != null)
			horasJornada();
	}

	public long getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(long formaPago) {
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

	public int getHorasJornada() {
		return this.horasJornada;
	}
	
	public void setHorasJornadas(int horasJornadas) {}

	// ---------------------------------------------------------------------------
	// PRIVADOS
	// ---------------------------------------------------------------------------
	
	private void horasJornada() {
		int jornada = 0;
		if (this.horaEntrada != null && this.horaSalida != null)
			jornada = calculaHorasJornada(this.horaEntrada, this.horaSalida);
		if (this.horaEntradaComplemento != null && this.horaSalidaComplemento != null)
			jornada += calculaHorasJornada(this.horaEntradaComplemento, this.horaSalidaComplemento);
		if (jornada <= 0)
			jornada = 8;
		this.horasJornada = jornada;
	}
	
	private int calculaHorasJornada(Date entrada, Date salida) {
		GregorianCalendar tEntrada = new GregorianCalendar(TimeZone.getTimeZone("America/Mazatlan"));
		GregorianCalendar tSalida = new GregorianCalendar(TimeZone.getTimeZone("America/Mazatlan"));
		long horas = 0;
		long minutos = 0;
		
		try {
			tEntrada.setTime(entrada);
			tSalida.setTime(salida);
			minutos = tSalida.getTimeInMillis() - tEntrada.getTimeInMillis();
			minutos = minutos / (1000 * 60);
			
			horas = 0;
			while (minutos >= 60) {
				horas += 1;
				minutos = minutos - 60;
				if (minutos < 0)
					minutos = 0;
			}
		} catch (Exception e) { 
			System.out.println(e.getMessage());
			horas = 0;
		}
		
		return (int) horas;
	}
}
