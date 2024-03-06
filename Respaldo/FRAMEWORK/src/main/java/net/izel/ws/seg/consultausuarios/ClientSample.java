package net.izel.ws.seg.consultausuarios;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.izel.ws.data.HeaderWS;

public class ClientSample {

	public static void main(String[] args) throws MalformedURLException {
	        System.out.println("***********************");

	        URL url = new URL(ConsultaUsuariosIfzService.WSDL_LOCATION.toString().replace("localhost", "192.168.0.24"));
	        ConsultaUsuariosIfzService service1 = new ConsultaUsuariosIfzService(url);
	        ConsultaUsuariosIfz port1 = service1.getConsultaUsuariosIfzPort();
	        ConsultaUsuariosRequest req= new ConsultaUsuariosRequest ();
	        HeaderWS header;
	        header= new HeaderWS();
	        header.setIdEmpresa(1L);	        
	        req.setHeader(header);
	        req.setTipoBusqueda("nombreUsuario");
	        req.setValor("");
	        
	        ConsultaUsuariosResponse response = port1.procesar(req);
	        if (response.getRespuesta().getErrores().getCodigoError()==0 ) {
	        	List<ConsultaUsuariosOBJ> lista = response.getRespuesta().getBody().getParams();
	        	for ( ConsultaUsuariosOBJ user :  lista) {
	        		System.out.println("ID: " + user.getId());
	        		System.out.println("Usuario: " + user.getUsuario());
	        		System.out.println("Correo: " + user.getCorreo());
	        		System.out.println("Nombre: " + user.getNombre());
	        		System.out.println("ID procesos: " + user.getIdRs());
	        		System.out.println("ID Seguimiento: " + user.getIdSeg());
	        		System.out.println("-----------------------------");
	        	}
	        		        	
	        }
	        else {
	        	System.out.println(response.getRespuesta().getErrores().getDescError());
	        }
	        
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
