package net.izel.ws.seg.pantallamodificar;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-03-01T22:48:24.838-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebService(targetNamespace = "http://pantallaModificar.seg.ws.izel.net/", name = "PantallaModificarIfz")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface PantallaModificarIfz {

    @WebResult(name = "PantallaModificarResponse", targetNamespace = "http://pantallaModificar.seg.ws.izel.net/", partName = "parameters")
    @WebMethod
    public PantallaModificarResponse modificar(
        @WebParam(partName = "parameters", name = "PantallaModificarRequest", targetNamespace = "http://pantallaModificar.seg.ws.izel.net/")
        PantallaModificarRequest parameters
    );
}
