
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
 *         &lt;element name="ProbarTimbradoCFDI33PrincipalResult" type="{http://tempuri.org/}StructCfd" minOccurs="0"/>
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
    "probarTimbradoCFDI33PrincipalResult"
})
@XmlRootElement(name = "ProbarTimbradoCFDI33PrincipalResponse")
public class ProbarTimbradoCFDI33PrincipalResponse {

    @XmlElement(name = "ProbarTimbradoCFDI33PrincipalResult")
    protected StructCfd probarTimbradoCFDI33PrincipalResult;

    /**
     * Obtiene el valor de la propiedad probarTimbradoCFDI33PrincipalResult.
     * 
     * @return
     *     possible object is
     *     {@link StructCfd }
     *     
     */
    public StructCfd getProbarTimbradoCFDI33PrincipalResult() {
        return probarTimbradoCFDI33PrincipalResult;
    }

    /**
     * Define el valor de la propiedad probarTimbradoCFDI33PrincipalResult.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCfd }
     *     
     */
    public void setProbarTimbradoCFDI33PrincipalResult(StructCfd value) {
        this.probarTimbradoCFDI33PrincipalResult = value;
    }

}
