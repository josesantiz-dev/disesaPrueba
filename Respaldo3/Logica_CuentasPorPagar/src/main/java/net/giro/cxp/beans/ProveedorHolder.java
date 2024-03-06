package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import net.giro.plataforma.topics.Meses;

public class ProveedorHolder implements Serializable {
	private static final long serialVersionUID = 1L;
	private int year;
	private Meses mes;
	// ----------------------------------------------------------------------------------------
	private long idProveedor; // *
	private String proveedor; // *
	private double montoConFactura;
	private double montoSinFactura;
	private double total;
	private List<PagosGastos> listItems;
	
	public ProveedorHolder(long idProveedor, String proveedor, List<PagosGastos> listItems) {
		this.idProveedor = idProveedor;
		this.proveedor = proveedor;
		this.listItems = listItems;
		// ----------------------------------------------------------------------------------------
		this.mes = null;
		this.year = 0;
		this.total = 0;
		this.montoConFactura = 0;
		this.montoSinFactura = 0;
		// ----------------------------------------------------------------------------------------
		generaDatos();
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Meses getMes() {
		return mes;
	}

	public void setMes(Meses mes) {
		this.mes = mes;
	}

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

	public double getMontoConFactura() {
		return montoConFactura;
	}

	public void setMontoConFactura(double montoConFactura) {
		this.montoConFactura = montoConFactura;
	}

	public double getMontoSinFactura() {
		return montoSinFactura;
	}

	public void setMontoSinFactura(double montoSinFactura) {
		this.montoSinFactura = montoSinFactura;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public List<PagosGastos> getListItems() {
		return listItems;
	}

	public void setListItems(List<PagosGastos> listItems) {
		this.listItems = listItems;
	}

	// -----------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------
	
	public String getDescripcion() {
		return this.idProveedor + " - " + this.proveedor;
	}
	
	public void setDescripcion(String value) {}

	public String getEjercicioPeriodo() {
		return this.mes.name() + " " + this.year;
	}
	
	public void setEjercicioPeriodo(String value) {}

	// -----------------------------------------------------------------------------------
	// METODOS
	// -----------------------------------------------------------------------------------
	
	private void generaDatos() {
		if (this.listItems == null || this.listItems.isEmpty())
			return;
		
		Calendar cal = Calendar.getInstance();
		for (PagosGastos item : this.listItems) {
			cal.setTime(item.getFecha());
			if (this.year <= 0)
				this.year = cal.get(Calendar.YEAR);
			if (this.mes == null)
				this.mes = Meses.fromOrdinal(cal.get(Calendar.MONTH));
			this.total += item.getMonto();
			this.montoConFactura += item.getConFactura();
			this.montoSinFactura += item.getSinFactura();
		}
	}
}
