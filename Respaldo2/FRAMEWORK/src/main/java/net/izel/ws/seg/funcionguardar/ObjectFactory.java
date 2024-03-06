
package net.izel.ws.seg.funcionguardar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import net.izel.ws.data.*;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.funcionguardar package. 
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

    private final static QName _FuncionGuardarRequest_QNAME = new QName("http://funcionGuardar.seg.ws.izel.net/", "FuncionGuardarRequest");
    private final static QName _FuncionGuardarResponse_QNAME = new QName("http://funcionGuardar.seg.ws.izel.net/", "FuncionGuardarResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.funcionguardar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FuncionGuardarResponse }
     * 
     */
    public FuncionGuardarResponse createFuncionGuardarResponse() {
        return new FuncionGuardarResponse();
    }

    /**
     * Create an instance of {@link FuncionGuardarRequest }
     * 
     */
    public FuncionGuardarRequest createFuncionGuardarRequest() {
        return new FuncionGuardarRequest();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link FuncionGuardarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funcionGuardar.seg.ws.izel.net/", name = "FuncionGuardarRequest")
    public JAXBElement<FuncionGuardarRequest> createFuncionGuardarRequest(FuncionGuardarRequest value) {
        return new JAXBElement<FuncionGuardarRequest>(_FuncionGuardarRequest_QNAME, FuncionGuardarRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FuncionGuardarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funcionGuardar.seg.ws.izel.net/", name = "FuncionGuardarResponse")
    public JAXBElement<FuncionGuardarResponse> createFuncionGuardarResponse(FuncionGuardarResponse value) {
        return new JAXBElement<FuncionGuardarResponse>(_FuncionGuardarResponse_QNAME, FuncionGuardarResponse.class, null, value);
    }

}
