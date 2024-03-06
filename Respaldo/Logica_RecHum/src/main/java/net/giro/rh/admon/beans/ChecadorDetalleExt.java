package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ChecadorDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private ChecadorExt idChecador; // Verificar ChecadorExt
	private EmpleadoExt idEmpleado;
	private String nombreEmpleado;
	private Date fecha;
	private Date horaEntrada;
	private Date horaSalida;
	private Date horaEntradaMarcada;
	private Date horaSalidaMarcada;
	private Date tiempoAsistido;
	private int horasTrabajadas;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private Date horasExtra;
	private Date horasExtraAutorizadas;
	private long usuarioAutoriza;
	
	
	public ChecadorDetalleExt() {
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.horaEntrada = Calendar.getInstance().getTime();
		this.horaSalida = Calendar.getInstance().getTime();
		this.horaEntradaMarcada = Calendar.getInstance().getTime();
		this.horaSalidaMarcada = Calendar.getInstance().getTime();
		this.tiempoAsistido = Calendar.getInstance().getTime();
		this.horasExtra = Calendar.getInstance().getTime();
		this.horasExtraAutorizadas = Calendar.getInstance().getTime();
	}
	
	public ChecadorDetalleExt(Long id) {
		super();
		this.id = id;
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.horaEntrada = Calendar.getInstance().getTime();
		this.horaSalida = Calendar.getInstance().getTime();
		this.horaEntradaMarcada = Calendar.getInstance().getTime();
		this.horaSalidaMarcada = Calendar.getInstance().getTime();
		this.tiempoAsistido = Calendar.getInstance().getTime();
		this.horasExtra = Calendar.getInstance().getTime();
		this.horasExtraAutorizadas = Calendar.getInstance().getTime();
	}

	public ChecadorDetalleExt(Long id, ChecadorExt idChecador, EmpleadoExt idEmpleado, Date fecha, Date horaEntrada, Date horaSalida,
			Date horaEntradaMarcada, Date horaSalidaMarcada, Date tiempoAsistido, int horasTrabajadas, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion, 
			String nombreEmpleado, Date horasExtra, Date horasExtraAutorizadas, long usuarioAutoriza) {
		super();
		this.id = id;
		this.idChecador = idChecador;
		this.idEmpleado = idEmpleado;
		this.fecha = fecha;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
		this.horaEntradaMarcada = horaEntradaMarcada;
		this.horaSalidaMarcada = horaSalidaMarcada;
		this.tiempoAsistido = tiempoAsistido;
		this.horasTrabajadas = horasTrabajadas;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.nombreEmpleado = nombreEmpleado;
		this.horasExtra = horasExtra;
		this.horasExtraAutorizadas = horasExtraAutorizadas;
		this.usuarioAutoriza = usuarioAutoriza;
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

	public ChecadorExt getIdChecador() {
		return idChecador;
	}

	public void setIdChecador(ChecadorExt idChecador) {
		this.idChecador = idChecador;
	}

	public EmpleadoExt getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(EmpleadoExt idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public int getHorasTrabajadas() {
		return horasTrabajadas;
	}

	public void setHorasTrabajadas(int horasTrabajadas) {
		this.horasTrabajadas = horasTrabajadas;
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

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}

	public Date getHoraEntradaMarcada() {
		return horaEntradaMarcada;
	}

	public void setHoraEntradaMarcada(Date horaEntradaMarcada) {
		this.horaEntradaMarcada = horaEntradaMarcada;
	}

	public Date getHoraSalidaMarcada() {
		return horaSalidaMarcada;
	}

	public void setHoraSalidaMarcada(Date horaSalidaMarcada) {
		this.horaSalidaMarcada = horaSalidaMarcada;
	}

	public Date getTiempoAsistido() {
		return tiempoAsistido;
	}

	public void setTiempoAsistido(Date tiempoAsistido) {
		this.tiempoAsistido = tiempoAsistido;
	}

	public Date getHorasExtra() {
		return horasExtra;
	}

	public void setHorasExtra(Date horasExtra) {
		this.horasExtra = horasExtra;
	}

	public Date getHorasExtraAutorizadas() {
		return horasExtraAutorizadas;
	}

	public void setHorasExtraAutorizadas(Date horasExtraAutorizadas) {
		this.horasExtraAutorizadas = horasExtraAutorizadas;
	}

	public long getUsuarioAutoriza() {
		return usuarioAutoriza;
	}

	public void setUsuarioAutoriza(long usuarioAutoriza) {
		this.usuarioAutoriza = usuarioAutoriza;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */