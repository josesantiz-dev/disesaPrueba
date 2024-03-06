
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
 *         &lt;element name="GetTicketSCResult" type="{http://tempuri.org/}StructCfd" minOccurs="0"/>
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
    "getTicketSCResult"
})
@XmlRootElement(name = "GetTicketSCResponse")
public class GetTicketSCResponse {

    @XmlElement(name = "GetTicketSCResult")
    protected StructCfd getTicketSCResult;

    /**
     * Obtiene el valor de la propiedad getTicketSCResult.
     * 
     * @return
     *     possible object is
     *     {@link StructCfd }
     *     
     */
    public StructCfd getGetTicketSCResult() {
        return getTicketSCResult;
    }

    /**
     * Define el valor de la propiedad getTicketSCResult.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCfd }
     *     
     */
    public void setGetTicketSCResult(StructCfd value) {
        this.getTicketSCResult = value;
    }

}
