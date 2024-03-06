
package org.tempuri.realvirtual.ws.v33;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="base64Cfd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalfactura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rfcreceptor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "base64Cfd",
    "totalfactura",
    "rfcreceptor"
})
@XmlRootElement(name = "CancelTicketExt")
public class CancelTicketExt {

    protected String base64Cfd;
    protected String totalfactura;
    protected String rfcreceptor;

    /**
     * Gets the value of the base64Cfd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBase64Cfd() {
        return base64Cfd;
    }

    /**
     * Sets the value of the base64Cfd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBase64Cfd(String value) {
        this.base64Cfd = value;
    }

    /**
     * Gets the value of the totalfactura property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalfactura() {
        return totalfactura;
    }

    /**
     * Sets the value of the totalfactura property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalfactura(String value) {
        this.totalfactura = value;
    }

    /**
     * Gets the value of the rfcreceptor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcreceptor() {
        return rfcreceptor;
    }

    /**
     * Sets the value of the rfcreceptor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcreceptor(String value) {
        this.rfcreceptor = value;
    }

}
