package mx.gob.sat.cfdi.v33.catalogos;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Clase Java para c_TipoDeComprobante.
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="c_TipoDeComprobante"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;whiteSpace value="collapse"/&gt;
 *     &lt;enumeration value="I"/&gt;
 *     &lt;enumeration value="E"/&gt;
 *     &lt;enumeration value="T"/&gt;
 *     &lt;enumeration value="N"/&gt;
 *     &lt;enumeration value="P"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "c_TipoDeComprobante", namespace = "http://www.sat.gob.mx/sitio_internet/cfd/catalogos")
@XmlEnum
public enum CTipoDeComprobante {
    I,
    E,
    T,
    N,
    P;

    public String value() {
        return name();
    }

    public static CTipoDeComprobante fromValue(String v) {
        return valueOf(v);
    }
}
