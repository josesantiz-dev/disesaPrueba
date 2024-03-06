
package net.izel.ws.seg.controlaccesousuarios;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.RespuestaErrorXML;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.controlaccesousuarios package. 
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

    private final static QName _ControlAccesoUsuariosResponse_QNAME = new QName("http://controlAccesoUsuarios.seg.ws.izel.net/", "ControlAccesoUsuariosResponse");
    private final static QName _ControlAccesoUsuariosRequest_QNAME = new QName("http://controlAccesoUsuarios.seg.ws.izel.net/", "ControlAccesoUsuariosRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.controlaccesousuarios
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ControlAccesoUsuariosRequest }
     * 
     */
    public ControlAccesoUsuariosRequest createControlAccesoUsuariosRequest() {
        return new ControlAccesoUsuariosRequest();
    }

    /**
     * Create an instance of {@link ControlAccesoUsuariosResponse }
     * 
     */
    public ControlAccesoUsuariosResponse createControlAccesoUsuariosResponse() {
        return new ControlAccesoUsuariosResponse();
    }

    /**
     * Create an instance of {@link HeaderWS }
     * 
     */
    public HeaderWS createHeaderWS() {
        return new HeaderWS();
    }

    /**
     * Create an instance of {@link ControlAccesoUsuariosRespuesta }
     * 
     */
    public ControlAccesoUsuariosRespuesta createControlAccesoUsuariosRespuesta() {
        return new ControlAccesoUsuariosRespuesta();
    }

    /**
     * Create an instance of {@link RespuestaErrorXML }
     * 
     */
    public RespuestaErrorXML createRespuestaErrorXML() {
        return new RespuestaErrorXML();
    }

    /**
     * Create an instance of {@link ControlAccesoUsuariosBody }
     * 
     */
    public ControlAccesoUsuariosBody createControlAccesoUsuariosBody() {
        return new ControlAccesoUsuariosBody();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ControlAccesoUsuariosResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://controlAccesoUsuarios.seg.ws.izel.net/", name = "ControlAccesoUsuariosResponse")
    public JAXBElement<ControlAccesoUsuariosResponse> createControlAccesoUsuariosResponse(ControlAccesoUsuariosResponse value) {
        return new JAXBElement<ControlAccesoUsuariosResponse>(_ControlAccesoUsuariosResponse_QNAME, ControlAccesoUsuariosResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ControlAccesoUsuariosRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://controlAccesoUsuarios.seg.ws.izel.net/", name = "ControlAccesoUsuariosRequest")
    public JAXBElement<ControlAccesoUsuariosRequest> createControlAccesoUsuariosRequest(ControlAccesoUsuariosRequest value) {
        return new JAXBElement<ControlAccesoUsuariosRequest>(_ControlAccesoUsuariosRequest_QNAME, ControlAccesoUsuariosRequest.class, null, value);
    }

}
