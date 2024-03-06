
package net.izel.ws.seg.menumodificar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import net.izel.ws.data.*;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.menumodificar package. 
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

    private final static QName _MenuModificarResponse_QNAME = new QName("http://menuModificar.seg.ws.izel.net/", "MenuModificarResponse");
    private final static QName _MenuModificarRequest_QNAME = new QName("http://menuModificar.seg.ws.izel.net/", "MenuModificarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.menumodificar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MenuModificarResponse }
     * 
     */
    public MenuModificarResponse createMenuModificarResponse() {
        return new MenuModificarResponse();
    }

    /**
     * Create an instance of {@link MenuModificarRequest }
     * 
     */
    public MenuModificarRequest createMenuModificarRequest() {
        return new MenuModificarRequest();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link MenuModificarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://menuModificar.seg.ws.izel.net/", name = "MenuModificarResponse")
    public JAXBElement<MenuModificarResponse> createMenuModificarResponse(MenuModificarResponse value) {
        return new JAXBElement<MenuModificarResponse>(_MenuModificarResponse_QNAME, MenuModificarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MenuModificarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://menuModificar.seg.ws.izel.net/", name = "MenuModificarRequest")
    public JAXBElement<MenuModificarRequest> createMenuModificarRequest(MenuModificarRequest value) {
        return new JAXBElement<MenuModificarRequest>(_MenuModificarRequest_QNAME, MenuModificarRequest.class, null, value);
    }

}
