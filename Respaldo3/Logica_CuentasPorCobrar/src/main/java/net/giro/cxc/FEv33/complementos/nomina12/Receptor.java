package net.giro.cxc.FEv33.complementos.nomina12;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SubContratacion" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="RfcLabora" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_RFC" />
 *                 &lt;attribute name="PorcentajeTiempo" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                       &lt;minInclusive value="0.001"/>
 *                       &lt;maxInclusive value="100.000"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                       &lt;pattern value="[0-9]{1,3}(.([0-9]{1,3}))?"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Curp" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_CURP" />
 *       &lt;attribute name="NumSeguridadSocial">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="15"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[0-9]{1,15}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="FechaInicioRelLaboral" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Fecha" />
 *       &lt;attribute name="Antigüedad">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="P(([1-9][0-9]{0,3})|0)W|P([1-9][0-9]?Y)?(([1-9]|1[012])M)?(0|[1-9]|[12][0-9]|3[01])D"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="TipoContrato" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_TipoContrato" />
 *       &lt;attribute name="Sindicalizado">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;enumeration value="Sí"/>
 *             &lt;enumeration value="No"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="TipoJornada" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_TipoJornada" />
 *       &lt;attribute name="TipoRegimen" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_TipoRegimen" />
 *       &lt;attribute name="NumEmpleado" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="15"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[^|]{1,15}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Departamento">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="100"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[^|]{1,100}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Puesto">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="100"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[^|]{1,100}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="RiesgoPuesto" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_RiesgoPuesto" />
 *       &lt;attribute name="PeriodicidadPago" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_PeriodicidadPago" />
 *       &lt;attribute name="Banco" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_Banco" />
 *       &lt;attribute name="CuentaBancaria" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_CuentaBancaria" />
 *       &lt;attribute name="SalarioBaseCotApor" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="SalarioDiarioIntegrado" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="ClaveEntFed" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_Estado" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "subContratacion"
})
public class Receptor implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "SubContratacion")
    protected List<SubContratacion> subContratacion;
    @XmlAttribute(name = "Curp", required = true)
    protected String curp;
    @XmlAttribute(name = "NumSeguridadSocial")
    protected String numSeguridadSocial;
    @XmlAttribute(name = "FechaInicioRelLaboral")
    protected XMLGregorianCalendar fechaInicioRelLaboral;
    @XmlAttribute(name = "Antig\u00fcedad")
    protected String antigüedad;
    @XmlAttribute(name = "TipoContrato", required = true)
    protected String tipoContrato;
    @XmlAttribute(name = "Sindicalizado")
    protected String sindicalizado;
    @XmlAttribute(name = "TipoJornada")
    protected String tipoJornada;
    @XmlAttribute(name = "TipoRegimen", required = true)
    protected String tipoRegimen;
    @XmlAttribute(name = "NumEmpleado", required = true)
    protected String numEmpleado;
    @XmlAttribute(name = "Departamento")
    protected String departamento;
    @XmlAttribute(name = "Puesto")
    protected String puesto;
    @XmlAttribute(name = "RiesgoPuesto")
    protected String riesgoPuesto;
    @XmlAttribute(name = "PeriodicidadPago", required = true)
    protected String periodicidadPago;
    @XmlAttribute(name = "Banco")
    protected String banco;
    @XmlAttribute(name = "CuentaBancaria")
    protected BigInteger cuentaBancaria;
    @XmlAttribute(name = "SalarioBaseCotApor")
    protected BigDecimal salarioBaseCotApor;
    @XmlAttribute(name = "SalarioDiarioIntegrado")
    protected BigDecimal salarioDiarioIntegrado;
    @XmlAttribute(name = "ClaveEntFed", required = true)
    protected CEstado claveEntFed;

    /**
     * Gets the value of the subContratacion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subContratacion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubContratacion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nomina.Receptor.SubContratacion }
     * 
     * 
     */
    public List<SubContratacion> getSubContratacion() {
        if (this.subContratacion == null) 
        	this.subContratacion = new ArrayList<SubContratacion>();
        return this.subContratacion;
    }

    /**
     * Gets the value of the curp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurp() {
        return curp;
    }

    /**
     * Sets the value of the curp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurp(String value) {
        this.curp = value;
    }

    /**
     * Gets the value of the numSeguridadSocial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumSeguridadSocial() {
        return numSeguridadSocial;
    }

    /**
     * Sets the value of the numSeguridadSocial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumSeguridadSocial(String value) {
        this.numSeguridadSocial = value;
    }

    /**
     * Gets the value of the fechaInicioRelLaboral property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaInicioRelLaboral() {
        return fechaInicioRelLaboral;
    }

    /**
     * Sets the value of the fechaInicioRelLaboral property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaInicioRelLaboral(XMLGregorianCalendar value) {
        this.fechaInicioRelLaboral = value;
    }

    /**
     * Gets the value of the antigüedad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAntigüedad() {
        return antigüedad;
    }

    /**
     * Sets the value of the antigüedad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAntigüedad(String value) {
        this.antigüedad = value;
    }

    /**
     * Gets the value of the tipoContrato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoContrato() {
        return tipoContrato;
    }

    /**
     * Sets the value of the tipoContrato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoContrato(String value) {
        this.tipoContrato = value;
    }

    /**
     * Gets the value of the sindicalizado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSindicalizado() {
        return sindicalizado;
    }

    /**
     * Sets the value of the sindicalizado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSindicalizado(String value) {
        this.sindicalizado = value;
    }

    /**
     * Gets the value of the tipoJornada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoJornada() {
        return tipoJornada;
    }

    /**
     * Sets the value of the tipoJornada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoJornada(String value) {
        this.tipoJornada = value;
    }

    /**
     * Gets the value of the tipoRegimen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRegimen() {
        return tipoRegimen;
    }

    /**
     * Sets the value of the tipoRegimen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRegimen(String value) {
        this.tipoRegimen = value;
    }

    /**
     * Gets the value of the numEmpleado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumEmpleado() {
        return numEmpleado;
    }

    /**
     * Sets the value of the numEmpleado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumEmpleado(String value) {
        this.numEmpleado = value;
    }

    /**
     * Gets the value of the departamento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Sets the value of the departamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartamento(String value) {
        this.departamento = value;
    }

    /**
     * Gets the value of the puesto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuesto() {
        return puesto;
    }

    /**
     * Sets the value of the puesto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuesto(String value) {
        this.puesto = value;
    }

    /**
     * Gets the value of the riesgoPuesto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiesgoPuesto() {
        return riesgoPuesto;
    }

    /**
     * Sets the value of the riesgoPuesto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiesgoPuesto(String value) {
        this.riesgoPuesto = value;
    }

    /**
     * Gets the value of the periodicidadPago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeriodicidadPago() {
        return periodicidadPago;
    }

    /**
     * Sets the value of the periodicidadPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeriodicidadPago(String value) {
        this.periodicidadPago = value;
    }

    /**
     * Gets the value of the banco property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBanco() {
        return banco;
    }

    /**
     * Sets the value of the banco property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBanco(String value) {
        this.banco = value;
    }

    /**
     * Gets the value of the cuentaBancaria property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCuentaBancaria() {
        return cuentaBancaria;
    }

    /**
     * Sets the value of the cuentaBancaria property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCuentaBancaria(BigInteger value) {
        this.cuentaBancaria = value;
    }

    /**
     * Gets the value of the salarioBaseCotApor property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSalarioBaseCotApor() {
        return salarioBaseCotApor;
    }

    /**
     * Sets the value of the salarioBaseCotApor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSalarioBaseCotApor(BigDecimal value) {
        this.salarioBaseCotApor = value;
    }

    /**
     * Gets the value of the salarioDiarioIntegrado property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSalarioDiarioIntegrado() {
        return salarioDiarioIntegrado;
    }

    /**
     * Sets the value of the salarioDiarioIntegrado property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSalarioDiarioIntegrado(BigDecimal value) {
        this.salarioDiarioIntegrado = value;
    }

    /**
     * Gets the value of the claveEntFed property.
     * 
     * @return
     *     possible object is
     *     {@link CEstado }
     *     
     */
    public CEstado getClaveEntFed() {
        return claveEntFed;
    }

    /**
     * Sets the value of the claveEntFed property.
     * 
     * @param value
     *     allowed object is
     *     {@link CEstado }
     *     
     */
    public void setClaveEntFed(CEstado value) {
        this.claveEntFed = value;
    }
}
