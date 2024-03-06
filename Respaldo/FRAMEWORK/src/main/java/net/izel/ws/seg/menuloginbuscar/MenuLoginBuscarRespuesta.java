
package net.izel.ws.seg.menuloginbuscar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import net.izel.ws.data.RespuestaErrorXML;


/**
 * <p>Java class for menuLoginBuscarRespuesta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="menuLoginBuscarRespuesta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="body" type="{http://menuLoginBuscar.seg.ws.izel.net/}menuLoginBuscarBody" minOccurs="0"/>
 *         &lt;element name="errores" type="{http://menuLoginBuscar.seg.ws.izel.net/}respuestaErrorXML" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "menuLoginBuscarRespuesta", propOrder = {
    "body",
    "errores"
})
public class MenuLoginBuscarRespuesta {

    protected MenuLoginBuscarBody body;
    protected RespuestaErrorXML errores;

    /**
     * Gets the value of the body property.
     * 
     * @return
     *     possible object is
     *     {@link MenuLoginBuscarBody }
     *     
     */
    public MenuLoginBuscarBody getBody() {
        return body;
    }

    /**
     * Sets the value of the body property.
     * 
     * @param value
     *     allowed object is
     *     {@link MenuLoginBuscarBody }
     *     
     */
    public void setBody(MenuLoginBuscarBody value) {
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
