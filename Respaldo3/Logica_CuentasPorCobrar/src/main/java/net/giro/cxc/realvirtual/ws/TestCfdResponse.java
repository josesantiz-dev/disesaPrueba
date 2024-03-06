
package net.giro.cxc.realvirtual.ws;

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
 *         &lt;element name="TestCfdResult" type="{http://tempuri.org/}StructCfd" minOccurs="0"/>
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
    "testCfdResult"
})
@XmlRootElement(name = "TestCfdResponse")
public class TestCfdResponse {

    @XmlElement(name = "TestCfdResult")
    protected StructCfd testCfdResult;

    /**
     * Obtiene el valor de la propiedad testCfdResult.
     * 
     * @return
     *     possible object is
     *     {@link StructCfd }
     *     
     */
    public StructCfd getTestCfdResult() {
        return testCfdResult;
    }

    /**
     * Define el valor de la propiedad testCfdResult.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCfd }
     *     
     */
    public void setTestCfdResult(StructCfd value) {
        this.testCfdResult = value;
    }

}
