package mx.gob.sat.cfdi.v33.complementos.pagos10;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mx.gob.sat.pagos package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mx.gob.sat.pagos
     * 
     */
    public ObjectFactory() {}

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
    public Pagos.Pago createPagosPago() {
        return new Pagos.Pago();
    }

    /**
     * Create an instance of {@link Pagos.Pago.Impuestos }
     * 
     */
    public Pagos.Pago.Impuestos createPagosPagoImpuestos() {
        return new Pagos.Pago.Impuestos();
    }

    /**
     * Create an instance of {@link Pagos.Pago.Impuestos.Traslados }
     * 
     */
    public Pagos.Pago.Impuestos.Traslados createPagosPagoImpuestosTraslados() {
        return new Pagos.Pago.Impuestos.Traslados();
    }

    /**
     * Create an instance of {@link Pagos.Pago.Impuestos.Retenciones }
     * 
     */
    public Pagos.Pago.Impuestos.Retenciones createPagosPagoImpuestosRetenciones() {
        return new Pagos.Pago.Impuestos.Retenciones();
    }

    /**
     * Create an instance of {@link Pagos.Pago.DoctoRelacionado }
     * 
     */
    public Pagos.Pago.DoctoRelacionado createPagosPagoDoctoRelacionado() {
        return new Pagos.Pago.DoctoRelacionado();
    }

    /**
     * Create an instance of {@link Pagos.Pago.Impuestos.Traslados.Traslado }
     * 
     */
    public Pagos.Pago.Impuestos.Traslados.Traslado createPagosPagoImpuestosTrasladosTraslado() {
        return new Pagos.Pago.Impuestos.Traslados.Traslado();
    }

    /**
     * Create an instance of {@link Pagos.Pago.Impuestos.Retenciones.Retencion }
     * 
     */
    public Pagos.Pago.Impuestos.Retenciones.Retencion createPagosPagoImpuestosRetencionesRetencion() {
        return new Pagos.Pago.Impuestos.Retenciones.Retencion();
    }
}
