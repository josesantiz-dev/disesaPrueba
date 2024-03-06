
package net.izel.ws.seg.funcionbuscar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FuncionBuscarResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuncionBuscarResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuesta" type="{http://funcionBuscar.seg.ws.izel.net/}funcionBuscarRespuesta" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuncionBuscarResponsePrueba", propOrder = {
    "respuesta"
})
public class FuncionBuscarResponse {

    protected FuncionBuscarRespuesta respuesta;

    /**
     * Gets the value of the respuesta property.
     * 
     * @return
     *     possible object is
     *     {@link FuncionBuscarRespuesta }
     *     
     */
    public FuncionBuscarRespuesta getRespuesta() {
        return respuesta;
    }

    /**
     * Sets the value of the respuesta property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuncionBuscarRespuesta }
     *     
     */
    public void setRespuesta(FuncionBuscarRespuesta value) {
        this.respuesta = value;
    }

}
