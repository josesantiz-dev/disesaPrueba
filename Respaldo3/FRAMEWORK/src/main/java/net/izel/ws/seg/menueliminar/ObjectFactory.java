
package net.izel.ws.seg.menueliminar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import net.izel.ws.data.*;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.menueliminar package. 
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

    private final static QName _MenuEliminarResponse_QNAME = new QName("http://menuEliminar.seg.ws.izel.net/", "MenuEliminarResponse");
    private final static QName _MenuEliminarRequest_QNAME = new QName("http://menuEliminar.seg.ws.izel.net/", "MenuEliminarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.menueliminar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MenuEliminarRequest }
     * 
     */
    public MenuEliminarRequest createMenuEliminarRequest() {
        return new MenuEliminarRequest();
    }

    /**
     * Create an instance of {@link MenuEliminarResponse }
     * 
     */
    public MenuEliminarResponse createMenuEliminarResponse() {
        return new MenuEliminarResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link MenuEliminarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://menuEliminar.seg.ws.izel.net/", name = "MenuEliminarResponse")
    public JAXBElement<MenuEliminarResponse> createMenuEliminarResponse(MenuEliminarResponse value) {
        return new JAXBElement<MenuEliminarResponse>(_MenuEliminarResponse_QNAME, MenuEliminarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MenuEliminarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://menuEliminar.seg.ws.izel.net/", name = "MenuEliminarRequest")
    public JAXBElement<MenuEliminarRequest> createMenuEliminarRequest(MenuEliminarRequest value) {
        return new JAXBElement<MenuEliminarRequest>(_MenuEliminarRequest_QNAME, MenuEliminarRequest.class, null, value);
    }

}
