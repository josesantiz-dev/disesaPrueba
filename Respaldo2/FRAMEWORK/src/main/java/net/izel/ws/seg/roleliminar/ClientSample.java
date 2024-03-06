package net.izel.ws.seg.roleliminar;

//import net.izel.ws.seg.roleliminar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        RolEliminarIfzService service1 = new RolEliminarIfzService();
	        System.out.println("Create Web Service...");
	        RolEliminarIfz port1 = service1.getRolEliminarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        RolEliminarIfz port2 = service1.getRolEliminarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
