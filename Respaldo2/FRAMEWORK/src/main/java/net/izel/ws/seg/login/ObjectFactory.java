
package net.izel.ws.seg.login;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.ParametroBody;
import net.izel.ws.data.RespuestaBodyXML;
import net.izel.ws.data.RespuestaErrorXML;
import net.izel.ws.data.RespuestaXML;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.login package. 
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

    private final static QName _AutentificacionRequest_QNAME = new QName("http://login.seg.ws.izel.net/", "AutentificacionRequest");
    private final static QName _AutentificacionResponse_QNAME = new QName("http://login.seg.ws.izel.net/", "AutentificacionResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.login
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AutentificacionRequest }
     * 
     */
    public AutentificacionRequest createAutentificacionRequest() {
        return new AutentificacionRequest();
    }

    /**
     * Create an instance of {@link AutentificacionResponse }
     * 
     */
    public AutentificacionResponse createAutentificacionResponse() {
        return new AutentificacionResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link AutentificacionRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.seg.ws.izel.net/", name = "AutentificacionRequest")
    public JAXBElement<AutentificacionRequest> createAutentificacionRequest(AutentificacionRequest value) {
        return new JAXBElement<AutentificacionRequest>(_AutentificacionRequest_QNAME, AutentificacionRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AutentificacionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.seg.ws.izel.net/", name = "AutentificacionResponse")
    public JAXBElement<AutentificacionResponse> createAutentificacionResponse(AutentificacionResponse value) {
        return new JAXBElement<AutentificacionResponse>(_AutentificacionResponse_QNAME, AutentificacionResponse.class, null, value);
    }

}
