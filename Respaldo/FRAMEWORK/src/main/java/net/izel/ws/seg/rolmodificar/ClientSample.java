package net.izel.ws.seg.rolmodificar;

//import net.izel.ws.seg.rolmodificar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        RolModificarIfzService service1 = new RolModificarIfzService();
	        System.out.println("Create Web Service...");
	        RolModificarIfz port1 = service1.getRolModificarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        RolModificarIfz port2 = service1.getRolModificarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
