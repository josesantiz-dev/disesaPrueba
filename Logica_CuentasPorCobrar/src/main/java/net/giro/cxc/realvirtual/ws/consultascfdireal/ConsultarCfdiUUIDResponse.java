package net.giro.cxc.realvirtual.ws.consultascfdireal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Consultar_cfdi_UUIDResult" type="{http://tempuri.org/}StructCfdi" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "consultarCfdiUUIDResult"
})
@XmlRootElement(name = "Consultar_cfdi_UUIDResponse")
public class ConsultarCfdiUUIDResponse {
    @XmlElement(name = "Consultar_cfdi_UUIDResult")
    protected StructCfdi consultarCfdiUUIDResult;

    /**
     * Obtiene el valor de la propiedad consultarCfdiUUIDResult.
     * 
     * @return
     *     possible object is
     *     {@link StructCfdi }
     *     
     */
    public StructCfdi getConsultarCfdiUUIDResult() {
        return consultarCfdiUUIDResult;
    }

    /**
     * Define el valor de la propiedad consultarCfdiUUIDResult.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCfdi }
     *     
     */
    public void setConsultarCfdiUUIDResult(StructCfdi value) {
        this.consultarCfdiUUIDResult = value;
    }
}
