package net.giro.cxc.FEv33.complementos.nomina12;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="EntidadSNCF" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="OrigenRecurso" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_OrigenRecurso" />
 *                 &lt;attribute name="MontoRecursoPropio" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Curp" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_CURP" />
 *       &lt;attribute name="RegistroPatronal">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="20"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[^|]{1,20}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="RfcPatronOrigen" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_RFC" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "entidadSNCF"
})
public class Emisor implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "EntidadSNCF")
    protected EntidadSNCF entidadSNCF;
    @XmlAttribute(name = "Curp")
    protected String curp;
    @XmlAttribute(name = "RegistroPatronal")
    protected String registroPatronal;
    @XmlAttribute(name = "RfcPatronOrigen")
    protected String rfcPatronOrigen;

    /**
     * Gets the value of the entidadSNCF property.
     * 
     * @return
     *     possible object is
     *     {@link Nomina.Emisor.EntidadSNCF }
     *     
     */
    public EntidadSNCF getEntidadSNCF() {
        return entidadSNCF;
    }

    /**
     * Sets the value of the entidadSNCF property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nomina.Emisor.EntidadSNCF }
     *     
     */
    public void setEntidadSNCF(EntidadSNCF value) {
        this.entidadSNCF = value;
    }

    /**
     * Gets the value of the curp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurp() {
        return curp;
    }

    /**
     * Sets the value of the curp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurp(String value) {
        this.curp = value;
    }

    /**
     * Gets the value of the registroPatronal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistroPatronal() {
        return registroPatronal;
    }

    /**
     * Sets the value of the registroPatronal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistroPatronal(String value) {
        this.registroPatronal = value;
    }

    /**
     * Gets the value of the rfcPatronOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcPatronOrigen() {
        return rfcPatronOrigen;
    }

    /**
     * Sets the value of the rfcPatronOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcPatronOrigen(String value) {
        this.rfcPatronOrigen = value;
    }
}
