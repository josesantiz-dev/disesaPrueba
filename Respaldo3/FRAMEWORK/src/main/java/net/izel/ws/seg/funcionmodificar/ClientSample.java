package net.izel.ws.seg.funcionmodificar;


public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        FuncionModificarIfzService service1 = new FuncionModificarIfzService();
	        System.out.println("Create Web Service...");
	        FuncionModificarIfz port1 = service1.getFuncionModificarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.modificar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        FuncionModificarIfz port2 = service1.getFuncionModificarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.modificar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
