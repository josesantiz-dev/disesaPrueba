
package net.izel.ws.seg.roleliminar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.*;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.roleliminar package. 
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

    private final static QName _RolEliminarResponse_QNAME = new QName("http://rolEliminar.seg.ws.izel.net/", "RolEliminarResponse");
    private final static QName _RolEliminarRequest_QNAME = new QName("http://rolEliminar.seg.ws.izel.net/", "RolEliminarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.roleliminar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RolEliminarResponse }
     * 
     */
    public RolEliminarResponse createRolEliminarResponse() {
        return new RolEliminarResponse();
    }

    /**
     * Create an instance of {@link RolEliminarRequest }
     * 
     */
    public RolEliminarRequest createRolEliminarRequest() {
        return new RolEliminarRequest();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link RolEliminarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolEliminar.seg.ws.izel.net/", name = "RolEliminarResponse")
    public JAXBElement<RolEliminarResponse> createRolEliminarResponse(RolEliminarResponse value) {
        return new JAXBElement<RolEliminarResponse>(_RolEliminarResponse_QNAME, RolEliminarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RolEliminarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolEliminar.seg.ws.izel.net/", name = "RolEliminarRequest")
    public JAXBElement<RolEliminarRequest> createRolEliminarRequest(RolEliminarRequest value) {
        return new JAXBElement<RolEliminarRequest>(_RolEliminarRequest_QNAME, RolEliminarRequest.class, null, value);
    }

}
