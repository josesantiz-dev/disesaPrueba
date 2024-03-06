package net.izel.ws.seg.corebuscar;

import net.izel.ws.data.HeaderWS;
//import net.izel.ws.seg.corebuscar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        CoreBuscarIfzService service1 = new CoreBuscarIfzService();
	        System.out.println("Create Web Service...");
	        CoreBuscarIfz port1 = service1.getCoreBuscarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        HeaderWS header = new HeaderWS ();
	        header.setIdEmpresa(1L);
	        CoreBuscarRequest req= new CoreBuscarRequest();
	        req.setHeader(header);
	        req.setNombre("");
	        port1.procesar(req);
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
