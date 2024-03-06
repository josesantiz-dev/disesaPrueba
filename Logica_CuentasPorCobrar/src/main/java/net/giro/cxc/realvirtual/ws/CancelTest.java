
package net.giro.cxc.realvirtual.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CanB64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "canB64"
})
@XmlRootElement(name = "CancelTest")
public class CancelTest {

    @XmlElement(name = "CanB64")
    protected String canB64;

    /**
     * Obtiene el valor de la propiedad canB64.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCanB64() {
        return canB64;
    }

    /**
     * Define el valor de la propiedad canB64.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCanB64(String value) {
        this.canB64 = value;
    }

}
