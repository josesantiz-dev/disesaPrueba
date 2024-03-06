
package net.izel.ws.seg.pantallamodificar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.*;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.pantallamodificar package. 
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

    private final static QName _PantallaModificarResponse_QNAME = new QName("http://pantallaModificar.seg.ws.izel.net/", "PantallaModificarResponse");
    private final static QName _PantallaModificarRequest_QNAME = new QName("http://pantallaModificar.seg.ws.izel.net/", "PantallaModificarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.pantallamodificar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PantallaModificarResponse }
     * 
     */
    public PantallaModificarResponse createPantallaModificarResponse() {
        return new PantallaModificarResponse();
    }

    /**
     * Create an instance of {@link PantallaModificarRequest }
     * 
     */
    public PantallaModificarRequest createPantallaModificarRequest() {
        return new PantallaModificarRequest();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link PantallaModificarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://pantallaModificar.seg.ws.izel.net/", name = "PantallaModificarResponse")
    public JAXBElement<PantallaModificarResponse> createPantallaModificarResponse(PantallaModificarResponse value) {
        return new JAXBElement<PantallaModificarResponse>(_PantallaModificarResponse_QNAME, PantallaModificarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PantallaModificarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://pantallaModificar.seg.ws.izel.net/", name = "PantallaModificarRequest")
    public JAXBElement<PantallaModificarRequest> createPantallaModificarRequest(PantallaModificarRequest value) {
        return new JAXBElement<PantallaModificarRequest>(_PantallaModificarRequest_QNAME, PantallaModificarRequest.class, null, value);
    }

}
