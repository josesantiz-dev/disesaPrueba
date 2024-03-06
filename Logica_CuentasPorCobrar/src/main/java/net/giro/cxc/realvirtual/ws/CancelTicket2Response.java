
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
 *         &lt;element name="CancelTicket2Result" type="{http://tempuri.org/}StructCancel" minOccurs="0"/>
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
    "cancelTicket2Result"
})
@XmlRootElement(name = "CancelTicket2Response")
public class CancelTicket2Response {

    @XmlElement(name = "CancelTicket2Result")
    protected StructCancel cancelTicket2Result;

    /**
     * Obtiene el valor de la propiedad cancelTicket2Result.
     * 
     * @return
     *     possible object is
     *     {@link StructCancel }
     *     
     */
    public StructCancel getCancelTicket2Result() {
        return cancelTicket2Result;
    }

    /**
     * Define el valor de la propiedad cancelTicket2Result.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCancel }
     *     
     */
    public void setCancelTicket2Result(StructCancel value) {
        this.cancelTicket2Result = value;
    }

}
