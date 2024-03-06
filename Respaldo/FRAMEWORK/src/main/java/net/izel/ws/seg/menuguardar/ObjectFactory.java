
package net.izel.ws.seg.menuguardar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import net.izel.ws.data.*;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.menuguardar package. 
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

    private final static QName _MenuGuardarResponse_QNAME = new QName("http://menuGuardar.seg.ws.izel.net/", "MenuGuardarResponse");
    private final static QName _MenuGuardarRequest_QNAME = new QName("http://menuGuardar.seg.ws.izel.net/", "MenuGuardarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.menuguardar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MenuGuardarRequest }
     * 
     */
    public MenuGuardarRequest createMenuGuardarRequest() {
        return new MenuGuardarRequest();
    }

    /**
     * Create an instance of {@link MenuGuardarResponse }
     * 
     */
    public MenuGuardarResponse createMenuGuardarResponse() {
        return new MenuGuardarResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link MenuGuardarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://menuGuardar.seg.ws.izel.net/", name = "MenuGuardarResponse")
    public JAXBElement<MenuGuardarResponse> createMenuGuardarResponse(MenuGuardarResponse value) {
        return new JAXBElement<MenuGuardarResponse>(_MenuGuardarResponse_QNAME, MenuGuardarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MenuGuardarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://menuGuardar.seg.ws.izel.net/", name = "MenuGuardarRequest")
    public JAXBElement<MenuGuardarRequest> createMenuGuardarRequest(MenuGuardarRequest value) {
        return new JAXBElement<MenuGuardarRequest>(_MenuGuardarRequest_QNAME, MenuGuardarRequest.class, null, value);
    }

}
