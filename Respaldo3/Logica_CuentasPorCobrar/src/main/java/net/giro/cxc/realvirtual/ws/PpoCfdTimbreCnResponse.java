
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
 *         &lt;element name="PpoCfdTimbre_CnResult" type="{http://tempuri.org/}ClienteUsuarioTimbre" minOccurs="0"/>
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
    "ppoCfdTimbreCnResult"
})
@XmlRootElement(name = "PpoCfdTimbre_CnResponse")
public class PpoCfdTimbreCnResponse {

    @XmlElement(name = "PpoCfdTimbre_CnResult")
    protected ClienteUsuarioTimbre ppoCfdTimbreCnResult;

    /**
     * Obtiene el valor de la propiedad ppoCfdTimbreCnResult.
     * 
     * @return
     *     possible object is
     *     {@link ClienteUsuarioTimbre }
     *     
     */
    public ClienteUsuarioTimbre getPpoCfdTimbreCnResult() {
        return ppoCfdTimbreCnResult;
    }

    /**
     * Define el valor de la propiedad ppoCfdTimbreCnResult.
     * 
     * @param value
     *     allowed object is
     *     {@link ClienteUsuarioTimbre }
     *     
     */
    public void setPpoCfdTimbreCnResult(ClienteUsuarioTimbre value) {
        this.ppoCfdTimbreCnResult = value;
    }

}
