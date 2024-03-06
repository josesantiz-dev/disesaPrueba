
package net.izel.ws.seg.rolmenueliminar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import net.izel.ws.data.*;

/**
 * <p>Java class for RolMenuEliminarResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RolMenuEliminarResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuesta" type="{http://rolMenuEliminar.seg.ws.izel.net/}respuestaXML" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RolMenuEliminarResponsePrueba", propOrder = {
    "respuesta"
})
public class RolMenuEliminarResponse {

    protected RespuestaXML respuesta;

    /**
     * Gets the value of the respuesta property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaXML }
     *     
     */
    public RespuestaXML getRespuesta() {
        return respuesta;
    }

    /**
     * Sets the value of the respuesta property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaXML }
     *     
     */
    public void setRespuesta(RespuestaXML value) {
        this.respuesta = value;
    }

}
