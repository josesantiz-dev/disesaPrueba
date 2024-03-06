package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.giro.plataforma.topics.Meses;

public class PagosGastosHolder implements Serializable {
	private static final long serialVersionUID = 1L;
	private Meses mes;
	private String proveedor;
	private double monto;
	private List<PagosGastos> listPagosGastos;
	// -----------------------------------------------------------------------------------
	private Date fecha;
	private int year;
	private int month;
	private long idProveedor;
	private double montoConFactura;
	private double montoSinFactura;

	public PagosGastosHolder() {
		this.mes = null;
		this.proveedor = "";
		this.listPagosGastos = new ArrayList<PagosGastos>();
	}

	public PagosGastosHolder(Meses mes, String proveedor, double monto, List<PagosGastos> listPagosGastos) {
		this();
		this.mes = mes;
		this.proveedor = proveedor;
		this.monto = monto;
		this.listPagosGastos = listPagosGastos;
	}

	public PagosGastosHolder(long idProveedor, String proveedor, Date fecha, List<PagosGastos> listPagosGastos) {
		this();
		this.idProveedor = idProveedor;
		this.proveedor = proveedor;
		this.fecha = fecha;
		this.listPagosGastos = listPagosGastos;
		generaEjercicioPeriodo();
		calculaMontos();
	}

	public int getAnnio() {
		return year;
	}

	public void setAnnio(int annio) {
		//this.annio = annio;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		//this.month = month;
	}

	public Meses getMes() {
		return mes;
	}
	
	public void setMes(Meses mes) {
		//this.mes = mes;
	}

	public String getMesProvision() {
		return mes.toString();
	}
	
	public void setMes(String mes) { }

	public long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public String getProveedor() {
		return proveedor;
	}
	
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	
	public double getMonto() {
		return monto;
	}
	
	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	public List<PagosGastos> getListPagosGastos() {
		return listPagosGastos;
	}
	
	public void setListPagosGastos(List<PagosGastos> listPagosGastos) {
		this.listPagosGastos = listPagosGastos;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getMontoConFactura() {
		return montoConFactura;
	}

	public void setMontoConFactura(double montoConFactura) {
		//this.montoConFactura = montoConFactura;
	}

	public double getMontoSinFactura() {
		return montoSinFactura;
	}

	public void setMontoSinFactura(double montoSinFactura) {
		//this.montoSinFactura = montoSinFactura;
	}

	// -----------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------
	
	private void generaEjercicioPeriodo() {
		this.year = 0;
		this.month = 0;
		if (this.fecha == null)
			return;

		Calendar cal = Calendar.getInstance();
		cal.setTime(this.fecha);
		this.year = cal.get(Calendar.YEAR);
		this.month = cal.get(Calendar.MONTH);
		this.mes = Meses.fromOrdinal(this.month);
	}
	
	private void calculaMontos() {
		this.monto = 0;
		this.montoConFactura = 0;
		this.montoSinFactura = 0;
		if (this.listPagosGastos == null || this.listPagosGastos.isEmpty())
			return;
		
		for (PagosGastos item : this.listPagosGastos) {
			this.monto += item.getMonto();
			this.montoConFactura += item.getConFactura();
			this.montoSinFactura += item.getSinFactura();
		}
	}
}
