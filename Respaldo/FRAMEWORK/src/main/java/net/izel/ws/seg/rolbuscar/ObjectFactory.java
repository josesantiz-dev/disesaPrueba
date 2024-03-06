
package net.izel.ws.seg.rolbuscar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.RespuestaErrorXML;
import net.izel.ws.seg.datos.Rol;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.rolbuscar package. 
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

    private final static QName _RolBuscarRequest_QNAME = new QName("http://rolBuscar.seg.ws.izel.net/", "RolBuscarRequest");
    private final static QName _RolBuscarResponse_QNAME = new QName("http://rolBuscar.seg.ws.izel.net/", "RolBuscarResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.rolbuscar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RolBuscarResponse }
     * 
     */
    public RolBuscarResponse createRolBuscarResponse() {
        return new RolBuscarResponse();
    }

    /**
     * Create an instance of {@link RolBuscarRequest }
     * 
     */
    public RolBuscarRequest createRolBuscarRequest() {
        return new RolBuscarRequest();
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
     * Create an instance of {@link Rol }
     * 
     */
    public Rol createRol() {
        return new Rol();
    }

    /**
     * Create an instance of {@link RolBuscarBody }
     * 
     */
    public RolBuscarBody createRolBuscarBody() {
        return new RolBuscarBody();
    }

    /**
     * Create an instance of {@link RolBuscarRespuesta }
     * 
     */
    public RolBuscarRespuesta createRolBuscarRespuesta() {
        return new RolBuscarRespuesta();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RolBuscarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolBuscar.seg.ws.izel.net/", name = "RolBuscarRequest")
    public JAXBElement<RolBuscarRequest> createRolBuscarRequest(RolBuscarRequest value) {
        return new JAXBElement<RolBuscarRequest>(_RolBuscarRequest_QNAME, RolBuscarRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RolBuscarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rolBuscar.seg.ws.izel.net/", name = "RolBuscarResponse")
    public JAXBElement<RolBuscarResponse> createRolBuscarResponse(RolBuscarResponse value) {
        return new JAXBElement<RolBuscarResponse>(_RolBuscarResponse_QNAME, RolBuscarResponse.class, null, value);
    }

}
