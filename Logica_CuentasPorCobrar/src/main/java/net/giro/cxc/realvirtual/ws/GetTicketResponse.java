
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
 *         &lt;element name="GetTicketResult" type="{http://tempuri.org/}StructCfd" minOccurs="0"/>
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
    "getTicketResult"
})
@XmlRootElement(name = "GetTicketResponse")
public class GetTicketResponse {

    @XmlElement(name = "GetTicketResult")
    protected StructCfd getTicketResult;

    /**
     * Obtiene el valor de la propiedad getTicketResult.
     * 
     * @return
     *     possible object is
     *     {@link StructCfd }
     *     
     */
    public StructCfd getGetTicketResult() {
        return getTicketResult;
    }

    /**
     * Define el valor de la propiedad getTicketResult.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCfd }
     *     
     */
    public void setGetTicketResult(StructCfd value) {
        this.getTicketResult = value;
    }

}
