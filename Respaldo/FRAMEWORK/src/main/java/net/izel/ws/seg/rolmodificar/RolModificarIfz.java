package net.izel.ws.seg.rolmodificar;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-03-01T08:59:27.521-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebService(targetNamespace = "http://rolModificar.seg.ws.izel.net/", name = "RolModificarIfz")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface RolModificarIfz {

    @WebResult(name = "RolModificarResponse", targetNamespace = "http://rolModificar.seg.ws.izel.net/", partName = "parameters")
    @WebMethod
    public RolModificarResponse guardar(
        @WebParam(partName = "parameters", name = "RolModificarRequest", targetNamespace = "http://rolModificar.seg.ws.izel.net/")
        RolModificarRequest parameters
    );
}
