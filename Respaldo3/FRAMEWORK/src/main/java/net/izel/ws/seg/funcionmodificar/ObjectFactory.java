
package net.izel.ws.seg.funcionmodificar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import net.izel.ws.data.*;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.funcionmodificar package. 
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

    private final static QName _FuncionModificarRequest_QNAME = new QName("http://funcionModificar.seg.ws.izel.net/", "FuncionModificarRequest");
    private final static QName _FuncionModificarResponse_QNAME = new QName("http://funcionModificar.seg.ws.izel.net/", "FuncionModificarResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.funcionmodificar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FuncionModificarRequest }
     * 
     */
    public FuncionModificarRequest createFuncionModificarRequest() {
        return new FuncionModificarRequest();
    }

    /**
     * Create an instance of {@link FuncionModificarResponse }
     * 
     */
    public FuncionModificarResponse createFuncionModificarResponse() {
        return new FuncionModificarResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link FuncionModificarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funcionModificar.seg.ws.izel.net/", name = "FuncionModificarRequest")
    public JAXBElement<FuncionModificarRequest> createFuncionModificarRequest(FuncionModificarRequest value) {
        return new JAXBElement<FuncionModificarRequest>(_FuncionModificarRequest_QNAME, FuncionModificarRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FuncionModificarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funcionModificar.seg.ws.izel.net/", name = "FuncionModificarResponse")
    public JAXBElement<FuncionModificarResponse> createFuncionModificarResponse(FuncionModificarResponse value) {
        return new JAXBElement<FuncionModificarResponse>(_FuncionModificarResponse_QNAME, FuncionModificarResponse.class, null, value);
    }

}
