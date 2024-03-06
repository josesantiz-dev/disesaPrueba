package net.izel.ws.seg.login;

import net.izel.framework.util.cat.Errores;
import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.RespuestaXML;


public class ClientSample {

	public static void main(String[] args) {
	        
	        AutentificacionIfzService service1 = new AutentificacionIfzService();
	        AutentificacionIfz port1 = service1.getAutentificacionIfzPort();
	        AutentificacionRequest req = new AutentificacionRequest();
	        
	        req.setHeader(new HeaderWS());
	        req.setIntentos(4);
	        req.setUsuario("valvarado");
	        req.setPassword("avictor");
	        AutentificacionResponse resp = port1.procesar(req);
	        RespuestaXML resXml = resp.getRespuesta();
	        
	        if(resXml.getErrores().getCodigoError() == 0){
	        	System.out.println(resXml.getBody().obtenerParametro("resultado"));
	        }else{
	        	System.out.println(Errores.descError.get(resXml.getErrores().obtenerUltimoError()));
	        }
	        
	}
}
