package net.izel.ws.seg.login;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-02-09T14:19:58.199-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebService(targetNamespace = "http://login.seg.ws.izel.net/", name = "AutentificacionIfz")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface AutentificacionIfz {

    @WebResult(name = "AutentificacionResponse", targetNamespace = "http://login.seg.ws.izel.net/", partName = "parameters")
    @WebMethod
    public AutentificacionResponse procesar(
        @WebParam(partName = "parameters", name = "AutentificacionRequest", targetNamespace = "http://login.seg.ws.izel.net/")
        AutentificacionRequest parameters
    );
}
