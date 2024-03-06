package net.izel.ws.seg.pantallamodificar;

//import net.izel.ws.seg.pantallamodificar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        PantallaModificarIfzService service1 = new PantallaModificarIfzService();
	        System.out.println("Create Web Service...");
	        PantallaModificarIfz port1 = service1.getPantallaModificarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.modificar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        PantallaModificarIfz port2 = service1.getPantallaModificarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.modificar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
