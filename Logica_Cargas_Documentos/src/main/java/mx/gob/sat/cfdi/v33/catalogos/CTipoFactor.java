package mx.gob.sat.cfdi.v33.catalogos;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Clase Java para c_TipoFactor.
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="c_TipoFactor"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;whiteSpace value="collapse"/&gt;
 *     &lt;enumeration value="Tasa"/&gt;
 *     &lt;enumeration value="Cuota"/&gt;
 *     &lt;enumeration value="Exento"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "c_TipoFactor", namespace = "http://www.sat.gob.mx/sitio_internet/cfd/catalogos")
@XmlEnum
public enum CTipoFactor {
    @XmlEnumValue("Tasa")
    TASA("Tasa"),
    @XmlEnumValue("Cuota")
    CUOTA("Cuota"),
    @XmlEnumValue("Exento")
    EXENTO("Exento");
    private final String value;

    CTipoFactor(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CTipoFactor fromValue(String v) {
        for (CTipoFactor c: CTipoFactor.values()) {
            if (c.value.equals(v)) 
                return c;
        }
        
        throw new IllegalArgumentException(v);
    }
}
