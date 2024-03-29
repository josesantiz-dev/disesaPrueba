package mx.gob.sat.cfdi.v32;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Tipo definido para expresar información aduanera
 * <p>Clase Java para t_InformacionAduanera complex type.
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <pre>
 * &lt;complexType name="t_InformacionAduanera"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="numero" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;minLength value="1"/&gt;
 *             &lt;whiteSpace value="collapse"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="fecha" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}date"&gt;
 *             &lt;whiteSpace value="collapse"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="aduana"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;minLength value="1"/&gt;
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
@XmlType(name = "t_InformacionAduanera")
public class TInformacionAduanera implements Serializable {
	private static final long serialVersionUID = 1L;
    @XmlAttribute(name = "numero", required = true)
    protected String numero;
    @XmlAttribute(name = "fecha", required = true)
    protected XMLGregorianCalendar fecha;
    @XmlAttribute(name = "aduana")
    protected String aduana;

    /**
     * Obtiene el valor de la propiedad numero.
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Define el valor de la propiedad numero.
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setNumero(String value) {
        this.numero = value;
    }

    /**
     * Obtiene el valor de la propiedad fecha.
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getFecha() {
        return fecha;
    }

    /**
     * Define el valor de la propiedad fecha.
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     */
    public void setFecha(XMLGregorianCalendar value) {
        this.fecha = value;
    }

    /**
     * Obtiene el valor de la propiedad aduana.
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getAduana() {
        return aduana;
    }

    /**
     * Define el valor de la propiedad aduana.
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setAduana(String value) {
        this.aduana = value;
    }
}
