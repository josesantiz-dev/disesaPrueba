package net.giro.cxc.fe.documentos;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Concepto implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name="Cantidad", required=false) 
	private BigDecimal cantidad;
	@XmlAttribute(name="Unidad", required=false) 
	private String unidad;
	@XmlAttribute(name="NoIdentificacion", required=false) 
	private String noIdentificacion;
	@XmlAttribute(name="Descripcion", required=false) 
	private String descripcion;
	@XmlAttribute(name="ValorUnitario", required=false) 
	private BigDecimal valorUnitario;
	@XmlAttribute(name="Importe", required=false) 
	private BigDecimal importe;
	@XmlAttribute(name="ClaveUnidad", required=false) 
	private String claveUnidad;
	@XmlAttribute(name="ClaveProdServ", required=false) 
	private String claveProdServ;
	@XmlAttribute(name="Descuento", required=false) 
	private String descuento;
	@XmlElement(name="Impuestos", required=false) 
	private Impuestos impuestos;
	@XmlElement(name="InformacionAduanera", required=false)
	private InformacionAduanera informacionAduanera;
	@XmlElement(name="CuentaPredial", required=false)
	private CuentaPredial cuentaPredial;
	@XmlElement(name="Parte", required=false)
	private Parte parte;
	
	
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
	
	public String getClaveUnidad() {
		return claveUnidad;
	}
	
	public void setClaveUnidad(String claveUnidad) {
		this.claveUnidad = claveUnidad;
	}
	
	public String getClaveProdServ() {
		return claveProdServ;
	}
	
	public void setClaveProdServ(String claveProdServ) {
		this.claveProdServ = claveProdServ;
	}
	
	public String getDescuento() {
		return descuento;
	}
	
	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}
	
	public Impuestos getImpuestos() {
		return impuestos;
	}
	
	public void setImpuestos(Impuestos impuestos) {
		this.impuestos = impuestos;
	}

	public InformacionAduanera getInformacionAduanera() {
		return informacionAduanera;
	}

	public void setInformacionAduanera(InformacionAduanera informacionAduanera) {
		this.informacionAduanera = informacionAduanera;
	}

	public CuentaPredial getCuentaPredial() {
		return cuentaPredial;
	}

	public void setCuentaPredial(CuentaPredial cuentaPredial) {
		this.cuentaPredial = cuentaPredial;
	}

	public Parte getParte() {
		return parte;
	}

	public void setParte(Parte parte) {
		this.parte = parte;
	}
}
