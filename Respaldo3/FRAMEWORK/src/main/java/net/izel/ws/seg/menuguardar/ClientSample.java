package net.izel.ws.seg.menuguardar;


public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        MenuGuardarIfzService service1 = new MenuGuardarIfzService();
	        System.out.println("Create Web Service...");
	        MenuGuardarIfz port1 = service1.getMenuGuardarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        MenuGuardarIfz port2 = service1.getMenuGuardarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
