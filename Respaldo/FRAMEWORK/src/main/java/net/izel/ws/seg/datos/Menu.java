
package net.izel.ws.seg.datos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for menu complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="menu">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="empresaId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="estatusId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fechaBaja" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaCreacion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idPadre" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="motivoBajaId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="orden" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="pantallas" type="{http://menuLoginBuscar.seg.ws.izel.net/}pantalla" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "menu", propOrder = {
    "empresaId",
    "estatusId",
    "fechaBaja",
    "fechaCreacion",
    "id",
    "idPadre",
    "motivoBajaId",
    "orden",
    "pantallas",
    "titulo"
})
public class Menu {

    protected Long empresaId;
    protected Long estatusId;
    protected Date fechaBaja;
    protected Date fechaCreacion;
    protected Long id;
    protected Long motivoBajaId;
    @XmlElement(nillable = true)
    protected List<Pantalla> pantallas;
    protected String titulo;
    private Long orden;
	private Long idPadre;
    /**
     * Gets the value of the empresaId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEmpresaId() {
        return empresaId;
    }

    /**
     * Sets the value of the empresaId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEmpresaId(Long value) {
        this.empresaId = value;
    }

    /**
     * Gets the value of the estatusId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEstatusId() {
        return estatusId;
    }

    /**
     * Sets the value of the estatusId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEstatusId(Long value) {
        this.estatusId = value;
    }

    /**
     * Gets the value of the fechaBaja property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getFechaBaja() {
        return fechaBaja;
    }

    /**
     * Sets the value of the fechaBaja property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setFechaBaja(Date value) {
        this.fechaBaja = value;
    }

    /**
     * Gets the value of the fechaCreacion property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Sets the value of the fechaCreacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setFechaCreacion(Date value) {
        this.fechaCreacion = value;
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
     * Gets the value of the motivoBajaId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMotivoBajaId() {
        return motivoBajaId;
    }

    /**
     * Sets the value of the motivoBajaId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMotivoBajaId(Long value) {
        this.motivoBajaId = value;
    }

    /**
     * Gets the value of the pantallas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pantallas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPantallas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Pantalla }
     * 
     * 
     */
    public List<Pantalla> getPantallas() {
        if (pantallas == null) {
            pantallas = new ArrayList<Pantalla>();
        }
        return this.pantallas;
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

    public Long getOrden() {
		return orden;
	}
    
	public void setOrden(Long orden) {
		this.orden = orden;
	}
	
	public Long getIdPadre() {
		return idPadre;
	}
	
	public void setIdPadre(Long idPadre) {
		this.idPadre = idPadre;
	}
}
