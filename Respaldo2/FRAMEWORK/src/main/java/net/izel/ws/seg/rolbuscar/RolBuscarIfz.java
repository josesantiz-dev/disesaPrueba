package net.izel.ws.seg.rolbuscar;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-02-28T23:07:28.380-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebService(targetNamespace = "http://rolBuscar.seg.ws.izel.net/", name = "RolBuscarIfz")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface RolBuscarIfz {

    @WebResult(name = "RolBuscarResponse", targetNamespace = "http://rolBuscar.seg.ws.izel.net/", partName = "parameters")
    @WebMethod
    public RolBuscarResponse procesar(
        @WebParam(partName = "parameters", name = "RolBuscarRequest", targetNamespace = "http://rolBuscar.seg.ws.izel.net/")
        RolBuscarRequest parameters
    );
}
