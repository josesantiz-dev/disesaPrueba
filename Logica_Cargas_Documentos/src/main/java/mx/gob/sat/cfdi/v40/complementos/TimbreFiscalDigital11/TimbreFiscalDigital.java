package mx.gob.sat.cfdi.v40.complementos.TimbreFiscalDigital11;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;

/**
 * <p>Clase Java para anonymous complex type.
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" fixed="1.0" /&gt;
 *       &lt;attribute name="UUID" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;whiteSpace value="collapse"/&gt;
 *             &lt;length value="36"/&gt;
 *             &lt;pattern value="[a-f0-9A-F]{8}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{12}"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="FechaTimbrado" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}dateTime"&gt;
 *             &lt;whiteSpace value="collapse"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="selloCFD" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;whiteSpace value="collapse"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="noCertificadoSAT" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;whiteSpace value="collapse"/&gt;
 *             &lt;length value="20"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="selloSAT" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;whiteSpace value="collapse"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "TimbreFiscalDigital")
public class TimbreFiscalDigital implements Serializable {
	private static final long serialVersionUID = 11L;
    @XmlAttribute(name = "Version", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String version;
    @XmlAttribute(name = "UUID", required = true)
    protected String uuid;
    @XmlAttribute(name = "FechaTimbrado", required = true)
    protected XMLGregorianCalendar fechaTimbrado;
    @XmlAttribute(name = "RfcProvCertif", required = true)
    protected String rfcProvCertif;
    @XmlAttribute(name = "Leyenda")
    protected String leyenda;
    @XmlAttribute(name = "SelloCFD", required = true)
    protected String selloCFD;
    @XmlAttribute(name = "NoCertificadoSAT", required = true)
    protected String noCertificadoSAT;
    @XmlAttribute(name = "SelloSAT", required = true)
    protected String selloSAT;

    /**
     * Obtiene el valor de la propiedad version.
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getVersion() {
    	version = (version != null && ! "".equals(version.trim()) ? version : "1.1");
        return version;
    }

    /**
     * Define el valor de la propiedad version.
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Obtiene el valor de la propiedad uuid.
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * Define el valor de la propiedad uuid.
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setUUID(String value) {
        this.uuid = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaTimbrado.
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getFechaTimbrado() {
        return fechaTimbrado;
    }

    /**
     * Define el valor de la propiedad fechaTimbrado.
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     */
    public void setFechaTimbrado(XMLGregorianCalendar value) {
        this.fechaTimbrado = value;
    }

    /**
     * Obtiene el valor de la propiedad rfcProvCertif.
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getRfcProvCertif() {
        return rfcProvCertif;
    }

    /**
     * Define el valor de la propiedad rfcProvCertif.
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setRfcProvCertif(String value) {
        this.rfcProvCertif = value;
    }

    /**
     * Obtiene el valor de la propiedad leyenda.
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getLeyenda() {
        return leyenda;
    }

    /**
     * Define el valor de la propiedad leyenda.
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setLeyenda(String value) {
        this.leyenda = value;
    }

    /**
     * Obtiene el valor de la propiedad selloCFD.
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getSelloCFD() {
        return selloCFD;
    }

    /**
     * Define el valor de la propiedad selloCFD.
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setSelloCFD(String value) {
        this.selloCFD = value;
    }

    /**
     * Obtiene el valor de la propiedad noCertificadoSAT.
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getNoCertificadoSAT() {
        return noCertificadoSAT;
    }

    /**
     * Define el valor de la propiedad noCertificadoSAT.
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setNoCertificadoSAT(String value) {
        this.noCertificadoSAT = value;
    }

    /**
     * Obtiene el valor de la propiedad selloSAT.
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getSelloSAT() {
        return selloSAT;
    }

    /**
     * Define el valor de la propiedad selloSAT.
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setSelloSAT(String value) {
        this.selloSAT = value;
    }
}
