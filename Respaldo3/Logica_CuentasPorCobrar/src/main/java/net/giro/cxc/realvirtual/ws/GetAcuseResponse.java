
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
 *         &lt;element name="GetAcuseResult" type="{http://tempuri.org/}StructAcuse" minOccurs="0"/>
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
    "getAcuseResult"
})
@XmlRootElement(name = "GetAcuseResponse")
public class GetAcuseResponse {

    @XmlElement(name = "GetAcuseResult")
    protected StructAcuse getAcuseResult;

    /**
     * Obtiene el valor de la propiedad getAcuseResult.
     * 
     * @return
     *     possible object is
     *     {@link StructAcuse }
     *     
     */
    public StructAcuse getGetAcuseResult() {
        return getAcuseResult;
    }

    /**
     * Define el valor de la propiedad getAcuseResult.
     * 
     * @param value
     *     allowed object is
     *     {@link StructAcuse }
     *     
     */
    public void setGetAcuseResult(StructAcuse value) {
        this.getAcuseResult = value;
    }

}
