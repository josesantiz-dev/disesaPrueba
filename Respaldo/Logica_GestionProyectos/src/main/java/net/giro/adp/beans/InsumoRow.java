package net.giro.adp.beans;

import java.io.Serializable;

public class InsumoRow implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String clave;
	private String descripcion;
	private String unidad;
	private String familia;
	private double cantidad;
	private double costoUnitario;
	private double monto;
	private double porcentaje;
	private int tipo;
	private String moneda;
	
	
	public InsumoRow() {
		this.clave = "";
		this.descripcion = "";
		this.unidad = "";
		this.familia = "";
		this.moneda = "";
	}

	
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	
	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getCostoUnitario() {
		return costoUnitario;
	}

	public void setCostoUnitario(double costoUnitario) {
		this.costoUnitario = costoUnitario;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * TIPO INSUMO - 1:Material, 2:Mano de Obra, 3:Herramienta, 4:Otros
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * TIPO INSUMO - 1:Material, 2:Mano de Obra, 3:Herramienta, 4:Otros
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	
	@Override
	public String toString() {
		return "InsumoRow {clave:" + this.clave + ", " + 
			"descripcion:" + this.descripcion + ", " + 
			"unidad:" + this.unidad + ", " + 
			"familia:" + this.familia + ", " + 
			"cantidad:" + this.cantidad + ", " + 
			"costoUnitario:" + this.costoUnitario + ", " + 
			"monto:" + this.monto + ", " + 
			"porcentaje:" + this.porcentaje + ", " + 
			"tipo:" + this.tipo + ", " + 
			"moneda:" + this.moneda + "}";
	}
}
