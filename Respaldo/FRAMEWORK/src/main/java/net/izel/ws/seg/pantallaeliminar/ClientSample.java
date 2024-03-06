package net.izel.ws.seg.pantallaeliminar;

//import net.izel.ws.seg.pantallaeliminar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        PantallaEliminarIfzService service1 = new PantallaEliminarIfzService();
	        System.out.println("Create Web Service...");
	        PantallaEliminarIfz port1 = service1.getPantallaEliminarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        PantallaEliminarIfz port2 = service1.getPantallaEliminarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
