
package net.izel.ws.seg.corebuscar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import net.izel.ws.data.*;
import net.izel.ws.seg.datos.Core;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.corebuscar package. 
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

    private final static QName _CoreBuscarRequest_QNAME = new QName("http://coreBuscar.seg.ws.izel.net/", "CoreBuscarRequest");
    private final static QName _CoreBuscarResponse_QNAME = new QName("http://coreBuscar.seg.ws.izel.net/", "CoreBuscarResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.corebuscar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CoreBuscarRequest }
     * 
     */
    public CoreBuscarRequest createCoreBuscarRequest() {
        return new CoreBuscarRequest();
    }

    /**
     * Create an instance of {@link CoreBuscarResponse }
     * 
     */
    public CoreBuscarResponse createCoreBuscarResponse() {
        return new CoreBuscarResponse();
    }

    /**
     * Create an instance of {@link CoreBuscarBody }
     * 
     */
    public CoreBuscarBody createCoreBuscarBody() {
        return new CoreBuscarBody();
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
     * Create an instance of {@link CoreBuscarRespuesta }
     * 
     */
    public CoreBuscarRespuesta createCoreBuscarRespuesta() {
        return new CoreBuscarRespuesta();
    }

    /**
     * Create an instance of {@link Core }
     * 
     */
    public Core createCore() {
        return new Core();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CoreBuscarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://coreBuscar.seg.ws.izel.net/", name = "CoreBuscarRequest")
    public JAXBElement<CoreBuscarRequest> createCoreBuscarRequest(CoreBuscarRequest value) {
        return new JAXBElement<CoreBuscarRequest>(_CoreBuscarRequest_QNAME, CoreBuscarRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CoreBuscarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://coreBuscar.seg.ws.izel.net/", name = "CoreBuscarResponse")
    public JAXBElement<CoreBuscarResponse> createCoreBuscarResponse(CoreBuscarResponse value) {
        return new JAXBElement<CoreBuscarResponse>(_CoreBuscarResponse_QNAME, CoreBuscarResponse.class, null, value);
    }

}
