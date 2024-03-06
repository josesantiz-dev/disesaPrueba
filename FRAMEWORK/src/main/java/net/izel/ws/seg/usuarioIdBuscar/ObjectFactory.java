
package net.izel.ws.seg.usuarioIdBuscar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.RespuestaErrorXML;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.usuarioidbuscar package. 
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

    private final static QName _UsuarioIdBuscarRequest_QNAME = new QName("http://usuarioIdBuscar.seg.ws.izel.net/", "UsuarioIdBuscarRequest");
    private final static QName _UsuarioIdBuscarResponse_QNAME = new QName("http://usuarioIdBuscar.seg.ws.izel.net/", "UsuarioIdBuscarResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.usuarioidbuscar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UsuarioIdBuscarResponse }
     * 
     */
    public UsuarioIdBuscarResponse createUsuarioIdBuscarResponse() {
        return new UsuarioIdBuscarResponse();
    }

    /**
     * Create an instance of {@link UsuarioIdBuscarRequest }
     * 
     */
    public UsuarioIdBuscarRequest createUsuarioIdBuscarRequest() {
        return new UsuarioIdBuscarRequest();
    }

    /**
     * Create an instance of {@link UsuarioIdBuscarObj }
     * 
     */
    public UsuarioIdBuscarObj createUsuarioIdBuscarObj() {
        return new UsuarioIdBuscarObj();
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
     * Create an instance of {@link UsuarioIdBuscarBody }
     * 
     */
    public UsuarioIdBuscarBody createUsuarioIdBuscarBody() {
        return new UsuarioIdBuscarBody();
    }

    /**
     * Create an instance of {@link UsuarioIdBuscarRespuesta }
     * 
     */
    public UsuarioIdBuscarRespuesta createUsuarioIdBuscarRespuesta() {
        return new UsuarioIdBuscarRespuesta();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioIdBuscarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://usuarioIdBuscar.seg.ws.izel.net/", name = "UsuarioIdBuscarRequest")
    public JAXBElement<UsuarioIdBuscarRequest> createUsuarioIdBuscarRequest(UsuarioIdBuscarRequest value) {
        return new JAXBElement<UsuarioIdBuscarRequest>(_UsuarioIdBuscarRequest_QNAME, UsuarioIdBuscarRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioIdBuscarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://usuarioIdBuscar.seg.ws.izel.net/", name = "UsuarioIdBuscarResponse")
    public JAXBElement<UsuarioIdBuscarResponse> createUsuarioIdBuscarResponse(UsuarioIdBuscarResponse value) {
        return new JAXBElement<UsuarioIdBuscarResponse>(_UsuarioIdBuscarResponse_QNAME, UsuarioIdBuscarResponse.class, null, value);
    }

}
