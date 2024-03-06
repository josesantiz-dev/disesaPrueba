
package net.izel.ws.seg.pantallaguardar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import net.izel.ws.data.*;

/**
 * <p>Java class for PantallaGuardarRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PantallaGuardarRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://pantallaGuardar.seg.ws.izel.net/}headerWS" minOccurs="0"/>
 *         &lt;element name="parametros" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idFuncion" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idMenu" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PantallaGuardarRequestPrueba", propOrder = {
    "header",
    "parametros",
    "idFuncion",
    "idMenu"
})
public class PantallaGuardarRequest {

    protected HeaderWS header;
    protected String parametros;
    protected Long idFuncion;
    protected Long idMenu;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderWS }
     *     
     */
    public HeaderWS getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderWS }
     *     
     */
    public void setHeader(HeaderWS value) {
        this.header = value;
    }

    /**
     * Gets the value of the parametros property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParametros() {
        return parametros;
    }

    /**
     * Sets the value of the parametros property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParametros(String value) {
        this.parametros = value;
    }

    /**
     * Gets the value of the idFuncion property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdFuncion() {
        return idFuncion;
    }

    /**
     * Sets the value of the idFuncion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdFuncion(Long value) {
        this.idFuncion = value;
    }

    /**
     * Gets the value of the idMenu property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdMenu() {
        return idMenu;
    }

    /**
     * Sets the value of the idMenu property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdMenu(Long value) {
        this.idMenu = value;
    }

}
