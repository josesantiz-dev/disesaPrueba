package net.izel.ws.seg.rolmenueliminar;

//import net.izel.ws.seg.rolmenueliminar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        RolMenuEliminarIfzService service1 = new RolMenuEliminarIfzService();
	        System.out.println("Create Web Service...");
	        RolMenuEliminarIfz port1 = service1.getRolMenuEliminarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        RolMenuEliminarIfz port2 = service1.getRolMenuEliminarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
