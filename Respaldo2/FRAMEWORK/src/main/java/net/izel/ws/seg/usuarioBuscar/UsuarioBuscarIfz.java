package net.izel.ws.seg.usuarioBuscar;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2014-01-29T18:31:38.515-07:00
 * Generated source version: 2.4.6
 * 
 */
@WebService(targetNamespace = "http://usuarioBuscar.seg.ws.izel.net/", name = "UsuarioBuscarIfz")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface UsuarioBuscarIfz {

    @WebResult(name = "UsuarioBuscarResponse", targetNamespace = "http://usuarioBuscar.seg.ws.izel.net/", partName = "parameters")
    @WebMethod
    public UsuarioBuscarResponse procesar(
        @WebParam(partName = "parameters", name = "UsuarioBuscarRequest", targetNamespace = "http://usuarioBuscar.seg.ws.izel.net/")
        UsuarioBuscarRequest parameters
    );
}