
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
 *         &lt;element name="AceptarRechazarSolicitudDeCancelacionResult" type="{http://tempuri.org/}StructAceptarRechazar" minOccurs="0"/>
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
    "aceptarRechazarSolicitudDeCancelacionResult"
})
@XmlRootElement(name = "AceptarRechazarSolicitudDeCancelacionResponse")
public class AceptarRechazarSolicitudDeCancelacionResponse {

    @XmlElement(name = "AceptarRechazarSolicitudDeCancelacionResult")
    protected StructAceptarRechazar aceptarRechazarSolicitudDeCancelacionResult;

    /**
     * Obtiene el valor de la propiedad aceptarRechazarSolicitudDeCancelacionResult.
     * 
     * @return
     *     possible object is
     *     {@link StructAceptarRechazar }
     *     
     */
    public StructAceptarRechazar getAceptarRechazarSolicitudDeCancelacionResult() {
        return aceptarRechazarSolicitudDeCancelacionResult;
    }

    /**
     * Define el valor de la propiedad aceptarRechazarSolicitudDeCancelacionResult.
     * 
     * @param value
     *     allowed object is
     *     {@link StructAceptarRechazar }
     *     
     */
    public void setAceptarRechazarSolicitudDeCancelacionResult(StructAceptarRechazar value) {
        this.aceptarRechazarSolicitudDeCancelacionResult = value;
    }

}
