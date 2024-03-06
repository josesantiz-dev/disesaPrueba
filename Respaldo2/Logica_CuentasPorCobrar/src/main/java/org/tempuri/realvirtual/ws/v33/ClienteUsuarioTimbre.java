
package org.tempuri.realvirtual.ws.v33;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ClienteUsuarioTimbre complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClienteUsuarioTimbre">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="StateDescript" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Cfdi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NoEncontrados" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClienteUsuarioTimbre", propOrder = {
    "state",
    "stateDescript",
    "estado",
    "descripcion",
    "cfdi",
    "noEncontrados"
})
public class ClienteUsuarioTimbre {

    @XmlElement(name = "State")
    protected int state;
    @XmlElement(name = "StateDescript")
    protected String stateDescript;
    @XmlElement(name = "Estado")
    protected String estado;
    @XmlElement(name = "Descripcion")
    protected String descripcion;
    @XmlElement(name = "Cfdi")
    protected String cfdi;
    @XmlElement(name = "NoEncontrados")
    protected String noEncontrados;

    /**
     * Gets the value of the state property.
     * 
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     */
    public void setState(int value) {
        this.state = value;
    }

    /**
     * Gets the value of the stateDescript property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateDescript() {
        return stateDescript;
    }

    /**
     * Sets the value of the stateDescript property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateDescript(String value) {
        this.stateDescript = value;
    }

    /**
     * Gets the value of the estado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

    /**
     * Gets the value of the descripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Gets the value of the cfdi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfdi() {
        return cfdi;
    }

    /**
     * Sets the value of the cfdi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfdi(String value) {
        this.cfdi = value;
    }

    /**
     * Gets the value of the noEncontrados property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoEncontrados() {
        return noEncontrados;
    }

    /**
     * Sets the value of the noEncontrados property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoEncontrados(String value) {
        this.noEncontrados = value;
    }

}
