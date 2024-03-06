
package net.izel.ws.seg.usuarioBuscar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import net.izel.ws.data.HeaderWS;


/**
 * <p>Java class for UsuarioBuscarRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UsuarioBuscarRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://usuarioBuscar.seg.ws.izel.net/}headerWS" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valorBusqueda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UsuarioBuscarRequest", propOrder = {
    "header",
    "nombre",
    "valorBusqueda"
})
public class UsuarioBuscarRequest {

    protected HeaderWS header;
    protected String nombre;
    protected String valorBusqueda;

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
     * Gets the value of the valorBusqueda property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorBusqueda() {
        return valorBusqueda;
    }

    /**
     * Sets the value of the valorBusqueda property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorBusqueda(String value) {
        this.valorBusqueda = value;
    }

}
