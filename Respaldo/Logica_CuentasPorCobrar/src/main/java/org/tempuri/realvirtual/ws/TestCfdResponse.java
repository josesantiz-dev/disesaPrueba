
package org.tempuri.realvirtual.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TestCfdResult" type="{http://tempuri.org/}StructCfd"/>
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

    @XmlElement(name = "TestCfdResult", required = true)
    protected StructCfd testCfdResult;

    /**
     * Gets the value of the testCfdResult property.
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
     * Sets the value of the testCfdResult property.
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
