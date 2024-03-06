package net.giro.cxc.fe.v33.complemento.pagos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import net.giro.cxc.fe.v33.CMetodoPago;
import net.giro.cxc.fe.v33.CMoneda;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="IdDocumento" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="16"/>
 *             &lt;maxLength value="36"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="([a-f0-9A-F]{8}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{12})|([0-9]{3}-[0-9]{2}-[0-9]{9})"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Serie">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="25"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[^|]{1,25}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Folio">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="40"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[^|]{1,40}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="MonedaDR" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_Moneda" />
 *       &lt;attribute name="TipoCambioDR">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *             &lt;fractionDigits value="6"/>
 *             &lt;minInclusive value="0.000001"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="MetodoDePagoDR" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_MetodoPago" />
 *       &lt;attribute name="NumParcialidad">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[1-9][0-9]{0,2}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ImpSaldoAnt" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *       &lt;attribute name="ImpPagado" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *       &lt;attribute name="ImpSaldoInsoluto" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name="")
@XmlAccessorType(XmlAccessType.FIELD)
public class DoctoRelacionado implements Serializable {
	private static final long serialVersionUID = -2639042111074327015L;
	
	@XmlAttribute(name="IdDocumento", required=true)
    protected String idDocumento;
    @XmlAttribute(name="Serie")
    protected String serie;
    @XmlAttribute(name="Folio")
    protected String folio;
    @XmlAttribute(name="MonedaDR", required=true)
    protected CMoneda monedaDR;
    @XmlAttribute(name="TipoCambioDR")
    protected BigDecimal tipoCambioDR;
    @XmlAttribute(name="MetodoDePagoDR", required=true)
    protected CMetodoPago metodoDePagoDR;
    @XmlAttribute(name="NumParcialidad")
    protected BigInteger numParcialidad;
    @XmlAttribute(name="ImpSaldoAnt")
    protected BigDecimal impSaldoAnt;
    @XmlAttribute(name="ImpPagado")
    protected BigDecimal impPagado;
    @XmlAttribute(name="ImpSaldoInsoluto")
    protected BigDecimal impSaldoInsoluto;

    /**
     * Gets the value of the idDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumento() {
        return idDocumento;
    }

    /**
     * Sets the value of the idDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumento(String value) {
        this.idDocumento=value;
    }

    /**
     * Gets the value of the serie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerie() {
        return serie;
    }

    /**
     * Sets the value of the serie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerie(String value) {
        this.serie=value;
    }

    /**
     * Gets the value of the folio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Sets the value of the folio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolio(String value) {
        this.folio=value;
    }

    /**
     * Gets the value of the monedaDR property.
     * 
     * @return
     *     possible object is
     *     {@link CMoneda }
     *     
     */
    public CMoneda getMonedaDR() {
        return monedaDR;
    }

    /**
     * Sets the value of the monedaDR property.
     * 
     * @param value
     *     allowed object is
     *     {@link CMoneda }
     *     
     */
    public void setMonedaDR(CMoneda value) {
        this.monedaDR=value;
    }

    /**
     * Gets the value of the tipoCambioDR property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTipoCambioDR() {
        return tipoCambioDR;
    }

    /**
     * Sets the value of the tipoCambioDR property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTipoCambioDR(BigDecimal value) {
        this.tipoCambioDR=value;
    }

    /**
     * Gets the value of the metodoDePagoDR property.
     * 
     * @return
     *     possible object is
     *     {@link CMetodoPago }
     *     
     */
    public CMetodoPago getMetodoDePagoDR() {
        return metodoDePagoDR;
    }

    /**
     * Sets the value of the metodoDePagoDR property.
     * 
     * @param value
     *     allowed object is
     *     {@link CMetodoPago }
     *     
     */
    public void setMetodoDePagoDR(CMetodoPago value) {
        this.metodoDePagoDR=value;
    }

    /**
     * Gets the value of the numParcialidad property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumParcialidad() {
        return numParcialidad;
    }

    /**
     * Sets the value of the numParcialidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumParcialidad(BigInteger value) {
        this.numParcialidad=value;
    }

    /**
     * Gets the value of the impSaldoAnt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImpSaldoAnt() {
        return impSaldoAnt;
    }

    /**
     * Sets the value of the impSaldoAnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImpSaldoAnt(BigDecimal value) {
        this.impSaldoAnt=value;
    }

    /**
     * Gets the value of the impPagado property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImpPagado() {
        return impPagado;
    }

    /**
     * Sets the value of the impPagado property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImpPagado(BigDecimal value) {
        this.impPagado=value;
    }

    /**
     * Gets the value of the impSaldoInsoluto property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImpSaldoInsoluto() {
        return impSaldoInsoluto;
    }

    /**
     * Sets the value of the impSaldoInsoluto property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImpSaldoInsoluto(BigDecimal value) {
        this.impSaldoInsoluto=value;
    }
}
