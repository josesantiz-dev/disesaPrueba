package net.izel.ws.seg.usuarioIdBuscar;

import java.net.MalformedURLException;
import java.net.URL;

import net.izel.framework.util.cat.Errores;
import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.RespuestaErrorXML;

public class clientSample {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub
		URL wsdl = new URL (UsuarioIdBuscarIfzService.WSDL_LOCATION.toString().replace("localhost", "192.168.0.139"));						        
		UsuarioIdBuscarIfzService service1 = new UsuarioIdBuscarIfzService(wsdl);
		UsuarioIdBuscarIfz port1 = service1.getUsuarioIdBuscarIfzPort();


		UsuarioIdBuscarRequest req = new UsuarioIdBuscarRequest();
		HeaderWS header = new HeaderWS();
		header.setIdEmpresa(1L);

		req.setHeader(header);
		req.setUsuarioId(1000000000061L);

		UsuarioIdBuscarResponse response = port1.procesar(req);
		UsuarioIdBuscarRespuesta respuesta = response.getRespuesta();

		Errores errores;
		errores = new Errores();
		if (respuesta.getErrores().getCodigoError() != 0) { 
			for (RespuestaErrorXML res: respuesta.getErrores().getErrores()){
				System.out.println("Codigo Error: " + res.getCodigoError());
				System.out.println("  Descripcion: " + errores.descError.get(res.getCodigoError()));
			}
		}
		else{

			for ( UsuarioIdBuscarObj res : respuesta.getBody().getParams() ) {
				System.out.println("ID : " + res.getId() );
				System.out.println("Usuario : " + res.getUsuario());
			   System.out.println("Nombre : " + res.getNombre() );
			   System.out.println("Correo : " + res.getCorreo() );
			}
		}

	}

}
