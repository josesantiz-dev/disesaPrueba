
package net.izel.ws.seg.datos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pantalla complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pantalla">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="funcion" type="{http://menuLoginBuscar.seg.ws.izel.net/}funcion" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="menu" type="{http://menuLoginBuscar.seg.ws.izel.net/}menu" minOccurs="0"/>
 *         &lt;element name="parametros" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pantalla", propOrder = {
    "funcion",
    "id",
    "menu",
    "parametros"
})
public class Pantalla {

    protected Funcion funcion;
    protected Long id;
    protected Menu menu;
    protected String parametros;

    /**
     * Gets the value of the funcion property.
     * 
     * @return
     *     possible object is
     *     {@link Funcion }
     *     
     */
    public Funcion getFuncion() {
        return funcion;
    }

    /**
     * Sets the value of the funcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Funcion }
     *     
     */
    public void setFuncion(Funcion value) {
        this.funcion = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the menu property.
     * 
     * @return
     *     possible object is
     *     {@link Menu }
     *     
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Sets the value of the menu property.
     * 
     * @param value
     *     allowed object is
     *     {@link Menu }
     *     
     */
    public void setMenu(Menu value) {
        this.menu = value;
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

}
