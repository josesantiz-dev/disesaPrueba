package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

@Root(strict=false)
@NamespaceList({
@Namespace(reference="http://www.sat.gob.mx/cfd/3", prefix="cfdi"),
@Namespace(reference="http://www.w3.org/2001/XMLSchema-instance", prefix="xsi")
})
public class Comprobante implements Serializable{	
	private static final long serialVersionUID = 1L;
	
	@Attribute(name="schemaLocation", required=false)
	String schemaLocation;
	@Attribute(name="version", required=false)
	String version;
	@Attribute(name="folio", required=false)
	String folio;
	@Attribute(name="fecha", required=false)
	String fecha;
	@Attribute(name="sello", required=false)
	String sello;
	@Attribute(name="formaDePago", required=false)
	String formaPago;
	@Attribute(name="noCertificado", required=false)
	String numeroCertificado;
	@Attribute(name="certificado", required=false)
	String certificado;
	@Attribute(name="subTotal", required=false)
	String subTotal;
	@Attribute(name="TipoCambio", required=false)
	String tipoCambio;
	@Attribute(name="Moneda", required=false)
	String moneda;
	@Attribute(name="total", required=false)
	String total;
	@Attribute(name="tipoDeComprobante", required=false)
	String tipoComprobante;
	@Attribute(name="metodoDePago", required=false)
	String metodoPago;
	@Attribute(name="LugarExpedicion", required=false)
	String lugarExpedicion;
	@Attribute(name="serie", required=false)
	String serie;
	@Attribute(name="descuento", required=false)
	String descuento;
	@Attribute(name="condicionesDePago", required=false)
	String condicionesPago;
	
	public String getDescuento() {
		return descuento;
	}
	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}
	public String getCondicionesPago() {
		return condicionesPago;
	}
	public void setCondicionesPago(String condicionesPago) {
		this.condicionesPago = condicionesPago;
	}
	@Element(name="Emisor")
	Emisor emisor;
	@Element(name="Receptor")
	Receptor receptor;
	@ElementList(name="Conceptos")
	List<Concepto> conceptos;
	@Element(name="Impuestos")
	Impuestos impuestos;
	@Element(name="Complemento")
	Complemento complemento;
	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getSello() {
		return sello;
	}
	public void setSello(String sello) {
		this.sello = sello;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public String getNumeroCertificado() {
		return numeroCertificado;
	}
	public void setNumeroCertificado(String numeroCertificado) {
		this.numeroCertificado = numeroCertificado;
	}
	public String getCertificado() {
		return certificado;
	}
	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public String getTipoCambio() {
		return tipoCambio;
	}
	public void setTipoCambio(String tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	public String getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	public String getLugarExpedicion() {
		return lugarExpedicion;
	}
	public void setLugarExpedicion(String lugarExpedicion) {
		this.lugarExpedicion = lugarExpedicion;
	}
	public Emisor getEmisor() {
		return emisor;
	}
	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}
	public Receptor getReceptor() {
		return receptor;
	}
	public void setReceptor(Receptor receptor) {
		this.receptor = receptor;
	}
	public List<Concepto> getConceptos() {
		return conceptos;
	}
	public void setConceptos(List<Concepto> conceptos) {
		this.conceptos = conceptos;
	}
	public Impuestos getImpuestos() {
		return impuestos;
	}
	public void setImpuestos(Impuestos impuestos) {
		this.impuestos = impuestos;
	}
	public Complemento getComplemento() {
		return complemento;
	}
	public void setComplemento(Complemento complemento) {
		this.complemento = complemento;
	}
	public String getSchemaLocation() {
		return schemaLocation;
	}
	public void setSchemaLocation(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
}
