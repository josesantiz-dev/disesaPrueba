
package net.izel.ws.seg.consultausuarios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for consultaUsuariosOBJ complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultaUsuariosOBJ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bloqueado" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="bloqueadoSeg" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="correo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="empleadoId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="empresaId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="expirado" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="expiradoSeg" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fechaCambio" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaIni" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaIniSeg" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaTerm" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaTermSeg" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idRs" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idSeg" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="modulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sistemaAnteriorId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="ultimoAcceso" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuarioSpark" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "consultaUsuariosOBJ", propOrder = {
    "bloqueado",
    "bloqueadoSeg",
    "correo",
    "empleadoId",
    "empresaId",
    "expirado",
    "expiradoSeg",
    "fechaCambio",
    "fechaIni",
    "fechaIniSeg",
    "fechaTerm",
    "fechaTermSeg",
    "id",
    "idRs",
    "idSeg",
    "modulo",
    "nombre",
    "password",
    "sistemaAnteriorId",
    "ultimoAcceso",
    "usuario",
    "usuarioSpark",
    "version"
})
public class ConsultaUsuariosOBJ {

    protected Long bloqueado;
    protected Long bloqueadoSeg;
    protected String correo;
    protected Long empleadoId;
    protected Long empresaId;
    protected Long expirado;
    protected Long expiradoSeg;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCambio;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaIni;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaIniSeg;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaTerm;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaTermSeg;
    protected Long id;
    protected Long idRs;
    protected Long idSeg;
    protected String modulo;
    protected String nombre;
    protected String password;
    protected Long sistemaAnteriorId;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar ultimoAcceso;
    protected String usuario;
    protected String usuarioSpark;
    protected Long version;

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
     * Gets the value of the bloqueadoSeg property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBloqueadoSeg() {
        return bloqueadoSeg;
    }

    /**
     * Sets the value of the bloqueadoSeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBloqueadoSeg(Long value) {
        this.bloqueadoSeg = value;
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
     * Gets the value of the empleadoId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEmpleadoId() {
        return empleadoId;
    }

    /**
     * Sets the value of the empleadoId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEmpleadoId(Long value) {
        this.empleadoId = value;
    }

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
     * Gets the value of the expiradoSeg property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getExpiradoSeg() {
        return expiradoSeg;
    }

    /**
     * Sets the value of the expiradoSeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setExpiradoSeg(Long value) {
        this.expiradoSeg = value;
    }

    /**
     * Gets the value of the fechaCambio property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCambio() {
        return fechaCambio;
    }

    /**
     * Sets the value of the fechaCambio property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCambio(XMLGregorianCalendar value) {
        this.fechaCambio = value;
    }

    /**
     * Gets the value of the fechaIni property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaIni() {
        return fechaIni;
    }

    /**
     * Sets the value of the fechaIni property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaIni(XMLGregorianCalendar value) {
        this.fechaIni = value;
    }

    /**
     * Gets the value of the fechaIniSeg property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaIniSeg() {
        return fechaIniSeg;
    }

    /**
     * Sets the value of the fechaIniSeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaIniSeg(XMLGregorianCalendar value) {
        this.fechaIniSeg = value;
    }

    /**
     * Gets the value of the fechaTerm property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaTerm() {
        return fechaTerm;
    }

    /**
     * Sets the value of the fechaTerm property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaTerm(XMLGregorianCalendar value) {
        this.fechaTerm = value;
    }

    /**
     * Gets the value of the fechaTermSeg property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaTermSeg() {
        return fechaTermSeg;
    }

    /**
     * Sets the value of the fechaTermSeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaTermSeg(XMLGregorianCalendar value) {
        this.fechaTermSeg = value;
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
     * Gets the value of the idRs property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdRs() {
        return idRs;
    }

    /**
     * Sets the value of the idRs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdRs(Long value) {
        this.idRs = value;
    }

    /**
     * Gets the value of the idSeg property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdSeg() {
        return idSeg;
    }

    /**
     * Sets the value of the idSeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdSeg(Long value) {
        this.idSeg = value;
    }

    /**
     * Gets the value of the modulo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModulo() {
        return modulo;
    }

    /**
     * Sets the value of the modulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModulo(String value) {
        this.modulo = value;
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
     * Gets the value of the sistemaAnteriorId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSistemaAnteriorId() {
        return sistemaAnteriorId;
    }

    /**
     * Sets the value of the sistemaAnteriorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSistemaAnteriorId(Long value) {
        this.sistemaAnteriorId = value;
    }

    /**
     * Gets the value of the ultimoAcceso property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUltimoAcceso() {
        return ultimoAcceso;
    }

    /**
     * Sets the value of the ultimoAcceso property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUltimoAcceso(XMLGregorianCalendar value) {
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
     * Gets the value of the usuarioSpark property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuarioSpark() {
        return usuarioSpark;
    }

    /**
     * Sets the value of the usuarioSpark property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuarioSpark(String value) {
        this.usuarioSpark = value;
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

}
