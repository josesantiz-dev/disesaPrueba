
package net.izel.ws.seg.listaUsuarios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListaUsuariosResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListaUsuariosResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuesta" type="{http://listaUsuarios.seg.ws.izel.net/}listaUsuariosRespuesta" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaUsuariosResponse", namespace = "http://ListaUsuarios.seg.ws.izel.net/", propOrder = {
    "respuesta"
})
public class ListaUsuariosResponse {

    protected ListaUsuariosRespuesta respuesta;

    /**
     * Gets the value of the respuesta property.
     * 
     * @return
     *     possible object is
     *     {@link ListaUsuariosRespuesta }
     *     
     */
    public ListaUsuariosRespuesta getRespuesta() {
        return respuesta;
    }

    /**
     * Sets the value of the respuesta property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaUsuariosRespuesta }
     *     
     */
    public void setRespuesta(ListaUsuariosRespuesta value) {
        this.respuesta = value;
    }

}
