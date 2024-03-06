package net.giro.cxc.fe.documentos;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Parte implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name="Cantidad", required=false) 
	private BigDecimal cantidad;
	@XmlAttribute(name="Unidad", required=false) 
	private String unidad;
	@XmlAttribute(name="NoIdentificacion", required=false) 
	private String noIdentificacion;
	@XmlAttribute(name="Descripcion", required=false) 
	private String descripcion;
	@XmlAttribute(required=false, name="ValorUnitario") 
	private BigDecimal valorUnitario;
	@XmlAttribute(name="Importe", required=false) 
	private BigDecimal importe;
	@XmlElement(name="InformacionAduanera", required=false)
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
