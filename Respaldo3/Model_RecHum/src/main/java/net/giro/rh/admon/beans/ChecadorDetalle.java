package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * checador_detalle
 * @author javaz
 *
 */
public class ChecadorDetalle implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Checador idChecador;
	private Long idEmpleado;
	private String nombreEmpleado;
	private Date fecha;
	private Date horaEntrada;
	private Date horaSalida;
	private Date horaEntradaComplemento;
	private Date horaSalidaComplemento;
	private Date horaEntradaMarcada;
	private Date horaSalidaMarcada;
	private Date horaEntradaMarcadaComplemento;
	private Date horaSalidaMarcadaComplemento;
	private Date tiempoAsistido;
	private int horasTrabajadas;
	private Date horasExtra;
	private Date horasExtraAutorizadas;
	private long usuarioAutoriza;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private boolean contratoInvalido;
	
	public ChecadorDetalle() {
		this.id = 0L;
		this.nombreEmpleado = "";
		this.fecha = Calendar.getInstance().getTime();
		this.horaEntrada = Calendar.getInstance().getTime();
		this.horaSalida = Calendar.getInstance().getTime();
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

	public Checador getIdChecador() {
		return idChecador;
	}

	public void setIdChecador(Checador idChecador) {
		this.idChecador = idChecador;
	}

	public Long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
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

	public int getHorasTrabajadas() {
		return horasTrabajadas;
	}

	public void setHorasTrabajadas(int horasTrabajadas) {
		this.horasTrabajadas = horasTrabajadas;
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

	public boolean isContratoInvalido() {
		return contratoInvalido;
	}

	public void setContratoInvalido(boolean contratoInvalido) {
		this.contratoInvalido = contratoInvalido;
	}

	// ---------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------
	
	public String getEntrada() {
		return (this.horaEntradaMarcada != null ? (new SimpleDateFormat("HH:mm")).format(this.horaEntradaMarcada) : "00:00");
	}
	
	public void setEntrada(String value) {
		SimpleDateFormat formatter = null;
		String strFecha = "";
		
		try {
			value = (value == null || "".equals(value.trim()) ? "00:00" : value.trim());
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			strFecha = formatter.format(this.fecha) + " " + value + ":00";
			formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
			this.horaEntradaMarcada = formatter.parse(strFecha);
		} catch (Exception e) {
			System.out.println("HoraEntradaMarcada. No se pudo convertir la hora indicada a fecha ---> " + value + " :: " + strFecha);
			e.printStackTrace();
		}
	}

	public String getSalida() {
		return (this.horaSalidaMarcada != null ? (new SimpleDateFormat("HH:mm")).format(this.horaSalidaMarcada) : "00:00");
	}
	
	public void setSalida(String value) {
		SimpleDateFormat formatter = null;
		String strFecha = "";
		
		try {
			value = (value == null || "".equals(value.trim()) ? "00:00" : value.trim());
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			strFecha = formatter.format(this.fecha) + " " + value + ":00";
			formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
			this.horaSalidaMarcada = formatter.parse(strFecha);
		} catch (Exception e) {
			System.out.println("HoraSalidaMarcada. No se pudo convertir la hora indicada a fecha ---> " + value + " :: " + strFecha);
			e.printStackTrace();
		}
	}

	public String getEntradaComplemento() {
		return (this.horaEntradaMarcadaComplemento != null ? (new SimpleDateFormat("HH:mm")).format(this.horaEntradaMarcadaComplemento) : "00:00");
	}
	
	public void setEntradaComplemento(String value) {
		SimpleDateFormat formatter = null;
		String strFecha = "";
		
		try {
			value = (value == null || "".equals(value.trim()) ? "00:00" : value.trim());
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			strFecha = formatter.format(this.fecha) + " " + value + ":00";
			formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
			this.horaEntradaMarcadaComplemento = formatter.parse(strFecha);
		} catch (Exception e) {
			System.out.println("HoraEntradaMarcadaComplemento. No se pudo convertir la hora indicada a fecha ---> " + value + " :: " + strFecha);
			e.printStackTrace();
		}
	}

	public String getSalidaComplemento() {
		return (this.horaSalidaMarcadaComplemento != null ? (new SimpleDateFormat("HH:mm")).format(this.horaSalidaMarcadaComplemento) : "00:00");
	}
	
	public void setSalidaComplemento(String value) {
		SimpleDateFormat formatter = null;
		String strFecha = "";
		
		try {
			value = (value == null || "".equals(value.trim()) ? "00:00" : value.trim());
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			strFecha = formatter.format(this.fecha) + " " + value + ":00";
			formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
			this.horaSalidaMarcadaComplemento = formatter.parse(strFecha);
		} catch (Exception e) {
			System.out.println("HoraSalidaMarcadaComplemento. No se pudo convertir la hora indicada a fecha ---> " + value + " :: " + strFecha);
			e.printStackTrace();
		}
	}
	
	public int getCantidadHorasExtra() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		int horas = 0;
		
		if (this.horasTrabajadas >= 8) {
			if (Integer.parseInt(formatter.format(this.horasExtra)) <= 0) {
				if (this.horasExtraAutorizadas == null || Integer.parseInt(formatter.format(this.horasExtraAutorizadas)) <= 0)
					return horas;
			}
			
			this.horasExtraAutorizadas = (this.horasExtraAutorizadas == null ? this.horasExtra : this.horasExtraAutorizadas);
			horas = Integer.parseInt(formatter.format(this.horasExtraAutorizadas));
		}
		
		return horas;
	}
	
	public void setCantidadHorasExtra(int horas) {
		SimpleDateFormat formatter = null;
		String strFecha = "";

		if (this.horasTrabajadas >= 8) { // && this.horasExtra != null) {
			if (this.usuarioAutoriza > 0)
				return;
			
			try {
				formatter = new SimpleDateFormat("yyyy-MM-dd");
				this.horasExtraAutorizadas = this.horasExtra;
				this.fecha = (this.fecha == null ? this.idChecador.getFecha() : this.fecha);
				strFecha = formatter.format(this.fecha) + " " + ((horas < 10) ? "0" : "") + horas + ":00:00";
				formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
				this.horasExtraAutorizadas = formatter.parse(strFecha);
			} catch (Exception e) {
				System.out.println("No se pudo convertir la hora indicada a fecha ---> " + horas + " :: " + strFecha);
				e.printStackTrace();
			}
		}
	}
	
	public String getHorario() {
		SimpleDateFormat dateFormatter = null;
		String horario = "";
		String complemento = "";

		if (this.horaEntrada != null && this.horaSalida != null) {
			dateFormatter = new SimpleDateFormat("HH:mm");
			horario = ":ENTRADA-:SALIDA".replace(":ENTRADA", dateFormatter.format(this.horaEntrada)).replace(":COMPLEMENTO", complemento).replace(":SALIDA", dateFormatter.format(this.horaSalida));
			/*if (this.horaEntradaComplemento != null && this.horaSalidaComplemento != null) 
				complemento = ":SALIDA :ENTRADA".replace(":ENTRADA", dateFormatter.format(this.horaEntradaComplemento)).replace(":SALIDA", dateFormatter.format(this.horaSalidaComplemento));
			horario = (! "".equals(complemento.trim()) ? ":ENTRADA-:COMPLEMENTO-:SALIDA" : ":ENTRADA-:SALIDA"); 
			horario = horario.replace(":ENTRADA", dateFormatter.format(this.horaEntrada)).replace(":COMPLEMENTO", complemento).replace(":SALIDA", dateFormatter.format(this.horaSalida));*/
		}
		
		return horario;
	}
	
	public void setHorario(String value) { }

	public String getHorarioComplemento() {
		SimpleDateFormat dateFormatter = null;
		String complemento = "";

		if (this.horaEntradaComplemento != null && this.horaSalidaComplemento != null) {
			dateFormatter = new SimpleDateFormat("HH:mm");
			complemento = ":ENTRADA-:SALIDA".replace(":ENTRADA", dateFormatter.format(this.horaEntradaComplemento)).replace(":SALIDA", dateFormatter.format(this.horaSalidaComplemento));
		}
		
		return complemento;
	}
	
	public void setHorarioComplemento(String value) { }
	
	public String getContratoHorario() {
		String horario = "";
		String horarioComplemento = "";

		horario = getHorario();
		horarioComplemento = " " + getHorarioComplemento();
		if (! "".equals(horarioComplemento.trim()))
			horario = horario + horarioComplemento;
		return horario;
	}
	
	public void setContratoHorario(String value) { }

	public int horasExtrasAutorizadas() {
		return (this.horasTrabajadas > 0 && this.usuarioAutoriza > 0 && this.horasExtraAutorizadas != null) ? Integer.parseInt((new SimpleDateFormat("HH")).format(this.horasExtraAutorizadas)): 0;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */