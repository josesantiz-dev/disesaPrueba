package net.izel.ws.seg.menumodificar;


public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        MenuModificarIfzService service1 = new MenuModificarIfzService();
	        System.out.println("Create Web Service...");
	        MenuModificarIfz port1 = service1.getMenuModificarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        MenuModificarIfz port2 = service1.getMenuModificarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
