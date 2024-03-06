package net.giro.cxc.fe.v33;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="Rfc" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_RFC" />
 *       &lt;attribute name="Nombre">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="254"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[^|]{1,254}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ResidenciaFiscal" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_Pais" />
 *       &lt;attribute name="NumRegIdTrib">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="40"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="UsoCFDI" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_UsoCFDI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class Receptor implements Serializable {
	private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "Rfc", required = true)
    protected String rfc;
    @XmlAttribute(name = "Nombre")
    protected String nombre;
    @XmlAttribute(name = "ResidenciaFiscal")
    protected CPais residenciaFiscal;
    @XmlAttribute(name = "NumRegIdTrib")
    protected String numRegIdTrib;
    @XmlAttribute(name = "UsoCFDI", required = true)
    protected CUsoCFDI usoCFDI;

    /**
     * Gets the value of the rfc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * Sets the value of the rfc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfc(String value) {
        this.rfc = value;
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the residenciaFiscal property.
     * 
     * @return
     *     possible object is
     *     {@link CPais }
     *     
     */
    public CPais getResidenciaFiscal() {
        return residenciaFiscal;
    }

    /**
     * Sets the value of the residenciaFiscal property.
     * 
     * @param value
     *     allowed object is
     *     {@link CPais }
     *     
     */
    public void setResidenciaFiscal(CPais value) {
        this.residenciaFiscal = value;
    }

    /**
     * Gets the value of the numRegIdTrib property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumRegIdTrib() {
        return numRegIdTrib;
    }

    /**
     * Sets the value of the numRegIdTrib property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumRegIdTrib(String value) {
        this.numRegIdTrib = value;
    }

    /**
     * Gets the value of the usoCFDI property.
     * 
     * @return
     *     possible object is
     *     {@link CUsoCFDI }
     *     
     */
    public CUsoCFDI getUsoCFDI() {
        return usoCFDI;
    }

    /**
     * Sets the value of the usoCFDI property.
     * 
     * @param value
     *     allowed object is
     *     {@link CUsoCFDI }
     *     
     */
    public void setUsoCFDI(CUsoCFDI value) {
        this.usoCFDI = value;
    }

}
