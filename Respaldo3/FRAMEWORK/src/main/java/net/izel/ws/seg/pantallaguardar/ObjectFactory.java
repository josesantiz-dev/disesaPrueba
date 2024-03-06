
package net.izel.ws.seg.pantallaguardar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.*;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.pantallaguardar package. 
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

    private final static QName _PantallaGuardarRequest_QNAME = new QName("http://pantallaGuardar.seg.ws.izel.net/", "PantallaGuardarRequest");
    private final static QName _PantallaGuardarResponse_QNAME = new QName("http://pantallaGuardar.seg.ws.izel.net/", "PantallaGuardarResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.pantallaguardar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PantallaGuardarRequest }
     * 
     */
    public PantallaGuardarRequest createPantallaGuardarRequest() {
        return new PantallaGuardarRequest();
    }

    /**
     * Create an instance of {@link PantallaGuardarResponse }
     * 
     */
    public PantallaGuardarResponse createPantallaGuardarResponse() {
        return new PantallaGuardarResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link PantallaGuardarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://pantallaGuardar.seg.ws.izel.net/", name = "PantallaGuardarRequest")
    public JAXBElement<PantallaGuardarRequest> createPantallaGuardarRequest(PantallaGuardarRequest value) {
        return new JAXBElement<PantallaGuardarRequest>(_PantallaGuardarRequest_QNAME, PantallaGuardarRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PantallaGuardarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://pantallaGuardar.seg.ws.izel.net/", name = "PantallaGuardarResponse")
    public JAXBElement<PantallaGuardarResponse> createPantallaGuardarResponse(PantallaGuardarResponse value) {
        return new JAXBElement<PantallaGuardarResponse>(_PantallaGuardarResponse_QNAME, PantallaGuardarResponse.class, null, value);
    }

}
