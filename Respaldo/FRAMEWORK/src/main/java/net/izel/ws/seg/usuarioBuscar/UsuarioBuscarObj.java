
package net.izel.ws.seg.usuarioBuscar;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for usuarioBuscarObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="usuarioBuscarObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bloqueado" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="correo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="expirado" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fechaCambio" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaIni" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaTerm" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ultimoAcceso" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="sistemaAnteriorId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "usuarioBuscarObj", propOrder = {
    "bloqueado",
    "correo",
    "expirado",
    "fechaCambio",
    "fechaIni",
    "fechaTerm",
    "id",
    "nombre",
    "password",
    "ultimoAcceso",
    "sistemaAnteriorId",
    "usuario",
    "version"    
})
public class UsuarioBuscarObj {

    protected Long bloqueado;
    protected String correo;
    protected Long expirado;
    @XmlSchemaType(name = "dateTime")
    protected Date fechaCambio;
    @XmlSchemaType(name = "dateTime")
    protected Date fechaIni;
    @XmlSchemaType(name = "dateTime")
    protected Date fechaTerm;
    protected Long id;
    protected String nombre;
    protected String password;
    @XmlSchemaType(name = "dateTime")
    protected Date ultimoAcceso;
    protected String usuario;
    protected Long version;
    protected Long sistemaAnteriorId;
    /**
     * Gets the value of the bloqueado property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBloqueado() {
        return bloqueado;
    }

    /**
     * Sets the value of the bloqueado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBloqueado(Long value) {
        this.bloqueado = value;
    }

    /**
     * Gets the value of the correo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Sets the value of the correo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorreo(String value) {
        this.correo = value;
    }

    /**
     * Gets the value of the expirado property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getExpirado() {
        return expirado;
    }

    /**
     * Sets the value of the expirado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setExpirado(Long value) {
        this.expirado = value;
    }

    /**
     * Gets the value of the fechaCambio property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getFechaCambio() {
        return fechaCambio;
    }

    /**
     * Sets the value of the fechaCambio property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setFechaCambio(Date value) {
        this.fechaCambio = value;
    }

    /**
     * Gets the value of the fechaIni property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getFechaIni() {
        return fechaIni;
    }

    /**
     * Sets the value of the fechaIni property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setFechaIni(Date value) {
        this.fechaIni = value;
    }

    /**
     * Gets the value of the fechaTerm property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getFechaTerm() {
        return fechaTerm;
    }

    /**
     * Sets the value of the fechaTerm property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setFechaTerm(Date value) {
        this.fechaTerm = value;
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
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the ultimoAcceso property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    /**
     * Sets the value of the ultimoAcceso property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setUltimoAcceso(Date value) {
        this.ultimoAcceso = value;
    }

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuario(String value) {
        this.usuario = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setVersion(Long value) {
        this.version = value;
    }
    
    public Long getSistemaAnteriorId() {
		return sistemaAnteriorId;
	}
	public void setSistemaAnteriorId(Long sistemaAnteriorId) {
		this.sistemaAnteriorId = sistemaAnteriorId;
	}

}
