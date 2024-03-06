
package net.izel.ws.seg.rolmenuguardar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import net.izel.ws.data.*;

/**
 * <p>Java class for RolMenuGuardarRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RolMenuGuardarRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://rolMenuGuardar.seg.ws.izel.net/}headerWS" minOccurs="0"/>
 *         &lt;element name="idRol" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="idMenu" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RolMenuGuardarRequestPrueba", propOrder = {
    "header",
    "idRol",
    "idMenu"
})
public class RolMenuGuardarRequest {

    protected HeaderWS header;
    protected long idRol;
    protected long idMenu;

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
     * Gets the value of the idRol property.
     * 
     */
    public long getIdRol() {
        return idRol;
    }

    /**
     * Sets the value of the idRol property.
     * 
     */
    public void setIdRol(long value) {
        this.idRol = value;
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

}
