//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.02 at 08:56:37 PM MDT 
//

package net.giro.cxc.FEv33.complementos.pagos10;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.giro.cxc.fe.v33.complemento.pagos package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.giro.cxc.fe.v33.complemento.pagos
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Pagos }
     * 
     */
    public Pagos createPagos() {
        return new Pagos();
    }

    /**
     * Create an instance of {@link Pagos.Pago }
     * 
     */
    public Pago createPagosPago() {
        return new Pago();
    }

    /**
     * Create an instance of {@link Pagos.Pago.Impuestos }
     * 
     */
    public Impuestos createPagosPagoImpuestos() {
        return new Impuestos();
    }

    /**
     * Create an instance of {@link Pagos.Pago.Impuestos.Traslados }
     * 
     */
    public Traslados createPagosPagoImpuestosTraslados() {
        return new Traslados();
    }

    /**
     * Create an instance of {@link Pagos.Pago.Impuestos.Retenciones }
     * 
     */
    public Retenciones createPagosPagoImpuestosRetenciones() {
        return new Retenciones();
    }

    /**
     * Create an instance of {@link Pagos.Pago.DoctoRelacionado }
     * 
     */
    public DoctoRelacionado createPagosPagoDoctoRelacionado() {
        return new DoctoRelacionado();
    }

    /**
     * Create an instance of {@link Pagos.Pago.Impuestos.Traslados.Traslado }
     * 
     */
    public Traslado createPagosPagoImpuestosTrasladosTraslado() {
        return new Traslado();
    }

    /**
     * Create an instance of {@link Pagos.Pago.Impuestos.Retenciones.Retencion }
     * 
     */
    public Retencion createPagosPagoImpuestosRetencionesRetencion() {
        return new Retencion();
    }
}
