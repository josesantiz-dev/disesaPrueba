package net.izel.ws.seg.menuguardar;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-03-01T17:22:34.011-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebService(targetNamespace = "http://menuGuardar.seg.ws.izel.net/", name = "MenuGuardarIfz")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface MenuGuardarIfz {

    @WebResult(name = "MenuGuardarResponse", targetNamespace = "http://menuGuardar.seg.ws.izel.net/", partName = "parameters")
    @WebMethod
    public MenuGuardarResponse guardar(
        @WebParam(partName = "parameters", name = "MenuGuardarRequest", targetNamespace = "http://menuGuardar.seg.ws.izel.net/")
        MenuGuardarRequest parameters
    );
}
