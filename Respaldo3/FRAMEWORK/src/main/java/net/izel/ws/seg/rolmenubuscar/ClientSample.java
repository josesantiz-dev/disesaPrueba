package net.izel.ws.seg.rolmenubuscar;

//import net.izel.ws.seg.rolmenubuscar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        RolMenuBuscarIfzService service1 = new RolMenuBuscarIfzService();
	        System.out.println("Create Web Service...");
	        RolMenuBuscarIfz port1 = service1.getRolMenuBuscarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.procesar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        RolMenuBuscarIfz port2 = service1.getRolMenuBuscarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.procesar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
