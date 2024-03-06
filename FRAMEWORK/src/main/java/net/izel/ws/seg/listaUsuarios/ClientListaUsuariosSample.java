package net.izel.ws.seg.listaUsuarios;


import java.net.MalformedURLException;
import java.net.URL;

import net.izel.ws.data.HeaderWS;


public class ClientListaUsuariosSample {

	public static void main(String[] args) throws MalformedURLException {


		System.out.println("***********************");
			URL url = new URL(ListaUsuariosIfzService.WSDL_LOCATION.toString().replace("localhost", "192.168.11.200"));

			ListaUsuariosIfzService service1 = new ListaUsuariosIfzService(url);
			ListaUsuariosIfz port1 = service1.getListaUsuariosIfzPort();
			ListaUsuariosRequest req = new ListaUsuariosRequest();
			HeaderWS header = new HeaderWS();
			header.setIdEmpresa(1L);
			req.setHeader( header );

			ListaUsuariosResponse response = port1.procesar(req);
			ListaUsuariosRespuesta respuesta = response.getRespuesta();

			if(response.getRespuesta().getErrores().getCodigoError() == 0){
				if(respuesta.getBody().getParams() != null){
					for(ListaUsuariosOBJ obj : respuesta.getBody().getParams()){
						System.out.println("usuarioId: " + obj.getUsuarioId());
						System.out.println("  usuario: " + obj.getUsuario());
						
						System.out.println("------------------------------------------------------------------");
					}
				}
			}else{
				System.out.println("Codigo Error: " + response.getRespuesta().getErrores().obtenerUltimoError());
			}
		
		System.out.println("***********************");
	}
}

