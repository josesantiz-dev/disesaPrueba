
package net.izel.ws.seg.rolmenubuscar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.*;
import net.izel.ws.seg.datos.*;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.rolmenubuscar package. 
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

    private final static QName _RolMenuBuscarResponse_QNAME = new QName("http://rolMenuBuscar.seg.ws.izel.net/", "RolMenuBuscarResponse");
    private final static QName _RolMenuBuscarRequest_QNAME = new QName("http://rolMenuBuscar.seg.ws.izel.net/", "RolMenuBuscarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.rolmenubuscar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RolMenuBuscarRequest }
     * 
     */
    public RolMenuBuscarRequest createRolMenuBuscarRequest() {
        return new RolMenuBuscarRequest();
    }

    /**
     * Create an instance of {@link RolMenuBuscarResponse }
     * 
     */
    public RolMenuBuscarResponse createRolMenuBuscarResponse() {
        return new RolMenuBuscarResponse();
    }

    /**
     * Create an instance of {@link RolMenuBuscarBody }
     * 
     */
    public RolMenuBuscarBody createRolMenuBuscarBody() {
        return new RolMenuBuscarBody();
    }

    /**
     * Create an instance of {@link RolMenuBuscarRespuesta }
     * 
     */
    public RolMenuBuscarRespuesta createRolMenuBuscarRespuesta() {
        return new RolMenuBuscarRespuesta();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link RolMenuBuscarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolMenuBuscar.seg.ws.izel.net/", name = "RolMenuBuscarResponse")
    public JAXBElement<RolMenuBuscarResponse> createRolMenuBuscarResponse(RolMenuBuscarResponse value) {
        return new JAXBElement<RolMenuBuscarResponse>(_RolMenuBuscarResponse_QNAME, RolMenuBuscarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RolMenuBuscarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolMenuBuscar.seg.ws.izel.net/", name = "RolMenuBuscarRequest")
    public JAXBElement<RolMenuBuscarRequest> createRolMenuBuscarRequest(RolMenuBuscarRequest value) {
        return new JAXBElement<RolMenuBuscarRequest>(_RolMenuBuscarRequest_QNAME, RolMenuBuscarRequest.class, null, value);
    }

}
