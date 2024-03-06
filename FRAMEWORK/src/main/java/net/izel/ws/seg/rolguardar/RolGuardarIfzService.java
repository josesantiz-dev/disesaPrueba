package net.izel.ws.seg.rolguardar;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-03-01T08:50:43.195-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebServiceClient(name = "RolGuardarIfzService", 
                  wsdlLocation = "http://localhost:17081/esb/seguridad/rol-guardar?wsdl",
                  targetNamespace = "http://rolGuardar.seg.ws.izel.net/") 
public class RolGuardarIfzService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://rolGuardar.seg.ws.izel.net/", "RolGuardarIfzService");
    public final static QName RolGuardarIfzPort = new QName("http://rolGuardar.seg.ws.izel.net/", "RolGuardarIfzPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:17081/esb/seguridad/rol-guardar?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RolGuardarIfzService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:17081/esb/seguridad/rol-guardar?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RolGuardarIfzService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RolGuardarIfzService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RolGuardarIfzService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RolGuardarIfzService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RolGuardarIfzService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RolGuardarIfzService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns RolGuardarIfz
     */
    @WebEndpoint(name = "RolGuardarIfzPort")
    public RolGuardarIfz getRolGuardarIfzPort() {
        return super.getPort(RolGuardarIfzPort, RolGuardarIfz.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RolGuardarIfz
     */
    @WebEndpoint(name = "RolGuardarIfzPort")
    public RolGuardarIfz getRolGuardarIfzPort(WebServiceFeature... features) {
        return super.getPort(RolGuardarIfzPort, RolGuardarIfz.class, features);
    }

}
