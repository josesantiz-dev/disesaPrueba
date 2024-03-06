package net.izel.ws.seg.funcioneliminar;

//import net.izel.ws.seg.funcioneliminar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        FuncionEliminarIfzService service1 = new FuncionEliminarIfzService();
	        System.out.println("Create Web Service...");
	        FuncionEliminarIfz port1 = service1.getFuncionEliminarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        FuncionEliminarIfz port2 = service1.getFuncionEliminarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.guardar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
