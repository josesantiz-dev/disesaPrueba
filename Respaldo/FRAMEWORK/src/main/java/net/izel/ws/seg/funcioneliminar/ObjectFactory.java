
package net.izel.ws.seg.funcioneliminar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import net.izel.ws.data.*;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.funcioneliminar package. 
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

    private final static QName _FuncionEliminarResponse_QNAME = new QName("http://funcionEliminar.seg.ws.izel.net/", "FuncionEliminarResponse");
    private final static QName _FuncionEliminarRequest_QNAME = new QName("http://funcionEliminar.seg.ws.izel.net/", "FuncionEliminarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.funcioneliminar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FuncionEliminarRequest }
     * 
     */
    public FuncionEliminarRequest createFuncionEliminarRequest() {
        return new FuncionEliminarRequest();
    }

    /**
     * Create an instance of {@link FuncionEliminarResponse }
     * 
     */
    public FuncionEliminarResponse createFuncionEliminarResponse() {
        return new FuncionEliminarResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link FuncionEliminarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funcionEliminar.seg.ws.izel.net/", name = "FuncionEliminarResponse")
    public JAXBElement<FuncionEliminarResponse> createFuncionEliminarResponse(FuncionEliminarResponse value) {
        return new JAXBElement<FuncionEliminarResponse>(_FuncionEliminarResponse_QNAME, FuncionEliminarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FuncionEliminarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funcionEliminar.seg.ws.izel.net/", name = "FuncionEliminarRequest")
    public JAXBElement<FuncionEliminarRequest> createFuncionEliminarRequest(FuncionEliminarRequest value) {
        return new JAXBElement<FuncionEliminarRequest>(_FuncionEliminarRequest_QNAME, FuncionEliminarRequest.class, null, value);
    }

}
