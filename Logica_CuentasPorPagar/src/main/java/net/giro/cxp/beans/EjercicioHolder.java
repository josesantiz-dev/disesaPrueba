package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.List;

import net.giro.plataforma.topics.Meses;

public class EjercicioHolder implements Serializable {
	private static final long serialVersionUID = 1L;
	private int year;  // *
	private Meses mes; // *
	private double montoConFactura;
	private double montoSinFactura;
	private double total;
	private List<ProveedorHolder> listItems;
	
	public EjercicioHolder(List<ProveedorHolder> listItems) {
		this.listItems = listItems;
		// ----------------------------------------------------
		this.mes = null;
		this.year = 0;
		this.total = 0;
		this.montoConFactura = 0;
		this.montoSinFactura = 0;
		// ----------------------------------------------------
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
	
	public List<ProveedorHolder> getListItems() {
		return listItems;
	}
	
	public void setListItems(List<ProveedorHolder> listItems) {
		this.listItems = listItems;
	}

	// -----------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------
	
	public String getDescripcion() {
		return this.mes.name() + " " + this.year;
	}
	
	public void setDescripcion(String value) {}
	
	// -----------------------------------------------------------------------------------
	// METODOS
	// -----------------------------------------------------------------------------------
	
	private void generaDatos() {
		if (this.listItems == null || this.listItems.isEmpty())
			return;

		for (ProveedorHolder item : this.listItems) {
			if (this.year <= 0)
				this.year = item.getYear();
			if (this.mes == null)
				this.mes = item.getMes();
			this.total += item.getTotal();
			this.montoConFactura += item.getMontoConFactura();
			this.montoSinFactura += item.getMontoSinFactura();
		}
	}
}
