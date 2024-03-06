package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * empleado_nomina 
 * @author javaz
 */
public class EmpleadoNomina implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private int tipo; // 0-Semanal, 1-Quincenal
	private Empleado idEmpleado;
	private long idContrato;
	private long idPeriodicidad;
	private long idObra; // obra a la que aplica
	private Date fecha;
	private int diaDescanso; // indica si el dia es su dia de descanso
	private int diaFestivo; // indica si el dia es un dia festivo
	private int factor; // factor multiplIcador si el dia es festivo
	private int horasTrabajadas;
	private int horasExtras;
	private BigDecimal pagoNormal;
	private BigDecimal septimoDia;
	private BigDecimal pagoExtra;
	private BigDecimal descuento;
	private BigDecimal infonavit;
	private BigDecimal pagoNeto;
	private long idEmpresa;
	private String observaciones;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public EmpleadoNomina() {
		this.observaciones = "";
		this.fecha = Calendar.getInstance().getTime();
		this.pagoNormal = BigDecimal.ZERO;
		this.septimoDia = BigDecimal.ZERO;
		this.pagoExtra = BigDecimal.ZERO;
		this.descuento = BigDecimal.ZERO;
		this.infonavit = BigDecimal.ZERO;
		this.pagoNeto = BigDecimal.ZERO;
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

	/**
	 * TIPO: 0-Semanal, 1-Quincenal
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * TIPO: 0-Semanal, 1-Quincenal
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Empleado getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Empleado idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public long getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(long idContrato) {
		this.idContrato = idContrato;
	}

	public long getIdPeriodicidad() {
		return idPeriodicidad;
	}

	public void setIdPeriodicidad(long idPeriodicidad) {
		this.idPeriodicidad = idPeriodicidad;
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

	/**
	 * 0|1 indica si el dia es su dia de descanso
	 * @return
	 */
	public int getDiaDescanso() {
		return diaDescanso;
	}

	/**
	 * 0|1 indica si el dia es su dia de descanso
	 * @param diaDescanso
	 */
	public void setDiaDescanso(int diaDescanso) {
		this.diaDescanso = diaDescanso;
	}

	/**
	 * 0|1 indica si el dia es festivo
	 * @return
	 */
	public int getDiaFestivo() {
		return diaFestivo;
	}

	/**
	 * 0|1 indica si el dia es festivo
	 * @param diaFestivo
	 */
	public void setDiaFestivo(int diaFestivo) {
		this.diaFestivo = diaFestivo;
	}

	/**
	 * factor multiplecador si el dia es festivo
	 * @return
	 */
	public int getFactor() {
		return factor;
	}

	/**
	 * factor multiplecador si el dia es festivo
	 * @param factor
	 */
	public void setFactor(int factor) {
		this.factor = factor;
	}

	public int getHorasTrabajadas() {
		return horasTrabajadas;
	}

	public void setHorasTrabajadas(int horasTrabajadas) {
		this.horasTrabajadas = horasTrabajadas;
	}

	public int getHorasExtras() {
		return horasExtras;
	}

	public void setHorasExtras(int horasExtras) {
		this.horasExtras = horasExtras;
	}

	public BigDecimal getPagoNormal() {
		return pagoNormal;
	}

	public void setPagoNormal(BigDecimal pagoNormal) {
		this.pagoNormal = pagoNormal;
	}

	public BigDecimal getSeptimoDia() {
		return septimoDia;
	}

	public void setSeptimoDia(BigDecimal septimoDia) {
		this.septimoDia = septimoDia;
	}

	public BigDecimal getPagoExtra() {
		return pagoExtra;
	}

	public void setPagoExtra(BigDecimal pagoExtra) {
		this.pagoExtra = pagoExtra;
	}

	public BigDecimal getDescuento() {
		return descuento;
	}

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	public BigDecimal getInfonavit() {
		return infonavit;
	}

	public void setInfonavit(BigDecimal infonavit) {
		this.infonavit = infonavit;
	}

	public BigDecimal getPagoNeto() {
		return pagoNeto;
	}

	public void setPagoNeto(BigDecimal pagoNeto) {
		this.pagoNeto = pagoNeto;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */