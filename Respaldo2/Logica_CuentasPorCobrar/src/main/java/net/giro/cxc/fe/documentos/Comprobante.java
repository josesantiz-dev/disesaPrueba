package net.giro.cxc.fe.documentos;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.simpleframework.xml.Root;

@Root(strict=false)
@XmlRootElement(name="Comprobante")
@XmlAccessorType(XmlAccessType.FIELD)
public class Comprobante implements Serializable { 
	private static final long serialVersionUID = 1L;
	
	/*@XmlAttribute(name="schemaLocation", required=false)
	private String schemaLocation;*/
	@XmlAttribute(name="Version", required=true)
	private String version;
	@XmlAttribute(name="Serie", required=false)
	private String serie;
	@XmlAttribute(name="Folio", required=false)
	private String folio;
	@XmlAttribute(name="Fecha", required=true)
	private String fecha;
	@XmlAttribute(name="Sello", required=true)
	private String sello;
	@XmlAttribute(name="FormaDePago", required=false)
	private String formaPago;
	@XmlAttribute(name="NoCertificado", required=true)
	private String noCertificado;
	@XmlAttribute(name="Certificado", required=true)
	private String certificado;
	@XmlAttribute(name="CondicionesDePago", required=false)
	private String condicionesPago;
	@XmlAttribute(name="SubTotal", required=true)
	private String subTotal;
	@XmlAttribute(name="Descuento", required=false)
	private String descuento;
	@XmlAttribute(name="Moneda", required=true)
	private String moneda;
	@XmlAttribute(name="TipoCambio", required=false)
	private String tipoCambio;
	@XmlAttribute(name="Total", required=true)
	private String total;
	@XmlAttribute(name="TipoDeComprobante", required=true)
	private String tipoComprobante;
	@XmlAttribute(name="MetodoPago", required=false)
	private String metodoPago;
	@XmlAttribute(name="LugarExpedicion", required=true)
	private String lugarExpedicion;
	@XmlAttribute(name="Confirmacion", required=false)
	private String confirmacion;
	@XmlElement(name="CfdiRelacionados", required=false)
	public CfdiRelacionados cfdiRelacionados;
	@XmlElement(name="Emisor", required=true)
	public Emisor emisor;
	@XmlElement(name="Receptor", required=true)
	public Receptor receptor;
	@XmlElementWrapper(name="Conceptos")
	@XmlElement(name="Concepto", required=true)
	public List<Concepto> conceptos;
	@XmlElement(name="Impuestos", required=false)
	public Impuestos impuestos;
	@XmlElement(name="Complemento", required=false)
	public Complemento complemento;
	

	public Comprobante() {}
	
	public Comprobante(String version, String fecha, String sello, String noCertificado, String certificado, String subTotal, 
			String moneda, String total, String tipoComprobante, String lugarExpedicion, Emisor emisor, Receptor receptor, List<Concepto> conceptos) {
		super();
		//this.schemaLocation = "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd";
		this.version = version;
		this.fecha = fecha;
		this.sello = sello;
		this.noCertificado = noCertificado;
		this.certificado = certificado;
		this.subTotal = subTotal;
		this.moneda = moneda;
		this.total = total;
		this.tipoComprobante = tipoComprobante;
		this.lugarExpedicion = lugarExpedicion;
		this.emisor = emisor;
		this.receptor = receptor;
		this.conceptos = conceptos;
	}
	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
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

	public String getNoCertificado() {
		return noCertificado;
	}

	public void setNoCertificado(String noCertificado) {
		this.noCertificado = noCertificado;
	}

	public String getCertificado() {
		return certificado;
	}

	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}

	public String getCondicionesPago() {
		return condicionesPago;
	}

	public void setCondicionesPago(String condicionesPago) {
		this.condicionesPago = condicionesPago;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(String tipoCambio) {
		this.tipoCambio = tipoCambio;
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

	public String getConfirmacion() {
		return confirmacion;
	}

	public void setConfirmacion(String confirmacion) {
		this.confirmacion = confirmacion;
	}

	public CfdiRelacionados getCfdiRelacionados() {
		return cfdiRelacionados;
	}

	public void setCfdiRelacionados(CfdiRelacionados cfdiRelacionados) {
		this.cfdiRelacionados = cfdiRelacionados;
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
}
