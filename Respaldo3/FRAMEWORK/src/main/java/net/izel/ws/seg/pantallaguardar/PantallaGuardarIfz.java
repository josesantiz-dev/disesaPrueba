package net.izel.ws.seg.pantallaguardar;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-03-01T23:28:29.446-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebService(targetNamespace = "http://pantallaGuardar.seg.ws.izel.net/", name = "PantallaGuardarIfz")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface PantallaGuardarIfz {

    @WebResult(name = "PantallaGuardarResponse", targetNamespace = "http://pantallaGuardar.seg.ws.izel.net/", partName = "parameters")
    @WebMethod
    public PantallaGuardarResponse guardar(
        @WebParam(partName = "parameters", name = "PantallaGuardarRequest", targetNamespace = "http://pantallaGuardar.seg.ws.izel.net/")
        PantallaGuardarRequest parameters
    );
}
