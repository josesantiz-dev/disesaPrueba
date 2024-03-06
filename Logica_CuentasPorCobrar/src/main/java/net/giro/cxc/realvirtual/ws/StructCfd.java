
package net.giro.cxc.realvirtual.ws;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para StructCfd complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="StructCfd">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RfcEmisor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RfcReceptor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Serie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Folio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FechaExpedicion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MontoOperacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MontoImpuesto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TipoComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Cadena" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Firma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SerieCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Cfdi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Timbre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StructCfd", propOrder = {
    "rfcEmisor",
    "rfcReceptor",
    "version",
    "serie",
    "folio",
    "fechaExpedicion",
    "montoOperacion",
    "montoImpuesto",
    "tipoComprobante",
    "cadena",
    "firma",
    "serieCertificado",
    "cfdi",
    "timbre",
    "descripcion",
    "state"
})
public class StructCfd {

    @XmlElement(name = "RfcEmisor")
    protected String rfcEmisor;
    @XmlElement(name = "RfcReceptor")
    protected String rfcReceptor;
    @XmlElement(name = "Version")
    protected String version;
    @XmlElement(name = "Serie")
    protected String serie;
    @XmlElement(name = "Folio")
    protected String folio;
    @XmlElement(name = "FechaExpedicion")
    protected String fechaExpedicion;
    @XmlElement(name = "MontoOperacion")
    protected String montoOperacion;
    @XmlElement(name = "MontoImpuesto")
    protected String montoImpuesto;
    @XmlElement(name = "TipoComprobante")
    protected String tipoComprobante;
    @XmlElement(name = "Cadena")
    protected String cadena;
    @XmlElement(name = "Firma")
    protected String firma;
    @XmlElement(name = "SerieCertificado")
    protected String serieCertificado;
    @XmlElement(name = "Cfdi")
    protected String cfdi;
    @XmlElement(name = "Timbre")
    protected String timbre;
    @XmlElement(name = "Descripcion")
    protected String descripcion;
    protected int state;

    /**
     * Obtiene el valor de la propiedad rfcEmisor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcEmisor() {
        return rfcEmisor;
    }

    /**
     * Define el valor de la propiedad rfcEmisor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcEmisor(String value) {
        this.rfcEmisor = value;
    }

    /**
     * Obtiene el valor de la propiedad rfcReceptor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcReceptor() {
        return rfcReceptor;
    }

    /**
     * Define el valor de la propiedad rfcReceptor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcReceptor(String value) {
        this.rfcReceptor = value;
    }

    /**
     * Obtiene el valor de la propiedad version.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Define el valor de la propiedad version.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Obtiene el valor de la propiedad serie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerie() {
        return serie;
    }

    /**
     * Define el valor de la propiedad serie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerie(String value) {
        this.serie = value;
    }

    /**
     * Obtiene el valor de la propiedad folio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Define el valor de la propiedad folio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolio(String value) {
        this.folio = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaExpedicion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaExpedicion() {
        return fechaExpedicion;
    }

    /**
     * Define el valor de la propiedad fechaExpedicion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaExpedicion(String value) {
        this.fechaExpedicion = value;
    }

    /**
     * Obtiene el valor de la propiedad montoOperacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMontoOperacion() {
        return montoOperacion;
    }

    /**
     * Define el valor de la propiedad montoOperacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMontoOperacion(String value) {
        this.montoOperacion = value;
    }

    /**
     * Obtiene el valor de la propiedad montoImpuesto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMontoImpuesto() {
        return montoImpuesto;
    }

    /**
     * Define el valor de la propiedad montoImpuesto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMontoImpuesto(String value) {
        this.montoImpuesto = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * Define el valor de la propiedad tipoComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoComprobante(String value) {
        this.tipoComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad cadena.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCadena() {
        return cadena;
    }

    /**
     * Define el valor de la propiedad cadena.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCadena(String value) {
        this.cadena = value;
    }

    /**
     * Obtiene el valor de la propiedad firma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirma() {
        return firma;
    }

    /**
     * Define el valor de la propiedad firma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirma(String value) {
        this.firma = value;
    }

    /**
     * Obtiene el valor de la propiedad serieCertificado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerieCertificado() {
        return serieCertificado;
    }

    /**
     * Define el valor de la propiedad serieCertificado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerieCertificado(String value) {
        this.serieCertificado = value;
    }

    /**
     * Obtiene el valor de la propiedad cfdi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfdi() {
        return cfdi;
    }

    /**
     * Define el valor de la propiedad cfdi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfdi(String value) {
        this.cfdi = value;
    }

    /**
     * Obtiene el valor de la propiedad timbre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimbre() {
        return timbre;
    }

    /**
     * Define el valor de la propiedad timbre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimbre(String value) {
        this.timbre = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Define el valor de la propiedad descripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Obtiene el valor de la propiedad state.
     * 
     */
    public int getState() {
        return state;
    }

    /**
     * Define el valor de la propiedad state.
     * 
     */
    public void setState(int value) {
        this.state = value;
    }

    // ---------------------------------------------------
    // EXTENDIDOS 
    // ---------------------------------------------------
    
    public void validaMensaje() {
		Pattern pat = null;
		Matcher match = null;
		int codigo = 0;
		
		try {
			if (this.descripcion == null || "".equals(this.descripcion.trim()))
				return;
			pat = Pattern.compile("(^\\d+)(\\s*[\\.\\-]{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.descripcion);
			if (match.find()) 
				codigo = Integer.valueOf(match.group(1));
			if (codigo > 0 && this.state == 0)
				this.state = codigo;
		} catch (Exception e) { 
			e.printStackTrace();
		}
    }
}
