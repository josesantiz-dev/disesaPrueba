
package net.izel.ws.seg.rolmenueliminar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.*;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.rolmenueliminar package. 
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

    private final static QName _RolMenuEliminarResponse_QNAME = new QName("http://rolMenuEliminar.seg.ws.izel.net/", "RolMenuEliminarResponse");
    private final static QName _RolMenuEliminarRequest_QNAME = new QName("http://rolMenuEliminar.seg.ws.izel.net/", "RolMenuEliminarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.rolmenueliminar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RolMenuEliminarResponse }
     * 
     */
    public RolMenuEliminarResponse createRolMenuEliminarResponse() {
        return new RolMenuEliminarResponse();
    }

    /**
     * Create an instance of {@link RolMenuEliminarRequest }
     * 
     */
    public RolMenuEliminarRequest createRolMenuEliminarRequest() {
        return new RolMenuEliminarRequest();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link RolMenuEliminarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolMenuEliminar.seg.ws.izel.net/", name = "RolMenuEliminarResponse")
    public JAXBElement<RolMenuEliminarResponse> createRolMenuEliminarResponse(RolMenuEliminarResponse value) {
        return new JAXBElement<RolMenuEliminarResponse>(_RolMenuEliminarResponse_QNAME, RolMenuEliminarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RolMenuEliminarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolMenuEliminar.seg.ws.izel.net/", name = "RolMenuEliminarRequest")
    public JAXBElement<RolMenuEliminarRequest> createRolMenuEliminarRequest(RolMenuEliminarRequest value) {
        return new JAXBElement<RolMenuEliminarRequest>(_RolMenuEliminarRequest_QNAME, RolMenuEliminarRequest.class, null, value);
    }

}
