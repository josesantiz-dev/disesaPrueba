
package net.izel.ws.seg.rolmenuguardar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import net.izel.ws.data.*;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.rolmenuguardar package. 
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

    private final static QName _RolMenuGuardarRequest_QNAME = new QName("http://rolMenuGuardar.seg.ws.izel.net/", "RolMenuGuardarRequest");
    private final static QName _RolMenuGuardarResponse_QNAME = new QName("http://rolMenuGuardar.seg.ws.izel.net/", "RolMenuGuardarResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.rolmenuguardar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RolMenuGuardarRequest }
     * 
     */
    public RolMenuGuardarRequest createRolMenuGuardarRequest() {
        return new RolMenuGuardarRequest();
    }

    /**
     * Create an instance of {@link RolMenuGuardarResponse }
     * 
     */
    public RolMenuGuardarResponse createRolMenuGuardarResponse() {
        return new RolMenuGuardarResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link RolMenuGuardarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolMenuGuardar.seg.ws.izel.net/", name = "RolMenuGuardarRequest")
    public JAXBElement<RolMenuGuardarRequest> createRolMenuGuardarRequest(RolMenuGuardarRequest value) {
        return new JAXBElement<RolMenuGuardarRequest>(_RolMenuGuardarRequest_QNAME, RolMenuGuardarRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RolMenuGuardarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolMenuGuardar.seg.ws.izel.net/", name = "RolMenuGuardarResponse")
    public JAXBElement<RolMenuGuardarResponse> createRolMenuGuardarResponse(RolMenuGuardarResponse value) {
        return new JAXBElement<RolMenuGuardarResponse>(_RolMenuGuardarResponse_QNAME, RolMenuGuardarResponse.class, null, value);
    }

}
