
package net.izel.ws.seg.menubuscar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MenuBuscarResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MenuBuscarResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuesta" type="{http://menuBuscar.seg.ws.izel.net/}menuBuscarRespuesta" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MenuBuscarResponsePrueba", propOrder = {
    "respuesta"
})
public class MenuBuscarResponse {

    protected MenuBuscarRespuesta respuesta;

    /**
     * Gets the value of the respuesta property.
     * 
     * @return
     *     possible object is
     *     {@link MenuBuscarRespuesta }
     *     
     */
    public MenuBuscarRespuesta getRespuesta() {
        return respuesta;
    }

    /**
     * Sets the value of the respuesta property.
     * 
     * @param value
     *     allowed object is
     *     {@link MenuBuscarRespuesta }
     *     
     */
    public void setRespuesta(MenuBuscarRespuesta value) {
        this.respuesta = value;
    }

}
