
package net.izel.ws.seg.menuloginbuscar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.RespuestaErrorXML;
import net.izel.ws.seg.datos.Core;
import net.izel.ws.seg.datos.Funcion;
import net.izel.ws.seg.datos.Menu;
import net.izel.ws.seg.datos.Pantalla;
import net.izel.ws.seg.datos.Rol;
import net.izel.ws.seg.datos.RolMenu;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.menuloginbuscar package. 
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

    private final static QName _MenuLoginBuscarRequest_QNAME = new QName("http://menuLoginBuscar.seg.ws.izel.net/", "MenuLoginBuscarRequest");
    private final static QName _MenuLoginBuscarResponse_QNAME = new QName("http://menuLoginBuscar.seg.ws.izel.net/", "MenuLoginBuscarResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.menuloginbuscar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MenuLoginBuscarResponse }
     * 
     */
    public MenuLoginBuscarResponse createMenuLoginBuscarResponse() {
        return new MenuLoginBuscarResponse();
    }

    /**
     * Create an instance of {@link MenuLoginBuscarRequest }
     * 
     */
    public MenuLoginBuscarRequest createMenuLoginBuscarRequest() {
        return new MenuLoginBuscarRequest();
    }

    /**
     * Create an instance of {@link HeaderWS }
     * 
     */
    public HeaderWS createHeaderWS() {
        return new HeaderWS();
    }

    /**
     * Create an instance of {@link MenuLoginBuscarBody }
     * 
     */
    public MenuLoginBuscarBody createMenuLoginBuscarBody() {
        return new MenuLoginBuscarBody();
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
     * Create an instance of {@link RolMenu }
     * 
     */
    public RolMenu createRolMenu() {
        return new RolMenu();
    }

    /**
     * Create an instance of {@link Funcion }
     * 
     */
    public Funcion createFuncion() {
        return new Funcion();
    }

    /**
     * Create an instance of {@link MenuLoginBuscarRespuesta }
     * 
     */
    public MenuLoginBuscarRespuesta createMenuLoginBuscarRespuesta() {
        return new MenuLoginBuscarRespuesta();
    }

    /**
     * Create an instance of {@link Rol }
     * 
     */
    public Rol createRol() {
        return new Rol();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link MenuLoginBuscarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://menuLoginBuscar.seg.ws.izel.net/", name = "MenuLoginBuscarRequest")
    public JAXBElement<MenuLoginBuscarRequest> createMenuLoginBuscarRequest(MenuLoginBuscarRequest value) {
        return new JAXBElement<MenuLoginBuscarRequest>(_MenuLoginBuscarRequest_QNAME, MenuLoginBuscarRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MenuLoginBuscarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://menuLoginBuscar.seg.ws.izel.net/", name = "MenuLoginBuscarResponse")
    public JAXBElement<MenuLoginBuscarResponse> createMenuLoginBuscarResponse(MenuLoginBuscarResponse value) {
        return new JAXBElement<MenuLoginBuscarResponse>(_MenuLoginBuscarResponse_QNAME, MenuLoginBuscarResponse.class, null, value);
    }

}
