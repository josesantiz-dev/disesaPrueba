
package net.giro.cxc.realvirtual.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ClienteUsuarioTimbre complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
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
     * Obtiene el valor de la propiedad state.
     * 
     */
    public int getState() {
        return state;
    }

    /**
     * Define el valor de la propiedad state.
     * 
     */
    public void setState(int value) {
        this.state = value;
    }

    /**
     * Obtiene el valor de la propiedad stateDescript.
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
     * Define el valor de la propiedad stateDescript.
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
     * Obtiene el valor de la propiedad estado.
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
     * Define el valor de la propiedad estado.
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
     * Obtiene el valor de la propiedad descripcion.
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
     * Define el valor de la propiedad descripcion.
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
     * Obtiene el valor de la propiedad cfdi.
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
     * Define el valor de la propiedad cfdi.
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
     * Obtiene el valor de la propiedad noEncontrados.
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
     * Define el valor de la propiedad noEncontrados.
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
