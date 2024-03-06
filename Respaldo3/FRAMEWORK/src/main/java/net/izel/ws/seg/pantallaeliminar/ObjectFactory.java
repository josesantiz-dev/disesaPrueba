
package net.izel.ws.seg.pantallaeliminar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.*;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.pantallaeliminar package. 
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

    private final static QName _PantallaEliminarRequest_QNAME = new QName("http://pantallaEliminar.seg.ws.izel.net/", "PantallaEliminarRequest");
    private final static QName _PantallaEliminarResponse_QNAME = new QName("http://pantallaEliminar.seg.ws.izel.net/", "PantallaEliminarResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.pantallaeliminar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PantallaEliminarRequest }
     * 
     */
    public PantallaEliminarRequest createPantallaEliminarRequest() {
        return new PantallaEliminarRequest();
    }

    /**
     * Create an instance of {@link PantallaEliminarResponse }
     * 
     */
    public PantallaEliminarResponse createPantallaEliminarResponse() {
        return new PantallaEliminarResponse();
    }

    /**
     * Create an instance of {@link ParametroBody }
     * 
     */
    public ParametroBody createParametroBody() {
        return new ParametroBody();
    }

    /**
     * Create an instance of {@link HeaderWS }
     * 
     */
    public HeaderWS createHeaderWS() {
        return new HeaderWS();
    }

    /**
     * Create an instance of {@link RespuestaErrorXML }
     * 
     */
    public RespuestaErrorXML createRespuestaErrorXML() {
        return new RespuestaErrorXML();
    }

    /**
     * Create an instance of {@link RespuestaBodyXML }
     * 
     */
    public RespuestaBodyXML createRespuestaBodyXML() {
        return new RespuestaBodyXML();
    }

    /**
     * Create an instance of {@link RespuestaXML }
     * 
     */
    public RespuestaXML createRespuestaXML() {
        return new RespuestaXML();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PantallaEliminarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://pantallaEliminar.seg.ws.izel.net/", name = "PantallaEliminarRequest")
    public JAXBElement<PantallaEliminarRequest> createPantallaEliminarRequest(PantallaEliminarRequest value) {
        return new JAXBElement<PantallaEliminarRequest>(_PantallaEliminarRequest_QNAME, PantallaEliminarRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PantallaEliminarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://pantallaEliminar.seg.ws.izel.net/", name = "PantallaEliminarResponse")
    public JAXBElement<PantallaEliminarResponse> createPantallaEliminarResponse(PantallaEliminarResponse value) {
        return new JAXBElement<PantallaEliminarResponse>(_PantallaEliminarResponse_QNAME, PantallaEliminarResponse.class, null, value);
    }

}
