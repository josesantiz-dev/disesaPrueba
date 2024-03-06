
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
 *         &lt;element name="EstatusCFDIResult" type="{http://tempuri.org/}StructCancel" minOccurs="0"/>
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
    "estatusCFDIResult"
})
@XmlRootElement(name = "EstatusCFDIResponse")
public class EstatusCFDIResponse {

    @XmlElement(name = "EstatusCFDIResult")
    protected StructCancel estatusCFDIResult;

    /**
     * Obtiene el valor de la propiedad estatusCFDIResult.
     * 
     * @return
     *     possible object is
     *     {@link StructCancel }
     *     
     */
    public StructCancel getEstatusCFDIResult() {
        return estatusCFDIResult;
    }

    /**
     * Define el valor de la propiedad estatusCFDIResult.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCancel }
     *     
     */
    public void setEstatusCFDIResult(StructCancel value) {
        this.estatusCFDIResult = value;
    }

}
