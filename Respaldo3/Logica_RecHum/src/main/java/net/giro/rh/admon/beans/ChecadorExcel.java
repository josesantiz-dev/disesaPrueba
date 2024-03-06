package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ChecadorExcel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long idEmpleado;
	private String nombreEmpleado;
	private Date fecha;
	private String entrada;
	private String salida;
	private String entradaMarcada;
	private String salidaMarcada;
	private String tiempoAsistido;
	
	
	public ChecadorExcel() {
		this.fecha = Calendar.getInstance().getTime();
	}

	public ChecadorExcel(Long idEmpleado, String nombreEmpleado, Date fecha, String entrada, String salida,
			String entradaMarcada, String salidaMarcada, String tiempoAsistido) {
		super();
		this.idEmpleado = idEmpleado;
		this.nombreEmpleado = nombreEmpleado;
		this.fecha = fecha;
		this.entrada = entrada;
		this.salida = salida;
		this.entradaMarcada = entradaMarcada;
		this.salidaMarcada = salidaMarcada;
		this.tiempoAsistido = tiempoAsistido;
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

	public String getEntrada() {
		return entrada;
	}

	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}

	public String getSalida() {
		return salida;
	}

	public void setSalida(String salida) {
		this.salida = salida;
	}

	public String getEntradaMarcada() {
		return entradaMarcada;
	}

	public void setEntradaMarcada(String entradaMarcada) {
		this.entradaMarcada = entradaMarcada;
	}

	public String getSalidaMarcada() {
		return salidaMarcada;
	}

	public void setSalidaMarcada(String salidaMarcada) {
		this.salidaMarcada = salidaMarcada;
	}

	public String getTiempoAsistido() {
		return tiempoAsistido;
	}

	public void setTiempoAsistido(String tiempoAsistido) {
		this.tiempoAsistido = tiempoAsistido;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */