package net.izel.ws.seg.rolmenuguardar;

//import net.izel.ws.seg.rolmenuguardar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        RolMenuGuardarIfzService service1 = new RolMenuGuardarIfzService();
	        System.out.println("Create Web Service...");
	        RolMenuGuardarIfz port1 = service1.getRolMenuGuardarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        RolMenuGuardarIfz port2 = service1.getRolMenuGuardarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
