package net.izel.ws.seg.rolmenubuscar;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-03-01T08:52:22.539-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebService(targetNamespace = "http://rolMenuBuscar.seg.ws.izel.net/", name = "RolMenuBuscarIfz")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface RolMenuBuscarIfz {

    @WebResult(name = "RolMenuBuscarResponse", targetNamespace = "http://rolMenuBuscar.seg.ws.izel.net/", partName = "parameters")
    @WebMethod
    public RolMenuBuscarResponse procesar(
        @WebParam(partName = "parameters", name = "RolMenuBuscarRequest", targetNamespace = "http://rolMenuBuscar.seg.ws.izel.net/")
        RolMenuBuscarRequest parameters
    );
}
