
package net.izel.ws.seg.listaUsuarios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import net.izel.ws.data.HeaderWS;


/**
 * <p>Java class for ListaUsuariosRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListaUsuariosRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://listaUsuarios.seg.ws.izel.net/}headerWS" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaUsuariosRequest", namespace = "http://ListaUsuarios.seg.ws.izel.net/", propOrder = {
    "header"
})
public class ListaUsuariosRequest {

    protected HeaderWS header;

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

}
