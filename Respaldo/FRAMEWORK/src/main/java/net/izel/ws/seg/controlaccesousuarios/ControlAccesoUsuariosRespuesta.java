
package net.izel.ws.seg.controlaccesousuarios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import net.izel.ws.data.RespuestaErrorXML;


/**
 * <p>Java class for controlAccesoUsuariosRespuesta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="controlAccesoUsuariosRespuesta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="body" type="{http://controlAccesoUsuarios.seg.ws.izel.net/}controlAccesoUsuariosBody" minOccurs="0"/>
 *         &lt;element name="errores" type="{http://controlAccesoUsuarios.seg.ws.izel.net/}respuestaErrorXML" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "controlAccesoUsuariosRespuesta", propOrder = {
    "body",
    "errores"
})
public class ControlAccesoUsuariosRespuesta {

    protected ControlAccesoUsuariosBody body;
    protected RespuestaErrorXML errores;

    /**
     * Gets the value of the body property.
     * 
     * @return
     *     possible object is
     *     {@link ControlAccesoUsuariosBody }
     *     
     */
    public ControlAccesoUsuariosBody getBody() {
        return body;
    }

    /**
     * Sets the value of the body property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlAccesoUsuariosBody }
     *     
     */
    public void setBody(ControlAccesoUsuariosBody value) {
        this.body = value;
    }

    /**
     * Gets the value of the errores property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaErrorXML }
     *     
     */
    public RespuestaErrorXML getErrores() {
        return errores;
    }

    /**
     * Sets the value of the errores property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaErrorXML }
     *     
     */
    public void setErrores(RespuestaErrorXML value) {
        this.errores = value;
    }

}
