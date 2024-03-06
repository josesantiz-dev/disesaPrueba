package net.izel.ws.seg.funcionguardar;


public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        FuncionGuardarIfzService service1 = new FuncionGuardarIfzService();
	        System.out.println("Create Web Service...");
	        FuncionGuardarIfz port1 = service1.getFuncionGuardarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        FuncionGuardarIfz port2 = service1.getFuncionGuardarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
