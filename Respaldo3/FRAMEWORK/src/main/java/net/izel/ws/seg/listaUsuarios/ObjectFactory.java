
package net.izel.ws.seg.listaUsuarios;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.RespuestaErrorXML;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.izel.ws.seg.listausuarios package. 
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

    private final static QName _ListaUsuariosRequest_QNAME = new QName("http://ListaUsuarios.seg.ws.izel.net/", "ListaUsuariosRequest");
    private final static QName _ListaUsuariosResponse_QNAME = new QName("http://ListaUsuarios.seg.ws.izel.net/", "ListaUsuariosResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.izel.ws.seg.listausuarios
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link HeaderWS }
     * 
     */
    public HeaderWS createHeaderWS() {
        return new HeaderWS();
    }

    /**
     * Create an instance of {@link ListaUsuariosBody }
     * 
     */
    public ListaUsuariosBody createListaUsuariosBody() {
        return new ListaUsuariosBody();
    }

    /**
     * Create an instance of {@link RespuestaErrorXML }
     * 
     */
    public RespuestaErrorXML createRespuestaErrorXML() {
        return new RespuestaErrorXML();
    }

    /**
     * Create an instance of {@link ListaUsuariosOBJ }
     * 
     */
    public ListaUsuariosOBJ createListaUsuariosOBJ() {
        return new ListaUsuariosOBJ();
    }

    /**
     * Create an instance of {@link ListaUsuariosRespuesta }
     * 
     */
    public ListaUsuariosRespuesta createListaUsuariosRespuesta() {
        return new ListaUsuariosRespuesta();
    }

    /**
     * Create an instance of {@link ListaUsuariosRequest }
     * 
     */
    public ListaUsuariosRequest createListaUsuariosRequest() {
        return new ListaUsuariosRequest();
    }

    /**
     * Create an instance of {@link ListaUsuariosResponse }
     * 
     */
    public ListaUsuariosResponse createListaUsuariosResponse() {
        return new ListaUsuariosResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListaUsuariosRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ListaUsuarios.seg.ws.izel.net/", name = "ListaUsuariosRequest")
    public JAXBElement<ListaUsuariosRequest> createListaUsuariosRequest(ListaUsuariosRequest value) {
        return new JAXBElement<ListaUsuariosRequest>(_ListaUsuariosRequest_QNAME, ListaUsuariosRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListaUsuariosResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ListaUsuarios.seg.ws.izel.net/", name = "ListaUsuariosResponse")
    public JAXBElement<ListaUsuariosResponse> createListaUsuariosResponse(ListaUsuariosResponse value) {
        return new JAXBElement<ListaUsuariosResponse>(_ListaUsuariosResponse_QNAME, ListaUsuariosResponse.class, null, value);
    }

}
