package net.izel.ws.seg.usuarioBuscar;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2014-01-29T18:31:38.649-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebServiceClient(name = "UsuarioBuscarIfzService", 
                  wsdlLocation = "http://localhost:17081/esb/seguridad/usuario-buscar?wsdl",
                  targetNamespace = "http://usuarioBuscar.seg.ws.izel.net/") 
public class UsuarioBuscarIfzService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://usuarioBuscar.seg.ws.izel.net/", "UsuarioBuscarIfzService");
    public final static QName UsuarioBuscarIfzPort = new QName("http://usuarioBuscar.seg.ws.izel.net/", "UsuarioBuscarIfzPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:17081/esb/seguridad/usuario-buscar?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(UsuarioBuscarIfzService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:17081/esb/seguridad/usuario-buscar?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public UsuarioBuscarIfzService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public UsuarioBuscarIfzService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public UsuarioBuscarIfzService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public UsuarioBuscarIfzService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public UsuarioBuscarIfzService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public UsuarioBuscarIfzService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns UsuarioBuscarIfz
     */
    @WebEndpoint(name = "UsuarioBuscarIfzPort")
    public UsuarioBuscarIfz getUsuarioBuscarIfzPort() {
        return super.getPort(UsuarioBuscarIfzPort, UsuarioBuscarIfz.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns UsuarioBuscarIfz
     */
    @WebEndpoint(name = "UsuarioBuscarIfzPort")
    public UsuarioBuscarIfz getUsuarioBuscarIfzPort(WebServiceFeature... features) {
        return super.getPort(UsuarioBuscarIfzPort, UsuarioBuscarIfz.class, features);
    }

}