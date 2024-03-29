
package net.izel.ws.seg.menuloginbuscar;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import net.izel.ws.seg.datos.Rol;
import net.izel.ws.seg.datos.RolMenu;


/**
 * <p>Java class for menuLoginBuscarBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="menuLoginBuscarBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="params" type="{http://menuLoginBuscar.seg.ws.izel.net/}rolMenu" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="roles" type="{http://menuLoginBuscar.seg.ws.izel.net/}rol" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "menuLoginBuscarBody", propOrder = {
    "params",
    "roles"
})
public class MenuLoginBuscarBody {

    @XmlElement(nillable = true)
    protected List<RolMenu> params;
    @XmlElement(nillable = true)
    protected List<Rol> roles;

    /**
     * Gets the value of the params property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the params property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParams().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RolMenu }
     * 
     * 
     */
    public List<RolMenu> getParams() {
        if (params == null) {
            params = new ArrayList<RolMenu>();
        }
        return this.params;
    }

    /**
     * Gets the value of the roles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Rol }
     * 
     * 
     */
    public List<Rol> getRoles() {
        if (roles == null) {
            roles = new ArrayList<Rol>();
        }
        return this.roles;
    }

}
