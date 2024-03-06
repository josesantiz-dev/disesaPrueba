
package net.izel.ws.seg.usuarioBuscar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.RespuestaErrorXML;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.usuariobuscar package. 
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

    private final static QName _UsuarioBuscarResponse_QNAME = new QName("http://usuarioBuscar.seg.ws.izel.net/", "UsuarioBuscarResponse");
    private final static QName _UsuarioBuscarRequest_QNAME = new QName("http://usuarioBuscar.seg.ws.izel.net/", "UsuarioBuscarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.usuariobuscar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UsuarioBuscarRequest }
     * 
     */
    public UsuarioBuscarRequest createUsuarioBuscarRequest() {
        return new UsuarioBuscarRequest();
    }

    /**
     * Create an instance of {@link UsuarioBuscarResponse }
     * 
     */
    public UsuarioBuscarResponse createUsuarioBuscarResponse() {
        return new UsuarioBuscarResponse();
    }

    /**
     * Create an instance of {@link UsuarioBuscarRespuesta }
     * 
     */
    public UsuarioBuscarRespuesta createUsuarioBuscarRespuesta() {
        return new UsuarioBuscarRespuesta();
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
     * Create an instance of {@link UsuarioBuscarObj }
     * 
     */
    public UsuarioBuscarObj createUsuarioBuscarObj() {
        return new UsuarioBuscarObj();
    }

    /**
     * Create an instance of {@link UsuarioBuscarBody }
     * 
     */
    public UsuarioBuscarBody createUsuarioBuscarBody() {
        return new UsuarioBuscarBody();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioBuscarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://usuarioBuscar.seg.ws.izel.net/", name = "UsuarioBuscarResponse")
    public JAXBElement<UsuarioBuscarResponse> createUsuarioBuscarResponse(UsuarioBuscarResponse value) {
        return new JAXBElement<UsuarioBuscarResponse>(_UsuarioBuscarResponse_QNAME, UsuarioBuscarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioBuscarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://usuarioBuscar.seg.ws.izel.net/", name = "UsuarioBuscarRequest")
    public JAXBElement<UsuarioBuscarRequest> createUsuarioBuscarRequest(UsuarioBuscarRequest value) {
        return new JAXBElement<UsuarioBuscarRequest>(_UsuarioBuscarRequest_QNAME, UsuarioBuscarRequest.class, null, value);
    }

}
