package net.izel.ws.seg.rolguardar;

//import net.izel.ws.seg.rolguardar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        RolGuardarIfzService service1 = new RolGuardarIfzService();
	        System.out.println("Create Web Service...");
	        RolGuardarIfz port1 = service1.getRolGuardarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        RolGuardarIfz port2 = service1.getRolGuardarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
