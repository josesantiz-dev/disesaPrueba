package net.izel.ws.seg.corebuscar;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-03-02T10:42:56.424-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebService(targetNamespace = "http://coreBuscar.seg.ws.izel.net/", name = "CoreBuscarIfz")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface CoreBuscarIfz {

    @WebResult(name = "CoreBuscarResponse", targetNamespace = "http://coreBuscar.seg.ws.izel.net/", partName = "parameters")
    @WebMethod
    public CoreBuscarResponse procesar(
        @WebParam(partName = "parameters", name = "CoreBuscarRequest", targetNamespace = "http://coreBuscar.seg.ws.izel.net/")
        CoreBuscarRequest parameters
    );
}