
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
 *         &lt;element name="ConsultarSolicitudesPendientesDeCancelacionResult" type="{http://tempuri.org/}StructSolicitudesPendientes" minOccurs="0"/>
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
    "consultarSolicitudesPendientesDeCancelacionResult"
})
@XmlRootElement(name = "ConsultarSolicitudesPendientesDeCancelacionResponse")
public class ConsultarSolicitudesPendientesDeCancelacionResponse {

    @XmlElement(name = "ConsultarSolicitudesPendientesDeCancelacionResult")
    protected StructSolicitudesPendientes consultarSolicitudesPendientesDeCancelacionResult;

    /**
     * Obtiene el valor de la propiedad consultarSolicitudesPendientesDeCancelacionResult.
     * 
     * @return
     *     possible object is
     *     {@link StructSolicitudesPendientes }
     *     
     */
    public StructSolicitudesPendientes getConsultarSolicitudesPendientesDeCancelacionResult() {
        return consultarSolicitudesPendientesDeCancelacionResult;
    }

    /**
     * Define el valor de la propiedad consultarSolicitudesPendientesDeCancelacionResult.
     * 
     * @param value
     *     allowed object is
     *     {@link StructSolicitudesPendientes }
     *     
     */
    public void setConsultarSolicitudesPendientesDeCancelacionResult(StructSolicitudesPendientes value) {
        this.consultarSolicitudesPendientesDeCancelacionResult = value;
    }

}
