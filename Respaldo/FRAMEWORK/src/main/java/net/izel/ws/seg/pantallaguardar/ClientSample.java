package net.izel.ws.seg.pantallaguardar;

//import net.izel.ws.seg.pantallaguardar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        PantallaGuardarIfzService service1 = new PantallaGuardarIfzService();
	        System.out.println("Create Web Service...");
	        PantallaGuardarIfz port1 = service1.getPantallaGuardarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        PantallaGuardarIfz port2 = service1.getPantallaGuardarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
