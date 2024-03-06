
package net.izel.ws.seg.menubuscar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.*;
import net.izel.ws.seg.datos.*;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.menubuscar package. 
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

    private final static QName _MenuBuscarRequest_QNAME = new QName("http://menuBuscar.seg.ws.izel.net/", "MenuBuscarRequest");
    private final static QName _MenuBuscarResponse_QNAME = new QName("http://menuBuscar.seg.ws.izel.net/", "MenuBuscarResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.menubuscar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MenuBuscarResponse }
     * 
     */
    public MenuBuscarResponse createMenuBuscarResponse() {
        return new MenuBuscarResponse();
    }

    /**
     * Create an instance of {@link MenuBuscarRequest }
     * 
     */
    public MenuBuscarRequest createMenuBuscarRequest() {
        return new MenuBuscarRequest();
    }

    /**
     * Create an instance of {@link MenuBuscarRespuesta }
     * 
     */
    public MenuBuscarRespuesta createMenuBuscarRespuesta() {
        return new MenuBuscarRespuesta();
    }

    /**
     * Create an instance of {@link HeaderWS }
     * 
     */
    public HeaderWS createHeaderWS() {
        return new HeaderWS();
    }

    /**
     * Create an instance of {@link MenuBuscarBody }
     * 
     */
    public MenuBuscarBody createMenuBuscarBody() {
        return new MenuBuscarBody();
    }

    /**
     * Create an instance of {@link RespuestaErrorXML }
     * 
     */
    public RespuestaErrorXML createRespuestaErrorXML() {
        return new RespuestaErrorXML();
    }

    /**
     * Create an instance of {@link Menu }
     * 
     */
    public Menu createMenu() {
        return new Menu();
    }

    /**
     * Create an instance of {@link Funcion }
     * 
     */
    public Funcion createFuncion() {
        return new Funcion();
    }

    /**
     * Create an instance of {@link Core }
     * 
     */
    public Core createCore() {
        return new Core();
    }

    /**
     * Create an instance of {@link Pantalla }
     * 
     */
    public Pantalla createPantalla() {
        return new Pantalla();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MenuBuscarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://menuBuscar.seg.ws.izel.net/", name = "MenuBuscarRequest")
    public JAXBElement<MenuBuscarRequest> createMenuBuscarRequest(MenuBuscarRequest value) {
        return new JAXBElement<MenuBuscarRequest>(_MenuBuscarRequest_QNAME, MenuBuscarRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MenuBuscarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://menuBuscar.seg.ws.izel.net/", name = "MenuBuscarResponse")
    public JAXBElement<MenuBuscarResponse> createMenuBuscarResponse(MenuBuscarResponse value) {
        return new JAXBElement<MenuBuscarResponse>(_MenuBuscarResponse_QNAME, MenuBuscarResponse.class, null, value);
    }

}
