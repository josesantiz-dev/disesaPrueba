package net.izel.ws.seg.menumodificar;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-03-01T17:23:10.123-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebService(targetNamespace = "http://menuModificar.seg.ws.izel.net/", name = "MenuModificarIfz")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface MenuModificarIfz {

    @WebResult(name = "MenuModificarResponse", targetNamespace = "http://menuModificar.seg.ws.izel.net/", partName = "parameters")
    @WebMethod
    public MenuModificarResponse guardar(
        @WebParam(partName = "parameters", name = "MenuModificarRequest", targetNamespace = "http://menuModificar.seg.ws.izel.net/")
        MenuModificarRequest parameters
    );
}
