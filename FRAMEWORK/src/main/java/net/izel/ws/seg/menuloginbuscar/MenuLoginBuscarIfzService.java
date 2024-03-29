package net.izel.ws.seg.menuloginbuscar;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-02-28T17:08:54.095-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebServiceClient(name = "MenuLoginBuscarIfzService", 
                  wsdlLocation = "http://localhost:17081/esb/seguridad/menu-login-buscar?wsdl",
                  targetNamespace = "http://menuLoginBuscar.seg.ws.izel.net/") 
public class MenuLoginBuscarIfzService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://menuLoginBuscar.seg.ws.izel.net/", "MenuLoginBuscarIfzService");
    public final static QName MenuLoginBuscarIfzPort = new QName("http://menuLoginBuscar.seg.ws.izel.net/", "MenuLoginBuscarIfzPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:17081/esb/seguridad/menu-login-buscar?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(MenuLoginBuscarIfzService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:17081/esb/seguridad/menu-login-buscar?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public MenuLoginBuscarIfzService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public MenuLoginBuscarIfzService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MenuLoginBuscarIfzService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MenuLoginBuscarIfzService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MenuLoginBuscarIfzService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MenuLoginBuscarIfzService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns MenuLoginBuscarIfz
     */
    @WebEndpoint(name = "MenuLoginBuscarIfzPort")
    public MenuLoginBuscarIfz getMenuLoginBuscarIfzPort() {
        return super.getPort(MenuLoginBuscarIfzPort, MenuLoginBuscarIfz.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns MenuLoginBuscarIfz
     */
    @WebEndpoint(name = "MenuLoginBuscarIfzPort")
    public MenuLoginBuscarIfz getMenuLoginBuscarIfzPort(WebServiceFeature... features) {
        return super.getPort(MenuLoginBuscarIfzPort, MenuLoginBuscarIfz.class, features);
    }

}
