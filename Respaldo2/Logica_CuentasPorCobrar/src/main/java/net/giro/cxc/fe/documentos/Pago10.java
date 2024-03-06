package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Pago10 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name="FechaPago", required=true)
	private String fechaPago;
	@XmlAttribute(name="FormaDePagoP", required=true)
	private String formaDePagoP;
	@XmlAttribute(name="MonedaP", required=true)
	private String monedaP;
	@XmlAttribute(name="TipoCambioP", required=false)
	private String tipoCambioP;
	@XmlAttribute(name="Monto", required=true)
	private String monto;
	@XmlAttribute(name="NumOperacion", required=false)
	private String numOperacion;
	@XmlAttribute(name="RfcEmisorCtaOrd", required=false)
	private String rfcEmisorCtaOrd;
	@XmlAttribute(name="NomBancoOrdExt", required=false)
	private String nomBancoOrdExt;
	@XmlAttribute(name="CtaOrdenante", required=false)
	private String ctaOrdenante;
	@XmlAttribute(name="RfcEmisorCtaBen", required=false)
	private String rfcEmisorCtaBen;
	@XmlAttribute(name="CtaBeneficiario", required=false)
	private String ctaBeneficiario;
	@XmlAttribute(name="TipoCadPago", required=false)
	private String tipoCadPago;
	@XmlAttribute(name="CertPago", required=false)
	private String certPago;
	@XmlAttribute(name="CadPago", required=false)
	private String cadPago;
	@XmlAttribute(name="SelloPago", required=false)
	private String selloPago;
	@XmlElement(name="Impuestos", required=false, namespace="http://www.sat.gob.mx/Pagos") 
	private Impuestos impuestos;
	@XmlElement(name="DoctoRelacionado", required=false, namespace="http://www.sat.gob.mx/Pagos") 
	private DoctoRelacionado doctoRelacionado;
	
	
	public Pago10() {}
	
	public Pago10(String fechaPago, String formaDePago, String moneda, String monto) {
		super();
		this.fechaPago = fechaPago;
		this.formaDePagoP = formaDePago;
		this.monedaP = moneda;
		this.monto = monto;
	}

	
	public String getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}

	public String getFormaDePagoP() {
		return formaDePagoP;
	}

	public void setFormaDePagoP(String formaDePagoP) {
		this.formaDePagoP = formaDePagoP;
	}

	/**
	 * Moneda del Pago
	 * @return
	 */
	public String getMonedaP() {
		return monedaP;
	}

	/**
	 * Moneda del pago
	 * @param monedaP
	 */
	public void setMonedaP(String monedaP) {
		this.monedaP = monedaP;
	}

	public String getTipoCambioP() {
		return tipoCambioP;
	}

	public void setTipoCambioP(String tipoCambioP) {
		this.tipoCambioP = tipoCambioP;
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getNumOperacion() {
		return numOperacion;
	}

	public void setNumOperacion(String numOperacion) {
		this.numOperacion = numOperacion;
	}

	public String getRfcEmisorCtaOrd() {
		return rfcEmisorCtaOrd;
	}

	public void setRfcEmisorCtaOrd(String rfcEmisorCtaOrd) {
		this.rfcEmisorCtaOrd = rfcEmisorCtaOrd;
	}

	public String getNomBancoOrdExt() {
		return nomBancoOrdExt;
	}

	public void setNomBancoOrdExt(String nomBancoOrdExt) {
		this.nomBancoOrdExt = nomBancoOrdExt;
	}

	public String getCtaOrdenante() {
		return ctaOrdenante;
	}

	public void setCtaOrdenante(String ctaOrdenante) {
		this.ctaOrdenante = ctaOrdenante;
	}

	public String getRfcEmisorCtaBen() {
		return rfcEmisorCtaBen;
	}

	public void setRfcEmisorCtaBen(String rfcEmisorCtaBen) {
		this.rfcEmisorCtaBen = rfcEmisorCtaBen;
	}

	public String getCtaBeneficiario() {
		return ctaBeneficiario;
	}

	public void setCtaBeneficiario(String ctaBeneficiario) {
		this.ctaBeneficiario = ctaBeneficiario;
	}

	public String getTipoCadPago() {
		return tipoCadPago;
	}

	public void setTipoCadPago(String tipoCadPago) {
		this.tipoCadPago = tipoCadPago;
	}

	public String getCertPago() {
		return certPago;
	}

	public void setCertPago(String certPago) {
		this.certPago = certPago;
	}

	public String getCadPago() {
		return cadPago;
	}

	public void setCadPago(String cadPago) {
		this.cadPago = cadPago;
	}

	public String getSelloPago() {
		return selloPago;
	}

	public void setSelloPago(String selloPago) {
		this.selloPago = selloPago;
	}

	public Impuestos getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(Impuestos impuestos) {
		this.impuestos = impuestos;
	}

	public DoctoRelacionado getDoctoRelacionado() {
		return doctoRelacionado;
	}

	public void setDoctoRelacionado(DoctoRelacionado doctoRelacionado) {
		this.doctoRelacionado = doctoRelacionado;
	}
}
