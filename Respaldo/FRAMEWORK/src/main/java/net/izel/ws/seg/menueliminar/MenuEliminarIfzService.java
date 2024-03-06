package net.izel.ws.seg.menueliminar;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-03-01T17:24:02.127-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebServiceClient(name = "MenuEliminarIfzService", 
                  wsdlLocation = "http://localhost:17081/esb/seguridad/menu-eliminar?wsdl",
                  targetNamespace = "http://menuEliminar.seg.ws.izel.net/") 
public class MenuEliminarIfzService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://menuEliminar.seg.ws.izel.net/", "MenuEliminarIfzService");
    public final static QName MenuEliminarIfzPort = new QName("http://menuEliminar.seg.ws.izel.net/", "MenuEliminarIfzPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:17081/esb/seguridad/menu-eliminar?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(MenuEliminarIfzService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:17081/esb/seguridad/menu-eliminar?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public MenuEliminarIfzService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public MenuEliminarIfzService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MenuEliminarIfzService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MenuEliminarIfzService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MenuEliminarIfzService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MenuEliminarIfzService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns MenuEliminarIfz
     */
    @WebEndpoint(name = "MenuEliminarIfzPort")
    public MenuEliminarIfz getMenuEliminarIfzPort() {
        return super.getPort(MenuEliminarIfzPort, MenuEliminarIfz.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns MenuEliminarIfz
     */
    @WebEndpoint(name = "MenuEliminarIfzPort")
    public MenuEliminarIfz getMenuEliminarIfzPort(WebServiceFeature... features) {
        return super.getPort(MenuEliminarIfzPort, MenuEliminarIfz.class, features);
    }

}