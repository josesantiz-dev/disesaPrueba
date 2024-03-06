
package net.izel.ws.seg.controlaccesousuarios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ControlAccesoUsuariosResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ControlAccesoUsuariosResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuesta" type="{http://controlAccesoUsuarios.seg.ws.izel.net/}controlAccesoUsuariosRespuesta" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ControlAccesoUsuariosResponse", propOrder = {
    "respuesta"
})
public class ControlAccesoUsuariosResponse {

    protected ControlAccesoUsuariosRespuesta respuesta;

    /**
     * Gets the value of the respuesta property.
     * 
     * @return
     *     possible object is
     *     {@link ControlAccesoUsuariosRespuesta }
     *     
     */
    public ControlAccesoUsuariosRespuesta getRespuesta() {
        return respuesta;
    }

    /**
     * Sets the value of the respuesta property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlAccesoUsuariosRespuesta }
     *     
     */
    public void setRespuesta(ControlAccesoUsuariosRespuesta value) {
        this.respuesta = value;
    }

}
