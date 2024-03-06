
package net.izel.ws.seg.consultausuarios;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.RespuestaErrorXML;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.consultausuarios package. 
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

    private final static QName _ConsultaUsuariosRequest_QNAME = new QName("http://consultaUsuarios.seg.ws.izel.net/", "ConsultaUsuariosRequest");
    private final static QName _ConsultaUsuariosResponse_QNAME = new QName("http://consultaUsuarios.seg.ws.izel.net/", "ConsultaUsuariosResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.consultausuarios
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConsultaUsuariosRequest }
     * 
     */
    public ConsultaUsuariosRequest createConsultaUsuariosRequest() {
        return new ConsultaUsuariosRequest();
    }

    /**
     * Create an instance of {@link ConsultaUsuariosResponse }
     * 
     */
    public ConsultaUsuariosResponse createConsultaUsuariosResponse() {
        return new ConsultaUsuariosResponse();
    }

    /**
     * Create an instance of {@link HeaderWS }
     * 
     */
    public HeaderWS createHeaderWS() {
        return new HeaderWS();
    }

    /**
     * Create an instance of {@link ConsultaUsuariosOBJ }
     * 
     */
    public ConsultaUsuariosOBJ createConsultaUsuariosOBJ() {
        return new ConsultaUsuariosOBJ();
    }

    /**
     * Create an instance of {@link ConsultaUsuariosRespuesta }
     * 
     */
    public ConsultaUsuariosRespuesta createConsultaUsuariosRespuesta() {
        return new ConsultaUsuariosRespuesta();
    }

    /**
     * Create an instance of {@link RespuestaErrorXML }
     * 
     */
    public RespuestaErrorXML createRespuestaErrorXML() {
        return new RespuestaErrorXML();
    }

    /**
     * Create an instance of {@link ConsultaUsuarioBody }
     * 
     */
    public ConsultaUsuarioBody createConsultaUsuarioBody() {
        return new ConsultaUsuarioBody();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultaUsuariosRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://consultaUsuarios.seg.ws.izel.net/", name = "ConsultaUsuariosRequest")
    public JAXBElement<ConsultaUsuariosRequest> createConsultaUsuariosRequest(ConsultaUsuariosRequest value) {
        return new JAXBElement<ConsultaUsuariosRequest>(_ConsultaUsuariosRequest_QNAME, ConsultaUsuariosRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultaUsuariosResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://consultaUsuarios.seg.ws.izel.net/", name = "ConsultaUsuariosResponse")
    public JAXBElement<ConsultaUsuariosResponse> createConsultaUsuariosResponse(ConsultaUsuariosResponse value) {
        return new JAXBElement<ConsultaUsuariosResponse>(_ConsultaUsuariosResponse_QNAME, ConsultaUsuariosResponse.class, null, value);
    }

}
