package net.izel.ws.seg.menubuscar;

//import net.izel.ws.seg.menubuscar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        MenuBuscarIfzService service1 = new MenuBuscarIfzService();
	        System.out.println("Create Web Service...");
	        MenuBuscarIfz port1 = service1.getMenuBuscarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.procesar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        MenuBuscarIfz port2 = service1.getMenuBuscarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.procesar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
