//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.18 at 11:10:04 AM MDT 
//


package net.giro.cxc.fe.v33.complemento.impuestoslocales;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.giro.cxc.fe.v33.complemento.impuestoslocales package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.giro.cxc.fe.v33.complemento.impuestoslocales
     * 
     */
    public ObjectFactory() {}

    /**
     * Create an instance of {@link ImpuestosLocales }
     * 
     */
    public ImpuestosLocales createImpuestosLocales() {
        return new ImpuestosLocales();
    }

    /**
     * Create an instance of {@link ImpuestosLocales.RetencionesLocales }
     * 
     */
    public RetencionesLocales createImpuestosLocalesRetencionesLocales() {
        return new RetencionesLocales();
    }

    /**
     * Create an instance of {@link ImpuestosLocales.TrasladosLocales }
     * 
     */
    public TrasladosLocales createImpuestosLocalesTrasladosLocales() {
        return new TrasladosLocales();
    }
}