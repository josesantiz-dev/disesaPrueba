package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Concepto implements Serializable {
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
	@Attribute(required=false, name="claveunidad") 
	private String claveUnidad;
	@Attribute(required=false, name="claveprodserv") 
	private String claveProdServ;
	@Attribute(required=false, name="descuento") 
	private String descuento;
	@Element(required=false, name="impuestos") 
	private Impuestos impuestos;
	@Element(name="informacionaduanera", required=false)
	private InformacionAduanera informacionAduanera;
	@Element(name="cuentapredial", required=false)
	private CuentaPredial cuentaPredial;
	@Element(name="parte", required=false)
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
