
package net.izel.ws.seg.rolmodificar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.*;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.rolmodificar package. 
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

    private final static QName _RolModificarResponse_QNAME = new QName("http://rolModificar.seg.ws.izel.net/", "RolModificarResponse");
    private final static QName _RolModificarRequest_QNAME = new QName("http://rolModificar.seg.ws.izel.net/", "RolModificarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.rolmodificar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RolModificarResponse }
     * 
     */
    public RolModificarResponse createRolModificarResponse() {
        return new RolModificarResponse();
    }

    /**
     * Create an instance of {@link RolModificarRequest }
     * 
     */
    public RolModificarRequest createRolModificarRequest() {
        return new RolModificarRequest();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link RolModificarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolModificar.seg.ws.izel.net/", name = "RolModificarResponse")
    public JAXBElement<RolModificarResponse> createRolModificarResponse(RolModificarResponse value) {
        return new JAXBElement<RolModificarResponse>(_RolModificarResponse_QNAME, RolModificarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RolModificarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolModificar.seg.ws.izel.net/", name = "RolModificarRequest")
    public JAXBElement<RolModificarRequest> createRolModificarRequest(RolModificarRequest value) {
        return new JAXBElement<RolModificarRequest>(_RolModificarRequest_QNAME, RolModificarRequest.class, null, value);
    }

}
