package mx.org.banxico.sie.api;

import java.io.Serializable;

public class Status implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean activoHistorico;
	private boolean activoOportuno;
	private String fechaConsulta;
	private String fechaFinPeriodoConsultaHistorica;
	private String fechaInicioPeriodoConsultaHistorica;
	private String fechaRegistro;
	private String numeroConsultasHistoricasDia;
	private String numeroConsultasHistoricasPeriodo;
	private String numeroConsultasOportunasDia;
	private String numeroConsultasOportunasPeriodo;
	private String numeroConsultasRestantesHistoricasDia;
	private String numeroConsultasRestantesHistoricasPeriodo;
	private String numeroConsultasRestantesOportunasDia;
	private String numeroConsultasRestantesOportunasPeriodo;
	private Object[] ultimosBloqueos;

	public boolean getActivoHistorico() {
		return activoHistorico;
	}

	public void setActivoHistorico(boolean activoHistorico) {
		this.activoHistorico = activoHistorico;
	}

	public void setActivoHistorico(String activoHistorico) {
		activoHistorico = (activoHistorico != null ? activoHistorico : "");
		setActivoHistorico(activoHistorico.toLowerCase().equals("true"));
	}

	public boolean getActivoOportuno() {
		return activoOportuno;
	}
	
	public void setActivoOportuno(boolean activoOportuno) {
		this.activoOportuno = activoOportuno;
	}

	public void setActivoOportuno(String activoOportuno) {
		activoOportuno = (activoOportuno != null ? activoOportuno : "");
		setActivoOportuno(activoOportuno.toLowerCase().equals("true"));
	}
	
	public String getFechaConsulta() {
		if (fechaConsulta == null)
			fechaConsulta = "";
		return fechaConsulta;
	}
	
	public void setFechaConsulta(String fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}
	
	public String getFechaFinPeriodoConsultaHistorica() {
		if (fechaFinPeriodoConsultaHistorica == null)
			fechaFinPeriodoConsultaHistorica = "";
		return fechaFinPeriodoConsultaHistorica;
	}
	
	public void setFechaFinPeriodoConsultaHistorica(String fechaFinPeriodoConsultaHistorica) {
		this.fechaFinPeriodoConsultaHistorica = fechaFinPeriodoConsultaHistorica;
	}
	
	public String getFechaInicioPeriodoConsultaHistorica() {
		if (fechaInicioPeriodoConsultaHistorica == null)
			fechaInicioPeriodoConsultaHistorica = "";
		return fechaInicioPeriodoConsultaHistorica;
	}
	
	public void setFechaInicioPeriodoConsultaHistorica(String fechaInicioPeriodoConsultaHistorica) {
		this.fechaInicioPeriodoConsultaHistorica = fechaInicioPeriodoConsultaHistorica;
	}
	
	public String getFechaRegistro() {
		if (fechaRegistro == null)
			fechaRegistro = "";
		return fechaRegistro;
	}
	
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public String getNumeroConsultasHistoricasDia() {
		if (numeroConsultasHistoricasDia == null)
			numeroConsultasHistoricasDia = "";
		return numeroConsultasHistoricasDia;
	}
	
	public void setNumeroConsultasHistoricasDia(String numeroConsultasHistoricasDia) {
		this.numeroConsultasHistoricasDia = numeroConsultasHistoricasDia;
	}
	
	public String getNumeroConsultasHistoricasPeriodo() {
		if (numeroConsultasHistoricasPeriodo == null)
			numeroConsultasHistoricasPeriodo = "";
		return numeroConsultasHistoricasPeriodo;
	}
	
	public void setNumeroConsultasHistoricasPeriodo(String numeroConsultasHistoricasPeriodo) {
		this.numeroConsultasHistoricasPeriodo = numeroConsultasHistoricasPeriodo;
	}
	
	public String getNumeroConsultasOportunasDia() {
		if (numeroConsultasOportunasDia == null)
			numeroConsultasOportunasDia = "";
		return numeroConsultasOportunasDia;
	}
	
	public void setNumeroConsultasOportunasDia(String numeroConsultasOportunasDia) {
		this.numeroConsultasOportunasDia = numeroConsultasOportunasDia;
	}
	
	public String getNumeroConsultasOportunasPeriodo() {
		if (numeroConsultasOportunasPeriodo == null)
			numeroConsultasOportunasPeriodo = "";
		return numeroConsultasOportunasPeriodo;
	}
	
	public void setNumeroConsultasOportunasPeriodo(String numeroConsultasOportunasPeriodo) {
		this.numeroConsultasOportunasPeriodo = numeroConsultasOportunasPeriodo;
	}
	
	public String getNumeroConsultasRestantesHistoricasDia() {
		if (numeroConsultasRestantesHistoricasDia == null)
			numeroConsultasRestantesHistoricasDia = "";
		return numeroConsultasRestantesHistoricasDia;
	}
	
	public void setNumeroConsultasRestantesHistoricasDia(String numeroConsultasRestantesHistoricasDia) {
		this.numeroConsultasRestantesHistoricasDia = numeroConsultasRestantesHistoricasDia;
	}
	
	public String getNumeroConsultasRestantesHistoricasPeriodo() {
		if (numeroConsultasRestantesHistoricasPeriodo == null)
			numeroConsultasRestantesHistoricasPeriodo = "";
		return numeroConsultasRestantesHistoricasPeriodo;
	}
	
	public void setNumeroConsultasRestantesHistoricasPeriodo(String numeroConsultasRestantesHistoricasPeriodo) {
		this.numeroConsultasRestantesHistoricasPeriodo = numeroConsultasRestantesHistoricasPeriodo;
	}
	
	public String getNumeroConsultasRestantesOportunasDia() {
		if (numeroConsultasRestantesOportunasDia == null)
			numeroConsultasRestantesOportunasDia = "";
		return numeroConsultasRestantesOportunasDia;
	}
	
	public void setNumeroConsultasRestantesOportunasDia(String numeroConsultasRestantesOportunasDia) {
		this.numeroConsultasRestantesOportunasDia = numeroConsultasRestantesOportunasDia;
	}
	
	public String getNumeroConsultasRestantesOportunasPeriodo() {
		if (numeroConsultasRestantesOportunasPeriodo == null)
			numeroConsultasRestantesOportunasPeriodo = "";
		return numeroConsultasRestantesOportunasPeriodo;
	}
	
	public void setNumeroConsultasRestantesOportunasPeriodo(String numeroConsultasRestantesOportunasPeriodo) {
		this.numeroConsultasRestantesOportunasPeriodo = numeroConsultasRestantesOportunasPeriodo;
	}
	
	public Object[] getUltimosBloqueos() {
		return ultimosBloqueos;
	}
	
	public void setUltimosBloqueos(Object[] ultimosBloqueos) {
		this.ultimosBloqueos = ultimosBloqueos;
	}
}
