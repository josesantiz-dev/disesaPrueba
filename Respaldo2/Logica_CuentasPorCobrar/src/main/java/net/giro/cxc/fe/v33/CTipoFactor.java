//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.02 at 08:55:34 PM MDT 
//


package net.giro.cxc.fe.v33;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for c_TipoFactor.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="c_TipoFactor">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;whiteSpace value="collapse"/>
 *     &lt;enumeration value="Tasa"/>
 *     &lt;enumeration value="Cuota"/>
 *     &lt;enumeration value="Exento"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
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
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
