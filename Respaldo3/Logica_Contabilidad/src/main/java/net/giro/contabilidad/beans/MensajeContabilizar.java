package net.giro.contabilidad.beans;

import java.io.Serializable;

/**
 * mensaje_contabilizar
 * @author javaz
 *
 */
public class MensajeContabilizar implements Serializable {
	private static final long serialVersionUID = 3L;
	private Long idMensajeTransaccion;
	private Long idTransaccion;
	private Long poliza;
	private Long lote;
	private Long idEmpresa;
	private Long ejercicio;
	private String periodo;
	private String fecha;
	
	public MensajeContabilizar() {
		this.periodo = "";
		this.fecha = "";
	}
	
	public MensajeContabilizar(Long idMensajeTransaccion, Long idTransaccion, Long poliza, Long lote, Long idEmpresa, Long ejercicio, String periodo) {
		this();
		this.idMensajeTransaccion = idMensajeTransaccion;
		this.idTransaccion = idTransaccion;
		this.poliza = poliza;
		this.lote = lote;
		this.idEmpresa = idEmpresa;
		this.ejercicio = ejercicio;
		this.periodo = periodo;
	}

	public Long getIdMensajeTransaccion() {
		return idMensajeTransaccion;
	}

	public void setIdMensajeTransaccion(Long idMensajeTransaccion) {
		this.idMensajeTransaccion = idMensajeTransaccion;
	}
	
	public Long getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(Long idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public Long getPoliza() {
		return poliza;
	}

	public void setPoliza(Long poliza) {
		this.poliza = poliza;
	}

	public Long getLote() {
		return lote;
	}

	public void setLote(Long lote) {
		this.lote = lote;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Long ejercicio) {
		this.ejercicio = ejercicio;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
}
