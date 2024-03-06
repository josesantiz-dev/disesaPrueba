
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
 *         &lt;element name="TimbrarCDClaveResult" type="{http://tempuri.org/}StructCfd" minOccurs="0"/>
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
    "timbrarCDClaveResult"
})
@XmlRootElement(name = "TimbrarCDClaveResponse")
public class TimbrarCDClaveResponse {

    @XmlElement(name = "TimbrarCDClaveResult")
    protected StructCfd timbrarCDClaveResult;

    /**
     * Obtiene el valor de la propiedad timbrarCDClaveResult.
     * 
     * @return
     *     possible object is
     *     {@link StructCfd }
     *     
     */
    public StructCfd getTimbrarCDClaveResult() {
        return timbrarCDClaveResult;
    }

    /**
     * Define el valor de la propiedad timbrarCDClaveResult.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCfd }
     *     
     */
    public void setTimbrarCDClaveResult(StructCfd value) {
        this.timbrarCDClaveResult = value;
    }

}
