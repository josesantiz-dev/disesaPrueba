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
public class Comprobante implements Serializable { 
	private static final long serialVersionUID = 1L;
	
	@Attribute(name="schemalocation", required=false)
	private String schemaLocation;
	@Attribute(name="version", required=false)
	private String version;
	@Attribute(name="folio", required=false)
	private String folio;
	@Attribute(name="fecha", required=false)
	private String fecha;
	@Attribute(name="sello", required=false)
	private String sello;
	@Attribute(name="formadepago", required=false)
	private String formaPago;
	@Attribute(name="nocertificado", required=false)
	private String numeroCertificado;
	@Attribute(name="certificado", required=false)
	private String certificado;
	@Attribute(name="subtotal", required=false)
	private String subTotal;
	@Attribute(name="tipocambio", required=false)
	private String tipoCambio;
	@Attribute(name="moneda", required=false)
	private String moneda;
	@Attribute(name="total", required=false)
	private String total;
	@Attribute(name="tipodecomprobante", required=false)
	private String tipoComprobante;
	@Attribute(name="metododepago", required=false)
	private String metodoPago;
	@Attribute(name="lugarexpedicion", required=false)
	private String lugarExpedicion;
	@Attribute(name="serie", required=false)
	private String serie;
	@Attribute(name="descuento", required=false)
	private String descuento;
	@Attribute(name="condicionesdepago", required=false)
	private String condicionesPago;
	@Element(name="emisor")
	private Emisor emisor;
	@Element(name="receptor")
	private Receptor receptor;
	@ElementList(name="conceptos")
	private List<Concepto> conceptos;
	@Element(name="impuestos", required=false)
	private Impuestos impuestos;
	@Element(name="complemento")
	private Complemento complemento;
	@Element(name="addenda", required=false)
	private Addenda addenda;
	
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
