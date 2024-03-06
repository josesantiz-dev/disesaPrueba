package net.izel.ws.seg.rolbuscar;

//import net.izel.ws.seg.rolbuscar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        RolBuscarIfzService service1 = new RolBuscarIfzService();
	        System.out.println("Create Web Service...");
	        RolBuscarIfz port1 = service1.getRolBuscarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.procesar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        RolBuscarIfz port2 = service1.getRolBuscarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.procesar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
