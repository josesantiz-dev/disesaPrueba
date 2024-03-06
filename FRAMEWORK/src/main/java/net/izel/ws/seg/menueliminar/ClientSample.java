package net.izel.ws.seg.menueliminar;

//import net.izel.ws.seg.menueliminar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        MenuEliminarIfzService service1 = new MenuEliminarIfzService();
	        System.out.println("Create Web Service...");
	        MenuEliminarIfz port1 = service1.getMenuEliminarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        MenuEliminarIfz port2 = service1.getMenuEliminarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
