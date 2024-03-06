package net.izel.ws.seg.menumodificar;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-03-01T17:23:10.183-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebServiceClient(name = "MenuModificarIfzService", 
                  wsdlLocation = "http://localhost:17081/esb/seguridad/menu-modificar?wsdl",
                  targetNamespace = "http://menuModificar.seg.ws.izel.net/") 
public class MenuModificarIfzService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://menuModificar.seg.ws.izel.net/", "MenuModificarIfzService");
    public final static QName MenuModificarIfzPort = new QName("http://menuModificar.seg.ws.izel.net/", "MenuModificarIfzPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:17081/esb/seguridad/menu-modificar?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(MenuModificarIfzService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:17081/esb/seguridad/menu-modificar?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public MenuModificarIfzService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public MenuModificarIfzService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MenuModificarIfzService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MenuModificarIfzService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MenuModificarIfzService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MenuModificarIfzService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns MenuModificarIfz
     */
    @WebEndpoint(name = "MenuModificarIfzPort")
    public MenuModificarIfz getMenuModificarIfzPort() {
        return super.getPort(MenuModificarIfzPort, MenuModificarIfz.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns MenuModificarIfz
     */
    @WebEndpoint(name = "MenuModificarIfzPort")
    public MenuModificarIfz getMenuModificarIfzPort(WebServiceFeature... features) {
        return super.getPort(MenuModificarIfzPort, MenuModificarIfz.class, features);
    }

}