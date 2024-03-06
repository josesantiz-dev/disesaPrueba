
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
 *         &lt;element name="ProbarTimbradoCFDI33SecundarioResult" type="{http://tempuri.org/}StructCfd" minOccurs="0"/>
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
    "probarTimbradoCFDI33SecundarioResult"
})
@XmlRootElement(name = "ProbarTimbradoCFDI33SecundarioResponse")
public class ProbarTimbradoCFDI33SecundarioResponse {

    @XmlElement(name = "ProbarTimbradoCFDI33SecundarioResult")
    protected StructCfd probarTimbradoCFDI33SecundarioResult;

    /**
     * Obtiene el valor de la propiedad probarTimbradoCFDI33SecundarioResult.
     * 
     * @return
     *     possible object is
     *     {@link StructCfd }
     *     
     */
    public StructCfd getProbarTimbradoCFDI33SecundarioResult() {
        return probarTimbradoCFDI33SecundarioResult;
    }

    /**
     * Define el valor de la propiedad probarTimbradoCFDI33SecundarioResult.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCfd }
     *     
     */
    public void setProbarTimbradoCFDI33SecundarioResult(StructCfd value) {
        this.probarTimbradoCFDI33SecundarioResult = value;
    }

}
