package net.giro.cxc.FEv33.complementos.pagos10;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import net.giro.cxc.FEv33.CMoneda;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DoctoRelacionado" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="IdDocumento" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="16"/>
 *                       &lt;maxLength value="36"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                       &lt;pattern value="([a-f0-9A-F]{8}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{12})|([0-9]{3}-[0-9]{2}-[0-9]{9})"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="Serie">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="25"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                       &lt;pattern value="[^|]{1,25}"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="Folio">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="40"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                       &lt;pattern value="[^|]{1,40}"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="MonedaDR" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_Moneda" />
 *                 &lt;attribute name="TipoCambioDR">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                       &lt;fractionDigits value="6"/>
 *                       &lt;minInclusive value="0.000001"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="MetodoDePagoDR" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_MetodoPago" />
 *                 &lt;attribute name="NumParcialidad">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                       &lt;whiteSpace value="collapse"/>
 *                       &lt;pattern value="[1-9][0-9]{0,2}"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="ImpSaldoAnt" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *                 &lt;attribute name="ImpPagado" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *                 &lt;attribute name="ImpSaldoInsoluto" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Impuestos" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Retenciones" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Retencion" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="Impuesto" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_Impuesto" />
 *                                     &lt;attribute name="Importe" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Traslados" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Traslado" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="Impuesto" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_Impuesto" />
 *                                     &lt;attribute name="TipoFactor" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_TipoFactor" />
 *                                     &lt;attribute name="TasaOCuota" use="required">
 *                                       &lt;simpleType>
 *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                           &lt;fractionDigits value="6"/>
 *                                           &lt;minInclusive value="0.000000"/>
 *                                           &lt;whiteSpace value="collapse"/>
 *                                         &lt;/restriction>
 *                                       &lt;/simpleType>
 *                                     &lt;/attribute>
 *                                     &lt;attribute name="Importe" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="TotalImpuestosRetenidos" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *                 &lt;attribute name="TotalImpuestosTrasladados" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="FechaPago" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_FechaH" />
 *       &lt;attribute name="FormaDePagoP" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_FormaPago" />
 *       &lt;attribute name="MonedaP" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_Moneda" />
 *       &lt;attribute name="TipoCambioP">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *             &lt;fractionDigits value="6"/>
 *             &lt;minInclusive value="0.000001"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Monto" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *       &lt;attribute name="NumOperacion">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="100"/>
 *             &lt;pattern value="[^|]{1,100}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="RfcEmisorCtaOrd">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="12"/>
 *             &lt;maxLength value="13"/>
 *             &lt;pattern value="XEXX010101000|[A-Z&amp;Ñ]{3}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[A-Z0-9]{2}[0-9A]"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="NomBancoOrdExt">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="300"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[^|]{1,300}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="CtaOrdenante">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="10"/>
 *             &lt;maxLength value="50"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[A-Z0-9_]{10,50}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="RfcEmisorCtaBen" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_RFC_PM" />
 *       &lt;attribute name="CtaBeneficiario">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="10"/>
 *             &lt;maxLength value="50"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[A-Z0-9_]{10,50}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="TipoCadPago" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Pagos}c_TipoCadenaPago" />
 *       &lt;attribute name="CertPago">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}base64Binary">
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="CadPago">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="8192"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="SelloPago">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}base64Binary">
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name="", propOrder={"doctoRelacionado", "impuestos"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Pago implements Serializable {
	private static final long serialVersionUID = 4497875037345063695L;
	
	@XmlElement(name="DoctoRelacionado")
    protected List<DoctoRelacionado> doctoRelacionado;
    @XmlElement(name="Impuestos")
    protected List<Impuestos> impuestos;
    @XmlAttribute(name="FechaPago", required=true)
    protected XMLGregorianCalendar fechaPago;
    @XmlAttribute(name="FormaDePagoP", required=true)
    protected String formaDePagoP;
    @XmlAttribute(name="MonedaP", required=true)
    protected CMoneda monedaP;
    @XmlAttribute(name="TipoCambioP")
    protected BigDecimal tipoCambioP;
    @XmlAttribute(name="Monto", required=true)
    protected BigDecimal monto;
    @XmlAttribute(name="NumOperacion")
    protected String numOperacion;
    @XmlAttribute(name="RfcEmisorCtaOrd")
    protected String rfcEmisorCtaOrd;
    @XmlAttribute(name="NomBancoOrdExt")
    protected String nomBancoOrdExt;
    @XmlAttribute(name="CtaOrdenante")
    protected String ctaOrdenante;
    @XmlAttribute(name="RfcEmisorCtaBen")
    protected String rfcEmisorCtaBen;
    @XmlAttribute(name="CtaBeneficiario")
    protected String ctaBeneficiario;
    @XmlAttribute(name="TipoCadPago")
    protected String tipoCadPago;
    @XmlAttribute(name="CertPago")
    protected byte[] certPago;
    @XmlAttribute(name="CadPago")
    protected String cadPago;
    @XmlAttribute(name="SelloPago")
    protected byte[] selloPago;

    /**
     * Gets the value of the doctoRelacionado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the doctoRelacionado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDoctoRelacionado().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Pagos.Pago.DoctoRelacionado }
     * 
     * 
     */
    public List<DoctoRelacionado> getDoctoRelacionado() {
        if (doctoRelacionado == null) {
            doctoRelacionado=new ArrayList<DoctoRelacionado>();
        }
        return this.doctoRelacionado;
    }

    /**
     * Gets the value of the impuestos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the impuestos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImpuestos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Pagos.Pago.Impuestos }
     * 
     * 
     */
    public List<Impuestos> getImpuestos() {
        if (impuestos == null) {
            impuestos=new ArrayList<Impuestos>();
        }
        return this.impuestos;
    }

    /**
     * Gets the value of the fechaPago property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaPago() {
        return fechaPago;
    }

    /**
     * Sets the value of the fechaPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaPago(XMLGregorianCalendar value) {
        this.fechaPago=value;
    }

    /**
     * Gets the value of the formaDePagoP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormaDePagoP() {
        return formaDePagoP;
    }

    /**
     * Sets the value of the formaDePagoP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormaDePagoP(String value) {
        this.formaDePagoP=value;
    }

    /**
     * Gets the value of the monedaP property.
     * 
     * @return
     *     possible object is
     *     {@link CMoneda }
     *     
     */
    public CMoneda getMonedaP() {
        return monedaP;
    }

    /**
     * Sets the value of the monedaP property.
     * 
     * @param value
     *     allowed object is
     *     {@link CMoneda }
     *     
     */
    public void setMonedaP(CMoneda value) {
        this.monedaP=value;
    }

    /**
     * Gets the value of the tipoCambioP property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTipoCambioP() {
        return tipoCambioP;
    }

    /**
     * Sets the value of the tipoCambioP property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTipoCambioP(BigDecimal value) {
        this.tipoCambioP=value;
    }

    /**
     * Gets the value of the monto property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMonto() {
        return monto;
    }

    /**
     * Sets the value of the monto property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMonto(BigDecimal value) {
        this.monto=value;
    }

    /**
     * Gets the value of the numOperacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumOperacion() {
        return numOperacion;
    }

    /**
     * Sets the value of the numOperacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumOperacion(String value) {
        this.numOperacion=value;
    }

    /**
     * Gets the value of the rfcEmisorCtaOrd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcEmisorCtaOrd() {
        return rfcEmisorCtaOrd;
    }

    /**
     * Sets the value of the rfcEmisorCtaOrd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcEmisorCtaOrd(String value) {
        this.rfcEmisorCtaOrd=value;
    }

    /**
     * Gets the value of the nomBancoOrdExt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomBancoOrdExt() {
        return nomBancoOrdExt;
    }

    /**
     * Sets the value of the nomBancoOrdExt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomBancoOrdExt(String value) {
        this.nomBancoOrdExt=value;
    }

    /**
     * Gets the value of the ctaOrdenante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtaOrdenante() {
        return ctaOrdenante;
    }

    /**
     * Sets the value of the ctaOrdenante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtaOrdenante(String value) {
        this.ctaOrdenante=value;
    }

    /**
     * Gets the value of the rfcEmisorCtaBen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcEmisorCtaBen() {
        return rfcEmisorCtaBen;
    }

    /**
     * Sets the value of the rfcEmisorCtaBen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcEmisorCtaBen(String value) {
        this.rfcEmisorCtaBen=value;
    }

    /**
     * Gets the value of the ctaBeneficiario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtaBeneficiario() {
        return ctaBeneficiario;
    }

    /**
     * Sets the value of the ctaBeneficiario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtaBeneficiario(String value) {
        this.ctaBeneficiario=value;
    }

    /**
     * Gets the value of the tipoCadPago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCadPago() {
        return tipoCadPago;
    }

    /**
     * Sets the value of the tipoCadPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCadPago(String value) {
        this.tipoCadPago=value;
    }

    /**
     * Gets the value of the certPago property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCertPago() {
        return certPago;
    }

    /**
     * Sets the value of the certPago property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCertPago(byte[] value) {
        this.certPago=value;
    }

    /**
     * Gets the value of the cadPago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCadPago() {
        return cadPago;
    }

    /**
     * Sets the value of the cadPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCadPago(String value) {
        this.cadPago=value;
    }

    /**
     * Gets the value of the selloPago property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSelloPago() {
        return selloPago;
    }

    /**
     * Sets the value of the selloPago property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSelloPago(byte[] value) {
        this.selloPago=value;
    }
}
