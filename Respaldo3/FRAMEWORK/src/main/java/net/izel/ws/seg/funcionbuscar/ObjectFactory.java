
package net.izel.ws.seg.funcionbuscar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.RespuestaErrorXML;
import net.izel.ws.seg.datos.Core;
import net.izel.ws.seg.datos.Funcion;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.funcionbuscar package. 
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

    private final static QName _FuncionBuscarResponse_QNAME = new QName("http://funcionBuscar.seg.ws.izel.net/", "FuncionBuscarResponse");
    private final static QName _FuncionBuscarRequest_QNAME = new QName("http://funcionBuscar.seg.ws.izel.net/", "FuncionBuscarRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.funcionbuscar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FuncionBuscarRequest }
     * 
     */
    public FuncionBuscarRequest createFuncionBuscarRequest() {
        return new FuncionBuscarRequest();
    }

    /**
     * Create an instance of {@link FuncionBuscarResponse }
     * 
     */
    public FuncionBuscarResponse createFuncionBuscarResponse() {
        return new FuncionBuscarResponse();
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
     * Create an instance of {@link Funcion }
     * 
     */
    public Funcion createFuncion() {
        return new Funcion();
    }

    /**
     * Create an instance of {@link Core }
     * 
     */
    public Core createCore() {
        return new Core();
    }

    /**
     * Create an instance of {@link FuncionBuscarBody }
     * 
     */
    public FuncionBuscarBody createFuncionBuscarBody() {
        return new FuncionBuscarBody();
    }

    /**
     * Create an instance of {@link FuncionBuscarRespuesta }
     * 
     */
    public FuncionBuscarRespuesta createFuncionBuscarRespuesta() {
        return new FuncionBuscarRespuesta();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FuncionBuscarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funcionBuscar.seg.ws.izel.net/", name = "FuncionBuscarResponse")
    public JAXBElement<FuncionBuscarResponse> createFuncionBuscarResponse(FuncionBuscarResponse value) {
        return new JAXBElement<FuncionBuscarResponse>(_FuncionBuscarResponse_QNAME, FuncionBuscarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FuncionBuscarRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funcionBuscar.seg.ws.izel.net/", name = "FuncionBuscarRequest")
    public JAXBElement<FuncionBuscarRequest> createFuncionBuscarRequest(FuncionBuscarRequest value) {
        return new JAXBElement<FuncionBuscarRequest>(_FuncionBuscarRequest_QNAME, FuncionBuscarRequest.class, null, value);
    }

}
