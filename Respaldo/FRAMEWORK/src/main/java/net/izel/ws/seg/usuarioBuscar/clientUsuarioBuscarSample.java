package net.izel.ws.seg.usuarioBuscar;

import java.net.MalformedURLException;
import java.net.URL;

import net.izel.framework.util.so.Errores;
import net.izel.ws.data.HeaderWS;
import net.izel.ws.data.RespuestaErrorXML;

public class clientUsuarioBuscarSample {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub
		URL wsdl = new URL (UsuarioBuscarIfzService.WSDL_LOCATION.toString().replace("localhost", "192.168.1.231"));						        
		UsuarioBuscarIfzService service1 = new UsuarioBuscarIfzService(wsdl);
		UsuarioBuscarIfz port1 = service1.getUsuarioBuscarIfzPort();


		UsuarioBuscarRequest req = new UsuarioBuscarRequest();
		HeaderWS header = new HeaderWS();
		header.setIdEmpresa(1L);

		req.setHeader(header);
		req.setNombre("mmeza");

		UsuarioBuscarResponse response = port1.procesar(req);
		UsuarioBuscarRespuesta respuesta = response.getRespuesta();

		Errores errores;
		errores = new Errores();
		if (respuesta.getErrores().getCodigoError() != 0) { 
			for (RespuestaErrorXML res: respuesta.getErrores().getErrores()){
				System.out.println("Codigo Error: " + res.getCodigoError());
				System.out.println("Descripcion: " + errores.descError.get(res.getCodigoError()));
			}
		}
		else{
			for(UsuarioBuscarObj res : respuesta.getBody().getParams() ) {
				System.out.println("ID : " + res.getId() );
				System.out.println("Usuario : " + res.getUsuario() );
			   System.out.println("Nombre : " + res.getNombre() );
			   
			}
		}
	}

}
