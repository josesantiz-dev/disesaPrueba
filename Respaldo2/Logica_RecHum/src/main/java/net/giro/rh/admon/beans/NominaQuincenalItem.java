package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

public class NominaQuincenalItem implements Serializable {
	private static final long serialVersionUID = 5184461342856449040L;
	
	private int rowIndex;
	private Long idObra;
	private Long idEmpleado;
	private String empleado;
	private String nss;
	private Long idSucursal;
	private String sucursal;
	private Date fecha;
	private double sueldoQuincenal;
	private double pagoDeposito;
	private String cuentaDeposito;
	private String observaciones;
	private String mensaje;
	
	
	public NominaQuincenalItem() {
		this.nss = "";
		this.empleado = "";
		this.cuentaDeposito = "";
		this.observaciones = "";
		this.sucursal = "";
	}

	
	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Long getIdObra() {
		return idObra;
	}

	public void setIdObra(Long idObra) {
		this.idObra = idObra;
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getSueldoQuincenal() {
		return sueldoQuincenal;
	}

	public void setSueldoQuincenal(double sueldoQuincenal) {
		this.sueldoQuincenal = sueldoQuincenal;
	}

	public double getPagoDeposito() {
		return pagoDeposito;
	}

	public void setPagoDeposito(double pagoDeposito) {
		this.pagoDeposito = pagoDeposito;
	}

	public String getCuentaDeposito() {
		return cuentaDeposito;
	}

	public void setCuentaDeposito(String cuentaDeposito) {
		this.cuentaDeposito = cuentaDeposito;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	//-----------------------------------------------------
	
	public boolean completo() {
		if (this.idObra == null || this.idObra <= 0L)
			return false;
		if (this.nss == null || "".equals(this.nss.trim()))
			return false;
		if (this.empleado == null || "".equals(this.empleado.trim()))
			return false;
		if (this.cuentaDeposito == null || "".equals(this.cuentaDeposito.trim()))
			return false;
		if (this.observaciones == null || "".equals(this.observaciones.trim()))
			return false;
		if (this.sucursal == null || "".equals(this.sucursal.trim()))
			return false;
		if (this.sueldoQuincenal <= 0)
			return false;
		
		return true;
	}


	public String getMensaje() {
		return mensaje;
	}


	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
