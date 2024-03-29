package net.izel.ws.seg.listaUsuarios;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2015-01-07T13:20:59.708-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebServiceClient(name = "ListaUsuariosIfzService", 
                  wsdlLocation = "http://localhost:9081/esb/seguridad/lista-usuarios?wsdl",
                  targetNamespace = "http://listaUsuarios.seg.ws.izel.net/") 
public class ListaUsuariosIfzService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://listaUsuarios.seg.ws.izel.net/", "ListaUsuariosIfzService");
    public final static QName ListaUsuariosIfzPort = new QName("http://listaUsuarios.seg.ws.izel.net/", "ListaUsuariosIfzPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:9081/esb/seguridad/lista-usuarios?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(ListaUsuariosIfzService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:9081/esb/seguridad/lista-usuarios?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public ListaUsuariosIfzService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ListaUsuariosIfzService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ListaUsuariosIfzService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ListaUsuariosIfzService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ListaUsuariosIfzService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ListaUsuariosIfzService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns ListaUsuariosIfz
     */
    @WebEndpoint(name = "ListaUsuariosIfzPort")
    public ListaUsuariosIfz getListaUsuariosIfzPort() {
        return super.getPort(ListaUsuariosIfzPort, ListaUsuariosIfz.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ListaUsuariosIfz
     */
    @WebEndpoint(name = "ListaUsuariosIfzPort")
    public ListaUsuariosIfz getListaUsuariosIfzPort(WebServiceFeature... features) {
        return super.getPort(ListaUsuariosIfzPort, ListaUsuariosIfz.class, features);
    }

}
