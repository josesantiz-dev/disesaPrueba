package net.izel.ws.seg.funcionbuscar;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-02-28T23:15:11.434-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebServiceClient(name = "FuncionBuscarIfzService", 
                  wsdlLocation = "http://localhost:17081/esb/seguridad/funcion-buscar?wsdl",
                  targetNamespace = "http://funcionBuscar.seg.ws.izel.net/") 
public class FuncionBuscarIfzService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://funcionBuscar.seg.ws.izel.net/", "FuncionBuscarIfzService");
    public final static QName FuncionBuscarIfzPort = new QName("http://funcionBuscar.seg.ws.izel.net/", "FuncionBuscarIfzPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:17081/esb/seguridad/funcion-buscar?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(FuncionBuscarIfzService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:17081/esb/seguridad/funcion-buscar?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public FuncionBuscarIfzService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public FuncionBuscarIfzService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FuncionBuscarIfzService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public FuncionBuscarIfzService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public FuncionBuscarIfzService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public FuncionBuscarIfzService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns FuncionBuscarIfz
     */
    @WebEndpoint(name = "FuncionBuscarIfzPort")
    public FuncionBuscarIfz getFuncionBuscarIfzPort() {
        return super.getPort(FuncionBuscarIfzPort, FuncionBuscarIfz.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns FuncionBuscarIfz
     */
    @WebEndpoint(name = "FuncionBuscarIfzPort")
    public FuncionBuscarIfz getFuncionBuscarIfzPort(WebServiceFeature... features) {
        return super.getPort(FuncionBuscarIfzPort, FuncionBuscarIfz.class, features);
    }

}
