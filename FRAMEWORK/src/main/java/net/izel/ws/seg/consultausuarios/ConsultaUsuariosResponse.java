
package net.izel.ws.seg.consultausuarios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConsultaUsuariosResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConsultaUsuariosResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuesta" type="{http://consultaUsuarios.seg.ws.izel.net/}consultaUsuariosRespuesta" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultaUsuariosResponse", propOrder = {
    "respuesta"
})
public class ConsultaUsuariosResponse {

    protected ConsultaUsuariosRespuesta respuesta;

    /**
     * Gets the value of the respuesta property.
     * 
     * @return
     *     possible object is
     *     {@link ConsultaUsuariosRespuesta }
     *     
     */
    public ConsultaUsuariosRespuesta getRespuesta() {
        return respuesta;
    }

    /**
     * Sets the value of the respuesta property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsultaUsuariosRespuesta }
     *     
     */
    public void setRespuesta(ConsultaUsuariosRespuesta value) {
        this.respuesta = value;
    }

}
