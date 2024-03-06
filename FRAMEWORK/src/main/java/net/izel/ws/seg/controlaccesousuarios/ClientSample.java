package net.izel.ws.seg.controlaccesousuarios;

import net.izel.ws.data.HeaderWS;


public class ClientSample {

	public static void main(String[] args) {
		HeaderWS hdr;
		
		System.out.println("***********************");
		ControlAccesoUsuariosIfzService service1 = new ControlAccesoUsuariosIfzService();
		ControlAccesoUsuariosIfz port1 = service1.getControlAccesoUsuariosIfzPort();
		ControlAccesoUsuariosRequest req= new ControlAccesoUsuariosRequest();	        
		req.setUsuarioId(1000000000001L);

		ControlAccesoUsuariosResponse response = port1.procesar(req);
		if(response.getRespuesta().getErrores().getCodigoError() == 0){

			hdr= response.getRespuesta().getBody().getParams().get(0);
			
			System.out.println("Empresa : " + hdr.getIdEmpresa());
			System.out.println("Sucursal : " + hdr.getIdSucursal());
			System.out.println("Punto atencion : " + hdr.getIdPuntoAtencion());
			System.out.println("Canal atencion : " + hdr.getIdCanalAtencion());
			System.out.println("Clase canal atencion : " + hdr.getIdClaseCanalAtencion());
					
		}


		System.out.println("Call Over!");
	}
}
