package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Representacion de los valores de un renglon para carga de asistencia desde checador (trabajadores semanales) 
 * @author javaz
 */
public class ChecadorSemanalItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id; // id sistema checador
	private String empleado; // nombre trabajador en checador
	private String nss; // nombre trabajador en checador
	private Date fecha; // fecha de registro
	private Date horaEntradaMarcada; // hora entrada en checador
	private Date horaSalidaMarcada; // hora salida en checador
	private Date horaEntradaMarcadaComplemento; // hora entrada en checador
	private Date horaSalidaMarcadaComplemento; // hora salida en checador
	private Date horaEntrada; // hora entrada horario
	private Date horaSalida; // hora salida horario
	private Date horaEntradaComplemento; // hora entrada horario
	private Date horaSalidaComplemento; // hora salida horario
	private Date tiempoAsistido; // tiempo asistido en checador del dia
	private Long idAIR;
	private String observaciones;
	private int row;
	// -------------------------------------
	private boolean valido;
	private Long idEmpleado;
	private String nombreEmpleado;
	private int horasTrabajadas;
	private Date horasExtra;
	
	public ChecadorSemanalItem() {
		this.empleado = "";
		this.observaciones = "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmpleado() {
		return empleado;
	}

	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}

	public String getNss() {
		return nss;
	}

	public void setNss(String nss) {
		this.nss = nss;
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

	public Date getHoraEntradaMarcadaComplemento() {
		return horaEntradaMarcadaComplemento;
	}

	public void setHoraEntradaMarcadaComplemento(Date horaEntradaMarcadaComplemento) {
		this.horaEntradaMarcadaComplemento = horaEntradaMarcadaComplemento;
	}

	public Date getHoraSalidaMarcadaComplemento() {
		return horaSalidaMarcadaComplemento;
	}

	public void setHoraSalidaMarcadaComplemento(Date horaSalidaMarcadaComplemento) {
		this.horaSalidaMarcadaComplemento = horaSalidaMarcadaComplemento;
	}

	public Date getTiempoAsistido() {
		return tiempoAsistido;
	}

	public void setTiempoAsistido(Date tiempoAsistido) {
		this.tiempoAsistido = tiempoAsistido;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	// -------------------------------------------------------------------------------------------------------------------------
	
	public Long getIdAIR() {
		return idAIR;
	}

	public void setIdAIR(Long idAIR) {
		this.idAIR = idAIR;
	}

	public boolean isValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}
	
	// -------------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------------------------------------
	
	public void asignarDatos(EmpleadoContrato contrato) {
		asignarEmpleado(contrato.getIdEmpleado().getId(), contrato.getIdEmpleado().getNombre()); 
		this.horaEntrada = contrato.getHoraEntrada(); 
		this.horaSalida = contrato.getHoraSalida(); 
		this.horaEntradaComplemento = contrato.getHoraEntradaComplemento(); 
		this.horaSalidaComplemento = contrato.getHoraSalidaComplemento(); 
	}
	
	public void asignarEmpleado(Long idEmpleado, String nombreEmpleado) {
		this.idEmpleado = idEmpleado;
		this.nombreEmpleado = nombreEmpleado;
	}
	
	public void validaTiempoAsistido(int horasContrato, TimeZone timeZone) {
		GregorianCalendar timeEntrada = null;
		GregorianCalendar timeSalida = null;
		SimpleDateFormat dateFormatter = null;
		SimpleDateFormat formatter = null;
		String horaFormateada = "00:00";
		long horasComplemento = 0;
		long minutosComplemento = 0;
		long horas = 0;
		long minutos = 0;
		
		try {
			// calculamos el tiempo extra si no esta establecido
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			timeEntrada = new GregorianCalendar(timeZone);
			timeSalida = new GregorianCalendar(timeZone);
			
			timeEntrada.setTime(this.horaEntradaMarcada);
			timeSalida.setTime(this.horaSalidaMarcada);
			minutos = timeSalida.getTimeInMillis() - timeEntrada.getTimeInMillis();
			minutos = minutos / (1000 * 60);
			
			if (minutos <= 0) {
				horaFormateada = dateFormatter.format(this.fecha) + " 00:00:00";
				this.tiempoAsistido = formatter.parse(horaFormateada);
				this.horasExtra = formatter.parse(horaFormateada);
				this.horasTrabajadas = 0;
				return;
			}
			
			horas = 0;
			while (minutos >= 60) {
				horas += 1;
				minutos = minutos - 60;
				minutos = (minutos > 0 ? minutos : 0);
			}

			if (this.horaEntradaMarcadaComplemento != null && this.horaSalidaMarcadaComplemento != null) {
				timeEntrada.setTime(this.horaEntradaMarcadaComplemento);
				timeSalida.setTime(this.horaSalidaMarcadaComplemento);
				minutosComplemento = timeEntrada.getTimeInMillis() - timeSalida.getTimeInMillis();
				minutosComplemento = minutosComplemento / (1000 * 60);
				while (minutosComplemento >= 60) {
					horasComplemento += 1;
					minutosComplemento = minutosComplemento - 60;
					minutosComplemento = (minutosComplemento > 0 ? minutosComplemento : 0);
				}
				
				horas = horas - horasComplemento;
			}
			
			// Asignamos Tiempo Asistido 
			horaFormateada = dateFormatter.format(this.fecha) + " ";
			horaFormateada += ((horas < 10) ? "0" + horas : horas).toString() + ":" + ((minutos < 10) ? "0" + minutos : minutos).toString() + ":00";
			this.tiempoAsistido = formatter.parse(horaFormateada);
			this.horasTrabajadas = (new Long(horas)).intValue();

			horas = horas - horasContrato;
			horas = (horas > 0 ? horas : 0);
			minutos = (horas > 0 ? minutos : 0);
			
			// Asignamos el tiempo extra generado
			horaFormateada = dateFormatter.format(this.fecha) + " ";
			horaFormateada += ((horas < 10) ? "0" + horas : horas).toString() + ":" + ((minutos < 10) ? "0" + minutos : minutos).toString() + ":00";
			this.horasExtra = formatter.parse(horaFormateada);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ChecadorDetalle getAsistencia() {
		ChecadorDetalle asistencia = null;
		
		try {
			asistencia = new ChecadorDetalle();
			//asistencia.setId(this.id);
			//asistencia.setIdChecador(this.idChecador);
			asistencia.setIdEmpleado(this.idEmpleado);
			asistencia.setNombreEmpleado(this.nombreEmpleado);
			asistencia.setFecha(this.fecha);
			asistencia.setHoraEntrada(this.horaEntrada);
			asistencia.setHoraSalida(this.horaSalida);
			asistencia.setHoraEntradaComplemento(this.horaEntradaComplemento);
			asistencia.setHoraSalidaComplemento(this.horaSalidaComplemento);
			asistencia.setHoraEntradaMarcada(this.horaEntradaMarcada);
			asistencia.setHoraSalidaMarcada(this.horaSalidaMarcada);
			asistencia.setHoraEntradaMarcadaComplemento(this.horaEntradaMarcadaComplemento);
			asistencia.setHoraSalidaMarcadaComplemento(this.horaSalidaMarcadaComplemento);
			asistencia.setTiempoAsistido(this.tiempoAsistido);
			asistencia.setHorasTrabajadas(this.horasTrabajadas);
			asistencia.setHorasExtra(this.horasExtra);
			//asistencia.setHorasExtraAutorizadas(this.horasExtraAutorizadas);
			//asistencia.setUsuarioAutoriza(this.usuarioAutoriza);
			//asistencia.setCreadoPor(this.creadoPor);
			//asistencia.setFechaCreacion(this.fechaCreacion);
			//asistencia.setModificadoPor(this.modificadoPor);
			//asistencia.setFechaModificacion(this.fechaModificacion);
			//asistencia.setContratoInvalido(this.contratoInvalido);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return asistencia;
	}
}
