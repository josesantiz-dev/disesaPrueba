package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Parte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Attribute(required=false) 
	private BigDecimal cantidad;
	@Attribute(required=false) 
	private String unidad;
	@Attribute(required=false, name="noidentificacion") 
	private String noIdentificacion;
	@Attribute(required=false) 
	private String descripcion;
	@Attribute(required=false, name="valorunitario") 
	private BigDecimal valorUnitario;
	@Attribute(required=false) 
	private BigDecimal importe;
	@Element(name="informacionaduanera", required=false)
	private InformacionAduanera informacionAduanera;
	
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
	
	public InformacionAduanera getInformacionAduanera() {
		return informacionAduanera;
	}
	
	public void setInformacionAduanera(InformacionAduanera informacionAduanera) {
		this.informacionAduanera = informacionAduanera;
	}
}
