
package net.izel.ws.seg.menumodificar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import net.izel.ws.data.*;


/**
 * <p>Java class for MenuModificarRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MenuModificarRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://menuModificar.seg.ws.izel.net/}headerWS" minOccurs="0"/>
 *         &lt;element name="idMenu" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="orden" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="titulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MenuModificarRequestPrueba", propOrder = {
    "header",
    "idMenu",
    "titulo",
    "orden"
})
public class MenuModificarRequest {

    protected HeaderWS header;
    protected long idMenu;
    protected String titulo;
    protected long orden;

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
     * Gets the value of the idMenu property.
     * 
     */
    public long getIdMenu() {
        return idMenu;
    }

    /**
     * Sets the value of the idMenu property.
     * 
     */
    public void setIdMenu(long value) {
        this.idMenu = value;
    }

    /**
     * Gets the value of the titulo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Sets the value of the titulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitulo(String value) {
        this.titulo = value;
    }

    /**
     * Gets the value of the orden property.
     * 
     */
    public long getOrden() {
        return orden;
    }

    /**
     * Sets the value of the orden property.
     * 
     */
    public void setOrden(long value) {
        this.orden = value;
    }
}
