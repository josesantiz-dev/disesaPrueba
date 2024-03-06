
package net.izel.ws.seg.rolguardar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import net.izel.ws.data.*;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.rolguardar package. 
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

    private final static QName _RolGuardarResponse_QNAME = new QName("http://rolGuardar.seg.ws.izel.net/", "RolGuardarResponse");
    private final static QName _RolGuardarRequest_QNAME = new QName("http://rolGuardar.seg.ws.izel.net/", "RolGuardarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.rolguardar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RolGuardarResponse }
     * 
     */
    public RolGuardarResponse createRolGuardarResponse() {
        return new RolGuardarResponse();
    }

    /**
     * Create an instance of {@link RolGuardarRequest }
     * 
     */
    public RolGuardarRequest createRolGuardarRequest() {
        return new RolGuardarRequest();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link RolGuardarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolGuardar.seg.ws.izel.net/", name = "RolGuardarResponse")
    public JAXBElement<RolGuardarResponse> createRolGuardarResponse(RolGuardarResponse value) {
        return new JAXBElement<RolGuardarResponse>(_RolGuardarResponse_QNAME, RolGuardarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RolGuardarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolGuardar.seg.ws.izel.net/", name = "RolGuardarRequest")
    public JAXBElement<RolGuardarRequest> createRolGuardarRequest(RolGuardarRequest value) {
        return new JAXBElement<RolGuardarRequest>(_RolGuardarRequest_QNAME, RolGuardarRequest.class, null, value);
    }

}
