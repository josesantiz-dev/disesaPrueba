
package net.izel.ws.seg.menuloginbuscar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MenuLoginBuscarResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MenuLoginBuscarResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuesta" type="{http://menuLoginBuscar.seg.ws.izel.net/}menuLoginBuscarRespuesta" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MenuLoginBuscarResponsePrueba", propOrder = {
    "respuesta"
})
public class MenuLoginBuscarResponse {

    protected MenuLoginBuscarRespuesta respuesta;

    /**
     * Gets the value of the respuesta property.
     * 
     * @return
     *     possible object is
     *     {@link MenuLoginBuscarRespuesta }
     *     
     */
    public MenuLoginBuscarRespuesta getRespuesta() {
        return respuesta;
    }

    /**
     * Sets the value of the respuesta property.
     * 
     * @param value
     *     allowed object is
     *     {@link MenuLoginBuscarRespuesta }
     *     
     */
    public void setRespuesta(MenuLoginBuscarRespuesta value) {
        this.respuesta = value;
    }

}
