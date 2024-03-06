package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.simpleframework.xml.Attribute;

public class Concepto implements Serializable {
	private static final long serialVersionUID = 1L;
	@Attribute(required=false)
	BigDecimal cantidad;
	@Attribute(required=false)
	String unidad;
	@Attribute(required=false)
	String noIdentificacion;
	@Attribute(required=false)
	String descripcion;
	@Attribute(required=false)
	BigDecimal valorUnitario;
	@Attribute(required=false)
	BigDecimal importe;
	
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public String getNoIdentificacion() {
		return noIdentificacion;
	}
	public void setNoIdentificacion(String noIdentificacion) {
		this.noIdentificacion = noIdentificacion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public BigDecimal getImporte() {
		return importe;
	}
	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}
}
