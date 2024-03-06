package net.izel.ws.seg.roleliminar;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-03-01T08:51:21.031-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebServiceClient(name = "RolEliminarIfzService", 
                  wsdlLocation = "http://localhost:17081/esb/seguridad/rol-eliminar?wsdl",
                  targetNamespace = "http://rolEliminar.seg.ws.izel.net/") 
public class RolEliminarIfzService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://rolEliminar.seg.ws.izel.net/", "RolEliminarIfzService");
    public final static QName RolEliminarIfzPort = new QName("http://rolEliminar.seg.ws.izel.net/", "RolEliminarIfzPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:17081/esb/seguridad/rol-eliminar?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RolEliminarIfzService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:17081/esb/seguridad/rol-eliminar?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RolEliminarIfzService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RolEliminarIfzService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RolEliminarIfzService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RolEliminarIfzService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RolEliminarIfzService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RolEliminarIfzService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns RolEliminarIfz
     */
    @WebEndpoint(name = "RolEliminarIfzPort")
    public RolEliminarIfz getRolEliminarIfzPort() {
        return super.getPort(RolEliminarIfzPort, RolEliminarIfz.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RolEliminarIfz
     */
    @WebEndpoint(name = "RolEliminarIfzPort")
    public RolEliminarIfz getRolEliminarIfzPort(WebServiceFeature... features) {
        return super.getPort(RolEliminarIfzPort, RolEliminarIfz.class, features);
    }

}
