package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class NominaQuincenalItem implements Serializable {
	private static final long serialVersionUID = 5184461342856449040L;
	private Integer rowIndex;
	private Long idObra;
	private Long idSucursal;
	private String sucursal;
	private Long idEmpleado;
	private String empleado;
	private String nss;
	private Date fechaDesde;
	private Date fechaHasta;
	private Long idContrato;
	private Long idPeriodicidad;
	private Double sueldoQuincenal;
	private Double dispersion;
	private Double pagoExtra;
	private Double descuentos;
	private Double pagoQuincenal;
	private String observaciones;
	private String altaImss;
	// ----------------------------
	private Date fecha; //  SIN USO
	private Double pagoDeposito; // SIN USO
	private String cuentaDeposito; // SIN USO
	// ----------------------------
	private String mensaje;
	
	public NominaQuincenalItem() {
		this.rowIndex = 0;
		this.idObra = 0L;
		this.idSucursal = 0L;
		this.sucursal = "";
		this.idEmpleado = 0L;
		this.empleado = "";
		this.nss = "";
		this.fechaDesde = Calendar.getInstance().getTime();
		this.fechaHasta = Calendar.getInstance().getTime();
		this.idContrato = 0L;
		this.idPeriodicidad = 0L;
		this.sueldoQuincenal = 0D;
		this.dispersion = 0D;
		this.pagoExtra = 0D;
		this.descuentos = 0D;
		this.pagoQuincenal = 0D;
		this.observaciones = "";
		this.altaImss = "";
		// ----------------------------
		this.fecha = Calendar.getInstance().getTime();
		this.pagoDeposito = 0D;
		this.cuentaDeposito = "";
		// ----------------------------
		this.mensaje = "";
	}

	
	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Long getIdObra() {
		return idObra;
	}

	public void setIdObra(Long idObra) {
		this.idObra = idObra;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public Long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado = idEmpleado;
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

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Long getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(Long idContrato) {
		this.idContrato = idContrato;
	}

	public Long getIdPeriodicidad() {
		return idPeriodicidad;
	}

	public void setIdPeriodicidad(Long idPeriodicidad) {
		this.idPeriodicidad = idPeriodicidad;
	}

	public Double getSueldoQuincenal() {
		return sueldoQuincenal;
	}

	public void setSueldoQuincenal(Double sueldoQuincenal) {
		this.sueldoQuincenal = sueldoQuincenal;
	}

	public Double getDispersion() {
		return dispersion;
	}

	public void setDispersion(Double dispersion) {
		this.dispersion = dispersion;
	}

	public Double getPagoExtra() {
		return pagoExtra;
	}

	public void setPagoExtra(Double pagoExtra) {
		this.pagoExtra = pagoExtra;
	}

	public Double getDescuentos() {
		return descuentos;
	}

	public void setDescuentos(Double descuentos) {
		this.descuentos = descuentos;
	}

	public Double getPagoQuincenal() {
		return pagoQuincenal;
	}

	public void setPagoQuincenal(Double pagoQuincenal) {
		this.pagoQuincenal = pagoQuincenal;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getAltaImss() {
		return altaImss;
	}

	public void setAltaImss(String altaImss) {
		this.altaImss = altaImss;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getPagoDeposito() {
		return pagoDeposito;
	}

	public void setPagoDeposito(Double pagoDeposito) {
		this.pagoDeposito = pagoDeposito;
	}

	public String getCuentaDeposito() {
		return cuentaDeposito;
	}

	public void setCuentaDeposito(String cuentaDeposito) {
		this.cuentaDeposito = cuentaDeposito;
	}
	
	//-----------------------------------------------------
	
	/**
	 * Sueldo Quincenal - Dispersion + Extras - Descuentos
	 * @return
	 */
	public double getSueldoNeto() {
		return this.dispersion + (this.sueldoQuincenal - this.dispersion + this.pagoExtra - this.descuentos);
	}
	
	public void setSueldoNeto(double suledoNeto) {}
	
	public boolean completo() {
		if (this.idObra == null || this.idObra <= 0L)
			return false;
		if (this.nss == null || "".equals(this.nss.trim()))
			return false;
		if (this.sueldoQuincenal <= 0 && this.pagoQuincenal <= 0)
			return false;
		return true;
	}

	public boolean sinPago() {
		if (this.completo())
			return false;
		return (this.sueldoQuincenal <= 0 && this.dispersion <= 0 && this.pagoExtra <= 0 && this.descuentos <= 0 && this.pagoQuincenal <= 0);
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public void calcularDispersion() {
		
	}
}
