
package net.izel.ws.seg.consultausuarios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import net.izel.ws.data.RespuestaErrorXML;


/**
 * <p>Java class for consultaUsuariosRespuesta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultaUsuariosRespuesta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="body" type="{http://consultaUsuarios.seg.ws.izel.net/}consultaUsuarioBody" minOccurs="0"/>
 *         &lt;element name="errores" type="{http://consultaUsuarios.seg.ws.izel.net/}respuestaErrorXML" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultaUsuariosRespuesta", propOrder = {
    "body",
    "errores"
})
public class ConsultaUsuariosRespuesta {

    protected ConsultaUsuarioBody body;
    protected RespuestaErrorXML errores;

    /**
     * Gets the value of the body property.
     * 
     * @return
     *     possible object is
     *     {@link ConsultaUsuarioBody }
     *     
     */
    public ConsultaUsuarioBody getBody() {
        return body;
    }

    /**
     * Sets the value of the body property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsultaUsuarioBody }
     *     
     */
    public void setBody(ConsultaUsuarioBody value) {
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
