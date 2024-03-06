
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
 *         &lt;element name="TestCfd33Result" type="{http://tempuri.org/}StructCfd" minOccurs="0"/>
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
    "testCfd33Result"
})
@XmlRootElement(name = "TestCfd33Response")
public class TestCfd33Response {

    @XmlElement(name = "TestCfd33Result")
    protected StructCfd testCfd33Result;

    /**
     * Obtiene el valor de la propiedad testCfd33Result.
     * 
     * @return
     *     possible object is
     *     {@link StructCfd }
     *     
     */
    public StructCfd getTestCfd33Result() {
        return testCfd33Result;
    }

    /**
     * Define el valor de la propiedad testCfd33Result.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCfd }
     *     
     */
    public void setTestCfd33Result(StructCfd value) {
        this.testCfd33Result = value;
    }

}
